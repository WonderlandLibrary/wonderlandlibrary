package com.google.zxing;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public final class BinaryBitmap {
   private final Binarizer binarizer;
   private BitMatrix matrix;

   public BinaryBitmap(Binarizer binarizer) {
      if (binarizer == null) {
         throw new IllegalArgumentException("Binarizer must be non-null.");
      } else {
         this.binarizer = binarizer;
      }
   }

   public int getWidth() {
      return this.binarizer.getWidth();
   }

   public int getHeight() {
      return this.binarizer.getHeight();
   }

   public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {
      return this.binarizer.getBlackRow(y, row);
   }

   public BitMatrix getBlackMatrix() throws NotFoundException {
      if (this.matrix == null) {
         this.matrix = this.binarizer.getBlackMatrix();
      }

      return this.matrix;
   }

   public boolean isCropSupported() {
      return this.binarizer.getLuminanceSource().isCropSupported();
   }

   public BinaryBitmap crop(int left, int top, int width, int height) {
      LuminanceSource newSource = this.binarizer.getLuminanceSource().crop(left, top, width, height);
      return new BinaryBitmap(this.binarizer.createBinarizer(newSource));
   }

   public boolean isRotateSupported() {
      return this.binarizer.getLuminanceSource().isRotateSupported();
   }

   public BinaryBitmap rotateCounterClockwise() {
      LuminanceSource newSource = this.binarizer.getLuminanceSource().rotateCounterClockwise();
      return new BinaryBitmap(this.binarizer.createBinarizer(newSource));
   }

   public BinaryBitmap rotateCounterClockwise45() {
      LuminanceSource newSource = this.binarizer.getLuminanceSource().rotateCounterClockwise45();
      return new BinaryBitmap(this.binarizer.createBinarizer(newSource));
   }
}
