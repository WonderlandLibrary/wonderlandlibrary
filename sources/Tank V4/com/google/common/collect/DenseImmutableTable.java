package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class DenseImmutableTable extends RegularImmutableTable {
   private final ImmutableMap rowKeyToIndex;
   private final ImmutableMap columnKeyToIndex;
   private final ImmutableMap rowMap;
   private final ImmutableMap columnMap;
   private final int[] rowCounts;
   private final int[] columnCounts;
   private final Object[][] values;
   private final int[] iterationOrderRow;
   private final int[] iterationOrderColumn;

   private static ImmutableMap makeIndex(ImmutableSet var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      int var2 = 0;

      for(Iterator var3 = var0.iterator(); var3.hasNext(); ++var2) {
         Object var4 = var3.next();
         var1.put(var4, var2);
      }

      return var1.build();
   }

   DenseImmutableTable(ImmutableList var1, ImmutableSet var2, ImmutableSet var3) {
      Object[][] var4 = (Object[][])(new Object[var2.size()][var3.size()]);
      this.values = var4;
      this.rowKeyToIndex = makeIndex(var2);
      this.columnKeyToIndex = makeIndex(var3);
      this.rowCounts = new int[this.rowKeyToIndex.size()];
      this.columnCounts = new int[this.columnKeyToIndex.size()];
      int[] var5 = new int[var1.size()];
      int[] var6 = new int[var1.size()];

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         Table.Cell var8 = (Table.Cell)var1.get(var7);
         Object var9 = var8.getRowKey();
         Object var10 = var8.getColumnKey();
         int var11 = (Integer)this.rowKeyToIndex.get(var9);
         int var12 = (Integer)this.columnKeyToIndex.get(var10);
         Object var13 = this.values[var11][var12];
         Preconditions.checkArgument(var13 == null, "duplicate key: (%s, %s)", var9, var10);
         this.values[var11][var12] = var8.getValue();
         int var10002 = this.rowCounts[var11]++;
         var10002 = this.columnCounts[var12]++;
         var5[var7] = var11;
         var6[var7] = var12;
      }

      this.iterationOrderRow = var5;
      this.iterationOrderColumn = var6;
      this.rowMap = new DenseImmutableTable.RowMap(this);
      this.columnMap = new DenseImmutableTable.ColumnMap(this);
   }

   public ImmutableMap columnMap() {
      return this.columnMap;
   }

   public ImmutableMap rowMap() {
      return this.rowMap;
   }

   public Object get(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null ? this.values[var3][var4] : null;
   }

   public int size() {
      return this.iterationOrderRow.length;
   }

   Table.Cell getCell(int var1) {
      int var2 = this.iterationOrderRow[var1];
      int var3 = this.iterationOrderColumn[var1];
      Object var4 = this.rowKeySet().asList().get(var2);
      Object var5 = this.columnKeySet().asList().get(var3);
      Object var6 = this.values[var2][var3];
      return cellOf(var4, var5, var6);
   }

   Object getValue(int var1) {
      return this.values[this.iterationOrderRow[var1]][this.iterationOrderColumn[var1]];
   }

   public Map columnMap() {
      return this.columnMap();
   }

   public Map rowMap() {
      return this.rowMap();
   }

   static int[] access$200(DenseImmutableTable var0) {
      return var0.rowCounts;
   }

   static ImmutableMap access$300(DenseImmutableTable var0) {
      return var0.columnKeyToIndex;
   }

   static Object[][] access$400(DenseImmutableTable var0) {
      return var0.values;
   }

   static int[] access$500(DenseImmutableTable var0) {
      return var0.columnCounts;
   }

   static ImmutableMap access$600(DenseImmutableTable var0) {
      return var0.rowKeyToIndex;
   }

   private final class ColumnMap extends DenseImmutableTable.ImmutableArrayMap {
      final DenseImmutableTable this$0;

      private ColumnMap(DenseImmutableTable var1) {
         super(DenseImmutableTable.access$500(var1).length);
         this.this$0 = var1;
      }

      ImmutableMap keyToIndex() {
         return DenseImmutableTable.access$300(this.this$0);
      }

      Map getValue(int var1) {
         return this.this$0.new Column(this.this$0, var1);
      }

      boolean isPartialView() {
         return false;
      }

      Object getValue(int var1) {
         return this.getValue(var1);
      }

      ColumnMap(DenseImmutableTable var1, Object var2) {
         this(var1);
      }
   }

   private final class RowMap extends DenseImmutableTable.ImmutableArrayMap {
      final DenseImmutableTable this$0;

      private RowMap(DenseImmutableTable var1) {
         super(DenseImmutableTable.access$200(var1).length);
         this.this$0 = var1;
      }

      ImmutableMap keyToIndex() {
         return DenseImmutableTable.access$600(this.this$0);
      }

      Map getValue(int var1) {
         return this.this$0.new Row(this.this$0, var1);
      }

      boolean isPartialView() {
         return false;
      }

      Object getValue(int var1) {
         return this.getValue(var1);
      }

      RowMap(DenseImmutableTable var1, Object var2) {
         this(var1);
      }
   }

   private final class Column extends DenseImmutableTable.ImmutableArrayMap {
      private final int columnIndex;
      final DenseImmutableTable this$0;

      Column(DenseImmutableTable var1, int var2) {
         super(DenseImmutableTable.access$500(var1)[var2]);
         this.this$0 = var1;
         this.columnIndex = var2;
      }

      ImmutableMap keyToIndex() {
         return DenseImmutableTable.access$600(this.this$0);
      }

      Object getValue(int var1) {
         return DenseImmutableTable.access$400(this.this$0)[var1][this.columnIndex];
      }

      boolean isPartialView() {
         return true;
      }
   }

   private final class Row extends DenseImmutableTable.ImmutableArrayMap {
      private final int rowIndex;
      final DenseImmutableTable this$0;

      Row(DenseImmutableTable var1, int var2) {
         super(DenseImmutableTable.access$200(var1)[var2]);
         this.this$0 = var1;
         this.rowIndex = var2;
      }

      ImmutableMap keyToIndex() {
         return DenseImmutableTable.access$300(this.this$0);
      }

      Object getValue(int var1) {
         return DenseImmutableTable.access$400(this.this$0)[this.rowIndex][var1];
      }

      boolean isPartialView() {
         return true;
      }
   }

   private abstract static class ImmutableArrayMap extends ImmutableMap {
      private final int size;

      ImmutableArrayMap(int var1) {
         this.size = var1;
      }

      abstract ImmutableMap keyToIndex();

      Object getKey(int var1) {
         return this.keyToIndex().keySet().asList().get(var1);
      }

      @Nullable
      abstract Object getValue(int var1);

      ImmutableSet createKeySet() {
         // $FF: Couldn't be decompiled
      }

      public int size() {
         return this.size;
      }

      public Object get(@Nullable Object var1) {
         Integer var2 = (Integer)this.keyToIndex().get(var1);
         return var2 == null ? null : this.getValue(var2);
      }

      ImmutableSet createEntrySet() {
         return new ImmutableMapEntrySet(this) {
            final DenseImmutableTable.ImmutableArrayMap this$0;

            {
               this.this$0 = var1;
            }

            ImmutableMap map() {
               return this.this$0;
            }

            public UnmodifiableIterator iterator() {
               return new AbstractIterator(this) {
                  private int index;
                  private final int maxIndex;
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                     this.index = -1;
                     this.maxIndex = this.this$1.this$0.keyToIndex().size();
                  }

                  protected Entry computeNext() {
                     ++this.index;

                     while(this.index < this.maxIndex) {
                        Object var1 = this.this$1.this$0.getValue(this.index);
                        if (var1 != null) {
                           return Maps.immutableEntry(this.this$1.this$0.getKey(this.index), var1);
                        }

                        ++this.index;
                     }

                     return (Entry)this.endOfData();
                  }

                  protected Object computeNext() {
                     return this.computeNext();
                  }
               };
            }

            public Iterator iterator() {
               return this.iterator();
            }
         };
      }
   }
}
