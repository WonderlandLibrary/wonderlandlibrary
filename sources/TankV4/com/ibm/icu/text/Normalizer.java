package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.lang.UCharacter;
import java.nio.CharBuffer;
import java.text.CharacterIterator;

public final class Normalizer implements Cloneable {
   private UCharacterIterator text;
   private Normalizer2 norm2;
   private Normalizer.Mode mode;
   private int options;
   private int currentIndex;
   private int nextIndex;
   private StringBuilder buffer;
   private int bufferPos;
   public static final int UNICODE_3_2 = 32;
   public static final int DONE = -1;
   public static final Normalizer.Mode NONE = new Normalizer.NONEMode();
   public static final Normalizer.Mode NFD = new Normalizer.NFDMode();
   public static final Normalizer.Mode NFKD = new Normalizer.NFKDMode();
   public static final Normalizer.Mode NFC = new Normalizer.NFCMode();
   public static final Normalizer.Mode DEFAULT;
   public static final Normalizer.Mode NFKC;
   public static final Normalizer.Mode FCD;
   /** @deprecated */
   public static final Normalizer.Mode NO_OP;
   /** @deprecated */
   public static final Normalizer.Mode COMPOSE;
   /** @deprecated */
   public static final Normalizer.Mode COMPOSE_COMPAT;
   /** @deprecated */
   public static final Normalizer.Mode DECOMP;
   /** @deprecated */
   public static final Normalizer.Mode DECOMP_COMPAT;
   /** @deprecated */
   public static final int IGNORE_HANGUL = 1;
   public static final Normalizer.QuickCheckResult NO;
   public static final Normalizer.QuickCheckResult YES;
   public static final Normalizer.QuickCheckResult MAYBE;
   public static final int FOLD_CASE_DEFAULT = 0;
   public static final int INPUT_IS_FCD = 131072;
   public static final int COMPARE_IGNORE_CASE = 65536;
   public static final int COMPARE_CODE_POINT_ORDER = 32768;
   public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
   public static final int COMPARE_NORM_OPTIONS_SHIFT = 20;
   private static final int COMPARE_EQUIV = 524288;

   public Normalizer(String var1, Normalizer.Mode var2, int var3) {
      this.text = UCharacterIterator.getInstance(var1);
      this.mode = var2;
      this.options = var3;
      this.norm2 = var2.getNormalizer2(var3);
      this.buffer = new StringBuilder();
   }

   public Normalizer(CharacterIterator var1, Normalizer.Mode var2, int var3) {
      this.text = UCharacterIterator.getInstance((CharacterIterator)var1.clone());
      this.mode = var2;
      this.options = var3;
      this.norm2 = var2.getNormalizer2(var3);
      this.buffer = new StringBuilder();
   }

   public Normalizer(UCharacterIterator var1, Normalizer.Mode var2, int var3) {
      try {
         this.text = (UCharacterIterator)var1.clone();
         this.mode = var2;
         this.options = var3;
         this.norm2 = var2.getNormalizer2(var3);
         this.buffer = new StringBuilder();
      } catch (CloneNotSupportedException var5) {
         throw new IllegalStateException(var5.toString());
      }
   }

   public Object clone() {
      try {
         Normalizer var1 = (Normalizer)super.clone();
         var1.text = (UCharacterIterator)this.text.clone();
         var1.mode = this.mode;
         var1.options = this.options;
         var1.norm2 = this.norm2;
         var1.buffer = new StringBuilder(this.buffer);
         var1.bufferPos = this.bufferPos;
         var1.currentIndex = this.currentIndex;
         var1.nextIndex = this.nextIndex;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException(var2);
      }
   }

   private static final Normalizer2 getComposeNormalizer2(boolean var0, int var1) {
      return (var0 ? NFKC : NFC).getNormalizer2(var1);
   }

   private static final Normalizer2 getDecomposeNormalizer2(boolean var0, int var1) {
      return (var0 ? NFKD : NFD).getNormalizer2(var1);
   }

