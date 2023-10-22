package com.google.zxing.qrcode.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class AlignmentPatternFinder {
   private final BitMatrix image;
   private final List possibleCenters;
   private final int startX;
   private final int startY;
   private final int width;
   private final int height;
   private final float moduleSize;
   private final int[] crossCheckStateCount;
   private final ResultPointCallback resultPointCallback;

   AlignmentPatternFinder(BitMatrix image, int startX, int startY, int width, int height, float moduleSize, ResultPointCallback resultPointCallback) {
      this.image = image;
      this.possibleCenters = new ArrayList(5);
      this.startX = startX;
      this.startY = startY;
      this.width = width;
      this.height = height;
      this.moduleSize = moduleSize;
      this.crossCheckStateCount = new int[3];
      this.resultPointCallback = resultPointCallback;
   }

   AlignmentPattern find() throws NotFoundException {
      int startX = this.startX;
      int height = this.height;
      int maxJ = startX + this.width;
      int middleI = this.startY + (height >> 1);
      int[] stateCount = new int[3];

      for(int iGen = 0; iGen < height; ++iGen) {
         int i = middleI + ((iGen & 1) == 0 ? iGen + 1 >> 1 : -(iGen + 1 >> 1));
         stateCount[0] = 0;
         stateCount[1] = 0;
         stateCount[2] = 0;

         int j;
         for(j = startX; j < maxJ && !this.image.get(j, i); ++j) {
         }

         AlignmentPattern confirmed;
         for(int currentState = 0; j < maxJ; ++j) {
            int var10002;
            if (this.image.get(j, i)) {
               if (currentState == 1) {
                  var10002 = stateCount[currentState]++;
               } else if (currentState == 2) {
                  if (this.foundPatternCross(stateCount)) {
                     confirmed = this.handlePossibleCenter(stateCount, i, j);
                     if (confirmed != null) {
                        return confirmed;
                     }
                  }

                  stateCount[0] = stateCount[2];
                  stateCount[1] = 1;
                  stateCount[2] = 0;
                  currentState = 1;
               } else {
                  ++currentState;
                  var10002 = stateCount[currentState]++;
               }
            } else {
               if (currentState == 1) {
                  ++currentState;
               }

               var10002 = stateCount[currentState]++;
            }
         }

         if (this.foundPatternCross(stateCount)) {
            confirmed = this.handlePossibleCenter(stateCount, i, maxJ);
            if (confirmed != null) {
               return confirmed;
            }
         }
      }

      if (!this.possibleCenters.isEmpty()) {
         return (AlignmentPattern)this.possibleCenters.get(0);
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static float centerFromEnd(int[] stateCount, int end) {
      return (float)(end - stateCount[2]) - (float)stateCount[1] / 2.0F;
   }

   private boolean foundPatternCross(int[] stateCount) {
      float moduleSize = this.moduleSize;
      float maxVariance = moduleSize / 2.0F;

      for(int i = 0; i < 3; ++i) {
         if (Math.abs(moduleSize - (float)stateCount[i]) >= maxVariance) {
            return false;
         }
      }

      return true;
   }

   private float crossCheckVertical(int startI, int centerJ, int maxCount, int originalStateCountTotal) {
      BitMatrix image = this.image;
      int maxI = image.getHeight();
      int[] stateCount = this.crossCheckStateCount;
      stateCount[0] = 0;
      stateCount[1] = 0;
      stateCount[2] = 0;

      int var10002;
      int i;
      for(i = startI; i >= 0 && image.get(centerJ, i) && stateCount[1] <= maxCount; --i) {
         var10002 = stateCount[1]++;
      }

      if (i >= 0 && stateCount[1] <= maxCount) {
         while(i >= 0 && !image.get(centerJ, i) && stateCount[0] <= maxCount) {
            var10002 = stateCount[0]++;
            --i;
         }

         if (stateCount[0] > maxCount) {
            return Float.NaN;
         } else {
            for(i = startI + 1; i < maxI && image.get(centerJ, i) && stateCount[1] <= maxCount; ++i) {
               var10002 = stateCount[1]++;
            }

            if (i != maxI && stateCount[1] <= maxCount) {
               while(i < maxI && !image.get(centerJ, i) && stateCount[2] <= maxCount) {
                  var10002 = stateCount[2]++;
                  ++i;
               }

               if (stateCount[2] > maxCount) {
                  return Float.NaN;
               } else {
                  int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
                  if (5 * Math.abs(stateCountTotal - originalStateCountTotal) >= 2 * originalStateCountTotal) {
                     return Float.NaN;
                  } else {
                     return this.foundPatternCross(stateCount) ? centerFromEnd(stateCount, i) : Float.NaN;
                  }
               }
            } else {
               return Float.NaN;
            }
         }
      } else {
         return Float.NaN;
      }
   }

   private AlignmentPattern handlePossibleCenter(int[] stateCount, int i, int j) {
      int stateCountTotal = stateCount[0] + stateCount[1] + stateCount[2];
      float centerJ = centerFromEnd(stateCount, j);
      float centerI = this.crossCheckVertical(i, (int)centerJ, 2 * stateCount[1], stateCountTotal);
      if (!Float.isNaN(centerI)) {
         float estimatedModuleSize = (float)(stateCount[0] + stateCount[1] + stateCount[2]) / 3.0F;
         Iterator i$ = this.possibleCenters.iterator();

         while(i$.hasNext()) {
            AlignmentPattern center = (AlignmentPattern)i$.next();
            if (center.aboutEquals(estimatedModuleSize, centerI, centerJ)) {
               return center.combineEstimate(centerI, centerJ, estimatedModuleSize);
            }
         }

         AlignmentPattern point = new AlignmentPattern(centerJ, centerI, estimatedModuleSize);
         this.possibleCenters.add(point);
         if (this.resultPointCallback != null) {
            this.resultPointCallback.foundPossibleResultPoint(point);
         }
      }

      return null;
   }
}
