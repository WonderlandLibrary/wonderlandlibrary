package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;

public class ToolBarBehavior extends BehaviorBase {
   private static final String CTRL_F5 = "Ctrl_F5";
   protected static final List TOOLBAR_BINDINGS = new ArrayList();

   public ToolBarBehavior(ToolBar var1) {
      super(var1, TOOLBAR_BINDINGS);
   }

   protected void callAction(String var1) {
      if ("Ctrl_F5".equals(var1)) {
         ToolBar var2 = (ToolBar)this.getControl();
         if (!var2.getItems().isEmpty()) {
            ((Node)var2.getItems().get(0)).requestFocus();
         }
      } else {
         super.callAction(var1);
      }

   }

   static {
      TOOLBAR_BINDINGS.add((new KeyBinding(KeyCode.F5, "Ctrl_F5")).ctrl());
   }
}
