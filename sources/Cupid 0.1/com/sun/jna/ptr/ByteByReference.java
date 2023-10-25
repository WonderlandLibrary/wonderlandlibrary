package com.sun.jna.ptr;

public class ByteByReference extends ByReference {
  public ByteByReference() {
    this((byte)0);
  }
  
  public ByteByReference(byte value) {
    super(1);
    setValue(value);
  }
  
  public void setValue(byte value) {
    getPointer().setByte(0L, value);
  }
  
  public byte getValue() {
    return getPointer().getByte(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\ByteByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */