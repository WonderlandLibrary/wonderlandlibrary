package javafx.scene.web;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class WebEvent extends Event {
   public static final EventType ANY;
   public static final EventType RESIZED;
   public static final EventType STATUS_CHANGED;
   public static final EventType VISIBILITY_CHANGED;
   public static final EventType ALERT;
   private final Object data;

   public WebEvent(@NamedArg("source") Object var1, @NamedArg("type") EventType var2, @NamedArg("data") Object var3) {
      super(var1, (EventTarget)null, var2);
      this.data = var3;
   }

   public Object getData() {
      return this.data;
   }

   public String toString() {
      return String.format("WebEvent [source = %s, eventType = %s, data = %s]", this.getSource(), this.getEventType(), this.getData());
   }

   static {
      ANY = new EventType(Event.ANY, "WEB");
      RESIZED = new EventType(ANY, "WEB_RESIZED");
      STATUS_CHANGED = new EventType(ANY, "WEB_STATUS_CHANGED");
      VISIBILITY_CHANGED = new EventType(ANY, "WEB_VISIBILITY_CHANGED");
      ALERT = new EventType(ANY, "WEB_ALERT");
   }
}
