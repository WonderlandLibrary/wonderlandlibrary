package optifine;

import java.lang.reflect.Field;

public class FieldLocatorFixed implements IFieldLocator {
  private Field field;
  
  public FieldLocatorFixed(Field p_i37_1_) {
    this.field = p_i37_1_;
  }
  
  public Field getField() {
    return this.field;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\FieldLocatorFixed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */