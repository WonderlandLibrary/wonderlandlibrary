package com.ibm.icu.text;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.Trie2;
import com.ibm.icu.impl.Trie2Writable;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.util.ULocale;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpoofChecker {
   public static final UnicodeSet INCLUSION = new UnicodeSet("[\\-.\\u00B7\\u05F3\\u05F4\\u0F0B\\u200C\\u200D\\u2019]");
   public static final UnicodeSet RECOMMENDED = new UnicodeSet("[[0-z\\u00C0-\\u017E\\u01A0\\u01A1\\u01AF\\u01B0\\u01CD-\\u01DC\\u01DE-\\u01E3\\u01E6-\\u01F5\\u01F8-\\u021B\\u021E\\u021F\\u0226-\\u0233\\u02BB\\u02BC\\u02EC\\u0300-\\u0304\\u0306-\\u030C\\u030F-\\u0311\\u0313\\u0314\\u031B\\u0323-\\u0328\\u032D\\u032E\\u0330\\u0331\\u0335\\u0338\\u0339\\u0342-\\u0345\\u037B-\\u03CE\\u03FC-\\u045F\\u048A-\\u0525\\u0531-\\u0586\\u05D0-\\u05F2\\u0621-\\u063F\\u0641-\\u0655\\u0660-\\u0669\\u0670-\\u068D\\u068F-\\u06D5\\u06E5\\u06E6\\u06EE-\\u06FF\\u0750-\\u07B1\\u0901-\\u0939\\u093C-\\u094D\\u0950\\u0960-\\u0972\\u0979-\\u0A4D\\u0A5C-\\u0A74\\u0A81-\\u0B43\\u0B47-\\u0B61\\u0B66-\\u0C56\\u0C60\\u0C61\\u0C66-\\u0CD6\\u0CE0-\\u0CEF\\u0D02-\\u0D28\\u0D2A-\\u0D39\\u0D3D-\\u0D43\\u0D46-\\u0D4D\\u0D57-\\u0D61\\u0D66-\\u0D8E\\u0D91-\\u0DA5\\u0DA7-\\u0DDE\\u0DF2\\u0E01-\\u0ED9\\u0F00\\u0F20-\\u0F8B\\u0F90-\\u109D\\u10D0-\\u10F0\\u10F7-\\u10FA\\u1200-\\u135A\\u135F\\u1380-\\u138F\\u1401-\\u167F\\u1780-\\u17A2\\u17A5-\\u17A7\\u17A9-\\u17B3\\u17B6-\\u17CA\\u17D2\\u17D7-\\u17DC\\u17E0-\\u17E9\\u1810-\\u18A8\\u18AA-\\u18F5\\u1E00-\\u1E99\\u1F00-\\u1FFC\\u2D30-\\u2D65\\u2D80-\\u2DDE\\u3005-\\u3007\\u3041-\\u31B7\\u3400-\\u9FCB\\uA000-\\uA48C\\uA67F\\uA717-\\uA71F\\uA788\\uAA60-\\uAA7B\\uAC00-\\uD7A3\\uFA0E-\\uFA29\\U00020000-\\U0002B734]-[[:Cn:][:nfkcqc=n:][:XIDC=n:]]]");
   public static final int SINGLE_SCRIPT_CONFUSABLE = 1;
   public static final int MIXED_SCRIPT_CONFUSABLE = 2;
   public static final int WHOLE_SCRIPT_CONFUSABLE = 4;
   public static final int ANY_CASE = 8;
   public static final int RESTRICTION_LEVEL = 16;
   /** @deprecated */
   public static final int SINGLE_SCRIPT = 16;
   public static final int INVISIBLE = 32;
   public static final int CHAR_LIMIT = 64;
   public static final int MIXED_NUMBERS = 128;
   public static final int ALL_CHECKS = -1;
   static final int MAGIC = 944111087;
   private IdentifierInfo fCachedIdentifierInfo;
   private int fMagic;
   private int fChecks;
   private SpoofChecker.SpoofData fSpoofData;
   private Set fAllowedLocales;
   private UnicodeSet fAllowedCharsSet;
   private SpoofChecker.RestrictionLevel fRestrictionLevel;
   private static Normalizer2 nfdNormalizer = Normalizer2.getNFDInstance();
   static final int SL_TABLE_FLAG = 16777216;
   static final int SA_TABLE_FLAG = 33554432;
   static final int ML_TABLE_FLAG = 67108864;
   static final int MA_TABLE_FLAG = 134217728;
   static final int KEY_MULTIPLE_VALUES = 268435456;
   static final int KEY_LENGTH_SHIFT = 29;
   static final boolean $assertionsDisabled = !SpoofChecker.class.desiredAssertionStatus();

   private SpoofChecker() {
      this.fCachedIdentifierInfo = null;
   }

   public SpoofChecker.RestrictionLevel getRestrictionLevel() {
      return this.fRestrictionLevel;
   }

   public int getChecks() {
      return this.fChecks;
   }

   public Set getAllowedLocales() {
      return this.fAllowedLocales;
   }

   public UnicodeSet getAllowedChars() {
      return this.fAllowedCharsSet;
   }

   public boolean failsChecks(String var1, SpoofChecker.CheckResult var2) {
      int var3 = var1.length();
      int var4 = 0;
      if (var2 != null) {
         var2.position = 0;
         var2.numerics = null;
         var2.restrictionLevel = null;
      }

      IdentifierInfo var5 = null;
      if (0 != (this.fChecks & 144)) {
         var5 = this.getIdentifierInfo().setIdentifier(var1).setIdentifierProfile(this.fAllowedCharsSet);
      }

      if (0 != (this.fChecks & 16)) {
         SpoofChecker.RestrictionLevel var6 = var5.getRestrictionLevel();
         if (var6.compareTo(this.fRestrictionLevel) > 0) {
            var4 |= 16;
         }

         if (var2 != null) {
            var2.restrictionLevel = var6;
         }
      }

      if (0 != (this.fChecks & 128)) {
         UnicodeSet var12 = var5.getNumerics();
         if (var12.size() > 1) {
            var4 |= 128;
         }

         if (var2 != null) {
            var2.numerics = var12;
         }
      }

      int var7;
      if (0 != (this.fChecks & 64)) {
         int var13 = 0;

         while(var13 < var3) {
            var7 = Character.codePointAt(var1, var13);
            var13 = Character.offsetByCodePoints(var1, var13, 1);
            if (!this.fAllowedCharsSet.contains(var7)) {
               var4 |= 64;
               break;
            }
         }
      }

      if (0 != (this.fChecks & 38)) {
         String var14 = nfdNormalizer.normalize(var1);
         int var9;
         if (0 != (this.fChecks & 32)) {
            var9 = 0;
            boolean var10 = false;
            UnicodeSet var11 = new UnicodeSet();
            var7 = 0;

            while(var7 < var3) {
               int var8 = Character.codePointAt(var14, var7);
               var7 = Character.offsetByCodePoints(var14, var7, 1);
               if (Character.getType(var8) != 6) {
                  var9 = 0;
                  if (var10) {
                     var11.clear();
                     var10 = false;
                  }
               } else if (var9 == 0) {
                  var9 = var8;
               } else {
                  if (!var10) {
                     var11.add(var9);
                     var10 = true;
                  }

                  if (var11.contains(var8)) {
                     var4 |= 32;
                     break;
                  }

                  var11.add(var8);
               }
            }
         }

         if (0 != (this.fChecks & 6)) {
            if (var5 == null) {
               var5 = this.getIdentifierInfo();
               var5.setIdentifier(var1);
            }

            var7 = var5.getScriptCount();
            SpoofChecker.ScriptSet var15 = new SpoofChecker.ScriptSet();
            this.wholeScriptCheck(var14, var15);
            var9 = var15.countMembers();
            if (0 != (this.fChecks & 4) && var9 >= 2 && var7 == 1) {
               var4 |= 4;
            }

            if (0 != (this.fChecks & 2) && var9 >= 1 && var7 > 1) {
               var4 |= 2;
            }
         }
      }

      if (var2 != null) {
         var2.checks = var4;
      }

      this.releaseIdentifierInfo(var5);
      return 0 != var4;
   }

   public boolean failsChecks(String var1) {
      return this.failsChecks(var1, (SpoofChecker.CheckResult)null);
   }

   public int areConfusable(String var1, String var2) {
      if ((this.fChecks & 7) == 0) {
         throw new IllegalArgumentException("No confusable checks are enabled.");
      } else {
         int var3 = this.fChecks & 8;
         int var4 = 0;
         IdentifierInfo var5 = this.getIdentifierInfo();
         var5.setIdentifier(var1);
         int var6 = var5.getScriptCount();
         var5.setIdentifier(var2);
         int var7 = var5.getScriptCount();
         this.releaseIdentifierInfo(var5);
         String var9;
         if (0 != (this.fChecks & 1) && var6 <= 1 && var7 <= 1) {
            var3 |= 1;
            String var8 = this.getSkeleton(var3, var1);
            var9 = this.getSkeleton(var3, var2);
            if (var8.equals(var9)) {
               var4 |= 1;
            }
         }

         if (0 != (var4 & 1)) {
            return var4;
         } else {
            boolean var11 = var6 <= 1 && var7 <= 1 && 0 != (this.fChecks & 4);
            if (0 != (this.fChecks & 2) || var11) {
               var3 &= -2;
               var9 = this.getSkeleton(var3, var1);
               String var10 = this.getSkeleton(var3, var2);
               if (var9.equals(var10)) {
                  var4 |= 2;
                  if (var11) {
                     var4 |= 4;
                  }
               }
            }

            return var4;
         }
      }
   }

   public String getSkeleton(int var1, String var2) {
      boolean var3 = false;
      int var9;
      switch(var1) {
      case 0:
         var9 = 67108864;
         break;
      case 1:
         var9 = 16777216;
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      default:
         throw new IllegalArgumentException("SpoofChecker.getSkeleton(), bad type value.");
      case 8:
         var9 = 134217728;
         break;
      case 9:
         var9 = 33554432;
      }

      String var4 = nfdNormalizer.normalize(var2);
      int var5 = var4.length();
      StringBuilder var6 = new StringBuilder();
      int var7 = 0;

      while(var7 < var5) {
         int var8 = Character.codePointAt(var4, var7);
         var7 += Character.charCount(var8);
         this.confusableLookup(var8, var9, var6);
      }

      String var10 = var6.toString();
      var10 = nfdNormalizer.normalize(var10);
      return var10;
   }

   private void confusableLookup(int var1, int var2, StringBuilder var3) {
      int var4 = 0;
      boolean var5 = false;
      int var6 = this.fSpoofData.fRawData.fCFUKeysSize;
      boolean var8 = false;

      int var16;
      do {
         int var9 = (var6 - var4) / 2;
         var16 = var4 + var9;
         int var7 = this.fSpoofData.fCFUKeys[var16] & 2097151;
         if (var1 == var7) {
            var8 = true;
            break;
         }

         if (var1 < var7) {
            var6 = var16;
         } else {
            var4 = var16 + 1;
         }
      } while(var4 < var6);

      if (!var8) {
         var3.appendCodePoint(var1);
      } else {
         boolean var17 = false;
         int var10 = this.fSpoofData.fCFUKeys[var16] & -16777216;
         int var11;
         if ((var10 & var2) == 0) {
            if (0 != (var10 & 268435456)) {
               for(var11 = var16 - 1; (this.fSpoofData.fCFUKeys[var11] & 16777215) == var1; --var11) {
                  var10 = this.fSpoofData.fCFUKeys[var11] & -16777216;
                  if (0 != (var10 & var2)) {
                     var16 = var11;
                     var17 = true;
                     break;
                  }
               }

               if (!var17) {
                  for(var11 = var16 + 1; (this.fSpoofData.fCFUKeys[var11] & 16777215) == var1; ++var11) {
                     var10 = this.fSpoofData.fCFUKeys[var11] & -16777216;
                     if (0 != (var10 & var2)) {
                        var16 = var11;
                        var17 = true;
                        break;
                     }
                  }
               }
            }

            if (!var17) {
               var3.appendCodePoint(var1);
               return;
            }
         }

         var11 = getKeyLength(var10) + 1;
         short var13 = this.fSpoofData.fCFUValues[var16];
         if (var11 == 1) {
            var3.append((char)var13);
         } else {
            if (var11 == 4) {
               int var15 = this.fSpoofData.fRawData.fCFUStringLengthsSize;

               int var14;
               for(var14 = 0; var14 < var15; ++var14) {
                  if (this.fSpoofData.fCFUStringLengths[var14].fLastString >= var13) {
                     var11 = this.fSpoofData.fCFUStringLengths[var14].fStrLength;
                     break;
                  }
               }

               if (!$assertionsDisabled && var14 >= var15) {
                  throw new AssertionError();
               }
            }

            if (!$assertionsDisabled && var13 + var11 > this.fSpoofData.fRawData.fCFUStringTableLen) {
               throw new AssertionError();
            } else {
               var3.append(this.fSpoofData.fCFUStrings, var13, var11);
            }
         }
      }
   }

   void wholeScriptCheck(CharSequence var1, SpoofChecker.ScriptSet var2) {
      int var3 = 0;
      Trie2 var5 = 0 != (this.fChecks & 8) ? this.fSpoofData.fAnyCaseTrie : this.fSpoofData.fLowerCaseTrie;
      var2.setAll();

      while(var3 < var1.length()) {
         int var4 = Character.codePointAt(var1, var3);
         var3 = Character.offsetByCodePoints(var1, var3, 1);
         int var6 = var5.get(var4);
         if (var6 == 0) {
            int var7 = UScript.getScript(var4);
            if (!$assertionsDisabled && var7 <= 1) {
               throw new AssertionError();
            }

            var2.intersect(var7);
         } else if (var6 != 1) {
            var2.intersect(this.fSpoofData.fScriptSets[var6]);
         }
      }

   }

   private IdentifierInfo getIdentifierInfo() {
      IdentifierInfo var1 = null;
      synchronized(this){}
      var1 = this.fCachedIdentifierInfo;
      this.fCachedIdentifierInfo = null;
      if (var1 == null) {
         var1 = new IdentifierInfo();
      }

      return var1;
   }

   private void releaseIdentifierInfo(IdentifierInfo var1) {
      if (var1 != null) {
         synchronized(this){}
         if (this.fCachedIdentifierInfo == null) {
            this.fCachedIdentifierInfo = var1;
         }
      }

   }

   static final int getKeyLength(int var0) {
      return var0 >> 29 & 3;
   }

   static int access$000(SpoofChecker var0) {
      return var0.fMagic;
   }

   static int access$100(SpoofChecker var0) {
      return var0.fChecks;
   }

   static UnicodeSet access$200(SpoofChecker var0) {
      return var0.fAllowedCharsSet;
   }

   static Set access$300(SpoofChecker var0) {
      return var0.fAllowedLocales;
   }

   static SpoofChecker.RestrictionLevel access$400(SpoofChecker var0) {
      return var0.fRestrictionLevel;
   }

   SpoofChecker(Object var1) {
      this();
   }

   static int access$002(SpoofChecker var0, int var1) {
      return var0.fMagic = var1;
   }

   static int access$102(SpoofChecker var0, int var1) {
      return var0.fChecks = var1;
   }

   static SpoofChecker.SpoofData access$602(SpoofChecker var0, SpoofChecker.SpoofData var1) {
      return var0.fSpoofData = var1;
   }

   static UnicodeSet access$202(SpoofChecker var0, UnicodeSet var1) {
      return var0.fAllowedCharsSet = var1;
   }

   static Set access$302(SpoofChecker var0, Set var1) {
      return var0.fAllowedLocales = var1;
   }

   static SpoofChecker.RestrictionLevel access$402(SpoofChecker var0, SpoofChecker.RestrictionLevel var1) {
      return var0.fRestrictionLevel = var1;
   }

   private static class ScriptSet {
      private int[] bits = new int[6];
      static final boolean $assertionsDisabled = !SpoofChecker.class.desiredAssertionStatus();

      public ScriptSet() {
      }

      public ScriptSet(DataInputStream var1) throws IOException {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            this.bits[var2] = var1.readInt();
         }

      }

      public void output(DataOutputStream var1) throws IOException {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            var1.writeInt(this.bits[var2]);
         }

      }

      public boolean equals(SpoofChecker.ScriptSet var1) {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            if (this.bits[var2] != var1.bits[var2]) {
               return false;
            }
         }

         return true;
      }

      public void Union(int var1) {
         int var2 = var1 / 32;
         int var3 = 1 << (var1 & 31);
         if (!$assertionsDisabled && var2 >= this.bits.length * 4 * 4) {
            throw new AssertionError();
         } else {
            int[] var10000 = this.bits;
            var10000[var2] |= var3;
         }
      }

      public void Union(SpoofChecker.ScriptSet var1) {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            int[] var10000 = this.bits;
            var10000[var2] |= var1.bits[var2];
         }

      }

      public void intersect(SpoofChecker.ScriptSet var1) {
         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            int[] var10000 = this.bits;
            var10000[var2] &= var1.bits[var2];
         }

      }

      public void intersect(int var1) {
         int var2 = var1 / 32;
         int var3 = 1 << (var1 & 31);
         if (!$assertionsDisabled && var2 >= this.bits.length * 4 * 4) {
            throw new AssertionError();
         } else {
            int var4;
            for(var4 = 0; var4 < var2; ++var4) {
               this.bits[var4] = 0;
            }

            int[] var10000 = this.bits;
            var10000[var2] &= var3;

            for(var4 = var2 + 1; var4 < this.bits.length; ++var4) {
               this.bits[var4] = 0;
            }

         }
      }

      public void setAll() {
         for(int var1 = 0; var1 < this.bits.length; ++var1) {
            this.bits[var1] = -1;
         }

      }

      public void resetAll() {
         for(int var1 = 0; var1 < this.bits.length; ++var1) {
            this.bits[var1] = 0;
         }

      }

      public int countMembers() {
         int var1 = 0;

         for(int var2 = 0; var2 < this.bits.length; ++var2) {
            for(int var3 = this.bits[var2]; var3 != 0; var3 &= var3 - 1) {
               ++var1;
            }
         }

         return var1;
      }
   }

   private static class SpoofData {
      SpoofChecker.SpoofDataHeader fRawData;
      int[] fCFUKeys;
      short[] fCFUValues;
      SpoofChecker.SpoofData.SpoofStringLengthsElement[] fCFUStringLengths;
      char[] fCFUStrings;
      Trie2 fAnyCaseTrie;
      Trie2 fLowerCaseTrie;
      SpoofChecker.ScriptSet[] fScriptSets;
      static final boolean $assertionsDisabled = !SpoofChecker.class.desiredAssertionStatus();

      public static SpoofChecker.SpoofData getDefault() throws IOException {
         InputStream var0 = ICUData.getRequiredStream("data/icudt51b/confusables.cfu");
         SpoofChecker.SpoofData var1 = new SpoofChecker.SpoofData(var0);
         return var1;
      }

      public SpoofData() {
         this.fRawData = new SpoofChecker.SpoofDataHeader();
         this.fRawData.fMagic = 944111087;
         this.fRawData.fFormatVersion[0] = 1;
         this.fRawData.fFormatVersion[1] = 0;
         this.fRawData.fFormatVersion[2] = 0;
         this.fRawData.fFormatVersion[3] = 0;
      }

      public SpoofData(InputStream var1) throws IOException {
         DataInputStream var2 = new DataInputStream(new BufferedInputStream(var1));
         var2.skip(128L);
         if (!$assertionsDisabled && !var2.markSupported()) {
            throw new AssertionError();
         } else {
            var2.mark(Integer.MAX_VALUE);
            this.fRawData = new SpoofChecker.SpoofDataHeader(var2);
            this.initPtrs(var2);
         }
      }

      static boolean validateDataVersion(SpoofChecker.SpoofDataHeader var0) {
         return var0 != null && var0.fMagic == 944111087 && var0.fFormatVersion[0] <= 1 && var0.fFormatVersion[1] <= 0;
      }

      void initPtrs(DataInputStream var1) throws IOException {
         this.fCFUKeys = null;
         this.fCFUValues = null;
         this.fCFUStringLengths = null;
         this.fCFUStrings = null;
         var1.reset();
         var1.skip((long)this.fRawData.fCFUKeys);
         int var2;
         if (this.fRawData.fCFUKeys != 0) {
            this.fCFUKeys = new int[this.fRawData.fCFUKeysSize];

            for(var2 = 0; var2 < this.fRawData.fCFUKeysSize; ++var2) {
               this.fCFUKeys[var2] = var1.readInt();
            }
         }

         var1.reset();
         var1.skip((long)this.fRawData.fCFUStringIndex);
         if (this.fRawData.fCFUStringIndex != 0) {
            this.fCFUValues = new short[this.fRawData.fCFUStringIndexSize];

            for(var2 = 0; var2 < this.fRawData.fCFUStringIndexSize; ++var2) {
               this.fCFUValues[var2] = var1.readShort();
            }
         }

         var1.reset();
         var1.skip((long)this.fRawData.fCFUStringTable);
         if (this.fRawData.fCFUStringTable != 0) {
            this.fCFUStrings = new char[this.fRawData.fCFUStringTableLen];

            for(var2 = 0; var2 < this.fRawData.fCFUStringTableLen; ++var2) {
               this.fCFUStrings[var2] = var1.readChar();
            }
         }

         var1.reset();
         var1.skip((long)this.fRawData.fCFUStringLengths);
         if (this.fRawData.fCFUStringLengths != 0) {
            this.fCFUStringLengths = new SpoofChecker.SpoofData.SpoofStringLengthsElement[this.fRawData.fCFUStringLengthsSize];

            for(var2 = 0; var2 < this.fRawData.fCFUStringLengthsSize; ++var2) {
               this.fCFUStringLengths[var2] = new SpoofChecker.SpoofData.SpoofStringLengthsElement();
               this.fCFUStringLengths[var2].fLastString = var1.readShort();
               this.fCFUStringLengths[var2].fStrLength = var1.readShort();
            }
         }

         var1.reset();
         var1.skip((long)this.fRawData.fAnyCaseTrie);
         if (this.fAnyCaseTrie == null && this.fRawData.fAnyCaseTrie != 0) {
            this.fAnyCaseTrie = Trie2.createFromSerialized(var1);
         }

         var1.reset();
         var1.skip((long)this.fRawData.fLowerCaseTrie);
         if (this.fLowerCaseTrie == null && this.fRawData.fLowerCaseTrie != 0) {
            this.fLowerCaseTrie = Trie2.createFromSerialized(var1);
         }

         var1.reset();
         var1.skip((long)this.fRawData.fScriptSets);
         if (this.fRawData.fScriptSets != 0) {
            this.fScriptSets = new SpoofChecker.ScriptSet[this.fRawData.fScriptSetsLength];

            for(var2 = 0; var2 < this.fRawData.fScriptSetsLength; ++var2) {
               this.fScriptSets[var2] = new SpoofChecker.ScriptSet(var1);
            }
         }

      }

      private static class SpoofStringLengthsElement {
         short fLastString;
         short fStrLength;

         private SpoofStringLengthsElement() {
         }

         SpoofStringLengthsElement(Object var1) {
            this();
         }
      }
   }

   private static class SpoofDataHeader {
      int fMagic;
      byte[] fFormatVersion = new byte[4];
      int fLength;
      int fCFUKeys;
      int fCFUKeysSize;
      int fCFUStringIndex;
      int fCFUStringIndexSize;
      int fCFUStringTable;
      int fCFUStringTableLen;
      int fCFUStringLengths;
      int fCFUStringLengthsSize;
      int fAnyCaseTrie;
      int fAnyCaseTrieLength;
      int fLowerCaseTrie;
      int fLowerCaseTrieLength;
      int fScriptSets;
      int fScriptSetsLength;
      int[] unused = new int[15];

      public SpoofDataHeader() {
      }

      public SpoofDataHeader(DataInputStream var1) throws IOException {
         this.fMagic = var1.readInt();

         int var2;
         for(var2 = 0; var2 < this.fFormatVersion.length; ++var2) {
            this.fFormatVersion[var2] = var1.readByte();
         }

         this.fLength = var1.readInt();
         this.fCFUKeys = var1.readInt();
         this.fCFUKeysSize = var1.readInt();
         this.fCFUStringIndex = var1.readInt();
         this.fCFUStringIndexSize = var1.readInt();
         this.fCFUStringTable = var1.readInt();
         this.fCFUStringTableLen = var1.readInt();
         this.fCFUStringLengths = var1.readInt();
         this.fCFUStringLengthsSize = var1.readInt();
         this.fAnyCaseTrie = var1.readInt();
         this.fAnyCaseTrieLength = var1.readInt();
         this.fLowerCaseTrie = var1.readInt();
         this.fLowerCaseTrieLength = var1.readInt();
         this.fScriptSets = var1.readInt();
         this.fScriptSetsLength = var1.readInt();

         for(var2 = 0; var2 < this.unused.length; ++var2) {
            this.unused[var2] = var1.readInt();
         }

      }

      public void output(DataOutputStream var1) throws IOException {
         var1.writeInt(this.fMagic);

         int var2;
         for(var2 = 0; var2 < this.fFormatVersion.length; ++var2) {
            var1.writeByte(this.fFormatVersion[var2]);
         }

         var1.writeInt(this.fLength);
         var1.writeInt(this.fCFUKeys);
         var1.writeInt(this.fCFUKeysSize);
         var1.writeInt(this.fCFUStringIndex);
         var1.writeInt(this.fCFUStringIndexSize);
         var1.writeInt(this.fCFUStringTable);
         var1.writeInt(this.fCFUStringTableLen);
         var1.writeInt(this.fCFUStringLengths);
         var1.writeInt(this.fCFUStringLengthsSize);
         var1.writeInt(this.fAnyCaseTrie);
         var1.writeInt(this.fAnyCaseTrieLength);
         var1.writeInt(this.fLowerCaseTrie);
         var1.writeInt(this.fLowerCaseTrieLength);
         var1.writeInt(this.fScriptSets);
         var1.writeInt(this.fScriptSetsLength);

         for(var2 = 0; var2 < this.unused.length; ++var2) {
            var1.writeInt(this.unused[var2]);
         }

      }
   }

   public static class CheckResult {
      public int checks = 0;
      /** @deprecated */
      public int position = 0;
      public UnicodeSet numerics;
      public SpoofChecker.RestrictionLevel restrictionLevel;
   }

   public static class Builder {
      int fMagic;
      int fChecks;
      SpoofChecker.SpoofData fSpoofData;
      final UnicodeSet fAllowedCharsSet = new UnicodeSet(0, 1114111);
      final Set fAllowedLocales = new LinkedHashSet();
      private SpoofChecker.RestrictionLevel fRestrictionLevel;

      public Builder() {
         this.fMagic = 944111087;
         this.fChecks = -1;
         this.fSpoofData = null;
         this.fRestrictionLevel = SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE;
      }

      public Builder(SpoofChecker var1) {
         this.fMagic = SpoofChecker.access$000(var1);
         this.fChecks = SpoofChecker.access$100(var1);
         this.fSpoofData = null;
         this.fAllowedCharsSet.set(SpoofChecker.access$200(var1));
         this.fAllowedLocales.addAll(SpoofChecker.access$300(var1));
         this.fRestrictionLevel = SpoofChecker.access$400(var1);
      }

      public SpoofChecker build() {
         if (this.fSpoofData == null) {
            try {
               this.fSpoofData = SpoofChecker.SpoofData.getDefault();
            } catch (IOException var2) {
               return null;
            }
         }

         if (!SpoofChecker.SpoofData.validateDataVersion(this.fSpoofData.fRawData)) {
            return null;
         } else {
            SpoofChecker var1 = new SpoofChecker();
            SpoofChecker.access$002(var1, this.fMagic);
            SpoofChecker.access$102(var1, this.fChecks);
            SpoofChecker.access$602(var1, this.fSpoofData);
            SpoofChecker.access$202(var1, (UnicodeSet)((UnicodeSet)this.fAllowedCharsSet.clone()));
            SpoofChecker.access$200(var1).freeze();
            SpoofChecker.access$302(var1, this.fAllowedLocales);
            SpoofChecker.access$402(var1, this.fRestrictionLevel);
            return var1;
         }
      }

      public SpoofChecker.Builder setData(Reader var1, Reader var2) throws ParseException, IOException {
         this.fSpoofData = new SpoofChecker.SpoofData();
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var4 = new DataOutputStream(var3);
         SpoofChecker.Builder.ConfusabledataBuilder.buildConfusableData(this.fSpoofData, var1);
         SpoofChecker.Builder.WSConfusableDataBuilder.buildWSConfusableData(this.fSpoofData, var4, var2);
         return this;
      }

      public SpoofChecker.Builder setChecks(int var1) {
         if (0 != (var1 & 0)) {
            throw new IllegalArgumentException("Bad Spoof Checks value.");
         } else {
            this.fChecks = var1 & -1;
            return this;
         }
      }

      public SpoofChecker.Builder setAllowedLocales(Set var1) {
         this.fAllowedCharsSet.clear();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            ULocale var3 = (ULocale)var2.next();
            this.addScriptChars(var3, this.fAllowedCharsSet);
         }

         this.fAllowedLocales.clear();
         if (var1.size() == 0) {
            this.fAllowedCharsSet.add(0, 1114111);
            this.fChecks &= -65;
            return this;
         } else {
            UnicodeSet var4 = new UnicodeSet();
            var4.applyIntPropertyValue(4106, 0);
            this.fAllowedCharsSet.addAll(var4);
            var4.applyIntPropertyValue(4106, 1);
            this.fAllowedCharsSet.addAll(var4);
            this.fAllowedLocales.addAll(var1);
            this.fChecks |= 64;
            return this;
         }
      }

      private void addScriptChars(ULocale var1, UnicodeSet var2) {
         int[] var3 = UScript.getCode(var1);
         UnicodeSet var4 = new UnicodeSet();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4.applyIntPropertyValue(4106, var3[var5]);
            var2.addAll(var4);
         }

      }

      public SpoofChecker.Builder setAllowedChars(UnicodeSet var1) {
         this.fAllowedCharsSet.set(var1);
         this.fAllowedLocales.clear();
         this.fChecks |= 64;
         return this;
      }

      public SpoofChecker.Builder setRestrictionLevel(SpoofChecker.RestrictionLevel var1) {
         this.fRestrictionLevel = var1;
         this.fChecks |= 16;
         return this;
      }

      private static class ConfusabledataBuilder {
         private SpoofChecker.SpoofData fSpoofData;
         private ByteArrayOutputStream bos;
         private DataOutputStream os;
         private Hashtable fSLTable;
         private Hashtable fSATable;
         private Hashtable fMLTable;
         private Hashtable fMATable;
         private UnicodeSet fKeySet;
         private StringBuffer fStringTable;
         private Vector fKeyVec;
         private Vector fValueVec;
         private Vector fStringLengthsTable;
         private SpoofChecker.Builder.ConfusabledataBuilder.SPUStringPool stringPool;
         private Pattern fParseLine;
         private Pattern fParseHexNum;
         private int fLineNum;
         static final boolean $assertionsDisabled = !SpoofChecker.class.desiredAssertionStatus();

         ConfusabledataBuilder(SpoofChecker.SpoofData var1, ByteArrayOutputStream var2) {
            this.bos = var2;
            this.os = new DataOutputStream(var2);
            this.fSpoofData = var1;
            this.fSLTable = new Hashtable();
            this.fSATable = new Hashtable();
            this.fMLTable = new Hashtable();
            this.fMATable = new Hashtable();
            this.fKeySet = new UnicodeSet();
            this.fKeyVec = new Vector();
            this.fValueVec = new Vector();
            this.stringPool = new SpoofChecker.Builder.ConfusabledataBuilder.SPUStringPool();
         }

         void build(Reader var1) throws ParseException, IOException {
            StringBuffer var2 = new StringBuffer();
            SpoofChecker.Builder.WSConfusableDataBuilder.readWholeFileToString(var1, var2);
            this.fParseLine = Pattern.compile("(?m)^[ \\t]*([0-9A-Fa-f]+)[ \\t]+;[ \\t]*([0-9A-Fa-f]+(?:[ \\t]+[0-9A-Fa-f]+)*)[ \\t]*;\\s*(?:(SL)|(SA)|(ML)|(MA))[ \\t]*(?:#.*?)?$|^([ \\t]*(?:#.*?)?)$|^(.*?)$");
            this.fParseHexNum = Pattern.compile("\\s*([0-9A-F]+)");
            if (var2.charAt(0) == '\ufeff') {
               var2.setCharAt(0, ' ');
            }

            Matcher var3 = this.fParseLine.matcher(var2);

            while(true) {
               int var4;
               int var7;
               do {
                  if (!var3.find()) {
                     this.stringPool.sort();
                     this.fStringTable = new StringBuffer();
                     this.fStringLengthsTable = new Vector();
                     var4 = 0;
                     int var11 = 0;
                     int var12 = this.stringPool.size();

                     int var9;
                     for(var7 = 0; var7 < var12; ++var7) {
                        SpoofChecker.Builder.ConfusabledataBuilder.SPUString var14 = this.stringPool.getByIndex(var7);
                        var9 = var14.fStr.length();
                        int var10 = this.fStringTable.length();
                        if (!$assertionsDisabled && var9 < var4) {
                           throw new AssertionError();
                        }

                        if (var9 == 1) {
                           var14.fStrTableIndex = var14.fStr.charAt(0);
                        } else {
                           if (var9 > var4 && var4 >= 4) {
                              this.fStringLengthsTable.addElement(var11);
                              this.fStringLengthsTable.addElement(var4);
                           }

                           var14.fStrTableIndex = var10;
                           this.fStringTable.append(var14.fStr);
                        }

                        var4 = var9;
                        var11 = var10;
                     }

                     if (var4 >= 4) {
                        this.fStringLengthsTable.addElement(var11);
                        this.fStringLengthsTable.addElement(var4);
                     }

                     for(int var15 = 0; var15 < this.fKeySet.getRangeCount(); ++var15) {
                        for(var9 = this.fKeySet.getRangeStart(var15); var9 <= this.fKeySet.getRangeEnd(var15); ++var9) {
                           this.addKeyEntry(var9, this.fSLTable, 16777216);
                           this.addKeyEntry(var9, this.fSATable, 33554432);
                           this.addKeyEntry(var9, this.fMLTable, 67108864);
                           this.addKeyEntry(var9, this.fMATable, 134217728);
                        }
                     }

                     this.outputData();
                     return;
                  }

                  ++this.fLineNum;
               } while(var3.start(7) >= 0);

               if (var3.start(8) >= 0) {
                  throw new ParseException("Confusables, line " + this.fLineNum + ": Unrecognized Line: " + var3.group(8), var3.start(8));
               }

               var4 = Integer.parseInt(var3.group(1), 16);
               if (var4 > 1114111) {
                  throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + var3.group(1), var3.start(1));
               }

               Matcher var5 = this.fParseHexNum.matcher(var3.group(2));
               StringBuilder var6 = new StringBuilder();

               while(var5.find()) {
                  var7 = Integer.parseInt(var5.group(1), 16);
                  if (var4 > 1114111) {
                     throw new ParseException("Confusables, line " + this.fLineNum + ": Bad code point: " + Integer.toString(var7, 16), var3.start(2));
                  }

                  var6.appendCodePoint(var7);
               }

               if (!$assertionsDisabled && var6.length() < 1) {
                  throw new AssertionError();
               }

               SpoofChecker.Builder.ConfusabledataBuilder.SPUString var13 = this.stringPool.addString(var6.toString());
               Hashtable var8 = var3.start(3) >= 0 ? this.fSLTable : (var3.start(4) >= 0 ? this.fSATable : (var3.start(5) >= 0 ? this.fMLTable : (var3.start(6) >= 0 ? this.fMATable : null)));
               if (!$assertionsDisabled && var8 == null) {
                  throw new AssertionError();
               }

               var8.put(var4, var13);
               this.fKeySet.add(var4);
            }
         }

         void addKeyEntry(int var1, Hashtable var2, int var3) {
            SpoofChecker.Builder.ConfusabledataBuilder.SPUString var4 = (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)var2.get(var1);
            if (var4 != null) {
               boolean var5 = false;

               int var7;
               for(int var6 = this.fKeyVec.size() - 1; var6 >= 0; --var6) {
                  var7 = (Integer)this.fKeyVec.elementAt(var6);
                  if ((var7 & 16777215) != var1) {
                     break;
                  }

                  String var8 = this.getMapping(var6);
                  if (var8.equals(var4.fStr)) {
                     var7 |= var3;
                     this.fKeyVec.setElementAt(var7, var6);
                     return;
                  }

                  var5 = true;
               }

               var7 = var1 | var3;
               if (var5) {
                  var7 |= 268435456;
               }

               int var12 = var4.fStr.length() - 1;
               if (var12 > 3) {
                  var12 = 3;
               }

               var7 |= var12 << 29;
               int var9 = var4.fStrTableIndex;
               this.fKeyVec.addElement(var7);
               this.fValueVec.addElement(var9);
               if (var5) {
                  int var10 = this.fKeyVec.size() - 2;
                  int var11 = (Integer)this.fKeyVec.elementAt(var10);
                  var11 |= 268435456;
                  this.fKeyVec.setElementAt(var11, var10);
               }

            }
         }

         String getMapping(int var1) {
            int var2 = (Integer)this.fKeyVec.elementAt(var1);
            int var3 = (Integer)this.fValueVec.elementAt(var1);
            int var4 = SpoofChecker.getKeyLength(var2);
            switch(var4) {
            case 0:
               char[] var6 = new char[]{(char)var3};
               return new String(var6);
            case 1:
            case 2:
               return this.fStringTable.substring(var3, var3 + var4 + 1);
            case 3:
               var4 = 0;

               for(int var7 = 0; var7 < this.fStringLengthsTable.size(); var7 += 2) {
                  int var5 = (Integer)this.fStringLengthsTable.elementAt(var7);
                  if (var3 <= var5) {
                     var4 = (Integer)this.fStringLengthsTable.elementAt(var7 + 1);
                     break;
                  }
               }

               if (!$assertionsDisabled && var4 < 3) {
                  throw new AssertionError();
               } else {
                  return this.fStringTable.substring(var3, var3 + var4);
               }
            default:
               if (!$assertionsDisabled) {
                  throw new AssertionError();
               } else {
                  return "";
               }
            }
         }

         void outputData() throws IOException {
            SpoofChecker.SpoofDataHeader var1 = this.fSpoofData.fRawData;
            int var2 = this.fKeyVec.size();
            int var4 = 0;
            var1.output(this.os);
            var1.fCFUKeys = this.os.size();
            if (!$assertionsDisabled && var1.fCFUKeys != 128) {
               throw new AssertionError();
            } else {
               var1.fCFUKeysSize = var2;

               int var3;
               int var5;
               for(var3 = 0; var3 < var2; ++var3) {
                  var5 = (Integer)this.fKeyVec.elementAt(var3);
                  if (!$assertionsDisabled && (var5 & 16777215) < (var4 & 16777215)) {
                     throw new AssertionError();
                  }

                  if (!$assertionsDisabled && (var5 & -16777216) == 0) {
                     throw new AssertionError();
                  }

                  this.os.writeInt(var5);
                  var4 = var5;
               }

               var5 = this.fValueVec.size();
               if (!$assertionsDisabled && var2 != var5) {
                  throw new AssertionError();
               } else {
                  var1.fCFUStringIndex = this.os.size();
                  var1.fCFUStringIndexSize = var5;

                  int var6;
                  for(var3 = 0; var3 < var5; ++var3) {
                     var6 = (Integer)this.fValueVec.elementAt(var3);
                     if (!$assertionsDisabled && var6 >= 65535) {
                        throw new AssertionError();
                     }

                     this.os.writeShort((short)var6);
                  }

                  var6 = this.fStringTable.length();
                  String var7 = this.fStringTable.toString();
                  var1.fCFUStringTable = this.os.size();
                  var1.fCFUStringTableLen = var6;

                  for(var3 = 0; var3 < var6; ++var3) {
                     this.os.writeChar(var7.charAt(var3));
                  }

                  int var8 = this.fStringLengthsTable.size();
                  int var9 = 0;
                  var1.fCFUStringLengthsSize = var8 / 2;
                  var1.fCFUStringLengths = this.os.size();

                  for(var3 = 0; var3 < var8; var3 += 2) {
                     int var10 = (Integer)this.fStringLengthsTable.elementAt(var3);
                     int var11 = (Integer)this.fStringLengthsTable.elementAt(var3 + 1);
                     if (!$assertionsDisabled && var10 >= var6) {
                        throw new AssertionError();
                     }

                     if (!$assertionsDisabled && var11 >= 40) {
                        throw new AssertionError();
                     }

                     if (!$assertionsDisabled && var11 <= var9) {
                        throw new AssertionError();
                     }

                     this.os.writeShort((short)var10);
                     this.os.writeShort((short)var11);
                     var9 = var11;
                  }

                  this.os.flush();
                  DataInputStream var12 = new DataInputStream(new ByteArrayInputStream(this.bos.toByteArray()));
                  var12.mark(Integer.MAX_VALUE);
                  this.fSpoofData.initPtrs(var12);
               }
            }
         }

         public static void buildConfusableData(SpoofChecker.SpoofData var0, Reader var1) throws IOException, ParseException {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            SpoofChecker.Builder.ConfusabledataBuilder var3 = new SpoofChecker.Builder.ConfusabledataBuilder(var0, var2);
            var3.build(var1);
         }

         private static class SPUStringPool {
            private Vector fVec = new Vector();
            private Hashtable fHash = new Hashtable();

            public SPUStringPool() {
            }

            public int size() {
               return this.fVec.size();
            }

            public SpoofChecker.Builder.ConfusabledataBuilder.SPUString getByIndex(int var1) {
               SpoofChecker.Builder.ConfusabledataBuilder.SPUString var2 = (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)this.fVec.elementAt(var1);
               return var2;
            }

            public SpoofChecker.Builder.ConfusabledataBuilder.SPUString addString(String var1) {
               SpoofChecker.Builder.ConfusabledataBuilder.SPUString var2 = (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)this.fHash.get(var1);
               if (var2 == null) {
                  var2 = new SpoofChecker.Builder.ConfusabledataBuilder.SPUString(var1);
                  this.fHash.put(var1, var2);
                  this.fVec.addElement(var2);
               }

               return var2;
            }

            public void sort() {
               Collections.sort(this.fVec, new SpoofChecker.Builder.ConfusabledataBuilder.SPUStringComparator());
            }
         }

         private static class SPUStringComparator implements Comparator {
            private SPUStringComparator() {
            }

            public int compare(SpoofChecker.Builder.ConfusabledataBuilder.SPUString var1, SpoofChecker.Builder.ConfusabledataBuilder.SPUString var2) {
               int var3 = var1.fStr.length();
               int var4 = var2.fStr.length();
               if (var3 < var4) {
                  return -1;
               } else {
                  return var3 > var4 ? 1 : var1.fStr.compareTo(var2.fStr);
               }
            }

            public int compare(Object var1, Object var2) {
               return this.compare((SpoofChecker.Builder.ConfusabledataBuilder.SPUString)var1, (SpoofChecker.Builder.ConfusabledataBuilder.SPUString)var2);
            }

            SPUStringComparator(Object var1) {
               this();
            }
         }

         private static class SPUString {
            String fStr;
            int fStrTableIndex;

            SPUString(String var1) {
               this.fStr = var1;
               this.fStrTableIndex = 0;
            }
         }
      }

      private static class WSConfusableDataBuilder {
         static String parseExp = "(?m)^([ \\t]*(?:#.*?)?)$|^(?:\\s*([0-9A-F]{4,})(?:..([0-9A-F]{4,}))?\\s*;\\s*([A-Za-z]+)\\s*;\\s*([A-Za-z]+)\\s*;\\s*(?:(A)|(L))[ \\t]*(?:#.*?)?)$|^(.*?)$";
         static final boolean $assertionsDisabled = !SpoofChecker.class.desiredAssertionStatus();

         static void readWholeFileToString(Reader var0, StringBuffer var1) throws IOException {
            LineNumberReader var2 = new LineNumberReader(var0);

            while(true) {
               String var3 = var2.readLine();
               if (var3 == null) {
                  return;
               }

               var1.append(var3);
               var1.append('\n');
            }
         }

         static void buildWSConfusableData(SpoofChecker.SpoofData var0, DataOutputStream var1, Reader var2) throws ParseException, IOException {
            Pattern var3 = null;
            StringBuffer var4 = new StringBuffer();
            int var5 = 0;
            Vector var6 = null;
            boolean var7 = true;
            Trie2Writable var8 = new Trie2Writable(0, 0);
            Trie2Writable var9 = new Trie2Writable(0, 0);
            var6 = new Vector();
            var6.addElement((Object)null);
            var6.addElement((Object)null);
            readWholeFileToString(var2, var4);
            var3 = Pattern.compile(parseExp);
            if (var4.charAt(0) == '\ufeff') {
               var4.setCharAt(0, ' ');
            }

            Matcher var10 = var3.matcher(var4);

            while(true) {
               int var11;
               int var12;
               int var15;
               do {
                  if (!var10.find()) {
                     int var22 = 2;

                     SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet var24;
                     int var25;
                     for(var11 = 2; var11 < var6.size(); ++var11) {
                        var24 = (SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet)var6.elementAt(var11);
                        if (var24.index == var11) {
                           var24.rindex = var22++;

                           for(var25 = var11 + 1; var25 < var6.size(); ++var25) {
                              SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet var27 = (SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet)var6.elementAt(var25);
                              if (var24.sset.equals(var27.sset) && var24.sset != var27.sset) {
                                 var27.sset = var24.sset;
                                 var27.index = var11;
                                 var27.rindex = var24.rindex;
                              }
                           }
                        }
                     }

                     for(var11 = 2; var11 < var6.size(); ++var11) {
                        var24 = (SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet)var6.elementAt(var11);
                        if (var24.rindex != var11) {
                           var24.trie.set(var24.codePoint, var24.rindex);
                        }
                     }

                     UnicodeSet var23 = new UnicodeSet();
                     var23.applyIntPropertyValue(4106, 0);
                     UnicodeSet var26 = new UnicodeSet();
                     var26.applyIntPropertyValue(4106, 1);
                     var23.addAll(var26);

                     for(var25 = 0; var25 < var23.getRangeCount(); ++var25) {
                        int var28 = var23.getRangeStart(var25);
                        var15 = var23.getRangeEnd(var25);
                        var8.setRange(var28, var15, 1, true);
                        var9.setRange(var28, var15, 1, true);
                     }

                     var8.toTrie2_16().serialize(var1);
                     var9.toTrie2_16().serialize(var1);
                     var0.fRawData.fScriptSetsLength = var22;
                     var11 = 2;

                     for(var12 = 2; var12 < var6.size(); ++var12) {
                        SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet var29 = (SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet)var6.elementAt(var12);
                        if (var29.rindex >= var11) {
                           if (!$assertionsDisabled && var11 != var29.rindex) {
                              throw new AssertionError();
                           }

                           var29.sset.output(var1);
                           ++var11;
                        }
                     }

                     return;
                  }

                  ++var5;
               } while(var10.start(1) >= 0);

               if (var10.start(8) >= 0) {
                  throw new ParseException("ConfusablesWholeScript, line " + var5 + ": Unrecognized input: " + var10.group(), var10.start());
               }

               var11 = Integer.parseInt(var10.group(2), 16);
               if (var11 > 1114111) {
                  throw new ParseException("ConfusablesWholeScript, line " + var5 + ": out of range code point: " + var10.group(2), var10.start(2));
               }

               var12 = var11;
               if (var10.start(3) >= 0) {
                  var12 = Integer.parseInt(var10.group(3), 16);
               }

               if (var12 > 1114111) {
                  throw new ParseException("ConfusablesWholeScript, line " + var5 + ": out of range code point: " + var10.group(3), var10.start(3));
               }

               String var13 = var10.group(4);
               String var14 = var10.group(5);
               var15 = UCharacter.getPropertyValueEnum(4106, var13);
               int var16 = UCharacter.getPropertyValueEnum(4106, var14);
               if (var15 == -1) {
                  throw new ParseException("ConfusablesWholeScript, line " + var5 + ": Invalid script code t: " + var10.group(4), var10.start(4));
               }

               if (var16 == -1) {
                  throw new ParseException("ConfusablesWholeScript, line " + var5 + ": Invalid script code t: " + var10.group(5), var10.start(5));
               }

               Trie2Writable var17 = var8;
               if (var10.start(7) >= 0) {
                  var17 = var9;
               }

               for(int var18 = var11; var18 <= var12; ++var18) {
                  int var19 = var17.get(var18);
                  SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet var20 = null;
                  if (var19 > 0) {
                     if (!$assertionsDisabled && var19 >= var6.size()) {
                        throw new AssertionError();
                     }

                     var20 = (SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet)var6.elementAt(var19);
                  } else {
                     var20 = new SpoofChecker.Builder.WSConfusableDataBuilder.BuilderScriptSet();
                     var20.codePoint = var18;
                     var20.trie = var17;
                     var20.sset = new SpoofChecker.ScriptSet();
                     var19 = var6.size();
                     var20.index = var19;
                     var20.rindex = 0;
                     var6.addElement(var20);
                     var17.set(var18, var19);
                  }

                  var20.sset.Union(var16);
                  var20.sset.Union(var15);
                  int var21 = UScript.getScript(var18);
                  if (var21 != var15) {
                     throw new ParseException("ConfusablesWholeScript, line " + var5 + ": Mismatch between source script and code point " + Integer.toString(var18, 16), var10.start(5));
                  }
               }
            }
         }

         private static class BuilderScriptSet {
            int codePoint = -1;
            Trie2Writable trie = null;
            SpoofChecker.ScriptSet sset = null;
            int index = 0;
            int rindex = 0;

            BuilderScriptSet() {
            }
         }
      }
   }

   public static enum RestrictionLevel {
      ASCII,
      HIGHLY_RESTRICTIVE,
      MODERATELY_RESTRICTIVE,
      MINIMALLY_RESTRICTIVE,
      UNRESTRICTIVE;

      private static final SpoofChecker.RestrictionLevel[] $VALUES = new SpoofChecker.RestrictionLevel[]{ASCII, HIGHLY_RESTRICTIVE, MODERATELY_RESTRICTIVE, MINIMALLY_RESTRICTIVE, UNRESTRICTIVE};
   }
}
