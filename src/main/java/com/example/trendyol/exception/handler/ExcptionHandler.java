package com.example.trendyol.exception.handler;

import com.example.trendyol.exception.IllegalUrlFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author created by cengizhan on 3.10.2020
 */
@ControllerAdvice
public class ExcptionHandler {
    @ExceptionHandler(value = {IllegalUrlFormatException.class})
    public ResponseEntity checkExceptionHandler() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("the url sent is incorrect");
    }
}
