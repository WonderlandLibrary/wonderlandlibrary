package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public final class StopConverter extends StyleConverterImpl {
   public static StopConverter getInstance() {
      return StopConverter.Holder.INSTANCE;
   }

   private StopConverter() {
   }

   public Stop convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Double var4 = ((Size)var3[0].convert(var2)).pixels(var2);
      Color var5 = (Color)var3[1].convert(var2);
      return new Stop(var4, var5);
   }

   public String toString() {
      return "StopConverter";
   }

   // $FF: synthetic method
   StopConverter(Object var1) {
      this();
   }

   private static class Holder {
      static final StopConverter INSTANCE = new StopConverter();
   }
}
