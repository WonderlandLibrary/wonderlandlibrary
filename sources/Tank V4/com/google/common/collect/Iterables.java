package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Iterables {
   private Iterables() {
   }

   public static Iterable unmodifiableIterable(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return (Iterable)(!(var0 instanceof Iterables.UnmodifiableIterable) && !(var0 instanceof ImmutableCollection) ? new Iterables.UnmodifiableIterable(var0) : var0);
   }

   /** @deprecated */
   @Deprecated
   public static Iterable unmodifiableIterable(ImmutableCollection var0) {
      return (Iterable)Preconditions.checkNotNull(var0);
   }

   public static int size(Iterable var0) {
      return var0 instanceof Collection ? ((Collection)var0).size() : Iterators.size(var0.iterator());
   }

   public static boolean contains(Iterable var0, @Nullable Object var1) {
      if (var0 instanceof Collection) {
         Collection var2 = (Collection)var0;
         return Collections2.safeContains(var2, var1);
      } else {
         return Iterators.contains(var0.iterator(), var1);
      }
   }

   public static boolean removeAll(Iterable var0, Collection var1) {
      return var0 instanceof Collection ? ((Collection)var0).removeAll((Collection)Preconditions.checkNotNull(var1)) : Iterators.removeAll(var0.iterator(), var1);
   }

   public static boolean retainAll(Iterable var0, Collection var1) {
      return var0 instanceof Collection ? ((Collection)var0).retainAll((Collection)Preconditions.checkNotNull(var1)) : Iterators.retainAll(var0.iterator(), var1);
   }

   public static boolean removeIf(Iterable var0, Predicate var1) {
      return var0 instanceof RandomAccess && var0 instanceof List ? removeIfFromRandomAccessList((List)var0, (Predicate)Preconditions.checkNotNull(var1)) : Iterators.removeIf(var0.iterator(), var1);
   }

   private static boolean removeIfFromRandomAccessList(List var0, Predicate var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var0.size(); ++var2) {
         Object var4 = var0.get(var2);
         if (!var1.apply(var4)) {
            if (var2 > var3) {
               try {
                  var0.set(var3, var4);
               } catch (UnsupportedOperationException var6) {
                  slowRemoveIfForRemainingElements(var0, var1, var3, var2);
                  return true;
               }
            }

            ++var3;
         }
      }

      var0.subList(var3, var0.size()).clear();
      return var2 != var3;
   }

   private static void slowRemoveIfForRemainingElements(List var0, Predicate var1, int var2, int var3) {
      int var4;
      for(var4 = var0.size() - 1; var4 > var3; --var4) {
         if (var1.apply(var0.get(var4))) {
            var0.remove(var4);
         }
      }

      for(var4 = var3 - 1; var4 >= var2; --var4) {
         var0.remove(var4);
      }

   }

   @Nullable
   static Object removeFirstMatching(Iterable var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      Iterator var2 = var0.iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = var2.next();
      } while(!var1.apply(var3));

      var2.remove();
      return var3;
   }

   public static boolean elementsEqual(Iterable var0, Iterable var1) {
      if (var0 instanceof Collection && var1 instanceof Collection) {
         Collection var2 = (Collection)var0;
         Collection var3 = (Collection)var1;
         if (var2.size() != var3.size()) {
            return false;
         }
      }

      return Iterators.elementsEqual(var0.iterator(), var1.iterator());
   }

   public static String toString(Iterable var0) {
      return Iterators.toString(var0.iterator());
   }

   public static Object getOnlyElement(Iterable var0) {
      return Iterators.getOnlyElement(var0.iterator());
   }

   @Nullable
   public static Object getOnlyElement(Iterable var0, @Nullable Object var1) {
      return Iterators.getOnlyElement(var0.iterator(), var1);
   }

   @GwtIncompatible("Array.newInstance(Class, int)")
   public static Object[] toArray(Iterable var0, Class var1) {
      Collection var2 = toCollection(var0);
      Object[] var3 = ObjectArrays.newArray(var1, var2.size());
      return var2.toArray(var3);
   }

   static Object[] toArray(Iterable var0) {
      return toCollection(var0).toArray();
   }

   private static Collection toCollection(Iterable var0) {
      return (Collection)(var0 instanceof Collection ? (Collection)var0 : Lists.newArrayList(var0.iterator()));
   }

   public static boolean addAll(Collection var0, Iterable var1) {
      if (var1 instanceof Collection) {
         Collection var2 = Collections2.cast(var1);
         return var0.addAll(var2);
      } else {
         return Iterators.addAll(var0, ((Iterable)Preconditions.checkNotNull(var1)).iterator());
      }
   }

   public static int frequency(Iterable var0, @Nullable Object var1) {
      if (var0 instanceof Multiset) {
         return ((Multiset)var0).count(var1);
      } else if (var0 instanceof Set) {
         return ((Set)var0).contains(var1) ? 1 : 0;
      } else {
         return Iterators.frequency(var0.iterator(), var1);
      }
   }

   public static Iterable cycle(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return new FluentIterable(var0) {
         final Iterable val$iterable;

         {
            this.val$iterable = var1;
         }

         public Iterator iterator() {
            return Iterators.cycle(this.val$iterable);
         }

         public String toString() {
            return this.val$iterable.toString() + " (cycled)";
         }
      };
   }

   public static Iterable cycle(Object... var0) {
      return cycle((Iterable)Lists.newArrayList(var0));
   }

   public static Iterable concat(Iterable var0, Iterable var1) {
      return concat((Iterable)ImmutableList.of(var0, var1));
   }

   public static Iterable concat(Iterable var0, Iterable var1, Iterable var2) {
      return concat((Iterable)ImmutableList.of(var0, var1, var2));
   }

   public static Iterable concat(Iterable var0, Iterable var1, Iterable var2, Iterable var3) {
      return concat((Iterable)ImmutableList.of(var0, var1, var2, var3));
   }

   public static Iterable concat(Iterable... var0) {
      return concat((Iterable)ImmutableList.copyOf((Object[])var0));
   }

   public static Iterable concat(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return new FluentIterable(var0) {
         final Iterable val$inputs;

         {
            this.val$inputs = var1;
         }

         public Iterator iterator() {
            return Iterators.concat(Iterables.access$100(this.val$inputs));
         }
      };
   }

   private static Iterator iterators(Iterable var0) {
      return new TransformedIterator(var0.iterator()) {
         Iterator transform(Iterable var1) {
            return var1.iterator();
         }

         Object transform(Object var1) {
            return this.transform((Iterable)var1);
         }
      };
   }

   public static Iterable partition(Iterable var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return new FluentIterable(var0, var1) {
         final Iterable val$iterable;
         final int val$size;

         {
            this.val$iterable = var1;
            this.val$size = var2;
         }

         public Iterator iterator() {
            return Iterators.partition(this.val$iterable.iterator(), this.val$size);
         }
      };
   }

   public static Iterable paddedPartition(Iterable var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return new FluentIterable(var0, var1) {
         final Iterable val$iterable;
         final int val$size;

         {
            this.val$iterable = var1;
            this.val$size = var2;
         }

         public Iterator iterator() {
            return Iterators.paddedPartition(this.val$iterable.iterator(), this.val$size);
         }
      };
   }

   public static Iterable filter(Iterable var0, Predicate var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable(var0, var1) {
         final Iterable val$unfiltered;
         final Predicate val$predicate;

         {
            this.val$unfiltered = var1;
            this.val$predicate = var2;
         }

         public Iterator iterator() {
            return Iterators.filter(this.val$unfiltered.iterator(), this.val$predicate);
         }
      };
   }

   @GwtIncompatible("Class.isInstance")
   public static Iterable filter(Iterable var0, Class var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable(var0, var1) {
         final Iterable val$unfiltered;
         final Class val$type;

         {
            this.val$unfiltered = var1;
            this.val$type = var2;
         }

         public Iterator iterator() {
            return Iterators.filter(this.val$unfiltered.iterator(), this.val$type);
         }
      };
   }

   public static boolean any(Iterable var0, Predicate var1) {
      return Iterators.any(var0.iterator(), var1);
   }

   public static boolean all(Iterable var0, Predicate var1) {
      return Iterators.all(var0.iterator(), var1);
   }

   public static Object find(Iterable var0, Predicate var1) {
      return Iterators.find(var0.iterator(), var1);
   }

   @Nullable
   public static Object find(Iterable var0, Predicate var1, @Nullable Object var2) {
      return Iterators.find(var0.iterator(), var1, var2);
   }

   public static Optional tryFind(Iterable var0, Predicate var1) {
      return Iterators.tryFind(var0.iterator(), var1);
   }

   public static int indexOf(Iterable var0, Predicate var1) {
      return Iterators.indexOf(var0.iterator(), var1);
   }

   public static Iterable transform(Iterable var0, Function var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable(var0, var1) {
         final Iterable val$fromIterable;
         final Function val$function;

         {
            this.val$fromIterable = var1;
            this.val$function = var2;
         }

         public Iterator iterator() {
            return Iterators.transform(this.val$fromIterable.iterator(), this.val$function);
         }
      };
   }

   public static Object get(Iterable var0, int var1) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof List ? ((List)var0).get(var1) : Iterators.get(var0.iterator(), var1);
   }

   @Nullable
   public static Object get(Iterable var0, int var1, @Nullable Object var2) {
      Preconditions.checkNotNull(var0);
      Iterators.checkNonnegative(var1);
      if (var0 instanceof List) {
         List var4 = Lists.cast(var0);
         return var1 < var4.size() ? var4.get(var1) : var2;
      } else {
         Iterator var3 = var0.iterator();
         Iterators.advance(var3, var1);
         return Iterators.getNext(var3, var2);
      }
   }

   @Nullable
   public static Object getFirst(Iterable var0, @Nullable Object var1) {
      return Iterators.getNext(var0.iterator(), var1);
   }

   public static Object getLast(Iterable var0) {
      if (var0 instanceof List) {
         List var1 = (List)var0;
         if (var1.isEmpty()) {
            throw new NoSuchElementException();
         } else {
            return getLastInNonemptyList(var1);
         }
      } else {
         return Iterators.getLast(var0.iterator());
      }
   }

   @Nullable
   public static Object getLast(Iterable var0, @Nullable Object var1) {
      if (var0 instanceof Collection) {
         Collection var2 = Collections2.cast(var0);
         if (var2.isEmpty()) {
            return var1;
         }

         if (var0 instanceof List) {
            return getLastInNonemptyList(Lists.cast(var0));
         }
      }

      return Iterators.getLast(var0.iterator(), var1);
   }

   private static Object getLastInNonemptyList(List var0) {
      return var0.get(var0.size() - 1);
   }

   public static Iterable skip(Iterable var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "number to skip cannot be negative");
      if (var0 instanceof List) {
         List var2 = (List)var0;
         return new FluentIterable(var2, var1) {
            final List val$list;
            final int val$numberToSkip;

            {
               this.val$list = var1;
               this.val$numberToSkip = var2;
            }

            public Iterator iterator() {
               int var1 = Math.min(this.val$list.size(), this.val$numberToSkip);
               return this.val$list.subList(var1, this.val$list.size()).iterator();
            }
         };
      } else {
         return new FluentIterable(var0, var1) {
            final Iterable val$iterable;
            final int val$numberToSkip;

            {
               this.val$iterable = var1;
               this.val$numberToSkip = var2;
            }

            public Iterator iterator() {
               Iterator var1 = this.val$iterable.iterator();
               Iterators.advance(var1, this.val$numberToSkip);
               return new Iterator(this, var1) {
                  boolean atStart;
                  final Iterator val$iterator;
                  final <undefinedtype> this$0;

                  {
                     this.this$0 = var1;
                     this.val$iterator = var2;
                     this.atStart = true;
                  }

                  public boolean hasNext() {
                     return this.val$iterator.hasNext();
                  }

                  public Object next() {
                     Object var1 = this.val$iterator.next();
                     this.atStart = false;
                     return var1;
                  }

                  public void remove() {
                     CollectPreconditions.checkRemove(!this.atStart);
                     this.val$iterator.remove();
                  }
               };
            }
         };
      }
   }

   public static Iterable limit(Iterable var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "limit is negative");
      return new FluentIterable(var0, var1) {
         final Iterable val$iterable;
         final int val$limitSize;

         {
            this.val$iterable = var1;
            this.val$limitSize = var2;
         }

         public Iterator iterator() {
            return Iterators.limit(this.val$iterable.iterator(), this.val$limitSize);
         }
      };
   }

   public static Iterable consumingIterable(Iterable var0) {
      if (var0 instanceof Queue) {
         return new FluentIterable(var0) {
            final Iterable val$iterable;

            {
               this.val$iterable = var1;
            }

            public Iterator iterator() {
               return new Iterables.ConsumingQueueIterator((Queue)this.val$iterable);
            }

            public String toString() {
               return "Iterables.consumingIterable(...)";
            }
         };
      } else {
         Preconditions.checkNotNull(var0);
         return new FluentIterable(var0) {
            final Iterable val$iterable;

            {
               this.val$iterable = var1;
            }

            public Iterator iterator() {
               return Iterators.consumingIterator(this.val$iterable.iterator());
            }

            public String toString() {
               return "Iterables.consumingIterable(...)";
            }
         };
      }
   }

   public static boolean isEmpty(Iterable var0) {
      if (var0 instanceof Collection) {
         return ((Collection)var0).isEmpty();
      } else {
         return !var0.iterator().hasNext();
      }
   }

   @Beta
   public static Iterable mergeSorted(Iterable var0, Comparator var1) {
      Preconditions.checkNotNull(var0, "iterables");
      Preconditions.checkNotNull(var1, "comparator");
      FluentIterable var2 = new FluentIterable(var0, var1) {
         final Iterable val$iterables;
         final Comparator val$comparator;

         {
            this.val$iterables = var1;
            this.val$comparator = var2;
         }

         public Iterator iterator() {
            return Iterators.mergeSorted(Iterables.transform(this.val$iterables, Iterables.access$300()), this.val$comparator);
         }
      };
      return new Iterables.UnmodifiableIterable(var2);
   }

   private static Function toIterator() {
      return new Function() {
         public Iterator apply(Iterable var1) {
            return var1.iterator();
         }

         public Object apply(Object var1) {
            return this.apply((Iterable)var1);
         }
      };
   }

   static Iterator access$100(Iterable var0) {
      return iterators(var0);
   }

   static Function access$300() {
      return toIterator();
   }

   private static class ConsumingQueueIterator extends AbstractIterator {
      private final Queue queue;

      private ConsumingQueueIterator(Queue var1) {
         this.queue = var1;
      }

      public Object computeNext() {
         try {
            return this.queue.remove();
         } catch (NoSuchElementException var2) {
            return this.endOfData();
         }
      }

      ConsumingQueueIterator(Queue var1, Object var2) {
         this(var1);
      }
   }

   private static final class UnmodifiableIterable extends FluentIterable {
      private final Iterable iterable;

      private UnmodifiableIterable(Iterable var1) {
         this.iterable = var1;
      }

      public Iterator iterator() {
         return Iterators.unmodifiableIterator(this.iterable.iterator());
      }

      public String toString() {
         return this.iterable.toString();
      }

      UnmodifiableIterable(Iterable var1, Object var2) {
         this(var1);
      }
   }
}
