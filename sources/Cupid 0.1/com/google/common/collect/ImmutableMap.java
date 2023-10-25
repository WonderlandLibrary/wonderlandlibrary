package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
  public static <K, V> ImmutableMap<K, V> of() {
    return ImmutableBiMap.of();
  }
  
  public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
    return ImmutableBiMap.of(k1, v1);
  }
  
  public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
    return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2) });
  }
  
  public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
  }
  
  public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
  }
  
  public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    return new RegularImmutableMap<K, V>((ImmutableMapEntry.TerminalEntry<?, ?>[])new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
  }
  
  static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K key, V value) {
    CollectPreconditions.checkEntryNotNull(key, value);
    return new ImmutableMapEntry.TerminalEntry<K, V>(key, value);
  }
  
  public static <K, V> Builder<K, V> builder() {
    return new Builder<K, V>();
  }
  
  static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
    if (!safe)
      throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2); 
  }
  
  public static class Builder<K, V> {
    ImmutableMapEntry.TerminalEntry<K, V>[] entries;
    
    int size;
    
    public Builder() {
      this(4);
    }
    
    Builder(int initialCapacity) {
      this.entries = (ImmutableMapEntry.TerminalEntry<K, V>[])new ImmutableMapEntry.TerminalEntry[initialCapacity];
      this.size = 0;
    }
    
    private void ensureCapacity(int minCapacity) {
      if (minCapacity > this.entries.length)
        this.entries = ObjectArrays.<ImmutableMapEntry.TerminalEntry<K, V>>arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity)); 
    }
    
    public Builder<K, V> put(K key, V value) {
      ensureCapacity(this.size + 1);
      ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
      this.entries[this.size++] = entry;
      return this;
    }
    
    public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
      return put(entry.getKey(), entry.getValue());
    }
    
    public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
      ensureCapacity(this.size + map.size());
      for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
        put(entry); 
      return this;
    }
    
    public ImmutableMap<K, V> build() {
      switch (this.size) {
        case 0:
          return ImmutableMap.of();
        case 1:
          return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
      } 
      return new RegularImmutableMap<K, V>(this.size, (ImmutableMapEntry.TerminalEntry<?, ?>[])this.entries);
    }
  }
  
  public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
    Map.Entry<K, V> onlyEntry;
    if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
      ImmutableMap<K, V> kvMap = (ImmutableMap)map;
      if (!kvMap.isPartialView())
        return kvMap; 
    } else if (map instanceof EnumMap) {
      return copyOfEnumMapUnsafe(map);
    } 
    Map.Entry[] arrayOfEntry = (Map.Entry[])map.entrySet().toArray((Object[])EMPTY_ENTRY_ARRAY);
    switch (arrayOfEntry.length) {
      case 0:
        return of();
      case 1:
        onlyEntry = arrayOfEntry[0];
        return of(onlyEntry.getKey(), onlyEntry.getValue());
    } 
    return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
  }
  
  private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) {
    return (ImmutableMap)copyOfEnumMap((EnumMap)map);
  }
  
  private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> original) {
    EnumMap<K, V> copy = new EnumMap<K, V>(original);
    for (Map.Entry<?, ?> entry : copy.entrySet())
      CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue()); 
    return ImmutableEnumMap.asImmutable(copy);
  }
  
  private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0];
  
  private transient ImmutableSet<Map.Entry<K, V>> entrySet;
  
  private transient ImmutableSet<K> keySet;
  
  private transient ImmutableCollection<V> values;
  
  private transient ImmutableSetMultimap<K, V> multimapView;
  
  @Deprecated
  public final V put(K k, V v) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final V remove(Object o) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final void putAll(Map<? extends K, ? extends V> map) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final void clear() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isEmpty() {
    return (size() == 0);
  }
  
  public boolean containsKey(@Nullable Object key) {
    return (get(key) != null);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return values().contains(value);
  }
  
  public ImmutableSet<Map.Entry<K, V>> entrySet() {
    ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
    return (result == null) ? (this.entrySet = createEntrySet()) : result;
  }
  
  public ImmutableSet<K> keySet() {
    ImmutableSet<K> result = this.keySet;
    return (result == null) ? (this.keySet = createKeySet()) : result;
  }
  
  ImmutableSet<K> createKeySet() {
    return new ImmutableMapKeySet<K, V>(this);
  }
  
  public ImmutableCollection<V> values() {
    ImmutableCollection<V> result = this.values;
    return (result == null) ? (this.values = new ImmutableMapValues<K, V>(this)) : result;
  }
  
  @Beta
  public ImmutableSetMultimap<K, V> asMultimap() {
    ImmutableSetMultimap<K, V> result = this.multimapView;
    return (result == null) ? (this.multimapView = createMultimapView()) : result;
  }
  
  private ImmutableSetMultimap<K, V> createMultimapView() {
    ImmutableMap<K, ImmutableSet<V>> map = viewMapValuesAsSingletonSets();
    return new ImmutableSetMultimap<K, V>(map, map.size(), null);
  }
  
  private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
    return new MapViewOfValuesAsSingletonSets<K, V>(this);
  }
  
  private static final class MapViewOfValuesAsSingletonSets<K, V> extends ImmutableMap<K, ImmutableSet<V>> {
    private final ImmutableMap<K, V> delegate;
    
    MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> delegate) {
      this.delegate = (ImmutableMap<K, V>)Preconditions.checkNotNull(delegate);
    }
    
    public int size() {
      return this.delegate.size();
    }
    
    public boolean containsKey(@Nullable Object key) {
      return this.delegate.containsKey(key);
    }
    
    public ImmutableSet<V> get(@Nullable Object key) {
      V outerValue = this.delegate.get(key);
      return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
    }
    
    boolean isPartialView() {
      return false;
    }
    
    ImmutableSet<Map.Entry<K, ImmutableSet<V>>> createEntrySet() {
      return (ImmutableSet)new ImmutableMapEntrySet<K, ImmutableSet<ImmutableSet<V>>>() {
          ImmutableMap<K, ImmutableSet<V>> map() {
            return ImmutableMap.MapViewOfValuesAsSingletonSets.this;
          }
          
          public UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> iterator() {
            final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.MapViewOfValuesAsSingletonSets.this.delegate.entrySet().iterator();
            return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>() {
                public boolean hasNext() {
                  return backingIterator.hasNext();
                }
                
                public Map.Entry<K, ImmutableSet<V>> next() {
                  final Map.Entry<K, V> backingEntry = backingIterator.next();
                  return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>() {
                      public K getKey() {
                        return (K)backingEntry.getKey();
                      }
                      
                      public ImmutableSet<V> getValue() {
                        return ImmutableSet.of((V)backingEntry.getValue());
                      }
                    };
                }
              };
          }
        };
    }
  }
  
  public boolean equals(@Nullable Object object) {
    return Maps.equalsImpl(this, object);
  }
  
  public int hashCode() {
    return entrySet().hashCode();
  }
  
  public String toString() {
    return Maps.toStringImpl(this);
  }
  
  static class SerializedForm implements Serializable {
    private final Object[] keys;
    
    private final Object[] values;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(ImmutableMap<?, ?> map) {
      this.keys = new Object[map.size()];
      this.values = new Object[map.size()];
      int i = 0;
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        this.keys[i] = entry.getKey();
        this.values[i] = entry.getValue();
        i++;
      } 
    }
    
    Object readResolve() {
      ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<Object, Object>();
      return createMap(builder);
    }
    
    Object createMap(ImmutableMap.Builder<Object, Object> builder) {
      for (int i = 0; i < this.keys.length; i++)
        builder.put(this.keys[i], this.values[i]); 
      return builder.build();
    }
  }
  
  Object writeReplace() {
    return new SerializedForm(this);
  }
  
  public abstract V get(@Nullable Object paramObject);
  
  abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
  
  abstract boolean isPartialView();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */