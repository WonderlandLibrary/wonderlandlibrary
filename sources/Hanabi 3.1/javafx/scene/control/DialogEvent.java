package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DialogEvent extends Event {
   private static final long serialVersionUID = 20140716L;
   public static final EventType ANY;
   public static final EventType DIALOG_SHOWING;
   public static final EventType DIALOG_SHOWN;
   public static final EventType DIALOG_HIDING;
   public static final EventType DIALOG_HIDDEN;
   public static final EventType DIALOG_CLOSE_REQUEST;

   public DialogEvent(@NamedArg("source") Dialog var1, @NamedArg("eventType") EventType var2) {
      super(var1, var1, var2);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("DialogEvent [");
      var1.append("source = ").append(this.getSource());
      var1.append(", target = ").append(this.getTarget());
      var1.append(", eventType = ").append(this.getEventType());
      var1.append(", consumed = ").append(this.isConsumed());
      return var1.append("]").toString();
   }

   public DialogEvent copyFor(Object var1, EventTarget var2) {
      return (DialogEvent)super.copyFor(var1, var2);
   }

   public DialogEvent copyFor(Object var1, EventTarget var2, EventType var3) {
      DialogEvent var4 = this.copyFor(var1, var2);
      var4.eventType = var3;
      return var4;
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(Event.ANY, "DIALOG");
      DIALOG_SHOWING = new EventType(ANY, "DIALOG_SHOWING");
      DIALOG_SHOWN = new EventType(ANY, "DIALOG_SHOWN");
      DIALOG_HIDING = new EventType(ANY, "DIALOG_HIDING");
      DIALOG_HIDDEN = new EventType(ANY, "DIALOG_HIDDEN");
      DIALOG_CLOSE_REQUEST = new EventType(ANY, "DIALOG_CLOSE_REQUEST");
   }
}
