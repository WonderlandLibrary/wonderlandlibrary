package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormat extends Format implements DateParser, DatePrinter {
   private static final long serialVersionUID = 2L;
   public static final int FULL = 0;
   public static final int LONG = 1;
   public static final int MEDIUM = 2;
   public static final int SHORT = 3;
   private static final FormatCache cache = new FormatCache() {
      protected FastDateFormat createInstance(String var1, TimeZone var2, Locale var3) {
         return new FastDateFormat(var1, var2, var3);
      }

      protected Format createInstance(String var1, TimeZone var2, Locale var3) {
         return this.createInstance(var1, var2, var3);
      }
   };
   private final FastDatePrinter printer;
   private final FastDateParser parser;

   public static FastDateFormat getInstance() {
      return (FastDateFormat)cache.getInstance();
   }

   public static FastDateFormat getInstance(String var0) {
      return (FastDateFormat)cache.getInstance(var0, (TimeZone)null, (Locale)null);
   }

   public static FastDateFormat getInstance(String var0, TimeZone var1) {
      return (FastDateFormat)cache.getInstance(var0, var1, (Locale)null);
   }

   public static FastDateFormat getInstance(String var0, Locale var1) {
      return (FastDateFormat)cache.getInstance(var0, (TimeZone)null, var1);
   }

   public static FastDateFormat getInstance(String var0, TimeZone var1, Locale var2) {
      return (FastDateFormat)cache.getInstance(var0, var1, var2);
   }

   public static FastDateFormat getDateInstance(int var0) {
      return (FastDateFormat)cache.getDateInstance(var0, (TimeZone)null, (Locale)null);
   }

   public static FastDateFormat getDateInstance(int var0, Locale var1) {
      return (FastDateFormat)cache.getDateInstance(var0, (TimeZone)null, var1);
   }

   public static FastDateFormat getDateInstance(int var0, TimeZone var1) {
      return (FastDateFormat)cache.getDateInstance(var0, var1, (Locale)null);
   }

   public static FastDateFormat getDateInstance(int var0, TimeZone var1, Locale var2) {
      return (FastDateFormat)cache.getDateInstance(var0, var1, var2);
   }

   public static FastDateFormat getTimeInstance(int var0) {
      return (FastDateFormat)cache.getTimeInstance(var0, (TimeZone)null, (Locale)null);
   }

   public static FastDateFormat getTimeInstance(int var0, Locale var1) {
      return (FastDateFormat)cache.getTimeInstance(var0, (TimeZone)null, var1);
   }

   public static FastDateFormat getTimeInstance(int var0, TimeZone var1) {
      return (FastDateFormat)cache.getTimeInstance(var0, var1, (Locale)null);
   }

   public static FastDateFormat getTimeInstance(int var0, TimeZone var1, Locale var2) {
      return (FastDateFormat)cache.getTimeInstance(var0, var1, var2);
   }

   public static FastDateFormat getDateTimeInstance(int var0, int var1) {
      return (FastDateFormat)cache.getDateTimeInstance(var0, var1, (TimeZone)null, (Locale)null);
   }

   public static FastDateFormat getDateTimeInstance(int var0, int var1, Locale var2) {
      return (FastDateFormat)cache.getDateTimeInstance(var0, var1, (TimeZone)null, var2);
   }

   public static FastDateFormat getDateTimeInstance(int var0, int var1, TimeZone var2) {
      return getDateTimeInstance(var0, var1, var2, (Locale)null);
   }

   public static FastDateFormat getDateTimeInstance(int var0, int var1, TimeZone var2, Locale var3) {
      return (FastDateFormat)cache.getDateTimeInstance(var0, var1, var2, var3);
   }

   protected FastDateFormat(String var1, TimeZone var2, Locale var3) {
      this(var1, var2, var3, (Date)null);
   }

   protected FastDateFormat(String var1, TimeZone var2, Locale var3, Date var4) {
      this.printer = new FastDatePrinter(var1, var2, var3);
      this.parser = new FastDateParser(var1, var2, var3, var4);
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      return this.printer.format(var1, var2, var3);
   }

   public String format(long var1) {
      return this.printer.format(var1);
   }

   public String format(Date var1) {
      return this.printer.format(var1);
   }

   public String format(Calendar var1) {
      return this.printer.format(var1);
   }

   public StringBuffer format(long var1, StringBuffer var3) {
      return this.printer.format(var1, var3);
   }

   public StringBuffer format(Date var1, StringBuffer var2) {
      return this.printer.format(var1, var2);
   }

   public StringBuffer format(Calendar var1, StringBuffer var2) {
      return this.printer.format(var1, var2);
   }

   public Date parse(String var1) throws ParseException {
      return this.parser.parse(var1);
   }

   public Date parse(String var1, ParsePosition var2) {
      return this.parser.parse(var1, var2);
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parser.parseObject(var1, var2);
   }

   public String getPattern() {
      return this.printer.getPattern();
   }

   public TimeZone getTimeZone() {
      return this.printer.getTimeZone();
   }

   public Locale getLocale() {
      return this.printer.getLocale();
   }

   public int getMaxLengthEstimate() {
      return this.printer.getMaxLengthEstimate();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FastDateFormat)) {
         return false;
      } else {
         FastDateFormat var2 = (FastDateFormat)var1;
         return this.printer.equals(var2.printer);
      }
   }

   public int hashCode() {
      return this.printer.hashCode();
   }

   public String toString() {
      return "FastDateFormat[" + this.printer.getPattern() + "," + this.printer.getLocale() + "," + this.printer.getTimeZone().getID() + "]";
   }

   protected StringBuffer applyRules(Calendar var1, StringBuffer var2) {
      return this.printer.applyRules(var1, var2);
   }
}
