package javafx.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyStringConverter extends NumberStringConverter {
   public CurrencyStringConverter() {
      this(Locale.getDefault());
   }

   public CurrencyStringConverter(Locale var1) {
      this(var1, (String)null);
   }

   public CurrencyStringConverter(String var1) {
      this(Locale.getDefault(), var1);
   }

   public CurrencyStringConverter(Locale var1, String var2) {
      super(var1, var2, (NumberFormat)null);
   }

   public CurrencyStringConverter(NumberFormat var1) {
      super((Locale)null, (String)null, var1);
   }

   protected NumberFormat getNumberFormat() {
      Locale var1 = this.locale == null ? Locale.getDefault() : this.locale;
      if (this.numberFormat != null) {
         return this.numberFormat;
      } else if (this.pattern != null) {
         DecimalFormatSymbols var2 = new DecimalFormatSymbols(var1);
         return new DecimalFormat(this.pattern, var2);
      } else {
         return NumberFormat.getCurrencyInstance(var1);
      }
   }
}
