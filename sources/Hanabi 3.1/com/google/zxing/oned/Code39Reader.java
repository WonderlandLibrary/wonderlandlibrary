package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Map;

public final class Code39Reader extends OneDReader {
   static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%";
   private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".toCharArray();
   static final int[] CHARACTER_ENCODINGS = new int[]{52, 289, 97, 352, 49, 304, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, 400, 208, 133, 388, 196, 148, 168, 162, 138, 42};
   private static final int ASTERISK_ENCODING;
   private final boolean usingCheckDigit;
   private final boolean extendedMode;

   public Code39Reader() {
      this.usingCheckDigit = false;
      this.extendedMode = false;
   }

   public Code39Reader(boolean usingCheckDigit) {
      this.usingCheckDigit = usingCheckDigit;
      this.extendedMode = false;
   }

   public Code39Reader(boolean usingCheckDigit, boolean extendedMode) {
      this.usingCheckDigit = usingCheckDigit;
      this.extendedMode = extendedMode;
   }

   public Result decodeRow(int rowNumber, BitArray row, Map hints) throws NotFoundException, ChecksumException, FormatException {
      int[] counters = new int[9];
      int[] start = findAsteriskPattern(row, counters);
      int nextStart = row.getNextSet(start[1]);
      int end = row.getSize();
      StringBuilder result = new StringBuilder(20);

      char decodedChar;
      int lastStart;
      int lastPatternSize;
      int[] arr$;
      int max;
      int total;
      int i;
      do {
         recordPattern(row, nextStart, counters);
         lastPatternSize = toNarrowWidePattern(counters);
         if (lastPatternSize < 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         decodedChar = patternToChar(lastPatternSize);
         result.append(decodedChar);
         lastStart = nextStart;
         arr$ = counters;
         max = counters.length;

         for(total = 0; total < max; ++total) {
            i = arr$[total];
            nextStart += i;
         }

         nextStart = row.getNextSet(nextStart);
      } while(decodedChar != '*');

      result.setLength(result.length() - 1);
      lastPatternSize = 0;
      arr$ = counters;
      max = counters.length;

      for(total = 0; total < max; ++total) {
         i = arr$[total];
         lastPatternSize += i;
      }

      int whiteSpaceAfterEnd = nextStart - lastStart - lastPatternSize;
      if (nextStart != end && whiteSpaceAfterEnd >> 1 < lastPatternSize) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         if (this.usingCheckDigit) {
            max = result.length() - 1;
            total = 0;

            for(i = 0; i < max; ++i) {
               total += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".indexOf(result.charAt(i));
            }

            if (result.charAt(max) != ALPHABET[total % 43]) {
               throw ChecksumException.getChecksumInstance();
            }

            result.setLength(max);
         }

         if (result.length() == 0) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String resultString;
            if (this.extendedMode) {
               resultString = decodeExtended(result);
            } else {
               resultString = result.toString();
            }

            float left = (float)(start[1] + start[0]) / 2.0F;
            float right = (float)(nextStart + lastStart) / 2.0F;
            return new Result(resultString, (byte[])null, new ResultPoint[]{new ResultPoint(left, (float)rowNumber), new ResultPoint(right, (float)rowNumber)}, BarcodeFormat.CODE_39);
         }
      }
   }

   private static int[] findAsteriskPattern(BitArray row, int[] counters) throws NotFoundException {
      int width = row.getSize();
      int rowOffset = row.getNextSet(0);
      int counterPosition = 0;
      int patternStart = rowOffset;
      boolean isWhite = false;
      int patternLength = counters.length;

      for(int i = rowOffset; i < width; ++i) {
         if (row.get(i) ^ isWhite) {
            int var10002 = counters[counterPosition]++;
         } else {
            if (counterPosition == patternLength - 1) {
               if (toNarrowWidePattern(counters) == ASTERISK_ENCODING && row.isRange(Math.max(0, patternStart - (i - patternStart >> 1)), patternStart, false)) {
                  return new int[]{patternStart, i};
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

   private static int toNarrowWidePattern(int[] counters) {
      int numCounters = counters.length;
      int maxNarrowCounter = 0;

      int wideCounters;
      do {
         int minCounter = Integer.MAX_VALUE;
         int[] arr$ = counters;
         int pattern = counters.length;

         int i;
         int counter;
         for(i = 0; i < pattern; ++i) {
            counter = arr$[i];
            if (counter < minCounter && counter > maxNarrowCounter) {
               minCounter = counter;
            }
         }

         maxNarrowCounter = minCounter;
         wideCounters = 0;
         int totalWideCountersWidth = 0;
         pattern = 0;

         for(i = 0; i < numCounters; ++i) {
            counter = counters[i];
            if (counters[i] > maxNarrowCounter) {
               pattern |= 1 << numCounters - 1 - i;
               ++wideCounters;
               totalWideCountersWidth += counter;
            }
         }

         if (wideCounters == 3) {
            for(i = 0; i < numCounters && wideCounters > 0; ++i) {
               counter = counters[i];
               if (counters[i] > maxNarrowCounter) {
                  --wideCounters;
                  if (counter << 1 >= totalWideCountersWidth) {
                     return -1;
                  }
               }
            }

            return pattern;
         }
      } while(wideCounters > 3);

      return -1;
   }

   private static char patternToChar(int pattern) throws NotFoundException {
      for(int i = 0; i < CHARACTER_ENCODINGS.length; ++i) {
         if (CHARACTER_ENCODINGS[i] == pattern) {
            return ALPHABET[i];
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static String decodeExtended(CharSequence encoded) throws FormatException {
      int length = encoded.length();
      StringBuilder decoded = new StringBuilder(length);

      for(int i = 0; i < length; ++i) {
         char c = encoded.charAt(i);
         if (c != '+' && c != '$' && c != '%' && c != '/') {
            decoded.append(c);
         } else {
            char next = encoded.charAt(i + 1);
            char decodedChar = 0;
            switch (c) {
               case '$':
                  if (next < 'A' || next > 'Z') {
                     throw FormatException.getFormatInstance();
                  }

                  decodedChar = (char)(next - 64);
                  break;
               case '%':
                  if (next >= 'A' && next <= 'E') {
                     decodedChar = (char)(next - 38);
                  } else {
                     if (next < 'F' || next > 'W') {
                        throw FormatException.getFormatInstance();
                     }

                     decodedChar = (char)(next - 11);
                  }
                  break;
               case '+':
                  if (next < 'A' || next > 'Z') {
                     throw FormatException.getFormatInstance();
                  }

                  decodedChar = (char)(next + 32);
                  break;
               case '/':
                  if (next >= 'A' && next <= 'O') {
                     decodedChar = (char)(next - 32);
                  } else {
                     if (next != 'Z') {
                        throw FormatException.getFormatInstance();
                     }

                     decodedChar = ':';
                  }
            }

            decoded.append(decodedChar);
            ++i;
         }
      }

      return decoded.toString();
   }

   static {
      ASTERISK_ENCODING = CHARACTER_ENCODINGS[39];
   }
}
