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

@GwtCompatible(
   emulated = true
)
public final class Lists {
   private Lists() {
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayList() {
      return new ArrayList();
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayList(Object... var0) {
      Preconditions.checkNotNull(var0);
      int var1 = computeArrayListCapacity(var0.length);
      ArrayList var2 = new ArrayList(var1);
      Collections.addAll(var2, var0);
      return var2;
   }

   @VisibleForTesting
   static int computeArrayListCapacity(int var0) {
      CollectPreconditions.checkNonnegative(var0, "arraySize");
      return Ints.saturatedCast(5L + (long)var0 + (long)(var0 / 10));
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayList(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof Collection ? new ArrayList(Collections2.cast(var0)) : newArrayList(var0.iterator());
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayList(Iterator var0) {
      ArrayList var1 = newArrayList();
      Iterators.addAll(var1, var0);
      return var1;
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayListWithCapacity(int var0) {
      CollectPreconditions.checkNonnegative(var0, "initialArraySize");
      return new ArrayList(var0);
   }

   @GwtCompatible(
      serializable = true
   )
   public static ArrayList newArrayListWithExpectedSize(int var0) {
      return new ArrayList(computeArrayListCapacity(var0));
   }

   @GwtCompatible(
      serializable = true
   )
   public static LinkedList newLinkedList() {
      return new LinkedList();
   }

   @GwtCompatible(
      serializable = true
   )
   public static LinkedList newLinkedList(Iterable var0) {
      LinkedList var1 = newLinkedList();
      Iterables.addAll(var1, var0);
      return var1;
   }

   @GwtIncompatible("CopyOnWriteArrayList")
   public static CopyOnWriteArrayList newCopyOnWriteArrayList() {
      return new CopyOnWriteArrayList();
   }

   @GwtIncompatible("CopyOnWriteArrayList")
   public static CopyOnWriteArrayList newCopyOnWriteArrayList(Iterable var0) {
      Object var1 = var0 instanceof Collection ? Collections2.cast(var0) : newArrayList(var0);
      return new CopyOnWriteArrayList((Collection)var1);
   }

   public static List asList(@Nullable Object var0, Object[] var1) {
      return new Lists.OnePlusArrayList(var0, var1);
   }

   public static List asList(@Nullable Object var0, @Nullable Object var1, Object[] var2) {
      return new Lists.TwoPlusArrayList(var0, var1, var2);
   }

   static List cartesianProduct(List var0) {
      return CartesianList.create(var0);
   }

   static List cartesianProduct(List... var0) {
      return cartesianProduct(Arrays.asList(var0));
   }

   public static List transform(List var0, Function var1) {
      return (List)(var0 instanceof RandomAccess ? new Lists.TransformingRandomAccessList(var0, var1) : new Lists.TransformingSequentialList(var0, var1));
   }

   public static List partition(List var0, int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return (List)(var0 instanceof RandomAccess ? new Lists.RandomAccessPartition(var0, var1) : new Lists.Partition(var0, var1));
   }

   @Beta
   public static ImmutableList charactersOf(String var0) {
      return new Lists.StringAsImmutableList((String)Preconditions.checkNotNull(var0));
   }

   @Beta
   public static List charactersOf(CharSequence var0) {
      return new Lists.CharSequenceAsList((CharSequence)Preconditions.checkNotNull(var0));
   }

   public static List reverse(List var0) {
      if (var0 instanceof ImmutableList) {
         return ((ImmutableList)var0).reverse();
      } else if (var0 instanceof Lists.ReverseList) {
         return ((Lists.ReverseList)var0).getForwardList();
      } else {
         return (List)(var0 instanceof RandomAccess ? new Lists.RandomAccessReverseList(var0) : new Lists.ReverseList(var0));
      }
   }

   static int hashCodeImpl(List var0) {
      int var1 = 1;

      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 = ~(~var1)) {
         Object var3 = var2.next();
         var1 = 31 * var1 + (var3 == null ? 0 : var3.hashCode());
      }

      return var1;
   }

   static boolean equalsImpl(List var0, @Nullable Object var1) {
      if (var1 == Preconditions.checkNotNull(var0)) {
         return true;
      } else if (!(var1 instanceof List)) {
         return false;
      } else {
         List var2 = (List)var1;
         return var0.size() == var2.size() && Iterators.elementsEqual(var0.iterator(), var2.iterator());
      }
   }

   static boolean addAllImpl(List var0, int var1, Iterable var2) {
      boolean var3 = false;
      ListIterator var4 = var0.listIterator(var1);

      for(Iterator var5 = var2.iterator(); var5.hasNext(); var3 = true) {
         Object var6 = var5.next();
         var4.add(var6);
      }

      return var3;
   }

   static int indexOfImpl(List var0, @Nullable Object var1) {
      ListIterator var2 = var0.listIterator();

      do {
         if (!var2.hasNext()) {
            return -1;
         }
      } while(!Objects.equal(var1, var2.next()));

      return var2.previousIndex();
   }

   static int lastIndexOfImpl(List var0, @Nullable Object var1) {
      ListIterator var2 = var0.listIterator(var0.size());

      do {
         if (!var2.hasPrevious()) {
            return -1;
         }
      } while(!Objects.equal(var1, var2.previous()));

      return var2.nextIndex();
   }

   static ListIterator listIteratorImpl(List var0, int var1) {
      return (new Lists.AbstractListWrapper(var0)).listIterator(var1);
   }

   static List subListImpl(List var0, int var1, int var2) {
      Object var3;
      if (var0 instanceof RandomAccess) {
         var3 = new Lists.RandomAccessListWrapper(var0) {
            private static final long serialVersionUID = 0L;

            public ListIterator listIterator(int var1) {
               return this.backingList.listIterator(var1);
            }
         };
      } else {
         var3 = new Lists.AbstractListWrapper(var0) {
            private static final long serialVersionUID = 0L;

            public ListIterator listIterator(int var1) {
               return this.backingList.listIterator(var1);
            }
         };
      }

      return ((List)var3).subList(var1, var2);
   }

   static List cast(Iterable var0) {
      return (List)var0;
   }

   private static class RandomAccessListWrapper extends Lists.AbstractListWrapper implements RandomAccess {
      RandomAccessListWrapper(List var1) {
         super(var1);
      }
   }

   private static class AbstractListWrapper extends AbstractList {
      final List backingList;

      AbstractListWrapper(List var1) {
         this.backingList = (List)Preconditions.checkNotNull(var1);
      }

      public void add(int var1, Object var2) {
         this.backingList.add(var1, var2);
      }

      public boolean addAll(int var1, Collection var2) {
         return this.backingList.addAll(var1, var2);
      }

      public Object get(int var1) {
         return this.backingList.get(var1);
      }

      public Object remove(int var1) {
         return this.backingList.remove(var1);
      }

      public Object set(int var1, Object var2) {
         return this.backingList.set(var1, var2);
      }

      public boolean contains(Object var1) {
         return this.backingList.contains(var1);
      }

      public int size() {
         return this.backingList.size();
      }
   }

   private static class RandomAccessReverseList extends Lists.ReverseList implements RandomAccess {
      RandomAccessReverseList(List var1) {
         super(var1);
      }
   }

   private static class ReverseList extends AbstractList {
      private final List forwardList;

      ReverseList(List var1) {
         this.forwardList = (List)Preconditions.checkNotNull(var1);
      }

      List getForwardList() {
         return this.forwardList;
      }

      private int reverseIndex(int var1) {
         int var2 = this.size();
         Preconditions.checkElementIndex(var1, var2);
         return var2 - 1 - var1;
      }

      private int reversePosition(int var1) {
         int var2 = this.size();
         Preconditions.checkPositionIndex(var1, var2);
         return var2 - var1;
      }

      public void add(int var1, @Nullable Object var2) {
         this.forwardList.add(this.reversePosition(var1), var2);
      }

      public void clear() {
         this.forwardList.clear();
      }

      public Object remove(int var1) {
         return this.forwardList.remove(this.reverseIndex(var1));
      }

      protected void removeRange(int var1, int var2) {
         this.subList(var1, var2).clear();
      }

      public Object set(int var1, @Nullable Object var2) {
         return this.forwardList.set(this.reverseIndex(var1), var2);
      }

      public Object get(int var1) {
         return this.forwardList.get(this.reverseIndex(var1));
      }

      public int size() {
         return this.forwardList.size();
      }

      public List subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.size());
         return Lists.reverse(this.forwardList.subList(this.reversePosition(var2), this.reversePosition(var1)));
      }

      public Iterator iterator() {
         return this.listIterator();
      }

      public ListIterator listIterator(int var1) {
         int var2 = this.reversePosition(var1);
         ListIterator var3 = this.forwardList.listIterator(var2);
         return new ListIterator(this, var3) {
            boolean canRemoveOrSet;
            final ListIterator val$forwardIterator;
            final Lists.ReverseList this$0;

            {
               this.this$0 = var1;
               this.val$forwardIterator = var2;
            }

            public void add(Object var1) {
               this.val$forwardIterator.add(var1);
               this.val$forwardIterator.previous();
               this.canRemoveOrSet = false;
            }

            public boolean hasNext() {
               return this.val$forwardIterator.hasPrevious();
            }

            public boolean hasPrevious() {
               return this.val$forwardIterator.hasNext();
            }

            public Object next() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  this.canRemoveOrSet = true;
                  return this.val$forwardIterator.previous();
               }
            }

            public int nextIndex() {
               return Lists.ReverseList.access$000(this.this$0, this.val$forwardIterator.nextIndex());
            }

            public Object previous() {
               if (!this.hasPrevious()) {
                  throw new NoSuchElementException();
               } else {
                  this.canRemoveOrSet = true;
                  return this.val$forwardIterator.next();
               }
            }

            public int previousIndex() {
               return this.nextIndex() - 1;
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.canRemoveOrSet);
               this.val$forwardIterator.remove();
               this.canRemoveOrSet = false;
            }

            public void set(Object var1) {
               Preconditions.checkState(this.canRemoveOrSet);
               this.val$forwardIterator.set(var1);
            }
         };
      }

      static int access$000(Lists.ReverseList var0, int var1) {
         return var0.reversePosition(var1);
      }
   }

   private static final class CharSequenceAsList extends AbstractList {
      private final CharSequence sequence;

      CharSequenceAsList(CharSequence var1) {
         this.sequence = var1;
      }

      public Character get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.sequence.charAt(var1);
      }

      public int size() {
         return this.sequence.length();
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static final class StringAsImmutableList extends ImmutableList {
      private final String string;

      StringAsImmutableList(String var1) {
         this.string = var1;
      }

      public int indexOf(@Nullable Object var1) {
         return var1 instanceof Character ? this.string.indexOf((Character)var1) : -1;
      }

      public int lastIndexOf(@Nullable Object var1) {
         return var1 instanceof Character ? this.string.lastIndexOf((Character)var1) : -1;
      }

      public ImmutableList subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.size());
         return Lists.charactersOf(this.string.substring(var1, var2));
      }

      boolean isPartialView() {
         return false;
      }

      public Character get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.string.charAt(var1);
      }

      public int size() {
         return this.string.length();
      }

      public List subList(int var1, int var2) {
         return this.subList(var1, var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static class RandomAccessPartition extends Lists.Partition implements RandomAccess {
      RandomAccessPartition(List var1, int var2) {
         super(var1, var2);
      }
   }

   private static class Partition extends AbstractList {
      final List list;
      final int size;

      Partition(List var1, int var2) {
         this.list = var1;
         this.size = var2;
      }

      public List get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         int var2 = var1 * this.size;
         int var3 = Math.min(var2 + this.size, this.list.size());
         return this.list.subList(var2, var3);
      }

      public int size() {
         return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
      }

      public boolean isEmpty() {
         return this.list.isEmpty();
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static class TransformingRandomAccessList extends AbstractList implements RandomAccess, Serializable {
      final List fromList;
      final Function function;
      private static final long serialVersionUID = 0L;

      TransformingRandomAccessList(List var1, Function var2) {
         this.fromList = (List)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public void clear() {
         this.fromList.clear();
      }

      public Object get(int var1) {
         return this.function.apply(this.fromList.get(var1));
      }

      public Iterator iterator() {
         return this.listIterator();
      }

      public ListIterator listIterator(int var1) {
         return new TransformedListIterator(this, this.fromList.listIterator(var1)) {
            final Lists.TransformingRandomAccessList this$0;

            {
               this.this$0 = var1;
            }

            Object transform(Object var1) {
               return this.this$0.function.apply(var1);
            }
         };
      }

      public boolean isEmpty() {
         return this.fromList.isEmpty();
      }

      public Object remove(int var1) {
         return this.function.apply(this.fromList.remove(var1));
      }

      public int size() {
         return this.fromList.size();
      }
   }

   private static class TransformingSequentialList extends AbstractSequentialList implements Serializable {
      final List fromList;
      final Function function;
      private static final long serialVersionUID = 0L;

      TransformingSequentialList(List var1, Function var2) {
         this.fromList = (List)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public void clear() {
         this.fromList.clear();
      }

      public int size() {
         return this.fromList.size();
      }

      public ListIterator listIterator(int var1) {
         return new TransformedListIterator(this, this.fromList.listIterator(var1)) {
            final Lists.TransformingSequentialList this$0;

            {
               this.this$0 = var1;
            }

            Object transform(Object var1) {
               return this.this$0.function.apply(var1);
            }
         };
      }
   }

   private static class TwoPlusArrayList extends AbstractList implements Serializable, RandomAccess {
      final Object first;
      final Object second;
      final Object[] rest;
      private static final long serialVersionUID = 0L;

      TwoPlusArrayList(@Nullable Object var1, @Nullable Object var2, Object[] var3) {
         this.first = var1;
         this.second = var2;
         this.rest = (Object[])Preconditions.checkNotNull(var3);
      }

      public int size() {
         return this.rest.length + 2;
      }

      public Object get(int var1) {
         switch(var1) {
         case 0:
            return this.first;
         case 1:
            return this.second;
         default:
            Preconditions.checkElementIndex(var1, this.size());
            return this.rest[var1 - 2];
         }
      }
   }

   private static class OnePlusArrayList extends AbstractList implements Serializable, RandomAccess {
      final Object first;
      final Object[] rest;
      private static final long serialVersionUID = 0L;

      OnePlusArrayList(@Nullable Object var1, Object[] var2) {
         this.first = var1;
         this.rest = (Object[])Preconditions.checkNotNull(var2);
      }

      public int size() {
         return this.rest.length + 1;
      }

      public Object get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return var1 == 0 ? this.first : this.rest[var1 - 1];
      }
   }
}
