package com.bookorder.management.controller;

import com.bookorder.management.exception.BookOrderException;
import com.bookorder.management.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @ExceptionHandler({BookOrderException.class})
    public ResponseEntity<ResponseModel> handleException(Exception e) {
        return new ResponseEntity<ResponseModel>(new ResponseModel("ERROR", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({OAuth2AccessDeniedException.class})
    public ResponseEntity<ResponseModel> handleAuthorizeException(Exception e) {
        return new ResponseEntity<ResponseModel>(new ResponseModel("ERROR", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
