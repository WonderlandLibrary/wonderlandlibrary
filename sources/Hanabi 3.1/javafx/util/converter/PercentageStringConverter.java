package javafx.util.converter;

import java.text.NumberFormat;
import java.util.Locale;

public class PercentageStringConverter extends NumberStringConverter {
   public PercentageStringConverter() {
      this(Locale.getDefault());
   }

   public PercentageStringConverter(Locale var1) {
      super(var1, (String)null, (NumberFormat)null);
   }

   public PercentageStringConverter(NumberFormat var1) {
      super((Locale)null, (String)null, var1);
   }

   public NumberFormat getNumberFormat() {
      Locale var1 = this.locale == null ? Locale.getDefault() : this.locale;
      return this.numberFormat != null ? this.numberFormat : NumberFormat.getPercentInstance(var1);
   }
}
