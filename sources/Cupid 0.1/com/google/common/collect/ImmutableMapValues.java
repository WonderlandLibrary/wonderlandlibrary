package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
  private final ImmutableMap<K, V> map;
  
  ImmutableMapValues(ImmutableMap<K, V> map) {
    this.map = map;
  }
  
  public int size() {
    return this.map.size();
  }
  
  public UnmodifiableIterator<V> iterator() {
    return Maps.valueIterator(this.map.entrySet().iterator());
  }
  
  public boolean contains(@Nullable Object object) {
    return (object != null && Iterators.contains(iterator(), object));
  }
  
  boolean isPartialView() {
    return true;
  }
  
  ImmutableList<V> createAsList() {
    final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
    return new ImmutableAsList<V>() {
        public V get(int index) {
          return (V)((Map.Entry)entryList.get(index)).getValue();
        }
        
        ImmutableCollection<V> delegateCollection() {
          return ImmutableMapValues.this;
        }
      };
  }
  
  @GwtIncompatible("serialization")
  Object writeReplace() {
    return new SerializedForm<V>(this.map);
  }
  
  @GwtIncompatible("serialization")
  private static class SerializedForm<V> implements Serializable {
    final ImmutableMap<?, V> map;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(ImmutableMap<?, V> map) {
      this.map = map;
    }
    
    Object readResolve() {
      return this.map.values();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableMapValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */