package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public final class ObservableSequentialListWrapper extends ModifiableObservableListBase implements ObservableList, SortableList {
   private final List backingList;
   private final ElementObserver elementObserver;
   private SortHelper helper;

   public ObservableSequentialListWrapper(List var1) {
      this.backingList = var1;
      this.elementObserver = null;
   }

   public ObservableSequentialListWrapper(List var1, Callback var2) {
      this.backingList = var1;
      this.elementObserver = new ElementObserver(var2, new Callback() {
         public InvalidationListener call(final Object var1) {
            return new InvalidationListener() {
               public void invalidated(Observable var1x) {
                  ObservableSequentialListWrapper.this.beginChange();
                  int var2 = 0;

                  for(Iterator var3 = ObservableSequentialListWrapper.this.backingList.iterator(); var3.hasNext(); ++var2) {
                     if (var3.next() == var1) {
                        ObservableSequentialListWrapper.this.nextUpdate(var2);
                     }
                  }

                  ObservableSequentialListWrapper.this.endChange();
               }
            };
         }
      }, this);
      Iterator var3 = this.backingList.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         this.elementObserver.attachListener(var4);
      }

   }

   public boolean contains(Object var1) {
      return this.backingList.contains(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.backingList.containsAll(var1);
   }

   public int indexOf(Object var1) {
      return this.backingList.indexOf(var1);
   }

   public int lastIndexOf(Object var1) {
      return this.backingList.lastIndexOf(var1);
   }

   public ListIterator listIterator(final int var1) {
      return new ListIterator() {
         private final ListIterator backingIt;
         private Object lastReturned;

         {
            this.backingIt = ObservableSequentialListWrapper.this.backingList.listIterator(var1);
         }

         public boolean hasNext() {
            return this.backingIt.hasNext();
         }

         public Object next() {
            return this.lastReturned = this.backingIt.next();
         }

         public boolean hasPrevious() {
            return this.backingIt.hasPrevious();
         }

         public Object previous() {
            return this.lastReturned = this.backingIt.previous();
         }

         public int nextIndex() {
            return this.backingIt.nextIndex();
         }

         public int previousIndex() {
            return this.backingIt.previousIndex();
         }

         public void remove() {
            ObservableSequentialListWrapper.this.beginChange();
            int var1x = this.previousIndex();
            this.backingIt.remove();
            ObservableSequentialListWrapper.this.nextRemove(var1x, this.lastReturned);
            ObservableSequentialListWrapper.this.endChange();
         }

         public void set(Object var1x) {
            ObservableSequentialListWrapper.this.beginChange();
            int var2 = this.previousIndex();
            this.backingIt.set(var1x);
            ObservableSequentialListWrapper.this.nextSet(var2, this.lastReturned);
            ObservableSequentialListWrapper.this.endChange();
         }

         public void add(Object var1x) {
            ObservableSequentialListWrapper.this.beginChange();
            int var2 = this.nextIndex();
            this.backingIt.add(var1x);
            ObservableSequentialListWrapper.this.nextAdd(var2, var2 + 1);
            ObservableSequentialListWrapper.this.endChange();
         }
      };
   }

   public Iterator iterator() {
      return this.listIterator();
   }

   public Object get(int var1) {
      try {
         return this.backingList.listIterator(var1).next();
      } catch (NoSuchElementException var3) {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   public boolean addAll(int var1, Collection var2) {
      try {
         this.beginChange();
         boolean var3 = false;
         ListIterator var4 = this.listIterator(var1);

         for(Iterator var5 = var2.iterator(); var5.hasNext(); var3 = true) {
            var4.add(var5.next());
         }

         this.endChange();
         return var3;
      } catch (NoSuchElementException var6) {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   public int size() {
      return this.backingList.size();
   }

   protected void doAdd(int var1, Object var2) {
      try {
         this.backingList.listIterator(var1).add(var2);
      } catch (NoSuchElementException var4) {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   protected Object doSet(int var1, Object var2) {
      try {
         ListIterator var3 = this.backingList.listIterator(var1);
         Object var4 = var3.next();
         var3.set(var2);
         return var4;
      } catch (NoSuchElementException var5) {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   protected Object doRemove(int var1) {
      try {
         ListIterator var2 = this.backingList.listIterator(var1);
         Object var3 = var2.next();
         var2.remove();
         return var3;
      } catch (NoSuchElementException var4) {
         throw new IndexOutOfBoundsException("Index: " + var1);
      }
   }

   public void sort() {
      if (!this.backingList.isEmpty()) {
         int[] var1 = this.getSortHelper().sort(this.backingList);
         this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), var1, this));
      }
   }

   public void sort(Comparator var1) {
      if (!this.backingList.isEmpty()) {
         int[] var2 = this.getSortHelper().sort(this.backingList, var1);
         this.fireChange(new NonIterableChange.SimplePermutationChange(0, this.size(), var2, this));
      }
   }

   private SortHelper getSortHelper() {
      if (this.helper == null) {
         this.helper = new SortHelper();
      }

      return this.helper;
   }
}
