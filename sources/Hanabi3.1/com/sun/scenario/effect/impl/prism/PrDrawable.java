package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.ImagePool;
import com.sun.scenario.effect.impl.PoolFilterable;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.ref.WeakReference;

public abstract class PrDrawable extends PrTexture implements PoolFilterable {
   private WeakReference pool;

   public static PrDrawable create(FilterContext var0, RTTexture var1) {
      return ((PrRenderer)Renderer.getRenderer(var0)).createDrawable(var1);
   }

   protected PrDrawable(RTTexture var1) {
      super(var1);
   }

   public void setImagePool(ImagePool var1) {
      this.pool = new WeakReference(var1);
   }

   public ImagePool getImagePool() {
      return this.pool == null ? null : (ImagePool)this.pool.get();
   }

   public float getPixelScale() {
      return 1.0F;
   }

   public int getMaxContentWidth() {
      return ((RTTexture)this.getTextureObject()).getMaxContentWidth();
   }

   public int getMaxContentHeight() {
      return ((RTTexture)this.getTextureObject()).getMaxContentHeight();
   }

   public void setContentWidth(int var1) {
      ((RTTexture)this.getTextureObject()).setContentWidth(var1);
   }

   public void setContentHeight(int var1) {
      ((RTTexture)this.getTextureObject()).setContentHeight(var1);
   }

   public abstract Graphics createGraphics();

   public void clear() {
      Graphics var1 = this.createGraphics();
      var1.clear();
   }
}
