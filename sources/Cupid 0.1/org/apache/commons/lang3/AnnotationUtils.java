package org.apache.commons.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnnotationUtils {
  private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle() {
      private static final long serialVersionUID = 1L;
      
      protected String getShortClassName(Class<?> cls) {
        Class<? extends Annotation> annotationType = null;
        for (Class<?> iface : ClassUtils.getAllInterfaces(cls)) {
          if (Annotation.class.isAssignableFrom(iface)) {
            Class<? extends Annotation> found = (Class)iface;
            annotationType = found;
            break;
          } 
        } 
        return (new StringBuilder((annotationType == null) ? "" : annotationType.getName())).insert(0, '@').toString();
      }
      
      protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value instanceof Annotation)
          value = AnnotationUtils.toString((Annotation)value); 
        super.appendDetail(buffer, fieldName, value);
      }
    };
  
  public static boolean equals(Annotation a1, Annotation a2) {
    if (a1 == a2)
      return true; 
    if (a1 == null || a2 == null)
      return false; 
    Class<? extends Annotation> type = a1.annotationType();
    Class<? extends Annotation> type2 = a2.annotationType();
    Validate.notNull(type, "Annotation %s with null annotationType()", new Object[] { a1 });
    Validate.notNull(type2, "Annotation %s with null annotationType()", new Object[] { a2 });
    if (!type.equals(type2))
      return false; 
    try {
      for (Method m : type.getDeclaredMethods()) {
        if ((m.getParameterTypes()).length == 0 && isValidAnnotationMemberType(m.getReturnType())) {
          Object v1 = m.invoke(a1, new Object[0]);
          Object v2 = m.invoke(a2, new Object[0]);
          if (!memberEquals(m.getReturnType(), v1, v2))
            return false; 
        } 
      } 
    } catch (IllegalAccessException ex) {
      return false;
    } catch (InvocationTargetException ex) {
      return false;
    } 
    return true;
  }
  
  public static int hashCode(Annotation a) {
    int result = 0;
    Class<? extends Annotation> type = a.annotationType();
    for (Method m : type.getDeclaredMethods()) {
      try {
        Object value = m.invoke(a, new Object[0]);
        if (value == null)
          throw new IllegalStateException(String.format("Annotation method %s returned null", new Object[] { m })); 
        result += hashMember(m.getName(), value);
      } catch (RuntimeException ex) {
        throw ex;
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      } 
    } 
    return result;
  }
  
  public static String toString(Annotation a) {
    ToStringBuilder builder = new ToStringBuilder(a, TO_STRING_STYLE);
    for (Method m : a.annotationType().getDeclaredMethods()) {
      if ((m.getParameterTypes()).length <= 0)
        try {
          builder.append(m.getName(), m.invoke(a, new Object[0]));
        } catch (RuntimeException ex) {
          throw ex;
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }  
    } 
    return builder.build();
  }
  
  public static boolean isValidAnnotationMemberType(Class<?> type) {
    if (type == null)
      return false; 
    if (type.isArray())
      type = type.getComponentType(); 
    return (type.isPrimitive() || type.isEnum() || type.isAnnotation() || String.class.equals(type) || Class.class.equals(type));
  }
  
  private static int hashMember(String name, Object value) {
    int part1 = name.hashCode() * 127;
    if (value.getClass().isArray())
      return part1 ^ arrayMemberHash(value.getClass().getComponentType(), value); 
    if (value instanceof Annotation)
      return part1 ^ hashCode((Annotation)value); 
    return part1 ^ value.hashCode();
  }
  
  private static boolean memberEquals(Class<?> type, Object o1, Object o2) {
    if (o1 == o2)
      return true; 
    if (o1 == null || o2 == null)
      return false; 
    if (type.isArray())
      return arrayMemberEquals(type.getComponentType(), o1, o2); 
    if (type.isAnnotation())
      return equals((Annotation)o1, (Annotation)o2); 
    return o1.equals(o2);
  }
  
  private static boolean arrayMemberEquals(Class<?> componentType, Object o1, Object o2) {
    if (componentType.isAnnotation())
      return annotationArrayMemberEquals((Annotation[])o1, (Annotation[])o2); 
    if (componentType.equals(byte.class))
      return Arrays.equals((byte[])o1, (byte[])o2); 
    if (componentType.equals(short.class))
      return Arrays.equals((short[])o1, (short[])o2); 
    if (componentType.equals(int.class))
      return Arrays.equals((int[])o1, (int[])o2); 
    if (componentType.equals(char.class))
      return Arrays.equals((char[])o1, (char[])o2); 
    if (componentType.equals(long.class))
      return Arrays.equals((long[])o1, (long[])o2); 
    if (componentType.equals(float.class))
      return Arrays.equals((float[])o1, (float[])o2); 
    if (componentType.equals(double.class))
      return Arrays.equals((double[])o1, (double[])o2); 
    if (componentType.equals(boolean.class))
      return Arrays.equals((boolean[])o1, (boolean[])o2); 
    return Arrays.equals((Object[])o1, (Object[])o2);
  }
  
  private static boolean annotationArrayMemberEquals(Annotation[] a1, Annotation[] a2) {
    if (a1.length != a2.length)
      return false; 
    for (int i = 0; i < a1.length; i++) {
      if (!equals(a1[i], a2[i]))
        return false; 
    } 
    return true;
  }
  
  private static int arrayMemberHash(Class<?> componentType, Object o) {
    if (componentType.equals(byte.class))
      return Arrays.hashCode((byte[])o); 
    if (componentType.equals(short.class))
      return Arrays.hashCode((short[])o); 
    if (componentType.equals(int.class))
      return Arrays.hashCode((int[])o); 
    if (componentType.equals(char.class))
      return Arrays.hashCode((char[])o); 
    if (componentType.equals(long.class))
      return Arrays.hashCode((long[])o); 
    if (componentType.equals(float.class))
      return Arrays.hashCode((float[])o); 
    if (componentType.equals(double.class))
      return Arrays.hashCode((double[])o); 
    if (componentType.equals(boolean.class))
      return Arrays.hashCode((boolean[])o); 
    return Arrays.hashCode((Object[])o);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\AnnotationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */