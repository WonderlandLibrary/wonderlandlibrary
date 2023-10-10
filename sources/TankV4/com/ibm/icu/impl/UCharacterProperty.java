package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.MissingResourceException;

public final class UCharacterProperty {
   public static final UCharacterProperty INSTANCE;
   public Trie2_16 m_trie_;
   public VersionInfo m_unicodeVersion_;
   public static final char LATIN_CAPITAL_LETTER_I_WITH_DOT_ABOVE_ = 'İ';
   public static final char LATIN_SMALL_LETTER_DOTLESS_I_ = 'ı';
   public static final char LATIN_SMALL_LETTER_I_ = 'i';
   public static final int TYPE_MASK = 31;
   public static final int SRC_NONE = 0;
   public static final int SRC_CHAR = 1;
   public static final int SRC_PROPSVEC = 2;
   public static final int SRC_NAMES = 3;
   public static final int SRC_CASE = 4;
   public static final int SRC_BIDI = 5;
   public static final int SRC_CHAR_AND_PROPSVEC = 6;
   public static final int SRC_CASE_AND_NORM = 7;
   public static final int SRC_NFC = 8;
   public static final int SRC_NFKC = 9;
   public static final int SRC_NFKC_CF = 10;
   public static final int SRC_NFC_CANON_ITER = 11;
   public static final int SRC_COUNT = 12;
   static final int MY_MASK = 30;
   private static final int GC_CN_MASK = getMask(0);
   private static final int GC_CC_MASK = getMask(15);
   private static final int GC_CS_MASK = getMask(18);
   private static final int GC_ZS_MASK = getMask(12);
   private static final int GC_ZL_MASK = getMask(13);
   private static final int GC_ZP_MASK = getMask(14);
   private static final int GC_Z_MASK;
   UCharacterProperty.BinaryProperty[] binProps = new UCharacterProperty.BinaryProperty[]{new UCharacterProperty.BinaryProperty(this, 1, 256), new UCharacterProperty.BinaryProperty(this, 1, 128), new UCharacterProperty.BinaryProperty(this, 5) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UBiDiProps.INSTANCE.isBidiControl(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 5) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UBiDiProps.INSTANCE.isMirrored(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1, 2), new UCharacterProperty.BinaryProperty(this, 1, 524288), new UCharacterProperty.BinaryProperty(this, 1, 1048576), new UCharacterProperty.BinaryProperty(this, 1, 1024), new UCharacterProperty.BinaryProperty(this, 1, 2048), new UCharacterProperty.BinaryProperty(this, 8) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         Normalizer2Impl var2 = Norm2AllModes.getNFCInstance().impl;
         return var2.isCompNo(var2.getNorm16(var1));
      }
   }, new UCharacterProperty.BinaryProperty(this, 1, 67108864), new UCharacterProperty.BinaryProperty(this, 1, 8192), new UCharacterProperty.BinaryProperty(this, 1, 16384), new UCharacterProperty.BinaryProperty(this, 1, 64), new UCharacterProperty.BinaryProperty(this, 1, 4), new UCharacterProperty.BinaryProperty(this, 1, 33554432), new UCharacterProperty.BinaryProperty(this, 1, 16777216), new UCharacterProperty.BinaryProperty(this, 1, 512), new UCharacterProperty.BinaryProperty(this, 1, 32768), new UCharacterProperty.BinaryProperty(this, 1, 65536), new UCharacterProperty.BinaryProperty(this, 5) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UBiDiProps.INSTANCE.isJoinControl(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1, 2097152), new UCharacterProperty.CaseBinaryProperty(this, 22), new UCharacterProperty.BinaryProperty(this, 1, 32), new UCharacterProperty.BinaryProperty(this, 1, 4096), new UCharacterProperty.BinaryProperty(this, 1, 8), new UCharacterProperty.BinaryProperty(this, 1, 131072), new UCharacterProperty.CaseBinaryProperty(this, 27), new UCharacterProperty.BinaryProperty(this, 1, 16), new UCharacterProperty.BinaryProperty(this, 1, 262144), new UCharacterProperty.CaseBinaryProperty(this, 30), new UCharacterProperty.BinaryProperty(this, 1, 1), new UCharacterProperty.BinaryProperty(this, 1, 8388608), new UCharacterProperty.BinaryProperty(this, 1, 4194304), new UCharacterProperty.CaseBinaryProperty(this, 34), new UCharacterProperty.BinaryProperty(this, 1, 134217728), new UCharacterProperty.BinaryProperty(this, 1, 268435456), new UCharacterProperty.NormInertBinaryProperty(this, 8, 37), new UCharacterProperty.NormInertBinaryProperty(this, 9, 38), new UCharacterProperty.NormInertBinaryProperty(this, 8, 39), new UCharacterProperty.NormInertBinaryProperty(this, 9, 40), new UCharacterProperty.BinaryProperty(this, 11) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return Norm2AllModes.getNFCInstance().impl.ensureCanonIterData().isCanonSegmentStarter(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1, 536870912), new UCharacterProperty.BinaryProperty(this, 1, 1073741824), new UCharacterProperty.BinaryProperty(this, 6) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UCharacter.isUAlphabetic(var1) || UCharacter.isDigit(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         if (var1 > 159) {
            return UCharacter.getType(var1) == 12;
         } else {
            return var1 == 9 || var1 == 32;
         }
      }
   }, new UCharacterProperty.BinaryProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UCharacterProperty.access$000(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         return UCharacter.getType(var1) == 12 || UCharacterProperty.access$000(var1);
      }
   }, new UCharacterProperty.BinaryProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         if (var1 <= 102 && var1 >= 65 && (var1 <= 70 || var1 >= 97) || var1 >= 65313 && var1 <= 65350 && (var1 <= 65318 || var1 >= 65345)) {
            return true;
         } else {
            return UCharacter.getType(var1) == 9;
         }
      }
   }, new UCharacterProperty.CaseBinaryProperty(this, 49), new UCharacterProperty.CaseBinaryProperty(this, 50), new UCharacterProperty.CaseBinaryProperty(this, 51), new UCharacterProperty.CaseBinaryProperty(this, 52), new UCharacterProperty.CaseBinaryProperty(this, 53), new UCharacterProperty.BinaryProperty(this, 7) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         String var2 = Norm2AllModes.getNFCInstance().impl.getDecomposition(var1);
         if (var2 != null) {
            var1 = var2.codePointAt(0);
            if (Character.charCount(var1) != var2.length()) {
               var1 = -1;
            }
         } else if (var1 < 0) {
            return false;
         }

         if (var1 >= 0) {
            UCaseProps var4 = UCaseProps.INSTANCE;
            UCaseProps.dummyStringBuilder.setLength(0);
            return var4.toFullFolding(var1, UCaseProps.dummyStringBuilder, 0) >= 0;
         } else {
            String var3 = UCharacter.foldCase(var2, true);
            return !var3.equals(var2);
         }
      }
   }, new UCharacterProperty.CaseBinaryProperty(this, 55), new UCharacterProperty.BinaryProperty(this, 10) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      boolean contains(int var1) {
         Normalizer2Impl var2 = Norm2AllModes.getNFKC_CFInstance().impl;
         String var3 = UTF16.valueOf(var1);
         StringBuilder var4 = new StringBuilder();
         Normalizer2Impl.ReorderingBuffer var5 = new Normalizer2Impl.ReorderingBuffer(var2, var4, 5);
         var2.compose(var3, 0, var3.length(), false, true, var5);
         return !Normalizer2Impl.UTF16Plus.equal(var4, var3);
      }
   }};
   private static final int[] gcbToHst;
   UCharacterProperty.IntProperty[] intProps = new UCharacterProperty.IntProperty[]{new UCharacterProperty.BiDiIntProperty(this) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return UBiDiProps.INSTANCE.getClass(var1);
      }
   }, new UCharacterProperty.IntProperty(this, 0, 130816, 8), new UCharacterProperty.CombiningClassIntProperty(this, 8) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return Norm2AllModes.getNFCInstance().decomp.getCombiningClass(var1);
      }
   }, new UCharacterProperty.IntProperty(this, 2, 31, 0), new UCharacterProperty.IntProperty(this, 0, 917504, 17), new UCharacterProperty.IntProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return this.this$0.getType(var1);
      }

      int getMaxValue(int var1) {
         return 29;
      }
   }, new UCharacterProperty.BiDiIntProperty(this) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return UBiDiProps.INSTANCE.getJoiningGroup(var1);
      }
   }, new UCharacterProperty.BiDiIntProperty(this) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return UBiDiProps.INSTANCE.getJoiningType(var1);
      }
   }, new UCharacterProperty.IntProperty(this, 2, 66060288, 20), new UCharacterProperty.IntProperty(this, 1) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return UCharacterProperty.access$200(UCharacterProperty.access$100(this.this$0.getProperty(var1)));
      }

      int getMaxValue(int var1) {
         return 3;
      }
   }, new UCharacterProperty.IntProperty(this, 0, 255, 0) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return UScript.getScript(var1);
      }
   }, new UCharacterProperty.IntProperty(this, 2) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         int var2 = (this.this$0.getAdditional(var1, 2) & 992) >>> 5;
         return var2 < UCharacterProperty.access$300().length ? UCharacterProperty.access$300()[var2] : 0;
      }

      int getMaxValue(int var1) {
         return 5;
      }
   }, new UCharacterProperty.NormQuickCheckIntProperty(this, 8, 4108, 1), new UCharacterProperty.NormQuickCheckIntProperty(this, 9, 4109, 1), new UCharacterProperty.NormQuickCheckIntProperty(this, 8, 4110, 2), new UCharacterProperty.NormQuickCheckIntProperty(this, 9, 4111, 2), new UCharacterProperty.CombiningClassIntProperty(this, 8) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return Norm2AllModes.getNFCInstance().impl.getFCD16(var1) >> 8;
      }
   }, new UCharacterProperty.CombiningClassIntProperty(this, 8) {
      final UCharacterProperty this$0;

      {
         this.this$0 = var1;
      }

      int getValue(int var1) {
         return Norm2AllModes.getNFCInstance().impl.getFCD16(var1) & 255;
      }
   }, new UCharacterProperty.IntProperty(this, 2, 992, 5), new UCharacterProperty.IntProperty(this, 2, 1015808, 15), new UCharacterProperty.IntProperty(this, 2, 31744, 10)};
   Trie2_16 m_additionalTrie_;
   int[] m_additionalVectors_;
   int m_additionalColumnsCount_;
   int m_maxBlockScriptValue_;
   int m_maxJTGValue_;
   public char[] m_scriptExtensions_;
   private static final String DATA_FILE_NAME_ = "data/icudt51b/uprops.icu";
   private static final int DATA_BUFFER_SIZE_ = 25000;
   private static final int LEAD_SURROGATE_SHIFT_ = 10;
   private static final int SURROGATE_OFFSET_ = -56613888;
   private static final int NUMERIC_TYPE_VALUE_SHIFT_ = 6;
   private static final int NTV_NONE_ = 0;
   private static final int NTV_DECIMAL_START_ = 1;
   private static final int NTV_DIGIT_START_ = 11;
   private static final int NTV_NUMERIC_START_ = 21;
   private static final int NTV_FRACTION_START_ = 176;
   private static final int NTV_LARGE_START_ = 480;
   private static final int NTV_BASE60_START_ = 768;
   private static final int NTV_RESERVED_START_ = 804;
   public static final int SCRIPT_X_MASK = 12583167;
   private static final int EAST_ASIAN_MASK_ = 917504;
   private static final int EAST_ASIAN_SHIFT_ = 17;
   private static final int BLOCK_MASK_ = 130816;
   private static final int BLOCK_SHIFT_ = 8;
   public static final int SCRIPT_MASK_ = 255;
   public static final int SCRIPT_X_WITH_COMMON = 4194304;
   public static final int SCRIPT_X_WITH_INHERITED = 8388608;
   public static final int SCRIPT_X_WITH_OTHER = 12582912;
   private static final int WHITE_SPACE_PROPERTY_ = 0;
   private static final int DASH_PROPERTY_ = 1;
   private static final int HYPHEN_PROPERTY_ = 2;
   private static final int QUOTATION_MARK_PROPERTY_ = 3;
   private static final int TERMINAL_PUNCTUATION_PROPERTY_ = 4;
   private static final int MATH_PROPERTY_ = 5;
   private static final int HEX_DIGIT_PROPERTY_ = 6;
   private static final int ASCII_HEX_DIGIT_PROPERTY_ = 7;
   private static final int ALPHABETIC_PROPERTY_ = 8;
   private static final int IDEOGRAPHIC_PROPERTY_ = 9;
   private static final int DIACRITIC_PROPERTY_ = 10;
   private static final int EXTENDER_PROPERTY_ = 11;
   private static final int NONCHARACTER_CODE_POINT_PROPERTY_ = 12;
   private static final int GRAPHEME_EXTEND_PROPERTY_ = 13;
   private static final int GRAPHEME_LINK_PROPERTY_ = 14;
   private static final int IDS_BINARY_OPERATOR_PROPERTY_ = 15;
   private static final int IDS_TRINARY_OPERATOR_PROPERTY_ = 16;
   private static final int RADICAL_PROPERTY_ = 17;
   private static final int UNIFIED_IDEOGRAPH_PROPERTY_ = 18;
   private static final int DEFAULT_IGNORABLE_CODE_POINT_PROPERTY_ = 19;
   private static final int DEPRECATED_PROPERTY_ = 20;
   private static final int LOGICAL_ORDER_EXCEPTION_PROPERTY_ = 21;
   private static final int XID_START_PROPERTY_ = 22;
   private static final int XID_CONTINUE_PROPERTY_ = 23;
   private static final int ID_START_PROPERTY_ = 24;
   private static final int ID_CONTINUE_PROPERTY_ = 25;
   private static final int GRAPHEME_BASE_PROPERTY_ = 26;
   private static final int S_TERM_PROPERTY_ = 27;
   private static final int VARIATION_SELECTOR_PROPERTY_ = 28;
   private static final int PATTERN_SYNTAX = 29;
   private static final int PATTERN_WHITE_SPACE = 30;
   private static final int LB_MASK = 66060288;
   private static final int LB_SHIFT = 20;
   private static final int SB_MASK = 1015808;
   private static final int SB_SHIFT = 15;
   private static final int WB_MASK = 31744;
   private static final int WB_SHIFT = 10;
   private static final int GCB_MASK = 992;
   private static final int GCB_SHIFT = 5;
   private static final int DECOMPOSITION_TYPE_MASK_ = 31;
   private static final int FIRST_NIBBLE_SHIFT_ = 4;
   private static final int LAST_NIBBLE_MASK_ = 15;
   private static final int AGE_SHIFT_ = 24;
   private static final byte[] DATA_FORMAT;
   private static final int TAB = 9;
   private static final int CR = 13;
   private static final int U_A = 65;
   private static final int U_F = 70;
   private static final int U_Z = 90;
   private static final int U_a = 97;
   private static final int U_f = 102;
   private static final int U_z = 122;
   private static final int DEL = 127;
   private static final int NL = 133;
   private static final int NBSP = 160;
   private static final int CGJ = 847;
   private static final int FIGURESP = 8199;
   private static final int HAIRSP = 8202;
   private static final int RLM = 8207;
   private static final int NNBSP = 8239;
   private static final int WJ = 8288;
   private static final int INHSWAP = 8298;
   private static final int NOMDIG = 8303;
   private static final int U_FW_A = 65313;
   private static final int U_FW_F = 65318;
   private static final int U_FW_Z = 65338;
   private static final int U_FW_a = 65345;
   private static final int U_FW_f = 65350;
   private static final int U_FW_z = 65370;
   private static final int ZWNBSP = 65279;
   static final boolean $assertionsDisabled = !UCharacterProperty.class.desiredAssertionStatus();

   public final int getProperty(int var1) {
      return this.m_trie_.get(var1);
   }

   public int getAdditional(int var1, int var2) {
      if (!$assertionsDisabled && var2 < 0) {
         throw new AssertionError();
      } else {
         return var2 >= this.m_additionalColumnsCount_ ? 0 : this.m_additionalVectors_[this.m_additionalTrie_.get(var1) + var2];
      }
   }

   public VersionInfo getAge(int var1) {
      int var2 = this.getAdditional(var1, 0) >> 24;
      return VersionInfo.getInstance(var2 >> 4 & 15, var2 & 15, 0, 0);
   }

   private static final boolean isgraphPOSIX(int var0) {
      return (getMask(UCharacter.getType(var0)) & (GC_CC_MASK | GC_CS_MASK | GC_CN_MASK | GC_Z_MASK)) == 0;
   }

   public boolean hasBinaryProperty(int var1, int var2) {
      return var2 >= 0 && 57 > var2 ? this.binProps[var2].contains(var1) : false;
   }

   public int getType(int var1) {
      return this.getProperty(var1) & 31;
   }

   public int getIntPropertyValue(int var1, int var2) {
      if (var2 < 4096) {
         if (0 <= var2 && var2 < 57) {
            return this.binProps[var2].contains(var1) ? 1 : 0;
         }
      } else {
         if (var2 < 4117) {
            return this.intProps[var2 - 4096].getValue(var1);
         }

         if (var2 == 8192) {
            return getMask(this.getType(var1));
         }
      }

      return 0;
   }

   public int getIntPropertyMaxValue(int var1) {
      if (var1 < 4096) {
         if (0 <= var1 && var1 < 57) {
            return 1;
         }
      } else if (var1 < 4117) {
         return this.intProps[var1 - 4096].getMaxValue(var1);
      }

      return -1;
   }

   public final int getSource(int var1) {
      if (var1 < 0) {
         return 0;
      } else if (var1 < 57) {
         return this.binProps[var1].getSource();
      } else if (var1 < 4096) {
         return 0;
      } else if (var1 < 4117) {
         return this.intProps[var1 - 4096].getSource();
      } else if (var1 < 16384) {
         switch(var1) {
         case 8192:
         case 12288:
            return 1;
         default:
            return 0;
         }
      } else if (var1 < 16397) {
         switch(var1) {
         case 16384:
            return 2;
         case 16385:
            return 5;
         case 16386:
         case 16388:
         case 16390:
         case 16391:
         case 16392:
         case 16393:
         case 16394:
         case 16396:
            return 4;
         case 16387:
         case 16389:
         case 16395:
            return 3;
         default:
            return 0;
         }
      } else {
         switch(var1) {
         case 28672:
            return 2;
         default:
            return 0;
         }
      }
   }

   public static int getRawSupplementary(char var0, char var1) {
      return (var0 << 10) + var1 + -56613888;
   }

   public int getMaxValues(int var1) {
      switch(var1) {
      case 0:
         return this.m_maxBlockScriptValue_;
      case 2:
         return this.m_maxJTGValue_;
      default:
         return 0;
      }
   }

   public static final int getMask(int var0) {
      return 1 << var0;
   }

   public static int getEuropeanDigit(int var0) {
      if ((var0 <= 122 || var0 >= 65313) && var0 >= 65 && (var0 <= 90 || var0 >= 97) && var0 <= 65370 && (var0 <= 65338 || var0 >= 65345)) {
         if (var0 <= 122) {
            return var0 + 10 - (var0 <= 90 ? 65 : 97);
         } else {
            return var0 <= 65338 ? var0 + 10 - 'Ａ' : var0 + 10 - 'ａ';
         }
      } else {
         return -1;
      }
   }

   public int digit(int var1) {
      int var2 = getNumericTypeValue(this.getProperty(var1)) - 1;
      return var2 <= 9 ? var2 : -1;
   }

   public int getNumericValue(int var1) {
      int var2 = getNumericTypeValue(this.getProperty(var1));
      if (var2 == 0) {
         return getEuropeanDigit(var1);
      } else if (var2 < 11) {
         return var2 - 1;
      } else if (var2 < 21) {
         return var2 - 11;
      } else if (var2 < 176) {
         return var2 - 21;
      } else if (var2 < 480) {
         return -2;
      } else {
         int var3;
         int var4;
         if (var2 >= 768) {
            if (var2 < 804) {
               var3 = (var2 >> 2) - 191;
               var4 = (var2 & 3) + 1;
               switch(var4) {
               case 0:
               default:
                  break;
               case 1:
                  var3 *= 60;
                  break;
               case 2:
                  var3 *= 3600;
                  break;
               case 3:
                  var3 *= 216000;
                  break;
               case 4:
                  var3 *= 12960000;
               }

               return var3;
            } else {
               return -2;
            }
         } else {
            var3 = (var2 >> 5) - 14;
            var4 = (var2 & 31) + 2;
            if (var4 < 9 || var4 == 9 && var3 <= 2) {
               int var5 = var3;

               do {
                  var5 *= 10;
                  --var4;
               } while(var4 > 0);

               return var5;
            } else {
               return -2;
            }
         }
      }
   }

   public double getUnicodeNumericValue(int var1) {
      int var2 = getNumericTypeValue(this.getProperty(var1));
      if (var2 == 0) {
         return -1.23456789E8D;
      } else if (var2 < 11) {
         return (double)(var2 - 1);
      } else if (var2 < 21) {
         return (double)(var2 - 11);
      } else if (var2 < 176) {
         return (double)(var2 - 21);
      } else {
         int var4;
         int var7;
         if (var2 < 480) {
            var7 = (var2 >> 4) - 12;
            var4 = (var2 & 15) + 1;
            return (double)var7 / (double)var4;
         } else if (var2 >= 768) {
            if (var2 < 804) {
               var7 = (var2 >> 2) - 191;
               var4 = (var2 & 3) + 1;
               switch(var4) {
               case 0:
               default:
                  break;
               case 1:
                  var7 *= 60;
                  break;
               case 2:
                  var7 *= 3600;
                  break;
               case 3:
                  var7 *= 216000;
                  break;
               case 4:
                  var7 *= 12960000;
               }

               return (double)var7;
            } else {
               return -1.23456789E8D;
            }
         } else {
            int var5 = (var2 >> 5) - 14;
            int var6 = (var2 & 31) + 2;

            double var3;
            for(var3 = (double)var5; var6 >= 4; var6 -= 4) {
               var3 *= 10000.0D;
            }

            switch(var6) {
            case 0:
            default:
               break;
            case 1:
               var3 *= 10.0D;
               break;
            case 2:
               var3 *= 100.0D;
               break;
            case 3:
               var3 *= 1000.0D;
            }

            return var3;
         }
      }
   }

   private static final int getNumericTypeValue(int var0) {
      return var0 >> 6;
   }

   private static final int ntvGetType(int var0) {
      return var0 == 0 ? 0 : (var0 < 11 ? 1 : (var0 < 21 ? 2 : 3));
   }

   private UCharacterProperty() throws IOException {
      if (this.binProps.length != 57) {
         throw new RuntimeException("binProps.length!=UProperty.BINARY_LIMIT");
      } else if (this.intProps.length != 21) {
         throw new RuntimeException("intProps.length!=(UProperty.INT_LIMIT-UProperty.INT_START)");
      } else {
         InputStream var1 = ICUData.getRequiredStream("data/icudt51b/uprops.icu");
         BufferedInputStream var2 = new BufferedInputStream(var1, 25000);
         this.m_unicodeVersion_ = ICUBinary.readHeaderAndDataVersion(var2, DATA_FORMAT, new UCharacterProperty.IsAcceptable());
         DataInputStream var3 = new DataInputStream(var2);
         int var4 = var3.readInt();
         var3.readInt();
         var3.readInt();
         int var5 = var3.readInt();
         int var6 = var3.readInt();
         this.m_additionalColumnsCount_ = var3.readInt();
         int var7 = var3.readInt();
         int var8 = var3.readInt();
         var3.readInt();
         var3.readInt();
         this.m_maxBlockScriptValue_ = var3.readInt();
         this.m_maxJTGValue_ = var3.readInt();
         var3.skipBytes(16);
         this.m_trie_ = Trie2_16.createFromSerialized(var3);
         int var9 = (var4 - 16) * 4;
         int var10 = this.m_trie_.getSerializedLength();
         if (var10 > var9) {
            throw new IOException("uprops.icu: not enough bytes for main trie");
         } else {
            var3.skipBytes(var9 - var10);
            var3.skipBytes((var5 - var4) * 4);
            int var11;
            int var12;
            if (this.m_additionalColumnsCount_ > 0) {
               this.m_additionalTrie_ = Trie2_16.createFromSerialized(var3);
               var9 = (var6 - var5) * 4;
               var10 = this.m_additionalTrie_.getSerializedLength();
               if (var10 > var9) {
                  throw new IOException("uprops.icu: not enough bytes for additional-properties trie");
               }

               var3.skipBytes(var9 - var10);
               var11 = var7 - var6;
               this.m_additionalVectors_ = new int[var11];

               for(var12 = 0; var12 < var11; ++var12) {
                  this.m_additionalVectors_[var12] = var3.readInt();
               }
            }

            var11 = (var8 - var7) * 2;
            if (var11 > 0) {
               this.m_scriptExtensions_ = new char[var11];

               for(var12 = 0; var12 < var11; ++var12) {
                  this.m_scriptExtensions_[var12] = var3.readChar();
               }
            }

            var1.close();
         }
      }
   }

   public UnicodeSet addPropertyStarts(UnicodeSet var1) {
      Iterator var2 = this.m_trie_.iterator();

      Trie2.Range var3;
      while(var2.hasNext() && !(var3 = (Trie2.Range)var2.next()).leadSurrogate) {
         var1.add(var3.startCodePoint);
      }

      var1.add(9);
      var1.add(10);
      var1.add(14);
      var1.add(28);
      var1.add(32);
      var1.add(133);
      var1.add(134);
      var1.add(127);
      var1.add(8202);
      var1.add(8208);
      var1.add(8298);
      var1.add(8304);
      var1.add(65279);
      var1.add(65280);
      var1.add(160);
      var1.add(161);
      var1.add(8199);
      var1.add(8200);
      var1.add(8239);
      var1.add(8240);
      var1.add(12295);
      var1.add(12296);
      var1.add(19968);
      var1.add(19969);
      var1.add(20108);
      var1.add(20109);
      var1.add(19977);
      var1.add(19978);
      var1.add(22235);
      var1.add(22236);
      var1.add(20116);
      var1.add(20117);
      var1.add(20845);
      var1.add(20846);
      var1.add(19971);
      var1.add(19972);
      var1.add(20843);
      var1.add(20844);
      var1.add(20061);
      var1.add(20062);
      var1.add(97);
      var1.add(123);
      var1.add(65);
      var1.add(91);
      var1.add(65345);
      var1.add(65371);
      var1.add(65313);
      var1.add(65339);
      var1.add(103);
      var1.add(71);
      var1.add(65351);
      var1.add(65319);
      var1.add(8288);
      var1.add(65520);
      var1.add(65532);
      var1.add(917504);
      var1.add(921600);
      var1.add(847);
      var1.add(848);
      return var1;
   }

   public void upropsvec_addPropertyStarts(UnicodeSet var1) {
      if (this.m_additionalColumnsCount_ > 0) {
         Iterator var2 = this.m_additionalTrie_.iterator();

         Trie2.Range var3;
         while(var2.hasNext() && !(var3 = (Trie2.Range)var2.next()).leadSurrogate) {
            var1.add(var3.startCodePoint);
         }
      }

   }

   static boolean access$000(int var0) {
      return isgraphPOSIX(var0);
   }

   static int access$100(int var0) {
      return getNumericTypeValue(var0);
   }

   static int access$200(int var0) {
      return ntvGetType(var0);
   }

   static int[] access$300() {
      return gcbToHst;
   }

   static {
      GC_Z_MASK = GC_ZS_MASK | GC_ZL_MASK | GC_ZP_MASK;
      gcbToHst = new int[]{0, 0, 0, 0, 1, 0, 4, 5, 3, 2};
      DATA_FORMAT = new byte[]{85, 80, 114, 111};

      try {
         INSTANCE = new UCharacterProperty();
      } catch (IOException var1) {
         throw new MissingResourceException(var1.getMessage(), "", "");
      }
   }

   private static final class IsAcceptable implements ICUBinary.Authenticate {
      private IsAcceptable() {
      }

      public boolean isDataVersionAcceptable(byte[] var1) {
         return var1[0] == 7;
      }

      IsAcceptable(Object var1) {
         this();
      }
   }

   private class NormQuickCheckIntProperty extends UCharacterProperty.IntProperty {
      int which;
      int max;
      final UCharacterProperty this$0;

      NormQuickCheckIntProperty(UCharacterProperty var1, int var2, int var3, int var4) {
         super(var1, var2);
         this.this$0 = var1;
         this.which = var3;
         this.max = var4;
      }

      int getValue(int var1) {
         return Norm2AllModes.getN2WithImpl(this.which - 4108).getQuickCheck(var1);
      }

      int getMaxValue(int var1) {
         return this.max;
      }
   }

   private class CombiningClassIntProperty extends UCharacterProperty.IntProperty {
      final UCharacterProperty this$0;

      CombiningClassIntProperty(UCharacterProperty var1, int var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      int getMaxValue(int var1) {
         return 255;
      }
   }

   private class BiDiIntProperty extends UCharacterProperty.IntProperty {
      final UCharacterProperty this$0;

      BiDiIntProperty(UCharacterProperty var1) {
         super(var1, 5);
         this.this$0 = var1;
      }

      int getMaxValue(int var1) {
         return UBiDiProps.INSTANCE.getMaxValue(var1);
      }
   }

   private class IntProperty {
      int column;
      int mask;
      int shift;
      final UCharacterProperty this$0;

      IntProperty(UCharacterProperty var1, int var2, int var3, int var4) {
         this.this$0 = var1;
         this.column = var2;
         this.mask = var3;
         this.shift = var4;
      }

      IntProperty(UCharacterProperty var1, int var2) {
         this.this$0 = var1;
         this.column = var2;
         this.mask = 0;
      }

      final int getSource() {
         return this.mask == 0 ? this.column : 2;
      }

      int getValue(int var1) {
         return (this.this$0.getAdditional(var1, this.column) & this.mask) >>> this.shift;
      }

      int getMaxValue(int var1) {
         return (this.this$0.getMaxValues(this.column) & this.mask) >>> this.shift;
      }
   }

   private class NormInertBinaryProperty extends UCharacterProperty.BinaryProperty {
      int which;
      final UCharacterProperty this$0;

      NormInertBinaryProperty(UCharacterProperty var1, int var2, int var3) {
         super(var1, var2);
         this.this$0 = var1;
         this.which = var3;
      }

      boolean contains(int var1) {
         return Norm2AllModes.getN2WithImpl(this.which - 37).isInert(var1);
      }
   }

   private class CaseBinaryProperty extends UCharacterProperty.BinaryProperty {
      int which;
      final UCharacterProperty this$0;

      CaseBinaryProperty(UCharacterProperty var1, int var2) {
         super(var1, 4);
         this.this$0 = var1;
         this.which = var2;
      }

      boolean contains(int var1) {
         return UCaseProps.INSTANCE.hasBinaryProperty(var1, this.which);
      }
   }

   private class BinaryProperty {
      int column;
      int mask;
      final UCharacterProperty this$0;

      BinaryProperty(UCharacterProperty var1, int var2, int var3) {
         this.this$0 = var1;
         this.column = var2;
         this.mask = var3;
      }

      BinaryProperty(UCharacterProperty var1, int var2) {
         this.this$0 = var1;
         this.column = var2;
         this.mask = 0;
      }

      final int getSource() {
         return this.mask == 0 ? this.column : 2;
      }

      boolean contains(int var1) {
         return (this.this$0.getAdditional(var1, this.column) & this.mask) != 0;
      }
   }
}
