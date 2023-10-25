package com.ibm.icu.text;

interface UnicodeReplacer {
  int replace(Replaceable paramReplaceable, int paramInt1, int paramInt2, int[] paramArrayOfint);
  
  String toReplacerPattern(boolean paramBoolean);
  
  void addReplacementSetTo(UnicodeSet paramUnicodeSet);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnicodeReplacer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */