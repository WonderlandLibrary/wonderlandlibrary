package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class TwoLevelFocusComboBehavior extends TwoLevelFocusBehavior {
   final EventDispatcher preemptiveEventDispatcher = (var1x, var2) -> {
      if (var1x instanceof KeyEvent && var1x.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent)var1x).isMetaDown() && !((KeyEvent)var1x).isControlDown() && !((KeyEvent)var1x).isAltDown() && this.isExternalFocus()) {
         EventTarget var3 = var1x.getTarget();
         switch (((KeyEvent)var1x).getCode()) {
            case TAB:
               if (((KeyEvent)var1x).isShiftDown()) {
                  ((Node)var3).impl_traverse(Direction.PREVIOUS);
               } else {
                  ((Node)var3).impl_traverse(Direction.NEXT);
               }

               var1x.consume();
               break;
            case UP:
               ((Node)var3).impl_traverse(Direction.UP);
               var1x.consume();
               break;
            case DOWN:
               ((Node)var3).impl_traverse(Direction.DOWN);
               var1x.consume();
               break;
            case LEFT:
               ((Node)var3).impl_traverse(Direction.LEFT);
               var1x.consume();
               break;
            case RIGHT:
               ((Node)var3).impl_traverse(Direction.RIGHT);
               var1x.consume();
               break;
            case ENTER:
               this.setExternalFocus(false);
               this.origEventDispatcher.dispatchEvent(var1x, var2);
               break;
            default:
               Scene var4 = this.tlNode.getScene();
               Event.fireEvent(var4, var1x);
               var1x.consume();
         }
      }

      return var1x;
   };
   final EventDispatcher tlfEventDispatcher = (var1x, var2) -> {
      if (var1x instanceof KeyEvent && this.isExternalFocus()) {
         var2 = var2.prepend(this.preemptiveEventDispatcher);
         return var2.dispatchEvent(var1x);
      } else {
         return this.origEventDispatcher.dispatchEvent(var1x, var2);
      }
   };
   private final EventHandler keyEventListener = (var1x) -> {
      this.postDispatchTidyup(var1x);
   };
   final ChangeListener focusListener = (var1x, var2, var3) -> {
      this.setExternalFocus(true);
   };
   private final EventHandler mouseEventListener = (var1x) -> {
      this.setExternalFocus(false);
   };

   public TwoLevelFocusComboBehavior(Node var1) {
      this.tlNode = var1;
      this.tlNode.addEventHandler(KeyEvent.ANY, this.keyEventListener);
      this.tlNode.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
      this.tlNode.focusedProperty().addListener(this.focusListener);
      this.origEventDispatcher = this.tlNode.getEventDispatcher();
      this.tlNode.setEventDispatcher(this.tlfEventDispatcher);
   }

   public void dispose() {
      this.tlNode.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
      this.tlNode.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
      this.tlNode.focusedProperty().removeListener(this.focusListener);
      this.tlNode.setEventDispatcher(this.origEventDispatcher);
   }

   private Event postDispatchTidyup(Event var1) {
      if (var1 instanceof KeyEvent && var1.getEventType() == KeyEvent.KEY_PRESSED && !this.isExternalFocus() && !((KeyEvent)var1).isMetaDown() && !((KeyEvent)var1).isControlDown() && !((KeyEvent)var1).isAltDown()) {
         switch (((KeyEvent)var1).getCode()) {
            case TAB:
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
               var1.consume();
               break;
            case ENTER:
               this.setExternalFocus(true);
               var1.consume();
         }
      }

      return var1;
   }
}
