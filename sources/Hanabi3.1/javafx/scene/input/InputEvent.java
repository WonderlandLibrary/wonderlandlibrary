package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class InputEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ANY;

   public InputEvent(@NamedArg("eventType") EventType var1) {
      super(var1);
   }

   public InputEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("eventType") EventType var3) {
      super(var1, var2, var3);
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ANY = new EventType(Event.ANY, "INPUT");
   }
}
