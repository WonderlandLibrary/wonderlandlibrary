package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextFieldBehavior extends TextInputControlBehavior {
   private TextFieldSkin skin;
   private ContextMenu contextMenu = new ContextMenu();
   private TwoLevelFocusBehavior tlFocus;
   private ChangeListener sceneListener;
   private ChangeListener focusOwnerListener;
   private boolean focusGainedByMouseClick = false;
   private boolean shiftDown = false;
   private boolean deferClick = false;

   public TextFieldBehavior(TextField var1) {
      super(var1, TEXT_INPUT_BINDINGS);
      if (IS_TOUCH_SUPPORTED) {
         this.contextMenu.getStyleClass().add("text-input-context-menu");
      }

      this.handleFocusChange();
      var1.focusedProperty().addListener((var1x, var2x, var3) -> {
         this.handleFocusChange();
      });
      this.focusOwnerListener = (var2x, var3, var4) -> {
         if (var4 == var1) {
            if (!this.focusGainedByMouseClick) {
               var1.selectRange(var1.getLength(), 0);
            }
         } else {
            var1.selectRange(0, 0);
         }

      };
      WeakChangeListener var2 = new WeakChangeListener(this.focusOwnerListener);
      this.sceneListener = (var1x, var2x, var3) -> {
         if (var2x != null) {
            var2x.focusOwnerProperty().removeListener(var2);
         }

         if (var3 != null) {
            var3.focusOwnerProperty().addListener(var2);
         }

      };
      var1.sceneProperty().addListener(new WeakChangeListener(this.sceneListener));
      if (var1.getScene() != null) {
         var1.getScene().focusOwnerProperty().addListener(var2);
      }

      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusBehavior(var1);
      }

   }

   public void dispose() {
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   private void handleFocusChange() {
      TextField var1 = (TextField)this.getControl();
      if (var1.isFocused()) {
         if (PlatformUtil.isIOS()) {
            TextInputTypes var2 = TextFieldBehavior.TextInputTypes.TEXT_FIELD;
            if (var1.getClass().equals(PasswordField.class)) {
               var2 = TextFieldBehavior.TextInputTypes.PASSWORD_FIELD;
            } else if (var1.getParent().getClass().equals(ComboBox.class)) {
               var2 = TextFieldBehavior.TextInputTypes.EDITABLE_COMBO;
            }

            Bounds var3 = var1.getBoundsInParent();
            double var4 = var3.getWidth();
            double var6 = var3.getHeight();
            Affine3D var8 = calculateNodeToSceneTransform(var1);
            String var9 = var1.getText();
            var1.getScene().getWindow().impl_getPeer().requestInput(var9, var2.ordinal(), var4, var6, var8.getMxx(), var8.getMxy(), var8.getMxz(), var8.getMxt(), var8.getMyx(), var8.getMyy(), var8.getMyz(), var8.getMyt(), var8.getMzx(), var8.getMzy(), var8.getMzz(), var8.getMzt());
         }

         if (!this.focusGainedByMouseClick) {
            this.setCaretAnimating(true);
         }
      } else {
         if (PlatformUtil.isIOS() && var1.getScene() != null) {
            var1.getScene().getWindow().impl_getPeer().releaseInput();
         }

         this.focusGainedByMouseClick = false;
         this.setCaretAnimating(false);
      }

   }

   static Affine3D calculateNodeToSceneTransform(Node var0) {
      Affine3D var1 = new Affine3D();

      do {
         var1.preConcatenate(((Node)var0).impl_getLeafTransform());
         var0 = ((Node)var0).getParent();
      } while(var0 != null);

      return var1;
   }

   public void setTextFieldSkin(TextFieldSkin var1) {
      this.skin = var1;
   }

   protected void fire(KeyEvent var1) {
      TextField var2 = (TextField)this.getControl();
      EventHandler var3 = var2.getOnAction();
      ActionEvent var4 = new ActionEvent(var2, (EventTarget)null);
      var2.fireEvent(var4);
      var2.commitValue();
      if (var3 == null && !var4.isConsumed()) {
         this.forwardToParent(var1);
      }

   }

   protected void cancelEdit(KeyEvent var1) {
      TextField var2 = (TextField)this.getControl();
      if (var2.getTextFormatter() != null) {
         var2.cancelEdit();
      } else {
         this.forwardToParent(var1);
      }

   }

   protected void deleteChar(boolean var1) {
      this.skin.deleteChar(var1);
   }

   protected void replaceText(int var1, int var2, String var3) {
      this.skin.replaceText(var1, var2, var3);
   }

   protected void deleteFromLineStart() {
      TextField var1 = (TextField)this.getControl();
      int var2 = var1.getCaretPosition();
      if (var2 > 0) {
         this.replaceText(0, var2, "");
      }

   }

   protected void setCaretAnimating(boolean var1) {
      if (this.skin != null) {
         this.skin.setCaretAnimating(var1);
      }

   }

   private void beep() {
   }

   public void mousePressed(MouseEvent var1) {
      TextField var2 = (TextField)this.getControl();
      super.mousePressed(var1);
      if (!var2.isDisabled()) {
         if (!var2.isFocused()) {
            this.focusGainedByMouseClick = true;
            var2.requestFocus();
         }

         this.setCaretAnimating(false);
         if (var1.isPrimaryButtonDown() && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown()) {
            HitInfo var3 = this.skin.getIndex(var1.getX(), var1.getY());
            String var4 = var2.textProperty().getValueSafe();
            int var5 = Utils.getHitInsertionIndex(var3, var4);
            int var6 = var2.getAnchor();
            int var7 = var2.getCaretPosition();
            if (var1.getClickCount() < 2 && (IS_TOUCH_SUPPORTED || var6 != var7 && (var5 > var6 && var5 < var7 || var5 < var6 && var5 > var7))) {
               this.deferClick = true;
            } else if (!var1.isControlDown() && !var1.isAltDown() && !var1.isShiftDown() && !var1.isMetaDown()) {
               switch (var1.getClickCount()) {
                  case 1:
                     this.mouseSingleClick(var3);
                     break;
                  case 2:
                     this.mouseDoubleClick(var3);
                     break;
                  case 3:
                     this.mouseTripleClick(var3);
               }
            } else if (var1.isShiftDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isMetaDown() && var1.getClickCount() == 1) {
               this.shiftDown = true;
               if (PlatformUtil.isMac()) {
                  var2.extendSelection(var5);
               } else {
                  this.skin.positionCaret(var3, true);
               }
            }

            this.skin.setForwardBias(var3.isLeading());
         }
      }

      if (this.contextMenu.isShowing()) {
         this.contextMenu.hide();
      }

   }

   public void mouseDragged(MouseEvent var1) {
      TextField var2 = (TextField)this.getControl();
      if (!var2.isDisabled() && !this.deferClick && var1.isPrimaryButtonDown() && !var1.isMiddleButtonDown() && !var1.isSecondaryButtonDown() && !var1.isControlDown() && !var1.isAltDown() && !var1.isShiftDown() && !var1.isMetaDown()) {
         this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), true);
      }

   }

   public void mouseReleased(MouseEvent var1) {
      TextField var2 = (TextField)this.getControl();
      super.mouseReleased(var1);
      if (!var2.isDisabled()) {
         this.setCaretAnimating(false);
         if (this.deferClick) {
            this.deferClick = false;
            this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), this.shiftDown);
            this.shiftDown = false;
         }

         this.setCaretAnimating(true);
      }

   }

   public void contextMenuRequested(ContextMenuEvent var1) {
      TextField var2 = (TextField)this.getControl();
      if (this.contextMenu.isShowing()) {
         this.contextMenu.hide();
      } else if (var2.getContextMenu() == null) {
         double var3 = var1.getScreenX();
         double var5 = var1.getScreenY();
         double var7 = var1.getSceneX();
         if (IS_TOUCH_SUPPORTED) {
            Point2D var9;
            if (var2.getSelection().getLength() == 0) {
               this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), false);
               var9 = this.skin.getMenuPosition();
            } else {
               var9 = this.skin.getMenuPosition();
               if (var9 != null && (var9.getX() <= 0.0 || var9.getY() <= 0.0)) {
                  this.skin.positionCaret(this.skin.getIndex(var1.getX(), var1.getY()), false);
                  var9 = this.skin.getMenuPosition();
               }
            }

            if (var9 != null) {
               Point2D var10 = ((TextField)this.getControl()).localToScene(var9);
               Scene var11 = ((TextField)this.getControl()).getScene();
               Window var12 = var11.getWindow();
               Point2D var13 = new Point2D(var12.getX() + var11.getX() + var10.getX(), var12.getY() + var11.getY() + var10.getY());
               var3 = var13.getX();
               var7 = var10.getX();
               var5 = var13.getY();
            }
         }

         this.skin.populateContextMenu(this.contextMenu);
         double var17 = this.contextMenu.prefWidth(-1.0);
         double var18 = var3 - (IS_TOUCH_SUPPORTED ? var17 / 2.0 : 0.0);
         Screen var19 = com.sun.javafx.util.Utils.getScreenForPoint(var3, 0.0);
         Rectangle2D var14 = var19.getBounds();
         if (var18 < var14.getMinX()) {
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", var3);
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", var7);
            this.contextMenu.show(this.getControl(), var14.getMinX(), var5);
         } else if (var3 + var17 > var14.getMaxX()) {
            double var15 = var17 - (var14.getMaxX() - var3);
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", var3);
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", var7);
            this.contextMenu.show(this.getControl(), var3 - var15, var5);
         } else {
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
            ((TextField)this.getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
            this.contextMenu.show(this.getControl(), var18, var5);
         }
      }

      var1.consume();
   }

   protected void mouseSingleClick(HitInfo var1) {
      this.skin.positionCaret(var1, false);
   }

   protected void mouseDoubleClick(HitInfo var1) {
      TextField var2 = (TextField)this.getControl();
      var2.previousWord();
      if (PlatformUtil.isWindows()) {
         var2.selectNextWord();
      } else {
         var2.selectEndOfNextWord();
      }

   }

   protected void mouseTripleClick(HitInfo var1) {
      ((TextField)this.getControl()).selectAll();
   }

   static enum TextInputTypes {
      TEXT_FIELD,
      PASSWORD_FIELD,
      EDITABLE_COMBO,
      TEXT_AREA;
   }
}
