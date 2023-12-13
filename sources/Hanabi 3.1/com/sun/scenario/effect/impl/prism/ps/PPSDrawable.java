package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.impl.prism.PrDrawable;

public class PPSDrawable extends PrDrawable {
   private RTTexture rtt;

   private PPSDrawable(RTTexture var1) {
      super(var1);
      this.rtt = var1;
   }

   static PPSDrawable create(RTTexture var0) {
      return new PPSDrawable(var0);
   }

   static int getCompatibleWidth(ResourceFactory var0, int var1) {
      return var0.getRTTWidth(var1, Texture.WrapMode.CLAMP_TO_ZERO);
   }

   static int getCompatibleHeight(ResourceFactory var0, int var1) {
      return var0.getRTTHeight(var1, Texture.WrapMode.CLAMP_TO_ZERO);
   }

   static PPSDrawable create(ResourceFactory var0, int var1, int var2) {
      RTTexture var3 = var0.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_TO_ZERO);
      return new PPSDrawable(var3);
   }

   public boolean isLost() {
      return this.rtt == null || this.rtt.isSurfaceLost();
   }

   public void flush() {
      if (this.rtt != null) {
         this.rtt.dispose();
         this.rtt = null;
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
      return this.rtt.getPhysicalWidth();
   }

   public int getPhysicalHeight() {
      return this.rtt.getPhysicalHeight();
   }

   public ShaderGraphics createGraphics() {
      return (ShaderGraphics)this.rtt.createGraphics();
   }
}
