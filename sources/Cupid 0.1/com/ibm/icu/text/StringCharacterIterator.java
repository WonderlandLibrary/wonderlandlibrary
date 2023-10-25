package com.ibm.icu.text;

import java.text.CharacterIterator;

public final class StringCharacterIterator implements CharacterIterator {
  private String text;
  
  private int begin;
  
  private int end;
  
  private int pos;
  
  public StringCharacterIterator(String text) {
    this(text, 0);
  }
  
  public StringCharacterIterator(String text, int pos) {
    this(text, 0, text.length(), pos);
  }
  
  public StringCharacterIterator(String text, int begin, int end, int pos) {
    if (text == null)
      throw new NullPointerException(); 
    this.text = text;
    if (begin < 0 || begin > end || end > text.length())
      throw new IllegalArgumentException("Invalid substring range"); 
    if (pos < begin || pos > end)
      throw new IllegalArgumentException("Invalid position"); 
    this.begin = begin;
    this.end = end;
    this.pos = pos;
  }
  
  public void setText(String text) {
    if (text == null)
      throw new NullPointerException(); 
    this.text = text;
    this.begin = 0;
    this.end = text.length();
    this.pos = 0;
  }
  
  public char first() {
    this.pos = this.begin;
    return current();
  }
  
  public char last() {
    if (this.end != this.begin) {
      this.pos = this.end - 1;
    } else {
      this.pos = this.end;
    } 
    return current();
  }
  
  public char setIndex(int p) {
    if (p < this.begin || p > this.end)
      throw new IllegalArgumentException("Invalid index"); 
    this.pos = p;
    return current();
  }
  
  public char current() {
    if (this.pos >= this.begin && this.pos < this.end)
      return this.text.charAt(this.pos); 
    return Character.MAX_VALUE;
  }
  
  public char next() {
    if (this.pos < this.end - 1) {
      this.pos++;
      return this.text.charAt(this.pos);
    } 
    this.pos = this.end;
    return Character.MAX_VALUE;
  }
  
  public char previous() {
    if (this.pos > this.begin) {
      this.pos--;
      return this.text.charAt(this.pos);
    } 
    return Character.MAX_VALUE;
  }
  
  public int getBeginIndex() {
    return this.begin;
  }
  
  public int getEndIndex() {
    return this.end;
  }
  
  public int getIndex() {
    return this.pos;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (!(obj instanceof StringCharacterIterator))
      return false; 
    StringCharacterIterator that = (StringCharacterIterator)obj;
    if (hashCode() != that.hashCode())
      return false; 
    if (!this.text.equals(that.text))
      return false; 
    if (this.pos != that.pos || this.begin != that.begin || this.end != that.end)
      return false; 
    return true;
  }
  
  public int hashCode() {
    return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
  }
  
  public Object clone() {
    try {
      StringCharacterIterator other = (StringCharacterIterator)super.clone();
      return other;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\StringCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */