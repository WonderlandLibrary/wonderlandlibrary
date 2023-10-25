package com.sun.jna;

import java.lang.reflect.Method;

public class MethodParameterContext extends FunctionParameterContext {
  private Method method;
  
  MethodParameterContext(Function f, Object[] args, int index, Method m) {
    super(f, args, index);
    this.method = m;
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\MethodParameterContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */