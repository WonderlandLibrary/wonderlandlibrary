package com.google.zxing.pdf417.detector;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.GridSampler;
import com.google.zxing.common.detector.MathUtils;
import java.util.Arrays;
import java.util.Map;

public final class Detector {
   private static final int INTEGER_MATH_SHIFT = 8;
   private static final int PATTERN_MATCH_RESULT_SCALE_FACTOR = 256;
   private static final int MAX_AVG_VARIANCE = 107;
   private static final int MAX_INDIVIDUAL_VARIANCE = 204;
   private static final int SKEW_THRESHOLD = 3;
   private static final int[] START_PATTERN = new int[]{8, 1, 1, 1, 1, 1, 1, 3};
   private static final int[] START_PATTERN_REVERSE = new int[]{3, 1, 1, 1, 1, 1, 1, 8};
   private static final int[] STOP_PATTERN = new int[]{7, 1, 1, 3, 1, 1, 1, 2, 1};
   private static final int[] STOP_PATTERN_REVERSE = new int[]{1, 2, 1, 1, 1, 3, 1, 1, 7};
   private final BinaryBitmap image;

   public Detector(BinaryBitmap image) {
      this.image = image;
   }

   public DetectorResult detect() throws NotFoundException {
      return this.detect((Map)null);
   }

