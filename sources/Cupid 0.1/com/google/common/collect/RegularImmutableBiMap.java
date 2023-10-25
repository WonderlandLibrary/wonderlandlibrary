package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
  static final double MAX_LOAD_FACTOR = 1.2D;
  
  private final transient ImmutableMapEntry<K, V>[] keyTable;
  
  private final transient ImmutableMapEntry<K, V>[] valueTable;
  
  private final transient ImmutableMapEntry<K, V>[] entries;
  
  private final transient int mask;
  
  private final transient int hashCode;
  
  private transient ImmutableBiMap<V, K> inverse;
  
  RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry<?, ?>... entriesToAdd) {
    this(entriesToAdd.length, entriesToAdd);
  }
  
  RegularImmutableBiMap(int n, ImmutableMapEntry.TerminalEntry<?, ?>[] entriesToAdd) {
    int tableSize = Hashing.closedTableSize(n, 1.2D);
    this.mask = tableSize - 1;
    ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])createEntryArray(tableSize);
    ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])createEntryArray(tableSize);
    ImmutableMapEntry[] arrayOfImmutableMapEntry3 = (ImmutableMapEntry[])createEntryArray(n);
    int hashCode = 0;
    for (int i = 0; i < n; i++) {
      ImmutableMapEntry.TerminalEntry<?, ?> terminalEntry = entriesToAdd[i];
      K key = (K)terminalEntry.getKey();
      V value = (V)terminalEntry.getValue();
      int keyHash = key.hashCode();
      int valueHash = value.hashCode();
      int keyBucket = Hashing.smear(keyHash) & this.mask;
      int valueBucket = Hashing.smear(valueHash) & this.mask;
      ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
      for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
        keyEntry = keyEntry.getNextInKeyBucket())
        checkNoConflict(!key.equals(keyEntry.getKey()), "key", terminalEntry, keyEntry); 
      ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
      for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
        valueEntry = valueEntry.getNextInValueBucket())
        checkNoConflict(!value.equals(valueEntry.getValue()), "value", terminalEntry, valueEntry); 
      ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? (ImmutableMapEntry)terminalEntry : new NonTerminalBiMapEntry<K, V>((ImmutableMapEntry)terminalEntry, nextInKeyBucket, nextInValueBucket);
      arrayOfImmutableMapEntry1[keyBucket] = newEntry;
      arrayOfImmutableMapEntry2[valueBucket] = newEntry;
      arrayOfImmutableMapEntry3[i] = newEntry;
      hashCode += keyHash ^ valueHash;
    } 
    this.keyTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1;
    this.valueTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2;
    this.entries = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry3;
    this.hashCode = hashCode;
  }
  
  RegularImmutableBiMap(Map.Entry<?, ?>[] entriesToAdd) {
    int n = entriesToAdd.length;
    int tableSize = Hashing.closedTableSize(n, 1.2D);
    this.mask = tableSize - 1;
    ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])createEntryArray(tableSize);
    ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])createEntryArray(tableSize);
    ImmutableMapEntry[] arrayOfImmutableMapEntry3 = (ImmutableMapEntry[])createEntryArray(n);
    int hashCode = 0;
    for (int i = 0; i < n; i++) {
      Map.Entry<?, ?> entry = entriesToAdd[i];
      K key = (K)entry.getKey();
      V value = (V)entry.getValue();
      CollectPreconditions.checkEntryNotNull(key, value);
      int keyHash = key.hashCode();
      int valueHash = value.hashCode();
      int keyBucket = Hashing.smear(keyHash) & this.mask;
      int valueBucket = Hashing.smear(valueHash) & this.mask;
      ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
      for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
        keyEntry = keyEntry.getNextInKeyBucket())
        checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry); 
      ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
      for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
        valueEntry = valueEntry.getNextInValueBucket())
        checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry); 
      ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? new ImmutableMapEntry.TerminalEntry<K, V>(key, value) : new NonTerminalBiMapEntry<K, V>(key, value, nextInKeyBucket, nextInValueBucket);
      arrayOfImmutableMapEntry1[keyBucket] = newEntry;
      arrayOfImmutableMapEntry2[valueBucket] = newEntry;
      arrayOfImmutableMapEntry3[i] = newEntry;
      hashCode += keyHash ^ valueHash;
    } 
    this.keyTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1;
    this.valueTable = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2;
    this.entries = (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry3;
    this.hashCode = hashCode;
  }
  
  private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V> {
    @Nullable
    private final ImmutableMapEntry<K, V> nextInKeyBucket;
    
    @Nullable
    private final ImmutableMapEntry<K, V> nextInValueBucket;
    
    NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
      super(key, value);
      this.nextInKeyBucket = nextInKeyBucket;
      this.nextInValueBucket = nextInValueBucket;
    }
    
    NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
      super(contents);
      this.nextInKeyBucket = nextInKeyBucket;
      this.nextInValueBucket = nextInValueBucket;
    }
    
    @Nullable
    ImmutableMapEntry<K, V> getNextInKeyBucket() {
      return this.nextInKeyBucket;
    }
    
    @Nullable
    ImmutableMapEntry<K, V> getNextInValueBucket() {
      return this.nextInValueBucket;
    }
  }
  
  private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) {
    return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[length];
  }
  
  @Nullable
  public V get(@Nullable Object key) {
    if (key == null)
      return null; 
    int bucket = Hashing.smear(key.hashCode()) & this.mask;
    for (ImmutableMapEntry<K, V> entry = this.keyTable[bucket]; entry != null; 
      entry = entry.getNextInKeyBucket()) {
      if (key.equals(entry.getKey()))
        return entry.getValue(); 
    } 
    return null;
  }
  
  ImmutableSet<Map.Entry<K, V>> createEntrySet() {
    return new ImmutableMapEntrySet<K, V>() {
        ImmutableMap<K, V> map() {
          return RegularImmutableBiMap.this;
        }
        
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
          return asList().iterator();
        }
        
        ImmutableList<Map.Entry<K, V>> createAsList() {
          return new RegularImmutableAsList<Map.Entry<K, V>>(this, (Object[])RegularImmutableBiMap.this.entries);
        }
        
        boolean isHashCodeFast() {
          return true;
        }
        
        public int hashCode() {
          return RegularImmutableBiMap.this.hashCode;
        }
      };
  }
  
  boolean isPartialView() {
    return false;
  }
  
  public int size() {
    return this.entries.length;
  }
  
  public ImmutableBiMap<V, K> inverse() {
    ImmutableBiMap<V, K> result = this.inverse;
    return (result == null) ? (this.inverse = new Inverse()) : result;
  }
  
  private final class Inverse extends ImmutableBiMap<V, K> {
    private Inverse() {}
    
    public int size() {
      return inverse().size();
    }
    
    public ImmutableBiMap<K, V> inverse() {
      return RegularImmutableBiMap.this;
    }
    
    public K get(@Nullable Object value) {
      if (value == null)
        return null; 
      int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
      for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; 
        entry = entry.getNextInValueBucket()) {
        if (value.equals(entry.getValue()))
          return entry.getKey(); 
      } 
      return null;
    }
    
    ImmutableSet<Map.Entry<V, K>> createEntrySet() {
      return new InverseEntrySet();
    }
    
    final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
      ImmutableMap<V, K> map() {
        return RegularImmutableBiMap.Inverse.this;
      }
      
      boolean isHashCodeFast() {
        return true;
      }
      
      public int hashCode() {
        return RegularImmutableBiMap.this.hashCode;
      }
      
      public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
        return asList().iterator();
      }
      
      ImmutableList<Map.Entry<V, K>> createAsList() {
        return new ImmutableAsList<Map.Entry<V, K>>() {
            public Map.Entry<V, K> get(int index) {
              Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
              return Maps.immutableEntry(entry.getValue(), entry.getKey());
            }
            
            ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
              return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
            }
          };
      }
    }
    
    boolean isPartialView() {
      return false;
    }
    
    Object writeReplace() {
      return new RegularImmutableBiMap.InverseSerializedForm<Object, Object>(RegularImmutableBiMap.this);
    }
  }
  
  private static class InverseSerializedForm<K, V> implements Serializable {
    private final ImmutableBiMap<K, V> forward;
    
    private static final long serialVersionUID = 1L;
    
    InverseSerializedForm(ImmutableBiMap<K, V> forward) {
      this.forward = forward;
    }
    
    Object readResolve() {
      return this.forward.inverse();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */