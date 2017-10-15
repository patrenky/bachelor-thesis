import { StyleSheet, Dimensions } from "react-native";

function vw(percentageWidth) {
  return Dimensions.get("window").width * (percentageWidth / 100);
}

function vh(percentageHeight) {
  return Dimensions.get("window").height * (percentageHeight / 100);
}

export default StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center"
  }
});
