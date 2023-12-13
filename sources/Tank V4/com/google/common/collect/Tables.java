package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Tables {
   private static final Function UNMODIFIABLE_WRAPPER = new Function() {
      public Map apply(Map var1) {
         return Collections.unmodifiableMap(var1);
      }

      public Object apply(Object var1) {
         return this.apply((Map)var1);
      }
   };

   private Tables() {
   }

   public static Table.Cell immutableCell(@Nullable Object var0, @Nullable Object var1, @Nullable Object var2) {
      return new Tables.ImmutableCell(var0, var1, var2);
   }

   public static Table transpose(Table var0) {
      return (Table)(var0 instanceof Tables.TransposeTable ? ((Tables.TransposeTable)var0).original : new Tables.TransposeTable(var0));
   }

   @Beta
   public static Table newCustomTable(Map var0, Supplier var1) {
      Preconditions.checkArgument(var0.isEmpty());
      Preconditions.checkNotNull(var1);
      return new StandardTable(var0, var1);
   }

   @Beta
   public static Table transformValues(Table var0, Function var1) {
      return new Tables.TransformedTable(var0, var1);
   }

   public static Table unmodifiableTable(Table var0) {
      return new Tables.UnmodifiableTable(var0);
   }

   @Beta
   public static RowSortedTable unmodifiableRowSortedTable(RowSortedTable var0) {
      return new Tables.UnmodifiableRowSortedMap(var0);
   }

   private static Function unmodifiableWrapper() {
      return UNMODIFIABLE_WRAPPER;
   }

   static boolean equalsImpl(Table var0, @Nullable Object var1) {
      if (var1 == var0) {
         return true;
      } else if (var1 instanceof Table) {
         Table var2 = (Table)var1;
         return var0.cellSet().equals(var2.cellSet());
      } else {
         return false;
      }
   }

   static Function access$000() {
      return unmodifiableWrapper();
   }

   static final class UnmodifiableRowSortedMap extends Tables.UnmodifiableTable implements RowSortedTable {
      private static final long serialVersionUID = 0L;

      public UnmodifiableRowSortedMap(RowSortedTable var1) {
         super(var1);
      }

      protected RowSortedTable delegate() {
         return (RowSortedTable)super.delegate();
      }

      public SortedMap rowMap() {
         Function var1 = Tables.access$000();
         return Collections.unmodifiableSortedMap(Maps.transformValues(this.delegate().rowMap(), var1));
      }

      public SortedSet rowKeySet() {
         return Collections.unmodifiableSortedSet(this.delegate().rowKeySet());
      }

      public Map rowMap() {
         return this.rowMap();
      }

      public Set rowKeySet() {
         return this.rowKeySet();
      }

      protected Table delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class UnmodifiableTable extends ForwardingTable implements Serializable {
      final Table delegate;
      private static final long serialVersionUID = 0L;

      UnmodifiableTable(Table var1) {
         this.delegate = (Table)Preconditions.checkNotNull(var1);
      }

      protected Table delegate() {
         return this.delegate;
      }

      public Set cellSet() {
         return Collections.unmodifiableSet(super.cellSet());
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public Map column(@Nullable Object var1) {
         return Collections.unmodifiableMap(super.column(var1));
      }

      public Set columnKeySet() {
         return Collections.unmodifiableSet(super.columnKeySet());
      }

      public Map columnMap() {
         Function var1 = Tables.access$000();
         return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), var1));
      }

      public Object put(@Nullable Object var1, @Nullable Object var2, @Nullable Object var3) {
         throw new UnsupportedOperationException();
      }

      public void putAll(Table var1) {
         throw new UnsupportedOperationException();
      }

      public Object remove(@Nullable Object var1, @Nullable Object var2) {
         throw new UnsupportedOperationException();
      }

      public Map row(@Nullable Object var1) {
         return Collections.unmodifiableMap(super.row(var1));
      }

      public Set rowKeySet() {
         return Collections.unmodifiableSet(super.rowKeySet());
      }

      public Map rowMap() {
         Function var1 = Tables.access$000();
         return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), var1));
      }

      public Collection values() {
         return Collections.unmodifiableCollection(super.values());
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class TransformedTable extends AbstractTable {
      final Table fromTable;
      final Function function;

      TransformedTable(Table var1, Function var2) {
         this.fromTable = (Table)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public boolean contains(Object var1, Object var2) {
         return this.fromTable.contains(var1, var2);
      }

      public Object get(Object var1, Object var2) {
         return this.contains(var1, var2) ? this.function.apply(this.fromTable.get(var1, var2)) : null;
      }

      public int size() {
         return this.fromTable.size();
      }

      public void clear() {
         this.fromTable.clear();
      }

      public Object put(Object var1, Object var2, Object var3) {
         throw new UnsupportedOperationException();
      }

      public void putAll(Table var1) {
         throw new UnsupportedOperationException();
      }

      public Object remove(Object var1, Object var2) {
         return this.contains(var1, var2) ? this.function.apply(this.fromTable.remove(var1, var2)) : null;
      }

      public Map row(Object var1) {
         return Maps.transformValues(this.fromTable.row(var1), this.function);
      }

      public Map column(Object var1) {
         return Maps.transformValues(this.fromTable.column(var1), this.function);
      }

      Function cellFunction() {
         return new Function(this) {
            final Tables.TransformedTable this$0;

            {
               this.this$0 = var1;
            }

            public Table.Cell apply(Table.Cell var1) {
               return Tables.immutableCell(var1.getRowKey(), var1.getColumnKey(), this.this$0.function.apply(var1.getValue()));
            }

            public Object apply(Object var1) {
               return this.apply((Table.Cell)var1);
            }
         };
      }

      Iterator cellIterator() {
         return Iterators.transform(this.fromTable.cellSet().iterator(), this.cellFunction());
      }

      public Set rowKeySet() {
         return this.fromTable.rowKeySet();
      }

      public Set columnKeySet() {
         return this.fromTable.columnKeySet();
      }

      Collection createValues() {
         return Collections2.transform(this.fromTable.values(), this.function);
      }

      public Map rowMap() {
         Function var1 = new Function(this) {
            final Tables.TransformedTable this$0;

            {
               this.this$0 = var1;
            }

            public Map apply(Map var1) {
               return Maps.transformValues(var1, this.this$0.function);
            }

            public Object apply(Object var1) {
               return this.apply((Map)var1);
            }
         };
         return Maps.transformValues(this.fromTable.rowMap(), var1);
      }

      public Map columnMap() {
         Function var1 = new Function(this) {
            final Tables.TransformedTable this$0;

            {
               this.this$0 = var1;
            }

            public Map apply(Map var1) {
               return Maps.transformValues(var1, this.this$0.function);
            }

            public Object apply(Object var1) {
               return this.apply((Map)var1);
            }
         };
         return Maps.transformValues(this.fromTable.columnMap(), var1);
      }
   }

   private static class TransposeTable extends AbstractTable {
      final Table original;
      private static final Function TRANSPOSE_CELL = new Function() {
         public Table.Cell apply(Table.Cell var1) {
            return Tables.immutableCell(var1.getColumnKey(), var1.getRowKey(), var1.getValue());
         }

         public Object apply(Object var1) {
            return this.apply((Table.Cell)var1);
         }
      };

      TransposeTable(Table var1) {
         this.original = (Table)Preconditions.checkNotNull(var1);
      }

      public void clear() {
         this.original.clear();
      }

      public Map column(Object var1) {
         return this.original.row(var1);
      }

      public Set columnKeySet() {
         return this.original.rowKeySet();
      }

      public Map columnMap() {
         return this.original.rowMap();
      }

      public boolean contains(@Nullable Object var1, @Nullable Object var2) {
         return this.original.contains(var2, var1);
      }

      public boolean containsColumn(@Nullable Object var1) {
         return this.original.containsRow(var1);
      }

      public boolean containsRow(@Nullable Object var1) {
         return this.original.containsColumn(var1);
      }

      public boolean containsValue(@Nullable Object var1) {
         return this.original.containsValue(var1);
      }

      public Object get(@Nullable Object var1, @Nullable Object var2) {
         return this.original.get(var2, var1);
      }

      public Object put(Object var1, Object var2, Object var3) {
         return this.original.put(var2, var1, var3);
      }

      public void putAll(Table var1) {
         this.original.putAll(Tables.transpose(var1));
      }

      public Object remove(@Nullable Object var1, @Nullable Object var2) {
         return this.original.remove(var2, var1);
      }

      public Map row(Object var1) {
         return this.original.column(var1);
      }

      public Set rowKeySet() {
         return this.original.columnKeySet();
      }

      public Map rowMap() {
         return this.original.columnMap();
      }

      public int size() {
         return this.original.size();
      }

      public Collection values() {
         return this.original.values();
      }

      Iterator cellIterator() {
         return Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
      }
   }

   abstract static class AbstractCell implements Table.Cell {
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell var2 = (Table.Cell)var1;
            return Objects.equal(this.getRowKey(), var2.getRowKey()) && Objects.equal(this.getColumnKey(), var2.getColumnKey()) && Objects.equal(this.getValue(), var2.getValue());
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.getRowKey(), this.getColumnKey(), this.getValue());
      }

      public String toString() {
         return "(" + this.getRowKey() + "," + this.getColumnKey() + ")=" + this.getValue();
      }
   }

   static final class ImmutableCell extends Tables.AbstractCell implements Serializable {
      private final Object rowKey;
      private final Object columnKey;
      private final Object value;
      private static final long serialVersionUID = 0L;

      ImmutableCell(@Nullable Object var1, @Nullable Object var2, @Nullable Object var3) {
         this.rowKey = var1;
         this.columnKey = var2;
         this.value = var3;
      }

      public Object getRowKey() {
         return this.rowKey;
      }

      public Object getColumnKey() {
         return this.columnKey;
      }

      public Object getValue() {
         return this.value;
      }
   }
}
