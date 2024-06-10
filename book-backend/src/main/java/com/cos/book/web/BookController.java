package com.cos.book.web;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @CrossOrigin // 외부에서도 api를 사용할 수 있도록 자신이 아니여도 api 허용해주는 어노테이션
    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book) //ResponseEntity는 return 시 http status code를 같이 보낼 수 있는 객체
    {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED); // HttpStatus.OK를 통해 status code를 201으로 보내고 데이터를 보낸다
    }

    @CrossOrigin // 외부에서도 api를 사용할 수 있도록 자신이 아니여도 api 허용해주는 어노테이션
    @GetMapping("/book")
    public ResponseEntity<?> findAll() //ResponseEntity는 return 시 http status code를 같이 보낼 수 있는 객체
    {
        return new ResponseEntity<>(bookService.selectAll(), HttpStatus.OK); // HttpStatus.OK를 통해 status code를 200으로 보내고 데이터를 보낸다
    }

    @CrossOrigin // 외부에서도 api를 사용할 수 있도록 자신이 아니여도 api 허용해주는 어노테이션
    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) //ResponseEntity는 return 시 http status code를 같이 보낼 수 있는 객체
    {
        return new ResponseEntity<>(bookService.selectOne(id), HttpStatus.OK); // HttpStatus.OK를 통해 status code를 200으로 보내고 데이터를 보낸다
    }

    @CrossOrigin // 외부에서도 api를 사용할 수 있도록 자신이 아니여도 api 허용해주는 어노테이션
    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book) //ResponseEntity는 return 시 http status code를 같이 보낼 수 있는 객체
    {
        return new ResponseEntity<>(bookService.update(id,book), HttpStatus.OK); // HttpStatus.OK를 통해 status code를 200으로 보내고 데이터를 보낸다
    }

    @CrossOrigin // 외부에서도 api를 사용할 수 있도록 자신이 아니여도 api 허용해주는 어노테이션
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) //ResponseEntity는 return 시 http status code를 같이 보낼 수 있는 객체
    {
        return new ResponseEntity<>(bookService.delete(id), HttpStatus.OK); // HttpStatus.OK를 통해 status code를 200으로 보내고 데이터를 보낸다
    }
}
