package com.sun.javafx.css.parser;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public final class LadderConverter extends StyleConverterImpl {
   public static LadderConverter getInstance() {
      return LadderConverter.Holder.INSTANCE;
   }

   private LadderConverter() {
   }

   public Color convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Color var4 = (Color)var3[0].convert(var2);
      Stop[] var5 = new Stop[var3.length - 1];

      for(int var6 = 1; var6 < var3.length; ++var6) {
         var5[var6 - 1] = (Stop)var3[var6].convert(var2);
      }

      return Utils.ladder(var4, var5);
   }

   public String toString() {
      return "LadderConverter";
   }

   // $FF: synthetic method
   LadderConverter(Object var1) {
      this();
   }

   private static class Holder {
      static final LadderConverter INSTANCE = new LadderConverter();
   }
}
