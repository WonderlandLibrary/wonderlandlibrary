package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public abstract class ImmutableCollection extends AbstractCollection implements Serializable {
   private transient ImmutableList asList;

   ImmutableCollection() {
   }

   public abstract UnmodifiableIterator iterator();

   public final Object[] toArray() {
      int var1 = this.size();
      if (var1 == 0) {
         return ObjectArrays.EMPTY_ARRAY;
      } else {
         Object[] var2 = new Object[this.size()];
         this.copyIntoArray(var2, 0);
         return var2;
      }
   }

   public final Object[] toArray(Object[] var1) {
      Preconditions.checkNotNull(var1);
      int var2 = this.size();
      if (var1.length < var2) {
         var1 = ObjectArrays.newArray(var1, var2);
      } else if (var1.length > var2) {
         var1[var2] = null;
      }

      this.copyIntoArray(var1, 0);
      return var1;
   }

   public boolean contains(@Nullable Object var1) {
      return var1 != null && super.contains(var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean add(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public ImmutableList asList() {
      ImmutableList var1 = this.asList;
      return var1 == null ? (this.asList = this.createAsList()) : var1;
   }

   ImmutableList createAsList() {
      switch(this.size()) {
      case 0:
         return ImmutableList.of();
      case 1:
         return ImmutableList.of(this.iterator().next());
      default:
         return new RegularImmutableAsList(this, this.toArray());
      }
   }

   abstract boolean isPartialView();

   int copyIntoArray(Object[] var1, int var2) {
      Object var4;
      for(Iterator var3 = this.iterator(); var3.hasNext(); var1[var2++] = var4) {
         var4 = var3.next();
      }

      return var2;
   }

   Object writeReplace() {
      return new ImmutableList.SerializedForm(this.toArray());
   }

   public Iterator iterator() {
      return this.iterator();
   }

   abstract static class ArrayBasedBuilder extends ImmutableCollection.Builder {
      Object[] contents;
      int size;

      ArrayBasedBuilder(int var1) {
         CollectPreconditions.checkNonnegative(var1, "initialCapacity");
         this.contents = new Object[var1];
         this.size = 0;
      }

      private void ensureCapacity(int var1) {
         if (this.contents.length < var1) {
            this.contents = ObjectArrays.arraysCopyOf(this.contents, expandedCapacity(this.contents.length, var1));
         }

      }

      public ImmutableCollection.ArrayBasedBuilder add(Object var1) {
         Preconditions.checkNotNull(var1);
         this.ensureCapacity(this.size + 1);
         this.contents[this.size++] = var1;
         return this;
      }

      public ImmutableCollection.Builder add(Object... var1) {
         ObjectArrays.checkElementsNotNull(var1);
         this.ensureCapacity(this.size + var1.length);
         System.arraycopy(var1, 0, this.contents, this.size, var1.length);
         this.size += var1.length;
         return this;
      }

      public ImmutableCollection.Builder addAll(Iterable var1) {
         if (var1 instanceof Collection) {
            Collection var2 = (Collection)var1;
            this.ensureCapacity(this.size + var2.size());
         }

         super.addAll(var1);
         return this;
      }

      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }

   public abstract static class Builder {
      static final int DEFAULT_INITIAL_CAPACITY = 4;

      static int expandedCapacity(int var0, int var1) {
         if (var1 < 0) {
            throw new AssertionError("cannot store more than MAX_VALUE elements");
         } else {
            int var2 = var0 + (var0 >> 1) + 1;
            if (var2 < var1) {
               var2 = Integer.highestOneBit(var1 - 1) << 1;
            }

            if (var2 < 0) {
               var2 = Integer.MAX_VALUE;
            }

            return var2;
         }
      }

      Builder() {
      }

      public abstract ImmutableCollection.Builder add(Object var1);

      public ImmutableCollection.Builder add(Object... var1) {
         Object[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            this.add(var5);
         }

         return this;
      }

      public ImmutableCollection.Builder addAll(Iterable var1) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            this.add(var3);
         }

         return this;
      }

      public ImmutableCollection.Builder addAll(Iterator var1) {
         while(var1.hasNext()) {
            this.add(var1.next());
         }

         return this;
      }

      public abstract ImmutableCollection build();
   }
}
