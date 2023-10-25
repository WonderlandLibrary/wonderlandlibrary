package com.ibm.icu.impl;

public final class USerializedSet {
  public final boolean getSet(char[] src, int srcStart) {
    this.array = null;
    this.arrayOffset = this.bmpLength = this.length = 0;
    this.length = src[srcStart++];
    if ((this.length & 0x8000) > 0) {
      this.length &= 0x7FFF;
      if (src.length < srcStart + 1 + this.length) {
        this.length = 0;
        throw new IndexOutOfBoundsException();
      } 
      this.bmpLength = src[srcStart++];
    } else {
      if (src.length < srcStart + this.length) {
        this.length = 0;
        throw new IndexOutOfBoundsException();
      } 
      this.bmpLength = this.length;
    } 
    this.array = new char[this.length];
    System.arraycopy(src, srcStart, this.array, 0, this.length);
    return true;
  }
  
  public final void setToOne(int c) {
    if (1114111 < c)
      return; 
    if (c < 65535) {
      this.bmpLength = this.length = 2;
      this.array[0] = (char)c;
      this.array[1] = (char)(c + 1);
    } else if (c == 65535) {
      this.bmpLength = 1;
      this.length = 3;
      this.array[0] = Character.MAX_VALUE;
      this.array[1] = '\001';
      this.array[2] = Character.MIN_VALUE;
    } else if (c < 1114111) {
      this.bmpLength = 0;
      this.length = 4;
      this.array[0] = (char)(c >> 16);
      this.array[1] = (char)c;
      c++;
      this.array[2] = (char)(c >> 16);
      this.array[3] = (char)c;
    } else {
      this.bmpLength = 0;
      this.length = 2;
      this.array[0] = '\020';
      this.array[1] = Character.MAX_VALUE;
    } 
  }
  
  public final boolean getRange(int rangeIndex, int[] range) {
    if (rangeIndex < 0)
      return false; 
    if (this.array == null)
      this.array = new char[8]; 
    if (range == null || range.length < 2)
      throw new IllegalArgumentException(); 
    rangeIndex *= 2;
    if (rangeIndex < this.bmpLength) {
      range[0] = this.array[rangeIndex++];
      if (rangeIndex < this.bmpLength) {
        range[1] = this.array[rangeIndex] - 1;
      } else if (rangeIndex < this.length) {
        range[1] = (this.array[rangeIndex] << 16 | this.array[rangeIndex + 1]) - 1;
      } else {
        range[1] = 1114111;
      } 
      return true;
    } 
    rangeIndex -= this.bmpLength;
    rangeIndex *= 2;
    int suppLength = this.length - this.bmpLength;
    if (rangeIndex < suppLength) {
      int offset = this.arrayOffset + this.bmpLength;
      range[0] = this.array[offset + rangeIndex] << 16 | this.array[offset + rangeIndex + 1];
      rangeIndex += 2;
      if (rangeIndex < suppLength) {
        range[1] = (this.array[offset + rangeIndex] << 16 | this.array[offset + rangeIndex + 1]) - 1;
      } else {
        range[1] = 1114111;
      } 
      return true;
    } 
    return false;
  }
  
  public final boolean contains(int c) {
    if (c > 1114111)
      return false; 
    if (c <= 65535) {
      int j;
      for (j = 0; j < this.bmpLength && (char)c >= this.array[j]; j++);
      return ((j & 0x1) != 0);
    } 
    char high = (char)(c >> 16), low = (char)c;
    int i = this.bmpLength;
    while (i < this.length && (high > this.array[i] || (high == this.array[i] && low >= this.array[i + 1])))
      i += 2; 
    return ((i + this.bmpLength & 0x2) != 0);
  }
  
  public final int countRanges() {
    return (this.bmpLength + (this.length - this.bmpLength) / 2 + 1) / 2;
  }
  
  private char[] array = new char[8];
  
  private int arrayOffset;
  
  private int bmpLength;
  
  private int length;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\USerializedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */