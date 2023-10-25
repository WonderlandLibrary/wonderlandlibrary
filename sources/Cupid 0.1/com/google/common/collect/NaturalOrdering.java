package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
  static final NaturalOrdering INSTANCE = new NaturalOrdering();
  
  private static final long serialVersionUID = 0L;
  
  public int compare(Comparable<Comparable> left, Comparable right) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    return left.compareTo(right);
  }
  
  public <S extends Comparable> Ordering<S> reverse() {
    return ReverseNaturalOrdering.INSTANCE;
  }
  
  private Object readResolve() {
    return INSTANCE;
  }
  
  public String toString() {
    return "Ordering.natural()";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\NaturalOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */