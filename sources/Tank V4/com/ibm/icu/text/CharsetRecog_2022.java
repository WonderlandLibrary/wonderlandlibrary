package com.ibm.icu.text;

abstract class CharsetRecog_2022 extends CharsetRecognizer {
   int match(byte[] var1, int var2, byte[][] var3) {
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      label61:
      for(int var4 = 0; var4 < var2; ++var4) {
         if (var1[var4] == 27) {
            label54:
            for(int var6 = 0; var6 < var3.length; ++var6) {
               byte[] var11 = var3[var6];
               if (var2 - var4 >= var11.length) {
                  for(int var5 = 1; var5 < var11.length; ++var5) {
                     if (var11[var5] != var1[var4 + var5]) {
                        continue label54;
                     }
                  }

                  ++var7;
                  var4 += var11.length - 1;
                  continue label61;
               }
            }

            ++var8;
         }

         if (var1[var4] == 14 || var1[var4] == 15) {
            ++var9;
         }
      }

      if (var7 == 0) {
         return 0;
      } else {
         int var10 = (100 * var7 - 100 * var8) / (var7 + var8);
         if (var7 + var9 < 5) {
            var10 -= (5 - (var7 + var9)) * 10;
         }

         if (var10 < 0) {
            var10 = 0;
         }

         return var10;
      }
   }

   static class CharsetRecog_2022CN extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{27, 36, 41, 65}, {27, 36, 41, 71}, {27, 36, 42, 72}, {27, 36, 41, 69}, {27, 36, 43, 73}, {27, 36, 43, 74}, {27, 36, 43, 75}, {27, 36, 43, 76}, {27, 36, 43, 77}, {27, 78}, {27, 79}};

      String getName() {
         return "ISO-2022-CN";
      }

      CharsetMatch match(CharsetDetector var1) {
         int var2 = this.match(var1.fInputBytes, var1.fInputLen, this.escapeSequences);
         return var2 == 0 ? null : new CharsetMatch(var1, this, var2);
      }
   }

   static class CharsetRecog_2022KR extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{27, 36, 41, 67}};

      String getName() {
         return "ISO-2022-KR";
      }

      CharsetMatch match(CharsetDetector var1) {
         int var2 = this.match(var1.fInputBytes, var1.fInputLen, this.escapeSequences);
         return var2 == 0 ? null : new CharsetMatch(var1, this, var2);
      }
   }

   static class CharsetRecog_2022JP extends CharsetRecog_2022 {
      private byte[][] escapeSequences = new byte[][]{{27, 36, 40, 67}, {27, 36, 40, 68}, {27, 36, 64}, {27, 36, 65}, {27, 36, 66}, {27, 38, 64}, {27, 40, 66}, {27, 40, 72}, {27, 40, 73}, {27, 40, 74}, {27, 46, 65}, {27, 46, 70}};

      String getName() {
         return "ISO-2022-JP";
      }

      CharsetMatch match(CharsetDetector var1) {
         int var2 = this.match(var1.fInputBytes, var1.fInputLen, this.escapeSequences);
         return var2 == 0 ? null : new CharsetMatch(var1, this, var2);
      }
   }
}
