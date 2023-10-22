package com.sun.scenario.effect.impl;

import com.sun.javafx.PlatformUtil;
import com.sun.scenario.effect.FilterContext;
import java.lang.reflect.Method;
import java.security.AccessController;

class RendererFactory {
   private static String rootPkg = "com.sun.scenario.effect";
   private static boolean tryRSL = true;
   private static boolean trySIMD = false;
   private static boolean tryJOGL = PlatformUtil.isMac();
   private static boolean tryPrism = true;

   private static boolean isRSLFriendly(Class var0) {
      if (var0.getName().equals("sun.java2d.pipe.hw.AccelGraphicsConfig")) {
         return true;
      } else {
         boolean var1 = false;
         Class[] var2 = var0.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class var5 = var2[var4];
            if (isRSLFriendly(var5)) {
               var1 = true;
               break;
            }
         }

         return var1;
      }
   }

   private static boolean isRSLAvailable(FilterContext var0) {
      return isRSLFriendly(var0.getReferent().getClass());
   }

   private static Renderer createRSLRenderer(FilterContext var0) {
      try {
         Class var1 = Class.forName(rootPkg + ".impl.j2d.rsl.RSLRenderer");
         Method var2 = var1.getMethod("createRenderer", FilterContext.class);
         return (Renderer)var2.invoke((Object)null, var0);
      } catch (Throwable var3) {
         return null;
      }
   }

   private static Renderer createJOGLRenderer(FilterContext var0) {
      if (tryJOGL) {
         try {
            Class var1 = Class.forName(rootPkg + ".impl.j2d.jogl.JOGLRenderer");
            Method var2 = var1.getMethod("createRenderer", FilterContext.class);
            return (Renderer)var2.invoke((Object)null, var0);
         } catch (Throwable var3) {
         }
      }

      return null;
   }

   private static Renderer createPrismRenderer(FilterContext var0) {
      if (tryPrism) {
         try {
            Class var1 = Class.forName(rootPkg + ".impl.prism.PrRenderer");
            Method var2 = var1.getMethod("createRenderer", FilterContext.class);
            return (Renderer)var2.invoke((Object)null, var0);
         } catch (Throwable var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   private static Renderer getSSERenderer() {
      if (trySIMD) {
         try {
            Class var0 = Class.forName(rootPkg + ".impl.j2d.J2DSWRenderer");
            Method var1 = var0.getMethod("getSSEInstance", (Class[])null);
            Renderer var2 = (Renderer)var1.invoke((Object)null, (Object[])null);
            if (var2 != null) {
               return var2;
            }
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

         trySIMD = false;
      }

      return null;
   }

   private static Renderer getJavaRenderer() {
      try {
         Class var0 = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
         Class var1 = Class.forName("com.sun.glass.ui.Screen");
         Method var2 = var0.getMethod("createJSWInstance", var1);
         Renderer var3 = (Renderer)var2.invoke((Object)null, null);
         if (var3 != null) {
            return var3;
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      return null;
   }

   private static Renderer getJavaRenderer(FilterContext var0) {
      try {
         Class var1 = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
         Method var2 = var1.getMethod("createJSWInstance", FilterContext.class);
         Renderer var3 = (Renderer)var2.invoke((Object)null, var0);
         if (var3 != null) {
            return var3;
         }
      } catch (Throwable var4) {
      }

      return null;
   }

   static Renderer getSoftwareRenderer() {
      Renderer var0 = getSSERenderer();
      if (var0 == null) {
         var0 = getJavaRenderer();
      }

      return var0;
   }

   static Renderer createRenderer(FilterContext var0) {
      return (Renderer)AccessController.doPrivileged(() -> {
         Renderer var1 = null;
         String var2 = var0.getClass().getName();
         String var3 = var2.substring(var2.lastIndexOf(".") + 1);
         if (var3.equals("PrFilterContext") && tryPrism) {
            var1 = createPrismRenderer(var0);
         }

         if (var1 == null && tryRSL && isRSLAvailable(var0)) {
            var1 = createRSLRenderer(var0);
         }

         if (var1 == null && tryJOGL) {
            var1 = createJOGLRenderer(var0);
         }

         if (var1 == null && trySIMD) {
            var1 = getSSERenderer();
         }

         if (var1 == null) {
            var1 = getJavaRenderer(var0);
         }

         return var1;
      });
   }

   static {
      try {
         if ("false".equals(System.getProperty("decora.rsl"))) {
            tryRSL = false;
         }

         if ("false".equals(System.getProperty("decora.simd"))) {
            trySIMD = false;
         }

         String var0 = System.getProperty("decora.jogl");
         if (var0 != null) {
            tryJOGL = Boolean.parseBoolean(var0);
         }

         if ("false".equals(System.getProperty("decora.prism"))) {
            tryPrism = false;
         }
      } catch (SecurityException var1) {
      }

   }
}
