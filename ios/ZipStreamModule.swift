import Foundation
import React
import ZipArchive

@objc(ZipStreamModule)
class ZipStreamModule: NSObject {

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }

    @objc
    func listZipContents(_ zipFilePath: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            let zipFile = ZipArchive()
            if !zipFile.unzipOpenFile(zipFilePath) {
                throw NSError(domain: "com.zipstream", code: 1, userInfo: [NSLocalizedDescriptionKey: "Failed to open ZIP file"])
            }
            let fileNames = zipFile.getZipFileContents()
            resolve(fileNames)
            zipFile.unzipCloseFile()
        } catch let error {
            reject("ERROR_LISTING_CONTENTS", "Failed to list contents of the ZIP file", error)
        }
    }

    @objc
       func streamFileFromZip(_ zipFilePath: String, entryName: String, type: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
           do {
               let zipFile = ZipArchive()
               if !zipFile.unzipOpenFile(zipFilePath) {
                   throw NSError(domain: "com.zipstream", code: 1, userInfo: [NSLocalizedDescriptionKey: "Failed to open ZIP file"])
               }
               
               guard let fileData = zipFile.unzipFileToMemory() else {
                   throw NSError(domain: "com.zipstream", code: 404, userInfo: [NSLocalizedDescriptionKey: "File not found in ZIP"])
               }
               
               guard let fileContents = fileData[entryName] as? Data else {
                   throw NSError(domain: "com.zipstream", code: 404, userInfo: [NSLocalizedDescriptionKey: "File not found in ZIP"])
               }
                
               if type == "base64" {
                  let base64Data = fileContents.base64EncodedString()
                   resolve(base64Data)
               } else if type == "arraybuffer" {
                   let arrayBuffer = [UInt8](fileContents)
                   resolve(arrayBuffer)
               }
                  
            else if type == "string" {
                  if let stringData = String(data: fileContents, encoding: .utf8) {
                      resolve(stringData)
                     
                  } else {
                      throw NSError(domain: "com.zipstream", code: 400, userInfo: [NSLocalizedDescriptionKey: "Failed to convert data to string"])
                  }
              } else {
                  throw NSError(domain: "com.zipstream", code: 400, userInfo: [NSLocalizedDescriptionKey: "Invalid type specified"])
              }
               
               zipFile.unzipCloseFile()
           } catch let error {
               reject("ERROR_STREAMING_FILE", "Failed to stream the file", error)
           }
       }
}
