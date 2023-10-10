package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

@Beta
@GwtCompatible
public abstract class MultimapBuilder {
   private static final int DEFAULT_EXPECTED_KEYS = 8;

   private MultimapBuilder() {
   }

   public static MultimapBuilder.MultimapBuilderWithKeys hashKeys() {
      return hashKeys(8);
   }

   public static MultimapBuilder.MultimapBuilderWithKeys hashKeys(int var0) {
      CollectPreconditions.checkNonnegative(var0, "expectedKeys");
      return new MultimapBuilder.MultimapBuilderWithKeys(var0) {
         final int val$expectedKeys;

         {
            this.val$expectedKeys = var1;
         }

         Map createMap() {
            return new HashMap(this.val$expectedKeys);
         }
      };
   }

   public static MultimapBuilder.MultimapBuilderWithKeys linkedHashKeys() {
      return linkedHashKeys(8);
   }

   public static MultimapBuilder.MultimapBuilderWithKeys linkedHashKeys(int var0) {
      CollectPreconditions.checkNonnegative(var0, "expectedKeys");
      return new MultimapBuilder.MultimapBuilderWithKeys(var0) {
         final int val$expectedKeys;

         {
            this.val$expectedKeys = var1;
         }

         Map createMap() {
            return new LinkedHashMap(this.val$expectedKeys);
         }
      };
   }

   public static MultimapBuilder.MultimapBuilderWithKeys treeKeys() {
      return treeKeys(Ordering.natural());
   }

   public static MultimapBuilder.MultimapBuilderWithKeys treeKeys(Comparator var0) {
      Preconditions.checkNotNull(var0);
      return new MultimapBuilder.MultimapBuilderWithKeys(var0) {
         final Comparator val$comparator;

         {
            this.val$comparator = var1;
         }

         Map createMap() {
            return new TreeMap(this.val$comparator);
         }
      };
   }

   public static MultimapBuilder.MultimapBuilderWithKeys enumKeys(Class var0) {
      Preconditions.checkNotNull(var0);
      return new MultimapBuilder.MultimapBuilderWithKeys(var0) {
         final Class val$keyClass;

         {
            this.val$keyClass = var1;
         }

         Map createMap() {
            return new EnumMap(this.val$keyClass);
         }
      };
   }

   public abstract Multimap build();

   public Multimap build(Multimap var1) {
      Multimap var2 = this.build();
      var2.putAll(var1);
      return var2;
   }

   MultimapBuilder(Object var1) {
      this();
   }

   public abstract static class SortedSetMultimapBuilder extends MultimapBuilder.SetMultimapBuilder {
      SortedSetMultimapBuilder() {
      }

      public abstract SortedSetMultimap build();

      public SortedSetMultimap build(Multimap var1) {
         return (SortedSetMultimap)super.build(var1);
      }

      public SetMultimap build(Multimap var1) {
         return this.build(var1);
      }

