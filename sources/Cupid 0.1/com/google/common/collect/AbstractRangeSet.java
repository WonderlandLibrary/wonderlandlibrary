package com.google.common.collect;

import javax.annotation.Nullable;

abstract class AbstractRangeSet<C extends Comparable> implements RangeSet<C> {
  public boolean contains(C value) {
    return (rangeContaining(value) != null);
  }
  
  public abstract Range<C> rangeContaining(C paramC);
  
  public boolean isEmpty() {
    return asRanges().isEmpty();
  }
  
  public void add(Range<C> range) {
    throw new UnsupportedOperationException();
  }
  
  public void remove(Range<C> range) {
    throw new UnsupportedOperationException();
  }
  
  public void clear() {
    remove((Range)Range.all());
  }
  
  public boolean enclosesAll(RangeSet<C> other) {
    for (Range<C> range : other.asRanges()) {
      if (!encloses(range))
        return false; 
    } 
    return true;
  }
  
  public void addAll(RangeSet<C> other) {
    for (Range<C> range : other.asRanges())
      add(range); 
  }
  
  public void removeAll(RangeSet<C> other) {
    for (Range<C> range : other.asRanges())
      remove(range); 
  }
  
  public abstract boolean encloses(Range<C> paramRange);
  
  public boolean equals(@Nullable Object obj) {
    if (obj == this)
      return true; 
    if (obj instanceof RangeSet) {
      RangeSet<?> other = (RangeSet)obj;
      return asRanges().equals(other.asRanges());
    } 
    return false;
  }
  
  public final int hashCode() {
    return asRanges().hashCode();
  }
  
  public final String toString() {
    return asRanges().toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AbstractRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */