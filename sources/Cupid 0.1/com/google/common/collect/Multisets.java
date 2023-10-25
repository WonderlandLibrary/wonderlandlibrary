package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets {
  public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
    if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
      return (Multiset)multiset; 
    return new UnmodifiableMultiset<E>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
  }
  
  @Deprecated
  public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
    return (Multiset<E>)Preconditions.checkNotNull(multiset);
  }
  
  static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
    final Multiset<? extends E> delegate;
    
    transient Set<E> elementSet;
    
    transient Set<Multiset.Entry<E>> entrySet;
    
    private static final long serialVersionUID = 0L;
    
    UnmodifiableMultiset(Multiset<? extends E> delegate) {
      this.delegate = delegate;
    }
    
    protected Multiset<E> delegate() {
      return (Multiset)this.delegate;
    }
    
    Set<E> createElementSet() {
      return Collections.unmodifiableSet(this.delegate.elementSet());
    }
    
    public Set<E> elementSet() {
      Set<E> es = this.elementSet;
      return (es == null) ? (this.elementSet = createElementSet()) : es;
    }
    
    public Set<Multiset.Entry<E>> entrySet() {
      Set<Multiset.Entry<E>> es = this.entrySet;
      return (es == null) ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
    }
    
    public Iterator<E> iterator() {
      return Iterators.unmodifiableIterator((Iterator)this.delegate.iterator());
    }
    
    public boolean add(E element) {
      throw new UnsupportedOperationException();
    }
    
    public int add(E element, int occurences) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends E> elementsToAdd) {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object element) {
      throw new UnsupportedOperationException();
    }
    
    public int remove(Object element, int occurrences) {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> elementsToRemove) {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> elementsToRetain) {
      throw new UnsupportedOperationException();
    }
    
    public void clear() {
      throw new UnsupportedOperationException();
    }
    
    public int setCount(E element, int count) {
      throw new UnsupportedOperationException();
    }
    
    public boolean setCount(E element, int oldCount, int newCount) {
      throw new UnsupportedOperationException();
    }
  }
  
  @Beta
  public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
    return new UnmodifiableSortedMultiset<E>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
  }
  
  public static <E> Multiset.Entry<E> immutableEntry(@Nullable E e, int n) {
    return new ImmutableEntry<E>(e, n);
  }
  
  static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
    @Nullable
    final E element;
    
    final int count;
    
    private static final long serialVersionUID = 0L;
    
    ImmutableEntry(@Nullable E element, int count) {
      this.element = element;
      this.count = count;
      CollectPreconditions.checkNonnegative(count, "count");
    }
    
    @Nullable
    public E getElement() {
      return this.element;
    }
    
    public int getCount() {
      return this.count;
    }
  }
  
  @Beta
  public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
    if (unfiltered instanceof FilteredMultiset) {
      FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
      Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
      return new FilteredMultiset<E>(filtered.unfiltered, combinedPredicate);
    } 
    return new FilteredMultiset<E>(unfiltered, predicate);
  }
  
  private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
    final Multiset<E> unfiltered;
    
    final Predicate<? super E> predicate;
    
    FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
      this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
      this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
    }
    
    public UnmodifiableIterator<E> iterator() {
      return Iterators.filter(this.unfiltered.iterator(), this.predicate);
    }
    
    Set<E> createElementSet() {
      return Sets.filter(this.unfiltered.elementSet(), this.predicate);
    }
    
    Set<Multiset.Entry<E>> createEntrySet() {
      return Sets.filter(this.unfiltered.entrySet(), new Predicate<Multiset.Entry<E>>() {
            public boolean apply(Multiset.Entry<E> entry) {
              return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
            }
          });
    }
    
    Iterator<Multiset.Entry<E>> entryIterator() {
      throw new AssertionError("should never be called");
    }
    
    int distinctElements() {
      return elementSet().size();
    }
    
    public int count(@Nullable Object element) {
      int count = this.unfiltered.count(element);
      if (count > 0) {
        E e = (E)element;
        return this.predicate.apply(e) ? count : 0;
      } 
      return 0;
    }
    
    public int add(@Nullable E element, int occurrences) {
      Preconditions.checkArgument(this.predicate.apply(element), "Element %s does not match predicate %s", new Object[] { element, this.predicate });
      return this.unfiltered.add(element, occurrences);
    }
    
    public int remove(@Nullable Object element, int occurrences) {
      CollectPreconditions.checkNonnegative(occurrences, "occurrences");
      if (occurrences == 0)
        return count(element); 
      return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
    }
    
    public void clear() {
      elementSet().clear();
    }
  }
  
  static int inferDistinctElements(Iterable<?> elements) {
    if (elements instanceof Multiset)
      return ((Multiset)elements).elementSet().size(); 
    return 11;
  }
  
  @Beta
  public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
    Preconditions.checkNotNull(multiset1);
    Preconditions.checkNotNull(multiset2);
    return new AbstractMultiset<E>() {
        public boolean contains(@Nullable Object element) {
          return (multiset1.contains(element) || multiset2.contains(element));
        }
        
        public boolean isEmpty() {
          return (multiset1.isEmpty() && multiset2.isEmpty());
        }
        
        public int count(Object element) {
          return Math.max(multiset1.count(element), multiset2.count(element));
        }
        
        Set<E> createElementSet() {
          return Sets.union(multiset1.elementSet(), multiset2.elementSet());
        }
        
        Iterator<Multiset.Entry<E>> entryIterator() {
          final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
          final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
          return new AbstractIterator() {
              protected Multiset.Entry<E> computeNext() {
                if (iterator1.hasNext()) {
                  Multiset.Entry<? extends E> entry1 = iterator1.next();
                  E element = entry1.getElement();
                  int count = Math.max(entry1.getCount(), multiset2.count(element));
                  return Multisets.immutableEntry(element, count);
                } 
                while (iterator2.hasNext()) {
                  Multiset.Entry<? extends E> entry2 = iterator2.next();
                  E element = entry2.getElement();
                  if (!multiset1.contains(element))
                    return Multisets.immutableEntry(element, entry2.getCount()); 
                } 
                return endOfData();
              }
            };
        }
        
        int distinctElements() {
          return elementSet().size();
        }
      };
  }
  
  public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
    Preconditions.checkNotNull(multiset1);
    Preconditions.checkNotNull(multiset2);
    return new AbstractMultiset<E>() {
        public int count(Object element) {
          int count1 = multiset1.count(element);
          return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
        }
        
        Set<E> createElementSet() {
          return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
        }
        
        Iterator<Multiset.Entry<E>> entryIterator() {
          final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
          return new AbstractIterator() {
              protected Multiset.Entry<E> computeNext() {
                while (iterator1.hasNext()) {
                  Multiset.Entry<E> entry1 = iterator1.next();
                  E element = entry1.getElement();
                  int count = Math.min(entry1.getCount(), multiset2.count(element));
                  if (count > 0)
                    return Multisets.immutableEntry(element, count); 
                } 
                return endOfData();
              }
            };
        }
        
        int distinctElements() {
          return elementSet().size();
        }
      };
  }
  
  @Beta
  public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
    Preconditions.checkNotNull(multiset1);
    Preconditions.checkNotNull(multiset2);
    return new AbstractMultiset<E>() {
        public boolean contains(@Nullable Object element) {
          return (multiset1.contains(element) || multiset2.contains(element));
        }
        
        public boolean isEmpty() {
          return (multiset1.isEmpty() && multiset2.isEmpty());
        }
        
        public int size() {
          return multiset1.size() + multiset2.size();
        }
        
        public int count(Object element) {
          return multiset1.count(element) + multiset2.count(element);
        }
        
        Set<E> createElementSet() {
          return Sets.union(multiset1.elementSet(), multiset2.elementSet());
        }
        
        Iterator<Multiset.Entry<E>> entryIterator() {
          final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
          final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
          return new AbstractIterator() {
              protected Multiset.Entry<E> computeNext() {
                if (iterator1.hasNext()) {
                  Multiset.Entry<? extends E> entry1 = iterator1.next();
                  E element = entry1.getElement();
                  int count = entry1.getCount() + multiset2.count(element);
                  return Multisets.immutableEntry(element, count);
                } 
                while (iterator2.hasNext()) {
                  Multiset.Entry<? extends E> entry2 = iterator2.next();
                  E element = entry2.getElement();
                  if (!multiset1.contains(element))
                    return Multisets.immutableEntry(element, entry2.getCount()); 
                } 
                return endOfData();
              }
            };
        }
        
        int distinctElements() {
          return elementSet().size();
        }
      };
  }
  
  @Beta
  public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
    Preconditions.checkNotNull(multiset1);
    Preconditions.checkNotNull(multiset2);
    return new AbstractMultiset<E>() {
        public int count(@Nullable Object element) {
          int count1 = multiset1.count(element);
          return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
        }
        
        Iterator<Multiset.Entry<E>> entryIterator() {
          final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
          return new AbstractIterator() {
              protected Multiset.Entry<E> computeNext() {
                while (iterator1.hasNext()) {
                  Multiset.Entry<E> entry1 = iterator1.next();
                  E element = entry1.getElement();
                  int count = entry1.getCount() - multiset2.count(element);
                  if (count > 0)
                    return Multisets.immutableEntry(element, count); 
                } 
                return endOfData();
              }
            };
        }
        
        int distinctElements() {
          return Iterators.size(entryIterator());
        }
      };
  }
  
  public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
    Preconditions.checkNotNull(superMultiset);
    Preconditions.checkNotNull(subMultiset);
    for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
      int superCount = superMultiset.count(entry.getElement());
      if (superCount < entry.getCount())
        return false; 
    } 
    return true;
  }
  
  public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
    return retainOccurrencesImpl(multisetToModify, multisetToRetain);
  }
  
  private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
    Preconditions.checkNotNull(multisetToModify);
    Preconditions.checkNotNull(occurrencesToRetain);
    Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
    boolean changed = false;
    while (entryIterator.hasNext()) {
      Multiset.Entry<E> entry = entryIterator.next();
      int retainCount = occurrencesToRetain.count(entry.getElement());
      if (retainCount == 0) {
        entryIterator.remove();
        changed = true;
        continue;
      } 
      if (retainCount < entry.getCount()) {
        multisetToModify.setCount(entry.getElement(), retainCount);
        changed = true;
      } 
    } 
    return changed;
  }
  
  public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
    return removeOccurrencesImpl(multisetToModify, occurrencesToRemove);
  }
  
  private static <E> boolean removeOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRemove) {
    Preconditions.checkNotNull(multisetToModify);
    Preconditions.checkNotNull(occurrencesToRemove);
    boolean changed = false;
    Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Multiset.Entry<E> entry = entryIterator.next();
      int removeCount = occurrencesToRemove.count(entry.getElement());
      if (removeCount >= entry.getCount()) {
        entryIterator.remove();
        changed = true;
        continue;
      } 
      if (removeCount > 0) {
        multisetToModify.remove(entry.getElement(), removeCount);
        changed = true;
      } 
    } 
    return changed;
  }
  
  static abstract class AbstractEntry<E> implements Multiset.Entry<E> {
    public boolean equals(@Nullable Object object) {
      if (object instanceof Multiset.Entry) {
        Multiset.Entry<?> that = (Multiset.Entry)object;
        return (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement()));
      } 
      return false;
    }
    
    public int hashCode() {
      E e = getElement();
      return ((e == null) ? 0 : e.hashCode()) ^ getCount();
    }
    
    public String toString() {
      String text = String.valueOf(getElement());
      int n = getCount();
      return (n == 1) ? text : (text + " x " + n);
    }
  }
  
  static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
    if (object == multiset)
      return true; 
    if (object instanceof Multiset) {
      Multiset<?> that = (Multiset)object;
      if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size())
        return false; 
      for (Multiset.Entry<?> entry : that.entrySet()) {
        if (multiset.count(entry.getElement()) != entry.getCount())
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
    if (elements.isEmpty())
      return false; 
    if (elements instanceof Multiset) {
      Multiset<? extends E> that = cast(elements);
      for (Multiset.Entry<? extends E> entry : that.entrySet())
        self.add(entry.getElement(), entry.getCount()); 
    } else {
      Iterators.addAll(self, elements.iterator());
    } 
    return true;
  }
  
  static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
    Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
    return self.elementSet().removeAll(collection);
  }
  
  static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
    Preconditions.checkNotNull(elementsToRetain);
    Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
    return self.elementSet().retainAll(collection);
  }
  
  static <E> int setCountImpl(Multiset<E> self, E element, int count) {
    CollectPreconditions.checkNonnegative(count, "count");
    int oldCount = self.count(element);
    int delta = count - oldCount;
    if (delta > 0) {
      self.add(element, delta);
    } else if (delta < 0) {
      self.remove(element, -delta);
    } 
    return oldCount;
  }
  
  static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
    CollectPreconditions.checkNonnegative(oldCount, "oldCount");
    CollectPreconditions.checkNonnegative(newCount, "newCount");
    if (self.count(element) == oldCount) {
      self.setCount(element, newCount);
      return true;
    } 
    return false;
  }
  
  static abstract class ElementSet<E> extends Sets.ImprovedAbstractSet<E> {
    abstract Multiset<E> multiset();
    
    public void clear() {
      multiset().clear();
    }
    
    public boolean contains(Object o) {
      return multiset().contains(o);
    }
    
    public boolean containsAll(Collection<?> c) {
      return multiset().containsAll(c);
    }
    
    public boolean isEmpty() {
      return multiset().isEmpty();
    }
    
    public Iterator<E> iterator() {
      return new TransformedIterator<Multiset.Entry<E>, E>(multiset().entrySet().iterator()) {
          E transform(Multiset.Entry<E> entry) {
            return entry.getElement();
          }
        };
    }
    
    public boolean remove(Object o) {
      int count = multiset().count(o);
      if (count > 0) {
        multiset().remove(o, count);
        return true;
      } 
      return false;
    }
    
    public int size() {
      return multiset().entrySet().size();
    }
  }
  
  static abstract class EntrySet<E> extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
    abstract Multiset<E> multiset();
    
    public boolean contains(@Nullable Object o) {
      if (o instanceof Multiset.Entry) {
        Multiset.Entry<?> entry = (Multiset.Entry)o;
        if (entry.getCount() <= 0)
          return false; 
        int count = multiset().count(entry.getElement());
        return (count == entry.getCount());
      } 
      return false;
    }
    
    public boolean remove(Object object) {
      if (object instanceof Multiset.Entry) {
        Multiset.Entry<?> entry = (Multiset.Entry)object;
        Object element = entry.getElement();
        int entryCount = entry.getCount();
        if (entryCount != 0) {
          Multiset<Object> multiset = (Multiset)multiset();
          return multiset.setCount(element, entryCount, 0);
        } 
      } 
      return false;
    }
    
    public void clear() {
      multiset().clear();
    }
  }
  
  static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
    return new MultisetIteratorImpl<E>(multiset, multiset.entrySet().iterator());
  }
  
  static final class MultisetIteratorImpl<E> implements Iterator<E> {
    private final Multiset<E> multiset;
    
    private final Iterator<Multiset.Entry<E>> entryIterator;
    
    private Multiset.Entry<E> currentEntry;
    
    private int laterCount;
    
    private int totalCount;
    
    private boolean canRemove;
    
    MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
      this.multiset = multiset;
      this.entryIterator = entryIterator;
    }
    
    public boolean hasNext() {
      return (this.laterCount > 0 || this.entryIterator.hasNext());
    }
    
    public E next() {
      if (!hasNext())
        throw new NoSuchElementException(); 
      if (this.laterCount == 0) {
        this.currentEntry = this.entryIterator.next();
        this.totalCount = this.laterCount = this.currentEntry.getCount();
      } 
      this.laterCount--;
      this.canRemove = true;
      return this.currentEntry.getElement();
    }
    
    public void remove() {
      CollectPreconditions.checkRemove(this.canRemove);
      if (this.totalCount == 1) {
        this.entryIterator.remove();
      } else {
        this.multiset.remove(this.currentEntry.getElement());
      } 
      this.totalCount--;
      this.canRemove = false;
    }
  }
  
  static int sizeImpl(Multiset<?> multiset) {
    long size = 0L;
    for (Multiset.Entry<?> entry : multiset.entrySet())
      size += entry.getCount(); 
    return Ints.saturatedCast(size);
  }
  
  static <T> Multiset<T> cast(Iterable<T> iterable) {
    return (Multiset<T>)iterable;
  }
  
  private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>() {
      public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
        return Ints.compare(entry2.getCount(), entry1.getCount());
      }
    };
  
  @Beta
  public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
    List<Multiset.Entry<E>> sortedEntries = DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet());
    return ImmutableMultiset.copyFromEntries(sortedEntries);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */