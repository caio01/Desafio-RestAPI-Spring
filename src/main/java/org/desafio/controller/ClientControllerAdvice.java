package org.desafio.controller;

import org.desafio.exception.ClientNullException;
import org.desafio.exception.DBException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ClientControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> error() {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("message", "Occurred a generic error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(ClientNullException.class)
    public ResponseEntity<Object> captureNullErrorClient() {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("message", "Verify fields of Client");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DBException.class)
    public ResponseEntity<Object> captureErrorConnectionDB() {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("message", "Problems of connection with Database");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
