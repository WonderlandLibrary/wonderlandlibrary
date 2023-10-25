package com.ibm.icu.text;

import java.text.CharacterIterator;
import java.util.Stack;

abstract class DictionaryBreakEngine implements LanguageBreakEngine {
  protected UnicodeSet fSet = new UnicodeSet();
  
  private final int fTypes;
  
  public DictionaryBreakEngine(int breakTypes) {
    this.fTypes = breakTypes;
  }
  
  public boolean handles(int c, int breakType) {
    return (breakType >= 0 && breakType < 32 && (1 << breakType & this.fTypes) != 0 && this.fSet.contains(c));
  }
  
  public int findBreaks(CharacterIterator text_, int startPos, int endPos, boolean reverse, int breakType, Stack<Integer> foundBreaks) {
    int current, rangeStart, rangeEnd;
    if (breakType < 0 || breakType >= 32 || (1 << breakType & this.fTypes) == 0)
      return 0; 
    int result = 0;
    UCharacterIterator text = UCharacterIterator.getInstance(text_);
    int start = text.getIndex();
    int c = text.current();
    if (reverse) {
      boolean isDict = this.fSet.contains(c);
      while ((current = text.getIndex()) > startPos && isDict) {
        c = text.previous();
        isDict = this.fSet.contains(c);
      } 
      rangeStart = (current < startPos) ? startPos : (current + (isDict ? 0 : 1));
      rangeEnd = start + 1;
    } else {
      while ((current = text.getIndex()) < endPos && this.fSet.contains(c))
        c = text.next(); 
      rangeStart = start;
      rangeEnd = current;
    } 
    result = divideUpDictionaryRange(text, rangeStart, rangeEnd, foundBreaks);
    text.setIndex(current);
    return result;
  }
  
  protected abstract int divideUpDictionaryRange(UCharacterIterator paramUCharacterIterator, int paramInt1, int paramInt2, Stack<Integer> paramStack);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\DictionaryBreakEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */