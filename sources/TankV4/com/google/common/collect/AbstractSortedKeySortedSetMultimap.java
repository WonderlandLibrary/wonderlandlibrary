package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
abstract class AbstractSortedKeySortedSetMultimap extends AbstractSortedSetMultimap {
   AbstractSortedKeySortedSetMultimap(SortedMap var1) {
      super(var1);
   }

   public SortedMap asMap() {
      return (SortedMap)super.asMap();
   }

   SortedMap backingMap() {
      return (SortedMap)super.backingMap();
   }

   public SortedSet keySet() {
      return (SortedSet)super.keySet();
   }

   public Map asMap() {
      return this.asMap();
   }

   public Set keySet() {
      return this.keySet();
   }

   Map backingMap() {
      return this.backingMap();
   }
}
