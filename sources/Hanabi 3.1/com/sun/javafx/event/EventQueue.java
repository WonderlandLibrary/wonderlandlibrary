package com.sun.javafx.event;

import java.util.ArrayDeque;
import java.util.Queue;
import javafx.event.Event;

public final class EventQueue {
   private Queue queue = new ArrayDeque();
   private boolean inLoop;

   public void postEvent(Event var1) {
      this.queue.add(var1);
   }

   public void fire() {
      if (!this.inLoop) {
         this.inLoop = true;

         try {
            while(!this.queue.isEmpty()) {
               Event var1 = (Event)this.queue.remove();
               Event.fireEvent(var1.getTarget(), var1);
            }
         } finally {
            this.inLoop = false;
         }

      }
   }
}
