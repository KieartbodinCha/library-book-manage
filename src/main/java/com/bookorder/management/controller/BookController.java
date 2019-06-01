package com.bookorder.management.controller;

import com.bookorder.management.entities.BookModel;
import com.bookorder.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<BookModel>> getBooks() {
        return new ResponseEntity<>(bookService.getAllBooksWithRecommend(), HttpStatus.OK);
    }
}
