import { reducer as formReducer } from "redux-form";
import { combineReducers } from "redux";
// import { NavigationReducer } from '@expo/ex-navigation';

import appReducer from "./appReducer";

const app = combineReducers({
  appReducer
});

export default {
  app,
  //   navigation: NavigationReducer,
  form: formReducer
};
