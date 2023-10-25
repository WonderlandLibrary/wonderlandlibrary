package com.google.gson.stream;

import java.io.IOException;

public final class MalformedJsonException extends IOException {
  private static final long serialVersionUID = 1L;
  
  public MalformedJsonException(String msg) {
    super(msg);
  }
  
  public MalformedJsonException(String msg, Throwable throwable) {
    super(msg);
    initCause(throwable);
  }
  
  public MalformedJsonException(Throwable throwable) {
    initCause(throwable);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\stream\MalformedJsonException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */