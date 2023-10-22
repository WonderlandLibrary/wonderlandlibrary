package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class DeriveColorConverter extends StyleConverterImpl {
   public static DeriveColorConverter getInstance() {
      return DeriveColorConverter.Holder.INSTANCE;
   }

   private DeriveColorConverter() {
   }

   public Color convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      Color var4 = (Color)var3[0].convert(var2);
      Size var5 = (Size)var3[1].convert(var2);
      return Utils.deriveColor(var4, var5.pixels(var2));
   }

   public String toString() {
      return "DeriveColorConverter";
   }

   // $FF: synthetic method
   DeriveColorConverter(Object var1) {
      this();
   }

   private static class Holder {
      static final DeriveColorConverter INSTANCE = new DeriveColorConverter();
   }
}
