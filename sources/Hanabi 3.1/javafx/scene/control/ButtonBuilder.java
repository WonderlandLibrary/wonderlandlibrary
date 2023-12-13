package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ButtonBuilder extends ButtonBaseBuilder implements Builder {
   private int __set;
   private boolean cancelButton;
   private boolean defaultButton;

   protected ButtonBuilder() {
   }

   public static ButtonBuilder create() {
      return new ButtonBuilder();
   }

   public void applyTo(Button var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCancelButton(this.cancelButton);
      }

      if ((var2 & 2) != 0) {
         var1.setDefaultButton(this.defaultButton);
      }

   }

   public ButtonBuilder cancelButton(boolean var1) {
      this.cancelButton = var1;
      this.__set |= 1;
      return this;
   }

   public ButtonBuilder defaultButton(boolean var1) {
      this.defaultButton = var1;
      this.__set |= 2;
      return this;
   }

   public Button build() {
      Button var1 = new Button();
      this.applyTo(var1);
      return var1;
   }
}
