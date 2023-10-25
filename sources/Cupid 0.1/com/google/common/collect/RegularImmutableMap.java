package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
  private final transient ImmutableMapEntry<K, V>[] entries;
  
  private final transient ImmutableMapEntry<K, V>[] table;
  
  private final transient int mask;
  
  private static final double MAX_LOAD_FACTOR = 1.2D;
  
  private static final long serialVersionUID = 0L;
  
  RegularImmutableMap(ImmutableMapEntry.TerminalEntry<?, ?>... theEntries) {
    this(theEntries.length, theEntries);
  }
  
  RegularImmutableMap(int size, ImmutableMapEntry.TerminalEntry<?, ?>[] theEntries) {
    this.entries = createEntryArray(size);
    int tableSize = Hashing.closedTableSize(size, 1.2D);
    this.table = createEntryArray(tableSize);
    this.mask = tableSize - 1;
    for (int entryIndex = 0; entryIndex < size; entryIndex++) {
      ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = theEntries[entryIndex];
      K key = (K)terminalEntry.getKey();
      int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
      ImmutableMapEntry<K, V> existing = this.table[tableIndex];
      ImmutableMapEntry<K, V> newEntry = (existing == null) ? (ImmutableMapEntry)terminalEntry : new NonTerminalMapEntry<K, V>((ImmutableMapEntry)terminalEntry, existing);
      this.table[tableIndex] = newEntry;
      this.entries[entryIndex] = newEntry;
      checkNoConflictInBucket(key, newEntry, existing);
    } 
  }
  
  RegularImmutableMap(Map.Entry<?, ?>[] theEntries) {
    int size = theEntries.length;
    this.entries = createEntryArray(size);
    int tableSize = Hashing.closedTableSize(size, 1.2D);
    this.table = createEntryArray(tableSize);
    this.mask = tableSize - 1;
    for (int entryIndex = 0; entryIndex < size; entryIndex++) {
      Map.Entry<?, ?> entry = theEntries[entryIndex];
      K key = (K)entry.getKey();
      V value = (V)entry.getValue();
      CollectPreconditions.checkEntryNotNull(key, value);
      int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
      ImmutableMapEntry<K, V> existing = this.table[tableIndex];
      ImmutableMapEntry<K, V> newEntry = (existing == null) ? new ImmutableMapEntry.TerminalEntry<K, V>(key, value) : new NonTerminalMapEntry<K, V>(key, value, existing);
      this.table[tableIndex] = newEntry;
      this.entries[entryIndex] = newEntry;
      checkNoConflictInBucket(key, newEntry, existing);
    } 
  }
  
  private void checkNoConflictInBucket(K key, ImmutableMapEntry<K, V> entry, ImmutableMapEntry<K, V> bucketHead) {
    for (; bucketHead != null; bucketHead = bucketHead.getNextInKeyBucket())
      checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead); 
  }
  
  private static final class NonTerminalMapEntry<K, V> extends ImmutableMapEntry<K, V> {
    private final ImmutableMapEntry<K, V> nextInKeyBucket;
    
    NonTerminalMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
      super(key, value);
      this.nextInKeyBucket = nextInKeyBucket;
    }
    
    NonTerminalMapEntry(ImmutableMapEntry<K, V> contents, ImmutableMapEntry<K, V> nextInKeyBucket) {
      super(contents);
      this.nextInKeyBucket = nextInKeyBucket;
    }
    
    ImmutableMapEntry<K, V> getNextInKeyBucket() {
      return this.nextInKeyBucket;
    }
    
    @Nullable
    ImmutableMapEntry<K, V> getNextInValueBucket() {
      return null;
    }
  }
  
  private ImmutableMapEntry<K, V>[] createEntryArray(int size) {
    return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
  }
  
  public V get(@Nullable Object key) {
    if (key == null)
      return null; 
    int index = Hashing.smear(key.hashCode()) & this.mask;
    ImmutableMapEntry<K, V> entry = this.table[index];
    for (; entry != null; 
      entry = entry.getNextInKeyBucket()) {
      K candidateKey = entry.getKey();
      if (key.equals(candidateKey))
        return entry.getValue(); 
    } 
    return null;
  }
  
  public int size() {
    return this.entries.length;
  }
  
  boolean isPartialView() {
    return false;
  }
  
  ImmutableSet<Map.Entry<K, V>> createEntrySet() {
    return new EntrySet();
  }
  
  private class EntrySet extends ImmutableMapEntrySet<K, V> {
    private EntrySet() {}
    
    ImmutableMap<K, V> map() {
      return RegularImmutableMap.this;
    }
    
    public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
      return asList().iterator();
    }
    
    ImmutableList<Map.Entry<K, V>> createAsList() {
      return new RegularImmutableAsList<Map.Entry<K, V>>(this, (Object[])RegularImmutableMap.this.entries);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */