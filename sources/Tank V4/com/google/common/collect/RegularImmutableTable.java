package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable extends ImmutableTable {
   abstract Table.Cell getCell(int var1);

   final ImmutableSet createCellSet() {
      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new RegularImmutableTable.CellSet(this));
   }

   abstract Object getValue(int var1);

   final ImmutableCollection createValues() {
      return (ImmutableCollection)(this.isEmpty() ? ImmutableList.of() : new RegularImmutableTable.Values(this));
   }

   static RegularImmutableTable forCells(List var0, @Nullable Comparator var1, @Nullable Comparator var2) {
      Preconditions.checkNotNull(var0);
      if (var1 != null || var2 != null) {
         Comparator var3 = new Comparator(var1, var2) {
            final Comparator val$rowComparator;
            final Comparator val$columnComparator;

            {
               this.val$rowComparator = var1;
               this.val$columnComparator = var2;
            }

            public int compare(Table.Cell var1, Table.Cell var2) {
               int var3 = this.val$rowComparator == null ? 0 : this.val$rowComparator.compare(var1.getRowKey(), var2.getRowKey());
               if (var3 != 0) {
                  return var3;
               } else {
                  return this.val$columnComparator == null ? 0 : this.val$columnComparator.compare(var1.getColumnKey(), var2.getColumnKey());
               }
            }

            public int compare(Object var1, Object var2) {
               return this.compare((Table.Cell)var1, (Table.Cell)var2);
            }
         };
         Collections.sort(var0, var3);
      }

      return forCellsInternal(var0, var1, var2);
   }

   static RegularImmutableTable forCells(Iterable var0) {
      return forCellsInternal(var0, (Comparator)null, (Comparator)null);
   }

   private static final RegularImmutableTable forCellsInternal(Iterable var0, @Nullable Comparator var1, @Nullable Comparator var2) {
      ImmutableSet.Builder var3 = ImmutableSet.builder();
      ImmutableSet.Builder var4 = ImmutableSet.builder();
      ImmutableList var5 = ImmutableList.copyOf(var0);
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Table.Cell var7 = (Table.Cell)var6.next();
         var3.add(var7.getRowKey());
         var4.add(var7.getColumnKey());
      }

      ImmutableSet var9 = var3.build();
      if (var1 != null) {
         ArrayList var10 = Lists.newArrayList((Iterable)var9);
         Collections.sort(var10, var1);
         var9 = ImmutableSet.copyOf((Collection)var10);
      }

      ImmutableSet var11 = var4.build();
      if (var2 != null) {
         ArrayList var8 = Lists.newArrayList((Iterable)var11);
         Collections.sort(var8, var2);
         var11 = ImmutableSet.copyOf((Collection)var8);
      }

      return (RegularImmutableTable)((long)var5.size() > (long)var9.size() * (long)var11.size() / 2L ? new DenseImmutableTable(var5, var9, var11) : new SparseImmutableTable(var5, var9, var11));
   }

   Collection createValues() {
      return this.createValues();
   }

   Set createCellSet() {
      return this.createCellSet();
   }

   private final class Values extends ImmutableList {
      final RegularImmutableTable this$0;

      private Values(RegularImmutableTable var1) {
         this.this$0 = var1;
      }

      public int size() {
         return this.this$0.size();
      }

      public Object get(int var1) {
         return this.this$0.getValue(var1);
      }

      boolean isPartialView() {
         return true;
      }

      Values(RegularImmutableTable var1, Object var2) {
         this(var1);
      }
   }

   private final class CellSet extends ImmutableSet {
      final RegularImmutableTable this$0;

      private CellSet(RegularImmutableTable var1) {
         this.this$0 = var1;
      }

      public int size() {
         return this.this$0.size();
      }

      public UnmodifiableIterator iterator() {
         return this.asList().iterator();
      }

      ImmutableList createAsList() {
         return new ImmutableAsList(this) {
            final RegularImmutableTable.CellSet this$1;

            {
               this.this$1 = var1;
            }

            public Table.Cell get(int var1) {
               return this.this$1.this$0.getCell(var1);
            }

            ImmutableCollection delegateCollection() {
               return this.this$1;
            }

            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public boolean contains(@Nullable Object var1) {
         if (!(var1 instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell var2 = (Table.Cell)var1;
            Object var3 = this.this$0.get(var2.getRowKey(), var2.getColumnKey());
            return var3 != null && var3.equals(var2.getValue());
         }
      }

      boolean isPartialView() {
         return false;
      }

      public Iterator iterator() {
         return this.iterator();
      }

      CellSet(RegularImmutableTable var1, Object var2) {
         this(var1);
      }
   }
}
