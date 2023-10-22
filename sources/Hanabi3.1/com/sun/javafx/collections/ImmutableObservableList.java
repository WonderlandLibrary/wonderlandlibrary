package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ImmutableObservableList extends AbstractList implements ObservableList {
   private final Object[] elements;

   public ImmutableObservableList(Object... var1) {
      this.elements = var1 != null && var1.length != 0 ? Arrays.copyOf(var1, var1.length) : null;
   }

   public void addListener(InvalidationListener var1) {
   }

   public void removeListener(InvalidationListener var1) {
   }

   public void addListener(ListChangeListener var1) {
   }

   public void removeListener(ListChangeListener var1) {
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

   public Object get(int var1) {
      if (var1 >= 0 && var1 < this.size()) {
         return this.elements[var1];
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int size() {
      return this.elements == null ? 0 : this.elements.length;
   }
}
