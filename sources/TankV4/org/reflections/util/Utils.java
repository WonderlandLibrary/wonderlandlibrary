package org.reflections.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Utils {
   public static String repeat(String var0, int var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(var0);
      }

      return var2.toString();
   }

   public static boolean isEmpty(Object[] var0) {
      return var0 == null || var0.length == 0;
   }

   public static File prepareFile(String var0) {
      File var1 = new File(var0);
      File var2 = var1.getAbsoluteFile().getParentFile();
      if (!var2.exists()) {
         var2.mkdirs();
      }

      return var1;
   }

   public static Member getMemberFromDescriptor(String var0, ClassLoader... var1) throws ReflectionsException {
      int var2 = var0.lastIndexOf(40);
      String var3 = var2 != -1 ? var0.substring(0, var2) : var0;
      String var4 = var2 != -1 ? var0.substring(var2 + 1, var0.lastIndexOf(41)) : "";
      int var5 = Math.max(var3.lastIndexOf(46), var3.lastIndexOf("$"));
      String var6 = var3.substring(var3.lastIndexOf(32) + 1, var5);
      String var7 = var3.substring(var5 + 1);
      Class[] var8 = null;
      if (var4 != null) {
         String[] var9 = var4.split(",");
         ArrayList var10 = new ArrayList(var9.length);
         String[] var11 = var9;
         int var12 = var9.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            String var14 = var11[var13];
            var10.add(ReflectionUtils.forName(var14.trim(), var1));
         }

         var8 = (Class[])var10.toArray(new Class[var10.size()]);
      }

      Class var16 = ReflectionUtils.forName(var6, var1);

      while(var16 != null) {
         try {
            if (var0.contains("(")) {
               if (!isConstructor(var0)) {
                  return var16.isInterface() ? var16.getMethod(var7, var8) : var16.getDeclaredMethod(var7, var8);
               }

               return var16.isInterface() ? var16.getConstructor(var8) : var16.getDeclaredConstructor(var8);
            }

            return var16.isInterface() ? var16.getField(var7) : var16.getDeclaredField(var7);
         } catch (Exception var15) {
            var16 = var16.getSuperclass();
         }
      }

      throw new ReflectionsException("Can't resolve member named " + var7 + " for class " + var6);
   }

   public static Set getMethodsFromDescriptors(Iterable var0, ClassLoader... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!isConstructor(var4)) {
            Method var5 = (Method)getMemberFromDescriptor(var4, var1);
            if (var5 != null) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   public static Set getConstructorsFromDescriptors(Iterable var0, ClassLoader... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (isConstructor(var4)) {
            Constructor var5 = (Constructor)getMemberFromDescriptor(var4, var1);
            if (var5 != null) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   public static Set getMembersFromDescriptors(Iterable var0, ClassLoader... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         try {
            var2.add(getMemberFromDescriptor(var4, var1));
         } catch (ReflectionsException var6) {
            throw new ReflectionsException("Can't resolve member named " + var4, var6);
         }
      }

      return var2;
   }

   public static Field getFieldFromString(String var0, ClassLoader... var1) {
      String var2 = var0.substring(0, var0.lastIndexOf(46));
      String var3 = var0.substring(var0.lastIndexOf(46) + 1);

      try {
         return ReflectionUtils.forName(var2, var1).getDeclaredField(var3);
      } catch (NoSuchFieldException var5) {
         throw new ReflectionsException("Can't resolve field named " + var3, var5);
      }
   }

   public static void close(InputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         if (Reflections.log != null) {
            Reflections.log.warn("Could not close InputStream", var2);
         }
      }

   }

   @Nullable
   public static Logger findLogger(Class var0) {
      try {
         Class.forName("org.slf4j.impl.StaticLoggerBinder");
         return LoggerFactory.getLogger(var0);
      } catch (Throwable var2) {
         return null;
      }
   }

   public static boolean isConstructor(String var0) {
      return var0.contains("init>");
   }

   public static String name(Class var0) {
      if (!var0.isArray()) {
         return var0.getName();
      } else {
         int var1;
         for(var1 = 0; var0.isArray(); var0 = var0.getComponentType()) {
            ++var1;
         }

         return var0.getName() + repeat("[]", var1);
      }
   }

   public static List names(Iterable var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         var1.add(name(var3));
      }

      return var1;
   }

   public static List names(Class... var0) {
      return names((Iterable)Arrays.asList(var0));
   }

   public static String name(Constructor var0) {
      return var0.getName() + "." + "<init>" + "(" + Joiner.on(",").join((Iterable)names(var0.getParameterTypes())) + ")";
   }

   public static String name(Method var0) {
      return var0.getDeclaringClass().getName() + "." + var0.getName() + "(" + Joiner.on(", ").join((Iterable)names(var0.getParameterTypes())) + ")";
   }

   public static String name(Field var0) {
      return var0.getDeclaringClass().getName() + "." + var0.getName();
   }
}
