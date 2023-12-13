package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.text.CharacterIterator;

public final class CharacterIteration {
   public static final int DONE32 = Integer.MAX_VALUE;

   private CharacterIteration() {
   }

   public static int next32(CharacterIterator var0) {
      char var1 = var0.current();
      if (var1 >= '\ud800' && var1 <= '\udbff') {
         var1 = var0.next();
         if (var1 < '\udc00' || var1 > '\udfff') {
            var1 = var0.previous();
         }
      }

      int var2 = var0.next();
      if (var2 >= 55296) {
         var2 = nextTrail32(var0, var2);
      }

      if (var2 >= 65536 && var2 != Integer.MAX_VALUE) {
         var0.previous();
      }

      return var2;
   }

   public static int nextTrail32(CharacterIterator var0, int var1) {
      if (var1 == 65535 && var0.getIndex() >= var0.getEndIndex()) {
         return Integer.MAX_VALUE;
      } else {
         int var2 = var1;
         if (var1 <= 56319) {
            char var3 = var0.next();
            if (UTF16.isTrailSurrogate(var3)) {
               var2 = (var1 - '\ud800' << 10) + (var3 - '\udc00') + 65536;
            } else {
               var0.previous();
            }
         }

         return var2;
      }
   }

   public static int previous32(CharacterIterator var0) {
      if (var0.getIndex() <= var0.getBeginIndex()) {
         return Integer.MAX_VALUE;
      } else {
         char var1 = var0.previous();
         int var2 = var1;
         if (UTF16.isTrailSurrogate(var1) && var0.getIndex() > var0.getBeginIndex()) {
            char var3 = var0.previous();
            if (UTF16.isLeadSurrogate(var3)) {
               var2 = (var3 - '\ud800' << 10) + (var1 - '\udc00') + 65536;
            } else {
               var0.next();
            }
         }

         return var2;
      }
   }

   public static int current32(CharacterIterator var0) {
      char var1 = var0.current();
      int var2 = var1;
      if (var1 < '\ud800') {
         return var1;
      } else {
         if (UTF16.isLeadSurrogate(var1)) {
            char var3 = var0.next();
            var0.previous();
            if (UTF16.isTrailSurrogate((char)var3)) {
               var2 = (var1 - '\ud800' << 10) + (var3 - '\udc00') + 65536;
            }
         } else if (var1 == '\uffff' && var0.getIndex() >= var0.getEndIndex()) {
            var2 = Integer.MAX_VALUE;
         }

         return var2;
      }
   }
}
