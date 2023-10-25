package com.google.common.collect;

abstract class ImmutableSortedMultisetFauxverideShim<E> extends ImmutableMultiset<E> {
  @Deprecated
  public static <E> ImmutableSortedMultiset.Builder<E> builder() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E element) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E e1, E e2) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public static <E> ImmutableSortedMultiset<E> copyOf(E[] elements) {
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSortedMultisetFauxverideShim.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */