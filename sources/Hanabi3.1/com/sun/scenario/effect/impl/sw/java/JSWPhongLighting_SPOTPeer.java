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

public class JSWPhongLighting_SPOTPeer extends JSWEffectPeer {
   private FloatBuffer kvals;

   public JSWPhongLighting_SPOTPeer(FilterContext var1, Renderer var2, String var3) {
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
      float[] var41 = this.getLightPosition();
      float var42 = var41[0];
      float var43 = var41[1];
      float var44 = var41[2];
      float var45 = this.getLightSpecularExponent();
      float var46 = this.getSpecularExponent();
      FloatBuffer var47 = this.getKvals();
      float[] var48 = new float[var47.capacity()];
      var47.get(var48);
      float var49 = this.getDiffuseConstant();
      float[] var50 = this.getLightColor();
      float var51 = var50[0];
      float var52 = var50[1];
      float var53 = var50[2];
      float[] var54 = this.getNormalizedLightDirection();
      float var55 = var54[0];
      float var56 = var54[1];
      float var57 = var54[2];
      float var58 = this.getSpecularConstant();
      float var59 = this.getSurfaceScale();
      float var60 = (var27[2] - var27[0]) / (float)var31;
      float var61 = (var27[3] - var27[1]) / (float)var32;
      float var62 = (var28[2] - var28[0]) / (float)var31;
      float var63 = (var28[3] - var28[1]) / (float)var32;
      float var64 = var27[1] + var61 * 0.5F;
      float var65 = var28[1] + var63 * 0.5F;

      for(int var66 = 0; var66 < 0 + var32; ++var66) {
         float var67 = (float)var66;
         int var36 = var66 * var34;
         float var68 = var27[0] + var60 * 0.5F;
         float var69 = var28[0] + var62 * 0.5F;

         for(int var70 = 0; var70 < 0 + var31; ++var70) {
            float var71 = (float)var70;
            int var78;
            int var80;
            if (var69 >= 0.0F && var65 >= 0.0F) {
               int var79 = (int)(var69 * (float)var20);
               var80 = (int)(var65 * (float)var21);
               boolean var81 = var79 >= var20 || var80 >= var21;
               var78 = var81 ? 0 : var23[var80 * var22 + var79];
            } else {
               var78 = 0;
            }

            float var72 = (float)(var78 >> 16 & 255) / 255.0F;
            float var73 = (float)(var78 >> 8 & 255) / 255.0F;
            float var74 = (float)(var78 & 255) / 255.0F;
            float var75 = (float)(var78 >>> 24) / 255.0F;
            float var128 = var75;
            float var129 = 0.0F;
            float var82 = 0.0F;
            float var83 = 1.0F;

            float var84;
            float var85;
            for(var80 = 0; var80 < 8; ++var80) {
               var84 = var68 + var48[var80 * 4 + 0];
               var85 = var64 + var48[var80 * 4 + 1];
               int var86;
               if (var84 >= 0.0F && var85 >= 0.0F) {
                  int var87 = (int)(var84 * (float)var10);
                  int var88 = (int)(var85 * (float)var11);
                  boolean var89 = var87 >= var10 || var88 >= var11;
                  var86 = var89 ? 0 : var13[var88 * var12 + var87];
               } else {
                  var86 = 0;
               }

               var75 = (float)(var86 >>> 24) / 255.0F;
               var129 += var48[var80 * 4 + 2] * var75;
               var82 += var48[var80 * 4 + 3] * var75;
            }

            float var90 = (float)Math.sqrt((double)(var129 * var129 + var82 * var82 + var83 * var83));
            var84 = var129 / var90;
            var85 = var82 / var90;
            float var130 = var83 / var90;
            float var131 = var84;
            float var132 = var85;
            float var133 = var130;
            int var92;
            if (var68 >= 0.0F && var64 >= 0.0F) {
               int var93 = (int)(var68 * (float)var10);
               int var94 = (int)(var64 * (float)var11);
               boolean var95 = var93 >= var10 || var94 >= var11;
               var92 = var95 ? 0 : var13[var94 * var12 + var93];
            } else {
               var92 = 0;
            }

            var75 = (float)(var92 >>> 24) / 255.0F;
            float var134 = var59 * var75;
            float var135 = var42 - var71;
            float var136 = var43 - var67;
            float var96 = var44 - var134;
            float var97 = (float)Math.sqrt((double)(var135 * var135 + var136 * var136 + var96 * var96));
            var84 = var135 / var97;
            var85 = var136 / var97;
            var130 = var96 / var97;
            var135 = var84;
            var136 = var85;
            var96 = var130;
            var97 = var84 * var55 + var85 * var56 + var130 * var57;
            float var101 = 0.0F;
            float var99 = var97 < var101 ? var97 : var101;
            var101 = -var99;
            float var100 = (float)Math.pow((double)var101, (double)var45);
            var101 = var51 * var100;
            float var102 = var52 * var100;
            float var103 = var53 * var100;
            float var104 = 0.0F;
            float var105 = 0.0F;
            float var106 = 1.0F;
            float var107 = var84 + var104;
            float var108 = var85 + var105;
            float var109 = var130 + var106;
            float var110 = (float)Math.sqrt((double)(var107 * var107 + var108 * var108 + var109 * var109));
            var84 = var107 / var110;
            var85 = var108 / var110;
            var130 = var109 / var110;
            var97 = var131 * var135 + var132 * var136 + var133 * var96;
            var110 = var49 * var97 * var101;
            float var111 = var49 * var97 * var102;
            float var112 = var49 * var97 * var103;
            float var120 = 0.0F;
            float var121 = 1.0F;
            float var114 = var110 < var120 ? var120 : (var110 > var121 ? var121 : var110);
            float var115 = var111 < var120 ? var120 : (var111 > var121 ? var121 : var111);
            float var116 = var112 < var120 ? var120 : (var112 > var121 ? var121 : var112);
            float var113 = 1.0F;
            var97 = var131 * var84 + var132 * var85 + var133 * var130;
            var100 = (float)Math.pow((double)var97, (double)var46);
            float var117 = var58 * var100 * var101;
            float var118 = var58 * var100 * var102;
            float var119 = var58 * var100 * var103;
            float var122 = var117 > var118 ? var117 : var118;
            var122 = var122 > var119 ? var122 : var119;
            float var76 = var72 * var114;
            float var77 = var73 * var115;
            float var127 = var74 * var116;
            var128 *= var113;
            var117 *= var128;
            var118 *= var128;
            var119 *= var128;
            var120 = var122 * var128;
            float var37 = var117 + var76 * (1.0F - var120);
            float var38 = var118 + var77 * (1.0F - var120);
            float var39 = var119 + var127 * (1.0F - var120);
            float var40 = var120 + var128 * (1.0F - var120);
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

            var35[var36 + var70] = (int)(var37 * 255.0F) << 16 | (int)(var38 * 255.0F) << 8 | (int)(var39 * 255.0F) << 0 | (int)(var40 * 255.0F) << 24;
            var68 += var60;
            var69 += var62;
         }

         var64 += var61;
         var65 += var63;
      }

      var5[0].releaseTransformedImage(var7);
      var5[1].releaseTransformedImage(var17);
      return new ImageData(this.getFilterContext(), var33, var6);
   }
}
