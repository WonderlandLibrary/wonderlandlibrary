package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GraphicsPipeline {
   private FontFactory fontFactory;
   private final Set disposeHooks = new HashSet();
   protected Map deviceDetails = null;
   private static GraphicsPipeline installedPipeline;

   public abstract boolean init();

   public void dispose() {
      this.notifyDisposeHooks();
      installedPipeline = null;
   }

   public void addDisposeHook(Runnable var1) {
      if (var1 != null) {
         synchronized(this.disposeHooks) {
            this.disposeHooks.add(var1);
         }
      }
   }

   private void notifyDisposeHooks() {
      ArrayList var1;
      synchronized(this.disposeHooks) {
         var1 = new ArrayList(this.disposeHooks);
         this.disposeHooks.clear();
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Runnable var3 = (Runnable)var2.next();
         var3.run();
      }

   }

   public abstract int getAdapterOrdinal(Screen var1);

   public abstract ResourceFactory getResourceFactory(Screen var1);

   public abstract ResourceFactory getDefaultResourceFactory(List var1);

   public abstract boolean is3DSupported();

   public boolean isMSAASupported() {
      return false;
   }

   public abstract boolean isVsyncSupported();

   public abstract boolean supportsShaderType(ShaderType var1);

   public abstract boolean supportsShaderModel(ShaderModel var1);

   public boolean supportsShader(ShaderType var1, ShaderModel var2) {
      return this.supportsShaderType(var1) && this.supportsShaderModel(var2);
   }

   public static ResourceFactory getDefaultResourceFactory() {
      List var0 = Screen.getScreens();
      return getPipeline().getDefaultResourceFactory(var0);
   }

   public FontFactory getFontFactory() {
      if (this.fontFactory == null) {
         this.fontFactory = PrismFontFactory.getFontFactory();
      }

      return this.fontFactory;
   }

   public Map getDeviceDetails() {
      return this.deviceDetails;
   }

   protected void setDeviceDetails(Map var1) {
      this.deviceDetails = var1;
   }

   public static GraphicsPipeline createPipeline() {
      if (PrismSettings.tryOrder.isEmpty()) {
         if (PrismSettings.verbose) {
            System.out.println("No Prism pipelines specified");
         }

         return null;
      } else if (installedPipeline != null) {
         throw new IllegalStateException("pipeline already created:" + installedPipeline);
      } else {
         Iterator var0 = PrismSettings.tryOrder.iterator();

         while(var0.hasNext()) {
            String var1 = (String)var0.next();
            if ("j2d".equals(var1)) {
               System.err.println("WARNING: The prism-j2d pipeline should not be used as the software");
               System.err.println("fallback pipeline. It is no longer tested nor intended to be used for");
               System.err.println("on-screen rendering. Please use the prism-sw pipeline instead by setting");
               System.err.println("the \"prism.order\" system property to \"sw\" rather than \"j2d\".");
            }

            if (PrismSettings.verbose && ("j2d".equals(var1) || "sw".equals(var1))) {
               System.err.println("*** Fallback to Prism SW pipeline");
            }

            String var2 = "com.sun.prism." + var1 + "." + var1.toUpperCase() + "Pipeline";

            try {
               if (PrismSettings.verbose) {
                  System.out.println("Prism pipeline name = " + var2);
               }

               Class var3 = Class.forName(var2);
               if (PrismSettings.verbose) {
                  System.out.println("(X) Got class = " + var3);
               }

               Method var4 = var3.getMethod("getInstance", (Class[])null);
               GraphicsPipeline var5 = (GraphicsPipeline)var4.invoke((Object)null, (Object[])null);
               if (var5 != null && var5.init()) {
                  if (PrismSettings.verbose) {
                     System.out.println("Initialized prism pipeline: " + var3.getName());
                  }

                  installedPipeline = var5;
                  return installedPipeline;
               }

               if (var5 != null) {
                  var5.dispose();
                  var5 = null;
               }

               if (PrismSettings.verbose) {
                  System.err.println("GraphicsPipeline.createPipeline: error initializing pipeline " + var2);
               }
            } catch (Throwable var6) {
               if (PrismSettings.verbose) {
                  System.err.println("GraphicsPipeline.createPipeline failed for " + var2);
                  var6.printStackTrace();
               }
            }
         }

         StringBuffer var7 = new StringBuffer("Graphics Device initialization failed for :  ");
         Iterator var8 = PrismSettings.tryOrder.iterator();
         if (var8.hasNext()) {
            var7.append((String)var8.next());

            while(var8.hasNext()) {
               var7.append(", ");
               var7.append((String)var8.next());
            }
         }

         System.err.println(var7);
         return null;
      }
   }

   public static GraphicsPipeline getPipeline() {
      return installedPipeline;
   }

   public boolean isEffectSupported() {
      return true;
   }

   public boolean isUploading() {
      return PrismSettings.forceUploadingPainter;
   }

   public static enum ShaderModel {
      SM3;
   }

   public static enum ShaderType {
      HLSL,
      GLSL;
   }
}
