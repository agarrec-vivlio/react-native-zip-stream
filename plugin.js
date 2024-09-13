const { withAndroidManifest, withInfoPlist } = require('@expo/config-plugins');

module.exports = function withZipStream(config) {
  // Modify AndroidManifest.xml for Android
  config = withAndroidManifest(config, (androidConfig) => {
    const androidManifest = androidConfig.modResults.manifest;

    if (!androidManifest['uses-permission']) {
      androidManifest['uses-permission'] = [];
    }

    // Request permission to read and write files in local storage (including the app's document directory)
    androidManifest['uses-permission'].push({
      $: { 'android:name': 'android.permission.READ_EXTERNAL_STORAGE' },
    });
    androidManifest['uses-permission'].push({
      $: { 'android:name': 'android.permission.WRITE_EXTERNAL_STORAGE' },
    });

    return androidConfig;
  });

  // Modify Info.plist for iOS
  config = withInfoPlist(config, (iosConfig) => {
    // Request access to the documents directory in iOS
    iosConfig.modResults.NSDocumentsFolderUsageDescription =
      'This app requires access to the documents folder to read and write ZIP files.';

    return iosConfig;
  });

  // Return the modified config
  return config;
};
