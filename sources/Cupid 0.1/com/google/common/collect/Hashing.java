package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
final class Hashing {
  private static final int C1 = -862048943;
  
  private static final int C2 = 461845907;
  
  static int smear(int hashCode) {
    return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
  }
  
  static int smearedHash(@Nullable Object o) {
    return smear((o == null) ? 0 : o.hashCode());
  }
  
  private static int MAX_TABLE_SIZE = 1073741824;
  
  static int closedTableSize(int expectedEntries, double loadFactor) {
    expectedEntries = Math.max(expectedEntries, 2);
    int tableSize = Integer.highestOneBit(expectedEntries);
    if (expectedEntries > (int)(loadFactor * tableSize)) {
      tableSize <<= 1;
      return (tableSize > 0) ? tableSize : MAX_TABLE_SIZE;
    } 
    return tableSize;
  }
  
  static boolean needsResizing(int size, int tableSize, double loadFactor) {
    return (size > loadFactor * tableSize && tableSize < MAX_TABLE_SIZE);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Hashing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */