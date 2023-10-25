package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
  private static final double LOAD_FACTOR = 1.0D;
  
  private transient BiEntry<K, V>[] hashTableKToV;
  
  private transient BiEntry<K, V>[] hashTableVToK;
  
  private transient int size;
  
  private transient int mask;
  
  private transient int modCount;
  
  private transient BiMap<V, K> inverse;
  
  @GwtIncompatible("Not needed in emulated source")
  private static final long serialVersionUID = 0L;
  
  public static <K, V> HashBiMap<K, V> create() {
    return create(16);
  }
  
  public static <K, V> HashBiMap<K, V> create(int expectedSize) {
    return new HashBiMap<K, V>(expectedSize);
  }
  
  public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
    HashBiMap<K, V> bimap = create(map.size());
    bimap.putAll(map);
    return bimap;
  }
  
  private static final class BiEntry<K, V> extends ImmutableEntry<K, V> {
    final int keyHash;
    
    final int valueHash;
    
    @Nullable
    BiEntry<K, V> nextInKToVBucket;
    
    @Nullable
    BiEntry<K, V> nextInVToKBucket;
    
    BiEntry(K key, int keyHash, V value, int valueHash) {
      super(key, value);
      this.keyHash = keyHash;
      this.valueHash = valueHash;
    }
  }
  
  private HashBiMap(int expectedSize) {
    init(expectedSize);
  }
  
  private void init(int expectedSize) {
    CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
    int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
    this.hashTableKToV = createTable(tableSize);
    this.hashTableVToK = createTable(tableSize);
    this.mask = tableSize - 1;
    this.modCount = 0;
    this.size = 0;
  }
  
  private void delete(BiEntry<K, V> entry) {
    int keyBucket = entry.keyHash & this.mask;
    BiEntry<K, V> prevBucketEntry = null;
    BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
    for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
      if (bucketEntry == entry) {
        if (prevBucketEntry == null) {
          this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
          break;
        } 
        prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
        break;
      } 
      prevBucketEntry = bucketEntry;
    } 
    int valueBucket = entry.valueHash & this.mask;
    prevBucketEntry = null;
    BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
    for (;; biEntry1 = biEntry1.nextInVToKBucket) {
      if (biEntry1 == entry) {
        if (prevBucketEntry == null) {
          this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
          break;
        } 
        prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
        break;
      } 
      prevBucketEntry = biEntry1;
    } 
    this.size--;
    this.modCount++;
  }
  
  private void insert(BiEntry<K, V> entry) {
    int keyBucket = entry.keyHash & this.mask;
    entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
    this.hashTableKToV[keyBucket] = entry;
    int valueBucket = entry.valueHash & this.mask;
    entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
    this.hashTableVToK[valueBucket] = entry;
    this.size++;
    this.modCount++;
  }
  
  private static int hash(@Nullable Object o) {
    return Hashing.smear((o == null) ? 0 : o.hashCode());
  }
  
  private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
    for (BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask]; entry != null; 
      entry = entry.nextInKToVBucket) {
      if (keyHash == entry.keyHash && Objects.equal(key, entry.key))
        return entry; 
    } 
    return null;
  }
  
  private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
    for (BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask]; entry != null; 
      entry = entry.nextInVToKBucket) {
      if (valueHash == entry.valueHash && Objects.equal(value, entry.value))
        return entry; 
    } 
    return null;
  }
  
  public boolean containsKey(@Nullable Object key) {
    return (seekByKey(key, hash(key)) != null);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return (seekByValue(value, hash(value)) != null);
  }
  
  @Nullable
  public V get(@Nullable Object key) {
    BiEntry<K, V> entry = seekByKey(key, hash(key));
    return (entry == null) ? null : entry.value;
  }
  
  public V put(@Nullable K key, @Nullable V value) {
    return put(key, value, false);
  }
  
  public V forcePut(@Nullable K key, @Nullable V value) {
    return put(key, value, true);
  }
  
  private V put(@Nullable K key, @Nullable V value, boolean force) {
    int keyHash = hash(key);
    int valueHash = hash(value);
    BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
    if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value))
      return value; 
    BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
    if (oldEntryForValue != null)
      if (force) {
        delete(oldEntryForValue);
      } else {
        throw new IllegalArgumentException("value already present: " + value);
      }  
    if (oldEntryForKey != null)
      delete(oldEntryForKey); 
    BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
    insert(newEntry);
    rehashIfNecessary();
    return (oldEntryForKey == null) ? null : oldEntryForKey.value;
  }
  
  @Nullable
  private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
    int valueHash = hash(value);
    int keyHash = hash(key);
    BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
    if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key))
      return key; 
    BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
    if (oldEntryForKey != null)
      if (force) {
        delete(oldEntryForKey);
      } else {
        throw new IllegalArgumentException("value already present: " + key);
      }  
    if (oldEntryForValue != null)
      delete(oldEntryForValue); 
    BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
    insert(newEntry);
    rehashIfNecessary();
    return (oldEntryForValue == null) ? null : oldEntryForValue.key;
  }
  
  private void rehashIfNecessary() {
    BiEntry<K, V>[] oldKToV = this.hashTableKToV;
    if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
      int newTableSize = oldKToV.length * 2;
      this.hashTableKToV = createTable(newTableSize);
      this.hashTableVToK = createTable(newTableSize);
      this.mask = newTableSize - 1;
      this.size = 0;
      for (int bucket = 0; bucket < oldKToV.length; bucket++) {
        BiEntry<K, V> entry = oldKToV[bucket];
        while (entry != null) {
          BiEntry<K, V> nextEntry = entry.nextInKToVBucket;
          insert(entry);
          entry = nextEntry;
        } 
      } 
      this.modCount++;
    } 
  }
  
  private BiEntry<K, V>[] createTable(int length) {
    return (BiEntry<K, V>[])new BiEntry[length];
  }
  
  public V remove(@Nullable Object key) {
    BiEntry<K, V> entry = seekByKey(key, hash(key));
    if (entry == null)
      return null; 
    delete(entry);
    return entry.value;
  }
  
  public void clear() {
    this.size = 0;
    Arrays.fill((Object[])this.hashTableKToV, (Object)null);
    Arrays.fill((Object[])this.hashTableVToK, (Object)null);
    this.modCount++;
  }
  
  public int size() {
    return this.size;
  }
  
  abstract class Itr<T> implements Iterator<T> {
    int nextBucket = 0;
    
    HashBiMap.BiEntry<K, V> next = null;
    
    HashBiMap.BiEntry<K, V> toRemove = null;
    
    int expectedModCount = HashBiMap.this.modCount;
    
    private void checkForConcurrentModification() {
      if (HashBiMap.this.modCount != this.expectedModCount)
        throw new ConcurrentModificationException(); 
    }
    
    public boolean hasNext() {
      checkForConcurrentModification();
      if (this.next != null)
        return true; 
      while (this.nextBucket < HashBiMap.this.hashTableKToV.length) {
        if (HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
          this.next = HashBiMap.this.hashTableKToV[this.nextBucket++];
          return true;
        } 
        this.nextBucket++;
      } 
      return false;
    }
    
    public T next() {
      checkForConcurrentModification();
      if (!hasNext())
        throw new NoSuchElementException(); 
      HashBiMap.BiEntry<K, V> entry = this.next;
      this.next = entry.nextInKToVBucket;
      this.toRemove = entry;
      return output(entry);
    }
    
    public void remove() {
      checkForConcurrentModification();
      CollectPreconditions.checkRemove((this.toRemove != null));
      HashBiMap.this.delete(this.toRemove);
      this.expectedModCount = HashBiMap.this.modCount;
      this.toRemove = null;
    }
    
    abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
  }
  
  public Set<K> keySet() {
    return new KeySet();
  }
  
  private final class KeySet extends Maps.KeySet<K, V> {
    KeySet() {
      super(HashBiMap.this);
    }
    
    public Iterator<K> iterator() {
      return new HashBiMap<K, V>.Itr<K>() {
          K output(HashBiMap.BiEntry<K, V> entry) {
            return entry.key;
          }
        };
    }
    
    public boolean remove(@Nullable Object o) {
      HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, HashBiMap.hash(o));
      if (entry == null)
        return false; 
      HashBiMap.this.delete(entry);
      return true;
    }
  }
  
  public Set<V> values() {
    return inverse().keySet();
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    return new EntrySet();
  }
  
  private final class EntrySet extends Maps.EntrySet<K, V> {
    private EntrySet() {}
    
    Map<K, V> map() {
      return HashBiMap.this;
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      return new HashBiMap<K, V>.Itr<Map.Entry<K, V>>() {
          Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
            return new MapEntry(entry);
          }
          
          class MapEntry extends AbstractMapEntry<K, V> {
            HashBiMap.BiEntry<K, V> delegate;
            
            public K getKey() {
              return this.delegate.key;
            }
            
            public V getValue() {
              return this.delegate.value;
            }
            
            public V setValue(V value) {
              V oldValue = this.delegate.value;
              int valueHash = HashBiMap.hash(value);
              if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue))
                return value; 
              Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", new Object[] { value });
              HashBiMap.this.delete(this.delegate);
              HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(this.delegate.key, this.delegate.keyHash, value, valueHash);
              HashBiMap.this.insert(newEntry);
              HashBiMap.EntrySet.null.this.expectedModCount = HashBiMap.this.modCount;
              if (HashBiMap.EntrySet.null.this.toRemove == this.delegate)
                HashBiMap.EntrySet.null.this.toRemove = newEntry; 
              this.delegate = newEntry;
              return oldValue;
            }
          }
        };
    }
  }
  
  public BiMap<V, K> inverse() {
    return (this.inverse == null) ? (this.inverse = new Inverse()) : this.inverse;
  }
  
  private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
    private Inverse() {}
    
    BiMap<K, V> forward() {
      return HashBiMap.this;
    }
    
    public int size() {
      return HashBiMap.this.size;
    }
    
    public void clear() {
      forward().clear();
    }
    
    public boolean containsKey(@Nullable Object value) {
      return forward().containsValue(value);
    }
    
    public K get(@Nullable Object value) {
      HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
      return (entry == null) ? null : entry.key;
    }
    
    public K put(@Nullable V value, @Nullable K key) {
      return HashBiMap.this.putInverse(value, key, false);
    }
    
    public K forcePut(@Nullable V value, @Nullable K key) {
      return HashBiMap.this.putInverse(value, key, true);
    }
    
    public K remove(@Nullable Object value) {
      HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
      if (entry == null)
        return null; 
      HashBiMap.this.delete(entry);
      return entry.key;
    }
    
    public BiMap<K, V> inverse() {
      return forward();
    }
    
    public Set<V> keySet() {
      return new InverseKeySet();
    }
    
    private final class InverseKeySet extends Maps.KeySet<V, K> {
      InverseKeySet() {
        super(HashBiMap.Inverse.this);
      }
      
      public boolean remove(@Nullable Object o) {
        HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, HashBiMap.hash(o));
        if (entry == null)
          return false; 
        HashBiMap.this.delete(entry);
        return true;
      }
      
      public Iterator<V> iterator() {
        return new HashBiMap<K, V>.Itr<V>() {
            V output(HashBiMap.BiEntry<K, V> entry) {
              return entry.value;
            }
          };
      }
    }
    
    public Set<K> values() {
      return forward().keySet();
    }
    
    public Set<Map.Entry<V, K>> entrySet() {
      return new Maps.EntrySet<K, V>() {
          Map<V, K> map() {
            return HashBiMap.Inverse.this;
          }
          
          public Iterator<Map.Entry<V, K>> iterator() {
            return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>() {
                Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
                  return new InverseEntry(entry);
                }
                
                class InverseEntry extends AbstractMapEntry<V, K> {
                  HashBiMap.BiEntry<K, V> delegate;
                  
                  public V getKey() {
                    return this.delegate.value;
                  }
                  
                  public K getValue() {
                    return this.delegate.key;
                  }
                  
                  public K setValue(K key) {
                    K oldKey = this.delegate.key;
                    int keyHash = HashBiMap.hash(key);
                    if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey))
                      return key; 
                    Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", new Object[] { key });
                    HashBiMap.this.delete(this.delegate);
                    HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(key, keyHash, this.delegate.value, this.delegate.valueHash);
                    HashBiMap.this.insert(newEntry);
                    HashBiMap.Inverse.null.null.this.expectedModCount = HashBiMap.this.modCount;
                    return oldKey;
                  }
                }
              };
          }
        };
    }
    
    Object writeReplace() {
      return new HashBiMap.InverseSerializedForm<Object, Object>(HashBiMap.this);
    }
  }
  
  private static final class InverseSerializedForm<K, V> implements Serializable {
    private final HashBiMap<K, V> bimap;
    
    InverseSerializedForm(HashBiMap<K, V> bimap) {
      this.bimap = bimap;
    }
    
    Object readResolve() {
      return this.bimap.inverse();
    }
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    Serialization.writeMap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int size = Serialization.readCount(stream);
    init(size);
    Serialization.populateMap(this, stream, size);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */