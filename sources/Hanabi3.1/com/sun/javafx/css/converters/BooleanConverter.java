package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class BooleanConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return BooleanConverter.Holder.INSTANCE;
   }

   private BooleanConverter() {
   }

   public Boolean convert(ParsedValue var1, Font var2) {
      String var3 = (String)var1.getValue();
      return Boolean.valueOf(var3);
   }

   public String toString() {
      return "BooleanConverter";
   }

   // $FF: synthetic method
   BooleanConverter(Object var1) {
      this();
   }

   private static class Holder {
      static final BooleanConverter INSTANCE = new BooleanConverter();
   }
}
