package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap implements Multimap {
   private transient Collection entries;
   private transient Set keySet;
   private transient Multiset keys;
   private transient Collection values;
   private transient Map asMap;

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsValue(@Nullable Object var1) {
      Iterator var2 = this.asMap().values().iterator();

      Collection var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (Collection)var2.next();
      } while(!var3.contains(var1));

      return true;
   }

   public boolean containsEntry(@Nullable Object var1, @Nullable Object var2) {
      Collection var3 = (Collection)this.asMap().get(var1);
      return var3 != null && var3.contains(var2);
   }

   public boolean remove(@Nullable Object var1, @Nullable Object var2) {
      Collection var3 = (Collection)this.asMap().get(var1);
      return var3 != null && var3.remove(var2);
   }

   public boolean put(@Nullable Object var1, @Nullable Object var2) {
      return this.get(var1).add(var2);
   }

   public boolean putAll(@Nullable Object var1, Iterable var2) {
      Preconditions.checkNotNull(var2);
      if (var2 instanceof Collection) {
         Collection var4 = (Collection)var2;
         return !var4.isEmpty() && this.get(var1).addAll(var4);
      } else {
         Iterator var3 = var2.iterator();
         return var3.hasNext() && Iterators.addAll(this.get(var1), var3);
      }
   }

   public boolean putAll(Multimap var1) {
      boolean var2 = false;

      Entry var4;
      for(Iterator var3 = var1.entries().iterator(); var3.hasNext(); var2 |= this.put(var4.getKey(), var4.getValue())) {
         var4 = (Entry)var3.next();
      }

      return var2;
   }

   public Collection replaceValues(@Nullable Object var1, Iterable var2) {
      Preconditions.checkNotNull(var2);
      Collection var3 = this.removeAll(var1);
      this.putAll(var1, var2);
      return var3;
   }

   public Collection entries() {
      Collection var1 = this.entries;
      return var1 == null ? (this.entries = this.createEntries()) : var1;
   }

   Collection createEntries() {
      return (Collection)(this instanceof SetMultimap ? new AbstractMultimap.EntrySet(this) : new AbstractMultimap.Entries(this));
   }

   abstract Iterator entryIterator();

   public Set keySet() {
      Set var1 = this.keySet;
      return var1 == null ? (this.keySet = this.createKeySet()) : var1;
   }

   Set createKeySet() {
      return new Maps.KeySet(this.asMap());
   }

   public Multiset keys() {
      Multiset var1 = this.keys;
      return var1 == null ? (this.keys = this.createKeys()) : var1;
   }

   Multiset createKeys() {
      return new Multimaps.Keys(this);
   }

   public Collection values() {
      Collection var1 = this.values;
      return var1 == null ? (this.values = this.createValues()) : var1;
   }

   Collection createValues() {
      return new AbstractMultimap.Values(this);
   }

   Iterator valueIterator() {
      return Maps.valueIterator(this.entries().iterator());
   }

   public Map asMap() {
      Map var1 = this.asMap;
      return var1 == null ? (this.asMap = this.createAsMap()) : var1;
   }

   abstract Map createAsMap();

   public boolean equals(@Nullable Object var1) {
      return Multimaps.equalsImpl(this, var1);
   }

   public int hashCode() {
      return this.asMap().hashCode();
   }

   public String toString() {
      return this.asMap().toString();
   }

   class Values extends AbstractCollection {
      final AbstractMultimap this$0;

      Values(AbstractMultimap var1) {
         this.this$0 = var1;
      }

      public Iterator iterator() {
         return this.this$0.valueIterator();
      }

      public int size() {
         return this.this$0.size();
      }

      public boolean contains(@Nullable Object var1) {
         return this.this$0.containsValue(var1);
      }

      public void clear() {
         this.this$0.clear();
      }
   }

   private class EntrySet extends AbstractMultimap.Entries implements Set {
      final AbstractMultimap this$0;

      private EntrySet(AbstractMultimap var1) {
         super(var1, null);
         this.this$0 = var1;
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      EntrySet(AbstractMultimap var1, Object var2) {
         this(var1);
      }
   }

   private class Entries extends Multimaps.Entries {
      final AbstractMultimap this$0;

      private Entries(AbstractMultimap var1) {
         this.this$0 = var1;
      }

      Multimap multimap() {
         return this.this$0;
      }

      public Iterator iterator() {
         return this.this$0.entryIterator();
      }

      Entries(AbstractMultimap var1, Object var2) {
         this(var1);
      }
   }
}
