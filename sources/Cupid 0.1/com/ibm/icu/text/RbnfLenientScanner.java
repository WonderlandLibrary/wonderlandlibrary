package com.ibm.icu.text;

public interface RbnfLenientScanner {
  boolean allIgnorable(String paramString);
  
  int prefixLength(String paramString1, String paramString2);
  
  int[] findText(String paramString1, String paramString2, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RbnfLenientScanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */