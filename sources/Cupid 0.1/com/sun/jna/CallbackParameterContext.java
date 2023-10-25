package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackParameterContext extends FromNativeContext {
  private Method method;
  
  private Object[] args;
  
  private int index;
  
  CallbackParameterContext(Class javaType, Method m, Object[] args, int index) {
    super(javaType);
    this.method = m;
    this.args = args;
    this.index = index;
  }
  
  public Method getMethod() {
    return this.method;
  }
  
  public Object[] getArguments() {
    return this.args;
  }
  
  public int getIndex() {
    return this.index;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\CallbackParameterContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */