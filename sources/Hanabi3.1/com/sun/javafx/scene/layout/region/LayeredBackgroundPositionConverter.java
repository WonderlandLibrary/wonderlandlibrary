package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.text.Font;

public final class LayeredBackgroundPositionConverter extends StyleConverterImpl {
   private static final LayeredBackgroundPositionConverter LAYERED_BACKGROUND_POSITION_CONVERTER = new LayeredBackgroundPositionConverter();

   public static LayeredBackgroundPositionConverter getInstance() {
      return LAYERED_BACKGROUND_POSITION_CONVERTER;
   }

   private LayeredBackgroundPositionConverter() {
   }

   public BackgroundPosition[] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      BackgroundPosition[] var4 = new BackgroundPosition[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = (BackgroundPosition)var3[var5].convert(var2);
      }

      return var4;
   }

   public String toString() {
      return "LayeredBackgroundPositionConverter";
   }
}
