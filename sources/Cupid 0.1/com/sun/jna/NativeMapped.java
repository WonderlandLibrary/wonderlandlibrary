package com.sun.jna;

public interface NativeMapped {
  Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  Object toNative();
  
  Class nativeType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\NativeMapped.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */