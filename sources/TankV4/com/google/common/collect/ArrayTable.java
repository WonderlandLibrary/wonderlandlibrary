package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(
   emulated = true
)
public final class ArrayTable extends AbstractTable implements Serializable {
   private final ImmutableList rowList;
   private final ImmutableList columnList;
   private final ImmutableMap rowKeyToIndex;
   private final ImmutableMap columnKeyToIndex;
   private final Object[][] array;
   private transient ArrayTable.ColumnMap columnMap;
   private transient ArrayTable.RowMap rowMap;
   private static final long serialVersionUID = 0L;

   public static ArrayTable create(Iterable var0, Iterable var1) {
      return new ArrayTable(var0, var1);
   }

   public static ArrayTable create(Table var0) {
      return var0 instanceof ArrayTable ? new ArrayTable((ArrayTable)var0) : new ArrayTable(var0);
   }

   private ArrayTable(Iterable var1, Iterable var2) {
      this.rowList = ImmutableList.copyOf(var1);
      this.columnList = ImmutableList.copyOf(var2);
      Preconditions.checkArgument(!this.rowList.isEmpty());
      Preconditions.checkArgument(!this.columnList.isEmpty());
      this.rowKeyToIndex = index(this.rowList);
      this.columnKeyToIndex = index(this.columnList);
      Object[][] var3 = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = var3;
      this.eraseAll();
   }

   private static ImmutableMap index(List var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1.put(var0.get(var2), var2);
      }

      return var1.build();
   }

   private ArrayTable(Table var1) {
      this(var1.rowKeySet(), var1.columnKeySet());
      this.putAll(var1);
   }

   private ArrayTable(ArrayTable var1) {
      this.rowList = var1.rowList;
      this.columnList = var1.columnList;
      this.rowKeyToIndex = var1.rowKeyToIndex;
      this.columnKeyToIndex = var1.columnKeyToIndex;
      Object[][] var2 = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = var2;
      this.eraseAll();

      for(int var3 = 0; var3 < this.rowList.size(); ++var3) {
         System.arraycopy(var1.array[var3], 0, var2[var3], 0, var1.array[var3].length);
      }

   }

   public ImmutableList rowKeyList() {
      return this.rowList;
   }

   public ImmutableList columnKeyList() {
      return this.columnList;
   }

   public Object at(int var1, int var2) {
      Preconditions.checkElementIndex(var1, this.rowList.size());
      Preconditions.checkElementIndex(var2, this.columnList.size());
      return this.array[var1][var2];
   }

   public Object set(int var1, int var2, @Nullable Object var3) {
      Preconditions.checkElementIndex(var1, this.rowList.size());
      Preconditions.checkElementIndex(var2, this.columnList.size());
      Object var4 = this.array[var1][var2];
      this.array[var1][var2] = var3;
      return var4;
   }

   @GwtIncompatible("reflection")
   public Object[][] toArray(Class var1) {
      Object[][] var2 = (Object[][])((Object[][])Array.newInstance(var1, new int[]{this.rowList.size(), this.columnList.size()}));

      for(int var3 = 0; var3 < this.rowList.size(); ++var3) {
         System.arraycopy(this.array[var3], 0, var2[var3], 0, this.array[var3].length);
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void eraseAll() {
      Object[][] var1 = this.array;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object[] var4 = var1[var3];
         Arrays.fill(var4, (Object)null);
      }

   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return this.containsRow(var1) && this.containsColumn(var2);
   }

   public boolean containsColumn(@Nullable Object var1) {
      return this.columnKeyToIndex.containsKey(var1);
   }

   public boolean containsRow(@Nullable Object var1) {
      return this.rowKeyToIndex.containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      Object[][] var2 = this.array;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object[] var5 = var2[var4];
         Object[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Object var9 = var6[var8];
            if (Objects.equal(var1, var9)) {
               return true;
            }
         }
      }

      return false;
   }

   public Object get(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null ? this.at(var3, var4) : null;
   }

   public boolean isEmpty() {
      return false;
   }

   public Object put(Object var1, Object var2, @Nullable Object var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Integer var4 = (Integer)this.rowKeyToIndex.get(var1);
      Preconditions.checkArgument(var4 != null, "Row %s not in %s", var1, this.rowList);
      Integer var5 = (Integer)this.columnKeyToIndex.get(var2);
      Preconditions.checkArgument(var5 != null, "Column %s not in %s", var2, this.columnList);
      return this.set(var4, var5, var3);
   }

   public void putAll(Table var1) {
      super.putAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public Object remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public Object erase(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null ? this.set(var3, var4, (Object)null) : null;
   }

   public int size() {
      return this.rowList.size() * this.columnList.size();
   }

   public Set cellSet() {
      return super.cellSet();
   }

   Iterator cellIterator() {
      return new AbstractIndexedListIterator(this, this.size()) {
         final ArrayTable this$0;

         {
            this.this$0 = var1;
         }

         protected Table.Cell get(int var1) {
            return new Tables.AbstractCell(this, var1) {
               final int rowIndex;
               final int columnIndex;
               final int val$index;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.val$index = var2;
                  this.rowIndex = this.val$index / ArrayTable.access$000(this.this$1.this$0).size();
                  this.columnIndex = this.val$index % ArrayTable.access$000(this.this$1.this$0).size();
               }

               public Object getRowKey() {
                  return ArrayTable.access$100(this.this$1.this$0).get(this.rowIndex);
               }

               public Object getColumnKey() {
                  return ArrayTable.access$000(this.this$1.this$0).get(this.columnIndex);
               }

               public Object getValue() {
                  return this.this$1.this$0.at(this.rowIndex, this.columnIndex);
               }
            };
         }

         protected Object get(int var1) {
            return this.get(var1);
         }
      };
   }

   public Map column(Object var1) {
      Preconditions.checkNotNull(var1);
      Integer var2 = (Integer)this.columnKeyToIndex.get(var1);
      return (Map)(var2 == null ? ImmutableMap.of() : new ArrayTable.Column(this, var2));
   }

   public ImmutableSet columnKeySet() {
      return this.columnKeyToIndex.keySet();
   }

   public Map columnMap() {
      ArrayTable.ColumnMap var1 = this.columnMap;
      return var1 == null ? (this.columnMap = new ArrayTable.ColumnMap(this)) : var1;
   }

   public Map row(Object var1) {
      Preconditions.checkNotNull(var1);
      Integer var2 = (Integer)this.rowKeyToIndex.get(var1);
      return (Map)(var2 == null ? ImmutableMap.of() : new ArrayTable.Row(this, var2));
   }

   public ImmutableSet rowKeySet() {
      return this.rowKeyToIndex.keySet();
   }

   public Map rowMap() {
      ArrayTable.RowMap var1 = this.rowMap;
      return var1 == null ? (this.rowMap = new ArrayTable.RowMap(this)) : var1;
   }

   public Collection values() {
      return super.values();
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

   public Set columnKeySet() {
      return this.columnKeySet();
   }

   public Set rowKeySet() {
      return this.rowKeySet();
   }

   static ImmutableList access$000(ArrayTable var0) {
      return var0.columnList;
   }

   static ImmutableList access$100(ArrayTable var0) {
      return var0.rowList;
   }

   static ImmutableMap access$200(ArrayTable var0) {
      return var0.rowKeyToIndex;
   }

   static ImmutableMap access$500(ArrayTable var0) {
      return var0.columnKeyToIndex;
   }

   private class RowMap extends ArrayTable.ArrayMap {
      final ArrayTable this$0;

      private RowMap(ArrayTable var1) {
         super(ArrayTable.access$200(var1), null);
         this.this$0 = var1;
      }

      String getKeyRole() {
         return "Row";
      }

      Map getValue(int var1) {
         return this.this$0.new Row(this.this$0, var1);
      }

      Map setValue(int var1, Map var2) {
         throw new UnsupportedOperationException();
      }

      public Map put(Object var1, Map var2) {
         throw new UnsupportedOperationException();
      }

      public Object put(Object var1, Object var2) {
         return this.put(var1, (Map)var2);
      }

      Object setValue(int var1, Object var2) {
         return this.setValue(var1, (Map)var2);
      }

      Object getValue(int var1) {
         return this.getValue(var1);
      }

      RowMap(ArrayTable var1, Object var2) {
         this(var1);
      }
   }

   private class Row extends ArrayTable.ArrayMap {
      final int rowIndex;
      final ArrayTable this$0;

      Row(ArrayTable var1, int var2) {
         super(ArrayTable.access$500(var1), null);
         this.this$0 = var1;
         this.rowIndex = var2;
      }

      String getKeyRole() {
         return "Column";
      }

      Object getValue(int var1) {
         return this.this$0.at(this.rowIndex, var1);
      }

      Object setValue(int var1, Object var2) {
         return this.this$0.set(this.rowIndex, var1, var2);
      }
   }

   private class ColumnMap extends ArrayTable.ArrayMap {
      final ArrayTable this$0;

      private ColumnMap(ArrayTable var1) {
         super(ArrayTable.access$500(var1), null);
         this.this$0 = var1;
      }

      String getKeyRole() {
         return "Column";
      }

      Map getValue(int var1) {
         return this.this$0.new Column(this.this$0, var1);
      }

      Map setValue(int var1, Map var2) {
         throw new UnsupportedOperationException();
      }

      public Map put(Object var1, Map var2) {
         throw new UnsupportedOperationException();
      }

      public Object put(Object var1, Object var2) {
         return this.put(var1, (Map)var2);
      }

      Object setValue(int var1, Object var2) {
         return this.setValue(var1, (Map)var2);
      }

      Object getValue(int var1) {
         return this.getValue(var1);
      }

      ColumnMap(ArrayTable var1, Object var2) {
         this(var1);
      }
   }

   private class Column extends ArrayTable.ArrayMap {
      final int columnIndex;
      final ArrayTable this$0;

      Column(ArrayTable var1, int var2) {
         super(ArrayTable.access$200(var1), null);
         this.this$0 = var1;
         this.columnIndex = var2;
      }

      String getKeyRole() {
         return "Row";
      }

      Object getValue(int var1) {
         return this.this$0.at(var1, this.columnIndex);
      }

      Object setValue(int var1, Object var2) {
         return this.this$0.set(var1, this.columnIndex, var2);
      }
   }

   private abstract static class ArrayMap extends Maps.ImprovedAbstractMap {
      private final ImmutableMap keyIndex;

      private ArrayMap(ImmutableMap var1) {
         this.keyIndex = var1;
      }

      public Set keySet() {
         return this.keyIndex.keySet();
      }

      Object getKey(int var1) {
         return this.keyIndex.keySet().asList().get(var1);
      }

      abstract String getKeyRole();

      @Nullable
      abstract Object getValue(int var1);

      @Nullable
      abstract Object setValue(int var1, Object var2);

      public int size() {
         return this.keyIndex.size();
      }

      public boolean isEmpty() {
         return this.keyIndex.isEmpty();
      }

      protected Set createEntrySet() {
         return new Maps.EntrySet(this) {
            final ArrayTable.ArrayMap this$0;

            {
               this.this$0 = var1;
            }

            Map map() {
               return this.this$0;
            }

            public Iterator iterator() {
               return new AbstractIndexedListIterator(this, this.size()) {
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                  }

                  protected Entry get(int var1) {
                     return new AbstractMapEntry(this, var1) {
                        final int val$index;
                        final <undefinedtype> this$2;

                        {
                           this.this$2 = var1;
                           this.val$index = var2;
                        }

                        public Object getKey() {
                           return this.this$2.this$1.this$0.getKey(this.val$index);
                        }

                        public Object getValue() {
                           return this.this$2.this$1.this$0.getValue(this.val$index);
                        }

                        public Object setValue(Object var1) {
                           return this.this$2.this$1.this$0.setValue(this.val$index, var1);
                        }
                     };
                  }

                  protected Object get(int var1) {
                     return this.get(var1);
                  }
               };
            }
         };
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.keyIndex.containsKey(var1);
      }

      public Object get(@Nullable Object var1) {
         Integer var2 = (Integer)this.keyIndex.get(var1);
         return var2 == null ? null : this.getValue(var2);
      }

      public Object put(Object var1, Object var2) {
         Integer var3 = (Integer)this.keyIndex.get(var1);
         if (var3 == null) {
            throw new IllegalArgumentException(this.getKeyRole() + " " + var1 + " not in " + this.keyIndex.keySet());
         } else {
            return this.setValue(var3, var2);
         }
      }

      public Object remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      ArrayMap(ImmutableMap var1, Object var2) {
         this(var1);
      }
   }
}
