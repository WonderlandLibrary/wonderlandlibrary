package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;

public class NodeEventDispatcher extends CompositeEventDispatcher {
   private final EnteredExitedHandler enteredExitedHandler;
   private final EventHandlerManager eventHandlerManager;

   public NodeEventDispatcher(Object var1) {
      this(new EnteredExitedHandler(var1), new EventHandlerManager(var1));
   }

   public NodeEventDispatcher(EnteredExitedHandler var1, EventHandlerManager var2) {
      this.enteredExitedHandler = var1;
      this.eventHandlerManager = var2;
      var1.insertNextDispatcher(var2);
   }

   public final EnteredExitedHandler getEnteredExitedHandler() {
      return this.enteredExitedHandler;
   }

   public final EventHandlerManager getEventHandlerManager() {
      return this.eventHandlerManager;
   }

   public BasicEventDispatcher getFirstDispatcher() {
      return this.enteredExitedHandler;
   }

   public BasicEventDispatcher getLastDispatcher() {
      return this.eventHandlerManager;
   }
}
