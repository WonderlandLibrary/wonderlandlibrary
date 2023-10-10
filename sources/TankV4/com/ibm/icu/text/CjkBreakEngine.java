package com.ibm.icu.text;

import java.io.IOException;
import java.text.CharacterIterator;
import java.util.Stack;

class CjkBreakEngine implements LanguageBreakEngine {
   private static final UnicodeSet fHangulWordSet = new UnicodeSet();
   private static final UnicodeSet fHanWordSet = new UnicodeSet();
   private static final UnicodeSet fKatakanaWordSet = new UnicodeSet();
   private static final UnicodeSet fHiraganaWordSet = new UnicodeSet();
   private final UnicodeSet fWordSet;
   private DictionaryMatcher fDictionary = null;
   private static final int kMaxKatakanaLength = 8;
   private static final int kMaxKatakanaGroupLength = 20;
   private static final int maxSnlp = 255;
   private static final int kint32max = Integer.MAX_VALUE;

   public CjkBreakEngine(boolean var1) throws IOException {
      this.fDictionary = DictionaryData.loadDictionaryFor("Hira");
      if (var1) {
         this.fWordSet = fHangulWordSet;
      } else {
         this.fWordSet = new UnicodeSet();
         this.fWordSet.addAll(fHanWordSet);
         this.fWordSet.addAll(fKatakanaWordSet);
         this.fWordSet.addAll(fHiraganaWordSet);
         this.fWordSet.add((CharSequence)"\\uff70\\u30fc");
      }

   }

   public boolean handles(int var1, int var2) {
      return var2 == 1 && this.fWordSet.contains(var1);
   }

   private static int getKatakanaCost(int var0) {
      int[] var1 = new int[]{8192, 984, 408, 240, 204, 252, 300, 372, 480};
      return var0 > 8 ? 8192 : var1[var0];
   }

   public int findBreaks(CharacterIterator param1, int param2, int param3, boolean param4, int param5, Stack param6) {
      // $FF: Couldn't be decompiled
   }

   static {
      fHangulWordSet.applyPattern("[\\uac00-\\ud7a3]");
      fHanWordSet.applyPattern("[:Han:]");
      fKatakanaWordSet.applyPattern("[[:Katakana:]\\uff9e\\uff9f]");
      fHiraganaWordSet.applyPattern("[:Hiragana:]");
      fHangulWordSet.freeze();
      fHanWordSet.freeze();
      fKatakanaWordSet.freeze();
      fHiraganaWordSet.freeze();
   }
}
