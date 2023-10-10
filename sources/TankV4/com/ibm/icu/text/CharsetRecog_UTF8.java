package com.ibm.icu.text;

class CharsetRecog_UTF8 extends CharsetRecognizer {
   String getName() {
      return "UTF-8";
   }

   CharsetMatch match(CharsetDetector var1) {
      boolean var2 = false;
      int var3 = 0;
      int var4 = 0;
      byte[] var5 = var1.fRawInput;
      boolean var7 = false;
      if (var1.fRawLength >= 3 && (var5[0] & 255) == 239 && (var5[1] & 255) == 187 && (var5[2] & 255) == 191) {
         var2 = true;
      }

      label97:
      for(int var6 = 0; var6 < var1.fRawLength; ++var6) {
         byte var9 = var5[var6];
         if ((var9 & 128) != 0) {
            int var10;
            if ((var9 & 224) == 192) {
               var10 = 1;
            } else if ((var9 & 240) == 224) {
               var10 = 2;
            } else if ((var9 & 248) == 240) {
               var10 = 3;
            } else {
               ++var4;
               if (var4 > 5) {
                  break;
               }

               var10 = 0;
            }

            do {
               ++var6;
               if (var6 >= var1.fRawLength) {
                  continue label97;
               }

               var9 = var5[var6];
               if ((var9 & 192) != 128) {
                  ++var4;
                  continue label97;
               }

               --var10;
            } while(var10 != 0);

            ++var3;
         }
      }

      byte var8 = 0;
      if (var2 && var4 == 0) {
         var8 = 100;
      } else if (var2 && var3 > var4 * 10) {
         var8 = 80;
      } else if (var3 > 3 && var4 == 0) {
         var8 = 100;
      } else if (var3 > 0 && var4 == 0) {
         var8 = 80;
      } else if (var3 == 0 && var4 == 0) {
         var8 = 10;
      } else if (var3 > var4 * 10) {
         var8 = 25;
      }

      return var8 == 0 ? null : new CharsetMatch(var1, this, var8);
   }
}
