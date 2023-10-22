package javafx.scene.control;

import com.sun.javafx.scene.control.skin.RadioButtonSkin;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class RadioButton extends ToggleButton {
   private static final String DEFAULT_STYLE_CLASS = "radio-button";

   public RadioButton() {
      this.initialize();
   }

   public RadioButton(String var1) {
      this.setText(var1);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("radio-button"));
      this.setAccessibleRole(AccessibleRole.RADIO_BUTTON);
      ((StyleableProperty)this.alignmentProperty()).applyStyle((StyleOrigin)null, Pos.CENTER_LEFT);
   }

   public void fire() {
      if (this.getToggleGroup() == null || !this.isSelected()) {
         super.fire();
      }

   }

   protected Skin createDefaultSkin() {
      return new RadioButtonSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Pos impl_cssGetAlignmentInitialValue() {
      return Pos.CENTER_LEFT;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case SELECTED:
            return this.isSelected();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
