package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

public abstract class BehaviorSkinBase extends SkinBase {
   protected static final boolean IS_TOUCH_SUPPORTED;
   private BehaviorBase behavior;
   private MultiplePropertyChangeListenerHandler changeListenerHandler;
   private final EventHandler mouseHandler = new EventHandler() {
      public void handle(MouseEvent var1) {
         EventType var2 = var1.getEventType();
         if (var2 == MouseEvent.MOUSE_ENTERED) {
            BehaviorSkinBase.this.behavior.mouseEntered(var1);
         } else if (var2 == MouseEvent.MOUSE_EXITED) {
            BehaviorSkinBase.this.behavior.mouseExited(var1);
         } else if (var2 == MouseEvent.MOUSE_PRESSED) {
            BehaviorSkinBase.this.behavior.mousePressed(var1);
         } else if (var2 == MouseEvent.MOUSE_RELEASED) {
            BehaviorSkinBase.this.behavior.mouseReleased(var1);
         } else {
            if (var2 != MouseEvent.MOUSE_DRAGGED) {
               throw new AssertionError("Unsupported event type received");
            }

            BehaviorSkinBase.this.behavior.mouseDragged(var1);
         }

      }
   };
   private final EventHandler contextMenuHandler = new EventHandler() {
      public void handle(ContextMenuEvent var1) {
         BehaviorSkinBase.this.behavior.contextMenuRequested(var1);
      }
   };

   protected BehaviorSkinBase(Control var1, BehaviorBase var2) {
      super(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Cannot pass null for behavior");
      } else {
         this.behavior = var2;
         var1.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
         var1.addEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
         var1.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
         var1.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
         var1.addEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
         var1.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, this.contextMenuHandler);
      }
   }

   public final BehaviorBase getBehavior() {
      return this.behavior;
   }

   public void dispose() {
      if (this.changeListenerHandler != null) {
         this.changeListenerHandler.dispose();
      }

      Control var1 = this.getSkinnable();
      if (var1 != null) {
         var1.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
         var1.removeEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
         var1.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
         var1.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
         var1.removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
      }

      if (this.behavior != null) {
         this.behavior.dispose();
         this.behavior = null;
      }

      super.dispose();
   }

   protected final void registerChangeListener(ObservableValue var1, String var2) {
      if (this.changeListenerHandler == null) {
         this.changeListenerHandler = new MultiplePropertyChangeListenerHandler((var1x) -> {
            this.handleControlPropertyChanged(var1x);
            return null;
         });
      }

      this.changeListenerHandler.registerChangeListener(var1, var2);
   }

   protected final void unregisterChangeListener(ObservableValue var1) {
      this.changeListenerHandler.unregisterChangeListener(var1);
   }

   protected void handleControlPropertyChanged(String var1) {
   }

   static {
      IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);
   }
}
