package com.ibm.icu.impl;

import java.util.Comparator;

public class MultiComparator<T> implements Comparator<T> {
  private Comparator<T>[] comparators;
  
  public MultiComparator(Comparator<T>... comparators) {
    this.comparators = comparators;
  }
  
  public int compare(T arg0, T arg1) {
    for (int i = 0; i < this.comparators.length; ) {
      int result = this.comparators[i].compare(arg0, arg1);
      if (result == 0) {
        i++;
        continue;
      } 
      if (result > 0)
        return i + 1; 
      return -(i + 1);
    } 
    return 0;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\MultiComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */