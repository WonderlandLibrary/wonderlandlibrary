package joptsimple.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class DateConverter implements ValueConverter {
   private final DateFormat formatter;

   public DateConverter(DateFormat var1) {
      if (var1 == null) {
         throw new NullPointerException("illegal null formatter");
      } else {
         this.formatter = var1;
      }
   }

   public static DateConverter datePattern(String var0) {
      SimpleDateFormat var1 = new SimpleDateFormat(var0);
      var1.setLenient(false);
      return new DateConverter(var1);
   }

   public Date convert(String var1) {
      ParsePosition var2 = new ParsePosition(0);
      Date var3 = this.formatter.parse(var1, var2);
      if (var2.getIndex() != var1.length()) {
         throw new ValueConversionException(this.message(var1));
      } else {
         return var3;
      }
   }

   public Class valueType() {
      return Date.class;
   }

   public String valuePattern() {
      return this.formatter instanceof SimpleDateFormat ? ((SimpleDateFormat)this.formatter).toPattern() : "";
   }

   private String message(String var1) {
      String var2 = "Value [" + var1 + "] does not match date/time pattern";
      if (this.formatter instanceof SimpleDateFormat) {
         var2 = var2 + " [" + ((SimpleDateFormat)this.formatter).toPattern() + ']';
      }

      return var2;
   }

   public Object convert(String var1) {
      return this.convert(var1);
   }
}
