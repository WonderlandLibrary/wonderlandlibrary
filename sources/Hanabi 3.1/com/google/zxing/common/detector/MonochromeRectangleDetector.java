package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class MonochromeRectangleDetector {
   private static final int MAX_MODULES = 32;
   private final BitMatrix image;

   public MonochromeRectangleDetector(BitMatrix image) {
      this.image = image;
   }

   public ResultPoint[] detect() throws NotFoundException {
      int height = this.image.getHeight();
      int width = this.image.getWidth();
      int halfHeight = height >> 1;
      int halfWidth = width >> 1;
      int deltaY = Math.max(1, height / 256);
      int deltaX = Math.max(1, width / 256);
      int top = 0;
      int left = 0;
      ResultPoint pointA = this.findCornerFromCenter(halfWidth, 0, left, width, halfHeight, -deltaY, top, height, halfWidth >> 1);
      top = (int)pointA.getY() - 1;
      ResultPoint pointB = this.findCornerFromCenter(halfWidth, -deltaX, left, width, halfHeight, 0, top, height, halfHeight >> 1);
      left = (int)pointB.getX() - 1;
      ResultPoint pointC = this.findCornerFromCenter(halfWidth, deltaX, left, width, halfHeight, 0, top, height, halfHeight >> 1);
      int right = (int)pointC.getX() + 1;
      ResultPoint pointD = this.findCornerFromCenter(halfWidth, 0, left, right, halfHeight, deltaY, top, height, halfWidth >> 1);
      int bottom = (int)pointD.getY() + 1;
      pointA = this.findCornerFromCenter(halfWidth, 0, left, right, halfHeight, -deltaY, top, bottom, halfWidth >> 2);
      return new ResultPoint[]{pointA, pointB, pointC, pointD};
   }

   private ResultPoint findCornerFromCenter(int centerX, int deltaX, int left, int right, int centerY, int deltaY, int top, int bottom, int maxWhiteRun) throws NotFoundException {
      int[] lastRange = null;
      int y = centerY;

      for(int x = centerX; y < bottom && y >= top && x < right && x >= left; x += deltaX) {
         int[] range;
         if (deltaX == 0) {
            range = this.blackWhiteRange(y, maxWhiteRun, left, right, true);
         } else {
            range = this.blackWhiteRange(x, maxWhiteRun, top, bottom, false);
         }

         if (range == null) {
            if (lastRange == null) {
               throw NotFoundException.getNotFoundInstance();
            }

            int lastX;
            if (deltaX == 0) {
               lastX = y - deltaY;
               if (lastRange[0] < centerX) {
                  if (lastRange[1] > centerX) {
                     return new ResultPoint(deltaY > 0 ? (float)lastRange[0] : (float)lastRange[1], (float)lastX);
                  }

                  return new ResultPoint((float)lastRange[0], (float)lastX);
               }

               return new ResultPoint((float)lastRange[1], (float)lastX);
            }

            lastX = x - deltaX;
            if (lastRange[0] < centerY) {
               if (lastRange[1] > centerY) {
                  return new ResultPoint((float)lastX, deltaX < 0 ? (float)lastRange[0] : (float)lastRange[1]);
               }

               return new ResultPoint((float)lastX, (float)lastRange[0]);
            }

            return new ResultPoint((float)lastX, (float)lastRange[1]);
         }

         lastRange = range;
         y += deltaY;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private int[] blackWhiteRange(int fixedDimension, int maxWhiteRun, int minDim, int maxDim, boolean horizontal) {
      int center = minDim + maxDim >> 1;
      int start = center;

      int end;
      int whiteRunStart;
      while(start >= minDim) {
         label90: {
            if (horizontal) {
               if (!this.image.get(start, fixedDimension)) {
                  break label90;
               }
            } else if (!this.image.get(fixedDimension, start)) {
               break label90;
            }

            --start;
            continue;
         }

         end = start;

         while(true) {
            --start;
            if (start < minDim) {
               break;
            }

            if (horizontal) {
               if (this.image.get(start, fixedDimension)) {
                  break;
               }
            } else if (this.image.get(fixedDimension, start)) {
               break;
            }
         }

         whiteRunStart = end - start;
         if (start < minDim || whiteRunStart > maxWhiteRun) {
            start = end;
            break;
         }
      }

      ++start;
      end = center;

      while(end < maxDim) {
         label100: {
            if (horizontal) {
               if (this.image.get(end, fixedDimension)) {
                  break label100;
               }
            } else if (this.image.get(fixedDimension, end)) {
               break label100;
            }

            whiteRunStart = end;

            while(true) {
               ++end;
               if (end >= maxDim) {
                  break;
               }

               if (horizontal) {
                  if (this.image.get(end, fixedDimension)) {
                     break;
                  }
               } else if (this.image.get(fixedDimension, end)) {
                  break;
               }
            }

            int whiteRunSize = end - whiteRunStart;
            if (end < maxDim && whiteRunSize <= maxWhiteRun) {
               continue;
            }

            end = whiteRunStart;
            break;
         }

         ++end;
      }

      --end;
      return end > start ? new int[]{start, end} : null;
   }
}
