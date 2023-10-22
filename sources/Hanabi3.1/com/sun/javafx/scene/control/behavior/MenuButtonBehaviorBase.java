package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class MenuButtonBehaviorBase extends ButtonBehavior {
   protected static final String OPEN_ACTION = "Open";
   protected static final String CLOSE_ACTION = "Close";
   protected static final List BASE_MENU_BUTTON_BINDINGS = new ArrayList();

   public MenuButtonBehaviorBase(MenuButton var1, List var2) {
      super(var1, var2);
   }

   protected void callAction(String var1) {
      MenuButton var2 = (MenuButton)this.getControl();
      Side var3 = var2.getPopupSide();
      if ("Close".equals(var1)) {
         var2.hide();
      } else if ("Open".equals(var1)) {
         if (var2.isShowing()) {
            var2.hide();
         } else {
            var2.show();
         }
      } else if ((var2.isShowing() || !"TraverseUp".equals(var1) || var3 != Side.TOP) && (!"TraverseDown".equals(var1) || var3 != Side.BOTTOM && var3 != Side.TOP) && (!"TraverseLeft".equals(var1) || var3 != Side.RIGHT && var3 != Side.LEFT) && (!"TraverseRight".equals(var1) || var3 != Side.RIGHT && var3 != Side.LEFT)) {
         super.callAction(var1);
      } else {
         var2.show();
      }

   }

   public void mousePressed(MouseEvent var1, boolean var2) {
      MenuButton var3 = (MenuButton)this.getControl();
      if (var2) {
         if (var3.isShowing()) {
            var3.hide();
         }

         super.mousePressed(var1);
      } else {
         if (!var3.isFocused() && var3.isFocusTraversable()) {
            var3.requestFocus();
         }

         if (var3.isShowing()) {
            var3.hide();
         } else if (var1.getButton() == MouseButton.PRIMARY) {
            var3.show();
         }
      }

   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1, boolean var2) {
      if (var2) {
         super.mouseReleased(var1);
      } else {
         if (((MenuButton)this.getControl()).isShowing() && !((MenuButton)this.getControl()).contains(var1.getX(), var1.getY())) {
            ((MenuButton)this.getControl()).hide();
         }

         ((MenuButton)this.getControl()).disarm();
      }

   }

   static {
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_PRESSED, "Close"));
      BASE_MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, KeyEvent.KEY_PRESSED, "Close"));
   }
}
