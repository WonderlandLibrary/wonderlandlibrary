package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ComboBoxListViewBehavior extends ComboBoxBaseBehavior {
   protected static final List COMBO_BOX_BINDINGS = new ArrayList();

   public ComboBoxListViewBehavior(ComboBox var1) {
      super(var1, COMBO_BOX_BINDINGS);
   }

   protected void callAction(String var1) {
      if ("selectPrevious".equals(var1)) {
         this.selectPrevious();
      } else if ("selectNext".equals(var1)) {
         this.selectNext();
      } else {
         super.callAction(var1);
      }

   }

   private ComboBox getComboBox() {
      return (ComboBox)this.getControl();
   }

   private void selectPrevious() {
      SingleSelectionModel var1 = this.getComboBox().getSelectionModel();
      if (var1 != null) {
         var1.selectPrevious();
      }
   }

   private void selectNext() {
      SingleSelectionModel var1 = this.getComboBox().getSelectionModel();
      if (var1 != null) {
         var1.selectNext();
      }
   }

   static {
      COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "selectPrevious"));
      COMBO_BOX_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "selectNext"));
      COMBO_BOX_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
   }
}
