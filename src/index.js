import { NativeModules } from 'react-native';

// Expose the module from the native code
const { ZipStreamModule } = NativeModules;

/**
 * Lists the contents of a ZIP file.
 * @param {string} zipFilePath - Path to the ZIP file.
 * @returns {Promise<Array<string>>} - Promise resolving to an array of file names in the ZIP.
 */
export const listZipContents = (zipFilePath) => {
  return ZipStreamModule.listZipContents(zipFilePath)
    .then((fileNames) => {
      console.log('Files in ZIP:', fileNames);
      return fileNames;
    })
    .catch((error) => {
      console.error('Error listing ZIP contents:', error);
      throw error;
    });
};

/**
 * Streams a specific file from the ZIP.
 * @param {string} zipFilePath - Path to the ZIP file.
 * @param {string} entryName - Name of the file to stream.
 * @param {string} type - Specifies the format of the output data : base64, arraybuffer, string
 * @returns {Promise<string>} - Promise resolving when streaming is completed.
 */
export const streamFileFromZip = async (zipFilePath, entryName, type = 'base64') => {
  try {
    const base64Data = await ZipStreamModule.streamFileFromZip(
      zipFilePath,
      entryName,
      type
    );
    return base64Data;
  } catch (error) {
    console.error('😷Error streaming file from ZIP:', error);
    throw error;
  }
};

