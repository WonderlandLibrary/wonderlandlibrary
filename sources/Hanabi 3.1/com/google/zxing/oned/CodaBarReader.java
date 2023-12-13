package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Map;

public final class CodaBarReader extends OneDReader {
   private static final int MAX_ACCEPTABLE = 512;
   private static final int PADDING = 384;
   private static final String ALPHABET_STRING = "0123456789-$:/.+ABCD";
   static final char[] ALPHABET = "0123456789-$:/.+ABCD".toCharArray();
   static final int[] CHARACTER_ENCODINGS = new int[]{3, 6, 9, 96, 18, 66, 33, 36, 48, 72, 12, 24, 69, 81, 84, 21, 26, 41, 11, 14};
   private static final int MIN_CHARACTER_LENGTH = 3;
   private static final char[] STARTEND_ENCODING = new char[]{'A', 'B', 'C', 'D'};
   private final StringBuilder decodeRowResult = new StringBuilder(20);
   private int[] counters = new int[80];
   private int counterLength = 0;

   public Result decodeRow(int rowNumber, BitArray row, Map hints) throws NotFoundException {
      this.setCounters(row);
      int startOffset = this.findStartPattern();
      int nextStart = startOffset;
      this.decodeRowResult.setLength(0);

      int trailingWhitespace;
      do {
         trailingWhitespace = this.toNarrowWidePattern(nextStart);
         if (trailingWhitespace == -1) {
            throw NotFoundException.getNotFoundInstance();
         }

         this.decodeRowResult.append((char)trailingWhitespace);
         nextStart += 8;
      } while((this.decodeRowResult.length() <= 1 || !arrayContains(STARTEND_ENCODING, ALPHABET[trailingWhitespace])) && nextStart < this.counterLength);

      trailingWhitespace = this.counters[nextStart - 1];
      int lastPatternSize = 0;

      int i;
      for(i = -8; i < -1; ++i) {
         lastPatternSize += this.counters[nextStart + i];
      }

      if (nextStart < this.counterLength && trailingWhitespace < lastPatternSize / 2) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         this.validatePattern(startOffset);

         for(i = 0; i < this.decodeRowResult.length(); ++i) {
            this.decodeRowResult.setCharAt(i, ALPHABET[this.decodeRowResult.charAt(i)]);
         }

         char startchar = this.decodeRowResult.charAt(0);
         if (!arrayContains(STARTEND_ENCODING, startchar)) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            char endchar = this.decodeRowResult.charAt(this.decodeRowResult.length() - 1);
            if (!arrayContains(STARTEND_ENCODING, endchar)) {
               throw NotFoundException.getNotFoundInstance();
            } else if (this.decodeRowResult.length() <= 3) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               this.decodeRowResult.deleteCharAt(this.decodeRowResult.length() - 1);
               this.decodeRowResult.deleteCharAt(0);
               int runningCount = 0;

               for(int i = 0; i < startOffset; ++i) {
                  runningCount += this.counters[i];
               }

               float left = (float)runningCount;

               for(int i = startOffset; i < nextStart - 1; ++i) {
                  runningCount += this.counters[i];
               }

               float right = (float)runningCount;
               return new Result(this.decodeRowResult.toString(), (byte[])null, new ResultPoint[]{new ResultPoint(left, (float)rowNumber), new ResultPoint(right, (float)rowNumber)}, BarcodeFormat.CODABAR);
            }
         }
      }
   }

   void validatePattern(int start) throws NotFoundException {
      int[] sizes = new int[]{0, 0, 0, 0};
      int[] counts = new int[]{0, 0, 0, 0};
      int end = this.decodeRowResult.length() - 1;
      int pos = start;
      int i = 0;

      while(true) {
         int pattern = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i)];

         int i;
         int pattern;
         for(i = 6; i >= 0; --i) {
            pattern = (i & 1) + (pattern & 1) * 2;
            sizes[pattern] += this.counters[pos + i];
            int var10002 = counts[pattern]++;
            pattern >>= 1;
         }

         if (i >= end) {
            int[] maxes = new int[4];
            int[] mins = new int[4];

            for(i = 0; i < 2; ++i) {
               mins[i] = 0;
               mins[i + 2] = (sizes[i] << 8) / counts[i] + (sizes[i + 2] << 8) / counts[i + 2] >> 1;
               maxes[i] = mins[i + 2];
               maxes[i + 2] = (sizes[i + 2] * 512 + 384) / counts[i + 2];
            }

            pos = start;
            i = 0;

            while(true) {
               pattern = CHARACTER_ENCODINGS[this.decodeRowResult.charAt(i)];

               for(int j = 6; j >= 0; --j) {
                  int category = (j & 1) + (pattern & 1) * 2;
                  int size = this.counters[pos + j] << 8;
                  if (size < mins[category] || size > maxes[category]) {
                     throw NotFoundException.getNotFoundInstance();
                  }

                  pattern >>= 1;
               }

               if (i >= end) {
                  return;
               }

               pos += 8;
               ++i;
            }
         }

         pos += 8;
         ++i;
      }
   }

   private void setCounters(BitArray row) throws NotFoundException {
      this.counterLength = 0;
      int i = row.getNextUnset(0);
      int end = row.getSize();
      if (i >= end) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         boolean isWhite = true;

         int count;
         for(count = 0; i < end; ++i) {
            if (row.get(i) ^ isWhite) {
               ++count;
            } else {
               this.counterAppend(count);
               count = 1;
               isWhite = !isWhite;
            }
         }

         this.counterAppend(count);
      }
   }

   private void counterAppend(int e) {
      this.counters[this.counterLength] = e;
      ++this.counterLength;
      if (this.counterLength >= this.counters.length) {
         int[] temp = new int[this.counterLength * 2];
         System.arraycopy(this.counters, 0, temp, 0, this.counterLength);
         this.counters = temp;
      }

   }

   private int findStartPattern() throws NotFoundException {
      for(int i = 1; i < this.counterLength; i += 2) {
         int charOffset = this.toNarrowWidePattern(i);
         if (charOffset != -1 && arrayContains(STARTEND_ENCODING, ALPHABET[charOffset])) {
            int patternSize = 0;

            for(int j = i; j < i + 7; ++j) {
               patternSize += this.counters[j];
            }

            if (i == 1 || this.counters[i - 1] >= patternSize / 2) {
               return i;
            }
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   static boolean arrayContains(char[] array, char key) {
      if (array != null) {
         char[] arr$ = array;
         int len$ = array.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            char c = arr$[i$];
            if (c == key) {
               return true;
            }
         }
      }

      return false;
   }

   private int toNarrowWidePattern(int position) {
      int end = position + 7;
      if (end >= this.counterLength) {
         return -1;
      } else {
         int[] maxes = new int[]{0, 0};
         int[] mins = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
         int[] thresholds = new int[]{0, 0};

         int i;
         int pattern;
         for(i = 0; i < 2; ++i) {
            for(pattern = position + i; pattern < end; pattern += 2) {
               if (this.counters[pattern] < mins[i]) {
                  mins[i] = this.counters[pattern];
               }

               if (this.counters[pattern] > maxes[i]) {
                  maxes[i] = this.counters[pattern];
               }
            }

            thresholds[i] = (mins[i] + maxes[i]) / 2;
         }

         i = 128;
         pattern = 0;

         int i;
         for(i = 0; i < 7; ++i) {
            int barOrSpace = i & 1;
            i >>= 1;
            if (this.counters[position + i] > thresholds[barOrSpace]) {
               pattern |= i;
            }
         }

         for(i = 0; i < CHARACTER_ENCODINGS.length; ++i) {
            if (CHARACTER_ENCODINGS[i] == pattern) {
               return i;
            }
         }

         return -1;
      }
   }
}
