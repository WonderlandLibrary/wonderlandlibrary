package javafx.event;

public class ActionEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType ACTION;
   public static final EventType ANY;

   public ActionEvent() {
      super(ACTION);
   }

   public ActionEvent(Object var1, EventTarget var2) {
      super(var1, var2, ACTION);
   }

   public ActionEvent copyFor(Object var1, EventTarget var2) {
      return (ActionEvent)super.copyFor(var1, var2);
   }

   public EventType getEventType() {
      return super.getEventType();
   }

   static {
      ACTION = new EventType(Event.ANY, "ACTION");
      ANY = ACTION;
   }
}
