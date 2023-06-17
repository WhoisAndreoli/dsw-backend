package br.com.dswbackend.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class HandlerExceptions {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> error(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream().map(t -> t.getDefaultMessage())
        .collect(Collectors.joining(","));
    ErrorResponse erros = new ErrorResponse(msg, HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.badRequest().body(erros);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> error(MethodArgumentTypeMismatchException e) {
    ErrorResponse erros = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.badRequest().body(erros);
  }

  @ExceptionHandler(ErrorException.class)
  public ResponseEntity<ErrorResponse> error(ErrorException e) {
    ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.badRequest().body(error);
  }

}
