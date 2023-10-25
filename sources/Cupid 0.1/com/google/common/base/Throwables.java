package com.google.common.base;

import com.google.common.annotations.Beta;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public final class Throwables {
  public static <X extends Throwable> void propagateIfInstanceOf(@Nullable Throwable throwable, Class<X> declaredType) throws X {
    if (throwable != null && declaredType.isInstance(throwable))
      throw (X)declaredType.cast(throwable); 
  }
  
  public static void propagateIfPossible(@Nullable Throwable throwable) {
    propagateIfInstanceOf(throwable, Error.class);
    propagateIfInstanceOf(throwable, RuntimeException.class);
  }
  
  public static <X extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X> declaredType) throws X {
    propagateIfInstanceOf(throwable, declaredType);
    propagateIfPossible(throwable);
  }
  
  public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@Nullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2 {
    Preconditions.checkNotNull(declaredType2);
    propagateIfInstanceOf(throwable, declaredType1);
    propagateIfPossible(throwable, declaredType2);
  }
  
  public static RuntimeException propagate(Throwable throwable) {
    propagateIfPossible(Preconditions.<Throwable>checkNotNull(throwable));
    throw new RuntimeException(throwable);
  }
  
  public static Throwable getRootCause(Throwable throwable) {
    Throwable cause;
    while ((cause = throwable.getCause()) != null)
      throwable = cause; 
    return throwable;
  }
  
  @Beta
  public static List<Throwable> getCausalChain(Throwable throwable) {
    Preconditions.checkNotNull(throwable);
    List<Throwable> causes = new ArrayList<Throwable>(4);
    while (throwable != null) {
      causes.add(throwable);
      throwable = throwable.getCause();
    } 
    return Collections.unmodifiableList(causes);
  }
  
  public static String getStackTraceAsString(Throwable throwable) {
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Throwables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */