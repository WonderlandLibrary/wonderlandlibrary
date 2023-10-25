package me.rhys.base.util.accessor.impl;

import java.lang.reflect.Field;
import me.rhys.base.util.accessor.Accessor;

public class FieldAccessor<T> extends Accessor {
  private Field field;
  
  public Field getField() {
    return this.field;
  }
  
  public FieldAccessor(Class<?> target, String name) {
    super(target);
    try {
      this.field = target.getDeclaredField(name);
      if (!this.field.isAccessible())
        this.field.setAccessible(true); 
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } 
  }
  
  public FieldAccessor(Class<?> target, int index) {
    super(target);
    this.field = target.getDeclaredFields()[index];
    if (!this.field.isAccessible())
      this.field.setAccessible(true); 
  }
  
  public void set(Object handle, T value) {
    try {
      this.field.set(handle, value);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } 
  }
  
  public T get(Object handle) {
    try {
      return (T)this.field.get(handle);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\accessor\impl\FieldAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */