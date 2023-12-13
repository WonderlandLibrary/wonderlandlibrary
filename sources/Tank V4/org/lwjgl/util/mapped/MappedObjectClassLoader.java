package org.lwjgl.util.mapped;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import org.lwjgl.LWJGLUtil;

public class MappedObjectClassLoader extends URLClassLoader {
   static final String MAPPEDOBJECT_PACKAGE_PREFIX = MappedObjectClassLoader.class.getPackage().getName() + ".";
   static boolean FORKED;
   private static long total_time_transforming;

   public static boolean fork(Class var0, String[] var1) {
      if (FORKED) {
         return false;
      } else {
         FORKED = true;

         try {
            MappedObjectClassLoader var2 = new MappedObjectClassLoader(var0);
            var2.loadMappedObject();
            Class var3 = var2.loadClass(var0.getName());
            Method var4 = var3.getMethod("main", String[].class);
            var4.invoke((Object)null, var1);
         } catch (InvocationTargetException var5) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var5.getCause());
         } catch (Throwable var6) {
            throw new Error("failed to fork", var6);
         }

         return true;
      }
   }

   private MappedObjectClassLoader(Class var1) {
      super(((URLClassLoader)var1.getClassLoader()).getURLs());
   }

   protected synchronized Class loadMappedObject() throws ClassNotFoundException {
      String var1 = MappedObject.class.getName();
      String var2 = var1.replace('.', '/');
      byte[] var3 = readStream(this.getResourceAsStream(var2.concat(".class")));
      long var4 = System.nanoTime();
      var3 = MappedObjectTransformer.transformMappedObject(var3);
      long var6 = System.nanoTime();
      total_time_transforming += var6 - var4;
      if (MappedObjectTransformer.PRINT_ACTIVITY) {
         printActivity(var2, var4, var6);
      }

      Class var8 = super.defineClass(var1, var3, 0, var3.length);
      this.resolveClass(var8);
      return var8;
   }

   protected synchronized Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
      if (!var1.startsWith("java.") && !var1.startsWith("javax.") && !var1.startsWith("sun.") && !var1.startsWith("sunw.") && !var1.startsWith("org.objectweb.asm.")) {
         String var3 = var1.replace('.', '/');
         boolean var4 = var1.startsWith(MAPPEDOBJECT_PACKAGE_PREFIX);
         if (!var4 || !var1.equals(MappedObjectClassLoader.class.getName()) && !var1.equals(MappedObjectTransformer.class.getName()) && !var1.equals(CacheUtil.class.getName())) {
            byte[] var5 = readStream(this.getResourceAsStream(var3.concat(".class")));
            if (!var4 || var1.substring(MAPPEDOBJECT_PACKAGE_PREFIX.length()).indexOf(46) != -1) {
               long var6 = System.nanoTime();
               byte[] var8 = MappedObjectTransformer.transformMappedAPI(var3, var5);
               long var9 = System.nanoTime();
               total_time_transforming += var9 - var6;
               if (var5 != var8) {
                  var5 = var8;
                  if (MappedObjectTransformer.PRINT_ACTIVITY) {
                     printActivity(var3, var6, var9);
                  }
               }
            }

            Class var11 = super.defineClass(var1, var5, 0, var5.length);
            if (var2) {
               this.resolveClass(var11);
            }

            return var11;
         } else {
            return super.loadClass(var1, var2);
         }
      } else {
         return super.loadClass(var1, var2);
      }
   }

   private static void printActivity(String var0, long var1, long var3) {
      StringBuilder var5 = new StringBuilder(MappedObjectClassLoader.class.getSimpleName() + ": " + var0);
      if (MappedObjectTransformer.PRINT_TIMING) {
         var5.append("\n\ttransforming took " + (var3 - var1) / 1000L + " micros (total: " + total_time_transforming / 1000L / 1000L + "ms)");
      }

      LWJGLUtil.log(var5);
   }

   private static byte[] readStream(InputStream var0) {
      byte[] var1 = new byte[256];
      int var2 = 0;

      try {
         while(true) {
            if (var1.length == var2) {
               var1 = copyOf(var1, var2 * 2);
            }

            int var3 = var0.read(var1, var2, var1.length - var2);
            if (var3 == -1) {
               break;
            }

            var2 += var3;
         }
      } catch (IOException var8) {
         try {
            var0.close();
         } catch (IOException var6) {
         }

         return copyOf(var1, var2);
      }

      try {
         var0.close();
      } catch (IOException var7) {
      }

      return copyOf(var1, var2);
   }

   private static byte[] copyOf(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }
}
