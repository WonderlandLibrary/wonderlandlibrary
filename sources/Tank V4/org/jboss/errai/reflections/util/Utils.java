package org.jboss.errai.reflections.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.jboss.errai.reflections.ReflectionUtils;
import org.jboss.errai.reflections.ReflectionsException;

public abstract class Utils {
   public static String repeat(String var0, int var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(var0);
      }

      return var2.toString();
   }

   public static File prepareFile(String var0) {
      File var1 = new File(var0);
      File var2 = var1.getAbsoluteFile().getParentFile();
      if (!var2.exists()) {
         var2.mkdirs();
      }

      return var1;
   }

   public static Method getMethodFromDescriptor(String var0) throws ReflectionsException {
      int var1 = var0.indexOf(40);
      String var2 = var0.substring(0, var1);
      String var3 = var0.substring(var1 + 1, var0.length() - 1);
      int var4 = var2.lastIndexOf(46);
      String var5 = var2.substring(var2.lastIndexOf(32) + 1, var4);
      String var6 = var2.substring(var4 + 1);
      Class[] var7 = null;
      if (var3 != null) {
         String[] var8 = var3.split(", ");
         ArrayList var9 = new ArrayList(var8.length);
         String[] var10 = var8;
         int var11 = var8.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            String var13 = var10[var12];
            var9.add(ReflectionUtils.forName(var13));
         }

         var7 = (Class[])var9.toArray(new Class[var9.size()]);
      }

      Class var15 = ReflectionUtils.forName(var5);

      try {
         return var0.contains("<init>") ? null : var15.getDeclaredMethod(var6, var7);
      } catch (NoSuchMethodException var14) {
         throw new ReflectionsException("Can't resolve method named " + var6, var14);
      }
   }

   public static Field getFieldFromString(String var0) {
      String var1 = var0.substring(0, var0.lastIndexOf(46));
      String var2 = var0.substring(var0.lastIndexOf(46) + 1);

      try {
         return ReflectionUtils.forName(var1).getDeclaredField(var2);
      } catch (NoSuchFieldException var4) {
         throw new ReflectionsException("Can't resolve field named " + var2, var4);
      }
   }

   public static void close(InputStream var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
