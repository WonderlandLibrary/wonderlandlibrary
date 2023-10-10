package org.lwjgl.util;

import java.io.Serializable;
import java.nio.ByteBuffer;

public final class Color implements ReadableColor, Serializable, WritableColor {
   static final long serialVersionUID = 1L;
   private byte red;
   private byte green;
   private byte blue;
   private byte alpha;

   public Color() {
      this((int)0, (int)0, (int)0, (int)255);
   }

   public Color(int var1, int var2, int var3) {
      this((int)var1, (int)var2, (int)var3, (int)255);
   }

   public Color(byte var1, byte var2, byte var3) {
      this(var1, var2, var3, (byte)-1);
   }

   public Color(int var1, int var2, int var3, int var4) {
      this.set(var1, var2, var3, var4);
   }

   public Color(byte var1, byte var2, byte var3, byte var4) {
      this.set(var1, var2, var3, var4);
   }

   public Color(ReadableColor var1) {
      this.setColor(var1);
   }

   public void set(int var1, int var2, int var3, int var4) {
      this.red = (byte)var1;
      this.green = (byte)var2;
      this.blue = (byte)var3;
      this.alpha = (byte)var4;
   }

   public void set(byte var1, byte var2, byte var3, byte var4) {
      this.red = var1;
      this.green = var2;
      this.blue = var3;
      this.alpha = var4;
   }

   public void set(int var1, int var2, int var3) {
      this.set((int)var1, (int)var2, (int)var3, (int)255);
   }

   public void set(byte var1, byte var2, byte var3) {
      this.set(var1, var2, var3, (byte)-1);
   }

   public int getRed() {
      return this.red & 255;
   }

   public int getGreen() {
      return this.green & 255;
   }

   public int getBlue() {
      return this.blue & 255;
   }

   public int getAlpha() {
      return this.alpha & 255;
   }

   public void setRed(int var1) {
      this.red = (byte)var1;
   }

   public void setGreen(int var1) {
      this.green = (byte)var1;
   }

   public void setBlue(int var1) {
      this.blue = (byte)var1;
   }

   public void setAlpha(int var1) {
      this.alpha = (byte)var1;
   }

   public void setRed(byte var1) {
      this.red = var1;
   }

   public void setGreen(byte var1) {
      this.green = var1;
   }

   public void setBlue(byte var1) {
      this.blue = var1;
   }

   public void setAlpha(byte var1) {
      this.alpha = var1;
   }

   public String toString() {
      return "Color [" + this.getRed() + ", " + this.getGreen() + ", " + this.getBlue() + ", " + this.getAlpha() + "]";
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof ReadableColor && ((ReadableColor)var1).getRed() == this.getRed() && ((ReadableColor)var1).getGreen() == this.getGreen() && ((ReadableColor)var1).getBlue() == this.getBlue() && ((ReadableColor)var1).getAlpha() == this.getAlpha();
   }

   public int hashCode() {
      return this.red << 24 | this.green << 16 | this.blue << 8 | this.alpha;
   }

   public byte getAlphaByte() {
      return this.alpha;
   }

   public byte getBlueByte() {
      return this.blue;
   }

   public byte getGreenByte() {
      return this.green;
   }

   public byte getRedByte() {
      return this.red;
   }

   public void writeRGBA(ByteBuffer var1) {
      var1.put(this.red);
      var1.put(this.green);
      var1.put(this.blue);
      var1.put(this.alpha);
   }

   public void writeRGB(ByteBuffer var1) {
      var1.put(this.red);
      var1.put(this.green);
      var1.put(this.blue);
   }

   public void writeABGR(ByteBuffer var1) {
      var1.put(this.alpha);
      var1.put(this.blue);
      var1.put(this.green);
      var1.put(this.red);
   }

   public void writeARGB(ByteBuffer var1) {
      var1.put(this.alpha);
      var1.put(this.red);
      var1.put(this.green);
      var1.put(this.blue);
   }

   public void writeBGR(ByteBuffer var1) {
      var1.put(this.blue);
      var1.put(this.green);
      var1.put(this.red);
   }

   public void writeBGRA(ByteBuffer var1) {
      var1.put(this.blue);
      var1.put(this.green);
      var1.put(this.red);
      var1.put(this.alpha);
   }

