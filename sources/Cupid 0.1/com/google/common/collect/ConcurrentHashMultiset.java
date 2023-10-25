package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
  private final transient ConcurrentMap<E, AtomicInteger> countMap;
  
  private transient EntrySet entrySet;
  
  private static final long serialVersionUID = 1L;
  
  private static class FieldSettersHolder {
    static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
  }
  
  public static <E> ConcurrentHashMultiset<E> create() {
    return new ConcurrentHashMultiset<E>(new ConcurrentHashMap<E, AtomicInteger>());
  }
  
  public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
    ConcurrentHashMultiset<E> multiset = create();
    Iterables.addAll(multiset, elements);
    return multiset;
  }
  
  @Beta
  public static <E> ConcurrentHashMultiset<E> create(MapMaker mapMaker) {
    return new ConcurrentHashMultiset<E>(mapMaker.makeMap());
  }
  
  @VisibleForTesting
  ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
    Preconditions.checkArgument(countMap.isEmpty());
    this.countMap = countMap;
  }
  
  public int count(@Nullable Object element) {
    AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
    return (existingCounter == null) ? 0 : existingCounter.get();
  }
  
  public int size() {
    long sum = 0L;
    for (AtomicInteger value : this.countMap.values())
      sum += value.get(); 
    return Ints.saturatedCast(sum);
  }
  
  public Object[] toArray() {
    return snapshot().toArray();
  }
  
  public <T> T[] toArray(T[] array) {
    return snapshot().toArray(array);
  }
  
  private List<E> snapshot() {
    List<E> list = Lists.newArrayListWithExpectedSize(size());
    for (Multiset.Entry<E> entry : entrySet()) {
      E element = entry.getElement();
      for (int i = entry.getCount(); i > 0; i--)
        list.add(element); 
    } 
    return list;
  }
  
  public int add(E element, int occurrences) {
    AtomicInteger existingCounter, newCounter;
    Preconditions.checkNotNull(element);
    if (occurrences == 0)
      return count(element); 
    Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
    do {
      existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
      if (existingCounter == null) {
        existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
        if (existingCounter == null)
          return 0; 
      } 
      while (true) {
        int oldValue = existingCounter.get();
        if (oldValue != 0) {
          try {
            int newValue = IntMath.checkedAdd(oldValue, occurrences);
            if (existingCounter.compareAndSet(oldValue, newValue))
              return oldValue; 
          } catch (ArithmeticException overflow) {
            throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
          } 
          continue;
        } 
        break;
      } 
      newCounter = new AtomicInteger(occurrences);
    } while (this.countMap.putIfAbsent(element, newCounter) != null && !this.countMap.replace(element, existingCounter, newCounter));
    return 0;
  }
  
  public int remove(@Nullable Object element, int occurrences) {
    if (occurrences == 0)
      return count(element); 
    Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
    AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
    if (existingCounter == null)
      return 0; 
    while (true) {
      int oldValue = existingCounter.get();
      if (oldValue != 0) {
        int newValue = Math.max(0, oldValue - occurrences);
        if (existingCounter.compareAndSet(oldValue, newValue)) {
          if (newValue == 0)
            this.countMap.remove(element, existingCounter); 
          return oldValue;
        } 
        continue;
      } 
      break;
    } 
    return 0;
  }
  
  public boolean removeExactly(@Nullable Object element, int occurrences) {
    if (occurrences == 0)
      return true; 
    Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
    AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
    if (existingCounter == null)
      return false; 
    while (true) {
      int oldValue = existingCounter.get();
      if (oldValue < occurrences)
        return false; 
      int newValue = oldValue - occurrences;
      if (existingCounter.compareAndSet(oldValue, newValue)) {
        if (newValue == 0)
          this.countMap.remove(element, existingCounter); 
        return true;
      } 
    } 
  }
  
  public int setCount(E element, int count) {
    Preconditions.checkNotNull(element);
    CollectPreconditions.checkNonnegative(count, "count");
    label26: while (true) {
      AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
      if (existingCounter == null) {
        if (count == 0)
          return 0; 
        existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count));
        if (existingCounter == null)
          return 0; 
      } 
      while (true) {
        int oldValue = existingCounter.get();
        if (oldValue == 0) {
          if (count == 0)
            return 0; 
          AtomicInteger newCounter = new AtomicInteger(count);
          if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter))
            return 0; 
          continue label26;
        } 
        if (existingCounter.compareAndSet(oldValue, count)) {
          if (count == 0)
            this.countMap.remove(element, existingCounter); 
          return oldValue;
        } 
      } 
      break;
    } 
  }
  
  public boolean setCount(E element, int expectedOldCount, int newCount) {
    Preconditions.checkNotNull(element);
    CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
    CollectPreconditions.checkNonnegative(newCount, "newCount");
    AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
    if (existingCounter == null) {
      if (expectedOldCount != 0)
        return false; 
      if (newCount == 0)
        return true; 
      return (this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
    } 
    int oldValue = existingCounter.get();
    if (oldValue == expectedOldCount) {
      if (oldValue == 0) {
        if (newCount == 0) {
          this.countMap.remove(element, existingCounter);
          return true;
        } 
        AtomicInteger newCounter = new AtomicInteger(newCount);
        return (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter));
      } 
      if (existingCounter.compareAndSet(oldValue, newCount)) {
        if (newCount == 0)
          this.countMap.remove(element, existingCounter); 
        return true;
      } 
    } 
    return false;
  }
  
  Set<E> createElementSet() {
    final Set<E> delegate = this.countMap.keySet();
    return new ForwardingSet<E>() {
        protected Set<E> delegate() {
          return delegate;
        }
        
        public boolean contains(@Nullable Object object) {
          return (object != null && Collections2.safeContains(delegate, object));
        }
        
        public boolean containsAll(Collection<?> collection) {
          return standardContainsAll(collection);
        }
        
        public boolean remove(Object object) {
          return (object != null && Collections2.safeRemove(delegate, object));
        }
        
        public boolean removeAll(Collection<?> c) {
          return standardRemoveAll(c);
        }
      };
  }
  
  public Set<Multiset.Entry<E>> entrySet() {
    EntrySet result = this.entrySet;
    if (result == null)
      this.entrySet = result = new EntrySet(); 
    return result;
  }
  
  int distinctElements() {
    return this.countMap.size();
  }
  
  public boolean isEmpty() {
    return this.countMap.isEmpty();
  }
  
  Iterator<Multiset.Entry<E>> entryIterator() {
    final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>() {
        private Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
        
        protected Multiset.Entry<E> computeNext() {
          while (true) {
            if (!this.mapEntries.hasNext())
              return endOfData(); 
            Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
            int count = ((AtomicInteger)mapEntry.getValue()).get();
            if (count != 0)
              return Multisets.immutableEntry(mapEntry.getKey(), count); 
          } 
        }
      };
    return new ForwardingIterator<Multiset.Entry<E>>() {
        private Multiset.Entry<E> last;
        
        protected Iterator<Multiset.Entry<E>> delegate() {
          return readOnlyIterator;
        }
        
        public Multiset.Entry<E> next() {
          this.last = super.next();
          return this.last;
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.last != null));
          ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
          this.last = null;
        }
      };
  }
  
  public void clear() {
    this.countMap.clear();
  }
  
  private class EntrySet extends AbstractMultiset<E>.EntrySet {
    private EntrySet() {}
    
    ConcurrentHashMultiset<E> multiset() {
      return ConcurrentHashMultiset.this;
    }
    
    public Object[] toArray() {
      return snapshot().toArray();
    }
    
    public <T> T[] toArray(T[] array) {
      return snapshot().toArray(array);
    }
    
    private List<Multiset.Entry<E>> snapshot() {
      List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
      Iterators.addAll(list, iterator());
      return list;
    }
  }
  
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(this.countMap);
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap<E, Integer>)stream.readObject();
    FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ConcurrentHashMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */