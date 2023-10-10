package org.apache.commons.lang3.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class FieldUtils {
   public static Field getField(Class var0, String var1) {
      Field var2 = getField(var0, var1, false);
      MemberUtils.setAccessibleWorkaround(var2);
      return var2;
   }

   public static Field getField(Class var0, String var1, boolean var2) {
      Validate.isTrue(var0 != null, "The class must not be null");
      Validate.isTrue(StringUtils.isNotBlank(var1), "The field name must not be blank/empty");

      for(Class var3 = var0; var3 != null; var3 = var3.getSuperclass()) {
         try {
            Field var4 = var3.getDeclaredField(var1);
            if (!Modifier.isPublic(var4.getModifiers())) {
               if (!var2) {
                  continue;
               }

               var4.setAccessible(true);
            }

            return var4;
         } catch (NoSuchFieldException var8) {
         }
      }

      Field var9 = null;
      Iterator var10 = ClassUtils.getAllInterfaces(var0).iterator();

      while(var10.hasNext()) {
         Class var5 = (Class)var10.next();

         try {
            Field var6 = var5.getField(var1);
            Validate.isTrue(var9 == null, "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", var1, var0);
            var9 = var6;
         } catch (NoSuchFieldException var7) {
         }
      }

      return var9;
   }

   public static Field getDeclaredField(Class var0, String var1) {
      return getDeclaredField(var0, var1, false);
   }

   public static Field getDeclaredField(Class var0, String var1, boolean var2) {
      Validate.isTrue(var0 != null, "The class must not be null");
      Validate.isTrue(StringUtils.isNotBlank(var1), "The field name must not be blank/empty");

      try {
         Field var3 = var0.getDeclaredField(var1);
         if (!MemberUtils.isAccessible(var3)) {
            if (!var2) {
               return null;
            }

            var3.setAccessible(true);
         }

         return var3;
      } catch (NoSuchFieldException var4) {
         return null;
      }
   }

   public static Field[] getAllFields(Class var0) {
      List var1 = getAllFieldsList(var0);
      return (Field[])var1.toArray(new Field[var1.size()]);
   }

   public static List getAllFieldsList(Class var0) {
      Validate.isTrue(var0 != null, "The class must not be null");
      ArrayList var1 = new ArrayList();

      for(Class var2 = var0; var2 != null; var2 = var2.getSuperclass()) {
         Field[] var3 = var2.getDeclaredFields();
         Field[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field var7 = var4[var6];
            var1.add(var7);
         }
      }

      return var1;
   }

   public static Object readStaticField(Field var0) throws IllegalAccessException {
      return readStaticField(var0, false);
   }

   public static Object readStaticField(Field var0, boolean var1) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "The field must not be null");
      Validate.isTrue(Modifier.isStatic(var0.getModifiers()), "The field '%s' is not static", var0.getName());
      return readField(var0, (Object)null, var1);
   }

   public static Object readStaticField(Class var0, String var1) throws IllegalAccessException {
      return readStaticField(var0, var1, false);
   }

   public static Object readStaticField(Class var0, String var1, boolean var2) throws IllegalAccessException {
      Field var3 = getField(var0, var1, var2);
      Validate.isTrue(var3 != null, "Cannot locate field '%s' on %s", var1, var0);
      return readStaticField(var3, false);
   }

   public static Object readDeclaredStaticField(Class var0, String var1) throws IllegalAccessException {
      return readDeclaredStaticField(var0, var1, false);
   }

   public static Object readDeclaredStaticField(Class var0, String var1, boolean var2) throws IllegalAccessException {
      Field var3 = getDeclaredField(var0, var1, var2);
      Validate.isTrue(var3 != null, "Cannot locate declared field %s.%s", var0.getName(), var1);
      return readStaticField(var3, false);
   }

   public static Object readField(Field var0, Object var1) throws IllegalAccessException {
      return readField(var0, var1, false);
   }

   public static Object readField(Field var0, Object var1, boolean var2) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "The field must not be null");
      if (var2 && !var0.isAccessible()) {
         var0.setAccessible(true);
      } else {
         MemberUtils.setAccessibleWorkaround(var0);
      }

      return var0.get(var1);
   }

   public static Object readField(Object var0, String var1) throws IllegalAccessException {
      return readField(var0, var1, false);
   }

   public static Object readField(Object var0, String var1, boolean var2) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "target object must not be null");
      Class var3 = var0.getClass();
      Field var4 = getField(var3, var1, var2);
      Validate.isTrue(var4 != null, "Cannot locate field %s on %s", var1, var3);
      return readField(var4, var0, false);
   }

   public static Object readDeclaredField(Object var0, String var1) throws IllegalAccessException {
      return readDeclaredField(var0, var1, false);
   }

   public static Object readDeclaredField(Object var0, String var1, boolean var2) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "target object must not be null");
      Class var3 = var0.getClass();
      Field var4 = getDeclaredField(var3, var1, var2);
      Validate.isTrue(var4 != null, "Cannot locate declared field %s.%s", var3, var1);
      return readField(var4, var0, false);
   }

   public static void writeStaticField(Field var0, Object var1) throws IllegalAccessException {
      writeStaticField(var0, var1, false);
   }

   public static void writeStaticField(Field var0, Object var1, boolean var2) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "The field must not be null");
      Validate.isTrue(Modifier.isStatic(var0.getModifiers()), "The field %s.%s is not static", var0.getDeclaringClass().getName(), var0.getName());
      writeField(var0, (Object)null, var1, var2);
   }

   public static void writeStaticField(Class var0, String var1, Object var2) throws IllegalAccessException {
      writeStaticField(var0, var1, var2, false);
   }

   public static void writeStaticField(Class var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Field var4 = getField(var0, var1, var3);
      Validate.isTrue(var4 != null, "Cannot locate field %s on %s", var1, var0);
      writeStaticField(var4, var2, false);
   }

   public static void writeDeclaredStaticField(Class var0, String var1, Object var2) throws IllegalAccessException {
      writeDeclaredStaticField(var0, var1, var2, false);
   }

   public static void writeDeclaredStaticField(Class var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Field var4 = getDeclaredField(var0, var1, var3);
      Validate.isTrue(var4 != null, "Cannot locate declared field %s.%s", var0.getName(), var1);
      writeField(var4, (Object)null, var2, false);
   }

   public static void writeField(Field var0, Object var1, Object var2) throws IllegalAccessException {
      writeField(var0, var1, var2, false);
   }

   public static void writeField(Field var0, Object var1, Object var2, boolean var3) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "The field must not be null");
      if (var3 && !var0.isAccessible()) {
         var0.setAccessible(true);
      } else {
         MemberUtils.setAccessibleWorkaround(var0);
      }

      var0.set(var1, var2);
   }

   public static void removeFinalModifier(Field var0) {
      removeFinalModifier(var0, true);
   }

   public static void removeFinalModifier(Field var0, boolean var1) {
      Validate.isTrue(var0 != null, "The field must not be null");

      try {
         if (Modifier.isFinal(var0.getModifiers())) {
            Field var2 = Field.class.getDeclaredField("modifiers");
            boolean var3 = var1 && !var2.isAccessible();
            if (var3) {
               var2.setAccessible(true);
            }

            var2.setInt(var0, var0.getModifiers() & -17);
            if (var3) {
               var2.setAccessible(false);
            }
         }
      } catch (NoSuchFieldException var5) {
      } catch (IllegalAccessException var6) {
      }

   }

   public static void writeField(Object var0, String var1, Object var2) throws IllegalAccessException {
      writeField(var0, var1, var2, false);
   }

   public static void writeField(Object var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "target object must not be null");
      Class var4 = var0.getClass();
      Field var5 = getField(var4, var1, var3);
      Validate.isTrue(var5 != null, "Cannot locate declared field %s.%s", var4.getName(), var1);
      writeField(var5, var0, var2, false);
   }

   public static void writeDeclaredField(Object var0, String var1, Object var2) throws IllegalAccessException {
      writeDeclaredField(var0, var1, var2, false);
   }

   public static void writeDeclaredField(Object var0, String var1, Object var2, boolean var3) throws IllegalAccessException {
      Validate.isTrue(var0 != null, "target object must not be null");
      Class var4 = var0.getClass();
      Field var5 = getDeclaredField(var4, var1, var3);
      Validate.isTrue(var5 != null, "Cannot locate declared field %s.%s", var4.getName(), var1);
      writeField(var5, var0, var2, false);
   }
}
