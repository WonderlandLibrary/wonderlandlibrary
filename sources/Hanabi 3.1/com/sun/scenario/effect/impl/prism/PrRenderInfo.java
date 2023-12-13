package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.ImageDataRenderer;

public class PrRenderInfo implements ImageDataRenderer {
   private Graphics g;

   public PrRenderInfo(Graphics var1) {
      this.g = var1;
   }

   public Graphics getGraphics() {
      return this.g;
   }

   public void renderImage(ImageData var1, BaseTransform var2, FilterContext var3) {
      if (var1.validate(var3)) {
         Rectangle var4 = var1.getUntransformedBounds();
         Texture var5 = ((PrTexture)var1.getUntransformedImage()).getTextureObject();
         BaseTransform var6 = null;
         if (!var2.isIdentity()) {
            var6 = this.g.getTransformNoClone().copy();
            this.g.transform(var2);
         }

         BaseTransform var7 = var1.getTransform();
         if (!var7.isIdentity()) {
            if (var6 == null) {
               var6 = this.g.getTransformNoClone().copy();
            }

            this.g.transform(var7);
         }

         this.g.drawTexture(var5, (float)var4.x, (float)var4.y, (float)var4.width, (float)var4.height);
         if (var6 != null) {
            this.g.setTransform(var6);
         }
      }

   }
}
