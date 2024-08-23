# react-native-zip-stream

A React Native module for streaming files from ZIP archives.

## Installation

1. Add the dependency to your project.

   ```bash
   npm install react-native-zip-stream
   ```


2. For iOS, install the required CocoaPods dependencies:

   ```bash
   cd ios
   pod install
   ```

## Usage

### List Zip Contents

Lists the contents of a ZIP file.

```typescript
import { listZipContents } from 'react-native-zip-stream';

const zipFilePath = '/path/to/your/zipfile.zip';

const exampleListZipContents = async () => {
  try {
    const fileNames = await listZipContents(zipFilePath);
    console.log('Files in ZIP:', fileNames);
  } catch (error) {
    console.error('Error listing ZIP contents:', error);
  }
};
```

### Stream File from Zip

Streams a specific file from a ZIP archive. The data can be returned in three formats: `base64`, `arraybuffer`, or `string`.

#### Parameters

- `zipFilePath`: Path to the ZIP file.
- `entryName`: Name of the file inside the ZIP to extract.
- `type`: Specifies the format of the output data. Can be one of:
  - `"base64"` (default): Returns the file content as a Base64-encoded string.
  - `"arraybuffer"`: Returns the file content as an ArrayBuffer (represented as an array of `UInt8` in Swift).
  - `"string"`: Returns the file content as a UTF-8 string.

#### Example

```typescript
import { streamFileFromZip } from 'react-native-zip-stream';

const zipFilePath = '/path/to/your/zipfile.zip';
const entryName = 'fileInsideZip.txt';

const example = async () => {
  try {
    // Example usage with 'base64' type
    const base64Data = await streamFileFromZip(zipFilePath, entryName, 'base64');
    console.log('Base64 Data:', base64Data);

    // Example usage with 'arraybuffer' type
    const arrayBufferData = await streamFileFromZip(zipFilePath, entryName, 'arraybuffer');
    console.log('ArrayBuffer Data:', new Uint8Array(arrayBufferData));

    // Example usage with 'string' type
    const stringData = await streamFileFromZip(zipFilePath, entryName, 'string');
    console.log('String Data:', stringData);
    
  } catch (error) {
    console.error('Error:', error);
  }
};
```