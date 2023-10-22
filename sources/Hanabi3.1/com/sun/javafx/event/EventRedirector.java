package com.sun.javafx.event;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class EventRedirector extends BasicEventDispatcher {
   private final EventDispatchChainImpl eventDispatchChain = new EventDispatchChainImpl();
   private final List eventDispatchers = new CopyOnWriteArrayList();
   private final Object eventSource;

   public EventRedirector(Object var1) {
      this.eventSource = var1;
   }

   protected void handleRedirectedEvent(Object var1, Event var2) {
   }

   public final void addEventDispatcher(EventDispatcher var1) {
      this.eventDispatchers.add(var1);
   }

   public final void removeEventDispatcher(EventDispatcher var1) {
      this.eventDispatchers.remove(var1);
   }

   public final Event dispatchCapturingEvent(Event var1) {
      EventType var2 = var1.getEventType();
      if (var2 == DirectEvent.DIRECT) {
         var1 = ((DirectEvent)var1).getOriginalEvent();
      } else {
         this.redirectEvent(var1);
         if (var2 == RedirectedEvent.REDIRECTED) {
            this.handleRedirectedEvent(var1.getSource(), ((RedirectedEvent)var1).getOriginalEvent());
         }
      }

      return var1;
   }

   private void redirectEvent(Event var1) {
      if (!this.eventDispatchers.isEmpty()) {
         RedirectedEvent var2 = var1.getEventType() == RedirectedEvent.REDIRECTED ? (RedirectedEvent)var1 : new RedirectedEvent(var1, this.eventSource, (EventTarget)null);
         Iterator var3 = this.eventDispatchers.iterator();

         while(var3.hasNext()) {
            EventDispatcher var4 = (EventDispatcher)var3.next();
            this.eventDispatchChain.reset();
            var4.dispatchEvent(var2, this.eventDispatchChain);
         }
      }

   }
}
