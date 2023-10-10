package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Multimaps {
   private Multimaps() {
   }

   public static Multimap newMultimap(Map var0, Supplier var1) {
      return new Multimaps.CustomMultimap(var0, var1);
   }

   public static ListMultimap newListMultimap(Map var0, Supplier var1) {
      return new Multimaps.CustomListMultimap(var0, var1);
   }

   public static SetMultimap newSetMultimap(Map var0, Supplier var1) {
      return new Multimaps.CustomSetMultimap(var0, var1);
   }

   public static SortedSetMultimap newSortedSetMultimap(Map var0, Supplier var1) {
      return new Multimaps.CustomSortedSetMultimap(var0, var1);
   }

   public static Multimap invertFrom(Multimap var0, Multimap var1) {
      Preconditions.checkNotNull(var1);
      Iterator var2 = var0.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getValue(), var3.getKey());
      }

      return var1;
   }

   public static Multimap synchronizedMultimap(Multimap var0) {
      return Synchronized.multimap(var0, (Object)null);
   }

   public static Multimap unmodifiableMultimap(Multimap var0) {
      return (Multimap)(!(var0 instanceof Multimaps.UnmodifiableMultimap) && !(var0 instanceof ImmutableMultimap) ? new Multimaps.UnmodifiableMultimap(var0) : var0);
   }

   /** @deprecated */
   @Deprecated
   public static Multimap unmodifiableMultimap(ImmutableMultimap var0) {
      return (Multimap)Preconditions.checkNotNull(var0);
   }

   public static SetMultimap synchronizedSetMultimap(SetMultimap var0) {
      return Synchronized.setMultimap(var0, (Object)null);
   }

   public static SetMultimap unmodifiableSetMultimap(SetMultimap var0) {
      return (SetMultimap)(!(var0 instanceof Multimaps.UnmodifiableSetMultimap) && !(var0 instanceof ImmutableSetMultimap) ? new Multimaps.UnmodifiableSetMultimap(var0) : var0);
   }

   /** @deprecated */
   @Deprecated
   public static SetMultimap unmodifiableSetMultimap(ImmutableSetMultimap var0) {
      return (SetMultimap)Preconditions.checkNotNull(var0);
   }

   public static SortedSetMultimap synchronizedSortedSetMultimap(SortedSetMultimap var0) {
      return Synchronized.sortedSetMultimap(var0, (Object)null);
   }

   public static SortedSetMultimap unmodifiableSortedSetMultimap(SortedSetMultimap var0) {
      return (SortedSetMultimap)(var0 instanceof Multimaps.UnmodifiableSortedSetMultimap ? var0 : new Multimaps.UnmodifiableSortedSetMultimap(var0));
   }

   public static ListMultimap synchronizedListMultimap(ListMultimap var0) {
      return Synchronized.listMultimap(var0, (Object)null);
   }

   public static ListMultimap unmodifiableListMultimap(ListMultimap var0) {
      return (ListMultimap)(!(var0 instanceof Multimaps.UnmodifiableListMultimap) && !(var0 instanceof ImmutableListMultimap) ? new Multimaps.UnmodifiableListMultimap(var0) : var0);
   }

   /** @deprecated */
   @Deprecated
   public static ListMultimap unmodifiableListMultimap(ImmutableListMultimap var0) {
      return (ListMultimap)Preconditions.checkNotNull(var0);
   }

   private static Collection unmodifiableValueCollection(Collection var0) {
      if (var0 instanceof SortedSet) {
         return Collections.unmodifiableSortedSet((SortedSet)var0);
      } else if (var0 instanceof Set) {
         return Collections.unmodifiableSet((Set)var0);
      } else {
         return (Collection)(var0 instanceof List ? Collections.unmodifiableList((List)var0) : Collections.unmodifiableCollection(var0));
      }
   }

   private static Collection unmodifiableEntries(Collection var0) {
      return (Collection)(var0 instanceof Set ? Maps.unmodifiableEntrySet((Set)var0) : new Maps.UnmodifiableEntries(Collections.unmodifiableCollection(var0)));
   }

   @Beta
   public static Map asMap(ListMultimap var0) {
      return var0.asMap();
   }

   @Beta
   public static Map asMap(SetMultimap var0) {
      return var0.asMap();
   }

   @Beta
   public static Map asMap(SortedSetMultimap var0) {
      return var0.asMap();
   }

   @Beta
   public static Map asMap(Multimap var0) {
      return var0.asMap();
   }

   public static SetMultimap forMap(Map var0) {
      return new Multimaps.MapMultimap(var0);
   }

   public static Multimap transformValues(Multimap var0, Function var1) {
      Preconditions.checkNotNull(var1);
      Maps.EntryTransformer var2 = Maps.asEntryTransformer(var1);
      return transformEntries(var0, var2);
   }

   public static Multimap transformEntries(Multimap var0, Maps.EntryTransformer var1) {
      return new Multimaps.TransformedEntriesMultimap(var0, var1);
   }

   public static ListMultimap transformValues(ListMultimap var0, Function var1) {
      Preconditions.checkNotNull(var1);
      Maps.EntryTransformer var2 = Maps.asEntryTransformer(var1);
      return transformEntries(var0, var2);
   }

   public static ListMultimap transformEntries(ListMultimap var0, Maps.EntryTransformer var1) {
      return new Multimaps.TransformedEntriesListMultimap(var0, var1);
   }

   public static ImmutableListMultimap index(Iterable var0, Function var1) {
      return index(var0.iterator(), var1);
   }

   public static ImmutableListMultimap index(Iterator var0, Function var1) {
      Preconditions.checkNotNull(var1);
      ImmutableListMultimap.Builder var2 = ImmutableListMultimap.builder();

      while(var0.hasNext()) {
         Object var3 = var0.next();
         Preconditions.checkNotNull(var3, var0);
         var2.put(var1.apply(var3), var3);
      }

      return var2.build();
   }

   public static Multimap filterKeys(Multimap var0, Predicate var1) {
      if (var0 instanceof SetMultimap) {
         return filterKeys((SetMultimap)var0, var1);
      } else if (var0 instanceof ListMultimap) {
         return filterKeys((ListMultimap)var0, var1);
      } else if (var0 instanceof FilteredKeyMultimap) {
         FilteredKeyMultimap var3 = (FilteredKeyMultimap)var0;
         return new FilteredKeyMultimap(var3.unfiltered, Predicates.and(var3.keyPredicate, var1));
      } else if (var0 instanceof FilteredMultimap) {
         FilteredMultimap var2 = (FilteredMultimap)var0;
         return filterFiltered(var2, Maps.keyPredicateOnEntries(var1));
      } else {
         return new FilteredKeyMultimap(var0, var1);
      }
   }

   public static SetMultimap filterKeys(SetMultimap var0, Predicate var1) {
      if (var0 instanceof FilteredKeySetMultimap) {
         FilteredKeySetMultimap var3 = (FilteredKeySetMultimap)var0;
         return new FilteredKeySetMultimap(var3.unfiltered(), Predicates.and(var3.keyPredicate, var1));
      } else if (var0 instanceof FilteredSetMultimap) {
         FilteredSetMultimap var2 = (FilteredSetMultimap)var0;
         return filterFiltered(var2, Maps.keyPredicateOnEntries(var1));
      } else {
         return new FilteredKeySetMultimap(var0, var1);
      }
   }

   public static ListMultimap filterKeys(ListMultimap var0, Predicate var1) {
      if (var0 instanceof FilteredKeyListMultimap) {
         FilteredKeyListMultimap var2 = (FilteredKeyListMultimap)var0;
         return new FilteredKeyListMultimap(var2.unfiltered(), Predicates.and(var2.keyPredicate, var1));
      } else {
         return new FilteredKeyListMultimap(var0, var1);
      }
   }

   public static Multimap filterValues(Multimap var0, Predicate var1) {
      return filterEntries(var0, Maps.valuePredicateOnEntries(var1));
   }

   public static SetMultimap filterValues(SetMultimap var0, Predicate var1) {
      return filterEntries(var0, Maps.valuePredicateOnEntries(var1));
   }

   public static Multimap filterEntries(Multimap var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      if (var0 instanceof SetMultimap) {
         return filterEntries((SetMultimap)var0, var1);
      } else {
         return (Multimap)(var0 instanceof FilteredMultimap ? filterFiltered((FilteredMultimap)var0, var1) : new FilteredEntryMultimap((Multimap)Preconditions.checkNotNull(var0), var1));
      }
   }

   public static SetMultimap filterEntries(SetMultimap var0, Predicate var1) {
      Preconditions.checkNotNull(var1);
      return (SetMultimap)(var0 instanceof FilteredSetMultimap ? filterFiltered((FilteredSetMultimap)var0, var1) : new FilteredEntrySetMultimap((SetMultimap)Preconditions.checkNotNull(var0), var1));
   }

   private static Multimap filterFiltered(FilteredMultimap var0, Predicate var1) {
      Predicate var2 = Predicates.and(var0.entryPredicate(), var1);
      return new FilteredEntryMultimap(var0.unfiltered(), var2);
   }

   private static SetMultimap filterFiltered(FilteredSetMultimap var0, Predicate var1) {
      Predicate var2 = Predicates.and(var0.entryPredicate(), var1);
      return new FilteredEntrySetMultimap(var0.unfiltered(), var2);
   }

   static boolean equalsImpl(Multimap var0, @Nullable Object var1) {
      if (var1 == var0) {
         return true;
      } else if (var1 instanceof Multimap) {
         Multimap var2 = (Multimap)var1;
         return var0.asMap().equals(var2.asMap());
      } else {
         return false;
      }
   }

   static Collection access$000(Collection var0) {
      return unmodifiableValueCollection(var0);
   }

   static Collection access$100(Collection var0) {
      return unmodifiableEntries(var0);
   }

   static final class AsMap extends Maps.ImprovedAbstractMap {
      private final Multimap multimap;

      AsMap(Multimap var1) {
         this.multimap = (Multimap)Preconditions.checkNotNull(var1);
      }

      public int size() {
         return this.multimap.keySet().size();
      }

      protected Set createEntrySet() {
         return new Multimaps.AsMap.EntrySet(this);
      }

      void removeValuesForKey(Object var1) {
         this.multimap.keySet().remove(var1);
      }

      public Collection get(Object var1) {
         return this.containsKey(var1) ? this.multimap.get(var1) : null;
      }

      public Collection remove(Object var1) {
         return this.containsKey(var1) ? this.multimap.removeAll(var1) : null;
      }

      public Set keySet() {
         return this.multimap.keySet();
      }

      public boolean isEmpty() {
         return this.multimap.isEmpty();
      }

      public boolean containsKey(Object var1) {
         return this.multimap.containsKey(var1);
      }

      public void clear() {
         this.multimap.clear();
      }

      public Object remove(Object var1) {
         return this.remove(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      static Multimap access$200(Multimaps.AsMap var0) {
         return var0.multimap;
      }

      class EntrySet extends Maps.EntrySet {
         final Multimaps.AsMap this$0;

         EntrySet(Multimaps.AsMap var1) {
            this.this$0 = var1;
         }

         Map map() {
            return this.this$0;
         }

         public Iterator iterator() {
            return Maps.asMapEntryIterator(Multimaps.AsMap.access$200(this.this$0).keySet(), new Function(this) {
               final Multimaps.AsMap.EntrySet this$1;

               {
                  this.this$1 = var1;
               }

               public Collection apply(Object var1) {
                  return Multimaps.AsMap.access$200(this.this$1.this$0).get(var1);
               }

               public Object apply(Object var1) {
                  return this.apply(var1);
               }
            });
         }

         public boolean remove(Object var1) {
            if (!this.contains(var1)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               this.this$0.removeValuesForKey(var2.getKey());
               return true;
            }
         }
      }
   }

   abstract static class Entries extends AbstractCollection {
      abstract Multimap multimap();

      public int size() {
         return this.multimap().size();
      }

      public boolean contains(@Nullable Object var1) {
         if (var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            return this.multimap().containsEntry(var2.getKey(), var2.getValue());
         } else {
            return false;
         }
      }

      public boolean remove(@Nullable Object var1) {
         if (var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            return this.multimap().remove(var2.getKey(), var2.getValue());
         } else {
            return false;
         }
      }

      public void clear() {
         this.multimap().clear();
      }
   }

   static class Keys extends AbstractMultiset {
      final Multimap multimap;

      Keys(Multimap var1) {
         this.multimap = var1;
      }

      Iterator entryIterator() {
         return new TransformedIterator(this, this.multimap.asMap().entrySet().iterator()) {
            final Multimaps.Keys this$0;

            {
               this.this$0 = var1;
            }

            Multiset.Entry transform(Entry var1) {
               return new Multisets.AbstractEntry(this, var1) {
                  final Entry val$backingEntry;
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                     this.val$backingEntry = var2;
                  }

                  public Object getElement() {
                     return this.val$backingEntry.getKey();
                  }

                  public int getCount() {
                     return ((Collection)this.val$backingEntry.getValue()).size();
                  }
               };
            }

            Object transform(Object var1) {
               return this.transform((Entry)var1);
            }
         };
      }

      int distinctElements() {
         return this.multimap.asMap().size();
      }

      Set createEntrySet() {
         return new Multimaps.Keys.KeysEntrySet(this);
      }

      public boolean contains(@Nullable Object var1) {
         return this.multimap.containsKey(var1);
      }

      public Iterator iterator() {
         return Maps.keyIterator(this.multimap.entries().iterator());
      }

      public int count(@Nullable Object var1) {
         Collection var2 = (Collection)Maps.safeGet(this.multimap.asMap(), var1);
         return var2 == null ? 0 : var2.size();
      }

      public int remove(@Nullable Object var1, int var2) {
         CollectPreconditions.checkNonnegative(var2, "occurrences");
         if (var2 == 0) {
            return this.count(var1);
         } else {
            Collection var3 = (Collection)Maps.safeGet(this.multimap.asMap(), var1);
            if (var3 == null) {
               return 0;
            } else {
               int var4 = var3.size();
               if (var2 >= var4) {
                  var3.clear();
               } else {
                  Iterator var5 = var3.iterator();

                  for(int var6 = 0; var6 < var2; ++var6) {
                     var5.next();
                     var5.remove();
                  }
               }

               return var4;
            }
         }
      }

      public void clear() {
         this.multimap.clear();
      }

      public Set elementSet() {
         return this.multimap.keySet();
      }

      class KeysEntrySet extends Multisets.EntrySet {
         final Multimaps.Keys this$0;

         KeysEntrySet(Multimaps.Keys var1) {
            this.this$0 = var1;
         }

         Multiset multiset() {
            return this.this$0;
         }

         public Iterator iterator() {
            return this.this$0.entryIterator();
         }

         public int size() {
            return this.this$0.distinctElements();
         }

         public boolean isEmpty() {
            return this.this$0.multimap.isEmpty();
         }

         public boolean contains(@Nullable Object var1) {
            if (!(var1 instanceof Multiset.Entry)) {
               return false;
            } else {
               Multiset.Entry var2 = (Multiset.Entry)var1;
               Collection var3 = (Collection)this.this$0.multimap.asMap().get(var2.getElement());
               return var3 != null && var3.size() == var2.getCount();
            }
         }

         public boolean remove(@Nullable Object var1) {
            if (var1 instanceof Multiset.Entry) {
               Multiset.Entry var2 = (Multiset.Entry)var1;
               Collection var3 = (Collection)this.this$0.multimap.asMap().get(var2.getElement());
               if (var3 != null && var3.size() == var2.getCount()) {
                  var3.clear();
                  return true;
               }
            }

            return false;
         }
      }
   }

   private static final class TransformedEntriesListMultimap extends Multimaps.TransformedEntriesMultimap implements ListMultimap {
      TransformedEntriesListMultimap(ListMultimap var1, Maps.EntryTransformer var2) {
         super(var1, var2);
      }

      List transform(Object var1, Collection var2) {
         return Lists.transform((List)var2, Maps.asValueToValueFunction(this.transformer, var1));
      }

      public List get(Object var1) {
         return this.transform(var1, this.fromMultimap.get(var1));
      }

      public List removeAll(Object var1) {
         return this.transform(var1, this.fromMultimap.removeAll(var1));
      }

      public List replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      Collection transform(Object var1, Collection var2) {
         return this.transform(var1, var2);
      }
   }

   private static class TransformedEntriesMultimap extends AbstractMultimap {
      final Multimap fromMultimap;
      final Maps.EntryTransformer transformer;

      TransformedEntriesMultimap(Multimap var1, Maps.EntryTransformer var2) {
         this.fromMultimap = (Multimap)Preconditions.checkNotNull(var1);
         this.transformer = (Maps.EntryTransformer)Preconditions.checkNotNull(var2);
      }

      Collection transform(Object var1, Collection var2) {
         Function var3 = Maps.asValueToValueFunction(this.transformer, var1);
         return (Collection)(var2 instanceof List ? Lists.transform((List)var2, var3) : Collections2.transform(var2, var3));
      }

      Map createAsMap() {
         return Maps.transformEntries(this.fromMultimap.asMap(), new Maps.EntryTransformer(this) {
            final Multimaps.TransformedEntriesMultimap this$0;

            {
               this.this$0 = var1;
            }

            public Collection transformEntry(Object var1, Collection var2) {
               return this.this$0.transform(var1, var2);
            }

            public Object transformEntry(Object var1, Object var2) {
               return this.transformEntry(var1, (Collection)var2);
            }
         });
      }

      public void clear() {
         this.fromMultimap.clear();
      }

      public boolean containsKey(Object var1) {
         return this.fromMultimap.containsKey(var1);
      }

      Iterator entryIterator() {
         return Iterators.transform(this.fromMultimap.entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
      }

      public Collection get(Object var1) {
         return this.transform(var1, this.fromMultimap.get(var1));
      }

      public boolean isEmpty() {
         return this.fromMultimap.isEmpty();
      }

      public Set keySet() {
         return this.fromMultimap.keySet();
      }

      public Multiset keys() {
         return this.fromMultimap.keys();
      }

      public boolean put(Object var1, Object var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Multimap var1) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object var1, Object var2) {
         return this.get(var1).remove(var2);
      }

      public Collection removeAll(Object var1) {
         return this.transform(var1, this.fromMultimap.removeAll(var1));
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.fromMultimap.size();
      }

      Collection createValues() {
         return Collections2.transform(this.fromMultimap.entries(), Maps.asEntryToValueFunction(this.transformer));
      }
   }

   private static class MapMultimap extends AbstractMultimap implements SetMultimap, Serializable {
      final Map map;
      private static final long serialVersionUID = 7845222491160860175L;

      MapMultimap(Map var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      public int size() {
         return this.map.size();
      }

      public boolean containsKey(Object var1) {
         return this.map.containsKey(var1);
      }

      public boolean containsValue(Object var1) {
         return this.map.containsValue(var1);
      }

      public boolean containsEntry(Object var1, Object var2) {
         return this.map.entrySet().contains(Maps.immutableEntry(var1, var2));
      }

      public Set get(Object var1) {
         return new Sets.ImprovedAbstractSet(this, var1) {
            final Object val$key;
            final Multimaps.MapMultimap this$0;

            {
               this.this$0 = var1;
               this.val$key = var2;
            }

            public Iterator iterator() {
               return new Iterator(this) {
                  int i;
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                  }

                  public Object next() {
                     if (this == false) {
                        throw new NoSuchElementException();
                     } else {
                        ++this.i;
                        return this.this$1.this$0.map.get(this.this$1.val$key);
                     }
                  }

                  public void remove() {
                     CollectPreconditions.checkRemove(this.i == 1);
                     this.i = -1;
                     this.this$1.this$0.map.remove(this.this$1.val$key);
                  }
               };
            }

            public int size() {
               return this.this$0.map.containsKey(this.val$key) ? 1 : 0;
            }
         };
      }

      public boolean put(Object var1, Object var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Multimap var1) {
         throw new UnsupportedOperationException();
      }

      public Set replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object var1, Object var2) {
         return this.map.entrySet().remove(Maps.immutableEntry(var1, var2));
      }

      public Set removeAll(Object var1) {
         HashSet var2 = new HashSet(2);
         if (!this.map.containsKey(var1)) {
            return var2;
         } else {
            var2.add(this.map.remove(var1));
            return var2;
         }
      }

      public void clear() {
         this.map.clear();
      }

      public Set keySet() {
         return this.map.keySet();
      }

      public Collection values() {
         return this.map.values();
      }

      public Set entries() {
         return this.map.entrySet();
      }

      Iterator entryIterator() {
         return this.map.entrySet().iterator();
      }

      Map createAsMap() {
         return new Multimaps.AsMap(this);
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public Collection entries() {
         return this.entries();
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }
   }

   private static class UnmodifiableSortedSetMultimap extends Multimaps.UnmodifiableSetMultimap implements SortedSetMultimap {
      private static final long serialVersionUID = 0L;

      UnmodifiableSortedSetMultimap(SortedSetMultimap var1) {
         super(var1);
      }

      public SortedSetMultimap delegate() {
         return (SortedSetMultimap)super.delegate();
      }

      public SortedSet get(Object var1) {
         return Collections.unmodifiableSortedSet(this.delegate().get(var1));
      }

      public SortedSet removeAll(Object var1) {
         throw new UnsupportedOperationException();
      }

      public SortedSet replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public Comparator valueComparator() {
         return this.delegate().valueComparator();
      }

      public Set replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Set removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Set get(Object var1) {
         return this.get(var1);
      }

      public SetMultimap delegate() {
         return this.delegate();
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Multimap delegate() {
         return this.delegate();
      }

      public Object delegate() {
         return this.delegate();
      }
   }

   private static class UnmodifiableSetMultimap extends Multimaps.UnmodifiableMultimap implements SetMultimap {
      private static final long serialVersionUID = 0L;

      UnmodifiableSetMultimap(SetMultimap var1) {
         super(var1);
      }

      public SetMultimap delegate() {
         return (SetMultimap)super.delegate();
      }

      public Set get(Object var1) {
         return Collections.unmodifiableSet(this.delegate().get(var1));
      }

      public Set entries() {
         return Maps.unmodifiableEntrySet(this.delegate().entries());
      }

      public Set removeAll(Object var1) {
         throw new UnsupportedOperationException();
      }

      public Set replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection entries() {
         return this.entries();
      }

      public Multimap delegate() {
         return this.delegate();
      }

      public Object delegate() {
         return this.delegate();
      }
   }

   private static class UnmodifiableListMultimap extends Multimaps.UnmodifiableMultimap implements ListMultimap {
      private static final long serialVersionUID = 0L;

      UnmodifiableListMultimap(ListMultimap var1) {
         super(var1);
      }

      public ListMultimap delegate() {
         return (ListMultimap)super.delegate();
      }

      public List get(Object var1) {
         return Collections.unmodifiableList(this.delegate().get(var1));
      }

      public List removeAll(Object var1) {
         throw new UnsupportedOperationException();
      }

      public List replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Multimap delegate() {
         return this.delegate();
      }

      public Object delegate() {
         return this.delegate();
      }
   }

   private static class UnmodifiableMultimap extends ForwardingMultimap implements Serializable {
      final Multimap delegate;
      transient Collection entries;
      transient Multiset keys;
      transient Set keySet;
      transient Collection values;
      transient Map map;
      private static final long serialVersionUID = 0L;

      UnmodifiableMultimap(Multimap var1) {
         this.delegate = (Multimap)Preconditions.checkNotNull(var1);
      }

      protected Multimap delegate() {
         return this.delegate;
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public Map asMap() {
         Map var1 = this.map;
         if (var1 == null) {
            var1 = this.map = Collections.unmodifiableMap(Maps.transformValues(this.delegate.asMap(), new Function(this) {
               final Multimaps.UnmodifiableMultimap this$0;

               {
                  this.this$0 = var1;
               }

               public Collection apply(Collection var1) {
                  return Multimaps.access$000(var1);
               }

               public Object apply(Object var1) {
                  return this.apply((Collection)var1);
               }
            }));
         }

         return var1;
      }

      public Collection entries() {
         Collection var1 = this.entries;
         if (var1 == null) {
            this.entries = var1 = Multimaps.access$100(this.delegate.entries());
         }

         return var1;
      }

      public Collection get(Object var1) {
         return Multimaps.access$000(this.delegate.get(var1));
      }

      public Multiset keys() {
         Multiset var1 = this.keys;
         if (var1 == null) {
            this.keys = var1 = Multisets.unmodifiableMultiset(this.delegate.keys());
         }

         return var1;
      }

      public Set keySet() {
         Set var1 = this.keySet;
         if (var1 == null) {
            this.keySet = var1 = Collections.unmodifiableSet(this.delegate.keySet());
         }

         return var1;
      }

      public boolean put(Object var1, Object var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public boolean putAll(Multimap var1) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object var1, Object var2) {
         throw new UnsupportedOperationException();
      }

      public Collection removeAll(Object var1) {
         throw new UnsupportedOperationException();
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         throw new UnsupportedOperationException();
      }

      public Collection values() {
         Collection var1 = this.values;
         if (var1 == null) {
            this.values = var1 = Collections.unmodifiableCollection(this.delegate.values());
         }

         return var1;
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class CustomSortedSetMultimap extends AbstractSortedSetMultimap {
      transient Supplier factory;
      transient Comparator valueComparator;
      @GwtIncompatible("not needed in emulated source")
      private static final long serialVersionUID = 0L;

      CustomSortedSetMultimap(Map var1, Supplier var2) {
         super(var1);
         this.factory = (Supplier)Preconditions.checkNotNull(var2);
         this.valueComparator = ((SortedSet)var2.get()).comparator();
      }

      protected SortedSet createCollection() {
         return (SortedSet)this.factory.get();
      }

      public Comparator valueComparator() {
         return this.valueComparator;
      }

      @GwtIncompatible("java.io.ObjectOutputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.factory);
         var1.writeObject(this.backingMap());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.factory = (Supplier)var1.readObject();
         this.valueComparator = ((SortedSet)this.factory.get()).comparator();
         Map var2 = (Map)var1.readObject();
         this.setMap(var2);
      }

      protected Set createCollection() {
         return this.createCollection();
      }

      protected Collection createCollection() {
         return this.createCollection();
      }
   }

   private static class CustomSetMultimap extends AbstractSetMultimap {
      transient Supplier factory;
      @GwtIncompatible("not needed in emulated source")
      private static final long serialVersionUID = 0L;

      CustomSetMultimap(Map var1, Supplier var2) {
         super(var1);
         this.factory = (Supplier)Preconditions.checkNotNull(var2);
      }

      protected Set createCollection() {
         return (Set)this.factory.get();
      }

      @GwtIncompatible("java.io.ObjectOutputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.factory);
         var1.writeObject(this.backingMap());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.factory = (Supplier)var1.readObject();
         Map var2 = (Map)var1.readObject();
         this.setMap(var2);
      }

      protected Collection createCollection() {
         return this.createCollection();
      }
   }

   private static class CustomListMultimap extends AbstractListMultimap {
      transient Supplier factory;
      @GwtIncompatible("java serialization not supported")
      private static final long serialVersionUID = 0L;

      CustomListMultimap(Map var1, Supplier var2) {
         super(var1);
         this.factory = (Supplier)Preconditions.checkNotNull(var2);
      }

      protected List createCollection() {
         return (List)this.factory.get();
      }

      @GwtIncompatible("java.io.ObjectOutputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.factory);
         var1.writeObject(this.backingMap());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.factory = (Supplier)var1.readObject();
         Map var2 = (Map)var1.readObject();
         this.setMap(var2);
      }

      protected Collection createCollection() {
         return this.createCollection();
      }
   }

   private static class CustomMultimap extends AbstractMapBasedMultimap {
      transient Supplier factory;
      @GwtIncompatible("java serialization not supported")
      private static final long serialVersionUID = 0L;

      CustomMultimap(Map var1, Supplier var2) {
         super(var1);
         this.factory = (Supplier)Preconditions.checkNotNull(var2);
      }

      protected Collection createCollection() {
         return (Collection)this.factory.get();
      }

      @GwtIncompatible("java.io.ObjectOutputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.factory);
         var1.writeObject(this.backingMap());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.factory = (Supplier)var1.readObject();
         Map var2 = (Map)var1.readObject();
         this.setMap(var2);
      }
   }
}
