import React from "react";
import { View, Image } from "react-native";

import styles from "../style";

import Img0 from "../img/lampicka05.jpg";
import Img1 from "../img/lampicka10.jpg";
import Img2 from "../img/lampicka15.jpg";

const Screen = () => (
  <View style={styles.container}>
    <View style={styles.row}>
      <Image source={Img0} style={{ width: 150, height: 150 }} />
      <Image source={Img1} style={{ width: 150, height: 150 }} />
      <Image source={Img2} style={{ width: 150, height: 150 }} />
    </View>
  </View>
);

export default Screen;
