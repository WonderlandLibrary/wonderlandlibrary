package javafx.css;

import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ObservableValue;

public abstract class StyleableStringProperty extends StringPropertyBase implements StyleableProperty {
   private StyleOrigin origin = null;

   public StyleableStringProperty() {
   }

   public StyleableStringProperty(String var1) {
      super(var1);
   }

   public void applyStyle(StyleOrigin var1, String var2) {
      this.set(var2);
      this.origin = var1;
   }

   public void bind(ObservableValue var1) {
      super.bind(var1);
      this.origin = StyleOrigin.USER;
   }

   public void set(String var1) {
      super.set(var1);
      this.origin = StyleOrigin.USER;
   }

   public StyleOrigin getStyleOrigin() {
      return this.origin;
   }
}
