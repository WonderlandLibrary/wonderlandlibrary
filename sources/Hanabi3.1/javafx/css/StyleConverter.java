package javafx.css;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import javafx.scene.text.Font;

public class StyleConverter {
   public Object convert(ParsedValue var1, Font var2) {
      return var1.getValue();
   }

   public static StyleConverter getBooleanConverter() {
      return BooleanConverter.getInstance();
   }

   public static StyleConverter getDurationConverter() {
      return DurationConverter.getInstance();
   }

   public static StyleConverter getColorConverter() {
      return ColorConverter.getInstance();
   }

   public static StyleConverter getEffectConverter() {
      return EffectConverter.getInstance();
   }

   public static StyleConverter getEnumConverter(Class var0) {
      EnumConverter var1 = new EnumConverter(var0);
      return var1;
   }

   public static StyleConverter getFontConverter() {
      return FontConverter.getInstance();
   }

   public static StyleConverter getInsetsConverter() {
      return InsetsConverter.getInstance();
   }

   public static StyleConverter getPaintConverter() {
      return PaintConverter.getInstance();
   }

   public static StyleConverter getSizeConverter() {
      return SizeConverter.getInstance();
   }

   public static StyleConverter getStringConverter() {
      return StringConverter.getInstance();
   }

   public static StyleConverter getUrlConverter() {
      return URLConverter.getInstance();
   }
}
