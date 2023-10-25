package com.sun.jna.ptr;

public class LongByReference extends ByReference {
  public LongByReference() {
    this(0L);
  }
  
  public LongByReference(long value) {
    super(8);
    setValue(value);
  }
  
  public void setValue(long value) {
    getPointer().setLong(0L, value);
  }
  
  public long getValue() {
    return getPointer().getLong(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\LongByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */