package com.sun.jna;

public class FunctionResultContext extends FromNativeContext {
  private Function function;
  
  private Object[] args;
  
  FunctionResultContext(Class resultClass, Function function, Object[] args) {
    super(resultClass);
    this.function = function;
    this.args = args;
  }
  
  public Function getFunction() {
    return this.function;
  }
  
  public Object[] getArguments() {
    return this.args;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\FunctionResultContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */