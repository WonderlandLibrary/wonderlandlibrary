package com.google.zxing.qrcode.encoder;

final class MaskUtil {
   private static final int N1 = 3;
   private static final int N2 = 3;
   private static final int N3 = 40;
   private static final int N4 = 10;

   private MaskUtil() {
   }

   static int applyMaskPenaltyRule1(ByteMatrix matrix) {
      return applyMaskPenaltyRule1Internal(matrix, true) + applyMaskPenaltyRule1Internal(matrix, false);
   }

   static int applyMaskPenaltyRule2(ByteMatrix matrix) {
      int penalty = 0;
      byte[][] array = matrix.getArray();
      int width = matrix.getWidth();
      int height = matrix.getHeight();

      for(int y = 0; y < height - 1; ++y) {
         for(int x = 0; x < width - 1; ++x) {
            int value = array[y][x];
            if (value == array[y][x + 1] && value == array[y + 1][x] && value == array[y + 1][x + 1]) {
               ++penalty;
            }
         }
      }

      return 3 * penalty;
   }

   static int applyMaskPenaltyRule3(ByteMatrix matrix) {
      int penalty = 0;
      byte[][] array = matrix.getArray();
      int width = matrix.getWidth();
      int height = matrix.getHeight();

      for(int y = 0; y < height; ++y) {
         for(int x = 0; x < width; ++x) {
            if (x + 6 < width && array[y][x] == 1 && array[y][x + 1] == 0 && array[y][x + 2] == 1 && array[y][x + 3] == 1 && array[y][x + 4] == 1 && array[y][x + 5] == 0 && array[y][x + 6] == 1 && (x + 10 < width && array[y][x + 7] == 0 && array[y][x + 8] == 0 && array[y][x + 9] == 0 && array[y][x + 10] == 0 || x - 4 >= 0 && array[y][x - 1] == 0 && array[y][x - 2] == 0 && array[y][x - 3] == 0 && array[y][x - 4] == 0)) {
               penalty += 40;
            }

            if (y + 6 < height && array[y][x] == 1 && array[y + 1][x] == 0 && array[y + 2][x] == 1 && array[y + 3][x] == 1 && array[y + 4][x] == 1 && array[y + 5][x] == 0 && array[y + 6][x] == 1 && (y + 10 < height && array[y + 7][x] == 0 && array[y + 8][x] == 0 && array[y + 9][x] == 0 && array[y + 10][x] == 0 || y - 4 >= 0 && array[y - 1][x] == 0 && array[y - 2][x] == 0 && array[y - 3][x] == 0 && array[y - 4][x] == 0)) {
               penalty += 40;
            }
         }
      }

      return penalty;
   }

   static int applyMaskPenaltyRule4(ByteMatrix matrix) {
      int numDarkCells = 0;
      byte[][] array = matrix.getArray();
      int width = matrix.getWidth();
      int height = matrix.getHeight();

      int numTotalCells;
      for(numTotalCells = 0; numTotalCells < height; ++numTotalCells) {
         byte[] arrayY = array[numTotalCells];

         for(int x = 0; x < width; ++x) {
            if (arrayY[x] == 1) {
               ++numDarkCells;
            }
         }
      }

      numTotalCells = matrix.getHeight() * matrix.getWidth();
      double darkRatio = (double)numDarkCells / (double)numTotalCells;
      int fivePercentVariances = (int)(Math.abs(darkRatio - 0.5) * 20.0);
      return fivePercentVariances * 10;
   }

   static boolean getDataMaskBit(int maskPattern, int x, int y) {
      int intermediate;
      int temp;
      switch (maskPattern) {
         case 0:
            intermediate = y + x & 1;
            break;
         case 1:
            intermediate = y & 1;
            break;
         case 2:
            intermediate = x % 3;
            break;
         case 3:
            intermediate = (y + x) % 3;
            break;
         case 4:
            intermediate = (y >>> 1) + x / 3 & 1;
            break;
         case 5:
            temp = y * x;
            intermediate = (temp & 1) + temp % 3;
            break;
         case 6:
            temp = y * x;
            intermediate = (temp & 1) + temp % 3 & 1;
            break;
         case 7:
            temp = y * x;
            intermediate = temp % 3 + (y + x & 1) & 1;
            break;
         default:
            throw new IllegalArgumentException("Invalid mask pattern: " + maskPattern);
      }

      return intermediate == 0;
   }

   private static int applyMaskPenaltyRule1Internal(ByteMatrix matrix, boolean isHorizontal) {
      int penalty = 0;
      int iLimit = isHorizontal ? matrix.getHeight() : matrix.getWidth();
      int jLimit = isHorizontal ? matrix.getWidth() : matrix.getHeight();
      byte[][] array = matrix.getArray();

      for(int i = 0; i < iLimit; ++i) {
         int numSameBitCells = 0;
         int prevBit = -1;

         for(int j = 0; j < jLimit; ++j) {
            int bit = isHorizontal ? array[i][j] : array[j][i];
            if (bit == prevBit) {
               ++numSameBitCells;
            } else {
               if (numSameBitCells >= 5) {
                  penalty += 3 + (numSameBitCells - 5);
               }

               numSameBitCells = 1;
               prevBit = bit;
            }
         }

         if (numSameBitCells > 5) {
            penalty += 3 + (numSameBitCells - 5);
         }
      }

      return penalty;
   }
}
