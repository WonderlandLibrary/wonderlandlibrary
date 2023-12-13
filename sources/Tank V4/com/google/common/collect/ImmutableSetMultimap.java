package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class ImmutableSetMultimap extends ImmutableMultimap implements SetMultimap {
   private final transient ImmutableSet emptySet;
   private transient ImmutableSetMultimap inverse;
   private transient ImmutableSet entries;
   @GwtIncompatible("not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static ImmutableSetMultimap of() {
      return EmptyImmutableSetMultimap.INSTANCE;
   }

   public static ImmutableSetMultimap of(Object var0, Object var1) {
      ImmutableSetMultimap.Builder var2 = builder();
      var2.put(var0, var1);
      return var2.build();
   }

   public static ImmutableSetMultimap of(Object var0, Object var1, Object var2, Object var3) {
      ImmutableSetMultimap.Builder var4 = builder();
      var4.put(var0, var1);
      var4.put(var2, var3);
      return var4.build();
   }

   public static ImmutableSetMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      ImmutableSetMultimap.Builder var6 = builder();
      var6.put(var0, var1);
      var6.put(var2, var3);
      var6.put(var4, var5);
      return var6.build();
   }

   public static ImmutableSetMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      ImmutableSetMultimap.Builder var8 = builder();
      var8.put(var0, var1);
      var8.put(var2, var3);
      var8.put(var4, var5);
      var8.put(var6, var7);
      return var8.build();
   }

   public static ImmutableSetMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      ImmutableSetMultimap.Builder var10 = builder();
      var10.put(var0, var1);
      var10.put(var2, var3);
      var10.put(var4, var5);
      var10.put(var6, var7);
      var10.put(var8, var9);
      return var10.build();
   }

   public static ImmutableSetMultimap.Builder builder() {
      return new ImmutableSetMultimap.Builder();
   }

   public static ImmutableSetMultimap copyOf(Multimap var0) {
      return copyOf(var0, (Comparator)null);
   }

   private static ImmutableSetMultimap copyOf(Multimap var0, Comparator var1) {
      Preconditions.checkNotNull(var0);
      if (var0.isEmpty() && var1 == null) {
         return of();
      } else {
         if (var0 instanceof ImmutableSetMultimap) {
            ImmutableSetMultimap var2 = (ImmutableSetMultimap)var0;
            if (!var2.isPartialView()) {
               return var2;
            }
         }

         ImmutableMap.Builder var9 = ImmutableMap.builder();
         int var3 = 0;
         Iterator var4 = var0.asMap().entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            Object var6 = var5.getKey();
            Collection var7 = (Collection)var5.getValue();
            ImmutableSet var8 = valueSet(var1, var7);
            if (!var8.isEmpty()) {
               var9.put(var6, var8);
               var3 += var8.size();
            }
         }

         return new ImmutableSetMultimap(var9.build(), var3, var1);
      }
   }

   ImmutableSetMultimap(ImmutableMap var1, int var2, @Nullable Comparator var3) {
      super(var1, var2);
      this.emptySet = emptySet(var3);
   }

   public ImmutableSet get(@Nullable Object var1) {
      ImmutableSet var2 = (ImmutableSet)this.map.get(var1);
      return (ImmutableSet)Objects.firstNonNull(var2, this.emptySet);
   }

   public ImmutableSetMultimap inverse() {
      ImmutableSetMultimap var1 = this.inverse;
      return var1 == null ? (this.inverse = this.invert()) : var1;
   }

   private ImmutableSetMultimap invert() {
      ImmutableSetMultimap.Builder var1 = builder();
      Iterator var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getValue(), var3.getKey());
      }

      ImmutableSetMultimap var4 = var1.build();
      var4.inverse = this;
      return var4;
   }

   /** @deprecated */
   @Deprecated
   public ImmutableSet removeAll(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public ImmutableSet replaceValues(Object var1, Iterable var2) {
      throw new UnsupportedOperationException();
   }

   public ImmutableSet entries() {
      ImmutableSet var1 = this.entries;
      return var1 == null ? (this.entries = new ImmutableSetMultimap.EntrySet(this)) : var1;
   }

   private static ImmutableSet valueSet(@Nullable Comparator var0, Collection var1) {
      return (ImmutableSet)(var0 == null ? ImmutableSet.copyOf(var1) : ImmutableSortedSet.copyOf(var0, var1));
   }

   private static ImmutableSet emptySet(@Nullable Comparator var0) {
      return (ImmutableSet)(var0 == null ? ImmutableSet.of() : ImmutableSortedSet.emptySet(var0));
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.valueComparator());
      Serialization.writeMultimap(this, var1);
   }

   @Nullable
   Comparator valueComparator() {
      return this.emptySet instanceof ImmutableSortedSet ? ((ImmutableSortedSet)this.emptySet).comparator() : null;
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Comparator var2 = (Comparator)var1.readObject();
      int var3 = var1.readInt();
      if (var3 < 0) {
         throw new InvalidObjectException("Invalid key count " + var3);
      } else {
         ImmutableMap.Builder var4 = ImmutableMap.builder();
         int var5 = 0;

         for(int var6 = 0; var6 < var3; ++var6) {
            Object var7 = var1.readObject();
            int var8 = var1.readInt();
            if (var8 <= 0) {
               throw new InvalidObjectException("Invalid value count " + var8);
            }

            Object[] var9 = new Object[var8];

            for(int var10 = 0; var10 < var8; ++var10) {
               var9[var10] = var1.readObject();
            }

            ImmutableSet var12 = valueSet(var2, Arrays.asList(var9));
            if (var12.size() != var9.length) {
               throw new InvalidObjectException("Duplicate key-value pairs exist for key " + var7);
            }

            var4.put(var7, var12);
            var5 += var8;
         }

         ImmutableMap var13;
         try {
            var13 = var4.build();
         } catch (IllegalArgumentException var11) {
            throw (InvalidObjectException)(new InvalidObjectException(var11.getMessage())).initCause(var11);
         }

         ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, var13);
         ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, var5);
         ImmutableMultimap.FieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(var2));
      }
   }

   public ImmutableCollection entries() {
      return this.entries();
   }

   public ImmutableMultimap inverse() {
      return this.inverse();
   }

   public ImmutableCollection get(Object var1) {
      return this.get(var1);
   }

   public ImmutableCollection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public ImmutableCollection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Collection entries() {
      return this.entries();
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Set entries() {
      return this.entries();
   }

   public Set replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public Set removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Set get(Object var1) {
      return this.get(var1);
   }

   static ImmutableSetMultimap access$000(Multimap var0, Comparator var1) {
      return copyOf(var0, var1);
   }

   private static final class EntrySet extends ImmutableSet {
      private final transient ImmutableSetMultimap multimap;

      EntrySet(ImmutableSetMultimap var1) {
         this.multimap = var1;
      }

      public boolean contains(@Nullable Object var1) {
         if (var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            return this.multimap.containsEntry(var2.getKey(), var2.getValue());
         } else {
            return false;
         }
      }

      public int size() {
         return this.multimap.size();
      }

      public UnmodifiableIterator iterator() {
         return this.multimap.entryIterator();
      }

      boolean isPartialView() {
         return false;
      }

      public Iterator iterator() {
         return this.iterator();
      }
   }

   public static final class Builder extends ImmutableMultimap.Builder {
      public Builder() {
         this.builderMultimap = new ImmutableSetMultimap.BuilderMultimap();
      }

      public ImmutableSetMultimap.Builder put(Object var1, Object var2) {
         this.builderMultimap.put(Preconditions.checkNotNull(var1), Preconditions.checkNotNull(var2));
         return this;
      }

      public ImmutableSetMultimap.Builder put(Entry var1) {
         this.builderMultimap.put(Preconditions.checkNotNull(var1.getKey()), Preconditions.checkNotNull(var1.getValue()));
         return this;
      }

      public ImmutableSetMultimap.Builder putAll(Object var1, Iterable var2) {
         Collection var3 = this.builderMultimap.get(Preconditions.checkNotNull(var1));
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var3.add(Preconditions.checkNotNull(var5));
         }

         return this;
      }

      public ImmutableSetMultimap.Builder putAll(Object var1, Object... var2) {
         return this.putAll(var1, (Iterable)Arrays.asList(var2));
      }

      public ImmutableSetMultimap.Builder putAll(Multimap var1) {
         Iterator var2 = var1.asMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.putAll(var3.getKey(), (Iterable)var3.getValue());
         }

         return this;
      }

      public ImmutableSetMultimap.Builder orderKeysBy(Comparator var1) {
         this.keyComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableSetMultimap.Builder orderValuesBy(Comparator var1) {
         super.orderValuesBy(var1);
         return this;
      }

      public ImmutableSetMultimap build() {
         if (this.keyComparator != null) {
            ImmutableSetMultimap.BuilderMultimap var1 = new ImmutableSetMultimap.BuilderMultimap();
            ArrayList var2 = Lists.newArrayList((Iterable)this.builderMultimap.asMap().entrySet());
            Collections.sort(var2, Ordering.from(this.keyComparator).onKeys());
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               var1.putAll(var4.getKey(), (Iterable)var4.getValue());
            }

            this.builderMultimap = var1;
         }

         return ImmutableSetMultimap.access$000(this.builderMultimap, this.valueComparator);
      }

      public ImmutableMultimap build() {
         return this.build();
      }

      public ImmutableMultimap.Builder orderValuesBy(Comparator var1) {
         return this.orderValuesBy(var1);
      }

      public ImmutableMultimap.Builder orderKeysBy(Comparator var1) {
         return this.orderKeysBy(var1);
      }

      public ImmutableMultimap.Builder putAll(Multimap var1) {
         return this.putAll(var1);
      }

      public ImmutableMultimap.Builder putAll(Object var1, Object[] var2) {
         return this.putAll(var1, var2);
      }

      public ImmutableMultimap.Builder putAll(Object var1, Iterable var2) {
         return this.putAll(var1, var2);
      }

      public ImmutableMultimap.Builder put(Entry var1) {
         return this.put(var1);
      }

      public ImmutableMultimap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }

   private static class BuilderMultimap extends AbstractMapBasedMultimap {
      private static final long serialVersionUID = 0L;

      BuilderMultimap() {
         super(new LinkedHashMap());
      }

      Collection createCollection() {
         return Sets.newLinkedHashSet();
      }
   }
}
