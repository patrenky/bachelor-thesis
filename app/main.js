import React from "react";
import Expo from "expo";
import { Provider } from "react-redux";

import configureStore from "./utils/store";

import TmpScreen from './screens/TmpScreen';

const Main = () => (
  <Provider store={configureStore()}>
    <TmpScreen />
  </Provider>
);

Expo.registerRootComponent(Main);
