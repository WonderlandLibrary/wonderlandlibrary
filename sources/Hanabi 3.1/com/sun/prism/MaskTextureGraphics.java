package com.sun.prism;

public interface MaskTextureGraphics extends Graphics {
   void drawPixelsMasked(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

   void maskInterpolatePixels(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);
}
