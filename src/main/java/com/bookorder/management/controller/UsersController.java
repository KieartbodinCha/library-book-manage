package com.bookorder.management.controller;

import com.bookorder.management.entities.BookOrderModel;
import com.bookorder.management.entities.UserModel;
import com.bookorder.management.exception.BookOrderException;
import com.bookorder.management.model.AccessTokenResponse;
import com.bookorder.management.model.BookOrderRequest;
import com.bookorder.management.model.BookOrderResponse;
import com.bookorder.management.service.BookService;
import com.bookorder.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.getAccessTokenByRestApi(userModel), HttpStatus.OK);
    }

    @DeleteMapping("/secure/users")
    public void deleteUserLogged() {
        UserModel currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            bookService.deleteBookOrdersForCurrentUser(currentUser);
            userService.deleteLogForCurrentUser(currentUser);
        }
    }

    @PostMapping("/users")
    public void createUser(@RequestBody UserModel userModel) throws BookOrderException {
        userService.saveUser(userModel);

    }


    @GetMapping("/secure/users")
    public UserModel getCurrentUser() {
        return userService.getCurrentUser();
    }


    @PostMapping("/secure/users/order")
    public BookOrderResponse getUserBookOrder(@RequestBody BookOrderRequest request) {
        List<BookOrderModel> orders = bookService.orderBooks(userService.getCurrentUser(), request.getOrders());
        BigDecimal sum = bookService.calculatePrice(request.getOrders());
        BookOrderResponse response = new BookOrderResponse();
        response.setPrice(sum);
        return response;
    }

}
