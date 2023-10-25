package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public class EqualsBuilder implements Builder<Boolean> {
  private static final ThreadLocal<Set<Pair<IDKey, IDKey>>> REGISTRY = new ThreadLocal<Set<Pair<IDKey, IDKey>>>();
  
  static Set<Pair<IDKey, IDKey>> getRegistry() {
    return REGISTRY.get();
  }
  
  static Pair<IDKey, IDKey> getRegisterPair(Object lhs, Object rhs) {
    IDKey left = new IDKey(lhs);
    IDKey right = new IDKey(rhs);
    return Pair.of(left, right);
  }
  
  static boolean isRegistered(Object lhs, Object rhs) {
    Set<Pair<IDKey, IDKey>> registry = getRegistry();
    Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
    Pair<IDKey, IDKey> swappedPair = Pair.of(pair.getLeft(), pair.getRight());
    return (registry != null && (registry.contains(pair) || registry.contains(swappedPair)));
  }
  
  static void register(Object lhs, Object rhs) {
    synchronized (EqualsBuilder.class) {
      if (getRegistry() == null)
        REGISTRY.set(new HashSet<Pair<IDKey, IDKey>>()); 
    } 
    Set<Pair<IDKey, IDKey>> registry = getRegistry();
    Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
    registry.add(pair);
  }
  
  static void unregister(Object lhs, Object rhs) {
    Set<Pair<IDKey, IDKey>> registry = getRegistry();
    if (registry != null) {
      Pair<IDKey, IDKey> pair = getRegisterPair(lhs, rhs);
      registry.remove(pair);
      synchronized (EqualsBuilder.class) {
        registry = getRegistry();
        if (registry != null && registry.isEmpty())
          REGISTRY.remove(); 
      } 
    } 
  }
  
  private boolean isEquals = true;
  
  public static boolean reflectionEquals(Object lhs, Object rhs, Collection<String> excludeFields) {
    return reflectionEquals(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
  }
  
  public static boolean reflectionEquals(Object lhs, Object rhs, String... excludeFields) {
    return reflectionEquals(lhs, rhs, false, null, excludeFields);
  }
  
  public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
    return reflectionEquals(lhs, rhs, testTransients, null, new String[0]);
  }
  
  public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass, String... excludeFields) {
    Class<?> testClass;
    if (lhs == rhs)
      return true; 
    if (lhs == null || rhs == null)
      return false; 
    Class<?> lhsClass = lhs.getClass();
    Class<?> rhsClass = rhs.getClass();
    if (lhsClass.isInstance(rhs)) {
      testClass = lhsClass;
      if (!rhsClass.isInstance(lhs))
        testClass = rhsClass; 
    } else if (rhsClass.isInstance(lhs)) {
      testClass = rhsClass;
      if (!lhsClass.isInstance(rhs))
        testClass = lhsClass; 
    } else {
      return false;
    } 
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    try {
      if (testClass.isArray()) {
        equalsBuilder.append(lhs, rhs);
      } else {
        reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
        while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
          testClass = testClass.getSuperclass();
          reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
        } 
      } 
    } catch (IllegalArgumentException e) {
      return false;
    } 
    return equalsBuilder.isEquals();
  }
  
  private static void reflectionAppend(Object lhs, Object rhs, Class<?> clazz, EqualsBuilder builder, boolean useTransients, String[] excludeFields) {
    if (isRegistered(lhs, rhs))
      return; 
    try {
      register(lhs, rhs);
      Field[] fields = clazz.getDeclaredFields();
      AccessibleObject.setAccessible((AccessibleObject[])fields, true);
      for (int i = 0; i < fields.length && builder.isEquals; i++) {
        Field f = fields[i];
        if (!ArrayUtils.contains((Object[])excludeFields, f.getName()) && f.getName().indexOf('$') == -1 && (useTransients || !Modifier.isTransient(f.getModifiers())) && !Modifier.isStatic(f.getModifiers()))
          try {
            builder.append(f.get(lhs), f.get(rhs));
          } catch (IllegalAccessException e) {
            throw new InternalError("Unexpected IllegalAccessException");
          }  
      } 
    } finally {
      unregister(lhs, rhs);
    } 
  }
  
  public EqualsBuilder appendSuper(boolean superEquals) {
    if (!this.isEquals)
      return this; 
    this.isEquals = superEquals;
    return this;
  }
  
  public EqualsBuilder append(Object lhs, Object rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    Class<?> lhsClass = lhs.getClass();
    if (!lhsClass.isArray()) {
      this.isEquals = lhs.equals(rhs);
    } else if (lhs.getClass() != rhs.getClass()) {
      setEquals(false);
    } else if (lhs instanceof long[]) {
      append((long[])lhs, (long[])rhs);
    } else if (lhs instanceof int[]) {
      append((int[])lhs, (int[])rhs);
    } else if (lhs instanceof short[]) {
      append((short[])lhs, (short[])rhs);
    } else if (lhs instanceof char[]) {
      append((char[])lhs, (char[])rhs);
    } else if (lhs instanceof byte[]) {
      append((byte[])lhs, (byte[])rhs);
    } else if (lhs instanceof double[]) {
      append((double[])lhs, (double[])rhs);
    } else if (lhs instanceof float[]) {
      append((float[])lhs, (float[])rhs);
    } else if (lhs instanceof boolean[]) {
      append((boolean[])lhs, (boolean[])rhs);
    } else {
      append((Object[])lhs, (Object[])rhs);
    } 
    return this;
  }
  
  public EqualsBuilder append(long lhs, long rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(int lhs, int rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(short lhs, short rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(char lhs, char rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(byte lhs, byte rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(double lhs, double rhs) {
    if (!this.isEquals)
      return this; 
    return append(Double.doubleToLongBits(lhs), Double.doubleToLongBits(rhs));
  }
  
  public EqualsBuilder append(float lhs, float rhs) {
    if (!this.isEquals)
      return this; 
    return append(Float.floatToIntBits(lhs), Float.floatToIntBits(rhs));
  }
  
  public EqualsBuilder append(boolean lhs, boolean rhs) {
    if (!this.isEquals)
      return this; 
    this.isEquals = (lhs == rhs);
    return this;
  }
  
  public EqualsBuilder append(Object[] lhs, Object[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(long[] lhs, long[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(int[] lhs, int[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(short[] lhs, short[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(char[] lhs, char[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(byte[] lhs, byte[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(double[] lhs, double[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(float[] lhs, float[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
    if (!this.isEquals)
      return this; 
    if (lhs == rhs)
      return this; 
    if (lhs == null || rhs == null) {
      setEquals(false);
      return this;
    } 
    if (lhs.length != rhs.length) {
      setEquals(false);
      return this;
    } 
    for (int i = 0; i < lhs.length && this.isEquals; i++)
      append(lhs[i], rhs[i]); 
    return this;
  }
  
  public boolean isEquals() {
    return this.isEquals;
  }
  
  public Boolean build() {
    return Boolean.valueOf(isEquals());
  }
  
  protected void setEquals(boolean isEquals) {
    this.isEquals = isEquals;
  }
  
  public void reset() {
    this.isEquals = true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\EqualsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */