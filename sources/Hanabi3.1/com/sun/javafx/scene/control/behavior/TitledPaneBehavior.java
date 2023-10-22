package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class TitledPaneBehavior extends BehaviorBase {
   private TitledPane titledPane;
   private static final String PRESS_ACTION = "Press";
   protected static final List TITLEDPANE_BINDINGS = new ArrayList();

   public TitledPaneBehavior(TitledPane var1) {
      super(var1, TITLEDPANE_BINDINGS);
      this.titledPane = var1;
   }

   protected void callAction(String var1) {
      switch (var1) {
         case "Press":
            if (this.titledPane.isCollapsible() && this.titledPane.isFocused()) {
               this.titledPane.setExpanded(!this.titledPane.isExpanded());
               this.titledPane.requestFocus();
            }
            break;
         default:
            super.callAction(var1);
      }

   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      TitledPane var2 = (TitledPane)this.getControl();
      var2.requestFocus();
   }

   public void expand() {
      this.titledPane.setExpanded(true);
   }

   public void collapse() {
      this.titledPane.setExpanded(false);
   }

   public void toggle() {
      this.titledPane.setExpanded(!this.titledPane.isExpanded());
   }

   static {
      TITLEDPANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Press"));
   }
}
