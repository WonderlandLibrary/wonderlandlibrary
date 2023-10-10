package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible(
   emulated = true
)
final class Platform {
   static Object[] newArray(Object[] var0, int var1) {
      Class var2 = var0.getClass().getComponentType();
      Object[] var3 = (Object[])((Object[])Array.newInstance(var2, var1));
      return var3;
   }

   static Set newSetFromMap(Map var0) {
      return Collections.newSetFromMap(var0);
   }

   static MapMaker tryWeakKeys(MapMaker var0) {
      return var0.weakKeys();
   }

   static SortedMap mapsTransformEntriesSortedMap(SortedMap var0, Maps.EntryTransformer var1) {
      return (SortedMap)(var0 instanceof NavigableMap ? Maps.transformEntries((NavigableMap)var0, var1) : Maps.transformEntriesIgnoreNavigable(var0, var1));
   }

   static SortedMap mapsAsMapSortedSet(SortedSet var0, Function var1) {
      return (SortedMap)(var0 instanceof NavigableSet ? Maps.asMap((NavigableSet)var0, var1) : Maps.asMapSortedIgnoreNavigable(var0, var1));
   }

   static SortedSet setsFilterSortedSet(SortedSet var0, Predicate var1) {
      return (SortedSet)(var0 instanceof NavigableSet ? Sets.filter((NavigableSet)var0, var1) : Sets.filterSortedIgnoreNavigable(var0, var1));
   }

   static SortedMap mapsFilterSortedMap(SortedMap var0, Predicate var1) {
      return (SortedMap)(var0 instanceof NavigableMap ? Maps.filterEntries((NavigableMap)var0, var1) : Maps.filterSortedIgnoreNavigable(var0, var1));
   }

   private Platform() {
   }
}