   public void readRGBA(ByteBuffer var1) {
      this.red = var1.get();
      this.green = var1.get();
      this.blue = var1.get();
      this.alpha = var1.get();
   }

   public void readRGB(ByteBuffer var1) {
      this.red = var1.get();
      this.green = var1.get();
      this.blue = var1.get();
   }

   public void readARGB(ByteBuffer var1) {
      this.alpha = var1.get();
      this.red = var1.get();
      this.green = var1.get();
      this.blue = var1.get();
   }

   public void readBGRA(ByteBuffer var1) {
      this.blue = var1.get();
      this.green = var1.get();
      this.red = var1.get();
      this.alpha = var1.get();
   }

   public void readBGR(ByteBuffer var1) {
      this.blue = var1.get();
      this.green = var1.get();
      this.red = var1.get();
   }

   public void readABGR(ByteBuffer var1) {
      this.alpha = var1.get();
      this.blue = var1.get();
      this.green = var1.get();
      this.red = var1.get();
   }

   public void setColor(ReadableColor var1) {
      this.red = var1.getRedByte();
      this.green = var1.getGreenByte();
      this.blue = var1.getBlueByte();
      this.alpha = var1.getAlphaByte();
   }

   public void fromHSB(float var1, float var2, float var3) {
      if (var2 == 0.0F) {
         this.red = this.green = this.blue = (byte)((int)(var3 * 255.0F + 0.5F));
      } else {
         float var4 = (var1 - (float)Math.floor((double)var1)) * 6.0F;
         float var5 = var4 - (float)Math.floor((double)var4);
         float var6 = var3 * (1.0F - var2);
         float var7 = var3 * (1.0F - var2 * var5);
         float var8 = var3 * (1.0F - var2 * (1.0F - var5));
         switch((int)var4) {
         case 0:
            this.red = (byte)((int)(var3 * 255.0F + 0.5F));
            this.green = (byte)((int)(var8 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var6 * 255.0F + 0.5F));
            break;
         case 1:
            this.red = (byte)((int)(var7 * 255.0F + 0.5F));
            this.green = (byte)((int)(var3 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var6 * 255.0F + 0.5F));
            break;
         case 2:
            this.red = (byte)((int)(var6 * 255.0F + 0.5F));
            this.green = (byte)((int)(var3 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var8 * 255.0F + 0.5F));
            break;
         case 3:
            this.red = (byte)((int)(var6 * 255.0F + 0.5F));
            this.green = (byte)((int)(var7 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var3 * 255.0F + 0.5F));
            break;
         case 4:
            this.red = (byte)((int)(var8 * 255.0F + 0.5F));
            this.green = (byte)((int)(var6 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var3 * 255.0F + 0.5F));
            break;
         case 5:
            this.red = (byte)((int)(var3 * 255.0F + 0.5F));
            this.green = (byte)((int)(var6 * 255.0F + 0.5F));
            this.blue = (byte)((int)(var7 * 255.0F + 0.5F));
         }
      }

   }

   public float[] toHSB(float[] var1) {
      int var2 = this.getRed();
      int var3 = this.getGreen();
      int var4 = this.getBlue();
      if (var1 == null) {
         var1 = new float[3];
      }

      int var5 = var2 <= var3 ? var3 : var2;
      if (var4 > var5) {
         var5 = var4;
      }

      int var6 = var2 >= var3 ? var3 : var2;
      if (var4 < var6) {
         var6 = var4;
      }

      float var7 = (float)var5 / 255.0F;
      float var8;
      if (var5 != 0) {
         var8 = (float)(var5 - var6) / (float)var5;
      } else {
         var8 = 0.0F;
      }

      float var9;
      if (var8 == 0.0F) {
         var9 = 0.0F;
      } else {
         float var10 = (float)(var5 - var2) / (float)(var5 - var6);
         float var11 = (float)(var5 - var3) / (float)(var5 - var6);
         float var12 = (float)(var5 - var4) / (float)(var5 - var6);
         if (var2 == var5) {
            var9 = var12 - var11;
         } else if (var3 == var5) {
            var9 = 2.0F + var10 - var12;
         } else {
            var9 = 4.0F + var11 - var10;
         }

         var9 /= 6.0F;
         if (var9 < 0.0F) {
            ++var9;
         }
      }

      var1[0] = var9;
      var1[1] = var8;
      var1[2] = var7;
      return var1;
   }
}
