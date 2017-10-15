import React from "react";
import Expo from "expo";
import { Provider } from "react-redux";
import { Text, View } from "react-native";

import configureStore from "./utils/store";
import styles from "./style";

const Main = () => (
  <Provider store={configureStore()}>
    <View style={styles.container}>
      <Text>react native core</Text>
    </View>
  </Provider>
);

Expo.registerRootComponent(Main);
