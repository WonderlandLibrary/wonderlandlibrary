package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
  public abstract K getKey();
  
  public abstract V getValue();
  
  public V setValue(V value) {
    throw new UnsupportedOperationException();
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof Map.Entry) {
      Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
      return (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue()));
    } 
    return false;
  }
  
  public int hashCode() {
    K k = getKey();
    V v = getValue();
    return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
  }
  
  public String toString() {
    return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */