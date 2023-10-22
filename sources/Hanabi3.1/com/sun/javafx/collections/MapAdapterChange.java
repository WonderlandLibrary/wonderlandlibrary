package com.sun.javafx.collections;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class MapAdapterChange extends MapChangeListener.Change {
   private final MapChangeListener.Change change;

   public MapAdapterChange(ObservableMap var1, MapChangeListener.Change var2) {
      super(var1);
      this.change = var2;
   }

   public boolean wasAdded() {
      return this.change.wasAdded();
   }

   public boolean wasRemoved() {
      return this.change.wasRemoved();
   }

   public Object getKey() {
      return this.change.getKey();
   }

   public Object getValueAdded() {
      return this.change.getValueAdded();
   }

   public Object getValueRemoved() {
      return this.change.getValueRemoved();
   }

   public String toString() {
      return this.change.toString();
   }
}
