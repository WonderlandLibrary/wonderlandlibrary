package org.lwjgl.opencl;

import java.util.Iterator;

final class FastLongMap<V> implements Iterable<FastLongMap.Entry<V>> {
  private Entry[] table;
  
  private int size;
  
  private int mask;
  
  private int capacity;
  
  private int threshold;
  
  FastLongMap() {
    this(16, 0.75F);
  }
  
  FastLongMap(int initialCapacity) {
    this(initialCapacity, 0.75F);
  }
  
  FastLongMap(int initialCapacity, float loadFactor) {
    if (initialCapacity > 1073741824)
      throw new IllegalArgumentException("initialCapacity is too large."); 
    if (initialCapacity < 0)
      throw new IllegalArgumentException("initialCapacity must be greater than zero."); 
    if (loadFactor <= 0.0F)
      throw new IllegalArgumentException("initialCapacity must be greater than zero."); 
    this.capacity = 1;
    while (this.capacity < initialCapacity)
      this.capacity <<= 1; 
    this.threshold = (int)(this.capacity * loadFactor);
    this.table = new Entry[this.capacity];
    this.mask = this.capacity - 1;
  }
  
  private int index(long key) {
    return index(key, this.mask);
  }
  
  private static int index(long key, int mask) {
    int hash = (int)(key ^ key >>> 32L);
    return hash & mask;
  }
  
  public V put(long key, V value) {
    Entry[] arrayOfEntry = this.table;
    int index = index(key);
    for (Entry<V> e = arrayOfEntry[index]; e != null; ) {
      if (e.key != key) {
        e = e.next;
        continue;
      } 
      T t = e.value;
      e.value = (T)value;
      return (V)t;
    } 
    arrayOfEntry[index] = new Entry<V>(key, value, arrayOfEntry[index]);
    if (this.size++ >= this.threshold)
      rehash((Entry<V>[])arrayOfEntry); 
    return null;
  }
  
  private void rehash(Entry<V>[] table) {
    int newCapacity = 2 * this.capacity;
    int newMask = newCapacity - 1;
    Entry[] arrayOfEntry = new Entry[newCapacity];
    for (int i = 0; i < table.length; i++) {
      Entry<V> e = table[i];
      if (e != null)
        do {
          Entry<V> next = e.next;
          int index = index(e.key, newMask);
          e.next = arrayOfEntry[index];
          arrayOfEntry[index] = e;
          e = next;
        } while (e != null); 
    } 
    this.table = arrayOfEntry;
    this.capacity = newCapacity;
    this.mask = newMask;
    this.threshold *= 2;
  }
  
  public V get(long key) {
    int index = index(key);
    for (Entry<V> e = this.table[index]; e != null; e = e.next) {
      if (e.key == key)
        return (V)e.value; 
    } 
    return null;
  }
  
  public boolean containsValue(Object value) {
    Entry[] arrayOfEntry = this.table;
    for (int i = arrayOfEntry.length - 1; i >= 0; i--) {
      for (Entry<V> e = arrayOfEntry[i]; e != null; e = e.next) {
        if (e.value.equals(value))
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean containsKey(long key) {
    int index = index(key);
    for (Entry<V> e = this.table[index]; e != null; e = e.next) {
      if (e.key == key)
        return true; 
    } 
    return false;
  }
  
  public V remove(long key) {
    int index = index(key);
    Entry<V> prev = this.table[index];
    Entry<V> e = prev;
    while (e != null) {
      Entry<V> next = e.next;
      if (e.key == key) {
        this.size--;
        if (prev == e) {
          this.table[index] = next;
        } else {
          prev.next = next;
        } 
        return (V)e.value;
      } 
      prev = e;
      e = next;
    } 
    return null;
  }
  
  public int size() {
    return this.size;
  }
  
  public boolean isEmpty() {
    return (this.size == 0);
  }
  
  public void clear() {
    Entry[] arrayOfEntry = this.table;
    for (int index = arrayOfEntry.length - 1; index >= 0; index--)
      arrayOfEntry[index] = null; 
    this.size = 0;
  }
  
  public EntryIterator iterator() {
    return new EntryIterator();
  }
  
  public class EntryIterator implements Iterator<Entry<V>> {
    private int nextIndex;
    
    private FastLongMap.Entry<V> current;
    
    EntryIterator() {
      reset();
    }
    
    public void reset() {
      this.current = null;
      FastLongMap.Entry[] arrayOfEntry = FastLongMap.this.table;
      int i;
      for (i = arrayOfEntry.length - 1; i >= 0 && 
        arrayOfEntry[i] == null; i--);
      this.nextIndex = i;
    }
    
    public boolean hasNext() {
      if (this.nextIndex >= 0)
        return true; 
      FastLongMap.Entry<V> e = this.current;
      return (e != null && e.next != null);
    }
    
    public FastLongMap.Entry<V> next() {
      FastLongMap.Entry<V> e = this.current;
      if (e != null) {
        e = e.next;
        if (e != null) {
          this.current = e;
          return e;
        } 
      } 
      FastLongMap.Entry[] arrayOfEntry = FastLongMap.this.table;
      int i = this.nextIndex;
      e = this.current = arrayOfEntry[i];
      do {
      
      } while (--i >= 0 && 
        arrayOfEntry[i] == null);
      this.nextIndex = i;
      return e;
    }
    
    public void remove() {
      FastLongMap.this.remove(this.current.key);
    }
  }
  
  static final class Entry<T> {
    final long key;
    
    T value;
    
    Entry<T> next;
    
    Entry(long key, T value, Entry<T> next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }
    
    public long getKey() {
      return this.key;
    }
    
    public T getValue() {
      return this.value;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\FastLongMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */