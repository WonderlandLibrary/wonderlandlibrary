package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ChoiceBoxBehavior extends BehaviorBase {
   protected static final List CHOICE_BUTTON_BINDINGS = new ArrayList();
   private TwoLevelFocusComboBehavior tlFocus;

   protected void callAction(String var1) {
      if (var1.equals("Cancel")) {
         this.cancel();
      } else if (var1.equals("Press")) {
         this.keyPressed();
      } else if (var1.equals("Release")) {
         this.keyReleased();
      } else if (var1.equals("Down")) {
         this.showPopup();
      } else {
         super.callAction(var1);
      }

   }

   public ChoiceBoxBehavior(ChoiceBox var1) {
      super(var1, CHOICE_BUTTON_BINDINGS);
      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusComboBehavior(var1);
      }

   }

   public void dispose() {
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   public void select(int var1) {
      SingleSelectionModel var2 = ((ChoiceBox)this.getControl()).getSelectionModel();
      if (var2 != null) {
         var2.select(var1);
      }
   }

   public void close() {
      ((ChoiceBox)this.getControl()).hide();
   }

   public void showPopup() {
      ((ChoiceBox)this.getControl()).show();
   }

   public void mousePressed(MouseEvent var1) {
      ChoiceBox var2 = (ChoiceBox)this.getControl();
      super.mousePressed(var1);
      if (var2.isFocusTraversable()) {
         var2.requestFocus();
      }

   }

   public void mouseReleased(MouseEvent var1) {
      ChoiceBox var2 = (ChoiceBox)this.getControl();
      super.mouseReleased(var1);
      if (!var2.isShowing() && var2.contains(var1.getX(), var1.getY())) {
         if (var1.getButton() == MouseButton.PRIMARY) {
            var2.show();
         }
      } else {
         var2.hide();
      }

   }

   private void keyPressed() {
      ChoiceBox var1 = (ChoiceBox)this.getControl();
      if (!var1.isShowing()) {
         var1.show();
      }

   }

   private void keyReleased() {
   }

   public void cancel() {
      ChoiceBox var1 = (ChoiceBox)this.getControl();
      var1.hide();
   }

   static {
      CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Press"));
      CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "Release"));
      if (Utils.isTwoLevelFocus()) {
         CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Press"));
         CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "Release"));
      }

      CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_RELEASED, "Cancel"));
      CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_RELEASED, "Down"));
      CHOICE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.CANCEL, KeyEvent.KEY_RELEASED, "Cancel"));
   }
}
