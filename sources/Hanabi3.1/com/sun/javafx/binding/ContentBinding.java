package com.sun.javafx.binding;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ContentBinding {
   private static void checkParameters(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         if (var0 == var1) {
            throw new IllegalArgumentException("Cannot bind object to itself");
         }
      } else {
         throw new NullPointerException("Both parameters must be specified.");
      }
   }

   public static Object bind(List var0, ObservableList var1) {
      checkParameters(var0, var1);
      ListContentBinding var2 = new ListContentBinding(var0);
      if (var0 instanceof ObservableList) {
         ((ObservableList)var0).setAll((Collection)var1);
      } else {
         var0.clear();
         var0.addAll(var1);
      }

      var1.removeListener(var2);
      var1.addListener(var2);
      return var2;
   }

   public static Object bind(Set var0, ObservableSet var1) {
      checkParameters(var0, var1);
      SetContentBinding var2 = new SetContentBinding(var0);
      var0.clear();
      var0.addAll(var1);
      var1.removeListener(var2);
      var1.addListener(var2);
      return var2;
   }

   public static Object bind(Map var0, ObservableMap var1) {
      checkParameters(var0, var1);
      MapContentBinding var2 = new MapContentBinding(var0);
      var0.clear();
      var0.putAll(var1);
      var1.removeListener(var2);
      var1.addListener(var2);
      return var2;
   }

   public static void unbind(Object var0, Object var1) {
      checkParameters(var0, var1);
      if (var0 instanceof List && var1 instanceof ObservableList) {
         ((ObservableList)var1).removeListener(new ListContentBinding((List)var0));
      } else if (var0 instanceof Set && var1 instanceof ObservableSet) {
         ((ObservableSet)var1).removeListener(new SetContentBinding((Set)var0));
      } else if (var0 instanceof Map && var1 instanceof ObservableMap) {
         ((ObservableMap)var1).removeListener(new MapContentBinding((Map)var0));
      }

   }

   private static class MapContentBinding implements MapChangeListener, WeakListener {
      private final WeakReference mapRef;

      public MapContentBinding(Map var1) {
         this.mapRef = new WeakReference(var1);
      }

      public void onChanged(MapChangeListener.Change var1) {
         Map var2 = (Map)this.mapRef.get();
         if (var2 == null) {
            var1.getMap().removeListener(this);
         } else if (var1.wasRemoved()) {
            var2.remove(var1.getKey());
         } else {
            var2.put(var1.getKey(), var1.getValueAdded());
         }

      }

      public boolean wasGarbageCollected() {
         return this.mapRef.get() == null;
      }

      public int hashCode() {
         Map var1 = (Map)this.mapRef.get();
         return var1 == null ? 0 : var1.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            Map var2 = (Map)this.mapRef.get();
            if (var2 == null) {
               return false;
            } else if (var1 instanceof MapContentBinding) {
               MapContentBinding var3 = (MapContentBinding)var1;
               Map var4 = (Map)var3.mapRef.get();
               return var2 == var4;
            } else {
               return false;
            }
         }
      }
   }

   private static class SetContentBinding implements SetChangeListener, WeakListener {
      private final WeakReference setRef;

      public SetContentBinding(Set var1) {
         this.setRef = new WeakReference(var1);
      }

      public void onChanged(SetChangeListener.Change var1) {
         Set var2 = (Set)this.setRef.get();
         if (var2 == null) {
            var1.getSet().removeListener(this);
         } else if (var1.wasRemoved()) {
            var2.remove(var1.getElementRemoved());
         } else {
            var2.add(var1.getElementAdded());
         }

      }

      public boolean wasGarbageCollected() {
         return this.setRef.get() == null;
      }

      public int hashCode() {
         Set var1 = (Set)this.setRef.get();
         return var1 == null ? 0 : var1.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            Set var2 = (Set)this.setRef.get();
            if (var2 == null) {
               return false;
            } else if (var1 instanceof SetContentBinding) {
               SetContentBinding var3 = (SetContentBinding)var1;
               Set var4 = (Set)var3.setRef.get();
               return var2 == var4;
            } else {
               return false;
            }
         }
      }
   }

   private static class ListContentBinding implements ListChangeListener, WeakListener {
      private final WeakReference listRef;

      public ListContentBinding(List var1) {
         this.listRef = new WeakReference(var1);
      }

      public void onChanged(ListChangeListener.Change var1) {
         List var2 = (List)this.listRef.get();
         if (var2 == null) {
            var1.getList().removeListener(this);
         } else {
            while(var1.next()) {
               if (var1.wasPermutated()) {
                  var2.subList(var1.getFrom(), var1.getTo()).clear();
                  var2.addAll(var1.getFrom(), var1.getList().subList(var1.getFrom(), var1.getTo()));
               } else {
                  if (var1.wasRemoved()) {
                     var2.subList(var1.getFrom(), var1.getFrom() + var1.getRemovedSize()).clear();
                  }

                  if (var1.wasAdded()) {
                     var2.addAll(var1.getFrom(), var1.getAddedSubList());
                  }
               }
            }
         }

      }

      public boolean wasGarbageCollected() {
         return this.listRef.get() == null;
      }

      public int hashCode() {
         List var1 = (List)this.listRef.get();
         return var1 == null ? 0 : var1.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            List var2 = (List)this.listRef.get();
            if (var2 == null) {
               return false;
            } else if (var1 instanceof ListContentBinding) {
               ListContentBinding var3 = (ListContentBinding)var1;
               List var4 = (List)var3.listRef.get();
               return var2 == var4;
            } else {
               return false;
            }
         }
      }
   }
}
