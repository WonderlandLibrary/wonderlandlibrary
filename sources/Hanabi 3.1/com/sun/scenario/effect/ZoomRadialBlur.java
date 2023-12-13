package com.sun.scenario.effect;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.state.ZoomRadialBlurState;

public class ZoomRadialBlur extends CoreEffect {
   private int r;
   private float centerX;
   private float centerY;
   private final ZoomRadialBlurState state;

   public ZoomRadialBlur() {
      this(1);
   }

   public ZoomRadialBlur(int var1) {
      this(var1, DefaultInput);
   }

   public ZoomRadialBlur(int var1, Effect var2) {
      super(var2);
      this.state = new ZoomRadialBlurState(this);
      this.setRadius(var1);
   }

   Object getState() {
      return this.state;
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public int getRadius() {
      return this.r;
   }

   public void setRadius(int var1) {
      if (var1 >= 1 && var1 <= 64) {
         int var2 = this.r;
         this.r = var1;
         this.state.invalidateDeltas();
         this.updatePeer();
      } else {
         throw new IllegalArgumentException("Radius must be in the range [1,64]");
      }
   }

   private void updatePeer() {
      int var1 = 4 + this.r - this.r % 4;
      this.updatePeerKey("ZoomRadialBlur", var1);
   }

   public float getCenterX() {
      return this.centerX;
   }

   public void setCenterX(float var1) {
      float var2 = this.centerX;
      this.centerX = var1;
   }

   public float getCenterY() {
      return this.centerY;
   }

   public void setCenterY(float var1) {
      float var2 = this.centerY;
      this.centerY = var1;
   }

   public ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, RenderState var4, ImageData... var5) {
      Rectangle var6 = var5[0].getUntransformedBounds();
      this.state.updateDeltas(1.0F / (float)var6.width, 1.0F / (float)var6.height);
      return super.filterImageDatas(var1, var2, var3, var4, var5);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.UserSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      int var5 = this.getRadius();
      var4.grow(var5, var5);
      return var4;
   }
}
