package com.adiths.orderservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Object> exception(Exception exception){
//        log.info(exception.getMessage());
//        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
