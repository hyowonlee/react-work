package com.cos.book.service;


import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

/**
 * 단위 테스트 (service 관련된 Bean이 ioc에 등록되면 됨 원본 자바파일 보면 bookRepository만 사용하기에 이런 테스트 시 필요한것들만 메모리에 뜨면 됨)
 *
 * 서비스 테스트는 생각해보면 사실 단위테스트가 아님
 * 서비스 테스트는 db에 접근하는 동작들이 있음 즉 BookRepository가 필요하다는 이야기임 그럼 service 단독으로 테스트하는게 아닌 Repository도 엮여있는 테스트라는 얘기임
 * 그렇다면 서비스를 테스트 하려면 어떻게 해야되냐면 BookRepository를 가짜 객체로 만들어서 테스트하면 Repository와 엮이지 않은 테스트임
 * @ExtendWith(MockitoExtension.class) 이 어노테이션에서 MockitoExtension이 Repository 가짜 객체를 만들어주는 환경 지원해준다
 * @Mock을 사용하면 그 객체는 spring bean (ioc)에 뜨는게 아닌 격리된 Mockito 환경에 메모리 공간에 뜨게된다
 *
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks // 해당 객체가 만들어질 때 이 java 파일에 @Mock로 떠있는 객체들을 이 안에 주입시켜줌 즉, BookService 안의 BookRepository에 밑에서 만든 Mock BookRepository 객체를 주입해준다는 것
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void save_test()
    {
        //service는 로직만 테스트하면 됨 내부에 있는 객체들은 다 가짜이니
        //지금 우리는 로직이 없으니 이런 형태로 테스트 한다라고만 알아두면 됨

        //given
        Book book = new Book();
        book.setTitle("책제목1");
        book.setAuthor("책저자1");

        //when
        when(bookRepository.save(book)).thenReturn(book);

        //test execute
        Book bookEntity = bookService.save(book);

        //then
        Assertions.assertEquals(bookEntity, book);
    }
}
