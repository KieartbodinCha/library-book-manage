package com.bookorder.management.service;

import com.bookorder.management.config.CacheConfig;
import com.bookorder.management.entities.BookModel;
import com.bookorder.management.entities.BookOrderModel;
import com.bookorder.management.entities.UserModel;
import com.bookorder.management.repositories.BookOrderRepository;
import com.bookorder.management.repositories.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Value("${endpoint.book.api}")
    private String endpointBookApi;


    @Value("${endpoint.book.recommended.api}")
    private String endpointRecommendedBookApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders httpHeaders;

    public BookService() {
        this.httpHeaders = new HttpHeaders();
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

    public List<BookOrderModel> orderBooks(UserModel userModel, List<Long> bookIds) {
        if (CollectionUtils.isNotEmpty(bookIds)) {
            List<BookOrderModel> bookOrderModels = new ArrayList<>();
            for (Long bookId : bookIds) {
                BookOrderModel orderModel = new BookOrderModel();
                orderModel.setUserId(userModel.getId());
                orderModel.setBookId(bookId);
                bookOrderModels.add(orderModel);
            }
            return bookOrderRepository.saveAll(bookOrderModels);
        }
        return Collections.EMPTY_LIST;
    }

    public BigDecimal calculatePrice(List<Long> bookIds) {
        BigDecimal sum = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(bookIds)) {
            sum = bookOrderRepository.selectSumPriceByIds(bookIds);
        }
        return sum;
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


    private List<BookModel> getAllBooksInDB() {
        return bookRepository.findAll(Sort.by("recommended").descending().and(Sort.by("id").ascending()));
    }

    public void deleteBookOrdersForCurrentUser(UserModel userModel) {
        if (userModel != null && userModel.getId() != null) {
            bookOrderRepository.deleteOrdersByUserId(userModel.getId());
        }
    }
}
