package javafx.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.util.StringConverter;

public class NumberStringConverter extends StringConverter {
   final Locale locale;
   final String pattern;
   final NumberFormat numberFormat;

   public NumberStringConverter() {
      this(Locale.getDefault());
   }

   public NumberStringConverter(Locale var1) {
      this(var1, (String)null);
   }

   public NumberStringConverter(String var1) {
      this(Locale.getDefault(), var1);
   }

   public NumberStringConverter(Locale var1, String var2) {
      this(var1, var2, (NumberFormat)null);
   }

   public NumberStringConverter(NumberFormat var1) {
      this((Locale)null, (String)null, var1);
   }

   NumberStringConverter(Locale var1, String var2, NumberFormat var3) {
      this.locale = var1;
      this.pattern = var2;
      this.numberFormat = var3;
   }

   public Number fromString(String var1) {
      try {
         if (var1 == null) {
            return null;
         } else {
            var1 = var1.trim();
            if (var1.length() < 1) {
               return null;
            } else {
               NumberFormat var2 = this.getNumberFormat();
               return var2.parse(var1);
            }
         }
      } catch (ParseException var3) {
         throw new RuntimeException(var3);
      }
   }

   public String toString(Number var1) {
      if (var1 == null) {
         return "";
      } else {
         NumberFormat var2 = this.getNumberFormat();
         return var2.format(var1);
      }
   }

   protected NumberFormat getNumberFormat() {
      Locale var1 = this.locale == null ? Locale.getDefault() : this.locale;
      if (this.numberFormat != null) {
         return this.numberFormat;
      } else if (this.pattern != null) {
         DecimalFormatSymbols var2 = new DecimalFormatSymbols(var1);
         return new DecimalFormat(this.pattern, var2);
      } else {
         return NumberFormat.getNumberInstance(var1);
      }
   }
}
