package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

public final class CompactCharArray implements Cloneable {
  public static final int UNICODECOUNT = 65536;
  
  public static final int BLOCKSHIFT = 5;
  
  static final int BLOCKCOUNT = 32;
  
  static final int INDEXSHIFT = 11;
  
  static final int INDEXCOUNT = 2048;
  
  static final int BLOCKMASK = 31;
  
  private char[] values;
  
  private char[] indices;
  
  private int[] hashes;
  
  private boolean isCompact;
  
  char defaultValue;
  
  public CompactCharArray() {
    this(false);
  }
  
  public CompactCharArray(char defaultValue) {
    this.values = new char[65536];
    this.indices = new char[2048];
    this.hashes = new int[2048];
    int i;
    for (i = 0; i < 65536; i++)
      this.values[i] = defaultValue; 
    for (i = 0; i < 2048; i++) {
      this.indices[i] = (char)(i << 5);
      this.hashes[i] = 0;
    } 
    this.isCompact = false;
    this.defaultValue = defaultValue;
  }
  
  public CompactCharArray(char[] indexArray, char[] newValues) {
    if (indexArray.length != 2048)
      throw new IllegalArgumentException("Index out of bounds."); 
    for (int i = 0; i < 2048; i++) {
      char index = indexArray[i];
      if (index < '\000' || index >= newValues.length + 32)
        throw new IllegalArgumentException("Index out of bounds."); 
    } 
    this.indices = indexArray;
    this.values = newValues;
    this.isCompact = true;
  }
  
  public CompactCharArray(String indexArray, String valueArray) {
    this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToCharArray(valueArray));
  }
  
  public char elementAt(char index) {
    int ix = (this.indices[index >> 5] & Character.MAX_VALUE) + (index & 0x1F);
    return (ix >= this.values.length) ? this.defaultValue : this.values[ix];
  }
  
  public void setElementAt(char index, char value) {
    if (this.isCompact)
      expand(); 
    this.values[index] = value;
    touchBlock(index >> 5, value);
  }
  
  public void setElementAt(char start, char end, char value) {
    if (this.isCompact)
      expand(); 
    for (int i = start; i <= end; i++) {
      this.values[i] = value;
      touchBlock(i >> 5, value);
    } 
  }
  
  public void compact() {
    compact(true);
  }
  
  public void compact(boolean exhaustive) {
    if (!this.isCompact) {
      int iBlockStart = 0;
      char iUntouched = Character.MAX_VALUE;
      int newSize = 0;
      char[] target = exhaustive ? new char[65536] : this.values;
      for (int i = 0; i < this.indices.length; i++, iBlockStart += 32) {
        this.indices[i] = Character.MAX_VALUE;
        boolean touched = blockTouched(i);
        if (!touched && iUntouched != Character.MAX_VALUE) {
          this.indices[i] = iUntouched;
        } else {
          int jBlockStart = 0;
          for (int j = 0; j < i; j++, jBlockStart += 32) {
            if (this.hashes[i] == this.hashes[j] && arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 32))
              this.indices[i] = this.indices[j]; 
          } 
          if (this.indices[i] == Character.MAX_VALUE) {
            int dest;
            if (exhaustive) {
              dest = FindOverlappingPosition(iBlockStart, target, newSize);
            } else {
              dest = newSize;
            } 
            int limit = dest + 32;
            if (limit > newSize) {
              for (int k = newSize; k < limit; k++)
                target[k] = this.values[iBlockStart + k - dest]; 
              newSize = limit;
            } 
            this.indices[i] = (char)dest;
            if (!touched)
              iUntouched = (char)jBlockStart; 
          } 
        } 
      } 
      char[] result = new char[newSize];
      System.arraycopy(target, 0, result, 0, newSize);
      this.values = result;
      this.isCompact = true;
      this.hashes = null;
    } 
  }
  
  private int FindOverlappingPosition(int start, char[] tempValues, int tempCount) {
    for (int i = 0; i < tempCount; i++) {
      int currentCount = 32;
      if (i + 32 > tempCount)
        currentCount = tempCount - i; 
      if (arrayRegionMatches(this.values, start, tempValues, i, currentCount))
        return i; 
    } 
    return tempCount;
  }
  
  static final boolean arrayRegionMatches(char[] source, int sourceStart, char[] target, int targetStart, int len) {
    int sourceEnd = sourceStart + len;
    int delta = targetStart - sourceStart;
    for (int i = sourceStart; i < sourceEnd; i++) {
      if (source[i] != target[i + delta])
        return false; 
    } 
    return true;
  }
  
  private final void touchBlock(int i, int value) {
    this.hashes[i] = this.hashes[i] + (value << 1) | 0x1;
  }
  
  private final boolean blockTouched(int i) {
    return (this.hashes[i] != 0);
  }
  
  public char[] getIndexArray() {
    return this.indices;
  }
  
  public char[] getValueArray() {
    return this.values;
  }
  
  public Object clone() {
    try {
      CompactCharArray other = (CompactCharArray)super.clone();
      other.values = (char[])this.values.clone();
      other.indices = (char[])this.indices.clone();
      if (this.hashes != null)
        other.hashes = (int[])this.hashes.clone(); 
      return other;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    } 
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (this == obj)
      return true; 
    if (getClass() != obj.getClass())
      return false; 
    CompactCharArray other = (CompactCharArray)obj;
    for (int i = 0; i < 65536; i++) {
      if (elementAt((char)i) != other.elementAt((char)i))
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    int result = 0;
    int increment = Math.min(3, this.values.length / 16);
    for (int i = 0; i < this.values.length; i += increment)
      result = result * 37 + this.values[i]; 
    return result;
  }
  
  private void expand() {
    if (this.isCompact) {
      this.hashes = new int[2048];
      char[] tempArray = new char[65536];
      int i;
      for (i = 0; i < 65536; i++)
        tempArray[i] = elementAt((char)i); 
      for (i = 0; i < 2048; i++)
        this.indices[i] = (char)(i << 5); 
      this.values = null;
      this.values = tempArray;
      this.isCompact = false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\CompactCharArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */