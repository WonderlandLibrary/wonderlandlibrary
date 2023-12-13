package com.ibm.icu.text;

interface UnicodeReplacer {
   int replace(Replaceable var1, int var2, int var3, int[] var4);

   String toReplacerPattern(boolean var1);

   void addReplacementSetTo(UnicodeSet var1);
}
