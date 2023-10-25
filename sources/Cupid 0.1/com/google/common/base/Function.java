package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public interface Function<F, T> {
  @Nullable
  T apply(@Nullable F paramF);
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */