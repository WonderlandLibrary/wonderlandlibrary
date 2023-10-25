package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackResultContext extends ToNativeContext {
  private Method method;
  
  CallbackResultContext(Method callbackMethod) {
    this.method = callbackMethod;
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\CallbackResultContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */