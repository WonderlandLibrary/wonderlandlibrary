package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

public final class DeriveSizeConverter extends StyleConverterImpl {
   public static DeriveSizeConverter getInstance() {
      return DeriveSizeConverter.Holder.INSTANCE;
   }

   private DeriveSizeConverter() {
   }

   public Size convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      double var4 = ((Size)var3[0].convert(var2)).pixels(var2);
      double var6 = ((Size)var3[1].convert(var2)).pixels(var2);
      return new Size(var4 + var6, SizeUnits.PX);
   }

   public String toString() {
      return "DeriveSizeConverter";
   }

   // $FF: synthetic method
   DeriveSizeConverter(Object var1) {
      this();
   }

   private static class Holder {
      static final DeriveSizeConverter INSTANCE = new DeriveSizeConverter();
   }
}