   public static String compose(String var0, boolean var1) {
      return compose(var0, var1, 0);
   }

   public static String compose(String var0, boolean var1, int var2) {
      return getComposeNormalizer2(var1, var2).normalize(var0);
   }

   public static int compose(char[] var0, char[] var1, boolean var2, int var3) {
      return compose(var0, 0, var0.length, var1, 0, var1.length, var2, var3);
   }

   public static int compose(char[] var0, int var1, int var2, char[] var3, int var4, int var5, boolean var6, int var7) {
      CharBuffer var8 = CharBuffer.wrap(var0, var1, var2 - var1);
      Normalizer.CharsAppendable var9 = new Normalizer.CharsAppendable(var3, var4, var5);
      getComposeNormalizer2(var6, var7).normalize(var8, (Appendable)var9);
      return var9.length();
   }

   public static String decompose(String var0, boolean var1) {
      return decompose(var0, var1, 0);
   }

   public static String decompose(String var0, boolean var1, int var2) {
      return getDecomposeNormalizer2(var1, var2).normalize(var0);
   }

   public static int decompose(char[] var0, char[] var1, boolean var2, int var3) {
      return decompose(var0, 0, var0.length, var1, 0, var1.length, var2, var3);
   }

   public static int decompose(char[] var0, int var1, int var2, char[] var3, int var4, int var5, boolean var6, int var7) {
      CharBuffer var8 = CharBuffer.wrap(var0, var1, var2 - var1);
      Normalizer.CharsAppendable var9 = new Normalizer.CharsAppendable(var3, var4, var5);
      getDecomposeNormalizer2(var6, var7).normalize(var8, (Appendable)var9);
      return var9.length();
   }

   public static String normalize(String var0, Normalizer.Mode var1, int var2) {
      return var1.getNormalizer2(var2).normalize(var0);
   }

   public static String normalize(String var0, Normalizer.Mode var1) {
      return normalize(var0, var1, 0);
   }

   public static int normalize(char[] var0, char[] var1, Normalizer.Mode var2, int var3) {
      return normalize(var0, 0, var0.length, var1, 0, var1.length, var2, var3);
   }

   public static int normalize(char[] var0, int var1, int var2, char[] var3, int var4, int var5, Normalizer.Mode var6, int var7) {
      CharBuffer var8 = CharBuffer.wrap(var0, var1, var2 - var1);
      Normalizer.CharsAppendable var9 = new Normalizer.CharsAppendable(var3, var4, var5);
      var6.getNormalizer2(var7).normalize(var8, (Appendable)var9);
      return var9.length();
   }

   public static String normalize(int var0, Normalizer.Mode var1, int var2) {
      if (var1 == NFD && var2 == 0) {
         String var3 = Norm2AllModes.getNFCInstance().impl.getDecomposition(var0);
         if (var3 == null) {
            var3 = UTF16.valueOf(var0);
         }

         return var3;
      } else {
         return normalize(UTF16.valueOf(var0), var1, var2);
      }
   }

   public static String normalize(int var0, Normalizer.Mode var1) {
      return normalize(var0, var1, 0);
   }

   public static Normalizer.QuickCheckResult quickCheck(String var0, Normalizer.Mode var1) {
      return quickCheck((String)var0, var1, 0);
   }

   public static Normalizer.QuickCheckResult quickCheck(String var0, Normalizer.Mode var1, int var2) {
      return var1.getNormalizer2(var2).quickCheck(var0);
   }

   public static Normalizer.QuickCheckResult quickCheck(char[] var0, Normalizer.Mode var1, int var2) {
      return quickCheck(var0, 0, var0.length, var1, var2);
   }

   public static Normalizer.QuickCheckResult quickCheck(char[] var0, int var1, int var2, Normalizer.Mode var3, int var4) {
      CharBuffer var5 = CharBuffer.wrap(var0, var1, var2 - var1);
      return var3.getNormalizer2(var4).quickCheck(var5);
   }

