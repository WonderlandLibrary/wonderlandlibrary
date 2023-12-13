package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.CharTrie;
import com.ibm.icu.impl.CharacterIteration;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.lang.UCharacter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.CharacterIterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class RuleBasedBreakIterator extends BreakIterator {
   public static final int WORD_NONE = 0;
   public static final int WORD_NONE_LIMIT = 100;
   public static final int WORD_NUMBER = 100;
   public static final int WORD_NUMBER_LIMIT = 200;
   public static final int WORD_LETTER = 200;
   public static final int WORD_LETTER_LIMIT = 300;
   public static final int WORD_KANA = 300;
   public static final int WORD_KANA_LIMIT = 400;
   public static final int WORD_IDEO = 400;
   public static final int WORD_IDEO_LIMIT = 500;
   private static final int START_STATE = 1;
   private static final int STOP_STATE = 0;
   private static final int RBBI_START = 0;
   private static final int RBBI_RUN = 1;
   private static final int RBBI_END = 2;
   private CharacterIterator fText;
   /** @deprecated */
   RBBIDataWrapper fRData;
   private int fLastRuleStatusIndex;
   private boolean fLastStatusIndexValid;
   private int fDictionaryCharCount;
   private static final String RBBI_DEBUG_ARG = "rbbi";
   /** @deprecated */
   private static final boolean TRACE = ICUDebug.enabled("rbbi") && ICUDebug.value("rbbi").indexOf("trace") >= 0;
   private int fBreakType;
   private final UnhandledBreakEngine fUnhandledBreakEngine;
   private int[] fCachedBreakPositions;
   private int fPositionInCache;
   private boolean fUseDictionary;
   private final Set fBreakEngines;
   static final String fDebugEnv = ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null;

   /** @deprecated */
   private RuleBasedBreakIterator() {
      this.fText = new java.text.StringCharacterIterator("");
      this.fBreakType = 2;
      this.fUnhandledBreakEngine = new UnhandledBreakEngine();
      this.fUseDictionary = true;
      this.fBreakEngines = Collections.synchronizedSet(new HashSet());
      this.fLastStatusIndexValid = true;
      this.fDictionaryCharCount = 0;
      this.fBreakEngines.add(this.fUnhandledBreakEngine);
   }

   public static RuleBasedBreakIterator getInstanceFromCompiledRules(InputStream var0) throws IOException {
      RuleBasedBreakIterator var1 = new RuleBasedBreakIterator();
      var1.fRData = RBBIDataWrapper.get(var0);
      return var1;
   }

   public RuleBasedBreakIterator(String var1) {
      this();

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         compileRules(var1, var2);
         byte[] var6 = var2.toByteArray();
         ByteArrayInputStream var4 = new ByteArrayInputStream(var6);
         this.fRData = RBBIDataWrapper.get(var4);
      } catch (IOException var5) {
         RuntimeException var3 = new RuntimeException("RuleBasedBreakIterator rule compilation internal error: " + var5.getMessage());
         throw var3;
      }
   }

   public Object clone() {
      RuleBasedBreakIterator var1 = (RuleBasedBreakIterator)super.clone();
      if (this.fText != null) {
         var1.fText = (CharacterIterator)((CharacterIterator)this.fText.clone());
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         try {
            RuleBasedBreakIterator var2 = (RuleBasedBreakIterator)var1;
            if (this.fRData == var2.fRData || this.fRData != null && var2.fRData != null) {
               if (this.fRData != null && var2.fRData != null && !this.fRData.fRuleSource.equals(var2.fRData.fRuleSource)) {
                  return false;
               } else if (this.fText == null && var2.fText == null) {
                  return true;
               } else {
                  return this.fText != null && var2.fText != null ? this.fText.equals(var2.fText) : false;
               }
            } else {
               return false;
            }
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public String toString() {
      String var1 = "";
      if (this.fRData != null) {
         var1 = this.fRData.fRuleSource;
      }

      return var1;
   }

   public int hashCode() {
      return this.fRData.fRuleSource.hashCode();
   }

   /** @deprecated */
   public void dump() {
      this.fRData.dump();
   }

   public static void compileRules(String var0, OutputStream var1) throws IOException {
      RBBIRuleBuilder.compileRules(var0, var1);
   }

   public int first() {
      this.fCachedBreakPositions = null;
      this.fDictionaryCharCount = 0;
      this.fPositionInCache = 0;
      this.fLastRuleStatusIndex = 0;
      this.fLastStatusIndexValid = true;
      if (this.fText == null) {
         return -1;
      } else {
         this.fText.first();
         return this.fText.getIndex();
      }
   }

   public int last() {
      this.fCachedBreakPositions = null;
      this.fDictionaryCharCount = 0;
      this.fPositionInCache = 0;
      if (this.fText == null) {
         this.fLastRuleStatusIndex = 0;
         this.fLastStatusIndexValid = true;
         return -1;
      } else {
         this.fLastStatusIndexValid = false;
         int var1 = this.fText.getEndIndex();
         this.fText.setIndex(var1);
         return var1;
      }
   }

   public int next(int var1) {
      int var2;
      for(var2 = this.current(); var1 > 0; --var1) {
         var2 = this.handleNext();
      }

      while(var1 < 0) {
         var2 = this.previous();
         ++var1;
      }

      return var2;
   }

   public int next() {
      return this.handleNext();
   }

   public int previous() {
      CharacterIterator var1 = this.getText();
      this.fLastStatusIndexValid = false;
      if (this.fCachedBreakPositions != null && this.fPositionInCache > 0) {
         --this.fPositionInCache;
         var1.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
         return this.fCachedBreakPositions[this.fPositionInCache];
      } else {
         this.fCachedBreakPositions = null;
         int var2 = this.current();
         int var3 = this.rulesPrevious();
         if (var3 == -1) {
            return var3;
         } else if (this.fDictionaryCharCount == 0) {
            return var3;
         } else if (this.fCachedBreakPositions != null) {
            this.fPositionInCache = this.fCachedBreakPositions.length - 2;
            return var3;
         } else {
            while(var3 < var2) {
               int var4 = this.handleNext();
               if (var4 >= var2) {
                  break;
               }

               var3 = var4;
            }

            if (this.fCachedBreakPositions != null) {
               for(this.fPositionInCache = 0; this.fPositionInCache < this.fCachedBreakPositions.length; ++this.fPositionInCache) {
                  if (this.fCachedBreakPositions[this.fPositionInCache] >= var2) {
                     --this.fPositionInCache;
                     break;
                  }
               }
            }

            this.fLastStatusIndexValid = false;
            var1.setIndex(var3);
            return var3;
         }
      }
   }

   private int rulesPrevious() {
      if (this.fText != null && this.current() != this.fText.getBeginIndex()) {
         if (this.fRData.fSRTable == null && this.fRData.fSFTable == null) {
            int var1 = this.current();
            CharacterIteration.previous32(this.fText);
            int var2 = this.handlePrevious(this.fRData.fRTable);
            if (var2 == -1) {
               var2 = this.fText.getBeginIndex();
               this.fText.setIndex(var2);
            }

            int var4 = 0;
            boolean var5 = false;

            while(true) {
               int var3 = this.handleNext();
               if (var3 == -1 || var3 >= var1) {
                  this.fText.setIndex(var2);
                  this.fLastRuleStatusIndex = var4;
                  this.fLastStatusIndexValid = var5;
                  return var2;
               }

               var2 = var3;
               var4 = this.fLastRuleStatusIndex;
               var5 = true;
            }
         } else {
            return this.handlePrevious(this.fRData.fRTable);
         }
      } else {
         this.fLastRuleStatusIndex = 0;
         this.fLastStatusIndexValid = true;
         return -1;
      }
   }

   public int following(int var1) {
      CharacterIterator var2 = this.getText();
      if (this.fCachedBreakPositions != null && var1 >= this.fCachedBreakPositions[0] && var1 < this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
         for(this.fPositionInCache = 0; this.fPositionInCache < this.fCachedBreakPositions.length && var1 >= this.fCachedBreakPositions[this.fPositionInCache]; ++this.fPositionInCache) {
         }

         var2.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
         return var2.getIndex();
      } else {
         this.fCachedBreakPositions = null;
         return this.rulesFollowing(var1);
      }
   }

   private int rulesFollowing(int var1) {
      this.fLastRuleStatusIndex = 0;
      this.fLastStatusIndexValid = true;
      if (this.fText != null && var1 < this.fText.getEndIndex()) {
         if (var1 < this.fText.getBeginIndex()) {
            return this.first();
         } else {
            boolean var2 = false;
            int var4;
            if (this.fRData.fSRTable != null) {
               this.fText.setIndex(var1);
               CharacterIteration.next32(this.fText);
               this.handlePrevious(this.fRData.fSRTable);

               for(var4 = this.next(); var4 <= var1; var4 = this.next()) {
               }

               return var4;
            } else if (this.fRData.fSFTable != null) {
               this.fText.setIndex(var1);
               CharacterIteration.previous32(this.fText);
               this.handleNext(this.fRData.fSFTable);

               for(int var3 = this.previous(); var3 > var1; var3 = var4) {
                  var4 = this.previous();
                  if (var4 <= var1) {
                     return var3;
                  }
               }

               var4 = this.next();
               if (var4 <= var1) {
                  return this.next();
               } else {
                  return var4;
               }
            } else {
               this.fText.setIndex(var1);
               if (var1 == this.fText.getBeginIndex()) {
                  return this.handleNext();
               } else {
                  for(var4 = this.previous(); var4 != -1 && var4 <= var1; var4 = this.next()) {
                  }

                  return var4;
               }
            }
         }
      } else {
         this.last();
         return this.next();
      }
   }

   public int preceding(int var1) {
      CharacterIterator var2 = this.getText();
      if (this.fCachedBreakPositions != null && var1 > this.fCachedBreakPositions[0] && var1 <= this.fCachedBreakPositions[this.fCachedBreakPositions.length - 1]) {
         for(this.fPositionInCache = 0; this.fPositionInCache < this.fCachedBreakPositions.length && var1 > this.fCachedBreakPositions[this.fPositionInCache]; ++this.fPositionInCache) {
         }

         --this.fPositionInCache;
         var2.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
         return var2.getIndex();
      } else {
         this.fCachedBreakPositions = null;
         return this.rulesPreceding(var1);
      }
   }

   private int rulesPreceding(int var1) {
      if (this.fText != null && var1 <= this.fText.getEndIndex()) {
         if (var1 < this.fText.getBeginIndex()) {
            return this.first();
         } else {
            int var2;
            if (this.fRData.fSFTable == null) {
               if (this.fRData.fSRTable != null) {
                  this.fText.setIndex(var1);
                  CharacterIteration.next32(this.fText);
                  this.handlePrevious(this.fRData.fSRTable);

                  for(int var3 = this.next(); var3 < var1; var3 = var2) {
                     var2 = this.next();
                     if (var2 >= var1) {
                        return var3;
                     }
                  }

                  var2 = this.previous();
                  if (var2 >= var1) {
                     return this.previous();
                  } else {
                     return var2;
                  }
               } else {
                  this.fText.setIndex(var1);
                  return this.previous();
               }
            } else {
               this.fText.setIndex(var1);
               CharacterIteration.previous32(this.fText);
               this.handleNext(this.fRData.fSFTable);

               for(var2 = this.previous(); var2 >= var1; var2 = this.previous()) {
               }

               return var2;
            }
         }
      } else {
         return this.last();
      }
   }

   protected static final void checkOffset(int var0, CharacterIterator var1) {
      if (var0 < var1.getBeginIndex() || var0 > var1.getEndIndex()) {
         throw new IllegalArgumentException("offset out of bounds");
      }
   }

   public boolean isBoundary(int var1) {
      checkOffset(var1, this.fText);
      if (var1 == this.fText.getBeginIndex()) {
         this.first();
         return true;
      } else if (var1 == this.fText.getEndIndex()) {
         this.last();
         return true;
      } else {
         this.fText.setIndex(var1);
         CharacterIteration.previous32(this.fText);
         int var2 = this.fText.getIndex();
         boolean var3 = this.following(var2) == var1;
         return var3;
      }
   }

   public int current() {
      return this.fText != null ? this.fText.getIndex() : -1;
   }

   private void makeRuleStatusValid() {
      if (!this.fLastStatusIndexValid) {
         int var1 = this.current();
         if (var1 != -1 && var1 != this.fText.getBeginIndex()) {
            int var2 = this.fText.getIndex();
            this.first();

            int var3;
            for(var3 = this.current(); this.fText.getIndex() < var2; var3 = this.next()) {
            }

            Assert.assrt(var2 == var3);
         } else {
            this.fLastRuleStatusIndex = 0;
            this.fLastStatusIndexValid = true;
         }

         Assert.assrt(this.fLastStatusIndexValid);
         Assert.assrt(this.fLastRuleStatusIndex >= 0 && this.fLastRuleStatusIndex < this.fRData.fStatusTable.length);
      }

   }

   public int getRuleStatus() {
      this.makeRuleStatusValid();
      int var1 = this.fLastRuleStatusIndex + this.fRData.fStatusTable[this.fLastRuleStatusIndex];
      int var2 = this.fRData.fStatusTable[var1];
      return var2;
   }

   public int getRuleStatusVec(int[] var1) {
      this.makeRuleStatusValid();
      int var2 = this.fRData.fStatusTable[this.fLastRuleStatusIndex];
      if (var1 != null) {
         int var3 = Math.min(var2, var1.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            var1[var4] = this.fRData.fStatusTable[this.fLastRuleStatusIndex + var4 + 1];
         }
      }

      return var2;
   }

   public CharacterIterator getText() {
      return this.fText;
   }

   public void setText(CharacterIterator var1) {
      this.fText = var1;
      int var2 = this.first();
      if (var1 != null) {
         this.fUseDictionary = (this.fBreakType == 1 || this.fBreakType == 2) && var1.getEndIndex() != var2;
      }

   }

   /** @deprecated */
   void setBreakType(int var1) {
      this.fBreakType = var1;
      if (var1 != 1 && var1 != 2) {
         this.fUseDictionary = false;
      }

   }

   /** @deprecated */
   int getBreakType() {
      return this.fBreakType;
   }

   /** @deprecated */
   private LanguageBreakEngine getEngineFor(int var1) {
      if (var1 != Integer.MAX_VALUE && this.fUseDictionary) {
         Iterator var2 = this.fBreakEngines.iterator();

         LanguageBreakEngine var3;
         do {
            if (!var2.hasNext()) {
               int var6 = UCharacter.getIntPropertyValue(var1, 4106);
               var3 = null;

               Object var7;
               try {
                  switch(var6) {
                  case 17:
                  case 20:
                  case 22:
                     if (this.getBreakType() == 1) {
                        var7 = new CjkBreakEngine(false);
                     } else {
                        this.fUnhandledBreakEngine.handleChar(var1, this.getBreakType());
                        var7 = this.fUnhandledBreakEngine;
                     }
                     break;
                  case 18:
                     if (this.getBreakType() == 1) {
                        var7 = new CjkBreakEngine(true);
                     } else {
                        this.fUnhandledBreakEngine.handleChar(var1, this.getBreakType());
                        var7 = this.fUnhandledBreakEngine;
                     }
                     break;
                  case 38:
                     var7 = new ThaiBreakEngine();
                     break;
                  default:
                     this.fUnhandledBreakEngine.handleChar(var1, this.getBreakType());
                     var7 = this.fUnhandledBreakEngine;
                  }
               } catch (IOException var5) {
                  var7 = null;
               }

               if (var7 != null) {
                  this.fBreakEngines.add(var7);
               }

               return (LanguageBreakEngine)var7;
            }

            var3 = (LanguageBreakEngine)var2.next();
         } while(!var3.handles(var1, this.fBreakType));

         return var3;
      } else {
         return null;
      }
   }

   private int handleNext() {
      if (this.fCachedBreakPositions == null || this.fPositionInCache == this.fCachedBreakPositions.length - 1) {
         int var1 = this.fText.getIndex();
         this.fDictionaryCharCount = 0;
         int var2 = this.handleNext(this.fRData.fFTable);
         if (this.fDictionaryCharCount <= 1 || var2 - var1 <= 1) {
            this.fCachedBreakPositions = null;
            return var2;
         }

         this.fText.setIndex(var1);
         LanguageBreakEngine var3 = this.getEngineFor(CharacterIteration.current32(this.fText));
         if (var3 == null) {
            this.fText.setIndex(var2);
            return var2;
         }

         Stack var4 = new Stack();
         var3.findBreaks(this.fText, var1, var2, false, this.getBreakType(), var4);
         int var5 = var4.size();
         this.fCachedBreakPositions = new int[var5 + 2];
         this.fCachedBreakPositions[0] = var1;

         for(int var6 = 0; var6 < var5; ++var6) {
            this.fCachedBreakPositions[var6 + 1] = (Integer)var4.elementAt(var6);
         }

         this.fCachedBreakPositions[var5 + 1] = var2;
         this.fPositionInCache = 0;
      }

      if (this.fCachedBreakPositions != null) {
         ++this.fPositionInCache;
         this.fText.setIndex(this.fCachedBreakPositions[this.fPositionInCache]);
         return this.fCachedBreakPositions[this.fPositionInCache];
      } else {
         Assert.assrt(false);
         return -1;
      }
   }

   private int handleNext(short[] var1) {
      if (TRACE) {
         System.out.println("Handle Next   pos      char  state category");
      }

      this.fLastStatusIndexValid = true;
      this.fLastRuleStatusIndex = 0;
      CharacterIterator var2 = this.fText;
      CharTrie var3 = this.fRData.fTrie;
      int var4 = var2.current();
      if (var4 >= 55296) {
         var4 = CharacterIteration.nextTrail32(var2, var4);
         if (var4 == Integer.MAX_VALUE) {
            return -1;
         }
      }

      int var5 = var2.getIndex();
      int var6 = var5;
      short var7 = 1;
      int var8 = this.fRData.getRowIndex(var7);
      short var9 = 3;
      short var10 = var1[5];
      byte var11 = 1;
      if ((var10 & 2) != 0) {
         var9 = 2;
         var11 = 0;
         if (TRACE) {
            System.out.print("            " + RBBIDataWrapper.intToString(var2.getIndex(), 5));
            System.out.print(RBBIDataWrapper.intToHexString(var4, 10));
            System.out.println(RBBIDataWrapper.intToString(var7, 7) + RBBIDataWrapper.intToString(var9, 6));
         }
      }

      short var12 = 0;
      short var13 = 0;
      int var14 = 0;

      while(var7 != 0) {
         if (var4 == Integer.MAX_VALUE) {
            if (var11 == 2) {
               if (var14 > var6) {
                  var6 = var14;
                  this.fLastRuleStatusIndex = var13;
               }
               break;
            }

            var11 = 2;
            var9 = 1;
         } else if (var11 == 1) {
            var9 = (short)var3.getCodePointValue(var4);
            if ((var9 & 16384) != 0) {
               ++this.fDictionaryCharCount;
               var9 &= -16385;
            }

            if (TRACE) {
               System.out.print("            " + RBBIDataWrapper.intToString(var2.getIndex(), 5));
               System.out.print(RBBIDataWrapper.intToHexString(var4, 10));
               System.out.println(RBBIDataWrapper.intToString(var7, 7) + RBBIDataWrapper.intToString(var9, 6));
            }

            var4 = var2.next();
            if (var4 >= 55296) {
               var4 = CharacterIteration.nextTrail32(var2, var4);
            }
         } else {
            var11 = 1;
         }

         var7 = var1[var8 + 4 + var9];
         var8 = this.fRData.getRowIndex(var7);
         if (var1[var8 + 0] == -1) {
            var6 = var2.getIndex();
            if (var4 >= 65536 && var4 <= 1114111) {
               --var6;
            }

            this.fLastRuleStatusIndex = var1[var8 + 2];
         }

         if (var1[var8 + 1] != 0) {
            if (var12 != 0 && var1[var8 + 0] == var12) {
               var6 = var14;
               this.fLastRuleStatusIndex = var13;
               var12 = 0;
               if ((var10 & 1) != 0) {
                  var2.setIndex(var14);
                  return var14;
               }
            } else {
               var14 = var2.getIndex();
               if (var4 >= 65536 && var4 <= 1114111) {
                  --var14;
               }

               var12 = var1[var8 + 1];
               var13 = var1[var8 + 2];
            }
         } else if (var1[var8 + 0] != 0) {
            var12 = 0;
         }
      }

      if (var6 == var5) {
         if (TRACE) {
            System.out.println("Iterator did not move. Advancing by 1.");
         }

         var2.setIndex(var5);
         CharacterIteration.next32(var2);
         var6 = var2.getIndex();
      } else {
         var2.setIndex(var6);
      }

      if (TRACE) {
         System.out.println("result = " + var6);
      }

      return var6;
   }

   private int handlePrevious(short[] var1) {
      if (this.fText != null && var1 != null) {
         boolean var3 = false;
         short var7 = 0;
         boolean var8 = false;
         boolean var9 = false;
         int var10 = 0;
         boolean var11 = (var1[5] & 1) != 0;
         this.fLastStatusIndexValid = false;
         this.fLastRuleStatusIndex = 0;
         int var15 = this.fText.getIndex();
         int var13 = var15;
         int var6 = CharacterIteration.previous32(this.fText);
         short var2 = 1;
         int var5 = this.fRData.getRowIndex(var2);
         int var12 = 3;
         byte var4 = 1;
         if ((var1[5] & 2) != 0) {
            var12 = 2;
            var4 = 0;
         }

         if (TRACE) {
            System.out.println("Handle Prev   pos   char  state category ");
         }

         while(true) {
            if (var6 == Integer.MAX_VALUE) {
               if (var4 == 2 || this.fRData.fHeader.fVersion == 1) {
                  if (var10 < var13) {
                     var13 = var10;
                     boolean var14 = false;
                  } else if (var13 == var15) {
                     this.fText.setIndex(var15);
                     CharacterIteration.previous32(this.fText);
                  }
                  break;
               }

               var4 = 2;
               var12 = 1;
            }

            if (var4 == 1) {
               var12 = (short)this.fRData.fTrie.getCodePointValue(var6);
               if ((var12 & 16384) != 0) {
                  ++this.fDictionaryCharCount;
                  var12 &= -16385;
               }
            }

            if (TRACE) {
               System.out.print("             " + this.fText.getIndex() + "   ");
               if (32 <= var6 && var6 < 127) {
                  System.out.print("  " + var6 + "  ");
               } else {
                  System.out.print(" " + Integer.toHexString(var6) + " ");
               }

               System.out.println(" " + var2 + "  " + var12 + " ");
            }

            var2 = var1[var5 + 4 + var12];
            var5 = this.fRData.getRowIndex(var2);
            if (var1[var5 + 0] == -1) {
               var13 = this.fText.getIndex();
            }

            if (var1[var5 + 1] != 0) {
               if (var7 != 0 && var1[var5 + 0] == var7) {
                  var13 = var10;
                  var7 = 0;
                  if (var11) {
                     break;
                  }
               } else {
                  var10 = this.fText.getIndex();
                  var7 = var1[var5 + 1];
               }
            } else if (var1[var5 + 0] != 0 && !var11) {
               var7 = 0;
            }

            if (var2 == 0) {
               break;
            }

            if (var4 == 1) {
               var6 = CharacterIteration.previous32(this.fText);
            } else if (var4 == 0) {
               var4 = 1;
            }
         }

         if (var13 == var15) {
            this.fText.setIndex(var15);
            CharacterIteration.previous32(this.fText);
            var13 = this.fText.getIndex();
         }

         this.fText.setIndex(var13);
         if (TRACE) {
            System.out.println("Result = " + var13);
         }

         return var13;
      } else {
         return 0;
      }
   }
}
