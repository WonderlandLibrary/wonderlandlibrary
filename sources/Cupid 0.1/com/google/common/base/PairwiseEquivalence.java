package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
  final Equivalence<? super T> elementEquivalence;
  
  private static final long serialVersionUID = 1L;
  
  PairwiseEquivalence(Equivalence<? super T> elementEquivalence) {
    this.elementEquivalence = Preconditions.<Equivalence<? super T>>checkNotNull(elementEquivalence);
  }
  
  protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
    Iterator<T> iteratorA = iterableA.iterator();
    Iterator<T> iteratorB = iterableB.iterator();
    while (iteratorA.hasNext() && iteratorB.hasNext()) {
      if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next()))
        return false; 
    } 
    return (!iteratorA.hasNext() && !iteratorB.hasNext());
  }
  
  protected int doHash(Iterable<T> iterable) {
    int hash = 78721;
    for (T element : iterable)
      hash = hash * 24943 + this.elementEquivalence.hash(element); 
    return hash;
  }
  
  public boolean equals(@Nullable Object object) {
    if (object instanceof PairwiseEquivalence) {
      PairwiseEquivalence<?> that = (PairwiseEquivalence)object;
      return this.elementEquivalence.equals(that.elementEquivalence);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.elementEquivalence.hashCode() ^ 0x46A3EB07;
  }
  
  public String toString() {
    return this.elementEquivalence + ".pairwise()";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\PairwiseEquivalence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */