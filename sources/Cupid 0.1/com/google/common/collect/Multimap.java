package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface Multimap<K, V> {
  int size();
  
  boolean isEmpty();
  
  boolean containsKey(@Nullable Object paramObject);
  
  boolean containsValue(@Nullable Object paramObject);
  
  boolean containsEntry(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  boolean put(@Nullable K paramK, @Nullable V paramV);
  
  boolean remove(@Nullable Object paramObject1, @Nullable Object paramObject2);
  
  boolean putAll(@Nullable K paramK, Iterable<? extends V> paramIterable);
  
  boolean putAll(Multimap<? extends K, ? extends V> paramMultimap);
  
  Collection<V> replaceValues(@Nullable K paramK, Iterable<? extends V> paramIterable);
  
  Collection<V> removeAll(@Nullable Object paramObject);
  
  void clear();
  
  Collection<V> get(@Nullable K paramK);
  
  Set<K> keySet();
  
  Multiset<K> keys();
  
  Collection<V> values();
  
  Collection<Map.Entry<K, V>> entries();
  
  Map<K, Collection<V>> asMap();
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Multimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */