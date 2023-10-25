package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public final class UCharArrayIterator extends UCharacterIterator {
  private final char[] text;
  
  private final int start;
  
  private final int limit;
  
  private int pos;
  
  public UCharArrayIterator(char[] text, int start, int limit) {
    if (start < 0 || limit > text.length || start > limit)
      throw new IllegalArgumentException("start: " + start + " or limit: " + limit + " out of range [0, " + text.length + ")"); 
    this.text = text;
    this.start = start;
    this.limit = limit;
    this.pos = start;
  }
  
  public int current() {
    return (this.pos < this.limit) ? this.text[this.pos] : -1;
  }
  
  public int getLength() {
    return this.limit - this.start;
  }
  
  public int getIndex() {
    return this.pos - this.start;
  }
  
  public int next() {
    return (this.pos < this.limit) ? this.text[this.pos++] : -1;
  }
  
  public int previous() {
    return (this.pos > this.start) ? this.text[--this.pos] : -1;
  }
  
  public void setIndex(int index) {
    if (index < 0 || index > this.limit - this.start)
      throw new IndexOutOfBoundsException("index: " + index + " out of range [0, " + (this.limit - this.start) + ")"); 
    this.pos = this.start + index;
  }
  
  public int getText(char[] fillIn, int offset) {
    int len = this.limit - this.start;
    System.arraycopy(this.text, this.start, fillIn, offset, len);
    return len;
  }
  
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\UCharArrayIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */