package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class ConstructorUtils {
  public static <T> T invokeConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeConstructor(cls, args, parameterTypes);
  }
  
  public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    args = ArrayUtils.nullToEmpty(args);
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
    if (ctor == null)
      throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName()); 
    return ctor.newInstance(args);
  }
  
  public static <T> T invokeExactConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    args = ArrayUtils.nullToEmpty(args);
    Class<?>[] parameterTypes = ClassUtils.toClass(args);
    return invokeExactConstructor(cls, args, parameterTypes);
  }
  
  public static <T> T invokeExactConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    args = ArrayUtils.nullToEmpty(args);
    parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
    Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
    if (ctor == null)
      throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName()); 
    return ctor.newInstance(args);
  }
  
  public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
    Validate.notNull(cls, "class cannot be null", new Object[0]);
    try {
      return getAccessibleConstructor(cls.getConstructor(parameterTypes));
    } catch (NoSuchMethodException e) {
      return null;
    } 
  }
  
  public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
    Validate.notNull(ctor, "constructor cannot be null", new Object[0]);
    return (MemberUtils.isAccessible(ctor) && isAccessible(ctor.getDeclaringClass())) ? ctor : null;
  }
  
  public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
    Validate.notNull(cls, "class cannot be null", new Object[0]);
    try {
      Constructor<T> ctor = cls.getConstructor(parameterTypes);
      MemberUtils.setAccessibleWorkaround(ctor);
      return ctor;
    } catch (NoSuchMethodException e) {
      Constructor<T> result = null;
      Constructor[] arrayOfConstructor = (Constructor[])cls.getConstructors();
      for (Constructor<?> ctor : arrayOfConstructor) {
        if (ClassUtils.isAssignable(parameterTypes, ctor.getParameterTypes(), true)) {
          ctor = getAccessibleConstructor(ctor);
          if (ctor != null) {
            MemberUtils.setAccessibleWorkaround(ctor);
            if (result == null || MemberUtils.compareParameterTypes(ctor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
              Constructor<T> constructor = (Constructor)ctor;
              result = constructor;
            } 
          } 
        } 
      } 
      return result;
    } 
  }
  
  private static boolean isAccessible(Class<?> type) {
    Class<?> cls = type;
    while (cls != null) {
      if (!Modifier.isPublic(cls.getModifiers()))
        return false; 
      cls = cls.getEnclosingClass();
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\reflect\ConstructorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */