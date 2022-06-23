package com.devcourse.eggmarket.domain.post.exception;

public class AlreadyCompletedException extends PurchaseInfoException {

  public AlreadyCompletedException(String message) {
    super(message);
  }
}
