package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;

public final class FinderPattern extends ResultPoint {
   private final float estimatedModuleSize;
   private int count;

   FinderPattern(float posX, float posY, float estimatedModuleSize) {
      this(posX, posY, estimatedModuleSize, 1);
   }

   private FinderPattern(float posX, float posY, float estimatedModuleSize, int count) {
      super(posX, posY);
      this.estimatedModuleSize = estimatedModuleSize;
      this.count = count;
   }

   public float getEstimatedModuleSize() {
      return this.estimatedModuleSize;
   }

   int getCount() {
      return this.count;
   }

   void incrementCount() {
      ++this.count;
   }

   boolean aboutEquals(float moduleSize, float i, float j) {
      if (Math.abs(i - this.getY()) <= moduleSize && Math.abs(j - this.getX()) <= moduleSize) {
         float moduleSizeDiff = Math.abs(moduleSize - this.estimatedModuleSize);
         return moduleSizeDiff <= 1.0F || moduleSizeDiff <= this.estimatedModuleSize;
      } else {
         return false;
      }
   }

   FinderPattern combineEstimate(float i, float j, float newModuleSize) {
      int combinedCount = this.count + 1;
      float combinedX = ((float)this.count * this.getX() + j) / (float)combinedCount;
      float combinedY = ((float)this.count * this.getY() + i) / (float)combinedCount;
      float combinedModuleSize = ((float)this.count * this.estimatedModuleSize + newModuleSize) / (float)combinedCount;
      return new FinderPattern(combinedX, combinedY, combinedModuleSize, combinedCount);
   }
}
