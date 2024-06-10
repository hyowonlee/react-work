import React from 'react';
import '../App.css';
import { useSelector } from 'react-redux';
const Top = () => {
  //const number = useSelector((store) => store.number); //store.js 에서 state를 가져옴
  //const username = useSelector((store) => store.username);
  const { number, username } = useSelector((store) => store);

  return (
    <div className="sub_container">
      <h1>Top</h1>
      번호 : {number} <br />
      이름 : {username}
    </div>
  );
};

export default Top;
