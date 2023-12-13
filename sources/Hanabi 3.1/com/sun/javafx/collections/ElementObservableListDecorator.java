package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;
import javafx.util.Callback;

public final class ElementObservableListDecorator extends ObservableListBase implements ObservableList {
   private final ObservableList decoratedList;
   private final ListChangeListener listener;
   private ElementObserver observer;

   public ElementObservableListDecorator(ObservableList var1, Callback var2) {
      this.observer = new ElementObserver(var2, new Callback() {
         public InvalidationListener call(final Object var1) {
            return new InvalidationListener() {
               public void invalidated(Observable var1x) {
                  ElementObservableListDecorator.this.beginChange();
                  int var2 = 0;
                  if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                     for(int var3 = ElementObservableListDecorator.this.size(); var2 < var3; ++var2) {
                        if (ElementObservableListDecorator.this.get(var2) == var1) {
                           ElementObservableListDecorator.this.nextUpdate(var2);
                        }
                     }
                  } else {
                     for(Iterator var4 = ElementObservableListDecorator.this.iterator(); var4.hasNext(); ++var2) {
                        if (var4.next() == var1) {
                           ElementObservableListDecorator.this.nextUpdate(var2);
                        }
                     }
                  }

                  ElementObservableListDecorator.this.endChange();
               }
            };
         }
      }, this);
      this.decoratedList = var1;
      int var3 = this.decoratedList.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         this.observer.attachListener(this.decoratedList.get(var4));
      }

      this.listener = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               if (var1.wasAdded() || var1.wasRemoved()) {
                  int var2 = var1.getRemovedSize();
                  List var3 = var1.getRemoved();

                  int var4;
                  for(var4 = 0; var4 < var2; ++var4) {
                     ElementObservableListDecorator.this.observer.detachListener(var3.get(var4));
                  }

                  if (ElementObservableListDecorator.this.decoratedList instanceof RandomAccess) {
                     var4 = var1.getTo();

                     for(int var7 = var1.getFrom(); var7 < var4; ++var7) {
                        ElementObservableListDecorator.this.observer.attachListener(ElementObservableListDecorator.this.decoratedList.get(var7));
                     }
                  } else {
                     Iterator var6 = var1.getAddedSubList().iterator();

                     while(var6.hasNext()) {
                        Object var5 = var6.next();
                        ElementObservableListDecorator.this.observer.attachListener(var5);
                     }
                  }
               }
            }

            var1.reset();
            ElementObservableListDecorator.this.fireChange(var1);
         }
      };
      this.decoratedList.addListener(new WeakListChangeListener(this.listener));
   }

   public Object[] toArray(Object[] var1) {
      return this.decoratedList.toArray(var1);
   }

   public Object[] toArray() {
      return this.decoratedList.toArray();
   }

   public List subList(int var1, int var2) {
      return this.decoratedList.subList(var1, var2);
   }

   public int size() {
      return this.decoratedList.size();
   }

   public Object set(int var1, Object var2) {
      return this.decoratedList.set(var1, var2);
   }

   public boolean retainAll(Collection var1) {
      return this.decoratedList.retainAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return this.decoratedList.removeAll(var1);
   }

   public Object remove(int var1) {
      return this.decoratedList.remove(var1);
   }

   public boolean remove(Object var1) {
      return this.decoratedList.remove(var1);
   }

   public ListIterator listIterator(int var1) {
      return this.decoratedList.listIterator(var1);
   }

   public ListIterator listIterator() {
      return this.decoratedList.listIterator();
   }

   public int lastIndexOf(Object var1) {
      return this.decoratedList.lastIndexOf(var1);
   }

   public Iterator iterator() {
      return this.decoratedList.iterator();
   }

   public boolean isEmpty() {
      return this.decoratedList.isEmpty();
   }

   public int indexOf(Object var1) {
      return this.decoratedList.indexOf(var1);
   }

   public Object get(int var1) {
      return this.decoratedList.get(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.decoratedList.containsAll(var1);
   }

   public boolean contains(Object var1) {
      return this.decoratedList.contains(var1);
   }

   public void clear() {
      this.decoratedList.clear();
   }

   public boolean addAll(int var1, Collection var2) {
      return this.decoratedList.addAll(var1, var2);
   }

   public boolean addAll(Collection var1) {
      return this.decoratedList.addAll(var1);
   }

   public void add(int var1, Object var2) {
      this.decoratedList.add(var1, var2);
   }

   public boolean add(Object var1) {
      return this.decoratedList.add(var1);
   }

   public boolean setAll(Collection var1) {
      return this.decoratedList.setAll(var1);
   }

   public boolean setAll(Object... var1) {
      return this.decoratedList.setAll(var1);
   }

   public boolean retainAll(Object... var1) {
      return this.decoratedList.retainAll(var1);
   }

   public boolean removeAll(Object... var1) {
      return this.decoratedList.removeAll(var1);
   }

   public void remove(int var1, int var2) {
      this.decoratedList.remove(var1, var2);
   }

   public boolean addAll(Object... var1) {
      return this.decoratedList.addAll(var1);
   }
}
