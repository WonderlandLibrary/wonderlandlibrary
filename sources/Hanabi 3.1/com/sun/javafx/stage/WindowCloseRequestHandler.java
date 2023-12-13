package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public final class WindowCloseRequestHandler extends BasicEventDispatcher {
   private final Window window;

   public WindowCloseRequestHandler(Window var1) {
      this.window = var1;
   }

   public Event dispatchBubblingEvent(Event var1) {
      if (var1.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
         this.window.hide();
         var1.consume();
      }

      return var1;
   }
}
