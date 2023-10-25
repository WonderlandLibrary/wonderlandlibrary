package com.sun.jna;

public interface ToNativeConverter {
  Object toNative(Object paramObject, ToNativeContext paramToNativeContext);
  
  Class nativeType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ToNativeConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */