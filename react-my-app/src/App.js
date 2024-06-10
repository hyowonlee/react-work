import React, { useState } from 'react';
import './App.css';
import Top from './components/Top';
import Bottom from './components/Bottom';

// 글쓰기, 글삭제, 글 목록보기
function App() {
  return (
    <div className="container">
      <h1>최상단 화면</h1>
      <Top />
      <Bottom />
    </div>
  );
}

export default App;
