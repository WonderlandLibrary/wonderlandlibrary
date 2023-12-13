package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.paint.Color;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ColorPickerBuilder extends ComboBoxBaseBuilder implements Builder {
   private boolean __set;
   private Collection customColors;

   protected ColorPickerBuilder() {
   }

   public static ColorPickerBuilder create() {
      return new ColorPickerBuilder();
   }

   public void applyTo(ColorPicker var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getCustomColors().addAll(this.customColors);
      }

   }

   public ColorPickerBuilder customColors(Collection var1) {
      this.customColors = var1;
      this.__set = true;
      return this;
   }

   public ColorPickerBuilder customColors(Color... var1) {
      return this.customColors((Collection)Arrays.asList(var1));
   }

   public ColorPicker build() {
      ColorPicker var1 = new ColorPicker();
      this.applyTo(var1);
      return var1;
   }
}
