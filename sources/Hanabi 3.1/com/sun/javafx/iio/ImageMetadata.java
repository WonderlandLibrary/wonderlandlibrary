package com.sun.javafx.iio;

public class ImageMetadata {
   public final Float gamma;
   public final Boolean blackIsZero;
   public final Integer backgroundIndex;
   public final Integer backgroundColor;
   public final Integer delayTime;
   public final Integer loopCount;
   public final Integer transparentIndex;
   public final Integer imageWidth;
   public final Integer imageHeight;
   public final Integer imageLeftPosition;
   public final Integer imageTopPosition;
   public final Integer disposalMethod;

   public ImageMetadata(Float var1, Boolean var2, Integer var3, Integer var4, Integer var5, Integer var6, Integer var7, Integer var8, Integer var9, Integer var10, Integer var11, Integer var12) {
      this.gamma = var1;
      this.blackIsZero = var2;
      this.backgroundIndex = var3;
      this.backgroundColor = var4;
      this.transparentIndex = var5;
      this.delayTime = var6;
      this.loopCount = var7;
      this.imageWidth = var8;
      this.imageHeight = var9;
      this.imageLeftPosition = var10;
      this.imageTopPosition = var11;
      this.disposalMethod = var12;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("[" + this.getClass().getName());
      if (this.gamma != null) {
         var1.append(" gamma: " + this.gamma);
      }

      if (this.blackIsZero != null) {
         var1.append(" blackIsZero: " + this.blackIsZero);
      }

      if (this.backgroundIndex != null) {
         var1.append(" backgroundIndex: " + this.backgroundIndex);
      }

      if (this.backgroundColor != null) {
         var1.append(" backgroundColor: " + this.backgroundColor);
      }

      if (this.delayTime != null) {
         var1.append(" delayTime: " + this.delayTime);
      }

      if (this.loopCount != null) {
         var1.append(" loopCount: " + this.loopCount);
      }

      if (this.transparentIndex != null) {
         var1.append(" transparentIndex: " + this.transparentIndex);
      }

      if (this.imageWidth != null) {
         var1.append(" imageWidth: " + this.imageWidth);
      }

      if (this.imageHeight != null) {
         var1.append(" imageHeight: " + this.imageHeight);
      }

      if (this.imageLeftPosition != null) {
         var1.append(" imageLeftPosition: " + this.imageLeftPosition);
      }

      if (this.imageTopPosition != null) {
         var1.append(" imageTopPosition: " + this.imageTopPosition);
      }

      if (this.disposalMethod != null) {
         var1.append(" disposalMethod: " + this.disposalMethod);
      }

      var1.append("]");
      return var1.toString();
   }
}
