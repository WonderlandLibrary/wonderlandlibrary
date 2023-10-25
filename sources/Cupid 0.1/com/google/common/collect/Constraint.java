package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface Constraint<E> {
  E checkElement(E paramE);
  
  String toString();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Constraint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */