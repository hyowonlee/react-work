import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import reducer from './store';
import { createStore } from 'redux';
import { Provider } from 'react-redux';

//redux store 생성 (store.js 파일의 export된 reducer를 가져다 사용한다)
//이 store를 모든 곳에서 사용해야하니 바로 및 root.render의 Provider에 store를 지정하여
//<App /> 컴포넌트를 감싸준다 그럼 일일이 props로 넘기지 않아도 해당 store를 어디서든 사용 가능
const store = createStore(reducer);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <Provider store={store}>
      <App />
    </Provider>
  </BrowserRouter>,
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
