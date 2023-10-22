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

public class JSWPhongLighting_POINTPeer extends JSWEffectPeer {
   private FloatBuffer kvals;

   public JSWPhongLighting_POINTPeer(FilterContext var1, Renderer var2, String var3) {
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
      float var54 = this.getSurfaceScale();
      float var55 = (var27[2] - var27[0]) / (float)var31;
      float var56 = (var27[3] - var27[1]) / (float)var32;
      float var57 = (var28[2] - var28[0]) / (float)var31;
      float var58 = (var28[3] - var28[1]) / (float)var32;
      float var59 = var27[1] + var56 * 0.5F;
      float var60 = var28[1] + var58 * 0.5F;

      for(int var61 = 0; var61 < 0 + var32; ++var61) {
         float var62 = (float)var61;
         int var36 = var61 * var34;
         float var63 = var27[0] + var55 * 0.5F;
         float var64 = var28[0] + var57 * 0.5F;

         for(int var65 = 0; var65 < 0 + var31; ++var65) {
            float var66 = (float)var65;
            int var73;
            int var75;
            if (var64 >= 0.0F && var60 >= 0.0F) {
               int var74 = (int)(var64 * (float)var20);
               var75 = (int)(var60 * (float)var21);
               boolean var76 = var74 >= var20 || var75 >= var21;
               var73 = var76 ? 0 : var23[var75 * var22 + var74];
            } else {
               var73 = 0;
            }

            float var67 = (float)(var73 >> 16 & 255) / 255.0F;
            float var68 = (float)(var73 >> 8 & 255) / 255.0F;
            float var69 = (float)(var73 & 255) / 255.0F;
            float var70 = (float)(var73 >>> 24) / 255.0F;
            float var120 = var70;
            float var121 = 0.0F;
            float var77 = 0.0F;
            float var78 = 1.0F;

            float var79;
            float var80;
            for(var75 = 0; var75 < 8; ++var75) {
               var79 = var63 + var47[var75 * 4 + 0];
               var80 = var59 + var47[var75 * 4 + 1];
               int var81;
               if (var79 >= 0.0F && var80 >= 0.0F) {
                  int var82 = (int)(var79 * (float)var10);
                  int var83 = (int)(var80 * (float)var11);
                  boolean var84 = var82 >= var10 || var83 >= var11;
                  var81 = var84 ? 0 : var13[var83 * var12 + var82];
               } else {
                  var81 = 0;
               }

               var70 = (float)(var81 >>> 24) / 255.0F;
               var121 += var47[var75 * 4 + 2] * var70;
               var77 += var47[var75 * 4 + 3] * var70;
            }

            float var85 = (float)Math.sqrt((double)(var121 * var121 + var77 * var77 + var78 * var78));
            var79 = var121 / var85;
            var80 = var77 / var85;
            float var122 = var78 / var85;
            float var123 = var79;
            float var124 = var80;
            float var125 = var122;
            int var87;
            if (var63 >= 0.0F && var59 >= 0.0F) {
               int var88 = (int)(var63 * (float)var10);
               int var89 = (int)(var59 * (float)var11);
               boolean var90 = var88 >= var10 || var89 >= var11;
               var87 = var90 ? 0 : var13[var89 * var12 + var88];
            } else {
               var87 = 0;
            }

            var70 = (float)(var87 >>> 24) / 255.0F;
            float var126 = var54 * var70;
            float var127 = var42 - var66;
            float var128 = var43 - var62;
            float var91 = var44 - var126;
            float var92 = (float)Math.sqrt((double)(var127 * var127 + var128 * var128 + var91 * var91));
            var79 = var127 / var92;
            var80 = var128 / var92;
            var122 = var91 / var92;
            var127 = var79;
            var128 = var80;
            var91 = var122;
            float var95 = 0.0F;
            float var96 = 0.0F;
            float var97 = 1.0F;
            float var98 = var79 + var95;
            float var99 = var80 + var96;
            float var100 = var122 + var97;
            float var101 = (float)Math.sqrt((double)(var98 * var98 + var99 * var99 + var100 * var100));
            var79 = var98 / var101;
            var80 = var99 / var101;
            var122 = var100 / var101;
            float var105 = var123 * var127 + var124 * var128 + var125 * var91;
            var101 = var48 * var105 * var50;
            float var102 = var48 * var105 * var51;
            float var103 = var48 * var105 * var52;
            float var112 = 0.0F;
            float var113 = 1.0F;
            float var106 = var101 < var112 ? var112 : (var101 > var113 ? var113 : var101);
            float var107 = var102 < var112 ? var112 : (var102 > var113 ? var113 : var102);
            float var108 = var103 < var112 ? var112 : (var103 > var113 ? var113 : var103);
            float var104 = 1.0F;
            var105 = var123 * var79 + var124 * var80 + var125 * var122;
            float var114 = (float)Math.pow((double)var105, (double)var45);
            float var109 = var53 * var114 * var50;
            float var110 = var53 * var114 * var51;
            float var111 = var53 * var114 * var52;
            float var115 = var109 > var110 ? var109 : var110;
            var115 = var115 > var111 ? var115 : var111;
            float var71 = var67 * var106;
            float var72 = var68 * var107;
            float var119 = var69 * var108;
            var120 *= var104;
            var109 *= var120;
            var110 *= var120;
            var111 *= var120;
            var112 = var115 * var120;
            float var37 = var109 + var71 * (1.0F - var112);
            float var38 = var110 + var72 * (1.0F - var112);
            float var39 = var111 + var119 * (1.0F - var112);
            float var40 = var112 + var120 * (1.0F - var112);
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

            var35[var36 + var65] = (int)(var37 * 255.0F) << 16 | (int)(var38 * 255.0F) << 8 | (int)(var39 * 255.0F) << 0 | (int)(var40 * 255.0F) << 24;
            var63 += var55;
            var64 += var57;
         }

         var59 += var56;
         var60 += var58;
      }

      var5[0].releaseTransformedImage(var7);
      var5[1].releaseTransformedImage(var17);
      return new ImageData(this.getFilterContext(), var33, var6);
   }
}
