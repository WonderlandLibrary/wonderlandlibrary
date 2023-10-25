package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public class ComputationException extends RuntimeException {
  private static final long serialVersionUID = 0L;
  
  public ComputationException(@Nullable Throwable cause) {
    super(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ComputationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */