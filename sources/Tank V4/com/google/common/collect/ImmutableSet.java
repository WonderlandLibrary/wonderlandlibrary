package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableSet extends ImmutableCollection implements Set {
   static final int MAX_TABLE_SIZE = 1073741824;
   private static final double DESIRED_LOAD_FACTOR = 0.7D;
   private static final int CUTOFF = 751619276;

   public static ImmutableSet of() {
      return EmptyImmutableSet.INSTANCE;
   }

   public static ImmutableSet of(Object var0) {
      return new SingletonImmutableSet(var0);
   }

   public static ImmutableSet of(Object var0, Object var1) {
      return construct(2, var0, var1);
   }

   public static ImmutableSet of(Object var0, Object var1, Object var2) {
      return construct(3, var0, var1, var2);
   }

   public static ImmutableSet of(Object var0, Object var1, Object var2, Object var3) {
      return construct(4, var0, var1, var2, var3);
   }

   public static ImmutableSet of(Object var0, Object var1, Object var2, Object var3, Object var4) {
      return construct(5, var0, var1, var2, var3, var4);
   }

   public static ImmutableSet of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object... var6) {
      boolean var7 = true;
      Object[] var8 = new Object[6 + var6.length];
      var8[0] = var0;
      var8[1] = var1;
      var8[2] = var2;
      var8[3] = var3;
      var8[4] = var4;
      var8[5] = var5;
      System.arraycopy(var6, 0, var8, 6, var6.length);
      return construct(var8.length, var8);
   }

   private static ImmutableSet construct(int param0, Object... param1) {
      // $FF: Couldn't be decompiled
   }

   public static ImmutableSet copyOf(Object[] var0) {
      switch(var0.length) {
      case 0:
         return of();
      case 1:
         return of(var0[0]);
      default:
         return construct(var0.length, (Object[])var0.clone());
      }
   }

   public static ImmutableSet copyOf(Iterable var0) {
      return var0 instanceof Collection ? copyOf(Collections2.cast(var0)) : copyOf(var0.iterator());
   }

   public static ImmutableSet copyOf(Iterator var0) {
      if (!var0.hasNext()) {
         return of();
      } else {
         Object var1 = var0.next();
         return !var0.hasNext() ? of(var1) : (new ImmutableSet.Builder()).add(var1).addAll(var0).build();
      }
   }

   public static ImmutableSet copyOf(Collection var0) {
      if (var0 instanceof ImmutableSet && !(var0 instanceof ImmutableSortedSet)) {
         ImmutableSet var1 = (ImmutableSet)var0;
         if (!var1.isPartialView()) {
            return var1;
         }
      } else if (var0 instanceof EnumSet) {
         return copyOfEnumSet((EnumSet)var0);
      }

      Object[] var2 = var0.toArray();
      return construct(var2.length, var2);
   }

   private static ImmutableSet copyOfEnumSet(EnumSet var0) {
      return ImmutableEnumSet.asImmutable(EnumSet.copyOf(var0));
   }

   ImmutableSet() {
   }

   boolean isHashCodeFast() {
      return false;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ImmutableSet && this.isHashCodeFast() && ((ImmutableSet)var1).isHashCodeFast() && this.hashCode() != var1.hashCode() ? false : Sets.equalsImpl(this, var1);
      }
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this);
   }

   public abstract UnmodifiableIterator iterator();

   Object writeReplace() {
      return new ImmutableSet.SerializedForm(this.toArray());
   }

   public static ImmutableSet.Builder builder() {
      return new ImmutableSet.Builder();
   }

   public Iterator iterator() {
      return this.iterator();
   }

   static ImmutableSet access$000(int var0, Object[] var1) {
      return construct(var0, var1);
   }

   public static class Builder extends ImmutableCollection.ArrayBasedBuilder {
      public Builder() {
         this(4);
      }

      Builder(int var1) {
         super(var1);
      }

      public ImmutableSet.Builder add(Object var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSet.Builder add(Object... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSet.Builder addAll(Iterable var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSet.Builder addAll(Iterator var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSet build() {
         ImmutableSet var1 = ImmutableSet.access$000(this.size, this.contents);
         this.size = var1.size();
         return var1;
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

   private static class SerializedForm implements Serializable {
      final Object[] elements;
      private static final long serialVersionUID = 0L;

      SerializedForm(Object[] var1) {
         this.elements = var1;
      }

      Object readResolve() {
         return ImmutableSet.copyOf(this.elements);
      }
   }
}
