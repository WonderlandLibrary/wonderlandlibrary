package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;

public class JSWPhongLighting_DISTANTPeer extends JSWEffectPeer {
   private FloatBuffer kvals;

   public JSWPhongLighting_DISTANTPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   protected final PhongLighting getEffect() {
      return (PhongLighting)super.getEffect();
   }

   private float getSurfaceScale() {
      return this.getEffect().getSurfaceScale();
   }

   private float getDiffuseConstant() {
      return this.getEffect().getDiffuseConstant();
   }

   private float getSpecularConstant() {
      return this.getEffect().getSpecularConstant();
   }

   private float getSpecularExponent() {
      return this.getEffect().getSpecularExponent();
   }

   private float[] getNormalizedLightPosition() {
      return this.getEffect().getLight().getNormalizedLightPosition();
   }

   private float[] getLightPosition() {
      PointLight var1 = (PointLight)this.getEffect().getLight();
      return new float[]{var1.getX(), var1.getY(), var1.getZ()};
   }

   private float[] getLightColor() {
      return this.getEffect().getLight().getColor().getPremultipliedRGBComponents();
   }

   private float getLightSpecularExponent() {
      return ((SpotLight)this.getEffect().getLight()).getSpecularExponent();
   }

   private float[] getNormalizedLightDirection() {
      return ((SpotLight)this.getEffect().getLight()).getNormalizedLightDirection();
   }

   private FloatBuffer getKvals() {
      Rectangle var1 = this.getInputNativeBounds(0);
      float var2 = 1.0F / (float)var1.width;
      float var3 = 1.0F / (float)var1.height;
      float[] var4 = new float[]{-1.0F, 0.0F, 1.0F, -2.0F, 0.0F, 2.0F, -1.0F, 0.0F, 1.0F};
      float[] var5 = new float[]{-1.0F, -2.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F};
      if (this.kvals == null) {
         this.kvals = BufferUtil.newFloatBuffer(32);
      }

      this.kvals.clear();
      int var6 = 0;
      float var7 = -this.getSurfaceScale() * 0.25F;

      for(int var8 = -1; var8 <= 1; ++var8) {
         for(int var9 = -1; var9 <= 1; ++var9) {
            if (var8 != 0 || var9 != 0) {
               this.kvals.put((float)var9 * var2);
               this.kvals.put((float)var8 * var3);
               this.kvals.put(var4[var6] * var7);
               this.kvals.put(var5[var6] * var7);
            }

            ++var6;
         }
      }

      this.kvals.rewind();
      return this.kvals;
   }

