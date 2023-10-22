package com.sun.javafx.event;

import java.util.HashMap;
import java.util.Map;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public class EventHandlerManager extends BasicEventDispatcher {
   private final Map eventHandlerMap;
   private final Object eventSource;

   public EventHandlerManager(Object var1) {
      this.eventSource = var1;
      this.eventHandlerMap = new HashMap();
   }

   public final void addEventHandler(EventType var1, EventHandler var2) {
      validateEventType(var1);
      validateEventHandler(var2);
      CompositeEventHandler var3 = this.createGetCompositeEventHandler(var1);
      var3.addEventHandler(var2);
   }

   public final void removeEventHandler(EventType var1, EventHandler var2) {
      validateEventType(var1);
      validateEventHandler(var2);
      CompositeEventHandler var3 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var3 != null) {
         var3.removeEventHandler(var2);
      }

   }

   public final void addEventFilter(EventType var1, EventHandler var2) {
      validateEventType(var1);
      validateEventFilter(var2);
      CompositeEventHandler var3 = this.createGetCompositeEventHandler(var1);
      var3.addEventFilter(var2);
   }

   public final void removeEventFilter(EventType var1, EventHandler var2) {
      validateEventType(var1);
      validateEventFilter(var2);
      CompositeEventHandler var3 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var3 != null) {
         var3.removeEventFilter(var2);
      }

   }

   public final void setEventHandler(EventType var1, EventHandler var2) {
      validateEventType(var1);
      CompositeEventHandler var3 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var3 == null) {
         if (var2 == null) {
            return;
         }

         var3 = new CompositeEventHandler();
         this.eventHandlerMap.put(var1, var3);
      }

      var3.setEventHandler(var2);
   }

   public final EventHandler getEventHandler(EventType var1) {
      CompositeEventHandler var2 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      return var2 != null ? var2.getEventHandler() : null;
   }

   public final Event dispatchCapturingEvent(Event var1) {
      EventType var2 = var1.getEventType();

      do {
         var1 = this.dispatchCapturingEvent(var2, var1);
         var2 = var2.getSuperType();
      } while(var2 != null);

      return var1;
   }

   public final Event dispatchBubblingEvent(Event var1) {
      EventType var2 = var1.getEventType();

      do {
         var1 = this.dispatchBubblingEvent(var2, var1);
         var2 = var2.getSuperType();
      } while(var2 != null);

      return var1;
   }

   private CompositeEventHandler createGetCompositeEventHandler(EventType var1) {
      CompositeEventHandler var2 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var2 == null) {
         var2 = new CompositeEventHandler();
         this.eventHandlerMap.put(var1, var2);
      }

      return var2;
   }

   protected Object getEventSource() {
      return this.eventSource;
   }

   private Event dispatchCapturingEvent(EventType var1, Event var2) {
      CompositeEventHandler var3 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var3 != null) {
         var2 = fixEventSource(var2, this.eventSource);
         var3.dispatchCapturingEvent(var2);
      }

      return var2;
   }

   private Event dispatchBubblingEvent(EventType var1, Event var2) {
      CompositeEventHandler var3 = (CompositeEventHandler)this.eventHandlerMap.get(var1);
      if (var3 != null) {
         var2 = fixEventSource(var2, this.eventSource);
         var3.dispatchBubblingEvent(var2);
      }

      return var2;
   }

   private static Event fixEventSource(Event var0, Object var1) {
      return var0.getSource() != var1 ? var0.copyFor(var1, var0.getTarget()) : var0;
   }

   private static void validateEventType(EventType var0) {
      if (var0 == null) {
         throw new NullPointerException("Event type must not be null");
      }
   }

   private static void validateEventHandler(EventHandler var0) {
      if (var0 == null) {
         throw new NullPointerException("Event handler must not be null");
      }
   }

   private static void validateEventFilter(EventHandler var0) {
      if (var0 == null) {
         throw new NullPointerException("Event filter must not be null");
      }
   }
}
