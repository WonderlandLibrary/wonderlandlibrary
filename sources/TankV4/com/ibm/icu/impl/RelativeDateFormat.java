package com.ibm.icu.impl;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Comparator;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.TreeSet;

public class RelativeDateFormat extends DateFormat {
   private static final long serialVersionUID = 1131984966440549435L;
   private DateFormat fDateFormat;
   private DateFormat fTimeFormat;
   private MessageFormat fCombinedFormat;
   private SimpleDateFormat fDateTimeFormat = null;
   private String fDatePattern = null;
   private String fTimePattern = null;
   int fDateStyle;
   int fTimeStyle;
   ULocale fLocale;
   private transient RelativeDateFormat.URelativeString[] fDates = null;

   public RelativeDateFormat(int var1, int var2, ULocale var3) {
      this.fLocale = var3;
      this.fTimeStyle = var1;
      this.fDateStyle = var2;
      int var4;
      DateFormat var5;
      if (this.fDateStyle != -1) {
         var4 = this.fDateStyle & -129;
         var5 = DateFormat.getDateInstance(var4, var3);
         if (!(var5 instanceof SimpleDateFormat)) {
            throw new IllegalArgumentException("Can't create SimpleDateFormat for date style");
         }

         this.fDateTimeFormat = (SimpleDateFormat)var5;
         this.fDatePattern = this.fDateTimeFormat.toPattern();
         if (this.fTimeStyle != -1) {
            var4 = this.fTimeStyle & -129;
            var5 = DateFormat.getTimeInstance(var4, var3);
            if (var5 instanceof SimpleDateFormat) {
               this.fTimePattern = ((SimpleDateFormat)var5).toPattern();
            }
         }
      } else {
         var4 = this.fTimeStyle & -129;
         var5 = DateFormat.getTimeInstance(var4, var3);
         if (!(var5 instanceof SimpleDateFormat)) {
            throw new IllegalArgumentException("Can't create SimpleDateFormat for time style");
         }

         this.fDateTimeFormat = (SimpleDateFormat)var5;
         this.fTimePattern = this.fDateTimeFormat.toPattern();
      }

      this.initializeCalendar((TimeZone)null, this.fLocale);
      this.loadDates();
      this.initializeCombinedFormat(this.calendar, this.fLocale);
   }

   public StringBuffer format(Calendar var1, StringBuffer var2, FieldPosition var3) {
      String var4 = null;
      if (this.fDateStyle != -1) {
         int var5 = dayDifference(var1);
         var4 = this.getStringForDay(var5);
      }

      if (this.fDateTimeFormat != null && (this.fDatePattern != null || this.fTimePattern != null)) {
         if (this.fDatePattern == null) {
            this.fDateTimeFormat.applyPattern(this.fTimePattern);
            this.fDateTimeFormat.format(var1, var2, var3);
         } else if (this.fTimePattern == null) {
            if (var4 != null) {
               var2.append(var4);
            } else {
               this.fDateTimeFormat.applyPattern(this.fDatePattern);
               this.fDateTimeFormat.format(var1, var2, var3);
            }
         } else {
            String var7 = this.fDatePattern;
            if (var4 != null) {
               var7 = "'" + var4.replace("'", "''") + "'";
            }

            StringBuffer var6 = new StringBuffer("");
            this.fCombinedFormat.format(new Object[]{this.fTimePattern, var7}, var6, new FieldPosition(0));
            this.fDateTimeFormat.applyPattern(var6.toString());
            this.fDateTimeFormat.format(var1, var2, var3);
         }
      } else if (this.fDateFormat != null) {
         if (var4 != null) {
            var2.append(var4);
         } else {
            this.fDateFormat.format(var1, var2, var3);
         }
      }

      return var2;
   }

   public void parse(String var1, Calendar var2, ParsePosition var3) {
      throw new UnsupportedOperationException("Relative Date parse is not implemented yet");
   }

   private String getStringForDay(int var1) {
      if (this.fDates == null) {
         this.loadDates();
      }

      for(int var2 = 0; var2 < this.fDates.length; ++var2) {
         if (this.fDates[var2].offset == var1) {
            return this.fDates[var2].string;
         }
      }

      return null;
   }

   private synchronized void loadDates() {
      ICUResourceBundle var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", this.fLocale);
      ICUResourceBundle var2 = var1.getWithFallback("fields/day/relative");
      TreeSet var3 = new TreeSet(new Comparator(this) {
         final RelativeDateFormat this$0;

         {
            this.this$0 = var1;
         }

         public int compare(RelativeDateFormat.URelativeString var1, RelativeDateFormat.URelativeString var2) {
            if (var1.offset == var2.offset) {
               return 0;
            } else {
               return var1.offset < var2.offset ? -1 : 1;
            }
         }

         public int compare(Object var1, Object var2) {
            return this.compare((RelativeDateFormat.URelativeString)var1, (RelativeDateFormat.URelativeString)var2);
         }
      });
      UResourceBundleIterator var4 = var2.getIterator();

      while(var4.hasNext()) {
         UResourceBundle var5 = var4.next();
         String var6 = var5.getKey();
         String var7 = var5.getString();
         RelativeDateFormat.URelativeString var8 = new RelativeDateFormat.URelativeString(var6, var7);
         var3.add(var8);
      }

      this.fDates = (RelativeDateFormat.URelativeString[])var3.toArray(new RelativeDateFormat.URelativeString[0]);
   }

   private static int dayDifference(Calendar var0) {
      Calendar var1 = (Calendar)var0.clone();
      Date var2 = new Date(System.currentTimeMillis());
      var1.clear();
      var1.setTime(var2);
      int var3 = var0.get(20) - var1.get(20);
      return var3;
   }

   private Calendar initializeCalendar(TimeZone var1, ULocale var2) {
      if (this.calendar == null) {
         if (var1 == null) {
            this.calendar = Calendar.getInstance(var2);
         } else {
            this.calendar = Calendar.getInstance(var1, var2);
         }
      }

      return this.calendar;
   }

   private MessageFormat initializeCombinedFormat(Calendar var1, ULocale var2) {
      String var3 = "{1} {0}";

      try {
         CalendarData var4 = new CalendarData(var2, var1.getType());
         String[] var5 = var4.getDateTimePatterns();
         if (var5 != null && var5.length >= 9) {
            int var6 = 8;
            if (var5.length >= 13) {
               switch(this.fDateStyle) {
               case 0:
               case 128:
                  ++var6;
                  break;
               case 1:
               case 129:
                  var6 += 2;
                  break;
               case 2:
               case 130:
                  var6 += 3;
                  break;
               case 3:
               case 131:
                  var6 += 4;
               }
            }

            var3 = var5[var6];
         }
      } catch (MissingResourceException var7) {
      }

      this.fCombinedFormat = new MessageFormat(var3, var2);
      return this.fCombinedFormat;
   }

   public static class URelativeString {
      public int offset;
      public String string;

      URelativeString(int var1, String var2) {
         this.offset = var1;
         this.string = var2;
      }

      URelativeString(String var1, String var2) {
         this.offset = Integer.parseInt(var1);
         this.string = var2;
      }
   }
}