      public SetMultimap build() {
         return this.build();
      }

      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class SetMultimapBuilder extends MultimapBuilder {
      SetMultimapBuilder() {
         super(null);
      }

      public abstract SetMultimap build();

      public SetMultimap build(Multimap var1) {
         return (SetMultimap)super.build(var1);
      }

      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class ListMultimapBuilder extends MultimapBuilder {
      ListMultimapBuilder() {
         super(null);
      }

      public abstract ListMultimap build();

      public ListMultimap build(Multimap var1) {
         return (ListMultimap)super.build(var1);
      }

      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class MultimapBuilderWithKeys {
      private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;

      MultimapBuilderWithKeys() {
      }

      abstract Map createMap();

      public MultimapBuilder.ListMultimapBuilder arrayListValues() {
         return this.arrayListValues(2);
      }

      public MultimapBuilder.ListMultimapBuilder arrayListValues(int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.ListMultimapBuilder(this, var1) {
            final int val$expectedValuesPerKey;
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
               this.val$expectedValuesPerKey = var2;
            }

            public ListMultimap build() {
               return Multimaps.newListMultimap(this.this$0.createMap(), new MultimapBuilder.ArrayListSupplier(this.val$expectedValuesPerKey));
            }

            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.ListMultimapBuilder linkedListValues() {
         return new MultimapBuilder.ListMultimapBuilder(this) {
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
            }

            public ListMultimap build() {
               return Multimaps.newListMultimap(this.this$0.createMap(), MultimapBuilder.LinkedListSupplier.instance());
            }

            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SetMultimapBuilder hashSetValues() {
         return this.hashSetValues(2);
      }

      public MultimapBuilder.SetMultimapBuilder hashSetValues(int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.SetMultimapBuilder(this, var1) {
            final int val$expectedValuesPerKey;
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
               this.val$expectedValuesPerKey = var2;
            }

            public SetMultimap build() {
               return Multimaps.newSetMultimap(this.this$0.createMap(), new MultimapBuilder.HashSetSupplier(this.val$expectedValuesPerKey));
            }

            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SetMultimapBuilder linkedHashSetValues() {
         return this.linkedHashSetValues(2);
      }

      public MultimapBuilder.SetMultimapBuilder linkedHashSetValues(int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.SetMultimapBuilder(this, var1) {
            final int val$expectedValuesPerKey;
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
               this.val$expectedValuesPerKey = var2;
            }

            public SetMultimap build() {
               return Multimaps.newSetMultimap(this.this$0.createMap(), new MultimapBuilder.LinkedHashSetSupplier(this.val$expectedValuesPerKey));
            }

            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SortedSetMultimapBuilder treeSetValues() {
         return this.treeSetValues(Ordering.natural());
      }

      public MultimapBuilder.SortedSetMultimapBuilder treeSetValues(Comparator var1) {
         Preconditions.checkNotNull(var1, "comparator");
         return new MultimapBuilder.SortedSetMultimapBuilder(this, var1) {
            final Comparator val$comparator;
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
               this.val$comparator = var2;
            }

            public SortedSetMultimap build() {
               return Multimaps.newSortedSetMultimap(this.this$0.createMap(), new MultimapBuilder.TreeSetSupplier(this.val$comparator));
            }

            public SetMultimap build() {
               return this.build();
            }

            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SetMultimapBuilder enumSetValues(Class var1) {
         Preconditions.checkNotNull(var1, "valueClass");
         return new MultimapBuilder.SetMultimapBuilder(this, var1) {
            final Class val$valueClass;
            final MultimapBuilder.MultimapBuilderWithKeys this$0;

            {
               this.this$0 = var1;
               this.val$valueClass = var2;
            }

            public SetMultimap build() {
               MultimapBuilder.EnumSetSupplier var1 = new MultimapBuilder.EnumSetSupplier(this.val$valueClass);
               return Multimaps.newSetMultimap(this.this$0.createMap(), var1);
            }

            public Multimap build() {
               return this.build();
            }
         };
      }
   }

   private static final class EnumSetSupplier implements Supplier, Serializable {
      private final Class clazz;

      EnumSetSupplier(Class var1) {
         this.clazz = (Class)Preconditions.checkNotNull(var1);
      }

      public Set get() {
         return EnumSet.noneOf(this.clazz);
      }

      public Object get() {
         return this.get();
      }
   }

   private static final class TreeSetSupplier implements Supplier, Serializable {
      private final Comparator comparator;

      TreeSetSupplier(Comparator var1) {
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public SortedSet get() {
         return new TreeSet(this.comparator);
      }

      public Object get() {
         return this.get();
      }
   }

   private static final class LinkedHashSetSupplier implements Supplier, Serializable {
      private final int expectedValuesPerKey;

      LinkedHashSetSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public Set get() {
         return new LinkedHashSet(this.expectedValuesPerKey);
      }

      public Object get() {
         return this.get();
      }
   }

   private static final class HashSetSupplier implements Supplier, Serializable {
      private final int expectedValuesPerKey;

      HashSetSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public Set get() {
         return new HashSet(this.expectedValuesPerKey);
      }

      public Object get() {
         return this.get();
      }
   }

   private static enum LinkedListSupplier implements Supplier {
      INSTANCE;

      private static final MultimapBuilder.LinkedListSupplier[] $VALUES = new MultimapBuilder.LinkedListSupplier[]{INSTANCE};

      public static Supplier instance() {
         MultimapBuilder.LinkedListSupplier var0 = INSTANCE;
         return var0;
      }

      public List get() {
         return new LinkedList();
      }

      public Object get() {
         return this.get();
      }
   }

   private static final class ArrayListSupplier implements Supplier, Serializable {
      private final int expectedValuesPerKey;

      ArrayListSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public List get() {
         return new ArrayList(this.expectedValuesPerKey);
      }

      public Object get() {
         return this.get();
      }
   }
}
