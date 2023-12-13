package com.sun.javafx.scene.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.util.Callback;

public final class MultiplePropertyChangeListenerHandler {
   private final Callback propertyChangedHandler;
   private Map propertyReferenceMap = new HashMap();
   private final ChangeListener propertyChangedListener = new ChangeListener() {
      public void changed(ObservableValue var1, Object var2, Object var3) {
         MultiplePropertyChangeListenerHandler.this.propertyChangedHandler.call(MultiplePropertyChangeListenerHandler.this.propertyReferenceMap.get(var1));
      }
   };
   private final WeakChangeListener weakPropertyChangedListener;

   public MultiplePropertyChangeListenerHandler(Callback var1) {
      this.weakPropertyChangedListener = new WeakChangeListener(this.propertyChangedListener);
      this.propertyChangedHandler = var1;
   }

   public final void registerChangeListener(ObservableValue var1, String var2) {
      if (!this.propertyReferenceMap.containsKey(var1)) {
         this.propertyReferenceMap.put(var1, var2);
         var1.addListener(this.weakPropertyChangedListener);
      }

   }

   public final void unregisterChangeListener(ObservableValue var1) {
      if (this.propertyReferenceMap.containsKey(var1)) {
         this.propertyReferenceMap.remove(var1);
         var1.removeListener(this.weakPropertyChangedListener);
      }

   }

   public void dispose() {
      Iterator var1 = this.propertyReferenceMap.keySet().iterator();

      while(var1.hasNext()) {
         ObservableValue var2 = (ObservableValue)var1.next();
         var2.removeListener(this.weakPropertyChangedListener);
      }

      this.propertyReferenceMap.clear();
   }
}
