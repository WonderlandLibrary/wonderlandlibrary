package com.sun.javafx.event;

import javafx.event.Event;

public abstract class CompositeEventDispatcher extends BasicEventDispatcher {
   public abstract BasicEventDispatcher getFirstDispatcher();

   public abstract BasicEventDispatcher getLastDispatcher();

   public final Event dispatchCapturingEvent(Event var1) {
      for(BasicEventDispatcher var2 = this.getFirstDispatcher(); var2 != null; var2 = var2.getNextDispatcher()) {
         var1 = var2.dispatchCapturingEvent(var1);
         if (var1.isConsumed()) {
            break;
         }
      }

      return var1;
   }

   public final Event dispatchBubblingEvent(Event var1) {
      for(BasicEventDispatcher var2 = this.getLastDispatcher(); var2 != null; var2 = var2.getPreviousDispatcher()) {
         var1 = var2.dispatchBubblingEvent(var1);
         if (var1.isConsumed()) {
            break;
         }
      }

      return var1;
   }
}
