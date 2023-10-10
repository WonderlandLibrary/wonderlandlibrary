package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
class SingletonImmutableTable extends ImmutableTable {
   final Object singleRowKey;
   final Object singleColumnKey;
   final Object singleValue;

   SingletonImmutableTable(Object var1, Object var2, Object var3) {
      this.singleRowKey = Preconditions.checkNotNull(var1);
      this.singleColumnKey = Preconditions.checkNotNull(var2);
      this.singleValue = Preconditions.checkNotNull(var3);
   }

   SingletonImmutableTable(Table.Cell var1) {
      this(var1.getRowKey(), var1.getColumnKey(), var1.getValue());
   }

   public ImmutableMap column(Object var1) {
      Preconditions.checkNotNull(var1);
      return this.containsColumn(var1) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
   }

   public ImmutableMap columnMap() {
      return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
   }

   public ImmutableMap rowMap() {
      return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
   }

   public int size() {
      return 1;
   }

   ImmutableSet createCellSet() {
      return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
   }

   ImmutableCollection createValues() {
      return ImmutableSet.of(this.singleValue);
   }

   Collection createValues() {
      return this.createValues();
   }

   Set createCellSet() {
      return this.createCellSet();
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
}
