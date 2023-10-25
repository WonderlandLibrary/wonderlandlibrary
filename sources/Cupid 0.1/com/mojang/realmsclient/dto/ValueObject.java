package com.mojang.realmsclient.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class ValueObject {
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    for (Field f : getClass().getFields()) {
      if (!isStatic(f))
        try {
          sb.append(f.getName()).append("=").append(f.get(this)).append(" ");
        } catch (IllegalAccessException ignore) {} 
    } 
    sb.deleteCharAt(sb.length() - 1);
    sb.append('}');
    return sb.toString();
  }
  
  private static boolean isStatic(Field f) {
    return Modifier.isStatic(f.getModifiers());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\dto\ValueObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */