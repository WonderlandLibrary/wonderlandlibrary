package com.sun.javafx.collections;

import java.util.IdentityHashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableListBase;
import javafx.util.Callback;

final class ElementObserver {
   private Callback extractor;
   private final Callback listenerGenerator;
   private final ObservableListBase list;
   private IdentityHashMap elementsMap = new IdentityHashMap();

   ElementObserver(Callback var1, Callback var2, ObservableListBase var3) {
      this.extractor = var1;
      this.listenerGenerator = var2;
      this.list = var3;
   }

   void attachListener(Object var1) {
      if (this.elementsMap != null && var1 != null) {
         if (this.elementsMap.containsKey(var1)) {
            ((ElementsMapElement)this.elementsMap.get(var1)).increment();
         } else {
            InvalidationListener var2 = (InvalidationListener)this.listenerGenerator.call(var1);
            Observable[] var3 = (Observable[])this.extractor.call(var1);
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Observable var6 = var3[var5];
               var6.addListener(var2);
            }

            this.elementsMap.put(var1, new ElementsMapElement(var2));
         }
      }

   }

   void detachListener(Object var1) {
      if (this.elementsMap != null && var1 != null) {
         ElementsMapElement var2 = (ElementsMapElement)this.elementsMap.get(var1);
         Observable[] var3 = (Observable[])this.extractor.call(var1);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Observable var6 = var3[var5];
            var6.removeListener(var2.getListener());
         }

         if (var2.decrement() == 0) {
            this.elementsMap.remove(var1);
         }
      }

   }

   private static class ElementsMapElement {
      InvalidationListener listener;
      int counter;

      public ElementsMapElement(InvalidationListener var1) {
         this.listener = var1;
         this.counter = 1;
      }

      public void increment() {
         ++this.counter;
      }

      public int decrement() {
         return --this.counter;
      }

      private InvalidationListener getListener() {
         return this.listener;
      }
   }
}
