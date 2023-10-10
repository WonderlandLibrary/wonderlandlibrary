package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
@Beta
public interface RowSortedTable extends Table {
   SortedSet rowKeySet();

   SortedMap rowMap();
}
