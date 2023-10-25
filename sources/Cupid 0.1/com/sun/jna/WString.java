package com.sun.jna;

import java.nio.CharBuffer;

public final class WString implements CharSequence, Comparable {
  private String string;
  
  public WString(String s) {
    if (s == null)
      throw new NullPointerException("String initializer must be non-null"); 
    this.string = s;
  }
  
  public String toString() {
    return this.string;
  }
  
  public boolean equals(Object o) {
    return (o instanceof WString && toString().equals(o.toString()));
  }
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  public int compareTo(Object o) {
    return toString().compareTo(o.toString());
  }
  
  public int length() {
    return toString().length();
  }
  
  public char charAt(int index) {
    return toString().charAt(index);
  }
  
  public CharSequence subSequence(int start, int end) {
    return CharBuffer.wrap(toString()).subSequence(start, end);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\WString.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */