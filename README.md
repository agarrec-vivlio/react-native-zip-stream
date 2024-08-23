# react-native-zip-stream

Ce module permet de lister et de streamer des fichiers à partir d'une archive ZIP dans une application React Native.

## Installation

```
yarn add react-native-zip-stream
```

## Autolinking

- **iOS:** Utilisez CocoaPods pour ajouter `ZipArchive`.
- **Android:** Le module sera automatiquement lié si vous utilisez React Native 0.60+.

## Usage

```javascript
import { listZipFiles, streamZipFile } from 'react-native-zip-stream';

// Liste des fichiers dans le ZIP
const files = await listZipFiles('/path/to/your/zipfile.zip');

// Streamer un fichier spécifique du ZIP
streamZipFile('/path/to/your/zipfile.zip', 'fileInsideZip.txt')
  .then(() => console.log('Streaming complet'))
  .catch(error => console.error('Erreur lors du streaming:', error));
```

## License

[MIT License](LICENSE)
