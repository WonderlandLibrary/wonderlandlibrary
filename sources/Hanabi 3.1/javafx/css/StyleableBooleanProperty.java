package javafx.css;

import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ObservableValue;

public abstract class StyleableBooleanProperty extends BooleanPropertyBase implements StyleableProperty {
   private StyleOrigin origin = null;

   public StyleableBooleanProperty() {
   }

   public StyleableBooleanProperty(boolean var1) {
      super(var1);
   }

   public void applyStyle(StyleOrigin var1, Boolean var2) {
      this.set(var2);
      this.origin = var1;
   }

   public void bind(ObservableValue var1) {
      super.bind(var1);
      this.origin = StyleOrigin.USER;
   }

   public void set(boolean var1) {
      super.set(var1);
      this.origin = StyleOrigin.USER;
   }

   public StyleOrigin getStyleOrigin() {
      return this.origin;
   }
}
