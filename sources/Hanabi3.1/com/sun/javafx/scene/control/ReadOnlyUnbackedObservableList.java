package com.sun.javafx.scene.control;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ReadOnlyUnbackedObservableList implements ObservableList {
   private ListListenerHelper listenerHelper;

   public abstract Object get(int var1);

   public abstract int size();

   public void addListener(InvalidationListener var1) {
      this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, var1);
   }

   public void addListener(ListChangeListener var1) {
      this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, var1);
   }

   public void removeListener(ListChangeListener var1) {
      this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, var1);
   }

   public void callObservers(ListChangeListener.Change var1) {
      ListListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
   }

   public int indexOf(Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < this.size(); ++var2) {
            Object var3 = this.get(var2);
            if (var1.equals(var3)) {
               return var2;
            }
         }

         return -1;
      }
   }

   public int lastIndexOf(Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = this.size() - 1; var2 >= 0; --var2) {
            Object var3 = this.get(var2);
            if (var1.equals(var3)) {
               return var2;
            }
         }

         return -1;
      }
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) != -1;
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

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public ListIterator listIterator() {
      return new SelectionListIterator(this);
   }

   public ListIterator listIterator(int var1) {
      return new SelectionListIterator(this, var1);
   }

   public Iterator iterator() {
      return new SelectionListIterator(this);
   }

   public List subList(final int var1, final int var2) {
      if (var1 >= 0 && var2 <= this.size() && var1 <= var2) {
         return new ReadOnlyUnbackedObservableList() {
            public Object get(int var1x) {
               return ReadOnlyUnbackedObservableList.this.get(var1x + var1);
            }

            public int size() {
               return var2 - var1;
            }
         };
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this.size()];

      for(int var2 = 0; var2 < this.size(); ++var2) {
         var1[var2] = this.get(var2);
      }

      return var1;
   }

   public Object[] toArray(Object[] var1) {
      Object[] var2 = this.toArray();
      int var3 = var2.length;
      if (var1.length < var3) {
         return (Object[])Arrays.copyOf(var2, var3, var1.getClass());
      } else {
         System.arraycopy(var2, 0, var1, 0, var3);
         if (var1.length > var3) {
            var1[var3] = null;
         }

         return var1;
      }
   }

   public String toString() {
      Iterator var1 = this.iterator();
      if (!var1.hasNext()) {
         return "[]";
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append('[');

         while(true) {
            Object var3 = var1.next();
            var2.append(var3 == this ? "(this Collection)" : var3);
            if (!var1.hasNext()) {
               return var2.append(']').toString();
            }

            var2.append(", ");
         }
      }
   }

   public boolean add(Object var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public void add(int var1, Object var2) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean addAll(Object... var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public Object set(int var1, Object var2) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean setAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean setAll(Object... var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public void clear() {
      throw new UnsupportedOperationException("Not supported.");
   }

   public Object remove(int var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public void remove(int var1, int var2) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean removeAll(Object... var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public boolean retainAll(Object... var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   private static class SelectionListIterator implements ListIterator {
      private int pos;
      private final ReadOnlyUnbackedObservableList list;

      public SelectionListIterator(ReadOnlyUnbackedObservableList var1) {
         this(var1, 0);
      }

      public SelectionListIterator(ReadOnlyUnbackedObservableList var1, int var2) {
         this.list = var1;
         this.pos = var2;
      }

      public boolean hasNext() {
         return this.pos < this.list.size();
      }

      public Object next() {
         return this.list.get(this.pos++);
      }

      public boolean hasPrevious() {
         return this.pos > 0;
      }

      public Object previous() {
         return this.list.get(this.pos--);
      }

      public int nextIndex() {
         return this.pos + 1;
      }

      public int previousIndex() {
         return this.pos - 1;
      }

      public void remove() {
         throw new UnsupportedOperationException("Not supported.");
      }

      public void set(Object var1) {
         throw new UnsupportedOperationException("Not supported.");
      }

      public void add(Object var1) {
         throw new UnsupportedOperationException("Not supported.");
      }
   }
}
