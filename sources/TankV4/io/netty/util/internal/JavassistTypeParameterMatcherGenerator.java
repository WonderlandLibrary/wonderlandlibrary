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

   public static void appendClassPath(ClassPath var0) {
      classPool.appendClassPath(var0);
   }

   public static void appendClassPath(String var0) throws NotFoundException {
      classPool.appendClassPath(var0);
   }

   public static TypeParameterMatcher generate(Class var0) {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      if (var1 == null) {
         var1 = ClassLoader.getSystemClassLoader();
      }

      return generate(var0, var1);
   }

   public static TypeParameterMatcher generate(Class var0, ClassLoader var1) {
      String var2 = typeName(var0);
      String var3 = "io.netty.util.internal.__matchers__." + var2 + "Matcher";

      try {
         try {
            return (TypeParameterMatcher)Class.forName(var3, true, var1).newInstance();
         } catch (Exception var8) {
            CtClass var4 = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), var3);
            var4.setModifiers(var4.getModifiers() | 16);
            var4.getDeclaredMethod("match").setBody("{ return $1 instanceof " + var2 + "; }");
            byte[] var5 = var4.toBytecode();
            var4.detach();
            Method var6 = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            var6.setAccessible(true);
            Class var7 = (Class)var6.invoke(var1, var3, var5, 0, var5.length);
            if (var0 != Object.class) {
               logger.debug("Generated: {}", (Object)var7.getName());
            }

            return (TypeParameterMatcher)var7.newInstance();
         }
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new RuntimeException(var10);
      }
   }

   private static String typeName(Class var0) {
      return var0.isArray() ? typeName(var0.getComponentType()) + "[]" : var0.getName();
   }

   private JavassistTypeParameterMatcherGenerator() {
   }

   static {
      classPool.appendClassPath((ClassPath)(new ClassClassPath(NoOpTypeParameterMatcher.class)));
   }
}
