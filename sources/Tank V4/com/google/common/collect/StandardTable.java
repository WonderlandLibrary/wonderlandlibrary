package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
class StandardTable extends AbstractTable implements Serializable {
   @GwtTransient
   final Map backingMap;
   @GwtTransient
   final Supplier factory;
   private transient Set columnKeySet;
   private transient Map rowMap;
   private transient StandardTable.ColumnMap columnMap;
   private static final long serialVersionUID = 0L;

   StandardTable(Map var1, Supplier var2) {
      this.backingMap = var1;
      this.factory = var2;
   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return var1 != null && var2 != null && super.contains(var1, var2);
   }

   public boolean containsColumn(@Nullable Object var1) {
      if (var1 == null) {
         return false;
      } else {
         Iterator var2 = this.backingMap.values().iterator();

         Map var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (Map)var2.next();
         } while(!Maps.safeContainsKey(var3, var1));

         return true;
      }
   }

   public boolean containsRow(@Nullable Object var1) {
      return var1 != null && Maps.safeContainsKey(this.backingMap, var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return var1 != null && super.containsValue(var1);
   }

   public Object get(@Nullable Object var1, @Nullable Object var2) {
      return var1 != null && var2 != null ? super.get(var1, var2) : null;
   }

   public boolean isEmpty() {
      return this.backingMap.isEmpty();
   }

   public int size() {
      int var1 = 0;

      Map var3;
      for(Iterator var2 = this.backingMap.values().iterator(); var2.hasNext(); var1 += var3.size()) {
         var3 = (Map)var2.next();
      }

      return var1;
   }

   public void clear() {
      this.backingMap.clear();
   }

   private Map getOrCreate(Object var1) {
      Map var2 = (Map)this.backingMap.get(var1);
      if (var2 == null) {
         var2 = (Map)this.factory.get();
         this.backingMap.put(var1, var2);
      }

      return var2;
   }

   public Object put(Object var1, Object var2, Object var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      return this.getOrCreate(var1).put(var2, var3);
   }

   public Object remove(@Nullable Object var1, @Nullable Object var2) {
      if (var1 != null && var2 != null) {
         Map var3 = (Map)Maps.safeGet(this.backingMap, var1);
         if (var3 == null) {
            return null;
         } else {
            Object var4 = var3.remove(var2);
            if (var3.isEmpty()) {
               this.backingMap.remove(var1);
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   private Map removeColumn(Object var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      Iterator var3 = this.backingMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         Object var5 = ((Map)var4.getValue()).remove(var1);
         if (var5 != null) {
            var2.put(var4.getKey(), var5);
            if (((Map)var4.getValue()).isEmpty()) {
               var3.remove();
            }
         }
      }

      return var2;
   }

   private boolean removeMapping(Object var1, Object var2, Object var3) {
      if (var3 != null) {
         this.remove(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public Set cellSet() {
      return super.cellSet();
   }

   Iterator cellIterator() {
      return new StandardTable.CellIterator(this);
   }

   public Map row(Object var1) {
      return new StandardTable.Row(this, var1);
   }

   public Map column(Object var1) {
      return new StandardTable.Column(this, var1);
   }

   public Set rowKeySet() {
      return this.rowMap().keySet();
   }

   public Set columnKeySet() {
      Set var1 = this.columnKeySet;
      return var1 == null ? (this.columnKeySet = new StandardTable.ColumnKeySet(this)) : var1;
   }

   Iterator createColumnKeyIterator() {
      return new StandardTable.ColumnKeyIterator(this);
   }

   public Collection values() {
      return super.values();
   }

   public Map rowMap() {
      Map var1 = this.rowMap;
      return var1 == null ? (this.rowMap = this.createRowMap()) : var1;
   }

   Map createRowMap() {
      return new StandardTable.RowMap(this);
   }

   public Map columnMap() {
      StandardTable.ColumnMap var1 = this.columnMap;
      return var1 == null ? (this.columnMap = new StandardTable.ColumnMap(this)) : var1;
   }

   static boolean access$400(StandardTable var0, Object var1, Object var2, Object var3) {
      return var0.containsMapping(var1, var2, var3);
   }

   static boolean access$500(StandardTable var0, Object var1, Object var2, Object var3) {
      return var0.removeMapping(var1, var2, var3);
   }

   static Map access$1000(StandardTable var0, Object var1) {
      return var0.removeColumn(var1);
   }

   private class ColumnMap extends Maps.ImprovedAbstractMap {
      final StandardTable this$0;

      private ColumnMap(StandardTable var1) {
         this.this$0 = var1;
      }

      public Map get(Object var1) {
         return this.this$0.containsColumn(var1) ? this.this$0.column(var1) : null;
      }

      public boolean containsKey(Object var1) {
         return this.this$0.containsColumn(var1);
      }

      public Map remove(Object var1) {
         return this.this$0.containsColumn(var1) ? StandardTable.access$1000(this.this$0, var1) : null;
      }

      public Set createEntrySet() {
         return new StandardTable.ColumnMap.ColumnMapEntrySet(this);
      }

      public Set keySet() {
         return this.this$0.columnKeySet();
      }

      Collection createValues() {
         return new StandardTable.ColumnMap.ColumnMapValues(this);
      }

      public Object remove(Object var1) {
         return this.remove(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      ColumnMap(StandardTable var1, Object var2) {
         this(var1);
      }

      private class ColumnMapValues extends Maps.Values {
         final StandardTable.ColumnMap this$1;

         ColumnMapValues(StandardTable.ColumnMap var1) {
            super(var1);
            this.this$1 = var1;
         }

         public boolean remove(Object var1) {
            Iterator var2 = this.this$1.entrySet().iterator();

            Entry var3;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var3 = (Entry)var2.next();
            } while(!((Map)var3.getValue()).equals(var1));

            StandardTable.access$1000(this.this$1.this$0, var3.getKey());
            return true;
         }

         public boolean removeAll(Collection var1) {
            Preconditions.checkNotNull(var1);
            boolean var2 = false;
            Iterator var3 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (var1.contains(this.this$1.this$0.column(var4))) {
                  StandardTable.access$1000(this.this$1.this$0, var4);
                  var2 = true;
               }
            }

            return var2;
         }

         public boolean retainAll(Collection var1) {
            Preconditions.checkNotNull(var1);
            boolean var2 = false;
            Iterator var3 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (!var1.contains(this.this$1.this$0.column(var4))) {
                  StandardTable.access$1000(this.this$1.this$0, var4);
                  var2 = true;
               }
            }

            return var2;
         }
      }

      class ColumnMapEntrySet extends StandardTable.TableSet {
         final StandardTable.ColumnMap this$1;

         ColumnMapEntrySet(StandardTable.ColumnMap var1) {
            super(var1.this$0, null);
            this.this$1 = var1;
         }

         public Iterator iterator() {
            return Maps.asMapEntryIterator(this.this$1.this$0.columnKeySet(), new Function(this) {
               final StandardTable.ColumnMap.ColumnMapEntrySet this$2;

               {
                  this.this$2 = var1;
               }

               public Map apply(Object var1) {
                  return this.this$2.this$1.this$0.column(var1);
               }

               public Object apply(Object var1) {
                  return this.apply(var1);
               }
            });
         }

         public int size() {
            return this.this$1.this$0.columnKeySet().size();
         }

         public boolean remove(Object var1) {
            if (var1 != false) {
               Entry var2 = (Entry)var1;
               StandardTable.access$1000(this.this$1.this$0, var2.getKey());
               return true;
            } else {
               return false;
            }
         }

         public boolean removeAll(Collection var1) {
            Preconditions.checkNotNull(var1);
            return Sets.removeAllImpl(this, (Iterator)var1.iterator());
         }

         public boolean retainAll(Collection var1) {
            Preconditions.checkNotNull(var1);
            boolean var2 = false;
            Iterator var3 = Lists.newArrayList(this.this$1.this$0.columnKeySet().iterator()).iterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (!var1.contains(Maps.immutableEntry(var4, this.this$1.this$0.column(var4)))) {
                  StandardTable.access$1000(this.this$1.this$0, var4);
                  var2 = true;
               }
            }

            return var2;
         }
      }
   }

   class RowMap extends Maps.ImprovedAbstractMap {
      final StandardTable this$0;

      RowMap(StandardTable var1) {
         this.this$0 = var1;
      }

      public boolean containsKey(Object var1) {
         return this.this$0.containsRow(var1);
      }

      public Map get(Object var1) {
         return this.this$0.containsRow(var1) ? this.this$0.row(var1) : null;
      }

      public Map remove(Object var1) {
         return var1 == null ? null : (Map)this.this$0.backingMap.remove(var1);
      }

      protected Set createEntrySet() {
         return new StandardTable.RowMap.EntrySet(this);
      }

      public Object remove(Object var1) {
         return this.remove(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      class EntrySet extends StandardTable.TableSet {
         final StandardTable.RowMap this$1;

         EntrySet(StandardTable.RowMap var1) {
            super(var1.this$0, null);
            this.this$1 = var1;
         }

         public Iterator iterator() {
            return Maps.asMapEntryIterator(this.this$1.this$0.backingMap.keySet(), new Function(this) {
               final StandardTable.RowMap.EntrySet this$2;

               {
                  this.this$2 = var1;
               }

               public Map apply(Object var1) {
                  return this.this$2.this$1.this$0.row(var1);
               }

               public Object apply(Object var1) {
                  return this.apply(var1);
               }
            });
         }

         public int size() {
            return this.this$1.this$0.backingMap.size();
         }

         public boolean contains(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return var2.getKey() != null && var2.getValue() instanceof Map && Collections2.safeContains(this.this$1.this$0.backingMap.entrySet(), var2);
            }
         }

         public boolean remove(Object var1) {
            if (!(var1 instanceof Entry)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               return var2.getKey() != null && var2.getValue() instanceof Map && this.this$1.this$0.backingMap.entrySet().remove(var2);
            }
         }
      }
   }

   private class ColumnKeyIterator extends AbstractIterator {
      final Map seen;
      final Iterator mapIterator;
      Iterator entryIterator;
      final StandardTable this$0;

      private ColumnKeyIterator(StandardTable var1) {
         this.this$0 = var1;
         this.seen = (Map)this.this$0.factory.get();
         this.mapIterator = this.this$0.backingMap.values().iterator();
         this.entryIterator = Iterators.emptyIterator();
      }

      protected Object computeNext() {
         while(true) {
            if (this.entryIterator.hasNext()) {
               Entry var1 = (Entry)this.entryIterator.next();
               if (!this.seen.containsKey(var1.getKey())) {
                  this.seen.put(var1.getKey(), var1.getValue());
                  return var1.getKey();
               }
            } else {
               if (!this.mapIterator.hasNext()) {
                  return this.endOfData();
               }

               this.entryIterator = ((Map)this.mapIterator.next()).entrySet().iterator();
            }
         }
      }

      ColumnKeyIterator(StandardTable var1, Object var2) {
         this(var1);
      }
   }

   private class ColumnKeySet extends StandardTable.TableSet {
      final StandardTable this$0;

      private ColumnKeySet(StandardTable var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.createColumnKeyIterator();
      }

      public int size() {
         return Iterators.size(this.iterator());
      }

      public boolean remove(Object var1) {
         if (var1 == null) {
            return false;
         } else {
            boolean var2 = false;
            Iterator var3 = this.this$0.backingMap.values().iterator();

            while(var3.hasNext()) {
               Map var4 = (Map)var3.next();
               if (var4.keySet().remove(var1)) {
                  var2 = true;
                  if (var4.isEmpty()) {
                     var3.remove();
                  }
               }
            }

            return var2;
         }
      }

      public boolean removeAll(Collection var1) {
         Preconditions.checkNotNull(var1);
         boolean var2 = false;
         Iterator var3 = this.this$0.backingMap.values().iterator();

         while(var3.hasNext()) {
            Map var4 = (Map)var3.next();
            if (Iterators.removeAll(var4.keySet().iterator(), var1)) {
               var2 = true;
               if (var4.isEmpty()) {
                  var3.remove();
               }
            }
         }

         return var2;
      }

      public boolean retainAll(Collection var1) {
         Preconditions.checkNotNull(var1);
         boolean var2 = false;
         Iterator var3 = this.this$0.backingMap.values().iterator();

         while(var3.hasNext()) {
            Map var4 = (Map)var3.next();
            if (var4.keySet().retainAll(var1)) {
               var2 = true;
               if (var4.isEmpty()) {
                  var3.remove();
               }
            }
         }

         return var2;
      }

      public boolean contains(Object var1) {
         return this.this$0.containsColumn(var1);
      }

      ColumnKeySet(StandardTable var1, Object var2) {
         this(var1);
      }
   }

   private class Column extends Maps.ImprovedAbstractMap {
      final Object columnKey;
      final StandardTable this$0;

      Column(StandardTable var1, Object var2) {
         this.this$0 = var1;
         this.columnKey = Preconditions.checkNotNull(var2);
      }

      public Object put(Object var1, Object var2) {
         return this.this$0.put(var1, this.columnKey, var2);
      }

      public Object get(Object var1) {
         return this.this$0.get(var1, this.columnKey);
      }

      public boolean containsKey(Object var1) {
         return this.this$0.contains(var1, this.columnKey);
      }

      public Object remove(Object var1) {
         return this.this$0.remove(var1, this.columnKey);
      }

      boolean removeFromColumnIf(Predicate var1) {
         boolean var2 = false;
         Iterator var3 = this.this$0.backingMap.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            Map var5 = (Map)var4.getValue();
            Object var6 = var5.get(this.columnKey);
            if (var6 != null && var1.apply(Maps.immutableEntry(var4.getKey(), var6))) {
               var5.remove(this.columnKey);
               var2 = true;
               if (var5.isEmpty()) {
                  var3.remove();
               }
            }
         }

         return var2;
      }

      Set createEntrySet() {
         return new StandardTable.Column.EntrySet(this);
      }

      Set createKeySet() {
         return new StandardTable.Column.KeySet(this);
      }

      Collection createValues() {
         return new StandardTable.Column.Values(this);
      }

      private class Values extends Maps.Values {
         final StandardTable.Column this$1;

         Values(StandardTable.Column var1) {
            super(var1);
            this.this$1 = var1;
         }

         public boolean remove(Object var1) {
            return var1 != null && this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.equalTo(var1)));
         }

         public boolean removeAll(Collection var1) {
            return this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.in(var1)));
         }

         public boolean retainAll(Collection var1) {
            return this.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(var1))));
         }
      }

      private class KeySet extends Maps.KeySet {
         final StandardTable.Column this$1;

         KeySet(StandardTable.Column var1) {
            super(var1);
            this.this$1 = var1;
         }

         public boolean contains(Object var1) {
            return this.this$1.this$0.contains(var1, this.this$1.columnKey);
         }

         public boolean remove(Object var1) {
            return this.this$1.this$0.remove(var1, this.this$1.columnKey) != null;
         }

         public boolean retainAll(Collection var1) {
            return this.this$1.removeFromColumnIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(var1))));
         }
      }

      private class EntrySetIterator extends AbstractIterator {
         final Iterator iterator;
         final StandardTable.Column this$1;

         private EntrySetIterator(StandardTable.Column var1) {
            this.this$1 = var1;
            this.iterator = this.this$1.this$0.backingMap.entrySet().iterator();
         }

         protected Entry computeNext() {
            while(true) {
               if (this.iterator.hasNext()) {
                  Entry var1 = (Entry)this.iterator.next();
                  if (!((Map)var1.getValue()).containsKey(this.this$1.columnKey)) {
                     continue;
                  }

                  return new AbstractMapEntry(this, var1) {
                     final Entry val$entry;
                     final StandardTable.Column.EntrySetIterator this$2;

                     {
                        this.this$2 = var1;
                        this.val$entry = var2;
                     }

                     public Object getKey() {
                        return this.val$entry.getKey();
                     }

                     public Object getValue() {
                        return ((Map)this.val$entry.getValue()).get(this.this$2.this$1.columnKey);
                     }

                     public Object setValue(Object var1) {
                        return ((Map)this.val$entry.getValue()).put(this.this$2.this$1.columnKey, Preconditions.checkNotNull(var1));
                     }
                  };
               }

               return (Entry)this.endOfData();
            }
         }

         protected Object computeNext() {
            return this.computeNext();
         }

         EntrySetIterator(StandardTable.Column var1, Object var2) {
            this(var1);
         }
      }

      private class EntrySet extends Sets.ImprovedAbstractSet {
         final StandardTable.Column this$1;

         private EntrySet(StandardTable.Column var1) {
            this.this$1 = var1;
         }

         public Iterator iterator() {
            return this.this$1.new EntrySetIterator(this.this$1);
         }

         public int size() {
            int var1 = 0;
            Iterator var2 = this.this$1.this$0.backingMap.values().iterator();

            while(var2.hasNext()) {
               Map var3 = (Map)var2.next();
               if (var3.containsKey(this.this$1.columnKey)) {
                  ++var1;
               }
            }

            return var1;
         }

         public boolean isEmpty() {
            return !this.this$1.this$0.containsColumn(this.this$1.columnKey);
         }

         public void clear() {
            this.this$1.removeFromColumnIf(Predicates.alwaysTrue());
         }

         public boolean contains(Object var1) {
            if (var1 instanceof Entry) {
               Entry var2 = (Entry)var1;
               return StandardTable.access$400(this.this$1.this$0, var2.getKey(), this.this$1.columnKey, var2.getValue());
            } else {
               return false;
            }
         }

         public boolean remove(Object var1) {
            if (var1 instanceof Entry) {
               Entry var2 = (Entry)var1;
               return StandardTable.access$500(this.this$1.this$0, var2.getKey(), this.this$1.columnKey, var2.getValue());
            } else {
               return false;
            }
         }

         public boolean retainAll(Collection var1) {
            return this.this$1.removeFromColumnIf(Predicates.not(Predicates.in(var1)));
         }

         EntrySet(StandardTable.Column var1, Object var2) {
            this(var1);
         }
      }
   }

   class Row extends Maps.ImprovedAbstractMap {
      final Object rowKey;
      Map backingRowMap;
      final StandardTable this$0;

      Row(StandardTable var1, Object var2) {
         this.this$0 = var1;
         this.rowKey = Preconditions.checkNotNull(var2);
      }

      Map backingRowMap() {
         return this.backingRowMap != null && (!this.backingRowMap.isEmpty() || !this.this$0.backingMap.containsKey(this.rowKey)) ? this.backingRowMap : (this.backingRowMap = this.computeBackingRowMap());
      }

      Map computeBackingRowMap() {
         return (Map)this.this$0.backingMap.get(this.rowKey);
      }

      void maintainEmptyInvariant() {
         if (this.backingRowMap() != null && this.backingRowMap.isEmpty()) {
            this.this$0.backingMap.remove(this.rowKey);
            this.backingRowMap = null;
         }

      }

      public boolean containsKey(Object var1) {
         Map var2 = this.backingRowMap();
         return var1 != null && var2 != null && Maps.safeContainsKey(var2, var1);
      }

      public Object get(Object var1) {
         Map var2 = this.backingRowMap();
         return var1 != null && var2 != null ? Maps.safeGet(var2, var1) : null;
      }

      public Object put(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         return this.backingRowMap != null && !this.backingRowMap.isEmpty() ? this.backingRowMap.put(var1, var2) : this.this$0.put(this.rowKey, var1, var2);
      }

      public Object remove(Object var1) {
         Map var2 = this.backingRowMap();
         if (var2 == null) {
            return null;
         } else {
            Object var3 = Maps.safeRemove(var2, var1);
            this.maintainEmptyInvariant();
            return var3;
         }
      }

      public void clear() {
         Map var1 = this.backingRowMap();
         if (var1 != null) {
            var1.clear();
         }

         this.maintainEmptyInvariant();
      }

      protected Set createEntrySet() {
         return new StandardTable.Row.RowEntrySet(this);
      }

      private final class RowEntrySet extends Maps.EntrySet {
         final StandardTable.Row this$1;

         private RowEntrySet(StandardTable.Row var1) {
            this.this$1 = var1;
         }

         Map map() {
            return this.this$1;
         }

         public int size() {
            Map var1 = this.this$1.backingRowMap();
            return var1 == null ? 0 : var1.size();
         }

         public Iterator iterator() {
            Map var1 = this.this$1.backingRowMap();
            if (var1 == null) {
               return Iterators.emptyModifiableIterator();
            } else {
               Iterator var2 = var1.entrySet().iterator();
               return new Iterator(this, var2) {
                  final Iterator val$iterator;
                  final StandardTable.Row.RowEntrySet this$2;

                  {
                     this.this$2 = var1;
                     this.val$iterator = var2;
                  }

                  public boolean hasNext() {
                     return this.val$iterator.hasNext();
                  }

                  public Entry next() {
                     Entry var1 = (Entry)this.val$iterator.next();
                     return new ForwardingMapEntry(this, var1) {
                        final Entry val$entry;
                        final <undefinedtype> this$3;

                        {
                           this.this$3 = var1;
                           this.val$entry = var2;
                        }

                        protected Entry delegate() {
                           return this.val$entry;
                        }

                        public Object setValue(Object var1) {
                           return super.setValue(Preconditions.checkNotNull(var1));
                        }

                        public boolean equals(Object var1) {
                           return this.standardEquals(var1);
                        }

                        protected Object delegate() {
                           return this.delegate();
                        }
                     };
                  }

                  public void remove() {
                     this.val$iterator.remove();
                     this.this$2.this$1.maintainEmptyInvariant();
                  }

                  public Object next() {
                     return this.next();
                  }
               };
            }
         }

         RowEntrySet(StandardTable.Row var1, Object var2) {
            this(var1);
         }
      }
   }

   private class CellIterator implements Iterator {
      final Iterator rowIterator;
      Entry rowEntry;
      Iterator columnIterator;
      final StandardTable this$0;

      private CellIterator(StandardTable var1) {
         this.this$0 = var1;
         this.rowIterator = this.this$0.backingMap.entrySet().iterator();
         this.columnIterator = Iterators.emptyModifiableIterator();
      }

      public boolean hasNext() {
         return this.rowIterator.hasNext() || this.columnIterator.hasNext();
      }

      public Table.Cell next() {
         if (!this.columnIterator.hasNext()) {
            this.rowEntry = (Entry)this.rowIterator.next();
            this.columnIterator = ((Map)this.rowEntry.getValue()).entrySet().iterator();
         }

         Entry var1 = (Entry)this.columnIterator.next();
         return Tables.immutableCell(this.rowEntry.getKey(), var1.getKey(), var1.getValue());
      }

      public void remove() {
         this.columnIterator.remove();
         if (((Map)this.rowEntry.getValue()).isEmpty()) {
            this.rowIterator.remove();
         }

      }

      public Object next() {
         return this.next();
      }

      CellIterator(StandardTable var1, Object var2) {
         this(var1);
      }
   }

   private abstract class TableSet extends Sets.ImprovedAbstractSet {
      final StandardTable this$0;

      private TableSet(StandardTable var1) {
         this.this$0 = var1;
      }

      public boolean isEmpty() {
         return this.this$0.backingMap.isEmpty();
      }

      public void clear() {
         this.this$0.backingMap.clear();
      }

      TableSet(StandardTable var1, Object var2) {
         this(var1);
      }
   }
}
