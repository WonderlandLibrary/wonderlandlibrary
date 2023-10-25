package com.ibm.icu.lang;

public class CharSequences {
  public static int matchAfter(CharSequence a, CharSequence b, int aIndex, int bIndex) {
    int i = aIndex, j = bIndex;
    int alen = a.length();
    int blen = b.length();
    for (; i < alen && j < blen; i++, j++) {
      char ca = a.charAt(i);
      char cb = b.charAt(j);
      if (ca != cb)
        break; 
    } 
    int result = i - aIndex;
    if (result != 0 && !onCharacterBoundary(a, i) && !onCharacterBoundary(b, j))
      result--; 
    return result;
  }
  
  public int codePointLength(CharSequence s) {
    return Character.codePointCount(s, 0, s.length());
  }
  
  public static final boolean equals(int codepoint, CharSequence other) {
    if (other == null)
      return false; 
    switch (other.length()) {
      case 1:
        return (codepoint == other.charAt(0));
      case 2:
        return (codepoint > 65535 && codepoint == Character.codePointAt(other, 0));
    } 
    return false;
  }
  
  public static final boolean equals(CharSequence other, int codepoint) {
    return equals(codepoint, other);
  }
  
  public static int compare(CharSequence string, int codePoint) {
    if (codePoint < 0 || codePoint > 1114111)
      throw new IllegalArgumentException(); 
    int stringLength = string.length();
    if (stringLength == 0)
      return -1; 
    char firstChar = string.charAt(0);
    int offset = codePoint - 65536;
    if (offset < 0) {
      int i = firstChar - codePoint;
      if (i != 0)
        return i; 
      return stringLength - 1;
    } 
    char lead = (char)((offset >>> 10) + 55296);
    int result = firstChar - lead;
    if (result != 0)
      return result; 
    if (stringLength > 1) {
      char trail = (char)((offset & 0x3FF) + 56320);
      result = string.charAt(1) - trail;
      if (result != 0)
        return result; 
    } 
    return stringLength - 2;
  }
  
  public static int compare(int codepoint, CharSequence a) {
    return -compare(a, codepoint);
  }
  
  public static int getSingleCodePoint(CharSequence s) {
    int length = s.length();
    if (length < 1 || length > 2)
      return Integer.MAX_VALUE; 
    int result = Character.codePointAt(s, 0);
    return (((result < 65536) ? true : false) == ((length == 1) ? true : false)) ? result : Integer.MAX_VALUE;
  }
  
  public static final <T> boolean equals(T a, T b) {
    return (a == null) ? ((b == null)) : ((b == null) ? false : a.equals(b));
  }
  
  public static int compare(CharSequence a, CharSequence b) {
    int alength = a.length();
    int blength = b.length();
    int min = (alength <= blength) ? alength : blength;
    for (int i = 0; i < min; i++) {
      int diff = a.charAt(i) - b.charAt(i);
      if (diff != 0)
        return diff; 
    } 
    return alength - blength;
  }
  
  public static boolean equalsChars(CharSequence a, CharSequence b) {
    return (a.length() == b.length() && compare(a, b) == 0);
  }
  
  public static boolean onCharacterBoundary(CharSequence s, int i) {
    return (i <= 0 || i >= s.length() || !Character.isHighSurrogate(s.charAt(i - 1)) || !Character.isLowSurrogate(s.charAt(i)));
  }
  
  public static int indexOf(CharSequence s, int codePoint) {
    for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
      int cp = Character.codePointAt(s, i);
      if (cp == codePoint)
        return i; 
    } 
    return -1;
  }
  
  public static int[] codePoints(CharSequence s) {
    int[] result = new int[s.length()];
    int j = 0;
    for (int i = 0; i < s.length(); i++) {
      char cp = s.charAt(i);
      if (cp >= '?' && cp <= '?' && i != 0) {
        char last = (char)result[j - 1];
        if (last >= '?' && last <= '?') {
          result[j - 1] = Character.toCodePoint(last, cp);
          continue;
        } 
      } 
      result[j++] = cp;
      continue;
    } 
    if (j == result.length)
      return result; 
    int[] shortResult = new int[j];
    System.arraycopy(result, 0, shortResult, 0, j);
    return shortResult;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\lang\CharSequences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */