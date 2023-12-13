package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public abstract class ImmutableMultimap extends AbstractMultimap implements Serializable {
   final transient ImmutableMap map;
   final transient int size;
   private static final long serialVersionUID = 0L;

   public static ImmutableMultimap of() {
      return ImmutableListMultimap.of();
   }

   public static ImmutableMultimap of(Object var0, Object var1) {
      return ImmutableListMultimap.of(var0, var1);
   }

   public static ImmutableMultimap of(Object var0, Object var1, Object var2, Object var3) {
      return ImmutableListMultimap.of(var0, var1, var2, var3);
   }

   public static ImmutableMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5);
   }

   public static ImmutableMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static ImmutableMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static ImmutableMultimap.Builder builder() {
      return new ImmutableMultimap.Builder();
   }

   public static ImmutableMultimap copyOf(Multimap var0) {
      if (var0 instanceof ImmutableMultimap) {
         ImmutableMultimap var1 = (ImmutableMultimap)var0;
         if (!var1.isPartialView()) {
            return var1;
         }
      }

      return ImmutableListMultimap.copyOf(var0);
   }

   ImmutableMultimap(ImmutableMap var1, int var2) {
      this.map = var1;
      this.size = var2;
   }

   /** @deprecated */
   @Deprecated
   public ImmutableCollection removeAll(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public ImmutableCollection replaceValues(Object var1, Iterable var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public abstract ImmutableCollection get(Object var1);

   public abstract ImmutableMultimap inverse();

   /** @deprecated */
   @Deprecated
   public boolean put(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean putAll(Object var1, Iterable var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean putAll(Multimap var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return var1 != null && super.containsValue(var1);
   }

   public int size() {
      return this.size;
   }

   public ImmutableSet keySet() {
      return this.map.keySet();
   }

   public ImmutableMap asMap() {
      return this.map;
   }

   Map createAsMap() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection entries() {
      return (ImmutableCollection)super.entries();
   }

   ImmutableCollection createEntries() {
      return new ImmutableMultimap.EntryCollection(this);
   }

   UnmodifiableIterator entryIterator() {
      return new ImmutableMultimap.Itr(this) {
         final ImmutableMultimap this$0;

         {
            this.this$0 = var1;
         }

         Entry output(Object var1, Object var2) {
            return Maps.immutableEntry(var1, var2);
         }

         Object output(Object var1, Object var2) {
            return this.output(var1, var2);
         }
      };
   }

   public ImmutableMultiset keys() {
      return (ImmutableMultiset)super.keys();
   }

   ImmutableMultiset createKeys() {
      return new ImmutableMultimap.Keys(this);
   }

   public ImmutableCollection values() {
      return (ImmutableCollection)super.values();
   }

   ImmutableCollection createValues() {
      return new ImmutableMultimap.Values(this);
   }

   UnmodifiableIterator valueIterator() {
      return new ImmutableMultimap.Itr(this) {
         final ImmutableMultimap this$0;

         {
            this.this$0 = var1;
         }

         Object output(Object var1, Object var2) {
            return var2;
         }
      };
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public Map asMap() {
      return this.asMap();
   }

   Iterator valueIterator() {
      return this.valueIterator();
   }

   Collection createValues() {
      return this.createValues();
   }

   public Collection values() {
      return this.values();
   }

   Multiset createKeys() {
      return this.createKeys();
   }

   public Multiset keys() {
      return this.keys();
   }

   public Set keySet() {
      return this.keySet();
   }

   Iterator entryIterator() {
      return this.entryIterator();
   }

   Collection createEntries() {
      return this.createEntries();
   }

   public Collection entries() {
      return this.entries();
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   private static final class Values extends ImmutableCollection {
      private final transient ImmutableMultimap multimap;
      private static final long serialVersionUID = 0L;

      Values(ImmutableMultimap var1) {
         this.multimap = var1;
      }

      public boolean contains(@Nullable Object var1) {
         return this.multimap.containsValue(var1);
      }

      public UnmodifiableIterator iterator() {
         return this.multimap.valueIterator();
      }

      @GwtIncompatible("not present in emulated superclass")
      int copyIntoArray(Object[] var1, int var2) {
         ImmutableCollection var4;
         for(Iterator var3 = this.multimap.map.values().iterator(); var3.hasNext(); var2 = var4.copyIntoArray(var1, var2)) {
            var4 = (ImmutableCollection)var3.next();
         }

         return var2;
      }

      public int size() {
         return this.multimap.size();
      }

      boolean isPartialView() {
         return true;
      }

      public Iterator iterator() {
         return this.iterator();
      }
   }

   class Keys extends ImmutableMultiset {
      final ImmutableMultimap this$0;

      Keys(ImmutableMultimap var1) {
         this.this$0 = var1;
      }

      public boolean contains(@Nullable Object var1) {
         return this.this$0.containsKey(var1);
      }

      public int count(@Nullable Object var1) {
         Collection var2 = (Collection)this.this$0.map.get(var1);
         return var2 == null ? 0 : var2.size();
      }

      public Set elementSet() {
         return this.this$0.keySet();
      }

      public int size() {
         return this.this$0.size();
      }

      Multiset.Entry getEntry(int var1) {
         Entry var2 = (Entry)this.this$0.map.entrySet().asList().get(var1);
         return Multisets.immutableEntry(var2.getKey(), ((Collection)var2.getValue()).size());
      }

      boolean isPartialView() {
         return true;
      }
   }

   private abstract class Itr extends UnmodifiableIterator {
      final Iterator mapIterator;
      Object key;
      Iterator valueIterator;
      final ImmutableMultimap this$0;

      private Itr(ImmutableMultimap var1) {
         this.this$0 = var1;
         this.mapIterator = this.this$0.asMap().entrySet().iterator();
         this.key = null;
         this.valueIterator = Iterators.emptyIterator();
      }

      abstract Object output(Object var1, Object var2);

      public boolean hasNext() {
         return this.mapIterator.hasNext() || this.valueIterator.hasNext();
      }

      public Object next() {
         if (!this.valueIterator.hasNext()) {
            Entry var1 = (Entry)this.mapIterator.next();
            this.key = var1.getKey();
            this.valueIterator = ((Collection)var1.getValue()).iterator();
         }

         return this.output(this.key, this.valueIterator.next());
      }

      Itr(ImmutableMultimap var1, Object var2) {
         this(var1);
      }
   }

   private static class EntryCollection extends ImmutableCollection {
      final ImmutableMultimap multimap;
      private static final long serialVersionUID = 0L;

      EntryCollection(ImmutableMultimap var1) {
         this.multimap = var1;
      }

      public UnmodifiableIterator iterator() {
         return this.multimap.entryIterator();
      }

      boolean isPartialView() {
         return this.multimap.isPartialView();
      }

      public int size() {
         return this.multimap.size();
      }

      public boolean contains(Object var1) {
         if (var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            return this.multimap.containsEntry(var2.getKey(), var2.getValue());
         } else {
            return false;
         }
      }

      public Iterator iterator() {
         return this.iterator();
      }
   }

   @GwtIncompatible("java serialization is not supported")
   static class FieldSettersHolder {
      static final Serialization.FieldSetter MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
      static final Serialization.FieldSetter SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
      static final Serialization.FieldSetter EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
   }

   public static class Builder {
      Multimap builderMultimap = new ImmutableMultimap.BuilderMultimap();
      Comparator keyComparator;
      Comparator valueComparator;

      public ImmutableMultimap.Builder put(Object var1, Object var2) {
         CollectPreconditions.checkEntryNotNull(var1, var2);
         this.builderMultimap.put(var1, var2);
         return this;
      }

      public ImmutableMultimap.Builder put(Entry var1) {
         return this.put(var1.getKey(), var1.getValue());
      }

      public ImmutableMultimap.Builder putAll(Object var1, Iterable var2) {
         if (var1 == null) {
            throw new NullPointerException("null key in entry: null=" + Iterables.toString(var2));
         } else {
            Collection var3 = this.builderMultimap.get(var1);
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               CollectPreconditions.checkEntryNotNull(var1, var5);
               var3.add(var5);
            }

            return this;
         }
      }

      public ImmutableMultimap.Builder putAll(Object var1, Object... var2) {
         return this.putAll(var1, (Iterable)Arrays.asList(var2));
      }

      public ImmutableMultimap.Builder putAll(Multimap var1) {
         Iterator var2 = var1.asMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.putAll(var3.getKey(), (Iterable)var3.getValue());
         }

         return this;
      }

      public ImmutableMultimap.Builder orderKeysBy(Comparator var1) {
         this.keyComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableMultimap.Builder orderValuesBy(Comparator var1) {
         this.valueComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableMultimap build() {
         if (this.valueComparator != null) {
            Iterator var1 = this.builderMultimap.asMap().values().iterator();

            while(var1.hasNext()) {
               Collection var2 = (Collection)var1.next();
               List var3 = (List)var2;
               Collections.sort(var3, this.valueComparator);
            }
         }

         if (this.keyComparator != null) {
            ImmutableMultimap.BuilderMultimap var5 = new ImmutableMultimap.BuilderMultimap();
            ArrayList var6 = Lists.newArrayList((Iterable)this.builderMultimap.asMap().entrySet());
            Collections.sort(var6, Ordering.from(this.keyComparator).onKeys());
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Entry var4 = (Entry)var7.next();
               var5.putAll(var4.getKey(), (Iterable)var4.getValue());
            }

            this.builderMultimap = var5;
         }

         return ImmutableMultimap.copyOf(this.builderMultimap);
      }
   }

   private static class BuilderMultimap extends AbstractMapBasedMultimap {
      private static final long serialVersionUID = 0L;

      BuilderMultimap() {
         super(new LinkedHashMap());
      }

      Collection createCollection() {
         return Lists.newArrayList();
      }
   }
}
