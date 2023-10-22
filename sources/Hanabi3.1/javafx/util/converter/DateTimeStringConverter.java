package javafx.util.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Locale.Category;
import javafx.util.StringConverter;

public class DateTimeStringConverter extends StringConverter {
   protected final Locale locale;
   protected final String pattern;
   protected final DateFormat dateFormat;
   protected final int dateStyle;
   protected final int timeStyle;

   public DateTimeStringConverter() {
      this((Locale)null, (String)null, (DateFormat)null, 2, 2);
   }

   public DateTimeStringConverter(int var1, int var2) {
      this((Locale)null, (String)null, (DateFormat)null, var1, var2);
   }

   public DateTimeStringConverter(Locale var1) {
      this(var1, (String)null, (DateFormat)null, 2, 2);
   }

   public DateTimeStringConverter(Locale var1, int var2, int var3) {
      this(var1, (String)null, (DateFormat)null, var2, var3);
   }

   public DateTimeStringConverter(String var1) {
      this((Locale)null, var1, (DateFormat)null, 2, 2);
   }

   public DateTimeStringConverter(Locale var1, String var2) {
      this(var1, var2, (DateFormat)null, 2, 2);
   }

   public DateTimeStringConverter(DateFormat var1) {
      this((Locale)null, (String)null, var1, 2, 2);
   }

   DateTimeStringConverter(Locale var1, String var2, DateFormat var3, int var4, int var5) {
      this.locale = var1 != null ? var1 : Locale.getDefault(Category.FORMAT);
      this.pattern = var2;
      this.dateFormat = var3;
      this.dateStyle = var4;
      this.timeStyle = var5;
   }

   public Date fromString(String var1) {
      try {
         if (var1 == null) {
            return null;
         } else {
            var1 = var1.trim();
            if (var1.length() < 1) {
               return null;
            } else {
               DateFormat var2 = this.getDateFormat();
               return var2.parse(var1);
            }
         }
      } catch (ParseException var3) {
         throw new RuntimeException(var3);
      }
   }

   public String toString(Date var1) {
      if (var1 == null) {
         return "";
      } else {
         DateFormat var2 = this.getDateFormat();
         return var2.format(var1);
      }
   }

   protected DateFormat getDateFormat() {
      Object var1 = null;
      if (this.dateFormat != null) {
         return this.dateFormat;
      } else {
         if (this.pattern != null) {
            var1 = new SimpleDateFormat(this.pattern, this.locale);
         } else {
            var1 = DateFormat.getDateTimeInstance(this.dateStyle, this.timeStyle, this.locale);
         }

         ((DateFormat)var1).setLenient(false);
         return (DateFormat)var1;
      }
   }
}
