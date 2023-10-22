package com.sun.webkit.text;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class TextBreakIterator {
   static final int CHARACTER_ITERATOR = 0;
   static final int WORD_ITERATOR = 1;
   static final int LINE_ITERATOR = 2;
   static final int SENTENCE_ITERATOR = 3;
   static final int TEXT_BREAK_FIRST = 0;
   static final int TEXT_BREAK_LAST = 1;
   static final int TEXT_BREAK_NEXT = 2;
   static final int TEXT_BREAK_PREVIOUS = 3;
   static final int TEXT_BREAK_CURRENT = 4;
   static final int TEXT_BREAK_PRECEDING = 5;
   static final int TEXT_BREAK_FOLLOWING = 6;
   static final int IS_TEXT_BREAK = 7;
   static final int IS_WORD_TEXT_BREAK = 8;
   private static final Map iteratorCache = new HashMap();

   static BreakIterator getIterator(int var0, String var1, String var2, boolean var3) {
      String[] var4 = var1.split("-");
      String var5;
      switch (var4.length) {
         case 1:
            var5 = null;
            break;
         case 2:
            var5 = var4[1];
            break;
         default:
            var5 = var4[2];
      }

      String var6 = var4[0].toLowerCase();
      Locale var7 = var5 == null ? new Locale(var6) : new Locale(var6, var5.toUpperCase());
      BreakIterator var8;
      if (var3) {
         var8 = createIterator(var0, var7);
      } else {
         CacheKey var9 = new CacheKey(var0, var7);
         var8 = (BreakIterator)iteratorCache.get(var9);
         if (var8 == null) {
            var8 = createIterator(var0, var7);
            iteratorCache.put(var9, var8);
         }
      }

      var8.setText(var2);
      return var8;
   }

   private static BreakIterator createIterator(int var0, Locale var1) {
      switch (var0) {
         case 0:
            return BreakIterator.getCharacterInstance(var1);
         case 1:
            return BreakIterator.getWordInstance(var1);
         case 2:
            return BreakIterator.getLineInstance(var1);
         case 3:
            return BreakIterator.getSentenceInstance(var1);
         default:
            throw new IllegalArgumentException("invalid type: " + var0);
      }
   }

   static int invokeMethod(BreakIterator var0, int var1, int var2) {
      CharacterIterator var3 = var0.getText();
      int var4 = var3.getEndIndex() - var3.getBeginIndex();
      if (var1 == 5 && var2 > var4) {
         return var4;
      } else {
         if (var2 < 0 || var2 > var4) {
            var2 = var2 < 0 ? 0 : var4;
         }

         switch (var1) {
            case 0:
               return var0.first();
            case 1:
               return var0.last();
            case 2:
               return var0.next();
            case 3:
               return var0.previous();
            case 4:
               return var0.current();
            case 5:
               return var0.preceding(var2);
            case 6:
               return var0.following(var2);
            case 7:
               return var0.isBoundary(var2) ? 1 : 0;
            case 8:
               return 1;
            default:
               throw new IllegalArgumentException("invalid method: " + var1);
         }
      }
   }

   private static final class CacheKey {
      private final int type;
      private final Locale locale;
      private final int hashCode;

      CacheKey(int var1, Locale var2) {
         this.type = var1;
         this.locale = var2;
         this.hashCode = var2.hashCode() + var1;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof CacheKey)) {
            return false;
         } else {
            CacheKey var2 = (CacheKey)var1;
            return var2.type == this.type && var2.locale.equals(this.locale);
         }
      }

      public int hashCode() {
         return this.hashCode;
      }
   }
}
