package javafx.css;

import javafx.scene.text.Font;

public class ParsedValue {
   protected final Object value;
   protected final StyleConverter converter;

   public final Object getValue() {
      return this.value;
   }

   public final StyleConverter getConverter() {
      return this.converter;
   }

   public Object convert(Font var1) {
      return this.converter != null ? this.converter.convert(this, var1) : this.value;
   }

   protected ParsedValue(Object var1, StyleConverter var2) {
      this.value = var1;
      this.converter = var2;
   }
}
