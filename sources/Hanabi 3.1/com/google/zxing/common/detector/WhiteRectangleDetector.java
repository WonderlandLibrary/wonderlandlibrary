package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class WhiteRectangleDetector {
   private static final int INIT_SIZE = 30;
   private static final int CORR = 1;
   private final BitMatrix image;
   private final int height;
   private final int width;
   private final int leftInit;
   private final int rightInit;
   private final int downInit;
   private final int upInit;

   public WhiteRectangleDetector(BitMatrix image) throws NotFoundException {
      this.image = image;
      this.height = image.getHeight();
      this.width = image.getWidth();
      this.leftInit = this.width - 30 >> 1;
      this.rightInit = this.width + 30 >> 1;
      this.upInit = this.height - 30 >> 1;
      this.downInit = this.height + 30 >> 1;
      if (this.upInit < 0 || this.leftInit < 0 || this.downInit >= this.height || this.rightInit >= this.width) {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public WhiteRectangleDetector(BitMatrix image, int initSize, int x, int y) throws NotFoundException {
      this.image = image;
      this.height = image.getHeight();
      this.width = image.getWidth();
      int halfsize = initSize >> 1;
      this.leftInit = x - halfsize;
      this.rightInit = x + halfsize;
      this.upInit = y - halfsize;
      this.downInit = y + halfsize;
      if (this.upInit < 0 || this.leftInit < 0 || this.downInit >= this.height || this.rightInit >= this.width) {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public ResultPoint[] detect() throws NotFoundException {
      int left = this.leftInit;
      int right = this.rightInit;
      int up = this.upInit;
      int down = this.downInit;
      boolean sizeExceeded = false;
      boolean aBlackPointFoundOnBorder = true;
      boolean atLeastOneBlackPointFoundOnBorder = false;

      while(aBlackPointFoundOnBorder) {
         aBlackPointFoundOnBorder = false;
         boolean rightBorderNotWhite = true;

         while(rightBorderNotWhite && right < this.width) {
            rightBorderNotWhite = this.containsBlackPoint(up, down, right, false);
            if (rightBorderNotWhite) {
               ++right;
               aBlackPointFoundOnBorder = true;
            }
         }

         if (right >= this.width) {
            sizeExceeded = true;
            break;
         }

         boolean bottomBorderNotWhite = true;

         while(bottomBorderNotWhite && down < this.height) {
            bottomBorderNotWhite = this.containsBlackPoint(left, right, down, true);
            if (bottomBorderNotWhite) {
               ++down;
               aBlackPointFoundOnBorder = true;
            }
         }

         if (down >= this.height) {
            sizeExceeded = true;
            break;
         }

         boolean leftBorderNotWhite = true;

         while(leftBorderNotWhite && left >= 0) {
            leftBorderNotWhite = this.containsBlackPoint(up, down, left, false);
            if (leftBorderNotWhite) {
               --left;
               aBlackPointFoundOnBorder = true;
            }
         }

         if (left < 0) {
            sizeExceeded = true;
            break;
         }

         boolean topBorderNotWhite = true;

         while(topBorderNotWhite && up >= 0) {
            topBorderNotWhite = this.containsBlackPoint(left, right, up, true);
            if (topBorderNotWhite) {
               --up;
               aBlackPointFoundOnBorder = true;
            }
         }

         if (up < 0) {
            sizeExceeded = true;
            break;
         }

         if (aBlackPointFoundOnBorder) {
            atLeastOneBlackPointFoundOnBorder = true;
         }
      }

      if (!sizeExceeded && atLeastOneBlackPointFoundOnBorder) {
         int maxSize = right - left;
         ResultPoint z = null;

         for(int i = 1; i < maxSize; ++i) {
            z = this.getBlackPointOnSegment((float)left, (float)(down - i), (float)(left + i), (float)down);
            if (z != null) {
               break;
            }
         }

         if (z == null) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            ResultPoint t = null;

            for(int i = 1; i < maxSize; ++i) {
               t = this.getBlackPointOnSegment((float)left, (float)(up + i), (float)(left + i), (float)up);
               if (t != null) {
                  break;
               }
            }

            if (t == null) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               ResultPoint x = null;

               for(int i = 1; i < maxSize; ++i) {
                  x = this.getBlackPointOnSegment((float)right, (float)(up + i), (float)(right - i), (float)up);
                  if (x != null) {
                     break;
                  }
               }

               if (x == null) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  ResultPoint y = null;

                  for(int i = 1; i < maxSize; ++i) {
                     y = this.getBlackPointOnSegment((float)right, (float)(down - i), (float)(right - i), (float)down);
                     if (y != null) {
                        break;
                     }
                  }

                  if (y == null) {
                     throw NotFoundException.getNotFoundInstance();
                  } else {
                     return this.centerEdges(y, z, x, t);
                  }
               }
            }
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private ResultPoint getBlackPointOnSegment(float aX, float aY, float bX, float bY) {
      int dist = MathUtils.round(MathUtils.distance(aX, aY, bX, bY));
      float xStep = (bX - aX) / (float)dist;
      float yStep = (bY - aY) / (float)dist;

      for(int i = 0; i < dist; ++i) {
         int x = MathUtils.round(aX + (float)i * xStep);
         int y = MathUtils.round(aY + (float)i * yStep);
         if (this.image.get(x, y)) {
            return new ResultPoint((float)x, (float)y);
         }
      }

      return null;
   }

   private ResultPoint[] centerEdges(ResultPoint y, ResultPoint z, ResultPoint x, ResultPoint t) {
      float yi = y.getX();
      float yj = y.getY();
      float zi = z.getX();
      float zj = z.getY();
      float xi = x.getX();
      float xj = x.getY();
      float ti = t.getX();
      float tj = t.getY();
      return yi < (float)(this.width / 2) ? new ResultPoint[]{new ResultPoint(ti - 1.0F, tj + 1.0F), new ResultPoint(zi + 1.0F, zj + 1.0F), new ResultPoint(xi - 1.0F, xj - 1.0F), new ResultPoint(yi + 1.0F, yj - 1.0F)} : new ResultPoint[]{new ResultPoint(ti + 1.0F, tj + 1.0F), new ResultPoint(zi + 1.0F, zj - 1.0F), new ResultPoint(xi - 1.0F, xj + 1.0F), new ResultPoint(yi - 1.0F, yj - 1.0F)};
   }

   private boolean containsBlackPoint(int a, int b, int fixed, boolean horizontal) {
      int x;
      if (horizontal) {
         for(x = a; x <= b; ++x) {
            if (this.image.get(x, fixed)) {
               return true;
            }
         }
      } else {
         for(x = a; x <= b; ++x) {
            if (this.image.get(fixed, x)) {
               return true;
            }
         }
      }

      return false;
   }
}
