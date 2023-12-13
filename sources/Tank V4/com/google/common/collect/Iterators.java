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
import java.util.ArrayList;
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

@GwtCompatible(
   emulated = true
)
public final class Iterators {
   static final UnmodifiableListIterator EMPTY_LIST_ITERATOR = new UnmodifiableListIterator() {
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
   private static final Iterator EMPTY_MODIFIABLE_ITERATOR = new Iterator() {
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

   private Iterators() {
   }

   public static UnmodifiableIterator emptyIterator() {
      return emptyListIterator();
   }

   static UnmodifiableListIterator emptyListIterator() {
      return EMPTY_LIST_ITERATOR;
   }

   static Iterator emptyModifiableIterator() {
      return EMPTY_MODIFIABLE_ITERATOR;
   }

   public static UnmodifiableIterator unmodifiableIterator(Iterator var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof UnmodifiableIterator ? (UnmodifiableIterator)var0 : new UnmodifiableIterator(var0) {
         final Iterator val$iterator;

         {
            this.val$iterator = var1;
         }

         public boolean hasNext() {
            return this.val$iterator.hasNext();
         }

         public Object next() {
            return this.val$iterator.next();
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static UnmodifiableIterator unmodifiableIterator(UnmodifiableIterator var0) {
      return (UnmodifiableIterator)Preconditions.checkNotNull(var0);
   }

   public static int size(Iterator var0) {
      int var1;
      for(var1 = 0; var0.hasNext(); ++var1) {
         var0.next();
      }

      return var1;
   }

   public static boolean contains(Iterator var0, @Nullable Object var1) {
      return any(var0, Predicates.equalTo(var1));
   }

   public static boolean removeAll(Iterator var0, Collection var1) {
      return removeIf(var0, Predicates.in(var1));
   }

   public static boolean removeIf(Iterator var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      boolean var2 = false;

      while(var0.hasNext()) {
         if (var1.apply(var0.next())) {
            var0.remove();
            var2 = true;
         }
      }

      return var2;
   }

   public static boolean retainAll(Iterator var0, Collection var1) {
      return removeIf(var0, Predicates.not(Predicates.in(var1)));
   }

   public static boolean elementsEqual(Iterator var0, Iterator var1) {
      while(true) {
         if (var0.hasNext()) {
            if (!var1.hasNext()) {
               return false;
            }

            Object var2 = var0.next();
            Object var3 = var1.next();
            if (Objects.equal(var2, var3)) {
               continue;
            }

            return false;
         }

         return !var1.hasNext();
      }
   }

   public static String toString(Iterator var0) {
      return Collections2.STANDARD_JOINER.appendTo((new StringBuilder()).append('['), var0).append(']').toString();
   }

   public static Object getOnlyElement(Iterator var0) {
      Object var1 = var0.next();
      if (!var0.hasNext()) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("expected one element but was: <" + var1);

         for(int var3 = 0; var3 < 4 && var0.hasNext(); ++var3) {
            var2.append(", " + var0.next());
         }

         if (var0.hasNext()) {
            var2.append(", ...");
         }

         var2.append('>');
         throw new IllegalArgumentException(var2.toString());
      }
   }

   @Nullable
   public static Object getOnlyElement(Iterator var0, @Nullable Object var1) {
      return var0.hasNext() ? getOnlyElement(var0) : var1;
   }

   @GwtIncompatible("Array.newInstance(Class, int)")
   public static Object[] toArray(Iterator var0, Class var1) {
      ArrayList var2 = Lists.newArrayList(var0);
      return Iterables.toArray(var2, var1);
   }

   public static boolean addAll(Collection var0, Iterator var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);

      boolean var2;
      for(var2 = false; var1.hasNext(); var2 |= var0.add(var1.next())) {
      }

      return var2;
   }

   public static int frequency(Iterator var0, @Nullable Object var1) {
      return size(filter(var0, Predicates.equalTo(var1)));
   }

   public static Iterator cycle(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return new Iterator(var0) {
         Iterator iterator;
         Iterator removeFrom;
         final Iterable val$iterable;

         {
            this.val$iterable = var1;
            this.iterator = Iterators.emptyIterator();
         }

         public Object next() {
            if (this == false) {
               throw new NoSuchElementException();
            } else {
               this.removeFrom = this.iterator;
               return this.iterator.next();
            }
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.removeFrom != null);
            this.removeFrom.remove();
            this.removeFrom = null;
         }
      };
   }

   public static Iterator cycle(Object... var0) {
      return cycle((Iterable)Lists.newArrayList(var0));
   }

   public static Iterator concat(Iterator var0, Iterator var1) {
      return concat((Iterator)ImmutableList.of(var0, var1).iterator());
   }

   public static Iterator concat(Iterator var0, Iterator var1, Iterator var2) {
      return concat((Iterator)ImmutableList.of(var0, var1, var2).iterator());
   }

   public static Iterator concat(Iterator var0, Iterator var1, Iterator var2, Iterator var3) {
      return concat((Iterator)ImmutableList.of(var0, var1, var2, var3).iterator());
   }

   public static Iterator concat(Iterator... var0) {
      return concat((Iterator)ImmutableList.copyOf((Object[])var0).iterator());
   }

   public static Iterator concat(Iterator var0) {
      Preconditions.checkNotNull(var0);
      return new Iterator(var0) {
         Iterator current;
         Iterator removeFrom;
         final Iterator val$inputs;

         {
            this.val$inputs = var1;
            this.current = Iterators.emptyIterator();
         }

         public Object next() {
            if (this == false) {
               throw new NoSuchElementException();
            } else {
               this.removeFrom = this.current;
               return this.current.next();
            }
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.removeFrom != null);
            this.removeFrom.remove();
            this.removeFrom = null;
         }
      };
   }

   public static UnmodifiableIterator partition(Iterator var0, int var1) {
      return partitionImpl(var0, var1, false);
   }

   public static UnmodifiableIterator paddedPartition(Iterator var0, int var1) {
      return partitionImpl(var0, var1, true);
   }

   private static UnmodifiableIterator partitionImpl(Iterator var0, int var1, boolean var2) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return new UnmodifiableIterator(var0, var1, var2) {
         final Iterator val$iterator;
         final int val$size;
         final boolean val$pad;

         {
            this.val$iterator = var1;
            this.val$size = var2;
            this.val$pad = var3;
         }

         public boolean hasNext() {
            return this.val$iterator.hasNext();
         }

         public List next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               Object[] var1 = new Object[this.val$size];

               int var2;
               for(var2 = 0; var2 < this.val$size && this.val$iterator.hasNext(); ++var2) {
                  var1[var2] = this.val$iterator.next();
               }

               for(int var3 = var2; var3 < this.val$size; ++var3) {
                  var1[var3] = null;
               }

               List var4 = Collections.unmodifiableList(Arrays.asList(var1));
               return !this.val$pad && var2 != this.val$size ? var4.subList(0, var2) : var4;
            }
         }

