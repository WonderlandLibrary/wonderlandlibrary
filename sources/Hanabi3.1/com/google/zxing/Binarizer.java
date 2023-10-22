package com.google.zxing;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

public abstract class Binarizer {
   private final LuminanceSource source;

   protected Binarizer(LuminanceSource source) {
      this.source = source;
   }

   public final LuminanceSource getLuminanceSource() {
      return this.source;
   }

   public abstract BitArray getBlackRow(int var1, BitArray var2) throws NotFoundException;

   public abstract BitMatrix getBlackMatrix() throws NotFoundException;

   public abstract Binarizer createBinarizer(LuminanceSource var1);

   public final int getWidth() {
      return this.source.getWidth();
   }

   public final int getHeight() {
      return this.source.getHeight();
   }
}
