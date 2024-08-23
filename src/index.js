import { NativeModules, NativeEventEmitter } from 'react-native';

const { ZipStreamModule } = NativeModules;
const zipStreamEventEmitter = new NativeEventEmitter(ZipStreamModule);

// Liste des fichiers contenus dans le ZIP
export const listZipFiles = async (zipFilePath) => {
  try {
    const files = await ZipStreamModule.listZipContents(zipFilePath);
    console.log('Fichiers dans le ZIP:', files);
    return files;
  } catch (error) {
    console.error('Erreur lors de la liste des fichiers:', error);
  }
};

// Streamer un fichier spécifique du ZIP
export const streamZipFile = (zipFilePath, entryName) => {
  return new Promise((resolve, reject) => {
    zipStreamEventEmitter.addListener('onZipData', (data) => {
      // Traiter le flux de données (base64)
      console.log('Données reçues:', data);
    });

    ZipStreamModule.streamFileFromZip(zipFilePath, entryName, (progress) => {
      console.log('Progress:', progress);
    })
    .then(resolve)
    .catch(reject);
  });
};
