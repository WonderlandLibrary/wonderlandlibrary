package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Maps {
   static final Joiner.MapJoiner STANDARD_JOINER;

   private Maps() {
   }

   static Function keyFunction() {
      return Maps.EntryFunction.KEY;
   }

   static Function valueFunction() {
      return Maps.EntryFunction.VALUE;
   }

   static Iterator keyIterator(Iterator var0) {
      return Iterators.transform(var0, keyFunction());
   }

   static Iterator valueIterator(Iterator var0) {
      return Iterators.transform(var0, valueFunction());
   }

   static UnmodifiableIterator valueIterator(UnmodifiableIterator var0) {
      return new UnmodifiableIterator(var0) {
         final UnmodifiableIterator val$entryIterator;

         {
            this.val$entryIterator = var1;
         }

         public boolean hasNext() {
            return this.val$entryIterator.hasNext();
         }

         public Object next() {
            return ((Entry)this.val$entryIterator.next()).getValue();
         }
      };
   }

   @GwtCompatible(
      serializable = true
   )
   @Beta
   public static ImmutableMap immutableEnumMap(Map var0) {
      if (var0 instanceof ImmutableEnumMap) {
         ImmutableEnumMap var3 = (ImmutableEnumMap)var0;
         return var3;
      } else if (var0.isEmpty()) {
         return ImmutableMap.of();
      } else {
         Iterator var1 = var0.entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            Preconditions.checkNotNull(var2.getKey());
            Preconditions.checkNotNull(var2.getValue());
         }

         return ImmutableEnumMap.asImmutable(new EnumMap(var0));
      }
   }

   public static HashMap newHashMap() {
      return new HashMap();
   }

   public static HashMap newHashMapWithExpectedSize(int var0) {
      return new HashMap(capacity(var0));
   }

   static int capacity(int var0) {
      if (var0 < 3) {
         CollectPreconditions.checkNonnegative(var0, "expectedSize");
         return var0 + 1;
      } else {
         return var0 < 1073741824 ? var0 + var0 / 3 : Integer.MAX_VALUE;
      }
   }

   public static HashMap newHashMap(Map var0) {
      return new HashMap(var0);
   }

   public static LinkedHashMap newLinkedHashMap() {
      return new LinkedHashMap();
   }

   public static LinkedHashMap newLinkedHashMap(Map var0) {
      return new LinkedHashMap(var0);
   }

   public static ConcurrentMap newConcurrentMap() {
      return (new MapMaker()).makeMap();
   }

   public static TreeMap newTreeMap() {
      return new TreeMap();
   }

   public static TreeMap newTreeMap(SortedMap var0) {
      return new TreeMap(var0);
   }

   public static TreeMap newTreeMap(@Nullable Comparator var0) {
      return new TreeMap(var0);
   }

   public static EnumMap newEnumMap(Class var0) {
      return new EnumMap((Class)Preconditions.checkNotNull(var0));
   }

   public static EnumMap newEnumMap(Map var0) {
      return new EnumMap(var0);
   }

   public static IdentityHashMap newIdentityHashMap() {
      return new IdentityHashMap();
   }

   public static MapDifference difference(Map var0, Map var1) {
      if (var0 instanceof SortedMap) {
         SortedMap var2 = (SortedMap)var0;
         SortedMapDifference var3 = difference(var2, var1);
         return var3;
      } else {
         return difference(var0, var1, Equivalence.equals());
      }
   }

   @Beta
   public static MapDifference difference(Map var0, Map var1, Equivalence var2) {
      Preconditions.checkNotNull(var2);
      HashMap var3 = newHashMap();
      HashMap var4 = new HashMap(var1);
      HashMap var5 = newHashMap();
      HashMap var6 = newHashMap();
      doDifference(var0, var1, var2, var3, var4, var5, var6);
      return new Maps.MapDifferenceImpl(var3, var4, var5, var6);
   }

   private static void doDifference(Map var0, Map var1, Equivalence var2, Map var3, Map var4, Map var5, Map var6) {
      Iterator var7 = var0.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var8 = (Entry)var7.next();
         Object var9 = var8.getKey();
         Object var10 = var8.getValue();
         if (var1.containsKey(var9)) {
            Object var11 = var4.remove(var9);
            if (var2.equivalent(var10, var11)) {
               var5.put(var9, var10);
            } else {
               var6.put(var9, Maps.ValueDifferenceImpl.create(var10, var11));
            }
         } else {
            var3.put(var9, var10);
         }
      }

   }

   private static Map unmodifiableMap(Map var0) {
      return (Map)(var0 instanceof SortedMap ? Collections.unmodifiableSortedMap((SortedMap)var0) : Collections.unmodifiableMap(var0));
   }

   public static SortedMapDifference difference(SortedMap var0, Map var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Comparator var2 = orNaturalOrder(var0.comparator());
      TreeMap var3 = newTreeMap(var2);
      TreeMap var4 = newTreeMap(var2);
      var4.putAll(var1);
      TreeMap var5 = newTreeMap(var2);
      TreeMap var6 = newTreeMap(var2);
      doDifference(var0, var1, Equivalence.equals(), var3, var4, var5, var6);
      return new Maps.SortedMapDifferenceImpl(var3, var4, var5, var6);
   }

   static Comparator orNaturalOrder(@Nullable Comparator var0) {
      return (Comparator)(var0 != null ? var0 : Ordering.natural());
   }

   @Beta
   public static Map asMap(Set var0, Function var1) {
      return (Map)(var0 instanceof SortedSet ? asMap((SortedSet)var0, var1) : new Maps.AsMapView(var0, var1));
   }

   @Beta
   public static SortedMap asMap(SortedSet var0, Function var1) {
      return Platform.mapsAsMapSortedSet(var0, var1);
   }

   static SortedMap asMapSortedIgnoreNavigable(SortedSet var0, Function var1) {
      return new Maps.SortedAsMapView(var0, var1);
   }

   @Beta
   @GwtIncompatible("NavigableMap")
   public static NavigableMap asMap(NavigableSet var0, Function var1) {
      return new Maps.NavigableAsMapView(var0, var1);
   }

   static Iterator asMapEntryIterator(Set var0, Function var1) {
      return new TransformedIterator(var0.iterator(), var1) {
         final Function val$function;

         {
            this.val$function = var2;
         }

         Entry transform(Object var1) {
            return Maps.immutableEntry(var1, this.val$function.apply(var1));
         }

         Object transform(Object var1) {
            return this.transform(var1);
         }
      };
   }

   private static Set removeOnlySet(Set var0) {
      return new ForwardingSet(var0) {
         final Set val$set;

         {
            this.val$set = var1;
         }

         protected Set delegate() {
            return this.val$set;
         }

         public boolean add(Object var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection var1) {
            throw new UnsupportedOperationException();
         }

         protected Collection delegate() {
            return this.delegate();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static SortedSet removeOnlySortedSet(SortedSet var0) {
      return new ForwardingSortedSet(var0) {
         final SortedSet val$set;

         {
            this.val$set = var1;
         }

         protected SortedSet delegate() {
            return this.val$set;
         }

         public boolean add(Object var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection var1) {
            throw new UnsupportedOperationException();
         }

         public SortedSet headSet(Object var1) {
            return Maps.access$300(super.headSet(var1));
         }

         public SortedSet subSet(Object var1, Object var2) {
            return Maps.access$300(super.subSet(var1, var2));
         }

         public SortedSet tailSet(Object var1) {
            return Maps.access$300(super.tailSet(var1));
         }

         protected Set delegate() {
            return this.delegate();
         }

         protected Collection delegate() {
            return this.delegate();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   @GwtIncompatible("NavigableSet")
   private static NavigableSet removeOnlyNavigableSet(NavigableSet var0) {
      return new ForwardingNavigableSet(var0) {
         final NavigableSet val$set;

         {
            this.val$set = var1;
         }

         protected NavigableSet delegate() {
            return this.val$set;
         }

         public boolean add(Object var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection var1) {
            throw new UnsupportedOperationException();
         }

         public SortedSet headSet(Object var1) {
            return Maps.access$300(super.headSet(var1));
         }

         public SortedSet subSet(Object var1, Object var2) {
            return Maps.access$300(super.subSet(var1, var2));
         }

         public SortedSet tailSet(Object var1) {
            return Maps.access$300(super.tailSet(var1));
         }

         public NavigableSet headSet(Object var1, boolean var2) {
            return Maps.access$400(super.headSet(var1, var2));
         }

         public NavigableSet tailSet(Object var1, boolean var2) {
            return Maps.access$400(super.tailSet(var1, var2));
         }

         public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
            return Maps.access$400(super.subSet(var1, var2, var3, var4));
         }

         public NavigableSet descendingSet() {
            return Maps.access$400(super.descendingSet());
         }

         protected SortedSet delegate() {
            return this.delegate();
         }

         protected Set delegate() {
            return this.delegate();
         }

         protected Collection delegate() {
            return this.delegate();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   @Beta
   public static ImmutableMap toMap(Iterable var0, Function var1) {
      return toMap(var0.iterator(), var1);
   }

   @Beta
   public static ImmutableMap toMap(Iterator var0, Function var1) {
      Preconditions.checkNotNull(var1);
      LinkedHashMap var2 = newLinkedHashMap();

      while(var0.hasNext()) {
         Object var3 = var0.next();
         var2.put(var3, var1.apply(var3));
      }

      return ImmutableMap.copyOf(var2);
   }

   public static ImmutableMap uniqueIndex(Iterable var0, Function var1) {
      return uniqueIndex(var0.iterator(), var1);
   }

   public static ImmutableMap uniqueIndex(Iterator var0, Function var1) {
      Preconditions.checkNotNull(var1);
      ImmutableMap.Builder var2 = ImmutableMap.builder();

      while(var0.hasNext()) {
         Object var3 = var0.next();
         var2.put(var1.apply(var3), var3);
      }

      return var2.build();
   }

   @GwtIncompatible("java.util.Properties")
   public static ImmutableMap fromProperties(Properties var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      Enumeration var2 = var0.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         var1.put(var3, var0.getProperty(var3));
      }

      return var1.build();
   }

   @GwtCompatible(
      serializable = true
   )
   public static Entry immutableEntry(@Nullable Object var0, @Nullable Object var1) {
      return new ImmutableEntry(var0, var1);
   }

   static Set unmodifiableEntrySet(Set var0) {
      return new Maps.UnmodifiableEntrySet(Collections.unmodifiableSet(var0));
   }

   static Entry unmodifiableEntry(Entry var0) {
      Preconditions.checkNotNull(var0);
      return new AbstractMapEntry(var0) {
         final Entry val$entry;

         {
            this.val$entry = var1;
         }

         public Object getKey() {
            return this.val$entry.getKey();
         }

         public Object getValue() {
            return this.val$entry.getValue();
         }
      };
   }

   @Beta
   public static Converter asConverter(BiMap var0) {
      return new Maps.BiMapConverter(var0);
   }

   public static BiMap synchronizedBiMap(BiMap var0) {
      return Synchronized.biMap(var0, (Object)null);
   }

   public static BiMap unmodifiableBiMap(BiMap var0) {
      return new Maps.UnmodifiableBiMap(var0, (BiMap)null);
   }

   public static Map transformValues(Map var0, Function var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   public static SortedMap transformValues(SortedMap var0, Function var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap transformValues(NavigableMap var0, Function var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   public static Map transformEntries(Map var0, Maps.EntryTransformer var1) {
      return (Map)(var0 instanceof SortedMap ? transformEntries((SortedMap)var0, var1) : new Maps.TransformedEntriesMap(var0, var1));
   }

   public static SortedMap transformEntries(SortedMap var0, Maps.EntryTransformer var1) {
      return Platform.mapsTransformEntriesSortedMap(var0, var1);
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap transformEntries(NavigableMap var0, Maps.EntryTransformer var1) {
      return new Maps.TransformedEntriesNavigableMap(var0, var1);
   }

   static SortedMap transformEntriesIgnoreNavigable(SortedMap var0, Maps.EntryTransformer var1) {
      return new Maps.TransformedEntriesSortedMap(var0, var1);
   }

   static Maps.EntryTransformer asEntryTransformer(Function var0) {
      Preconditions.checkNotNull(var0);
      return new Maps.EntryTransformer(var0) {
         final Function val$function;

         {
            this.val$function = var1;
         }

         public Object transformEntry(Object var1, Object var2) {
            return this.val$function.apply(var2);
         }
      };
   }

   static Function asValueToValueFunction(Maps.EntryTransformer var0, Object var1) {
      Preconditions.checkNotNull(var0);
      return new Function(var0, var1) {
         final Maps.EntryTransformer val$transformer;
         final Object val$key;

         {
            this.val$transformer = var1;
            this.val$key = var2;
         }

         public Object apply(@Nullable Object var1) {
            return this.val$transformer.transformEntry(this.val$key, var1);
         }
      };
   }

   static Function asEntryToValueFunction(Maps.EntryTransformer var0) {
      Preconditions.checkNotNull(var0);
      return new Function(var0) {
         final Maps.EntryTransformer val$transformer;

         {
            this.val$transformer = var1;
         }

         public Object apply(Entry var1) {
            return this.val$transformer.transformEntry(var1.getKey(), var1.getValue());
         }

         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };
   }

   static Entry transformEntry(Maps.EntryTransformer var0, Entry var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMapEntry(var1, var0) {
         final Entry val$entry;
         final Maps.EntryTransformer val$transformer;

         {
            this.val$entry = var1;
            this.val$transformer = var2;
         }

         public Object getKey() {
            return this.val$entry.getKey();
         }

         public Object getValue() {
            return this.val$transformer.transformEntry(this.val$entry.getKey(), this.val$entry.getValue());
         }
      };
   }

   static Function asEntryToEntryFunction(Maps.EntryTransformer var0) {
      Preconditions.checkNotNull(var0);
      return new Function(var0) {
         final Maps.EntryTransformer val$transformer;

         {
            this.val$transformer = var1;
         }

         public Entry apply(Entry var1) {
            return Maps.transformEntry(this.val$transformer, var1);
         }

         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };
   }

   static Predicate keyPredicateOnEntries(Predicate var0) {
      return Predicates.compose(var0, keyFunction());
   }

   static Predicate valuePredicateOnEntries(Predicate var0) {
      return Predicates.compose(var0, valueFunction());
   }

   public static Map filterKeys(Map var0, Predicate var1) {
      if (var0 instanceof SortedMap) {
         return filterKeys((SortedMap)var0, var1);
      } else if (var0 instanceof BiMap) {
         return filterKeys((BiMap)var0, var1);
      } else {
         Preconditions.checkNotNull(var1);
         Predicate var2 = keyPredicateOnEntries(var1);
         return (Map)(var0 instanceof Maps.AbstractFilteredMap ? filterFiltered((Maps.AbstractFilteredMap)var0, var2) : new Maps.FilteredKeyMap((Map)Preconditions.checkNotNull(var0), var1, var2));
      }
   }

   public static SortedMap filterKeys(SortedMap var0, Predicate var1) {
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap filterKeys(NavigableMap var0, Predicate var1) {
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   public static BiMap filterKeys(BiMap var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   public static Map filterValues(Map var0, Predicate var1) {
      if (var0 instanceof SortedMap) {
         return filterValues((SortedMap)var0, var1);
      } else {
         return (Map)(var0 instanceof BiMap ? filterValues((BiMap)var0, var1) : filterEntries(var0, valuePredicateOnEntries(var1)));
      }
   }

   public static SortedMap filterValues(SortedMap var0, Predicate var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap filterValues(NavigableMap var0, Predicate var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   public static BiMap filterValues(BiMap var0, Predicate var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   public static Map filterEntries(Map var0, Predicate var1) {
      if (var0 instanceof SortedMap) {
         return filterEntries((SortedMap)var0, var1);
      } else if (var0 instanceof BiMap) {
         return filterEntries((BiMap)var0, var1);
      } else {
         Preconditions.checkNotNull(var1);
         return (Map)(var0 instanceof Maps.AbstractFilteredMap ? filterFiltered((Maps.AbstractFilteredMap)var0, var1) : new Maps.FilteredEntryMap((Map)Preconditions.checkNotNull(var0), var1));
      }
   }

   public static SortedMap filterEntries(SortedMap var0, Predicate var1) {
      return Platform.mapsFilterSortedMap(var0, var1);
   }

   static SortedMap filterSortedIgnoreNavigable(SortedMap var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      return (SortedMap)(var0 instanceof Maps.FilteredEntrySortedMap ? filterFiltered((Maps.FilteredEntrySortedMap)var0, var1) : new Maps.FilteredEntrySortedMap((SortedMap)Preconditions.checkNotNull(var0), var1));
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap filterEntries(NavigableMap var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      return (NavigableMap)(var0 instanceof Maps.FilteredEntryNavigableMap ? filterFiltered((Maps.FilteredEntryNavigableMap)var0, var1) : new Maps.FilteredEntryNavigableMap((NavigableMap)Preconditions.checkNotNull(var0), var1));
   }

   public static BiMap filterEntries(BiMap var0, Predicate var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return (BiMap)(var0 instanceof Maps.FilteredEntryBiMap ? filterFiltered((Maps.FilteredEntryBiMap)var0, var1) : new Maps.FilteredEntryBiMap(var0, var1));
   }

   private static Map filterFiltered(Maps.AbstractFilteredMap var0, Predicate var1) {
      return new Maps.FilteredEntryMap(var0.unfiltered, Predicates.and(var0.predicate, var1));
   }

   private static SortedMap filterFiltered(Maps.FilteredEntrySortedMap var0, Predicate var1) {
      Predicate var2 = Predicates.and(var0.predicate, var1);
      return new Maps.FilteredEntrySortedMap(var0.sortedMap(), var2);
   }

   @GwtIncompatible("NavigableMap")
   private static NavigableMap filterFiltered(Maps.FilteredEntryNavigableMap var0, Predicate var1) {
      Predicate var2 = Predicates.and(Maps.FilteredEntryNavigableMap.access$600(var0), var1);
      return new Maps.FilteredEntryNavigableMap(Maps.FilteredEntryNavigableMap.access$700(var0), var2);
   }

   private static BiMap filterFiltered(Maps.FilteredEntryBiMap var0, Predicate var1) {
      Predicate var2 = Predicates.and(var0.predicate, var1);
      return new Maps.FilteredEntryBiMap(var0.unfiltered(), var2);
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap unmodifiableNavigableMap(NavigableMap var0) {
      Preconditions.checkNotNull(var0);
      return (NavigableMap)(var0 instanceof Maps.UnmodifiableNavigableMap ? var0 : new Maps.UnmodifiableNavigableMap(var0));
   }

   @Nullable
   private static Entry unmodifiableOrNull(@Nullable Entry var0) {
      return var0 == null ? null : unmodifiableEntry(var0);
   }

   @GwtIncompatible("NavigableMap")
   public static NavigableMap synchronizedNavigableMap(NavigableMap var0) {
      return Synchronized.navigableMap(var0);
   }

   static Object safeGet(Map var0, @Nullable Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.get(var1);
      } catch (ClassCastException var3) {
         return null;
      } catch (NullPointerException var4) {
         return null;
      }
   }

   static boolean safeContainsKey(Map var0, Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.containsKey(var1);
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   static Object safeRemove(Map var0, Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.remove(var1);
      } catch (ClassCastException var3) {
         return null;
      } catch (NullPointerException var4) {
         return null;
      }
   }

   static boolean containsKeyImpl(Map var0, @Nullable Object var1) {
      return Iterators.contains(keyIterator(var0.entrySet().iterator()), var1);
   }

   static boolean containsValueImpl(Map var0, @Nullable Object var1) {
      return Iterators.contains(valueIterator(var0.entrySet().iterator()), var1);
   }

   static boolean containsEntryImpl(Collection var0, Object var1) {
      return !(var1 instanceof Entry) ? false : var0.contains(unmodifiableEntry((Entry)var1));
   }

   static boolean removeEntryImpl(Collection var0, Object var1) {
      return !(var1 instanceof Entry) ? false : var0.remove(unmodifiableEntry((Entry)var1));
   }

   static boolean equalsImpl(Map var0, Object var1) {
      if (var0 == var1) {
         return true;
      } else if (var1 instanceof Map) {
         Map var2 = (Map)var1;
         return var0.entrySet().equals(var2.entrySet());
      } else {
         return false;
      }
   }

   static String toStringImpl(Map var0) {
      StringBuilder var1 = Collections2.newStringBuilderForCollection(var0.size()).append('{');
      STANDARD_JOINER.appendTo(var1, var0);
      return var1.append('}').toString();
   }

   static void putAllImpl(Map var0, Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var0.put(var3.getKey(), var3.getValue());
      }

   }

   @Nullable
   static Object keyOrNull(@Nullable Entry var0) {
      return var0 == null ? null : var0.getKey();
   }

   @Nullable
   static Object valueOrNull(@Nullable Entry var0) {
      return var0 == null ? null : var0.getValue();
   }

   static Map access$100(Map var0) {
      return unmodifiableMap(var0);
   }

   static Set access$200(Set var0) {
      return removeOnlySet(var0);
   }

   static SortedSet access$300(SortedSet var0) {
      return removeOnlySortedSet(var0);
   }

   static NavigableSet access$400(NavigableSet var0) {
      return removeOnlyNavigableSet(var0);
   }

   static Entry access$800(Entry var0) {
      return unmodifiableOrNull(var0);
   }

   static {
      STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
   }

   @GwtIncompatible("NavigableMap")
   abstract static class DescendingMap extends ForwardingMap implements NavigableMap {
      private transient Comparator comparator;
      private transient Set entrySet;
      private transient NavigableSet navigableKeySet;

      abstract NavigableMap forward();

      protected final Map delegate() {
         return this.forward();
      }

      public Comparator comparator() {
         Comparator var1 = this.comparator;
         if (var1 == null) {
            Object var2 = this.forward().comparator();
            if (var2 == null) {
               var2 = Ordering.natural();
            }

            var1 = this.comparator = reverse((Comparator)var2);
         }

         return var1;
      }

      private static Ordering reverse(Comparator var0) {
         return Ordering.from(var0).reverse();
      }

      public Object firstKey() {
         return this.forward().lastKey();
      }

      public Object lastKey() {
         return this.forward().firstKey();
      }

      public Entry lowerEntry(Object var1) {
         return this.forward().higherEntry(var1);
      }

      public Object lowerKey(Object var1) {
         return this.forward().higherKey(var1);
      }

      public Entry floorEntry(Object var1) {
         return this.forward().ceilingEntry(var1);
      }

      public Object floorKey(Object var1) {
         return this.forward().ceilingKey(var1);
      }

      public Entry ceilingEntry(Object var1) {
         return this.forward().floorEntry(var1);
      }

      public Object ceilingKey(Object var1) {
         return this.forward().floorKey(var1);
      }

      public Entry higherEntry(Object var1) {
         return this.forward().lowerEntry(var1);
      }

      public Object higherKey(Object var1) {
         return this.forward().lowerKey(var1);
      }

      public Entry firstEntry() {
         return this.forward().lastEntry();
      }

      public Entry lastEntry() {
         return this.forward().firstEntry();
      }

      public Entry pollFirstEntry() {
         return this.forward().pollLastEntry();
      }

      public Entry pollLastEntry() {
         return this.forward().pollFirstEntry();
      }

      public NavigableMap descendingMap() {
         return this.forward();
      }

      public Set entrySet() {
         Set var1 = this.entrySet;
         return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
      }

      abstract Iterator entryIterator();

      Set createEntrySet() {
         return new Maps.EntrySet(this) {
            final Maps.DescendingMap this$0;

            {
               this.this$0 = var1;
            }

            Map map() {
               return this.this$0;
            }

            public Iterator iterator() {
               return this.this$0.entryIterator();
            }
         };
      }

      public Set keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet navigableKeySet() {
         NavigableSet var1 = this.navigableKeySet;
         return var1 == null ? (this.navigableKeySet = new Maps.NavigableKeySet(this)) : var1;
      }

      public NavigableSet descendingKeySet() {
         return this.forward().navigableKeySet();
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.forward().subMap(var3, var4, var1, var2).descendingMap();
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return this.forward().tailMap(var1, var2).descendingMap();
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.forward().headMap(var1, var2).descendingMap();
      }

      public SortedMap subMap(Object var1, Object var2) {
         return this.subMap(var1, true, var2, false);
      }

      public SortedMap headMap(Object var1) {
         return this.headMap(var1, false);
      }

      public SortedMap tailMap(Object var1) {
         return this.tailMap(var1, true);
      }

      public Collection values() {
         return new Maps.Values(this);
      }

      public String toString() {
         return this.standardToString();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   abstract static class EntrySet extends Sets.ImprovedAbstractSet {
      abstract Map map();

      public int size() {
         return this.map().size();
      }

      public void clear() {
         this.map().clear();
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean remove(Object var1) {
         if (var1 != false) {
            Entry var2 = (Entry)var1;
            return this.map().keySet().remove(var2.getKey());
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection var1) {
         try {
            return super.removeAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var3) {
            return Sets.removeAllImpl(this, (Iterator)var1.iterator());
         }
      }

      public boolean retainAll(Collection var1) {
         try {
            return super.retainAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var7) {
            HashSet var3 = Sets.newHashSetWithExpectedSize(var1.size());
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (var5 != false) {
                  Entry var6 = (Entry)var5;
                  var3.add(var6.getKey());
               }
            }

            return this.map().keySet().retainAll(var3);
         }
      }
   }

   static class Values extends AbstractCollection {
      final Map map;

      Values(Map var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      final Map map() {
         return this.map;
      }

      public Iterator iterator() {
         return Maps.valueIterator(this.map().entrySet().iterator());
      }

      public boolean remove(Object var1) {
         try {
            return super.remove(var1);
         } catch (UnsupportedOperationException var5) {
            Iterator var3 = this.map().entrySet().iterator();

            Entry var4;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var4 = (Entry)var3.next();
            } while(!Objects.equal(var1, var4.getValue()));

            this.map().remove(var4.getKey());
            return true;
         }
      }

      public boolean removeAll(Collection var1) {
         try {
            return super.removeAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var6) {
            HashSet var3 = Sets.newHashSet();
            Iterator var4 = this.map().entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               if (var1.contains(var5.getValue())) {
                  var3.add(var5.getKey());
               }
            }

            return this.map().keySet().removeAll(var3);
         }
      }

      public boolean retainAll(Collection var1) {
         try {
            return super.retainAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var6) {
            HashSet var3 = Sets.newHashSet();
            Iterator var4 = this.map().entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               if (var1.contains(var5.getValue())) {
                  var3.add(var5.getKey());
               }
            }

            return this.map().keySet().retainAll(var3);
         }
      }

      public int size() {
         return this.map().size();
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean contains(@Nullable Object var1) {
         return this.map().containsValue(var1);
      }

      public void clear() {
         this.map().clear();
      }
   }

   @GwtIncompatible("NavigableMap")
   static class NavigableKeySet extends Maps.SortedKeySet implements NavigableSet {
      NavigableKeySet(NavigableMap var1) {
         super(var1);
      }

      NavigableMap map() {
         return (NavigableMap)this.map;
      }

      public Object lower(Object var1) {
         return this.map().lowerKey(var1);
      }

      public Object floor(Object var1) {
         return this.map().floorKey(var1);
      }

      public Object ceiling(Object var1) {
         return this.map().ceilingKey(var1);
      }

      public Object higher(Object var1) {
         return this.map().higherKey(var1);
      }

      public Object pollFirst() {
         return Maps.keyOrNull(this.map().pollFirstEntry());
      }

      public Object pollLast() {
         return Maps.keyOrNull(this.map().pollLastEntry());
      }

      public NavigableSet descendingSet() {
         return this.map().descendingKeySet();
      }

      public Iterator descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return this.map().subMap(var1, var2, var3, var4).navigableKeySet();
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return this.map().headMap(var1, var2).navigableKeySet();
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return this.map().tailMap(var1, var2).navigableKeySet();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.subSet(var1, true, var2, false);
      }

      public SortedSet headSet(Object var1) {
         return this.headSet(var1, false);
      }

      public SortedSet tailSet(Object var1) {
         return this.tailSet(var1, true);
      }

      SortedMap map() {
         return this.map();
      }

      Map map() {
         return this.map();
      }
   }

   static class SortedKeySet extends Maps.KeySet implements SortedSet {
      SortedKeySet(SortedMap var1) {
         super(var1);
      }

      SortedMap map() {
         return (SortedMap)super.map();
      }

      public Comparator comparator() {
         return this.map().comparator();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return new Maps.SortedKeySet(this.map().subMap(var1, var2));
      }

      public SortedSet headSet(Object var1) {
         return new Maps.SortedKeySet(this.map().headMap(var1));
      }

      public SortedSet tailSet(Object var1) {
         return new Maps.SortedKeySet(this.map().tailMap(var1));
      }

      public Object first() {
         return this.map().firstKey();
      }

      public Object last() {
         return this.map().lastKey();
      }

      Map map() {
         return this.map();
      }
   }

   static class KeySet extends Sets.ImprovedAbstractSet {
      final Map map;

      KeySet(Map var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      Map map() {
         return this.map;
      }

      public Iterator iterator() {
         return Maps.keyIterator(this.map().entrySet().iterator());
      }

      public int size() {
         return this.map().size();
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean contains(Object var1) {
         return this.map().containsKey(var1);
      }

      public boolean remove(Object var1) {
         if (this.contains(var1)) {
            this.map().remove(var1);
            return true;
         } else {
            return false;
         }
      }

      public void clear() {
         this.map().clear();
      }
   }

   @GwtCompatible
   abstract static class ImprovedAbstractMap extends AbstractMap {
      private transient Set entrySet;
      private transient Set keySet;
      private transient Collection values;

      abstract Set createEntrySet();

      public Set entrySet() {
         Set var1 = this.entrySet;
         return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
      }

      public Set keySet() {
         Set var1 = this.keySet;
         return var1 == null ? (this.keySet = this.createKeySet()) : var1;
      }

      Set createKeySet() {
         return new Maps.KeySet(this);
      }

      public Collection values() {
         Collection var1 = this.values;
         return var1 == null ? (this.values = this.createValues()) : var1;
      }

      Collection createValues() {
         return new Maps.Values(this);
      }
   }

   @GwtIncompatible("NavigableMap")
   static class UnmodifiableNavigableMap extends ForwardingSortedMap implements NavigableMap, Serializable {
      private final NavigableMap delegate;
      private transient Maps.UnmodifiableNavigableMap descendingMap;

      UnmodifiableNavigableMap(NavigableMap var1) {
         this.delegate = var1;
      }

      UnmodifiableNavigableMap(NavigableMap var1, Maps.UnmodifiableNavigableMap var2) {
         this.delegate = var1;
         this.descendingMap = var2;
      }

      protected SortedMap delegate() {
         return Collections.unmodifiableSortedMap(this.delegate);
      }

      public Entry lowerEntry(Object var1) {
         return Maps.access$800(this.delegate.lowerEntry(var1));
      }

      public Object lowerKey(Object var1) {
         return this.delegate.lowerKey(var1);
      }

      public Entry floorEntry(Object var1) {
         return Maps.access$800(this.delegate.floorEntry(var1));
      }

      public Object floorKey(Object var1) {
         return this.delegate.floorKey(var1);
      }

      public Entry ceilingEntry(Object var1) {
         return Maps.access$800(this.delegate.ceilingEntry(var1));
      }

      public Object ceilingKey(Object var1) {
         return this.delegate.ceilingKey(var1);
      }

      public Entry higherEntry(Object var1) {
         return Maps.access$800(this.delegate.higherEntry(var1));
      }

      public Object higherKey(Object var1) {
         return this.delegate.higherKey(var1);
      }

      public Entry firstEntry() {
         return Maps.access$800(this.delegate.firstEntry());
      }

      public Entry lastEntry() {
         return Maps.access$800(this.delegate.lastEntry());
      }

      public final Entry pollFirstEntry() {
         throw new UnsupportedOperationException();
      }

      public final Entry pollLastEntry() {
         throw new UnsupportedOperationException();
      }

      public NavigableMap descendingMap() {
         Maps.UnmodifiableNavigableMap var1 = this.descendingMap;
         return var1 == null ? (this.descendingMap = new Maps.UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : var1;
      }

      public Set keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet navigableKeySet() {
         return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
      }

      public NavigableSet descendingKeySet() {
         return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
      }

      public SortedMap subMap(Object var1, Object var2) {
         return this.subMap(var1, true, var2, false);
      }

      public SortedMap headMap(Object var1) {
         return this.headMap(var1, false);
      }

      public SortedMap tailMap(Object var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return Maps.unmodifiableNavigableMap(this.delegate.subMap(var1, var2, var3, var4));
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return Maps.unmodifiableNavigableMap(this.delegate.headMap(var1, var2));
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return Maps.unmodifiableNavigableMap(this.delegate.tailMap(var1, var2));
      }

      protected Map delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static final class FilteredEntryBiMap extends Maps.FilteredEntryMap implements BiMap {
      private final BiMap inverse;

      private static Predicate inversePredicate(Predicate var0) {
         return new Predicate(var0) {
            final Predicate val$forwardPredicate;

            {
               this.val$forwardPredicate = var1;
            }

            public boolean apply(Entry var1) {
               return this.val$forwardPredicate.apply(Maps.immutableEntry(var1.getValue(), var1.getKey()));
            }

            public boolean apply(Object var1) {
               return this.apply((Entry)var1);
            }
         };
      }

      FilteredEntryBiMap(BiMap var1, Predicate var2) {
         super(var1, var2);
         this.inverse = new Maps.FilteredEntryBiMap(var1.inverse(), inversePredicate(var2), this);
      }

      private FilteredEntryBiMap(BiMap var1, Predicate var2, BiMap var3) {
         super(var1, var2);
         this.inverse = var3;
      }

      BiMap unfiltered() {
         return (BiMap)this.unfiltered;
      }

      public Object forcePut(@Nullable Object var1, @Nullable Object var2) {
         Preconditions.checkArgument(this.apply(var1, var2));
         return this.unfiltered().forcePut(var1, var2);
      }

      public BiMap inverse() {
         return this.inverse;
      }

      public Set values() {
         return this.inverse.keySet();
      }

      public Collection values() {
         return this.values();
      }
   }

   @GwtIncompatible("NavigableMap")
   private static class FilteredEntryNavigableMap extends AbstractNavigableMap {
      private final NavigableMap unfiltered;
      private final Predicate entryPredicate;
      private final Map filteredDelegate;

      FilteredEntryNavigableMap(NavigableMap var1, Predicate var2) {
         this.unfiltered = (NavigableMap)Preconditions.checkNotNull(var1);
         this.entryPredicate = var2;
         this.filteredDelegate = new Maps.FilteredEntryMap(var1, var2);
      }

      public Comparator comparator() {
         return this.unfiltered.comparator();
      }

      public NavigableSet navigableKeySet() {
         return new Maps.NavigableKeySet(this, this) {
            final Maps.FilteredEntryNavigableMap this$0;

            {
               this.this$0 = var1;
            }

            public boolean removeAll(Collection var1) {
               return Iterators.removeIf(Maps.FilteredEntryNavigableMap.access$700(this.this$0).entrySet().iterator(), Predicates.and(Maps.FilteredEntryNavigableMap.access$600(this.this$0), Maps.keyPredicateOnEntries(Predicates.in(var1))));
            }

            public boolean retainAll(Collection var1) {
               return Iterators.removeIf(Maps.FilteredEntryNavigableMap.access$700(this.this$0).entrySet().iterator(), Predicates.and(Maps.FilteredEntryNavigableMap.access$600(this.this$0), Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(var1)))));
            }
         };
      }

      public Collection values() {
         return new Maps.FilteredMapValues(this, this.unfiltered, this.entryPredicate);
      }

      Iterator entryIterator() {
         return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
      }

      Iterator descendingEntryIterator() {
         return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
      }

      public int size() {
         return this.filteredDelegate.size();
      }

      @Nullable
      public Object get(@Nullable Object var1) {
         return this.filteredDelegate.get(var1);
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.filteredDelegate.containsKey(var1);
      }

      public Object put(Object var1, Object var2) {
         return this.filteredDelegate.put(var1, var2);
      }

      public Object remove(@Nullable Object var1) {
         return this.filteredDelegate.remove(var1);
      }

      public void putAll(Map var1) {
         this.filteredDelegate.putAll(var1);
      }

      public void clear() {
         this.filteredDelegate.clear();
      }

      public Set entrySet() {
         return this.filteredDelegate.entrySet();
      }

      public Entry pollFirstEntry() {
         return (Entry)Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
      }

      public Entry pollLastEntry() {
         return (Entry)Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
      }

      public NavigableMap descendingMap() {
         return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return Maps.filterEntries(this.unfiltered.subMap(var1, var2, var3, var4), this.entryPredicate);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return Maps.filterEntries(this.unfiltered.headMap(var1, var2), this.entryPredicate);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return Maps.filterEntries(this.unfiltered.tailMap(var1, var2), this.entryPredicate);
      }

      static Predicate access$600(Maps.FilteredEntryNavigableMap var0) {
         return var0.entryPredicate;
      }

      static NavigableMap access$700(Maps.FilteredEntryNavigableMap var0) {
         return var0.unfiltered;
      }
   }

   private static class FilteredEntrySortedMap extends Maps.FilteredEntryMap implements SortedMap {
      FilteredEntrySortedMap(SortedMap var1, Predicate var2) {
         super(var1, var2);
      }

      SortedMap sortedMap() {
         return (SortedMap)this.unfiltered;
      }

      public SortedSet keySet() {
         return (SortedSet)super.keySet();
      }

      SortedSet createKeySet() {
         return new Maps.FilteredEntrySortedMap.SortedKeySet(this);
      }

      public Comparator comparator() {
         return this.sortedMap().comparator();
      }

      public Object firstKey() {
         return this.keySet().iterator().next();
      }

      public Object lastKey() {
         SortedMap var1 = this.sortedMap();

         while(true) {
            Object var2 = var1.lastKey();
            if (this.apply(var2, this.unfiltered.get(var2))) {
               return var2;
            }

            var1 = this.sortedMap().headMap(var2);
         }
      }

      public SortedMap headMap(Object var1) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().headMap(var1), this.predicate);
      }

      public SortedMap subMap(Object var1, Object var2) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().subMap(var1, var2), this.predicate);
      }

      public SortedMap tailMap(Object var1) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().tailMap(var1), this.predicate);
      }

      Set createKeySet() {
         return this.createKeySet();
      }

      public Set keySet() {
         return this.keySet();
      }

      class SortedKeySet extends Maps.FilteredEntryMap.KeySet implements SortedSet {
         final Maps.FilteredEntrySortedMap this$0;

         SortedKeySet(Maps.FilteredEntrySortedMap var1) {
            super(var1);
            this.this$0 = var1;
         }

         public Comparator comparator() {
            return this.this$0.sortedMap().comparator();
         }

         public SortedSet subSet(Object var1, Object var2) {
            return (SortedSet)this.this$0.subMap(var1, var2).keySet();
         }

         public SortedSet headSet(Object var1) {
            return (SortedSet)this.this$0.headMap(var1).keySet();
         }

         public SortedSet tailSet(Object var1) {
            return (SortedSet)this.this$0.tailMap(var1).keySet();
         }

         public Object first() {
            return this.this$0.firstKey();
         }

         public Object last() {
            return this.this$0.lastKey();
         }
      }
   }

   static class FilteredEntryMap extends Maps.AbstractFilteredMap {
      final Set filteredEntrySet;

      FilteredEntryMap(Map var1, Predicate var2) {
         super(var1, var2);
         this.filteredEntrySet = Sets.filter(var1.entrySet(), this.predicate);
      }

      protected Set createEntrySet() {
         return new Maps.FilteredEntryMap.EntrySet(this);
      }

      Set createKeySet() {
         return new Maps.FilteredEntryMap.KeySet(this);
      }

      class KeySet extends Maps.KeySet {
         final Maps.FilteredEntryMap this$0;

         KeySet(Maps.FilteredEntryMap var1) {
            super(var1);
            this.this$0 = var1;
         }

         public boolean remove(Object var1) {
            if (this.this$0.containsKey(var1)) {
               this.this$0.unfiltered.remove(var1);
               return true;
            } else {
               return false;
            }
         }

         private boolean removeIf(Predicate var1) {
            return Iterables.removeIf(this.this$0.unfiltered.entrySet(), Predicates.and(this.this$0.predicate, Maps.keyPredicateOnEntries(var1)));
         }

         public boolean removeAll(Collection var1) {
            return this.removeIf(Predicates.in(var1));
         }

         public boolean retainAll(Collection var1) {
            return this.removeIf(Predicates.not(Predicates.in(var1)));
         }

         public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
         }

         public Object[] toArray(Object[] var1) {
            return Lists.newArrayList(this.iterator()).toArray(var1);
         }
      }

      private class EntrySet extends ForwardingSet {
         final Maps.FilteredEntryMap this$0;

         private EntrySet(Maps.FilteredEntryMap var1) {
            this.this$0 = var1;
         }

         protected Set delegate() {
            return this.this$0.filteredEntrySet;
         }

         public Iterator iterator() {
            return new TransformedIterator(this, this.this$0.filteredEntrySet.iterator()) {
               final Maps.FilteredEntryMap.EntrySet this$1;

               {
                  this.this$1 = var1;
               }

               Entry transform(Entry var1) {
                  return new ForwardingMapEntry(this, var1) {
                     final Entry val$entry;
                     final <undefinedtype> this$2;

                     {
                        this.this$2 = var1;
                        this.val$entry = var2;
                     }

                     protected Entry delegate() {
                        return this.val$entry;
                     }

                     public Object setValue(Object var1) {
                        Preconditions.checkArgument(this.this$2.this$1.this$0.apply(this.getKey(), var1));
                        return super.setValue(var1);
                     }

                     protected Object delegate() {
                        return this.delegate();
                     }
                  };
               }

               Object transform(Object var1) {
                  return this.transform((Entry)var1);
               }
            };
         }

         protected Collection delegate() {
            return this.delegate();
         }

         protected Object delegate() {
            return this.delegate();
         }

         EntrySet(Maps.FilteredEntryMap var1, Object var2) {
            this(var1);
         }
      }
   }

   private static class FilteredKeyMap extends Maps.AbstractFilteredMap {
      Predicate keyPredicate;

      FilteredKeyMap(Map var1, Predicate var2, Predicate var3) {
         super(var1, var3);
         this.keyPredicate = var2;
      }

      protected Set createEntrySet() {
         return Sets.filter(this.unfiltered.entrySet(), this.predicate);
      }

      Set createKeySet() {
         return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
      }

      public boolean containsKey(Object var1) {
         return this.unfiltered.containsKey(var1) && this.keyPredicate.apply(var1);
      }
   }

   private static final class FilteredMapValues extends Maps.Values {
      Map unfiltered;
      Predicate predicate;

      FilteredMapValues(Map var1, Map var2, Predicate var3) {
         super(var1);
         this.unfiltered = var2;
         this.predicate = var3;
      }

      public boolean remove(Object var1) {
         return Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(var1)))) != null;
      }

      private boolean removeIf(Predicate var1) {
         return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(var1)));
      }

      public boolean removeAll(Collection var1) {
         return this.removeIf(Predicates.in(var1));
      }

      public boolean retainAll(Collection var1) {
         return this.removeIf(Predicates.not(Predicates.in(var1)));
      }

      public Object[] toArray() {
         return Lists.newArrayList(this.iterator()).toArray();
      }

      public Object[] toArray(Object[] var1) {
         return Lists.newArrayList(this.iterator()).toArray(var1);
      }
   }

   private abstract static class AbstractFilteredMap extends Maps.ImprovedAbstractMap {
      final Map unfiltered;
      final Predicate predicate;

      AbstractFilteredMap(Map var1, Predicate var2) {
         this.unfiltered = var1;
         this.predicate = var2;
      }

      boolean apply(@Nullable Object var1, @Nullable Object var2) {
         return this.predicate.apply(Maps.immutableEntry(var1, var2));
      }

      public Object put(Object var1, Object var2) {
         Preconditions.checkArgument(this.apply(var1, var2));
         return this.unfiltered.put(var1, var2);
      }

      public void putAll(Map var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Preconditions.checkArgument(this.apply(var3.getKey(), var3.getValue()));
         }

         this.unfiltered.putAll(var1);
      }

      public Object get(Object var1) {
         Object var2 = this.unfiltered.get(var1);
         return var2 != null && this.apply(var1, var2) ? var2 : null;
      }

      public boolean isEmpty() {
         return this.entrySet().isEmpty();
      }

      public Object remove(Object var1) {
         return var1 != false ? this.unfiltered.remove(var1) : null;
      }

      Collection createValues() {
         return new Maps.FilteredMapValues(this, this.unfiltered, this.predicate);
      }
   }

   @GwtIncompatible("NavigableMap")
   private static class TransformedEntriesNavigableMap extends Maps.TransformedEntriesSortedMap implements NavigableMap {
      TransformedEntriesNavigableMap(NavigableMap var1, Maps.EntryTransformer var2) {
         super(var1, var2);
      }

      public Entry ceilingEntry(Object var1) {
         return this.transformEntry(this.fromMap().ceilingEntry(var1));
      }

      public Object ceilingKey(Object var1) {
         return this.fromMap().ceilingKey(var1);
      }

      public NavigableSet descendingKeySet() {
         return this.fromMap().descendingKeySet();
      }

      public NavigableMap descendingMap() {
         return Maps.transformEntries(this.fromMap().descendingMap(), this.transformer);
      }

      public Entry firstEntry() {
         return this.transformEntry(this.fromMap().firstEntry());
      }

      public Entry floorEntry(Object var1) {
         return this.transformEntry(this.fromMap().floorEntry(var1));
      }

      public Object floorKey(Object var1) {
         return this.fromMap().floorKey(var1);
      }

      public NavigableMap headMap(Object var1) {
         return this.headMap(var1, false);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return Maps.transformEntries(this.fromMap().headMap(var1, var2), this.transformer);
      }

      public Entry higherEntry(Object var1) {
         return this.transformEntry(this.fromMap().higherEntry(var1));
      }

      public Object higherKey(Object var1) {
         return this.fromMap().higherKey(var1);
      }

      public Entry lastEntry() {
         return this.transformEntry(this.fromMap().lastEntry());
      }

      public Entry lowerEntry(Object var1) {
         return this.transformEntry(this.fromMap().lowerEntry(var1));
      }

      public Object lowerKey(Object var1) {
         return this.fromMap().lowerKey(var1);
      }

      public NavigableSet navigableKeySet() {
         return this.fromMap().navigableKeySet();
      }

      public Entry pollFirstEntry() {
         return this.transformEntry(this.fromMap().pollFirstEntry());
      }

      public Entry pollLastEntry() {
         return this.transformEntry(this.fromMap().pollLastEntry());
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return Maps.transformEntries(this.fromMap().subMap(var1, var2, var3, var4), this.transformer);
      }

      public NavigableMap subMap(Object var1, Object var2) {
         return this.subMap(var1, true, var2, false);
      }

      public NavigableMap tailMap(Object var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return Maps.transformEntries(this.fromMap().tailMap(var1, var2), this.transformer);
      }

      @Nullable
      private Entry transformEntry(@Nullable Entry var1) {
         return var1 == null ? null : Maps.transformEntry(this.transformer, var1);
      }

      protected NavigableMap fromMap() {
         return (NavigableMap)super.fromMap();
      }

      public SortedMap tailMap(Object var1) {
         return this.tailMap(var1);
      }

      public SortedMap subMap(Object var1, Object var2) {
         return this.subMap(var1, var2);
      }

      public SortedMap headMap(Object var1) {
         return this.headMap(var1);
      }

      protected SortedMap fromMap() {
         return this.fromMap();
      }
   }

   static class TransformedEntriesSortedMap extends Maps.TransformedEntriesMap implements SortedMap {
      protected SortedMap fromMap() {
         return (SortedMap)this.fromMap;
      }

      TransformedEntriesSortedMap(SortedMap var1, Maps.EntryTransformer var2) {
         super(var1, var2);
      }

      public Comparator comparator() {
         return this.fromMap().comparator();
      }

      public Object firstKey() {
         return this.fromMap().firstKey();
      }

      public SortedMap headMap(Object var1) {
         return Maps.transformEntries(this.fromMap().headMap(var1), this.transformer);
      }

      public Object lastKey() {
         return this.fromMap().lastKey();
      }

      public SortedMap subMap(Object var1, Object var2) {
         return Maps.transformEntries(this.fromMap().subMap(var1, var2), this.transformer);
      }

      public SortedMap tailMap(Object var1) {
         return Maps.transformEntries(this.fromMap().tailMap(var1), this.transformer);
      }
   }

   static class TransformedEntriesMap extends Maps.ImprovedAbstractMap {
      final Map fromMap;
      final Maps.EntryTransformer transformer;

      TransformedEntriesMap(Map var1, Maps.EntryTransformer var2) {
         this.fromMap = (Map)Preconditions.checkNotNull(var1);
         this.transformer = (Maps.EntryTransformer)Preconditions.checkNotNull(var2);
      }

      public int size() {
         return this.fromMap.size();
      }

      public boolean containsKey(Object var1) {
         return this.fromMap.containsKey(var1);
      }

      public Object get(Object var1) {
         Object var2 = this.fromMap.get(var1);
         return var2 == null && !this.fromMap.containsKey(var1) ? null : this.transformer.transformEntry(var1, var2);
      }

      public Object remove(Object var1) {
         return this.fromMap.containsKey(var1) ? this.transformer.transformEntry(var1, this.fromMap.remove(var1)) : null;
      }

      public void clear() {
         this.fromMap.clear();
      }

      public Set keySet() {
         return this.fromMap.keySet();
      }

      protected Set createEntrySet() {
         return new Maps.EntrySet(this) {
            final Maps.TransformedEntriesMap this$0;

            {
               this.this$0 = var1;
            }

            Map map() {
               return this.this$0;
            }

            public Iterator iterator() {
               return Iterators.transform(this.this$0.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(this.this$0.transformer));
            }
         };
      }
   }

   public interface EntryTransformer {
      Object transformEntry(@Nullable Object var1, @Nullable Object var2);
   }

   private static class UnmodifiableBiMap extends ForwardingMap implements BiMap, Serializable {
      final Map unmodifiableMap;
      final BiMap delegate;
      BiMap inverse;
      transient Set values;
      private static final long serialVersionUID = 0L;

      UnmodifiableBiMap(BiMap var1, @Nullable BiMap var2) {
         this.unmodifiableMap = Collections.unmodifiableMap(var1);
         this.delegate = var1;
         this.inverse = var2;
      }

      protected Map delegate() {
         return this.unmodifiableMap;
      }

      public Object forcePut(Object var1, Object var2) {
         throw new UnsupportedOperationException();
      }

      public BiMap inverse() {
         BiMap var1 = this.inverse;
         return var1 == null ? (this.inverse = new Maps.UnmodifiableBiMap(this.delegate.inverse(), this)) : var1;
      }

      public Set values() {
         Set var1 = this.values;
         return var1 == null ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : var1;
      }

      public Collection values() {
         return this.values();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static final class BiMapConverter extends Converter implements Serializable {
      private final BiMap bimap;
      private static final long serialVersionUID = 0L;

      BiMapConverter(BiMap var1) {
         this.bimap = (BiMap)Preconditions.checkNotNull(var1);
      }

      protected Object doForward(Object var1) {
         return convert(this.bimap, var1);
      }

      protected Object doBackward(Object var1) {
         return convert(this.bimap.inverse(), var1);
      }

      private static Object convert(BiMap var0, Object var1) {
         Object var2 = var0.get(var1);
         Preconditions.checkArgument(var2 != null, "No non-null mapping present for input: %s", var1);
         return var2;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Maps.BiMapConverter) {
            Maps.BiMapConverter var2 = (Maps.BiMapConverter)var1;
            return this.bimap.equals(var2.bimap);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.bimap.hashCode();
      }

      public String toString() {
         return "Maps.asConverter(" + this.bimap + ")";
      }
   }

   static class UnmodifiableEntrySet extends Maps.UnmodifiableEntries implements Set {
      UnmodifiableEntrySet(Set var1) {
         super(var1);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   static class UnmodifiableEntries extends ForwardingCollection {
      private final Collection entries;

      UnmodifiableEntries(Collection var1) {
         this.entries = var1;
      }

      protected Collection delegate() {
         return this.entries;
      }

      public Iterator iterator() {
         Iterator var1 = super.iterator();
         return new UnmodifiableIterator(this, var1) {
            final Iterator val$delegate;
            final Maps.UnmodifiableEntries this$0;

            {
               this.this$0 = var1;
               this.val$delegate = var2;
            }

            public boolean hasNext() {
               return this.val$delegate.hasNext();
            }

            public Entry next() {
               return Maps.unmodifiableEntry((Entry)this.val$delegate.next());
            }

            public Object next() {
               return this.next();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableMap")
   private static final class NavigableAsMapView extends AbstractNavigableMap {
      private final NavigableSet set;
      private final Function function;

      NavigableAsMapView(NavigableSet var1, Function var2) {
         this.set = (NavigableSet)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return Maps.asMap(this.set.subSet(var1, var2, var3, var4), this.function);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return Maps.asMap(this.set.headSet(var1, var2), this.function);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return Maps.asMap(this.set.tailSet(var1, var2), this.function);
      }

      public Comparator comparator() {
         return this.set.comparator();
      }

      @Nullable
      public Object get(@Nullable Object var1) {
         return Collections2.safeContains(this.set, var1) ? this.function.apply(var1) : null;
      }

      public void clear() {
         this.set.clear();
      }

      Iterator entryIterator() {
         return Maps.asMapEntryIterator(this.set, this.function);
      }

      Iterator descendingEntryIterator() {
         return this.descendingMap().entrySet().iterator();
      }

      public NavigableSet navigableKeySet() {
         return Maps.access$400(this.set);
      }

      public int size() {
         return this.set.size();
      }

      public NavigableMap descendingMap() {
         return Maps.asMap(this.set.descendingSet(), this.function);
      }
   }

   private static class SortedAsMapView extends Maps.AsMapView implements SortedMap {
      SortedAsMapView(SortedSet var1, Function var2) {
         super(var1, var2);
      }

      SortedSet backingSet() {
         return (SortedSet)super.backingSet();
      }

      public Comparator comparator() {
         return this.backingSet().comparator();
      }

      public Set keySet() {
         return Maps.access$300(this.backingSet());
      }

      public SortedMap subMap(Object var1, Object var2) {
         return Maps.asMap(this.backingSet().subSet(var1, var2), this.function);
      }

      public SortedMap headMap(Object var1) {
         return Maps.asMap(this.backingSet().headSet(var1), this.function);
      }

      public SortedMap tailMap(Object var1) {
         return Maps.asMap(this.backingSet().tailSet(var1), this.function);
      }

      public Object firstKey() {
         return this.backingSet().first();
      }

      public Object lastKey() {
         return this.backingSet().last();
      }

      Set backingSet() {
         return this.backingSet();
      }
   }

   private static class AsMapView extends Maps.ImprovedAbstractMap {
      private final Set set;
      final Function function;

      Set backingSet() {
         return this.set;
      }

      AsMapView(Set var1, Function var2) {
         this.set = (Set)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public Set createKeySet() {
         return Maps.access$200(this.backingSet());
      }

      Collection createValues() {
         return Collections2.transform(this.set, this.function);
      }

      public int size() {
         return this.backingSet().size();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.backingSet().contains(var1);
      }

      public Object get(@Nullable Object var1) {
         return Collections2.safeContains(this.backingSet(), var1) ? this.function.apply(var1) : null;
      }

      public Object remove(@Nullable Object var1) {
         return this.backingSet().remove(var1) ? this.function.apply(var1) : null;
      }

      public void clear() {
         this.backingSet().clear();
      }

      protected Set createEntrySet() {
         return new Maps.EntrySet(this) {
            final Maps.AsMapView this$0;

            {
               this.this$0 = var1;
            }

            Map map() {
               return this.this$0;
            }

            public Iterator iterator() {
               return Maps.asMapEntryIterator(this.this$0.backingSet(), this.this$0.function);
            }
         };
      }
   }

   static class SortedMapDifferenceImpl extends Maps.MapDifferenceImpl implements SortedMapDifference {
      SortedMapDifferenceImpl(SortedMap var1, SortedMap var2, SortedMap var3, SortedMap var4) {
         super(var1, var2, var3, var4);
      }

      public SortedMap entriesDiffering() {
         return (SortedMap)super.entriesDiffering();
      }

      public SortedMap entriesInCommon() {
         return (SortedMap)super.entriesInCommon();
      }

      public SortedMap entriesOnlyOnLeft() {
         return (SortedMap)super.entriesOnlyOnLeft();
      }

      public SortedMap entriesOnlyOnRight() {
         return (SortedMap)super.entriesOnlyOnRight();
      }

      public Map entriesDiffering() {
         return this.entriesDiffering();
      }

      public Map entriesInCommon() {
         return this.entriesInCommon();
      }

      public Map entriesOnlyOnRight() {
         return this.entriesOnlyOnRight();
      }

      public Map entriesOnlyOnLeft() {
         return this.entriesOnlyOnLeft();
      }
   }

   static class ValueDifferenceImpl implements MapDifference.ValueDifference {
      private final Object left;
      private final Object right;

      static MapDifference.ValueDifference create(@Nullable Object var0, @Nullable Object var1) {
         return new Maps.ValueDifferenceImpl(var0, var1);
      }

      private ValueDifferenceImpl(@Nullable Object var1, @Nullable Object var2) {
         this.left = var1;
         this.right = var2;
      }

      public Object leftValue() {
         return this.left;
      }

      public Object rightValue() {
         return this.right;
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof MapDifference.ValueDifference)) {
            return false;
         } else {
            MapDifference.ValueDifference var2 = (MapDifference.ValueDifference)var1;
            return Objects.equal(this.left, var2.leftValue()) && Objects.equal(this.right, var2.rightValue());
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.left, this.right);
      }

      public String toString() {
         return "(" + this.left + ", " + this.right + ")";
      }
   }

   static class MapDifferenceImpl implements MapDifference {
      final Map onlyOnLeft;
      final Map onlyOnRight;
      final Map onBoth;
      final Map differences;

      MapDifferenceImpl(Map var1, Map var2, Map var3, Map var4) {
         this.onlyOnLeft = Maps.access$100(var1);
         this.onlyOnRight = Maps.access$100(var2);
         this.onBoth = Maps.access$100(var3);
         this.differences = Maps.access$100(var4);
      }

      public Map entriesOnlyOnLeft() {
         return this.onlyOnLeft;
      }

      public Map entriesOnlyOnRight() {
         return this.onlyOnRight;
      }

      public Map entriesInCommon() {
         return this.onBoth;
      }

      public Map entriesDiffering() {
         return this.differences;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof MapDifference)) {
            return false;
         } else {
            MapDifference var2 = (MapDifference)var1;
            return this.entriesOnlyOnLeft().equals(var2.entriesOnlyOnLeft()) && this.entriesOnlyOnRight().equals(var2.entriesOnlyOnRight()) && this.entriesInCommon().equals(var2.entriesInCommon()) && this.entriesDiffering().equals(var2.entriesDiffering());
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.entriesOnlyOnLeft(), this.entriesOnlyOnRight(), this.entriesInCommon(), this.entriesDiffering());
      }

      public String toString() {
         if (this != false) {
            return "equal";
         } else {
            StringBuilder var1 = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
               var1.append(": only on left=").append(this.onlyOnLeft);
            }

            if (!this.onlyOnRight.isEmpty()) {
               var1.append(": only on right=").append(this.onlyOnRight);
            }

            if (!this.differences.isEmpty()) {
               var1.append(": value differences=").append(this.differences);
            }

            return var1.toString();
         }
      }
   }

   private static enum EntryFunction implements Function {
      KEY {
         @Nullable
         public Object apply(Entry var1) {
            return var1.getKey();
         }

         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      },
      VALUE {
         @Nullable
         public Object apply(Entry var1) {
            return var1.getValue();
         }

         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };

      private static final Maps.EntryFunction[] $VALUES = new Maps.EntryFunction[]{KEY, VALUE};

      private EntryFunction() {
      }

      EntryFunction(Object var3) {
         this();
      }
   }
}
