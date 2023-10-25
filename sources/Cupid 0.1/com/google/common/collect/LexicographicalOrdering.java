package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable {
  final Ordering<? super T> elementOrder;
  
  private static final long serialVersionUID = 0L;
  
  LexicographicalOrdering(Ordering<? super T> elementOrder) {
    this.elementOrder = elementOrder;
  }
  
  public int compare(Iterable<T> leftIterable, Iterable<T> rightIterable) {
    Iterator<T> left = leftIterable.iterator();
    Iterator<T> right = rightIterable.iterator();
    while (left.hasNext()) {
      if (!right.hasNext())
        return 1; 
      int result = this.elementOrder.compare(left.next(), right.next());
      if (result != 0)
        return result; 
    } 
    if (right.hasNext())
      return -1; 
    return 0;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this)
      return true; 
    if (object instanceof LexicographicalOrdering) {
      LexicographicalOrdering<?> that = (LexicographicalOrdering)object;
      return this.elementOrder.equals(that.elementOrder);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.elementOrder.hashCode() ^ 0x7BB78CF5;
  }
  
  public String toString() {
    return this.elementOrder + ".lexicographical()";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\LexicographicalOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */