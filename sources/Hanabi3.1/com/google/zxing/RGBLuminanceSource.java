package com.google.zxing;

public final class RGBLuminanceSource extends LuminanceSource {
   private final byte[] luminances;
   private final int dataWidth;
   private final int dataHeight;
   private final int left;
   private final int top;

   public RGBLuminanceSource(int width, int height, int[] pixels) {
      super(width, height);
      this.dataWidth = width;
      this.dataHeight = height;
      this.left = 0;
      this.top = 0;
      this.luminances = new byte[width * height];

      for(int y = 0; y < height; ++y) {
         int offset = y * width;

         for(int x = 0; x < width; ++x) {
            int pixel = pixels[offset + x];
            int r = pixel >> 16 & 255;
            int g = pixel >> 8 & 255;
            int b = pixel & 255;
            if (r == g && g == b) {
               this.luminances[offset + x] = (byte)r;
            } else {
               this.luminances[offset + x] = (byte)(r + g + g + b >> 2);
            }
         }
      }

   }

   private RGBLuminanceSource(byte[] pixels, int dataWidth, int dataHeight, int left, int top, int width, int height) {
      super(width, height);
      if (left + width <= dataWidth && top + height <= dataHeight) {
         this.luminances = pixels;
         this.dataWidth = dataWidth;
         this.dataHeight = dataHeight;
         this.left = left;
         this.top = top;
      } else {
         throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
      }
   }

   public byte[] getRow(int y, byte[] row) {
      if (y >= 0 && y < this.getHeight()) {
         int width = this.getWidth();
         if (row == null || row.length < width) {
            row = new byte[width];
         }

         int offset = (y + this.top) * this.dataWidth + this.left;
         System.arraycopy(this.luminances, offset, row, 0, width);
         return row;
      } else {
         throw new IllegalArgumentException("Requested row is outside the image: " + y);
      }
   }

   public byte[] getMatrix() {
      int width = this.getWidth();
      int height = this.getHeight();
      if (width == this.dataWidth && height == this.dataHeight) {
         return this.luminances;
      } else {
         int area = width * height;
         byte[] matrix = new byte[area];
         int inputOffset = this.top * this.dataWidth + this.left;
         if (width == this.dataWidth) {
            System.arraycopy(this.luminances, inputOffset, matrix, 0, area);
            return matrix;
         } else {
            byte[] rgb = this.luminances;

            for(int y = 0; y < height; ++y) {
               int outputOffset = y * width;
               System.arraycopy(rgb, inputOffset, matrix, outputOffset, width);
               inputOffset += this.dataWidth;
            }

            return matrix;
         }
      }
   }

   public boolean isCropSupported() {
      return true;
   }

   public LuminanceSource crop(int left, int top, int width, int height) {
      return new RGBLuminanceSource(this.luminances, this.dataWidth, this.dataHeight, this.left + left, this.top + top, width, height);
   }
}