         public Object next() {
            return this.next();
         }
      };
   }

   public static UnmodifiableIterator filter(Iterator var0, Predicate var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractIterator(var0, var1) {
         final Iterator val$unfiltered;
         final Predicate val$predicate;

         {
            this.val$unfiltered = var1;
            this.val$predicate = var2;
         }

         protected Object computeNext() {
            while(true) {
               if (this.val$unfiltered.hasNext()) {
                  Object var1 = this.val$unfiltered.next();
                  if (!this.val$predicate.apply(var1)) {
                     continue;
                  }

                  return var1;
               }

               return this.endOfData();
            }
         }
      };
   }

   @GwtIncompatible("Class.isInstance")
   public static UnmodifiableIterator filter(Iterator var0, Class var1) {
      return filter(var0, Predicates.instanceOf(var1));
   }

   public static boolean any(Iterator var0, Predicate var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean all(Iterator var0, Predicate var1) {
      Preconditions.checkNotNull(var1);

      Object var2;
      do {
         if (!var0.hasNext()) {
            return true;
         }

         var2 = var0.next();
      } while(var1.apply(var2));

      return false;
   }

   public static Object find(Iterator var0, Predicate var1) {
      return filter(var0, var1).next();
   }

   @Nullable
   public static Object find(Iterator var0, Predicate var1, @Nullable Object var2) {
      return getNext(filter(var0, var1), var2);
   }

   public static Optional tryFind(Iterator var0, Predicate var1) {
      UnmodifiableIterator var2 = filter(var0, var1);
      return var2.hasNext() ? Optional.of(var2.next()) : Optional.absent();
   }

   public static int indexOf(Iterator var0, Predicate var1) {
      Preconditions.checkNotNull(var1, "predicate");

      for(int var2 = 0; var0.hasNext(); ++var2) {
         Object var3 = var0.next();
         if (var1.apply(var3)) {
            return var2;
         }
      }

      return -1;
   }

   public static Iterator transform(Iterator var0, Function var1) {
      Preconditions.checkNotNull(var1);
      return new TransformedIterator(var0, var1) {
         final Function val$function;

         {
            this.val$function = var2;
         }

         Object transform(Object var1) {
            return this.val$function.apply(var1);
         }
      };
   }

   public static Object get(Iterator var0, int var1) {
      checkNonnegative(var1);
      int var2 = advance(var0, var1);
      if (!var0.hasNext()) {
         throw new IndexOutOfBoundsException("position (" + var1 + ") must be less than the number of elements that remained (" + var2 + ")");
      } else {
         return var0.next();
      }
   }

   static void checkNonnegative(int var0) {
      if (var0 < 0) {
         throw new IndexOutOfBoundsException("position (" + var0 + ") must not be negative");
      }
   }

   @Nullable
   public static Object get(Iterator var0, int var1, @Nullable Object var2) {
      checkNonnegative(var1);
      advance(var0, var1);
      return getNext(var0, var2);
   }

   @Nullable
   public static Object getNext(Iterator var0, @Nullable Object var1) {
      return var0.hasNext() ? var0.next() : var1;
   }

   public static Object getLast(Iterator var0) {
      Object var1;
      do {
         var1 = var0.next();
      } while(var0.hasNext());

      return var1;
   }

   @Nullable
   public static Object getLast(Iterator var0, @Nullable Object var1) {
      return var0.hasNext() ? getLast(var0) : var1;
   }

   public static int advance(Iterator var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "numberToAdvance must be nonnegative");

      int var2;
      for(var2 = 0; var2 < var1 && var0.hasNext(); ++var2) {
         var0.next();
      }

      return var2;
   }

   public static Iterator limit(Iterator var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "limit is negative");
      return new Iterator(var1, var0) {
         private int count;
         final int val$limitSize;
         final Iterator val$iterator;

         {
            this.val$limitSize = var1;
            this.val$iterator = var2;
         }

         public Object next() {
            // $FF: Couldn't be decompiled
         }

         public void remove() {
            this.val$iterator.remove();
         }
      };
   }

   public static Iterator consumingIterator(Iterator var0) {
      Preconditions.checkNotNull(var0);
      return new UnmodifiableIterator(var0) {
         final Iterator val$iterator;

         {
            this.val$iterator = var1;
         }

         public boolean hasNext() {
            return this.val$iterator.hasNext();
         }

         public Object next() {
            Object var1 = this.val$iterator.next();
            this.val$iterator.remove();
            return var1;
         }

         public String toString() {
            return "Iterators.consumingIterator(...)";
         }
      };
   }

   @Nullable
   static Object pollNext(Iterator var0) {
      if (var0.hasNext()) {
         Object var1 = var0.next();
         var0.remove();
         return var1;
      } else {
         return null;
      }
   }

   static void clear(Iterator var0) {
      Preconditions.checkNotNull(var0);

      while(var0.hasNext()) {
         var0.next();
         var0.remove();
      }

   }

   public static UnmodifiableIterator forArray(Object... var0) {
      return forArray(var0, 0, var0.length, 0);
   }

   static UnmodifiableListIterator forArray(Object[] var0, int var1, int var2, int var3) {
      Preconditions.checkArgument(var2 >= 0);
      int var4 = var1 + var2;
      Preconditions.checkPositionIndexes(var1, var4, var0.length);
      Preconditions.checkPositionIndex(var3, var2);
      return (UnmodifiableListIterator)(var2 == 0 ? emptyListIterator() : new AbstractIndexedListIterator(var2, var3, var0, var1) {
         final Object[] val$array;
         final int val$offset;

         {
            this.val$array = var3;
            this.val$offset = var4;
         }

         protected Object get(int var1) {
            return this.val$array[this.val$offset + var1];
         }
      });
   }

   public static UnmodifiableIterator singletonIterator(@Nullable Object var0) {
      return new UnmodifiableIterator(var0) {
         boolean done;
         final Object val$value;

         {
            this.val$value = var1;
         }

         public boolean hasNext() {
            return !this.done;
         }

         public Object next() {
            if (this.done) {
               throw new NoSuchElementException();
            } else {
               this.done = true;
               return this.val$value;
            }
         }
      };
   }

   public static UnmodifiableIterator forEnumeration(Enumeration var0) {
      Preconditions.checkNotNull(var0);
      return new UnmodifiableIterator(var0) {
         final Enumeration val$enumeration;

         {
            this.val$enumeration = var1;
         }

         public boolean hasNext() {
            return this.val$enumeration.hasMoreElements();
         }

         public Object next() {
            return this.val$enumeration.nextElement();
         }
      };
   }

   public static Enumeration asEnumeration(Iterator var0) {
      Preconditions.checkNotNull(var0);
      return new Enumeration(var0) {
         final Iterator val$iterator;

         {
            this.val$iterator = var1;
         }

         public boolean hasMoreElements() {
            return this.val$iterator.hasNext();
         }

         public Object nextElement() {
            return this.val$iterator.next();
         }
      };
   }

   public static PeekingIterator peekingIterator(Iterator var0) {
      if (var0 instanceof Iterators.PeekingImpl) {
         Iterators.PeekingImpl var1 = (Iterators.PeekingImpl)var0;
         return var1;
      } else {
         return new Iterators.PeekingImpl(var0);
      }
   }

   /** @deprecated */
   @Deprecated
   public static PeekingIterator peekingIterator(PeekingIterator var0) {
      return (PeekingIterator)Preconditions.checkNotNull(var0);
   }

   @Beta
   public static UnmodifiableIterator mergeSorted(Iterable var0, Comparator var1) {
      Preconditions.checkNotNull(var0, "iterators");
      Preconditions.checkNotNull(var1, "comparator");
      return new Iterators.MergingIterator(var0, var1);
   }

   static ListIterator cast(Iterator var0) {
      return (ListIterator)var0;
   }

   private static class MergingIterator extends UnmodifiableIterator {
      final Queue queue;

      public MergingIterator(Iterable var1, Comparator var2) {
         Comparator var3 = new Comparator(this, var2) {
            final Comparator val$itemComparator;
            final Iterators.MergingIterator this$0;

            {
               this.this$0 = var1;
               this.val$itemComparator = var2;
            }

            public int compare(PeekingIterator var1, PeekingIterator var2) {
               return this.val$itemComparator.compare(var1.peek(), var2.peek());
            }

            public int compare(Object var1, Object var2) {
               return this.compare((PeekingIterator)var1, (PeekingIterator)var2);
            }
         };
         this.queue = new PriorityQueue(2, var3);
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Iterator var5 = (Iterator)var4.next();
            if (var5.hasNext()) {
               this.queue.add(Iterators.peekingIterator(var5));
            }
         }

      }

      public boolean hasNext() {
         return !this.queue.isEmpty();
      }

      public Object next() {
         PeekingIterator var1 = (PeekingIterator)this.queue.remove();
         Object var2 = var1.next();
         if (var1.hasNext()) {
            this.queue.add(var1);
         }

         return var2;
      }
   }

   private static class PeekingImpl implements PeekingIterator {
      private final Iterator iterator;
      private boolean hasPeeked;
      private Object peekedElement;

      public PeekingImpl(Iterator var1) {
         this.iterator = (Iterator)Preconditions.checkNotNull(var1);
      }

      public boolean hasNext() {
         return this.hasPeeked || this.iterator.hasNext();
      }

      public Object next() {
         if (!this.hasPeeked) {
            return this.iterator.next();
         } else {
            Object var1 = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return var1;
         }
      }

      public void remove() {
         Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
         this.iterator.remove();
      }

      public Object peek() {
         if (!this.hasPeeked) {
            this.peekedElement = this.iterator.next();
            this.hasPeeked = true;
         }

         return this.peekedElement;
      }
   }
}
