package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableList extends ImmutableCollection implements List, RandomAccess {
   private static final ImmutableList EMPTY;

   public static ImmutableList of() {
      return EMPTY;
   }

   public static ImmutableList of(Object var0) {
      return new SingletonImmutableList(var0);
   }

   public static ImmutableList of(Object var0, Object var1) {
      return construct(var0, var1);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2) {
      return construct(var0, var1, var2);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3) {
      return construct(var0, var1, var2, var3);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4) {
      return construct(var0, var1, var2, var3, var4);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return construct(var0, var1, var2, var3, var4, var5);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6) {
      return construct(var0, var1, var2, var3, var4, var5, var6);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return construct(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      return construct(var0, var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return construct(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10) {
      return construct(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static ImmutableList of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object... var12) {
      Object[] var13 = new Object[12 + var12.length];
      var13[0] = var0;
      var13[1] = var1;
      var13[2] = var2;
      var13[3] = var3;
      var13[4] = var4;
      var13[5] = var5;
      var13[6] = var6;
      var13[7] = var7;
      var13[8] = var8;
      var13[9] = var9;
      var13[10] = var10;
      var13[11] = var11;
      System.arraycopy(var12, 0, var13, 12, var12.length);
      return construct(var13);
   }

   public static ImmutableList copyOf(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof Collection ? copyOf(Collections2.cast(var0)) : copyOf(var0.iterator());
   }

   public static ImmutableList copyOf(Collection var0) {
      if (var0 instanceof ImmutableCollection) {
         ImmutableList var1 = ((ImmutableCollection)var0).asList();
         return var1.isPartialView() ? asImmutableList(var1.toArray()) : var1;
      } else {
         return construct(var0.toArray());
      }
   }

   public static ImmutableList copyOf(Iterator var0) {
      if (!var0.hasNext()) {
         return of();
      } else {
         Object var1 = var0.next();
         return !var0.hasNext() ? of(var1) : (new ImmutableList.Builder()).add(var1).addAll(var0).build();
      }
   }

   public static ImmutableList copyOf(Object[] var0) {
      switch(var0.length) {
      case 0:
         return of();
      case 1:
         return new SingletonImmutableList(var0[0]);
      default:
         return new RegularImmutableList(ObjectArrays.checkElementsNotNull((Object[])var0.clone()));
      }
   }

   private static ImmutableList construct(Object... var0) {
      return asImmutableList(ObjectArrays.checkElementsNotNull(var0));
   }

   static ImmutableList asImmutableList(Object[] var0) {
      return asImmutableList(var0, var0.length);
   }

   static ImmutableList asImmutableList(Object[] var0, int var1) {
      switch(var1) {
      case 0:
         return of();
      case 1:
         SingletonImmutableList var2 = new SingletonImmutableList(var0[0]);
         return var2;
      default:
         if (var1 < var0.length) {
            var0 = ObjectArrays.arraysCopyOf(var0, var1);
         }

         return new RegularImmutableList(var0);
      }
   }

   ImmutableList() {
   }

   public UnmodifiableIterator iterator() {
      return this.listIterator();
   }

   public UnmodifiableListIterator listIterator() {
      return this.listIterator(0);
   }

   public UnmodifiableListIterator listIterator(int var1) {
      return new AbstractIndexedListIterator(this, this.size(), var1) {
         final ImmutableList this$0;

         {
            this.this$0 = var1;
         }

         protected Object get(int var1) {
            return this.this$0.get(var1);
         }
      };
   }

   public int indexOf(@Nullable Object var1) {
      return var1 == null ? -1 : Lists.indexOfImpl(this, var1);
   }

   public int lastIndexOf(@Nullable Object var1) {
      return var1 == null ? -1 : Lists.lastIndexOfImpl(this, var1);
   }

   public boolean contains(@Nullable Object var1) {
      return this.indexOf(var1) >= 0;
   }

   public ImmutableList subList(int var1, int var2) {
      Preconditions.checkPositionIndexes(var1, var2, this.size());
      int var3 = var2 - var1;
      switch(var3) {
      case 0:
         return of();
      case 1:
         return of(this.get(var1));
      default:
         return this.subListUnchecked(var1, var2);
      }
   }

   ImmutableList subListUnchecked(int var1, int var2) {
      return new ImmutableList.SubList(this, var1, var2 - var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Object set(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void add(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Object remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public final ImmutableList asList() {
      return this;
   }

   int copyIntoArray(Object[] var1, int var2) {
      int var3 = this.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         var1[var2 + var4] = this.get(var4);
      }

      return var2 + var3;
   }

   public ImmutableList reverse() {
      return new ImmutableList.ReverseImmutableList(this);
   }

   public boolean equals(@Nullable Object var1) {
      return Lists.equalsImpl(this, var1);
   }

   public int hashCode() {
      int var1 = 1;
      int var2 = this.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = 31 * var1 + this.get(var3).hashCode();
         var1 = ~(~var1);
      }

      return var1;
   }

   private void readObject(ObjectInputStream var1) throws InvalidObjectException {
      throw new InvalidObjectException("Use SerializedForm");
   }

   Object writeReplace() {
      return new ImmutableList.SerializedForm(this.toArray());
   }

   public static ImmutableList.Builder builder() {
      return new ImmutableList.Builder();
   }

   public Iterator iterator() {
      return this.iterator();
   }

   public List subList(int var1, int var2) {
      return this.subList(var1, var2);
   }

   public ListIterator listIterator(int var1) {
      return this.listIterator(var1);
   }

   public ListIterator listIterator() {
      return this.listIterator();
   }

   static {
      EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
   }

   public static final class Builder extends ImmutableCollection.ArrayBasedBuilder {
      public Builder() {
         this(4);
      }

      Builder(int var1) {
         super(var1);
      }

      public ImmutableList.Builder add(Object var1) {
         super.add(var1);
         return this;
      }

      public ImmutableList.Builder addAll(Iterable var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableList.Builder add(Object... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableList.Builder addAll(Iterator var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableList build() {
         return ImmutableList.asImmutableList(this.contents, this.size);
      }

      public ImmutableCollection.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder add(Object[] var1) {
         return this.add(var1);
      }

      public ImmutableCollection.ArrayBasedBuilder add(Object var1) {
         return this.add(var1);
      }

      public ImmutableCollection build() {
         return this.build();
      }

      public ImmutableCollection.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }

   static class SerializedForm implements Serializable {
      final Object[] elements;
      private static final long serialVersionUID = 0L;

      SerializedForm(Object[] var1) {
         this.elements = var1;
      }

      Object readResolve() {
         return ImmutableList.copyOf(this.elements);
      }
   }

   private static class ReverseImmutableList extends ImmutableList {
      private final transient ImmutableList forwardList;

      ReverseImmutableList(ImmutableList var1) {
         this.forwardList = var1;
      }

      private int reverseIndex(int var1) {
         return this.size() - 1 - var1;
      }

      private int reversePosition(int var1) {
         return this.size() - var1;
      }

      public ImmutableList reverse() {
         return this.forwardList;
      }

      public boolean contains(@Nullable Object var1) {
         return this.forwardList.contains(var1);
      }

      public int indexOf(@Nullable Object var1) {
         int var2 = this.forwardList.lastIndexOf(var1);
         return var2 >= 0 ? this.reverseIndex(var2) : -1;
      }

      public int lastIndexOf(@Nullable Object var1) {
         int var2 = this.forwardList.indexOf(var1);
         return var2 >= 0 ? this.reverseIndex(var2) : -1;
      }

      public ImmutableList subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.size());
         return this.forwardList.subList(this.reversePosition(var2), this.reversePosition(var1)).reverse();
      }

      public Object get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.forwardList.get(this.reverseIndex(var1));
      }

      public int size() {
         return this.forwardList.size();
      }

      boolean isPartialView() {
         return this.forwardList.isPartialView();
      }

      public List subList(int var1, int var2) {
         return this.subList(var1, var2);
      }

      public ListIterator listIterator(int var1) {
         return super.listIterator(var1);
      }

      public ListIterator listIterator() {
         return super.listIterator();
      }

      public Iterator iterator() {
         return super.iterator();
      }
   }

   class SubList extends ImmutableList {
      final transient int offset;
      final transient int length;
      final ImmutableList this$0;

      SubList(ImmutableList var1, int var2, int var3) {
         this.this$0 = var1;
         this.offset = var2;
         this.length = var3;
      }

      public int size() {
         return this.length;
      }

      public Object get(int var1) {
         Preconditions.checkElementIndex(var1, this.length);
         return this.this$0.get(var1 + this.offset);
      }

      public ImmutableList subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.length);
         return this.this$0.subList(var1 + this.offset, var2 + this.offset);
      }

      boolean isPartialView() {
         return true;
      }

      public List subList(int var1, int var2) {
         return this.subList(var1, var2);
      }

      public ListIterator listIterator(int var1) {
         return super.listIterator(var1);
      }

      public ListIterator listIterator() {
         return super.listIterator();
      }

      public Iterator iterator() {
         return super.iterator();
      }
   }
}
