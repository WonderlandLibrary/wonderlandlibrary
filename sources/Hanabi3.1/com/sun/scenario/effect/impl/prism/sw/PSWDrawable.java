package com.sun.scenario.effect.impl.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import java.nio.IntBuffer;
import java.util.Arrays;

public class PSWDrawable extends PrDrawable implements HeapImage {
   private RTTexture rtt;
   private Image image;
   private boolean heapDirty;
   private boolean vramDirty;

   private PSWDrawable(RTTexture var1, boolean var2) {
      super(var1);
      this.rtt = var1;
      this.vramDirty = var2;
   }

   public static PSWDrawable create(RTTexture var0) {
      return new PSWDrawable(var0, true);
   }

   static int getCompatibleWidth(Screen var0, int var1) {
      ResourceFactory var2 = GraphicsPipeline.getPipeline().getResourceFactory(var0);
      return var2.getRTTWidth(var1, Texture.WrapMode.CLAMP_TO_ZERO);
   }

   static int getCompatibleHeight(Screen var0, int var1) {
      ResourceFactory var2 = GraphicsPipeline.getPipeline().getResourceFactory(var0);
      return var2.getRTTHeight(var1, Texture.WrapMode.CLAMP_TO_ZERO);
   }

   static PSWDrawable create(Screen var0, int var1, int var2) {
      ResourceFactory var3 = GraphicsPipeline.getPipeline().getResourceFactory(var0);
      RTTexture var4 = var3.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_TO_ZERO);
      return new PSWDrawable(var4, false);
   }

   public boolean isLost() {
      return this.rtt == null || this.rtt.isSurfaceLost();
   }

   public void flush() {
      if (this.rtt != null) {
         this.rtt.dispose();
         this.rtt = null;
         this.image = null;
      }

   }

   public Object getData() {
      return this;
   }

   public int getContentWidth() {
      return this.rtt.getContentWidth();
   }

   public int getContentHeight() {
      return this.rtt.getContentHeight();
   }

   public int getMaxContentWidth() {
      return this.rtt.getMaxContentWidth();
   }

   public int getMaxContentHeight() {
      return this.rtt.getMaxContentHeight();
   }

   public void setContentWidth(int var1) {
      this.rtt.setContentWidth(var1);
   }

   public void setContentHeight(int var1) {
      this.rtt.setContentHeight(var1);
   }

   public int getPhysicalWidth() {
      return this.rtt.getContentWidth();
   }

   public int getPhysicalHeight() {
      return this.rtt.getContentHeight();
   }

   public int getScanlineStride() {
      return this.rtt.getContentWidth();
   }

   public int[] getPixelArray() {
      int[] var1 = this.rtt.getPixels();
      if (var1 != null) {
         return var1;
      } else {
         if (this.image == null) {
            int var2 = this.rtt.getContentWidth();
            int var3 = this.rtt.getContentHeight();
            var1 = new int[var2 * var3];
            this.image = Image.fromIntArgbPreData(var1, var2, var3);
         }

         IntBuffer var4 = (IntBuffer)this.image.getPixelBuffer();
         if (this.vramDirty) {
            this.rtt.readPixels(var4);
            this.vramDirty = false;
         }

         this.heapDirty = true;
         return var4.array();
      }
   }

   public RTTexture getTextureObject() {
      if (this.heapDirty) {
         int var1 = this.rtt.getContentWidth();
         int var2 = this.rtt.getContentHeight();
         Screen var3 = this.rtt.getAssociatedScreen();
         ResourceFactory var4 = GraphicsPipeline.getPipeline().getResourceFactory(var3);
         Texture var5 = var4.createTexture(this.image, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE);
         Graphics var6 = this.createGraphics();
         var6.drawTexture(var5, 0.0F, 0.0F, (float)var1, (float)var2);
         var6.sync();
         var5.dispose();
         this.heapDirty = false;
      }

      return this.rtt;
   }

   public Graphics createGraphics() {
      this.vramDirty = true;
      return this.rtt.createGraphics();
   }

   public void clear() {
      Graphics var1 = this.createGraphics();
      var1.clear();
      if (this.image != null) {
         IntBuffer var2 = (IntBuffer)this.image.getPixelBuffer();
         Arrays.fill(var2.array(), 0);
      }

      this.heapDirty = false;
      this.vramDirty = false;
   }
}
