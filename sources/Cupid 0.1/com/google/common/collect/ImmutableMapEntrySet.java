package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
  abstract ImmutableMap<K, V> map();
  
  public int size() {
    return map().size();
  }
  
  public boolean contains(@Nullable Object object) {
    if (object instanceof Map.Entry) {
      Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
      V value = map().get(entry.getKey());
      return (value != null && value.equals(entry.getValue()));
    } 
    return false;
  }
  
  boolean isPartialView() {
    return map().isPartialView();
  }
  
  @GwtIncompatible("serialization")
  Object writeReplace() {
    return new EntrySetSerializedForm<K, V>(map());
  }
  
  @GwtIncompatible("serialization")
  private static class EntrySetSerializedForm<K, V> implements Serializable {
    final ImmutableMap<K, V> map;
    
    private static final long serialVersionUID = 0L;
    
    EntrySetSerializedForm(ImmutableMap<K, V> map) {
      this.map = map;
    }
    
    Object readResolve() {
      return this.map.entrySet();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableMapEntrySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */