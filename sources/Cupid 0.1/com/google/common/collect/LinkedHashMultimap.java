package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
  private static final int DEFAULT_KEY_CAPACITY = 16;
  
  private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
  
  @VisibleForTesting
  static final double VALUE_SET_LOAD_FACTOR = 1.0D;
  
  public static <K, V> LinkedHashMultimap<K, V> create() {
    return new LinkedHashMultimap<K, V>(16, 2);
  }
  
  public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
    return new LinkedHashMultimap<K, V>(Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
  }
  
  public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
    LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
    result.putAll(multimap);
    return result;
  }
  
  private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
    pred.setSuccessorInValueSet(succ);
    succ.setPredecessorInValueSet(pred);
  }
  
  private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
    pred.setSuccessorInMultimap(succ);
    succ.setPredecessorInMultimap(pred);
  }
  
  private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) {
    succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
  }
  
  private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) {
    succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
  }
  
  @VisibleForTesting
  static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V> {
    final int smearedValueHash;
    
    @Nullable
    ValueEntry<K, V> nextInValueBucket;
    
    LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
    
    LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
    
    ValueEntry<K, V> predecessorInMultimap;
    
    ValueEntry<K, V> successorInMultimap;
    
    ValueEntry(@Nullable K key, @Nullable V value, int smearedValueHash, @Nullable ValueEntry<K, V> nextInValueBucket) {
      super(key, value);
      this.smearedValueHash = smearedValueHash;
      this.nextInValueBucket = nextInValueBucket;
    }
    
    boolean matchesValue(@Nullable Object v, int smearedVHash) {
      return (this.smearedValueHash == smearedVHash && Objects.equal(getValue(), v));
    }
    
    public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
      return this.predecessorInValueSet;
    }
    
    public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
      return this.successorInValueSet;
    }
    
    public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
      this.predecessorInValueSet = entry;
    }
    
    public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
      this.successorInValueSet = entry;
    }
    
    public ValueEntry<K, V> getPredecessorInMultimap() {
      return this.predecessorInMultimap;
    }
    
    public ValueEntry<K, V> getSuccessorInMultimap() {
      return this.successorInMultimap;
    }
    
    public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) {
      this.successorInMultimap = multimapSuccessor;
    }
    
    public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) {
      this.predecessorInMultimap = multimapPredecessor;
    }
  }
  
  @VisibleForTesting
  transient int valueSetCapacity = 2;
  
  private transient ValueEntry<K, V> multimapHeaderEntry;
  
  @GwtIncompatible("java serialization not supported")
  private static final long serialVersionUID = 1L;
  
  private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
    super(new LinkedHashMap<K, Collection<V>>(keyCapacity));
    CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
    this.valueSetCapacity = valueSetCapacity;
    this.multimapHeaderEntry = new ValueEntry<K, V>(null, null, 0, null);
    succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
  }
  
  Set<V> createCollection() {
    return new LinkedHashSet<V>(this.valueSetCapacity);
  }
  
  Collection<V> createCollection(K key) {
    return new ValueSet(key, this.valueSetCapacity);
  }
  
  public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    return super.replaceValues(key, values);
  }
  
  public Set<Map.Entry<K, V>> entries() {
    return super.entries();
  }
  
  public Collection<V> values() {
    return super.values();
  }
  
  @VisibleForTesting
  final class ValueSet extends Sets.ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
    private final K key;
    
    @VisibleForTesting
    LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
    
    private int size = 0;
    
    private int modCount = 0;
    
    private LinkedHashMultimap.ValueSetLink<K, V> firstEntry;
    
    private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;
    
    ValueSet(K key, int expectedValues) {
      this.key = key;
      this.firstEntry = this;
      this.lastEntry = this;
      int tableSize = Hashing.closedTableSize(expectedValues, 1.0D);
      LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[tableSize];
      this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
    }
    
    private int mask() {
      return this.hashTable.length - 1;
    }
    
    public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
      return this.lastEntry;
    }
    
    public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
      return this.firstEntry;
    }
    
    public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
      this.lastEntry = entry;
    }
    
    public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
      this.firstEntry = entry;
    }
    
    public Iterator<V> iterator() {
      return new Iterator<V>() {
          LinkedHashMultimap.ValueSetLink<K, V> nextEntry = LinkedHashMultimap.ValueSet.this.firstEntry;
          
          LinkedHashMultimap.ValueEntry<K, V> toRemove;
          
          int expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
          
          private void checkForComodification() {
            if (LinkedHashMultimap.ValueSet.this.modCount != this.expectedModCount)
              throw new ConcurrentModificationException(); 
          }
          
          public boolean hasNext() {
            checkForComodification();
            return (this.nextEntry != LinkedHashMultimap.ValueSet.this);
          }
          
          public V next() {
            if (!hasNext())
              throw new NoSuchElementException(); 
            LinkedHashMultimap.ValueEntry<K, V> entry = (LinkedHashMultimap.ValueEntry<K, V>)this.nextEntry;
            V result = entry.getValue();
            this.toRemove = entry;
            this.nextEntry = entry.getSuccessorInValueSet();
            return result;
          }
          
          public void remove() {
            checkForComodification();
            CollectPreconditions.checkRemove((this.toRemove != null));
            LinkedHashMultimap.ValueSet.this.remove(this.toRemove.getValue());
            this.expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
            this.toRemove = null;
          }
        };
    }
    
    public int size() {
      return this.size;
    }
    
    public boolean contains(@Nullable Object o) {
      int smearedHash = Hashing.smearedHash(o);
      for (LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[smearedHash & mask()]; entry != null; 
        entry = entry.nextInValueBucket) {
        if (entry.matchesValue(o, smearedHash))
          return true; 
      } 
      return false;
    }
    
    public boolean add(@Nullable V value) {
      int smearedHash = Hashing.smearedHash(value);
      int bucket = smearedHash & mask();
      LinkedHashMultimap.ValueEntry<K, V> rowHead = this.hashTable[bucket];
      for (LinkedHashMultimap.ValueEntry<K, V> entry = rowHead; entry != null; 
        entry = entry.nextInValueBucket) {
        if (entry.matchesValue(value, smearedHash))
          return false; 
      } 
      LinkedHashMultimap.ValueEntry<K, V> newEntry = new LinkedHashMultimap.ValueEntry<K, V>(this.key, value, smearedHash, rowHead);
      LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
      LinkedHashMultimap.succeedsInValueSet(newEntry, this);
      LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
      LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
      this.hashTable[bucket] = newEntry;
      this.size++;
      this.modCount++;
      rehashIfNecessary();
      return true;
    }
    
    private void rehashIfNecessary() {
      if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
        LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
        this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
        int mask = arrayOfValueEntry.length - 1;
        LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
        for (; entry != this; entry = entry.getSuccessorInValueSet()) {
          LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
          int bucket = valueEntry.smearedValueHash & mask;
          valueEntry.nextInValueBucket = arrayOfValueEntry[bucket];
          arrayOfValueEntry[bucket] = valueEntry;
        } 
      } 
    }
    
    public boolean remove(@Nullable Object o) {
      int smearedHash = Hashing.smearedHash(o);
      int bucket = smearedHash & mask();
      LinkedHashMultimap.ValueEntry<K, V> prev = null;
      for (LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket]; entry != null; 
        prev = entry, entry = entry.nextInValueBucket) {
        if (entry.matchesValue(o, smearedHash)) {
          if (prev == null) {
            this.hashTable[bucket] = entry.nextInValueBucket;
          } else {
            prev.nextInValueBucket = entry.nextInValueBucket;
          } 
          LinkedHashMultimap.deleteFromValueSet(entry);
          LinkedHashMultimap.deleteFromMultimap(entry);
          this.size--;
          this.modCount++;
          return true;
        } 
      } 
      return false;
    }
    
    public void clear() {
      Arrays.fill((Object[])this.hashTable, (Object)null);
      this.size = 0;
      LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
      for (; entry != this; entry = entry.getSuccessorInValueSet()) {
        LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
        LinkedHashMultimap.deleteFromMultimap(valueEntry);
      } 
      LinkedHashMultimap.succeedsInValueSet(this, this);
      this.modCount++;
    }
  }
  
  Iterator<Map.Entry<K, V>> entryIterator() {
    return new Iterator<Map.Entry<K, V>>() {
        LinkedHashMultimap.ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
        
        LinkedHashMultimap.ValueEntry<K, V> toRemove;
        
        public boolean hasNext() {
          return (this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry);
        }
        
        public Map.Entry<K, V> next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          LinkedHashMultimap.ValueEntry<K, V> result = this.nextEntry;
          this.toRemove = result;
          this.nextEntry = this.nextEntry.successorInMultimap;
          return result;
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.toRemove != null));
          LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
          this.toRemove = null;
        }
      };
  }
  
  Iterator<V> valueIterator() {
    return Maps.valueIterator(entryIterator());
  }
  
  public void clear() {
    super.clear();
    succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeInt(this.valueSetCapacity);
    stream.writeInt(keySet().size());
    for (K key : keySet())
      stream.writeObject(key); 
    stream.writeInt(size());
    for (Map.Entry<K, V> entry : entries()) {
      stream.writeObject(entry.getKey());
      stream.writeObject(entry.getValue());
    } 
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.multimapHeaderEntry = new ValueEntry<K, V>(null, null, 0, null);
    succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    this.valueSetCapacity = stream.readInt();
    int distinctKeys = stream.readInt();
    Map<K, Collection<V>> map = new LinkedHashMap<K, Collection<V>>(Maps.capacity(distinctKeys));
    for (int i = 0; i < distinctKeys; i++) {
      K key = (K)stream.readObject();
      map.put(key, createCollection(key));
    } 
    int entries = stream.readInt();
    for (int j = 0; j < entries; j++) {
      K key = (K)stream.readObject();
      V value = (V)stream.readObject();
      ((Collection<V>)map.get(key)).add(value);
    } 
    setMap(map);
  }
  
  private static interface ValueSetLink<K, V> {
    ValueSetLink<K, V> getPredecessorInValueSet();
    
    ValueSetLink<K, V> getSuccessorInValueSet();
    
    void setPredecessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
    
    void setSuccessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\LinkedHashMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */