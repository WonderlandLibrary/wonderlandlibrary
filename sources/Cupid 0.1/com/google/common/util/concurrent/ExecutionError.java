package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public class ExecutionError extends Error {
  private static final long serialVersionUID = 0L;
  
  protected ExecutionError() {}
  
  protected ExecutionError(@Nullable String message) {
    super(message);
  }
  
  public ExecutionError(@Nullable String message, @Nullable Error cause) {
    super(message, cause);
  }
  
  public ExecutionError(@Nullable Error cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\ExecutionError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */