package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class MapConstraints {
   private MapConstraints() {
   }

   public static MapConstraint notNull() {
      return MapConstraints.NotNullMapConstraint.INSTANCE;
   }

   public static Map constrainedMap(Map var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedMap(var0, var1);
   }

   public static Multimap constrainedMultimap(Multimap var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedMultimap(var0, var1);
   }

   public static ListMultimap constrainedListMultimap(ListMultimap var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedListMultimap(var0, var1);
   }

   public static SetMultimap constrainedSetMultimap(SetMultimap var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedSetMultimap(var0, var1);
   }

   public static SortedSetMultimap constrainedSortedSetMultimap(SortedSetMultimap var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedSortedSetMultimap(var0, var1);
   }

   private static Entry constrainedEntry(Entry var0, MapConstraint var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new ForwardingMapEntry(var0, var1) {
         final Entry val$entry;
         final MapConstraint val$constraint;

         {
            this.val$entry = var1;
            this.val$constraint = var2;
         }

         protected Entry delegate() {
            return this.val$entry;
         }

         public Object setValue(Object var1) {
            this.val$constraint.checkKeyValue(this.getKey(), var1);
            return this.val$entry.setValue(var1);
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static Entry constrainedAsMapEntry(Entry var0, MapConstraint var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new ForwardingMapEntry(var0, var1) {
         final Entry val$entry;
         final MapConstraint val$constraint;

         {
            this.val$entry = var1;
            this.val$constraint = var2;
         }

         protected Entry delegate() {
            return this.val$entry;
         }

         public Collection getValue() {
            return Constraints.constrainedTypePreservingCollection((Collection)this.val$entry.getValue(), new Constraint(this) {
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
               }

               public Object checkElement(Object var1) {
                  this.this$0.val$constraint.checkKeyValue(this.this$0.getKey(), var1);
                  return var1;
               }
            });
         }

         public Object getValue() {
            return this.getValue();
         }

         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static Set constrainedAsMapEntries(Set var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedAsMapEntries(var0, var1);
   }

   private static Collection constrainedEntries(Collection var0, MapConstraint var1) {
      return (Collection)(var0 instanceof Set ? constrainedEntrySet((Set)var0, var1) : new MapConstraints.ConstrainedEntries(var0, var1));
   }

   private static Set constrainedEntrySet(Set var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedEntrySet(var0, var1);
   }

   public static BiMap constrainedBiMap(BiMap var0, MapConstraint var1) {
      return new MapConstraints.ConstrainedBiMap(var0, (BiMap)null, var1);
   }

   private static Collection checkValues(Object var0, Iterable var1, MapConstraint var2) {
      ArrayList var3 = Lists.newArrayList(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var2.checkKeyValue(var0, var5);
      }

      return var3;
   }

   private static Map checkMap(Map var0, MapConstraint var1) {
      LinkedHashMap var2 = new LinkedHashMap(var0);
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         var1.checkKeyValue(var4.getKey(), var4.getValue());
      }

      return var2;
   }

   static Set access$000(Set var0, MapConstraint var1) {
      return constrainedEntrySet(var0, var1);
   }

   static Map access$100(Map var0, MapConstraint var1) {
      return checkMap(var0, var1);
   }

   static Set access$200(Set var0, MapConstraint var1) {
      return constrainedAsMapEntries(var0, var1);
   }

   static Collection access$300(Collection var0, MapConstraint var1) {
      return constrainedEntries(var0, var1);
   }

   static Collection access$400(Object var0, Iterable var1, MapConstraint var2) {
      return checkValues(var0, var1, var2);
   }

   static Entry access$500(Entry var0, MapConstraint var1) {
      return constrainedEntry(var0, var1);
   }

   static Entry access$700(Entry var0, MapConstraint var1) {
      return constrainedAsMapEntry(var0, var1);
   }

   private static class ConstrainedSortedSetMultimap extends MapConstraints.ConstrainedSetMultimap implements SortedSetMultimap {
      ConstrainedSortedSetMultimap(SortedSetMultimap var1, MapConstraint var2) {
         super(var1, var2);
      }

      public SortedSet get(Object var1) {
         return (SortedSet)super.get(var1);
      }

      public SortedSet removeAll(Object var1) {
         return (SortedSet)super.removeAll(var1);
      }

      public SortedSet replaceValues(Object var1, Iterable var2) {
         return (SortedSet)super.replaceValues(var1, var2);
      }

      public Comparator valueComparator() {
         return ((SortedSetMultimap)this.delegate()).valueComparator();
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

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }
   }

   private static class ConstrainedSetMultimap extends MapConstraints.ConstrainedMultimap implements SetMultimap {
      ConstrainedSetMultimap(SetMultimap var1, MapConstraint var2) {
         super(var1, var2);
      }

      public Set get(Object var1) {
         return (Set)super.get(var1);
      }

      public Set entries() {
         return (Set)super.entries();
      }

      public Set removeAll(Object var1) {
         return (Set)super.removeAll(var1);
      }

      public Set replaceValues(Object var1, Iterable var2) {
         return (Set)super.replaceValues(var1, var2);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection entries() {
         return this.entries();
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }
   }

   private static class ConstrainedListMultimap extends MapConstraints.ConstrainedMultimap implements ListMultimap {
      ConstrainedListMultimap(ListMultimap var1, MapConstraint var2) {
         super(var1, var2);
      }

      public List get(Object var1) {
         return (List)super.get(var1);
      }

      public List removeAll(Object var1) {
         return (List)super.removeAll(var1);
      }

      public List replaceValues(Object var1, Iterable var2) {
         return (List)super.replaceValues(var1, var2);
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

   static class ConstrainedAsMapEntries extends ForwardingSet {
      private final MapConstraint constraint;
      private final Set entries;

      ConstrainedAsMapEntries(Set var1, MapConstraint var2) {
         this.entries = var1;
         this.constraint = var2;
      }

      protected Set delegate() {
         return this.entries;
      }

      public Iterator iterator() {
         Iterator var1 = this.entries.iterator();
         return new ForwardingIterator(this, var1) {
            final Iterator val$iterator;
            final MapConstraints.ConstrainedAsMapEntries this$0;

            {
               this.this$0 = var1;
               this.val$iterator = var2;
            }

            public Entry next() {
               return MapConstraints.access$700((Entry)this.val$iterator.next(), MapConstraints.ConstrainedAsMapEntries.access$600(this.this$0));
            }

            protected Iterator delegate() {
               return this.val$iterator;
            }

            public Object next() {
               return this.next();
            }

            protected Object delegate() {
               return this.delegate();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      public boolean contains(Object var1) {
         return Maps.containsEntryImpl(this.delegate(), var1);
      }

      public boolean containsAll(Collection var1) {
         return this.standardContainsAll(var1);
      }

      public boolean equals(@Nullable Object var1) {
         return this.standardEquals(var1);
      }

      public int hashCode() {
         return this.standardHashCode();
      }

      public boolean remove(Object var1) {
         return Maps.removeEntryImpl(this.delegate(), var1);
      }

      public boolean removeAll(Collection var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.standardRetainAll(var1);
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }

      static MapConstraint access$600(MapConstraints.ConstrainedAsMapEntries var0) {
         return var0.constraint;
      }
   }

   static class ConstrainedEntrySet extends MapConstraints.ConstrainedEntries implements Set {
      ConstrainedEntrySet(Set var1, MapConstraint var2) {
         super(var1, var2);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   private static class ConstrainedEntries extends ForwardingCollection {
      final MapConstraint constraint;
      final Collection entries;

      ConstrainedEntries(Collection var1, MapConstraint var2) {
         this.entries = var1;
         this.constraint = var2;
      }

      protected Collection delegate() {
         return this.entries;
      }

      public Iterator iterator() {
         Iterator var1 = this.entries.iterator();
         return new ForwardingIterator(this, var1) {
            final Iterator val$iterator;
            final MapConstraints.ConstrainedEntries this$0;

            {
               this.this$0 = var1;
               this.val$iterator = var2;
            }

            public Entry next() {
               return MapConstraints.access$500((Entry)this.val$iterator.next(), this.this$0.constraint);
            }

            protected Iterator delegate() {
               return this.val$iterator;
            }

            public Object next() {
               return this.next();
            }

            protected Object delegate() {
               return this.delegate();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.standardToArray(var1);
      }

      public boolean contains(Object var1) {
         return Maps.containsEntryImpl(this.delegate(), var1);
      }

      public boolean containsAll(Collection var1) {
         return this.standardContainsAll(var1);
      }

      public boolean remove(Object var1) {
         return Maps.removeEntryImpl(this.delegate(), var1);
      }

      public boolean removeAll(Collection var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.standardRetainAll(var1);
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class ConstrainedAsMapValues extends ForwardingCollection {
      final Collection delegate;
      final Set entrySet;

      ConstrainedAsMapValues(Collection var1, Set var2) {
         this.delegate = var1;
         this.entrySet = var2;
      }

      protected Collection delegate() {
         return this.delegate;
      }

      public Iterator iterator() {
         Iterator var1 = this.entrySet.iterator();
         return new Iterator(this, var1) {
            final Iterator val$iterator;
            final MapConstraints.ConstrainedAsMapValues this$0;

            {
               this.this$0 = var1;
               this.val$iterator = var2;
            }

            public boolean hasNext() {
               return this.val$iterator.hasNext();
            }

            public Collection next() {
               return (Collection)((Entry)this.val$iterator.next()).getValue();
            }

            public void remove() {
               this.val$iterator.remove();
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

      public boolean contains(Object var1) {
         return this.standardContains(var1);
      }

      public boolean containsAll(Collection var1) {
         return this.standardContainsAll(var1);
      }

      public boolean remove(Object var1) {
         return this.standardRemove(var1);
      }

      public boolean removeAll(Collection var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.standardRetainAll(var1);
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class ConstrainedMultimap extends ForwardingMultimap implements Serializable {
      final MapConstraint constraint;
      final Multimap delegate;
      transient Collection entries;
      transient Map asMap;

      public ConstrainedMultimap(Multimap var1, MapConstraint var2) {
         this.delegate = (Multimap)Preconditions.checkNotNull(var1);
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var2);
      }

      protected Multimap delegate() {
         return this.delegate;
      }

      public Map asMap() {
         Object var1 = this.asMap;
         if (var1 == null) {
            Map var2 = this.delegate.asMap();
            this.asMap = (Map)(var1 = new ForwardingMap(this, var2) {
               Set entrySet;
               Collection values;
               final Map val$asMapDelegate;
               final MapConstraints.ConstrainedMultimap this$0;

               {
                  this.this$0 = var1;
                  this.val$asMapDelegate = var2;
               }

               protected Map delegate() {
                  return this.val$asMapDelegate;
               }

               public Set entrySet() {
                  Set var1 = this.entrySet;
                  if (var1 == null) {
                     this.entrySet = var1 = MapConstraints.access$200(this.val$asMapDelegate.entrySet(), this.this$0.constraint);
                  }

                  return var1;
               }

               public Collection get(Object var1) {
                  try {
                     Collection var2 = this.this$0.get(var1);
                     return var2.isEmpty() ? null : var2;
                  } catch (ClassCastException var3) {
                     return null;
                  }
               }

               public Collection values() {
                  Object var1 = this.values;
                  if (var1 == null) {
                     this.values = (Collection)(var1 = new MapConstraints.ConstrainedAsMapValues(this.delegate().values(), this.entrySet()));
                  }

                  return (Collection)var1;
               }

               public boolean containsValue(Object var1) {
                  return this.values().contains(var1);
               }

               public Object get(Object var1) {
                  return this.get(var1);
               }

               protected Object delegate() {
                  return this.delegate();
               }
            });
         }

         return (Map)var1;
      }

      public Collection entries() {
         Collection var1 = this.entries;
         if (var1 == null) {
            this.entries = var1 = MapConstraints.access$300(this.delegate.entries(), this.constraint);
         }

         return var1;
      }

      public Collection get(Object var1) {
         return Constraints.constrainedTypePreservingCollection(this.delegate.get(var1), new Constraint(this, var1) {
            final Object val$key;
            final MapConstraints.ConstrainedMultimap this$0;

            {
               this.this$0 = var1;
               this.val$key = var2;
            }

            public Object checkElement(Object var1) {
               this.this$0.constraint.checkKeyValue(this.val$key, var1);
               return var1;
            }
         });
      }

      public boolean put(Object var1, Object var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate.put(var1, var2);
      }

      public boolean putAll(Object var1, Iterable var2) {
         return this.delegate.putAll(var1, MapConstraints.access$400(var1, var2, this.constraint));
      }

      public boolean putAll(Multimap var1) {
         boolean var2 = false;

         Entry var4;
         for(Iterator var3 = var1.entries().iterator(); var3.hasNext(); var2 |= this.put(var4.getKey(), var4.getValue())) {
            var4 = (Entry)var3.next();
         }

         return var2;
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.delegate.replaceValues(var1, MapConstraints.access$400(var1, var2, this.constraint));
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class InverseConstraint implements MapConstraint {
      final MapConstraint constraint;

      public InverseConstraint(MapConstraint var1) {
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var1);
      }

      public void checkKeyValue(Object var1, Object var2) {
         this.constraint.checkKeyValue(var2, var1);
      }
   }

   private static class ConstrainedBiMap extends MapConstraints.ConstrainedMap implements BiMap {
      volatile BiMap inverse;

      ConstrainedBiMap(BiMap var1, @Nullable BiMap var2, MapConstraint var3) {
         super(var1, var3);
         this.inverse = var2;
      }

      protected BiMap delegate() {
         return (BiMap)super.delegate();
      }

      public Object forcePut(Object var1, Object var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate().forcePut(var1, var2);
      }

      public BiMap inverse() {
         if (this.inverse == null) {
            this.inverse = new MapConstraints.ConstrainedBiMap(this.delegate().inverse(), this, new MapConstraints.InverseConstraint(this.constraint));
         }

         return this.inverse;
      }

      public Set values() {
         return this.delegate().values();
      }

      protected Map delegate() {
         return this.delegate();
      }

      public Collection values() {
         return this.values();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static class ConstrainedMap extends ForwardingMap {
      private final Map delegate;
      final MapConstraint constraint;
      private transient Set entrySet;

      ConstrainedMap(Map var1, MapConstraint var2) {
         this.delegate = (Map)Preconditions.checkNotNull(var1);
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var2);
      }

      protected Map delegate() {
         return this.delegate;
      }

      public Set entrySet() {
         Set var1 = this.entrySet;
         if (var1 == null) {
            this.entrySet = var1 = MapConstraints.access$000(this.delegate.entrySet(), this.constraint);
         }

         return var1;
      }

      public Object put(Object var1, Object var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate.put(var1, var2);
      }

      public void putAll(Map var1) {
         this.delegate.putAll(MapConstraints.access$100(var1, this.constraint));
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static enum NotNullMapConstraint implements MapConstraint {
      INSTANCE;

      private static final MapConstraints.NotNullMapConstraint[] $VALUES = new MapConstraints.NotNullMapConstraint[]{INSTANCE};

      public void checkKeyValue(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
      }

      public String toString() {
         return "Not null";
      }
   }
}
