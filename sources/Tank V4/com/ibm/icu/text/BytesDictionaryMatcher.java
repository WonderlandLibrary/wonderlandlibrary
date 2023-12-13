package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.util.BytesTrie;
import java.text.CharacterIterator;

class BytesDictionaryMatcher extends DictionaryMatcher {
   private final byte[] characters;
   private final int transform;

   public BytesDictionaryMatcher(byte[] var1, int var2) {
      this.characters = var1;
      Assert.assrt((var2 & 2130706432) == 16777216);
      this.transform = var2;
   }

   private int transform(int var1) {
      if (var1 == 8205) {
         return 255;
      } else if (var1 == 8204) {
         return 254;
      } else {
         int var2 = var1 - (this.transform & 2097151);
         return var2 >= 0 && 253 >= var2 ? var2 : -1;
      }
   }

   public int matches(CharacterIterator var1, int var2, int[] var3, int[] var4, int var5, int[] var6) {
      UCharacterIterator var7 = UCharacterIterator.getInstance(var1);
      BytesTrie var8 = new BytesTrie(this.characters, 0);
      int var9 = var7.nextCodePoint();
      BytesTrie.Result var10 = var8.first(this.transform(var9));
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
         var10 = var8.next(this.transform(var9));
      }

      var4[0] = var12;
      return var11;
   }

   public int getType() {
      return 0;
   }
}
