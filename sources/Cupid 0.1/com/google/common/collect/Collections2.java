package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public final class Collections2 {
  public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
    if (unfiltered instanceof FilteredCollection)
      return ((FilteredCollection<E>)unfiltered).createCombined(predicate); 
    return new FilteredCollection<E>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
  }
  
  static boolean safeContains(Collection<?> collection, @Nullable Object object) {
    Preconditions.checkNotNull(collection);
    try {
      return collection.contains(object);
    } catch (ClassCastException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    } 
  }
  
  static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
    Preconditions.checkNotNull(collection);
    try {
      return collection.remove(object);
    } catch (ClassCastException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    } 
  }
  
  static class FilteredCollection<E> extends AbstractCollection<E> {
    final Collection<E> unfiltered;
    
    final Predicate<? super E> predicate;
    
    FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
      this.unfiltered = unfiltered;
      this.predicate = predicate;
    }
    
    FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
      return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
    }
    
    public boolean add(E element) {
      Preconditions.checkArgument(this.predicate.apply(element));
      return this.unfiltered.add(element);
    }
    
    public boolean addAll(Collection<? extends E> collection) {
      for (E element : collection)
        Preconditions.checkArgument(this.predicate.apply(element)); 
      return this.unfiltered.addAll(collection);
    }
    
    public void clear() {
      Iterables.removeIf(this.unfiltered, this.predicate);
    }
    
    public boolean contains(@Nullable Object element) {
      if (Collections2.safeContains(this.unfiltered, element)) {
        E e = (E)element;
        return this.predicate.apply(e);
      } 
      return false;
    }
    
    public boolean containsAll(Collection<?> collection) {
      return Collections2.containsAllImpl(this, collection);
    }
    
    public boolean isEmpty() {
      return !Iterables.any(this.unfiltered, this.predicate);
    }
    
    public Iterator<E> iterator() {
      return Iterators.filter(this.unfiltered.iterator(), this.predicate);
    }
    
    public boolean remove(Object element) {
      return (contains(element) && this.unfiltered.remove(element));
    }
    
    public boolean removeAll(Collection<?> collection) {
      return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.in(collection)));
    }
    
    public boolean retainAll(Collection<?> collection) {
      return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.not(Predicates.in(collection))));
    }
    
    public int size() {
      return Iterators.size(iterator());
    }
    
    public Object[] toArray() {
      return Lists.<E>newArrayList(iterator()).toArray();
    }
    
    public <T> T[] toArray(T[] array) {
      return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
    }
  }
  
  public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
    return new TransformedCollection<F, T>(fromCollection, function);
  }
  
  static class TransformedCollection<F, T> extends AbstractCollection<T> {
    final Collection<F> fromCollection;
    
    final Function<? super F, ? extends T> function;
    
    TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
      this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
      this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
    }
    
    public void clear() {
      this.fromCollection.clear();
    }
    
    public boolean isEmpty() {
      return this.fromCollection.isEmpty();
    }
    
    public Iterator<T> iterator() {
      return Iterators.transform(this.fromCollection.iterator(), this.function);
    }
    
    public int size() {
      return this.fromCollection.size();
    }
  }
  
  static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
    return Iterables.all(c, Predicates.in(self));
  }
  
  static String toStringImpl(final Collection<?> collection) {
    StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
    STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>() {
            public Object apply(Object input) {
              return (input == collection) ? "(this Collection)" : input;
            }
          }));
    return sb.append(']').toString();
  }
  
  static StringBuilder newStringBuilderForCollection(int size) {
    CollectPreconditions.checkNonnegative(size, "size");
    return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
  }
  
  static <T> Collection<T> cast(Iterable<T> iterable) {
    return (Collection<T>)iterable;
  }
  
  static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");
  
  @Beta
  public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
    return orderedPermutations(elements, Ordering.natural());
  }
  
  @Beta
  public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
    return new OrderedPermutationCollection<E>(elements, comparator);
  }
  
  private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
    final ImmutableList<E> inputList;
    
    final Comparator<? super E> comparator;
    
    final int size;
    
    OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
      this.inputList = Ordering.<E>from(comparator).immutableSortedCopy(input);
      this.comparator = comparator;
      this.size = calculateSize(this.inputList, comparator);
    }
    
    private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
      long permutations = 1L;
      int n = 1;
      int r = 1;
      while (n < sortedInputList.size()) {
        int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
        if (comparison < 0) {
          permutations *= LongMath.binomial(n, r);
          r = 0;
          if (!Collections2.isPositiveInt(permutations))
            return Integer.MAX_VALUE; 
        } 
        n++;
        r++;
      } 
      permutations *= LongMath.binomial(n, r);
      if (!Collections2.isPositiveInt(permutations))
        return Integer.MAX_VALUE; 
      return (int)permutations;
    }
    
    public int size() {
      return this.size;
    }
    
    public boolean isEmpty() {
      return false;
    }
    
    public Iterator<List<E>> iterator() {
      return new Collections2.OrderedPermutationIterator<E>(this.inputList, this.comparator);
    }
    
    public boolean contains(@Nullable Object obj) {
      if (obj instanceof List) {
        List<?> list = (List)obj;
        return Collections2.isPermutation(this.inputList, list);
      } 
      return false;
    }
    
    public String toString() {
      return "orderedPermutationCollection(" + this.inputList + ")";
    }
  }
  
  private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
    List<E> nextPermutation;
    
    final Comparator<? super E> comparator;
    
    OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
      this.nextPermutation = Lists.newArrayList(list);
      this.comparator = comparator;
    }
    
    protected List<E> computeNext() {
      if (this.nextPermutation == null)
        return endOfData(); 
      ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
      calculateNextPermutation();
      return next;
    }
    
    void calculateNextPermutation() {
      int j = findNextJ();
      if (j == -1) {
        this.nextPermutation = null;
        return;
      } 
      int l = findNextL(j);
      Collections.swap(this.nextPermutation, j, l);
      int n = this.nextPermutation.size();
      Collections.reverse(this.nextPermutation.subList(j + 1, n));
    }
    
    int findNextJ() {
      for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
        if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0)
          return k; 
      } 
      return -1;
    }
    
    int findNextL(int j) {
      E ak = this.nextPermutation.get(j);
      for (int l = this.nextPermutation.size() - 1; l > j; l--) {
        if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0)
          return l; 
      } 
      throw new AssertionError("this statement should be unreachable");
    }
  }
  
  @Beta
  public static <E> Collection<List<E>> permutations(Collection<E> elements) {
    return new PermutationCollection<E>(ImmutableList.copyOf(elements));
  }
  
  private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
    final ImmutableList<E> inputList;
    
    PermutationCollection(ImmutableList<E> input) {
      this.inputList = input;
    }
    
    public int size() {
      return IntMath.factorial(this.inputList.size());
    }
    
    public boolean isEmpty() {
      return false;
    }
    
    public Iterator<List<E>> iterator() {
      return new Collections2.PermutationIterator<E>(this.inputList);
    }
    
    public boolean contains(@Nullable Object obj) {
      if (obj instanceof List) {
        List<?> list = (List)obj;
        return Collections2.isPermutation(this.inputList, list);
      } 
      return false;
    }
    
    public String toString() {
      return "permutations(" + this.inputList + ")";
    }
  }
  
  private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
    final List<E> list;
    
    final int[] c;
    
    final int[] o;
    
    int j;
    
    PermutationIterator(List<E> list) {
      this.list = new ArrayList<E>(list);
      int n = list.size();
      this.c = new int[n];
      this.o = new int[n];
      Arrays.fill(this.c, 0);
      Arrays.fill(this.o, 1);
      this.j = Integer.MAX_VALUE;
    }
    
    protected List<E> computeNext() {
      if (this.j <= 0)
        return endOfData(); 
      ImmutableList<E> next = ImmutableList.copyOf(this.list);
      calculateNextPermutation();
      return next;
    }
    
    void calculateNextPermutation() {
      this.j = this.list.size() - 1;
      int s = 0;
      if (this.j == -1)
        return; 
      while (true) {
        int q = this.c[this.j] + this.o[this.j];
        if (q < 0) {
          switchDirection();
          continue;
        } 
        if (q == this.j + 1) {
          if (this.j == 0)
            break; 
          s++;
          switchDirection();
          continue;
        } 
        Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
        this.c[this.j] = q;
        break;
      } 
    }
    
    void switchDirection() {
      this.o[this.j] = -this.o[this.j];
      this.j--;
    }
  }
  
  private static boolean isPermutation(List<?> first, List<?> second) {
    if (first.size() != second.size())
      return false; 
    Multiset<?> firstMultiset = HashMultiset.create(first);
    Multiset<?> secondMultiset = HashMultiset.create(second);
    return firstMultiset.equals(secondMultiset);
  }
  
  private static boolean isPositiveInt(long n) {
    return (n >= 0L && n <= 2147483647L);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */