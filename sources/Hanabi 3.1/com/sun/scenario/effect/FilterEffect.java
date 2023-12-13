package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class FilterEffect extends Effect {
   protected FilterEffect() {
   }

   protected FilterEffect(Effect var1) {
      super(var1);
   }

   protected FilterEffect(Effect var1, Effect var2) {
      super(var1, var2);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      int var3 = this.getNumInputs();
      RenderState var4 = this.getRenderState((FilterContext)null, var1, (Rectangle)null, (Object)null, var2);
      BaseTransform var5 = var4.getInputTransform(var1);
      BaseBounds var6;
      if (var3 == 1) {
         Effect var7 = this.getDefaultedInput(0, var2);
         var6 = var7.getBounds(var5, var2);
      } else {
         BaseBounds[] var10 = new BaseBounds[var3];

         for(int var8 = 0; var8 < var3; ++var8) {
            Effect var9 = this.getDefaultedInput(var8, var2);
            var10[var8] = var9.getBounds(var5, var2);
         }

         var6 = combineBounds(var10);
      }

      return transformBounds(var4.getResultTransform(var1), var6);
   }

   protected static Rectangle untransformClip(BaseTransform var0, Rectangle var1) {
      if (!var0.isIdentity() && var1 != null && !var1.isEmpty()) {
         Rectangle var2 = new Rectangle();
         if (var0.isTranslateOrIdentity()) {
            var2.setBounds(var1);
            double var10 = -var0.getMxt();
            double var5 = -var0.getMyt();
            int var7 = (int)Math.floor(var10);
            int var8 = (int)Math.floor(var5);
            var2.translate(var7, var8);
            if ((double)var7 != var10) {
               ++var2.width;
            }

            if ((double)var8 != var5) {
               ++var2.height;
            }

            return var2;
         } else {
            RectBounds var3 = new RectBounds(var1);

            try {
               var3.grow(-0.5F, -0.5F);
               var3 = (RectBounds)var0.inverseTransform((BaseBounds)var3, (BaseBounds)var3);
               var3.grow(0.5F, 0.5F);
               var2.setBounds((BaseBounds)var3);
            } catch (NoninvertibleTransformException var9) {
            }

            return var2;
         }
      } else {
         return var1;
      }
   }

   public abstract RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5);

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      RenderState var6 = this.getRenderState(var1, var2, var3, var4, var5);
      int var7 = this.getNumInputs();
      ImageData[] var8 = new ImageData[var7];
      BaseTransform var10 = var6.getInputTransform(var2);
      BaseTransform var11 = var6.getResultTransform(var2);
      Rectangle var9;
      if (var11.isIdentity()) {
         var9 = var3;
      } else {
         var9 = untransformClip(var11, var3);
      }

      for(int var12 = 0; var12 < var7; ++var12) {
         Effect var13 = this.getDefaultedInput(var12, var5);
         var8[var12] = var13.filter(var1, var10, var6.getInputClip(var12, var9), (Object)null, var5);
         if (!var8[var12].validate(var1)) {
            for(int var14 = 0; var14 <= var12; ++var14) {
               var8[var14].unref();
            }

            return new ImageData(var1, (Filterable)null, (Rectangle)null);
         }
      }

      ImageData var15 = this.filterImageDatas(var1, var10, var9, var6, var8);

      for(int var16 = 0; var16 < var7; ++var16) {
         var8[var16].unref();
      }

      if (!var11.isIdentity()) {
         if (var4 instanceof ImageDataRenderer) {
            ImageDataRenderer var17 = (ImageDataRenderer)var4;
            var17.renderImage(var15, var11, var1);
            var15.unref();
            var15 = null;
         } else {
            var15 = var15.transform(var11);
         }
      }

      return var15;
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).untransform(var1, var2);
   }

   protected abstract ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, RenderState var4, ImageData... var5);
}
