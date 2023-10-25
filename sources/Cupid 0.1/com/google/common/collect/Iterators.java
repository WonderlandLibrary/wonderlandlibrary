package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Iterators {
  static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator() {
      public boolean hasNext() {
        return false;
      }
      
      public Object next() {
        throw new NoSuchElementException();
      }
      
      public boolean hasPrevious() {
        return false;
      }
      
      public Object previous() {
        throw new NoSuchElementException();
      }
      
      public int nextIndex() {
        return 0;
      }
      
      public int previousIndex() {
        return -1;
      }
    };
  
  public static <T> UnmodifiableIterator<T> emptyIterator() {
    return emptyListIterator();
  }
  
  static <T> UnmodifiableListIterator<T> emptyListIterator() {
    return (UnmodifiableListIterator)EMPTY_LIST_ITERATOR;
  }
  
  private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator() {
      public boolean hasNext() {
        return false;
      }
      
      public Object next() {
        throw new NoSuchElementException();
      }
      
      public void remove() {
        CollectPreconditions.checkRemove(false);
      }
    };
  
  static <T> Iterator<T> emptyModifiableIterator() {
    return (Iterator)EMPTY_MODIFIABLE_ITERATOR;
  }
  
  public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
    Preconditions.checkNotNull(iterator);
    if (iterator instanceof UnmodifiableIterator)
      return (UnmodifiableIterator<T>)iterator; 
    return new UnmodifiableIterator<T>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }
        
        public T next() {
          return iterator.next();
        }
      };
  }
  
  @Deprecated
  public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
    return (UnmodifiableIterator<T>)Preconditions.checkNotNull(iterator);
  }
  
  public static int size(Iterator<?> iterator) {
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    } 
    return count;
  }
  
  public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
    return any(iterator, Predicates.equalTo(element));
  }
  
  public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
    return removeIf(removeFrom, Predicates.in(elementsToRemove));
  }
  
  public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
    Preconditions.checkNotNull(predicate);
    boolean modified = false;
    while (removeFrom.hasNext()) {
      if (predicate.apply(removeFrom.next())) {
        removeFrom.remove();
        modified = true;
      } 
    } 
    return modified;
  }
  
  public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
    return removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain)));
  }
  
  public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
    while (iterator1.hasNext()) {
      if (!iterator2.hasNext())
        return false; 
      Object o1 = iterator1.next();
      Object o2 = iterator2.next();
      if (!Objects.equal(o1, o2))
        return false; 
    } 
    return !iterator2.hasNext();
  }
  
  public static String toString(Iterator<?> iterator) {
    return Collections2.STANDARD_JOINER.appendTo((new StringBuilder()).append('['), iterator).append(']').toString();
  }
  
  public static <T> T getOnlyElement(Iterator<T> iterator) {
    T first = iterator.next();
    if (!iterator.hasNext())
      return first; 
    StringBuilder sb = new StringBuilder();
    sb.append("expected one element but was: <" + first);
    for (int i = 0; i < 4 && iterator.hasNext(); i++)
      sb.append(", " + iterator.next()); 
    if (iterator.hasNext())
      sb.append(", ..."); 
    sb.append('>');
    throw new IllegalArgumentException(sb.toString());
  }
  
  @Nullable
  public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue) {
    return iterator.hasNext() ? getOnlyElement((Iterator)iterator) : defaultValue;
  }
  
  @GwtIncompatible("Array.newInstance(Class, int)")
  public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
    List<T> list = Lists.newArrayList(iterator);
    return Iterables.toArray(list, type);
  }
  
  public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
    Preconditions.checkNotNull(addTo);
    Preconditions.checkNotNull(iterator);
    boolean wasModified = false;
    while (iterator.hasNext())
      wasModified |= addTo.add(iterator.next()); 
    return wasModified;
  }
  
  public static int frequency(Iterator<?> iterator, @Nullable Object element) {
    return size(filter(iterator, Predicates.equalTo(element)));
  }
  
  public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
    Preconditions.checkNotNull(iterable);
    return new Iterator<T>() {
        Iterator<T> iterator = Iterators.emptyIterator();
        
        Iterator<T> removeFrom;
        
        public boolean hasNext() {
          if (!this.iterator.hasNext())
            this.iterator = iterable.iterator(); 
          return this.iterator.hasNext();
        }
        
        public T next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          this.removeFrom = this.iterator;
          return this.iterator.next();
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.removeFrom != null));
          this.removeFrom.remove();
          this.removeFrom = null;
        }
      };
  }
  
  public static <T> Iterator<T> cycle(T... elements) {
    return cycle(Lists.newArrayList(elements));
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
    return concat(ImmutableList.<Iterator<? extends T>>of(a, b).iterator());
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
    return concat(ImmutableList.<Iterator<? extends T>>of(a, b, c).iterator());
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
    return concat(ImmutableList.<Iterator<? extends T>>of(a, b, c, d).iterator());
  }
  
  public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
    return concat(ImmutableList.<Iterator<? extends T>>copyOf(inputs).iterator());
  }
  
  public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
    Preconditions.checkNotNull(inputs);
    return new Iterator<T>() {
        Iterator<? extends T> current = Iterators.emptyIterator();
        
        Iterator<? extends T> removeFrom;
        
        public boolean hasNext() {
          boolean currentHasNext;
          while (!(currentHasNext = ((Iterator)Preconditions.checkNotNull(this.current)).hasNext()) && inputs.hasNext())
            this.current = inputs.next(); 
          return currentHasNext;
        }
        
        public T next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          this.removeFrom = this.current;
          return this.current.next();
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.removeFrom != null));
          this.removeFrom.remove();
          this.removeFrom = null;
        }
      };
  }
  
  public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
    return partitionImpl(iterator, size, false);
  }
  
  public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
    return partitionImpl(iterator, size, true);
  }
  
  private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument((size > 0));
    return new UnmodifiableIterator<List<T>>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }
        
        public List<T> next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          Object[] array = new Object[size];
          int count = 0;
          for (; count < size && iterator.hasNext(); count++)
            array[count] = iterator.next(); 
          for (int i = count; i < size; i++)
            array[i] = null; 
          List<T> list = Collections.unmodifiableList(Arrays.asList((T[])array));
          return (pad || count == size) ? list : list.subList(0, count);
        }
      };
  }
  
  public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
    Preconditions.checkNotNull(unfiltered);
    Preconditions.checkNotNull(predicate);
    return new AbstractIterator<T>() {
        protected T computeNext() {
          while (unfiltered.hasNext()) {
            T element = unfiltered.next();
            if (predicate.apply(element))
              return element; 
          } 
          return endOfData();
        }
      };
  }
  
  @GwtIncompatible("Class.isInstance")
  public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> type) {
    return filter((Iterator)unfiltered, Predicates.instanceOf(type));
  }
  
  public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
    return (indexOf(iterator, predicate) != -1);
  }
  
  public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
    Preconditions.checkNotNull(predicate);
    while (iterator.hasNext()) {
      T element = iterator.next();
      if (!predicate.apply(element))
        return false; 
    } 
    return true;
  }
  
  public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
    return filter(iterator, predicate).next();
  }
  
  @Nullable
  public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue) {
    return getNext(filter(iterator, predicate), defaultValue);
  }
  
  public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
    UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
    return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
  }
  
  public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
    Preconditions.checkNotNull(predicate, "predicate");
    for (int i = 0; iterator.hasNext(); i++) {
      T current = iterator.next();
      if (predicate.apply(current))
        return i; 
    } 
    return -1;
  }
  
  public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
    Preconditions.checkNotNull(function);
    return new TransformedIterator<F, T>(fromIterator) {
        T transform(F from) {
          return (T)function.apply(from);
        }
      };
  }
  
  public static <T> T get(Iterator<T> iterator, int position) {
    checkNonnegative(position);
    int skipped = advance(iterator, position);
    if (!iterator.hasNext())
      throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")"); 
    return iterator.next();
  }
  
  static void checkNonnegative(int position) {
    if (position < 0)
      throw new IndexOutOfBoundsException("position (" + position + ") must not be negative"); 
  }
  
  @Nullable
  public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue) {
    checkNonnegative(position);
    advance(iterator, position);
    return getNext(iterator, defaultValue);
  }
  
  @Nullable
  public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue) {
    return iterator.hasNext() ? iterator.next() : defaultValue;
  }
  
  public static <T> T getLast(Iterator<T> iterator) {
    while (true) {
      T current = iterator.next();
      if (!iterator.hasNext())
        return current; 
    } 
  }
  
  @Nullable
  public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue) {
    return iterator.hasNext() ? getLast((Iterator)iterator) : defaultValue;
  }
  
  public static int advance(Iterator<?> iterator, int numberToAdvance) {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
    int i;
    for (i = 0; i < numberToAdvance && iterator.hasNext(); i++)
      iterator.next(); 
    return i;
  }
  
  public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument((limitSize >= 0), "limit is negative");
    return new Iterator<T>() {
        private int count;
        
        public boolean hasNext() {
          return (this.count < limitSize && iterator.hasNext());
        }
        
        public T next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          this.count++;
          return iterator.next();
        }
        
        public void remove() {
          iterator.remove();
        }
      };
  }
  
  public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
    Preconditions.checkNotNull(iterator);
    return new UnmodifiableIterator<T>() {
        public boolean hasNext() {
          return iterator.hasNext();
        }
        
        public T next() {
          T next = iterator.next();
          iterator.remove();
          return next;
        }
        
        public String toString() {
          return "Iterators.consumingIterator(...)";
        }
      };
  }
  
  @Nullable
  static <T> T pollNext(Iterator<T> iterator) {
    if (iterator.hasNext()) {
      T result = iterator.next();
      iterator.remove();
      return result;
    } 
    return null;
  }
  
  static void clear(Iterator<?> iterator) {
    Preconditions.checkNotNull(iterator);
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    } 
  }
  
  public static <T> UnmodifiableIterator<T> forArray(T... array) {
    return forArray(array, 0, array.length, 0);
  }
  
  static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index) {
    Preconditions.checkArgument((length >= 0));
    int end = offset + length;
    Preconditions.checkPositionIndexes(offset, end, array.length);
    Preconditions.checkPositionIndex(index, length);
    if (length == 0)
      return emptyListIterator(); 
    return new AbstractIndexedListIterator<T>(length, index) {
        protected T get(int index) {
          return (T)array[offset + index];
        }
      };
  }
  
  public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
    return new UnmodifiableIterator<T>() {
        boolean done;
        
        public boolean hasNext() {
          return !this.done;
        }
        
        public T next() {
          if (this.done)
            throw new NoSuchElementException(); 
          this.done = true;
          return (T)value;
        }
      };
  }
  
  public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
    Preconditions.checkNotNull(enumeration);
    return new UnmodifiableIterator<T>() {
        public boolean hasNext() {
          return enumeration.hasMoreElements();
        }
        
        public T next() {
          return enumeration.nextElement();
        }
      };
  }
  
  public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
    Preconditions.checkNotNull(iterator);
    return new Enumeration<T>() {
        public boolean hasMoreElements() {
          return iterator.hasNext();
        }
        
        public T nextElement() {
          return iterator.next();
        }
      };
  }
  
  private static class PeekingImpl<E> implements PeekingIterator<E> {
    private final Iterator<? extends E> iterator;
    
    private boolean hasPeeked;
    
    private E peekedElement;
    
    public PeekingImpl(Iterator<? extends E> iterator) {
      this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
    }
    
    public boolean hasNext() {
      return (this.hasPeeked || this.iterator.hasNext());
    }
    
    public E next() {
      if (!this.hasPeeked)
        return this.iterator.next(); 
      E result = this.peekedElement;
      this.hasPeeked = false;
      this.peekedElement = null;
      return result;
    }
    
    public void remove() {
      Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
      this.iterator.remove();
    }
    
    public E peek() {
      if (!this.hasPeeked) {
        this.peekedElement = this.iterator.next();
        this.hasPeeked = true;
      } 
      return this.peekedElement;
    }
  }
  
  public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
    if (iterator instanceof PeekingImpl) {
      PeekingImpl<T> peeking = (PeekingImpl)iterator;
      return peeking;
    } 
    return new PeekingImpl<T>(iterator);
  }
  
  @Deprecated
  public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
    return (PeekingIterator<T>)Preconditions.checkNotNull(iterator);
  }
  
  @Beta
  public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
    Preconditions.checkNotNull(iterators, "iterators");
    Preconditions.checkNotNull(comparator, "comparator");
    return new MergingIterator<T>(iterators, comparator);
  }
  
  private static class MergingIterator<T> extends UnmodifiableIterator<T> {
    final Queue<PeekingIterator<T>> queue;
    
    public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
      Comparator<PeekingIterator<T>> heapComparator = (Comparator)new Comparator<PeekingIterator<PeekingIterator<T>>>() {
          public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2) {
            return itemComparator.compare(o1.peek(), o2.peek());
          }
        };
      this.queue = new PriorityQueue<PeekingIterator<T>>(2, heapComparator);
      for (Iterator<? extends T> iterator : iterators) {
        if (iterator.hasNext())
          this.queue.add(Iterators.peekingIterator(iterator)); 
      } 
    }
    
    public boolean hasNext() {
      return !this.queue.isEmpty();
    }
    
    public T next() {
      PeekingIterator<T> nextIter = this.queue.remove();
      T next = nextIter.next();
      if (nextIter.hasNext())
        this.queue.add(nextIter); 
      return next;
    }
  }
  
  static <T> ListIterator<T> cast(Iterator<T> iterator) {
    return (ListIterator<T>)iterator;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */