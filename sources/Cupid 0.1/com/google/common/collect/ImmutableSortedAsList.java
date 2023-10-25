package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
  ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
    super(backingSet, backingList);
  }
  
  ImmutableSortedSet<E> delegateCollection() {
    return (ImmutableSortedSet<E>)super.delegateCollection();
  }
  
  public Comparator<? super E> comparator() {
    return delegateCollection().comparator();
  }
  
  @GwtIncompatible("ImmutableSortedSet.indexOf")
  public int indexOf(@Nullable Object target) {
    int index = delegateCollection().indexOf(target);
    return (index >= 0 && get(index).equals(target)) ? index : -1;
  }
  
  @GwtIncompatible("ImmutableSortedSet.indexOf")
  public int lastIndexOf(@Nullable Object target) {
    return indexOf(target);
  }
  
  public boolean contains(Object target) {
    return (indexOf(target) >= 0);
  }
  
  @GwtIncompatible("super.subListUnchecked does not exist; inherited subList is valid if slow")
  ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
    return (new RegularImmutableSortedSet<E>(super.subListUnchecked(fromIndex, toIndex), comparator())).asList();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSortedAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */