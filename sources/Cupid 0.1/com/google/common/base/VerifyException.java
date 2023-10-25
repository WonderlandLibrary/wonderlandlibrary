package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public class VerifyException extends RuntimeException {
  public VerifyException() {}
  
  public VerifyException(@Nullable String message) {
    super(message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\VerifyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */