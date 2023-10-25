package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class Verify {
  public static void verify(boolean expression) {
    if (!expression)
      throw new VerifyException(); 
  }
  
  public static void verify(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
    if (!expression)
      throw new VerifyException(Preconditions.format(errorMessageTemplate, errorMessageArgs)); 
  }
  
  public static <T> T verifyNotNull(@Nullable T reference) {
    return verifyNotNull(reference, "expected a non-null reference", new Object[0]);
  }
  
  public static <T> T verifyNotNull(@Nullable T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
    verify((reference != null), errorMessageTemplate, errorMessageArgs);
    return reference;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Verify.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */