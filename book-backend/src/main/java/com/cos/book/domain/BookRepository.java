package com.cos.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository //원랜 이 어노테이션을 적어야 스프링 ioc에 빈으로 등록되지만
// JpaRepository를 extends 해주면 생략 가능함
// Jpa Repository는 CRUD 함수를 들고있음 이 Repository는 db에 데이터를 insert 하거나 가져오는 역할
public interface BookRepository extends JpaRepository<Book, Long> {
}
