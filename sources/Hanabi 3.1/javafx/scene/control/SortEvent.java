package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class SortEvent extends Event {
   public static final EventType ANY;
   private static final EventType SORT_EVENT;

   public static EventType sortEvent() {
      return SORT_EVENT;
   }

   public SortEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2) {
      super(var1, var2, sortEvent());
   }

   public Object getSource() {
      return super.getSource();
   }

   static {
      ANY = new EventType(Event.ANY, "SORT");
      SORT_EVENT = new EventType(ANY, "SORT_EVENT");
   }
}
