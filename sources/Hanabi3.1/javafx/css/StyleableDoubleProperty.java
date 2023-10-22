package javafx.css;

import javafx.beans.property.DoublePropertyBase;
import javafx.beans.value.ObservableValue;

public abstract class StyleableDoubleProperty extends DoublePropertyBase implements StyleableProperty {
   private StyleOrigin origin = null;

   public StyleableDoubleProperty() {
   }

   public StyleableDoubleProperty(double var1) {
      super(var1);
   }

   public void applyStyle(StyleOrigin var1, Number var2) {
      this.setValue(var2);
      this.origin = var1;
   }

   public void bind(ObservableValue var1) {
      super.bind(var1);
      this.origin = StyleOrigin.USER;
   }

   public void set(double var1) {
      super.set(var1);
      this.origin = StyleOrigin.USER;
   }

   public StyleOrigin getStyleOrigin() {
      return this.origin;
   }
}
