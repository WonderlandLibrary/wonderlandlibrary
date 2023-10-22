package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;

public abstract class PPSOneSamplerPeer extends PPSEffectPeer {
   private Shader shader;

   protected PPSOneSamplerPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public void dispose() {
      if (this.shader != null) {
         this.shader.dispose();
      }

   }

   ImageData filterImpl(ImageData... var1) {
      Filterable var2 = var1[0].getUntransformedImage();
      PrTexture var3 = (PrTexture)var2;
      Rectangle var4 = var1[0].getUntransformedBounds();
      Rectangle var5 = this.getDestBounds();
      int var6 = var5.width;
      int var7 = var5.height;
      PPSRenderer var8 = this.getRenderer();
      PPSDrawable var9 = var8.getCompatibleImage(var6, var7);
      if (var9 == null) {
         var8.markLost();
         return new ImageData(this.getFilterContext(), var9, var5);
      } else {
         this.setDestNativeBounds(var9.getPhysicalWidth(), var9.getPhysicalHeight());
         BaseTransform var10 = var1[0].getTransform();
         this.setInputBounds(0, var4);
         this.setInputTransform(0, var10);
         this.setInputNativeBounds(0, var3.getNativeBounds());
         float[] var11 = new float[8];
         int var12 = this.getTextureCoordinates(0, var11, (float)var4.x, (float)var4.y, (float)var2.getPhysicalWidth(), (float)var2.getPhysicalHeight(), var5, var10);
         ShaderGraphics var13 = var9.createGraphics();
         if (var13 == null) {
            var8.markLost();
            return new ImageData(this.getFilterContext(), var9, var5);
         } else {
            if (this.shader == null) {
               this.shader = this.createShader();
            }

            if (this.shader != null && this.shader.isValid()) {
               var13.setExternalShader(this.shader);
               this.updateShader(this.shader);
               float var14 = 0.0F;
               float var15 = 0.0F;
               float var16 = (float)var6;
               float var17 = (float)var7;
               Texture var18 = var3.getTextureObject();
               if (var18 == null) {
                  var8.markLost();
                  return new ImageData(this.getFilterContext(), var9, var5);
               } else {
                  float var19 = (float)var18.getContentX() / (float)var18.getPhysicalWidth();
                  float var20 = (float)var18.getContentY() / (float)var18.getPhysicalHeight();
                  float var21 = var19 + var11[0];
                  float var22 = var20 + var11[1];
                  float var23 = var19 + var11[2];
                  float var24 = var20 + var11[3];
                  if (var12 < 8) {
                     var13.drawTextureRaw(var18, var14, var15, var16, var17, var21, var22, var23, var24);
                  } else {
                     float var25 = var19 + var11[4];
                     float var26 = var20 + var11[5];
                     float var27 = var19 + var11[6];
                     float var28 = var20 + var11[7];
                     var13.drawMappedTextureRaw(var18, var14, var15, var16, var17, var21, var22, var25, var26, var27, var28, var23, var24);
                  }

                  var13.setExternalShader((Shader)null);
                  return new ImageData(this.getFilterContext(), var9, var5);
               }
            } else {
               var8.markLost();
               return new ImageData(this.getFilterContext(), var9, var5);
            }
         }
      }
   }
}
