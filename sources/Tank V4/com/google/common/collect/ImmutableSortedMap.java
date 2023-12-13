package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableSortedMap extends ImmutableSortedMapFauxverideShim implements NavigableMap {
   private static final Comparator NATURAL_ORDER = Ordering.natural();
   private static final ImmutableSortedMap NATURAL_EMPTY_MAP;
   private transient ImmutableSortedMap descendingMap;
   private static final long serialVersionUID = 0L;

   static ImmutableSortedMap emptyMap(Comparator var0) {
      return (ImmutableSortedMap)(Ordering.natural().equals(var0) ? of() : new EmptyImmutableSortedMap(var0));
   }

   static ImmutableSortedMap fromSortedEntries(Comparator var0, int var1, Entry[] var2) {
      if (var1 == 0) {
         return emptyMap(var0);
      } else {
         ImmutableList.Builder var3 = ImmutableList.builder();
         ImmutableList.Builder var4 = ImmutableList.builder();

         for(int var5 = 0; var5 < var1; ++var5) {
            Entry var6 = var2[var5];
            var3.add(var6.getKey());
            var4.add(var6.getValue());
         }

         return new RegularImmutableSortedMap(new RegularImmutableSortedSet(var3.build(), var0), var4.build());
      }
   }

   static ImmutableSortedMap from(ImmutableSortedSet var0, ImmutableList var1) {
      return (ImmutableSortedMap)(var0.isEmpty() ? emptyMap(var0.comparator()) : new RegularImmutableSortedMap((RegularImmutableSortedSet)var0, var1));
   }

   public static ImmutableSortedMap of() {
      return NATURAL_EMPTY_MAP;
   }

   public static ImmutableSortedMap of(Comparable var0, Object var1) {
      return from(ImmutableSortedSet.of(var0), ImmutableList.of(var1));
   }

   public static ImmutableSortedMap of(Comparable var0, Object var1, Comparable var2, Object var3) {
      return fromEntries(Ordering.natural(), false, 2, entryOf(var0, var1), entryOf(var2, var3));
   }

   public static ImmutableSortedMap of(Comparable var0, Object var1, Comparable var2, Object var3, Comparable var4, Object var5) {
      return fromEntries(Ordering.natural(), false, 3, entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5));
   }

   public static ImmutableSortedMap of(Comparable var0, Object var1, Comparable var2, Object var3, Comparable var4, Object var5, Comparable var6, Object var7) {
      return fromEntries(Ordering.natural(), false, 4, entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7));
   }

   public static ImmutableSortedMap of(Comparable var0, Object var1, Comparable var2, Object var3, Comparable var4, Object var5, Comparable var6, Object var7, Comparable var8, Object var9) {
      return fromEntries(Ordering.natural(), false, 5, entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9));
   }

   public static ImmutableSortedMap copyOf(Map var0) {
      Ordering var1 = Ordering.natural();
      return copyOfInternal(var0, var1);
   }

   public static ImmutableSortedMap copyOf(Map var0, Comparator var1) {
      return copyOfInternal(var0, (Comparator)Preconditions.checkNotNull(var1));
   }

   public static ImmutableSortedMap copyOfSorted(SortedMap var0) {
      Comparator var1 = var0.comparator();
      if (var1 == null) {
         var1 = NATURAL_ORDER;
      }

      return copyOfInternal(var0, var1);
   }

   private static ImmutableSortedMap copyOfInternal(Map var0, Comparator var1) {
      boolean var2 = false;
      if (var0 instanceof SortedMap) {
         SortedMap var3 = (SortedMap)var0;
         Comparator var4 = var3.comparator();
         var2 = var4 == null ? var1 == NATURAL_ORDER : var1.equals(var4);
      }

      if (var2 && var0 instanceof ImmutableSortedMap) {
         ImmutableSortedMap var5 = (ImmutableSortedMap)var0;
         if (var5 == false) {
            return var5;
         }
      }

      Entry[] var6 = (Entry[])var0.entrySet().toArray(new Entry[0]);
      return fromEntries(var1, var2, var6.length, var6);
   }

   static ImmutableSortedMap fromEntries(Comparator var0, boolean var1, int var2, Entry... var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         Entry var5 = var3[var4];
         var3[var4] = entryOf(var5.getKey(), var5.getValue());
      }

      if (!var1) {
         sortEntries(var0, var2, var3);
         validateEntries(var2, var3, var0);
      }

      return fromSortedEntries(var0, var2, var3);
   }

   private static void sortEntries(Comparator var0, int var1, Entry[] var2) {
      Arrays.sort(var2, 0, var1, Ordering.from(var0).onKeys());
   }

   private static void validateEntries(int var0, Entry[] var1, Comparator var2) {
      for(int var3 = 1; var3 < var0; ++var3) {
         checkNoConflict(var2.compare(var1[var3 - 1].getKey(), var1[var3].getKey()) != 0, "key", var1[var3 - 1], var1[var3]);
      }

   }

   public static ImmutableSortedMap.Builder naturalOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural());
   }

   public static ImmutableSortedMap.Builder orderedBy(Comparator var0) {
      return new ImmutableSortedMap.Builder(var0);
   }

   public static ImmutableSortedMap.Builder reverseOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural().reverse());
   }

   ImmutableSortedMap() {
   }

   ImmutableSortedMap(ImmutableSortedMap var1) {
      this.descendingMap = var1;
   }

   public int size() {
      return this.values().size();
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   public ImmutableSet entrySet() {
      return super.entrySet();
   }

   public abstract ImmutableSortedSet keySet();

   public abstract ImmutableCollection values();

   public Comparator comparator() {
      return this.keySet().comparator();
   }

   public Object firstKey() {
      return this.keySet().first();
   }

   public Object lastKey() {
      return this.keySet().last();
   }

   public ImmutableSortedMap headMap(Object var1) {
      return this.headMap(var1, false);
   }

   public abstract ImmutableSortedMap headMap(Object var1, boolean var2);

   public ImmutableSortedMap subMap(Object var1, Object var2) {
      return this.subMap(var1, true, var2, false);
   }

   public ImmutableSortedMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(this.comparator().compare(var1, var3) <= 0, "expected fromKey <= toKey but %s > %s", var1, var3);
      return this.headMap(var3, var4).tailMap(var1, var2);
   }

   public ImmutableSortedMap tailMap(Object var1) {
      return this.tailMap(var1, true);
   }

   public abstract ImmutableSortedMap tailMap(Object var1, boolean var2);

   public Entry lowerEntry(Object var1) {
      return this.headMap(var1, false).lastEntry();
   }

   public Object lowerKey(Object var1) {
      return Maps.keyOrNull(this.lowerEntry(var1));
   }

   public Entry floorEntry(Object var1) {
      return this.headMap(var1, true).lastEntry();
   }

   public Object floorKey(Object var1) {
      return Maps.keyOrNull(this.floorEntry(var1));
   }

   public Entry ceilingEntry(Object var1) {
      return this.tailMap(var1, true).firstEntry();
   }

   public Object ceilingKey(Object var1) {
      return Maps.keyOrNull(this.ceilingEntry(var1));
   }

   public Entry higherEntry(Object var1) {
      return this.tailMap(var1, false).firstEntry();
   }

   public Object higherKey(Object var1) {
      return Maps.keyOrNull(this.higherEntry(var1));
   }

   public Entry firstEntry() {
      return this.isEmpty() ? null : (Entry)this.entrySet().asList().get(0);
   }

   public Entry lastEntry() {
      return this.isEmpty() ? null : (Entry)this.entrySet().asList().get(this.size() - 1);
   }

   /** @deprecated */
   @Deprecated
   public final Entry pollFirstEntry() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Entry pollLastEntry() {
      throw new UnsupportedOperationException();
   }

   public ImmutableSortedMap descendingMap() {
      ImmutableSortedMap var1 = this.descendingMap;
      if (var1 == null) {
         var1 = this.descendingMap = this.createDescendingMap();
      }

      return var1;
   }

   abstract ImmutableSortedMap createDescendingMap();

   public ImmutableSortedSet navigableKeySet() {
      return this.keySet();
   }

   public ImmutableSortedSet descendingKeySet() {
      return this.keySet().descendingSet();
   }

   Object writeReplace() {
      return new ImmutableSortedMap.SerializedForm(this);
   }

   public ImmutableSet keySet() {
      return this.keySet();
   }

   public Set entrySet() {
      return this.entrySet();
   }

   public Collection values() {
      return this.values();
   }

   public Set keySet() {
      return this.keySet();
   }

   public SortedMap tailMap(Object var1) {
      return this.tailMap(var1);
   }

   public SortedMap headMap(Object var1) {
      return this.headMap(var1);
   }

   public SortedMap subMap(Object var1, Object var2) {
      return this.subMap(var1, var2);
   }

   public NavigableMap tailMap(Object var1, boolean var2) {
      return this.tailMap(var1, var2);
   }

   public NavigableMap headMap(Object var1, boolean var2) {
      return this.headMap(var1, var2);
   }

   public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subMap(var1, var2, var3, var4);
   }

   public NavigableSet descendingKeySet() {
      return this.descendingKeySet();
   }

   public NavigableSet navigableKeySet() {
      return this.navigableKeySet();
   }

   public NavigableMap descendingMap() {
      return this.descendingMap();
   }

   static {
      NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
   }

   private static class SerializedForm extends ImmutableMap.SerializedForm {
      private final Comparator comparator;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableSortedMap var1) {
         super(var1);
         this.comparator = var1.comparator();
      }

      Object readResolve() {
         ImmutableSortedMap.Builder var1 = new ImmutableSortedMap.Builder(this.comparator);
         return this.createMap(var1);
      }
   }

   public static class Builder extends ImmutableMap.Builder {
      private final Comparator comparator;

      public Builder(Comparator var1) {
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public ImmutableSortedMap.Builder put(Object var1, Object var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableSortedMap.Builder put(Entry var1) {
         super.put(var1);
         return this;
      }

      public ImmutableSortedMap.Builder putAll(Map var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableSortedMap build() {
         return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, this.entries);
      }

      public ImmutableMap build() {
         return this.build();
      }

      public ImmutableMap.Builder putAll(Map var1) {
         return this.putAll(var1);
      }

      public ImmutableMap.Builder put(Entry var1) {
         return this.put(var1);
      }

      public ImmutableMap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
