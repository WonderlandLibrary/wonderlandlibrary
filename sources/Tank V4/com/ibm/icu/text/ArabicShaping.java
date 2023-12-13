package com.ibm.icu.text;

import com.ibm.icu.impl.UBiDiProps;

public final class ArabicShaping {
   private final int options;
   private boolean isLogical;
   private boolean spacesRelativeToTextBeginEnd;
   private char tailChar;
   public static final int SEEN_TWOCELL_NEAR = 2097152;
   public static final int SEEN_MASK = 7340032;
   public static final int YEHHAMZA_TWOCELL_NEAR = 16777216;
   public static final int YEHHAMZA_MASK = 58720256;
   public static final int TASHKEEL_BEGIN = 262144;
   public static final int TASHKEEL_END = 393216;
   public static final int TASHKEEL_RESIZE = 524288;
   public static final int TASHKEEL_REPLACE_BY_TATWEEL = 786432;
   public static final int TASHKEEL_MASK = 917504;
   public static final int SPACES_RELATIVE_TO_TEXT_BEGIN_END = 67108864;
   public static final int SPACES_RELATIVE_TO_TEXT_MASK = 67108864;
   public static final int SHAPE_TAIL_NEW_UNICODE = 134217728;
   public static final int SHAPE_TAIL_TYPE_MASK = 134217728;
   public static final int LENGTH_GROW_SHRINK = 0;
   public static final int LAMALEF_RESIZE = 0;
   public static final int LENGTH_FIXED_SPACES_NEAR = 1;
   public static final int LAMALEF_NEAR = 1;
   public static final int LENGTH_FIXED_SPACES_AT_END = 2;
   public static final int LAMALEF_END = 2;
   public static final int LENGTH_FIXED_SPACES_AT_BEGINNING = 3;
   public static final int LAMALEF_BEGIN = 3;
   public static final int LAMALEF_AUTO = 65536;
   public static final int LENGTH_MASK = 65539;
   public static final int LAMALEF_MASK = 65539;
   public static final int TEXT_DIRECTION_LOGICAL = 0;
   public static final int TEXT_DIRECTION_VISUAL_RTL = 0;
   public static final int TEXT_DIRECTION_VISUAL_LTR = 4;
   public static final int TEXT_DIRECTION_MASK = 4;
   public static final int LETTERS_NOOP = 0;
   public static final int LETTERS_SHAPE = 8;
   public static final int LETTERS_UNSHAPE = 16;
   public static final int LETTERS_SHAPE_TASHKEEL_ISOLATED = 24;
   public static final int LETTERS_MASK = 24;
   public static final int DIGITS_NOOP = 0;
   public static final int DIGITS_EN2AN = 32;
   public static final int DIGITS_AN2EN = 64;
   public static final int DIGITS_EN2AN_INIT_LR = 96;
   public static final int DIGITS_EN2AN_INIT_AL = 128;
   public static final int DIGITS_MASK = 224;
   public static final int DIGIT_TYPE_AN = 0;
   public static final int DIGIT_TYPE_AN_EXTENDED = 256;
   public static final int DIGIT_TYPE_MASK = 256;
   private static final char HAMZAFE_CHAR = 'ﺀ';
   private static final char HAMZA06_CHAR = 'ء';
   private static final char YEH_HAMZA_CHAR = 'ئ';
   private static final char YEH_HAMZAFE_CHAR = 'ﺉ';
   private static final char LAMALEF_SPACE_SUB = '\uffff';
   private static final char TASHKEEL_SPACE_SUB = '\ufffe';
   private static final char LAM_CHAR = 'ل';
   private static final char SPACE_CHAR = ' ';
   private static final char SHADDA_CHAR = 'ﹼ';
   private static final char SHADDA06_CHAR = 'ّ';
   private static final char TATWEEL_CHAR = 'ـ';
   private static final char SHADDA_TATWEEL_CHAR = 'ﹽ';
   private static final char NEW_TAIL_CHAR = 'ﹳ';
   private static final char OLD_TAIL_CHAR = '\u200b';
   private static final int SHAPE_MODE = 0;
   private static final int DESHAPE_MODE = 1;
   private static final int IRRELEVANT = 4;
   private static final int LAMTYPE = 16;
   private static final int ALEFTYPE = 32;
   private static final int LINKR = 1;
   private static final int LINKL = 2;
   private static final int LINK_MASK = 3;
   private static final int[] irrelevantPos = new int[]{0, 2, 4, 6, 8, 10, 12, 14};
   private static final int[] tailFamilyIsolatedFinal = new int[]{1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1};
   private static final int[] tashkeelMedial = new int[]{0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
   private static final char[] yehHamzaToYeh = new char[]{'ﻯ', 'ﻰ'};
   private static final char[] convertNormalizedLamAlef = new char[]{'آ', 'أ', 'إ', 'ا'};
   private static final int[] araLink = new int[]{4385, 4897, 5377, 5921, 6403, 7457, 7939, 8961, 9475, 10499, 11523, 12547, 13571, 14593, 15105, 15617, 16129, 16643, 17667, 18691, 19715, 20739, 21763, 22787, 23811, 0, 0, 0, 0, 0, 3, 24835, 25859, 26883, 27923, 28931, 29955, 30979, 32001, 32513, 33027, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 34049, 34561, 35073, 35585, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 33, 33, 0, 33, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 3, 3, 1, 1};
   private static final int[] presLink = new int[]{3, 3, 3, 0, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 32, 33, 32, 33, 0, 1, 32, 33, 0, 2, 3, 1, 32, 33, 0, 2, 3, 1, 0, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 16, 18, 19, 17, 0, 2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 1};
   private static int[] convertFEto06 = new int[]{1611, 1611, 1612, 1612, 1613, 1613, 1614, 1614, 1615, 1615, 1616, 1616, 1617, 1617, 1618, 1618, 1569, 1570, 1570, 1571, 1571, 1572, 1572, 1573, 1573, 1574, 1574, 1574, 1574, 1575, 1575, 1576, 1576, 1576, 1576, 1577, 1577, 1578, 1578, 1578, 1578, 1579, 1579, 1579, 1579, 1580, 1580, 1580, 1580, 1581, 1581, 1581, 1581, 1582, 1582, 1582, 1582, 1583, 1583, 1584, 1584, 1585, 1585, 1586, 1586, 1587, 1587, 1587, 1587, 1588, 1588, 1588, 1588, 1589, 1589, 1589, 1589, 1590, 1590, 1590, 1590, 1591, 1591, 1591, 1591, 1592, 1592, 1592, 1592, 1593, 1593, 1593, 1593, 1594, 1594, 1594, 1594, 1601, 1601, 1601, 1601, 1602, 1602, 1602, 1602, 1603, 1603, 1603, 1603, 1604, 1604, 1604, 1604, 1605, 1605, 1605, 1605, 1606, 1606, 1606, 1606, 1607, 1607, 1607, 1607, 1608, 1608, 1609, 1609, 1610, 1610, 1610, 1610, 1628, 1628, 1629, 1629, 1630, 1630, 1631, 1631};
   private static final int[][][] shapeTable = new int[][][]{{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 1, 0, 3}, {0, 1, 0, 1}}, {{0, 0, 2, 2}, {0, 0, 1, 2}, {0, 1, 1, 2}, {0, 1, 1, 3}}, {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 1, 0, 3}, {0, 1, 0, 3}}, {{0, 0, 1, 2}, {0, 0, 1, 2}, {0, 1, 1, 2}, {0, 1, 1, 3}}};

   public int shape(char[] var1, int var2, int var3, char[] var4, int var5, int var6) throws ArabicShapingException {
      if (var1 == null) {
         throw new IllegalArgumentException("source can not be null");
      } else if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length) {
         if (var4 == null && var6 != 0) {
            throw new IllegalArgumentException("null dest requires destSize == 0");
         } else if (var6 != 0 && (var5 < 0 || var6 < 0 || var5 + var6 > var4.length)) {
            throw new IllegalArgumentException("bad dest start (" + var5 + ") or size (" + var6 + ") for buffer of length " + var4.length);
         } else if ((this.options & 917504) > 0 && (this.options & 917504) != 262144 && (this.options & 917504) != 393216 && (this.options & 917504) != 524288 && (this.options & 917504) != 786432) {
            throw new IllegalArgumentException("Wrong Tashkeel argument");
         } else if ((this.options & 65539) > 0 && (this.options & 65539) != 3 && (this.options & 65539) != 2 && (this.options & 65539) != 0 && (this.options & 65539) != 65536 && (this.options & 65539) != 1) {
            throw new IllegalArgumentException("Wrong Lam Alef argument");
         } else if ((this.options & 917504) > 0 && (this.options & 24) == 16) {
            throw new IllegalArgumentException("Tashkeel replacement should not be enabled in deshaping mode ");
         } else {
            return this.internalShape(var1, var2, var3, var4, var5, var6);
         }
      } else {
         throw new IllegalArgumentException("bad source start (" + var2 + ") or length (" + var3 + ") for buffer of length " + var1.length);
      }
   }

   public void shape(char[] var1, int var2, int var3) throws ArabicShapingException {
      if ((this.options & 65539) == 0) {
         throw new ArabicShapingException("Cannot shape in place with length option resize.");
      } else {
         this.shape(var1, var2, var3, var1, var2, var3);
      }
   }

   public String shape(String var1) throws ArabicShapingException {
      char[] var2 = var1.toCharArray();
      char[] var3 = var2;
      if ((this.options & 65539) == 0 && (this.options & 24) == 16) {
         var3 = new char[var2.length * 2];
      }

      int var4 = this.shape(var2, 0, var2.length, var3, 0, var3.length);
      return new String(var3, 0, var4);
   }

   public ArabicShaping(int var1) {
      this.options = var1;
      if ((var1 & 224) > 128) {
         throw new IllegalArgumentException("bad DIGITS options");
      } else {
         this.isLogical = (var1 & 4) == 0;
         this.spacesRelativeToTextBeginEnd = (var1 & 67108864) == 67108864;
         if ((var1 & 134217728) == 134217728) {
            this.tailChar = 'ﹳ';
         } else {
            this.tailChar = 8203;
         }

      }
   }

   public boolean equals(Object var1) {
      return var1 != null && var1.getClass() == ArabicShaping.class && this.options == ((ArabicShaping)var1).options;
   }

   public int hashCode() {
      return this.options;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.append('[');
      switch(this.options & 65539) {
      case 0:
         var1.append("LamAlef resize");
         break;
      case 1:
         var1.append("LamAlef spaces at near");
         break;
      case 2:
         var1.append("LamAlef spaces at end");
         break;
      case 3:
         var1.append("LamAlef spaces at begin");
         break;
      case 65536:
         var1.append("lamAlef auto");
      }

      switch(this.options & 4) {
      case 0:
         var1.append(", logical");
         break;
      case 4:
         var1.append(", visual");
      }

      switch(this.options & 24) {
      case 0:
         var1.append(", no letter shaping");
         break;
      case 8:
         var1.append(", shape letters");
         break;
      case 16:
         var1.append(", unshape letters");
         break;
      case 24:
         var1.append(", shape letters tashkeel isolated");
      }

      switch(this.options & 7340032) {
      case 2097152:
         var1.append(", Seen at near");
      default:
         switch(this.options & 58720256) {
         case 16777216:
            var1.append(", Yeh Hamza at near");
         default:
            switch(this.options & 917504) {
            case 262144:
               var1.append(", Tashkeel at begin");
               break;
            case 393216:
               var1.append(", Tashkeel at end");
               break;
            case 524288:
               var1.append(", Tashkeel resize");
               break;
            case 786432:
               var1.append(", Tashkeel replace with tatweel");
            }

            switch(this.options & 224) {
            case 0:
               var1.append(", no digit shaping");
               break;
            case 32:
               var1.append(", shape digits to AN");
               break;
            case 64:
               var1.append(", shape digits to EN");
               break;
            case 96:
               var1.append(", shape digits to AN contextually: default EN");
               break;
            case 128:
               var1.append(", shape digits to AN contextually: default AL");
            }

            switch(this.options & 256) {
            case 0:
               var1.append(", standard Arabic-Indic digits");
               break;
            case 256:
               var1.append(", extended Arabic-Indic digits");
            }

            var1.append("]");
            return var1.toString();
         }
      }
   }

   private void shapeToArabicDigitsWithContext(char[] var1, int var2, int var3, char var4, boolean var5) {
      UBiDiProps var6 = UBiDiProps.INSTANCE;
      var4 = (char)(var4 - 48);
      int var7 = var2 + var3;

      while(true) {
         --var7;
         if (var7 < var2) {
            return;
         }

         char var8 = var1[var7];
         switch(var6.getClass(var8)) {
         case 0:
         case 1:
            var5 = false;
            break;
         case 2:
            if (var5 && var8 <= '9') {
               var1[var7] = (char)(var8 + var4);
            }
            break;
         case 13:
            var5 = true;
         }
      }
   }

   private static void invertBuffer(char[] var0, int var1, int var2) {
      int var3 = var1;

      for(int var4 = var1 + var2 - 1; var3 < var4; --var4) {
         char var5 = var0[var3];
         var0[var3] = var0[var4];
         var0[var4] = var5;
         ++var3;
      }

   }

   private static char changeLamAlef(char var0) {
      switch(var0) {
      case 'آ':
         return 'ٜ';
      case 'أ':
         return 'ٝ';
      case 'ؤ':
      case 'ئ':
      default:
         return '\u0000';
      case 'إ':
         return 'ٞ';
      case 'ا':
         return 'ٟ';
      }
   }

   private static int specialChar(char var0) {
      if ((var0 <= 1569 || var0 >= 1574) && var0 != 1575 && (var0 <= 1582 || var0 >= 1587) && (var0 <= 1607 || var0 >= 1610) && var0 != 1577) {
         if (var0 >= 1611 && var0 <= 1618) {
            return 2;
         } else {
            return (var0 < 1619 || var0 > 1621) && var0 != 1648 && (var0 < 'ﹰ' || var0 > 'ﹿ') ? 0 : 3;
         }
      } else {
         return 1;
      }
   }

   private static int getLink(char var0) {
      if (var0 >= 1570 && var0 <= 1747) {
         return araLink[var0 - 1570];
      } else if (var0 == 8205) {
         return 3;
      } else if (var0 >= 8301 && var0 <= 8303) {
         return 4;
      } else {
         return var0 >= 'ﹰ' && var0 <= 'ﻼ' ? presLink[var0 - 'ﹰ'] : 0;
      }
   }

   private static int countSpacesLeft(char[] var0, int var1, int var2) {
      int var3 = var1;

      for(int var4 = var1 + var2; var3 < var4; ++var3) {
         if (var0[var3] != ' ') {
            return var3 - var1;
         }
      }

      return var2;
   }

   private static int countSpacesRight(char[] var0, int var1, int var2) {
      int var3 = var1 + var2;

      do {
         --var3;
         if (var3 < var1) {
            return var2;
         }
      } while(var0[var3] == ' ');

      return var1 + var2 - 1 - var3;
   }

   private static int isSeenTailFamilyChar(char var0) {
      return var0 >= 'ﺱ' && var0 < 'ﺿ' ? tailFamilyIsolatedFinal[var0 - 'ﺱ'] : 0;
   }

   private static int isSeenFamilyChar(char var0) {
      return var0 >= 1587 && var0 <= 1590 ? 1 : 0;
   }

   private static int isTashkeelOnTatweelChar(char var0) {
      if (var0 >= 'ﹰ' && var0 <= 'ﹿ' && var0 != 'ﹳ' && var0 != '\ufe75' && var0 != 'ﹽ') {
         return tashkeelMedial[var0 - 'ﹰ'];
      } else {
         return (var0 < 'ﳲ' || var0 > 'ﳴ') && var0 != 'ﹽ' ? 0 : 2;
      }
   }

   private static int isIsolatedTashkeelChar(char var0) {
      if (var0 >= 'ﹰ' && var0 <= 'ﹿ' && var0 != 'ﹳ' && var0 != '\ufe75') {
         return 1 - tashkeelMedial[var0 - 'ﹰ'];
      } else {
         return var0 >= 'ﱞ' && var0 <= 'ﱣ' ? 1 : 0;
      }
   }

   private int calculateSize(char[] param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private static int countSpaceSub(char[] var0, int var1, char var2) {
      int var3 = 0;

      int var4;
      for(var4 = 0; var3 < var1; ++var3) {
         if (var0[var3] == var2) {
            ++var4;
         }
      }

      return var4;
   }

   private static void shiftArray(char[] var0, int var1, int var2, char var3) {
      int var4 = var2;
      int var5 = var2;

      while(true) {
         --var5;
         if (var5 < var1) {
            return;
         }

         char var6 = var0[var5];
         if (var6 != var3) {
            --var4;
            if (var4 != var5) {
               var0[var4] = var6;
            }
         }
      }
   }

   private static int flipArray(char[] var0, int var1, int var2, int var3) {
      if (var3 > var1) {
         int var4 = var3;

         for(var3 = var1; var4 < var2; var0[var3++] = var0[var4++]) {
         }
      } else {
         var3 = var2;
      }

      return var3;
   }

   private static int handleTashkeelWithTatweel(char[] var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         if (isTashkeelOnTatweelChar(var0[var2]) == 1) {
            var0[var2] = 1600;
         } else if (isTashkeelOnTatweelChar(var0[var2]) == 2) {
            var0[var2] = 'ﹽ';
         } else if (isIsolatedTashkeelChar(var0[var2]) == 1 && var0[var2] != 'ﹼ') {
            var0[var2] = ' ';
         }
      }

      return var1;
   }

   private int handleGeneratedSpaces(char[] var1, int var2, int var3) {
      int var4 = this.options & 65539;
      int var5 = this.options & 917504;
      boolean var6 = false;
      boolean var7 = false;
      if (!this.isLogical & !this.spacesRelativeToTextBeginEnd) {
         switch(var4) {
         case 2:
            var4 = 3;
            break;
         case 3:
            var4 = 2;
         }

         switch(var5) {
         case 262144:
            var5 = 393216;
            break;
         case 393216:
            var5 = 262144;
         }
      }

      int var8;
      int var9;
      if (var4 == 1) {
         var8 = var2;

         for(var9 = var2 + var3; var8 < var9; ++var8) {
            if (var1[var8] == '\uffff') {
               var1[var8] = ' ';
            }
         }
      } else {
         var8 = var2 + var3;
         var9 = countSpaceSub(var1, var3, '\uffff');
         int var10 = countSpaceSub(var1, var3, '\ufffe');
         if (var4 == 2) {
            var6 = true;
         }

         if (var5 == 393216) {
            var7 = true;
         }

         if (var6 && var4 == 2) {
            shiftArray(var1, var2, var8, '\uffff');

            while(var9 > var2) {
               --var9;
               var1[var9] = ' ';
            }
         }

         if (var7 && var5 == 393216) {
            shiftArray(var1, var2, var8, '\ufffe');

            while(var10 > var2) {
               --var10;
               var1[var10] = ' ';
            }
         }

         var6 = false;
         var7 = false;
         if (var4 == 0) {
            var6 = true;
         }

         if (var5 == 524288) {
            var7 = true;
         }

         if (var6 && var4 == 0) {
            shiftArray(var1, var2, var8, '\uffff');
            var9 = flipArray(var1, var2, var8, var9);
            var3 = var9 - var2;
         }

         if (var7 && var5 == 524288) {
            shiftArray(var1, var2, var8, '\ufffe');
            var10 = flipArray(var1, var2, var8, var10);
            var3 = var10 - var2;
         }

         var6 = false;
         var7 = false;
         if (var4 == 3 || var4 == 65536) {
            var6 = true;
         }

         if (var5 == 262144) {
            var7 = true;
         }

         if (var6 && (var4 == 3 || var4 == 65536)) {
            shiftArray(var1, var2, var8, '\uffff');

            for(var9 = flipArray(var1, var2, var8, var9); var9 < var8; var1[var9++] = ' ') {
            }
         }

         if (var7 && var5 == 262144) {
            shiftArray(var1, var2, var8, '\ufffe');

            for(var10 = flipArray(var1, var2, var8, var10); var10 < var8; var1[var10++] = ' ') {
            }
         }
      }

      return var3;
   }

   private boolean expandCompositCharAtBegin(char[] param1, int param2, int param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   private boolean expandCompositCharAtEnd(char[] param1, int param2, int param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   private boolean expandCompositCharAtNear(char[] param1, int param2, int param3, int param4, int param5, int param6) {
      // $FF: Couldn't be decompiled
   }

   private int expandCompositChar(char[] param1, int param2, int param3, int param4, int param5) throws ArabicShapingException {
      // $FF: Couldn't be decompiled
   }

   private int normalize(char[] param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private int deshapeNormalize(char[] param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private int shapeUnicode(char[] param1, int param2, int param3, int param4, int param5) throws ArabicShapingException {
      // $FF: Couldn't be decompiled
   }

   private int deShapeUnicode(char[] var1, int var2, int var3, int var4) throws ArabicShapingException {
      int var5 = this.deshapeNormalize(var1, var2, var3);
      if (var5 != 0) {
         var4 = this.expandCompositChar(var1, var2, var3, var5, 1);
      } else {
         var4 = var3;
      }

      return var4;
   }

   private int internalShape(char[] var1, int var2, int var3, char[] var4, int var5, int var6) throws ArabicShapingException {
      if (var3 == 0) {
         return 0;
      } else if (var6 == 0) {
         return (this.options & 24) != 0 && (this.options & 65539) == 0 ? this.calculateSize(var1, var2, var3) : var3;
      } else {
         char[] var7 = new char[var3 * 2];
         System.arraycopy(var1, var2, var7, 0, var3);
         if (this.isLogical) {
            invertBuffer(var7, 0, var3);
         }

         int var8 = var3;
         switch(this.options & 24) {
         case 8:
            if ((this.options & 917504) > 0 && (this.options & 917504) != 786432) {
               var8 = this.shapeUnicode(var7, 0, var3, var6, 2);
            } else {
               var8 = this.shapeUnicode(var7, 0, var3, var6, 0);
               if ((this.options & 917504) == 786432) {
                  var8 = handleTashkeelWithTatweel(var7, var3);
               }
            }
            break;
         case 16:
            var8 = this.deShapeUnicode(var7, 0, var3, var6);
            break;
         case 24:
            var8 = this.shapeUnicode(var7, 0, var3, var6, 1);
         }

         if (var8 > var6) {
            throw new ArabicShapingException("not enough room for result data");
         } else {
            if ((this.options & 224) != 0) {
               char var9 = '0';
               switch(this.options & 256) {
               case 0:
                  var9 = 1632;
                  break;
               case 256:
                  var9 = 1776;
               }

               int var11;
               label75:
               switch(this.options & 224) {
               case 32:
                  int var14 = var9 - 48;
                  var11 = 0;

                  while(true) {
                     if (var11 >= var8) {
                        break label75;
                     }

                     char var15 = var7[var11];
                     if (var15 <= '9' && var15 >= '0') {
                        var7[var11] = (char)(var7[var11] + var14);
                     }

                     ++var11;
                  }
               case 64:
                  char var10 = (char)(var9 + 9);
                  var11 = 48 - var9;
                  int var12 = 0;

                  while(true) {
                     if (var12 >= var8) {
                        break label75;
                     }

                     char var13 = var7[var12];
                     if (var13 <= var10 && var13 >= var9) {
                        var7[var12] = (char)(var7[var12] + var11);
                     }

                     ++var12;
                  }
               case 96:
                  this.shapeToArabicDigitsWithContext(var7, 0, var8, var9, false);
                  break;
               case 128:
                  this.shapeToArabicDigitsWithContext(var7, 0, var8, var9, true);
               }
            }

            if (this.isLogical) {
               invertBuffer(var7, 0, var8);
            }

            System.arraycopy(var7, 0, var4, var5, var8);
            return var8;
         }
      }
   }
}
