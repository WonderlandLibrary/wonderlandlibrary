package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
  final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
  
  final transient int size;
  
  private static final long serialVersionUID = 0L;
  
  public static <K, V> ImmutableMultimap<K, V> of() {
    return ImmutableListMultimap.of();
  }
  
  public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
    return ImmutableListMultimap.of(k1, v1);
  }
  
  public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
    return ImmutableListMultimap.of(k1, v1, k2, v2);
  }
  
  public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
  }
  
  public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
  }
  
  public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
  }
  
  public static <K, V> Builder<K, V> builder() {
    return new Builder<K, V>();
  }
  
  private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
    private static final long serialVersionUID = 0L;
    
    BuilderMultimap() {
      super(new LinkedHashMap<K, Collection<V>>());
    }
    
    Collection<V> createCollection() {
      return Lists.newArrayList();
    }
  }
  
  public static class Builder<K, V> {
    Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap<K, V>();
    
    Comparator<? super K> keyComparator;
    
    Comparator<? super V> valueComparator;
    
    public Builder<K, V> put(K key, V value) {
      CollectPreconditions.checkEntryNotNull(key, value);
      this.builderMultimap.put(key, value);
      return this;
    }
    
    public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
      return put(entry.getKey(), entry.getValue());
    }
    
    public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
      if (key == null)
        throw new NullPointerException("null key in entry: null=" + Iterables.toString(values)); 
      Collection<V> valueList = this.builderMultimap.get(key);
      for (V value : values) {
        CollectPreconditions.checkEntryNotNull(key, value);
        valueList.add(value);
      } 
      return this;
    }
    
    public Builder<K, V> putAll(K key, V... values) {
      return putAll(key, Arrays.asList(values));
    }
    
    public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
      for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet())
        putAll(entry.getKey(), entry.getValue()); 
      return this;
    }
    
    public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
      this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
      return this;
    }
    
    public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
      this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
      return this;
    }
    
    public ImmutableMultimap<K, V> build() {
      if (this.valueComparator != null)
        for (Collection<V> values : (Iterable<Collection<V>>)this.builderMultimap.asMap().values()) {
          List<V> list = (List<V>)values;
          Collections.sort(list, this.valueComparator);
        }  
      if (this.keyComparator != null) {
        Multimap<K, V> sortedCopy = new ImmutableMultimap.BuilderMultimap<K, V>();
        List<Map.Entry<K, Collection<V>>> entries = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
        Collections.sort(entries, Ordering.<K>from(this.keyComparator).onKeys());
        for (Map.Entry<K, Collection<V>> entry : entries)
          sortedCopy.putAll(entry.getKey(), entry.getValue()); 
        this.builderMultimap = sortedCopy;
      } 
      return ImmutableMultimap.copyOf(this.builderMultimap);
    }
  }
  
  public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
    if (multimap instanceof ImmutableMultimap) {
      ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
      if (!kvMultimap.isPartialView())
        return kvMultimap; 
    } 
    return ImmutableListMultimap.copyOf(multimap);
  }
  
  @GwtIncompatible("java serialization is not supported")
  static class FieldSettersHolder {
    static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
    
    static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
    
    static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
  }
  
  ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
    this.map = map;
    this.size = size;
  }
  
  @Deprecated
  public ImmutableCollection<V> removeAll(Object key) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public boolean put(K key, V value) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public boolean putAll(K key, Iterable<? extends V> values) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public boolean remove(Object key, Object value) {
    throw new UnsupportedOperationException();
  }
  
  boolean isPartialView() {
    return this.map.isPartialView();
  }
  
  public boolean containsKey(@Nullable Object key) {
    return this.map.containsKey(key);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return (value != null && super.containsValue(value));
  }
  
  public int size() {
    return this.size;
  }
  
  public ImmutableSet<K> keySet() {
    return this.map.keySet();
  }
  
  public ImmutableMap<K, Collection<V>> asMap() {
    return (ImmutableMap)this.map;
  }
  
  Map<K, Collection<V>> createAsMap() {
    throw new AssertionError("should never be called");
  }
  
  public ImmutableCollection<Map.Entry<K, V>> entries() {
    return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
  }
  
  ImmutableCollection<Map.Entry<K, V>> createEntries() {
    return new EntryCollection<K, V>(this);
  }
  
  private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> {
    final ImmutableMultimap<K, V> multimap;
    
    private static final long serialVersionUID = 0L;
    
    EntryCollection(ImmutableMultimap<K, V> multimap) {
      this.multimap = multimap;
    }
    
    public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
      return this.multimap.entryIterator();
    }
    
    boolean isPartialView() {
      return this.multimap.isPartialView();
    }
    
    public int size() {
      return this.multimap.size();
    }
    
    public boolean contains(Object object) {
      if (object instanceof Map.Entry) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
        return this.multimap.containsEntry(entry.getKey(), entry.getValue());
      } 
      return false;
    }
  }
  
  private abstract class Itr<T> extends UnmodifiableIterator<T> {
    final Iterator<Map.Entry<K, Collection<V>>> mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
    
    K key = null;
    
    Iterator<V> valueIterator = Iterators.emptyIterator();
    
    public boolean hasNext() {
      return (this.mapIterator.hasNext() || this.valueIterator.hasNext());
    }
    
    public T next() {
      if (!this.valueIterator.hasNext()) {
        Map.Entry<K, Collection<V>> mapEntry = this.mapIterator.next();
        this.key = mapEntry.getKey();
        this.valueIterator = ((Collection<V>)mapEntry.getValue()).iterator();
      } 
      return output(this.key, this.valueIterator.next());
    }
    
    private Itr() {}
    
    abstract T output(K param1K, V param1V);
  }
  
  UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
    return new Itr<Map.Entry<K, V>>() {
        Map.Entry<K, V> output(K key, V value) {
          return Maps.immutableEntry(key, value);
        }
      };
  }
  
  public ImmutableMultiset<K> keys() {
    return (ImmutableMultiset<K>)super.keys();
  }
  
  ImmutableMultiset<K> createKeys() {
    return new Keys();
  }
  
  class Keys extends ImmutableMultiset<K> {
    public boolean contains(@Nullable Object object) {
      return ImmutableMultimap.this.containsKey(object);
    }
    
    public int count(@Nullable Object element) {
      Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
      return (values == null) ? 0 : values.size();
    }
    
    public Set<K> elementSet() {
      return ImmutableMultimap.this.keySet();
    }
    
    public int size() {
      return ImmutableMultimap.this.size();
    }
    
    Multiset.Entry<K> getEntry(int index) {
      Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
      return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
    }
    
    boolean isPartialView() {
      return true;
    }
  }
  
  public ImmutableCollection<V> values() {
    return (ImmutableCollection<V>)super.values();
  }
  
  ImmutableCollection<V> createValues() {
    return new Values<K, V>(this);
  }
  
  UnmodifiableIterator<V> valueIterator() {
    return new Itr<V>() {
        V output(K key, V value) {
          return value;
        }
      };
  }
  
  public abstract ImmutableCollection<V> get(K paramK);
  
  public abstract ImmutableMultimap<V, K> inverse();
  
  private static final class Values<K, V> extends ImmutableCollection<V> {
    private final transient ImmutableMultimap<K, V> multimap;
    
    private static final long serialVersionUID = 0L;
    
    Values(ImmutableMultimap<K, V> multimap) {
      this.multimap = multimap;
    }
    
    public boolean contains(@Nullable Object object) {
      return this.multimap.containsValue(object);
    }
    
    public UnmodifiableIterator<V> iterator() {
      return this.multimap.valueIterator();
    }
    
    @GwtIncompatible("not present in emulated superclass")
    int copyIntoArray(Object[] dst, int offset) {
      for (ImmutableCollection<V> valueCollection : this.multimap.map.values())
        offset = valueCollection.copyIntoArray(dst, offset); 
      return offset;
    }
    
    public int size() {
      return this.multimap.size();
    }
    
    boolean isPartialView() {
      return true;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */