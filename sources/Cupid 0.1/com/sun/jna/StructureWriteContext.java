package com.sun.jna;

import java.lang.reflect.Field;

public class StructureWriteContext extends ToNativeContext {
  private Structure struct;
  
  private Field field;
  
  StructureWriteContext(Structure struct, Field field) {
    this.struct = struct;
    this.field = field;
  }
  
  public Structure getStructure() {
    return this.struct;
  }
  
  public Field getField() {
    return this.field;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\StructureWriteContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */