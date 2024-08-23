package com.zipstream;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipStreamModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

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
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry entry;
            List<String> fileList = new ArrayList<>();

            while ((entry = zis.getNextEntry()) != null) {
                fileList.add(entry.getName());
            }

            zis.close();
            promise.resolve(fileList);
        } catch (IOException e) {
            promise.reject("ERROR_LISTING_CONTENTS", "Failed to list contents of the ZIP file", e);
        }
    }

    @ReactMethod
    public void streamFileFromZip(String zipFilePath, String entryName, Callback progressCallback, Promise promise) {
        try {
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(entryName)) {
                    byte[] buffer = new byte[1024];
                    int len;

                    while ((len = zis.read(buffer)) > 0) {
                        String data = android.util.Base64.encodeToString(buffer, 0, len, android.util.Base64.DEFAULT);
                        sendEvent("onZipData", data);
                    }
                    zis.closeEntry();
                    break;
                }
            }
            zis.close();
            promise.resolve("Streaming completed");
        } catch (IOException e) {
            promise.reject("ERROR_STREAMING_FILE", "Failed to stream the file", e);
        }
    }

    private void sendEvent(String eventName, String data) {
        this.reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, data);
    }
}
