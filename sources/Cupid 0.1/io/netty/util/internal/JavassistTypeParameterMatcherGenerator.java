package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public final class JavassistTypeParameterMatcherGenerator {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(JavassistTypeParameterMatcherGenerator.class);
  
  private static final ClassPool classPool = new ClassPool(true);
  
  static {
    classPool.appendClassPath((ClassPath)new ClassClassPath(NoOpTypeParameterMatcher.class));
  }
  
  public static void appendClassPath(ClassPath classpath) {
    classPool.appendClassPath(classpath);
  }
  
  public static void appendClassPath(String pathname) throws NotFoundException {
    classPool.appendClassPath(pathname);
  }
  
  public static TypeParameterMatcher generate(Class<?> type) {
    ClassLoader classLoader = PlatformDependent.getContextClassLoader();
    if (classLoader == null)
      classLoader = PlatformDependent.getSystemClassLoader(); 
    return generate(type, classLoader);
  }
  
  public static TypeParameterMatcher generate(Class<?> type, ClassLoader classLoader) {
    String typeName = typeName(type);
    String className = "io.netty.util.internal.__matchers__." + typeName + "Matcher";
    try {
      return (TypeParameterMatcher)Class.forName(className, true, classLoader).newInstance();
    } catch (Exception e) {
      CtClass c = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), className);
      c.setModifiers(c.getModifiers() | 0x10);
      c.getDeclaredMethod("match").setBody("{ return $1 instanceof " + typeName + "; }");
      byte[] byteCode = c.toBytecode();
      c.detach();
      Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
      method.setAccessible(true);
      Class<?> generated = (Class)method.invoke(classLoader, new Object[] { className, byteCode, Integer.valueOf(0), Integer.valueOf(byteCode.length) });
      if (type != Object.class)
        logger.debug("Generated: {}", generated.getName()); 
      return (TypeParameterMatcher)generated.newInstance();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
  
  private static String typeName(Class<?> type) {
    if (type.isArray())
      return typeName(type.getComponentType()) + "[]"; 
    return type.getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\JavassistTypeParameterMatcherGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */