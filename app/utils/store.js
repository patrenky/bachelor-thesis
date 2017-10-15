import { createStore, compose, combineReducers, applyMiddleware } from 'redux';
// import { createNavigationEnabledStore } from '@expo/ex-navigation';

import thunk from 'redux-thunk';
// import { logger } from 'redux-logger';

import rootReducer from './../reducers';

// const createStoreWithNavigation = createNavigationEnabledStore({
//   createStore,
//   navigationStateKey: 'navigation',
// });

export default function configureStore() {
  const store = createStore(combineReducers(rootReducer), compose(applyMiddleware(thunk)));

  return store;
}
