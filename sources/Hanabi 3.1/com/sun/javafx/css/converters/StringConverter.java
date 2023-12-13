package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

public final class StringConverter extends StyleConverterImpl {
   public static StyleConverter getInstance() {
      return StringConverter.Holder.INSTANCE;
   }

   private StringConverter() {
   }

   public String convert(ParsedValue var1, Font var2) {
      String var3 = (String)var1.getValue();
      return var3 == null ? null : Utils.convertUnicode(var3);
   }

   public String toString() {
      return "StringConverter";
   }

   // $FF: synthetic method
   StringConverter(Object var1) {
      this();
   }

   public static final class SequenceConverter extends StyleConverterImpl {
      public static SequenceConverter getInstance() {
         return StringConverter.Holder.SEQUENCE_INSTANCE;
      }

      private SequenceConverter() {
      }

      public String[] convert(ParsedValue var1, Font var2) {
         ParsedValue[] var3 = (ParsedValue[])var1.getValue();
         String[] var4 = new String[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4[var5] = (String)StringConverter.getInstance().convert(var3[var5], var2);
         }

         return var4;
      }

      public String toString() {
         return "String.SequenceConverter";
      }

      // $FF: synthetic method
      SequenceConverter(Object var1) {
         this();
      }
   }

   private static class Holder {
      static final StringConverter INSTANCE = new StringConverter();
      static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
   }
}
