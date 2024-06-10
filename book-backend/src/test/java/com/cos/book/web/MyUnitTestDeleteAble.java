package com.cos.book.web;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@Transactional
@AutoConfigureMockMvc
@Slf4j
public class MyUnitTestDeleteAble {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void test() throws Exception {
        //given
        log.info("given start ==========================================");
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"title1","author1"));
        books.add(new Book(2L,"title2","author2"));
        Mockito.when(bookService.selectAll()).thenReturn(books);

        //when
        log.info("when start ==========================================");
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        log.info("then start ==========================================");
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("title1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("title2"));



    }
}
