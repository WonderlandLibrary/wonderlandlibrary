package joptsimple.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import joptsimple.ValueConverter;

public final class Reflection {
   private Reflection() {
      throw new UnsupportedOperationException();
   }

   public static ValueConverter findConverter(Class var0) {
      Class var1 = Classes.wrapperOf(var0);
      ValueConverter var2 = valueOfConverter(var1);
      if (var2 != null) {
         return var2;
      } else {
         ValueConverter var3 = constructorConverter(var1);
         if (var3 != null) {
            return var3;
         } else {
            throw new IllegalArgumentException(var0 + " is not a value type");
         }
      }
   }

   private static ValueConverter valueOfConverter(Class var0) {
      try {
         Method var1 = var0.getDeclaredMethod("valueOf", String.class);
         return var0 != false ? new MethodInvokingValueConverter(var1, var0) : null;
      } catch (NoSuchMethodException var2) {
         return null;
      }
   }

   private static ValueConverter constructorConverter(Class var0) {
      try {
         return new ConstructorInvokingValueConverter(var0.getConstructor(String.class));
      } catch (NoSuchMethodException var2) {
         return null;
      }
   }

   public static Object instantiate(Constructor var0, Object... var1) {
      try {
         return var0.newInstance(var1);
      } catch (Exception var3) {
         throw reflectionException(var3);
      }
   }

   public static Object invoke(Method var0, Object... var1) {
      try {
         return var0.invoke((Object)null, var1);
      } catch (Exception var3) {
         throw reflectionException(var3);
      }
   }

   public static Object convertWith(ValueConverter var0, String var1) {
      return var0 == null ? var1 : var0.convert(var1);
   }

   private static RuntimeException reflectionException(Exception var0) {
      if (var0 instanceof IllegalArgumentException) {
         return new ReflectionException(var0);
      } else if (var0 instanceof InvocationTargetException) {
         return new ReflectionException(var0.getCause());
      } else {
         return (RuntimeException)(var0 instanceof RuntimeException ? (RuntimeException)var0 : new ReflectionException(var0));
      }
   }
}
