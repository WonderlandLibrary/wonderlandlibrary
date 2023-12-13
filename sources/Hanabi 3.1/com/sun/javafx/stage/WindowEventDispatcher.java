package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventRedirector;
import javafx.stage.Window;

public class WindowEventDispatcher extends CompositeEventDispatcher {
   private final EventRedirector eventRedirector;
   private final WindowCloseRequestHandler windowCloseRequestHandler;
   private final EventHandlerManager eventHandlerManager;

   public WindowEventDispatcher(Window var1) {
      this(new EventRedirector(var1), new WindowCloseRequestHandler(var1), new EventHandlerManager(var1));
   }

   public WindowEventDispatcher(EventRedirector var1, WindowCloseRequestHandler var2, EventHandlerManager var3) {
      this.eventRedirector = var1;
      this.windowCloseRequestHandler = var2;
      this.eventHandlerManager = var3;
      var1.insertNextDispatcher(var2);
      var2.insertNextDispatcher(var3);
   }

   public final EventRedirector getEventRedirector() {
      return this.eventRedirector;
   }

   public final WindowCloseRequestHandler getWindowCloseRequestHandler() {
      return this.windowCloseRequestHandler;
   }

   public final EventHandlerManager getEventHandlerManager() {
      return this.eventHandlerManager;
   }

   public BasicEventDispatcher getFirstDispatcher() {
      return this.eventRedirector;
   }

   public BasicEventDispatcher getLastDispatcher() {
      return this.eventHandlerManager;
   }
}
