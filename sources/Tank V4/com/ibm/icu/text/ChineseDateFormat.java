package com.ibm.icu.text;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.InvalidObjectException;
import java.text.FieldPosition;
import java.util.Locale;

/** @deprecated */
public class ChineseDateFormat extends SimpleDateFormat {
   static final long serialVersionUID = -4610300753104099899L;

   /** @deprecated */
   public ChineseDateFormat(String var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   /** @deprecated */
   public ChineseDateFormat(String var1, ULocale var2) {
      this(var1, (String)null, var2);
   }

   /** @deprecated */
   public ChineseDateFormat(String var1, String var2, ULocale var3) {
      super(var1, new ChineseDateFormatSymbols(var3), new ChineseCalendar(TimeZone.getDefault(), var3), var3, true, var2);
   }

   /** @deprecated */
   protected void subFormat(StringBuffer var1, char var2, int var3, int var4, int var5, DisplayContext var6, FieldPosition var7, Calendar var8) {
      super.subFormat(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   /** @deprecated */
   protected int subParse(String var1, int var2, char var3, int var4, boolean var5, boolean var6, boolean[] var7, Calendar var8) {
      return super.subParse(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   /** @deprecated */
   protected DateFormat.Field patternCharToDateFormatField(char var1) {
      return super.patternCharToDateFormatField(var1);
   }

   /** @deprecated */
   public static class Field extends DateFormat.Field {
      private static final long serialVersionUID = -5102130532751400330L;
      /** @deprecated */
      public static final ChineseDateFormat.Field IS_LEAP_MONTH = new ChineseDateFormat.Field("is leap month", 22);

      /** @deprecated */
      protected Field(String var1, int var2) {
         super(var1, var2);
      }

      /** @deprecated */
      public static DateFormat.Field ofCalendarField(int var0) {
         return (DateFormat.Field)(var0 == 22 ? IS_LEAP_MONTH : DateFormat.Field.ofCalendarField(var0));
      }

      /** @deprecated */
      protected Object readResolve() throws InvalidObjectException {
         if (this.getClass() != ChineseDateFormat.Field.class) {
            throw new InvalidObjectException("A subclass of ChineseDateFormat.Field must implement readResolve.");
         } else if (this.getName().equals(IS_LEAP_MONTH.getName())) {
            return IS_LEAP_MONTH;
         } else {
            throw new InvalidObjectException("Unknown attribute name.");
         }
      }
   }
}
