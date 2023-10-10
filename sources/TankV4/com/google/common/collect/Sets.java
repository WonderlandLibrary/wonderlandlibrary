package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Sets {
   private Sets() {
   }

   @GwtCompatible(
      serializable = true
   )
   public static ImmutableSet immutableEnumSet(Enum var0, Enum... var1) {
      return ImmutableEnumSet.asImmutable(EnumSet.of(var0, var1));
   }

   @GwtCompatible(
      serializable = true
   )
   public static ImmutableSet immutableEnumSet(Iterable var0) {
      if (var0 instanceof ImmutableEnumSet) {
         return (ImmutableEnumSet)var0;
      } else if (var0 instanceof Collection) {
         Collection var3 = (Collection)var0;
         return var3.isEmpty() ? ImmutableSet.of() : ImmutableEnumSet.asImmutable(EnumSet.copyOf(var3));
      } else {
         Iterator var1 = var0.iterator();
         if (var1.hasNext()) {
            EnumSet var2 = EnumSet.of((Enum)var1.next());
            Iterators.addAll(var2, var1);
            return ImmutableEnumSet.asImmutable(var2);
         } else {
            return ImmutableSet.of();
         }
      }
   }

   public static EnumSet newEnumSet(Iterable var0, Class var1) {
      EnumSet var2 = EnumSet.noneOf(var1);
      Iterables.addAll(var2, var0);
      return var2;
   }

   public static HashSet newHashSet() {
      return new HashSet();
   }

   public static HashSet newHashSet(Object... var0) {
      HashSet var1 = newHashSetWithExpectedSize(var0.length);
      Collections.addAll(var1, var0);
      return var1;
   }

   public static HashSet newHashSetWithExpectedSize(int var0) {
      return new HashSet(Maps.capacity(var0));
   }

   public static HashSet newHashSet(Iterable var0) {
      return var0 instanceof Collection ? new HashSet(Collections2.cast(var0)) : newHashSet(var0.iterator());
   }

   public static HashSet newHashSet(Iterator var0) {
      HashSet var1 = newHashSet();
      Iterators.addAll(var1, var0);
      return var1;
   }

   public static Set newConcurrentHashSet() {
      return newSetFromMap(new ConcurrentHashMap());
   }

   public static Set newConcurrentHashSet(Iterable var0) {
      Set var1 = newConcurrentHashSet();
      Iterables.addAll(var1, var0);
      return var1;
   }

   public static LinkedHashSet newLinkedHashSet() {
      return new LinkedHashSet();
   }

   public static LinkedHashSet newLinkedHashSetWithExpectedSize(int var0) {
      return new LinkedHashSet(Maps.capacity(var0));
   }

   public static LinkedHashSet newLinkedHashSet(Iterable var0) {
      if (var0 instanceof Collection) {
         return new LinkedHashSet(Collections2.cast(var0));
      } else {
         LinkedHashSet var1 = newLinkedHashSet();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static TreeSet newTreeSet() {
      return new TreeSet();
   }

   public static TreeSet newTreeSet(Iterable var0) {
      TreeSet var1 = newTreeSet();
      Iterables.addAll(var1, var0);
      return var1;
   }

   public static TreeSet newTreeSet(Comparator var0) {
      return new TreeSet((Comparator)Preconditions.checkNotNull(var0));
   }

   public static Set newIdentityHashSet() {
      return newSetFromMap(Maps.newIdentityHashMap());
   }

   @GwtIncompatible("CopyOnWriteArraySet")
   public static CopyOnWriteArraySet newCopyOnWriteArraySet() {
      return new CopyOnWriteArraySet();
   }

   @GwtIncompatible("CopyOnWriteArraySet")
   public static CopyOnWriteArraySet newCopyOnWriteArraySet(Iterable var0) {
      Object var1 = var0 instanceof Collection ? Collections2.cast(var0) : Lists.newArrayList(var0);
      return new CopyOnWriteArraySet((Collection)var1);
   }

   public static EnumSet complementOf(Collection var0) {
      if (var0 instanceof EnumSet) {
         return EnumSet.complementOf((EnumSet)var0);
      } else {
         Preconditions.checkArgument(!var0.isEmpty(), "collection is empty; use the other version of this method");
         Class var1 = ((Enum)var0.iterator().next()).getDeclaringClass();
         return makeComplementByHand(var0, var1);
      }
   }

   public static EnumSet complementOf(Collection var0, Class var1) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof EnumSet ? EnumSet.complementOf((EnumSet)var0) : makeComplementByHand(var0, var1);
   }

   private static EnumSet makeComplementByHand(Collection var0, Class var1) {
      EnumSet var2 = EnumSet.allOf(var1);
      var2.removeAll(var0);
      return var2;
   }

   public static Set newSetFromMap(Map var0) {
      return Platform.newSetFromMap(var0);
   }

   public static Sets.SetView union(Set var0, Set var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      Sets.SetView var2 = difference(var1, var0);
      return new Sets.SetView(var0, var2, var1) {
         final Set val$set1;
         final Set val$set2minus1;
         final Set val$set2;

         {
            this.val$set1 = var1;
            this.val$set2minus1 = var2;
            this.val$set2 = var3;
         }

         public int size() {
            return this.val$set1.size() + this.val$set2minus1.size();
         }

         public boolean isEmpty() {
            return this.val$set1.isEmpty() && this.val$set2.isEmpty();
         }

         public Iterator iterator() {
            return Iterators.unmodifiableIterator(Iterators.concat(this.val$set1.iterator(), this.val$set2minus1.iterator()));
         }

         public boolean contains(Object var1) {
            return this.val$set1.contains(var1) || this.val$set2.contains(var1);
         }

         public Set copyInto(Set var1) {
            var1.addAll(this.val$set1);
            var1.addAll(this.val$set2);
            return var1;
         }

         public ImmutableSet immutableCopy() {
            return (new ImmutableSet.Builder()).addAll((Iterable)this.val$set1).addAll((Iterable)this.val$set2).build();
         }
      };
   }

   public static Sets.SetView intersection(Set var0, Set var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      Predicate var2 = Predicates.in(var1);
      return new Sets.SetView(var0, var2, var1) {
         final Set val$set1;
         final Predicate val$inSet2;
         final Set val$set2;

         {
            this.val$set1 = var1;
            this.val$inSet2 = var2;
            this.val$set2 = var3;
         }

         public Iterator iterator() {
            return Iterators.filter(this.val$set1.iterator(), this.val$inSet2);
         }

         public int size() {
            return Iterators.size(this.iterator());
         }

         public boolean isEmpty() {
            return !this.iterator().hasNext();
         }

         public boolean contains(Object var1) {
            return this.val$set1.contains(var1) && this.val$set2.contains(var1);
         }

         public boolean containsAll(Collection var1) {
            return this.val$set1.containsAll(var1) && this.val$set2.containsAll(var1);
         }
      };
   }

   public static Sets.SetView difference(Set var0, Set var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      Predicate var2 = Predicates.not(Predicates.in(var1));
      return new Sets.SetView(var0, var2, var1) {
         final Set val$set1;
         final Predicate val$notInSet2;
         final Set val$set2;

         {
            this.val$set1 = var1;
            this.val$notInSet2 = var2;
            this.val$set2 = var3;
         }

         public Iterator iterator() {
            return Iterators.filter(this.val$set1.iterator(), this.val$notInSet2);
         }

         public int size() {
            return Iterators.size(this.iterator());
         }

         public boolean isEmpty() {
            return this.val$set2.containsAll(this.val$set1);
         }

         public boolean contains(Object var1) {
            return this.val$set1.contains(var1) && !this.val$set2.contains(var1);
         }
      };
   }

   public static Sets.SetView symmetricDifference(Set var0, Set var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      return difference(union(var0, var1), intersection(var0, var1));
   }

   public static Set filter(Set var0, Predicate var1) {
      if (var0 instanceof SortedSet) {
         return filter((SortedSet)var0, var1);
      } else if (var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredSet((Set)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredSet((Set)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   public static SortedSet filter(SortedSet var0, Predicate var1) {
      return Platform.setsFilterSortedSet(var0, var1);
   }

   static SortedSet filterSortedIgnoreNavigable(SortedSet var0, Predicate var1) {
      if (var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredSortedSet((SortedSet)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredSortedSet((SortedSet)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   @GwtIncompatible("NavigableSet")
   public static NavigableSet filter(NavigableSet var0, Predicate var1) {
      if (var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredNavigableSet((NavigableSet)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredNavigableSet((NavigableSet)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   public static Set cartesianProduct(List var0) {
      return Sets.CartesianSet.create(var0);
   }

   public static Set cartesianProduct(Set... var0) {
      return cartesianProduct(Arrays.asList(var0));
   }

   @GwtCompatible(
      serializable = false
   )
   public static Set powerSet(Set var0) {
      return new Sets.PowerSet(var0);
   }

   static int hashCodeImpl(Set var0) {
      int var1 = 0;

      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 = ~(~var1)) {
         Object var3 = var2.next();
         var1 += var3 != null ? var3.hashCode() : 0;
      }

      return var1;
   }

   static boolean equalsImpl(Set var0, @Nullable Object var1) {
      if (var0 == var1) {
         return true;
      } else if (var1 instanceof Set) {
         Set var2 = (Set)var1;

         try {
            return var0.size() == var2.size() && var0.containsAll(var2);
         } catch (NullPointerException var4) {
            return false;
         } catch (ClassCastException var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   @GwtIncompatible("NavigableSet")
   public static NavigableSet unmodifiableNavigableSet(NavigableSet var0) {
      return (NavigableSet)(!(var0 instanceof ImmutableSortedSet) && !(var0 instanceof Sets.UnmodifiableNavigableSet) ? new Sets.UnmodifiableNavigableSet(var0) : var0);
   }

   @GwtIncompatible("NavigableSet")
   public static NavigableSet synchronizedNavigableSet(NavigableSet var0) {
      return Synchronized.navigableSet(var0);
   }

   static boolean removeAllImpl(Set var0, Iterator var1) {
      boolean var2;
      for(var2 = false; var1.hasNext(); var2 |= var0.remove(var1.next())) {
      }

      return var2;
   }

   static boolean removeAllImpl(Set var0, Collection var1) {
      Preconditions.checkNotNull(var1);
      if (var1 instanceof Multiset) {
         var1 = ((Multiset)var1).elementSet();
      }

      return var1 instanceof Set && ((Collection)var1).size() > var0.size() ? Iterators.removeAll(var0.iterator(), (Collection)var1) : removeAllImpl(var0, ((Collection)var1).iterator());
   }

   @GwtIncompatible("NavigableSet")
   static class DescendingSet extends ForwardingNavigableSet {
      private final NavigableSet forward;

      DescendingSet(NavigableSet var1) {
         this.forward = var1;
      }

      protected NavigableSet delegate() {
         return this.forward;
      }

      public Object lower(Object var1) {
         return this.forward.higher(var1);
      }

      public Object floor(Object var1) {
         return this.forward.ceiling(var1);
      }

      public Object ceiling(Object var1) {
         return this.forward.floor(var1);
      }

      public Object higher(Object var1) {
         return this.forward.lower(var1);
      }

      public Object pollFirst() {
         return this.forward.pollLast();
      }

      public Object pollLast() {
         return this.forward.pollFirst();
      }

      public NavigableSet descendingSet() {
         return this.forward;
      }

      public Iterator descendingIterator() {
         return this.forward.iterator();
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return this.forward.subSet(var3, var4, var1, var2).descendingSet();
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return this.forward.tailSet(var1, var2).descendingSet();
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return this.forward.headSet(var1, var2).descendingSet();
      }

      public Comparator comparator() {
         Comparator var1 = this.forward.comparator();
         return var1 == null ? Ordering.natural().reverse() : reverse(var1);
      }

      private static Ordering reverse(Comparator var0) {
         return Ordering.from(var0).reverse();
      }

      public Object first() {
         return this.forward.last();
      }

      public SortedSet headSet(Object var1) {
         return this.standardHeadSet(var1);
      }

      public Object last() {
         return this.forward.first();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.standardSubSet(var1, var2);
      }

      public SortedSet tailSet(Object var1) {
         return this.standardTailSet(var1);
      }

      public Iterator iterator() {
         return this.forward.descendingIterator();
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      public String toString() {
         return this.standardToString();
      }

      protected SortedSet delegate() {
         return this.delegate();
      }

      protected Set delegate() {
         return this.delegate();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableSet")
   static final class UnmodifiableNavigableSet extends ForwardingSortedSet implements NavigableSet, Serializable {
      private final NavigableSet delegate;
      private transient Sets.UnmodifiableNavigableSet descendingSet;
      private static final long serialVersionUID = 0L;

      UnmodifiableNavigableSet(NavigableSet var1) {
         this.delegate = (NavigableSet)Preconditions.checkNotNull(var1);
      }

      protected SortedSet delegate() {
         return Collections.unmodifiableSortedSet(this.delegate);
      }

      public Object lower(Object var1) {
         return this.delegate.lower(var1);
      }

      public Object floor(Object var1) {
         return this.delegate.floor(var1);
      }

      public Object ceiling(Object var1) {
         return this.delegate.ceiling(var1);
      }

      public Object higher(Object var1) {
         return this.delegate.higher(var1);
      }

      public Object pollFirst() {
         throw new UnsupportedOperationException();
      }

      public Object pollLast() {
         throw new UnsupportedOperationException();
      }

      public NavigableSet descendingSet() {
         Sets.UnmodifiableNavigableSet var1 = this.descendingSet;
         if (var1 == null) {
            var1 = this.descendingSet = new Sets.UnmodifiableNavigableSet(this.delegate.descendingSet());
            var1.descendingSet = this;
         }

         return var1;
      }

      public Iterator descendingIterator() {
         return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return Sets.unmodifiableNavigableSet(this.delegate.subSet(var1, var2, var3, var4));
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return Sets.unmodifiableNavigableSet(this.delegate.headSet(var1, var2));
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return Sets.unmodifiableNavigableSet(this.delegate.tailSet(var1, var2));
      }

      protected Set delegate() {
         return this.delegate();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static final class PowerSet extends AbstractSet {
      final ImmutableMap inputSet;

      PowerSet(Set var1) {
         ImmutableMap.Builder var2 = ImmutableMap.builder();
         int var3 = 0;
         Iterator var4 = ((Set)Preconditions.checkNotNull(var1)).iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var2.put(var5, var3++);
         }

         this.inputSet = var2.build();
         Preconditions.checkArgument(this.inputSet.size() <= 30, "Too many elements to create power set: %s > 30", this.inputSet.size());
      }

      public int size() {
         return 1 << this.inputSet.size();
      }

      public boolean isEmpty() {
         return false;
      }

      public Iterator iterator() {
         return new AbstractIndexedListIterator(this, this.size()) {
            final Sets.PowerSet this$0;

            {
               this.this$0 = var1;
            }

            protected Set get(int var1) {
               return new Sets.SubSet(this.this$0.inputSet, var1);
            }

            protected Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public boolean contains(@Nullable Object var1) {
         if (var1 instanceof Set) {
            Set var2 = (Set)var1;
            return this.inputSet.keySet().containsAll(var2);
         } else {
            return false;
         }
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Sets.PowerSet) {
            Sets.PowerSet var2 = (Sets.PowerSet)var1;
            return this.inputSet.equals(var2.inputSet);
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
      }

      public String toString() {
         return "powerSet(" + this.inputSet + ")";
      }
   }

   private static final class SubSet extends AbstractSet {
      private final ImmutableMap inputSet;
      private final int mask;

      SubSet(ImmutableMap var1, int var2) {
         this.inputSet = var1;
         this.mask = var2;
      }

      public Iterator iterator() {
         return new UnmodifiableIterator(this) {
            final ImmutableList elements;
            int remainingSetBits;
            final Sets.SubSet this$0;

            {
               this.this$0 = var1;
               this.elements = Sets.SubSet.access$100(this.this$0).keySet().asList();
               this.remainingSetBits = Sets.SubSet.access$200(this.this$0);
            }

            public boolean hasNext() {
               return this.remainingSetBits != 0;
            }

            public Object next() {
               int var1 = Integer.numberOfTrailingZeros(this.remainingSetBits);
               if (var1 == 32) {
                  throw new NoSuchElementException();
               } else {
                  this.remainingSetBits &= ~(1 << var1);
                  return this.elements.get(var1);
               }
            }
         };
      }

      public int size() {
         return Integer.bitCount(this.mask);
      }

      public boolean contains(@Nullable Object var1) {
         Integer var2 = (Integer)this.inputSet.get(var1);
         return var2 != null && (this.mask & 1 << var2) != 0;
      }

      static ImmutableMap access$100(Sets.SubSet var0) {
         return var0.inputSet;
      }

      static int access$200(Sets.SubSet var0) {
         return var0.mask;
      }
   }

   private static final class CartesianSet extends ForwardingCollection implements Set {
      private final transient ImmutableList axes;
      private final transient CartesianList delegate;

      static Set create(List var0) {
         ImmutableList.Builder var1 = new ImmutableList.Builder(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Set var3 = (Set)var2.next();
            ImmutableSet var4 = ImmutableSet.copyOf((Collection)var3);
            if (var4.isEmpty()) {
               return ImmutableSet.of();
            }

            var1.add((Object)var4);
         }

         ImmutableList var5 = var1.build();
         ImmutableList var6 = new ImmutableList(var5) {
            final ImmutableList val$axes;

            {
               this.val$axes = var1;
            }

            public int size() {
               return this.val$axes.size();
            }

            public List get(int var1) {
               return ((ImmutableSet)this.val$axes.get(var1)).asList();
            }

            boolean isPartialView() {
               return true;
            }

            public Object get(int var1) {
               return this.get(var1);
            }
         };
         return new Sets.CartesianSet(var5, new CartesianList(var6));
      }

      private CartesianSet(ImmutableList var1, CartesianList var2) {
         this.axes = var1;
         this.delegate = var2;
      }

      protected Collection delegate() {
         return this.delegate;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Sets.CartesianSet) {
            Sets.CartesianSet var2 = (Sets.CartesianSet)var1;
            return this.axes.equals(var2.axes);
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         int var1 = this.size() - 1;

         int var2;
         for(var2 = 0; var2 < this.axes.size(); ++var2) {
            var1 *= 31;
            var1 = ~(~var1);
         }

         var2 = 1;

         for(Iterator var3 = this.axes.iterator(); var3.hasNext(); var2 = ~(~var2)) {
            Set var4 = (Set)var3.next();
            var2 = 31 * var2 + this.size() / var4.size() * var4.hashCode();
         }

         var2 += var1;
         return ~(~var2);
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableSet")
   private static class FilteredNavigableSet extends Sets.FilteredSortedSet implements NavigableSet {
      FilteredNavigableSet(NavigableSet var1, Predicate var2) {
         super(var1, var2);
      }

      NavigableSet unfiltered() {
         return (NavigableSet)this.unfiltered;
      }

      @Nullable
      public Object lower(Object var1) {
         return Iterators.getNext(this.headSet(var1, false).descendingIterator(), (Object)null);
      }

      @Nullable
      public Object floor(Object var1) {
         return Iterators.getNext(this.headSet(var1, true).descendingIterator(), (Object)null);
      }

      public Object ceiling(Object var1) {
         return Iterables.getFirst(this.tailSet(var1, true), (Object)null);
      }

      public Object higher(Object var1) {
         return Iterables.getFirst(this.tailSet(var1, false), (Object)null);
      }

      public Object pollFirst() {
         return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
      }

      public Object pollLast() {
         return Iterables.removeFirstMatching(this.unfiltered().descendingSet(), this.predicate);
      }

      public NavigableSet descendingSet() {
         return Sets.filter(this.unfiltered().descendingSet(), this.predicate);
      }

      public Iterator descendingIterator() {
         return Iterators.filter(this.unfiltered().descendingIterator(), this.predicate);
      }

      public Object last() {
         return this.descendingIterator().next();
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return Sets.filter(this.unfiltered().subSet(var1, var2, var3, var4), this.predicate);
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return Sets.filter(this.unfiltered().headSet(var1, var2), this.predicate);
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return Sets.filter(this.unfiltered().tailSet(var1, var2), this.predicate);
      }
   }

   private static class FilteredSortedSet extends Sets.FilteredSet implements SortedSet {
      FilteredSortedSet(SortedSet var1, Predicate var2) {
         super(var1, var2);
      }

      public Comparator comparator() {
         return ((SortedSet)this.unfiltered).comparator();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).subSet(var1, var2), this.predicate);
      }

      public SortedSet headSet(Object var1) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).headSet(var1), this.predicate);
      }

      public SortedSet tailSet(Object var1) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).tailSet(var1), this.predicate);
      }

      public Object first() {
         return this.iterator().next();
      }

      public Object last() {
         SortedSet var1 = (SortedSet)this.unfiltered;

         while(true) {
            Object var2 = var1.last();
            if (this.predicate.apply(var2)) {
               return var2;
            }

            var1 = var1.headSet(var2);
         }
      }
   }

   private static class FilteredSet extends Collections2.FilteredCollection implements Set {
      FilteredSet(Set var1, Predicate var2) {
         super(var1, var2);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   public abstract static class SetView extends AbstractSet {
      private SetView() {
      }

      public ImmutableSet immutableCopy() {
         return ImmutableSet.copyOf((Collection)this);
      }

      public Set copyInto(Set var1) {
         var1.addAll(this);
         return var1;
      }

      SetView(Object var1) {
         this();
      }
   }

   abstract static class ImprovedAbstractSet extends AbstractSet {
      public boolean removeAll(Collection var1) {
         return Sets.removeAllImpl(this, (Collection)var1);
      }

      public boolean retainAll(Collection var1) {
         return super.retainAll((Collection)Preconditions.checkNotNull(var1));
      }
   }
}
