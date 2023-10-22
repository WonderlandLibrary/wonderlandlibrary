package javafx.scene.control;

/** @deprecated */
@Deprecated
public class RadioButtonBuilder extends ToggleButtonBuilder {
   protected RadioButtonBuilder() {
   }

   public static RadioButtonBuilder create() {
      return new RadioButtonBuilder();
   }

   public RadioButton build() {
      RadioButton var1 = new RadioButton();
      this.applyTo(var1);
      return var1;
   }
}
