package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E> {
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  
  private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet<Comparable>(NATURAL_ORDER);
  
  final transient Comparator<? super E> comparator;
  
  @GwtIncompatible("NavigableSet")
  transient ImmutableSortedSet<E> descendingSet;
  
  private static <E> ImmutableSortedSet<E> emptySet() {
    return (ImmutableSortedSet)NATURAL_EMPTY_SET;
  }
  
  static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
    if (NATURAL_ORDER.equals(comparator))
      return emptySet(); 
    return new EmptyImmutableSortedSet<E>(comparator);
  }
  
  public static <E> ImmutableSortedSet<E> of() {
    return emptySet();
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
    return new RegularImmutableSortedSet<E>(ImmutableList.of(element), Ordering.natural());
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
    return construct(Ordering.natural(), 2, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
    return construct(Ordering.natural(), 3, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
    return construct(Ordering.natural(), 4, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
    return construct(Ordering.natural(), 5, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
    Comparable[] contents = new Comparable[6 + remaining.length];
    contents[0] = (Comparable)e1;
    contents[1] = (Comparable)e2;
    contents[2] = (Comparable)e3;
    contents[3] = (Comparable)e4;
    contents[4] = (Comparable)e5;
    contents[5] = (Comparable)e6;
    System.arraycopy(remaining, 0, contents, 6, remaining.length);
    return construct(Ordering.natural(), contents.length, (E[])contents);
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
    return construct(Ordering.natural(), elements.length, (E[])elements.clone());
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
    Ordering<E> naturalOrder = Ordering.natural();
    return copyOf(naturalOrder, elements);
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
    Ordering<E> naturalOrder = Ordering.natural();
    return copyOf(naturalOrder, elements);
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
    Ordering<E> naturalOrder = Ordering.natural();
    return copyOf(naturalOrder, elements);
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
    return (new Builder<E>(comparator)).addAll(elements).build();
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
    Preconditions.checkNotNull(comparator);
    boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
    if (hasSameComparator && elements instanceof ImmutableSortedSet) {
      ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
      if (!original.isPartialView())
        return original; 
    } 
    E[] array = (E[])Iterables.toArray(elements);
    return construct(comparator, array.length, array);
  }
  
  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
    return copyOf(comparator, elements);
  }
  
  public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
    Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
    ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
    if (list.isEmpty())
      return emptySet(comparator); 
    return new RegularImmutableSortedSet<E>(list, comparator);
  }
  
  static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
    if (n == 0)
      return emptySet(comparator); 
    ObjectArrays.checkElementsNotNull((Object[])contents, n);
    Arrays.sort(contents, 0, n, comparator);
    int uniques = 1;
    for (int i = 1; i < n; i++) {
      E cur = contents[i];
      E prev = contents[uniques - 1];
      if (comparator.compare(cur, prev) != 0)
        contents[uniques++] = cur; 
    } 
    Arrays.fill((Object[])contents, uniques, n, (Object)null);
    return new RegularImmutableSortedSet<E>(ImmutableList.asImmutableList((Object[])contents, uniques), comparator);
  }
  
  public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
    return new Builder<E>(comparator);
  }
  
  public static <E extends Comparable<?>> Builder<E> reverseOrder() {
    return new Builder<E>(Ordering.<Comparable>natural().reverse());
  }
  
  public static <E extends Comparable<?>> Builder<E> naturalOrder() {
    return new Builder<E>(Ordering.natural());
  }
  
  public static final class Builder<E> extends ImmutableSet.Builder<E> {
    private final Comparator<? super E> comparator;
    
    public Builder(Comparator<? super E> comparator) {
      this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
    }
    
    public Builder<E> add(E element) {
      super.add(element);
      return this;
    }
    
    public Builder<E> add(E... elements) {
      super.add(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterable<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public Builder<E> addAll(Iterator<? extends E> elements) {
      super.addAll(elements);
      return this;
    }
    
    public ImmutableSortedSet<E> build() {
      E[] contentsArray = (E[])this.contents;
      ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
      this.size = result.size();
      return result;
    }
  }
  
  int unsafeCompare(Object a, Object b) {
    return unsafeCompare(this.comparator, a, b);
  }
  
  static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
    Comparator<Object> unsafeComparator = (Comparator)comparator;
    return unsafeComparator.compare(a, b);
  }
  
  ImmutableSortedSet(Comparator<? super E> comparator) {
    this.comparator = comparator;
  }
  
  public Comparator<? super E> comparator() {
    return this.comparator;
  }
  
  public ImmutableSortedSet<E> headSet(E toElement) {
    return headSet(toElement, false);
  }
  
  @GwtIncompatible("NavigableSet")
  public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
    return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
  }
  
  public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
    return subSet(fromElement, true, toElement, false);
  }
  
  @GwtIncompatible("NavigableSet")
  public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
    Preconditions.checkNotNull(fromElement);
    Preconditions.checkNotNull(toElement);
    Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
    return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
  }
  
  public ImmutableSortedSet<E> tailSet(E fromElement) {
    return tailSet(fromElement, true);
  }
  
  @GwtIncompatible("NavigableSet")
  public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
    return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
  }
  
  @GwtIncompatible("NavigableSet")
  public E lower(E e) {
    return Iterators.getNext(headSet(e, false).descendingIterator(), null);
  }
  
  @GwtIncompatible("NavigableSet")
  public E floor(E e) {
    return Iterators.getNext(headSet(e, true).descendingIterator(), null);
  }
  
  @GwtIncompatible("NavigableSet")
  public E ceiling(E e) {
    return Iterables.getFirst(tailSet(e, true), null);
  }
  
  @GwtIncompatible("NavigableSet")
  public E higher(E e) {
    return Iterables.getFirst(tailSet(e, false), null);
  }
  
  public E first() {
    return iterator().next();
  }
  
  public E last() {
    return descendingIterator().next();
  }
  
  @Deprecated
  @GwtIncompatible("NavigableSet")
  public final E pollFirst() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  @GwtIncompatible("NavigableSet")
  public final E pollLast() {
    throw new UnsupportedOperationException();
  }
  
  @GwtIncompatible("NavigableSet")
  public ImmutableSortedSet<E> descendingSet() {
    ImmutableSortedSet<E> result = this.descendingSet;
    if (result == null) {
      result = this.descendingSet = createDescendingSet();
      result.descendingSet = this;
    } 
    return result;
  }
  
  @GwtIncompatible("NavigableSet")
  ImmutableSortedSet<E> createDescendingSet() {
    return new DescendingImmutableSortedSet<E>(this);
  }
  
  private static class SerializedForm<E> implements Serializable {
    final Comparator<? super E> comparator;
    
    final Object[] elements;
    
    private static final long serialVersionUID = 0L;
    
    public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
      this.comparator = comparator;
      this.elements = elements;
    }
    
    Object readResolve() {
      return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
    }
  }
  
  private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Use SerializedForm");
  }
  
  Object writeReplace() {
    return new SerializedForm<E>(this.comparator, toArray());
  }
  
  public abstract UnmodifiableIterator<E> iterator();
  
  abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
  
  abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
  
  abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
  
  @GwtIncompatible("NavigableSet")
  public abstract UnmodifiableIterator<E> descendingIterator();
  
  abstract int indexOf(@Nullable Object paramObject);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */