package com.sun.javafx.collections;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;

public class UnmodifiableObservableMap extends AbstractMap implements ObservableMap {
   private MapListenerHelper listenerHelper;
   private final ObservableMap backingMap;
   private final MapChangeListener listener;
   private Set keyset;
   private Collection values;
   private Set entryset;

   public UnmodifiableObservableMap(ObservableMap var1) {
      this.backingMap = var1;
      this.listener = (var1x) -> {
         this.callObservers(new MapAdapterChange(this, var1x));
      };
      this.backingMap.addListener(new WeakMapChangeListener(this.listener));
   }

   private void callObservers(MapChangeListener.Change var1) {
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

   public Set keySet() {
      if (this.keyset == null) {
         this.keyset = Collections.unmodifiableSet(this.backingMap.keySet());
      }

      return this.keyset;
   }

   public Collection values() {
      if (this.values == null) {
         this.values = Collections.unmodifiableCollection(this.backingMap.values());
      }

      return this.values;
   }

   public Set entrySet() {
      if (this.entryset == null) {
         this.entryset = Collections.unmodifiableMap(this.backingMap).entrySet();
      }

      return this.entryset;
   }
}
