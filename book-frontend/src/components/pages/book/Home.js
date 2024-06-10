import React, { useEffect, useState } from 'react';
import BookItem from '../../BookItem';

const Home = () => {
  const [books, setBooks] = useState([]);
  // 함수 실행시 최초 한번 실행되는 useEffect 사용해서 데이터를 받아올것

  useEffect(() => {
    fetch('http://localhost:8080/book', { method: 'GET' }) // 요청시 backend에서 crossorigin 세팅을 해서 외부에서도 api 사용하게 해야됨 여기선 해당 java Controller method에 @Crossorigin 어노테이션 추가함
      .then((res) => res.json())
      .then((res) => {
        console.log(1, res);
        setBooks(res);
      }); // 비동기 함수
  }, []);

  return (
    <div>
      {books.map((book) => (
        <BookItem key={book.id} book={book} />
      ))}
    </div>
  );
};

export default Home;
