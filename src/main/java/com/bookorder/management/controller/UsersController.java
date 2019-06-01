package com.bookorder.management.controller;

import com.bookorder.management.entities.UserModel;
import com.bookorder.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

@RestController
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel userModel, Model model, ServletRequest request) {
        System.out.println(String.format(" usermodel : %s", userModel));
        System.out.println(String.format(" model : %s", model));
        System.out.println(String.format(" request : %s", request));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/api/users")
    public void deleteUserLogged(@RequestBody UserModel userModel) {

    }


    @PostMapping("/api/users")
    public void createUser(@RequestBody UserModel userModel) {
        UserModel savedUser = userService.saveUser(userModel);

    }

    @GetMapping("/api/users")
    public UserModel getCurrentUser() {
        return null;
    }


    @GetMapping("/api/users/order")
    public String getUserBookOrder() {
        return "test";
    }

}
