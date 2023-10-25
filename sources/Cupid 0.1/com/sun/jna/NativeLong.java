package com.sun.jna;

public class NativeLong extends IntegerType {
  public static final int SIZE = Native.LONG_SIZE;
  
  public NativeLong() {
    this(0L);
  }
  
  public NativeLong(long value) {
    super(SIZE, value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\NativeLong.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */