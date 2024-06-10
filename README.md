##React-Springboot Book 프로젝트

> https://www.youtube.com/playlist?list=PL93mKxaRDidEfLM0I_FFb-98vfAQgXT82

### 스프링부트 (book-backend)

- springboot ^2.0 (2.0이상)
- JPA
- H2 (MySQL)
- Maven
- Lombok

### React (book-frontend) (react-my-app은 backend 연동전 강의에서 진행한 단독 react 코드)

- yarn add react-router-dom (라우팅할때 필요한 라이브러리 링크넣을때 a태그 대신 사용한 그거 <Link> 태그)
- yarn add redux react-redux (redux: create store 시 사용, react-redux:provider 생성/연결 ,useDispatch/useSelect 등 사용할때 필요)
- yarn add react-bootstrap bootstrap (디자인용 부트스트랩)

```txt
import 'bootstrap/dist/css/bootstrap.min.css'; // index.js에 react bootstrap 적용
```

### 프로젝트 세팅

- prettier 세팅파일 파일명 : .prettierrc

```json
{
  "singleQuote": true,
  "semi": true,
  "tabWidth": 2,
  "trailingComma": "all",
  "printWidth": 80
}
```
