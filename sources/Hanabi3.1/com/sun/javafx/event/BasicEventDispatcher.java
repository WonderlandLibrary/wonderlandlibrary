package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public abstract class BasicEventDispatcher implements EventDispatcher {
   private BasicEventDispatcher previousDispatcher;
   private BasicEventDispatcher nextDispatcher;

   public Event dispatchEvent(Event var1, EventDispatchChain var2) {
      var1 = this.dispatchCapturingEvent(var1);
      if (var1.isConsumed()) {
         return null;
      } else {
         var1 = var2.dispatchEvent(var1);
         if (var1 != null) {
            var1 = this.dispatchBubblingEvent(var1);
            if (var1.isConsumed()) {
               return null;
            }
         }

         return var1;
      }
   }

   public Event dispatchCapturingEvent(Event var1) {
      return var1;
   }

   public Event dispatchBubblingEvent(Event var1) {
      return var1;
   }

   public final BasicEventDispatcher getPreviousDispatcher() {
      return this.previousDispatcher;
   }

   public final BasicEventDispatcher getNextDispatcher() {
      return this.nextDispatcher;
   }

   public final void insertNextDispatcher(BasicEventDispatcher var1) {
      if (this.nextDispatcher != null) {
         this.nextDispatcher.previousDispatcher = var1;
      }

      var1.nextDispatcher = this.nextDispatcher;
      var1.previousDispatcher = this;
      this.nextDispatcher = var1;
   }
}
