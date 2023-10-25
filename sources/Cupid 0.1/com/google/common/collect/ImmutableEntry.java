package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable {
  final K key;
  
  final V value;
  
  private static final long serialVersionUID = 0L;
  
  ImmutableEntry(@Nullable K key, @Nullable V value) {
    this.key = key;
    this.value = value;
  }
  
  @Nullable
  public final K getKey() {
    return this.key;
  }
  
  @Nullable
  public final V getValue() {
    return this.value;
  }
  
  public final V setValue(V value) {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */