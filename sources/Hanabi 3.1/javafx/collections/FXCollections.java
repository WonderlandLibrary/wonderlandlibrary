package javafx.collections;

import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.MapAdapterChange;
import com.sun.javafx.collections.MapListenerHelper;
import com.sun.javafx.collections.ObservableFloatArrayImpl;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.ObservableSequentialListWrapper;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.sun.javafx.collections.SetAdapterChange;
import com.sun.javafx.collections.SetListenerHelper;
import com.sun.javafx.collections.SortableList;
import com.sun.javafx.collections.SourceAdapterChange;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.util.Callback;

public class FXCollections {
   private static ObservableMap EMPTY_OBSERVABLE_MAP = new EmptyObservableMap();
   private static ObservableList EMPTY_OBSERVABLE_LIST = new EmptyObservableList();
   private static ObservableSet EMPTY_OBSERVABLE_SET = new EmptyObservableSet();
   private static Random r;

   private FXCollections() {
   }

   public static ObservableList observableList(List var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return (ObservableList)(var0 instanceof RandomAccess ? new ObservableListWrapper(var0) : new ObservableSequentialListWrapper(var0));
      }
   }

   public static ObservableList observableList(List var0, Callback var1) {
      if (var0 != null && var1 != null) {
         return (ObservableList)(var0 instanceof RandomAccess ? new ObservableListWrapper(var0, var1) : new ObservableSequentialListWrapper(var0, var1));
      } else {
         throw new NullPointerException();
      }
   }

   public static ObservableMap observableMap(Map var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new ObservableMapWrapper(var0);
      }
   }

   public static ObservableSet observableSet(Set var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new ObservableSetWrapper(var0);
      }
   }

   public static ObservableSet observableSet(Object... var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         HashSet var1 = new HashSet(var0.length);
         Collections.addAll(var1, var0);
         return new ObservableSetWrapper(var1);
      }
   }

   public static ObservableMap unmodifiableObservableMap(ObservableMap var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new UnmodifiableObservableMap(var0);
      }
   }

   public static ObservableMap checkedObservableMap(ObservableMap var0, Class var1, Class var2) {
      if (var0 != null && var1 != null && var2 != null) {
         return new CheckedObservableMap(var0, var1, var2);
      } else {
         throw new NullPointerException();
      }
   }

   public static ObservableMap synchronizedObservableMap(ObservableMap var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new SynchronizedObservableMap(var0);
      }
   }

   public static ObservableMap emptyObservableMap() {
      return EMPTY_OBSERVABLE_MAP;
   }

   public static ObservableIntegerArray observableIntegerArray() {
      return new ObservableIntegerArrayImpl();
   }

   public static ObservableIntegerArray observableIntegerArray(int... var0) {
      return new ObservableIntegerArrayImpl(var0);
   }

   public static ObservableIntegerArray observableIntegerArray(ObservableIntegerArray var0) {
      return new ObservableIntegerArrayImpl(var0);
   }

   public static ObservableFloatArray observableFloatArray() {
      return new ObservableFloatArrayImpl();
   }

   public static ObservableFloatArray observableFloatArray(float... var0) {
      return new ObservableFloatArrayImpl(var0);
   }

   public static ObservableFloatArray observableFloatArray(ObservableFloatArray var0) {
      return new ObservableFloatArrayImpl(var0);
   }

   public static ObservableList observableArrayList() {
      return observableList(new ArrayList());
   }

   public static ObservableList observableArrayList(Callback var0) {
      return observableList(new ArrayList(), var0);
   }

   public static ObservableList observableArrayList(Object... var0) {
      ObservableList var1 = observableArrayList();
      var1.addAll(var0);
      return var1;
   }

   public static ObservableList observableArrayList(Collection var0) {
      ObservableList var1 = observableArrayList();
      var1.addAll(var0);
      return var1;
   }

   public static ObservableMap observableHashMap() {
      return observableMap(new HashMap());
   }

   public static ObservableList concat(ObservableList... var0) {
      if (var0.length == 0) {
         return observableArrayList();
      } else if (var0.length == 1) {
         return observableArrayList((Collection)var0[0]);
      } else {
         ArrayList var1 = new ArrayList();
         ObservableList[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ObservableList var5 = var2[var4];
            var1.addAll(var5);
         }

         return observableList(var1);
      }
   }

   public static ObservableList unmodifiableObservableList(ObservableList var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new UnmodifiableObservableListImpl(var0);
      }
   }

   public static ObservableList checkedObservableList(ObservableList var0, Class var1) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new CheckedObservableList(var0, var1);
      }
   }

   public static ObservableList synchronizedObservableList(ObservableList var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new SynchronizedObservableList(var0);
      }
   }

   public static ObservableList emptyObservableList() {
      return EMPTY_OBSERVABLE_LIST;
   }

   public static ObservableList singletonObservableList(Object var0) {
      return new SingletonObservableList(var0);
   }

   public static ObservableSet unmodifiableObservableSet(ObservableSet var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new UnmodifiableObservableSet(var0);
      }
   }

   public static ObservableSet checkedObservableSet(ObservableSet var0, Class var1) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new CheckedObservableSet(var0, var1);
      }
   }

   public static ObservableSet synchronizedObservableSet(ObservableSet var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         return new SynchronizedObservableSet(var0);
      }
   }

   public static ObservableSet emptyObservableSet() {
      return EMPTY_OBSERVABLE_SET;
   }

   public static void copy(ObservableList var0, List var1) {
      int var2 = var1.size();
      if (var2 > var0.size()) {
         throw new IndexOutOfBoundsException("Source does not fit in dest");
      } else {
         Object[] var3 = (Object[])var0.toArray();
         System.arraycopy(var1.toArray(), 0, var3, 0, var2);
         var0.setAll(var3);
      }
   }

   public static void fill(ObservableList var0, Object var1) {
      Object[] var2 = (Object[])(new Object[var0.size()]);
      Arrays.fill(var2, var1);
      var0.setAll(var2);
   }

   public static boolean replaceAll(ObservableList var0, Object var1, Object var2) {
      Object[] var3 = (Object[])var0.toArray();
      boolean var4 = false;

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5].equals(var1)) {
            var3[var5] = var2;
            var4 = true;
         }
      }

      if (var4) {
         var0.setAll(var3);
      }

      return var4;
   }

   public static void reverse(ObservableList var0) {
      Object[] var1 = var0.toArray();

      for(int var2 = 0; var2 < var1.length / 2; ++var2) {
         Object var3 = var1[var2];
         var1[var2] = var1[var1.length - var2 - 1];
         var1[var1.length - var2 - 1] = var3;
      }

      var0.setAll(var1);
   }

   public static void rotate(ObservableList var0, int var1) {
      Object[] var2 = var0.toArray();
      int var3 = var0.size();
      var1 %= var3;
      if (var1 < 0) {
         var1 += var3;
      }

      if (var1 != 0) {
         int var4 = 0;

         for(int var5 = 0; var5 != var3; ++var4) {
            Object var6 = var2[var4];
            int var8 = var4;

            do {
               var8 += var1;
               if (var8 >= var3) {
                  var8 -= var3;
               }

               Object var7 = var2[var8];
               var2[var8] = var6;
               var6 = var7;
               ++var5;
            } while(var8 != var4);
         }

         var0.setAll(var2);
      }
   }

   public static void shuffle(ObservableList var0) {
      if (r == null) {
         r = new Random();
      }

      shuffle(var0, r);
   }

   public static void shuffle(ObservableList var0, Random var1) {
      Object[] var2 = var0.toArray();

      for(int var3 = var0.size(); var3 > 1; --var3) {
         swap(var2, var3 - 1, var1.nextInt(var3));
      }

      var0.setAll(var2);
   }

   private static void swap(Object[] var0, int var1, int var2) {
      Object var3 = var0[var1];
      var0[var1] = var0[var2];
      var0[var2] = var3;
   }

   public static void sort(ObservableList var0) {
      if (var0 instanceof SortableList) {
         ((SortableList)var0).sort();
      } else {
         ArrayList var1 = new ArrayList(var0);
         Collections.sort(var1);
         var0.setAll((Collection)var1);
      }

   }

   public static void sort(ObservableList var0, Comparator var1) {
      if (var0 instanceof SortableList) {
         ((SortableList)var0).sort(var1);
      } else {
         ArrayList var2 = new ArrayList(var0);
         Collections.sort(var2, var1);
         var0.setAll((Collection)var2);
      }

   }

   private static class SynchronizedObservableMap extends SynchronizedMap implements ObservableMap {
      private final ObservableMap backingMap;
      private MapListenerHelper listenerHelper;
      private final MapChangeListener listener;

      SynchronizedObservableMap(ObservableMap var1, Object var2) {
         super(var1, var2);
         this.backingMap = var1;
         this.listener = (var1x) -> {
            MapListenerHelper.fireValueChangedEvent(this.listenerHelper, new MapAdapterChange(this, var1x));
         };
         this.backingMap.addListener(new WeakMapChangeListener(this.listener));
      }

      SynchronizedObservableMap(ObservableMap var1) {
         this(var1, new Object());
      }

      public void addListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, var1);
         }
      }

      public void removeListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, var1);
         }
      }

      public void addListener(MapChangeListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, var1);
         }
      }

      public void removeListener(MapChangeListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, var1);
         }
      }
   }

   private static class SynchronizedCollection implements Collection {
      private final Collection backingCollection;
      final Object mutex;

      SynchronizedCollection(Collection var1, Object var2) {
         this.backingCollection = var1;
         this.mutex = var2;
      }

      SynchronizedCollection(Collection var1) {
         this(var1, new Object());
      }

      public int size() {
         synchronized(this.mutex) {
            return this.backingCollection.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.backingCollection.isEmpty();
         }
      }

      public boolean contains(Object var1) {
         synchronized(this.mutex) {
            return this.backingCollection.contains(var1);
         }
      }

      public Iterator iterator() {
         return this.backingCollection.iterator();
      }

      public Object[] toArray() {
         synchronized(this.mutex) {
            return this.backingCollection.toArray();
         }
      }

      public Object[] toArray(Object[] var1) {
         synchronized(this.mutex) {
            return this.backingCollection.toArray(var1);
         }
      }

      public boolean add(Object var1) {
         synchronized(this.mutex) {
            return this.backingCollection.add(var1);
         }
      }

      public boolean remove(Object var1) {
         synchronized(this.mutex) {
            return this.backingCollection.remove(var1);
         }
      }

      public boolean containsAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingCollection.containsAll(var1);
         }
      }

      public boolean addAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingCollection.addAll(var1);
         }
      }

      public boolean removeAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingCollection.removeAll(var1);
         }
      }

      public boolean retainAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingCollection.retainAll(var1);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.backingCollection.clear();
         }
      }
   }

   private static class SynchronizedMap implements Map {
      final Object mutex;
      private final Map backingMap;
      private transient Set keySet;
      private transient Set entrySet;
      private transient Collection values;

      SynchronizedMap(Map var1, Object var2) {
         this.keySet = null;
         this.entrySet = null;
         this.values = null;
         this.backingMap = var1;
         this.mutex = var2;
      }

      SynchronizedMap(Map var1) {
         this(var1, new Object());
      }

      public int size() {
         synchronized(this.mutex) {
            return this.backingMap.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.backingMap.isEmpty();
         }
      }

      public boolean containsKey(Object var1) {
         synchronized(this.mutex) {
            return this.backingMap.containsKey(var1);
         }
      }

      public boolean containsValue(Object var1) {
         synchronized(this.mutex) {
            return this.backingMap.containsValue(var1);
         }
      }

      public Object get(Object var1) {
         synchronized(this.mutex) {
            return this.backingMap.get(var1);
         }
      }

      public Object put(Object var1, Object var2) {
         synchronized(this.mutex) {
            return this.backingMap.put(var1, var2);
         }
      }

      public Object remove(Object var1) {
         synchronized(this.mutex) {
            return this.backingMap.remove(var1);
         }
      }

      public void putAll(Map var1) {
         synchronized(this.mutex) {
            this.backingMap.putAll(var1);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.backingMap.clear();
         }
      }

      public Set keySet() {
         synchronized(this.mutex) {
            if (this.keySet == null) {
               this.keySet = new SynchronizedSet(this.backingMap.keySet(), this.mutex);
            }

            return this.keySet;
         }
      }

      public Collection values() {
         synchronized(this.mutex) {
            if (this.values == null) {
               this.values = new SynchronizedCollection(this.backingMap.values(), this.mutex);
            }

            return this.values;
         }
      }

      public Set entrySet() {
         synchronized(this.mutex) {
            if (this.entrySet == null) {
               this.entrySet = new SynchronizedSet(this.backingMap.entrySet(), this.mutex);
            }

            return this.entrySet;
         }
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.backingMap.equals(var1);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.backingMap.hashCode();
         }
      }
   }

   private static class CheckedObservableMap extends AbstractMap implements ObservableMap {
      private final ObservableMap backingMap;
      private final Class keyType;
      private final Class valueType;
      private MapListenerHelper listenerHelper;
      private final MapChangeListener listener;
      private transient Set entrySet = null;

      CheckedObservableMap(ObservableMap var1, Class var2, Class var3) {
         this.backingMap = var1;
         this.keyType = var2;
         this.valueType = var3;
         this.listener = (var1x) -> {
            this.callObservers(new MapAdapterChange(this, var1x));
         };
         this.backingMap.addListener(new WeakMapChangeListener(this.listener));
      }

      private void callObservers(MapChangeListener.Change var1) {
         MapListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
      }

      void typeCheck(Object var1, Object var2) {
         if (var1 != null && !this.keyType.isInstance(var1)) {
            throw new ClassCastException("Attempt to insert " + var1.getClass() + " key into map with key type " + this.keyType);
         } else if (var2 != null && !this.valueType.isInstance(var2)) {
            throw new ClassCastException("Attempt to insert " + var2.getClass() + " value into map with value type " + this.valueType);
         }
      }

      public void addListener(InvalidationListener var1) {
         this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public void addListener(MapChangeListener var1) {
         this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(MapChangeListener var1) {
         this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public int size() {
         return this.backingMap.size();
      }

      public boolean isEmpty() {
         return this.backingMap.isEmpty();
      }

      public boolean containsKey(Object var1) {
         return this.backingMap.containsKey(var1);
      }

      public boolean containsValue(Object var1) {
         return this.backingMap.containsValue(var1);
      }

      public Object get(Object var1) {
         return this.backingMap.get(var1);
      }

      public Object put(Object var1, Object var2) {
         this.typeCheck(var1, var2);
         return this.backingMap.put(var1, var2);
      }

      public Object remove(Object var1) {
         return this.backingMap.remove(var1);
      }

      public void putAll(Map var1) {
         Object[] var2 = var1.entrySet().toArray();
         ArrayList var3 = new ArrayList(var2.length);
         Object[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Object var7 = var4[var6];
            Map.Entry var8 = (Map.Entry)var7;
            Object var9 = var8.getKey();
            Object var10 = var8.getValue();
            this.typeCheck(var9, var10);
            var3.add(new AbstractMap.SimpleImmutableEntry(var9, var10));
         }

         Iterator var11 = var3.iterator();

         while(var11.hasNext()) {
            Map.Entry var12 = (Map.Entry)var11.next();
            this.backingMap.put(var12.getKey(), var12.getValue());
         }

      }

      public void clear() {
         this.backingMap.clear();
      }

      public Set keySet() {
         return this.backingMap.keySet();
      }

      public Collection values() {
         return this.backingMap.values();
      }

      public Set entrySet() {
         if (this.entrySet == null) {
            this.entrySet = new CheckedEntrySet(this.backingMap.entrySet(), this.valueType);
         }

         return this.entrySet;
      }

      public boolean equals(Object var1) {
         return var1 == this || this.backingMap.equals(var1);
      }

      public int hashCode() {
         return this.backingMap.hashCode();
      }

      static class CheckedEntrySet implements Set {
         private final Set s;
         private final Class valueType;

         CheckedEntrySet(Set var1, Class var2) {
            this.s = var1;
            this.valueType = var2;
         }

         public int size() {
            return this.s.size();
         }

         public boolean isEmpty() {
            return this.s.isEmpty();
         }

         public String toString() {
            return this.s.toString();
         }

         public int hashCode() {
            return this.s.hashCode();
         }

         public void clear() {
            this.s.clear();
         }

         public boolean add(Map.Entry var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection var1) {
            throw new UnsupportedOperationException();
         }

         public Iterator iterator() {
            final Iterator var1 = this.s.iterator();
            final Class var2 = this.valueType;
            return new Iterator() {
               public boolean hasNext() {
                  return var1.hasNext();
               }

               public void remove() {
                  var1.remove();
               }

               public Map.Entry next() {
                  return FXCollections.CheckedObservableMap.CheckedEntrySet.checkedEntry((Map.Entry)var1.next(), var2);
               }
            };
         }

         public Object[] toArray() {
            Object[] var1 = this.s.toArray();
            Object[] var2 = CheckedEntry.class.isInstance(var1.getClass().getComponentType()) ? var1 : new Object[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2[var3] = checkedEntry((Map.Entry)var1[var3], this.valueType);
            }

            return var2;
         }

         public Object[] toArray(Object[] var1) {
            Object[] var2 = this.s.toArray(var1.length == 0 ? var1 : Arrays.copyOf(var1, 0));

            for(int var3 = 0; var3 < var2.length; ++var3) {
               var2[var3] = checkedEntry((Map.Entry)var2[var3], this.valueType);
            }

            if (var2.length > var1.length) {
               return var2;
            } else {
               System.arraycopy(var2, 0, var1, 0, var2.length);
               if (var1.length > var2.length) {
                  var1[var2.length] = null;
               }

               return var1;
            }
         }

         public boolean contains(Object var1) {
            if (!(var1 instanceof Map.Entry)) {
               return false;
            } else {
               Map.Entry var2 = (Map.Entry)var1;
               return this.s.contains(var2 instanceof CheckedEntry ? var2 : checkedEntry(var2, this.valueType));
            }
         }

         public boolean containsAll(Collection var1) {
            Iterator var2 = var1.iterator();

            Object var3;
            do {
               if (!var2.hasNext()) {
                  return true;
               }

               var3 = var2.next();
            } while(this.contains(var3));

            return false;
         }

         public boolean remove(Object var1) {
            return !(var1 instanceof Map.Entry) ? false : this.s.remove(new AbstractMap.SimpleImmutableEntry((Map.Entry)var1));
         }

         public boolean removeAll(Collection var1) {
            return this.batchRemove(var1, false);
         }

         public boolean retainAll(Collection var1) {
            return this.batchRemove(var1, true);
         }

         private boolean batchRemove(Collection var1, boolean var2) {
            boolean var3 = false;
            Iterator var4 = this.iterator();

            while(var4.hasNext()) {
               if (var1.contains(var4.next()) != var2) {
                  var4.remove();
                  var3 = true;
               }
            }

            return var3;
         }

         public boolean equals(Object var1) {
            if (var1 == this) {
               return true;
            } else if (!(var1 instanceof Set)) {
               return false;
            } else {
               Set var2 = (Set)var1;
               return var2.size() == this.s.size() && this.containsAll(var2);
            }
         }

         static CheckedEntry checkedEntry(Map.Entry var0, Class var1) {
            return new CheckedEntry(var0, var1);
         }

         private static class CheckedEntry implements Map.Entry {
            private final Map.Entry e;
            private final Class valueType;

            CheckedEntry(Map.Entry var1, Class var2) {
               this.e = var1;
               this.valueType = var2;
            }

            public Object getKey() {
               return this.e.getKey();
            }

            public Object getValue() {
               return this.e.getValue();
            }

            public int hashCode() {
               return this.e.hashCode();
            }

            public String toString() {
               return this.e.toString();
            }

            public Object setValue(Object var1) {
               if (var1 != null && !this.valueType.isInstance(var1)) {
                  throw new ClassCastException(this.badValueMsg(var1));
               } else {
                  return this.e.setValue(var1);
               }
            }

            private String badValueMsg(Object var1) {
               return "Attempt to insert " + var1.getClass() + " value into map with value type " + this.valueType;
            }

            public boolean equals(Object var1) {
               if (var1 == this) {
                  return true;
               } else {
                  return !(var1 instanceof Map.Entry) ? false : this.e.equals(new AbstractMap.SimpleImmutableEntry((Map.Entry)var1));
               }
            }
         }
      }
   }

   private static class EmptyObservableMap extends AbstractMap implements ObservableMap {
      public EmptyObservableMap() {
      }

      public void addListener(InvalidationListener var1) {
      }

      public void removeListener(InvalidationListener var1) {
      }

      public void addListener(MapChangeListener var1) {
      }

      public void removeListener(MapChangeListener var1) {
      }

      public int size() {
         return 0;
      }

      public boolean isEmpty() {
         return true;
      }

      public boolean containsKey(Object var1) {
         return false;
      }

      public boolean containsValue(Object var1) {
         return false;
      }

      public Object get(Object var1) {
         return null;
      }

      public Set keySet() {
         return FXCollections.EMPTY_OBSERVABLE_SET;
      }

      public Collection values() {
         return FXCollections.EMPTY_OBSERVABLE_SET;
      }

      public Set entrySet() {
         return FXCollections.EMPTY_OBSERVABLE_SET;
      }

      public boolean equals(Object var1) {
         return var1 instanceof Map && ((Map)var1).isEmpty();
      }

      public int hashCode() {
         return 0;
      }
   }

   private static class CheckedObservableSet extends AbstractSet implements ObservableSet {
      private final ObservableSet backingSet;
      private final Class type;
      private SetListenerHelper listenerHelper;
      private final SetChangeListener listener;

      CheckedObservableSet(ObservableSet var1, Class var2) {
         if (var1 != null && var2 != null) {
            this.backingSet = var1;
            this.type = var2;
            this.listener = (var1x) -> {
               this.callObservers(new SetAdapterChange(this, var1x));
            };
            this.backingSet.addListener(new WeakSetChangeListener(this.listener));
         } else {
            throw new NullPointerException();
         }
      }

      private void callObservers(SetChangeListener.Change var1) {
         SetListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
      }

      void typeCheck(Object var1) {
         if (var1 != null && !this.type.isInstance(var1)) {
            throw new ClassCastException("Attempt to insert " + var1.getClass() + " element into collection with element type " + this.type);
         }
      }

      public void addListener(InvalidationListener var1) {
         this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public void addListener(SetChangeListener var1) {
         this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(SetChangeListener var1) {
         this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public int size() {
         return this.backingSet.size();
      }

      public boolean isEmpty() {
         return this.backingSet.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.backingSet.contains(var1);
      }

      public Object[] toArray() {
         return this.backingSet.toArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.backingSet.toArray(var1);
      }

      public boolean add(Object var1) {
         this.typeCheck(var1);
         return this.backingSet.add(var1);
      }

      public boolean remove(Object var1) {
         return this.backingSet.remove(var1);
      }

      public boolean containsAll(Collection var1) {
         return this.backingSet.containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         Object[] var2 = null;

         try {
            var2 = var1.toArray((Object[])((Object[])Array.newInstance(this.type, 0)));
         } catch (ArrayStoreException var4) {
            throw new ClassCastException();
         }

         return this.backingSet.addAll(Arrays.asList(var2));
      }

      public boolean retainAll(Collection var1) {
         return this.backingSet.retainAll(var1);
      }

      public boolean removeAll(Collection var1) {
         return this.backingSet.removeAll(var1);
      }

      public void clear() {
         this.backingSet.clear();
      }

      public boolean equals(Object var1) {
         return var1 == this || this.backingSet.equals(var1);
      }

      public int hashCode() {
         return this.backingSet.hashCode();
      }

      public Iterator iterator() {
         final Iterator var1 = this.backingSet.iterator();
         return new Iterator() {
            public boolean hasNext() {
               return var1.hasNext();
            }

            public Object next() {
               return var1.next();
            }

            public void remove() {
               var1.remove();
            }
         };
      }
   }

   private static class SynchronizedObservableSet extends SynchronizedSet implements ObservableSet {
      private final ObservableSet backingSet;
      private SetListenerHelper listenerHelper;
      private final SetChangeListener listener;

      SynchronizedObservableSet(ObservableSet var1, Object var2) {
         super(var1, var2);
         this.backingSet = var1;
         this.listener = (var1x) -> {
            SetListenerHelper.fireValueChangedEvent(this.listenerHelper, new SetAdapterChange(this, var1x));
         };
         this.backingSet.addListener(new WeakSetChangeListener(this.listener));
      }

      SynchronizedObservableSet(ObservableSet var1) {
         this(var1, new Object());
      }

      public void addListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
         }
      }

      public void removeListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
         }
      }

      public void addListener(SetChangeListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
         }
      }

      public void removeListener(SetChangeListener var1) {
         synchronized(this.mutex) {
            this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
         }
      }
   }

   private static class SynchronizedSet implements Set {
      final Object mutex;
      private final Set backingSet;

      SynchronizedSet(Set var1, Object var2) {
         this.backingSet = var1;
         this.mutex = var2;
      }

      SynchronizedSet(Set var1) {
         this(var1, new Object());
      }

      public int size() {
         synchronized(this.mutex) {
            return this.backingSet.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.backingSet.isEmpty();
         }
      }

      public boolean contains(Object var1) {
         synchronized(this.mutex) {
            return this.backingSet.contains(var1);
         }
      }

      public Iterator iterator() {
         return this.backingSet.iterator();
      }

      public Object[] toArray() {
         synchronized(this.mutex) {
            return this.backingSet.toArray();
         }
      }

      public Object[] toArray(Object[] var1) {
         synchronized(this.mutex) {
            return this.backingSet.toArray(var1);
         }
      }

      public boolean add(Object var1) {
         synchronized(this.mutex) {
            return this.backingSet.add(var1);
         }
      }

      public boolean remove(Object var1) {
         synchronized(this.mutex) {
            return this.backingSet.remove(var1);
         }
      }

      public boolean containsAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingSet.containsAll(var1);
         }
      }

      public boolean addAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingSet.addAll(var1);
         }
      }

      public boolean retainAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingSet.retainAll(var1);
         }
      }

      public boolean removeAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingSet.removeAll(var1);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.backingSet.clear();
         }
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.backingSet.equals(var1);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.backingSet.hashCode();
         }
      }
   }

   private static class UnmodifiableObservableSet extends AbstractSet implements ObservableSet {
      private final ObservableSet backingSet;
      private SetListenerHelper listenerHelper;
      private SetChangeListener listener;

      public UnmodifiableObservableSet(ObservableSet var1) {
         this.backingSet = var1;
         this.listener = null;
      }

      private void initListener() {
         if (this.listener == null) {
            this.listener = (var1) -> {
               this.callObservers(new SetAdapterChange(this, var1));
            };
            this.backingSet.addListener(new WeakSetChangeListener(this.listener));
         }

      }

      private void callObservers(SetChangeListener.Change var1) {
         SetListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
      }

      public Iterator iterator() {
         return new Iterator() {
            private final Iterator i;

            {
               this.i = UnmodifiableObservableSet.this.backingSet.iterator();
            }

            public boolean hasNext() {
               return this.i.hasNext();
            }

            public Object next() {
               return this.i.next();
            }

            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }

      public int size() {
         return this.backingSet.size();
      }

      public void addListener(InvalidationListener var1) {
         this.initListener();
         this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(InvalidationListener var1) {
         this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public void addListener(SetChangeListener var1) {
         this.initListener();
         this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, var1);
      }

      public void removeListener(SetChangeListener var1) {
         this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, var1);
      }

      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }
   }

   private static class EmptyObservableSet extends AbstractSet implements ObservableSet {
      public EmptyObservableSet() {
      }

      public void addListener(InvalidationListener var1) {
      }

      public void removeListener(InvalidationListener var1) {
      }

      public void addListener(SetChangeListener var1) {
      }

      public void removeListener(SetChangeListener var1) {
      }

      public int size() {
         return 0;
      }

      public boolean isEmpty() {
         return true;
      }

      public boolean contains(Object var1) {
         return false;
      }

      public boolean containsAll(Collection var1) {
         return var1.isEmpty();
      }

      public Object[] toArray() {
         return new Object[0];
      }

      public Object[] toArray(Object[] var1) {
         if (var1.length > 0) {
            var1[0] = null;
         }

         return var1;
      }

      public Iterator iterator() {
         return new Iterator() {
            public boolean hasNext() {
               return false;
            }

            public Object next() {
               throw new NoSuchElementException();
            }

            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }
   }

   private static class CheckedObservableList extends ObservableListBase implements ObservableList {
      private final ObservableList list;
      private final Class type;
      private final ListChangeListener listener;

      CheckedObservableList(ObservableList var1, Class var2) {
         if (var1 != null && var2 != null) {
            this.list = var1;
            this.type = var2;
            this.listener = (var1x) -> {
               this.fireChange(new SourceAdapterChange(this, var1x));
            };
            var1.addListener(new WeakListChangeListener(this.listener));
         } else {
            throw new NullPointerException();
         }
      }

      void typeCheck(Object var1) {
         if (var1 != null && !this.type.isInstance(var1)) {
            throw new ClassCastException("Attempt to insert " + var1.getClass() + " element into collection with element type " + this.type);
         }
      }

      public int size() {
         return this.list.size();
      }

      public boolean isEmpty() {
         return this.list.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.list.contains(var1);
      }

      public Object[] toArray() {
         return this.list.toArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.list.toArray(var1);
      }

      public String toString() {
         return this.list.toString();
      }

      public boolean remove(Object var1) {
         return this.list.remove(var1);
      }

      public boolean containsAll(Collection var1) {
         return this.list.containsAll(var1);
      }

      public boolean removeAll(Collection var1) {
         return this.list.removeAll(var1);
      }

      public boolean retainAll(Collection var1) {
         return this.list.retainAll(var1);
      }

      public boolean removeAll(Object... var1) {
         return this.list.removeAll(var1);
      }

      public boolean retainAll(Object... var1) {
         return this.list.retainAll(var1);
      }

      public void remove(int var1, int var2) {
         this.list.remove(var1, var2);
      }

      public void clear() {
         this.list.clear();
      }

      public boolean equals(Object var1) {
         return var1 == this || this.list.equals(var1);
      }

      public int hashCode() {
         return this.list.hashCode();
      }

      public Object get(int var1) {
         return this.list.get(var1);
      }

      public Object remove(int var1) {
         return this.list.remove(var1);
      }

      public int indexOf(Object var1) {
         return this.list.indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         return this.list.lastIndexOf(var1);
      }

      public Object set(int var1, Object var2) {
         this.typeCheck(var2);
         return this.list.set(var1, var2);
      }

      public void add(int var1, Object var2) {
         this.typeCheck(var2);
         this.list.add(var1, var2);
      }

      public boolean addAll(int var1, Collection var2) {
         Object[] var3 = null;

         try {
            var3 = var2.toArray((Object[])((Object[])Array.newInstance(this.type, 0)));
         } catch (ArrayStoreException var5) {
            throw new ClassCastException();
         }

         return this.list.addAll(var1, Arrays.asList(var3));
      }

      public boolean addAll(Collection var1) {
         Object[] var2 = null;

         try {
            var2 = var1.toArray((Object[])((Object[])Array.newInstance(this.type, 0)));
         } catch (ArrayStoreException var4) {
            throw new ClassCastException();
         }

         return this.list.addAll(Arrays.asList(var2));
      }

      public ListIterator listIterator() {
         return this.listIterator(0);
      }

      public ListIterator listIterator(final int var1) {
         return new ListIterator() {
            ListIterator i;

            {
               this.i = CheckedObservableList.this.list.listIterator(var1);
            }

            public boolean hasNext() {
               return this.i.hasNext();
            }

            public Object next() {
               return this.i.next();
            }

            public boolean hasPrevious() {
               return this.i.hasPrevious();
            }

            public Object previous() {
               return this.i.previous();
            }

            public int nextIndex() {
               return this.i.nextIndex();
            }

            public int previousIndex() {
               return this.i.previousIndex();
            }

            public void remove() {
               this.i.remove();
            }

            public void set(Object var1x) {
               CheckedObservableList.this.typeCheck(var1x);
               this.i.set(var1x);
            }

            public void add(Object var1x) {
               CheckedObservableList.this.typeCheck(var1x);
               this.i.add(var1x);
            }
         };
      }

      public Iterator iterator() {
         return new Iterator() {
            private final Iterator it;

            {
               this.it = CheckedObservableList.this.list.iterator();
            }

            public boolean hasNext() {
               return this.it.hasNext();
            }

            public Object next() {
               return this.it.next();
            }

            public void remove() {
               this.it.remove();
            }
         };
      }

      public boolean add(Object var1) {
         this.typeCheck(var1);
         return this.list.add(var1);
      }

      public List subList(int var1, int var2) {
         return Collections.checkedList(this.list.subList(var1, var2), this.type);
      }

      public boolean addAll(Object... var1) {
         try {
            Object[] var2 = (Object[])((Object[])Array.newInstance(this.type, var1.length));
            System.arraycopy(var1, 0, var2, 0, var1.length);
            return this.list.addAll(var2);
         } catch (ArrayStoreException var3) {
            throw new ClassCastException();
         }
      }

      public boolean setAll(Object... var1) {
         try {
            Object[] var2 = (Object[])((Object[])Array.newInstance(this.type, var1.length));
            System.arraycopy(var1, 0, var2, 0, var1.length);
            return this.list.setAll(var2);
         } catch (ArrayStoreException var3) {
            throw new ClassCastException();
         }
      }

      public boolean setAll(Collection var1) {
         Object[] var2 = null;

         try {
            var2 = var1.toArray((Object[])((Object[])Array.newInstance(this.type, 0)));
         } catch (ArrayStoreException var4) {
            throw new ClassCastException();
         }

         return this.list.setAll((Collection)Arrays.asList(var2));
      }
   }

   private static class SynchronizedObservableList extends SynchronizedList implements ObservableList {
      private ListListenerHelper helper;
      private final ObservableList backingList;
      private final ListChangeListener listener;

      SynchronizedObservableList(ObservableList var1, Object var2) {
         super(var1, var2);
         this.backingList = var1;
         this.listener = (var1x) -> {
            ListListenerHelper.fireValueChangedEvent(this.helper, new SourceAdapterChange(this, var1x));
         };
         this.backingList.addListener(new WeakListChangeListener(this.listener));
      }

      SynchronizedObservableList(ObservableList var1) {
         this(var1, new Object());
      }

      public boolean addAll(Object... var1) {
         synchronized(this.mutex) {
            return this.backingList.addAll(var1);
         }
      }

      public boolean setAll(Object... var1) {
         synchronized(this.mutex) {
            return this.backingList.setAll(var1);
         }
      }

      public boolean removeAll(Object... var1) {
         synchronized(this.mutex) {
            return this.backingList.removeAll(var1);
         }
      }

      public boolean retainAll(Object... var1) {
         synchronized(this.mutex) {
            return this.backingList.retainAll(var1);
         }
      }

      public void remove(int var1, int var2) {
         synchronized(this.mutex) {
            this.backingList.remove(var1, var2);
         }
      }

      public boolean setAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingList.setAll(var1);
         }
      }

      public final void addListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.helper = ListListenerHelper.addListener(this.helper, var1);
         }
      }

      public final void removeListener(InvalidationListener var1) {
         synchronized(this.mutex) {
            this.helper = ListListenerHelper.removeListener(this.helper, var1);
         }
      }

      public void addListener(ListChangeListener var1) {
         synchronized(this.mutex) {
            this.helper = ListListenerHelper.addListener(this.helper, var1);
         }
      }

      public void removeListener(ListChangeListener var1) {
         synchronized(this.mutex) {
            this.helper = ListListenerHelper.removeListener(this.helper, var1);
         }
      }
   }

   private static class SynchronizedList implements List {
      final Object mutex;
      private final List backingList;

      SynchronizedList(List var1, Object var2) {
         this.backingList = var1;
         this.mutex = var2;
      }

      public int size() {
         synchronized(this.mutex) {
            return this.backingList.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.backingList.isEmpty();
         }
      }

      public boolean contains(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.contains(var1);
         }
      }

      public Iterator iterator() {
         return this.backingList.iterator();
      }

      public Object[] toArray() {
         synchronized(this.mutex) {
            return this.backingList.toArray();
         }
      }

      public Object[] toArray(Object[] var1) {
         synchronized(this.mutex) {
            return this.backingList.toArray(var1);
         }
      }

      public boolean add(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.add(var1);
         }
      }

      public boolean remove(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.remove(var1);
         }
      }

      public boolean containsAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingList.containsAll(var1);
         }
      }

      public boolean addAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingList.addAll(var1);
         }
      }

      public boolean addAll(int var1, Collection var2) {
         synchronized(this.mutex) {
            return this.backingList.addAll(var1, var2);
         }
      }

      public boolean removeAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingList.removeAll(var1);
         }
      }

      public boolean retainAll(Collection var1) {
         synchronized(this.mutex) {
            return this.backingList.retainAll(var1);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.backingList.clear();
         }
      }

      public Object get(int var1) {
         synchronized(this.mutex) {
            return this.backingList.get(var1);
         }
      }

      public Object set(int var1, Object var2) {
         synchronized(this.mutex) {
            return this.backingList.set(var1, var2);
         }
      }

      public void add(int var1, Object var2) {
         synchronized(this.mutex) {
            this.backingList.add(var1, var2);
         }
      }

      public Object remove(int var1) {
         synchronized(this.mutex) {
            return this.backingList.remove(var1);
         }
      }

      public int indexOf(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.indexOf(var1);
         }
      }

      public int lastIndexOf(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.lastIndexOf(var1);
         }
      }

      public ListIterator listIterator() {
         return this.backingList.listIterator();
      }

      public ListIterator listIterator(int var1) {
         synchronized(this.mutex) {
            return this.backingList.listIterator(var1);
         }
      }

      public List subList(int var1, int var2) {
         synchronized(this.mutex) {
            return new SynchronizedList(this.backingList.subList(var1, var2), this.mutex);
         }
      }

      public String toString() {
         synchronized(this.mutex) {
            return this.backingList.toString();
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.backingList.hashCode();
         }
      }

      public boolean equals(Object var1) {
         synchronized(this.mutex) {
            return this.backingList.equals(var1);
         }
      }
   }

   private static class UnmodifiableObservableListImpl extends ObservableListBase implements ObservableList {
      private final ObservableList backingList;
      private final ListChangeListener listener;

      public UnmodifiableObservableListImpl(ObservableList var1) {
         this.backingList = var1;
         this.listener = (var1x) -> {
            this.fireChange(new SourceAdapterChange(this, var1x));
         };
         this.backingList.addListener(new WeakListChangeListener(this.listener));
      }

      public Object get(int var1) {
         return this.backingList.get(var1);
      }

      public int size() {
         return this.backingList.size();
      }

      public boolean addAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public void remove(int var1, int var2) {
         throw new UnsupportedOperationException();
      }
   }

   private static class SingletonObservableList extends AbstractList implements ObservableList {
      private final Object element;

      public SingletonObservableList(Object var1) {
         if (var1 == null) {
            throw new NullPointerException();
         } else {
            this.element = var1;
         }
      }

      public boolean addAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public void remove(int var1, int var2) {
         throw new UnsupportedOperationException();
      }

      public void addListener(InvalidationListener var1) {
      }

      public void removeListener(InvalidationListener var1) {
      }

      public void addListener(ListChangeListener var1) {
      }

      public void removeListener(ListChangeListener var1) {
      }

      public int size() {
         return 1;
      }

      public boolean isEmpty() {
         return false;
      }

      public boolean contains(Object var1) {
         return this.element.equals(var1);
      }

      public Object get(int var1) {
         if (var1 != 0) {
            throw new IndexOutOfBoundsException();
         } else {
            return this.element;
         }
      }
   }

   private static class EmptyObservableList extends AbstractList implements ObservableList {
      private static final ListIterator iterator = new ListIterator() {
         public boolean hasNext() {
            return false;
         }

         public Object next() {
            throw new NoSuchElementException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         public boolean hasPrevious() {
            return false;
         }

         public Object previous() {
            throw new NoSuchElementException();
         }

         public int nextIndex() {
            return 0;
         }

         public int previousIndex() {
            return -1;
         }

         public void set(Object var1) {
            throw new UnsupportedOperationException();
         }

         public void add(Object var1) {
            throw new UnsupportedOperationException();
         }
      };

      public EmptyObservableList() {
      }

      public final void addListener(InvalidationListener var1) {
      }

      public final void removeListener(InvalidationListener var1) {
      }

      public void addListener(ListChangeListener var1) {
      }

      public void removeListener(ListChangeListener var1) {
      }

      public int size() {
         return 0;
      }

      public boolean contains(Object var1) {
         return false;
      }

      public Iterator iterator() {
         return iterator;
      }

      public boolean containsAll(Collection var1) {
         return var1.isEmpty();
      }

      public Object get(int var1) {
         throw new IndexOutOfBoundsException();
      }

      public int indexOf(Object var1) {
         return -1;
      }

      public int lastIndexOf(Object var1) {
         return -1;
      }

      public ListIterator listIterator() {
         return iterator;
      }

      public ListIterator listIterator(int var1) {
         if (var1 != 0) {
            throw new IndexOutOfBoundsException();
         } else {
            return iterator;
         }
      }

      public List subList(int var1, int var2) {
         if (var1 == 0 && var2 == 0) {
            return this;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public boolean addAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean setAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Object... var1) {
         throw new UnsupportedOperationException();
      }

      public void remove(int var1, int var2) {
         throw new UnsupportedOperationException();
      }
   }
}
