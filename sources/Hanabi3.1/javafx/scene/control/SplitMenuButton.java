package javafx.scene.control;

import com.sun.javafx.scene.control.skin.SplitMenuButtonSkin;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class SplitMenuButton extends MenuButton {
   private static final String DEFAULT_STYLE_CLASS = "split-menu-button";

   public SplitMenuButton() {
      this((MenuItem[])null);
   }

   public SplitMenuButton(MenuItem... var1) {
      if (var1 != null) {
         this.getItems().addAll(var1);
      }

      this.getStyleClass().setAll((Object[])("split-menu-button"));
      this.setAccessibleRole(AccessibleRole.SPLIT_MENU_BUTTON);
      this.setMnemonicParsing(true);
   }

   public void fire() {
      if (!this.isDisabled()) {
         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new SplitMenuButtonSkin(this);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case EXPANDED:
            return this.isShowing();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case FIRE:
            this.fire();
            break;
         case EXPAND:
            this.show();
            break;
         case COLLAPSE:
            this.hide();
            break;
         default:
            super.executeAccessibleAction(var1);
      }

   }
}
