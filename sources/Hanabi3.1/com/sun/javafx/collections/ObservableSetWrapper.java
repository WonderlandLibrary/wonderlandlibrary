package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ObservableSetWrapper implements ObservableSet {
   private final Set backingSet;
   private SetListenerHelper listenerHelper;

   public ObservableSetWrapper(Set var1) {
      this.backingSet = var1;
   }

   private void callObservers(SetChangeListener.Change var1) {
      SetListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
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

   public Iterator iterator() {
      return new Iterator() {
         private final Iterator backingIt;
         private Object lastElement;

         {
            this.backingIt = ObservableSetWrapper.this.backingSet.iterator();
         }

         public boolean hasNext() {
            return this.backingIt.hasNext();
         }

         public Object next() {
            this.lastElement = this.backingIt.next();
            return this.lastElement;
         }

         public void remove() {
            this.backingIt.remove();
            ObservableSetWrapper.this.callObservers(ObservableSetWrapper.this.new SimpleRemoveChange(this.lastElement));
         }
      };
   }

   public Object[] toArray() {
      return this.backingSet.toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.backingSet.toArray(var1);
   }

   public boolean add(Object var1) {
      boolean var2 = this.backingSet.add(var1);
      if (var2) {
         this.callObservers(new SimpleAddChange(var1));
      }

      return var2;
   }

   public boolean remove(Object var1) {
      boolean var2 = this.backingSet.remove(var1);
      if (var2) {
         this.callObservers(new SimpleRemoveChange(var1));
      }

      return var2;
   }

   public boolean containsAll(Collection var1) {
      return this.backingSet.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      boolean var2 = false;

      Object var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 |= this.add(var4)) {
         var4 = var3.next();
      }

      return var2;
   }

   public boolean retainAll(Collection var1) {
      return this.removeRetain(var1, false);
   }

   public boolean removeAll(Collection var1) {
      return this.removeRetain(var1, true);
   }

   private boolean removeRetain(Collection var1, boolean var2) {
      boolean var3 = false;
      Iterator var4 = this.backingSet.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         if (var2 == var1.contains(var5)) {
            var3 = true;
            var4.remove();
            this.callObservers(new SimpleRemoveChange(var5));
         }
      }

      return var3;
   }

   public void clear() {
      Iterator var1 = this.backingSet.iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         var1.remove();
         this.callObservers(new SimpleRemoveChange(var2));
      }

   }

   public String toString() {
      return this.backingSet.toString();
   }

   public boolean equals(Object var1) {
      return this.backingSet.equals(var1);
   }

   public int hashCode() {
      return this.backingSet.hashCode();
   }

   private class SimpleRemoveChange extends SetChangeListener.Change {
      private final Object removed;

      public SimpleRemoveChange(Object var2) {
         super(ObservableSetWrapper.this);
         this.removed = var2;
      }

      public boolean wasAdded() {
         return false;
      }

      public boolean wasRemoved() {
         return true;
      }

      public Object getElementAdded() {
         return null;
      }

      public Object getElementRemoved() {
         return this.removed;
      }

      public String toString() {
         return "removed " + this.removed;
      }
   }

   private class SimpleAddChange extends SetChangeListener.Change {
      private final Object added;

      public SimpleAddChange(Object var2) {
         super(ObservableSetWrapper.this);
         this.added = var2;
      }

      public boolean wasAdded() {
         return true;
      }

      public boolean wasRemoved() {
         return false;
      }

      public Object getElementAdded() {
         return this.added;
      }

      public Object getElementRemoved() {
         return null;
      }

      public String toString() {
         return "added " + this.added;
      }
   }
}
