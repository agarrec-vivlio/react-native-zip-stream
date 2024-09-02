#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ZipStream, NSObject)

// Exporting the listZipContents method
RCT_EXTERN_METHOD(listZipContents:(NSString *)zipFilePath
                 resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject)

// Exporting the streamFileFromZip method
RCT_EXTERN_METHOD(streamFileFromZip:(NSString *)zipFilePath
                 entryName:(NSString *)entryName
                 type:(NSString *)type
                 resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject)

// Exporting the unzipFile method
RCT_EXTERN_METHOD(unzipFile:(NSString *)zipFilePath
                 destinationPath:(NSString *)destinationPath
                 resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject)

// Exporting the createZipFile method
RCT_EXTERN_METHOD(createZipFile:(NSString *)destinationPath
                 sourcePath:(NSString *)sourcePath
                 resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject)

// Indicates whether this module needs to be initialized on the main thread
+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
