package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;

public final class AlignmentPattern extends ResultPoint {
   private final float estimatedModuleSize;

   AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
      super(posX, posY);
      this.estimatedModuleSize = estimatedModuleSize;
   }

   boolean aboutEquals(float moduleSize, float i, float j) {
      if (Math.abs(i - this.getY()) <= moduleSize && Math.abs(j - this.getX()) <= moduleSize) {
         float moduleSizeDiff = Math.abs(moduleSize - this.estimatedModuleSize);
         return moduleSizeDiff <= 1.0F || moduleSizeDiff <= this.estimatedModuleSize;
      } else {
         return false;
      }
   }

   AlignmentPattern combineEstimate(float i, float j, float newModuleSize) {
      float combinedX = (this.getX() + j) / 2.0F;
      float combinedY = (this.getY() + i) / 2.0F;
      float combinedModuleSize = (this.estimatedModuleSize + newModuleSize) / 2.0F;
      return new AlignmentPattern(combinedX, combinedY, combinedModuleSize);
   }
}
