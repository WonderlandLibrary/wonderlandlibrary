package com.sun.jna;

public interface TypeMapper {
  FromNativeConverter getFromNativeConverter(Class paramClass);
  
  ToNativeConverter getToNativeConverter(Class paramClass);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\TypeMapper.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */