package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
@Beta
public class TreeBasedTable extends StandardRowSortedTable {
   private final Comparator columnComparator;
   private static final long serialVersionUID = 0L;

   public static TreeBasedTable create() {
      return new TreeBasedTable(Ordering.natural(), Ordering.natural());
   }

   public static TreeBasedTable create(Comparator var0, Comparator var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new TreeBasedTable(var0, var1);
   }

   public static TreeBasedTable create(TreeBasedTable var0) {
      TreeBasedTable var1 = new TreeBasedTable(var0.rowComparator(), var0.columnComparator());
      var1.putAll(var0);
      return var1;
   }

   TreeBasedTable(Comparator var1, Comparator var2) {
      super(new TreeMap(var1), new TreeBasedTable.Factory(var2));
      this.columnComparator = var2;
   }

   public Comparator rowComparator() {
      return this.rowKeySet().comparator();
   }

   public Comparator columnComparator() {
      return this.columnComparator;
   }

   public SortedMap row(Object var1) {
      return new TreeBasedTable.TreeRow(this, var1);
   }

   public SortedSet rowKeySet() {
      return super.rowKeySet();
   }

   public SortedMap rowMap() {
      return super.rowMap();
   }

   Iterator createColumnKeyIterator() {
      Comparator var1 = this.columnComparator();
      UnmodifiableIterator var2 = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new Function(this) {
         final TreeBasedTable this$0;

         {
            this.this$0 = var1;
         }

         public Iterator apply(Map var1) {
            return var1.keySet().iterator();
         }

         public Object apply(Object var1) {
            return this.apply((Map)var1);
         }
      }), var1);
      return new AbstractIterator(this, var2, var1) {
         Object lastValue;
         final Iterator val$merged;
         final Comparator val$comparator;
         final TreeBasedTable this$0;

         {
            this.this$0 = var1;
            this.val$merged = var2;
            this.val$comparator = var3;
         }

         protected Object computeNext() {
            Object var1;
            boolean var2;
            do {
               if (!this.val$merged.hasNext()) {
                  this.lastValue = null;
                  return this.endOfData();
               }

               var1 = this.val$merged.next();
               var2 = this.lastValue != null && this.val$comparator.compare(var1, this.lastValue) == 0;
            } while(var2);

            this.lastValue = var1;
            return this.lastValue;
         }
      };
   }

   public Map rowMap() {
      return this.rowMap();
   }

   public Set rowKeySet() {
      return this.rowKeySet();
   }

   public Map row(Object var1) {
      return this.row(var1);
   }

   public Map columnMap() {
      return super.columnMap();
   }

   public Collection values() {
      return super.values();
   }

   public Set columnKeySet() {
      return super.columnKeySet();
   }

   public Map column(Object var1) {
      return super.column(var1);
   }

   public Set cellSet() {
      return super.cellSet();
   }

   public Object remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   public Object put(Object var1, Object var2, Object var3) {
      return super.put(var1, var2, var3);
   }

   public void clear() {
      super.clear();
   }

   public int size() {
      return super.size();
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public Object get(Object var1, Object var2) {
      return super.get(var1, var2);
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   public boolean containsRow(Object var1) {
      return super.containsRow(var1);
   }

   public boolean containsColumn(Object var1) {
      return super.containsColumn(var1);
   }

   public boolean contains(Object var1, Object var2) {
      return super.contains(var1, var2);
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

   public void putAll(Table var1) {
      super.putAll(var1);
   }

   private class TreeRow extends StandardTable.Row implements SortedMap {
      @Nullable
      final Object lowerBound;
      @Nullable
      final Object upperBound;
      transient SortedMap wholeRow;
      final TreeBasedTable this$0;

      TreeRow(TreeBasedTable var1, Object var2) {
         this(var1, var2, (Object)null, (Object)null);
      }

      TreeRow(TreeBasedTable var1, @Nullable Object var2, @Nullable Object var3, Object var4) {
         super(var2);
         this.this$0 = var1;
         this.lowerBound = var3;
         this.upperBound = var4;
         Preconditions.checkArgument(var3 == null || var4 == null || this.compare(var3, var4) <= 0);
      }

      public SortedSet keySet() {
         return new Maps.SortedKeySet(this);
      }

      public Comparator comparator() {
         return this.this$0.columnComparator();
      }

      int compare(Object var1, Object var2) {
         Comparator var3 = this.comparator();
         return var3.compare(var1, var2);
      }

      public SortedMap subMap(Object var1, Object var2) {
         TreeBasedTable.TreeRow var10001;
         label12: {
            if (Preconditions.checkNotNull(var1) != null) {
               var10001 = this;
               if (Preconditions.checkNotNull(var2) != null) {
                  boolean var10002 = true;
                  break label12;
               }
            }

            var10001 = false;
         }

         Preconditions.checkArgument((boolean)var10001);
         return this.this$0.new TreeRow(this.this$0, this.rowKey, var1, var2);
      }

      public SortedMap headMap(Object var1) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return this.this$0.new TreeRow(this.this$0, this.rowKey, this.lowerBound, var1);
      }

      public SortedMap tailMap(Object var1) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return this.this$0.new TreeRow(this.this$0, this.rowKey, var1, this.upperBound);
      }

      public Object firstKey() {
         SortedMap var1 = this.backingRowMap();
         if (var1 == null) {
            throw new NoSuchElementException();
         } else {
            return this.backingRowMap().firstKey();
         }
      }

      public Object lastKey() {
         SortedMap var1 = this.backingRowMap();
         if (var1 == null) {
            throw new NoSuchElementException();
         } else {
            return this.backingRowMap().lastKey();
         }
      }

      SortedMap wholeRow() {
         if (this.wholeRow == null || this.wholeRow.isEmpty() && this.this$0.backingMap.containsKey(this.rowKey)) {
            this.wholeRow = (SortedMap)this.this$0.backingMap.get(this.rowKey);
         }

         return this.wholeRow;
      }

      SortedMap backingRowMap() {
         return (SortedMap)super.backingRowMap();
      }

      SortedMap computeBackingRowMap() {
         SortedMap var1 = this.wholeRow();
         if (var1 != null) {
            if (this.lowerBound != null) {
               var1 = var1.tailMap(this.lowerBound);
            }

            if (this.upperBound != null) {
               var1 = var1.headMap(this.upperBound);
            }

            return var1;
         } else {
            return null;
         }
      }

      void maintainEmptyInvariant() {
         if (this.wholeRow() != null && this.wholeRow.isEmpty()) {
            this.this$0.backingMap.remove(this.rowKey);
            this.wholeRow = null;
            this.backingRowMap = null;
         }

      }

      public boolean containsKey(Object var1) {
         return var1 != null && super.containsKey(var1);
      }

      public Object put(Object var1, Object var2) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return super.put(var1, var2);
      }

      Map computeBackingRowMap() {
         return this.computeBackingRowMap();
      }

      Map backingRowMap() {
         return this.backingRowMap();
      }

      public Set keySet() {
         return this.keySet();
      }
   }

   private static class Factory implements Supplier, Serializable {
      final Comparator comparator;
      private static final long serialVersionUID = 0L;

      Factory(Comparator var1) {
         this.comparator = var1;
      }

      public TreeMap get() {
         return new TreeMap(this.comparator);
      }

      public Object get() {
         return this.get();
      }
   }
}
