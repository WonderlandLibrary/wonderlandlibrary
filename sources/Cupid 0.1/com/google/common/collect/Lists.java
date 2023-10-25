package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Lists {
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayList() {
    return new ArrayList<E>();
  }
  
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayList(E... elements) {
    Preconditions.checkNotNull(elements);
    int capacity = computeArrayListCapacity(elements.length);
    ArrayList<E> list = new ArrayList<E>(capacity);
    Collections.addAll(list, elements);
    return list;
  }
  
  @VisibleForTesting
  static int computeArrayListCapacity(int arraySize) {
    CollectPreconditions.checkNonnegative(arraySize, "arraySize");
    return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
  }
  
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
    Preconditions.checkNotNull(elements);
    return (elements instanceof Collection) ? new ArrayList<E>(Collections2.cast(elements)) : newArrayList(elements.iterator());
  }
  
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
    ArrayList<E> list = newArrayList();
    Iterators.addAll(list, elements);
    return list;
  }
  
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
    CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
    return new ArrayList<E>(initialArraySize);
  }
  
  @GwtCompatible(serializable = true)
  public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
    return new ArrayList<E>(computeArrayListCapacity(estimatedSize));
  }
  
  @GwtCompatible(serializable = true)
  public static <E> LinkedList<E> newLinkedList() {
    return new LinkedList<E>();
  }
  
  @GwtCompatible(serializable = true)
  public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
    LinkedList<E> list = newLinkedList();
    Iterables.addAll(list, elements);
    return list;
  }
  
  @GwtIncompatible("CopyOnWriteArrayList")
  public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
    return new CopyOnWriteArrayList<E>();
  }
  
  @GwtIncompatible("CopyOnWriteArrayList")
  public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
    Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : newArrayList(elements);
    return new CopyOnWriteArrayList<E>(elementsCollection);
  }
  
  public static <E> List<E> asList(@Nullable E first, E[] rest) {
    return new OnePlusArrayList<E>(first, rest);
  }
  
  private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
    final E first;
    
    final E[] rest;
    
    private static final long serialVersionUID = 0L;
    
    OnePlusArrayList(@Nullable E first, E[] rest) {
      this.first = first;
      this.rest = (E[])Preconditions.checkNotNull(rest);
    }
    
    public int size() {
      return this.rest.length + 1;
    }
    
    public E get(int index) {
      Preconditions.checkElementIndex(index, size());
      return (index == 0) ? this.first : this.rest[index - 1];
    }
  }
  
  public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) {
    return new TwoPlusArrayList<E>(first, second, rest);
  }
  
  private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
    final E first;
    
    final E second;
    
    final E[] rest;
    
    private static final long serialVersionUID = 0L;
    
    TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
      this.first = first;
      this.second = second;
      this.rest = (E[])Preconditions.checkNotNull(rest);
    }
    
    public int size() {
      return this.rest.length + 2;
    }
    
    public E get(int index) {
      switch (index) {
        case 0:
          return this.first;
        case 1:
          return this.second;
      } 
      Preconditions.checkElementIndex(index, size());
      return this.rest[index - 2];
    }
  }
  
  static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
    return CartesianList.create(lists);
  }
  
  static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
    return cartesianProduct(Arrays.asList(lists));
  }
  
  public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
    return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<F, T>(fromList, function) : new TransformingSequentialList<F, T>(fromList, function);
  }
  
  private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
    final List<F> fromList;
    
    final Function<? super F, ? extends T> function;
    
    private static final long serialVersionUID = 0L;
    
    TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
      this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
      this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
    }
    
    public void clear() {
      this.fromList.clear();
    }
    
    public int size() {
      return this.fromList.size();
    }
    
    public ListIterator<T> listIterator(int index) {
      return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
          T transform(F from) {
            return (T)Lists.TransformingSequentialList.this.function.apply(from);
          }
        };
    }
  }
  
  private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
    final List<F> fromList;
    
    final Function<? super F, ? extends T> function;
    
    private static final long serialVersionUID = 0L;
    
    TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
      this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
      this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
    }
    
    public void clear() {
      this.fromList.clear();
    }
    
    public T get(int index) {
      return (T)this.function.apply(this.fromList.get(index));
    }
    
    public Iterator<T> iterator() {
      return listIterator();
    }
    
    public ListIterator<T> listIterator(int index) {
      return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
          T transform(F from) {
            return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
          }
        };
    }
    
    public boolean isEmpty() {
      return this.fromList.isEmpty();
    }
    
    public T remove(int index) {
      return (T)this.function.apply(this.fromList.remove(index));
    }
    
    public int size() {
      return this.fromList.size();
    }
  }
  
  public static <T> List<List<T>> partition(List<T> list, int size) {
    Preconditions.checkNotNull(list);
    Preconditions.checkArgument((size > 0));
    return (list instanceof RandomAccess) ? new RandomAccessPartition<T>(list, size) : new Partition<T>(list, size);
  }
  
  private static class Partition<T> extends AbstractList<List<T>> {
    final List<T> list;
    
    final int size;
    
    Partition(List<T> list, int size) {
      this.list = list;
      this.size = size;
    }
    
    public List<T> get(int index) {
      Preconditions.checkElementIndex(index, size());
      int start = index * this.size;
      int end = Math.min(start + this.size, this.list.size());
      return this.list.subList(start, end);
    }
    
    public int size() {
      return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
    }
    
    public boolean isEmpty() {
      return this.list.isEmpty();
    }
  }
  
  private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
    RandomAccessPartition(List<T> list, int size) {
      super(list, size);
    }
  }
  
  @Beta
  public static ImmutableList<Character> charactersOf(String string) {
    return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
  }
  
  private static final class StringAsImmutableList extends ImmutableList<Character> {
    private final String string;
    
    StringAsImmutableList(String string) {
      this.string = string;
    }
    
    public int indexOf(@Nullable Object object) {
      return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
    }
    
    public int lastIndexOf(@Nullable Object object) {
      return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
    }
    
    public ImmutableList<Character> subList(int fromIndex, int toIndex) {
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
      return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
    }
    
    boolean isPartialView() {
      return false;
    }
    
    public Character get(int index) {
      Preconditions.checkElementIndex(index, size());
      return Character.valueOf(this.string.charAt(index));
    }
    
    public int size() {
      return this.string.length();
    }
  }
  
  @Beta
  public static List<Character> charactersOf(CharSequence sequence) {
    return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
  }
  
  private static final class CharSequenceAsList extends AbstractList<Character> {
    private final CharSequence sequence;
    
    CharSequenceAsList(CharSequence sequence) {
      this.sequence = sequence;
    }
    
    public Character get(int index) {
      Preconditions.checkElementIndex(index, size());
      return Character.valueOf(this.sequence.charAt(index));
    }
    
    public int size() {
      return this.sequence.length();
    }
  }
  
  public static <T> List<T> reverse(List<T> list) {
    if (list instanceof ImmutableList)
      return ((ImmutableList<T>)list).reverse(); 
    if (list instanceof ReverseList)
      return ((ReverseList<T>)list).getForwardList(); 
    if (list instanceof RandomAccess)
      return new RandomAccessReverseList<T>(list); 
    return new ReverseList<T>(list);
  }
  
  private static class ReverseList<T> extends AbstractList<T> {
    private final List<T> forwardList;
    
    ReverseList(List<T> forwardList) {
      this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
    }
    
    List<T> getForwardList() {
      return this.forwardList;
    }
    
    private int reverseIndex(int index) {
      int size = size();
      Preconditions.checkElementIndex(index, size);
      return size - 1 - index;
    }
    
    private int reversePosition(int index) {
      int size = size();
      Preconditions.checkPositionIndex(index, size);
      return size - index;
    }
    
    public void add(int index, @Nullable T element) {
      this.forwardList.add(reversePosition(index), element);
    }
    
    public void clear() {
      this.forwardList.clear();
    }
    
    public T remove(int index) {
      return this.forwardList.remove(reverseIndex(index));
    }
    
    protected void removeRange(int fromIndex, int toIndex) {
      subList(fromIndex, toIndex).clear();
    }
    
    public T set(int index, @Nullable T element) {
      return this.forwardList.set(reverseIndex(index), element);
    }
    
    public T get(int index) {
      return this.forwardList.get(reverseIndex(index));
    }
    
    public int size() {
      return this.forwardList.size();
    }
    
    public List<T> subList(int fromIndex, int toIndex) {
      Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
      return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
    }
    
    public Iterator<T> iterator() {
      return listIterator();
    }
    
    public ListIterator<T> listIterator(int index) {
      int start = reversePosition(index);
      final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
      return new ListIterator<T>() {
          boolean canRemoveOrSet;
          
          public void add(T e) {
            forwardIterator.add(e);
            forwardIterator.previous();
            this.canRemoveOrSet = false;
          }
          
          public boolean hasNext() {
            return forwardIterator.hasPrevious();
          }
          
          public boolean hasPrevious() {
            return forwardIterator.hasNext();
          }
          
          public T next() {
            if (!hasNext())
              throw new NoSuchElementException(); 
            this.canRemoveOrSet = true;
            return forwardIterator.previous();
          }
          
          public int nextIndex() {
            return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
          }
          
          public T previous() {
            if (!hasPrevious())
              throw new NoSuchElementException(); 
            this.canRemoveOrSet = true;
            return forwardIterator.next();
          }
          
          public int previousIndex() {
            return nextIndex() - 1;
          }
          
          public void remove() {
            CollectPreconditions.checkRemove(this.canRemoveOrSet);
            forwardIterator.remove();
            this.canRemoveOrSet = false;
          }
          
          public void set(T e) {
            Preconditions.checkState(this.canRemoveOrSet);
            forwardIterator.set(e);
          }
        };
    }
  }
  
  private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
    RandomAccessReverseList(List<T> forwardList) {
      super(forwardList);
    }
  }
  
  static int hashCodeImpl(List<?> list) {
    int hashCode = 1;
    for (Object o : list) {
      hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
      hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
    } 
    return hashCode;
  }
  
  static boolean equalsImpl(List<?> list, @Nullable Object object) {
    if (object == Preconditions.checkNotNull(list))
      return true; 
    if (!(object instanceof List))
      return false; 
    List<?> o = (List)object;
    return (list.size() == o.size() && Iterators.elementsEqual(list.iterator(), o.iterator()));
  }
  
  static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
    boolean changed = false;
    ListIterator<E> listIterator = list.listIterator(index);
    for (E e : elements) {
      listIterator.add(e);
      changed = true;
    } 
    return changed;
  }
  
  static int indexOfImpl(List<?> list, @Nullable Object element) {
    ListIterator<?> listIterator = list.listIterator();
    while (listIterator.hasNext()) {
      if (Objects.equal(element, listIterator.next()))
        return listIterator.previousIndex(); 
    } 
    return -1;
  }
  
  static int lastIndexOfImpl(List<?> list, @Nullable Object element) {
    ListIterator<?> listIterator = list.listIterator(list.size());
    while (listIterator.hasPrevious()) {
      if (Objects.equal(element, listIterator.previous()))
        return listIterator.nextIndex(); 
    } 
    return -1;
  }
  
  static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
    return (new AbstractListWrapper<E>(list)).listIterator(index);
  }
  
  static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
    List<E> wrapper;
    if (list instanceof RandomAccess) {
      wrapper = new RandomAccessListWrapper<E>(list) {
          private static final long serialVersionUID = 0L;
          
          public ListIterator<E> listIterator(int index) {
            return this.backingList.listIterator(index);
          }
        };
    } else {
      wrapper = new AbstractListWrapper<E>(list) {
          private static final long serialVersionUID = 0L;
          
          public ListIterator<E> listIterator(int index) {
            return this.backingList.listIterator(index);
          }
        };
    } 
    return wrapper.subList(fromIndex, toIndex);
  }
  
  private static class AbstractListWrapper<E> extends AbstractList<E> {
    final List<E> backingList;
    
    AbstractListWrapper(List<E> backingList) {
      this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
    }
    
    public void add(int index, E element) {
      this.backingList.add(index, element);
    }
    
    public boolean addAll(int index, Collection<? extends E> c) {
      return this.backingList.addAll(index, c);
    }
    
    public E get(int index) {
      return this.backingList.get(index);
    }
    
    public E remove(int index) {
      return this.backingList.remove(index);
    }
    
    public E set(int index, E element) {
      return this.backingList.set(index, element);
    }
    
    public boolean contains(Object o) {
      return this.backingList.contains(o);
    }
    
    public int size() {
      return this.backingList.size();
    }
  }
  
  private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {
    RandomAccessListWrapper(List<E> backingList) {
      super(backingList);
    }
  }
  
  static <T> List<T> cast(Iterable<T> iterable) {
    return (List<T>)iterable;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */