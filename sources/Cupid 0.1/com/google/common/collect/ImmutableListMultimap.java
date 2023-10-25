package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
  private transient ImmutableListMultimap<V, K> inverse;
  
  @GwtIncompatible("Not needed in emulated source")
  private static final long serialVersionUID = 0L;
  
  public static <K, V> ImmutableListMultimap<K, V> of() {
    return EmptyImmutableListMultimap.INSTANCE;
  }
  
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
    Builder<K, V> builder = builder();
    builder.put(k1, v1);
    return builder.build();
  }
  
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
    Builder<K, V> builder = builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    return builder.build();
  }
  
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    Builder<K, V> builder = builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    return builder.build();
  }
  
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    Builder<K, V> builder = builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    builder.put(k4, v4);
    return builder.build();
  }
  
  public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    Builder<K, V> builder = builder();
    builder.put(k1, v1);
    builder.put(k2, v2);
    builder.put(k3, v3);
    builder.put(k4, v4);
    builder.put(k5, v5);
    return builder.build();
  }
  
  public static <K, V> Builder<K, V> builder() {
    return new Builder<K, V>();
  }
  
  public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
    public Builder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }
    
    public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
      super.put(entry);
      return this;
    }
    
    public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
      super.putAll(key, values);
      return this;
    }
    
    public Builder<K, V> putAll(K key, V... values) {
      super.putAll(key, values);
      return this;
    }
    
    public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
      super.putAll(multimap);
      return this;
    }
    
    public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
      super.orderKeysBy(keyComparator);
      return this;
    }
    
    public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
      super.orderValuesBy(valueComparator);
      return this;
    }
    
    public ImmutableListMultimap<K, V> build() {
      return (ImmutableListMultimap<K, V>)super.build();
    }
  }
  
  public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
    if (multimap.isEmpty())
      return of(); 
    if (multimap instanceof ImmutableListMultimap) {
      ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
      if (!kvMultimap.isPartialView())
        return kvMultimap; 
    } 
    ImmutableMap.Builder<K, ImmutableList<V>> builder = ImmutableMap.builder();
    int size = 0;
    for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
      ImmutableList<V> list = ImmutableList.copyOf(entry.getValue());
      if (!list.isEmpty()) {
        builder.put(entry.getKey(), list);
        size += list.size();
      } 
    } 
    return new ImmutableListMultimap<K, V>(builder.build(), size);
  }
  
  ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
    super((ImmutableMap)map, size);
  }
  
  public ImmutableList<V> get(@Nullable K key) {
    ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
    return (list == null) ? ImmutableList.<V>of() : list;
  }
  
  public ImmutableListMultimap<V, K> inverse() {
    ImmutableListMultimap<V, K> result = this.inverse;
    return (result == null) ? (this.inverse = invert()) : result;
  }
  
  private ImmutableListMultimap<V, K> invert() {
    Builder<V, K> builder = builder();
    for (Map.Entry<K, V> entry : entries())
      builder.put(entry.getValue(), entry.getKey()); 
    ImmutableListMultimap<V, K> invertedMultimap = builder.build();
    invertedMultimap.inverse = this;
    return invertedMultimap;
  }
  
  @Deprecated
  public ImmutableList<V> removeAll(Object key) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
    throw new UnsupportedOperationException();
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    Serialization.writeMultimap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    ImmutableMap<Object, ImmutableList<Object>> tmpMap;
    stream.defaultReadObject();
    int keyCount = stream.readInt();
    if (keyCount < 0)
      throw new InvalidObjectException("Invalid key count " + keyCount); 
    ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
    int tmpSize = 0;
    for (int i = 0; i < keyCount; i++) {
      Object key = stream.readObject();
      int valueCount = stream.readInt();
      if (valueCount <= 0)
        throw new InvalidObjectException("Invalid value count " + valueCount); 
      Object[] array = new Object[valueCount];
      for (int j = 0; j < valueCount; j++)
        array[j] = stream.readObject(); 
      builder.put(key, ImmutableList.copyOf(array));
      tmpSize += valueCount;
    } 
    try {
      tmpMap = builder.build();
    } catch (IllegalArgumentException e) {
      throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
    } 
    ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
    ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */