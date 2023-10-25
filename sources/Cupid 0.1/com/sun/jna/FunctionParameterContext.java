package com.sun.jna;

public class FunctionParameterContext extends ToNativeContext {
  private Function function;
  
  private Object[] args;
  
  private int index;
  
  FunctionParameterContext(Function f, Object[] args, int index) {
    this.function = f;
    this.args = args;
    this.index = index;
  }
  
  public Function getFunction() {
    return this.function;
  }
  
  public Object[] getParameters() {
    return this.args;
  }
  
  public int getParameterIndex() {
    return this.index;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\FunctionParameterContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */