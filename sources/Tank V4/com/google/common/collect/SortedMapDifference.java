package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@GwtCompatible
public interface SortedMapDifference extends MapDifference {
   SortedMap entriesOnlyOnLeft();

   SortedMap entriesOnlyOnRight();

   SortedMap entriesInCommon();

   SortedMap entriesDiffering();
}
