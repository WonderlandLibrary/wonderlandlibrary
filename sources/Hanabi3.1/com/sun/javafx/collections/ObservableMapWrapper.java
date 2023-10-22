package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class ObservableMapWrapper implements ObservableMap {
   private ObservableEntrySet entrySet;
   private ObservableKeySet keySet;
   private ObservableValues values;
   private MapListenerHelper listenerHelper;
   private final Map backingMap;

   public ObservableMapWrapper(Map var1) {
      this.backingMap = var1;
   }

   protected void callObservers(MapChangeListener.Change var1) {
      MapListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
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
      Object var3;
      if (this.backingMap.containsKey(var1)) {
         var3 = this.backingMap.put(var1, var2);
         if (var3 == null && var2 != null || var3 != null && !var3.equals(var2)) {
            this.callObservers(new SimpleChange(var1, var3, var2, true, true));
         }
      } else {
         var3 = this.backingMap.put(var1, var2);
         this.callObservers(new SimpleChange(var1, var3, var2, true, false));
      }

      return var3;
   }

   public Object remove(Object var1) {
      if (!this.backingMap.containsKey(var1)) {
         return null;
      } else {
         Object var2 = this.backingMap.remove(var1);
         this.callObservers(new SimpleChange(var1, var2, (Object)null, false, true));
         return var2;
      }
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      Iterator var1 = this.backingMap.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         Object var3 = var2.getKey();
         Object var4 = var2.getValue();
         var1.remove();
         this.callObservers(new SimpleChange(var3, var4, (Object)null, false, true));
      }

   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new ObservableKeySet();
      }

      return this.keySet;
   }

   public Collection values() {
      if (this.values == null) {
         this.values = new ObservableValues();
      }

      return this.values;
   }

   public Set entrySet() {
      if (this.entrySet == null) {
         this.entrySet = new ObservableEntrySet();
      }

      return this.entrySet;
   }

   public String toString() {
      return this.backingMap.toString();
   }

   public boolean equals(Object var1) {
      return this.backingMap.equals(var1);
   }

   public int hashCode() {
      return this.backingMap.hashCode();
   }

   private class ObservableEntrySet implements Set {
      private ObservableEntrySet() {
      }

      public int size() {
         return ObservableMapWrapper.this.backingMap.size();
      }

      public boolean isEmpty() {
         return ObservableMapWrapper.this.backingMap.isEmpty();
      }

      public boolean contains(Object var1) {
         return ObservableMapWrapper.this.backingMap.entrySet().contains(var1);
      }

      public Iterator iterator() {
         return new Iterator() {
            private Iterator backingIt;
            private Object lastKey;
            private Object lastValue;

            {
               this.backingIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            }

            public boolean hasNext() {
               return this.backingIt.hasNext();
            }

            public Map.Entry next() {
               Map.Entry var1 = (Map.Entry)this.backingIt.next();
               this.lastKey = var1.getKey();
               this.lastValue = var1.getValue();
               return ObservableMapWrapper.this.new ObservableEntry(var1);
            }

            public void remove() {
               this.backingIt.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(this.lastKey, this.lastValue, (Object)null, false, true));
            }
         };
      }

      public Object[] toArray() {
         Object[] var1 = ObservableMapWrapper.this.backingMap.entrySet().toArray();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = ObservableMapWrapper.this.new ObservableEntry((Map.Entry)var1[var2]);
         }

         return var1;
      }

      public Object[] toArray(Object[] var1) {
         Object[] var2 = ObservableMapWrapper.this.backingMap.entrySet().toArray(var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = ObservableMapWrapper.this.new ObservableEntry((Map.Entry)var2[var3]);
         }

         return var2;
      }

      public boolean add(Map.Entry var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean remove(Object var1) {
         boolean var2 = ObservableMapWrapper.this.backingMap.entrySet().remove(var1);
         if (var2) {
            Map.Entry var3 = (Map.Entry)var1;
            ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(var3.getKey(), var3.getValue(), (Object)null, false, true));
         }

         return var2;
      }

      public boolean containsAll(Collection var1) {
         return ObservableMapWrapper.this.backingMap.entrySet().containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean retainAll(Collection var1) {
         return this.removeRetain(var1, false);
      }

      private boolean removeRetain(Collection var1, boolean var2) {
         boolean var3 = false;
         Iterator var4 = ObservableMapWrapper.this.backingMap.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            if (var2 == var1.contains(var5)) {
               var3 = true;
               Object var6 = var5.getKey();
               Object var7 = var5.getValue();
               var4.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(var6, var7, (Object)null, false, true));
            }
         }

         return var3;
      }

      public boolean removeAll(Collection var1) {
         return this.removeRetain(var1, true);
      }

      public void clear() {
         ObservableMapWrapper.this.clear();
      }

      public String toString() {
         return ObservableMapWrapper.this.backingMap.entrySet().toString();
      }

      public boolean equals(Object var1) {
         return ObservableMapWrapper.this.backingMap.entrySet().equals(var1);
      }

      public int hashCode() {
         return ObservableMapWrapper.this.backingMap.entrySet().hashCode();
      }

      // $FF: synthetic method
      ObservableEntrySet(Object var2) {
         this();
      }
   }

   private class ObservableEntry implements Map.Entry {
      private final Map.Entry backingEntry;

      public ObservableEntry(Map.Entry var2) {
         this.backingEntry = var2;
      }

      public Object getKey() {
         return this.backingEntry.getKey();
      }

      public Object getValue() {
         return this.backingEntry.getValue();
      }

      public Object setValue(Object var1) {
         Object var2 = this.backingEntry.setValue(var1);
         ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(this.getKey(), var2, var1, true, true));
         return var2;
      }

      public final boolean equals(Object var1) {
         if (!(var1 instanceof Map.Entry)) {
            return false;
         } else {
            Map.Entry var2 = (Map.Entry)var1;
            Object var3 = this.getKey();
            Object var4 = var2.getKey();
            if (var3 == var4 || var3 != null && var3.equals(var4)) {
               Object var5 = this.getValue();
               Object var6 = var2.getValue();
               if (var5 == var6 || var5 != null && var5.equals(var6)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final int hashCode() {
         return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
      }

      public final String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   private class ObservableValues implements Collection {
      private ObservableValues() {
      }

      public int size() {
         return ObservableMapWrapper.this.backingMap.size();
      }

      public boolean isEmpty() {
         return ObservableMapWrapper.this.backingMap.isEmpty();
      }

      public boolean contains(Object var1) {
         return ObservableMapWrapper.this.backingMap.values().contains(var1);
      }

      public Iterator iterator() {
         return new Iterator() {
            private Iterator entryIt;
            private Object lastKey;
            private Object lastValue;

            {
               this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            }

            public boolean hasNext() {
               return this.entryIt.hasNext();
            }

            public Object next() {
               Map.Entry var1 = (Map.Entry)this.entryIt.next();
               this.lastKey = var1.getKey();
               this.lastValue = var1.getValue();
               return this.lastValue;
            }

            public void remove() {
               this.entryIt.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(this.lastKey, this.lastValue, (Object)null, false, true));
            }
         };
      }

      public Object[] toArray() {
         return ObservableMapWrapper.this.backingMap.values().toArray();
      }

      public Object[] toArray(Object[] var1) {
         return ObservableMapWrapper.this.backingMap.values().toArray(var1);
      }

      public boolean add(Object var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean remove(Object var1) {
         Iterator var2 = this.iterator();

         do {
            if (!var2.hasNext()) {
               return false;
            }
         } while(!var2.next().equals(var1));

         var2.remove();
         return true;
      }

      public boolean containsAll(Collection var1) {
         return ObservableMapWrapper.this.backingMap.values().containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean removeAll(Collection var1) {
         return this.removeRetain(var1, true);
      }

      private boolean removeRetain(Collection var1, boolean var2) {
         boolean var3 = false;
         Iterator var4 = ObservableMapWrapper.this.backingMap.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            if (var2 == var1.contains(var5.getValue())) {
               var3 = true;
               Object var6 = var5.getKey();
               Object var7 = var5.getValue();
               var4.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(var6, var7, (Object)null, false, true));
            }
         }

         return var3;
      }

      public boolean retainAll(Collection var1) {
         return this.removeRetain(var1, false);
      }

      public void clear() {
         ObservableMapWrapper.this.clear();
      }

      public String toString() {
         return ObservableMapWrapper.this.backingMap.values().toString();
      }

      public boolean equals(Object var1) {
         return ObservableMapWrapper.this.backingMap.values().equals(var1);
      }

      public int hashCode() {
         return ObservableMapWrapper.this.backingMap.values().hashCode();
      }

      // $FF: synthetic method
      ObservableValues(Object var2) {
         this();
      }
   }

   private class ObservableKeySet implements Set {
      private ObservableKeySet() {
      }

      public int size() {
         return ObservableMapWrapper.this.backingMap.size();
      }

      public boolean isEmpty() {
         return ObservableMapWrapper.this.backingMap.isEmpty();
      }

      public boolean contains(Object var1) {
         return ObservableMapWrapper.this.backingMap.keySet().contains(var1);
      }

      public Iterator iterator() {
         return new Iterator() {
            private Iterator entryIt;
            private Object lastKey;
            private Object lastValue;

            {
               this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            }

            public boolean hasNext() {
               return this.entryIt.hasNext();
            }

            public Object next() {
               Map.Entry var1 = (Map.Entry)this.entryIt.next();
               this.lastKey = var1.getKey();
               this.lastValue = var1.getValue();
               return var1.getKey();
            }

            public void remove() {
               this.entryIt.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(this.lastKey, this.lastValue, (Object)null, false, true));
            }
         };
      }

      public Object[] toArray() {
         return ObservableMapWrapper.this.backingMap.keySet().toArray();
      }

      public Object[] toArray(Object[] var1) {
         return ObservableMapWrapper.this.backingMap.keySet().toArray(var1);
      }

      public boolean add(Object var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean remove(Object var1) {
         return ObservableMapWrapper.this.remove(var1) != null;
      }

      public boolean containsAll(Collection var1) {
         return ObservableMapWrapper.this.backingMap.keySet().containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public boolean retainAll(Collection var1) {
         return this.removeRetain(var1, false);
      }

      private boolean removeRetain(Collection var1, boolean var2) {
         boolean var3 = false;
         Iterator var4 = ObservableMapWrapper.this.backingMap.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            if (var2 == var1.contains(var5.getKey())) {
               var3 = true;
               Object var6 = var5.getKey();
               Object var7 = var5.getValue();
               var4.remove();
               ObservableMapWrapper.this.callObservers(ObservableMapWrapper.this.new SimpleChange(var6, var7, (Object)null, false, true));
            }
         }

         return var3;
      }

      public boolean removeAll(Collection var1) {
         return this.removeRetain(var1, true);
      }

      public void clear() {
         ObservableMapWrapper.this.clear();
      }

      public String toString() {
         return ObservableMapWrapper.this.backingMap.keySet().toString();
      }

      public boolean equals(Object var1) {
         return ObservableMapWrapper.this.backingMap.keySet().equals(var1);
      }

      public int hashCode() {
         return ObservableMapWrapper.this.backingMap.keySet().hashCode();
      }

      // $FF: synthetic method
      ObservableKeySet(Object var2) {
         this();
      }
   }

   private class SimpleChange extends MapChangeListener.Change {
      private final Object key;
      private final Object old;
      private final Object added;
      private final boolean wasAdded;
      private final boolean wasRemoved;

      public SimpleChange(Object var2, Object var3, Object var4, boolean var5, boolean var6) {
         super(ObservableMapWrapper.this);

         assert var5 || var6;

         this.key = var2;
         this.old = var3;
         this.added = var4;
         this.wasAdded = var5;
         this.wasRemoved = var6;
      }

      public boolean wasAdded() {
         return this.wasAdded;
      }

      public boolean wasRemoved() {
         return this.wasRemoved;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValueAdded() {
         return this.added;
      }

      public Object getValueRemoved() {
         return this.old;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         if (this.wasAdded) {
            if (this.wasRemoved) {
               var1.append("replaced ").append(this.old).append("by ").append(this.added);
            } else {
               var1.append("added ").append(this.added);
            }
         } else {
            var1.append("removed ").append(this.old);
         }

         var1.append(" at key ").append(this.key);
         return var1.toString();
      }
   }
}
