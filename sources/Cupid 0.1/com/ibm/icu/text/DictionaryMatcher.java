package com.ibm.icu.text;

import java.text.CharacterIterator;

abstract class DictionaryMatcher {
  public abstract int matches(CharacterIterator paramCharacterIterator, int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3);
  
  public int matches(CharacterIterator text, int maxLength, int[] lengths, int[] count, int limit) {
    return matches(text, maxLength, lengths, count, limit, null);
  }
  
  public abstract int getType();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\DictionaryMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */