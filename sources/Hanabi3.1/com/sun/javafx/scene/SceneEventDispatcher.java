package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;

public class SceneEventDispatcher extends CompositeEventDispatcher {
   private final KeyboardShortcutsHandler keyboardShortcutsHandler;
   private final EnteredExitedHandler enteredExitedHandler;
   private final EventHandlerManager eventHandlerManager;

   public SceneEventDispatcher(Object var1) {
      this(new KeyboardShortcutsHandler(), new EnteredExitedHandler(var1), new EventHandlerManager(var1));
   }

   public SceneEventDispatcher(KeyboardShortcutsHandler var1, EnteredExitedHandler var2, EventHandlerManager var3) {
      this.keyboardShortcutsHandler = var1;
      this.enteredExitedHandler = var2;
      this.eventHandlerManager = var3;
      var1.insertNextDispatcher(var2);
      var2.insertNextDispatcher(var3);
   }

   public final KeyboardShortcutsHandler getKeyboardShortcutsHandler() {
      return this.keyboardShortcutsHandler;
   }

   public final EnteredExitedHandler getEnteredExitedHandler() {
      return this.enteredExitedHandler;
   }

   public final EventHandlerManager getEventHandlerManager() {
      return this.eventHandlerManager;
   }

   public BasicEventDispatcher getFirstDispatcher() {
      return this.keyboardShortcutsHandler;
   }

   public BasicEventDispatcher getLastDispatcher() {
      return this.eventHandlerManager;
   }
}
