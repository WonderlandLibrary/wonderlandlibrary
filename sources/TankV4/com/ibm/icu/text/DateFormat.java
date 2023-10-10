package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.RelativeDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.InvalidObjectException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public abstract class DateFormat extends UFormat {
   protected Calendar calendar;
   protected NumberFormat numberFormat;
   public static final int ERA_FIELD = 0;
   public static final int YEAR_FIELD = 1;
   public static final int MONTH_FIELD = 2;
   public static final int DATE_FIELD = 3;
   public static final int HOUR_OF_DAY1_FIELD = 4;
   public static final int HOUR_OF_DAY0_FIELD = 5;
   public static final int MINUTE_FIELD = 6;
   public static final int SECOND_FIELD = 7;
   public static final int FRACTIONAL_SECOND_FIELD = 8;
   public static final int MILLISECOND_FIELD = 8;
   public static final int DAY_OF_WEEK_FIELD = 9;
   public static final int DAY_OF_YEAR_FIELD = 10;
   public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
   public static final int WEEK_OF_YEAR_FIELD = 12;
   public static final int WEEK_OF_MONTH_FIELD = 13;
   public static final int AM_PM_FIELD = 14;
   public static final int HOUR1_FIELD = 15;
   public static final int HOUR0_FIELD = 16;
   public static final int TIMEZONE_FIELD = 17;
   public static final int YEAR_WOY_FIELD = 18;
   public static final int DOW_LOCAL_FIELD = 19;
   public static final int EXTENDED_YEAR_FIELD = 20;
   public static final int JULIAN_DAY_FIELD = 21;
   public static final int MILLISECONDS_IN_DAY_FIELD = 22;
   public static final int TIMEZONE_RFC_FIELD = 23;
   public static final int TIMEZONE_GENERIC_FIELD = 24;
   public static final int STANDALONE_DAY_FIELD = 25;
   public static final int STANDALONE_MONTH_FIELD = 26;
   public static final int QUARTER_FIELD = 27;
   public static final int STANDALONE_QUARTER_FIELD = 28;
   public static final int TIMEZONE_SPECIAL_FIELD = 29;
   public static final int YEAR_NAME_FIELD = 30;
   public static final int TIMEZONE_LOCALIZED_GMT_OFFSET_FIELD = 31;
   public static final int TIMEZONE_ISO_FIELD = 32;
   public static final int TIMEZONE_ISO_LOCAL_FIELD = 33;
   public static final int FIELD_COUNT = 34;
   private static final long serialVersionUID = 7218322306649953788L;
   public static final int NONE = -1;
   public static final int FULL = 0;
   public static final int LONG = 1;
   public static final int MEDIUM = 2;
   public static final int SHORT = 3;
   public static final int DEFAULT = 2;
   public static final int RELATIVE = 128;
   public static final int RELATIVE_FULL = 128;
   public static final int RELATIVE_LONG = 129;
   public static final int RELATIVE_MEDIUM = 130;
   public static final int RELATIVE_SHORT = 131;
   public static final int RELATIVE_DEFAULT = 130;
   public static final String YEAR = "y";
   public static final String QUARTER = "QQQQ";
   public static final String ABBR_QUARTER = "QQQ";
   public static final String YEAR_QUARTER = "yQQQQ";
   public static final String YEAR_ABBR_QUARTER = "yQQQ";
   public static final String MONTH = "MMMM";
   public static final String ABBR_MONTH = "MMM";
   public static final String NUM_MONTH = "M";
   public static final String YEAR_MONTH = "yMMMM";
   public static final String YEAR_ABBR_MONTH = "yMMM";
   public static final String YEAR_NUM_MONTH = "yM";
   public static final String DAY = "d";
   public static final String YEAR_MONTH_DAY = "yMMMMd";
   public static final String YEAR_ABBR_MONTH_DAY = "yMMMd";
   public static final String YEAR_NUM_MONTH_DAY = "yMd";
   public static final String WEEKDAY = "EEEE";
   public static final String ABBR_WEEKDAY = "E";
   public static final String YEAR_MONTH_WEEKDAY_DAY = "yMMMMEEEEd";
   public static final String YEAR_ABBR_MONTH_WEEKDAY_DAY = "yMMMEd";
   public static final String YEAR_NUM_MONTH_WEEKDAY_DAY = "yMEd";
   public static final String MONTH_DAY = "MMMMd";
   public static final String ABBR_MONTH_DAY = "MMMd";
   public static final String NUM_MONTH_DAY = "Md";
   public static final String MONTH_WEEKDAY_DAY = "MMMMEEEEd";
   public static final String ABBR_MONTH_WEEKDAY_DAY = "MMMEd";
   public static final String NUM_MONTH_WEEKDAY_DAY = "MEd";
   public static final String HOUR = "j";
   public static final String HOUR24 = "H";
   public static final String MINUTE = "m";
   public static final String HOUR_MINUTE = "jm";
   public static final String HOUR24_MINUTE = "Hm";
   public static final String SECOND = "s";
   public static final String HOUR_MINUTE_SECOND = "jms";
   public static final String HOUR24_MINUTE_SECOND = "Hms";
   public static final String MINUTE_SECOND = "ms";
   public static final String LOCATION_TZ = "VVVV";
   public static final String GENERIC_TZ = "vvvv";
   public static final String ABBR_GENERIC_TZ = "v";
   public static final String SPECIFIC_TZ = "zzzz";
   public static final String ABBR_SPECIFIC_TZ = "z";
   public static final String ABBR_UTC_TZ = "ZZZZ";
   /** @deprecated */
   public static final String STANDALONE_MONTH = "LLLL";
   /** @deprecated */
   public static final String ABBR_STANDALONE_MONTH = "LLL";
   /** @deprecated */
   public static final String HOUR_MINUTE_GENERIC_TZ = "jmv";
   /** @deprecated */
   public static final String HOUR_MINUTE_TZ = "jmz";
   /** @deprecated */
   public static final String HOUR_GENERIC_TZ = "jv";
   /** @deprecated */
   public static final String HOUR_TZ = "jz";

   public final StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof Calendar) {
         return this.format((Calendar)var1, var2, var3);
      } else if (var1 instanceof Date) {
         return this.format((Date)var1, var2, var3);
      } else if (var1 instanceof Number) {
         return this.format(new Date(((Number)var1).longValue()), var2, var3);
      } else {
         throw new IllegalArgumentException("Cannot format given Object (" + var1.getClass().getName() + ") as a Date");
      }
   }

   public abstract StringBuffer format(Calendar var1, StringBuffer var2, FieldPosition var3);

   public StringBuffer format(Date var1, StringBuffer var2, FieldPosition var3) {
      this.calendar.setTime(var1);
      return this.format(this.calendar, var2, var3);
   }

   public final String format(Date var1) {
      return this.format(var1, new StringBuffer(64), new FieldPosition(0)).toString();
   }

   public Date parse(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      Date var3 = this.parse(var1, var2);
      if (var2.getIndex() == 0) {
         throw new ParseException("Unparseable date: \"" + var1 + "\"", var2.getErrorIndex());
      } else {
         return var3;
      }
   }

   public abstract void parse(String var1, Calendar var2, ParsePosition var3);

   public Date parse(String var1, ParsePosition var2) {
      Date var3 = null;
      int var4 = var2.getIndex();
      TimeZone var5 = this.calendar.getTimeZone();
      this.calendar.clear();
      this.parse(var1, this.calendar, var2);
      if (var2.getIndex() != var4) {
         try {
            var3 = this.calendar.getTime();
         } catch (IllegalArgumentException var7) {
            var2.setIndex(var4);
            var2.setErrorIndex(var4);
         }
      }

      this.calendar.setTimeZone(var5);
      return var3;
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   public static final DateFormat getTimeInstance() {
      return get(-1, 2, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getTimeInstance(int var0) {
      return get(-1, var0, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getTimeInstance(int var0, Locale var1) {
      return get(-1, var0, ULocale.forLocale(var1));
   }

   public static final DateFormat getTimeInstance(int var0, ULocale var1) {
      return get(-1, var0, var1);
   }

   public static final DateFormat getDateInstance() {
      return get(2, -1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateInstance(int var0) {
      return get(var0, -1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateInstance(int var0, Locale var1) {
      return get(var0, -1, ULocale.forLocale(var1));
   }

   public static final DateFormat getDateInstance(int var0, ULocale var1) {
      return get(var0, -1, var1);
   }

   public static final DateFormat getDateTimeInstance() {
      return get(2, 2, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateTimeInstance(int var0, int var1) {
      return get(var0, var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateTimeInstance(int var0, int var1, Locale var2) {
      return get(var0, var1, ULocale.forLocale(var2));
   }

   public static final DateFormat getDateTimeInstance(int var0, int var1, ULocale var2) {
      return get(var0, var1, var2);
   }

   public static final DateFormat getInstance() {
      return getDateTimeInstance(3, 3);
   }

   public static Locale[] getAvailableLocales() {
      return ICUResourceBundle.getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return ICUResourceBundle.getAvailableULocales();
   }

   public void setCalendar(Calendar var1) {
      this.calendar = var1;
   }

   public Calendar getCalendar() {
      return this.calendar;
   }

   public void setNumberFormat(NumberFormat var1) {
      this.numberFormat = var1;
      this.numberFormat.setParseIntegerOnly(true);
   }

   public NumberFormat getNumberFormat() {
      return this.numberFormat;
   }

   public void setTimeZone(TimeZone var1) {
      this.calendar.setTimeZone(var1);
   }

   public TimeZone getTimeZone() {
      return this.calendar.getTimeZone();
   }

   public void setLenient(boolean var1) {
      this.calendar.setLenient(var1);
   }

   public boolean isLenient() {
      return this.calendar.isLenient();
   }

   public int hashCode() {
      return this.numberFormat.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         DateFormat var2 = (DateFormat)var1;
         return this.calendar.isEquivalentTo(var2.calendar) && this.numberFormat.equals(var2.numberFormat);
      } else {
         return false;
      }
   }

   public Object clone() {
      DateFormat var1 = (DateFormat)super.clone();
      var1.calendar = (Calendar)this.calendar.clone();
      var1.numberFormat = (NumberFormat)this.numberFormat.clone();
      return var1;
   }

   private static DateFormat get(int var0, int var1, ULocale var2) {
      if ((var1 == -1 || (var1 & 128) <= 0) && (var0 == -1 || (var0 & 128) <= 0)) {
         if (var1 >= -1 && var1 <= 3) {
            if (var0 >= -1 && var0 <= 3) {
               try {
                  Calendar var6 = Calendar.getInstance(var2);
                  DateFormat var4 = var6.getDateTimeFormat(var0, var1, var2);
                  var4.setLocale(var6.getLocale(ULocale.VALID_LOCALE), var6.getLocale(ULocale.ACTUAL_LOCALE));
                  return var4;
               } catch (MissingResourceException var5) {
                  return new SimpleDateFormat("M/d/yy h:mm a");
               }
            } else {
               throw new IllegalArgumentException("Illegal date style " + var0);
            }
         } else {
            throw new IllegalArgumentException("Illegal time style " + var1);
         }
      } else {
         RelativeDateFormat var3 = new RelativeDateFormat(var1, var0, var2);
         return var3;
      }
   }

   protected DateFormat() {
   }

   public static final DateFormat getDateInstance(Calendar var0, int var1, Locale var2) {
      return getDateTimeInstance(var0, var1, -1, (ULocale)ULocale.forLocale(var2));
   }

   public static final DateFormat getDateInstance(Calendar var0, int var1, ULocale var2) {
      return getDateTimeInstance(var0, var1, -1, (ULocale)var2);
   }

   public static final DateFormat getTimeInstance(Calendar var0, int var1, Locale var2) {
      return getDateTimeInstance(var0, -1, var1, (ULocale)ULocale.forLocale(var2));
   }

   public static final DateFormat getTimeInstance(Calendar var0, int var1, ULocale var2) {
      return getDateTimeInstance(var0, -1, var1, (ULocale)var2);
   }

   public static final DateFormat getDateTimeInstance(Calendar var0, int var1, int var2, Locale var3) {
      return var0.getDateTimeFormat(var1, var2, ULocale.forLocale(var3));
   }

   public static final DateFormat getDateTimeInstance(Calendar var0, int var1, int var2, ULocale var3) {
      return var0.getDateTimeFormat(var1, var2, var3);
   }

   public static final DateFormat getInstance(Calendar var0, Locale var1) {
      return getDateTimeInstance(var0, 3, 3, (ULocale)ULocale.forLocale(var1));
   }

   public static final DateFormat getInstance(Calendar var0, ULocale var1) {
      return getDateTimeInstance(var0, 3, 3, (ULocale)var1);
   }

   public static final DateFormat getInstance(Calendar var0) {
      return getInstance(var0, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateInstance(Calendar var0, int var1) {
      return getDateInstance(var0, var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getTimeInstance(Calendar var0, int var1) {
      return getTimeInstance(var0, var1, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getDateTimeInstance(Calendar var0, int var1, int var2) {
      return getDateTimeInstance(var0, var1, var2, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getPatternInstance(String var0) {
      return getPatternInstance(var0, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateFormat getPatternInstance(String var0, Locale var1) {
      return getPatternInstance(var0, ULocale.forLocale(var1));
   }

   public static final DateFormat getPatternInstance(String var0, ULocale var1) {
      DateTimePatternGenerator var2 = DateTimePatternGenerator.getInstance(var1);
      String var3 = var2.getBestPattern(var0);
      return new SimpleDateFormat(var3, var1);
   }

   public static final DateFormat getPatternInstance(Calendar var0, String var1, Locale var2) {
      return getPatternInstance(var0, var1, ULocale.forLocale(var2));
   }

   public static final DateFormat getPatternInstance(Calendar var0, String var1, ULocale var2) {
      DateTimePatternGenerator var3 = DateTimePatternGenerator.getInstance(var2);
      String var4 = var3.getBestPattern(var1);
      SimpleDateFormat var5 = new SimpleDateFormat(var4, var2);
      var5.setCalendar(var0);
      return var5;
   }

   public static class Field extends java.text.Format.Field {
      private static final long serialVersionUID = -3627456821000730829L;
      private static final int CAL_FIELD_COUNT;
      private static final DateFormat.Field[] CAL_FIELDS;
      private static final Map FIELD_NAME_MAP;
      public static final DateFormat.Field AM_PM;
      public static final DateFormat.Field DAY_OF_MONTH;
      public static final DateFormat.Field DAY_OF_WEEK;
      public static final DateFormat.Field DAY_OF_WEEK_IN_MONTH;
      public static final DateFormat.Field DAY_OF_YEAR;
      public static final DateFormat.Field ERA;
      public static final DateFormat.Field HOUR_OF_DAY0;
      public static final DateFormat.Field HOUR_OF_DAY1;
      public static final DateFormat.Field HOUR0;
      public static final DateFormat.Field HOUR1;
      public static final DateFormat.Field MILLISECOND;
      public static final DateFormat.Field MINUTE;
      public static final DateFormat.Field MONTH;
      public static final DateFormat.Field SECOND;
      public static final DateFormat.Field TIME_ZONE;
      public static final DateFormat.Field WEEK_OF_MONTH;
      public static final DateFormat.Field WEEK_OF_YEAR;
      public static final DateFormat.Field YEAR;
      public static final DateFormat.Field DOW_LOCAL;
      public static final DateFormat.Field EXTENDED_YEAR;
      public static final DateFormat.Field JULIAN_DAY;
      public static final DateFormat.Field MILLISECONDS_IN_DAY;
      public static final DateFormat.Field YEAR_WOY;
      public static final DateFormat.Field QUARTER;
      private final int calendarField;

      protected Field(String var1, int var2) {
         super(var1);
         this.calendarField = var2;
         if (this.getClass() == DateFormat.Field.class) {
            FIELD_NAME_MAP.put(var1, this);
            if (var2 >= 0 && var2 < CAL_FIELD_COUNT) {
               CAL_FIELDS[var2] = this;
            }
         }

      }

      public static DateFormat.Field ofCalendarField(int var0) {
         if (var0 >= 0 && var0 < CAL_FIELD_COUNT) {
            return CAL_FIELDS[var0];
         } else {
            throw new IllegalArgumentException("Calendar field number is out of range");
         }
      }

      public int getCalendarField() {
         return this.calendarField;
      }

      protected Object readResolve() throws InvalidObjectException {
         if (this.getClass() != DateFormat.Field.class) {
            throw new InvalidObjectException("A subclass of DateFormat.Field must implement readResolve.");
         } else {
            Object var1 = FIELD_NAME_MAP.get(this.getName());
            if (var1 == null) {
               throw new InvalidObjectException("Unknown attribute name.");
            } else {
               return var1;
            }
         }
      }

      static {
         GregorianCalendar var0 = new GregorianCalendar();
         CAL_FIELD_COUNT = var0.getFieldCount();
         CAL_FIELDS = new DateFormat.Field[CAL_FIELD_COUNT];
         FIELD_NAME_MAP = new HashMap(CAL_FIELD_COUNT);
         AM_PM = new DateFormat.Field("am pm", 9);
         DAY_OF_MONTH = new DateFormat.Field("day of month", 5);
         DAY_OF_WEEK = new DateFormat.Field("day of week", 7);
         DAY_OF_WEEK_IN_MONTH = new DateFormat.Field("day of week in month", 8);
         DAY_OF_YEAR = new DateFormat.Field("day of year", 6);
         ERA = new DateFormat.Field("era", 0);
         HOUR_OF_DAY0 = new DateFormat.Field("hour of day", 11);
         HOUR_OF_DAY1 = new DateFormat.Field("hour of day 1", -1);
         HOUR0 = new DateFormat.Field("hour", 10);
         HOUR1 = new DateFormat.Field("hour 1", -1);
         MILLISECOND = new DateFormat.Field("millisecond", 14);
         MINUTE = new DateFormat.Field("minute", 12);
         MONTH = new DateFormat.Field("month", 2);
         SECOND = new DateFormat.Field("second", 13);
         TIME_ZONE = new DateFormat.Field("time zone", -1);
         WEEK_OF_MONTH = new DateFormat.Field("week of month", 4);
         WEEK_OF_YEAR = new DateFormat.Field("week of year", 3);
         YEAR = new DateFormat.Field("year", 1);
         DOW_LOCAL = new DateFormat.Field("local day of week", 18);
         EXTENDED_YEAR = new DateFormat.Field("extended year", 19);
         JULIAN_DAY = new DateFormat.Field("Julian day", 20);
         MILLISECONDS_IN_DAY = new DateFormat.Field("milliseconds in day", 21);
         YEAR_WOY = new DateFormat.Field("year for week of year", 17);
         QUARTER = new DateFormat.Field("quarter", -1);
      }
   }
}
