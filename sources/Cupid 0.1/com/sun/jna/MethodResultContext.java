package com.sun.jna;

import java.lang.reflect.Method;

public class MethodResultContext extends FunctionResultContext {
  private final Method method;
  
  MethodResultContext(Class resultClass, Function function, Object[] args, Method method) {
    super(resultClass, function, args);
    this.method = method;
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\MethodResultContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */