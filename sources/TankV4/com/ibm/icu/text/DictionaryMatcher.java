package com.ibm.icu.text;

import java.text.CharacterIterator;

abstract class DictionaryMatcher {
   public abstract int matches(CharacterIterator var1, int var2, int[] var3, int[] var4, int var5, int[] var6);

   public int matches(CharacterIterator var1, int var2, int[] var3, int[] var4, int var5) {
      return this.matches(var1, var2, var3, var4, var5, (int[])null);
   }

   public abstract int getType();
}
