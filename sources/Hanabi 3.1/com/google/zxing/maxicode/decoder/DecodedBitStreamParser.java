package com.google.zxing.maxicode.decoder;

import com.google.zxing.common.DecoderResult;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

final class DecodedBitStreamParser {
   private static final char SHIFTA = '\ufff0';
   private static final char SHIFTB = '\ufff1';
   private static final char SHIFTC = '\ufff2';
   private static final char SHIFTD = '\ufff3';
   private static final char SHIFTE = '\ufff4';
   private static final char TWOSHIFTA = '\ufff5';
   private static final char THREESHIFTA = '\ufff6';
   private static final char LATCHA = '\ufff7';
   private static final char LATCHB = '\ufff8';
   private static final char LOCK = '\ufff9';
   private static final char ECI = '\ufffa';
   private static final char NS = '\ufffb';
   private static final char PAD = '￼';
   private static final char FS = '\u001c';
   private static final char GS = '\u001d';
   private static final char RS = '\u001e';
   private static final NumberFormat NINE_DIGITS = new DecimalFormat("000000000");
   private static final NumberFormat THREE_DIGITS = new DecimalFormat("000");
   private static final String[] SETS = new String[]{"\nABCDEFGHIJKLMNOPQRSTUVWXYZ\ufffa\u001c\u001d\u001e\ufffb ￼\"#$%&'()*+,-./0123456789:\ufff1\ufff2\ufff3\ufff4\ufff8", "`abcdefghijklmnopqrstuvwxyz\ufffa\u001c\u001d\u001e\ufffb{￼}~\u007f;<=>?[\\]^_ ,./:@!|￼\ufff5\ufff6￼\ufff0\ufff2\ufff3\ufff4\ufff7", "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚ\ufffa\u001c\u001d\u001eÛÜÝÞßª¬±²³µ¹º¼½¾\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\ufff7 \ufff9\ufff3\ufff4\ufff8", "àáâãäåæçèéêëìíîïðñòóôõö÷øùú\ufffa\u001c\u001d\u001e\ufffbûüýþÿ¡¨«¯°´·¸»¿\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\ufff7 \ufff2\ufff9\ufff4\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\ufffa￼￼\u001b\ufffb\u001c\u001d\u001e\u001f\u009f ¢£¤¥¦§©\u00ad®¶\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\ufff7 \ufff2\ufff3\ufff9\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?"};

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(byte[] bytes, int mode) {
      StringBuilder result = new StringBuilder(144);
      switch (mode) {
         case 2:
         case 3:
            String postcode;
            if (mode == 2) {
               int pc = getPostCode2(bytes);
               NumberFormat df = new DecimalFormat("0000000000".substring(0, getPostCode2Length(bytes)));
               postcode = df.format((long)pc);
            } else {
               postcode = getPostCode3(bytes);
            }

            String country = THREE_DIGITS.format((long)getCountry(bytes));
            String service = THREE_DIGITS.format((long)getServiceClass(bytes));
            result.append(getMessage(bytes, 10, 84));
            if (result.toString().startsWith("[)>\u001e01\u001d")) {
               result.insert(9, postcode + '\u001d' + country + '\u001d' + service + '\u001d');
            } else {
               result.insert(0, postcode + '\u001d' + country + '\u001d' + service + '\u001d');
            }
            break;
         case 4:
            result.append(getMessage(bytes, 1, 93));
            break;
         case 5:
            result.append(getMessage(bytes, 1, 77));
      }

      return new DecoderResult(bytes, result.toString(), (List)null, String.valueOf(mode));
   }

   private static int getBit(int bit, byte[] bytes) {
      --bit;
      return (bytes[bit / 6] & 1 << 5 - bit % 6) == 0 ? 0 : 1;
   }

   private static int getInt(byte[] bytes, byte[] x) {
      int val = 0;

      for(int i = 0; i < x.length; ++i) {
         val += getBit(x[i], bytes) << x.length - i - 1;
      }

      return val;
   }

   private static int getCountry(byte[] bytes) {
      return getInt(bytes, new byte[]{53, 54, 43, 44, 45, 46, 47, 48, 37, 38});
   }

   private static int getServiceClass(byte[] bytes) {
      return getInt(bytes, new byte[]{55, 56, 57, 58, 59, 60, 49, 50, 51, 52});
   }

   private static int getPostCode2Length(byte[] bytes) {
      return getInt(bytes, new byte[]{39, 40, 41, 42, 31, 32});
   }

   private static int getPostCode2(byte[] bytes) {
      return getInt(bytes, new byte[]{33, 34, 35, 36, 25, 26, 27, 28, 29, 30, 19, 20, 21, 22, 23, 24, 13, 14, 15, 16, 17, 18, 7, 8, 9, 10, 11, 12, 1, 2});
   }

   private static String getPostCode3(byte[] bytes) {
      return String.valueOf(new char[]{SETS[0].charAt(getInt(bytes, new byte[]{39, 40, 41, 42, 31, 32})), SETS[0].charAt(getInt(bytes, new byte[]{33, 34, 35, 36, 25, 26})), SETS[0].charAt(getInt(bytes, new byte[]{27, 28, 29, 30, 19, 20})), SETS[0].charAt(getInt(bytes, new byte[]{21, 22, 23, 24, 13, 14})), SETS[0].charAt(getInt(bytes, new byte[]{15, 16, 17, 18, 7, 8})), SETS[0].charAt(getInt(bytes, new byte[]{9, 10, 11, 12, 1, 2}))});
   }

   private static String getMessage(byte[] bytes, int start, int len) {
      StringBuilder sb = new StringBuilder();
      int shift = -1;
      int set = 0;
      int lastset = 0;

      for(int i = start; i < start + len; ++i) {
         char c = SETS[set].charAt(bytes[i]);
         switch (c) {
            case '\ufff0':
            case '\ufff1':
            case '\ufff2':
            case '\ufff3':
            case '\ufff4':
               lastset = set;
               set = c - '\ufff0';
               shift = 1;
               break;
            case '\ufff5':
               lastset = set;
               set = 0;
               shift = 2;
               break;
            case '\ufff6':
               lastset = set;
               set = 0;
               shift = 3;
               break;
            case '\ufff7':
               set = 0;
               shift = -1;
               break;
            case '\ufff8':
               set = 1;
               shift = -1;
               break;
            case '\ufff9':
               shift = -1;
               break;
            case '\ufffa':
            default:
               sb.append(c);
               break;
            case '\ufffb':
               ++i;
               int var10000 = bytes[i] << 24;
               ++i;
               var10000 += bytes[i] << 18;
               ++i;
               var10000 += bytes[i] << 12;
               ++i;
               var10000 += bytes[i] << 6;
               ++i;
               int nsval = var10000 + bytes[i];
               sb.append(NINE_DIGITS.format((long)nsval));
         }

         if (shift-- == 0) {
            set = lastset;
         }
      }

      while(sb.length() > 0 && sb.charAt(sb.length() - 1) == '￼') {
         sb.setLength(sb.length() - 1);
      }

      return sb.toString();
   }
}
