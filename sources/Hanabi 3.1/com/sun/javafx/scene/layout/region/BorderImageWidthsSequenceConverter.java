package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;

public class BorderImageWidthsSequenceConverter extends StyleConverterImpl {
   private static final BorderImageWidthsSequenceConverter CONVERTER = new BorderImageWidthsSequenceConverter();

   public static BorderImageWidthsSequenceConverter getInstance() {
      return CONVERTER;
   }

   public BorderWidths[] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      BorderWidths[] var4 = new BorderWidths[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = BorderImageWidthConverter.getInstance().convert(var3[var5], var2);
      }

      return var4;
   }

   public String toString() {
      return "BorderImageWidthsSequenceConverter";
   }
}
