package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.DecoderResult;
import java.math.BigInteger;
import java.util.List;

final class DecodedBitStreamParser {
   private static final int TEXT_COMPACTION_MODE_LATCH = 900;
   private static final int BYTE_COMPACTION_MODE_LATCH = 901;
   private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
   private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
   private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
   private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
   private static final int MACRO_PDF417_TERMINATOR = 922;
   private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
   private static final int MAX_NUMERIC_CODEWORDS = 15;
   private static final int PL = 25;
   private static final int LL = 27;
   private static final int AS = 27;
   private static final int ML = 28;
   private static final int AL = 28;
   private static final int PS = 29;
   private static final int PAL = 29;
   private static final char[] PUNCT_CHARS = new char[]{';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', '\r', '\t', ',', ':', '\n', '-', '.', '$', '/', '"', '|', '*', '(', ')', '?', '{', '}', '\''};
   private static final char[] MIXED_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', '\r', '\t', ',', ':', '#', '-', '.', '$', '/', '+', '%', '*', '=', '^'};
   private static final BigInteger[] EXP900 = new BigInteger[16];

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(int[] codewords) throws FormatException {
      StringBuilder result = new StringBuilder(100);
      int codeIndex = 1;

      for(int code = codewords[codeIndex++]; codeIndex < codewords[0]; code = codewords[codeIndex++]) {
         switch (code) {
            case 900:
               codeIndex = textCompaction(codewords, codeIndex, result);
               break;
            case 901:
               codeIndex = byteCompaction(code, codewords, codeIndex, result);
               break;
            case 902:
               codeIndex = numericCompaction(codewords, codeIndex, result);
               break;
            case 913:
               codeIndex = byteCompaction(code, codewords, codeIndex, result);
               break;
            case 924:
               codeIndex = byteCompaction(code, codewords, codeIndex, result);
               break;
            default:
               --codeIndex;
               codeIndex = textCompaction(codewords, codeIndex, result);
         }

         if (codeIndex >= codewords.length) {
            throw FormatException.getFormatInstance();
         }
      }

      if (result.length() == 0) {
         throw FormatException.getFormatInstance();
      } else {
         return new DecoderResult((byte[])null, result.toString(), (List)null, (String)null);
      }
   }

   private static int textCompaction(int[] codewords, int codeIndex, StringBuilder result) {
      int[] textCompactionData = new int[codewords[0] << 1];
      int[] byteCompactionData = new int[codewords[0] << 1];
      int index = 0;
      boolean end = false;

      while(codeIndex < codewords[0] && !end) {
         int code = codewords[codeIndex++];
         if (code < 900) {
            textCompactionData[index] = code / 30;
            textCompactionData[index + 1] = code % 30;
            index += 2;
         } else {
            switch (code) {
               case 900:
                  textCompactionData[index++] = 900;
                  break;
               case 901:
                  --codeIndex;
                  end = true;
                  break;
               case 902:
                  --codeIndex;
                  end = true;
                  break;
               case 913:
                  textCompactionData[index] = 913;
                  code = codewords[codeIndex++];
                  byteCompactionData[index] = code;
                  ++index;
                  break;
               case 924:
                  --codeIndex;
                  end = true;
            }
         }
      }

      decodeTextCompaction(textCompactionData, byteCompactionData, index, result);
      return codeIndex;
   }

   private static void decodeTextCompaction(int[] textCompactionData, int[] byteCompactionData, int length, StringBuilder result) {
      Mode subMode = DecodedBitStreamParser.Mode.ALPHA;
      Mode priorToShiftMode = DecodedBitStreamParser.Mode.ALPHA;

      for(int i = 0; i < length; ++i) {
         int subModeCh = textCompactionData[i];
         char ch = 0;
         switch (subMode) {
            case ALPHA:
               if (subModeCh < 26) {
                  ch = (char)(65 + subModeCh);
               } else if (subModeCh == 26) {
                  ch = ' ';
               } else if (subModeCh == 27) {
                  subMode = DecodedBitStreamParser.Mode.LOWER;
               } else if (subModeCh == 28) {
                  subMode = DecodedBitStreamParser.Mode.MIXED;
               } else if (subModeCh == 29) {
                  priorToShiftMode = subMode;
                  subMode = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               } else if (subModeCh == 913) {
                  result.append((char)byteCompactionData[i]);
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
               break;
            case LOWER:
               if (subModeCh < 26) {
                  ch = (char)(97 + subModeCh);
               } else if (subModeCh == 26) {
                  ch = ' ';
               } else if (subModeCh == 27) {
                  priorToShiftMode = subMode;
                  subMode = DecodedBitStreamParser.Mode.ALPHA_SHIFT;
               } else if (subModeCh == 28) {
                  subMode = DecodedBitStreamParser.Mode.MIXED;
               } else if (subModeCh == 29) {
                  priorToShiftMode = subMode;
                  subMode = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               } else if (subModeCh == 913) {
                  result.append((char)byteCompactionData[i]);
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
               break;
            case MIXED:
               if (subModeCh < 25) {
                  ch = MIXED_CHARS[subModeCh];
               } else if (subModeCh == 25) {
                  subMode = DecodedBitStreamParser.Mode.PUNCT;
               } else if (subModeCh == 26) {
                  ch = ' ';
               } else if (subModeCh == 27) {
                  subMode = DecodedBitStreamParser.Mode.LOWER;
               } else if (subModeCh == 28) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               } else if (subModeCh == 29) {
                  priorToShiftMode = subMode;
                  subMode = DecodedBitStreamParser.Mode.PUNCT_SHIFT;
               } else if (subModeCh == 913) {
                  result.append((char)byteCompactionData[i]);
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
               break;
            case PUNCT:
               if (subModeCh < 29) {
                  ch = PUNCT_CHARS[subModeCh];
               } else if (subModeCh == 29) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               } else if (subModeCh == 913) {
                  result.append((char)byteCompactionData[i]);
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
               break;
            case ALPHA_SHIFT:
               subMode = priorToShiftMode;
               if (subModeCh < 26) {
                  ch = (char)(65 + subModeCh);
               } else if (subModeCh == 26) {
                  ch = ' ';
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
               break;
            case PUNCT_SHIFT:
               subMode = priorToShiftMode;
               if (subModeCh < 29) {
                  ch = PUNCT_CHARS[subModeCh];
               } else if (subModeCh == 29) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               } else if (subModeCh == 913) {
                  result.append((char)byteCompactionData[i]);
               } else if (subModeCh == 900) {
                  subMode = DecodedBitStreamParser.Mode.ALPHA;
               }
         }

         if (ch != 0) {
            result.append(ch);
         }
      }

   }

   private static int byteCompaction(int mode, int[] codewords, int codeIndex, StringBuilder result) {
      int count;
      long value;
      int j;
      if (mode == 901) {
         count = 0;
         value = 0L;
         char[] decodedData = new char[6];
         int[] byteCompactedCodewords = new int[6];
         boolean end = false;
         j = codewords[codeIndex++];

         label123:
         while(true) {
            int j;
            do {
               label111:
               do {
                  while(codeIndex < codewords[0] && !end) {
                     byteCompactedCodewords[count++] = j;
                     value = 900L * value + (long)j;
                     j = codewords[codeIndex++];
                     if (j != 900 && j != 901 && j != 902 && j != 924 && j != 928 && j != 923 && j != 922) {
                        continue label111;
                     }

                     --codeIndex;
                     end = true;
                  }

                  if (codeIndex == codewords[0] && j < 900) {
                     byteCompactedCodewords[count++] = j;
                  }

                  for(j = 0; j < count; ++j) {
                     result.append((char)byteCompactedCodewords[j]);
                  }
                  break label123;
               } while(count % 5 != 0);
            } while(count <= 0);

            for(j = 0; j < 6; ++j) {
               decodedData[5 - j] = (char)((int)(value % 256L));
               value >>= 8;
            }

            result.append(decodedData);
            count = 0;
         }
      } else if (mode == 924) {
         count = 0;
         value = 0L;
         boolean end = false;

         while(true) {
            do {
               do {
                  if (codeIndex >= codewords[0] || end) {
                     return codeIndex;
                  }

                  int code = codewords[codeIndex++];
                  if (code < 900) {
                     ++count;
                     value = 900L * value + (long)code;
                  } else if (code == 900 || code == 901 || code == 902 || code == 924 || code == 928 || code == 923 || code == 922) {
                     --codeIndex;
                     end = true;
                  }
               } while(count % 5 != 0);
            } while(count <= 0);

            char[] decodedData = new char[6];

            for(j = 0; j < 6; ++j) {
               decodedData[5 - j] = (char)((int)(value & 255L));
               value >>= 8;
            }

            result.append(decodedData);
            count = 0;
         }
      }

      return codeIndex;
   }

   private static int numericCompaction(int[] codewords, int codeIndex, StringBuilder result) throws FormatException {
      int count = 0;
      boolean end = false;
      int[] numericCodewords = new int[15];

      while(codeIndex < codewords[0] && !end) {
         int code = codewords[codeIndex++];
         if (codeIndex == codewords[0]) {
            end = true;
         }

         if (code < 900) {
            numericCodewords[count] = code;
            ++count;
         } else if (code == 900 || code == 901 || code == 924 || code == 928 || code == 923 || code == 922) {
            --codeIndex;
            end = true;
         }

         if (count % 15 == 0 || code == 902 || end) {
            String s = decodeBase900toBase10(numericCodewords, count);
            result.append(s);
            count = 0;
         }
      }

      return codeIndex;
   }

   private static String decodeBase900toBase10(int[] codewords, int count) throws FormatException {
      BigInteger result = BigInteger.ZERO;

      for(int i = 0; i < count; ++i) {
         result = result.add(EXP900[count - i - 1].multiply(BigInteger.valueOf((long)codewords[i])));
      }

      String resultString = result.toString();
      if (resultString.charAt(0) != '1') {
         throw FormatException.getFormatInstance();
      } else {
         return resultString.substring(1);
      }
   }

   static {
      EXP900[0] = BigInteger.ONE;
      BigInteger nineHundred = BigInteger.valueOf(900L);
      EXP900[1] = nineHundred;

      for(int i = 2; i < EXP900.length; ++i) {
         EXP900[i] = EXP900[i - 1].multiply(nineHundred);
      }

   }

   private static enum Mode {
      ALPHA,
      LOWER,
      MIXED,
      PUNCT,
      ALPHA_SHIFT,
      PUNCT_SHIFT;
   }
}
