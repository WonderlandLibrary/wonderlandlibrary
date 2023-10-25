package com.sun.jna.ptr;

public class FloatByReference extends ByReference {
  public FloatByReference() {
    this(0.0F);
  }
  
  public FloatByReference(float value) {
    super(4);
    setValue(value);
  }
  
  public void setValue(float value) {
    getPointer().setFloat(0L, value);
  }
  
  public float getValue() {
    return getPointer().getFloat(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\FloatByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */