package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

public class Button extends ButtonBase {
   private BooleanProperty defaultButton;
   private BooleanProperty cancelButton;
   private static final String DEFAULT_STYLE_CLASS = "button";
   private static final PseudoClass PSEUDO_CLASS_DEFAULT = PseudoClass.getPseudoClass("default");
   private static final PseudoClass PSEUDO_CLASS_CANCEL = PseudoClass.getPseudoClass("cancel");

   public Button() {
      this.initialize();
   }

   public Button(String var1) {
      super(var1);
      this.initialize();
   }

   public Button(String var1, Node var2) {
      super(var1, var2);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("button"));
      this.setAccessibleRole(AccessibleRole.BUTTON);
      this.setMnemonicParsing(true);
   }

   public final void setDefaultButton(boolean var1) {
      this.defaultButtonProperty().set(var1);
   }

   public final boolean isDefaultButton() {
      return this.defaultButton == null ? false : this.defaultButton.get();
   }

   public final BooleanProperty defaultButtonProperty() {
      if (this.defaultButton == null) {
         this.defaultButton = new BooleanPropertyBase(false) {
            protected void invalidated() {
               Button.this.pseudoClassStateChanged(Button.PSEUDO_CLASS_DEFAULT, this.get());
            }

            public Object getBean() {
               return Button.this;
            }

            public String getName() {
               return "defaultButton";
            }
         };
      }

      return this.defaultButton;
   }

   public final void setCancelButton(boolean var1) {
      this.cancelButtonProperty().set(var1);
   }

   public final boolean isCancelButton() {
      return this.cancelButton == null ? false : this.cancelButton.get();
   }

   public final BooleanProperty cancelButtonProperty() {
      if (this.cancelButton == null) {
         this.cancelButton = new BooleanPropertyBase(false) {
            protected void invalidated() {
               Button.this.pseudoClassStateChanged(Button.PSEUDO_CLASS_CANCEL, this.get());
            }

            public Object getBean() {
               return Button.this;
            }

            public String getName() {
               return "cancelButton";
            }
         };
      }

      return this.cancelButton;
   }

   public void fire() {
      if (!this.isDisabled()) {
         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new ButtonSkin(this);
   }
}
