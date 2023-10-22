package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class InvertMask extends CoreEffect {
   private int pad;
   private int xoff;
   private int yoff;

   public InvertMask() {
      this(10);
   }

   public InvertMask(Effect var1) {
      this(10, var1);
   }

   public InvertMask(int var1) {
      this(var1, DefaultInput);
   }

   public InvertMask(int var1, Effect var2) {
      super(var2);
      this.setPad(var1);
      this.updatePeerKey("InvertMask");
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public int getPad() {
      return this.pad;
   }

   public void setPad(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Pad value must be non-negative");
      } else {
         int var2 = this.pad;
         this.pad = var1;
      }
   }

   public int getOffsetX() {
      return this.xoff;
   }

   public void setOffsetX(int var1) {
      int var2 = this.xoff;
      this.xoff = var1;
   }

   public int getOffsetY() {
      return this.yoff;
   }

   public void setOffsetY(int var1) {
      float var2 = (float)this.yoff;
      this.yoff = var1;
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseBounds var3 = super.getBounds(BaseTransform.IDENTITY_TRANSFORM, var2);
      Object var4 = new RectBounds(var3.getMinX(), var3.getMinY(), var3.getMaxX(), var3.getMaxY());
      ((RectBounds)var4).grow((float)this.pad, (float)this.pad);
      if (!var1.isIdentity()) {
         var4 = transformBounds(var1, (BaseBounds)var4);
      }

      return (BaseBounds)var4;
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = super.getResultBounds(var1, var2, var3);
      Rectangle var5 = new Rectangle(var4);
      var5.grow(this.pad, this.pad);
      return var5;
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return new RenderState() {
         public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
            return RenderState.EffectCoordinateSpace.UserSpace;
         }

         public BaseTransform getInputTransform(BaseTransform var1) {
            return BaseTransform.IDENTITY_TRANSFORM;
         }

         public BaseTransform getResultTransform(BaseTransform var1) {
            return var1;
         }

         public Rectangle getInputClip(int var1, Rectangle var2) {
            if (var2 != null && InvertMask.this.pad != 0) {
               var2 = new Rectangle(var2);
               var2.grow(InvertMask.this.pad, InvertMask.this.pad);
            }

            return var2;
         }
      };
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      if (this.xoff != 0 || this.yoff != 0) {
         for(int var5 = 0; var5 < var4.size(); ++var5) {
            var4.getDirtyRegion(var5).translate((float)this.xoff, (float)this.yoff, 0.0F);
         }
      }

      return var4;
   }
}
