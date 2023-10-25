package com.ibm.icu.impl;

public final class UCharacterUtility {
  private static final int NON_CHARACTER_SUFFIX_MIN_3_0_ = 65534;
  
  private static final int NON_CHARACTER_MIN_3_1_ = 64976;
  
  private static final int NON_CHARACTER_MAX_3_1_ = 65007;
  
  public static boolean isNonCharacter(int ch) {
    if ((ch & 0xFFFE) == 65534)
      return true; 
    return (ch >= 64976 && ch <= 65007);
  }
  
  static int toInt(char msc, char lsc) {
    return msc << 16 | lsc;
  }
  
  static int getNullTermByteSubString(StringBuffer str, byte[] array, int index) {
    byte b = 1;
    while (b != 0) {
      b = array[index];
      if (b != 0)
        str.append((char)(b & 0xFF)); 
      index++;
    } 
    return index;
  }
  
  static int compareNullTermByteSubString(String str, byte[] array, int strindex, int aindex) {
    byte b = 1;
    int length = str.length();
    while (b != 0) {
      b = array[aindex];
      aindex++;
      if (b == 0)
        break; 
      if (strindex == length || str.charAt(strindex) != (char)(b & 0xFF))
        return -1; 
      strindex++;
    } 
    return strindex;
  }
  
  static int skipNullTermByteSubString(byte[] array, int index, int skipcount) {
    for (int i = 0; i < skipcount; i++) {
      byte b = 1;
      while (b != 0) {
        b = array[index];
        index++;
      } 
    } 
    return index;
  }
  
  static int skipByteSubString(byte[] array, int index, int length, byte skipend) {
    int result;
    for (result = 0; result < length; result++) {
      byte b = array[index + result];
      if (b == skipend) {
        result++;
        break;
      } 
    } 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\UCharacterUtility.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */