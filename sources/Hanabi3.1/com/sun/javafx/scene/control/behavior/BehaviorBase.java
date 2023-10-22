package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class BehaviorBase {
   protected static final boolean IS_TOUCH_SUPPORTED;
   protected static final List TRAVERSAL_BINDINGS;
   static final String TRAVERSE_UP = "TraverseUp";
   static final String TRAVERSE_DOWN = "TraverseDown";
   static final String TRAVERSE_LEFT = "TraverseLeft";
   static final String TRAVERSE_RIGHT = "TraverseRight";
   static final String TRAVERSE_NEXT = "TraverseNext";
   static final String TRAVERSE_PREVIOUS = "TraversePrevious";
   private final Control control;
   private final List keyBindings;
   private final EventHandler keyEventListener = (var1x) -> {
      if (!var1x.isConsumed()) {
         this.callActionForEvent(var1x);
      }

   };
   private final InvalidationListener focusListener = (var1x) -> {
      this.focusChanged();
   };

   public BehaviorBase(Control var1, List var2) {
      this.control = var1;
      this.keyBindings = var2 == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(var2));
      var1.addEventHandler(KeyEvent.ANY, this.keyEventListener);
      var1.focusedProperty().addListener(this.focusListener);
   }

   public void dispose() {
      this.control.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
      this.control.focusedProperty().removeListener(this.focusListener);
   }

   public final Control getControl() {
      return this.control;
   }

   protected void callActionForEvent(KeyEvent var1) {
      String var2 = this.matchActionForEvent(var1);
      if (var2 != null) {
         this.callAction(var2);
         var1.consume();
      }

   }

   protected String matchActionForEvent(KeyEvent var1) {
      if (var1 == null) {
         throw new NullPointerException("KeyEvent must not be null");
      } else {
         KeyBinding var2 = null;
         int var3 = 0;
         int var4 = this.keyBindings.size();

         for(int var5 = 0; var5 < var4; ++var5) {
            KeyBinding var6 = (KeyBinding)this.keyBindings.get(var5);
            int var7 = var6.getSpecificity(this.control, var1);
            if (var7 > var3) {
               var3 = var7;
               var2 = var6;
            }
         }

         String var8 = null;
         if (var2 != null) {
            var8 = var2.getAction();
         }

         return var8;
      }
   }

   protected void callAction(String var1) {
      switch (var1) {
         case "TraverseUp":
            this.traverseUp();
            break;
         case "TraverseDown":
            this.traverseDown();
            break;
         case "TraverseLeft":
            this.traverseLeft();
            break;
         case "TraverseRight":
            this.traverseRight();
            break;
         case "TraverseNext":
            this.traverseNext();
            break;
         case "TraversePrevious":
            this.traversePrevious();
      }

   }

   protected void traverse(Node var1, Direction var2) {
      var1.impl_traverse(var2);
   }

   public final void traverseUp() {
      this.traverse(this.control, Direction.UP);
   }

   public final void traverseDown() {
      this.traverse(this.control, Direction.DOWN);
   }

   public final void traverseLeft() {
      this.traverse(this.control, Direction.LEFT);
   }

   public final void traverseRight() {
      this.traverse(this.control, Direction.RIGHT);
   }

   public final void traverseNext() {
      this.traverse(this.control, Direction.NEXT);
   }

   public final void traversePrevious() {
      this.traverse(this.control, Direction.PREVIOUS);
   }

   protected void focusChanged() {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseDragged(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void contextMenuRequested(ContextMenuEvent var1) {
   }

   static {
      IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);
      TRAVERSAL_BINDINGS = new ArrayList();
      TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
      TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext"));
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraversePrevious")).shift());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.UP, "TraverseUp")).shift().alt().ctrl());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "TraverseDown")).shift().alt().ctrl());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.LEFT, "TraverseLeft")).shift().alt().ctrl());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.RIGHT, "TraverseRight")).shift().alt().ctrl());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraverseNext")).shift().alt().ctrl());
      TRAVERSAL_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraversePrevious")).alt().ctrl());
   }
}
