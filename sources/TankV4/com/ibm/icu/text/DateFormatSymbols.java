package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DateFormatSymbols implements Serializable, Cloneable {
   public static final int FORMAT = 0;
   public static final int STANDALONE = 1;
   /** @deprecated */
   public static final int DT_CONTEXT_COUNT = 2;
   public static final int ABBREVIATED = 0;
   public static final int WIDE = 1;
   public static final int NARROW = 2;
   public static final int SHORT = 3;
   /** @deprecated */
   public static final int DT_WIDTH_COUNT = 4;
   static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
   static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
   static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
   static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
   static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
   static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
   static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
   static final int DT_MONTH_PATTERN_COUNT = 7;
   String[] eras;
   String[] eraNames;
   String[] narrowEras;
   String[] months;
   String[] shortMonths;
   String[] narrowMonths;
   String[] standaloneMonths;
   String[] standaloneShortMonths;
   String[] standaloneNarrowMonths;
   String[] weekdays;
   String[] shortWeekdays;
   String[] shorterWeekdays;
   String[] narrowWeekdays;
   String[] standaloneWeekdays;
   String[] standaloneShortWeekdays;
   String[] standaloneShorterWeekdays;
   String[] standaloneNarrowWeekdays;
   String[] ampms;
   String[] shortQuarters;
   String[] quarters;
   String[] standaloneShortQuarters;
   String[] standaloneQuarters;
   String[] leapMonthPatterns;
   String[] shortYearNames;
   private String[][] zoneStrings;
   static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
   String localPatternChars;
   private static final long serialVersionUID = -5987973545549424702L;
   private static final String[][] CALENDAR_CLASSES = new String[][]{{"GregorianCalendar", "gregorian"}, {"JapaneseCalendar", "japanese"}, {"BuddhistCalendar", "buddhist"}, {"TaiwanCalendar", "roc"}, {"PersianCalendar", "persian"}, {"IslamicCalendar", "islamic"}, {"HebrewCalendar", "hebrew"}, {"ChineseCalendar", "chinese"}, {"IndianCalendar", "indian"}, {"CopticCalendar", "coptic"}, {"EthiopicCalendar", "ethiopic"}};
   private static final Map contextUsageTypeMap = new HashMap();
   Map capitalization;
   static final int millisPerHour = 3600000;
   private static ICUCache DFSCACHE;
   private ULocale requestedLocale;
   private ULocale validLocale;
   private ULocale actualLocale;

   public DateFormatSymbols() {
      this(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public DateFormatSymbols(Locale var1) {
      this(ULocale.forLocale(var1));
   }

   public DateFormatSymbols(ULocale var1) {
      this.eras = null;
      this.eraNames = null;
      this.narrowEras = null;
      this.months = null;
      this.shortMonths = null;
      this.narrowMonths = null;
      this.standaloneMonths = null;
      this.standaloneShortMonths = null;
      this.standaloneNarrowMonths = null;
      this.weekdays = null;
      this.shortWeekdays = null;
      this.shorterWeekdays = null;
      this.narrowWeekdays = null;
      this.standaloneWeekdays = null;
      this.standaloneShortWeekdays = null;
      this.standaloneShorterWeekdays = null;
      this.standaloneNarrowWeekdays = null;
      this.ampms = null;
      this.shortQuarters = null;
      this.quarters = null;
      this.standaloneShortQuarters = null;
      this.standaloneQuarters = null;
      this.leapMonthPatterns = null;
      this.shortYearNames = null;
      this.zoneStrings = (String[][])null;
      this.localPatternChars = null;
      this.capitalization = null;
      this.initializeData(var1, CalendarUtil.getCalendarType(var1));
   }

   public static DateFormatSymbols getInstance() {
      return new DateFormatSymbols();
   }

   public static DateFormatSymbols getInstance(Locale var0) {
      return new DateFormatSymbols(var0);
   }

   public static DateFormatSymbols getInstance(ULocale var0) {
      return new DateFormatSymbols(var0);
   }

   public static Locale[] getAvailableLocales() {
      return ICUResourceBundle.getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return ICUResourceBundle.getAvailableULocales();
   }

   public String[] getEras() {
      return this.duplicate(this.eras);
   }

   public void setEras(String[] var1) {
      this.eras = this.duplicate(var1);
   }

   public String[] getEraNames() {
      return this.duplicate(this.eraNames);
   }

   public void setEraNames(String[] var1) {
      this.eraNames = this.duplicate(var1);
   }

   public String[] getMonths() {
      return this.duplicate(this.months);
   }

   public String[] getMonths(int var1, int var2) {
      String[] var3 = null;
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
         case 3:
            var3 = this.shortMonths;
            return this.duplicate(var3);
         case 1:
            var3 = this.months;
            return this.duplicate(var3);
         case 2:
            var3 = this.narrowMonths;
            return this.duplicate(var3);
         default:
            return this.duplicate(var3);
         }
      case 1:
         switch(var2) {
         case 0:
         case 3:
            var3 = this.standaloneShortMonths;
            break;
         case 1:
            var3 = this.standaloneMonths;
            break;
         case 2:
            var3 = this.standaloneNarrowMonths;
         }
      }

      return this.duplicate(var3);
   }

   public void setMonths(String[] var1) {
      this.months = this.duplicate(var1);
   }

   public void setMonths(String[] var1, int var2, int var3) {
      switch(var2) {
      case 0:
         switch(var3) {
         case 0:
            this.shortMonths = this.duplicate(var1);
            return;
         case 1:
            this.months = this.duplicate(var1);
            return;
         case 2:
            this.narrowMonths = this.duplicate(var1);
            return;
         default:
            return;
         }
      case 1:
         switch(var3) {
         case 0:
            this.standaloneShortMonths = this.duplicate(var1);
            break;
         case 1:
            this.standaloneMonths = this.duplicate(var1);
            break;
         case 2:
            this.standaloneNarrowMonths = this.duplicate(var1);
         }
      }

   }

   public String[] getShortMonths() {
      return this.duplicate(this.shortMonths);
   }

   public void setShortMonths(String[] var1) {
      this.shortMonths = this.duplicate(var1);
   }

   public String[] getWeekdays() {
      return this.duplicate(this.weekdays);
   }

   public String[] getWeekdays(int var1, int var2) {
      String[] var3 = null;
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
            var3 = this.shortWeekdays;
            return this.duplicate(var3);
         case 1:
            var3 = this.weekdays;
            return this.duplicate(var3);
         case 2:
            var3 = this.narrowWeekdays;
            return this.duplicate(var3);
         case 3:
            var3 = this.shorterWeekdays != null ? this.shorterWeekdays : this.shortWeekdays;
            return this.duplicate(var3);
         default:
            return this.duplicate(var3);
         }
      case 1:
         switch(var2) {
         case 0:
            var3 = this.standaloneShortWeekdays;
            break;
         case 1:
            var3 = this.standaloneWeekdays;
            break;
         case 2:
            var3 = this.standaloneNarrowWeekdays;
            break;
         case 3:
            var3 = this.standaloneShorterWeekdays != null ? this.standaloneShorterWeekdays : this.standaloneShortWeekdays;
         }
      }

      return this.duplicate(var3);
   }

   public void setWeekdays(String[] var1, int var2, int var3) {
      switch(var2) {
      case 0:
         switch(var3) {
         case 0:
            this.shortWeekdays = this.duplicate(var1);
            return;
         case 1:
            this.weekdays = this.duplicate(var1);
            return;
         case 2:
            this.narrowWeekdays = this.duplicate(var1);
            return;
         case 3:
            this.shorterWeekdays = this.duplicate(var1);
            return;
         default:
            return;
         }
      case 1:
         switch(var3) {
         case 0:
            this.standaloneShortWeekdays = this.duplicate(var1);
            break;
         case 1:
            this.standaloneWeekdays = this.duplicate(var1);
            break;
         case 2:
            this.standaloneNarrowWeekdays = this.duplicate(var1);
            break;
         case 3:
            this.standaloneShorterWeekdays = this.duplicate(var1);
         }
      }

   }

   public void setWeekdays(String[] var1) {
      this.weekdays = this.duplicate(var1);
   }

   public String[] getShortWeekdays() {
      return this.duplicate(this.shortWeekdays);
   }

   public void setShortWeekdays(String[] var1) {
      this.shortWeekdays = this.duplicate(var1);
   }

   public String[] getQuarters(int var1, int var2) {
      String[] var3 = null;
      switch(var1) {
      case 0:
         switch(var2) {
         case 0:
         case 3:
            var3 = this.shortQuarters;
            return this.duplicate(var3);
         case 1:
            var3 = this.quarters;
            return this.duplicate(var3);
         case 2:
            var3 = null;
            return this.duplicate(var3);
         default:
            return this.duplicate(var3);
         }
      case 1:
         switch(var2) {
         case 0:
         case 3:
            var3 = this.standaloneShortQuarters;
            break;
         case 1:
            var3 = this.standaloneQuarters;
            break;
         case 2:
            var3 = null;
         }
      }

      return this.duplicate(var3);
   }

   public void setQuarters(String[] var1, int var2, int var3) {
      switch(var2) {
      case 0:
         switch(var3) {
         case 0:
            this.shortQuarters = this.duplicate(var1);
            return;
         case 1:
            this.quarters = this.duplicate(var1);
            return;
         case 2:
         default:
            return;
         }
      case 1:
         switch(var3) {
         case 0:
            this.standaloneShortQuarters = this.duplicate(var1);
            break;
         case 1:
            this.standaloneQuarters = this.duplicate(var1);
         case 2:
         }
      }

   }

   public String[] getAmPmStrings() {
      return this.duplicate(this.ampms);
   }

   public void setAmPmStrings(String[] var1) {
      this.ampms = this.duplicate(var1);
   }

   public String[][] getZoneStrings() {
      if (this.zoneStrings != null) {
         return this.duplicate(this.zoneStrings);
      } else {
         String[] var1 = TimeZone.getAvailableIDs();
         TimeZoneNames var2 = TimeZoneNames.getInstance(this.validLocale);
         long var3 = System.currentTimeMillis();
         String[][] var5 = new String[var1.length][5];

         for(int var6 = 0; var6 < var1.length; ++var6) {
            String var7 = TimeZone.getCanonicalID(var1[var6]);
            if (var7 == null) {
               var7 = var1[var6];
            }

            var5[var6][0] = var1[var6];
            var5[var6][1] = var2.getDisplayName(var7, TimeZoneNames.NameType.LONG_STANDARD, var3);
            var5[var6][2] = var2.getDisplayName(var7, TimeZoneNames.NameType.SHORT_STANDARD, var3);
            var5[var6][3] = var2.getDisplayName(var7, TimeZoneNames.NameType.LONG_DAYLIGHT, var3);
            var5[var6][4] = var2.getDisplayName(var7, TimeZoneNames.NameType.SHORT_DAYLIGHT, var3);
         }

         this.zoneStrings = var5;
         return this.zoneStrings;
      }
   }

   public void setZoneStrings(String[][] var1) {
      this.zoneStrings = this.duplicate(var1);
   }

   public String getLocalPatternChars() {
      return this.localPatternChars;
   }

   public void setLocalPatternChars(String var1) {
      this.localPatternChars = var1;
   }

   public Object clone() {
      try {
         DateFormatSymbols var1 = (DateFormatSymbols)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }

   public int hashCode() {
      return this.requestedLocale.toString().hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         DateFormatSymbols var2 = (DateFormatSymbols)var1;
         return Utility.arrayEquals((Object[])this.eras, var2.eras) && Utility.arrayEquals((Object[])this.eraNames, var2.eraNames) && Utility.arrayEquals((Object[])this.months, var2.months) && Utility.arrayEquals((Object[])this.shortMonths, var2.shortMonths) && Utility.arrayEquals((Object[])this.narrowMonths, var2.narrowMonths) && Utility.arrayEquals((Object[])this.standaloneMonths, var2.standaloneMonths) && Utility.arrayEquals((Object[])this.standaloneShortMonths, var2.standaloneShortMonths) && Utility.arrayEquals((Object[])this.standaloneNarrowMonths, var2.standaloneNarrowMonths) && Utility.arrayEquals((Object[])this.weekdays, var2.weekdays) && Utility.arrayEquals((Object[])this.shortWeekdays, var2.shortWeekdays) && Utility.arrayEquals((Object[])this.shorterWeekdays, var2.shorterWeekdays) && Utility.arrayEquals((Object[])this.narrowWeekdays, var2.narrowWeekdays) && Utility.arrayEquals((Object[])this.standaloneWeekdays, var2.standaloneWeekdays) && Utility.arrayEquals((Object[])this.standaloneShortWeekdays, var2.standaloneShortWeekdays) && Utility.arrayEquals((Object[])this.standaloneShorterWeekdays, var2.standaloneShorterWeekdays) && Utility.arrayEquals((Object[])this.standaloneNarrowWeekdays, var2.standaloneNarrowWeekdays) && Utility.arrayEquals((Object[])this.ampms, var2.ampms) && this.zoneStrings == var2.zoneStrings && this.requestedLocale.getDisplayName().equals(var2.requestedLocale.getDisplayName()) && Utility.arrayEquals((Object)this.localPatternChars, var2.localPatternChars);
      } else {
         return false;
      }
   }

   protected void initializeData(ULocale var1, String var2) {
      String var3 = var1.getBaseName() + "+" + var2;
      DateFormatSymbols var4 = (DateFormatSymbols)DFSCACHE.get(var3);
      if (var4 == null) {
         CalendarData var5 = new CalendarData(var1, var2);
         this.initializeData(var1, var5);
         if (this.getClass().getName().equals("com.ibm.icu.text.DateFormatSymbols")) {
            var4 = (DateFormatSymbols)this.clone();
            DFSCACHE.put(var3, var4);
         }
      } else {
         this.initializeData(var4);
      }

   }

   void initializeData(DateFormatSymbols var1) {
      this.eras = var1.eras;
      this.eraNames = var1.eraNames;
      this.narrowEras = var1.narrowEras;
      this.months = var1.months;
      this.shortMonths = var1.shortMonths;
      this.narrowMonths = var1.narrowMonths;
      this.standaloneMonths = var1.standaloneMonths;
      this.standaloneShortMonths = var1.standaloneShortMonths;
      this.standaloneNarrowMonths = var1.standaloneNarrowMonths;
      this.weekdays = var1.weekdays;
      this.shortWeekdays = var1.shortWeekdays;
      this.shorterWeekdays = var1.shorterWeekdays;
      this.narrowWeekdays = var1.narrowWeekdays;
      this.standaloneWeekdays = var1.standaloneWeekdays;
      this.standaloneShortWeekdays = var1.standaloneShortWeekdays;
      this.standaloneShorterWeekdays = var1.standaloneShorterWeekdays;
      this.standaloneNarrowWeekdays = var1.standaloneNarrowWeekdays;
      this.ampms = var1.ampms;
      this.shortQuarters = var1.shortQuarters;
      this.quarters = var1.quarters;
      this.standaloneShortQuarters = var1.standaloneShortQuarters;
      this.standaloneQuarters = var1.standaloneQuarters;
      this.leapMonthPatterns = var1.leapMonthPatterns;
      this.shortYearNames = var1.shortYearNames;
      this.zoneStrings = var1.zoneStrings;
      this.localPatternChars = var1.localPatternChars;
      this.capitalization = var1.capitalization;
      this.actualLocale = var1.actualLocale;
      this.validLocale = var1.validLocale;
      this.requestedLocale = var1.requestedLocale;
   }

   /** @deprecated */
   protected void initializeData(ULocale var1, CalendarData var2) {
      this.eras = var2.getEras("abbreviated");
      this.eraNames = var2.getEras("wide");
      this.narrowEras = var2.getEras("narrow");
      this.months = var2.getStringArray("monthNames", "wide");
      this.shortMonths = var2.getStringArray("monthNames", "abbreviated");
      this.narrowMonths = var2.getStringArray("monthNames", "narrow");
      this.standaloneMonths = var2.getStringArray("monthNames", "stand-alone", "wide");
      this.standaloneShortMonths = var2.getStringArray("monthNames", "stand-alone", "abbreviated");
      this.standaloneNarrowMonths = var2.getStringArray("monthNames", "stand-alone", "narrow");
      String[] var3 = var2.getStringArray("dayNames", "wide");
      this.weekdays = new String[8];
      this.weekdays[0] = "";
      System.arraycopy(var3, 0, this.weekdays, 1, var3.length);
      String[] var4 = var2.getStringArray("dayNames", "abbreviated");
      this.shortWeekdays = new String[8];
      this.shortWeekdays[0] = "";
      System.arraycopy(var4, 0, this.shortWeekdays, 1, var4.length);
      String[] var5 = var2.getStringArray("dayNames", "short");
      this.shorterWeekdays = new String[8];
      this.shorterWeekdays[0] = "";
      System.arraycopy(var5, 0, this.shorterWeekdays, 1, var5.length);
      String[] var6 = null;

      try {
         var6 = var2.getStringArray("dayNames", "narrow");
      } catch (MissingResourceException var28) {
         try {
            var6 = var2.getStringArray("dayNames", "stand-alone", "narrow");
         } catch (MissingResourceException var27) {
            var6 = var2.getStringArray("dayNames", "abbreviated");
         }
      }

      this.narrowWeekdays = new String[8];
      this.narrowWeekdays[0] = "";
      System.arraycopy(var6, 0, this.narrowWeekdays, 1, var6.length);
      String[] var7 = null;
      var7 = var2.getStringArray("dayNames", "stand-alone", "wide");
      this.standaloneWeekdays = new String[8];
      this.standaloneWeekdays[0] = "";
      System.arraycopy(var7, 0, this.standaloneWeekdays, 1, var7.length);
      String[] var8 = null;
      var8 = var2.getStringArray("dayNames", "stand-alone", "abbreviated");
      this.standaloneShortWeekdays = new String[8];
      this.standaloneShortWeekdays[0] = "";
      System.arraycopy(var8, 0, this.standaloneShortWeekdays, 1, var8.length);
      String[] var9 = null;
      var9 = var2.getStringArray("dayNames", "stand-alone", "short");
      this.standaloneShorterWeekdays = new String[8];
      this.standaloneShorterWeekdays[0] = "";
      System.arraycopy(var9, 0, this.standaloneShorterWeekdays, 1, var9.length);
      String[] var10 = null;
      var10 = var2.getStringArray("dayNames", "stand-alone", "narrow");
      this.standaloneNarrowWeekdays = new String[8];
      this.standaloneNarrowWeekdays[0] = "";
      System.arraycopy(var10, 0, this.standaloneNarrowWeekdays, 1, var10.length);
      this.ampms = var2.getStringArray("AmPmMarkers");
      this.quarters = var2.getStringArray("quarters", "wide");
      this.shortQuarters = var2.getStringArray("quarters", "abbreviated");
      this.standaloneQuarters = var2.getStringArray("quarters", "stand-alone", "wide");
      this.standaloneShortQuarters = var2.getStringArray("quarters", "stand-alone", "abbreviated");
      ICUResourceBundle var11 = null;

      try {
         var11 = var2.get("monthPatterns");
      } catch (MissingResourceException var26) {
         var11 = null;
      }

      if (var11 != null) {
         this.leapMonthPatterns = new String[7];
         this.leapMonthPatterns[0] = var2.get("monthPatterns", "wide").get("leap").getString();
         this.leapMonthPatterns[1] = var2.get("monthPatterns", "abbreviated").get("leap").getString();
         this.leapMonthPatterns[2] = var2.get("monthPatterns", "narrow").get("leap").getString();
         this.leapMonthPatterns[3] = var2.get("monthPatterns", "stand-alone", "wide").get("leap").getString();
         this.leapMonthPatterns[4] = var2.get("monthPatterns", "stand-alone", "abbreviated").get("leap").getString();
         this.leapMonthPatterns[5] = var2.get("monthPatterns", "stand-alone", "narrow").get("leap").getString();
         this.leapMonthPatterns[6] = var2.get("monthPatterns", "numeric", "all").get("leap").getString();
      }

      ICUResourceBundle var12 = null;

      try {
         var12 = var2.get("cyclicNameSets");
      } catch (MissingResourceException var25) {
         var12 = null;
      }

      if (var12 != null) {
         this.shortYearNames = var2.get("cyclicNameSets", "years", "format", "abbreviated").getStringArray();
      }

      this.requestedLocale = var1;
      ICUResourceBundle var13 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1);
      this.localPatternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
      ULocale var14 = var13.getULocale();
      this.setLocale(var14, var14);
      this.capitalization = new HashMap();
      boolean[] var15 = new boolean[]{false, false};
      DateFormatSymbols.CapitalizationContextUsage[] var16 = DateFormatSymbols.CapitalizationContextUsage.values();
      DateFormatSymbols.CapitalizationContextUsage[] var17 = var16;
      int var18 = var16.length;

      for(int var19 = 0; var19 < var18; ++var19) {
         DateFormatSymbols.CapitalizationContextUsage var20 = var17[var19];
         this.capitalization.put(var20, var15);
      }

      var17 = null;

      ICUResourceBundle var29;
      try {
         var29 = var13.getWithFallback("contextTransforms");
      } catch (MissingResourceException var24) {
         var29 = null;
      }

      if (var29 != null) {
         UResourceBundleIterator var30 = var29.getIterator();

         while(var30.hasNext()) {
            UResourceBundle var31 = var30.next();
            int[] var32 = var31.getIntVector();
            if (var32.length >= 2) {
               String var21 = var31.getKey();
               DateFormatSymbols.CapitalizationContextUsage var22 = (DateFormatSymbols.CapitalizationContextUsage)contextUsageTypeMap.get(var21);
               if (var22 != null) {
                  boolean[] var23 = new boolean[]{var32[0] != 0, var32[1] != 0};
                  this.capitalization.put(var22, var23);
               }
            }
         }
      }

   }

   private final String[] duplicate(String[] var1) {
      return (String[])var1.clone();
   }

   private final String[][] duplicate(String[][] var1) {
      String[][] var2 = new String[var1.length][];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.duplicate(var1[var3]);
      }

      return var2;
   }

   public DateFormatSymbols(Calendar var1, Locale var2) {
      this.eras = null;
      this.eraNames = null;
      this.narrowEras = null;
      this.months = null;
      this.shortMonths = null;
      this.narrowMonths = null;
      this.standaloneMonths = null;
      this.standaloneShortMonths = null;
      this.standaloneNarrowMonths = null;
      this.weekdays = null;
      this.shortWeekdays = null;
      this.shorterWeekdays = null;
      this.narrowWeekdays = null;
      this.standaloneWeekdays = null;
      this.standaloneShortWeekdays = null;
      this.standaloneShorterWeekdays = null;
      this.standaloneNarrowWeekdays = null;
      this.ampms = null;
      this.shortQuarters = null;
      this.quarters = null;
      this.standaloneShortQuarters = null;
      this.standaloneQuarters = null;
      this.leapMonthPatterns = null;
      this.shortYearNames = null;
      this.zoneStrings = (String[][])null;
      this.localPatternChars = null;
      this.capitalization = null;
      this.initializeData(ULocale.forLocale(var2), var1.getType());
   }

   public DateFormatSymbols(Calendar var1, ULocale var2) {
      this.eras = null;
      this.eraNames = null;
      this.narrowEras = null;
      this.months = null;
      this.shortMonths = null;
      this.narrowMonths = null;
      this.standaloneMonths = null;
      this.standaloneShortMonths = null;
      this.standaloneNarrowMonths = null;
      this.weekdays = null;
      this.shortWeekdays = null;
      this.shorterWeekdays = null;
      this.narrowWeekdays = null;
      this.standaloneWeekdays = null;
      this.standaloneShortWeekdays = null;
      this.standaloneShorterWeekdays = null;
      this.standaloneNarrowWeekdays = null;
      this.ampms = null;
      this.shortQuarters = null;
      this.quarters = null;
      this.standaloneShortQuarters = null;
      this.standaloneQuarters = null;
      this.leapMonthPatterns = null;
      this.shortYearNames = null;
      this.zoneStrings = (String[][])null;
      this.localPatternChars = null;
      this.capitalization = null;
      this.initializeData(var2, var1.getType());
   }

   public DateFormatSymbols(Class var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   public DateFormatSymbols(Class var1, ULocale var2) {
      this.eras = null;
      this.eraNames = null;
      this.narrowEras = null;
      this.months = null;
      this.shortMonths = null;
      this.narrowMonths = null;
      this.standaloneMonths = null;
      this.standaloneShortMonths = null;
      this.standaloneNarrowMonths = null;
      this.weekdays = null;
      this.shortWeekdays = null;
      this.shorterWeekdays = null;
      this.narrowWeekdays = null;
      this.standaloneWeekdays = null;
      this.standaloneShortWeekdays = null;
      this.standaloneShorterWeekdays = null;
      this.standaloneNarrowWeekdays = null;
      this.ampms = null;
      this.shortQuarters = null;
      this.quarters = null;
      this.standaloneShortQuarters = null;
      this.standaloneQuarters = null;
      this.leapMonthPatterns = null;
      this.shortYearNames = null;
      this.zoneStrings = (String[][])null;
      this.localPatternChars = null;
      this.capitalization = null;
      String var3 = var1.getName();
      int var4 = var3.lastIndexOf(46);
      String var5 = var3.substring(var4 + 1);
      String var6 = null;
      String[][] var7 = CALENDAR_CLASSES;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String[] var10 = var7[var9];
         if (var10[0].equals(var5)) {
            var6 = var10[1];
            break;
         }
      }

      if (var6 == null) {
         var6 = var5.replaceAll("Calendar", "").toLowerCase(Locale.ENGLISH);
      }

      this.initializeData(var2, var6);
   }

   public DateFormatSymbols(ResourceBundle var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   public DateFormatSymbols(ResourceBundle var1, ULocale var2) {
      this.eras = null;
      this.eraNames = null;
      this.narrowEras = null;
      this.months = null;
      this.shortMonths = null;
      this.narrowMonths = null;
      this.standaloneMonths = null;
      this.standaloneShortMonths = null;
      this.standaloneNarrowMonths = null;
      this.weekdays = null;
      this.shortWeekdays = null;
      this.shorterWeekdays = null;
      this.narrowWeekdays = null;
      this.standaloneWeekdays = null;
      this.standaloneShortWeekdays = null;
      this.standaloneShorterWeekdays = null;
      this.standaloneNarrowWeekdays = null;
      this.ampms = null;
      this.shortQuarters = null;
      this.quarters = null;
      this.standaloneShortQuarters = null;
      this.standaloneQuarters = null;
      this.leapMonthPatterns = null;
      this.shortYearNames = null;
      this.zoneStrings = (String[][])null;
      this.localPatternChars = null;
      this.capitalization = null;
      this.initializeData(var2, new CalendarData((ICUResourceBundle)var1, CalendarUtil.getCalendarType(var2)));
   }

   /** @deprecated */
   public static ResourceBundle getDateFormatBundle(Class var0, Locale var1) throws MissingResourceException {
      return null;
   }

   /** @deprecated */
   public static ResourceBundle getDateFormatBundle(Class var0, ULocale var1) throws MissingResourceException {
      return null;
   }

   /** @deprecated */
   public static ResourceBundle getDateFormatBundle(Calendar var0, Locale var1) throws MissingResourceException {
      return null;
   }

   /** @deprecated */
   public static ResourceBundle getDateFormatBundle(Calendar var0, ULocale var1) throws MissingResourceException {
      return null;
   }

   public final ULocale getLocale(ULocale.Type var1) {
      return var1 == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
   }

   final void setLocale(ULocale var1, ULocale var2) {
      if (var1 == null != (var2 == null)) {
         throw new IllegalArgumentException();
      } else {
         this.validLocale = var1;
         this.actualLocale = var2;
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
   }

   static {
      contextUsageTypeMap.put("month-format-except-narrow", DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT);
      contextUsageTypeMap.put("month-standalone-except-narrow", DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE);
      contextUsageTypeMap.put("month-narrow", DateFormatSymbols.CapitalizationContextUsage.MONTH_NARROW);
      contextUsageTypeMap.put("day-format-except-narrow", DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT);
      contextUsageTypeMap.put("day-standalone-except-narrow", DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE);
      contextUsageTypeMap.put("day-narrow", DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW);
      contextUsageTypeMap.put("era-name", DateFormatSymbols.CapitalizationContextUsage.ERA_WIDE);
      contextUsageTypeMap.put("era-abbr", DateFormatSymbols.CapitalizationContextUsage.ERA_ABBREV);
      contextUsageTypeMap.put("era-narrow", DateFormatSymbols.CapitalizationContextUsage.ERA_NARROW);
      contextUsageTypeMap.put("zone-long", DateFormatSymbols.CapitalizationContextUsage.ZONE_LONG);
      contextUsageTypeMap.put("zone-short", DateFormatSymbols.CapitalizationContextUsage.ZONE_SHORT);
      contextUsageTypeMap.put("metazone-long", DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG);
      contextUsageTypeMap.put("metazone-short", DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT);
      DFSCACHE = new SimpleCache();
   }

   static enum CapitalizationContextUsage {
      OTHER,
      MONTH_FORMAT,
      MONTH_STANDALONE,
      MONTH_NARROW,
      DAY_FORMAT,
      DAY_STANDALONE,
      DAY_NARROW,
      ERA_WIDE,
      ERA_ABBREV,
      ERA_NARROW,
      ZONE_LONG,
      ZONE_SHORT,
      METAZONE_LONG,
      METAZONE_SHORT;

      private static final DateFormatSymbols.CapitalizationContextUsage[] $VALUES = new DateFormatSymbols.CapitalizationContextUsage[]{OTHER, MONTH_FORMAT, MONTH_STANDALONE, MONTH_NARROW, DAY_FORMAT, DAY_STANDALONE, DAY_NARROW, ERA_WIDE, ERA_ABBREV, ERA_NARROW, ZONE_LONG, ZONE_SHORT, METAZONE_LONG, METAZONE_SHORT};
   }
}
