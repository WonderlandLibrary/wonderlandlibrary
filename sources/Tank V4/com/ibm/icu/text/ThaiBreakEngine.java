package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.io.IOException;
import java.text.CharacterIterator;
import java.util.Stack;

class ThaiBreakEngine implements LanguageBreakEngine {
   private static final byte THAI_LOOKAHEAD = 3;
   private static final byte THAI_ROOT_COMBINE_THRESHOLD = 3;
   private static final byte THAI_PREFIX_COMBINE_THRESHOLD = 3;
   private static final char THAI_PAIYANNOI = 'ฯ';
   private static final char THAI_MAIYAMOK = 'ๆ';
   private static final byte THAI_MIN_WORD = 2;
   private DictionaryMatcher fDictionary = DictionaryData.loadDictionaryFor("Thai");
   private static UnicodeSet fThaiWordSet = new UnicodeSet();
   private static UnicodeSet fEndWordSet = new UnicodeSet();
   private static UnicodeSet fBeginWordSet = new UnicodeSet();
   private static UnicodeSet fSuffixSet = new UnicodeSet();
   private static UnicodeSet fMarkSet = new UnicodeSet();

   public ThaiBreakEngine() throws IOException {
   }

   public boolean handles(int var1, int var2) {
      if (var2 != 1 && var2 != 2) {
         return false;
      } else {
         int var3 = UCharacter.getIntPropertyValue(var1, 4106);
         return var3 == 38;
      }
   }

   public int findBreaks(CharacterIterator var1, int var2, int var3, boolean var4, int var5, Stack var6) {
      if (var3 - var2 < 2) {
         return 0;
      } else {
         int var7 = 0;
         ThaiBreakEngine.PossibleWord[] var10 = new ThaiBreakEngine.PossibleWord[3];

         for(int var11 = 0; var11 < 3; ++var11) {
            var10[var11] = new ThaiBreakEngine.PossibleWord();
         }

         var1.setIndex(var2);

         int var9;
         while((var9 = var1.getIndex()) < var3) {
            int var8 = 0;
            int var12 = var10[var7 % 3].candidates(var1, this.fDictionary, var3);
            if (var12 == 1) {
               var8 = var10[var7 % 3].acceptMarked(var1);
               ++var7;
            } else if (var12 > 1) {
               boolean var13 = false;
               if (var1.getIndex() < var3) {
                  do {
                     byte var14 = 1;
                     if (var10[(var7 + 1) % 3].candidates(var1, this.fDictionary, var3) > 0) {
                        if (var14 < 2) {
                           var10[var7 % 3].markCurrent();
                           boolean var19 = true;
                        }

                        if (var1.getIndex() >= var3) {
                           break;
                        }

                        do {
                           if (var10[(var7 + 2) % 3].candidates(var1, this.fDictionary, var3) > 0) {
                              var10[var7 % 3].markCurrent();
                              var13 = true;
                              break;
                           }
                        } while(var10[(var7 + 1) % 3].backUp(var1));
                     }
                  } while(var10[var7 % 3].backUp(var1) && !var13);
               }

               var8 = var10[var7 % 3].acceptMarked(var1);
               ++var7;
            }

            char var17;
            int var18;
            if (var1.getIndex() < var3 && var8 < 3) {
               if (var10[var7 % 3].candidates(var1, this.fDictionary, var3) > 0 || var8 != 0 && var10[var7 % 3].longestPrefix() >= 3) {
                  var1.setIndex(var9 + var8);
               } else {
                  var18 = var3 - (var9 + var8);
                  char var20 = var1.current();
                  int var15 = 0;

                  while(true) {
                     var1.next();
                     var17 = var1.current();
                     ++var15;
                     --var18;
                     if (var18 <= 0) {
                        break;
                     }

                     if (fEndWordSet.contains(var20) && fBeginWordSet.contains(var17)) {
                        int var16 = var10[(var7 + 1) % 3].candidates(var1, this.fDictionary, var3);
                        var1.setIndex(var9 + var8 + var15);
                        if (var16 > 0) {
                           break;
                        }
                     }

                     var20 = var17;
                  }

                  if (var8 <= 0) {
                     ++var7;
                  }

                  var8 += var15;
               }
            }

            while((var18 = var1.getIndex()) < var3 && fMarkSet.contains(var1.current())) {
               var1.next();
               var8 += var1.getIndex() - var18;
            }

            if (var1.getIndex() < var3 && var8 > 0) {
               if (var10[var7 % 3].candidates(var1, this.fDictionary, var3) <= 0 && fSuffixSet.contains(var17 = var1.current())) {
                  if (var17 == 3631) {
                     if (!fSuffixSet.contains(var1.previous())) {
                        var1.next();
                        var1.next();
                        ++var8;
                        var17 = var1.current();
                     } else {
                        var1.next();
                     }
                  }

                  if (var17 == 3654) {
                     if (var1.previous() != 3654) {
                        var1.next();
                        var1.next();
                        ++var8;
                     } else {
                        var1.next();
                     }
                  }
               } else {
                  var1.setIndex(var9 + var8);
               }
            }

            if (var8 > 0) {
               var6.push(var9 + var8);
            }
         }

         if ((Integer)var6.peek() >= var3) {
            var6.pop();
            --var7;
         }

         return var7;
      }
   }

   static {
      fThaiWordSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]]");
      fThaiWordSet.compact();
      fMarkSet.applyPattern("[[:Thai:]&[:LineBreak=SA:]&[:M:]]");
      fMarkSet.add(32);
      fEndWordSet = fThaiWordSet;
      fEndWordSet.remove(3633);
      fEndWordSet.remove(3648, 3652);
      fBeginWordSet.add(3585, 3630);
      fBeginWordSet.add(3648, 3652);
      fSuffixSet.add(3631);
      fSuffixSet.add(3654);
      fMarkSet.compact();
      fEndWordSet.compact();
      fBeginWordSet.compact();
      fSuffixSet.compact();
      fThaiWordSet.freeze();
      fMarkSet.freeze();
      fEndWordSet.freeze();
      fBeginWordSet.freeze();
      fSuffixSet.freeze();
   }

   static class PossibleWord {
      private static final int POSSIBLE_WORD_LIST_MAX = 20;
      private int[] lengths = new int[20];
      private int[] count = new int[1];
      private int prefix;
      private int offset = -1;
      private int mark;
      private int current;

      public PossibleWord() {
      }

      public int candidates(CharacterIterator var1, DictionaryMatcher var2, int var3) {
         int var4 = var1.getIndex();
         if (var4 != this.offset) {
            this.offset = var4;
            this.prefix = var2.matches(var1, var3 - var4, this.lengths, this.count, this.lengths.length);
            if (this.count[0] <= 0) {
               var1.setIndex(var4);
            }
         }

         if (this.count[0] > 0) {
            var1.setIndex(var4 + this.lengths[this.count[0] - 1]);
         }

         this.current = this.count[0] - 1;
         this.mark = this.current;
         return this.count[0];
      }

      public int acceptMarked(CharacterIterator var1) {
         var1.setIndex(this.offset + this.lengths[this.mark]);
         return this.lengths[this.mark];
      }

      public boolean backUp(CharacterIterator var1) {
         if (this.current > 0) {
            var1.setIndex(this.offset + this.lengths[--this.current]);
            return true;
         } else {
            return false;
         }
      }

      public int longestPrefix() {
         return this.prefix;
      }

      public void markCurrent() {
         this.mark = this.current;
      }
   }
}
