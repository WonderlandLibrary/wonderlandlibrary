package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class PointerByReference extends ByReference {
  public PointerByReference() {
    this(null);
  }
  
  public PointerByReference(Pointer value) {
    super(Pointer.SIZE);
    setValue(value);
  }
  
  public void setValue(Pointer value) {
    getPointer().setPointer(0L, value);
  }
  
  public Pointer getValue() {
    return getPointer().getPointer(0L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\PointerByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */