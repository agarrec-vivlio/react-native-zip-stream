import Foundation
import React
import ZipArchive

@objc(ZipStreamModule)
class ZipStreamModule: NSObject {

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }

    // Liste tous les fichiers dans le ZIP
    @objc
    func listZipContents(_ zipFilePath: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            let zipFile = try ZipArchive(url: URL(fileURLWithPath: zipFilePath))
            let fileNames = zipFile.fileNames
            resolve(fileNames)
        } catch let error {
            reject("ERROR_LISTING_CONTENTS", "Failed to list contents of the ZIP file", error)
        }
    }

    // Stream un fichier spécifique du ZIP
    @objc
    func streamFileFromZip(_ zipFilePath: String, entryName: String, progressCallback: @escaping RCTResponseSenderBlock, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            let zipFile = try ZipArchive(url: URL(fileURLWithPath: zipFilePath))
            guard let fileStream = zipFile.extractFileStream(entryName) else {
                throw NSError(domain: "com.zipstream", code: 404, userInfo: [NSLocalizedDescriptionKey: "File not found in ZIP"])
            }
            
            let buffer = UnsafeMutablePointer<UInt8>.allocate(capacity: 1024)
            defer { buffer.deallocate() }
            
            while true {
                let bytesRead = fileStream.read(buffer, maxLength: 1024)
                if bytesRead == 0 {
                    break
                }
                let data = Data(bytes: buffer, count: bytesRead)
                let base64Data = data.base64EncodedString()
                sendEvent("onZipData", body: base64Data)
            }
            
            fileStream.close()
            resolve("Streaming completed")
        } catch let error {
            reject("ERROR_STREAMING_FILE", "Failed to stream the file", error)
        }
    }

    private func sendEvent(_ eventName: String, body: Any) {
        RCTEventEmitter.sharedInstance.sendEvent(withName: eventName, body: body)
    }
}
