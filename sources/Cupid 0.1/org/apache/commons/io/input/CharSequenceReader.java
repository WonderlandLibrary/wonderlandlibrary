package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

public class CharSequenceReader extends Reader implements Serializable {
  private final CharSequence charSequence;
  
  private int idx;
  
  private int mark;
  
  public CharSequenceReader(CharSequence charSequence) {
    this.charSequence = (charSequence != null) ? charSequence : "";
  }
  
  public void close() {
    this.idx = 0;
    this.mark = 0;
  }
  
  public void mark(int readAheadLimit) {
    this.mark = this.idx;
  }
  
  public boolean markSupported() {
    return true;
  }
  
  public int read() {
    if (this.idx >= this.charSequence.length())
      return -1; 
    return this.charSequence.charAt(this.idx++);
  }
  
  public int read(char[] array, int offset, int length) {
    if (this.idx >= this.charSequence.length())
      return -1; 
    if (array == null)
      throw new NullPointerException("Character array is missing"); 
    if (length < 0 || offset < 0 || offset + length > array.length)
      throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length); 
    int count = 0;
    for (int i = 0; i < length; i++) {
      int c = read();
      if (c == -1)
        return count; 
      array[offset + i] = (char)c;
      count++;
    } 
    return count;
  }
  
  public void reset() {
    this.idx = this.mark;
  }
  
  public long skip(long n) {
    if (n < 0L)
      throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n); 
    if (this.idx >= this.charSequence.length())
      return -1L; 
    int dest = (int)Math.min(this.charSequence.length(), this.idx + n);
    int count = dest - this.idx;
    this.idx = dest;
    return count;
  }
  
  public String toString() {
    return this.charSequence.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\CharSequenceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */