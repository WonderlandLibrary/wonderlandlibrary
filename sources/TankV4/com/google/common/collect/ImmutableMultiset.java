package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMultiset extends ImmutableCollection implements Multiset {
   private static final ImmutableMultiset EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
   private transient ImmutableSet entrySet;

   public static ImmutableMultiset of() {
      return EMPTY;
   }

   public static ImmutableMultiset of(Object var0) {
      return copyOfInternal(var0);
   }

   public static ImmutableMultiset of(Object var0, Object var1) {
      return copyOfInternal(var0, var1);
   }

   public static ImmutableMultiset of(Object var0, Object var1, Object var2) {
      return copyOfInternal(var0, var1, var2);
   }

   public static ImmutableMultiset of(Object var0, Object var1, Object var2, Object var3) {
      return copyOfInternal(var0, var1, var2, var3);
   }

   public static ImmutableMultiset of(Object var0, Object var1, Object var2, Object var3, Object var4) {
      return copyOfInternal(var0, var1, var2, var3, var4);
   }

   public static ImmutableMultiset of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object... var6) {
      return (new ImmutableMultiset.Builder()).add(var0).add(var1).add(var2).add(var3).add(var4).add(var5).add(var6).build();
   }

   public static ImmutableMultiset copyOf(Object[] var0) {
      return copyOf((Iterable)Arrays.asList(var0));
   }

   public static ImmutableMultiset copyOf(Iterable var0) {
      if (var0 instanceof ImmutableMultiset) {
         ImmutableMultiset var1 = (ImmutableMultiset)var0;
         if (!var1.isPartialView()) {
            return var1;
         }
      }

      Object var2 = var0 instanceof Multiset ? Multisets.cast(var0) : LinkedHashMultiset.create(var0);
      return copyOfInternal((Multiset)var2);
   }

   private static ImmutableMultiset copyOfInternal(Object... var0) {
      return copyOf((Iterable)Arrays.asList(var0));
   }

   private static ImmutableMultiset copyOfInternal(Multiset var0) {
      return copyFromEntries(var0.entrySet());
   }

   static ImmutableMultiset copyFromEntries(Collection var0) {
      long var1 = 0L;
      ImmutableMap.Builder var3 = ImmutableMap.builder();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Multiset.Entry var5 = (Multiset.Entry)var4.next();
         int var6 = var5.getCount();
         if (var6 > 0) {
            var3.put(var5.getElement(), var6);
            var1 += (long)var6;
         }
      }

      if (var1 == 0L) {
         return of();
      } else {
         return new RegularImmutableMultiset(var3.build(), Ints.saturatedCast(var1));
      }
   }

   public static ImmutableMultiset copyOf(Iterator var0) {
      LinkedHashMultiset var1 = LinkedHashMultiset.create();
      Iterators.addAll(var1, var0);
      return copyOfInternal((Multiset)var1);
   }

   ImmutableMultiset() {
   }

   public UnmodifiableIterator iterator() {
      UnmodifiableIterator var1 = this.entrySet().iterator();
      return new UnmodifiableIterator(this, var1) {
         int remaining;
         Object element;
         final Iterator val$entryIterator;
         final ImmutableMultiset this$0;

         {
            this.this$0 = var1;
            this.val$entryIterator = var2;
         }

         public boolean hasNext() {
            return this.remaining > 0 || this.val$entryIterator.hasNext();
         }

         public Object next() {
            if (this.remaining <= 0) {
               Multiset.Entry var1 = (Multiset.Entry)this.val$entryIterator.next();
               this.element = var1.getElement();
               this.remaining = var1.getCount();
            }

            --this.remaining;
            return this.element;
         }
      };
   }

   public boolean contains(@Nullable Object var1) {
      return this.count(var1) > 0;
   }

   public boolean containsAll(Collection var1) {
      return this.elementSet().containsAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public final int add(Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final int remove(Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final int setCount(Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean setCount(Object var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("not present in emulated superclass")
   int copyIntoArray(Object[] var1, int var2) {
      Multiset.Entry var4;
      for(Iterator var3 = this.entrySet().iterator(); var3.hasNext(); var2 += var4.getCount()) {
         var4 = (Multiset.Entry)var3.next();
         Arrays.fill(var1, var2, var2 + var4.getCount(), var4.getElement());
      }

      return var2;
   }

   public boolean equals(@Nullable Object var1) {
      return Multisets.equalsImpl(this, var1);
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this.entrySet());
   }

   public String toString() {
      return this.entrySet().toString();
   }

   public ImmutableSet entrySet() {
      ImmutableSet var1 = this.entrySet;
      return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
   }

   private final ImmutableSet createEntrySet() {
      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new ImmutableMultiset.EntrySet(this));
   }

   abstract Multiset.Entry getEntry(int var1);

   Object writeReplace() {
      return new ImmutableMultiset.SerializedForm(this);
   }

   public static ImmutableMultiset.Builder builder() {
      return new ImmutableMultiset.Builder();
   }

   public Iterator iterator() {
      return this.iterator();
   }

   public Set entrySet() {
      return this.entrySet();
   }

   public static class Builder extends ImmutableCollection.Builder {
      final Multiset contents;

      public Builder() {
         this(LinkedHashMultiset.create());
      }

      Builder(Multiset var1) {
         this.contents = var1;
      }

      public ImmutableMultiset.Builder add(Object var1) {
         this.contents.add(Preconditions.checkNotNull(var1));
         return this;
      }

      public ImmutableMultiset.Builder addCopies(Object var1, int var2) {
         this.contents.add(Preconditions.checkNotNull(var1), var2);
         return this;
      }

      public ImmutableMultiset.Builder setCount(Object var1, int var2) {
         this.contents.setCount(Preconditions.checkNotNull(var1), var2);
         return this;
      }

      public ImmutableMultiset.Builder add(Object... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableMultiset.Builder addAll(Iterable var1) {
         if (var1 instanceof Multiset) {
            Multiset var2 = Multisets.cast(var1);
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Multiset.Entry var4 = (Multiset.Entry)var3.next();
               this.addCopies(var4.getElement(), var4.getCount());
            }
         } else {
            super.addAll(var1);
         }

         return this;
      }

      public ImmutableMultiset.Builder addAll(Iterator var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableMultiset build() {
         return ImmutableMultiset.copyOf((Iterable)this.contents);
      }

      public ImmutableCollection build() {
         return this.build();
      }

      public ImmutableCollection.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder add(Object[] var1) {
         return this.add(var1);
      }

      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }

   private static class SerializedForm implements Serializable {
      final Object[] elements;
      final int[] counts;
      private static final long serialVersionUID = 0L;

      SerializedForm(Multiset var1) {
         int var2 = var1.entrySet().size();
         this.elements = new Object[var2];
         this.counts = new int[var2];
         int var3 = 0;

         for(Iterator var4 = var1.entrySet().iterator(); var4.hasNext(); ++var3) {
            Multiset.Entry var5 = (Multiset.Entry)var4.next();
            this.elements[var3] = var5.getElement();
            this.counts[var3] = var5.getCount();
         }

      }

      Object readResolve() {
         LinkedHashMultiset var1 = LinkedHashMultiset.create(this.elements.length);

         for(int var2 = 0; var2 < this.elements.length; ++var2) {
            var1.add(this.elements[var2], this.counts[var2]);
         }

         return ImmutableMultiset.copyOf((Iterable)var1);
      }
   }

   static class EntrySetSerializedForm implements Serializable {
      final ImmutableMultiset multiset;

      EntrySetSerializedForm(ImmutableMultiset var1) {
         this.multiset = var1;
      }

      Object readResolve() {
         return this.multiset.entrySet();
      }
   }

   private final class EntrySet extends ImmutableSet {
      private static final long serialVersionUID = 0L;
      final ImmutableMultiset this$0;

      private EntrySet(ImmutableMultiset var1) {
         this.this$0 = var1;
      }

      boolean isPartialView() {
         return this.this$0.isPartialView();
      }

      public UnmodifiableIterator iterator() {
         return this.asList().iterator();
      }

      ImmutableList createAsList() {
         return new ImmutableAsList(this) {
            final ImmutableMultiset.EntrySet this$1;

            {
               this.this$1 = var1;
            }

            public Multiset.Entry get(int var1) {
               return this.this$1.this$0.getEntry(var1);
            }

            ImmutableCollection delegateCollection() {
               return this.this$1;
            }

            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public int size() {
         return this.this$0.elementSet().size();
      }

      public boolean contains(Object var1) {
         if (var1 instanceof Multiset.Entry) {
            Multiset.Entry var2 = (Multiset.Entry)var1;
            if (var2.getCount() <= 0) {
               return false;
            } else {
               int var3 = this.this$0.count(var2.getElement());
               return var3 == var2.getCount();
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.this$0.hashCode();
      }

      Object writeReplace() {
         return new ImmutableMultiset.EntrySetSerializedForm(this.this$0);
      }

      public Iterator iterator() {
         return this.iterator();
      }

      EntrySet(ImmutableMultiset var1, Object var2) {
         this(var1);
      }
   }
}
