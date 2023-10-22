package com.google.zxing;

public abstract class LuminanceSource {
   private final int width;
   private final int height;

   protected LuminanceSource(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public abstract byte[] getRow(int var1, byte[] var2);

   public abstract byte[] getMatrix();

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public boolean isCropSupported() {
      return false;
   }

   public LuminanceSource crop(int left, int top, int width, int height) {
      throw new UnsupportedOperationException("This luminance source does not support cropping.");
   }

   public boolean isRotateSupported() {
      return false;
   }

   public LuminanceSource rotateCounterClockwise() {
      throw new UnsupportedOperationException("This luminance source does not support rotation by 90 degrees.");
   }

   public LuminanceSource rotateCounterClockwise45() {
      throw new UnsupportedOperationException("This luminance source does not support rotation by 45 degrees.");
   }

   public final String toString() {
      byte[] row = new byte[this.width];
      StringBuilder result = new StringBuilder(this.height * (this.width + 1));

      for(int y = 0; y < this.height; ++y) {
         row = this.getRow(y, row);

         for(int x = 0; x < this.width; ++x) {
            int luminance = row[x] & 255;
            char c;
            if (luminance < 64) {
               c = '#';
            } else if (luminance < 128) {
               c = '+';
            } else if (luminance < 192) {
               c = '.';
            } else {
               c = ' ';
            }

            result.append(c);
         }

         result.append('\n');
      }

      return result.toString();
   }
}
