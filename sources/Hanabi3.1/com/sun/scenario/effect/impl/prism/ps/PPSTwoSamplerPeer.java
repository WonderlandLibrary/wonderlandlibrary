package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;

public abstract class PPSTwoSamplerPeer extends PPSEffectPeer {
   private Shader shader;

   protected PPSTwoSamplerPeer(FilterContext var1, Renderer var2, String var3) {
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
         Filterable var7 = var1[0].getUntransformedImage();
         PrTexture var8 = (PrTexture)var7;
         Rectangle var9 = var1[0].getUntransformedBounds();
         BaseTransform var10 = var1[0].getTransform();
         this.setInputBounds(0, var9);
         this.setInputTransform(0, var10);
         this.setInputNativeBounds(0, var8.getNativeBounds());
         float[] var12 = new float[8];
         PrTexture var11;
         int var13;
         Rectangle var15;
         if (var1.length > 1) {
            Filterable var14 = var1[1].getUntransformedImage();
            var11 = (PrTexture)var14;
            if (var11 == null) {
               var5.markLost();
               return new ImageData(this.getFilterContext(), var6, var2);
            }

            var15 = var1[1].getUntransformedBounds();
            BaseTransform var16 = var1[1].getTransform();
            this.setInputBounds(1, var15);
            this.setInputTransform(1, var16);
            this.setInputNativeBounds(1, var11.getNativeBounds());
            var13 = this.getTextureCoordinates(1, var12, (float)var15.x, (float)var15.y, (float)var14.getPhysicalWidth(), (float)var14.getPhysicalHeight(), var2, var16);
         } else {
            FloatMap var43 = (FloatMap)this.getSamplerData(1);
            var11 = (PrTexture)var43.getAccelData(this.getFilterContext());
            if (var11 == null) {
               var5.markLost();
               return new ImageData(this.getFilterContext(), var6, var2);
            }

            var15 = new Rectangle(var43.getWidth(), var43.getHeight());
            Rectangle var46 = var11.getNativeBounds();
            this.setInputBounds(1, var15);
            this.setInputNativeBounds(1, var46);
            var12[0] = var12[1] = 0.0F;
            var12[2] = (float)var15.width / (float)var46.width;
            var12[3] = (float)var15.height / (float)var46.height;
            var13 = 4;
         }

         float[] var44 = new float[8];
         int var45 = this.getTextureCoordinates(0, var44, (float)var9.x, (float)var9.y, (float)var7.getPhysicalWidth(), (float)var7.getPhysicalHeight(), var2, var10);
         ShaderGraphics var47 = var6.createGraphics();
         if (var47 == null) {
            var5.markLost();
            return new ImageData(this.getFilterContext(), var6, var2);
         } else {
            if (this.shader == null) {
               this.shader = this.createShader();
            }

            if (this.shader != null && this.shader.isValid()) {
               var47.setExternalShader(this.shader);
               this.updateShader(this.shader);
               float var17 = 0.0F;
               float var18 = 0.0F;
               float var19 = (float)var3;
               float var20 = (float)var4;
               Texture var21 = var8.getTextureObject();
               if (var21 == null) {
                  var5.markLost();
                  return new ImageData(this.getFilterContext(), var6, var2);
               } else {
                  Texture var22 = var11.getTextureObject();
                  if (var22 == null) {
                     var5.markLost();
                     return new ImageData(this.getFilterContext(), var6, var2);
                  } else {
                     float var23 = (float)var21.getContentX() / (float)var21.getPhysicalWidth();
                     float var24 = (float)var21.getContentY() / (float)var21.getPhysicalHeight();
                     float var25 = var23 + var44[0];
                     float var26 = var24 + var44[1];
                     float var27 = var23 + var44[2];
                     float var28 = var24 + var44[3];
                     float var29 = (float)var22.getContentX() / (float)var22.getPhysicalWidth();
                     float var30 = (float)var22.getContentY() / (float)var22.getPhysicalHeight();
                     float var31 = var29 + var12[0];
                     float var32 = var30 + var12[1];
                     float var33 = var29 + var12[2];
                     float var34 = var30 + var12[3];
                     if (var45 < 8 && var13 < 8) {
                        var47.drawTextureRaw2(var21, var22, var17, var18, var19, var20, var25, var26, var27, var28, var31, var32, var33, var34);
                     } else {
                        float var35;
                        float var36;
                        float var37;
                        float var38;
                        if (var45 < 8) {
                           var35 = var27;
                           var36 = var26;
                           var37 = var25;
                           var38 = var28;
                        } else {
                           var35 = var23 + var44[4];
                           var36 = var24 + var44[5];
                           var37 = var23 + var44[6];
                           var38 = var24 + var44[7];
                        }

                        float var39;
                        float var40;
                        float var41;
                        float var42;
                        if (var13 < 8) {
                           var39 = var33;
                           var40 = var32;
                           var41 = var31;
                           var42 = var34;
                        } else {
                           var39 = var29 + var12[4];
                           var40 = var30 + var12[5];
                           var41 = var29 + var12[6];
                           var42 = var30 + var12[7];
                        }

                        var47.drawMappedTextureRaw2(var21, var22, var17, var18, var19, var20, var25, var26, var35, var36, var37, var38, var27, var28, var31, var32, var39, var40, var41, var42, var33, var34);
                     }

                     var47.setExternalShader((Shader)null);
                     if (var1.length <= 1) {
                        var11.unlock();
                     }

                     return new ImageData(this.getFilterContext(), var6, var2);
                  }
               }
            } else {
               var5.markLost();
               return new ImageData(this.getFilterContext(), var6, var2);
            }
         }
      }
   }
}
