package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class FluentIterable<E> implements Iterable<E> {
  private final Iterable<E> iterable;
  
  protected FluentIterable() {
    this.iterable = this;
  }
  
  FluentIterable(Iterable<E> iterable) {
    this.iterable = (Iterable<E>)Preconditions.checkNotNull(iterable);
  }
  
  public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
    return (iterable instanceof FluentIterable) ? (FluentIterable<E>)iterable : new FluentIterable<E>(iterable) {
        public Iterator<E> iterator() {
          return iterable.iterator();
        }
      };
  }
  
  @Deprecated
  public static <E> FluentIterable<E> from(FluentIterable<E> iterable) {
    return (FluentIterable<E>)Preconditions.checkNotNull(iterable);
  }
  
  public String toString() {
    return Iterables.toString(this.iterable);
  }
  
  public final int size() {
    return Iterables.size(this.iterable);
  }
  
  public final boolean contains(@Nullable Object element) {
    return Iterables.contains(this.iterable, element);
  }
  
  @CheckReturnValue
  public final FluentIterable<E> cycle() {
    return from(Iterables.cycle(this.iterable));
  }
  
  @CheckReturnValue
  public final FluentIterable<E> filter(Predicate<? super E> predicate) {
    return from(Iterables.filter(this.iterable, predicate));
  }
  
  @CheckReturnValue
  @GwtIncompatible("Class.isInstance")
  public final <T> FluentIterable<T> filter(Class<T> type) {
    return from(Iterables.filter(this.iterable, type));
  }
  
  public final boolean anyMatch(Predicate<? super E> predicate) {
    return Iterables.any(this.iterable, predicate);
  }
  
  public final boolean allMatch(Predicate<? super E> predicate) {
    return Iterables.all(this.iterable, predicate);
  }
  
  public final Optional<E> firstMatch(Predicate<? super E> predicate) {
    return Iterables.tryFind(this.iterable, predicate);
  }
  
  public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
    return from(Iterables.transform(this.iterable, function));
  }
  
  public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
    return from(Iterables.concat(transform(function)));
  }
  
  public final Optional<E> first() {
    Iterator<E> iterator = this.iterable.iterator();
    return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
  }
  
  public final Optional<E> last() {
    if (this.iterable instanceof List) {
      List<E> list = (List<E>)this.iterable;
      if (list.isEmpty())
        return Optional.absent(); 
      return Optional.of(list.get(list.size() - 1));
    } 
    Iterator<E> iterator = this.iterable.iterator();
    if (!iterator.hasNext())
      return Optional.absent(); 
    if (this.iterable instanceof SortedSet) {
      SortedSet<E> sortedSet = (SortedSet<E>)this.iterable;
      return Optional.of(sortedSet.last());
    } 
    while (true) {
      E current = iterator.next();
      if (!iterator.hasNext())
        return Optional.of(current); 
    } 
  }
  
  @CheckReturnValue
  public final FluentIterable<E> skip(int numberToSkip) {
    return from(Iterables.skip(this.iterable, numberToSkip));
  }
  
  @CheckReturnValue
  public final FluentIterable<E> limit(int size) {
    return from(Iterables.limit(this.iterable, size));
  }
  
  public final boolean isEmpty() {
    return !this.iterable.iterator().hasNext();
  }
  
  public final ImmutableList<E> toList() {
    return ImmutableList.copyOf(this.iterable);
  }
  
  @Beta
  public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
    return Ordering.<E>from(comparator).immutableSortedCopy(this.iterable);
  }
  
  public final ImmutableSet<E> toSet() {
    return ImmutableSet.copyOf(this.iterable);
  }
  
  public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
    return ImmutableSortedSet.copyOf(comparator, this.iterable);
  }
  
  public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) {
    return Maps.toMap(this.iterable, valueFunction);
  }
  
  public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) {
    return Multimaps.index(this.iterable, keyFunction);
  }
  
  public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) {
    return Maps.uniqueIndex(this.iterable, keyFunction);
  }
  
  @GwtIncompatible("Array.newArray(Class, int)")
  public final E[] toArray(Class<E> type) {
    return Iterables.toArray(this.iterable, type);
  }
  
  public final <C extends java.util.Collection<? super E>> C copyInto(C collection) {
    Preconditions.checkNotNull(collection);
    if (this.iterable instanceof java.util.Collection) {
      collection.addAll(Collections2.cast(this.iterable));
    } else {
      for (E item : this.iterable)
        collection.add(item); 
    } 
    return collection;
  }
  
  public final E get(int position) {
    return Iterables.get(this.iterable, position);
  }
  
  private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
    public FluentIterable<E> apply(Iterable<E> fromObject) {
      return FluentIterable.from(fromObject);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\FluentIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */