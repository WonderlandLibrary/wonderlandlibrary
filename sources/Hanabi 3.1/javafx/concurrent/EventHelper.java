package javafx.concurrent;

import com.sun.javafx.event.EventHandlerManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;

class EventHelper {
   private final EventTarget target;
   private final ObjectProperty onReady;
   private final ObjectProperty onScheduled;
   private final ObjectProperty onRunning;
   private final ObjectProperty onSucceeded;
   private final ObjectProperty onCancelled;
   private final ObjectProperty onFailed;
   private EventHandlerManager internalEventDispatcher;

   final ObjectProperty onReadyProperty() {
      return this.onReady;
   }

   final EventHandler getOnReady() {
      return (EventHandler)this.onReady.get();
   }

   final void setOnReady(EventHandler var1) {
      this.onReady.set(var1);
   }

   final ObjectProperty onScheduledProperty() {
      return this.onScheduled;
   }

   final EventHandler getOnScheduled() {
      return (EventHandler)this.onScheduled.get();
   }

   final void setOnScheduled(EventHandler var1) {
      this.onScheduled.set(var1);
   }

   final ObjectProperty onRunningProperty() {
      return this.onRunning;
   }

   final EventHandler getOnRunning() {
      return (EventHandler)this.onRunning.get();
   }

   final void setOnRunning(EventHandler var1) {
      this.onRunning.set(var1);
   }

   final ObjectProperty onSucceededProperty() {
      return this.onSucceeded;
   }

   final EventHandler getOnSucceeded() {
      return (EventHandler)this.onSucceeded.get();
   }

   final void setOnSucceeded(EventHandler var1) {
      this.onSucceeded.set(var1);
   }

   final ObjectProperty onCancelledProperty() {
      return this.onCancelled;
   }

   final EventHandler getOnCancelled() {
      return (EventHandler)this.onCancelled.get();
   }

   final void setOnCancelled(EventHandler var1) {
      this.onCancelled.set(var1);
   }

   final ObjectProperty onFailedProperty() {
      return this.onFailed;
   }

   final EventHandler getOnFailed() {
      return (EventHandler)this.onFailed.get();
   }

   final void setOnFailed(EventHandler var1) {
      this.onFailed.set(var1);
   }

   EventHelper(EventTarget var1) {
      this.target = var1;
      this.onReady = new SimpleObjectProperty(var1, "onReady") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_READY, var1);
         }
      };
      this.onScheduled = new SimpleObjectProperty(var1, "onScheduled") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_SCHEDULED, var1);
         }
      };
      this.onRunning = new SimpleObjectProperty(var1, "onRunning") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING, var1);
         }
      };
      this.onSucceeded = new SimpleObjectProperty(var1, "onSucceeded") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, var1);
         }
      };
      this.onCancelled = new SimpleObjectProperty(var1, "onCancelled") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, var1);
         }
      };
      this.onFailed = new SimpleObjectProperty(var1, "onFailed") {
         protected void invalidated() {
            EventHandler var1 = (EventHandler)this.get();
            EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, var1);
         }
      };
   }

   final void addEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().addEventHandler(var1, var2);
   }

   final void removeEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().removeEventHandler(var1, var2);
   }

   final void addEventFilter(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().addEventFilter(var1, var2);
   }

   final void removeEventFilter(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().removeEventFilter(var1, var2);
   }

   final void setEventHandler(EventType var1, EventHandler var2) {
      this.getInternalEventDispatcher().setEventHandler(var1, var2);
   }

   private EventHandlerManager getInternalEventDispatcher() {
      if (this.internalEventDispatcher == null) {
         this.internalEventDispatcher = new EventHandlerManager(this.target);
      }

      return this.internalEventDispatcher;
   }

   final void fireEvent(Event var1) {
      Event.fireEvent(this.target, var1);
   }

   EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      return this.internalEventDispatcher == null ? var1 : var1.append(this.getInternalEventDispatcher());
   }
}
