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
    func streamFileFromZip(_ zipFilePath: String, entryName: String, progressCallback: @escaping RCTResponseSenderBlock, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            let zipFile = ZipArchive()
            if !zipFile.unzipOpenFile(zipFilePath) {
                throw NSError(domain: "com.zipstream", code: 1, userInfo: [NSLocalizedDescriptionKey: "Failed to open ZIP file"])
            }
            guard let fileData = zipFile.unzipFileToMemory() else {
                throw NSError(domain: "com.zipstream", code: 404, userInfo: [NSLocalizedDescriptionKey: "File not found in ZIP"])
            }
            let fileContents = fileData[entryName] as? Data ?? Data()
            let base64Data = fileContents.base64EncodedString()
            progressCallback([base64Data])
            resolve("Streaming completed")
            zipFile.unzipCloseFile()
        } catch let error {
            reject("ERROR_STREAMING_FILE", "Failed to stream the file", error)
        }
    }
}
