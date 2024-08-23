# react-native-zip-stream

This module allows you to list and stream files from a ZIP archive in a React Native application.

## Installation

```bash
yarn add react-native-zip-stream
```

## Usage

### Importing the Module

```javascript
import { listZipContents, streamFileFromZip } from 'react-native-zip-stream';


```

### List Files in ZIP

```javascript
/**
 * Lists all files in the ZIP archive.
 * @param {string} zipFilePath - Path to the ZIP file.
 * @returns {Promise<string[]>} - A promise that resolves with the list of file names.
 */
const listZipFiles = async (zipFilePath) => {
  try {
    const fileNames = await listZipContents(zipFilePath);
    console.log('Files in ZIP:', fileNames);
    return fileNames;
  } catch (error) {
    console.error('Error listing ZIP contents:', error);
    throw error;
  }
};
```

### Stream a Specific File from ZIP


```javascript
const exampleUsage = async () => {
  try {
    const files = await listZipFiles('/path/to/your/zipfile.zip');
    console.log('Files:', files);
    
    await streamFileFromZip('/path/to/your/zipfile.zip', 'fileInsideZip.txt');
    console.log('Streaming completed');
  } catch (error) {
    console.error('Error:', error);
  }
};

exampleUsage();
```

