package javassist.util.proxy;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.lang.reflect.Method;

public class RuntimeSupport {
   public static MethodHandler default_interceptor = new RuntimeSupport.DefaultMethodHandler();

   public static void find2Methods(Class var0, String var1, String var2, int var3, String var4, Method[] var5) {
      var5[var3 + 1] = var2 == null ? null : findMethod(var0, var2, var4);
      var5[var3] = findSuperClassMethod(var0, var1, var4);
   }

   /** @deprecated */
   public static void find2Methods(Object var0, String var1, String var2, int var3, String var4, Method[] var5) {
      var5[var3 + 1] = var2 == null ? null : findMethod(var0, var2, var4);
      var5[var3] = findSuperMethod(var0, var1, var4);
   }

   /** @deprecated */
   public static Method findMethod(Object var0, String var1, String var2) {
      Method var3 = findMethod2(var0.getClass(), var1, var2);
      if (var3 == null) {
         error(var0.getClass(), var1, var2);
      }

      return var3;
   }

   public static Method findMethod(Class var0, String var1, String var2) {
      Method var3 = findMethod2(var0, var1, var2);
      if (var3 == null) {
         error(var0, var1, var2);
      }

      return var3;
   }

   public static Method findSuperMethod(Object var0, String var1, String var2) {
      Class var3 = var0.getClass();
      return findSuperClassMethod(var3, var1, var2);
   }

   public static Method findSuperClassMethod(Class var0, String var1, String var2) {
      Method var3 = findSuperMethod2(var0.getSuperclass(), var1, var2);
      if (var3 == null) {
         var3 = searchInterfaces(var0, var1, var2);
      }

      if (var3 == null) {
         error(var0, var1, var2);
      }

      return var3;
   }

   private static void error(Class var0, String var1, String var2) {
      throw new RuntimeException("not found " + var1 + ":" + var2 + " in " + var0.getName());
   }

   private static Method findSuperMethod2(Class var0, String var1, String var2) {
      Method var3 = findMethod2(var0, var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         Class var4 = var0.getSuperclass();
         if (var4 != null) {
            var3 = findSuperMethod2(var4, var1, var2);
            if (var3 != null) {
               return var3;
            }
         }

         return searchInterfaces(var0, var1, var2);
      }
   }

   private static Method searchInterfaces(Class var0, String var1, String var2) {
      Method var3 = null;
      Class[] var4 = var0.getInterfaces();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var3 = findSuperMethod2(var4[var5], var1, var2);
         if (var3 != null) {
            return var3;
         }
      }

      return var3;
   }

   private static Method findMethod2(Class var0, String var1, String var2) {
      Method[] var3 = SecurityActions.getDeclaredMethods(var0);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         if (var3[var5].getName().equals(var1) && makeDescriptor(var3[var5]).equals(var2)) {
            return var3[var5];
         }
      }

      return null;
   }

   public static String makeDescriptor(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      return makeDescriptor(var1, var0.getReturnType());
   }

   public static String makeDescriptor(Class[] var0, Class var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append('(');

      for(int var3 = 0; var3 < var0.length; ++var3) {
         makeDesc(var2, var0[var3]);
      }

      var2.append(')');
      if (var1 != null) {
         makeDesc(var2, var1);
      }

      return var2.toString();
   }

   public static String makeDescriptor(String var0, Class var1) {
      StringBuffer var2 = new StringBuffer(var0);
      makeDesc(var2, var1);
      return var2.toString();
   }

   private static void makeDesc(StringBuffer var0, Class var1) {
      if (var1.isArray()) {
         var0.append('[');
         makeDesc(var0, var1.getComponentType());
      } else if (var1.isPrimitive()) {
         if (var1 == Void.TYPE) {
            var0.append('V');
         } else if (var1 == Integer.TYPE) {
            var0.append('I');
         } else if (var1 == Byte.TYPE) {
            var0.append('B');
         } else if (var1 == Long.TYPE) {
            var0.append('J');
         } else if (var1 == Double.TYPE) {
            var0.append('D');
         } else if (var1 == Float.TYPE) {
            var0.append('F');
         } else if (var1 == Character.TYPE) {
            var0.append('C');
         } else if (var1 == Short.TYPE) {
            var0.append('S');
         } else {
            if (var1 != Boolean.TYPE) {
               throw new RuntimeException("bad type: " + var1.getName());
            }

            var0.append('Z');
         }
      } else {
         var0.append('L').append(var1.getName().replace('.', '/')).append(';');
      }

   }

   public static SerializedProxy makeSerializedProxy(Object var0) throws InvalidClassException {
      Class var1 = var0.getClass();
      MethodHandler var2 = null;
      if (var0 instanceof ProxyObject) {
         var2 = ((ProxyObject)var0).getHandler();
      } else if (var0 instanceof Proxy) {
         var2 = ProxyFactory.getHandler((Proxy)var0);
      }

      return new SerializedProxy(var1, ProxyFactory.getFilterSignature(var1), var2);
   }

   static class DefaultMethodHandler implements MethodHandler, Serializable {
      public Object invoke(Object var1, Method var2, Method var3, Object[] var4) throws Exception {
         return var3.invoke(var1, var4);
      }
   }
}
