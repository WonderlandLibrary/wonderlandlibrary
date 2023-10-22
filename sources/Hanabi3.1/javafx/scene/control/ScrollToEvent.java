package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ScrollToEvent extends Event {
   public static final EventType ANY;
   private static final EventType SCROLL_TO_TOP_INDEX;
   private static final EventType SCROLL_TO_COLUMN;
   private static final long serialVersionUID = -8557345736849482516L;
   private final Object scrollTarget;

   public static EventType scrollToTopIndex() {
      return SCROLL_TO_TOP_INDEX;
   }

   public static EventType scrollToColumn() {
      return SCROLL_TO_COLUMN;
   }

   public ScrollToEvent(@NamedArg("source") Object var1, @NamedArg("target") EventTarget var2, @NamedArg("type") EventType var3, @NamedArg("scrollTarget") Object var4) {
      super(var1, var2, var3);

      assert var4 != null;

      this.scrollTarget = var4;
   }

   public Object getScrollTarget() {
      return this.scrollTarget;
   }

   static {
      ANY = new EventType(Event.ANY, "SCROLL_TO");
      SCROLL_TO_TOP_INDEX = new EventType(ANY, "SCROLL_TO_TOP_INDEX");
      SCROLL_TO_COLUMN = new EventType(ANY, "SCROLL_TO_COLUMN");
   }
}
