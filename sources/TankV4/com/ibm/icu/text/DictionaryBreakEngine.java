package com.ibm.icu.text;

import java.text.CharacterIterator;
import java.util.Stack;

abstract class DictionaryBreakEngine implements LanguageBreakEngine {
   protected UnicodeSet fSet = new UnicodeSet();
   private final int fTypes;

   public DictionaryBreakEngine(int var1) {
      this.fTypes = var1;
   }

   public boolean handles(int var1, int var2) {
      return var2 >= 0 && var2 < 32 && (1 << var2 & this.fTypes) != 0 && this.fSet.contains(var1);
   }

   public int findBreaks(CharacterIterator var1, int var2, int var3, boolean var4, int var5, Stack var6) {
      if (var5 >= 0 && var5 < 32 && (1 << var5 & this.fTypes) != 0) {
         boolean var7 = false;
         UCharacterIterator var8 = UCharacterIterator.getInstance(var1);
         int var9 = var8.getIndex();
         int var13 = var8.current();
         int var10;
         int var11;
         int var12;
         if (var4) {
            boolean var14;
            for(var14 = this.fSet.contains(var13); (var10 = var8.getIndex()) > var2 && var14; var14 = this.fSet.contains(var13)) {
               var13 = var8.previous();
            }

            var11 = var10 < var2 ? var2 : var10 + (var14 ? 0 : 1);
            var12 = var9 + 1;
         } else {
            while((var10 = var8.getIndex()) < var3 && this.fSet.contains(var13)) {
               var13 = var8.next();
            }

            var11 = var9;
            var12 = var10;
         }

         int var15 = this.divideUpDictionaryRange(var8, var11, var12, var6);
         var8.setIndex(var10);
         return var15;
      } else {
         return 0;
      }
   }

   protected abstract int divideUpDictionaryRange(UCharacterIterator var1, int var2, int var3, Stack var4);
}
