package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Code128Reader extends OneDReader {
   static final int[][] CODE_PATTERNS = new int[][]{{2, 1, 2, 2, 2, 2}, {2, 2, 2, 1, 2, 2}, {2, 2, 2, 2, 2, 1}, {1, 2, 1, 2, 2, 3}, {1, 2, 1, 3, 2, 2}, {1, 3, 1, 2, 2, 2}, {1, 2, 2, 2, 1, 3}, {1, 2, 2, 3, 1, 2}, {1, 3, 2, 2, 1, 2}, {2, 2, 1, 2, 1, 3}, {2, 2, 1, 3, 1, 2}, {2, 3, 1, 2, 1, 2}, {1, 1, 2, 2, 3, 2}, {1, 2, 2, 1, 3, 2}, {1, 2, 2, 2, 3, 1}, {1, 1, 3, 2, 2, 2}, {1, 2, 3, 1, 2, 2}, {1, 2, 3, 2, 2, 1}, {2, 2, 3, 2, 1, 1}, {2, 2, 1, 1, 3, 2}, {2, 2, 1, 2, 3, 1}, {2, 1, 3, 2, 1, 2}, {2, 2, 3, 1, 1, 2}, {3, 1, 2, 1, 3, 1}, {3, 1, 1, 2, 2, 2}, {3, 2, 1, 1, 2, 2}, {3, 2, 1, 2, 2, 1}, {3, 1, 2, 2, 1, 2}, {3, 2, 2, 1, 1, 2}, {3, 2, 2, 2, 1, 1}, {2, 1, 2, 1, 2, 3}, {2, 1, 2, 3, 2, 1}, {2, 3, 2, 1, 2, 1}, {1, 1, 1, 3, 2, 3}, {1, 3, 1, 1, 2, 3}, {1, 3, 1, 3, 2, 1}, {1, 1, 2, 3, 1, 3}, {1, 3, 2, 1, 1, 3}, {1, 3, 2, 3, 1, 1}, {2, 1, 1, 3, 1, 3}, {2, 3, 1, 1, 1, 3}, {2, 3, 1, 3, 1, 1}, {1, 1, 2, 1, 3, 3}, {1, 1, 2, 3, 3, 1}, {1, 3, 2, 1, 3, 1}, {1, 1, 3, 1, 2, 3}, {1, 1, 3, 3, 2, 1}, {1, 3, 3, 1, 2, 1}, {3, 1, 3, 1, 2, 1}, {2, 1, 1, 3, 3, 1}, {2, 3, 1, 1, 3, 1}, {2, 1, 3, 1, 1, 3}, {2, 1, 3, 3, 1, 1}, {2, 1, 3, 1, 3, 1}, {3, 1, 1, 1, 2, 3}, {3, 1, 1, 3, 2, 1}, {3, 3, 1, 1, 2, 1}, {3, 1, 2, 1, 1, 3}, {3, 1, 2, 3, 1, 1}, {3, 3, 2, 1, 1, 1}, {3, 1, 4, 1, 1, 1}, {2, 2, 1, 4, 1, 1}, {4, 3, 1, 1, 1, 1}, {1, 1, 1, 2, 2, 4}, {1, 1, 1, 4, 2, 2}, {1, 2, 1, 1, 2, 4}, {1, 2, 1, 4, 2, 1}, {1, 4, 1, 1, 2, 2}, {1, 4, 1, 2, 2, 1}, {1, 1, 2, 2, 1, 4}, {1, 1, 2, 4, 1, 2}, {1, 2, 2, 1, 1, 4}, {1, 2, 2, 4, 1, 1}, {1, 4, 2, 1, 1, 2}, {1, 4, 2, 2, 1, 1}, {2, 4, 1, 2, 1, 1}, {2, 2, 1, 1, 1, 4}, {4, 1, 3, 1, 1, 1}, {2, 4, 1, 1, 1, 2}, {1, 3, 4, 1, 1, 1}, {1, 1, 1, 2, 4, 2}, {1, 2, 1, 1, 4, 2}, {1, 2, 1, 2, 4, 1}, {1, 1, 4, 2, 1, 2}, {1, 2, 4, 1, 1, 2}, {1, 2, 4, 2, 1, 1}, {4, 1, 1, 2, 1, 2}, {4, 2, 1, 1, 1, 2}, {4, 2, 1, 2, 1, 1}, {2, 1, 2, 1, 4, 1}, {2, 1, 4, 1, 2, 1}, {4, 1, 2, 1, 2, 1}, {1, 1, 1, 1, 4, 3}, {1, 1, 1, 3, 4, 1}, {1, 3, 1, 1, 4, 1}, {1, 1, 4, 1, 1, 3}, {1, 1, 4, 3, 1, 1}, {4, 1, 1, 1, 1, 3}, {4, 1, 1, 3, 1, 1}, {1, 1, 3, 1, 4, 1}, {1, 1, 4, 1, 3, 1}, {3, 1, 1, 1, 4, 1}, {4, 1, 1, 1, 3, 1}, {2, 1, 1, 4, 1, 2}, {2, 1, 1, 2, 1, 4}, {2, 1, 1, 2, 3, 2}, {2, 3, 3, 1, 1, 1, 2}};
   private static final int MAX_AVG_VARIANCE = 64;
   private static final int MAX_INDIVIDUAL_VARIANCE = 179;
   private static final int CODE_SHIFT = 98;
   private static final int CODE_CODE_C = 99;
   private static final int CODE_CODE_B = 100;
   private static final int CODE_CODE_A = 101;
   private static final int CODE_FNC_1 = 102;
   private static final int CODE_FNC_2 = 97;
   private static final int CODE_FNC_3 = 96;
   private static final int CODE_FNC_4_A = 101;
   private static final int CODE_FNC_4_B = 100;
   private static final int CODE_START_A = 103;
   private static final int CODE_START_B = 104;
   private static final int CODE_START_C = 105;
   private static final int CODE_STOP = 106;

   private static int[] findStartPattern(BitArray row) throws NotFoundException {
      int width = row.getSize();
      int rowOffset = row.getNextSet(0);
      int counterPosition = 0;
      int[] counters = new int[6];
      int patternStart = rowOffset;
      boolean isWhite = false;
      int patternLength = counters.length;

      for(int i = rowOffset; i < width; ++i) {
         if (row.get(i) ^ isWhite) {
            int var10002 = counters[counterPosition]++;
         } else {
            if (counterPosition == patternLength - 1) {
               int bestVariance = 64;
               int bestMatch = -1;

               for(int startCode = 103; startCode <= 105; ++startCode) {
                  int variance = patternMatchVariance(counters, CODE_PATTERNS[startCode], 179);
                  if (variance < bestVariance) {
                     bestVariance = variance;
                     bestMatch = startCode;
                  }
               }

               if (bestMatch >= 0 && row.isRange(Math.max(0, patternStart - (i - patternStart) / 2), patternStart, false)) {
                  return new int[]{patternStart, i, bestMatch};
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

      throw NotFoundException.getNotFoundInstance();
   }

   private static int decodeCode(BitArray row, int[] counters, int rowOffset) throws NotFoundException {
      recordPattern(row, rowOffset, counters);
      int bestVariance = 64;
      int bestMatch = -1;

      for(int d = 0; d < CODE_PATTERNS.length; ++d) {
         int[] pattern = CODE_PATTERNS[d];
         int variance = patternMatchVariance(counters, pattern, 179);
         if (variance < bestVariance) {
            bestVariance = variance;
            bestMatch = d;
         }
      }

      if (bestMatch >= 0) {
         return bestMatch;
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   public Result decodeRow(int rowNumber, BitArray row, Map hints) throws NotFoundException, FormatException, ChecksumException {
      int[] startPatternInfo = findStartPattern(row);
      int startCode = startPatternInfo[2];
      int codeSet;
      switch (startCode) {
         case 103:
            codeSet = 101;
            break;
         case 104:
            codeSet = 100;
            break;
         case 105:
            codeSet = 99;
            break;
         default:
            throw FormatException.getFormatInstance();
      }

      boolean done = false;
      boolean isNextShifted = false;
      StringBuilder result = new StringBuilder(20);
      List rawCodes = new ArrayList(20);
      int lastStart = startPatternInfo[0];
      int nextStart = startPatternInfo[1];
      int[] counters = new int[6];
      int lastCode = 0;
      int code = 0;
      int checksumTotal = startCode;
      int multiplier = 0;
      boolean lastCharacterWasPrintable = true;

      int rawCodesSize;
      while(!done) {
         boolean unshift = isNextShifted;
         isNextShifted = false;
         lastCode = code;
         code = decodeCode(row, counters, nextStart);
         rawCodes.add((byte)code);
         if (code != 106) {
            lastCharacterWasPrintable = true;
         }

         if (code != 106) {
            ++multiplier;
            checksumTotal += multiplier * code;
         }

         lastStart = nextStart;
         int[] arr$ = counters;
         int len$ = counters.length;

         for(rawCodesSize = 0; rawCodesSize < len$; ++rawCodesSize) {
            int counter = arr$[rawCodesSize];
            nextStart += counter;
         }

         switch (code) {
            case 103:
            case 104:
            case 105:
               throw FormatException.getFormatInstance();
            default:
               label111:
               switch (codeSet) {
                  case 99:
                     if (code < 100) {
                        if (code < 10) {
                           result.append('0');
                        }

                        result.append(code);
                     } else {
                        if (code != 106) {
                           lastCharacterWasPrintable = false;
                        }

                        switch (code) {
                           case 100:
                              codeSet = 100;
                              break label111;
                           case 101:
                              codeSet = 101;
                           case 102:
                           case 103:
                           case 104:
                           case 105:
                           default:
                              break label111;
                           case 106:
                              done = true;
                        }
                     }
                     break;
                  case 100:
                     if (code < 96) {
                        result.append((char)(32 + code));
                     } else {
                        if (code != 106) {
                           lastCharacterWasPrintable = false;
                        }

                        switch (code) {
                           case 96:
                           case 97:
                           case 100:
                           case 102:
                           case 103:
                           case 104:
                           case 105:
                           default:
                              break label111;
                           case 98:
                              isNextShifted = true;
                              codeSet = 101;
                              break label111;
                           case 99:
                              codeSet = 99;
                              break label111;
                           case 101:
                              codeSet = 101;
                              break label111;
                           case 106:
                              done = true;
                        }
                     }
                     break;
                  case 101:
                     if (code < 64) {
                        result.append((char)(32 + code));
                     } else if (code < 96) {
                        result.append((char)(code - 64));
                     } else {
                        if (code != 106) {
                           lastCharacterWasPrintable = false;
                        }

                        switch (code) {
                           case 96:
                           case 97:
                           case 101:
                           case 102:
                           case 103:
                           case 104:
                           case 105:
                           default:
                              break;
                           case 98:
                              isNextShifted = true;
                              codeSet = 100;
                              break;
                           case 99:
                              codeSet = 99;
                              break;
                           case 100:
                              codeSet = 100;
                              break;
                           case 106:
                              done = true;
                        }
                     }
               }

               if (unshift) {
                  codeSet = codeSet == 101 ? 100 : 101;
               }
         }
      }

      nextStart = row.getNextUnset(nextStart);
      if (!row.isRange(nextStart, Math.min(row.getSize(), nextStart + (nextStart - lastStart) / 2), false)) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         checksumTotal -= multiplier * lastCode;
         if (checksumTotal % 103 != lastCode) {
            throw ChecksumException.getChecksumInstance();
         } else {
            int resultLength = result.length();
            if (resultLength == 0) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               if (resultLength > 0 && lastCharacterWasPrintable) {
                  if (codeSet == 99) {
                     result.delete(resultLength - 2, resultLength);
                  } else {
                     result.delete(resultLength - 1, resultLength);
                  }
               }

               float left = (float)(startPatternInfo[1] + startPatternInfo[0]) / 2.0F;
               float right = (float)(nextStart + lastStart) / 2.0F;
               rawCodesSize = rawCodes.size();
               byte[] rawBytes = new byte[rawCodesSize];

               for(int i = 0; i < rawCodesSize; ++i) {
                  rawBytes[i] = (Byte)rawCodes.get(i);
               }

               return new Result(result.toString(), rawBytes, new ResultPoint[]{new ResultPoint(left, (float)rowNumber), new ResultPoint(right, (float)rowNumber)}, BarcodeFormat.CODE_128);
            }
         }
      }
   }
}
