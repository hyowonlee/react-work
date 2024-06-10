package com.cos.book.service;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final이 붙은 변수에 대해 매개변수를 가진 생성자 만들어줌 자동으로 생성자 주입을 해줌
@Service // service에선 로직 기능을 정의할 수 있고, 트랜잭션을 관리할 수 있음
public class BookService {

    private final BookRepository bookRepository; // @RequiredArgsConstructor 를 통해 생성자 주입

    @Transactional // 롤백을 위한 트랜잭션 관리
    public Book save (Book book)
    {
        return bookRepository.save(book); // jpa엔 저장해주는 save 함수가 존재 이 함수는 자기가 save한 entity를 그대로 리턴
    }

    @Transactional(readOnly = true) // readOnly를 준 이유는 jpa에서 변경감지라는 내부 기능이 있는데 엔티티가 변경됐는지 감시하는데 readonly를 걸어놓으면 변경감지가 활성화 되지 않아 리소스를 아낄수있음
    // 그리고 select때 transactional을 거는 이유는 한 트랜잭션 내에서는 select 하는 데이터가 달라져도 그 데이터의 정합성을 유지해주기 때문 (select했는데 중간에 딴놈이 update해서 값이 바뀌어도 select 시 같은값을 가져옴)
    public Book selectOne (Long id)
    {
        return bookRepository.findById(id).orElseThrow(() -> { return new IllegalArgumentException("id를 확인해주세요"); }); // Supplier와 람다식을 사용
    }

    @Transactional(readOnly = true)
    public List<Book> selectAll ()
    {
        return bookRepository.findAll();
    }

    @Transactional
    public Book update (Long id, Book book)
    {
        // 더티체킹
        Book bookEntity = bookRepository.findById(id).orElseThrow(()->{return new IllegalArgumentException("id를 확인해주세요"); }); // 영속화
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
    }

    @Transactional
    public String delete (Long id)
    {
        bookRepository.deleteById(id); // 오류가 터지면 java exception을 타니까 exception은 나중에 낚아채서 한꺼번에 관리하는 global exception handler로 되니까 신경 안써도 됨
        return "ok";
    }
}
