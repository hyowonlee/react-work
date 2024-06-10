package com.cos.book.web;


import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 *  통합 테스트
 *  @SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 실제 프로젝트에서 사용하고있는 톰캣을 사용하는게 아니라 테스트용 가짜 톰캣으로 테스트
 *  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // 실제 프로젝트에서 사용하는 톰캣으로 테스트
 *  @AutoConfigureMockMvc = MockMvc 를 IoC에 bean 등록
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc //MockMvc 사용을 위한 어노테이션
@Transactional // 각각의 테스트함수가 종료될때마다 트랜잭션을 rollback 하기위한 어노테이션
@Slf4j
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc; //MockMvc class는 테스트시 컨트롤러의 url로 request를 날려 테스트 하는 라이브러리 @AutoConfigureMockMvc 이 어노테이션이 있어야 bean 등록됨

    @Autowired
    private BookRepository bookRepository; // 통합테스트니까 모든 bean들이 메모리에 올라가서 repository 객체도 autowired로 사용 가능

    ///////////////////////////////
    //findAll_test()에서 new book 의 id가 null이면 이 파일 전체 테스트를 돌리면 id 체크하는 부분에서 오류가 남
    //db의 auto increment 때문에 이전 테스트에서 시퀀스 값이 증가되었기에 Transactional로 롤백 되어도 시퀀스 값은 리셋이 안되어서
    //원하는 id 값이 안나와서 에러가 나옴
    //그래서 각 테스트 실행 전에 메소드를 수행해서 시퀀스값을 초기화 해주기 위해 @BeforeEach 어노테이션을 사용한다
    //EntityManager는 JPA의 구현체로 쿼리 날릴때 쓰려고 AutoWired 한것
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init()
    {
//        // 매 테스트마다 db 넣기 귀찮으면 beforeeach에 등록하고 aftereach 에서 delete 함 대신 꼬일수도 있어서 추천은 안함
//        List<Book> books = new ArrayList<>();
//        books.add(new Book(null, "스프링부트 따라하기", "코스"));
//        books.add(new Book(null, "리엑트 따라하기", "토스"));
//        books.add(new Book(null, "junit 따라하기", "토스"));
//        bookRepository.saveAll(books); // 실제 repository class를 autowired 받아서 저장함

        entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
//    @AfterEach
//    public void end()
//    {
//        // 매 테스트마다 db 넣기 귀찮으면 beforeeach에 등록하고 aftereach 에서 delete 함 대신 꼬일수도 있어서 추천은 안함
//        bookRepository.deleteAll();
//    }

    ///////////////////////////////


    @Test
    public void save_test() throws Exception {
        log.info("save_test() 시작 ================================================================");

        /*
         *
         * 단위테스트 코드에서 save_test만 그대로 들고와봄
         * 통합테스트에선 진짜 객체들을 사용하기 때문에 stub이 필요없다
         * 그러니 mock service 객체를 위한 when() 구문이 필요가 없어져서 주석처리 해도 문제없이 동작
         *
         */
        // given (테스트 하기 위한 준비)
        Book book = new Book(null, "스프링 따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book); // object 를 json으로 바꾸는 함수
        // 여기서 json으로 만든 이유는 Controller 에서 requestbody 즉 json으로 데이터를 받기 때문에 거기에 던져줄려고 (given)

        // when 함수는 가정법 같이 미리 행동을 지정하는 것
        // service 는 mock이니 실제로 동작하지 않을거니까 when으로미리  행동을 지정한다 이걸 stub 이라고도 함
//        when(bookService.save(book)).thenReturn(new Book(1L,"스프링 따라하기","코스")); // save 항수 실행 후 book을 리턴하니 그 동작을 when으로 지정한 것

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book") // save 함수 실행
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 보내는 데이터의 타입은 json
                .content(content) // 보내는 데이터는 위에서 만든 book 객체
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 받는 데이터 타입은 json

        // then (테스트 검증)
        resultAction
                .andExpect(status().isCreated()) // 201 status code를 기대한다는 의미
                .andExpect(jsonPath("$.title").value("스프링 따라하기")) // return값이 json이니 jsonpath를 써서 원하는 json 값이 왔는지 확인
                .andDo(MockMvcResultHandlers.print()); // 수행할 행동이 테스트의 결과를 print 하라는 의미
    }

    /*
    *  단위테스트 코드에서 findAll_test 그대로 들고와봄
    *  통합테스트에선 진짜 객체들을 사용하기 때문에 stub이(given) 필요없다
    *  그러니 mock service 객체를 위한 when() 구문도 필요가 없어져서 주석처리 해도 문제없이 동작
    *
    * */
    @Test
    public void findAll_test() throws Exception {
        //given
        // 테스트라 지금 db에 데이터가 없어서 findAll 해도 가져올 데이터가 없으니 given(준비)에서 임시 데이터를 만들어서 when(실행)에서 결과값을 해당 데이터 리턴
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "코스"));
        books.add(new Book(2L, "리엑트 따라하기", "토스"));
        bookRepository.saveAll(books); // 실제 repository class를 autowired 받아서 저장함


        //stub 생성 (서비스 호출시 미리 준비한 임시 데이터 반환하게 stub 세팅)
        //when(bookService.selectAll()).thenReturn(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book") // findall 함수 실행
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 보내는 데이터가 없으니 받는 데이터 타입만 지정
                                                           // (controller의 findall 함수는 @RestController 가 있으니 즉 @ResponseBody가 붙어있는거고
                                                           // 따라서 @ResponseBody는 json 데이터를 리턴하니까 타입을 json으로 지정한것)

        //then
        resultAction
                .andExpect(status().isOk()) // 200 status code를 기대한다는 의미
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$", Matchers.hasSize(2))) // 받은 데이터의 개수가 2개를 기대
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기")) // 받은 json 데이터의 첫번째 데이터 title을 이걸로 기대
                .andDo(MockMvcResultHandlers.print()); // 수행할 행동이 테스트의 결과를 print 하라는 의미
    }




        @Test
    public void findById_test() throws Exception
    {
        //given
        Long id = 2L;
        // 테스트라 지금 db에 데이터가 없어서 findAll 해도 가져올 데이터가 없으니 given(준비)에서 임시 데이터를 만들어서 when(실행)에서 결과값을 해당 데이터 리턴
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "코스"));
        books.add(new Book(2L, "리엑트 따라하기", "토스"));
        bookRepository.saveAll(books); // 실제 repository class를 autowired 받아서 저장함

        //when(bookService.selectOne(id)).thenReturn(book);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("리엑트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void update_test() throws Exception
    {
        //given
        Long id = 1L;
        Book book = new Book(null,"C++ 따라하기","코스");
        String content = new ObjectMapper().writeValueAsString(book); // object 를 json으로 바꾸는 함수
        // 여기서 json으로 만든 이유는 Controller 에서 requestbody 즉 json으로 데이터를 받기 때문에 거기에 던져줄려고 (given)

        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리엑트 따라하기", "토스"));
        bookRepository.saveAll(books); // 실제 repository class를 autowired 받아서 저장함

        //when(bookService.update(id,book)).thenReturn(new Book(1L,"C++ 따라하기","코스"));
        //when
        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id) // save 함수 실행
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 테스트에서 보내는 데이터의 타입은 json (위에서 content를 json으로 만들었으니)
                .content(content) // 보내는 데이터는 위에서 만든 book 객체
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 받는 데이터 타입은 json

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++ 따라하기"))
                .andExpect(jsonPath("$.id").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_test() throws Exception
    {
        //given
        Long id = 1L;
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "스프링부트 따라하기", "코스"));
        books.add(new Book(null, "리엑트 따라하기", "토스"));
        bookRepository.saveAll(books); // 실제 repository class를 autowired 받아서 저장함
        //when(bookService.delete(id)).thenReturn("ok"); // delete에 대한 stub만 구현해주면 됨

        //when
        ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id) // delete 함수 실행
                .accept(MediaType.TEXT_PLAIN)); // 받는 데이터 타입은 ok 라는 string이니

        //delete 함수가 실행되면 남은 객체를 확인하여 제대로 수행됐는지 확인하기 위해 하나더 생성
        ResultActions resultAction2 = mockMvc.perform(get("/book") // findall 함수 실행
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 보내는 데이터가 없으니 받는 데이터 타입만 지정
                                                           // (controller의 findall 함수는 @RestController 가 있으니 즉 @ResponseBody가 붙어있는거고
                                                           // 따라서 @ResponseBody는 json 데이터를 리턴하니까 타입을 json으로 지정한것)

        //then
        //delete 함수의 실행 결과가 200이 잘 나오는지
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //delete 함수의 실행 결과에 남은 객체를 확인해서 잘 들어갔나 확인
        resultAction2
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(2L))
                .andExpect(jsonPath("$", Matchers.hasSize(1))) // 받은 데이터의 개수가 2개를 기대
                .andExpect(jsonPath("$.[0].title").value("리엑트 따라하기")) // 받은 json 데이터의 첫번째 데이터 title을 이걸로 기대
                .andDo(MockMvcResultHandlers.print());

        // String을 받아올때 String을 검증하려면 이렇게 해야함 위의 resultAction에선 상태코드가 맞는지만 검사하고 여기서 받은 문자가 ok 가 맞는지 확인
        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        Assertions.assertEquals("ok", result);
    }

}