   public static boolean isNormalized(char[] var0, int var1, int var2, Normalizer.Mode var3, int var4) {
      CharBuffer var5 = CharBuffer.wrap(var0, var1, var2 - var1);
      return var3.getNormalizer2(var4).isNormalized(var5);
   }

   public static boolean isNormalized(String var0, Normalizer.Mode var1, int var2) {
      return var1.getNormalizer2(var2).isNormalized(var0);
   }

   public static boolean isNormalized(int var0, Normalizer.Mode var1, int var2) {
      return isNormalized(UTF16.valueOf(var0), var1, var2);
   }

   public static int compare(char[] var0, int var1, int var2, char[] var3, int var4, int var5, int var6) {
      if (var0 != null && var1 >= 0 && var2 >= 0 && var3 != null && var4 >= 0 && var5 >= 0 && var2 >= var1 && var5 >= var4) {
         return internalCompare(CharBuffer.wrap(var0, var1, var2 - var1), CharBuffer.wrap(var3, var4, var5 - var4), var6);
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int compare(String var0, String var1, int var2) {
      return internalCompare(var0, var1, var2);
   }

   public static int compare(char[] var0, char[] var1, int var2) {
      return internalCompare(CharBuffer.wrap(var0), CharBuffer.wrap(var1), var2);
   }

   public static int compare(int var0, int var1, int var2) {
      return internalCompare(UTF16.valueOf(var0), UTF16.valueOf(var1), var2 | 131072);
   }

   public static int compare(int var0, String var1, int var2) {
      return internalCompare(UTF16.valueOf(var0), var1, var2);
   }

   public static int concatenate(char[] var0, int var1, int var2, char[] var3, int var4, int var5, char[] var6, int var7, int var8, Normalizer.Mode var9, int var10) {
      if (var6 == null) {
         throw new IllegalArgumentException();
      } else if (var3 == var6 && var4 < var8 && var7 < var5) {
         throw new IllegalArgumentException("overlapping right and dst ranges");
      } else {
         StringBuilder var11 = new StringBuilder(var2 - var1 + var5 - var4 + 16);
         var11.append(var0, var1, var2 - var1);
         CharBuffer var12 = CharBuffer.wrap(var3, var4, var5 - var4);
         var9.getNormalizer2(var10).append(var11, var12);
         int var13 = var11.length();
         if (var13 <= var8 - var7) {
            var11.getChars(0, var13, var6, var7);
            return var13;
         } else {
            throw new IndexOutOfBoundsException(Integer.toString(var13));
         }
      }
   }

   public static String concatenate(char[] var0, char[] var1, Normalizer.Mode var2, int var3) {
      StringBuilder var4 = (new StringBuilder(var0.length + var1.length + 16)).append(var0);
      return var2.getNormalizer2(var3).append(var4, CharBuffer.wrap(var1)).toString();
   }

   public static String concatenate(String var0, String var1, Normalizer.Mode var2, int var3) {
      StringBuilder var4 = (new StringBuilder(var0.length() + var1.length() + 16)).append(var0);
      return var2.getNormalizer2(var3).append(var4, var1).toString();
   }

   public static int getFC_NFKC_Closure(int var0, char[] var1) {
      String var2 = getFC_NFKC_Closure(var0);
      int var3 = var2.length();
      if (var3 != 0 && var1 != null && var3 <= var1.length) {
         var2.getChars(0, var3, var1, 0);
      }

      return var3;
   }

   public static String getFC_NFKC_Closure(int var0) {
      Normalizer2 var1 = Normalizer.ModeImpl.access$300(Normalizer.NFKCModeImpl.access$1000());
      UCaseProps var2 = UCaseProps.INSTANCE;
      StringBuilder var3 = new StringBuilder();
      int var4 = var2.toFullFolding(var0, var3, 0);
      if (var4 < 0) {
         Normalizer2Impl var5 = ((Norm2AllModes.Normalizer2WithImpl)var1).impl;
         if (var5.getCompQuickCheck(var5.getNorm16(var0)) != 0) {
            return "";
         }

         var3.appendCodePoint(var0);
      } else if (var4 > 31) {
         var3.appendCodePoint(var4);
      }

      String var7 = var1.normalize(var3);
      String var6 = var1.normalize(UCharacter.foldCase(var7, 0));
      return var7.equals(var6) ? "" : var6;
   }

   public int current() {
      return this.bufferPos >= this.buffer.length() && !(this < 0) ? -1 : this.buffer.codePointAt(this.bufferPos);
   }

   public int next() {
      if (this.bufferPos >= this.buffer.length() && !(this < 0)) {
         return -1;
      } else {
         int var1 = this.buffer.codePointAt(this.bufferPos);
         this.bufferPos += Character.charCount(var1);
         return var1;
      }
   }

   public int previous() {
      if (this.bufferPos <= 0 && !(this >= 0)) {
         return -1;
      } else {
         int var1 = this.buffer.codePointBefore(this.bufferPos);
         this.bufferPos -= Character.charCount(var1);
         return var1;
      }
   }

   public void reset() {
      this.text.setToStart();
      this.currentIndex = this.nextIndex = 0;
      this.clearBuffer();
   }

   public void setIndexOnly(int var1) {
      this.text.setIndex(var1);
      this.currentIndex = this.nextIndex = var1;
      this.clearBuffer();
   }

   /** @deprecated */
   public int setIndex(int var1) {
      this.setIndexOnly(var1);
      return this.current();
   }

   /** @deprecated */
   public int getBeginIndex() {
      return 0;
   }

   /** @deprecated */
   public int getEndIndex() {
      return this.endIndex();
   }

   public int first() {
      this.reset();
      return this.next();
   }

   public int last() {
      this.text.setToLimit();
      this.currentIndex = this.nextIndex = this.text.getIndex();
      this.clearBuffer();
      return this.previous();
   }

   public int getIndex() {
      return this.bufferPos < this.buffer.length() ? this.currentIndex : this.nextIndex;
   }

   public int startIndex() {
      return 0;
   }

   public int endIndex() {
      return this.text.getLength();
   }

   public void setMode(Normalizer.Mode var1) {
      this.mode = var1;
      this.norm2 = this.mode.getNormalizer2(this.options);
   }

   public Normalizer.Mode getMode() {
      return this.mode;
   }

   public void setOption(int var1, boolean var2) {
      if (var2) {
         this.options |= var1;
      } else {
         this.options &= ~var1;
      }

      this.norm2 = this.mode.getNormalizer2(this.options);
   }

   public int getOption(int var1) {
      return (this.options & var1) != 0 ? 1 : 0;
   }

   public int getText(char[] var1) {
      return this.text.getText(var1);
   }

   public int getLength() {
      return this.text.getLength();
   }

   public String getText() {
      return this.text.getText();
   }

   public void setText(StringBuffer var1) {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var1);
      if (var2 == null) {
         throw new IllegalStateException("Could not create a new UCharacterIterator");
      } else {
         this.text = var2;
         this.reset();
      }
   }

   public void setText(char[] var1) {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var1);
      if (var2 == null) {
         throw new IllegalStateException("Could not create a new UCharacterIterator");
      } else {
         this.text = var2;
         this.reset();
      }
   }

   public void setText(String var1) {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var1);
      if (var2 == null) {
         throw new IllegalStateException("Could not create a new UCharacterIterator");
      } else {
         this.text = var2;
         this.reset();
      }
   }

   public void setText(CharacterIterator var1) {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var1);
      if (var2 == null) {
         throw new IllegalStateException("Could not create a new UCharacterIterator");
      } else {
         this.text = var2;
         this.reset();
      }
   }

   public void setText(UCharacterIterator var1) {
      try {
         UCharacterIterator var2 = (UCharacterIterator)var1.clone();
         if (var2 == null) {
            throw new IllegalStateException("Could not create a new UCharacterIterator");
         } else {
            this.text = var2;
            this.reset();
         }
      } catch (CloneNotSupportedException var3) {
         throw new IllegalStateException("Could not clone the UCharacterIterator");
      }
   }

   private void clearBuffer() {
      this.buffer.setLength(0);
      this.bufferPos = 0;
   }

   private static int internalCompare(CharSequence var0, CharSequence var1, int var2) {
      int var3 = var2 >>> 20;
      var2 |= 524288;
      if ((var2 & 131072) == 0 || (var2 & 1) != 0) {
         Normalizer2 var4;
         if ((var2 & 1) != 0) {
            var4 = NFD.getNormalizer2(var3);
         } else {
            var4 = FCD.getNormalizer2(var3);
         }

         int var5 = var4.spanQuickCheckYes((CharSequence)var0);
         int var6 = var4.spanQuickCheckYes((CharSequence)var1);
         StringBuilder var7;
         if (var5 < ((CharSequence)var0).length()) {
            var7 = (new StringBuilder(((CharSequence)var0).length() + 16)).append((CharSequence)var0, 0, var5);
            var0 = var4.normalizeSecondAndAppend(var7, ((CharSequence)var0).subSequence(var5, ((CharSequence)var0).length()));
         }

         if (var6 < ((CharSequence)var1).length()) {
            var7 = (new StringBuilder(((CharSequence)var1).length() + 16)).append((CharSequence)var1, 0, var6);
            var1 = var4.normalizeSecondAndAppend(var7, ((CharSequence)var1).subSequence(var6, ((CharSequence)var1).length()));
         }
      }

      return cmpEquivFold((CharSequence)var0, (CharSequence)var1, var2);
   }

   private static final Normalizer.CmpEquivLevel[] createCmpEquivLevelStack() {
      return new Normalizer.CmpEquivLevel[]{new Normalizer.CmpEquivLevel(), new Normalizer.CmpEquivLevel()};
   }

   static int cmpEquivFold(CharSequence var0, CharSequence var1, int var2) {
      Normalizer.CmpEquivLevel[] var10 = null;
      Normalizer.CmpEquivLevel[] var11 = null;
      Normalizer2Impl var3;
      if ((var2 & 524288) != 0) {
         var3 = Norm2AllModes.getNFCInstance().impl;
      } else {
         var3 = null;
      }

      UCaseProps var4;
      StringBuilder var14;
      StringBuilder var15;
      if ((var2 & 65536) != 0) {
         var4 = UCaseProps.INSTANCE;
         var14 = new StringBuilder();
         var15 = new StringBuilder();
      } else {
         var4 = null;
         var15 = null;
         var14 = null;
      }

      int var5 = 0;
      int var7 = ((CharSequence)var0).length();
      int var6 = 0;
      int var8 = ((CharSequence)var1).length();
      int var17 = 0;
      int var16 = 0;
      int var19 = -1;
      int var18 = -1;

      while(true) {
         while(true) {
            if (var18 < 0) {
               while(true) {
                  if (var5 != var7) {
                     var18 = ((CharSequence)var0).charAt(var5++);
                     break;
                  }

                  if (var16 == 0) {
                     var18 = -1;
                     break;
                  }

                  do {
                     --var16;
                     var0 = var10[var16].cs;
                  } while(var0 == null);

                  var5 = var10[var16].s;
                  var7 = ((CharSequence)var0).length();
               }
            }

            if (var19 < 0) {
               while(true) {
                  if (var6 != var8) {
                     var19 = ((CharSequence)var1).charAt(var6++);
                     break;
                  }

                  if (var17 == 0) {
                     var19 = -1;
                     break;
                  }

                  do {
                     --var17;
                     var1 = var11[var17].cs;
                  } while(var1 == null);

                  var6 = var11[var17].s;
                  var8 = ((CharSequence)var1).length();
               }
            }

            if (var18 != var19) {
               if (var18 < 0) {
                  return -1;
               }

               if (var19 < 0) {
                  return 1;
               }

               int var20 = var18;
               char var22;
               if (UTF16.isSurrogate((char)var18)) {
                  if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var18)) {
                     if (var5 != var7 && Character.isLowSurrogate(var22 = ((CharSequence)var0).charAt(var5))) {
                        var20 = Character.toCodePoint((char)var18, var22);
                     }
                  } else if (0 <= var5 - 2 && Character.isHighSurrogate(var22 = ((CharSequence)var0).charAt(var5 - 2))) {
                     var20 = Character.toCodePoint(var22, (char)var18);
                  }
               }

               int var21 = var19;
               if (UTF16.isSurrogate((char)var19)) {
                  if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var19)) {
                     if (var6 != var8 && Character.isLowSurrogate(var22 = ((CharSequence)var1).charAt(var6))) {
                        var21 = Character.toCodePoint((char)var19, var22);
                     }
                  } else if (0 <= var6 - 2 && Character.isHighSurrogate(var22 = ((CharSequence)var1).charAt(var6 - 2))) {
                     var21 = Character.toCodePoint(var22, (char)var19);
                  }
               }

               int var9;
               if (var16 == 0 && (var2 & 65536) != 0 && (var9 = var4.toFullFolding(var20, var14, var2)) >= 0) {
                  if (UTF16.isSurrogate((char)var18)) {
                     if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var18)) {
                        ++var5;
                     } else {
                        --var6;
                        var19 = ((CharSequence)var1).charAt(var6 - 1);
                     }
                  }

                  if (var10 == null) {
                     var10 = createCmpEquivLevelStack();
                  }

                  var10[0].cs = (CharSequence)var0;
                  var10[0].s = var5;
                  ++var16;
                  if (var9 <= 31) {
                     var14.delete(0, var14.length() - var9);
                  } else {
                     var14.setLength(0);
                     var14.appendCodePoint(var9);
                  }

                  var0 = var14;
                  var5 = 0;
                  var7 = var14.length();
                  var18 = -1;
               } else if (var17 == 0 && (var2 & 65536) != 0 && (var9 = var4.toFullFolding(var21, var15, var2)) >= 0) {
                  if (UTF16.isSurrogate((char)var19)) {
                     if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var19)) {
                        ++var6;
                     } else {
                        --var5;
                        var18 = ((CharSequence)var0).charAt(var5 - 1);
                     }
                  }

                  if (var11 == null) {
                     var11 = createCmpEquivLevelStack();
                  }

                  var11[0].cs = (CharSequence)var1;
                  var11[0].s = var6;
                  ++var17;
                  if (var9 <= 31) {
                     var15.delete(0, var15.length() - var9);
                  } else {
                     var15.setLength(0);
                     var15.appendCodePoint(var9);
                  }

                  var1 = var15;
                  var6 = 0;
                  var8 = var15.length();
                  var19 = -1;
               } else {
                  String var12;
                  if (var16 < 2 && (var2 & 524288) != 0 && (var12 = var3.getDecomposition(var20)) != null) {
                     if (UTF16.isSurrogate((char)var18)) {
                        if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var18)) {
                           ++var5;
                        } else {
                           --var6;
                           var19 = ((CharSequence)var1).charAt(var6 - 1);
                        }
                     }

                     if (var10 == null) {
                        var10 = createCmpEquivLevelStack();
                     }

                     var10[var16].cs = (CharSequence)var0;
                     var10[var16].s = var5;
                     ++var16;
                     if (var16 < 2) {
                        var10[var16++].cs = null;
                     }

                     var0 = var12;
                     var5 = 0;
                     var7 = var12.length();
                     var18 = -1;
                  } else {
                     String var13;
                     if (var17 >= 2 || (var2 & 524288) == 0 || (var13 = var3.getDecomposition(var21)) == null) {
                        if (var18 >= 55296 && var19 >= 55296 && (var2 & '耀') != 0) {
                           if ((var18 > 56319 || var5 == var7 || !Character.isLowSurrogate(((CharSequence)var0).charAt(var5))) && (!Character.isLowSurrogate((char)var18) || 0 == var5 - 1 || !Character.isHighSurrogate(((CharSequence)var0).charAt(var5 - 2)))) {
                              var18 -= 10240;
                           }

                           if ((var19 > 56319 || var6 == var8 || !Character.isLowSurrogate(((CharSequence)var1).charAt(var6))) && (!Character.isLowSurrogate((char)var19) || 0 == var6 - 1 || !Character.isHighSurrogate(((CharSequence)var1).charAt(var6 - 2)))) {
                              var19 -= 10240;
                           }
                        }

                        return var18 - var19;
                     }

                     if (UTF16.isSurrogate((char)var19)) {
                        if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var19)) {
                           ++var6;
                        } else {
                           --var5;
                           var18 = ((CharSequence)var0).charAt(var5 - 1);
                        }
                     }

                     if (var11 == null) {
                        var11 = createCmpEquivLevelStack();
                     }

                     var11[var17].cs = (CharSequence)var1;
                     var11[var17].s = var6;
                     ++var17;
                     if (var17 < 2) {
                        var11[var17++].cs = null;
                     }

                     var1 = var13;
                     var6 = 0;
                     var8 = var13.length();
                     var19 = -1;
                  }
               }
            } else {
               if (var18 < 0) {
                  return 0;
               }

               var19 = -1;
               var18 = -1;
            }
         }
      }
   }

   static {
      DEFAULT = NFC;
      NFKC = new Normalizer.NFKCMode();
      FCD = new Normalizer.FCDMode();
      NO_OP = NONE;
      COMPOSE = NFC;
      COMPOSE_COMPAT = NFKC;
      DECOMP = NFD;
      DECOMP_COMPAT = NFKD;
      NO = new Normalizer.QuickCheckResult(0);
      YES = new Normalizer.QuickCheckResult(1);
      MAYBE = new Normalizer.QuickCheckResult(2);
   }

   private static final class CharsAppendable implements Appendable {
      private final char[] chars;
      private final int start;
      private final int limit;
      private int offset;

      public CharsAppendable(char[] var1, int var2, int var3) {
         this.chars = var1;
         this.start = this.offset = var2;
         this.limit = var3;
      }

      public int length() {
         int var1 = this.offset - this.start;
         if (this.offset <= this.limit) {
            return var1;
         } else {
            throw new IndexOutOfBoundsException(Integer.toString(var1));
         }
      }

      public Appendable append(char var1) {
         if (this.offset < this.limit) {
            this.chars[this.offset] = var1;
         }

         ++this.offset;
         return this;
      }

      public Appendable append(CharSequence var1) {
         return this.append(var1, 0, var1.length());
      }

      public Appendable append(CharSequence var1, int var2, int var3) {
         int var4 = var3 - var2;
         if (var4 <= this.limit - this.offset) {
            while(var2 < var3) {
               this.chars[this.offset++] = var1.charAt(var2++);
            }
         } else {
            this.offset += var4;
         }

         return this;
      }
   }

   private static final class CmpEquivLevel {
      CharSequence cs;
      int s;

      private CmpEquivLevel() {
      }

      CmpEquivLevel(Object var1) {
         this();
      }
   }

   public static final class QuickCheckResult {
      private QuickCheckResult(int var1) {
      }

      QuickCheckResult(int var1, Object var2) {
         this(var1);
      }
   }

   private static final class FCDMode extends Normalizer.Mode {
      private FCDMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return (var1 & 32) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.FCD32ModeImpl.access$1100()) : Normalizer.ModeImpl.access$300(Normalizer.FCDModeImpl.access$1200());
      }

      FCDMode(Object var1) {
         this();
      }
   }

   private static final class NFKCMode extends Normalizer.Mode {
      private NFKCMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return (var1 & 32) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFKC32ModeImpl.access$900()) : Normalizer.ModeImpl.access$300(Normalizer.NFKCModeImpl.access$1000());
      }

      NFKCMode(Object var1) {
         this();
      }
   }

   private static final class NFCMode extends Normalizer.Mode {
      private NFCMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return (var1 & 32) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFC32ModeImpl.access$700()) : Normalizer.ModeImpl.access$300(Normalizer.NFCModeImpl.access$800());
      }

      NFCMode(Object var1) {
         this();
      }
   }

   private static final class NFKDMode extends Normalizer.Mode {
      private NFKDMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return (var1 & 32) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFKD32ModeImpl.access$500()) : Normalizer.ModeImpl.access$300(Normalizer.NFKDModeImpl.access$600());
      }

      NFKDMode(Object var1) {
         this();
      }
   }

   private static final class NFDMode extends Normalizer.Mode {
      private NFDMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return (var1 & 32) != 0 ? Normalizer.ModeImpl.access$300(Normalizer.NFD32ModeImpl.access$200()) : Normalizer.ModeImpl.access$300(Normalizer.NFDModeImpl.access$400());
      }

      NFDMode(Object var1) {
         this();
      }
   }

   private static final class NONEMode extends Normalizer.Mode {
      private NONEMode() {
      }

      protected Normalizer2 getNormalizer2(int var1) {
         return Norm2AllModes.NOOP_NORMALIZER2;
      }

      NONEMode(Object var1) {
         this();
      }
   }

   public abstract static class Mode {
      /** @deprecated */
      protected abstract Normalizer2 getNormalizer2(int var1);
   }

   private static final class FCD32ModeImpl {
      private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getFCDNormalizer2(), Normalizer.Unicode32.access$100()));

      static Normalizer.ModeImpl access$1100() {
         return INSTANCE;
      }
   }

   private static final class NFKC32ModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$900() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().comp, Normalizer.Unicode32.access$100()));
      }
   }

   private static final class NFC32ModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$700() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFCInstance().comp, Normalizer.Unicode32.access$100()));
      }
   }

   private static final class NFKD32ModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$500() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFKCInstance().decomp, Normalizer.Unicode32.access$100()));
      }
   }

   private static final class NFD32ModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$200() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(new FilteredNormalizer2(Norm2AllModes.getNFCInstance().decomp, Normalizer.Unicode32.access$100()));
      }
   }

   private static final class Unicode32 {
      private static final UnicodeSet INSTANCE = (new UnicodeSet("[:age=3.2:]")).freeze();

      static UnicodeSet access$100() {
         return INSTANCE;
      }
   }

   private static final class FCDModeImpl {
      private static final Normalizer.ModeImpl INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getFCDNormalizer2());

      static Normalizer.ModeImpl access$1200() {
         return INSTANCE;
      }
   }

   private static final class NFKCModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$1000() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFKCInstance().comp);
      }
   }

   private static final class NFCModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$800() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFCInstance().comp);
      }
   }

   private static final class NFKDModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$600() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFKCInstance().decomp);
      }
   }

   private static final class NFDModeImpl {
      private static final Normalizer.ModeImpl INSTANCE;

      static Normalizer.ModeImpl access$400() {
         return INSTANCE;
      }

      static {
         INSTANCE = new Normalizer.ModeImpl(Norm2AllModes.getNFCInstance().decomp);
      }
   }

   private static final class ModeImpl {
      private final Normalizer2 normalizer2;

      private ModeImpl(Normalizer2 var1) {
         this.normalizer2 = var1;
      }

      ModeImpl(Normalizer2 var1, Object var2) {
         this(var1);
      }

      static Normalizer2 access$300(Normalizer.ModeImpl var0) {
         return var0.normalizer2;
      }
   }
}
