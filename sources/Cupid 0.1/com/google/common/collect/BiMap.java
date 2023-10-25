package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
  V put(@Nullable K paramK, @Nullable V paramV);
  
  V forcePut(@Nullable K paramK, @Nullable V paramV);
  
  void putAll(Map<? extends K, ? extends V> paramMap);
  
  Set<V> values();
  
  BiMap<V, K> inverse();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\BiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */