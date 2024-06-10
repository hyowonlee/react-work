import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { Link, useNavigate, useParams } from 'react-router-dom';

const Detail = (props) => {
  let navigate = useNavigate();
  const id = useParams().id;
  const [book, setBook] = useState({
    id: '',
    title: '',
    author: '',
  });

  useEffect(() => {
    fetch('http://localhost:8080/book/' + id, { method: 'GET' }) // 요청시 backend에서 crossorigin 세팅을 해서 외부에서도 api 사용하게 해야됨 여기선 해당 java Controller method에 @Crossorigin 어노테이션 추가함
      .then((res) => res.json())
      .then((res) => {
        console.log(1, res);
        setBook(res);
      })
      .catch((err) => alert(err));
  }, []);

  const deleteBook = (id) => {
    fetch('http://localhost:8080/book/' + id, { method: 'DELETE' }) // 요청시 backend에서 crossorigin 세팅을 해서 외부에서도 api 사용하게 해야됨 여기선 해당 java Controller method에 @Crossorigin 어노테이션 추가함
      .then((res) => res.text())
      .then((res) => {
        if (res === 'ok') {
          navigate('/');
        } else {
          alert('삭제실패');
        }
      })
      .catch((err) => alert(err));
  };

  const updateBook = () => {
    navigate('/updateForm/' + id);
  };

  return (
    <div>
      <h1>책 상세보기</h1>
      <Button variant="warning" onClick={updateBook}>
        수정
      </Button>{' '}
      <Button variant="danger" onClick={() => deleteBook(book.id)}>
        삭제
      </Button>
      <hr />
      <h3>{book.author}</h3>
      <h1>{book.title}</h1>
    </div>
  );
};

export default Detail;
