package br.com.dswbackend.exceptions;

public class ErrorException extends RuntimeException {

  public ErrorException(String msg) {
    super(msg);
  }
}
