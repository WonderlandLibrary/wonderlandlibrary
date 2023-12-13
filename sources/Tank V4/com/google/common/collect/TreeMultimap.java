package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class TreeMultimap extends AbstractSortedKeySortedSetMultimap {
   private transient Comparator keyComparator;
   private transient Comparator valueComparator;
   @GwtIncompatible("not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static TreeMultimap create() {
      return new TreeMultimap(Ordering.natural(), Ordering.natural());
   }

   public static TreeMultimap create(Comparator var0, Comparator var1) {
      return new TreeMultimap((Comparator)Preconditions.checkNotNull(var0), (Comparator)Preconditions.checkNotNull(var1));
   }

   public static TreeMultimap create(Multimap var0) {
      return new TreeMultimap(Ordering.natural(), Ordering.natural(), var0);
   }

   TreeMultimap(Comparator var1, Comparator var2) {
      super(new TreeMap(var1));
      this.keyComparator = var1;
      this.valueComparator = var2;
   }

   private TreeMultimap(Comparator var1, Comparator var2, Multimap var3) {
      this(var1, var2);
      this.putAll(var3);
   }

   SortedSet createCollection() {
      return new TreeSet(this.valueComparator);
   }

   Collection createCollection(@Nullable Object var1) {
      if (var1 == null) {
         this.keyComparator().compare(var1, var1);
      }

      return super.createCollection(var1);
   }

   public Comparator keyComparator() {
      return this.keyComparator;
   }

   public Comparator valueComparator() {
      return this.valueComparator;
   }

   @GwtIncompatible("NavigableMap")
   NavigableMap backingMap() {
      return (NavigableMap)super.backingMap();
   }

   @GwtIncompatible("NavigableSet")
   public NavigableSet get(@Nullable Object var1) {
      return (NavigableSet)super.get(var1);
   }

   @GwtIncompatible("NavigableSet")
   Collection unmodifiableCollectionSubclass(Collection var1) {
      return Sets.unmodifiableNavigableSet((NavigableSet)var1);
   }

   @GwtIncompatible("NavigableSet")
   Collection wrapCollection(Object var1, Collection var2) {
      return new AbstractMapBasedMultimap.WrappedNavigableSet(var1, (NavigableSet)var2, (AbstractMapBasedMultimap.WrappedCollection)null);
   }

   @GwtIncompatible("NavigableSet")
   public NavigableSet keySet() {
      return (NavigableSet)super.keySet();
   }

   @GwtIncompatible("NavigableSet")
   NavigableSet createKeySet() {
      return new AbstractMapBasedMultimap.NavigableKeySet(this.backingMap());
   }

   @GwtIncompatible("NavigableMap")
   public NavigableMap asMap() {
      return (NavigableMap)super.asMap();
   }

   @GwtIncompatible("NavigableMap")
   NavigableMap createAsMap() {
      return new AbstractMapBasedMultimap.NavigableAsMap(this.backingMap());
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.keyComparator());
      var1.writeObject(this.valueComparator());
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyComparator = (Comparator)Preconditions.checkNotNull((Comparator)var1.readObject());
      this.valueComparator = (Comparator)Preconditions.checkNotNull((Comparator)var1.readObject());
      this.setMap(new TreeMap(this.keyComparator));
      Serialization.populateMultimap(this, var1);
   }

   public SortedSet keySet() {
      return this.keySet();
   }

   SortedMap backingMap() {
      return this.backingMap();
   }

   public SortedMap asMap() {
      return this.asMap();
   }

   public Collection values() {
      return super.values();
   }

   public Map asMap() {
      return this.asMap();
   }

   public SortedSet replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   public SortedSet removeAll(Object var1) {
      return super.removeAll(var1);
   }

   public SortedSet get(Object var1) {
      return this.get(var1);
   }

   public Set get(Object var1) {
      return this.get(var1);
   }

   public Set keySet() {
      return this.keySet();
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   public Set entries() {
      return super.entries();
   }

   Set createCollection() {
      return this.createCollection();
   }

   Map createAsMap() {
      return this.createAsMap();
   }

   Set createKeySet() {
      return this.createKeySet();
   }

   public void clear() {
      super.clear();
   }

   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   public int size() {
      return super.size();
   }

   Map backingMap() {
      return this.backingMap();
   }

   Collection createCollection() {
      return this.createCollection();
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public Multiset keys() {
      return super.keys();
   }

   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }
}
