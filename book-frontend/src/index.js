import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'; // react bootstrap 적용

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    {/* 라우팅을 위한 BrowserRouter 추가 */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>,
);
