package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.BitSet;

@GwtIncompatible("no precomputation is done in GWT")
final class SmallCharMatcher extends CharMatcher.FastMatcher {
  static final int MAX_SIZE = 1023;
  
  private final char[] table;
  
  private final boolean containsZero;
  
  private final long filter;
  
  private static final int C1 = -862048943;
  
  private static final int C2 = 461845907;
  
  private static final double DESIRED_LOAD_FACTOR = 0.5D;
  
  private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
    super(description);
    this.table = table;
    this.filter = filter;
    this.containsZero = containsZero;
  }
  
  static int smear(int hashCode) {
    return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
  }
  
  private boolean checkFilter(int c) {
    return (1L == (0x1L & this.filter >> c));
  }
  
  @VisibleForTesting
  static int chooseTableSize(int setSize) {
    if (setSize == 1)
      return 2; 
    int tableSize = Integer.highestOneBit(setSize - 1) << 1;
    while (tableSize * 0.5D < setSize)
      tableSize <<= 1; 
    return tableSize;
  }
  
  static CharMatcher from(BitSet chars, String description) {
    long filter = 0L;
    int size = chars.cardinality();
    boolean containsZero = chars.get(0);
    char[] table = new char[chooseTableSize(size)];
    int mask = table.length - 1;
    int c;
    for (c = chars.nextSetBit(0); c != -1; ) {
      filter |= 1L << c;
      int index = smear(c) & mask;
      for (;; c = chars.nextSetBit(c + 1)) {
        if (table[index] == '\000') {
          table[index] = (char)c;
        } else {
          index = index + 1 & mask;
          continue;
        } 
      } 
    } 
    return new SmallCharMatcher(table, filter, containsZero, description);
  }
  
  public boolean matches(char c) {
    if (c == '\000')
      return this.containsZero; 
    if (!checkFilter(c))
      return false; 
    int mask = this.table.length - 1;
    int startingIndex = smear(c) & mask;
    int index = startingIndex;
    while (true) {
      if (this.table[index] == '\000')
        return false; 
      if (this.table[index] == c)
        return true; 
      index = index + 1 & mask;
      if (index == startingIndex)
        return false; 
    } 
  }
  
  void setBits(BitSet table) {
    if (this.containsZero)
      table.set(0); 
    for (char c : this.table) {
      if (c != '\000')
        table.set(c); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\SmallCharMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */