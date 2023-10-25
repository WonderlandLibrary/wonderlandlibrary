package com.sun.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface InvocationMapper {
  InvocationHandler getInvocationHandler(NativeLibrary paramNativeLibrary, Method paramMethod);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\InvocationMapper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */