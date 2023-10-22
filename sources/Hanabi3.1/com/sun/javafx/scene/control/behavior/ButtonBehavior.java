package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ButtonBehavior extends BehaviorBase {
   private boolean keyDown;
   private static final String PRESS_ACTION = "Press";
   private static final String RELEASE_ACTION = "Release";
   protected static final List BUTTON_BINDINGS = new ArrayList();

   public ButtonBehavior(ButtonBase var1) {
      super(var1, BUTTON_BINDINGS);
   }

   public ButtonBehavior(ButtonBase var1, List var2) {
      super(var1, var2);
   }

   protected void focusChanged() {
      ButtonBase var1 = (ButtonBase)this.getControl();
      if (this.keyDown && !var1.isFocused()) {
         this.keyDown = false;
         var1.disarm();
      }

   }

   protected void callAction(String var1) {
      if (!((ButtonBase)this.getControl()).isDisabled()) {
         if ("Press".equals(var1)) {
            this.keyPressed();
         } else if ("Release".equals(var1)) {
            this.keyReleased();
         } else {
            super.callAction(var1);
         }
      }

   }

   private void keyPressed() {
      ButtonBase var1 = (ButtonBase)this.getControl();
      if (!var1.isPressed() && !var1.isArmed()) {
         this.keyDown = true;
         var1.arm();
      }

   }

   private void keyReleased() {
      ButtonBase var1 = (ButtonBase)this.getControl();
      if (this.keyDown) {
         this.keyDown = false;
         if (var1.isArmed()) {
            var1.disarm();
            var1.fire();
         }
      }

   }

   public void mousePressed(MouseEvent var1) {
      ButtonBase var2 = (ButtonBase)this.getControl();
      super.mousePressed(var1);
      if (!var2.isFocused() && var2.isFocusTraversable()) {
         var2.requestFocus();
      }

      boolean var3 = var1.getButton() == MouseButton.PRIMARY && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown() && !var1.isShiftDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isMetaDown();
      if (!var2.isArmed() && var3) {
         var2.arm();
      }

   }

   public void mouseReleased(MouseEvent var1) {
      ButtonBase var2 = (ButtonBase)this.getControl();
      if (!this.keyDown && var2.isArmed()) {
         var2.fire();
         var2.disarm();
      }

   }

   public void mouseEntered(MouseEvent var1) {
      ButtonBase var2 = (ButtonBase)this.getControl();
      super.mouseEntered(var1);
      if (!this.keyDown && var2.isPressed()) {
         var2.arm();
      }

   }

   public void mouseExited(MouseEvent var1) {
      ButtonBase var2 = (ButtonBase)this.getControl();
      super.mouseExited(var1);
      if (!this.keyDown && var2.isArmed()) {
         var2.disarm();
      }

   }

   static {
      BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Press"));
      BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "Release"));
   }
}
