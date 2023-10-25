package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Beta
@GwtIncompatible("hasn't been tested yet")
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  
  private static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new EmptyImmutableSortedMultiset<Comparable>(NATURAL_ORDER);
  
  transient ImmutableSortedMultiset<E> descendingMultiset;
  
  public static <E> ImmutableSortedMultiset<E> of() {
    return (ImmutableSortedMultiset)NATURAL_EMPTY_MULTISET;
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
    RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)ImmutableSortedSet.<E>of(element);
    int[] counts = { 1 };
    long[] cumulativeCounts = { 0L, 1L };
    return new RegularImmutableSortedMultiset<E>(elementSet, counts, cumulativeCounts, 0, 1);
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
    return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2 }));
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
    return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 }));
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
    return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 }));
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
    return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 }));
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
    int size = remaining.length + 6;
    List<E> all = Lists.newArrayListWithCapacity(size);
    Collections.addAll(all, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5, (Comparable)e6 });
    Collections.addAll(all, remaining);
    return copyOf(Ordering.natural(), all);
  }
  
  public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
    return copyOf(Ordering.natural(), Arrays.asList(elements));
  }
  
  public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
    Ordering<E> naturalOrder = Ordering.natural();
    return copyOf(naturalOrder, elements);
  }
  
  public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
    Ordering<E> naturalOrder = Ordering.natural();
    return copyOf(naturalOrder, elements);
  }
  
  public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
    Preconditions.checkNotNull(comparator);
    return (new Builder<E>(comparator)).addAll(elements).build();
  }
  
  public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
    if (elements instanceof ImmutableSortedMultiset) {
      ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset)elements;
      if (comparator.equals(multiset.comparator())) {
        if (multiset.isPartialView())
          return copyOfSortedEntries(comparator, multiset.entrySet().asList()); 
        return multiset;
      } 
    } 
    elements = Lists.newArrayList(elements);
    TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator));
    Iterables.addAll(sortedCopy, elements);
    return copyOfSortedEntries(comparator, sortedCopy.entrySet());
  }
  
  public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
    return copyOfSortedEntries(sortedMultiset.comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
  }
  
  private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Multiset.Entry<E>> entries) {
    if (entries.isEmpty())
      return emptyMultiset(comparator); 
    ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<E>(entries.size());
    int[] counts = new int[entries.size()];
    long[] cumulativeCounts = new long[entries.size() + 1];
    int i = 0;
    for (Multiset.Entry<E> entry : entries) {
      elementsBuilder.add(entry.getElement());
      counts[i] = entry.getCount();
      cumulativeCounts[i + 1] = cumulativeCounts[i] + counts[i];
      i++;
    } 
    return new RegularImmutableSortedMultiset<E>(new RegularImmutableSortedSet<E>(elementsBuilder.build(), comparator), counts, cumulativeCounts, 0, entries.size());
  }
  
  static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
    if (NATURAL_ORDER.equals(comparator))
      return (ImmutableSortedMultiset)NATURAL_EMPTY_MULTISET; 
    return new EmptyImmutableSortedMultiset<E>(comparator);
  }
  
  public final Comparator<? super E> comparator() {
    return elementSet().comparator();
  }
  
  public ImmutableSortedMultiset<E> descendingMultiset() {
    ImmutableSortedMultiset<E> result = this.descendingMultiset;
    if (result == null)
      return this.descendingMultiset = new DescendingImmutableSortedMultiset<E>(this); 
    return result;
  }
  
  @Deprecated
  public final Multiset.Entry<E> pollFirstEntry() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final Multiset.Entry<E> pollLastEntry() {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
    Preconditions.checkArgument((comparator().compare(lowerBound, upperBound) <= 0), "Expected lowerBound <= upperBound but %s > %s", new Object[] { lowerBound, upperBound });
    return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
  }
  
  public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
    return new Builder<E>(comparator);
  }
  
  public static <E extends Comparable<E>> Builder<E> reverseOrder() {
    return new Builder<E>(Ordering.<Comparable>natural().reverse());
  }
  
  public static <E extends Comparable<E>> Builder<E> naturalOrder() {
    return new Builder<E>(Ordering.natural());
  }
  
  public static class Builder<E> extends ImmutableMultiset.Builder<E> {
    public Builder(Comparator<? super E> comparator) {
      super(TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator)));
    }
    
    public Builder<E> add(E element) {
      super.add(element);
      return this;
    }
    
    public Builder<E> addCopies(E element, int occurrences) {
      super.addCopies(element, occurrences);
      return this;
    }
    
    public Builder<E> setCount(E element, int count) {
      super.setCount(element, count);
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
    
    public ImmutableSortedMultiset<E> build() {
      return ImmutableSortedMultiset.copyOfSorted((SortedMultiset<E>)this.contents);
    }
  }
  
  private static final class SerializedForm<E> implements Serializable {
    Comparator<? super E> comparator;
    
    E[] elements;
    
    int[] counts;
    
    SerializedForm(SortedMultiset<E> multiset) {
      this.comparator = multiset.comparator();
      int n = multiset.entrySet().size();
      this.elements = (E[])new Object[n];
      this.counts = new int[n];
      int i = 0;
      for (Multiset.Entry<E> entry : multiset.entrySet()) {
        this.elements[i] = entry.getElement();
        this.counts[i] = entry.getCount();
        i++;
      } 
    }
    
    Object readResolve() {
      int n = this.elements.length;
      ImmutableSortedMultiset.Builder<E> builder = new ImmutableSortedMultiset.Builder<E>(this.comparator);
      for (int i = 0; i < n; i++)
        builder.addCopies(this.elements[i], this.counts[i]); 
      return builder.build();
    }
  }
  
  Object writeReplace() {
    return new SerializedForm<E>(this);
  }
  
  public abstract ImmutableSortedSet<E> elementSet();
  
  public abstract ImmutableSortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);
  
  public abstract ImmutableSortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */