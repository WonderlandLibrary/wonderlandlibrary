package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import java.util.HashMap;
import java.util.List;

public class J2DPipeline extends GraphicsPipeline {
   private static J2DPipeline theInstance;
   private final HashMap factories = new HashMap(1);
   private FontFactory j2DFontFactory;

   public boolean init() {
      return true;
   }

   private J2DPipeline() {
   }

   public static J2DPipeline getInstance() {
      if (theInstance == null) {
         theInstance = new J2DPipeline();
      }

      return theInstance;
   }

   public int getAdapterOrdinal(Screen var1) {
      return Screen.getScreens().indexOf(var1);
   }

   public ResourceFactory getResourceFactory(Screen var1) {
      Integer var2 = new Integer(var1.getAdapterOrdinal());
      J2DResourceFactory var3 = (J2DResourceFactory)this.factories.get(var2);
      if (var3 == null) {
         var3 = new J2DResourceFactory(var1);
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

   public FontFactory getFontFactory() {
      if (this.j2DFontFactory == null) {
         FontFactory var1 = super.getFontFactory();
         this.j2DFontFactory = new J2DFontFactory(var1);
      }

      return this.j2DFontFactory;
   }

   public boolean isUploading() {
      return true;
   }
}
