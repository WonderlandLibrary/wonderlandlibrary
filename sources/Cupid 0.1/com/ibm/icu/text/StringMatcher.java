package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;

class StringMatcher implements UnicodeMatcher, UnicodeReplacer {
  private String pattern;
  
  private int matchStart;
  
  private int matchLimit;
  
  private int segmentNumber;
  
  private final RuleBasedTransliterator.Data data;
  
  public StringMatcher(String theString, int segmentNum, RuleBasedTransliterator.Data theData) {
    this.data = theData;
    this.pattern = theString;
    this.matchStart = this.matchLimit = -1;
    this.segmentNumber = segmentNum;
  }
  
  public StringMatcher(String theString, int start, int limit, int segmentNum, RuleBasedTransliterator.Data theData) {
    this(theString.substring(start, limit), segmentNum, theData);
  }
  
  public int matches(Replaceable text, int[] offset, int limit, boolean incremental) {
    int[] cursor = { offset[0] };
    if (limit < cursor[0]) {
      for (int i = this.pattern.length() - 1; i >= 0; i--) {
        char keyChar = this.pattern.charAt(i);
        UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
        if (subm == null) {
          if (cursor[0] > limit && keyChar == text.charAt(cursor[0])) {
            cursor[0] = cursor[0] - 1;
          } else {
            return 0;
          } 
        } else {
          int m = subm.matches(text, cursor, limit, incremental);
          if (m != 2)
            return m; 
        } 
      } 
      if (this.matchStart < 0) {
        this.matchStart = cursor[0] + 1;
        this.matchLimit = offset[0] + 1;
      } 
    } else {
      for (int i = 0; i < this.pattern.length(); i++) {
        if (incremental && cursor[0] == limit)
          return 1; 
        char keyChar = this.pattern.charAt(i);
        UnicodeMatcher subm = this.data.lookupMatcher(keyChar);
        if (subm == null) {
          if (cursor[0] < limit && keyChar == text.charAt(cursor[0])) {
            cursor[0] = cursor[0] + 1;
          } else {
            return 0;
          } 
        } else {
          int m = subm.matches(text, cursor, limit, incremental);
          if (m != 2)
            return m; 
        } 
      } 
      this.matchStart = offset[0];
      this.matchLimit = cursor[0];
    } 
    offset[0] = cursor[0];
    return 2;
  }
  
  public String toPattern(boolean escapeUnprintable) {
    StringBuffer result = new StringBuffer();
    StringBuffer quoteBuf = new StringBuffer();
    if (this.segmentNumber > 0)
      result.append('('); 
    for (int i = 0; i < this.pattern.length(); i++) {
      char keyChar = this.pattern.charAt(i);
      UnicodeMatcher m = this.data.lookupMatcher(keyChar);
      if (m == null) {
        Utility.appendToRule(result, keyChar, false, escapeUnprintable, quoteBuf);
      } else {
        Utility.appendToRule(result, m.toPattern(escapeUnprintable), true, escapeUnprintable, quoteBuf);
      } 
    } 
    if (this.segmentNumber > 0)
      result.append(')'); 
    Utility.appendToRule(result, -1, true, escapeUnprintable, quoteBuf);
    return result.toString();
  }
  
  public boolean matchesIndexValue(int v) {
    if (this.pattern.length() == 0)
      return true; 
    int c = UTF16.charAt(this.pattern, 0);
    UnicodeMatcher m = this.data.lookupMatcher(c);
    return (m == null) ? (((c & 0xFF) == v)) : m.matchesIndexValue(v);
  }
  
  public void addMatchSetTo(UnicodeSet toUnionTo) {
    for (int i = 0; i < this.pattern.length(); i += UTF16.getCharCount(ch)) {
      int ch = UTF16.charAt(this.pattern, i);
      UnicodeMatcher matcher = this.data.lookupMatcher(ch);
      if (matcher == null) {
        toUnionTo.add(ch);
      } else {
        matcher.addMatchSetTo(toUnionTo);
      } 
    } 
  }
  
  public int replace(Replaceable text, int start, int limit, int[] cursor) {
    int outLen = 0;
    int dest = limit;
    if (this.matchStart >= 0 && 
      this.matchStart != this.matchLimit) {
      text.copy(this.matchStart, this.matchLimit, dest);
      outLen = this.matchLimit - this.matchStart;
    } 
    text.replace(start, limit, "");
    return outLen;
  }
  
  public String toReplacerPattern(boolean escapeUnprintable) {
    StringBuffer rule = new StringBuffer("$");
    Utility.appendNumber(rule, this.segmentNumber, 10, 1);
    return rule.toString();
  }
  
  public void resetMatch() {
    this.matchStart = this.matchLimit = -1;
  }
  
  public void addReplacementSetTo(UnicodeSet toUnionTo) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\StringMatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */