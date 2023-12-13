package javafx.css;

import javafx.beans.property.FloatPropertyBase;
import javafx.beans.value.ObservableValue;

public abstract class StyleableFloatProperty extends FloatPropertyBase implements StyleableProperty {
   private StyleOrigin origin = null;

   public StyleableFloatProperty() {
   }

   public StyleableFloatProperty(float var1) {
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

   public void set(float var1) {
      super.set(var1);
      this.origin = StyleOrigin.USER;
   }

   public StyleOrigin getStyleOrigin() {
      return this.origin;
   }
}