   public DetectorResult detect(Map hints) throws NotFoundException {
      BitMatrix matrix = this.image.getBlackMatrix();
      boolean tryHarder = hints != null && hints.containsKey(DecodeHintType.TRY_HARDER);
      ResultPoint[] vertices = findVertices(matrix, tryHarder);
      if (vertices == null) {
         vertices = findVertices180(matrix, tryHarder);
         if (vertices != null) {
            correctCodeWordVertices(vertices, true);
         }
      } else {
         correctCodeWordVertices(vertices, false);
      }

      if (vertices == null) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         float moduleWidth = computeModuleWidth(vertices);
         if (moduleWidth < 1.0F) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            int dimension = computeDimension(vertices[4], vertices[6], vertices[5], vertices[7], moduleWidth);
            if (dimension < 1) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               int ydimension = computeYDimension(vertices[4], vertices[6], vertices[5], vertices[7], moduleWidth);
               ydimension = ydimension > dimension ? ydimension : dimension;
               BitMatrix bits = sampleGrid(matrix, vertices[4], vertices[5], vertices[6], vertices[7], dimension, ydimension);
               return new DetectorResult(bits, new ResultPoint[]{vertices[5], vertices[4], vertices[6], vertices[7]});
            }
         }
      }
   }

   private static ResultPoint[] findVertices(BitMatrix matrix, boolean tryHarder) {
      int height = matrix.getHeight();
      int width = matrix.getWidth();
      ResultPoint[] result = new ResultPoint[8];
      boolean found = false;
      int[] counters = new int[START_PATTERN.length];
      int rowStep = Math.max(1, height >> (tryHarder ? 9 : 7));

      int i;
      int[] loc;
      for(i = 0; i < height; i += rowStep) {
         loc = findGuardPattern(matrix, 0, i, width, false, START_PATTERN, counters);
         if (loc != null) {
            result[0] = new ResultPoint((float)loc[0], (float)i);
            result[4] = new ResultPoint((float)loc[1], (float)i);
            found = true;
            break;
         }
      }

      if (found) {
         found = false;

         for(i = height - 1; i > 0; i -= rowStep) {
            loc = findGuardPattern(matrix, 0, i, width, false, START_PATTERN, counters);
            if (loc != null) {
               result[1] = new ResultPoint((float)loc[0], (float)i);
               result[5] = new ResultPoint((float)loc[1], (float)i);
               found = true;
               break;
            }
         }
      }

      counters = new int[STOP_PATTERN.length];
      if (found) {
         found = false;

         for(i = 0; i < height; i += rowStep) {
            loc = findGuardPattern(matrix, 0, i, width, false, STOP_PATTERN, counters);
            if (loc != null) {
               result[2] = new ResultPoint((float)loc[1], (float)i);
               result[6] = new ResultPoint((float)loc[0], (float)i);
               found = true;
               break;
            }
         }
      }

      if (found) {
         found = false;

         for(i = height - 1; i > 0; i -= rowStep) {
            loc = findGuardPattern(matrix, 0, i, width, false, STOP_PATTERN, counters);
            if (loc != null) {
               result[3] = new ResultPoint((float)loc[1], (float)i);
               result[7] = new ResultPoint((float)loc[0], (float)i);
               found = true;
               break;
            }
         }
      }

      return found ? result : null;
   }

   private static ResultPoint[] findVertices180(BitMatrix matrix, boolean tryHarder) {
      int height = matrix.getHeight();
      int width = matrix.getWidth();
      int halfWidth = width >> 1;
      ResultPoint[] result = new ResultPoint[8];
      boolean found = false;
      int[] counters = new int[START_PATTERN_REVERSE.length];
      int rowStep = Math.max(1, height >> (tryHarder ? 9 : 7));

      int i;
      int[] loc;
      for(i = height - 1; i > 0; i -= rowStep) {
         loc = findGuardPattern(matrix, halfWidth, i, halfWidth, true, START_PATTERN_REVERSE, counters);
         if (loc != null) {
            result[0] = new ResultPoint((float)loc[1], (float)i);
            result[4] = new ResultPoint((float)loc[0], (float)i);
            found = true;
            break;
         }
      }

      if (found) {
         found = false;

         for(i = 0; i < height; i += rowStep) {
            loc = findGuardPattern(matrix, halfWidth, i, halfWidth, true, START_PATTERN_REVERSE, counters);
            if (loc != null) {
               result[1] = new ResultPoint((float)loc[1], (float)i);
               result[5] = new ResultPoint((float)loc[0], (float)i);
               found = true;
               break;
            }
         }
      }

      counters = new int[STOP_PATTERN_REVERSE.length];
      if (found) {
         found = false;

         for(i = height - 1; i > 0; i -= rowStep) {
            loc = findGuardPattern(matrix, 0, i, halfWidth, false, STOP_PATTERN_REVERSE, counters);
            if (loc != null) {
               result[2] = new ResultPoint((float)loc[0], (float)i);
               result[6] = new ResultPoint((float)loc[1], (float)i);
               found = true;
               break;
            }
         }
      }

      if (found) {
         found = false;

         for(i = 0; i < height; i += rowStep) {
            loc = findGuardPattern(matrix, 0, i, halfWidth, false, STOP_PATTERN_REVERSE, counters);
            if (loc != null) {
               result[3] = new ResultPoint((float)loc[0], (float)i);
               result[7] = new ResultPoint((float)loc[1], (float)i);
               found = true;
               break;
            }
         }
      }

      return found ? result : null;
   }

   private static void correctCodeWordVertices(ResultPoint[] vertices, boolean upsideDown) {
      float v0x = vertices[0].getX();
      float v0y = vertices[0].getY();
      float v2x = vertices[2].getX();
      float v2y = vertices[2].getY();
      float v4x = vertices[4].getX();
      float v4y = vertices[4].getY();
      float v6x = vertices[6].getX();
      float v6y = vertices[6].getY();
      float skew = v4y - v6y;
      if (upsideDown) {
         skew = -skew;
      }

      float v1x;
      float v1y;
      float v3x;
      float v3y;
      if (skew > 3.0F) {
         v1x = v6x - v0x;
         v1y = v6y - v0y;
         v3x = v1x * v1x + v1y * v1y;
         v3y = (v4x - v0x) * v1x / v3x;
         vertices[4] = new ResultPoint(v0x + v3y * v1x, v0y + v3y * v1y);
      } else if (-skew > 3.0F) {
         v1x = v2x - v4x;
         v1y = v2y - v4y;
         v3x = v1x * v1x + v1y * v1y;
         v3y = (v2x - v6x) * v1x / v3x;
         vertices[6] = new ResultPoint(v2x - v3y * v1x, v2y - v3y * v1y);
      }

      v1x = vertices[1].getX();
      v1y = vertices[1].getY();
      v3x = vertices[3].getX();
      v3y = vertices[3].getY();
      float v5x = vertices[5].getX();
      float v5y = vertices[5].getY();
      float v7x = vertices[7].getX();
      float v7y = vertices[7].getY();
      skew = v7y - v5y;
      if (upsideDown) {
         skew = -skew;
      }

      float deltax;
      float deltay;
      float delta2;
      float correction;
      if (skew > 3.0F) {
         deltax = v7x - v1x;
         deltay = v7y - v1y;
         delta2 = deltax * deltax + deltay * deltay;
         correction = (v5x - v1x) * deltax / delta2;
         vertices[5] = new ResultPoint(v1x + correction * deltax, v1y + correction * deltay);
      } else if (-skew > 3.0F) {
         deltax = v3x - v5x;
         deltay = v3y - v5y;
         delta2 = deltax * deltax + deltay * deltay;
         correction = (v3x - v7x) * deltax / delta2;
         vertices[7] = new ResultPoint(v3x - correction * deltax, v3y - correction * deltay);
      }

   }

   private static float computeModuleWidth(ResultPoint[] vertices) {
      float pixels1 = ResultPoint.distance(vertices[0], vertices[4]);
      float pixels2 = ResultPoint.distance(vertices[1], vertices[5]);
      float moduleWidth1 = (pixels1 + pixels2) / 34.0F;
      float pixels3 = ResultPoint.distance(vertices[6], vertices[2]);
      float pixels4 = ResultPoint.distance(vertices[7], vertices[3]);
      float moduleWidth2 = (pixels3 + pixels4) / 36.0F;
      return (moduleWidth1 + moduleWidth2) / 2.0F;
   }

   private static int computeDimension(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft, ResultPoint bottomRight, float moduleWidth) {
      int topRowDimension = MathUtils.round(ResultPoint.distance(topLeft, topRight) / moduleWidth);
      int bottomRowDimension = MathUtils.round(ResultPoint.distance(bottomLeft, bottomRight) / moduleWidth);
      return ((topRowDimension + bottomRowDimension >> 1) + 8) / 17 * 17;
   }

   private static int computeYDimension(ResultPoint topLeft, ResultPoint topRight, ResultPoint bottomLeft, ResultPoint bottomRight, float moduleWidth) {
      int leftColumnDimension = MathUtils.round(ResultPoint.distance(topLeft, bottomLeft) / moduleWidth);
      int rightColumnDimension = MathUtils.round(ResultPoint.distance(topRight, bottomRight) / moduleWidth);
      return leftColumnDimension + rightColumnDimension >> 1;
   }

   private static BitMatrix sampleGrid(BitMatrix matrix, ResultPoint topLeft, ResultPoint bottomLeft, ResultPoint topRight, ResultPoint bottomRight, int xdimension, int ydimension) throws NotFoundException {
      GridSampler sampler = GridSampler.getInstance();
      return sampler.sampleGrid(matrix, xdimension, ydimension, 0.0F, 0.0F, (float)xdimension, 0.0F, (float)xdimension, (float)ydimension, 0.0F, (float)ydimension, topLeft.getX(), topLeft.getY(), topRight.getX(), topRight.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(), bottomLeft.getY());
   }

   private static int[] findGuardPattern(BitMatrix matrix, int column, int row, int width, boolean whiteFirst, int[] pattern, int[] counters) {
      Arrays.fill(counters, 0, counters.length, 0);
      int patternLength = pattern.length;
      boolean isWhite = whiteFirst;
      int counterPosition = 0;
      int patternStart = column;

      for(int x = column; x < column + width; ++x) {
         boolean pixel = matrix.get(x, row);
         if (pixel ^ isWhite) {
            int var10002 = counters[counterPosition]++;
         } else {
            if (counterPosition == patternLength - 1) {
               if (patternMatchVariance(counters, pattern, 204) < 107) {
                  return new int[]{patternStart, x};
               }

               patternStart += counters[0] + counters[1];
               System.arraycopy(counters, 2, counters, 0, patternLength - 2);
               counters[patternLength - 2] = 0;
               counters[patternLength - 1] = 0;
               --counterPosition;
            } else {
               ++counterPosition;
            }

            counters[counterPosition] = 1;
            isWhite = !isWhite;
         }
      }

      return null;
   }

   private static int patternMatchVariance(int[] counters, int[] pattern, int maxIndividualVariance) {
      int numCounters = counters.length;
      int total = 0;
      int patternLength = 0;

      int unitBarWidth;
      for(unitBarWidth = 0; unitBarWidth < numCounters; ++unitBarWidth) {
         total += counters[unitBarWidth];
         patternLength += pattern[unitBarWidth];
      }

      if (total < patternLength) {
         return Integer.MAX_VALUE;
      } else {
         unitBarWidth = (total << 8) / patternLength;
         maxIndividualVariance = maxIndividualVariance * unitBarWidth >> 8;
         int totalVariance = 0;

         for(int x = 0; x < numCounters; ++x) {
            int counter = counters[x] << 8;
            int scaledPattern = pattern[x] * unitBarWidth;
            int variance = counter > scaledPattern ? counter - scaledPattern : scaledPattern - counter;
            if (variance > maxIndividualVariance) {
               return Integer.MAX_VALUE;
            }

            totalVariance += variance;
         }

         return totalVariance / total;
      }
   }
}
