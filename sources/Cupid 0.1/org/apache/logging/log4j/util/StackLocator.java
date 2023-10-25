package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import java.util.Stack;
import java.util.function.Predicate;

public final class StackLocator {
  static final int JDK_7u25_OFFSET;
  
  private static final Method GET_CALLER_CLASS;
  
  private static final StackLocator INSTANCE;
  
  static {
    Method getCallerClass;
    int java7u25CompensationOffset = 0;
    try {
      Class<?> sunReflectionClass = LoaderUtil.loadClass("sun.reflect.Reflection");
      getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", new Class[] { int.class });
      Object o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
      getCallerClass.invoke(null, new Object[] { Integer.valueOf(0) });
      if (o == null || o != sunReflectionClass) {
        getCallerClass = null;
        java7u25CompensationOffset = -1;
      } else {
        o = getCallerClass.invoke(null, new Object[] { Integer.valueOf(1) });
        if (o == sunReflectionClass) {
          System.out.println("WARNING: Java 1.7.0_25 is in use which has a broken implementation of Reflection.getCallerClass().  Please consider upgrading to Java 1.7.0_40 or later.");
          java7u25CompensationOffset = 1;
        } 
      } 
    } catch (Exception|LinkageError e) {
      System.out.println("WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance.");
      getCallerClass = null;
      java7u25CompensationOffset = -1;
    } 
    GET_CALLER_CLASS = getCallerClass;
    JDK_7u25_OFFSET = java7u25CompensationOffset;
    INSTANCE = new StackLocator();
  }
  
  public static StackLocator getInstance() {
    return INSTANCE;
  }
  
  @PerformanceSensitive
  public Class<?> getCallerClass(Class<?> sentinelClass, Predicate<Class<?>> callerPredicate) {
    if (sentinelClass == null)
      throw new IllegalArgumentException("sentinelClass cannot be null"); 
    if (callerPredicate == null)
      throw new IllegalArgumentException("callerPredicate cannot be null"); 
    boolean foundSentinel = false;
    Class<?> clazz;
    for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
      if (sentinelClass.equals(clazz)) {
        foundSentinel = true;
      } else if (foundSentinel && callerPredicate.test(clazz)) {
        return clazz;
      } 
    } 
    return null;
  }
  
  @PerformanceSensitive
  public Class<?> getCallerClass(int depth) {
    if (depth < 0)
      throw new IndexOutOfBoundsException(Integer.toString(depth)); 
    if (GET_CALLER_CLASS == null)
      return null; 
    try {
      return (Class)GET_CALLER_CLASS.invoke(null, new Object[] { Integer.valueOf(depth + 1 + JDK_7u25_OFFSET) });
    } catch (Exception e) {
      return null;
    } 
  }
  
  @PerformanceSensitive
  public Class<?> getCallerClass(String fqcn, String pkg) {
    boolean next = false;
    Class<?> clazz;
    for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
      if (fqcn.equals(clazz.getName())) {
        next = true;
      } else if (next && clazz.getName().startsWith(pkg)) {
        return clazz;
      } 
    } 
    return null;
  }
  
  @PerformanceSensitive
  public Class<?> getCallerClass(Class<?> anchor) {
    boolean next = false;
    Class<?> clazz;
    for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
      if (anchor.equals(clazz)) {
        next = true;
      } else if (next) {
        return clazz;
      } 
    } 
    return Object.class;
  }
  
  @PerformanceSensitive
  public Stack<Class<?>> getCurrentStackTrace() {
    if (PrivateSecurityManagerStackTraceUtil.isEnabled())
      return PrivateSecurityManagerStackTraceUtil.getCurrentStackTrace(); 
    Stack<Class<?>> classes = new Stack<>();
    Class<?> clazz;
    for (int i = 1; null != (clazz = getCallerClass(i)); i++)
      classes.push(clazz); 
    return classes;
  }
  
  public StackTraceElement calcLocation(String fqcnOfLogger) {
    if (fqcnOfLogger == null)
      return null; 
    StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
    boolean found = false;
    for (int i = 0; i < stackTrace.length; i++) {
      String className = stackTrace[i].getClassName();
      if (fqcnOfLogger.equals(className)) {
        found = true;
      } else if (found && !fqcnOfLogger.equals(className)) {
        return stackTrace[i];
      } 
    } 
    return null;
  }
  
  public StackTraceElement getStackTraceElement(int depth) {
    StackTraceElement[] elements = (new Throwable()).getStackTrace();
    int i = 0;
    for (StackTraceElement element : elements) {
      if (isValid(element)) {
        if (i == depth)
          return element; 
        i++;
      } 
    } 
    throw new IndexOutOfBoundsException(Integer.toString(depth));
  }
  
  private boolean isValid(StackTraceElement element) {
    if (element.isNativeMethod())
      return false; 
    String cn = element.getClassName();
    if (cn.startsWith("sun.reflect."))
      return false; 
    String mn = element.getMethodName();
    if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance")))
      return false; 
    if (cn.startsWith("jdk.internal.reflect."))
      return false; 
    if (cn.equals("java.lang.Class") && mn.equals("newInstance"))
      return false; 
    if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke"))
      return false; 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\StackLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */