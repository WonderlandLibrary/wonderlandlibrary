package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
public class HashBasedTable extends StandardTable {
   private static final long serialVersionUID = 0L;

   public static HashBasedTable create() {
      return new HashBasedTable(new HashMap(), new HashBasedTable.Factory(0));
   }

   public static HashBasedTable create(int var0, int var1) {
      CollectPreconditions.checkNonnegative(var1, "expectedCellsPerRow");
      HashMap var2 = Maps.newHashMapWithExpectedSize(var0);
      return new HashBasedTable(var2, new HashBasedTable.Factory(var1));
   }

   public static HashBasedTable create(Table var0) {
      HashBasedTable var1 = create();
      var1.putAll(var0);
      return var1;
   }

   HashBasedTable(Map var1, HashBasedTable.Factory var2) {
      super(var1, var2);
   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return super.contains(var1, var2);
   }

   public boolean containsColumn(@Nullable Object var1) {
      return super.containsColumn(var1);
   }

   public boolean containsRow(@Nullable Object var1) {
      return super.containsRow(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return super.containsValue(var1);
   }

   public Object get(@Nullable Object var1, @Nullable Object var2) {
      return super.get(var1, var2);
   }

   public boolean equals(@Nullable Object var1) {
      return super.equals(var1);
   }

   public Object remove(@Nullable Object var1, @Nullable Object var2) {
      return super.remove(var1, var2);
   }

   public Map columnMap() {
      return super.columnMap();
   }

   public Map rowMap() {
      return super.rowMap();
   }

   public Collection values() {
      return super.values();
   }

   public Set columnKeySet() {
      return super.columnKeySet();
   }

   public Set rowKeySet() {
      return super.rowKeySet();
   }

   public Map column(Object var1) {
      return super.column(var1);
   }

   public Map row(Object var1) {
      return super.row(var1);
   }

   public Set cellSet() {
      return super.cellSet();
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

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public void putAll(Table var1) {
      super.putAll(var1);
   }

   private static class Factory implements Supplier, Serializable {
      final int expectedSize;
      private static final long serialVersionUID = 0L;

      Factory(int var1) {
         this.expectedSize = var1;
      }

      public Map get() {
         return Maps.newHashMapWithExpectedSize(this.expectedSize);
      }

      public Object get() {
         return this.get();
      }
   }
}
