package com.ibm.icu.text;

import com.ibm.icu.util.BytesTrie;
import com.ibm.icu.util.CharsTrie;
import java.text.CharacterIterator;

class CharsDictionaryMatcher extends DictionaryMatcher {
   private CharSequence characters;

   public CharsDictionaryMatcher(CharSequence var1) {
      this.characters = var1;
   }

   public int matches(CharacterIterator var1, int var2, int[] var3, int[] var4, int var5, int[] var6) {
      UCharacterIterator var7 = UCharacterIterator.getInstance(var1);
      CharsTrie var8 = new CharsTrie(this.characters, 0);
      int var9 = var7.nextCodePoint();
      BytesTrie.Result var10 = var8.firstForCodePoint(var9);
      int var11 = 1;
      int var12 = 0;

      while(true) {
         if (var10.hasValue()) {
            if (var12 < var5) {
               if (var6 != null) {
                  var6[var12] = var8.getValue();
               }

               var3[var12] = var11;
               ++var12;
            }

            if (var10 == BytesTrie.Result.FINAL_VALUE) {
               break;
            }
         } else if (var10 == BytesTrie.Result.NO_MATCH) {
            break;
         }

         if (var11 >= var2) {
            break;
         }

         var9 = var7.nextCodePoint();
         ++var11;
         var10 = var8.nextForCodePoint(var9);
      }

      var4[0] = var12;
      return var11;
   }

   public int getType() {
      return 1;
   }
}
