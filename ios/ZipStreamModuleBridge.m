
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(ZipStreamModule, NSObject)

RCT_EXTERN_METHOD(listZipContents:(NSString *)zipFilePath resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(
  streamFileFromZip:(NSString *)zipFilePath
  entryName:(NSString *)entryName
  type:(NSString *)type
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  unzipFile:(NSString *)zipFilePath
  destinationPath:(NSString *)destinationPath
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  createZipFile:(NSString *)destinationPath
  sourcePath:(NSString *)sourcePath
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  unzipFileWithProgress:(NSString *)zipFilePath
  destinationPath:(NSString *)destinationPath
  resolver:(RCTPromiseResolveBlock)resolve
  rejecter:(RCTPromiseRejectBlock)reject
)

@end
