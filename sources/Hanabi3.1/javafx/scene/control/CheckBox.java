package javafx.scene.control;

import com.sun.javafx.scene.control.skin.CheckBoxSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class CheckBox extends ButtonBase {
   private BooleanProperty indeterminate;
   private BooleanProperty selected;
   private BooleanProperty allowIndeterminate;
   private static final String DEFAULT_STYLE_CLASS = "check-box";
   private static final PseudoClass PSEUDO_CLASS_DETERMINATE = PseudoClass.getPseudoClass("determinate");
   private static final PseudoClass PSEUDO_CLASS_INDETERMINATE = PseudoClass.getPseudoClass("indeterminate");
   private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

   public CheckBox() {
      this.initialize();
   }

   public CheckBox(String var1) {
      this.setText(var1);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("check-box"));
      this.setAccessibleRole(AccessibleRole.CHECK_BOX);
      this.setAlignment(Pos.CENTER_LEFT);
      this.setMnemonicParsing(true);
      this.pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, true);
   }

   public final void setIndeterminate(boolean var1) {
      this.indeterminateProperty().set(var1);
   }

   public final boolean isIndeterminate() {
      return this.indeterminate == null ? false : this.indeterminate.get();
   }

   public final BooleanProperty indeterminateProperty() {
      if (this.indeterminate == null) {
         this.indeterminate = new BooleanPropertyBase(false) {
            protected void invalidated() {
               boolean var1 = this.get();
               CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_DETERMINATE, !var1);
               CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_INDETERMINATE, var1);
               CheckBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.INDETERMINATE);
            }

            public Object getBean() {
               return CheckBox.this;
            }

            public String getName() {
               return "indeterminate";
            }
         };
      }

      return this.indeterminate;
   }

   public final void setSelected(boolean var1) {
      this.selectedProperty().set(var1);
   }

   public final boolean isSelected() {
      return this.selected == null ? false : this.selected.get();
   }

   public final BooleanProperty selectedProperty() {
      if (this.selected == null) {
         this.selected = new BooleanPropertyBase() {
            protected void invalidated() {
               Boolean var1 = this.get();
               CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_SELECTED, var1);
               CheckBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
            }

            public Object getBean() {
               return CheckBox.this;
            }

            public String getName() {
               return "selected";
            }
         };
      }

      return this.selected;
   }

   public final void setAllowIndeterminate(boolean var1) {
      this.allowIndeterminateProperty().set(var1);
   }

   public final boolean isAllowIndeterminate() {
      return this.allowIndeterminate == null ? false : this.allowIndeterminate.get();
   }

   public final BooleanProperty allowIndeterminateProperty() {
      if (this.allowIndeterminate == null) {
         this.allowIndeterminate = new SimpleBooleanProperty(this, "allowIndeterminate");
      }

      return this.allowIndeterminate;
   }

   public void fire() {
      if (!this.isDisabled()) {
         if (this.isAllowIndeterminate()) {
            if (!this.isSelected() && !this.isIndeterminate()) {
               this.setIndeterminate(true);
            } else if (this.isSelected() && !this.isIndeterminate()) {
               this.setSelected(false);
            } else if (this.isIndeterminate()) {
               this.setSelected(true);
               this.setIndeterminate(false);
            }
         } else {
            this.setSelected(!this.isSelected());
            this.setIndeterminate(false);
         }

         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new CheckBoxSkin(this);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case SELECTED:
            return this.isSelected();
         case INDETERMINATE:
            return this.isIndeterminate();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
