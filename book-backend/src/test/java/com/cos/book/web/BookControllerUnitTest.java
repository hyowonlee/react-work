package com.cos.book.web;


import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//단위 테스트
@WebMvcTest // 이 어노테이션을 쓰면 이 Controller test 를 위한 객체들이 메모리에 뜨게됨 (예를들면 테스트용 filter등 과 테스트를 진행할 controller 자체 객체 등)
// 근데 이 어노테이션을 적으면 Controller 등록된 bean을 자동으로 띄우는듯 함 왜냐면 아무것도 안적고 실했는데 Controller 객체 안에있는 service ㅠㄷ무dl
@Slf4j
public class BookControllerUnitTest {
    @Autowired
    private MockMvc mockMvc; // controller url 주소 호출 해주는 도구

    @MockBean // spring IoC 환경에 해당 클래스의 껍데기 bean 인 mock이 등록됨
    private BookService bookService;

    @Test
    public void save_test() throws Exception {
        log.info("save_test() 시작 ================================================================");

        //BDDMockito 패턴 given, when, then
        // given (테스트 하기 위한 준비)
        Book book = new Book(null,"스프링 따라하기","코스");
        String content = new ObjectMapper().writeValueAsString(book); // object 를 json으로 바꾸는 함수
        // 여기서 json으로 만든 이유는 Controller 에서 requestbody 즉 json으로 데이터를 받기 때문에 거기에 던져줄려고 (given)

        // when 함수는 가정법 같이 미리 행동을 지정하는 것
        // service 는 mock이니 실제로 동작하지 않을거니까 when으로미리  행동을 지정한다 이걸 stub 이라고도 함
        when(bookService.save(book)).thenReturn(new Book(1L,"스프링 따라하기","코스")); // save 함수 실행 후 book을 리턴하니 그 동작을 when 함수로 직접 지정한 것
        
        
        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book") // save 함수 실행
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 테스트에서 보내는 데이터의 타입은 json (위에서 content를 json으로 만들었으니)
                .content(content) // 보내는 데이터는 위에서 만든 book 객체
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 받는 데이터 타입은 json

        // then (테스트 검증)
        resultAction
                .andExpect(status().isCreated()) // 201 status code를 기대한다는 의미
                .andExpect(jsonPath("$.title").value("스프링 따라하기")) // return값이 json이니 jsonpath를 써서 원하는 json 값이 왔는지 확인
                .andDo(MockMvcResultHandlers.print()); // 수행할 행동이 테스트의 결과를 print 하라는 의미

    }

    @Test
    public void findAll_test() throws Exception {
        //given
        // 단위테스트라 지금 db에 데이터가 없어서 findAll 해도 가져올 데이터가 없으니 given(준비)에서 임시 데이터를 만들어서 when(실행)에서 결과값을 해당 데이터 리턴
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "스프링부트 따라하기", "코스"));
        books.add(new Book(2L, "리엑트 따라하기", "토스"));
        //stub 생성 (서비스 호출시 미리 준비한 임시 데이터 반환하게 stub 세팅)
        when(bookService.selectAll()).thenReturn(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book") // findall 함수 실행
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 보내는 데이터가 없으니 받는 데이터 타입만 지정
                                                           // (controller의 findall 함수는 @RestController 가 있으니 즉 @ResponseBody가 붙어있는거고
                                                           // 따라서 @ResponseBody는 json 데이터를 리턴하니까 타입을 json으로 지정한것)

        //then
        resultAction
                .andExpect(status().isOk()) // 200 status code를 기대한다는 의미
                .andExpect(jsonPath("$", Matchers.hasSize(2))) // 받은 데이터의 개수가 2개를 기대
                .andExpect(jsonPath("$.[0].title").value("스프링부트 따라하기")) // 받은 json 데이터의 첫번째 데이터 title을 이걸로 기대
                .andDo(MockMvcResultHandlers.print()); // 수행할 행동이 테스트의 결과를 print 하라는 의미
    }

    @Test
    public void findById_test() throws Exception
    {
        //given
        Book book = new Book(1L,"자바 공부하기", "쌀");
        Long id = 1L;
        when(bookService.selectOne(id)).thenReturn(book);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바 공부하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_test() throws Exception
    {
        //given
        Long id = 1L;
        Book book = new Book(null,"스프링 따라하기","코스");
        String content = new ObjectMapper().writeValueAsString(book); // object 를 json으로 바꾸는 함수
        // 여기서 json으로 만든 이유는 Controller 에서 requestbody 즉 json으로 데이터를 받기 때문에 거기에 던져줄려고 (given)

        when(bookService.update(id,book)).thenReturn(new Book(1L,"C++ 따라하기","코스"));
        //when
        ResultActions resultAction = mockMvc.perform(put("/book/{id}", id) // save 함수 실행
                .contentType(MediaType.APPLICATION_JSON_UTF8) // 테스트에서 보내는 데이터의 타입은 json (위에서 content를 json으로 만들었으니)
                .content(content) // 보내는 데이터는 위에서 만든 book 객체
                .accept(MediaType.APPLICATION_JSON_UTF8)); // 받는 데이터 타입은 json

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++ 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_test() throws Exception
    {
        //given
        Long id = 1L;
        when(bookService.delete(id)).thenReturn("ok"); // delete에 대한 stub만 구현해주면 됨

        //when
        ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id) // delete 함수 실행
                .accept(MediaType.TEXT_PLAIN)); // 받는 데이터 타입은 ok 라는 string이니

        //then
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // String을 받아올때 String을 검증하려면 이렇게 해야함 위의 resultAction에선 상태코드가 맞는지만 검사하고 여기서 받은 문자가 ok 가 맞는지 확인
        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        Assertions.assertEquals("ok", result);
    }

}
