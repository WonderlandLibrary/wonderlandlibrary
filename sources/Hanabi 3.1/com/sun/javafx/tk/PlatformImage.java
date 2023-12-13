package com.sun.javafx.tk;

import java.nio.Buffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public interface PlatformImage {
   float getPixelScale();

   int getArgb(int var1, int var2);

   void setArgb(int var1, int var2, int var3);

   PixelFormat getPlatformPixelFormat();

   boolean isWritable();

   PlatformImage promoteToWritableImage();

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7);

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8);

   void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, Buffer var6, int var7);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, byte[] var6, int var7, int var8);

   void setPixels(int var1, int var2, int var3, int var4, PixelFormat var5, int[] var6, int var7, int var8);

   void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);
}
