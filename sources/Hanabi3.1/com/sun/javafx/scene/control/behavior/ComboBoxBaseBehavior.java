package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ComboBoxBaseBehavior extends BehaviorBase {
   private TwoLevelFocusComboBehavior tlFocus;
   private KeyEvent lastEvent;
   private boolean keyDown;
   private static final String PRESS_ACTION = "Press";
   private static final String RELEASE_ACTION = "Release";
   protected static final List COMBO_BOX_BASE_BINDINGS = new ArrayList();
   private boolean showPopupOnMouseRelease = true;
   private boolean mouseInsideButton = false;

   public ComboBoxBaseBehavior(ComboBoxBase var1, List var2) {
      super(var1, var2);
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

   protected void focusChanged() {
      ComboBoxBase var1 = (ComboBoxBase)this.getControl();
      if (this.keyDown && !var1.isFocused()) {
         this.keyDown = false;
         var1.disarm();
      }

   }

   protected void callActionForEvent(KeyEvent var1) {
      this.lastEvent = var1;
      this.showPopupOnMouseRelease = true;
      super.callActionForEvent(var1);
   }

   protected void callAction(String var1) {
      if ("Press".equals(var1)) {
         this.keyPressed();
      } else if ("Release".equals(var1)) {
         this.keyReleased();
      } else if ("showPopup".equals(var1)) {
         this.show();
      } else if ("togglePopup".equals(var1)) {
         if (((ComboBoxBase)this.getControl()).isShowing()) {
            this.hide();
         } else {
            this.show();
         }
      } else if ("Cancel".equals(var1)) {
         this.cancelEdit(this.lastEvent);
      } else if ("ToParent".equals(var1)) {
         this.forwardToParent(this.lastEvent);
      } else {
         super.callAction(var1);
      }

   }

   private void keyPressed() {
      if (Utils.isTwoLevelFocus()) {
         this.show();
         if (this.tlFocus != null) {
            this.tlFocus.setExternalFocus(false);
         }
      } else if (!((ComboBoxBase)this.getControl()).isPressed() && !((ComboBoxBase)this.getControl()).isArmed()) {
         this.keyDown = true;
         ((ComboBoxBase)this.getControl()).arm();
      }

   }

   private void keyReleased() {
      if (!Utils.isTwoLevelFocus() && this.keyDown) {
         this.keyDown = false;
         if (((ComboBoxBase)this.getControl()).isArmed()) {
            ((ComboBoxBase)this.getControl()).disarm();
         }
      }

   }

   protected void forwardToParent(KeyEvent var1) {
      if (((ComboBoxBase)this.getControl()).getParent() != null) {
         ((ComboBoxBase)this.getControl()).getParent().fireEvent(var1);
      }

   }

   protected void cancelEdit(KeyEvent var1) {
      ComboBoxBase var2 = (ComboBoxBase)this.getControl();
      TextField var3 = null;
      if (var2 instanceof DatePicker) {
         var3 = ((DatePicker)var2).getEditor();
      } else if (var2 instanceof ComboBox) {
         var3 = var2.isEditable() ? ((ComboBox)var2).getEditor() : null;
      }

      if (var3 != null && var3.getTextFormatter() != null) {
         var3.cancelEdit();
      } else {
         this.forwardToParent(var1);
      }

   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      this.arm(var1);
   }

   public void mouseReleased(MouseEvent var1) {
      super.mouseReleased(var1);
      this.disarm();
      if (this.showPopupOnMouseRelease) {
         this.show();
      } else {
         this.showPopupOnMouseRelease = true;
         this.hide();
      }

   }

   public void mouseEntered(MouseEvent var1) {
      super.mouseEntered(var1);
      if (!((ComboBoxBase)this.getControl()).isEditable()) {
         this.mouseInsideButton = true;
      } else {
         EventTarget var2 = var1.getTarget();
         this.mouseInsideButton = var2 instanceof Node && "arrow-button".equals(((Node)var2).getId());
      }

      this.arm();
   }

   public void mouseExited(MouseEvent var1) {
      super.mouseExited(var1);
      this.mouseInsideButton = false;
      this.disarm();
   }

   private void getFocus() {
      if (!((ComboBoxBase)this.getControl()).isFocused() && ((ComboBoxBase)this.getControl()).isFocusTraversable()) {
         ((ComboBoxBase)this.getControl()).requestFocus();
      }

   }

   private void arm(MouseEvent var1) {
      boolean var2 = var1.getButton() == MouseButton.PRIMARY && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown() && !var1.isShiftDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isMetaDown();
      if (!((ComboBoxBase)this.getControl()).isArmed() && var2) {
         ((ComboBoxBase)this.getControl()).arm();
      }

   }

   public void show() {
      if (!((ComboBoxBase)this.getControl()).isShowing()) {
         ((ComboBoxBase)this.getControl()).requestFocus();
         ((ComboBoxBase)this.getControl()).show();
      }

   }

   public void hide() {
      if (((ComboBoxBase)this.getControl()).isShowing()) {
         ((ComboBoxBase)this.getControl()).hide();
      }

   }

   public void onAutoHide() {
      this.hide();
      this.showPopupOnMouseRelease = this.mouseInsideButton ? !this.showPopupOnMouseRelease : true;
   }

   public void arm() {
      if (((ComboBoxBase)this.getControl()).isPressed()) {
         ((ComboBoxBase)this.getControl()).arm();
      }

   }

   public void disarm() {
      if (!this.keyDown && ((ComboBoxBase)this.getControl()).isArmed()) {
         ((ComboBoxBase)this.getControl()).disarm();
      }

   }

   static {
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F4, KeyEvent.KEY_RELEASED, "togglePopup"));
      COMBO_BOX_BASE_BINDINGS.add((new KeyBinding(KeyCode.UP, "togglePopup")).alt());
      COMBO_BOX_BASE_BINDINGS.add((new KeyBinding(KeyCode.DOWN, "togglePopup")).alt());
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Press"));
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "Release"));
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Press"));
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "Release"));
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "Cancel"));
      COMBO_BOX_BASE_BINDINGS.add(new KeyBinding(KeyCode.F10, "ToParent"));
   }
}
