package com.ibm.icu.impl;

public final class Differ<T> {
  private int STACKSIZE;
  
  private int EQUALSIZE;
  
  private T[] a;
  
  private T[] b;
  
  private T last;
  
  private T next;
  
  private int aCount;
  
  private int bCount;
  
  private int aLine;
  
  private int bLine;
  
  private int maxSame;
  
  private int aTop;
  
  private int bTop;
  
  public Differ(int stackSize, int matchCount) {
    this.last = null;
    this.next = null;
    this.aCount = 0;
    this.bCount = 0;
    this.aLine = 1;
    this.bLine = 1;
    this.maxSame = 0;
    this.aTop = 0;
    this.bTop = 0;
    this.STACKSIZE = stackSize;
    this.EQUALSIZE = matchCount;
    this.a = (T[])new Object[stackSize + matchCount];
    this.b = (T[])new Object[stackSize + matchCount];
  }
  
  public void add(T aStr, T bStr) {
    addA(aStr);
    addB(bStr);
  }
  
  public void addA(T aStr) {
    flush();
    this.a[this.aCount++] = aStr;
  }
  
  public void addB(T bStr) {
    flush();
    this.b[this.bCount++] = bStr;
  }
  
  public int getALine(int offset) {
    return this.aLine + this.maxSame + offset;
  }
  
  public T getA(int offset) {
    if (offset < 0)
      return this.last; 
    if (offset > this.aTop - this.maxSame)
      return this.next; 
    return this.a[offset];
  }
  
  public int getACount() {
    return this.aTop - this.maxSame;
  }
  
  public int getBCount() {
    return this.bTop - this.maxSame;
  }
  
  public int getBLine(int offset) {
    return this.bLine + this.maxSame + offset;
  }
  
  public T getB(int offset) {
    if (offset < 0)
      return this.last; 
    if (offset > this.bTop - this.maxSame)
      return this.next; 
    return this.b[offset];
  }
  
  public void checkMatch(boolean finalPass) {
    int max = this.aCount;
    if (max > this.bCount)
      max = this.bCount; 
    int i;
    for (i = 0; i < max && this.a[i].equals(this.b[i]); i++);
    this.maxSame = i;
    this.aTop = this.bTop = this.maxSame;
    if (this.maxSame > 0)
      this.last = this.a[this.maxSame - 1]; 
    this.next = null;
    if (finalPass) {
      this.aTop = this.aCount;
      this.bTop = this.bCount;
      this.next = null;
      return;
    } 
    if (this.aCount - this.maxSame < this.EQUALSIZE || this.bCount - this.maxSame < this.EQUALSIZE)
      return; 
    int match = find(this.a, this.aCount - this.EQUALSIZE, this.aCount, this.b, this.maxSame, this.bCount);
    if (match != -1) {
      this.aTop = this.aCount - this.EQUALSIZE;
      this.bTop = match;
      this.next = this.a[this.aTop];
      return;
    } 
    match = find(this.b, this.bCount - this.EQUALSIZE, this.bCount, this.a, this.maxSame, this.aCount);
    if (match != -1) {
      this.bTop = this.bCount - this.EQUALSIZE;
      this.aTop = match;
      this.next = this.b[this.bTop];
      return;
    } 
    if (this.aCount >= this.STACKSIZE || this.bCount >= this.STACKSIZE) {
      this.aCount = (this.aCount + this.maxSame) / 2;
      this.bCount = (this.bCount + this.maxSame) / 2;
      this.next = null;
    } 
  }
  
  public int find(T[] aArr, int aStart, int aEnd, T[] bArr, int bStart, int bEnd) {
    int len = aEnd - aStart;
    int bEndMinus = bEnd - len;
    for (int i = bStart; i <= bEndMinus; i++) {
      int j = 0;
      while (true) {
        if (j < len) {
          if (!bArr[i + j].equals(aArr[aStart + j]))
            break; 
          j++;
          continue;
        } 
        return i;
      } 
    } 
    return -1;
  }
  
  private void flush() {
    if (this.aTop != 0) {
      int newCount = this.aCount - this.aTop;
      System.arraycopy(this.a, this.aTop, this.a, 0, newCount);
      this.aCount = newCount;
      this.aLine += this.aTop;
      this.aTop = 0;
    } 
    if (this.bTop != 0) {
      int newCount = this.bCount - this.bTop;
      System.arraycopy(this.b, this.bTop, this.b, 0, newCount);
      this.bCount = newCount;
      this.bLine += this.bTop;
      this.bTop = 0;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Differ.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */