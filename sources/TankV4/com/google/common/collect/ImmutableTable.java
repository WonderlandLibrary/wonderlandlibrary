package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ImmutableTable extends AbstractTable {
   private static final ImmutableTable EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());

   public static ImmutableTable of() {
      return EMPTY;
   }

   public static ImmutableTable of(Object var0, Object var1, Object var2) {
      return new SingletonImmutableTable(var0, var1, var2);
   }

   public static ImmutableTable copyOf(Table var0) {
      if (var0 instanceof ImmutableTable) {
         ImmutableTable var6 = (ImmutableTable)var0;
         return var6;
      } else {
         int var1 = var0.size();
         switch(var1) {
         case 0:
            return of();
         case 1:
            Table.Cell var2 = (Table.Cell)Iterables.getOnlyElement(var0.cellSet());
            return of(var2.getRowKey(), var2.getColumnKey(), var2.getValue());
         default:
            ImmutableSet.Builder var3 = ImmutableSet.builder();
            Iterator var4 = var0.cellSet().iterator();

            while(var4.hasNext()) {
               Table.Cell var5 = (Table.Cell)var4.next();
               var3.add((Object)cellOf(var5.getRowKey(), var5.getColumnKey(), var5.getValue()));
            }

            return RegularImmutableTable.forCells(var3.build());
         }
      }
   }

   public static ImmutableTable.Builder builder() {
      return new ImmutableTable.Builder();
   }

   static Table.Cell cellOf(Object var0, Object var1, Object var2) {
      return Tables.immutableCell(Preconditions.checkNotNull(var0), Preconditions.checkNotNull(var1), Preconditions.checkNotNull(var2));
   }

   ImmutableTable() {
   }

   public ImmutableSet cellSet() {
      return (ImmutableSet)super.cellSet();
   }

   abstract ImmutableSet createCellSet();

   final UnmodifiableIterator cellIterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection values() {
      return (ImmutableCollection)super.values();
   }

   abstract ImmutableCollection createValues();

   final Iterator valuesIterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableMap column(Object var1) {
      Preconditions.checkNotNull(var1);
      return (ImmutableMap)Objects.firstNonNull((ImmutableMap)this.columnMap().get(var1), ImmutableMap.of());
   }

   public ImmutableSet columnKeySet() {
      return this.columnMap().keySet();
   }

   public abstract ImmutableMap columnMap();

   public ImmutableMap row(Object var1) {
      Preconditions.checkNotNull(var1);
      return (ImmutableMap)Objects.firstNonNull((ImmutableMap)this.rowMap().get(var1), ImmutableMap.of());
   }

   public ImmutableSet rowKeySet() {
      return this.rowMap().keySet();
   }

   public abstract ImmutableMap rowMap();

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return this.get(var1, var2) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Object put(Object var1, Object var2, Object var3) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void putAll(Table var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Object remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
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

   Collection createValues() {
      return this.createValues();
   }

   public Collection values() {
      return this.values();
   }

   Iterator cellIterator() {
      return this.cellIterator();
   }

   Set createCellSet() {
      return this.createCellSet();
   }

   public Set cellSet() {
      return this.cellSet();
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public Object get(Object var1, Object var2) {
      return super.get(var1, var2);
   }

   public Set columnKeySet() {
      return this.columnKeySet();
   }

   public Set rowKeySet() {
      return this.rowKeySet();
   }

   public boolean containsColumn(Object var1) {
      return super.containsColumn(var1);
   }

   public boolean containsRow(Object var1) {
      return super.containsRow(var1);
   }

   public Map columnMap() {
      return this.columnMap();
   }

   public Map rowMap() {
      return this.rowMap();
   }

   public Map column(Object var1) {
      return this.column(var1);
   }

   public Map row(Object var1) {
      return this.row(var1);
   }

   public static final class Builder {
      private final List cells = Lists.newArrayList();
      private Comparator rowComparator;
      private Comparator columnComparator;

      public ImmutableTable.Builder orderRowsBy(Comparator var1) {
         this.rowComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableTable.Builder orderColumnsBy(Comparator var1) {
         this.columnComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableTable.Builder put(Object var1, Object var2, Object var3) {
         this.cells.add(ImmutableTable.cellOf(var1, var2, var3));
         return this;
      }

      public ImmutableTable.Builder put(Table.Cell var1) {
         if (var1 instanceof Tables.ImmutableCell) {
            Preconditions.checkNotNull(var1.getRowKey());
            Preconditions.checkNotNull(var1.getColumnKey());
            Preconditions.checkNotNull(var1.getValue());
            this.cells.add(var1);
         } else {
            this.put(var1.getRowKey(), var1.getColumnKey(), var1.getValue());
         }

         return this;
      }

      public ImmutableTable.Builder putAll(Table var1) {
         Iterator var2 = var1.cellSet().iterator();

         while(var2.hasNext()) {
            Table.Cell var3 = (Table.Cell)var2.next();
            this.put(var3);
         }

         return this;
      }

      public ImmutableTable build() {
         int var1 = this.cells.size();
         switch(var1) {
         case 0:
            return ImmutableTable.of();
         case 1:
            return new SingletonImmutableTable((Table.Cell)Iterables.getOnlyElement(this.cells));
         default:
            return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
         }
      }
   }
}
