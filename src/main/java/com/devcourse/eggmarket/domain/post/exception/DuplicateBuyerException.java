package com.devcourse.eggmarket.domain.post.exception;

public class DuplicateBuyerException extends RuntimeException {

  public DuplicateBuyerException(String message) {
    super(message);
  }
}
