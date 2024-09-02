import React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import {
  listZipContents,
  // streamFileFromZip,
  // unzipFile,
} from 'react-native-zip-stream';
import RNFS from 'react-native-fs';

export default function App() {
  const test = async () => {
    const data = await listZipContents(
      `${RNFS.DocumentDirectoryPath}/esmod-1.epub`
    );

    console.log(data);

    // const file = await streamFileFromZip(
    //   `${RNFS.DocumentDirectoryPath}/esmod-1.epub`,
    //   data[0]
    // );

    // console.log(`${RNFS.DocumentDirectoryPath}/test`);

    // console.log(
    //   await unzipFile(
    //     `${RNFS.DocumentDirectoryPath}/esmod-1.epub`,
    //     `${RNFS.DocumentDirectoryPath}/test`
    //   )
    // );
  };
  test();
  return (
    <View style={styles.container}>
      <Text>TEst</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
