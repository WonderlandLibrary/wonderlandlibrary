package org.apache.http.util;

import java.io.Serializable;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HTTP;

@NotThreadSafe
public final class CharArrayBuffer implements Serializable {
  private static final long serialVersionUID = -6208952725094867135L;
  
  private char[] buffer;
  
  private int len;
  
  public CharArrayBuffer(int capacity) {
    Args.notNegative(capacity, "Buffer capacity");
    this.buffer = new char[capacity];
  }
  
  private void expand(int newlen) {
    char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
    System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
    this.buffer = newbuffer;
  }
  
  public void append(char[] b, int off, int len) {
    if (b == null)
      return; 
    if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
      throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length); 
    if (len == 0)
      return; 
    int newlen = this.len + len;
    if (newlen > this.buffer.length)
      expand(newlen); 
    System.arraycopy(b, off, this.buffer, this.len, len);
    this.len = newlen;
  }
  
  public void append(String str) {
    String s = (str != null) ? str : "null";
    int strlen = s.length();
    int newlen = this.len + strlen;
    if (newlen > this.buffer.length)
      expand(newlen); 
    s.getChars(0, strlen, this.buffer, this.len);
    this.len = newlen;
  }
  
  public void append(CharArrayBuffer b, int off, int len) {
    if (b == null)
      return; 
    append(b.buffer, off, len);
  }
  
  public void append(CharArrayBuffer b) {
    if (b == null)
      return; 
    append(b.buffer, 0, b.len);
  }
  
  public void append(char ch) {
    int newlen = this.len + 1;
    if (newlen > this.buffer.length)
      expand(newlen); 
    this.buffer[this.len] = ch;
    this.len = newlen;
  }
  
  public void append(byte[] b, int off, int len) {
    if (b == null)
      return; 
    if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
      throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length); 
    if (len == 0)
      return; 
    int oldlen = this.len;
    int newlen = oldlen + len;
    if (newlen > this.buffer.length)
      expand(newlen); 
    for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++)
      this.buffer[i2] = (char)(b[i1] & 0xFF); 
    this.len = newlen;
  }
  
  public void append(ByteArrayBuffer b, int off, int len) {
    if (b == null)
      return; 
    append(b.buffer(), off, len);
  }
  
  public void append(Object obj) {
    append(String.valueOf(obj));
  }
  
  public void clear() {
    this.len = 0;
  }
  
  public char[] toCharArray() {
    char[] b = new char[this.len];
    if (this.len > 0)
      System.arraycopy(this.buffer, 0, b, 0, this.len); 
    return b;
  }
  
  public char charAt(int i) {
    return this.buffer[i];
  }
  
  public char[] buffer() {
    return this.buffer;
  }
  
  public int capacity() {
    return this.buffer.length;
  }
  
  public int length() {
    return this.len;
  }
  
  public void ensureCapacity(int required) {
    if (required <= 0)
      return; 
    int available = this.buffer.length - this.len;
    if (required > available)
      expand(this.len + required); 
  }
  
  public void setLength(int len) {
    if (len < 0 || len > this.buffer.length)
      throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length); 
    this.len = len;
  }
  
  public boolean isEmpty() {
    return (this.len == 0);
  }
  
  public boolean isFull() {
    return (this.len == this.buffer.length);
  }
  
  public int indexOf(int ch, int from, int to) {
    int beginIndex = from;
    if (beginIndex < 0)
      beginIndex = 0; 
    int endIndex = to;
    if (endIndex > this.len)
      endIndex = this.len; 
    if (beginIndex > endIndex)
      return -1; 
    for (int i = beginIndex; i < endIndex; i++) {
      if (this.buffer[i] == ch)
        return i; 
    } 
    return -1;
  }
  
  public int indexOf(int ch) {
    return indexOf(ch, 0, this.len);
  }
  
  public String substring(int beginIndex, int endIndex) {
    return new String(this.buffer, beginIndex, endIndex - beginIndex);
  }
  
  public String substringTrimmed(int from, int to) {
    int beginIndex = from;
    int endIndex = to;
    if (beginIndex < 0)
      throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex); 
    if (endIndex > this.len)
      throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len); 
    if (beginIndex > endIndex)
      throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex); 
    while (beginIndex < endIndex && HTTP.isWhitespace(this.buffer[beginIndex]))
      beginIndex++; 
    while (endIndex > beginIndex && HTTP.isWhitespace(this.buffer[endIndex - 1]))
      endIndex--; 
    return new String(this.buffer, beginIndex, endIndex - beginIndex);
  }
  
  public String toString() {
    return new String(this.buffer, 0, this.len);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\htt\\util\CharArrayBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */