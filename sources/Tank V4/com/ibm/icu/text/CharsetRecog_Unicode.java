package com.ibm.icu.text;

abstract class CharsetRecog_Unicode extends CharsetRecognizer {
   abstract String getName();

   abstract CharsetMatch match(CharsetDetector var1);

   static class CharsetRecog_UTF_32_LE extends CharsetRecog_Unicode.CharsetRecog_UTF_32 {
      int getChar(byte[] var1, int var2) {
         return (var1[var2 + 3] & 255) << 24 | (var1[var2 + 2] & 255) << 16 | (var1[var2 + 1] & 255) << 8 | var1[var2 + 0] & 255;
      }

      String getName() {
         return "UTF-32LE";
      }
   }

   static class CharsetRecog_UTF_32_BE extends CharsetRecog_Unicode.CharsetRecog_UTF_32 {
      int getChar(byte[] var1, int var2) {
         return (var1[var2 + 0] & 255) << 24 | (var1[var2 + 1] & 255) << 16 | (var1[var2 + 2] & 255) << 8 | var1[var2 + 3] & 255;
      }

      String getName() {
         return "UTF-32BE";
      }
   }

   abstract static class CharsetRecog_UTF_32 extends CharsetRecog_Unicode {
      abstract int getChar(byte[] var1, int var2);

      abstract String getName();

      CharsetMatch match(CharsetDetector var1) {
         byte[] var2 = var1.fRawInput;
         int var3 = var1.fRawLength / 4 * 4;
         int var4 = 0;
         int var5 = 0;
         boolean var6 = false;
         byte var7 = 0;
         if (var3 == 0) {
            return null;
         } else {
            if (this.getChar(var2, 0) == 65279) {
               var6 = true;
            }

            for(int var8 = 0; var8 < var3; var8 += 4) {
               int var9 = this.getChar(var2, var8);
               if (var9 >= 0 && var9 < 1114111 && (var9 < 55296 || var9 > 57343)) {
                  ++var4;
               } else {
                  ++var5;
               }
            }

            if (var6 && var5 == 0) {
               var7 = 100;
            } else if (var6 && var4 > var5 * 10) {
               var7 = 80;
            } else if (var4 > 3 && var5 == 0) {
               var7 = 100;
            } else if (var4 > 0 && var5 == 0) {
               var7 = 80;
            } else if (var4 > var5 * 10) {
               var7 = 25;
            }

            return var7 == 0 ? null : new CharsetMatch(var1, this, var7);
         }
      }
   }

   static class CharsetRecog_UTF_16_LE extends CharsetRecog_Unicode {
      String getName() {
         return "UTF-16LE";
      }

      CharsetMatch match(CharsetDetector var1) {
         byte[] var2 = var1.fRawInput;
         if (var2.length >= 2 && (var2[0] & 255) == 255 && (var2[1] & 255) == 254) {
            if (var2.length >= 4 && var2[2] == 0 && var2[3] == 0) {
               return null;
            } else {
               byte var3 = 100;
               return new CharsetMatch(var1, this, var3);
            }
         } else {
            return null;
         }
      }
   }

   static class CharsetRecog_UTF_16_BE extends CharsetRecog_Unicode {
      String getName() {
         return "UTF-16BE";
      }

      CharsetMatch match(CharsetDetector var1) {
         byte[] var2 = var1.fRawInput;
         if (var2.length >= 2 && (var2[0] & 255) == 254 && (var2[1] & 255) == 255) {
            byte var3 = 100;
            return new CharsetMatch(var1, this, var3);
         } else {
            return null;
         }
      }
   }
}
