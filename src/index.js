
import { NativeModules, NativeEventEmitter } from 'react-native';

const { ZipStreamModule } = NativeModules;
const zipStreamEmitter = new NativeEventEmitter(ZipStreamModule);

/**
 * Validates the input parameters.
 * @param {string} param - The parameter to validate.
 * @param {string} paramName - The name of the parameter for error messages.
 * @throws {Error} Throws an error if the parameter is not a non-empty string.
 */
const validateStringParam = (param, paramName) => {
  if (typeof param !== 'string' || param.trim() === '') {
    throw new Error(`${paramName} should be a non-empty string.`);
  }
};

/**
 * Lists the contents of a ZIP file.
 *
 * @param {string} zipFilePath - The full path to the ZIP file.
 * @returns {Promise<string[]>} - A promise that resolves to an array of file names inside the ZIP file.
 *
 * @example
 * const zipFilePath = '/path/to/your/zipfile.zip';
 * const exampleListZipContents = async () => {
 *   try {
 *     const fileNames = await listZipContents(zipFilePath);
 *     console.log('Files in ZIP:', fileNames);
 *   } catch (error) {
 *     console.error('Error listing ZIP contents:', error);
 *   }
 * };
 */
export const listZipContents = async (zipFilePath) => {
  validateStringParam(zipFilePath, 'zipFilePath');

  try {
    return await ZipStreamModule.listZipContents(zipFilePath);
  } catch (error) {
    console.error('Error listing ZIP contents:', error);
    throw error;
  }
};

/**
 * Streams a specific file from the ZIP archive.
 *
 * @param {string} zipFilePath - The full path to the ZIP file.
 * @param {string} entryName - The name of the file within the ZIP archive to extract.
 * @param {string} [type='base64'] - The format in which to return the file data. Can be 'base64', 'arraybuffer', or 'string'.
 * @returns {Promise<string|ArrayBuffer|Uint8Array>} - A promise that resolves to the file content in the specified format.
 *
 * @example
 * const zipFilePath = '/path/to/your/zipfile.zip';
 * const entryName = 'fileInsideZip.txt';
 * const exampleStreamFile = async () => {
 *   try {
 *     const base64Data = await streamFileFromZip(zipFilePath, entryName, 'base64');
 *     console.log('Base64 Data:', base64Data);
 *
 *     const arrayBufferData = await streamFileFromZip(zipFilePath, entryName, 'arraybuffer');
 *     console.log('ArrayBuffer Data:', new Uint8Array(arrayBufferData));
 *
 *     const stringData = await streamFileFromZip(zipFilePath, entryName, 'string');
 *     console.log('String Data:', stringData);
 *   } catch (error) {
 *     console.error('Error streaming file:', error);
 *   }
 * };
 */
export const streamFileFromZip = async (zipFilePath, entryName, type = 'base64') => {
  validateStringParam(zipFilePath, 'zipFilePath');
  validateStringParam(entryName, 'entryName');

  try {
    return await ZipStreamModule.streamFileFromZip(zipFilePath, entryName, type);
  } catch (error) {
    console.error('Error streaming file from ZIP:', error);
    throw error;
  }
};

/**
 * Unzips a ZIP file to a specified destination directory.
 *
 * @param {string} zipFilePath - The full path to the ZIP file.
 * @param {string} destinationPath - The path where the contents of the ZIP file should be extracted.
 * @returns {Promise<boolean>} - A promise that resolves to true if the operation is successful.
 *
 * @example
 * const zipFilePath = '/path/to/your/zipfile.zip';
 * const destinationPath = '/path/to/extract/';
 * const exampleUnzipFile = async () => {
 *   try {
 *     const success = await unzipFile(zipFilePath, destinationPath);
 *     console.log('Unzip successful:', success);
 *   } catch (error) {
 *     console.error('Error unzipping file:', error);
 *   }
 * };
 */
export const unzipFile = async (zipFilePath, destinationPath) => {
  validateStringParam(zipFilePath, 'zipFilePath');
  validateStringParam(destinationPath, 'destinationPath');

  try {
    return await ZipStreamModule.unzipFile(zipFilePath, destinationPath);
  } catch (error) {
    console.error('Error unzipping file:', error);
    throw error;
  }
};

/**
 * Creates a ZIP file from a specified source directory.
 *
 * @param {string} destinationPath - The full path where the ZIP file should be created.
 * @param {string} sourcePath - The path to the directory or file that should be zipped.
 * @returns {Promise<boolean>} - A promise that resolves to true if the ZIP file is created successfully.
 *
 * @example
 * const sourcePath = '/path/to/source/folder';
 * const destinationPath = '/path/to/output.zip';
 * const exampleCreateZipFile = async () => {
 *   try {
 *     const success = await createZipFile(destinationPath, sourcePath);
 *     console.log('Zip creation successful:', success);
 *   } catch (error) {
 *     console.error('Error creating ZIP file:', error);
 *   }
 * };
 */
export const createZipFile = async (destinationPath, sourcePath) => {
  validateStringParam(destinationPath, 'destinationPath');
  validateStringParam(sourcePath, 'sourcePath');

  try {
    return await ZipStreamModule.createZipFile(destinationPath, sourcePath);
  } catch (error) {
    console.error('Error creating ZIP file:', error);
    throw error;
  }
};

/**
 * Unzips a ZIP file with progress updates to a specified destination directory.
 *
 * @param {string} zipFilePath - The full path to the ZIP file.
 * @param {string} destinationPath - The path where the contents of the ZIP file should be extracted.
 * @param {function} progressCallback - A callback function that receives progress updates.
 * @returns {Promise<boolean>} - A promise that resolves to true if the operation is successful.
 *
 * @example
 * const zipFilePath = '/path/to/your/zipfile.zip';
 * const destinationPath = '/path/to/extract/';
 * const exampleUnzipWithProgress = async () => {
 *   try {
 *     await unzipFileWithProgress(zipFilePath, destinationPath, (progress) => {
 *       console.log('Progress:', progress);
 *     });
 *     console.log('Unzip with progress complete');
 *   } catch (error) {
 *     console.error('Error during unzip with progress:', error);
 *   }
 * };
 */
export const unzipFileWithProgress = async (zipFilePath, destinationPath, progressCallback) => {
  validateStringParam(zipFilePath, 'zipFilePath');
  validateStringParam(destinationPath, 'destinationPath');

  const subscription = zipStreamEmitter.addListener('onProgressUpdate', progressCallback);

  try {
    return await ZipStreamModule.unzipFileWithProgress(zipFilePath, destinationPath);
  } catch (error) {
    console.error('Error unzipping file with progress:', error);
    throw error;
  } finally {
    subscription.remove();
  }
};
