package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractTable implements Table {
   private transient Set cellSet;
   private transient Collection values;

   public boolean containsRow(@Nullable Object var1) {
      return Maps.safeContainsKey(this.rowMap(), var1);
   }

   public boolean containsColumn(@Nullable Object var1) {
      return Maps.safeContainsKey(this.columnMap(), var1);
   }

   public Set rowKeySet() {
      return this.rowMap().keySet();
   }

   public Set columnKeySet() {
      return this.columnMap().keySet();
   }

   public boolean containsValue(@Nullable Object var1) {
      Iterator var2 = this.rowMap().values().iterator();

      Map var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (Map)var2.next();
      } while(!var3.containsValue(var1));

      return true;
   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      Map var3 = (Map)Maps.safeGet(this.rowMap(), var1);
      return var3 != null && Maps.safeContainsKey(var3, var2);
   }

   public Object get(@Nullable Object var1, @Nullable Object var2) {
      Map var3 = (Map)Maps.safeGet(this.rowMap(), var1);
      return var3 == null ? null : Maps.safeGet(var3, var2);
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public void clear() {
      Iterators.clear(this.cellSet().iterator());
   }

   public Object remove(@Nullable Object var1, @Nullable Object var2) {
      Map var3 = (Map)Maps.safeGet(this.rowMap(), var1);
      return var3 == null ? null : Maps.safeRemove(var3, var2);
   }

   public Object put(Object var1, Object var2, Object var3) {
      return this.row(var1).put(var2, var3);
   }

   public void putAll(Table var1) {
      Iterator var2 = var1.cellSet().iterator();

      while(var2.hasNext()) {
         Table.Cell var3 = (Table.Cell)var2.next();
         this.put(var3.getRowKey(), var3.getColumnKey(), var3.getValue());
      }

   }

   public Set cellSet() {
      Set var1 = this.cellSet;
      return var1 == null ? (this.cellSet = this.createCellSet()) : var1;
   }

   Set createCellSet() {
      return new AbstractTable.CellSet(this);
   }

   abstract Iterator cellIterator();

   public Collection values() {
      Collection var1 = this.values;
      return var1 == null ? (this.values = this.createValues()) : var1;
   }

   Collection createValues() {
      return new AbstractTable.Values(this);
   }

   Iterator valuesIterator() {
      return new TransformedIterator(this, this.cellSet().iterator()) {
         final AbstractTable this$0;

         {
            this.this$0 = var1;
         }

         Object transform(Table.Cell var1) {
            return var1.getValue();
         }

         Object transform(Object var1) {
            return this.transform((Table.Cell)var1);
         }
      };
   }

   public boolean equals(@Nullable Object var1) {
      return Tables.equalsImpl(this, var1);
   }

   public int hashCode() {
      return this.cellSet().hashCode();
   }

   public String toString() {
      return this.rowMap().toString();
   }

   class Values extends AbstractCollection {
      final AbstractTable this$0;

      Values(AbstractTable var1) {
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.valuesIterator();
      }

      public boolean contains(Object var1) {
         return this.this$0.containsValue(var1);
      }

      public void clear() {
         this.this$0.clear();
      }

      public int size() {
         return this.this$0.size();
      }
   }

   class CellSet extends AbstractSet {
      final AbstractTable this$0;

      CellSet(AbstractTable var1) {
         this.this$0 = var1;
      }

      public boolean contains(Object var1) {
         if (!(var1 instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell var2 = (Table.Cell)var1;
            Map var3 = (Map)Maps.safeGet(this.this$0.rowMap(), var2.getRowKey());
            return var3 != null && Collections2.safeContains(var3.entrySet(), Maps.immutableEntry(var2.getColumnKey(), var2.getValue()));
         }
      }

      public boolean remove(@Nullable Object var1) {
         if (!(var1 instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell var2 = (Table.Cell)var1;
            Map var3 = (Map)Maps.safeGet(this.this$0.rowMap(), var2.getRowKey());
            return var3 != null && Collections2.safeRemove(var3.entrySet(), Maps.immutableEntry(var2.getColumnKey(), var2.getValue()));
         }
      }

      public void clear() {
         this.this$0.clear();
      }

      public Iterator iterator() {
         return this.this$0.cellIterator();
      }

      public int size() {
         return this.this$0.size();
      }
   }
}
