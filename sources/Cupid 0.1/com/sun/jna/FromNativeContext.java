package com.sun.jna;

public class FromNativeContext {
  private Class type;
  
  FromNativeContext(Class javaType) {
    this.type = javaType;
  }
  
  public Class getTargetType() {
    return this.type;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\FromNativeContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */