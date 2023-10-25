package com.sun.jna.ptr;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public abstract class ByReference extends PointerType {
  protected ByReference(int dataSize) {
    setPointer((Pointer)new Memory(dataSize));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\ptr\ByReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */