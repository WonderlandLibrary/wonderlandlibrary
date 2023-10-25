package com.ibm.icu.text;

public interface UForwardCharacterIterator {
  public static final int DONE = -1;
  
  int next();
  
  int nextCodePoint();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UForwardCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */