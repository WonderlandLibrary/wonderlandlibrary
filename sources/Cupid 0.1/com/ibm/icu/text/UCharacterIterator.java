package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteratorWrapper;
import com.ibm.icu.impl.ReplaceableUCharacterIterator;
import com.ibm.icu.impl.UCharArrayIterator;
import com.ibm.icu.impl.UCharacterIteratorWrapper;
import com.ibm.icu.impl.UCharacterProperty;
import java.text.CharacterIterator;

public abstract class UCharacterIterator implements Cloneable, UForwardCharacterIterator {
  public static final UCharacterIterator getInstance(Replaceable source) {
    return (UCharacterIterator)new ReplaceableUCharacterIterator(source);
  }
  
  public static final UCharacterIterator getInstance(String source) {
    return (UCharacterIterator)new ReplaceableUCharacterIterator(source);
  }
  
  public static final UCharacterIterator getInstance(char[] source) {
    return getInstance(source, 0, source.length);
  }
  
  public static final UCharacterIterator getInstance(char[] source, int start, int limit) {
    return (UCharacterIterator)new UCharArrayIterator(source, start, limit);
  }
  
  public static final UCharacterIterator getInstance(StringBuffer source) {
    return (UCharacterIterator)new ReplaceableUCharacterIterator(source);
  }
  
  public static final UCharacterIterator getInstance(CharacterIterator source) {
    return (UCharacterIterator)new CharacterIteratorWrapper(source);
  }
  
  public CharacterIterator getCharacterIterator() {
    return (CharacterIterator)new UCharacterIteratorWrapper(this);
  }
  
  public abstract int current();
  
  public int currentCodePoint() {
    int ch = current();
    if (UTF16.isLeadSurrogate((char)ch)) {
      next();
      int ch2 = current();
      previous();
      if (UTF16.isTrailSurrogate((char)ch2))
        return UCharacterProperty.getRawSupplementary((char)ch, (char)ch2); 
    } 
    return ch;
  }
  
  public abstract int getLength();
  
  public abstract int getIndex();
  
  public abstract int next();
  
  public int nextCodePoint() {
    int ch1 = next();
    if (UTF16.isLeadSurrogate((char)ch1)) {
      int ch2 = next();
      if (UTF16.isTrailSurrogate((char)ch2))
        return UCharacterProperty.getRawSupplementary((char)ch1, (char)ch2); 
      if (ch2 != -1)
        previous(); 
    } 
    return ch1;
  }
  
  public abstract int previous();
  
  public int previousCodePoint() {
    int ch1 = previous();
    if (UTF16.isTrailSurrogate((char)ch1)) {
      int ch2 = previous();
      if (UTF16.isLeadSurrogate((char)ch2))
        return UCharacterProperty.getRawSupplementary((char)ch2, (char)ch1); 
      if (ch2 != -1)
        next(); 
    } 
    return ch1;
  }
  
  public abstract void setIndex(int paramInt);
  
  public void setToLimit() {
    setIndex(getLength());
  }
  
  public void setToStart() {
    setIndex(0);
  }
  
  public abstract int getText(char[] paramArrayOfchar, int paramInt);
  
  public final int getText(char[] fillIn) {
    return getText(fillIn, 0);
  }
  
  public String getText() {
    char[] text = new char[getLength()];
    getText(text);
    return new String(text);
  }
  
  public int moveIndex(int delta) {
    int x = Math.max(0, Math.min(getIndex() + delta, getLength()));
    setIndex(x);
    return x;
  }
  
  public int moveCodePointIndex(int delta) {
    if (delta > 0) {
      for (; delta > 0 && nextCodePoint() != -1; delta--);
    } else {
      for (; delta < 0 && previousCodePoint() != -1; delta++);
    } 
    if (delta != 0)
      throw new IndexOutOfBoundsException(); 
    return getIndex();
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UCharacterIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */