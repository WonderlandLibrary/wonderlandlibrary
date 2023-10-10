package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class ParseRequest {
   final String rawValue;
   final int radix;

   private ParseRequest(String var1, int var2) {
      this.rawValue = var1;
      this.radix = var2;
   }

   static ParseRequest fromString(String var0) {
      if (var0.length() == 0) {
         throw new NumberFormatException("empty string");
      } else {
         char var3 = var0.charAt(0);
         String var1;
         byte var2;
         if (!var0.startsWith("0x") && !var0.startsWith("0X")) {
            if (var3 == '#') {
               var1 = var0.substring(1);
               var2 = 16;
            } else if (var3 == '0' && var0.length() > 1) {
               var1 = var0.substring(1);
               var2 = 8;
            } else {
               var1 = var0;
               var2 = 10;
            }
         } else {
            var1 = var0.substring(2);
            var2 = 16;
         }

         return new ParseRequest(var1, var2);
      }
   }
}
