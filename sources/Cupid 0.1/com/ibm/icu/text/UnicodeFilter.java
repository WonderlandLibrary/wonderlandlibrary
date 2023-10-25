package com.ibm.icu.text;

public abstract class UnicodeFilter implements UnicodeMatcher {
  public abstract boolean contains(int paramInt);
  
  public int matches(Replaceable text, int[] offset, int limit, boolean incremental) {
    int c;
    if (offset[0] < limit && contains(c = text.char32At(offset[0]))) {
      offset[0] = offset[0] + UTF16.getCharCount(c);
      return 2;
    } 
    if (offset[0] > limit && contains(text.char32At(offset[0]))) {
      offset[0] = offset[0] - 1;
      if (offset[0] >= 0)
        offset[0] = offset[0] - UTF16.getCharCount(text.char32At(offset[0])) - 1; 
      return 2;
    } 
    if (incremental && offset[0] == limit)
      return 1; 
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnicodeFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */