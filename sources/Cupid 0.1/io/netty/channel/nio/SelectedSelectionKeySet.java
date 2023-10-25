package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Iterator;

final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {
  private SelectionKey[] keysA;
  
  private int keysASize;
  
  private SelectionKey[] keysB;
  
  private int keysBSize;
  
  private boolean isA = true;
  
  SelectedSelectionKeySet() {
    this.keysA = new SelectionKey[1024];
    this.keysB = (SelectionKey[])this.keysA.clone();
  }
  
  public boolean add(SelectionKey o) {
    if (o == null)
      return false; 
    if (this.isA) {
      int size = this.keysASize;
      this.keysA[size++] = o;
      this.keysASize = size;
      if (size == this.keysA.length)
        doubleCapacityA(); 
    } else {
      int size = this.keysBSize;
      this.keysB[size++] = o;
      this.keysBSize = size;
      if (size == this.keysB.length)
        doubleCapacityB(); 
    } 
    return true;
  }
  
  private void doubleCapacityA() {
    SelectionKey[] newKeysA = new SelectionKey[this.keysA.length << 1];
    System.arraycopy(this.keysA, 0, newKeysA, 0, this.keysASize);
    this.keysA = newKeysA;
  }
  
  private void doubleCapacityB() {
    SelectionKey[] newKeysB = new SelectionKey[this.keysB.length << 1];
    System.arraycopy(this.keysB, 0, newKeysB, 0, this.keysBSize);
    this.keysB = newKeysB;
  }
  
  SelectionKey[] flip() {
    if (this.isA) {
      this.isA = false;
      this.keysA[this.keysASize] = null;
      this.keysBSize = 0;
      return this.keysA;
    } 
    this.isA = true;
    this.keysB[this.keysBSize] = null;
    this.keysASize = 0;
    return this.keysB;
  }
  
  public int size() {
    if (this.isA)
      return this.keysASize; 
    return this.keysBSize;
  }
  
  public boolean remove(Object o) {
    return false;
  }
  
  public boolean contains(Object o) {
    return false;
  }
  
  public Iterator<SelectionKey> iterator() {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\nio\SelectedSelectionKeySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */