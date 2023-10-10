package org.apache.commons.lang3.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

public class MethodUtils {
   public static Object invokeMethod(Object var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeMethod(var0, var1, var2, var3);
   }

   public static Object invokeMethod(Object var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var3 = ArrayUtils.nullToEmpty(var3);
      var2 = ArrayUtils.nullToEmpty(var2);
      Method var4 = getMatchingAccessibleMethod(var0.getClass(), var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on object: " + var0.getClass().getName());
      } else {
         return var4.invoke(var0, var2);
      }
   }

   public static Object invokeExactMethod(Object var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeExactMethod(var0, var1, var2, var3);
   }

   public static Object invokeExactMethod(Object var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getAccessibleMethod(var0.getClass(), var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on object: " + var0.getClass().getName());
      } else {
         return var4.invoke(var0, var2);
      }
   }

   public static Object invokeExactStaticMethod(Class var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getAccessibleMethod(var0, var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on class: " + var0.getName());
      } else {
         return var4.invoke((Object)null, var2);
      }
   }

   public static Object invokeStaticMethod(Class var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeStaticMethod(var0, var1, var2, var3);
   }

   public static Object invokeStaticMethod(Class var0, String var1, Object[] var2, Class[] var3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      var3 = ArrayUtils.nullToEmpty(var3);
      Method var4 = getMatchingAccessibleMethod(var0, var1, var3);
      if (var4 == null) {
         throw new NoSuchMethodException("No such accessible method: " + var1 + "() on class: " + var0.getName());
      } else {
         return var4.invoke((Object)null, var2);
      }
   }

   public static Object invokeExactStaticMethod(Class var0, String var1, Object... var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
      var2 = ArrayUtils.nullToEmpty(var2);
      Class[] var3 = ClassUtils.toClass(var2);
      return invokeExactStaticMethod(var0, var1, var2, var3);
   }

   public static Method getAccessibleMethod(Class var0, String var1, Class... var2) {
      try {
         return getAccessibleMethod(var0.getMethod(var1, var2));
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   public static Method getAccessibleMethod(Method var0) {
      if (!MemberUtils.isAccessible(var0)) {
         return null;
      } else {
         Class var1 = var0.getDeclaringClass();
         if (Modifier.isPublic(var1.getModifiers())) {
            return var0;
         } else {
            String var2 = var0.getName();
            Class[] var3 = var0.getParameterTypes();
            var0 = getAccessibleMethodFromInterfaceNest(var1, var2, var3);
            if (var0 == null) {
               var0 = getAccessibleMethodFromSuperclass(var1, var2, var3);
            }

            return var0;
         }
      }
   }

   private static Method getAccessibleMethodFromSuperclass(Class var0, String var1, Class... var2) {
      for(Class var3 = var0.getSuperclass(); var3 != null; var3 = var3.getSuperclass()) {
         if (Modifier.isPublic(var3.getModifiers())) {
            try {
               return var3.getMethod(var1, var2);
            } catch (NoSuchMethodException var5) {
               return null;
            }
         }
      }

      return null;
   }

   private static Method getAccessibleMethodFromInterfaceNest(Class var0, String var1, Class... var2) {
      while(var0 != null) {
         Class[] var3 = var0.getInterfaces();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (Modifier.isPublic(var3[var4].getModifiers())) {
               try {
                  return var3[var4].getDeclaredMethod(var1, var2);
               } catch (NoSuchMethodException var6) {
                  Method var5 = getAccessibleMethodFromInterfaceNest(var3[var4], var1, var2);
                  if (var5 != null) {
                     return var5;
                  }
               }
            }
         }

         var0 = var0.getSuperclass();
      }

      return null;
   }

   public static Method getMatchingAccessibleMethod(Class var0, String var1, Class... var2) {
      Method var3;
      try {
         var3 = var0.getMethod(var1, var2);
         MemberUtils.setAccessibleWorkaround(var3);
         return var3;
      } catch (NoSuchMethodException var10) {
         var3 = null;
         Method[] var4 = var0.getMethods();
         Method[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method var8 = var5[var7];
            if (var8.getName().equals(var1) && ClassUtils.isAssignable(var2, var8.getParameterTypes(), true)) {
               Method var9 = getAccessibleMethod(var8);
               if (var9 != null && (var3 == null || MemberUtils.compareParameterTypes(var9.getParameterTypes(), var3.getParameterTypes(), var2) < 0)) {
                  var3 = var9;
               }
            }
         }

         if (var3 != null) {
            MemberUtils.setAccessibleWorkaround(var3);
         }

         return var3;
      }
   }

   public static Set getOverrideHierarchy(Method var0, ClassUtils.Interfaces var1) {
      Validate.notNull(var0);
      LinkedHashSet var2 = new LinkedHashSet();
      var2.add(var0);
      Class[] var3 = var0.getParameterTypes();
      Class var4 = var0.getDeclaringClass();
      Iterator var5 = ClassUtils.hierarchy(var4, var1).iterator();
      var5.next();

      while(true) {
         label32:
         while(true) {
            Method var7;
            do {
               if (!var5.hasNext()) {
                  return var2;
               }

               Class var6 = (Class)var5.next();
               var7 = getMatchingAccessibleMethod(var6, var0.getName(), var3);
            } while(var7 == null);

            if (Arrays.equals(var7.getParameterTypes(), var3)) {
               var2.add(var7);
            } else {
               Map var8 = TypeUtils.getTypeArguments(var4, var7.getDeclaringClass());

               for(int var9 = 0; var9 < var3.length; ++var9) {
                  Type var10 = TypeUtils.unrollVariables(var8, var0.getGenericParameterTypes()[var9]);
                  Type var11 = TypeUtils.unrollVariables(var8, var7.getGenericParameterTypes()[var9]);
                  if (!TypeUtils.equals((Type)var10, var11)) {
                     continue label32;
                  }
               }

               var2.add(var7);
            }
         }
      }
   }
}
