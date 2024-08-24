package com.reactnativezipstream;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Base64;
import java.util.Enumeration;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ZipStreamModule extends ReactContextBaseJavaModule {

    public ZipStreamModule(ReactApplicationContext reactContext) {
        super(reactContext);
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
}