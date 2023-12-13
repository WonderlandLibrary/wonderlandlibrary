package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public final class Code128Writer extends OneDimensionalCodeWriter {
   private static final int CODE_START_B = 104;
   private static final int CODE_START_C = 105;
   private static final int CODE_CODE_B = 100;
   private static final int CODE_CODE_C = 99;
   private static final int CODE_STOP = 106;
   private static final char ESCAPE_FNC_1 = 'ñ';
   private static final char ESCAPE_FNC_2 = 'ò';
   private static final char ESCAPE_FNC_3 = 'ó';
   private static final char ESCAPE_FNC_4 = 'ô';
   private static final int CODE_FNC_1 = 102;
   private static final int CODE_FNC_2 = 97;
   private static final int CODE_FNC_3 = 96;
   private static final int CODE_FNC_4_B = 100;

   public BitMatrix encode(String contents, BarcodeFormat format, int width, int height, Map hints) throws WriterException {
      if (format != BarcodeFormat.CODE_128) {
         throw new IllegalArgumentException("Can only encode CODE_128, but got " + format);
      } else {
         return super.encode(contents, format, width, height, hints);
      }
   }

   public boolean[] encode(String contents) {
      int length = contents.length();
      if (length >= 1 && length <= 80) {
         int checkSum;
         for(int i = 0; i < length; ++i) {
            checkSum = contents.charAt(i);
            if (checkSum < 32 || checkSum > 126) {
               switch (checkSum) {
                  case 241:
                  case 242:
                  case 243:
                  case 244:
                     break;
                  default:
                     throw new IllegalArgumentException("Bad character in input: " + checkSum);
               }
            }
         }

         Collection patterns = new ArrayList();
         checkSum = 0;
         int checkWeight = 1;
         int codeSet = 0;
         int position = 0;

         int codeWidth;
         int patternIndex;
         while(position < length) {
            codeWidth = codeSet == 99 ? 2 : 4;
            byte newCodeSet;
            if (isDigits(contents, position, codeWidth)) {
               newCodeSet = 99;
            } else {
               newCodeSet = 100;
            }

            if (newCodeSet == codeSet) {
               if (codeSet == 100) {
                  patternIndex = contents.charAt(position) - 32;
                  ++position;
               } else {
                  switch (contents.charAt(position)) {
                     case 'ñ':
                        patternIndex = 102;
                        ++position;
                        break;
                     case 'ò':
                        patternIndex = 97;
                        ++position;
                        break;
                     case 'ó':
                        patternIndex = 96;
                        ++position;
                        break;
                     case 'ô':
                        patternIndex = 100;
                        ++position;
                        break;
                     default:
                        patternIndex = Integer.parseInt(contents.substring(position, position + 2));
                        position += 2;
                  }
               }
            } else {
               if (codeSet == 0) {
                  if (newCodeSet == 100) {
                     patternIndex = 104;
                  } else {
                     patternIndex = 105;
                  }
               } else {
                  patternIndex = newCodeSet;
               }

               codeSet = newCodeSet;
            }

            patterns.add(Code128Reader.CODE_PATTERNS[patternIndex]);
            checkSum += patternIndex * checkWeight;
            if (position != 0) {
               ++checkWeight;
            }
         }

         checkSum %= 103;
         patterns.add(Code128Reader.CODE_PATTERNS[checkSum]);
         patterns.add(Code128Reader.CODE_PATTERNS[106]);
         codeWidth = 0;
         Iterator i$ = patterns.iterator();

         while(i$.hasNext()) {
            int[] pattern = (int[])i$.next();
            int[] arr$ = pattern;
            int len$ = pattern.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               int width = arr$[i$];
               codeWidth += width;
            }
         }

         boolean[] result = new boolean[codeWidth];
         patternIndex = 0;

         int[] pattern;
         for(Iterator i$ = patterns.iterator(); i$.hasNext(); patternIndex += appendPattern(result, patternIndex, pattern, true)) {
            pattern = (int[])i$.next();
         }

         return result;
      } else {
         throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + length);
      }
   }

   private static boolean isDigits(CharSequence value, int start, int length) {
      int end = start + length;
      int last = value.length();

      for(int i = start; i < end && i < last; ++i) {
         char c = value.charAt(i);
         if (c < '0' || c > '9') {
            if (c != 241) {
               return false;
            }

            ++end;
         }
      }

      return end <= last;
   }
}
