package com.reactnativezipstream;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

public class ZipStreamModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext reactContext;

    public ZipStreamModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ZipStreamModule";
    }

    @ReactMethod
    public void listZipContents(String zipFilePath, Promise promise) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ArrayList<String> fileNames = new ArrayList<>();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                fileNames.add(entry.getName());
            }

            promise.resolve(fileNames);
            zipFile.close();
        } catch (Exception e) {
            promise.reject("ERROR_LISTING_CONTENTS", "Failed to list contents of the ZIP file", e);
        }
    }

    @ReactMethod
    public void streamFileFromZip(String zipFilePath, String entryName, String type, Promise promise) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            ZipEntry entry = zipFile.getEntry(entryName);

            if (entry == null) {
                promise.reject("ERROR_STREAMING_FILE", "File not found in ZIP");
                return;
            }

            InputStream inputStream = zipFile.getInputStream(entry);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            byte[] fileContents = byteArrayOutputStream.toByteArray();

            if ("base64".equals(type)) {
                String base64Data = Base64.getEncoder().encodeToString(fileContents);
                promise.resolve(base64Data);
            } else if ("arraybuffer".equals(type)) {
                promise.resolve(fileContents);
            } else if ("string".equals(type)) {
                String stringData = new String(fileContents, StandardCharsets.UTF_8);
                promise.resolve(stringData);
            } else {
                promise.reject("ERROR_STREAMING_FILE", "Invalid type specified");
            }

            zipFile.close();
        } catch (Exception e) {
            promise.reject("ERROR_STREAMING_FILE", "Failed to stream the file", e);
        }
    }

    @ReactMethod
    public void unzipFile(String zipFilePath, String destinationPath, Promise promise) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File outFile = new File(destinationPath, entry.getName());

                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    InputStream inputStream = zipFile.getInputStream(entry);
                    FileOutputStream outputStream = new FileOutputStream(outFile);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                    inputStream.close();
                }
            }

            zipFile.close();
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("ERROR_UNZIPPING_FILE", "Failed to unzip the file", e);
        }
    }

    @ReactMethod
    public void createZipFile(String destinationPath, String sourcePath, Promise promise) {
        try {
            FileOutputStream fos = new FileOutputStream(destinationPath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            File sourceFile = new File(sourcePath);
            zipDirectory(sourceFile, sourceFile.getName(), zos);

            zos.close();
            fos.close();
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("ERROR_CREATING_ZIP", "Failed to create ZIP file", e);
        }
    }

    private void zipDirectory(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(fileName + "/"));
                zos.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipDirectory(childFile, fileName + "/" + childFile.getName(), zos);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fis.close();
    }

    @ReactMethod
    public void unzipFileWithProgress(String zipFilePath, String destinationPath, Promise promise) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            int totalEntries = zipFile.size();
            int currentEntry = 0;

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File outFile = new File(destinationPath, entry.getName());

                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    InputStream inputStream = zipFile.getInputStream(entry);
                    FileOutputStream outputStream = new FileOutputStream(outFile);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }

                    outputStream.close();
                    inputStream.close();
                }

                currentEntry++;
                float progress = (float) currentEntry / totalEntries * 100;
                sendProgressUpdate(progress);
            }

            zipFile.close();
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("ERROR_UNZIPPING_FILE", "Failed to unzip the file", e);
        }
    }

    private void sendProgressUpdate(float progress) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onProgressUpdate", progress);
    }
}
