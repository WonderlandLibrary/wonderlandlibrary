package com.sun.javafx.iio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ImageFrame {
   private ImageStorage.ImageType imageType;
   private ByteBuffer imageData;
   private int width;
   private int height;
   private int stride;
   private float pixelScale;
   private byte[][] palette;
   private ImageMetadata metadata;

   public ImageFrame(ImageStorage.ImageType var1, ByteBuffer var2, int var3, int var4, int var5, byte[][] var6, ImageMetadata var7) {
      this(var1, var2, var3, var4, var5, var6, 1.0F, var7);
   }

   public ImageFrame(ImageStorage.ImageType var1, ByteBuffer var2, int var3, int var4, int var5, byte[][] var6, float var7, ImageMetadata var8) {
      this.imageType = var1;
      this.imageData = var2;
      this.width = var3;
      this.height = var4;
      this.stride = var5;
      this.palette = var6;
      this.pixelScale = var7;
      this.metadata = var8;
   }

   public ImageStorage.ImageType getImageType() {
      return this.imageType;
   }

   public Buffer getImageData() {
      return this.imageData;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getStride() {
      return this.stride;
   }

   public byte[][] getPalette() {
      return this.palette;
   }

   public void setPixelScale(float var1) {
      this.pixelScale = var1;
   }

   public float getPixelScale() {
      return this.pixelScale;
   }

   public ImageMetadata getMetadata() {
      return this.metadata;
   }
}
