package com.cos.book.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 서버 실행시 object relation mapping이 됨 즉 이 객체로 h2 db에 테이블이 생성됨
public class Book {
    @Id // PK를 이 변수로 세팅
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 넘버링 전략, 해당 db의 번호증가 전략을 따라가겠다
    private Long id;

    private String title;

    private String author;
}