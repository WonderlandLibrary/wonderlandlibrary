package me.rhys.base.util.accessor.impl;

import java.lang.reflect.Method;
import me.rhys.base.util.accessor.Accessor;

public class MethodAccessor extends Accessor {
  private Method method;
  
  public MethodAccessor(Class<?> target, int index) {
    super(target);
    this.method = target.getDeclaredMethods()[index];
    this.method.setAccessible(true);
  }
  
  public MethodAccessor(Class<?> target, String name, Class<?>... parameterTypes) {
    super(target);
    try {
      this.method = target.getDeclaredMethod(name, parameterTypes);
      this.method.setAccessible(true);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } 
  }
  
  public MethodAccessor(Class<?> target, Method method) {
    super(target);
    this.method = method;
    this.method.setAccessible(true);
  }
  
  public <T> T invoke(Object parent, Object... params) {
    try {
      return (T)this.method.invoke(parent, params);
    } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public Method getMethod() {
    return this.method;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\accessor\impl\MethodAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */