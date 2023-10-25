package com.sun.jna;

import java.lang.reflect.Field;

public class StructureReadContext extends FromNativeContext {
  private Structure structure;
  
  private Field field;
  
  StructureReadContext(Structure struct, Field field) {
    super(field.getType());
    this.structure = struct;
    this.field = field;
  }
  
  public Structure getStructure() {
    return this.structure;
  }
  
  public Field getField() {
    return this.field;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\StructureReadContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */