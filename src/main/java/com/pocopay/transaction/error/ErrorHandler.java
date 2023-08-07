package com.pocopay.transaction.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .code("internal_error")
                .message(ex.getMessage()).build();
        return ResponseEntity.status(status).body(responseBody);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .code("bad_request")
                .message(ex.getMessage()).build();
        return ResponseEntity.status(status).body(responseBody);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse responseBody = ExceptionResponse.builder()
                .code("not_found")
                .message(ex.getMessage()).build();
        return ResponseEntity.status(status).body(responseBody);
    }
}
