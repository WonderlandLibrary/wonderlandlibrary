package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
  private final transient ImmutableMap<E, Integer> map;
  
  private final transient int size;
  
  RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
    this.map = map;
    this.size = size;
  }
  
  boolean isPartialView() {
    return this.map.isPartialView();
  }
  
  public int count(@Nullable Object element) {
    Integer value = this.map.get(element);
    return (value == null) ? 0 : value.intValue();
  }
  
  public int size() {
    return this.size;
  }
  
  public boolean contains(@Nullable Object element) {
    return this.map.containsKey(element);
  }
  
  public ImmutableSet<E> elementSet() {
    return this.map.keySet();
  }
  
  Multiset.Entry<E> getEntry(int index) {
    Map.Entry<E, Integer> mapEntry = this.map.entrySet().asList().get(index);
    return Multisets.immutableEntry(mapEntry.getKey(), ((Integer)mapEntry.getValue()).intValue());
  }
  
  public int hashCode() {
    return this.map.hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\RegularImmutableMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */