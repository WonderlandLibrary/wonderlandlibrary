package com.sun.glass.ui;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public abstract class Pixels {
   protected final int width;
   protected final int height;
   protected final int bytesPerComponent;
   protected final ByteBuffer bytes;
   protected final IntBuffer ints;
   private final float scalex;
   private final float scaley;

   public static int getNativeFormat() {
      Application.checkEventThread();
      return Application.GetApplication().staticPixels_getNativeFormat();
   }

   protected Pixels(int var1, int var2, ByteBuffer var3) {
      this.width = var1;
      this.height = var2;
      this.bytesPerComponent = 1;
      this.bytes = var3.slice();
      if (this.width > 0 && this.height > 0 && this.width * this.height * 4 <= this.bytes.capacity()) {
         this.ints = null;
         this.scalex = 1.0F;
         this.scaley = 1.0F;
      } else {
         throw new IllegalArgumentException("Too small byte buffer size " + this.width + "x" + this.height + " [" + this.width * this.height * 4 + "] > " + this.bytes.capacity());
      }
   }

   protected Pixels(int var1, int var2, IntBuffer var3) {
      this.width = var1;
      this.height = var2;
      this.bytesPerComponent = 4;
      this.ints = var3.slice();
      if (this.width > 0 && this.height > 0 && this.width * this.height <= this.ints.capacity()) {
         this.bytes = null;
         this.scalex = 1.0F;
         this.scaley = 1.0F;
      } else {
         throw new IllegalArgumentException("Too small int buffer size " + this.width + "x" + this.height + " [" + this.width * this.height + "] > " + this.ints.capacity());
      }
   }

   protected Pixels(int var1, int var2, IntBuffer var3, float var4, float var5) {
      this.width = var1;
      this.height = var2;
      this.bytesPerComponent = 4;
      this.ints = var3.slice();
      if (this.width > 0 && this.height > 0 && this.width * this.height <= this.ints.capacity()) {
         this.bytes = null;
         this.scalex = var4;
         this.scaley = var5;
      } else {
         throw new IllegalArgumentException("Too small int buffer size " + this.width + "x" + this.height + " [" + this.width * this.height + "] > " + this.ints.capacity());
      }
   }

   public final float getScaleX() {
      Application.checkEventThread();
      return this.scalex;
   }

   public final float getScaleY() {
      Application.checkEventThread();
      return this.scaley;
   }

   public final float getScaleXUnsafe() {
      return this.scalex;
   }

   public final float getScaleYUnsafe() {
      return this.scaley;
   }

   public final int getWidth() {
      Application.checkEventThread();
      return this.width;
   }

   public final int getWidthUnsafe() {
      return this.width;
   }

   public final int getHeight() {
      Application.checkEventThread();
      return this.height;
   }

   public final int getHeightUnsafe() {
      return this.height;
   }

   public final int getBytesPerComponent() {
      Application.checkEventThread();
      return this.bytesPerComponent;
   }

   public final Buffer getPixels() {
      if (this.bytes != null) {
         this.bytes.rewind();
         return this.bytes;
      } else if (this.ints != null) {
         this.ints.rewind();
         return this.ints;
      } else {
         throw new RuntimeException("Unexpected Pixels state.");
      }
   }

   public final ByteBuffer asByteBuffer() {
      Application.checkEventThread();
      ByteBuffer var1 = ByteBuffer.allocateDirect(this.getWidth() * this.getHeight() * 4);
      var1.order(ByteOrder.nativeOrder());
      var1.rewind();
      this.asByteBuffer(var1);
      return var1;
   }

   public final void asByteBuffer(ByteBuffer var1) {
      Application.checkEventThread();
      if (!var1.isDirect()) {
         throw new RuntimeException("Expected direct buffer.");
      } else if (var1.remaining() < this.getWidth() * this.getHeight() * 4) {
         throw new RuntimeException("Too small buffer.");
      } else {
         this._fillDirectByteBuffer(var1);
      }
   }

   private void attachData(long var1) {
      if (this.ints != null) {
         int[] var3 = !this.ints.isDirect() ? this.ints.array() : null;
         this._attachInt(var1, this.width, this.height, this.ints, var3, var3 != null ? this.ints.arrayOffset() : 0);
      }

      if (this.bytes != null) {
         byte[] var4 = !this.bytes.isDirect() ? this.bytes.array() : null;
         this._attachByte(var1, this.width, this.height, this.bytes, var4, var4 != null ? this.bytes.arrayOffset() : 0);
      }

   }

   protected abstract void _fillDirectByteBuffer(ByteBuffer var1);

   protected abstract void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

   protected abstract void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);

   public final boolean equals(Object var1) {
      Application.checkEventThread();
      boolean var2 = var1 != null && this.getClass().equals(var1.getClass());
      if (var2) {
         Pixels var3 = (Pixels)var1;
         var2 = this.getWidth() == var3.getWidth() && this.getHeight() == var3.getHeight();
         if (var2) {
            ByteBuffer var4 = this.asByteBuffer();
            ByteBuffer var5 = var3.asByteBuffer();
            var2 = var4.compareTo(var5) == 0;
         }
      }

      return var2;
   }

   public final int hashCode() {
      Application.checkEventThread();
      int var1 = this.getWidth();
      var1 = 31 * var1 + this.getHeight();
      var1 = 17 * var1 + this.asByteBuffer().hashCode();
      return var1;
   }

   public static class Format {
      public static final int BYTE_BGRA_PRE = 1;
      public static final int BYTE_ARGB = 2;
   }
}
