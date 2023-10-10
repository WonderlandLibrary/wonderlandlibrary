package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;

@GwtCompatible
class TrieParser {
   private static final Joiner PREFIX_JOINER = Joiner.on("");

   static ImmutableMap parseTrie(CharSequence var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      int var2 = var0.length();

      for(int var3 = 0; var3 < var2; var3 += doParseTrieToBuilder(Lists.newLinkedList(), var0.subSequence(var3, var2), var1)) {
      }

      return var1.build();
   }

   private static int doParseTrieToBuilder(List var0, CharSequence var1, ImmutableMap.Builder var2) {
      int var3 = var1.length();
      int var4 = 0;

      char var5;
      for(var5 = 0; var4 < var3; ++var4) {
         var5 = var1.charAt(var4);
         if (var5 == '&' || var5 == '?' || var5 == '!' || var5 == ':' || var5 == ',') {
            break;
         }
      }

      var0.add(0, reverse(var1.subSequence(0, var4)));
      if (var5 == '!' || var5 == '?' || var5 == ':' || var5 == ',') {
         String var6 = PREFIX_JOINER.join((Iterable)var0);
         if (var6.length() > 0) {
            var2.put(var6, PublicSuffixType.fromCode(var5));
         }
      }

      ++var4;
      if (var5 != '?' && var5 != ',') {
         label67: {
            do {
               if (var4 >= var3) {
                  break label67;
               }

               var4 += doParseTrieToBuilder(var0, var1.subSequence(var4, var3), var2);
            } while(var1.charAt(var4) != '?' && var1.charAt(var4) != ',');

            ++var4;
         }
      }

      var0.remove(0);
      return var4;
   }

   private static CharSequence reverse(CharSequence var0) {
      int var1 = var0.length();
      if (var1 <= 1) {
         return var0;
      } else {
         char[] var2 = new char[var1];
         var2[0] = var0.charAt(var1 - 1);

         for(int var3 = 1; var3 < var1; ++var3) {
            var2[var3] = var0.charAt(var1 - 1 - var3);
            if (Character.isSurrogatePair(var2[var3], var2[var3 - 1])) {
               swap(var2, var3 - 1, var3);
            }
         }

         return new String(var2);
      }
   }

   private static void swap(char[] var0, int var1, int var2) {
      char var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }
}
