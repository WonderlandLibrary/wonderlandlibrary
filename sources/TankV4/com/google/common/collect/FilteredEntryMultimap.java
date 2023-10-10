package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredEntryMultimap extends AbstractMultimap implements FilteredMultimap {
   final Multimap unfiltered;
   final Predicate predicate;

   FilteredEntryMultimap(Multimap var1, Predicate var2) {
      this.unfiltered = (Multimap)Preconditions.checkNotNull(var1);
      this.predicate = (Predicate)Preconditions.checkNotNull(var2);
   }

   public Multimap unfiltered() {
      return this.unfiltered;
   }

   public Predicate entryPredicate() {
      return this.predicate;
   }

   public int size() {
      return this.entries().size();
   }

   private boolean satisfies(Object var1, Object var2) {
      return this.predicate.apply(Maps.immutableEntry(var1, var2));
   }

   static Collection filterCollection(Collection var0, Predicate var1) {
      return (Collection)(var0 instanceof Set ? Sets.filter((Set)var0, var1) : Collections2.filter(var0, var1));
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.asMap().get(var1) != null;
   }

   public Collection removeAll(@Nullable Object var1) {
      return (Collection)Objects.firstNonNull(this.asMap().remove(var1), this.unmodifiableEmptyCollection());
   }

   Collection unmodifiableEmptyCollection() {
      return (Collection)(this.unfiltered instanceof SetMultimap ? Collections.emptySet() : Collections.emptyList());
   }

   public void clear() {
      this.entries().clear();
   }

   public Collection get(Object var1) {
      return filterCollection(this.unfiltered.get(var1), new FilteredEntryMultimap.ValuePredicate(this, var1));
   }

   Collection createEntries() {
      return filterCollection(this.unfiltered.entries(), this.predicate);
   }

   Collection createValues() {
      return new FilteredMultimapValues(this);
   }

   Iterator entryIterator() {
      throw new AssertionError("should never be called");
   }

   Map createAsMap() {
      return new FilteredEntryMultimap.AsMap(this);
   }

   public Set keySet() {
      return this.asMap().keySet();
   }

   boolean removeEntriesIf(Predicate var1) {
      Iterator var2 = this.unfiltered.asMap().entrySet().iterator();
      boolean var3 = false;

      while(var2.hasNext()) {
         Entry var4 = (Entry)var2.next();
         Object var5 = var4.getKey();
         Collection var6 = filterCollection((Collection)var4.getValue(), new FilteredEntryMultimap.ValuePredicate(this, var5));
         if (!var6.isEmpty() && var1.apply(Maps.immutableEntry(var5, var6))) {
            if (var6.size() == ((Collection)var4.getValue()).size()) {
               var2.remove();
            } else {
               var6.clear();
            }

            var3 = true;
         }
      }

      return var3;
   }

   Multiset createKeys() {
      return new FilteredEntryMultimap.Keys(this);
   }

   static boolean access$000(FilteredEntryMultimap var0, Object var1, Object var2) {
      return var0.satisfies(var1, var2);
   }

   class Keys extends Multimaps.Keys {
      final FilteredEntryMultimap this$0;

      Keys(FilteredEntryMultimap var1) {
         super(var1);
         this.this$0 = var1;
      }

      public int remove(@Nullable Object var1, int var2) {
         CollectPreconditions.checkNonnegative(var2, "occurrences");
         if (var2 == 0) {
            return this.count(var1);
         } else {
            Collection var3 = (Collection)this.this$0.unfiltered.asMap().get(var1);
            if (var3 == null) {
               return 0;
            } else {
               Object var4 = var1;
               int var5 = 0;
               Iterator var6 = var3.iterator();

               while(var6.hasNext()) {
                  Object var7 = var6.next();
                  if (FilteredEntryMultimap.access$000(this.this$0, var4, var7)) {
                     ++var5;
                     if (var5 <= var2) {
                        var6.remove();
                     }
                  }
               }

               return var5;
            }
         }
      }

      public Set entrySet() {
         return new Multisets.EntrySet(this) {
            final FilteredEntryMultimap.Keys this$1;

            {
               this.this$1 = var1;
            }

            Multiset multiset() {
               return this.this$1;
            }

            public Iterator iterator() {
               return this.this$1.entryIterator();
            }

            public int size() {
               return this.this$1.this$0.keySet().size();
            }

            private boolean removeEntriesIf(Predicate var1) {
               return this.this$1.this$0.removeEntriesIf(new Predicate(this, var1) {
                  final Predicate val$predicate;
                  final <undefinedtype> this$2;

                  {
                     this.this$2 = var1;
                     this.val$predicate = var2;
                  }

                  public boolean apply(Entry var1) {
                     return this.val$predicate.apply(Multisets.immutableEntry(var1.getKey(), ((Collection)var1.getValue()).size()));
                  }

                  public boolean apply(Object var1) {
                     return this.apply((Entry)var1);
                  }
               });
            }

            public boolean removeAll(Collection var1) {
               return this.removeEntriesIf(Predicates.in(var1));
            }

            public boolean retainAll(Collection var1) {
               return this.removeEntriesIf(Predicates.not(Predicates.in(var1)));
            }
         };
      }
   }

   class AsMap extends Maps.ImprovedAbstractMap {
      final FilteredEntryMultimap this$0;

      AsMap(FilteredEntryMultimap var1) {
         this.this$0 = var1;
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      public void clear() {
         this.this$0.clear();
      }

      public Collection get(@Nullable Object var1) {
         Collection var2 = (Collection)this.this$0.unfiltered.asMap().get(var1);
         if (var2 == null) {
            return null;
         } else {
            var2 = FilteredEntryMultimap.filterCollection(var2, this.this$0.new ValuePredicate(this.this$0, var1));
            return var2.isEmpty() ? null : var2;
         }
      }

      public Collection remove(@Nullable Object var1) {
         Collection var2 = (Collection)this.this$0.unfiltered.asMap().get(var1);
         if (var2 == null) {
            return null;
         } else {
            Object var3 = var1;
            ArrayList var4 = Lists.newArrayList();
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               if (FilteredEntryMultimap.access$000(this.this$0, var3, var6)) {
                  var5.remove();
                  var4.add(var6);
               }
            }

            if (var4.isEmpty()) {
               return null;
            } else if (this.this$0.unfiltered instanceof SetMultimap) {
               return Collections.unmodifiableSet(Sets.newLinkedHashSet(var4));
            } else {
               return Collections.unmodifiableList(var4);
            }
         }
      }

      Set createKeySet() {
         return new Maps.KeySet(this, this) {
            final FilteredEntryMultimap.AsMap this$1;

            {
               this.this$1 = var1;
            }

            public boolean removeAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(var1)));
            }

            public boolean retainAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(var1))));
            }

            public boolean remove(@Nullable Object var1) {
               return this.this$1.remove(var1) != null;
            }
         };
      }

      Set createEntrySet() {
         return new Maps.EntrySet(this) {
            final FilteredEntryMultimap.AsMap this$1;

            {
               this.this$1 = var1;
            }

            Map map() {
               return this.this$1;
            }

            public Iterator iterator() {
               return new AbstractIterator(this) {
                  final Iterator backingIterator;
                  final <undefinedtype> this$2;

                  {
                     this.this$2 = var1;
                     this.backingIterator = this.this$2.this$1.this$0.unfiltered.asMap().entrySet().iterator();
                  }

                  protected Entry computeNext() {
                     while(true) {
                        if (this.backingIterator.hasNext()) {
                           Entry var1 = (Entry)this.backingIterator.next();
                           Object var2 = var1.getKey();
                           Collection var3 = FilteredEntryMultimap.filterCollection((Collection)var1.getValue(), this.this$2.this$1.this$0.new ValuePredicate(this.this$2.this$1.this$0, var2));
                           if (var3.isEmpty()) {
                              continue;
                           }

                           return Maps.immutableEntry(var2, var3);
                        }

                        return (Entry)this.endOfData();
                     }
                  }

                  protected Object computeNext() {
                     return this.computeNext();
                  }
               };
            }

            public boolean removeAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Predicates.in(var1));
            }

            public boolean retainAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Predicates.not(Predicates.in(var1)));
            }

            public int size() {
               return Iterators.size(this.iterator());
            }
         };
      }

      Collection createValues() {
         return new Maps.Values(this, this) {
            final FilteredEntryMultimap.AsMap this$1;

            {
               this.this$1 = var1;
            }

            public boolean remove(@Nullable Object var1) {
               if (var1 instanceof Collection) {
                  Collection var2 = (Collection)var1;
                  Iterator var3 = this.this$1.this$0.unfiltered.asMap().entrySet().iterator();

                  while(var3.hasNext()) {
                     Entry var4 = (Entry)var3.next();
                     Object var5 = var4.getKey();
                     Collection var6 = FilteredEntryMultimap.filterCollection((Collection)var4.getValue(), this.this$1.this$0.new ValuePredicate(this.this$1.this$0, var5));
                     if (!var6.isEmpty() && var2.equals(var6)) {
                        if (var6.size() == ((Collection)var4.getValue()).size()) {
                           var3.remove();
                        } else {
                           var6.clear();
                        }

                        return true;
                     }
                  }
               }

               return false;
            }

            public boolean removeAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(var1)));
            }

            public boolean retainAll(Collection var1) {
               return this.this$1.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(var1))));
            }
         };
      }

      public Object remove(Object var1) {
         return this.remove(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }
   }

   final class ValuePredicate implements Predicate {
      private final Object key;
      final FilteredEntryMultimap this$0;

      ValuePredicate(FilteredEntryMultimap var1, Object var2) {
         this.this$0 = var1;
         this.key = var2;
      }

      public boolean apply(@Nullable Object var1) {
         return FilteredEntryMultimap.access$000(this.this$0, this.key, var1);
      }
   }
}
