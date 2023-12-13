package javafx.css;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ObservableValue;

public abstract class StyleableObjectProperty extends ObjectPropertyBase implements StyleableProperty {
   private StyleOrigin origin = null;

   public StyleableObjectProperty() {
   }

   public StyleableObjectProperty(Object var1) {
      super(var1);
   }

   public void applyStyle(StyleOrigin var1, Object var2) {
      this.set(var2);
      this.origin = var1;
   }

   public void bind(ObservableValue var1) {
      super.bind(var1);
      this.origin = StyleOrigin.USER;
   }

   public void set(Object var1) {
      super.set(var1);
      this.origin = StyleOrigin.USER;
   }

   public StyleOrigin getStyleOrigin() {
      return this.origin;
   }
}
