package com.ibm.icu.text;

import java.text.ParsePosition;

public interface SymbolTable {
  public static final char SYMBOL_REF = '$';
  
  char[] lookup(String paramString);
  
  UnicodeMatcher lookupMatcher(int paramInt);
  
  String parseReference(String paramString, ParsePosition paramParsePosition, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\SymbolTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */