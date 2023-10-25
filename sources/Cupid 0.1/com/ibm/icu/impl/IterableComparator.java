package com.ibm.icu.impl;

import java.util.Comparator;
import java.util.Iterator;

public class IterableComparator<T> implements Comparator<Iterable<T>> {
  private final Comparator<T> comparator;
  
  private final int shorterFirst;
  
  public IterableComparator() {
    this(null, true);
  }
  
  public IterableComparator(Comparator<T> comparator) {
    this(comparator, true);
  }
  
  public IterableComparator(Comparator<T> comparator, boolean shorterFirst) {
    this.comparator = comparator;
    this.shorterFirst = shorterFirst ? 1 : -1;
  }
  
  public int compare(Iterable<T> a, Iterable<T> b) {
    if (a == null)
      return (b == null) ? 0 : -this.shorterFirst; 
    if (b == null)
      return this.shorterFirst; 
    Iterator<T> ai = a.iterator();
    Iterator<T> bi = b.iterator();
    while (true) {
      if (!ai.hasNext())
        return bi.hasNext() ? -this.shorterFirst : 0; 
      if (!bi.hasNext())
        return this.shorterFirst; 
      T aItem = ai.next();
      T bItem = bi.next();
      int result = (this.comparator != null) ? this.comparator.compare(aItem, bItem) : ((Comparable<T>)aItem).compareTo(bItem);
      if (result != 0)
        return result; 
    } 
  }
  
  public static <T> int compareIterables(Iterable<T> a, Iterable<T> b) {
    return NOCOMPARATOR.compare(a, b);
  }
  
  private static final IterableComparator NOCOMPARATOR = new IterableComparator();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\IterableComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */