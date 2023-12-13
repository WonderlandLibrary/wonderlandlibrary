package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class ButtonSkin extends LabeledSkinBase {
   Runnable defaultButtonRunnable = () -> {
      if (((Button)this.getSkinnable()).getScene() != null && ((Button)this.getSkinnable()).impl_isTreeVisible() && !((Button)this.getSkinnable()).isDisabled()) {
         ((Button)this.getSkinnable()).fire();
      }

   };
   Runnable cancelButtonRunnable = () -> {
      if (((Button)this.getSkinnable()).getScene() != null && ((Button)this.getSkinnable()).impl_isTreeVisible() && !((Button)this.getSkinnable()).isDisabled()) {
         ((Button)this.getSkinnable()).fire();
      }

   };
   private KeyCodeCombination defaultAcceleratorKeyCodeCombination;
   private KeyCodeCombination cancelAcceleratorKeyCodeCombination;

   public ButtonSkin(Button var1) {
      super(var1, new ButtonBehavior(var1));
      this.registerChangeListener(var1.defaultButtonProperty(), "DEFAULT_BUTTON");
      this.registerChangeListener(var1.cancelButtonProperty(), "CANCEL_BUTTON");
      this.registerChangeListener(var1.focusedProperty(), "FOCUSED");
      if (((Button)this.getSkinnable()).isDefaultButton()) {
         this.setDefaultButton(true);
      }

      if (((Button)this.getSkinnable()).isCancelButton()) {
         this.setCancelButton(true);
      }

   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("DEFAULT_BUTTON".equals(var1)) {
         this.setDefaultButton(((Button)this.getSkinnable()).isDefaultButton());
      } else if ("CANCEL_BUTTON".equals(var1)) {
         this.setCancelButton(((Button)this.getSkinnable()).isCancelButton());
      } else if ("FOCUSED".equals(var1)) {
         if (!((Button)this.getSkinnable()).isFocused()) {
            ContextMenu var2 = ((Button)this.getSkinnable()).getContextMenu();
            if (var2 != null && var2.isShowing()) {
               var2.hide();
               Utils.removeMnemonics(var2, ((Button)this.getSkinnable()).getScene());
            }
         }
      } else if ("PARENT".equals(var1) && ((Button)this.getSkinnable()).getParent() == null && ((Button)this.getSkinnable()).getScene() != null) {
         if (((Button)this.getSkinnable()).isDefaultButton()) {
            ((Button)this.getSkinnable()).getScene().getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
         }

         if (((Button)this.getSkinnable()).isCancelButton()) {
            ((Button)this.getSkinnable()).getScene().getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
         }
      }

   }

   private void setDefaultButton(boolean var1) {
      Scene var2 = ((Button)this.getSkinnable()).getScene();
      if (var2 != null) {
         KeyCode var3 = KeyCode.ENTER;
         this.defaultAcceleratorKeyCodeCombination = new KeyCodeCombination(var3, new KeyCombination.Modifier[0]);
         Runnable var4 = (Runnable)var2.getAccelerators().get(this.defaultAcceleratorKeyCodeCombination);
         if (!var1) {
            if (this.defaultButtonRunnable.equals(var4)) {
               var2.getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
            }
         } else if (!this.defaultButtonRunnable.equals(var4)) {
            var2.getAccelerators().remove(this.defaultAcceleratorKeyCodeCombination);
            var2.getAccelerators().put(this.defaultAcceleratorKeyCodeCombination, this.defaultButtonRunnable);
         }
      }

   }

   private void setCancelButton(boolean var1) {
      Scene var2 = ((Button)this.getSkinnable()).getScene();
      if (var2 != null) {
         KeyCode var3 = KeyCode.ESCAPE;
         this.cancelAcceleratorKeyCodeCombination = new KeyCodeCombination(var3, new KeyCombination.Modifier[0]);
         Runnable var4 = (Runnable)var2.getAccelerators().get(this.cancelAcceleratorKeyCodeCombination);
         if (!var1) {
            if (this.cancelButtonRunnable.equals(var4)) {
               var2.getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
            }
         } else if (!this.cancelButtonRunnable.equals(var4)) {
            var2.getAccelerators().remove(this.cancelAcceleratorKeyCodeCombination);
            var2.getAccelerators().put(this.cancelAcceleratorKeyCodeCombination, this.cancelButtonRunnable);
         }
      }

   }
}
