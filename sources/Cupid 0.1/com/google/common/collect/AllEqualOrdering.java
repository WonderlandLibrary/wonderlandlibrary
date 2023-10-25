package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class AllEqualOrdering extends Ordering<Object> implements Serializable {
  static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
  
  private static final long serialVersionUID = 0L;
  
  public int compare(@Nullable Object left, @Nullable Object right) {
    return 0;
  }
  
  public <E> List<E> sortedCopy(Iterable<E> iterable) {
    return Lists.newArrayList(iterable);
  }
  
  public <E> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
    return ImmutableList.copyOf(iterable);
  }
  
  public <S> Ordering<S> reverse() {
    return this;
  }
  
  private Object readResolve() {
    return INSTANCE;
  }
  
  public String toString() {
    return "Ordering.allEqual()";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\AllEqualOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */