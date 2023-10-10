package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class RegularContiguousSet extends ContiguousSet {
   private final Range range;
   private static final long serialVersionUID = 0L;

   RegularContiguousSet(Range var1, DiscreteDomain var2) {
      super(var2);
      this.range = var1;
   }

   private ContiguousSet intersectionInCurrentDomain(Range var1) {
      return (ContiguousSet)(this.range.isConnected(var1) ? ContiguousSet.create(this.range.intersection(var1), this.domain) : new EmptyContiguousSet(this.domain));
   }

   ContiguousSet headSetImpl(Comparable var1, boolean var2) {
      return this.intersectionInCurrentDomain(Range.upTo(var1, BoundType.forBoolean(var2)));
   }

   ContiguousSet subSetImpl(Comparable var1, boolean var2, Comparable var3, boolean var4) {
      return (ContiguousSet)(var1.compareTo(var3) == 0 && !var2 && !var4 ? new EmptyContiguousSet(this.domain) : this.intersectionInCurrentDomain(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4))));
   }

   ContiguousSet tailSetImpl(Comparable var1, boolean var2) {
      return this.intersectionInCurrentDomain(Range.downTo(var1, BoundType.forBoolean(var2)));
   }

   @GwtIncompatible("not used by GWT emulation")
   int indexOf(Object var1) {
      return var1 == null ? (int)this.domain.distance(this.first(), (Comparable)var1) : -1;
   }

   public UnmodifiableIterator iterator() {
      return new AbstractSequentialIterator(this, this.first()) {
         final Comparable last;
         final RegularContiguousSet this$0;

         {
            this.this$0 = var1;
            this.last = this.this$0.last();
         }

         protected Comparable computeNext(Comparable var1) {
            return RegularContiguousSet.access$000(var1, this.last) ? null : this.this$0.domain.next(var1);
         }

         protected Object computeNext(Object var1) {
            return this.computeNext((Comparable)var1);
         }
      };
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator descendingIterator() {
      return new AbstractSequentialIterator(this, this.last()) {
         final Comparable first;
         final RegularContiguousSet this$0;

         {
            this.this$0 = var1;
            this.first = this.this$0.first();
         }

         protected Comparable computeNext(Comparable var1) {
            return RegularContiguousSet.access$000(var1, this.first) ? null : this.this$0.domain.previous(var1);
         }

         protected Object computeNext(Object var1) {
            return this.computeNext((Comparable)var1);
         }
      };
   }

   private static boolean equalsOrThrow(Comparable var0, @Nullable Comparable var1) {
      return var1 != null && Range.compareOrThrow(var0, var1) == 0;
   }

   boolean isPartialView() {
      return false;
   }

   public Comparable first() {
      return this.range.lowerBound.leastValueAbove(this.domain);
   }

   public Comparable last() {
      return this.range.upperBound.greatestValueBelow(this.domain);
   }

   public int size() {
      long var1 = this.domain.distance(this.first(), this.last());
      return var1 >= 2147483647L ? Integer.MAX_VALUE : (int)var1 + 1;
   }

   public boolean containsAll(Collection var1) {
      return Collections2.containsAllImpl(this, var1);
   }

   public boolean isEmpty() {
      return false;
   }

   public ContiguousSet intersection(ContiguousSet var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(this.domain.equals(var1.domain));
      if (var1.isEmpty()) {
         return var1;
      } else {
         Comparable var2 = (Comparable)Ordering.natural().max(this.first(), var1.first());
         Comparable var3 = (Comparable)Ordering.natural().min(this.last(), var1.last());
         return (ContiguousSet)(var2.compareTo(var3) < 0 ? ContiguousSet.create(Range.closed(var2, var3), this.domain) : new EmptyContiguousSet(this.domain));
      }
   }

   public Range range() {
      return this.range(BoundType.CLOSED, BoundType.CLOSED);
   }

   public Range range(BoundType var1, BoundType var2) {
      return Range.create(this.range.lowerBound.withLowerBoundType(var1, this.domain), this.range.upperBound.withUpperBoundType(var2, this.domain));
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else {
         if (var1 instanceof RegularContiguousSet) {
            RegularContiguousSet var2 = (RegularContiguousSet)var1;
            if (this.domain.equals(var2.domain)) {
               return this.first().equals(var2.first()) && this.last().equals(var2.last());
            }
         }

         return super.equals(var1);
      }
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this);
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new RegularContiguousSet.SerializedForm(this.range, this.domain);
   }

   public Object last() {
      return this.last();
   }

   public Object first() {
      return this.first();
   }

   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this.tailSetImpl((Comparable)var1, var2);
   }

   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subSetImpl((Comparable)var1, var2, (Comparable)var3, var4);
   }

   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this.headSetImpl((Comparable)var1, var2);
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public Iterator iterator() {
      return this.iterator();
   }

   static boolean access$000(Comparable var0, Comparable var1) {
      return equalsOrThrow(var0, var1);
   }

   @GwtIncompatible("serialization")
   private static final class SerializedForm implements Serializable {
      final Range range;
      final DiscreteDomain domain;

      private SerializedForm(Range var1, DiscreteDomain var2) {
         this.range = var1;
         this.domain = var2;
      }

      private Object readResolve() {
         return new RegularContiguousSet(this.range, this.domain);
      }

      SerializedForm(Range var1, DiscreteDomain var2, Object var3) {
         this(var1, var2);
      }
   }
}
