package com.sun.javafx.collections;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class SetAdapterChange extends SetChangeListener.Change {
   private final SetChangeListener.Change change;

   public SetAdapterChange(ObservableSet var1, SetChangeListener.Change var2) {
      super(var1);
      this.change = var2;
   }

   public String toString() {
      return this.change.toString();
   }

   public boolean wasAdded() {
      return this.change.wasAdded();
   }

   public boolean wasRemoved() {
      return this.change.wasRemoved();
   }

   public Object getElementAdded() {
      return this.change.getElementAdded();
   }

   public Object getElementRemoved() {
      return this.change.getElementRemoved();
   }
}
