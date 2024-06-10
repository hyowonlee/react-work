package com.cos.book.domain;


//단위 테스트 (DB 관련된 Bean이 ioc에 등록되면 됨)

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest // Repository 테스트를 위해 JPA 관련된 객체들만 메모리에 띄우는 어노테이션
// 테스트에 사용할 db를 실제 db를 쓸건지 가짜 db를 쓸건지 설정하는 어노테이션으로 Replace.ANY는 가짜 내장 DB Replace.NONE는 실제 프로젝트에서 쓰는 DB로 테스트 하게됨
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional // 각각의 테스트함수가 종료될때마다 트랜잭션을 rollback 하기위한 어노테이션
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void save_test()
    {
        //실제 db 환경을 들고온거니까 repository 관련된 bean이 다 올라가있어서 실제 객체로 테스트해보면 됨

        //given
        Book book = new Book();
        book.setTitle("책제목1");
        book.setAuthor("책저자1");

        //when
        Book bookEntity = bookRepository.save(book);

        //then
        Assertions.assertEquals("책제목1", bookEntity.getTitle());
    }
}
