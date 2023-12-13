package javafx.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeStringConverter extends DateTimeStringConverter {
   public TimeStringConverter() {
      this((Locale)null, (String)null, (DateFormat)null, 2);
   }

   public TimeStringConverter(int var1) {
      this((Locale)null, (String)null, (DateFormat)null, var1);
   }

   public TimeStringConverter(Locale var1) {
      this(var1, (String)null, (DateFormat)null, 2);
   }

   public TimeStringConverter(Locale var1, int var2) {
      this(var1, (String)null, (DateFormat)null, var2);
   }

   public TimeStringConverter(String var1) {
      this((Locale)null, var1, (DateFormat)null, 2);
   }

   public TimeStringConverter(Locale var1, String var2) {
      this(var1, var2, (DateFormat)null, 2);
   }

   public TimeStringConverter(DateFormat var1) {
      this((Locale)null, (String)null, var1, 2);
   }

   private TimeStringConverter(Locale var1, String var2, DateFormat var3, int var4) {
      super(var1, var2, var3, 2, var4);
   }

   protected DateFormat getDateFormat() {
      Object var1 = null;
      if (this.dateFormat != null) {
         return this.dateFormat;
      } else {
         if (this.pattern != null) {
            var1 = new SimpleDateFormat(this.pattern, this.locale);
         } else {
            var1 = DateFormat.getTimeInstance(this.timeStyle, this.locale);
         }

         ((DateFormat)var1).setLenient(false);
         return (DateFormat)var1;
      }
   }
}
