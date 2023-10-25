package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedSet;

@GwtCompatible
final class SortedIterables {
  public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements) {
    Comparator<?> comparator2;
    Preconditions.checkNotNull(comparator);
    Preconditions.checkNotNull(elements);
    if (elements instanceof SortedSet) {
      comparator2 = comparator((SortedSet)elements);
    } else if (elements instanceof SortedIterable) {
      comparator2 = ((SortedIterable)elements).comparator();
    } else {
      return false;
    } 
    return comparator.equals(comparator2);
  }
  
  public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet) {
    Comparator<? super E> result = sortedSet.comparator();
    if (result == null)
      result = Ordering.natural(); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\SortedIterables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */