package com.zipstream

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.io.*
import java.nio.charset.StandardCharsets
import android.util.Base64
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException

class ZipStreamModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun listZipContents(zipFilePath: String, promise: Promise) {
    try {
      ZipFile(zipFilePath).use { zipFile ->
        val entries = zipFile.entries()
        val fileNames = ArrayList<String>()

        while (entries.hasMoreElements()) {
          val entry = entries.nextElement()
          fileNames.add(entry.name)
        }

        promise.resolve(fileNames)
      }
    } catch (e: Exception) {
      promise.reject("ERROR_LISTING_CONTENTS", "Failed to list contents of the ZIP file", e)
    }
  }

  @ReactMethod
  fun streamFileFromZip(zipFilePath: String, entryName: String, type: String, password: String?, promise: Promise) {
      try {
          val zipFile = ZipFile(zipFilePath)

          if (zipFile.isEncrypted && password != null) {
              zipFile.setPassword(password.toCharArray())
          }

          val entry = zipFile.getFileHeader(entryName)

          if (entry == null) {
              promise.reject("ERROR_STREAMING_FILE", "File not found in ZIP")
              return
          }

          val inputStream = zipFile.getInputStream(entry)
          val byteArrayOutputStream = ByteArrayOutputStream()
          val buffer = ByteArray(1024)
          var length: Int

          while (inputStream.read(buffer).also { length = it } != -1) {
              byteArrayOutputStream.write(buffer, 0, length)
          }

          val fileContents = byteArrayOutputStream.toByteArray()

          when (type) {
              "base64" -> {
                  val base64Data = Base64.encodeToString(fileContents, Base64.NO_WRAP)
                  promise.resolve(base64Data)
              }
              "arraybuffer" -> promise.resolve(fileContents)
              "string" -> {
                  val stringData = String(fileContents, StandardCharsets.UTF_8)
                  promise.resolve(stringData)
              }
              else -> promise.reject("ERROR_STREAMING_FILE", "Invalid type specified")
          }

          zipFile.close()
      } catch (e: Exception) {
          promise.reject("ERROR_STREAMING_FILE", "Failed to stream the file", e)
      }
  }


  @ReactMethod
  fun unzipFile(zipFilePath: String, destinationPath: String, password: String?, promise: Promise) {
      try {
          val zipFile = ZipFile(zipFilePath)

          if (zipFile.isEncrypted && password != null) {
              zipFile.setPassword(password.toCharArray())
          }

          zipFile.extractAll(destinationPath)
          promise.resolve(true)
      } catch (e: ZipException) {
          promise.reject("ERROR_UNZIPPING_FILE", "Failed to unzip the file", e)
      } catch (e: Exception) {
          promise.reject("ERROR_UNZIPPING_FILE", "An error occurred", e)
      }
  }

  @ReactMethod
  fun createZipFile(destinationPath: String, sourcePath: String, promise: Promise) {
    try {
      FileOutputStream(destinationPath).use { fos ->
        ZipOutputStream(fos).use { zos ->
          val sourceFile = File(sourcePath)
          zipDirectory(sourceFile, sourceFile.name, zos)
        }
      }
      promise.resolve(true)
    } catch (e: Exception) {
      promise.reject("ERROR_CREATING_ZIP", "Failed to create ZIP file", e)
    }
  }

  @Throws(IOException::class)
  private fun zipDirectory(fileToZip: File, fileName: String, zos: ZipOutputStream) {
    if (fileToZip.isHidden) {
      return
    }
    if (fileToZip.isDirectory) {
      val zipEntryName = if (fileName.endsWith("/")) fileName else "$fileName/"
      zos.putNextEntry(ZipEntry(zipEntryName))
      zos.closeEntry()

      fileToZip.listFiles()?.forEach { childFile ->
        zipDirectory(childFile, "$fileName/${childFile.name}", zos)
      }
    } else {
      FileInputStream(fileToZip).use { fis ->
        val zipEntry = ZipEntry(fileName)
        zos.putNextEntry(zipEntry)
        val buffer = ByteArray(4096)
        var length: Int
        while (fis.read(buffer).also { length = it } >= 0) {
          zos.write(buffer, 0, length)
        }
      }
    }
  }

  companion object {
    const val NAME = "ZipStream"
  }
}
