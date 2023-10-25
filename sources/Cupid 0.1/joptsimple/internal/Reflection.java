package joptsimple.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import joptsimple.ValueConverter;

public final class Reflection {
  private Reflection() {
    throw new UnsupportedOperationException();
  }
  
  public static <V> ValueConverter<V> findConverter(Class<V> clazz) {
    Class<V> maybeWrapper = Classes.wrapperOf(clazz);
    ValueConverter<V> valueOf = valueOfConverter(maybeWrapper);
    if (valueOf != null)
      return valueOf; 
    ValueConverter<V> constructor = constructorConverter(maybeWrapper);
    if (constructor != null)
      return constructor; 
    throw new IllegalArgumentException(clazz + " is not a value type");
  }
  
  private static <V> ValueConverter<V> valueOfConverter(Class<V> clazz) {
    try {
      Method valueOf = clazz.getDeclaredMethod("valueOf", new Class[] { String.class });
      if (meetsConverterRequirements(valueOf, clazz))
        return new MethodInvokingValueConverter<V>(valueOf, clazz); 
      return null;
    } catch (NoSuchMethodException ignored) {
      return null;
    } 
  }
  
  private static <V> ValueConverter<V> constructorConverter(Class<V> clazz) {
    try {
      return new ConstructorInvokingValueConverter<V>(clazz.getConstructor(new Class[] { String.class }));
    } catch (NoSuchMethodException ignored) {
      return null;
    } 
  }
  
  public static <T> T instantiate(Constructor<T> constructor, Object... args) {
    try {
      return constructor.newInstance(args);
    } catch (Exception ex) {
      throw reflectionException(ex);
    } 
  }
  
  public static Object invoke(Method method, Object... args) {
    try {
      return method.invoke(null, args);
    } catch (Exception ex) {
      throw reflectionException(ex);
    } 
  }
  
  public static <V> V convertWith(ValueConverter<V> converter, String raw) {
    return (converter == null) ? (V)raw : (V)converter.convert(raw);
  }
  
  private static boolean meetsConverterRequirements(Method method, Class<?> expectedReturnType) {
    int modifiers = method.getModifiers();
    return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && expectedReturnType.equals(method.getReturnType()));
  }
  
  private static RuntimeException reflectionException(Exception ex) {
    if (ex instanceof IllegalArgumentException)
      return new ReflectionException(ex); 
    if (ex instanceof java.lang.reflect.InvocationTargetException)
      return new ReflectionException(ex.getCause()); 
    if (ex instanceof RuntimeException)
      return (RuntimeException)ex; 
    return new ReflectionException(ex);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Reflection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */