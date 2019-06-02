package com.bookorder.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssistantController extends BaseController {

    @GetMapping("/assist/hello")
    public ResponseEntity<String> get() {
        return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
    }


}
