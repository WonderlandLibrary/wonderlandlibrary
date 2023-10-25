package com.sun.jna;

import java.nio.CharBuffer;

class NativeString implements CharSequence, Comparable {
  private Pointer pointer;
  
  private boolean wide;
  
  public NativeString(String string) {
    this(string, false);
  }
  
  public NativeString(String string, boolean wide) {
    if (string == null)
      throw new NullPointerException("String must not be null"); 
    this.wide = wide;
    if (wide) {
      int len = (string.length() + 1) * Native.WCHAR_SIZE;
      this.pointer = new Memory(len);
      this.pointer.setString(0L, string, true);
    } else {
      byte[] data = Native.getBytes(string);
      this.pointer = new Memory((data.length + 1));
      this.pointer.write(0L, data, 0, data.length);
      this.pointer.setByte(data.length, (byte)0);
    } 
  }
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  public boolean equals(Object other) {
    if (other instanceof CharSequence)
      return (compareTo(other) == 0); 
    return false;
  }
  
  public String toString() {
    String s = this.wide ? "const wchar_t*" : "const char*";
    s = s + "(" + this.pointer.getString(0L, this.wide) + ")";
    return s;
  }
  
  public Pointer getPointer() {
    return this.pointer;
  }
  
  public char charAt(int index) {
    return toString().charAt(index);
  }
  
  public int length() {
    return toString().length();
  }
  
  public CharSequence subSequence(int start, int end) {
    return CharBuffer.wrap(toString()).subSequence(start, end);
  }
  
  public int compareTo(Object other) {
    if (other == null)
      return 1; 
    return toString().compareTo(other.toString());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\NativeString.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */