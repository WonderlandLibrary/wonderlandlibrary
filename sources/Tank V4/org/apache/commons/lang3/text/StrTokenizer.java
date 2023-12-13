package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StrTokenizer implements ListIterator, Cloneable {
   private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
   private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE;
   private char[] chars;
   private String[] tokens;
   private int tokenPos;
   private StrMatcher delimMatcher;
   private StrMatcher quoteMatcher;
   private StrMatcher ignoredMatcher;
   private StrMatcher trimmerMatcher;
   private boolean emptyAsNull;
   private boolean ignoreEmptyTokens;

   private static StrTokenizer getCSVClone() {
      return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
   }

   public static StrTokenizer getCSVInstance() {
      return getCSVClone();
   }

   public static StrTokenizer getCSVInstance(String var0) {
      StrTokenizer var1 = getCSVClone();
      var1.reset(var0);
      return var1;
   }

   public static StrTokenizer getCSVInstance(char[] var0) {
      StrTokenizer var1 = getCSVClone();
      var1.reset(var0);
      return var1;
   }

   private static StrTokenizer getTSVClone() {
      return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
   }

   public static StrTokenizer getTSVInstance() {
      return getTSVClone();
   }

   public static StrTokenizer getTSVInstance(String var0) {
      StrTokenizer var1 = getTSVClone();
      var1.reset(var0);
      return var1;
   }

   public static StrTokenizer getTSVInstance(char[] var0) {
      StrTokenizer var1 = getTSVClone();
      var1.reset(var0);
      return var1;
   }

   public StrTokenizer() {
      this.delimMatcher = StrMatcher.splitMatcher();
      this.quoteMatcher = StrMatcher.noneMatcher();
      this.ignoredMatcher = StrMatcher.noneMatcher();
      this.trimmerMatcher = StrMatcher.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      this.chars = null;
   }

   public StrTokenizer(String var1) {
      this.delimMatcher = StrMatcher.splitMatcher();
      this.quoteMatcher = StrMatcher.noneMatcher();
      this.ignoredMatcher = StrMatcher.noneMatcher();
      this.trimmerMatcher = StrMatcher.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      if (var1 != null) {
         this.chars = var1.toCharArray();
      } else {
         this.chars = null;
      }

   }

   public StrTokenizer(String var1, char var2) {
      this(var1);
      this.setDelimiterChar(var2);
   }

   public StrTokenizer(String var1, String var2) {
      this(var1);
      this.setDelimiterString(var2);
   }

   public StrTokenizer(String var1, StrMatcher var2) {
      this(var1);
      this.setDelimiterMatcher(var2);
   }

   public StrTokenizer(String var1, char var2, char var3) {
      this(var1, var2);
      this.setQuoteChar(var3);
   }

   public StrTokenizer(String var1, StrMatcher var2, StrMatcher var3) {
      this(var1, var2);
      this.setQuoteMatcher(var3);
   }

   public StrTokenizer(char[] var1) {
      this.delimMatcher = StrMatcher.splitMatcher();
      this.quoteMatcher = StrMatcher.noneMatcher();
      this.ignoredMatcher = StrMatcher.noneMatcher();
      this.trimmerMatcher = StrMatcher.noneMatcher();
      this.emptyAsNull = false;
      this.ignoreEmptyTokens = true;
      this.chars = ArrayUtils.clone(var1);
   }

   public StrTokenizer(char[] var1, char var2) {
      this(var1);
      this.setDelimiterChar(var2);
   }

   public StrTokenizer(char[] var1, String var2) {
      this(var1);
      this.setDelimiterString(var2);
   }

   public StrTokenizer(char[] var1, StrMatcher var2) {
      this(var1);
      this.setDelimiterMatcher(var2);
   }

   public StrTokenizer(char[] var1, char var2, char var3) {
      this(var1, var2);
      this.setQuoteChar(var3);
   }

   public StrTokenizer(char[] var1, StrMatcher var2, StrMatcher var3) {
      this(var1, var2);
      this.setQuoteMatcher(var3);
   }

   public int size() {
      this.checkTokenized();
      return this.tokens.length;
   }

   public String nextToken() {
      // $FF: Couldn't be decompiled
   }

   public String previousToken() {
      return this > 0 ? this.tokens[--this.tokenPos] : null;
   }

   public String[] getTokenArray() {
      this.checkTokenized();
      return (String[])this.tokens.clone();
   }

   public List getTokenList() {
      this.checkTokenized();
      ArrayList var1 = new ArrayList(this.tokens.length);
      String[] var2 = this.tokens;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.add(var5);
      }

      return var1;
   }

   public StrTokenizer reset() {
      this.tokenPos = 0;
      this.tokens = null;
      return this;
   }

   public StrTokenizer reset(String var1) {
      this.reset();
      if (var1 != null) {
         this.chars = var1.toCharArray();
      } else {
         this.chars = null;
      }

      return this;
   }

   public StrTokenizer reset(char[] var1) {
      this.reset();
      this.chars = ArrayUtils.clone(var1);
      return this;
   }

   public String next() {
      // $FF: Couldn't be decompiled
   }

   public int nextIndex() {
      return this.tokenPos;
   }

   public String previous() {
      if (this > 0) {
         return this.tokens[--this.tokenPos];
      } else {
         throw new NoSuchElementException();
      }
   }

   public int previousIndex() {
      return this.tokenPos - 1;
   }

   public void remove() {
      throw new UnsupportedOperationException("remove() is unsupported");
   }

   public void set(String var1) {
      throw new UnsupportedOperationException("set() is unsupported");
   }

   public void add(String var1) {
      throw new UnsupportedOperationException("add() is unsupported");
   }

   private void checkTokenized() {
      if (this.tokens == null) {
         List var1;
         if (this.chars == null) {
            var1 = this.tokenize((char[])null, 0, 0);
            this.tokens = (String[])var1.toArray(new String[var1.size()]);
         } else {
            var1 = this.tokenize(this.chars, 0, this.chars.length);
            this.tokens = (String[])var1.toArray(new String[var1.size()]);
         }
      }

   }

   protected List tokenize(char[] var1, int var2, int var3) {
      if (var1 != null && var3 != 0) {
         StrBuilder var4 = new StrBuilder();
         ArrayList var5 = new ArrayList();
         int var6 = var2;

         while(var6 >= 0 && var6 < var3) {
            var6 = this.readNextToken(var1, var6, var3, var4, var5);
            if (var6 >= var3) {
               this.addToken(var5, "");
            }
         }

         return var5;
      } else {
         return Collections.emptyList();
      }
   }

   private void addToken(List var1, String var2) {
      if (StringUtils.isEmpty(var2)) {
         if (this.isIgnoreEmptyTokens()) {
            return;
         }

         if (this.isEmptyTokenAsNull()) {
            var2 = null;
         }
      }

      var1.add(var2);
   }

   private int readNextToken(char[] var1, int var2, int var3, StrBuilder var4, List var5) {
      while(true) {
         int var6;
         if (var2 < var3) {
            var6 = Math.max(this.getIgnoredMatcher().isMatch(var1, var2, var2, var3), this.getTrimmerMatcher().isMatch(var1, var2, var2, var3));
            if (var6 != 0 && this.getDelimiterMatcher().isMatch(var1, var2, var2, var3) <= 0 && this.getQuoteMatcher().isMatch(var1, var2, var2, var3) <= 0) {
               var2 += var6;
               continue;
            }
         }

         if (var2 >= var3) {
            this.addToken(var5, "");
            return -1;
         }

         var6 = this.getDelimiterMatcher().isMatch(var1, var2, var2, var3);
         if (var6 > 0) {
            this.addToken(var5, "");
            return var2 + var6;
         }

         int var7 = this.getQuoteMatcher().isMatch(var1, var2, var2, var3);
         if (var7 > 0) {
            return this.readWithQuotes(var1, var2 + var7, var3, var4, var5, var2, var7);
         }

         return this.readWithQuotes(var1, var2, var3, var4, var5, 0, 0);
      }
   }

   private int readWithQuotes(char[] var1, int var2, int var3, StrBuilder var4, List var5, int var6, int var7) {
      var4.clear();
      int var8 = var2;
      boolean var9 = var7 > 0;
      int var10 = 0;

      while(true) {
         while(var8 < var3) {
            if (var9) {
               if (var6 < var7) {
                  int var10006 = var8 + var7;
                  if (var6 < var7) {
                     var4.append(var1, var8, var7);
                     var8 += var7 * 2;
                     var10 = var4.size();
                  } else {
                     var9 = false;
                     var8 += var7;
                  }
               } else {
                  var4.append(var1[var8++]);
                  var10 = var4.size();
               }
            } else {
               int var11 = this.getDelimiterMatcher().isMatch(var1, var8, var2, var3);
               if (var11 > 0) {
                  this.addToken(var5, var4.substring(0, var10));
                  return var8 + var11;
               }

               if (var7 > 0 && var6 < var7) {
                  var9 = true;
                  var8 += var7;
               } else {
                  int var12 = this.getIgnoredMatcher().isMatch(var1, var8, var2, var3);
                  if (var12 > 0) {
                     var8 += var12;
                  } else {
                     int var13 = this.getTrimmerMatcher().isMatch(var1, var8, var2, var3);
                     if (var13 > 0) {
                        var4.append(var1, var8, var13);
                        var8 += var13;
                     } else {
                        var4.append(var1[var8++]);
                        var10 = var4.size();
                     }
                  }
               }
            }
         }

         this.addToken(var5, var4.substring(0, var10));
         return -1;
      }
   }

   public StrMatcher getDelimiterMatcher() {
      return this.delimMatcher;
   }

   public StrTokenizer setDelimiterMatcher(StrMatcher var1) {
      if (var1 == null) {
         this.delimMatcher = StrMatcher.noneMatcher();
      } else {
         this.delimMatcher = var1;
      }

      return this;
   }

   public StrTokenizer setDelimiterChar(char var1) {
      return this.setDelimiterMatcher(StrMatcher.charMatcher(var1));
   }

   public StrTokenizer setDelimiterString(String var1) {
      return this.setDelimiterMatcher(StrMatcher.stringMatcher(var1));
   }

   public StrMatcher getQuoteMatcher() {
      return this.quoteMatcher;
   }

   public StrTokenizer setQuoteMatcher(StrMatcher var1) {
      if (var1 != null) {
         this.quoteMatcher = var1;
      }

      return this;
   }

   public StrTokenizer setQuoteChar(char var1) {
      return this.setQuoteMatcher(StrMatcher.charMatcher(var1));
   }

   public StrMatcher getIgnoredMatcher() {
      return this.ignoredMatcher;
   }

   public StrTokenizer setIgnoredMatcher(StrMatcher var1) {
      if (var1 != null) {
         this.ignoredMatcher = var1;
      }

      return this;
   }

   public StrTokenizer setIgnoredChar(char var1) {
      return this.setIgnoredMatcher(StrMatcher.charMatcher(var1));
   }

   public StrMatcher getTrimmerMatcher() {
      return this.trimmerMatcher;
   }

   public StrTokenizer setTrimmerMatcher(StrMatcher var1) {
      if (var1 != null) {
         this.trimmerMatcher = var1;
      }

      return this;
   }

   public boolean isEmptyTokenAsNull() {
      return this.emptyAsNull;
   }

   public StrTokenizer setEmptyTokenAsNull(boolean var1) {
      this.emptyAsNull = var1;
      return this;
   }

   public boolean isIgnoreEmptyTokens() {
      return this.ignoreEmptyTokens;
   }

   public StrTokenizer setIgnoreEmptyTokens(boolean var1) {
      this.ignoreEmptyTokens = var1;
      return this;
   }

   public String getContent() {
      return this.chars == null ? null : new String(this.chars);
   }

   public Object clone() {
      try {
         return this.cloneReset();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   Object cloneReset() throws CloneNotSupportedException {
      StrTokenizer var1 = (StrTokenizer)super.clone();
      if (var1.chars != null) {
         var1.chars = (char[])var1.chars.clone();
      }

      var1.reset();
      return var1;
   }

   public String toString() {
      return this.tokens == null ? "StrTokenizer[not tokenized yet]" : "StrTokenizer" + this.getTokenList();
   }

   public void add(Object var1) {
      this.add((String)var1);
   }

   public void set(Object var1) {
      this.set((String)var1);
   }

   public Object previous() {
      return this.previous();
   }

   public Object next() {
      return this.next();
   }

   static {
      CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
      CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
      CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
      CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
      CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
      CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
      TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
      TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
      TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
      TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
      TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
      TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
      TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
   }
}
