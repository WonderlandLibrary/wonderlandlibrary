package javafx.concurrent;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class WorkerStateEvent extends Event {
   public static final EventType ANY;
   public static final EventType WORKER_STATE_READY;
   public static final EventType WORKER_STATE_SCHEDULED;
   public static final EventType WORKER_STATE_RUNNING;
   public static final EventType WORKER_STATE_SUCCEEDED;
   public static final EventType WORKER_STATE_CANCELLED;
   public static final EventType WORKER_STATE_FAILED;

   public WorkerStateEvent(@NamedArg("worker") Worker var1, @NamedArg("eventType") EventType var2) {
      super(var1, var1 instanceof EventTarget ? (EventTarget)var1 : null, var2);
   }

   public Worker getSource() {
      return (Worker)super.getSource();
   }

   static {
      ANY = new EventType(Event.ANY, "WORKER_STATE");
      WORKER_STATE_READY = new EventType(ANY, "WORKER_STATE_READY");
      WORKER_STATE_SCHEDULED = new EventType(ANY, "WORKER_STATE_SCHEDULED");
      WORKER_STATE_RUNNING = new EventType(ANY, "WORKER_STATE_RUNNING");
      WORKER_STATE_SUCCEEDED = new EventType(ANY, "WORKER_STATE_SUCCEEDED");
      WORKER_STATE_CANCELLED = new EventType(ANY, "WORKER_STATE_CANCELLED");
      WORKER_STATE_FAILED = new EventType(ANY, "WORKER_STATE_FAILED");
   }
}
