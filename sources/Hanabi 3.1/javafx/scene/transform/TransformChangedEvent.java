package javafx.scene.transform;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class TransformChangedEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType TRANSFORM_CHANGED;
   public static final EventType ANY;

   public TransformChangedEvent() {
      super(TRANSFORM_CHANGED);
   }

   public TransformChangedEvent(Object var1, EventTarget var2) {
      super(var1, var2, TRANSFORM_CHANGED);
   }

   static {
      TRANSFORM_CHANGED = new EventType(Event.ANY, "TRANSFORM_CHANGED");
      ANY = TRANSFORM_CHANGED;
   }
}
