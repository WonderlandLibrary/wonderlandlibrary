package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.Map;

@GwtCompatible
interface FilteredMultimap<K, V> extends Multimap<K, V> {
  Multimap<K, V> unfiltered();
  
  Predicate<? super Map.Entry<K, V>> entryPredicate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\FilteredMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */