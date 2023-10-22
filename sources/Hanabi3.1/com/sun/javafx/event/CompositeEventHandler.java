package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;

public final class CompositeEventHandler {
   private EventProcessorRecord firstRecord;
   private EventProcessorRecord lastRecord;
   private EventHandler eventHandler;

   public void setEventHandler(EventHandler var1) {
      this.eventHandler = var1;
   }

   public EventHandler getEventHandler() {
      return this.eventHandler;
   }

   public void addEventHandler(EventHandler var1) {
      if (this.find(var1, false) == null) {
         this.append(this.lastRecord, this.createEventHandlerRecord(var1));
      }

   }

   public void removeEventHandler(EventHandler var1) {
      EventProcessorRecord var2 = this.find(var1, false);
      if (var2 != null) {
         this.remove(var2);
      }

   }

   public void addEventFilter(EventHandler var1) {
      if (this.find(var1, true) == null) {
         this.append(this.lastRecord, this.createEventFilterRecord(var1));
      }

   }

   public void removeEventFilter(EventHandler var1) {
      EventProcessorRecord var2 = this.find(var1, true);
      if (var2 != null) {
         this.remove(var2);
      }

   }

   public void dispatchBubblingEvent(Event var1) {
      Event var2 = var1;

      for(EventProcessorRecord var3 = this.firstRecord; var3 != null; var3 = var3.nextRecord) {
         if (var3.isDisconnected()) {
            this.remove(var3);
         } else {
            var3.handleBubblingEvent(var2);
         }
      }

      if (this.eventHandler != null) {
         this.eventHandler.handle(var2);
      }

   }

   public void dispatchCapturingEvent(Event var1) {
      Event var2 = var1;

      for(EventProcessorRecord var3 = this.firstRecord; var3 != null; var3 = var3.nextRecord) {
         if (var3.isDisconnected()) {
            this.remove(var3);
         } else {
            var3.handleCapturingEvent(var2);
         }
      }

   }

   boolean containsHandler(EventHandler var1) {
      return this.find(var1, false) != null;
   }

   boolean containsFilter(EventHandler var1) {
      return this.find(var1, true) != null;
   }

   private EventProcessorRecord createEventHandlerRecord(EventHandler var1) {
      return (EventProcessorRecord)(var1 instanceof WeakEventHandler ? new WeakEventHandlerRecord((WeakEventHandler)var1) : new NormalEventHandlerRecord(var1));
   }

   private EventProcessorRecord createEventFilterRecord(EventHandler var1) {
      return (EventProcessorRecord)(var1 instanceof WeakEventHandler ? new WeakEventFilterRecord((WeakEventHandler)var1) : new NormalEventFilterRecord(var1));
   }

   private void remove(EventProcessorRecord var1) {
      EventProcessorRecord var2 = var1.prevRecord;
      EventProcessorRecord var3 = var1.nextRecord;
      if (var2 != null) {
         var2.nextRecord = var3;
      } else {
         this.firstRecord = var3;
      }

      if (var3 != null) {
         var3.prevRecord = var2;
      } else {
         this.lastRecord = var2;
      }

   }

   private void append(EventProcessorRecord var1, EventProcessorRecord var2) {
      EventProcessorRecord var3;
      if (var1 != null) {
         var3 = var1.nextRecord;
         var1.nextRecord = var2;
      } else {
         var3 = this.firstRecord;
         this.firstRecord = var2;
      }

      if (var3 != null) {
         var3.prevRecord = var2;
      } else {
         this.lastRecord = var2;
      }

      var2.prevRecord = var1;
      var2.nextRecord = var3;
   }

   private EventProcessorRecord find(EventHandler var1, boolean var2) {
      for(EventProcessorRecord var3 = this.firstRecord; var3 != null; var3 = var3.nextRecord) {
         if (var3.isDisconnected()) {
            this.remove(var3);
         } else if (var3.stores(var1, var2)) {
            return var3;
         }
      }

      return null;
   }

   private static final class WeakEventFilterRecord extends EventProcessorRecord {
      private final WeakEventHandler weakEventFilter;

      public WeakEventFilterRecord(WeakEventHandler var1) {
         super(null);
         this.weakEventFilter = var1;
      }

      public boolean stores(EventHandler var1, boolean var2) {
         return var2 && this.weakEventFilter == var1;
      }

      public void handleBubblingEvent(Event var1) {
      }

      public void handleCapturingEvent(Event var1) {
         this.weakEventFilter.handle(var1);
      }

      public boolean isDisconnected() {
         return this.weakEventFilter.wasGarbageCollected();
      }
   }

   private static final class NormalEventFilterRecord extends EventProcessorRecord {
      private final EventHandler eventFilter;

      public NormalEventFilterRecord(EventHandler var1) {
         super(null);
         this.eventFilter = var1;
      }

      public boolean stores(EventHandler var1, boolean var2) {
         return var2 && this.eventFilter == var1;
      }

      public void handleBubblingEvent(Event var1) {
      }

      public void handleCapturingEvent(Event var1) {
         this.eventFilter.handle(var1);
      }

      public boolean isDisconnected() {
         return false;
      }
   }

   private static final class WeakEventHandlerRecord extends EventProcessorRecord {
      private final WeakEventHandler weakEventHandler;

      public WeakEventHandlerRecord(WeakEventHandler var1) {
         super(null);
         this.weakEventHandler = var1;
      }

      public boolean stores(EventHandler var1, boolean var2) {
         return !var2 && this.weakEventHandler == var1;
      }

      public void handleBubblingEvent(Event var1) {
         this.weakEventHandler.handle(var1);
      }

      public void handleCapturingEvent(Event var1) {
      }

      public boolean isDisconnected() {
         return this.weakEventHandler.wasGarbageCollected();
      }
   }

   private static final class NormalEventHandlerRecord extends EventProcessorRecord {
      private final EventHandler eventHandler;

      public NormalEventHandlerRecord(EventHandler var1) {
         super(null);
         this.eventHandler = var1;
      }

      public boolean stores(EventHandler var1, boolean var2) {
         return !var2 && this.eventHandler == var1;
      }

      public void handleBubblingEvent(Event var1) {
         this.eventHandler.handle(var1);
      }

      public void handleCapturingEvent(Event var1) {
      }

      public boolean isDisconnected() {
         return false;
      }
   }

   private abstract static class EventProcessorRecord {
      private EventProcessorRecord nextRecord;
      private EventProcessorRecord prevRecord;

      private EventProcessorRecord() {
      }

      public abstract boolean stores(EventHandler var1, boolean var2);

      public abstract void handleBubblingEvent(Event var1);

      public abstract void handleCapturingEvent(Event var1);

      public abstract boolean isDisconnected();

      // $FF: synthetic method
      EventProcessorRecord(Object var1) {
         this();
      }
   }
}
