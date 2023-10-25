package org.apache.commons.compress.archivers.sevenz;

import java.util.LinkedList;

class Folder {
  Coder[] coders;
  
  long totalInputStreams;
  
  long totalOutputStreams;
  
  BindPair[] bindPairs;
  
  long[] packedStreams;
  
  long[] unpackSizes;
  
  boolean hasCrc;
  
  long crc;
  
  int numUnpackSubStreams;
  
  Iterable<Coder> getOrderedCoders() {
    LinkedList<Coder> l = new LinkedList<Coder>();
    int current = (int)this.packedStreams[0];
    while (current != -1) {
      l.addLast(this.coders[current]);
      int pair = findBindPairForOutStream(current);
      current = (pair != -1) ? (int)(this.bindPairs[pair]).inIndex : -1;
    } 
    return l;
  }
  
  int findBindPairForInStream(int index) {
    for (int i = 0; i < this.bindPairs.length; i++) {
      if ((this.bindPairs[i]).inIndex == index)
        return i; 
    } 
    return -1;
  }
  
  int findBindPairForOutStream(int index) {
    for (int i = 0; i < this.bindPairs.length; i++) {
      if ((this.bindPairs[i]).outIndex == index)
        return i; 
    } 
    return -1;
  }
  
  long getUnpackSize() {
    if (this.totalOutputStreams == 0L)
      return 0L; 
    for (int i = (int)this.totalOutputStreams - 1; i >= 0; i--) {
      if (findBindPairForOutStream(i) < 0)
        return this.unpackSizes[i]; 
    } 
    return 0L;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\Folder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */