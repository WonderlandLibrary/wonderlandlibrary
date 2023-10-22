package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.paint.Color;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;

public abstract class PPSZeroSamplerPeer extends PPSEffectPeer {
   private Shader shader;

   protected PPSZeroSamplerPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public void dispose() {
      if (this.shader != null) {
         this.shader.dispose();
      }

   }

   ImageData filterImpl(ImageData... var1) {
      Rectangle var2 = this.getDestBounds();
      int var3 = var2.width;
      int var4 = var2.height;
      PPSRenderer var5 = this.getRenderer();
      PPSDrawable var6 = var5.getCompatibleImage(var3, var4);
      if (var6 == null) {
         var5.markLost();
         return new ImageData(this.getFilterContext(), var6, var2);
      } else {
         this.setDestNativeBounds(var6.getPhysicalWidth(), var6.getPhysicalHeight());
         ShaderGraphics var7 = var6.createGraphics();
         if (var7 == null) {
            var5.markLost();
            return new ImageData(this.getFilterContext(), var6, var2);
         } else {
            if (this.shader == null) {
               this.shader = this.createShader();
            }

            if (this.shader != null && this.shader.isValid()) {
               var7.setExternalShader(this.shader);
               this.updateShader(this.shader);
               float var8 = 0.0F;
               float var9 = 0.0F;
               float var10 = (float)var3;
               float var11 = (float)var4;
               var7.setPaint(Color.WHITE);
               var7.fillQuad(var8, var9, var10, var11);
               var7.setExternalShader((Shader)null);
               return new ImageData(this.getFilterContext(), var6, var2);
            } else {
               var5.markLost();
               return new ImageData(this.getFilterContext(), var6, var2);
            }
         }
      }
   }
}
