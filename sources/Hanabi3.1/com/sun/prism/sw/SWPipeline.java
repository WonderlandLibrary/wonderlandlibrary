package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import java.security.AccessController;
import java.util.HashMap;
import java.util.List;

public final class SWPipeline extends GraphicsPipeline {
   private static SWPipeline theInstance;
   private final HashMap factories = new HashMap(1);

   public boolean init() {
      return true;
   }

   private SWPipeline() {
   }

   public static SWPipeline getInstance() {
      if (theInstance == null) {
         theInstance = new SWPipeline();
      }

      return theInstance;
   }

   public int getAdapterOrdinal(Screen var1) {
      return Screen.getScreens().indexOf(var1);
   }

   public ResourceFactory getResourceFactory(Screen var1) {
      Integer var2 = new Integer(var1.getAdapterOrdinal());
      SWResourceFactory var3 = (SWResourceFactory)this.factories.get(var2);
      if (var3 == null) {
         var3 = new SWResourceFactory(var1);
         this.factories.put(var2, var3);
      }

      return var3;
   }

   public ResourceFactory getDefaultResourceFactory(List var1) {
      return this.getResourceFactory(Screen.getMainScreen());
   }

   public boolean is3DSupported() {
      return false;
   }

   public boolean isVsyncSupported() {
      return false;
   }

   public boolean supportsShaderType(GraphicsPipeline.ShaderType var1) {
      return false;
   }

   public boolean supportsShaderModel(GraphicsPipeline.ShaderModel var1) {
      return false;
   }

   public void dispose() {
      super.dispose();
   }

   public boolean isUploading() {
      return true;
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("prism_sw");
         return null;
      });
   }
}
