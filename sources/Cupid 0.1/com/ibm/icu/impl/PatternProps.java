package com.ibm.icu.impl;

public final class PatternProps {
  public static boolean isSyntax(int c) {
    if (c < 0)
      return false; 
    if (c <= 255)
      return (latin1[c] == 3); 
    if (c < 8208)
      return false; 
    if (c <= 12336) {
      int bits = syntax2000[index2000[c - 8192 >> 5]];
      return ((bits >> (c & 0x1F) & 0x1) != 0);
    } 
    if (64830 <= c && c <= 65094)
      return (c <= 64831 || 65093 <= c); 
    return false;
  }
  
  public static boolean isSyntaxOrWhiteSpace(int c) {
    if (c < 0)
      return false; 
    if (c <= 255)
      return (latin1[c] != 0); 
    if (c < 8206)
      return false; 
    if (c <= 12336) {
      int bits = syntaxOrWhiteSpace2000[index2000[c - 8192 >> 5]];
      return ((bits >> (c & 0x1F) & 0x1) != 0);
    } 
    if (64830 <= c && c <= 65094)
      return (c <= 64831 || 65093 <= c); 
    return false;
  }
  
  public static boolean isWhiteSpace(int c) {
    if (c < 0)
      return false; 
    if (c <= 255)
      return (latin1[c] == 5); 
    if (8206 <= c && c <= 8233)
      return (c <= 8207 || 8232 <= c); 
    return false;
  }
  
  public static int skipWhiteSpace(CharSequence s, int i) {
    while (i < s.length() && isWhiteSpace(s.charAt(i)))
      i++; 
    return i;
  }
  
  public static String trimWhiteSpace(String s) {
    if (s.length() == 0 || (!isWhiteSpace(s.charAt(0)) && !isWhiteSpace(s.charAt(s.length() - 1))))
      return s; 
    int start = 0;
    int limit = s.length();
    while (start < limit && isWhiteSpace(s.charAt(start)))
      start++; 
    if (start < limit)
      while (isWhiteSpace(s.charAt(limit - 1)))
        limit--;  
    return s.substring(start, limit);
  }
  
  public static boolean isIdentifier(CharSequence s) {
    int limit = s.length();
    if (limit == 0)
      return false; 
    int start = 0;
    while (true) {
      if (isSyntaxOrWhiteSpace(s.charAt(start++)))
        return false; 
      if (start >= limit)
        return true; 
    } 
  }
  
  public static boolean isIdentifier(CharSequence s, int start, int limit) {
    if (start >= limit)
      return false; 
    while (true) {
      if (isSyntaxOrWhiteSpace(s.charAt(start++)))
        return false; 
      if (start >= limit)
        return true; 
    } 
  }
  
  public static int skipIdentifier(CharSequence s, int i) {
    while (i < s.length() && !isSyntaxOrWhiteSpace(s.charAt(i)))
      i++; 
    return i;
  }
  
  private static final byte[] latin1 = new byte[] { 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 
      5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 5, 3, 3, 3, 3, 3, 3, 3, 
      3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 
      3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 
      0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 
      0, 3, 3, 0, 3, 0, 3, 3, 0, 0, 
      0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 
      0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 
      0, 0, 0, 0, 0, 0 };
  
  private static final byte[] index2000 = new byte[] { 
      2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 5, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 
      7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
      0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 
      0, 0, 0, 0, 0, 0, 0, 0, 8, 9 };
  
  private static final int[] syntax2000 = new int[] { 0, -1, -65536, 2147418367, 2146435070, -65536, 4194303, -1048576, -242, 65537 };
  
  private static final int[] syntaxOrWhiteSpace2000 = new int[] { 0, -1, -16384, 2147419135, 2146435070, -65536, 4194303, -1048576, -242, 65537 };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\PatternProps.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */