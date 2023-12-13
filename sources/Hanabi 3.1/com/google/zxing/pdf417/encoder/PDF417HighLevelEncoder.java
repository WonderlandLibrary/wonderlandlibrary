package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import java.math.BigInteger;
import java.util.Arrays;

final class PDF417HighLevelEncoder {
   private static final int TEXT_COMPACTION = 0;
   private static final int BYTE_COMPACTION = 1;
   private static final int NUMERIC_COMPACTION = 2;
   private static final int SUBMODE_ALPHA = 0;
   private static final int SUBMODE_LOWER = 1;
   private static final int SUBMODE_MIXED = 2;
   private static final int SUBMODE_PUNCTUATION = 3;
   private static final int LATCH_TO_TEXT = 900;
   private static final int LATCH_TO_BYTE_PADDED = 901;
   private static final int LATCH_TO_NUMERIC = 902;
   private static final int SHIFT_TO_BYTE = 913;
   private static final int LATCH_TO_BYTE = 924;
   private static final byte[] TEXT_MIXED_RAW = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 38, 13, 9, 44, 58, 35, 45, 46, 36, 47, 43, 37, 42, 61, 94, 0, 32, 0, 0, 0};
   private static final byte[] TEXT_PUNCTUATION_RAW = new byte[]{59, 60, 62, 64, 91, 92, 93, 95, 96, 126, 33, 13, 9, 44, 58, 10, 45, 46, 36, 47, 34, 124, 42, 40, 41, 63, 123, 125, 39, 0};
   private static final byte[] MIXED = new byte[128];
   private static final byte[] PUNCTUATION = new byte[128];

   private PDF417HighLevelEncoder() {
   }

   private static byte[] getBytesForMessage(String msg) {
      return msg.getBytes();
   }

   static String encodeHighLevel(String msg, Compaction compaction) throws WriterException {
      byte[] bytes = null;
      StringBuilder sb = new StringBuilder(msg.length());
      int len = msg.length();
      int p = 0;
      int textSubMode = 0;
      if (compaction == Compaction.TEXT) {
         encodeText(msg, p, len, sb, textSubMode);
      } else if (compaction == Compaction.BYTE) {
         bytes = getBytesForMessage(msg);
         encodeBinary(bytes, p, bytes.length, 1, sb);
      } else if (compaction == Compaction.NUMERIC) {
         sb.append('Ά');
         encodeNumeric(msg, p, len, sb);
      } else {
         int encodingMode = 0;

         while(true) {
            while(p < len) {
               int n = determineConsecutiveDigitCount(msg, p);
               if (n >= 13) {
                  sb.append('Ά');
                  encodingMode = 2;
                  textSubMode = 0;
                  encodeNumeric(msg, p, n, sb);
                  p += n;
               } else {
                  int t = determineConsecutiveTextCount(msg, p);
                  if (t < 5 && n != len) {
                     if (bytes == null) {
                        bytes = getBytesForMessage(msg);
                     }

                     int b = determineConsecutiveBinaryCount(msg, bytes, p);
                     if (b == 0) {
                        b = 1;
                     }

                     if (b == 1 && encodingMode == 0) {
                        encodeBinary(bytes, p, 1, 0, sb);
                     } else {
                        encodeBinary(bytes, p, b, encodingMode, sb);
                        encodingMode = 1;
                        textSubMode = 0;
                     }

                     p += b;
                  } else {
                     if (encodingMode != 0) {
                        sb.append('΄');
                        encodingMode = 0;
                        textSubMode = 0;
                     }

                     textSubMode = encodeText(msg, p, t, sb, textSubMode);
                     p += t;
                  }
               }
            }

            return sb.toString();
         }
      }

      return sb.toString();
   }

   private static int encodeText(CharSequence msg, int startpos, int count, StringBuilder sb, int initialSubmode) {
      StringBuilder tmp = new StringBuilder(count);
      int submode = initialSubmode;
      int idx = 0;

      while(true) {
         while(true) {
            char h = msg.charAt(startpos + idx);
            int next;
            switch (submode) {
               case 0:
                  if (isAlphaUpper(h)) {
                     if (h == ' ') {
                        tmp.append('\u001a');
                     } else {
                        tmp.append((char)(h - 65));
                     }
                  } else {
                     if (isAlphaLower(h)) {
                        submode = 1;
                        tmp.append('\u001b');
                        continue;
                     }

                     if (isMixed(h)) {
                        submode = 2;
                        tmp.append('\u001c');
                        continue;
                     }

                     tmp.append('\u001d');
                     tmp.append((char)PUNCTUATION[h]);
                  }
                  break;
               case 1:
                  if (isAlphaLower(h)) {
                     if (h == ' ') {
                        tmp.append('\u001a');
                     } else {
                        tmp.append((char)(h - 97));
                     }
                  } else if (isAlphaUpper(h)) {
                     tmp.append('\u001b');
                     tmp.append((char)(h - 65));
                  } else {
                     if (isMixed(h)) {
                        submode = 2;
                        tmp.append('\u001c');
                        continue;
                     }

                     tmp.append('\u001d');
                     tmp.append((char)PUNCTUATION[h]);
                  }
                  break;
               case 2:
                  if (isMixed(h)) {
                     tmp.append((char)MIXED[h]);
                  } else {
                     if (isAlphaUpper(h)) {
                        submode = 0;
                        tmp.append('\u001c');
                        continue;
                     }

                     if (isAlphaLower(h)) {
                        submode = 1;
                        tmp.append('\u001b');
                        continue;
                     }

                     if (startpos + idx + 1 < count) {
                        next = msg.charAt(startpos + idx + 1);
                        if (isPunctuation((char)next)) {
                           submode = 3;
                           tmp.append('\u0019');
                           continue;
                        }
                     }

                     tmp.append('\u001d');
                     tmp.append((char)PUNCTUATION[h]);
                  }
                  break;
               default:
                  if (!isPunctuation(h)) {
                     submode = 0;
                     tmp.append('\u001d');
                     continue;
                  }

                  tmp.append((char)PUNCTUATION[h]);
            }

            ++idx;
            if (idx >= count) {
               h = 0;
               next = tmp.length();

               for(int i = 0; i < next; ++i) {
                  boolean odd = i % 2 != 0;
                  if (odd) {
                     h = (char)(h * 30 + tmp.charAt(i));
                     sb.append(h);
                  } else {
                     h = tmp.charAt(i);
                  }
               }

               if (next % 2 != 0) {
                  sb.append((char)(h * 30 + 29));
               }

               return submode;
            }
         }
      }
   }

   private static void encodeBinary(byte[] bytes, int startpos, int count, int startmode, StringBuilder sb) {
      if (count == 1 && startmode == 0) {
         sb.append('Α');
      }

      int idx = startpos;
      if (count >= 6) {
         sb.append('Μ');

         for(char[] chars = new char[5]; startpos + count - idx >= 6; idx += 6) {
            long t = 0L;

            int i;
            for(i = 0; i < 6; ++i) {
               t <<= 8;
               t += (long)(bytes[idx + i] & 255);
            }

            for(i = 0; i < 5; ++i) {
               chars[i] = (char)((int)(t % 900L));
               t /= 900L;
            }

            for(i = chars.length - 1; i >= 0; --i) {
               sb.append(chars[i]);
            }
         }
      }

      if (idx < startpos + count) {
         sb.append('΅');
      }

      for(int i = idx; i < startpos + count; ++i) {
         int ch = bytes[i] & 255;
         sb.append((char)ch);
      }

   }

   private static void encodeNumeric(String msg, int startpos, int count, StringBuilder sb) {
      int idx = 0;
      StringBuilder tmp = new StringBuilder(count / 3 + 1);
      BigInteger num900 = BigInteger.valueOf(900L);

      int len;
      for(BigInteger num0 = BigInteger.valueOf(0L); idx < count - 1; idx += len) {
         tmp.setLength(0);
         len = Math.min(44, count - idx);
         String part = '1' + msg.substring(startpos + idx, startpos + idx + len);
         BigInteger bigint = new BigInteger(part);

         do {
            BigInteger c = bigint.mod(num900);
            tmp.append((char)c.intValue());
            bigint = bigint.divide(num900);
         } while(!bigint.equals(num0));

         for(int i = tmp.length() - 1; i >= 0; --i) {
            sb.append(tmp.charAt(i));
         }
      }

   }

   private static boolean isDigit(char ch) {
      return ch >= '0' && ch <= '9';
   }

   private static boolean isAlphaUpper(char ch) {
      return ch == ' ' || ch >= 'A' && ch <= 'Z';
   }

   private static boolean isAlphaLower(char ch) {
      return ch == ' ' || ch >= 'a' && ch <= 'z';
   }

   private static boolean isMixed(char ch) {
      return MIXED[ch] != -1;
   }

   private static boolean isPunctuation(char ch) {
      return PUNCTUATION[ch] != -1;
   }

   private static boolean isText(char ch) {
      return ch == '\t' || ch == '\n' || ch == '\r' || ch >= ' ' && ch <= '~';
   }

   private static int determineConsecutiveDigitCount(CharSequence msg, int startpos) {
      int count = 0;
      int len = msg.length();
      int idx = startpos;
      if (startpos < len) {
         char ch = msg.charAt(startpos);

         while(isDigit(ch) && idx < len) {
            ++count;
            ++idx;
            if (idx < len) {
               ch = msg.charAt(idx);
            }
         }
      }

      return count;
   }

   private static int determineConsecutiveTextCount(CharSequence msg, int startpos) {
      int len = msg.length();
      int idx = startpos;

      while(idx < len) {
         char ch = msg.charAt(idx);
         int numericCount = 0;

         while(numericCount < 13 && isDigit(ch) && idx < len) {
            ++numericCount;
            ++idx;
            if (idx < len) {
               ch = msg.charAt(idx);
            }
         }

         if (numericCount >= 13) {
            return idx - startpos - numericCount;
         }

         if (numericCount <= 0) {
            ch = msg.charAt(idx);
            if (!isText(ch)) {
               break;
            }

            ++idx;
         }
      }

      return idx - startpos;
   }

   private static int determineConsecutiveBinaryCount(CharSequence msg, byte[] bytes, int startpos) throws WriterException {
      int len = msg.length();

      int idx;
      for(idx = startpos; idx < len; ++idx) {
         char ch = msg.charAt(idx);

         int numericCount;
         int textCount;
         for(numericCount = 0; numericCount < 13 && isDigit(ch); ch = msg.charAt(textCount)) {
            ++numericCount;
            textCount = idx + numericCount;
            if (textCount >= len) {
               break;
            }
         }

         if (numericCount >= 13) {
            return idx - startpos;
         }

         int i;
         for(textCount = 0; textCount < 5 && isText(ch); ch = msg.charAt(i)) {
            ++textCount;
            i = idx + textCount;
            if (i >= len) {
               break;
            }
         }

         if (textCount >= 5) {
            return idx - startpos;
         }

         ch = msg.charAt(idx);
         if (bytes[idx] == 63 && ch != '?') {
            throw new WriterException("Non-encodable character detected: " + ch + " (Unicode: " + ch + ')');
         }
      }

      return idx - startpos;
   }

   static {
      Arrays.fill(MIXED, (byte)-1);

      byte i;
      byte b;
      for(i = 0; i < TEXT_MIXED_RAW.length; ++i) {
         b = TEXT_MIXED_RAW[i];
         if (b > 0) {
            MIXED[b] = i;
         }
      }

      Arrays.fill(PUNCTUATION, (byte)-1);

      for(i = 0; i < TEXT_PUNCTUATION_RAW.length; ++i) {
         b = TEXT_PUNCTUATION_RAW[i];
         if (b > 0) {
            PUNCTUATION[b] = i;
         }
      }

   }
}
