package com.bookorder.management.service;

import com.bookorder.management.config.CacheConfig;
import com.bookorder.management.entities.BookModel;
import com.bookorder.management.repositories.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private HttpHeaders httpHeaders;

    @Value("${endpoint.book.api}")
    private String endpointBookApi;


    @Value("${endpoint.book.recommended.api}")
    private String endpointRecommendedBookApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    private ObjectMapper objectMapper;

    public BookService() {
        this.httpHeaders = new HttpHeaders();
        this.objectMapper = new ObjectMapper();
    }

    private List<BookModel> getBookFromApi() {
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> response = restTemplate.exchange(endpointBookApi, HttpMethod.GET, entity, Object.class);
        List<BookModel> bookList = objectMapper.convertValue(response.getBody(), new TypeReference<List<BookModel>>() {
        });
        return bookList;
    }

    private List<BookModel> getRecommendedBookFromApi() {
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<Object> response = restTemplate.exchange(endpointRecommendedBookApi, HttpMethod.GET, entity, Object.class);
        List<BookModel> bookList = objectMapper.convertValue(response.getBody(), new TypeReference<List<BookModel>>() {
        });
        return bookList;
    }

    @Cacheable(value = CacheConfig.BOOK_CACHE)
    public List<BookModel> getAllBooksWithRecommend() {
        List<Long> recBooksId = new ArrayList<>();
        getRecommendedBookFromApi().stream().forEach(bookModel -> recBooksId.add(bookModel.getId()));
        bookRepository.saveAll(getBookFromApi());
        List<BookModel> recommendBooks = bookRepository.findAllById(recBooksId);
        recommendBooks.parallelStream().forEach(bookModel -> bookModel.setRecommended(true));
        bookRepository.saveAll(recommendBooks);
        return getAllBooksInDB();
    }

    public List<BookModel> getAllBooksInDB() {
        return bookRepository.findAll(Sort.by("recommended").descending().and(Sort.by("id").ascending()));
    }

}
