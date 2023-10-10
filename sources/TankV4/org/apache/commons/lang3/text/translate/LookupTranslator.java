package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class LookupTranslator extends CharSequenceTranslator {
   private final HashMap lookupMap = new HashMap();
   private final int shortest;
   private final int longest;

   public LookupTranslator(CharSequence[]... var1) {
      int var2 = Integer.MAX_VALUE;
      int var3 = 0;
      if (var1 != null) {
         CharSequence[][] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CharSequence[] var7 = var4[var6];
            this.lookupMap.put(var7[0].toString(), var7[1]);
            int var8 = var7[0].length();
            if (var8 < var2) {
               var2 = var8;
            }

            if (var8 > var3) {
               var3 = var8;
            }
         }
      }

      this.shortest = var2;
      this.longest = var3;
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var4 = this.longest;
      if (var2 + this.longest > var1.length()) {
         var4 = var1.length() - var2;
      }

      for(int var5 = var4; var5 >= this.shortest; --var5) {
         CharSequence var6 = var1.subSequence(var2, var2 + var5);
         CharSequence var7 = (CharSequence)this.lookupMap.get(var6.toString());
         if (var7 != null) {
            var3.write(var7.toString());
            return var5;
         }
      }

      return 0;
   }
}
