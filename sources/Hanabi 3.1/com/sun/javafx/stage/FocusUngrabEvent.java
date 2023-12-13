package com.sun.javafx.stage;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public final class FocusUngrabEvent extends Event {
   private static final long serialVersionUID = 20121107L;
   public static final EventType FOCUS_UNGRAB;
   public static final EventType ANY;

   public FocusUngrabEvent() {
      super(FOCUS_UNGRAB);
   }

   public FocusUngrabEvent(Object var1, EventTarget var2) {
      super(var1, var2, FOCUS_UNGRAB);
   }

   static {
      FOCUS_UNGRAB = new EventType(Event.ANY, "FOCUS_UNGRAB");
      ANY = FOCUS_UNGRAB;
   }
}
