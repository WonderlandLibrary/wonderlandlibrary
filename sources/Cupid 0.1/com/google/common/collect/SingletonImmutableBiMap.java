package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
  final transient K singleKey;
  
  final transient V singleValue;
  
  transient ImmutableBiMap<V, K> inverse;
  
  SingletonImmutableBiMap(K singleKey, V singleValue) {
    CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
    this.singleKey = singleKey;
    this.singleValue = singleValue;
  }
  
  private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
    this.singleKey = singleKey;
    this.singleValue = singleValue;
    this.inverse = inverse;
  }
  
  SingletonImmutableBiMap(Map.Entry<? extends K, ? extends V> entry) {
    this(entry.getKey(), entry.getValue());
  }
  
  public V get(@Nullable Object key) {
    return this.singleKey.equals(key) ? this.singleValue : null;
  }
  
  public int size() {
    return 1;
  }
  
  public boolean containsKey(@Nullable Object key) {
    return this.singleKey.equals(key);
  }
  
  public boolean containsValue(@Nullable Object value) {
    return this.singleValue.equals(value);
  }
  
  boolean isPartialView() {
    return false;
  }
  
  ImmutableSet<Map.Entry<K, V>> createEntrySet() {
    return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
  }
  
  ImmutableSet<K> createKeySet() {
    return ImmutableSet.of(this.singleKey);
  }
  
  public ImmutableBiMap<V, K> inverse() {
    ImmutableBiMap<V, K> result = this.inverse;
    if (result == null)
      return this.inverse = new SingletonImmutableBiMap((K)this.singleValue, (V)this.singleKey, this); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SingletonImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */