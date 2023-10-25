package com.sun.jna.ptr;

public class ShortByReference extends ByReference {
  public ShortByReference() {
    this((short)0);
  }
  
  public ShortByReference(short value) {
    super(2);
    setValue(value);
  }
  
  public void setValue(short value) {
    getPointer().setShort(0L, value);
  }
  
  public short getValue() {
    return getPointer().getShort(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\ShortByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */