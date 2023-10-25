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
  
  private static final char LAMALEF_SPACE_SUB = '￿';
  
  private static final char TASHKEEL_SPACE_SUB = '￾';
  
  private static final char LAM_CHAR = 'ل';
  
  private static final char SPACE_CHAR = ' ';
  
  private static final char SHADDA_CHAR = 'ﹼ';
  
  private static final char SHADDA06_CHAR = 'ّ';
  
  private static final char TATWEEL_CHAR = 'ـ';
  
  private static final char SHADDA_TATWEEL_CHAR = 'ﹽ';
  
  private static final char NEW_TAIL_CHAR = 'ﹳ';
  
  private static final char OLD_TAIL_CHAR = '​';
  
  private static final int SHAPE_MODE = 0;
  
  private static final int DESHAPE_MODE = 1;
  
  private static final int IRRELEVANT = 4;
  
  private static final int LAMTYPE = 16;
  
  private static final int ALEFTYPE = 32;
  
  private static final int LINKR = 1;
  
  private static final int LINKL = 2;
  
  private static final int LINK_MASK = 3;
  
  public int shape(char[] source, int sourceStart, int sourceLength, char[] dest, int destStart, int destSize) throws ArabicShapingException {
    if (source == null)
      throw new IllegalArgumentException("source can not be null"); 
    if (sourceStart < 0 || sourceLength < 0 || sourceStart + sourceLength > source.length)
      throw new IllegalArgumentException("bad source start (" + sourceStart + ") or length (" + sourceLength + ") for buffer of length " + source.length); 
    if (dest == null && destSize != 0)
      throw new IllegalArgumentException("null dest requires destSize == 0"); 
    if (destSize != 0 && (destStart < 0 || destSize < 0 || destStart + destSize > dest.length))
      throw new IllegalArgumentException("bad dest start (" + destStart + ") or size (" + destSize + ") for buffer of length " + dest.length); 
    if ((this.options & 0xE0000) > 0 && (this.options & 0xE0000) != 262144 && (this.options & 0xE0000) != 393216 && (this.options & 0xE0000) != 524288 && (this.options & 0xE0000) != 786432)
      throw new IllegalArgumentException("Wrong Tashkeel argument"); 
    if ((this.options & 0x10003) > 0 && (this.options & 0x10003) != 3 && (this.options & 0x10003) != 2 && (this.options & 0x10003) != 0 && (this.options & 0x10003) != 65536 && (this.options & 0x10003) != 1)
      throw new IllegalArgumentException("Wrong Lam Alef argument"); 
    if ((this.options & 0xE0000) > 0 && (this.options & 0x18) == 16)
      throw new IllegalArgumentException("Tashkeel replacement should not be enabled in deshaping mode "); 
    return internalShape(source, sourceStart, sourceLength, dest, destStart, destSize);
  }
  
  public void shape(char[] source, int start, int length) throws ArabicShapingException {
    if ((this.options & 0x10003) == 0)
      throw new ArabicShapingException("Cannot shape in place with length option resize."); 
    shape(source, start, length, source, start, length);
  }
  
  public String shape(String text) throws ArabicShapingException {
    char[] src = text.toCharArray();
    char[] dest = src;
    if ((this.options & 0x10003) == 0 && (this.options & 0x18) == 16)
      dest = new char[src.length * 2]; 
    int len = shape(src, 0, src.length, dest, 0, dest.length);
    return new String(dest, 0, len);
  }
  
  public ArabicShaping(int options) {
    this.options = options;
    if ((options & 0xE0) > 128)
      throw new IllegalArgumentException("bad DIGITS options"); 
    this.isLogical = ((options & 0x4) == 0);
    this.spacesRelativeToTextBeginEnd = ((options & 0x4000000) == 67108864);
    if ((options & 0x8000000) == 134217728) {
      this.tailChar = 'ﹳ';
    } else {
      this.tailChar = '​';
    } 
  }
  
  public boolean equals(Object rhs) {
    return (rhs != null && rhs.getClass() == ArabicShaping.class && this.options == ((ArabicShaping)rhs).options);
  }
  
  public int hashCode() {
    return this.options;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder(super.toString());
    buf.append('[');
    switch (this.options & 0x10003) {
      case 0:
        buf.append("LamAlef resize");
        break;
      case 1:
        buf.append("LamAlef spaces at near");
        break;
      case 3:
        buf.append("LamAlef spaces at begin");
        break;
      case 2:
        buf.append("LamAlef spaces at end");
        break;
      case 65536:
        buf.append("lamAlef auto");
        break;
    } 
    switch (this.options & 0x4) {
      case 0:
        buf.append(", logical");
        break;
      case 4:
        buf.append(", visual");
        break;
    } 
    switch (this.options & 0x18) {
      case 0:
        buf.append(", no letter shaping");
        break;
      case 8:
        buf.append(", shape letters");
        break;
      case 24:
        buf.append(", shape letters tashkeel isolated");
        break;
      case 16:
        buf.append(", unshape letters");
        break;
    } 
    switch (this.options & 0x700000) {
      case 2097152:
        buf.append(", Seen at near");
        break;
    } 
    switch (this.options & 0x3800000) {
      case 16777216:
        buf.append(", Yeh Hamza at near");
        break;
    } 
    switch (this.options & 0xE0000) {
      case 262144:
        buf.append(", Tashkeel at begin");
        break;
      case 393216:
        buf.append(", Tashkeel at end");
        break;
      case 786432:
        buf.append(", Tashkeel replace with tatweel");
        break;
      case 524288:
        buf.append(", Tashkeel resize");
        break;
    } 
    switch (this.options & 0xE0) {
      case 0:
        buf.append(", no digit shaping");
        break;
      case 32:
        buf.append(", shape digits to AN");
        break;
      case 64:
        buf.append(", shape digits to EN");
        break;
      case 96:
        buf.append(", shape digits to AN contextually: default EN");
        break;
      case 128:
        buf.append(", shape digits to AN contextually: default AL");
        break;
    } 
    switch (this.options & 0x100) {
      case 0:
        buf.append(", standard Arabic-Indic digits");
        break;
      case 256:
        buf.append(", extended Arabic-Indic digits");
        break;
    } 
    buf.append("]");
    return buf.toString();
  }
  
  private static final int[] irrelevantPos = new int[] { 0, 2, 4, 6, 8, 10, 12, 14 };
  
  private static final int[] tailFamilyIsolatedFinal = new int[] { 
      1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 
      0, 0, 1, 1 };
  
  private static final int[] tashkeelMedial = new int[] { 
      0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 
      0, 1, 0, 1, 0, 1 };
  
  private static final char[] yehHamzaToYeh = new char[] { 'ﻯ', 'ﻰ' };
  
  private static final char[] convertNormalizedLamAlef = new char[] { 'آ', 'أ', 'إ', 'ا' };
  
  private static final int[] araLink = new int[] { 
      4385, 4897, 5377, 5921, 6403, 7457, 7939, 8961, 9475, 10499, 
      11523, 12547, 13571, 14593, 15105, 15617, 16129, 16643, 17667, 18691, 
      19715, 20739, 21763, 22787, 23811, 0, 0, 0, 0, 0, 
      3, 24835, 25859, 26883, 27923, 28931, 29955, 30979, 32001, 32513, 
      33027, 4, 4, 4, 4, 4, 4, 4, 4, 4, 
      4, 4, 0, 0, 0, 0, 0, 0, 34049, 34561, 
      35073, 35585, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 
      33, 33, 0, 33, 1, 1, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
      3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      3, 1, 3, 3, 3, 3, 1, 1 };
  
  private static final int[] presLink = new int[] { 
      3, 3, 3, 0, 3, 0, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 0, 32, 33, 32, 
      33, 0, 1, 32, 33, 0, 2, 3, 1, 32, 
      33, 0, 2, 3, 1, 0, 1, 0, 2, 3, 
      1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 
      2, 3, 1, 0, 2, 3, 1, 0, 1, 0, 
      1, 0, 1, 0, 1, 0, 2, 3, 1, 0, 
      2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 
      1, 0, 2, 3, 1, 0, 2, 3, 1, 0, 
      2, 3, 1, 0, 2, 3, 1, 0, 2, 3, 
      1, 0, 2, 3, 1, 0, 2, 3, 1, 16, 
      18, 19, 17, 0, 2, 3, 1, 0, 2, 3, 
      1, 0, 2, 3, 1, 0, 1, 0, 1, 0, 
      2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 
      1 };
  
  private static int[] convertFEto06 = new int[] { 
      1611, 1611, 1612, 1612, 1613, 1613, 1614, 1614, 1615, 1615, 
      1616, 1616, 1617, 1617, 1618, 1618, 1569, 1570, 1570, 1571, 
      1571, 1572, 1572, 1573, 1573, 1574, 1574, 1574, 1574, 1575, 
      1575, 1576, 1576, 1576, 1576, 1577, 1577, 1578, 1578, 1578, 
      1578, 1579, 1579, 1579, 1579, 1580, 1580, 1580, 1580, 1581, 
      1581, 1581, 1581, 1582, 1582, 1582, 1582, 1583, 1583, 1584, 
      1584, 1585, 1585, 1586, 1586, 1587, 1587, 1587, 1587, 1588, 
      1588, 1588, 1588, 1589, 1589, 1589, 1589, 1590, 1590, 1590, 
      1590, 1591, 1591, 1591, 1591, 1592, 1592, 1592, 1592, 1593, 
      1593, 1593, 1593, 1594, 1594, 1594, 1594, 1601, 1601, 1601, 
      1601, 1602, 1602, 1602, 1602, 1603, 1603, 1603, 1603, 1604, 
      1604, 1604, 1604, 1605, 1605, 1605, 1605, 1606, 1606, 1606, 
      1606, 1607, 1607, 1607, 1607, 1608, 1608, 1609, 1609, 1610, 
      1610, 1610, 1610, 1628, 1628, 1629, 1629, 1630, 1630, 1631, 
      1631 };
  
  private static final int[][][] shapeTable = new int[][][] { { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 1, 0, 3 }, { 0, 1, 0, 1 } }, { { 0, 0, 2, 2 }, { 0, 0, 1, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 3 } }, { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 1, 0, 3 }, { 0, 1, 0, 3 } }, { { 0, 0, 1, 2 }, { 0, 0, 1, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 3 } } };
  
  private void shapeToArabicDigitsWithContext(char[] dest, int start, int length, char digitBase, boolean lastStrongWasAL) {
    UBiDiProps bdp = UBiDiProps.INSTANCE;
    digitBase = (char)(digitBase - 48);
    for (int i = start + length; --i >= start; ) {
      char ch = dest[i];
      switch (bdp.getClass(ch)) {
        case 0:
        case 1:
          lastStrongWasAL = false;
        case 13:
          lastStrongWasAL = true;
        case 2:
          if (lastStrongWasAL && ch <= '9')
            dest[i] = (char)(ch + digitBase); 
      } 
    } 
  }
  
  private static void invertBuffer(char[] buffer, int start, int length) {
    for (int i = start, j = start + length - 1; i < j; i++, j--) {
      char temp = buffer[i];
      buffer[i] = buffer[j];
      buffer[j] = temp;
    } 
  }
  
  private static char changeLamAlef(char ch) {
    switch (ch) {
      case 'آ':
        return 'ٜ';
      case 'أ':
        return 'ٝ';
      case 'إ':
        return 'ٞ';
      case 'ا':
        return 'ٟ';
    } 
    return Character.MIN_VALUE;
  }
  
  private static int specialChar(char ch) {
    if ((ch > 'ء' && ch < 'ئ') || ch == 'ا' || (ch > 'خ' && ch < 'س') || (ch > 'ه' && ch < 'ي') || ch == 'ة')
      return 1; 
    if (ch >= 'ً' && ch <= 'ْ')
      return 2; 
    if ((ch >= 'ٓ' && ch <= 'ٕ') || ch == 'ٰ' || (ch >= 'ﹰ' && ch <= 'ﹿ'))
      return 3; 
    return 0;
  }
  
  private static int getLink(char ch) {
    if (ch >= 'آ' && ch <= 'ۓ')
      return araLink[ch - 1570]; 
    if (ch == '‍')
      return 3; 
    if (ch >= '⁭' && ch <= '⁯')
      return 4; 
    if (ch >= 'ﹰ' && ch <= 'ﻼ')
      return presLink[ch - 65136]; 
    return 0;
  }
  
  private static int countSpacesLeft(char[] dest, int start, int count) {
    for (int i = start, e = start + count; i < e; i++) {
      if (dest[i] != ' ')
        return i - start; 
    } 
    return count;
  }
  
  private static int countSpacesRight(char[] dest, int start, int count) {
    for (int i = start + count; --i >= start;) {
      if (dest[i] != ' ')
        return start + count - 1 - i; 
    } 
    return count;
  }
  
  private static boolean isTashkeelChar(char ch) {
    return (ch >= 'ً' && ch <= 'ْ');
  }
  
  private static int isSeenTailFamilyChar(char ch) {
    if (ch >= 'ﺱ' && ch < 'ﺿ')
      return tailFamilyIsolatedFinal[ch - 65201]; 
    return 0;
  }
  
  private static int isSeenFamilyChar(char ch) {
    if (ch >= 'س' && ch <= 'ض')
      return 1; 
    return 0;
  }
  
  private static boolean isTailChar(char ch) {
    if (ch == '​' || ch == 'ﹳ')
      return true; 
    return false;
  }
  
  private static boolean isAlefMaksouraChar(char ch) {
    return (ch == 'ﻯ' || ch == 'ﻰ' || ch == 'ى');
  }
  
  private static boolean isYehHamzaChar(char ch) {
    if (ch == 'ﺉ' || ch == 'ﺊ')
      return true; 
    return false;
  }
  
  private static boolean isTashkeelCharFE(char ch) {
    return (ch != '﹵' && ch >= 'ﹰ' && ch <= 'ﹿ');
  }
  
  private static int isTashkeelOnTatweelChar(char ch) {
    if (ch >= 'ﹰ' && ch <= 'ﹿ' && ch != 'ﹳ' && ch != '﹵' && ch != 'ﹽ')
      return tashkeelMedial[ch - 65136]; 
    if ((ch >= 'ﳲ' && ch <= 'ﳴ') || ch == 'ﹽ')
      return 2; 
    return 0;
  }
  
  private static int isIsolatedTashkeelChar(char ch) {
    if (ch >= 'ﹰ' && ch <= 'ﹿ' && ch != 'ﹳ' && ch != '﹵')
      return 1 - tashkeelMedial[ch - 65136]; 
    if (ch >= 'ﱞ' && ch <= 'ﱣ')
      return 1; 
    return 0;
  }
  
  private static boolean isAlefChar(char ch) {
    return (ch == 'آ' || ch == 'أ' || ch == 'إ' || ch == 'ا');
  }
  
  private static boolean isLamAlefChar(char ch) {
    return (ch >= 'ﻵ' && ch <= 'ﻼ');
  }
  
  private static boolean isNormalizedLamAlefChar(char ch) {
    return (ch >= 'ٜ' && ch <= 'ٟ');
  }
  
  private int calculateSize(char[] source, int sourceStart, int sourceLength) {
    int i, e, destSize = sourceLength;
    switch (this.options & 0x18) {
      case 8:
      case 24:
        if (this.isLogical) {
          for (int j = sourceStart, k = sourceStart + sourceLength - 1; j < k; j++) {
            if ((source[j] == 'ل' && isAlefChar(source[j + 1])) || isTashkeelCharFE(source[j]))
              destSize--; 
          } 
          break;
        } 
        for (i = sourceStart + 1, e = sourceStart + sourceLength; i < e; i++) {
          if ((source[i] == 'ل' && isAlefChar(source[i - 1])) || isTashkeelCharFE(source[i]))
            destSize--; 
        } 
        break;
      case 16:
        for (i = sourceStart, e = sourceStart + sourceLength; i < e; i++) {
          if (isLamAlefChar(source[i]))
            destSize++; 
        } 
        break;
    } 
    return destSize;
  }
  
  private static int countSpaceSub(char[] dest, int length, char subChar) {
    int i = 0;
    int count = 0;
    while (i < length) {
      if (dest[i] == subChar)
        count++; 
      i++;
    } 
    return count;
  }
  
  private static void shiftArray(char[] dest, int start, int e, char subChar) {
    int w = e;
    int r = e;
    while (--r >= start) {
      char ch = dest[r];
      w--;
      if (ch != subChar && w != r)
        dest[w] = ch; 
    } 
  }
  
  private static int flipArray(char[] dest, int start, int e, int w) {
    if (w > start) {
      int r = w;
      w = start;
      while (r < e)
        dest[w++] = dest[r++]; 
    } else {
      w = e;
    } 
    return w;
  }
  
  private static int handleTashkeelWithTatweel(char[] dest, int sourceLength) {
    for (int i = 0; i < sourceLength; i++) {
      if (isTashkeelOnTatweelChar(dest[i]) == 1) {
        dest[i] = 'ـ';
      } else if (isTashkeelOnTatweelChar(dest[i]) == 2) {
        dest[i] = 'ﹽ';
      } else if (isIsolatedTashkeelChar(dest[i]) == 1 && dest[i] != 'ﹼ') {
        dest[i] = ' ';
      } 
    } 
    return sourceLength;
  }
  
  private int handleGeneratedSpaces(char[] dest, int start, int length) {
    int lenOptionsLamAlef = this.options & 0x10003;
    int lenOptionsTashkeel = this.options & 0xE0000;
    boolean lamAlefOn = false;
    boolean tashkeelOn = false;
    if (((!this.isLogical ? 1 : 0) & (!this.spacesRelativeToTextBeginEnd ? 1 : 0)) != 0) {
      switch (lenOptionsLamAlef) {
        case 3:
          lenOptionsLamAlef = 2;
          break;
        case 2:
          lenOptionsLamAlef = 3;
          break;
      } 
      switch (lenOptionsTashkeel) {
        case 262144:
          lenOptionsTashkeel = 393216;
          break;
        case 393216:
          lenOptionsTashkeel = 262144;
          break;
      } 
    } 
    if (lenOptionsLamAlef == 1) {
      for (int i = start, e = i + length; i < e; i++) {
        if (dest[i] == Character.MAX_VALUE)
          dest[i] = ' '; 
      } 
    } else {
      int e = start + length;
      int wL = countSpaceSub(dest, length, '￿');
      int wT = countSpaceSub(dest, length, '￾');
      if (lenOptionsLamAlef == 2)
        lamAlefOn = true; 
      if (lenOptionsTashkeel == 393216)
        tashkeelOn = true; 
      if (lamAlefOn && lenOptionsLamAlef == 2) {
        shiftArray(dest, start, e, '￿');
        while (wL > start)
          dest[--wL] = ' '; 
      } 
      if (tashkeelOn && lenOptionsTashkeel == 393216) {
        shiftArray(dest, start, e, '￾');
        while (wT > start)
          dest[--wT] = ' '; 
      } 
      lamAlefOn = false;
      tashkeelOn = false;
      if (lenOptionsLamAlef == 0)
        lamAlefOn = true; 
      if (lenOptionsTashkeel == 524288)
        tashkeelOn = true; 
      if (lamAlefOn && lenOptionsLamAlef == 0) {
        shiftArray(dest, start, e, '￿');
        wL = flipArray(dest, start, e, wL);
        length = wL - start;
      } 
      if (tashkeelOn && lenOptionsTashkeel == 524288) {
        shiftArray(dest, start, e, '￾');
        wT = flipArray(dest, start, e, wT);
        length = wT - start;
      } 
      lamAlefOn = false;
      tashkeelOn = false;
      if (lenOptionsLamAlef == 3 || lenOptionsLamAlef == 65536)
        lamAlefOn = true; 
      if (lenOptionsTashkeel == 262144)
        tashkeelOn = true; 
      if (lamAlefOn && (lenOptionsLamAlef == 3 || lenOptionsLamAlef == 65536)) {
        shiftArray(dest, start, e, '￿');
        wL = flipArray(dest, start, e, wL);
        while (wL < e)
          dest[wL++] = ' '; 
      } 
      if (tashkeelOn && lenOptionsTashkeel == 262144) {
        shiftArray(dest, start, e, '￾');
        wT = flipArray(dest, start, e, wT);
        while (wT < e)
          dest[wT++] = ' '; 
      } 
    } 
    return length;
  }
  
  private boolean expandCompositCharAtBegin(char[] dest, int start, int length, int lacount) {
    boolean spaceNotFound = false;
    if (lacount > countSpacesRight(dest, start, length)) {
      spaceNotFound = true;
      return spaceNotFound;
    } 
    for (int r = start + length - lacount, w = start + length; --r >= start; ) {
      char ch = dest[r];
      if (isNormalizedLamAlefChar(ch)) {
        dest[--w] = 'ل';
        dest[--w] = convertNormalizedLamAlef[ch - 1628];
        continue;
      } 
      dest[--w] = ch;
    } 
    return spaceNotFound;
  }
  
  private boolean expandCompositCharAtEnd(char[] dest, int start, int length, int lacount) {
    boolean spaceNotFound = false;
    if (lacount > countSpacesLeft(dest, start, length)) {
      spaceNotFound = true;
      return spaceNotFound;
    } 
    for (int r = start + lacount, w = start, e = start + length; r < e; r++) {
      char ch = dest[r];
      if (isNormalizedLamAlefChar(ch)) {
        dest[w++] = convertNormalizedLamAlef[ch - 1628];
        dest[w++] = 'ل';
      } else {
        dest[w++] = ch;
      } 
    } 
    return spaceNotFound;
  }
  
  private boolean expandCompositCharAtNear(char[] dest, int start, int length, int yehHamzaOption, int seenTailOption, int lamAlefOption) {
    boolean spaceNotFound = false;
    if (isNormalizedLamAlefChar(dest[start])) {
      spaceNotFound = true;
      return spaceNotFound;
    } 
    for (int i = start + length; --i >= start; ) {
      char ch = dest[i];
      if (lamAlefOption == 1 && isNormalizedLamAlefChar(ch)) {
        if (i > start && dest[i - 1] == ' ') {
          dest[i] = 'ل';
          dest[--i] = convertNormalizedLamAlef[ch - 1628];
          continue;
        } 
        spaceNotFound = true;
        return spaceNotFound;
      } 
      if (seenTailOption == 1 && isSeenTailFamilyChar(ch) == 1) {
        if (i > start && dest[i - 1] == ' ') {
          dest[i - 1] = this.tailChar;
          continue;
        } 
        spaceNotFound = true;
        return spaceNotFound;
      } 
      if (yehHamzaOption == 1 && isYehHamzaChar(ch)) {
        if (i > start && dest[i - 1] == ' ') {
          dest[i] = yehHamzaToYeh[ch - 65161];
          dest[i - 1] = 'ﺀ';
          continue;
        } 
        spaceNotFound = true;
        return spaceNotFound;
      } 
    } 
    return false;
  }
  
  private int expandCompositChar(char[] dest, int start, int length, int lacount, int shapingMode) throws ArabicShapingException {
    int lenOptionsLamAlef = this.options & 0x10003;
    int lenOptionsSeen = this.options & 0x700000;
    int lenOptionsYehHamza = this.options & 0x3800000;
    boolean spaceNotFound = false;
    if (!this.isLogical && !this.spacesRelativeToTextBeginEnd)
      switch (lenOptionsLamAlef) {
        case 3:
          lenOptionsLamAlef = 2;
          break;
        case 2:
          lenOptionsLamAlef = 3;
          break;
      }  
    if (shapingMode == 1) {
      if (lenOptionsLamAlef == 65536) {
        if (this.isLogical) {
          spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount);
          if (spaceNotFound)
            spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount); 
          if (spaceNotFound)
            spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1); 
          if (spaceNotFound)
            throw new ArabicShapingException("No spacefor lamalef"); 
        } else {
          spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount);
          if (spaceNotFound)
            spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount); 
          if (spaceNotFound)
            spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1); 
          if (spaceNotFound)
            throw new ArabicShapingException("No spacefor lamalef"); 
        } 
      } else if (lenOptionsLamAlef == 2) {
        spaceNotFound = expandCompositCharAtEnd(dest, start, length, lacount);
        if (spaceNotFound)
          throw new ArabicShapingException("No spacefor lamalef"); 
      } else if (lenOptionsLamAlef == 3) {
        spaceNotFound = expandCompositCharAtBegin(dest, start, length, lacount);
        if (spaceNotFound)
          throw new ArabicShapingException("No spacefor lamalef"); 
      } else if (lenOptionsLamAlef == 1) {
        spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 0, 1);
        if (spaceNotFound)
          throw new ArabicShapingException("No spacefor lamalef"); 
      } else if (lenOptionsLamAlef == 0) {
        for (int r = start + length, w = r + lacount; --r >= start; ) {
          char ch = dest[r];
          if (isNormalizedLamAlefChar(ch)) {
            dest[--w] = 'ل';
            dest[--w] = convertNormalizedLamAlef[ch - 1628];
            continue;
          } 
          dest[--w] = ch;
        } 
        length += lacount;
      } 
    } else {
      if (lenOptionsSeen == 2097152) {
        spaceNotFound = expandCompositCharAtNear(dest, start, length, 0, 1, 0);
        if (spaceNotFound)
          throw new ArabicShapingException("No space for Seen tail expansion"); 
      } 
      if (lenOptionsYehHamza == 16777216) {
        spaceNotFound = expandCompositCharAtNear(dest, start, length, 1, 0, 0);
        if (spaceNotFound)
          throw new ArabicShapingException("No space for YehHamza expansion"); 
      } 
    } 
    return length;
  }
  
  private int normalize(char[] dest, int start, int length) {
    int lacount = 0;
    for (int i = start, e = i + length; i < e; i++) {
      char ch = dest[i];
      if (ch >= 'ﹰ' && ch <= 'ﻼ') {
        if (isLamAlefChar(ch))
          lacount++; 
        dest[i] = (char)convertFEto06[ch - 65136];
      } 
    } 
    return lacount;
  }
  
  private int deshapeNormalize(char[] dest, int start, int length) {
    int lacount = 0;
    int yehHamzaComposeEnabled = 0;
    int seenComposeEnabled = 0;
    yehHamzaComposeEnabled = ((this.options & 0x3800000) == 16777216) ? 1 : 0;
    seenComposeEnabled = ((this.options & 0x700000) == 2097152) ? 1 : 0;
    for (int i = start, e = i + length; i < e; i++) {
      char ch = dest[i];
      if (yehHamzaComposeEnabled == 1 && (ch == 'ء' || ch == 'ﺀ') && i < length - 1 && isAlefMaksouraChar(dest[i + 1])) {
        dest[i] = ' ';
        dest[i + 1] = 'ئ';
      } else if (seenComposeEnabled == 1 && isTailChar(ch) && i < length - 1 && isSeenTailFamilyChar(dest[i + 1]) == 1) {
        dest[i] = ' ';
      } else if (ch >= 'ﹰ' && ch <= 'ﻼ') {
        if (isLamAlefChar(ch))
          lacount++; 
        dest[i] = (char)convertFEto06[ch - 65136];
      } 
    } 
    return lacount;
  }
  
  private int shapeUnicode(char[] dest, int start, int length, int destSize, int tashkeelFlag) throws ArabicShapingException {
    int lamalef_count = normalize(dest, start, length);
    boolean lamalef_found = false, seenfam_found = false;
    boolean yehhamza_found = false, tashkeel_found = false;
    int i = start + length - 1;
    int currLink = getLink(dest[i]);
    int nextLink = 0;
    int prevLink = 0;
    int lastLink = 0;
    int lastPos = i;
    int nx = -2;
    int nw = 0;
    while (i >= 0) {
      if ((currLink & 0xFF00) > 0 || isTashkeelChar(dest[i])) {
        nw = i - 1;
        nx = -2;
        while (nx < 0) {
          if (nw == -1) {
            nextLink = 0;
            nx = Integer.MAX_VALUE;
            continue;
          } 
          nextLink = getLink(dest[nw]);
          if ((nextLink & 0x4) == 0) {
            nx = nw;
            continue;
          } 
          nw--;
        } 
        if ((currLink & 0x20) > 0 && (lastLink & 0x10) > 0) {
          lamalef_found = true;
          char wLamalef = changeLamAlef(dest[i]);
          if (wLamalef != '\000') {
            dest[i] = Character.MAX_VALUE;
            dest[lastPos] = wLamalef;
            i = lastPos;
          } 
          lastLink = prevLink;
          currLink = getLink(wLamalef);
        } 
        if (i > 0 && dest[i - 1] == ' ') {
          if (isSeenFamilyChar(dest[i]) == 1) {
            seenfam_found = true;
          } else if (dest[i] == 'ئ') {
            yehhamza_found = true;
          } 
        } else if (i == 0) {
          if (isSeenFamilyChar(dest[i]) == 1) {
            seenfam_found = true;
          } else if (dest[i] == 'ئ') {
            yehhamza_found = true;
          } 
        } 
        int flag = specialChar(dest[i]);
        int shape = shapeTable[nextLink & 0x3][lastLink & 0x3][currLink & 0x3];
        if (flag == 1) {
          shape &= 0x1;
        } else if (flag == 2) {
          if (tashkeelFlag == 0 && (lastLink & 0x2) != 0 && (nextLink & 0x1) != 0 && dest[i] != 'ٌ' && dest[i] != 'ٍ' && ((nextLink & 0x20) != 32 || (lastLink & 0x10) != 16)) {
            shape = 1;
          } else if (tashkeelFlag == 2 && dest[i] == 'ّ') {
            shape = 1;
          } else {
            shape = 0;
          } 
        } 
        if (flag == 2) {
          if (tashkeelFlag == 2 && dest[i] != 'ّ') {
            dest[i] = '￾';
            tashkeel_found = true;
          } else {
            dest[i] = (char)(65136 + irrelevantPos[dest[i] - 1611] + shape);
          } 
        } else {
          dest[i] = (char)(65136 + (currLink >> 8) + shape);
        } 
      } 
      if ((currLink & 0x4) == 0) {
        prevLink = lastLink;
        lastLink = currLink;
        lastPos = i;
      } 
      i--;
      if (i == nx) {
        currLink = nextLink;
        nx = -2;
        continue;
      } 
      if (i != -1)
        currLink = getLink(dest[i]); 
    } 
    destSize = length;
    if (lamalef_found || tashkeel_found)
      destSize = handleGeneratedSpaces(dest, start, length); 
    if (seenfam_found || yehhamza_found)
      destSize = expandCompositChar(dest, start, destSize, lamalef_count, 0); 
    return destSize;
  }
  
  private int deShapeUnicode(char[] dest, int start, int length, int destSize) throws ArabicShapingException {
    int lamalef_count = deshapeNormalize(dest, start, length);
    if (lamalef_count != 0) {
      destSize = expandCompositChar(dest, start, length, lamalef_count, 1);
    } else {
      destSize = length;
    } 
    return destSize;
  }
  
  private int internalShape(char[] source, int sourceStart, int sourceLength, char[] dest, int destStart, int destSize) throws ArabicShapingException {
    if (sourceLength == 0)
      return 0; 
    if (destSize == 0) {
      if ((this.options & 0x18) != 0 && (this.options & 0x10003) == 0)
        return calculateSize(source, sourceStart, sourceLength); 
      return sourceLength;
    } 
    char[] temp = new char[sourceLength * 2];
    System.arraycopy(source, sourceStart, temp, 0, sourceLength);
    if (this.isLogical)
      invertBuffer(temp, 0, sourceLength); 
    int outputSize = sourceLength;
    switch (this.options & 0x18) {
      case 24:
        outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 1);
        break;
      case 8:
        if ((this.options & 0xE0000) > 0 && (this.options & 0xE0000) != 786432) {
          outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 2);
          break;
        } 
        outputSize = shapeUnicode(temp, 0, sourceLength, destSize, 0);
        if ((this.options & 0xE0000) == 786432)
          outputSize = handleTashkeelWithTatweel(temp, sourceLength); 
        break;
      case 16:
        outputSize = deShapeUnicode(temp, 0, sourceLength, destSize);
        break;
    } 
    if (outputSize > destSize)
      throw new ArabicShapingException("not enough room for result data"); 
    if ((this.options & 0xE0) != 0) {
      int digitDelta;
      char digitTop;
      int i, j, k;
      char digitBase = '0';
      switch (this.options & 0x100) {
        case 0:
          digitBase = '٠';
          break;
        case 256:
          digitBase = '۰';
          break;
      } 
      switch (this.options & 0xE0) {
        case 32:
          digitDelta = digitBase - 48;
          for (i = 0; i < outputSize; i++) {
            char ch = temp[i];
            if (ch <= '9' && ch >= '0')
              temp[i] = (char)(temp[i] + digitDelta); 
          } 
          break;
        case 64:
          digitTop = (char)(digitBase + 9);
          j = 48 - digitBase;
          for (k = 0; k < outputSize; k++) {
            char ch = temp[k];
            if (ch <= digitTop && ch >= digitBase)
              temp[k] = (char)(temp[k] + j); 
          } 
          break;
        case 96:
          shapeToArabicDigitsWithContext(temp, 0, outputSize, digitBase, false);
          break;
        case 128:
          shapeToArabicDigitsWithContext(temp, 0, outputSize, digitBase, true);
          break;
      } 
    } 
    if (this.isLogical)
      invertBuffer(temp, 0, outputSize); 
    System.arraycopy(temp, 0, dest, destStart, outputSize);
    return outputSize;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\ArabicShaping.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */