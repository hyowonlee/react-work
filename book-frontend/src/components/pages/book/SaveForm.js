import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const SaveForm = () => {
  let navigate = useNavigate();

  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  const changeValue = (e) => {
    setBook({
      ...book, // ...안쓰면 title 적고 author 적을때 초기화되니 이전 데이터를 가지고 있도록 ...을 사용
      [e.target.name]: e.target.value, // //computed property name (js 문법 키값 동적할당) 태그에서 name과 value를 받아옴
    });
  };

  const submitBook = (e) => {
    e.preventDefault(); // form태그에서 submit이 되면 새로고침되는데 그걸 막기위해 submit의 역할을 막는 메소드
    fetch('http://localhost:8080/book', {
      // 요청시 backend에서 crossorigin 세팅을 해서 외부에서도 api 사용하게 해야됨 여기선 해당 java Controller method에 @Crossorigin 어노테이션 추가함
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book), // js객체를 json 데이터로 변경
    })
      .then((res) => {
        if (res.status === 201) {
          return res.json(); //받은 json데이터를 js 객체로 변경
        } else {
          return null;
        }
      })
      .then((res) => {
        if (res !== null) {
          navigate('/'); // react router dom 6버전의 화면 전환
        } else {
          alert('등록 실패');
        }
      })
      .catch((err) => alert(err)); // catch는 fetch에서 에러를 잡는데 "네트워크 에러만" 잡아줌
  };

  return (
    <div>
      {/* form submit시 수행할 함수 지정 onSubmit 새로고침 막으려고 지정함*/}
      <Form onSubmit={submitBook}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Title</Form.Label>
          <Form.Control
            name="title"
            type="text"
            placeholder="Enter Title"
            onChange={changeValue} // state에 값을 넣기 위해 함수 호출(값 변경마다 호출됨)
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Author</Form.Label>
          <Form.Control
            name="author"
            type="text"
            placeholder="Enter Author"
            onChange={changeValue} // state에 값을 넣기 위해 함수 호출(값 변경마다 호출됨)
          />
        </Form.Group>

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
};

export default SaveForm;
