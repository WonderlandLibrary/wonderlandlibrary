package com.ibm.icu.text;

public interface UnicodeMatcher {
  public static final int U_MISMATCH = 0;
  
  public static final int U_PARTIAL_MATCH = 1;
  
  public static final int U_MATCH = 2;
  
  public static final char ETHER = '￿';
  
  int matches(Replaceable paramReplaceable, int[] paramArrayOfint, int paramInt, boolean paramBoolean);
  
  String toPattern(boolean paramBoolean);
  
  boolean matchesIndexValue(int paramInt);
  
  void addMatchSetTo(UnicodeSet paramUnicodeSet);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnicodeMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */