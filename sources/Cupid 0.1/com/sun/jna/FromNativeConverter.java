package com.sun.jna;

public interface FromNativeConverter {
  Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  Class nativeType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\FromNativeConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */