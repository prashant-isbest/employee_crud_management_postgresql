// package com.prashant.exception;

// import java.util.NoSuchElementException;

// import com.prashant.employee.model.ErrorModel;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import
// org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// @RestControllerAdvice
// public class RestExceptionHandler extends ResponseEntityExceptionHandler {

// @ExceptionHandler(NoSuchElementException.class)
// private ResponseEntity<ErrorModel>
// handleNoSuchElementFoundException(NoSuchElementException ex) {
// ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Entity not found",
// ex.getMessage());
// return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
// }
// }