   private int getKvalsArrayLength() {
      return 8;
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setEffect(var1);
      Rectangle var6 = this.getResultBounds(var3, var4, var5);
      this.setDestBounds(var6);
      HeapImage var7 = (HeapImage)var5[0].getTransformedImage(var6);
      byte var8 = 0;
      byte var9 = 0;
      int var10 = var7.getPhysicalWidth();
      int var11 = var7.getPhysicalHeight();
      int var12 = var7.getScanlineStride();
      int[] var13 = var7.getPixelArray();
      Rectangle var14 = new Rectangle(var8, var9, var10, var11);
      Rectangle var15 = var5[0].getTransformedBounds(var6);
      BaseTransform var16 = BaseTransform.IDENTITY_TRANSFORM;
      this.setInputBounds(0, var15);
      this.setInputNativeBounds(0, var14);
      HeapImage var17 = (HeapImage)var5[1].getTransformedImage(var6);
      byte var18 = 0;
      byte var19 = 0;
      int var20 = var17.getPhysicalWidth();
      int var21 = var17.getPhysicalHeight();
      int var22 = var17.getScanlineStride();
      int[] var23 = var17.getPixelArray();
      Rectangle var24 = new Rectangle(var18, var19, var20, var21);
      Rectangle var25 = var5[1].getTransformedBounds(var6);
      BaseTransform var26 = BaseTransform.IDENTITY_TRANSFORM;
      this.setInputBounds(1, var25);
      this.setInputNativeBounds(1, var24);
      float[] var27 = new float[4];
      this.getTextureCoordinates(0, var27, (float)var15.x, (float)var15.y, (float)var10, (float)var11, var6, var16);
      float[] var28 = new float[4];
      this.getTextureCoordinates(1, var28, (float)var25.x, (float)var25.y, (float)var20, (float)var21, var6, var26);
      int var31 = var6.width;
      int var32 = var6.height;
      HeapImage var33 = (HeapImage)this.getRenderer().getCompatibleImage(var31, var32);
      this.setDestNativeBounds(var33.getPhysicalWidth(), var33.getPhysicalHeight());
      int var34 = var33.getScanlineStride();
      int[] var35 = var33.getPixelArray();
      float[] var41 = this.getNormalizedLightPosition();
      float var42 = var41[0];
      float var43 = var41[1];
      float var44 = var41[2];
      float var45 = this.getSpecularExponent();
      FloatBuffer var46 = this.getKvals();
      float[] var47 = new float[var46.capacity()];
      var46.get(var47);
      float var48 = this.getDiffuseConstant();
      float[] var49 = this.getLightColor();
      float var50 = var49[0];
      float var51 = var49[1];
      float var52 = var49[2];
      float var53 = this.getSpecularConstant();
      float var54 = (var27[2] - var27[0]) / (float)var31;
      float var55 = (var27[3] - var27[1]) / (float)var32;
      float var56 = (var28[2] - var28[0]) / (float)var31;
      float var57 = (var28[3] - var28[1]) / (float)var32;
      float var58 = var27[1] + var55 * 0.5F;
      float var59 = var28[1] + var57 * 0.5F;

      for(int var60 = 0; var60 < 0 + var32; ++var60) {
         float var61 = (float)var60;
         int var36 = var60 * var34;
         float var62 = var27[0] + var54 * 0.5F;
         float var63 = var28[0] + var56 * 0.5F;

         for(int var64 = 0; var64 < 0 + var31; ++var64) {
            float var65 = (float)var64;
            int var72;
            int var74;
            if (var63 >= 0.0F && var59 >= 0.0F) {
               int var73 = (int)(var63 * (float)var20);
               var74 = (int)(var59 * (float)var21);
               boolean var75 = var73 >= var20 || var74 >= var21;
               var72 = var75 ? 0 : var23[var74 * var22 + var73];
            } else {
               var72 = 0;
            }

            float var66 = (float)(var72 >> 16 & 255) / 255.0F;
            float var67 = (float)(var72 >> 8 & 255) / 255.0F;
            float var68 = (float)(var72 & 255) / 255.0F;
            float var69 = (float)(var72 >>> 24) / 255.0F;
            float var116 = 0.0F;
            float var76 = 0.0F;
            float var77 = 1.0F;

            float var78;
            float var79;
            for(var74 = 0; var74 < 8; ++var74) {
               var78 = var62 + var47[var74 * 4 + 0];
               var79 = var58 + var47[var74 * 4 + 1];
               int var80;
               if (var78 >= 0.0F && var79 >= 0.0F) {
                  int var81 = (int)(var78 * (float)var10);
                  int var82 = (int)(var79 * (float)var11);
                  boolean var83 = var81 >= var10 || var82 >= var11;
                  var80 = var83 ? 0 : var13[var82 * var12 + var81];
               } else {
                  var80 = 0;
               }

               var69 = (float)(var80 >>> 24) / 255.0F;
               var116 += var47[var74 * 4 + 2] * var69;
               var76 += var47[var74 * 4 + 3] * var69;
            }

            float var84 = (float)Math.sqrt((double)(var116 * var116 + var76 * var76 + var77 * var77));
            var78 = var116 / var84;
            var79 = var76 / var84;
            float var117 = var77 / var84;
            float var118 = var78;
            float var119 = var79;
            float var120 = var117;
            float var90 = 0.0F;
            float var91 = 0.0F;
            float var92 = 1.0F;
            float var93 = var42 + var90;
            float var94 = var43 + var91;
            float var95 = var44 + var92;
            float var96 = (float)Math.sqrt((double)(var93 * var93 + var94 * var94 + var95 * var95));
            var78 = var93 / var96;
            var79 = var94 / var96;
            var117 = var95 / var96;
            float var100 = var118 * var42 + var119 * var43 + var120 * var44;
            var96 = var48 * var100 * var50;
            float var97 = var48 * var100 * var51;
            float var98 = var48 * var100 * var52;
            float var107 = 0.0F;
            float var108 = 1.0F;
            float var101 = var96 < var107 ? var107 : (var96 > var108 ? var108 : var96);
            float var102 = var97 < var107 ? var107 : (var97 > var108 ? var108 : var97);
            float var103 = var98 < var107 ? var107 : (var98 > var108 ? var108 : var98);
            float var99 = 1.0F;
            var100 = var118 * var78 + var119 * var79 + var120 * var117;
            float var109 = (float)Math.pow((double)var100, (double)var45);
            float var104 = var53 * var109 * var50;
            float var105 = var53 * var109 * var51;
            float var106 = var53 * var109 * var52;
            float var110 = var104 > var105 ? var104 : var105;
            var110 = var110 > var106 ? var110 : var106;
            float var70 = var66 * var101;
            float var71 = var67 * var102;
            float var114 = var68 * var103;
            float var115 = var69 * var99;
            var104 *= var115;
            var105 *= var115;
            var106 *= var115;
            var107 = var110 * var115;
            float var37 = var104 + var70 * (1.0F - var107);
            float var38 = var105 + var71 * (1.0F - var107);
            float var39 = var106 + var114 * (1.0F - var107);
            float var40 = var107 + var115 * (1.0F - var107);
            if (var40 < 0.0F) {
               var40 = 0.0F;
            } else if (var40 > 1.0F) {
               var40 = 1.0F;
            }

            if (var37 < 0.0F) {
               var37 = 0.0F;
            } else if (var37 > var40) {
               var37 = var40;
            }

            if (var38 < 0.0F) {
               var38 = 0.0F;
            } else if (var38 > var40) {
               var38 = var40;
            }

            if (var39 < 0.0F) {
               var39 = 0.0F;
            } else if (var39 > var40) {
               var39 = var40;
            }

            var35[var36 + var64] = (int)(var37 * 255.0F) << 16 | (int)(var38 * 255.0F) << 8 | (int)(var39 * 255.0F) << 0 | (int)(var40 * 255.0F) << 24;
            var62 += var54;
            var63 += var56;
         }

         var58 += var55;
         var59 += var57;
      }

      var5[0].releaseTransformedImage(var7);
      var5[1].releaseTransformedImage(var17);
      return new ImageData(this.getFilterContext(), var33, var6);
   }
}
