package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
class StandardRowSortedTable extends StandardTable implements RowSortedTable {
   private static final long serialVersionUID = 0L;

   StandardRowSortedTable(SortedMap var1, Supplier var2) {
      super(var1, var2);
   }

   private SortedMap sortedBackingMap() {
      return (SortedMap)this.backingMap;
   }

   public SortedSet rowKeySet() {
      return (SortedSet)this.rowMap().keySet();
   }

   public SortedMap rowMap() {
      return (SortedMap)super.rowMap();
   }

   SortedMap createRowMap() {
      return new StandardRowSortedTable.RowSortedMap(this);
   }

   Map createRowMap() {
      return this.createRowMap();
   }

   public Map rowMap() {
      return this.rowMap();
   }

   public Set rowKeySet() {
      return this.rowKeySet();
   }

   static SortedMap access$100(StandardRowSortedTable var0) {
      return var0.sortedBackingMap();
   }

   private class RowSortedMap extends StandardTable.RowMap implements SortedMap {
      final StandardRowSortedTable this$0;

      private RowSortedMap(StandardRowSortedTable var1) {
         super();
         this.this$0 = var1;
      }

      public SortedSet keySet() {
         return (SortedSet)super.keySet();
      }

      SortedSet createKeySet() {
         return new Maps.SortedKeySet(this);
      }

      public Comparator comparator() {
         return StandardRowSortedTable.access$100(this.this$0).comparator();
      }

      public Object firstKey() {
         return StandardRowSortedTable.access$100(this.this$0).firstKey();
      }

      public Object lastKey() {
         return StandardRowSortedTable.access$100(this.this$0).lastKey();
      }

      public SortedMap headMap(Object var1) {
         Preconditions.checkNotNull(var1);
         return (new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).headMap(var1), this.this$0.factory)).rowMap();
      }

      public SortedMap subMap(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         return (new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).subMap(var1, var2), this.this$0.factory)).rowMap();
      }

      public SortedMap tailMap(Object var1) {
         Preconditions.checkNotNull(var1);
         return (new StandardRowSortedTable(StandardRowSortedTable.access$100(this.this$0).tailMap(var1), this.this$0.factory)).rowMap();
      }

      Set createKeySet() {
         return this.createKeySet();
      }

      public Set keySet() {
         return this.keySet();
      }

      RowSortedMap(StandardRowSortedTable var1, Object var2) {
         this(var1);
      }
   }
}
