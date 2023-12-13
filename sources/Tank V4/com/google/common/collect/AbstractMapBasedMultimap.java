package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractMapBasedMultimap extends AbstractMultimap implements Serializable {
   private transient Map map;
   private transient int totalSize;
   private static final long serialVersionUID = 2447537837011683357L;

   protected AbstractMapBasedMultimap(Map var1) {
      Preconditions.checkArgument(var1.isEmpty());
      this.map = var1;
   }

   final void setMap(Map var1) {
      this.map = var1;
      this.totalSize = 0;

      Collection var3;
      for(Iterator var2 = var1.values().iterator(); var2.hasNext(); this.totalSize += var3.size()) {
         var3 = (Collection)var2.next();
         Preconditions.checkArgument(!var3.isEmpty());
      }

   }

   Collection createUnmodifiableEmptyCollection() {
      return this.unmodifiableCollectionSubclass(this.createCollection());
   }

   abstract Collection createCollection();

   Collection createCollection(@Nullable Object var1) {
      return this.createCollection();
   }

   Map backingMap() {
      return this.map;
   }

   public int size() {
      return this.totalSize;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean put(@Nullable Object var1, @Nullable Object var2) {
      Collection var3 = (Collection)this.map.get(var1);
      if (var3 == null) {
         var3 = this.createCollection(var1);
         if (var3.add(var2)) {
            ++this.totalSize;
            this.map.put(var1, var3);
            return true;
         } else {
            throw new AssertionError("New Collection violated the Collection spec");
         }
      } else if (var3.add(var2)) {
         ++this.totalSize;
         return true;
      } else {
         return false;
      }
   }

   private Collection getOrCreateCollection(@Nullable Object var1) {
      Collection var2 = (Collection)this.map.get(var1);
      if (var2 == null) {
         var2 = this.createCollection(var1);
         this.map.put(var1, var2);
      }

      return var2;
   }

   public Collection replaceValues(@Nullable Object var1, Iterable var2) {
      Iterator var3 = var2.iterator();
      if (!var3.hasNext()) {
         return this.removeAll(var1);
      } else {
         Collection var4 = this.getOrCreateCollection(var1);
         Collection var5 = this.createCollection();
         var5.addAll(var4);
         this.totalSize -= var4.size();
         var4.clear();

         while(var3.hasNext()) {
            if (var4.add(var3.next())) {
               ++this.totalSize;
            }
         }

         return this.unmodifiableCollectionSubclass(var5);
      }
   }

   public Collection removeAll(@Nullable Object var1) {
      Collection var2 = (Collection)this.map.remove(var1);
      if (var2 == null) {
         return this.createUnmodifiableEmptyCollection();
      } else {
         Collection var3 = this.createCollection();
         var3.addAll(var2);
         this.totalSize -= var2.size();
         var2.clear();
         return this.unmodifiableCollectionSubclass(var3);
      }
   }

   Collection unmodifiableCollectionSubclass(Collection var1) {
      if (var1 instanceof SortedSet) {
         return Collections.unmodifiableSortedSet((SortedSet)var1);
      } else if (var1 instanceof Set) {
         return Collections.unmodifiableSet((Set)var1);
      } else {
         return (Collection)(var1 instanceof List ? Collections.unmodifiableList((List)var1) : Collections.unmodifiableCollection(var1));
      }
   }

   public void clear() {
      Iterator var1 = this.map.values().iterator();

      while(var1.hasNext()) {
         Collection var2 = (Collection)var1.next();
         var2.clear();
      }

      this.map.clear();
      this.totalSize = 0;
   }

   public Collection get(@Nullable Object var1) {
      Collection var2 = (Collection)this.map.get(var1);
      if (var2 == null) {
         var2 = this.createCollection(var1);
      }

      return this.wrapCollection(var1, var2);
   }

   Collection wrapCollection(@Nullable Object var1, Collection var2) {
      if (var2 instanceof SortedSet) {
         return new AbstractMapBasedMultimap.WrappedSortedSet(this, var1, (SortedSet)var2, (AbstractMapBasedMultimap.WrappedCollection)null);
      } else if (var2 instanceof Set) {
         return new AbstractMapBasedMultimap.WrappedSet(this, var1, (Set)var2);
      } else {
         return (Collection)(var2 instanceof List ? this.wrapList(var1, (List)var2, (AbstractMapBasedMultimap.WrappedCollection)null) : new AbstractMapBasedMultimap.WrappedCollection(this, var1, var2, (AbstractMapBasedMultimap.WrappedCollection)null));
      }
   }

   private List wrapList(@Nullable Object var1, List var2, @Nullable AbstractMapBasedMultimap.WrappedCollection var3) {
      return (List)(var2 instanceof RandomAccess ? new AbstractMapBasedMultimap.RandomAccessWrappedList(this, var1, var2, var3) : new AbstractMapBasedMultimap.WrappedList(this, var1, var2, var3));
   }

   private Iterator iteratorOrListIterator(Collection var1) {
      return (Iterator)(var1 instanceof List ? ((List)var1).listIterator() : var1.iterator());
   }

   Set createKeySet() {
      return (Set)(this.map instanceof SortedMap ? new AbstractMapBasedMultimap.SortedKeySet(this, (SortedMap)this.map) : new AbstractMapBasedMultimap.KeySet(this, this.map));
   }

   private int removeValuesForKey(Object var1) {
      Collection var2 = (Collection)Maps.safeRemove(this.map, var1);
      int var3 = 0;
      if (var2 != null) {
         var3 = var2.size();
         var2.clear();
         this.totalSize -= var3;
      }

      return var3;
   }

   public Collection values() {
      return super.values();
   }

   Iterator valueIterator() {
      return new AbstractMapBasedMultimap.Itr(this) {
         final AbstractMapBasedMultimap this$0;

         {
            this.this$0 = var1;
         }

         Object output(Object var1, Object var2) {
            return var2;
         }
      };
   }

   public Collection entries() {
      return super.entries();
   }

   Iterator entryIterator() {
      return new AbstractMapBasedMultimap.Itr(this) {
         final AbstractMapBasedMultimap this$0;

         {
            this.this$0 = var1;
         }

         Entry output(Object var1, Object var2) {
            return Maps.immutableEntry(var1, var2);
         }

         Object output(Object var1, Object var2) {
            return this.output(var1, var2);
         }
      };
   }

   Map createAsMap() {
      return (Map)(this.map instanceof SortedMap ? new AbstractMapBasedMultimap.SortedAsMap(this, (SortedMap)this.map) : new AbstractMapBasedMultimap.AsMap(this, this.map));
   }

   static Map access$000(AbstractMapBasedMultimap var0) {
      return var0.map;
   }

   static Iterator access$100(AbstractMapBasedMultimap var0, Collection var1) {
      return var0.iteratorOrListIterator(var1);
   }

   static int access$210(AbstractMapBasedMultimap var0) {
      return var0.totalSize--;
   }

   static int access$208(AbstractMapBasedMultimap var0) {
      return var0.totalSize++;
   }

   static int access$212(AbstractMapBasedMultimap var0, int var1) {
      return var0.totalSize += var1;
   }

   static int access$220(AbstractMapBasedMultimap var0, int var1) {
      return var0.totalSize -= var1;
   }

   static List access$300(AbstractMapBasedMultimap var0, Object var1, List var2, AbstractMapBasedMultimap.WrappedCollection var3) {
      return var0.wrapList(var1, var2, var3);
   }

   static int access$400(AbstractMapBasedMultimap var0, Object var1) {
      return var0.removeValuesForKey(var1);
   }

   @GwtIncompatible("NavigableAsMap")
   class NavigableAsMap extends AbstractMapBasedMultimap.SortedAsMap implements NavigableMap {
      final AbstractMapBasedMultimap this$0;

      NavigableAsMap(AbstractMapBasedMultimap var1, NavigableMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      NavigableMap sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      public Entry lowerEntry(Object var1) {
         Entry var2 = this.sortedMap().lowerEntry(var1);
         return var2 == null ? null : this.wrapEntry(var2);
      }

      public Object lowerKey(Object var1) {
         return this.sortedMap().lowerKey(var1);
      }

      public Entry floorEntry(Object var1) {
         Entry var2 = this.sortedMap().floorEntry(var1);
         return var2 == null ? null : this.wrapEntry(var2);
      }

      public Object floorKey(Object var1) {
         return this.sortedMap().floorKey(var1);
      }

      public Entry ceilingEntry(Object var1) {
         Entry var2 = this.sortedMap().ceilingEntry(var1);
         return var2 == null ? null : this.wrapEntry(var2);
      }

      public Object ceilingKey(Object var1) {
         return this.sortedMap().ceilingKey(var1);
      }

      public Entry higherEntry(Object var1) {
         Entry var2 = this.sortedMap().higherEntry(var1);
         return var2 == null ? null : this.wrapEntry(var2);
      }

      public Object higherKey(Object var1) {
         return this.sortedMap().higherKey(var1);
      }

      public Entry firstEntry() {
         Entry var1 = this.sortedMap().firstEntry();
         return var1 == null ? null : this.wrapEntry(var1);
      }

      public Entry lastEntry() {
         Entry var1 = this.sortedMap().lastEntry();
         return var1 == null ? null : this.wrapEntry(var1);
      }

      public Entry pollFirstEntry() {
         return this.pollAsMapEntry(this.entrySet().iterator());
      }

      public Entry pollLastEntry() {
         return this.pollAsMapEntry(this.descendingMap().entrySet().iterator());
      }

      Entry pollAsMapEntry(Iterator var1) {
         if (!var1.hasNext()) {
            return null;
         } else {
            Entry var2 = (Entry)var1.next();
            Collection var3 = this.this$0.createCollection();
            var3.addAll((Collection)var2.getValue());
            var1.remove();
            return Maps.immutableEntry(var2.getKey(), this.this$0.unmodifiableCollectionSubclass(var3));
         }
      }

      public NavigableMap descendingMap() {
         return this.this$0.new NavigableAsMap(this.this$0, this.sortedMap().descendingMap());
      }

      public NavigableSet keySet() {
         return (NavigableSet)super.keySet();
      }

      NavigableSet createKeySet() {
         return this.this$0.new NavigableKeySet(this.this$0, this.sortedMap());
      }

      public NavigableSet navigableKeySet() {
         return this.keySet();
      }

      public NavigableSet descendingKeySet() {
         return this.descendingMap().navigableKeySet();
      }

      public NavigableMap subMap(Object var1, Object var2) {
         return this.subMap(var1, true, var2, false);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.this$0.new NavigableAsMap(this.this$0, this.sortedMap().subMap(var1, var2, var3, var4));
      }

      public NavigableMap headMap(Object var1) {
         return this.headMap(var1, false);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return this.this$0.new NavigableAsMap(this.this$0, this.sortedMap().headMap(var1, var2));
      }

      public NavigableMap tailMap(Object var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.this$0.new NavigableAsMap(this.this$0, this.sortedMap().tailMap(var1, var2));
      }

      SortedSet createKeySet() {
         return this.createKeySet();
      }

      public SortedSet keySet() {
         return this.keySet();
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

      SortedMap sortedMap() {
         return this.sortedMap();
      }

      public Set keySet() {
         return this.keySet();
      }

      Set createKeySet() {
         return this.createKeySet();
      }
   }

   private class SortedAsMap extends AbstractMapBasedMultimap.AsMap implements SortedMap {
      SortedSet sortedKeySet;
      final AbstractMapBasedMultimap this$0;

      SortedAsMap(AbstractMapBasedMultimap var1, SortedMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      SortedMap sortedMap() {
         return (SortedMap)this.submap;
      }

      public Comparator comparator() {
         return this.sortedMap().comparator();
      }

      public Object firstKey() {
         return this.sortedMap().firstKey();
      }

      public Object lastKey() {
         return this.sortedMap().lastKey();
      }

      public SortedMap headMap(Object var1) {
         return this.this$0.new SortedAsMap(this.this$0, this.sortedMap().headMap(var1));
      }

      public SortedMap subMap(Object var1, Object var2) {
         return this.this$0.new SortedAsMap(this.this$0, this.sortedMap().subMap(var1, var2));
      }

      public SortedMap tailMap(Object var1) {
         return this.this$0.new SortedAsMap(this.this$0, this.sortedMap().tailMap(var1));
      }

      public SortedSet keySet() {
         SortedSet var1 = this.sortedKeySet;
         return var1 == null ? (this.sortedKeySet = this.createKeySet()) : var1;
      }

      SortedSet createKeySet() {
         return this.this$0.new SortedKeySet(this.this$0, this.sortedMap());
      }

      public Set keySet() {
         return this.keySet();
      }

      Set createKeySet() {
         return this.createKeySet();
      }
   }

   private class AsMap extends Maps.ImprovedAbstractMap {
      final transient Map submap;
      final AbstractMapBasedMultimap this$0;

      AsMap(AbstractMapBasedMultimap var1, Map var2) {
         this.this$0 = var1;
         this.submap = var2;
      }

      protected Set createEntrySet() {
         return new AbstractMapBasedMultimap.AsMap.AsMapEntries(this);
      }

      public boolean containsKey(Object var1) {
         return Maps.safeContainsKey(this.submap, var1);
      }

      public Collection get(Object var1) {
         Collection var2 = (Collection)Maps.safeGet(this.submap, var1);
         return var2 == null ? null : this.this$0.wrapCollection(var1, var2);
      }

      public Set keySet() {
         return this.this$0.keySet();
      }

      public int size() {
         return this.submap.size();
      }

      public Collection remove(Object var1) {
         Collection var2 = (Collection)this.submap.remove(var1);
         if (var2 == null) {
            return null;
         } else {
            Collection var3 = this.this$0.createCollection();
            var3.addAll(var2);
            AbstractMapBasedMultimap.access$220(this.this$0, var2.size());
            var2.clear();
            return var3;
         }
      }

      public boolean equals(@Nullable Object var1) {
         return this == var1 || this.submap.equals(var1);
      }

      public int hashCode() {
         return this.submap.hashCode();
      }

      public String toString() {
         return this.submap.toString();
      }

      public void clear() {
         if (this.submap == AbstractMapBasedMultimap.access$000(this.this$0)) {
            this.this$0.clear();
         } else {
            Iterators.clear(new AbstractMapBasedMultimap.AsMap.AsMapIterator(this));
         }

      }

      Entry wrapEntry(Entry var1) {
         Object var2 = var1.getKey();
         return Maps.immutableEntry(var2, this.this$0.wrapCollection(var2, (Collection)var1.getValue()));
      }

      public Object remove(Object var1) {
         return this.remove(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      class AsMapIterator implements Iterator {
         final Iterator delegateIterator;
         Collection collection;
         final AbstractMapBasedMultimap.AsMap this$1;

         AsMapIterator(AbstractMapBasedMultimap.AsMap var1) {
            this.this$1 = var1;
            this.delegateIterator = this.this$1.submap.entrySet().iterator();
         }

         public boolean hasNext() {
            return this.delegateIterator.hasNext();
         }

         public Entry next() {
            Entry var1 = (Entry)this.delegateIterator.next();
            this.collection = (Collection)var1.getValue();
            return this.this$1.wrapEntry(var1);
         }

         public void remove() {
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.access$220(this.this$1.this$0, this.collection.size());
            this.collection.clear();
         }

         public Object next() {
            return this.next();
         }
      }

      class AsMapEntries extends Maps.EntrySet {
         final AbstractMapBasedMultimap.AsMap this$1;

         AsMapEntries(AbstractMapBasedMultimap.AsMap var1) {
            this.this$1 = var1;
         }

         Map map() {
            return this.this$1;
         }

         public Iterator iterator() {
            return this.this$1.new AsMapIterator(this.this$1);
         }

         public boolean contains(Object var1) {
            return Collections2.safeContains(this.this$1.submap.entrySet(), var1);
         }

         public boolean remove(Object var1) {
            if (!this.contains(var1)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               AbstractMapBasedMultimap.access$400(this.this$1.this$0, var2.getKey());
               return true;
            }
         }
      }
   }

   private abstract class Itr implements Iterator {
      final Iterator keyIterator;
      Object key;
      Collection collection;
      Iterator valueIterator;
      final AbstractMapBasedMultimap this$0;

      Itr(AbstractMapBasedMultimap var1) {
         this.this$0 = var1;
         this.keyIterator = AbstractMapBasedMultimap.access$000(var1).entrySet().iterator();
         this.key = null;
         this.collection = null;
         this.valueIterator = Iterators.emptyModifiableIterator();
      }

      abstract Object output(Object var1, Object var2);

      public boolean hasNext() {
         return this.keyIterator.hasNext() || this.valueIterator.hasNext();
      }

      public Object next() {
         if (!this.valueIterator.hasNext()) {
            Entry var1 = (Entry)this.keyIterator.next();
            this.key = var1.getKey();
            this.collection = (Collection)var1.getValue();
            this.valueIterator = this.collection.iterator();
         }

         return this.output(this.key, this.valueIterator.next());
      }

      public void remove() {
         this.valueIterator.remove();
         if (this.collection.isEmpty()) {
            this.keyIterator.remove();
         }

         AbstractMapBasedMultimap.access$210(this.this$0);
      }
   }

   @GwtIncompatible("NavigableSet")
   class NavigableKeySet extends AbstractMapBasedMultimap.SortedKeySet implements NavigableSet {
      final AbstractMapBasedMultimap this$0;

      NavigableKeySet(AbstractMapBasedMultimap var1, NavigableMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      NavigableMap sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      public Object lower(Object var1) {
         return this.sortedMap().lowerKey(var1);
      }

      public Object floor(Object var1) {
         return this.sortedMap().floorKey(var1);
      }

      public Object ceiling(Object var1) {
         return this.sortedMap().ceilingKey(var1);
      }

      public Object higher(Object var1) {
         return this.sortedMap().higherKey(var1);
      }

      public Object pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      public Object pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      public NavigableSet descendingSet() {
         return this.this$0.new NavigableKeySet(this.this$0, this.sortedMap().descendingMap());
      }

      public Iterator descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet headSet(Object var1) {
         return this.headSet(var1, false);
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return this.this$0.new NavigableKeySet(this.this$0, this.sortedMap().headMap(var1, var2));
      }

      public NavigableSet subSet(Object var1, Object var2) {
         return this.subSet(var1, true, var2, false);
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return this.this$0.new NavigableKeySet(this.this$0, this.sortedMap().subMap(var1, var2, var3, var4));
      }

      public NavigableSet tailSet(Object var1) {
         return this.tailSet(var1, true);
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return this.this$0.new NavigableKeySet(this.this$0, this.sortedMap().tailMap(var1, var2));
      }

      public SortedSet tailSet(Object var1) {
         return this.tailSet(var1);
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.subSet(var1, var2);
      }

      public SortedSet headSet(Object var1) {
         return this.headSet(var1);
      }

      SortedMap sortedMap() {
         return this.sortedMap();
      }
   }

   private class SortedKeySet extends AbstractMapBasedMultimap.KeySet implements SortedSet {
      final AbstractMapBasedMultimap this$0;

      SortedKeySet(AbstractMapBasedMultimap var1, SortedMap var2) {
         super(var1, var2);
         this.this$0 = var1;
      }

      SortedMap sortedMap() {
         return (SortedMap)super.map();
      }

      public Comparator comparator() {
         return this.sortedMap().comparator();
      }

      public Object first() {
         return this.sortedMap().firstKey();
      }

      public SortedSet headSet(Object var1) {
         return this.this$0.new SortedKeySet(this.this$0, this.sortedMap().headMap(var1));
      }

      public Object last() {
         return this.sortedMap().lastKey();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.this$0.new SortedKeySet(this.this$0, this.sortedMap().subMap(var1, var2));
      }

      public SortedSet tailSet(Object var1) {
         return this.this$0.new SortedKeySet(this.this$0, this.sortedMap().tailMap(var1));
      }
   }

   private class KeySet extends Maps.KeySet {
      final AbstractMapBasedMultimap this$0;

      KeySet(AbstractMapBasedMultimap var1, Map var2) {
         super(var2);
         this.this$0 = var1;
      }

      public Iterator iterator() {
         Iterator var1 = this.map().entrySet().iterator();
         return new Iterator(this, var1) {
            Entry entry;
            final Iterator val$entryIterator;
            final AbstractMapBasedMultimap.KeySet this$1;

            {
               this.this$1 = var1;
               this.val$entryIterator = var2;
            }

            public boolean hasNext() {
               return this.val$entryIterator.hasNext();
            }

            public Object next() {
               this.entry = (Entry)this.val$entryIterator.next();
               return this.entry.getKey();
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.entry != null);
               Collection var1 = (Collection)this.entry.getValue();
               this.val$entryIterator.remove();
               AbstractMapBasedMultimap.access$220(this.this$1.this$0, var1.size());
               var1.clear();
            }
         };
      }

      public boolean remove(Object var1) {
         int var2 = 0;
         Collection var3 = (Collection)this.map().remove(var1);
         if (var3 != null) {
            var2 = var3.size();
            var3.clear();
            AbstractMapBasedMultimap.access$220(this.this$0, var2);
         }

         return var2 > 0;
      }

      public void clear() {
         Iterators.clear(this.iterator());
      }

      public boolean containsAll(Collection var1) {
         return this.map().keySet().containsAll(var1);
      }

      public boolean equals(@Nullable Object var1) {
         return this == var1 || this.map().keySet().equals(var1);
      }

      public int hashCode() {
         return this.map().keySet().hashCode();
      }
   }

   private class RandomAccessWrappedList extends AbstractMapBasedMultimap.WrappedList implements RandomAccess {
      final AbstractMapBasedMultimap this$0;

      RandomAccessWrappedList(@Nullable AbstractMapBasedMultimap var1, Object var2, @Nullable List var3, AbstractMapBasedMultimap.WrappedCollection var4) {
         super(var1, var2, var3, var4);
         this.this$0 = var1;
      }
   }

   private class WrappedList extends AbstractMapBasedMultimap.WrappedCollection implements List {
      final AbstractMapBasedMultimap this$0;

      WrappedList(@Nullable AbstractMapBasedMultimap var1, Object var2, @Nullable List var3, AbstractMapBasedMultimap.WrappedCollection var4) {
         super(var1, var2, var3, var4);
         this.this$0 = var1;
      }

      List getListDelegate() {
         return (List)this.getDelegate();
      }

      public boolean addAll(int var1, Collection var2) {
         if (var2.isEmpty()) {
            return false;
         } else {
            int var3 = this.size();
            boolean var4 = this.getListDelegate().addAll(var1, var2);
            if (var4) {
               int var5 = this.getDelegate().size();
               AbstractMapBasedMultimap.access$212(this.this$0, var5 - var3);
               if (var3 == 0) {
                  this.addToMap();
               }
            }

            return var4;
         }
      }

      public Object get(int var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().get(var1);
      }

      public Object set(int var1, Object var2) {
         this.refreshIfEmpty();
         return this.getListDelegate().set(var1, var2);
      }

      public void add(int var1, Object var2) {
         this.refreshIfEmpty();
         boolean var3 = this.getDelegate().isEmpty();
         this.getListDelegate().add(var1, var2);
         AbstractMapBasedMultimap.access$208(this.this$0);
         if (var3) {
            this.addToMap();
         }

      }

      public Object remove(int var1) {
         this.refreshIfEmpty();
         Object var2 = this.getListDelegate().remove(var1);
         AbstractMapBasedMultimap.access$210(this.this$0);
         this.removeIfEmpty();
         return var2;
      }

      public int indexOf(Object var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().lastIndexOf(var1);
      }

      public ListIterator listIterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedListIterator(this);
      }

      public ListIterator listIterator(int var1) {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedListIterator(this, var1);
      }

      public List subList(int var1, int var2) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.access$300(this.this$0, this.getKey(), this.getListDelegate().subList(var1, var2), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      private class WrappedListIterator extends AbstractMapBasedMultimap.WrappedCollection.WrappedIterator implements ListIterator {
         final AbstractMapBasedMultimap.WrappedList this$1;

         WrappedListIterator(AbstractMapBasedMultimap.WrappedList var1) {
            super(var1);
            this.this$1 = var1;
         }

         public WrappedListIterator(AbstractMapBasedMultimap.WrappedList var1, int var2) {
            super(var1, var1.getListDelegate().listIterator(var2));
            this.this$1 = var1;
         }

         private ListIterator getDelegateListIterator() {
            return (ListIterator)this.getDelegateIterator();
         }

         public boolean hasPrevious() {
            return this.getDelegateListIterator().hasPrevious();
         }

         public Object previous() {
            return this.getDelegateListIterator().previous();
         }

         public int nextIndex() {
            return this.getDelegateListIterator().nextIndex();
         }

         public int previousIndex() {
            return this.getDelegateListIterator().previousIndex();
         }

         public void set(Object var1) {
            this.getDelegateListIterator().set(var1);
         }

         public void add(Object var1) {
            boolean var2 = this.this$1.isEmpty();
            this.getDelegateListIterator().add(var1);
            AbstractMapBasedMultimap.access$208(this.this$1.this$0);
            if (var2) {
               this.this$1.addToMap();
            }

         }
      }
   }

   @GwtIncompatible("NavigableSet")
   class WrappedNavigableSet extends AbstractMapBasedMultimap.WrappedSortedSet implements NavigableSet {
      final AbstractMapBasedMultimap this$0;

      WrappedNavigableSet(@Nullable AbstractMapBasedMultimap var1, Object var2, @Nullable NavigableSet var3, AbstractMapBasedMultimap.WrappedCollection var4) {
         super(var1, var2, var3, var4);
         this.this$0 = var1;
      }

      NavigableSet getSortedSetDelegate() {
         return (NavigableSet)super.getSortedSetDelegate();
      }

      public Object lower(Object var1) {
         return this.getSortedSetDelegate().lower(var1);
      }

      public Object floor(Object var1) {
         return this.getSortedSetDelegate().floor(var1);
      }

      public Object ceiling(Object var1) {
         return this.getSortedSetDelegate().ceiling(var1);
      }

      public Object higher(Object var1) {
         return this.getSortedSetDelegate().higher(var1);
      }

      public Object pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      public Object pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      private NavigableSet wrap(NavigableSet var1) {
         return this.this$0.new WrappedNavigableSet(this.this$0, this.key, var1, (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public NavigableSet descendingSet() {
         return this.wrap(this.getSortedSetDelegate().descendingSet());
      }

      public Iterator descendingIterator() {
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, this.getSortedSetDelegate().descendingIterator());
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return this.wrap(this.getSortedSetDelegate().subSet(var1, var2, var3, var4));
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return this.wrap(this.getSortedSetDelegate().headSet(var1, var2));
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return this.wrap(this.getSortedSetDelegate().tailSet(var1, var2));
      }

      SortedSet getSortedSetDelegate() {
         return this.getSortedSetDelegate();
      }
   }

   private class WrappedSortedSet extends AbstractMapBasedMultimap.WrappedCollection implements SortedSet {
      final AbstractMapBasedMultimap this$0;

      WrappedSortedSet(@Nullable AbstractMapBasedMultimap var1, Object var2, @Nullable SortedSet var3, AbstractMapBasedMultimap.WrappedCollection var4) {
         super(var1, var2, var3, var4);
         this.this$0 = var1;
      }

      SortedSet getSortedSetDelegate() {
         return (SortedSet)this.getDelegate();
      }

      public Comparator comparator() {
         return this.getSortedSetDelegate().comparator();
      }

      public Object first() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().first();
      }

      public Object last() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().last();
      }

      public SortedSet headSet(Object var1) {
         this.refreshIfEmpty();
         return this.this$0.new WrappedSortedSet(this.this$0, this.getKey(), this.getSortedSetDelegate().headSet(var1), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public SortedSet subSet(Object var1, Object var2) {
         this.refreshIfEmpty();
         return this.this$0.new WrappedSortedSet(this.this$0, this.getKey(), this.getSortedSetDelegate().subSet(var1, var2), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public SortedSet tailSet(Object var1) {
         this.refreshIfEmpty();
         return this.this$0.new WrappedSortedSet(this.this$0, this.getKey(), this.getSortedSetDelegate().tailSet(var1), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }
   }

   private class WrappedSet extends AbstractMapBasedMultimap.WrappedCollection implements Set {
      final AbstractMapBasedMultimap this$0;

      WrappedSet(@Nullable AbstractMapBasedMultimap var1, Object var2, Set var3) {
         super(var1, var2, var3, (AbstractMapBasedMultimap.WrappedCollection)null);
         this.this$0 = var1;
      }

      public boolean removeAll(Collection var1) {
         if (var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = Sets.removeAllImpl((Set)this.delegate, var1);
            if (var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.access$212(this.this$0, var4 - var2);
               this.removeIfEmpty();
            }

            return var3;
         }
      }
   }

   private class WrappedCollection extends AbstractCollection {
      final Object key;
      Collection delegate;
      final AbstractMapBasedMultimap.WrappedCollection ancestor;
      final Collection ancestorDelegate;
      final AbstractMapBasedMultimap this$0;

      WrappedCollection(@Nullable AbstractMapBasedMultimap var1, Object var2, @Nullable Collection var3, AbstractMapBasedMultimap.WrappedCollection var4) {
         this.this$0 = var1;
         this.key = var2;
         this.delegate = var3;
         this.ancestor = var4;
         this.ancestorDelegate = var4 == null ? null : var4.getDelegate();
      }

      void refreshIfEmpty() {
         if (this.ancestor != null) {
            this.ancestor.refreshIfEmpty();
            if (this.ancestor.getDelegate() != this.ancestorDelegate) {
               throw new ConcurrentModificationException();
            }
         } else if (this.delegate.isEmpty()) {
            Collection var1 = (Collection)AbstractMapBasedMultimap.access$000(this.this$0).get(this.key);
            if (var1 != null) {
               this.delegate = var1;
            }
         }

      }

      void removeIfEmpty() {
         if (this.ancestor != null) {
            this.ancestor.removeIfEmpty();
         } else if (this.delegate.isEmpty()) {
            AbstractMapBasedMultimap.access$000(this.this$0).remove(this.key);
         }

      }

      Object getKey() {
         return this.key;
      }

      void addToMap() {
         if (this.ancestor != null) {
            this.ancestor.addToMap();
         } else {
            AbstractMapBasedMultimap.access$000(this.this$0).put(this.key, this.delegate);
         }

      }

      public int size() {
         this.refreshIfEmpty();
         return this.delegate.size();
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 == this) {
            return true;
         } else {
            this.refreshIfEmpty();
            return this.delegate.equals(var1);
         }
      }

      public int hashCode() {
         this.refreshIfEmpty();
         return this.delegate.hashCode();
      }

      public String toString() {
         this.refreshIfEmpty();
         return this.delegate.toString();
      }

      Collection getDelegate() {
         return this.delegate;
      }

      public Iterator iterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this);
      }

      public boolean add(Object var1) {
         this.refreshIfEmpty();
         boolean var2 = this.delegate.isEmpty();
         boolean var3 = this.delegate.add(var1);
         if (var3) {
            AbstractMapBasedMultimap.access$208(this.this$0);
            if (var2) {
               this.addToMap();
            }
         }

         return var3;
      }

      AbstractMapBasedMultimap.WrappedCollection getAncestor() {
         return this.ancestor;
      }

      public boolean addAll(Collection var1) {
         if (var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = this.delegate.addAll(var1);
            if (var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.access$212(this.this$0, var4 - var2);
               if (var2 == 0) {
                  this.addToMap();
               }
            }

            return var3;
         }
      }

      public boolean contains(Object var1) {
         this.refreshIfEmpty();
         return this.delegate.contains(var1);
      }

      public boolean containsAll(Collection var1) {
         this.refreshIfEmpty();
         return this.delegate.containsAll(var1);
      }

      public void clear() {
         int var1 = this.size();
         if (var1 != 0) {
            this.delegate.clear();
            AbstractMapBasedMultimap.access$220(this.this$0, var1);
            this.removeIfEmpty();
         }
      }

      public boolean remove(Object var1) {
         this.refreshIfEmpty();
         boolean var2 = this.delegate.remove(var1);
         if (var2) {
            AbstractMapBasedMultimap.access$210(this.this$0);
            this.removeIfEmpty();
         }

         return var2;
      }

      public boolean removeAll(Collection var1) {
         if (var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = this.delegate.removeAll(var1);
            if (var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.access$212(this.this$0, var4 - var2);
               this.removeIfEmpty();
            }

            return var3;
         }
      }

      public boolean retainAll(Collection var1) {
         Preconditions.checkNotNull(var1);
         int var2 = this.size();
         boolean var3 = this.delegate.retainAll(var1);
         if (var3) {
            int var4 = this.delegate.size();
            AbstractMapBasedMultimap.access$212(this.this$0, var4 - var2);
            this.removeIfEmpty();
         }

         return var3;
      }

      class WrappedIterator implements Iterator {
         final Iterator delegateIterator;
         final Collection originalDelegate;
         final AbstractMapBasedMultimap.WrappedCollection this$1;

         WrappedIterator(AbstractMapBasedMultimap.WrappedCollection var1) {
            this.this$1 = var1;
            this.originalDelegate = this.this$1.delegate;
            this.delegateIterator = AbstractMapBasedMultimap.access$100(var1.this$0, var1.delegate);
         }

         WrappedIterator(AbstractMapBasedMultimap.WrappedCollection var1, Iterator var2) {
            this.this$1 = var1;
            this.originalDelegate = this.this$1.delegate;
            this.delegateIterator = var2;
         }

         void validateIterator() {
            this.this$1.refreshIfEmpty();
            if (this.this$1.delegate != this.originalDelegate) {
               throw new ConcurrentModificationException();
            }
         }

         public boolean hasNext() {
            this.validateIterator();
            return this.delegateIterator.hasNext();
         }

         public Object next() {
            this.validateIterator();
            return this.delegateIterator.next();
         }

         public void remove() {
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.access$210(this.this$1.this$0);
            this.this$1.removeIfEmpty();
         }

         Iterator getDelegateIterator() {
            this.validateIterator();
            return this.delegateIterator;
         }
      }
   }
}
