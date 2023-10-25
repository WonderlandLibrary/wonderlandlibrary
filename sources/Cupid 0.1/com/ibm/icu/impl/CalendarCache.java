package com.ibm.icu.impl;

public class CalendarCache {
  public CalendarCache() {
    this.pIndex = 0;
    this.size = 0;
    this.arraySize = primes[this.pIndex];
    this.threshold = this.arraySize * 3 / 4;
    this.keys = new long[this.arraySize];
    this.values = new long[this.arraySize];
    makeArrays(this.arraySize);
  }
  
  private void makeArrays(int newSize) {
    this.keys = new long[newSize];
    this.values = new long[newSize];
    for (int i = 0; i < newSize; i++)
      this.values[i] = EMPTY; 
    this.arraySize = newSize;
    this.threshold = (int)(this.arraySize * 0.75D);
    this.size = 0;
  }
  
  public synchronized long get(long key) {
    return this.values[findIndex(key)];
  }
  
  public synchronized void put(long key, long value) {
    if (this.size >= this.threshold)
      rehash(); 
    int index = findIndex(key);
    this.keys[index] = key;
    this.values[index] = value;
    this.size++;
  }
  
  private final int findIndex(long key) {
    int index = hash(key);
    int delta = 0;
    while (this.values[index] != EMPTY && this.keys[index] != key) {
      if (delta == 0)
        delta = hash2(key); 
      index = (index + delta) % this.arraySize;
    } 
    return index;
  }
  
  private void rehash() {
    int oldSize = this.arraySize;
    long[] oldKeys = this.keys;
    long[] oldValues = this.values;
    if (this.pIndex < primes.length - 1) {
      this.arraySize = primes[++this.pIndex];
    } else {
      this.arraySize = this.arraySize * 2 + 1;
    } 
    this.size = 0;
    makeArrays(this.arraySize);
    for (int i = 0; i < oldSize; i++) {
      if (oldValues[i] != EMPTY)
        put(oldKeys[i], oldValues[i]); 
    } 
  }
  
  private final int hash(long key) {
    int h = (int)((key * 15821L + 1L) % this.arraySize);
    if (h < 0)
      h += this.arraySize; 
    return h;
  }
  
  private final int hash2(long key) {
    return this.arraySize - 2 - (int)(key % (this.arraySize - 2));
  }
  
  private static final int[] primes = new int[] { 
      61, 127, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 
      131071, 262139 };
  
  private int pIndex;
  
  private int size;
  
  private int arraySize;
  
  private int threshold;
  
  private long[] keys;
  
  private long[] values;
  
  public static long EMPTY = Long.MIN_VALUE;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CalendarCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */