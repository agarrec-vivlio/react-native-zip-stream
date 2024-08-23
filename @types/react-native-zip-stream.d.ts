// @types/react-native-zip-stream.d.ts
declare module 'react-native-zip-stream' {
export type StreamType = 'base64' | 'arraybuffer' | 'string';

export function streamFileFromZip(
    zipFilePath: string,
    entryName: string,
    type: StreamType
): Promise<string | Uint8Array>;

export function listZipContents(
    zipFilePath: string
): Promise<string[]>;
}