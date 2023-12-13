package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.DateNumberFormat;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public class SimpleDateFormat extends DateFormat {
   private static final long serialVersionUID = 4774881970558875024L;
   static final int currentSerialVersion = 2;
   static boolean DelayedHebrewMonthCheck = false;
   private static final int[] CALENDAR_FIELD_TO_LEVEL = new int[]{0, 10, 20, 20, 30, 30, 20, 30, 30, 40, 50, 50, 60, 70, 80, 0, 0, 10, 30, 10, 0, 40};
   private static final int[] PATTERN_CHAR_TO_LEVEL = new int[]{-1, 40, -1, -1, 20, 30, 30, 0, 50, -1, -1, 50, 20, 20, -1, 0, -1, 20, -1, 80, -1, 10, 0, 30, 0, 10, 0, -1, -1, -1, -1, -1, -1, 40, -1, 30, 30, 30, -1, 0, 50, -1, -1, 50, -1, 60, -1, -1, -1, 20, -1, 70, -1, 10, 0, 20, 0, 10, 0, -1, -1, -1, -1, -1};
   private static final int HEBREW_CAL_CUR_MILLENIUM_START_YEAR = 5000;
   private static final int HEBREW_CAL_CUR_MILLENIUM_END_YEAR = 6000;
   private int serialVersionOnStream;
   private String pattern;
   private String override;
   private HashMap numberFormatters;
   private HashMap overrideMap;
   private DateFormatSymbols formatData;
   private transient ULocale locale;
   private Date defaultCenturyStart;
   private transient int defaultCenturyStartYear;
   private transient long defaultCenturyBase;
   private transient TimeZoneFormat.TimeType tztype;
   private static final int millisPerHour = 3600000;
   private static final int ISOSpecialEra = -32000;
   private static final String SUPPRESS_NEGATIVE_PREFIX = "\uab00";
   private transient boolean useFastFormat;
   private volatile TimeZoneFormat tzFormat;
   private transient DisplayContext capitalizationSetting;
   private static ULocale cachedDefaultLocale = null;
   private static String cachedDefaultPattern = null;
   private static final String FALLBACKPATTERN = "yy/MM/dd HH:mm";
   private static final int PATTERN_CHAR_BASE = 64;
   private static final int[] PATTERN_CHAR_TO_INDEX = new int[]{-1, 22, -1, -1, 10, 9, 11, 0, 5, -1, -1, 16, 26, 2, -1, 31, -1, 27, -1, 8, -1, 30, 29, 13, 32, 18, 23, -1, -1, -1, -1, -1, -1, 14, -1, 25, 3, 19, -1, 21, 15, -1, -1, 4, -1, 6, -1, -1, -1, 28, -1, 7, -1, 20, 24, 12, 33, 1, 17, -1, -1, -1, -1, -1};
   private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD = new int[]{0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 17, 18, 19, 20, 21, 15, 15, 18, 2, 2, 2, 15, 1, 15, 15, 15};
   private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33};
   private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE;
   private static ICUCache PARSED_PATTERN_CACHE;
   private transient Object[] patternItems;
   private transient boolean useLocalZeroPaddingNumberFormat;
   private transient char[] decDigits;
   private transient char[] decimalBuf;
   private static final String NUMERIC_FORMAT_CHARS = "MYyudehHmsSDFwWkK";
   static final UnicodeSet DATE_PATTERN_TYPE;

   public SimpleDateFormat() {
      this(getDefaultPattern(), (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
   }

   public SimpleDateFormat(String var1) {
      this(var1, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
   }

   public SimpleDateFormat(String var1, Locale var2) {
      this(var1, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, ULocale.forLocale(var2), true, (String)null);
   }

   public SimpleDateFormat(String var1, ULocale var2) {
      this(var1, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, var2, true, (String)null);
   }

   public SimpleDateFormat(String var1, String var2, ULocale var3) {
      this(var1, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, var3, false, var2);
   }

   public SimpleDateFormat(String var1, DateFormatSymbols var2) {
      this(var1, (DateFormatSymbols)var2.clone(), (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
   }

   /** @deprecated */
   public SimpleDateFormat(String var1, DateFormatSymbols var2, ULocale var3) {
      this(var1, (DateFormatSymbols)var2.clone(), (Calendar)null, (NumberFormat)null, var3, true, (String)null);
   }

   SimpleDateFormat(String var1, DateFormatSymbols var2, Calendar var3, ULocale var4, boolean var5, String var6) {
      this(var1, (DateFormatSymbols)var2.clone(), (Calendar)var3.clone(), (NumberFormat)null, var4, var5, var6);
   }

   private SimpleDateFormat(String var1, DateFormatSymbols var2, Calendar var3, NumberFormat var4, ULocale var5, boolean var6, String var7) {
      this.serialVersionOnStream = 2;
      this.tztype = TimeZoneFormat.TimeType.UNKNOWN;
      this.pattern = var1;
      this.formatData = var2;
      this.calendar = var3;
      this.numberFormat = var4;
      this.locale = var5;
      this.useFastFormat = var6;
      this.override = var7;
      this.initialize();
   }

   /** @deprecated */
   public static SimpleDateFormat getInstance(Calendar.FormatConfiguration var0) {
      String var1 = var0.getOverrideString();
      boolean var2 = var1 != null && var1.length() > 0;
      return new SimpleDateFormat(var0.getPatternString(), var0.getDateFormatSymbols(), var0.getCalendar(), (NumberFormat)null, var0.getLocale(), var2, var0.getOverrideString());
   }

   private void initialize() {
      if (this.locale == null) {
         this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
      }

      if (this.formatData == null) {
         this.formatData = new DateFormatSymbols(this.locale);
      }

      if (this.calendar == null) {
         this.calendar = Calendar.getInstance(this.locale);
      }

      if (this.numberFormat == null) {
         NumberingSystem var1 = NumberingSystem.getInstance(this.locale);
         if (var1.isAlgorithmic()) {
            this.numberFormat = NumberFormat.getInstance(this.locale);
         } else {
            String var2 = var1.getDescription();
            String var3 = var1.getName();
            this.numberFormat = new DateNumberFormat(this.locale, var2, var3);
         }
      }

      this.defaultCenturyBase = System.currentTimeMillis();
      this.setLocale(this.calendar.getLocale(ULocale.VALID_LOCALE), this.calendar.getLocale(ULocale.ACTUAL_LOCALE));
      this.initLocalZeroPaddingNumberFormat();
      if (this.override != null) {
         this.initNumberFormatters(this.locale);
      }

      this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
   }

   private synchronized void initializeTimeZoneFormat(boolean var1) {
      if (var1 || this.tzFormat == null) {
         this.tzFormat = TimeZoneFormat.getInstance(this.locale);
         String var2 = null;
         if (this.numberFormat instanceof DecimalFormat) {
            DecimalFormatSymbols var3 = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols();
            var2 = new String(var3.getDigits());
         } else if (this.numberFormat instanceof DateNumberFormat) {
            var2 = new String(((DateNumberFormat)this.numberFormat).getDigits());
         }

         if (var2 != null && !this.tzFormat.getGMTOffsetDigits().equals(var2)) {
            if (this.tzFormat.isFrozen()) {
               this.tzFormat = this.tzFormat.cloneAsThawed();
            }

            this.tzFormat.setGMTOffsetDigits(var2);
         }
      }

   }

   private TimeZoneFormat tzFormat() {
      if (this.tzFormat == null) {
         this.initializeTimeZoneFormat(false);
      }

      return this.tzFormat;
   }

   private static synchronized String getDefaultPattern() {
      ULocale var0 = ULocale.getDefault(ULocale.Category.FORMAT);
      if (!var0.equals(cachedDefaultLocale)) {
         cachedDefaultLocale = var0;
         Calendar var1 = Calendar.getInstance(cachedDefaultLocale);

         try {
            CalendarData var2 = new CalendarData(cachedDefaultLocale, var1.getType());
            String[] var3 = var2.getDateTimePatterns();
            int var4 = 8;
            if (var3.length >= 13) {
               var4 += 4;
            }

            cachedDefaultPattern = MessageFormat.format(var3[var4], var3[3], var3[7]);
         } catch (MissingResourceException var5) {
            cachedDefaultPattern = "yy/MM/dd HH:mm";
         }
      }

      return cachedDefaultPattern;
   }

   private void parseAmbiguousDatesAsAfter(Date var1) {
      this.defaultCenturyStart = var1;
      this.calendar.setTime(var1);
      this.defaultCenturyStartYear = this.calendar.get(1);
   }

   private void initializeDefaultCenturyStart(long var1) {
      this.defaultCenturyBase = var1;
      Calendar var3 = (Calendar)this.calendar.clone();
      var3.setTimeInMillis(var1);
      var3.add(1, -80);
      this.defaultCenturyStart = var3.getTime();
      this.defaultCenturyStartYear = var3.get(1);
   }

   private Date getDefaultCenturyStart() {
      if (this.defaultCenturyStart == null) {
         this.initializeDefaultCenturyStart(this.defaultCenturyBase);
      }

      return this.defaultCenturyStart;
   }

   private int getDefaultCenturyStartYear() {
      if (this.defaultCenturyStart == null) {
         this.initializeDefaultCenturyStart(this.defaultCenturyBase);
      }

      return this.defaultCenturyStartYear;
   }

   public void set2DigitYearStart(Date var1) {
      this.parseAmbiguousDatesAsAfter(var1);
   }

   public Date get2DigitYearStart() {
      return this.getDefaultCenturyStart();
   }

   public StringBuffer format(Calendar var1, StringBuffer var2, FieldPosition var3) {
      TimeZone var4 = null;
      if (var1 != this.calendar && !var1.getType().equals(this.calendar.getType())) {
         this.calendar.setTimeInMillis(var1.getTimeInMillis());
         var4 = this.calendar.getTimeZone();
         this.calendar.setTimeZone(var1.getTimeZone());
         var1 = this.calendar;
      }

      StringBuffer var5 = this.format(var1, this.capitalizationSetting, var2, var3, (List)null);
      if (var4 != null) {
         this.calendar.setTimeZone(var4);
      }

      return var5;
   }

   private StringBuffer format(Calendar var1, DisplayContext var2, StringBuffer var3, FieldPosition var4, List var5) {
      var4.setBeginIndex(0);
      var4.setEndIndex(0);
      Object[] var6 = this.getPatternItems();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         if (var6[var7] instanceof String) {
            var3.append((String)var6[var7]);
         } else {
            SimpleDateFormat.PatternItem var8 = (SimpleDateFormat.PatternItem)var6[var7];
            int var9 = 0;
            if (var5 != null) {
               var9 = var3.length();
            }

            if (this.useFastFormat) {
               this.subFormat(var3, var8.type, var8.length, var3.length(), var7, var2, var4, var1);
            } else {
               var3.append(this.subFormat(var8.type, var8.length, var3.length(), var7, var2, var4, var1));
            }

            if (var5 != null) {
               int var10 = var3.length();
               if (var10 - var9 > 0) {
                  DateFormat.Field var11 = this.patternCharToDateFormatField(var8.type);
                  FieldPosition var12 = new FieldPosition(var11);
                  var12.setBeginIndex(var9);
                  var12.setEndIndex(var10);
                  var5.add(var12);
               }
            }
         }
      }

      return var3;
   }

   protected DateFormat.Field patternCharToDateFormatField(char var1) {
      int var2 = -1;
      if ('A' <= var1 && var1 <= 'z') {
         var2 = PATTERN_CHAR_TO_INDEX[var1 - 64];
      }

      return var2 != -1 ? PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[var2] : null;
   }

   protected String subFormat(char var1, int var2, int var3, FieldPosition var4, DateFormatSymbols var5, Calendar var6) throws IllegalArgumentException {
      return this.subFormat(var1, var2, var3, 0, DisplayContext.CAPITALIZATION_NONE, var4, var6);
   }

   /** @deprecated */
   protected String subFormat(char var1, int var2, int var3, int var4, DisplayContext var5, FieldPosition var6, Calendar var7) {
      StringBuffer var8 = new StringBuffer();
      this.subFormat(var8, var1, var2, var3, var4, var5, var6, var7);
      return var8.toString();
   }

   /** @deprecated */
   protected void subFormat(StringBuffer var1, char var2, int var3, int var4, int var5, DisplayContext var6, FieldPosition var7, Calendar var8) {
      int var9 = Integer.MAX_VALUE;
      int var10 = var1.length();
      TimeZone var11 = var8.getTimeZone();
      long var12 = var8.getTimeInMillis();
      String var14 = null;
      int var15 = -1;
      if ('A' <= var2 && var2 <= 'z') {
         var15 = PATTERN_CHAR_TO_INDEX[var2 - 64];
      }

      if (var15 == -1) {
         if (var2 != 'l') {
            throw new IllegalArgumentException("Illegal pattern character '" + var2 + "' in \"" + this.pattern + '"');
         }
      } else {
         int var16 = PATTERN_INDEX_TO_CALENDAR_FIELD[var15];
         int var17 = var8.get(var16);
         NumberFormat var18 = this.getNumberFormat(var2);
         DateFormatSymbols.CapitalizationContextUsage var19 = DateFormatSymbols.CapitalizationContextUsage.OTHER;
         boolean var20;
         switch(var15) {
         case 0:
            if (var8.getType().equals("chinese")) {
               this.zeroPaddingNumber(var18, var1, var17, 1, 9);
            } else if (var3 == 5) {
               safeAppend(this.formatData.narrowEras, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.ERA_NARROW;
            } else if (var3 == 4) {
               safeAppend(this.formatData.eraNames, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.ERA_WIDE;
            } else {
               safeAppend(this.formatData.eras, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.ERA_ABBREV;
            }
            break;
         case 2:
         case 26:
            if (var8.getType().equals("hebrew")) {
               var20 = HebrewCalendar.isLeapYear(var8.get(1));
               if (var20 && var17 == 6 && var3 >= 3) {
                  var17 = 13;
               }

               if (!var20 && var17 >= 6 && var3 < 3) {
                  --var17;
               }
            }

            int var23 = this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7 ? var8.get(22) : 0;
            if (var3 == 5) {
               if (var15 == 2) {
                  safeAppendWithMonthPattern(this.formatData.narrowMonths, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[2] : null);
               } else {
                  safeAppendWithMonthPattern(this.formatData.standaloneNarrowMonths, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[5] : null);
               }

               var19 = DateFormatSymbols.CapitalizationContextUsage.MONTH_NARROW;
            } else if (var3 == 4) {
               if (var15 == 2) {
                  safeAppendWithMonthPattern(this.formatData.months, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[0] : null);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
               } else {
                  safeAppendWithMonthPattern(this.formatData.standaloneMonths, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[3] : null);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
               }
            } else if (var3 == 3) {
               if (var15 == 2) {
                  safeAppendWithMonthPattern(this.formatData.shortMonths, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[1] : null);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
               } else {
                  safeAppendWithMonthPattern(this.formatData.standaloneShortMonths, var17, var1, var23 != 0 ? this.formatData.leapMonthPatterns[4] : null);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
               }
            } else {
               StringBuffer var24 = new StringBuffer();
               this.zeroPaddingNumber(var18, var24, var17 + 1, var3, Integer.MAX_VALUE);
               String[] var22 = new String[]{var24.toString()};
               safeAppendWithMonthPattern(var22, 0, var1, var23 != 0 ? this.formatData.leapMonthPatterns[6] : null);
            }
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         case 10:
         case 11:
         case 12:
         case 13:
         case 16:
         case 20:
         case 21:
         case 22:
         default:
            this.zeroPaddingNumber(var18, var1, var17, var3, Integer.MAX_VALUE);
            break;
         case 4:
            if (var17 == 0) {
               this.zeroPaddingNumber(var18, var1, var8.getMaximum(11) + 1, var3, Integer.MAX_VALUE);
            } else {
               this.zeroPaddingNumber(var18, var1, var17, var3, Integer.MAX_VALUE);
            }
            break;
         case 8:
            this.numberFormat.setMinimumIntegerDigits(Math.min(3, var3));
            this.numberFormat.setMaximumIntegerDigits(Integer.MAX_VALUE);
            if (var3 == 1) {
               var17 /= 100;
            } else if (var3 == 2) {
               var17 /= 10;
            }

            FieldPosition var21 = new FieldPosition(-1);
            this.numberFormat.format((long)var17, var1, var21);
            if (var3 > 3) {
               this.numberFormat.setMinimumIntegerDigits(var3 - 3);
               this.numberFormat.format(0L, var1, var21);
            }
            break;
         case 14:
            safeAppend(this.formatData.ampms, var17, var1);
            break;
         case 15:
            if (var17 == 0) {
               this.zeroPaddingNumber(var18, var1, var8.getLeastMaximum(10) + 1, var3, Integer.MAX_VALUE);
            } else {
               this.zeroPaddingNumber(var18, var1, var17, var3, Integer.MAX_VALUE);
            }
            break;
         case 17:
            if (var3 < 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.SPECIFIC_SHORT, var11, var12);
               var19 = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
            } else {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.SPECIFIC_LONG, var11, var12);
               var19 = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
            }

            var1.append(var14);
            break;
         case 19:
            if (var3 < 3) {
               this.zeroPaddingNumber(var18, var1, var17, var3, Integer.MAX_VALUE);
               break;
            } else {
               var17 = var8.get(7);
            }
         case 9:
            if (var3 == 5) {
               safeAppend(this.formatData.narrowWeekdays, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
            } else if (var3 == 4) {
               safeAppend(this.formatData.weekdays, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
            } else if (var3 == 6 && this.formatData.shorterWeekdays != null) {
               safeAppend(this.formatData.shorterWeekdays, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
            } else {
               safeAppend(this.formatData.shortWeekdays, var17, var1);
               var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
            }
            break;
         case 23:
            if (var3 < 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, var11, var12);
            } else if (var3 == 5) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, var11, var12);
            } else {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, var11, var12);
            }

            var1.append(var14);
            break;
         case 24:
            if (var3 == 1) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_SHORT, var11, var12);
               var19 = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
            } else if (var3 == 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_LONG, var11, var12);
               var19 = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
            }

            var1.append(var14);
            break;
         case 25:
            if (var3 < 3) {
               this.zeroPaddingNumber(var18, var1, var17, 1, Integer.MAX_VALUE);
            } else {
               var17 = var8.get(7);
               if (var3 == 5) {
                  safeAppend(this.formatData.standaloneNarrowWeekdays, var17, var1);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
               } else if (var3 == 4) {
                  safeAppend(this.formatData.standaloneWeekdays, var17, var1);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
               } else if (var3 == 6 && this.formatData.standaloneShorterWeekdays != null) {
                  safeAppend(this.formatData.standaloneShorterWeekdays, var17, var1);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
               } else {
                  safeAppend(this.formatData.standaloneShortWeekdays, var17, var1);
                  var19 = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
               }
            }
            break;
         case 27:
            if (var3 >= 4) {
               safeAppend(this.formatData.quarters, var17 / 3, var1);
            } else if (var3 == 3) {
               safeAppend(this.formatData.shortQuarters, var17 / 3, var1);
            } else {
               this.zeroPaddingNumber(var18, var1, var17 / 3 + 1, var3, Integer.MAX_VALUE);
            }
            break;
         case 28:
            if (var3 >= 4) {
               safeAppend(this.formatData.standaloneQuarters, var17 / 3, var1);
            } else if (var3 == 3) {
               safeAppend(this.formatData.standaloneShortQuarters, var17 / 3, var1);
            } else {
               this.zeroPaddingNumber(var18, var1, var17 / 3 + 1, var3, Integer.MAX_VALUE);
            }
            break;
         case 29:
            if (var3 == 1) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ZONE_ID_SHORT, var11, var12);
            } else if (var3 == 2) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ZONE_ID, var11, var12);
            } else if (var3 == 3) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.EXEMPLAR_LOCATION, var11, var12);
            } else if (var3 == 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.GENERIC_LOCATION, var11, var12);
               var19 = DateFormatSymbols.CapitalizationContextUsage.ZONE_LONG;
            }

            var1.append(var14);
            break;
         case 30:
            if (this.formatData.shortYearNames != null && var17 <= this.formatData.shortYearNames.length) {
               safeAppend(this.formatData.shortYearNames, var17 - 1, var1);
               break;
            }
         case 1:
         case 18:
            if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && var17 > 5000 && var17 < 6000) {
               var17 -= 5000;
            }

            if (var3 == 2) {
               this.zeroPaddingNumber(var18, var1, var17, 2, 2);
            } else {
               this.zeroPaddingNumber(var18, var1, var17, var3, Integer.MAX_VALUE);
            }
            break;
         case 31:
            if (var3 == 1) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT_SHORT, var11, var12);
            } else if (var3 == 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, var11, var12);
            }

            var1.append(var14);
            break;
         case 32:
            if (var3 == 1) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_SHORT, var11, var12);
            } else if (var3 == 2) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FIXED, var11, var12);
            } else if (var3 == 3) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FIXED, var11, var12);
            } else if (var3 == 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FULL, var11, var12);
            } else if (var3 == 5) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, var11, var12);
            }

            var1.append(var14);
            break;
         case 33:
            if (var3 == 1) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT, var11, var12);
            } else if (var3 == 2) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED, var11, var12);
            } else if (var3 == 3) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED, var11, var12);
            } else if (var3 == 4) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, var11, var12);
            } else if (var3 == 5) {
               var14 = this.tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL, var11, var12);
            }

            var1.append(var14);
         }

         if (var5 == 0) {
            var20 = false;
            if (var6 != null) {
               switch(var6) {
               case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE:
                  var20 = true;
                  break;
               case CAPITALIZATION_FOR_UI_LIST_OR_MENU:
               case CAPITALIZATION_FOR_STANDALONE:
                  if (this.formatData.capitalization != null) {
                     boolean[] var25 = (boolean[])this.formatData.capitalization.get(var19);
                     var20 = var6 == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU ? var25[0] : var25[1];
                  }
               }
            }

            if (var20) {
               String var26 = var1.substring(var10);
               String var27 = UCharacter.toTitleCase(this.locale, var26, (BreakIterator)null, 768);
               var1.replace(var10, var1.length(), var27);
            }
         }

         if (var7.getBeginIndex() == var7.getEndIndex()) {
            if (var7.getField() == PATTERN_INDEX_TO_DATE_FORMAT_FIELD[var15]) {
               var7.setBeginIndex(var4);
               var7.setEndIndex(var4 + var1.length() - var10);
            } else if (var7.getFieldAttribute() == PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[var15]) {
               var7.setBeginIndex(var4);
               var7.setEndIndex(var4 + var1.length() - var10);
            }
         }

      }
   }

   private static void safeAppend(String[] var0, int var1, StringBuffer var2) {
      if (var0 != null && var1 >= 0 && var1 < var0.length) {
         var2.append(var0[var1]);
      }

   }

   private static void safeAppendWithMonthPattern(String[] var0, int var1, StringBuffer var2, String var3) {
      if (var0 != null && var1 >= 0 && var1 < var0.length) {
         if (var3 == null) {
            var2.append(var0[var1]);
         } else {
            var2.append(MessageFormat.format(var3, var0[var1]));
         }
      }

   }

   private Object[] getPatternItems() {
      if (this.patternItems != null) {
         return this.patternItems;
      } else {
         this.patternItems = (Object[])PARSED_PATTERN_CACHE.get(this.pattern);
         if (this.patternItems != null) {
            return this.patternItems;
         } else {
            boolean var1 = false;
            boolean var2 = false;
            StringBuilder var3 = new StringBuilder();
            char var4 = 0;
            int var5 = 1;
            ArrayList var6 = new ArrayList();

            for(int var7 = 0; var7 < this.pattern.length(); ++var7) {
               char var8 = this.pattern.charAt(var7);
               if (var8 == '\'') {
                  if (var1) {
                     var3.append('\'');
                     var1 = false;
                  } else {
                     var1 = true;
                     if (var4 != 0) {
                        var6.add(new SimpleDateFormat.PatternItem(var4, var5));
                        var4 = 0;
                     }
                  }

                  var2 = !var2;
               } else {
                  var1 = false;
                  if (var2) {
                     var3.append(var8);
                  } else if ((var8 < 'a' || var8 > 'z') && (var8 < 'A' || var8 > 'Z')) {
                     if (var4 != 0) {
                        var6.add(new SimpleDateFormat.PatternItem(var4, var5));
                        var4 = 0;
                     }

                     var3.append(var8);
                  } else if (var8 == var4) {
                     ++var5;
                  } else {
                     if (var4 == 0) {
                        if (var3.length() > 0) {
                           var6.add(var3.toString());
                           var3.setLength(0);
                        }
                     } else {
                        var6.add(new SimpleDateFormat.PatternItem(var4, var5));
                     }

                     var4 = var8;
                     var5 = 1;
                  }
               }
            }

            if (var4 == 0) {
               if (var3.length() > 0) {
                  var6.add(var3.toString());
                  var3.setLength(0);
               }
            } else {
               var6.add(new SimpleDateFormat.PatternItem(var4, var5));
            }

            this.patternItems = var6.toArray(new Object[var6.size()]);
            PARSED_PATTERN_CACHE.put(this.pattern, this.patternItems);
            return this.patternItems;
         }
      }
   }

   /** @deprecated */
   protected void zeroPaddingNumber(NumberFormat var1, StringBuffer var2, int var3, int var4, int var5) {
      if (this.useLocalZeroPaddingNumberFormat && var3 >= 0) {
         this.fastZeroPaddingNumber(var2, var3, var4, var5);
      } else {
         var1.setMinimumIntegerDigits(var4);
         var1.setMaximumIntegerDigits(var5);
         var1.format((long)var3, var2, new FieldPosition(-1));
      }

   }

   public void setNumberFormat(NumberFormat var1) {
      super.setNumberFormat(var1);
      this.initLocalZeroPaddingNumberFormat();
      this.initializeTimeZoneFormat(true);
   }

   private void initLocalZeroPaddingNumberFormat() {
      if (this.numberFormat instanceof DecimalFormat) {
         this.decDigits = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getDigits();
         this.useLocalZeroPaddingNumberFormat = true;
      } else if (this.numberFormat instanceof DateNumberFormat) {
         this.decDigits = ((DateNumberFormat)this.numberFormat).getDigits();
         this.useLocalZeroPaddingNumberFormat = true;
      } else {
         this.useLocalZeroPaddingNumberFormat = false;
      }

      if (this.useLocalZeroPaddingNumberFormat) {
         this.decimalBuf = new char[10];
      }

   }

   private void fastZeroPaddingNumber(StringBuffer var1, int var2, int var3, int var4) {
      int var5 = this.decimalBuf.length < var4 ? this.decimalBuf.length : var4;
      int var6 = var5 - 1;

      while(true) {
         this.decimalBuf[var6] = this.decDigits[var2 % 10];
         var2 /= 10;
         if (var6 == 0 || var2 == 0) {
            int var7;
            for(var7 = var3 - (var5 - var6); var7 > 0 && var6 > 0; --var7) {
               --var6;
               this.decimalBuf[var6] = this.decDigits[0];
            }

            while(var7 > 0) {
               var1.append(this.decDigits[0]);
               --var7;
            }

            var1.append(this.decimalBuf, var6, var5 - var6);
            return;
         }

         --var6;
      }
   }

   protected String zeroPaddingNumber(long var1, int var3, int var4) {
      this.numberFormat.setMinimumIntegerDigits(var3);
      this.numberFormat.setMaximumIntegerDigits(var4);
      return this.numberFormat.format(var1);
   }

   private static final boolean isNumeric(char var0, int var1) {
      int var2 = "MYyudehHmsSDFwWkK".indexOf(var0);
      return var2 > 0 || var2 == 0 && var1 < 3;
   }

   public void parse(String var1, Calendar var2, ParsePosition var3) {
      TimeZone var4 = null;
      Calendar var5 = null;
      if (var2 != this.calendar && !var2.getType().equals(this.calendar.getType())) {
         this.calendar.setTimeInMillis(var2.getTimeInMillis());
         var4 = this.calendar.getTimeZone();
         this.calendar.setTimeZone(var2.getTimeZone());
         var5 = var2;
         var2 = this.calendar;
      }

      int var6 = var3.getIndex();
      int var7 = var6;
      this.tztype = TimeZoneFormat.TimeType.UNKNOWN;
      boolean[] var8 = new boolean[]{false};
      int var9 = -1;
      int var10 = 0;
      int var11 = 0;
      MessageFormat var12 = null;
      if (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7) {
         var12 = new MessageFormat(this.formatData.leapMonthPatterns[6], this.locale);
      }

      Object[] var13 = this.getPatternItems();
      int var14 = 0;

      while(true) {
         while(var14 < var13.length) {
            if (var13[var14] instanceof SimpleDateFormat.PatternItem) {
               SimpleDateFormat.PatternItem var15 = (SimpleDateFormat.PatternItem)var13[var14];
               if (var15.isNumeric && var9 == -1 && var14 + 1 < var13.length && var13[var14 + 1] instanceof SimpleDateFormat.PatternItem && ((SimpleDateFormat.PatternItem)var13[var14 + 1]).isNumeric) {
                  var9 = var14;
                  var10 = var15.length;
                  var11 = var6;
               }

               int var16;
               if (var9 != -1) {
                  var16 = var15.length;
                  if (var9 == var14) {
                     var16 = var10;
                  }

                  var6 = this.subParse(var1, var6, var15.type, var16, true, false, var8, var2, var12);
                  if (var6 < 0) {
                     --var10;
                     if (var10 == 0) {
                        var3.setIndex(var7);
                        var3.setErrorIndex(var6);
                        if (var4 != null) {
                           this.calendar.setTimeZone(var4);
                        }

                        return;
                     }

                     var14 = var9;
                     var6 = var11;
                     continue;
                  }
               } else if (var15.type != 'l') {
                  var9 = -1;
                  var16 = var6;
                  var6 = this.subParse(var1, var6, var15.type, var15.length, false, true, var8, var2, var12);
                  if (var6 < 0) {
                     if (var6 != -32000) {
                        var3.setIndex(var7);
                        var3.setErrorIndex(var16);
                        if (var4 != null) {
                           this.calendar.setTimeZone(var4);
                        }

                        return;
                     }

                     var6 = var16;
                     if (var14 + 1 < var13.length) {
                        String var17 = null;

                        try {
                           var17 = (String)var13[var14 + 1];
                        } catch (ClassCastException var32) {
                           var3.setIndex(var7);
                           var3.setErrorIndex(var16);
                           if (var4 != null) {
                              this.calendar.setTimeZone(var4);
                           }

                           return;
                        }

                        if (var17 == null) {
                           var17 = (String)var13[var14 + 1];
                        }

                        int var18 = var17.length();

                        int var19;
                        for(var19 = 0; var19 < var18; ++var19) {
                           char var20 = var17.charAt(var19);
                           if (!PatternProps.isWhiteSpace(var20)) {
                              break;
                           }
                        }

                        if (var19 == var18) {
                           ++var14;
                        }
                     }
                  }
               }
            } else {
               var9 = -1;
               boolean[] var34 = new boolean[1];
               var6 = this.matchLiteral(var1, var6, var13, var14, var34);
               if (!var34[0]) {
                  var3.setIndex(var7);
                  var3.setErrorIndex(var6);
                  if (var4 != null) {
                     this.calendar.setTimeZone(var4);
                  }

                  return;
               }
            }

            ++var14;
         }

         if (var6 < var1.length()) {
            char var35 = var1.charAt(var6);
            if (var35 == '.' && this.isLenient() && var13.length != 0) {
               Object var37 = var13[var13.length - 1];
               if (var37 instanceof SimpleDateFormat.PatternItem && !((SimpleDateFormat.PatternItem)var37).isNumeric) {
                  ++var6;
               }
            }
         }

         var3.setIndex(var6);

         try {
            if (var8[0] || this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
               Calendar var36;
               if (var8[0]) {
                  var36 = (Calendar)var2.clone();
                  Date var38 = var36.getTime();
                  if (var38.before(this.getDefaultCenturyStart())) {
                     var2.set(1, this.getDefaultCenturyStartYear() + 100);
                  }
               }

               if (this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
                  var36 = (Calendar)var2.clone();
                  TimeZone var39 = var36.getTimeZone();
                  BasicTimeZone var40 = null;
                  if (var39 instanceof BasicTimeZone) {
                     var40 = (BasicTimeZone)var39;
                  }

                  var36.set(15, 0);
                  var36.set(16, 0);
                  long var41 = var36.getTimeInMillis();
                  int[] var42 = new int[2];
                  if (var40 != null) {
                     if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
                        var40.getOffsetFromLocal(var41, 1, 1, var42);
                     } else {
                        var40.getOffsetFromLocal(var41, 3, 3, var42);
                     }
                  } else {
                     var39.getOffset(var41, true, var42);
                     if (this.tztype == TimeZoneFormat.TimeType.STANDARD && var42[1] != 0 || this.tztype == TimeZoneFormat.TimeType.DAYLIGHT && var42[1] == 0) {
                        var39.getOffset(var41 - 86400000L, true, var42);
                     }
                  }

                  int var21 = var42[1];
                  if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
                     if (var42[1] != 0) {
                        var21 = 0;
                     }
                  } else if (var42[1] == 0) {
                     if (var40 != null) {
                        long var22 = var41 + (long)var42[0];
                        long var26 = var22;
                        long var28 = var22;
                        int var30 = 0;
                        int var31 = 0;

                        TimeZoneTransition var24;
                        do {
                           var24 = var40.getPreviousTransition(var26, true);
                           if (var24 == null) {
                              break;
                           }

                           var26 = var24.getTime() - 1L;
                           var30 = var24.getFrom().getDSTSavings();
                        } while(var30 == 0);

                        TimeZoneTransition var25;
                        do {
                           var25 = var40.getNextTransition(var28, false);
                           if (var25 == null) {
                              break;
                           }

                           var28 = var25.getTime();
                           var31 = var25.getTo().getDSTSavings();
                        } while(var31 == 0);

                        if (var24 != null && var25 != null) {
                           if (var22 - var26 > var28 - var22) {
                              var21 = var31;
                           } else {
                              var21 = var30;
                           }
                        } else if (var24 != null && var30 != 0) {
                           var21 = var30;
                        } else if (var25 != null && var31 != 0) {
                           var21 = var31;
                        } else {
                           var21 = var40.getDSTSavings();
                        }
                     } else {
                        var21 = var39.getDSTSavings();
                     }

                     if (var21 == 0) {
                        var21 = 3600000;
                     }
                  }

                  var2.set(15, var42[0]);
                  var2.set(16, var21);
               }
            }
         } catch (IllegalArgumentException var33) {
            var3.setErrorIndex(var6);
            var3.setIndex(var7);
            if (var4 != null) {
               this.calendar.setTimeZone(var4);
            }

            return;
         }

         if (var5 != null) {
            var5.setTimeZone(var2.getTimeZone());
            var5.setTimeInMillis(var2.getTimeInMillis());
         }

         if (var4 != null) {
            this.calendar.setTimeZone(var4);
         }

         return;
      }
   }

   private int matchLiteral(String var1, int var2, Object[] var3, int var4, boolean[] var5) {
      int var6 = var2;
      String var7 = (String)var3[var4];
      int var8 = var7.length();
      int var9 = var1.length();
      int var10 = 0;

      while(var10 < var8 && var2 < var9) {
         char var11 = var7.charAt(var10);
         char var12 = var1.charAt(var2);
         if (PatternProps.isWhiteSpace(var11) && PatternProps.isWhiteSpace(var12)) {
            while(var10 + 1 < var8 && PatternProps.isWhiteSpace(var7.charAt(var10 + 1))) {
               ++var10;
            }

            while(var2 + 1 < var9 && PatternProps.isWhiteSpace(var1.charAt(var2 + 1))) {
               ++var2;
            }
         } else if (var11 != var12) {
            if (var12 != '.' || var2 != var6 || 0 >= var4 || !this.isLenient()) {
               break;
            }

            Object var13 = var3[var4 - 1];
            if (!(var13 instanceof SimpleDateFormat.PatternItem)) {
               break;
            }

            boolean var14 = ((SimpleDateFormat.PatternItem)var13).isNumeric;
            if (var14) {
               break;
            }

            ++var2;
            continue;
         }

         ++var10;
         ++var2;
      }

      var5[0] = var10 == var8;
      if (!var5[0] && this.isLenient() && 0 < var4 && var4 < var3.length - 1 && var6 < var9) {
         Object var17 = var3[var4 - 1];
         Object var18 = var3[var4 + 1];
         if (var17 instanceof SimpleDateFormat.PatternItem && var18 instanceof SimpleDateFormat.PatternItem) {
            char var19 = ((SimpleDateFormat.PatternItem)var17).type;
            char var20 = ((SimpleDateFormat.PatternItem)var18).type;
            if (DATE_PATTERN_TYPE.contains(var19) != DATE_PATTERN_TYPE.contains(var20)) {
               int var15 = var6;

               while(true) {
                  char var16 = var1.charAt(var15);
                  if (!PatternProps.isWhiteSpace(var16)) {
                     var5[0] = var15 > var6;
                     var2 = var15;
                     break;
                  }

                  ++var15;
               }
            }
         }
      }

      return var2;
   }

   protected int matchString(String var1, int var2, int var3, String[] var4, Calendar var5) {
      return this.matchString(var1, var2, var3, var4, (String)null, var5);
   }

   protected int matchString(String var1, int var2, int var3, String[] var4, String var5, Calendar var6) {
      int var7 = 0;
      int var8 = var4.length;
      if (var3 == 7) {
         var7 = 1;
      }

      int var9 = 0;
      int var10 = -1;
      byte var11 = 0;

      for(boolean var12 = false; var7 < var8; ++var7) {
         int var13 = var4[var7].length();
         int var15;
         if (var13 > var9 && (var15 = this.regionMatchesWithOptionalDot(var1, var2, var4[var7], var13)) >= 0) {
            var10 = var7;
            var9 = var15;
            var11 = 0;
         }

         if (var5 != null) {
            String var14 = MessageFormat.format(var5, var4[var7]);
            var13 = var14.length();
            if (var13 > var9 && (var15 = this.regionMatchesWithOptionalDot(var1, var2, var14, var13)) >= 0) {
               var10 = var7;
               var9 = var15;
               var11 = 1;
            }
         }
      }

      if (var10 >= 0) {
         if (var3 == 1) {
            ++var10;
         }

         var6.set(var3, var10);
         if (var5 != null) {
            var6.set(22, var11);
         }

         return var2 + var9;
      } else {
         return -var2;
      }
   }

   private int regionMatchesWithOptionalDot(String var1, int var2, String var3, int var4) {
      boolean var5 = var1.regionMatches(true, var2, var3, 0, var4);
      if (var5) {
         return var4;
      } else {
         return var3.length() > 0 && var3.charAt(var3.length() - 1) == '.' && var1.regionMatches(true, var2, var3, 0, var4 - 1) ? var4 - 1 : -1;
      }
   }

   protected int matchQuarterString(String var1, int var2, int var3, String[] var4, Calendar var5) {
      int var6 = 0;
      int var7 = var4.length;
      int var8 = 0;
      int var9 = -1;

      for(boolean var10 = false; var6 < var7; ++var6) {
         int var11 = var4[var6].length();
         int var12;
         if (var11 > var8 && (var12 = this.regionMatchesWithOptionalDot(var1, var2, var4[var6], var11)) >= 0) {
            var9 = var6;
            var8 = var12;
         }
      }

      if (var9 >= 0) {
         var5.set(var3, var9 * 3);
         return var2 + var8;
      } else {
         return -var2;
      }
   }

   protected int subParse(String var1, int var2, char var3, int var4, boolean var5, boolean var6, boolean[] var7, Calendar var8) {
      return this.subParse(var1, var2, var3, var4, var5, var6, var7, var8, (MessageFormat)null);
   }

   protected int subParse(String var1, int var2, char var3, int var4, boolean var5, boolean var6, boolean[] var7, Calendar var8, MessageFormat var9) {
      Number var10 = null;
      NumberFormat var11 = null;
      int var12 = 0;
      ParsePosition var14 = new ParsePosition(0);
      boolean var15 = this.isLenient();
      int var16 = -1;
      if ('A' <= var3 && var3 <= 'z') {
         var16 = PATTERN_CHAR_TO_INDEX[var3 - 64];
      }

      if (var16 == -1) {
         return -var2;
      } else {
         var11 = this.getNumberFormat(var3);
         int var17 = PATTERN_INDEX_TO_CALENDAR_FIELD[var16];
         if (var9 != null) {
            var9.setFormatByArgumentIndex(0, var11);
         }

         while(var2 < var1.length()) {
            int var18 = UTF16.charAt(var1, var2);
            if (!UCharacter.isUWhiteSpace(var18) || !PatternProps.isWhiteSpace(var18)) {
               var14.setIndex(var2);
               boolean var22;
               if (var16 == 4 || var16 == 15 || var16 == 2 && var4 <= 2 || var16 == 26 && var4 <= 2 || var16 == 1 || var16 == 18 || var16 == 30 || var16 == 0 && var8.getType().equals("chinese") || var16 == 8) {
                  var22 = false;
                  if (var9 != null && (var16 == 2 || var16 == 26)) {
                     Object[] var19 = var9.parse(var1, var14);
                     if (var19 != null && var14.getIndex() > var2 && var19[0] instanceof Number) {
                        var22 = true;
                        var10 = (Number)var19[0];
                        var8.set(22, 1);
                     } else {
                        var14.setIndex(var2);
                        var8.set(22, 0);
                     }
                  }

                  if (!var22) {
                     if (var5) {
                        if (var2 + var4 > var1.length()) {
                           return -var2;
                        }

                        var10 = this.parseInt(var1, var4, var14, var6, var11);
                     } else {
                        var10 = this.parseInt(var1, var14, var6, var11);
                     }

                     if (var10 == null && var16 != 30) {
                        return -var2;
                     }
                  }

                  if (var10 != null) {
                     var12 = var10.intValue();
                  }
               }

               TimeZoneFormat.Style var20;
               TimeZone var21;
               Output var23;
               int var24;
               switch(var16) {
               case 0:
                  if (var8.getType().equals("chinese")) {
                     var8.set(0, var12);
                     return var14.getIndex();
                  }

                  var22 = false;
                  if (var4 == 5) {
                     var18 = this.matchString(var1, var2, 0, this.formatData.narrowEras, (String)null, var8);
                  } else if (var4 == 4) {
                     var18 = this.matchString(var1, var2, 0, this.formatData.eraNames, (String)null, var8);
                  } else {
                     var18 = this.matchString(var1, var2, 0, this.formatData.eras, (String)null, var8);
                  }

                  if (var18 == -var2) {
                     var18 = -32000;
                  }

                  return var18;
               case 1:
               case 18:
                  if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && var12 < 1000) {
                     var12 += 5000;
                  } else if (var4 == 2 && var14.getIndex() - var2 == 2 && !var8.getType().equals("chinese") && UCharacter.isDigit(var1.charAt(var2)) && UCharacter.isDigit(var1.charAt(var2 + 1))) {
                     var24 = this.getDefaultCenturyStartYear() % 100;
                     var7[0] = var12 == var24;
                     var12 += this.getDefaultCenturyStartYear() / 100 * 100 + (var12 < var24 ? 100 : 0);
                  }

                  var8.set(var17, var12);
                  if (DelayedHebrewMonthCheck) {
                     if (!HebrewCalendar.isLeapYear(var12)) {
                        var8.add(2, 1);
                     }

                     DelayedHebrewMonthCheck = false;
                  }

                  return var14.getIndex();
               case 2:
               case 26:
                  if (var4 <= 2) {
                     var8.set(2, var12 - 1);
                     if (var8.getType().equals("hebrew") && var12 >= 6) {
                        if (var8.isSet(1)) {
                           if (!HebrewCalendar.isLeapYear(var8.get(1))) {
                              var8.set(2, var12);
                           }
                        } else {
                           DelayedHebrewMonthCheck = true;
                        }
                     }

                     return var14.getIndex();
                  }

                  boolean var25 = this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7;
                  int var26 = var16 == 2 ? this.matchString(var1, var2, 2, this.formatData.months, var25 ? this.formatData.leapMonthPatterns[0] : null, var8) : this.matchString(var1, var2, 2, this.formatData.standaloneMonths, var25 ? this.formatData.leapMonthPatterns[3] : null, var8);
                  if (var26 > 0) {
                     return var26;
                  }

                  return var16 == 2 ? this.matchString(var1, var2, 2, this.formatData.shortMonths, var25 ? this.formatData.leapMonthPatterns[1] : null, var8) : this.matchString(var1, var2, 2, this.formatData.standaloneShortMonths, var25 ? this.formatData.leapMonthPatterns[4] : null, var8);
               case 3:
               case 5:
               case 6:
               case 7:
               case 10:
               case 11:
               case 12:
               case 13:
               case 16:
               case 19:
               case 20:
               case 21:
               case 22:
               default:
                  if (var5) {
                     if (var2 + var4 > var1.length()) {
                        return -var2;
                     }

                     var10 = this.parseInt(var1, var4, var14, var6, var11);
                  } else {
                     var10 = this.parseInt(var1, var14, var6, var11);
                  }

                  if (var10 != null) {
                     var8.set(var17, var10.intValue());
                     return var14.getIndex();
                  }

                  return -var2;
               case 4:
                  if (var12 == var8.getMaximum(11) + 1) {
                     var12 = 0;
                  }

                  var8.set(11, var12);
                  return var14.getIndex();
               case 8:
                  int var13 = var14.getIndex() - var2;
                  if (var13 < 3) {
                     while(var13 < 3) {
                        var12 *= 10;
                        ++var13;
                     }
                  } else {
                     for(var24 = 1; var13 > 3; --var13) {
                        var24 *= 10;
                     }

                     var12 /= var24;
                  }

                  var8.set(14, var12);
                  return var14.getIndex();
               case 9:
                  var24 = this.matchString(var1, var2, 7, this.formatData.weekdays, (String)null, var8);
                  if (var24 > 0) {
                     return var24;
                  }

                  if ((var24 = this.matchString(var1, var2, 7, this.formatData.shortWeekdays, (String)null, var8)) > 0) {
                     return var24;
                  }

                  if (this.formatData.shorterWeekdays != null) {
                     return this.matchString(var1, var2, 7, this.formatData.shorterWeekdays, (String)null, var8);
                  }

                  return var24;
               case 14:
                  return this.matchString(var1, var2, 9, this.formatData.ampms, (String)null, var8);
               case 15:
                  if (var12 == var8.getLeastMaximum(10) + 1) {
                     var12 = 0;
                  }

                  var8.set(10, var12);
                  return var14.getIndex();
               case 17:
                  var23 = new Output();
                  var20 = var4 < 4 ? TimeZoneFormat.Style.SPECIFIC_SHORT : TimeZoneFormat.Style.SPECIFIC_LONG;
                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 23:
                  var23 = new Output();
                  var20 = var4 < 4 ? TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL : (var4 == 5 ? TimeZoneFormat.Style.ISO_EXTENDED_FULL : TimeZoneFormat.Style.LOCALIZED_GMT);
                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 24:
                  var23 = new Output();
                  var20 = var4 < 4 ? TimeZoneFormat.Style.GENERIC_SHORT : TimeZoneFormat.Style.GENERIC_LONG;
                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 25:
                  var24 = this.matchString(var1, var2, 7, this.formatData.standaloneWeekdays, (String)null, var8);
                  if (var24 > 0) {
                     return var24;
                  }

                  if ((var24 = this.matchString(var1, var2, 7, this.formatData.standaloneShortWeekdays, (String)null, var8)) > 0) {
                     return var24;
                  }

                  if (this.formatData.standaloneShorterWeekdays != null) {
                     return this.matchString(var1, var2, 7, this.formatData.standaloneShorterWeekdays, (String)null, var8);
                  }

                  return var24;
               case 27:
                  if (var4 <= 2) {
                     var8.set(2, (var12 - 1) * 3);
                     return var14.getIndex();
                  }

                  var24 = this.matchQuarterString(var1, var2, 2, this.formatData.quarters, var8);
                  if (var24 > 0) {
                     return var24;
                  }

                  return this.matchQuarterString(var1, var2, 2, this.formatData.shortQuarters, var8);
               case 28:
                  if (var4 <= 2) {
                     var8.set(2, (var12 - 1) * 3);
                     return var14.getIndex();
                  }

                  var24 = this.matchQuarterString(var1, var2, 2, this.formatData.standaloneQuarters, var8);
                  if (var24 > 0) {
                     return var24;
                  }

                  return this.matchQuarterString(var1, var2, 2, this.formatData.standaloneShortQuarters, var8);
               case 29:
                  var23 = new Output();
                  var20 = null;
                  switch(var4) {
                  case 1:
                     var20 = TimeZoneFormat.Style.ZONE_ID_SHORT;
                     break;
                  case 2:
                     var20 = TimeZoneFormat.Style.ZONE_ID;
                     break;
                  case 3:
                     var20 = TimeZoneFormat.Style.EXEMPLAR_LOCATION;
                     break;
                  default:
                     var20 = TimeZoneFormat.Style.GENERIC_LOCATION;
                  }

                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 30:
                  if (this.formatData.shortYearNames != null) {
                     var24 = this.matchString(var1, var2, 1, this.formatData.shortYearNames, (String)null, var8);
                     if (var24 > 0) {
                        return var24;
                     }
                  }

                  if (var10 == null || !var15 && this.formatData.shortYearNames != null && var12 <= this.formatData.shortYearNames.length) {
                     return -var2;
                  }

                  var8.set(1, var12);
                  return var14.getIndex();
               case 31:
                  var23 = new Output();
                  var20 = var4 < 4 ? TimeZoneFormat.Style.LOCALIZED_GMT_SHORT : TimeZoneFormat.Style.LOCALIZED_GMT;
                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 32:
                  var23 = new Output();
                  switch(var4) {
                  case 1:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_SHORT;
                     break;
                  case 2:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_FIXED;
                     break;
                  case 3:
                     var20 = TimeZoneFormat.Style.ISO_EXTENDED_FIXED;
                     break;
                  case 4:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_FULL;
                     break;
                  default:
                     var20 = TimeZoneFormat.Style.ISO_EXTENDED_FULL;
                  }

                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               case 33:
                  var23 = new Output();
                  switch(var4) {
                  case 1:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT;
                     break;
                  case 2:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED;
                     break;
                  case 3:
                     var20 = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED;
                     break;
                  case 4:
                     var20 = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL;
                     break;
                  default:
                     var20 = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL;
                  }

                  var21 = this.tzFormat().parse(var20, var1, var14, var23);
                  if (var21 != null) {
                     this.tztype = (TimeZoneFormat.TimeType)var23.value;
                     var8.setTimeZone(var21);
                     return var14.getIndex();
                  }

                  return -var2;
               }
            }

            var2 += UTF16.getCharCount(var18);
         }

         return -var2;
      }
   }

   private Number parseInt(String var1, ParsePosition var2, boolean var3, NumberFormat var4) {
      return this.parseInt(var1, -1, var2, var3, var4);
   }

   private Number parseInt(String var1, int var2, ParsePosition var3, boolean var4, NumberFormat var5) {
      int var7 = var3.getIndex();
      Object var6;
      if (var4) {
         var6 = var5.parse(var1, var3);
      } else if (var5 instanceof DecimalFormat) {
         String var8 = ((DecimalFormat)var5).getNegativePrefix();
         ((DecimalFormat)var5).setNegativePrefix("\uab00");
         var6 = var5.parse(var1, var3);
         ((DecimalFormat)var5).setNegativePrefix(var8);
      } else {
         boolean var11 = var5 instanceof DateNumberFormat;
         if (var11) {
            ((DateNumberFormat)var5).setParsePositiveOnly(true);
         }

         var6 = var5.parse(var1, var3);
         if (var11) {
            ((DateNumberFormat)var5).setParsePositiveOnly(false);
         }
      }

      if (var2 > 0) {
         int var12 = var3.getIndex() - var7;
         if (var12 > var2) {
            double var9 = ((Number)var6).doubleValue();

            for(var12 -= var2; var12 > 0; --var12) {
               var9 /= 10.0D;
            }

            var3.setIndex(var7 + var2);
            var6 = (int)var9;
         }
      }

      return (Number)var6;
   }

   private String translatePattern(String var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder();
      boolean var5 = false;

      for(int var6 = 0; var6 < var1.length(); ++var6) {
         char var7 = var1.charAt(var6);
         if (var5) {
            if (var7 == '\'') {
               var5 = false;
            }
         } else if (var7 == '\'') {
            var5 = true;
         } else if (var7 >= 'a' && var7 <= 'z' || var7 >= 'A' && var7 <= 'Z') {
            int var8 = var2.indexOf(var7);
            if (var8 != -1) {
               var7 = var3.charAt(var8);
            }
         }

         var4.append(var7);
      }

      if (var5) {
         throw new IllegalArgumentException("Unfinished quote in pattern");
      } else {
         return var4.toString();
      }
   }

   public String toPattern() {
      return this.pattern;
   }

   public String toLocalizedPattern() {
      return this.translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx", this.formatData.localPatternChars);
   }

   public void applyPattern(String var1) {
      this.pattern = var1;
      this.setLocale((ULocale)null, (ULocale)null);
      this.patternItems = null;
   }

   public void applyLocalizedPattern(String var1) {
      this.pattern = this.translatePattern(var1, this.formatData.localPatternChars, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx");
      this.setLocale((ULocale)null, (ULocale)null);
   }

   public DateFormatSymbols getDateFormatSymbols() {
      return (DateFormatSymbols)this.formatData.clone();
   }

   public void setDateFormatSymbols(DateFormatSymbols var1) {
      this.formatData = (DateFormatSymbols)var1.clone();
   }

   protected DateFormatSymbols getSymbols() {
      return this.formatData;
   }

   public TimeZoneFormat getTimeZoneFormat() {
      return this.tzFormat().freeze();
   }

   public void setTimeZoneFormat(TimeZoneFormat var1) {
      if (var1.isFrozen()) {
         this.tzFormat = var1;
      } else {
         this.tzFormat = var1.cloneAsThawed().freeze();
      }

   }

   public void setContext(DisplayContext var1) {
      if (var1.type() == DisplayContext.Type.CAPITALIZATION) {
         this.capitalizationSetting = var1;
      }

   }

   public DisplayContext getContext(DisplayContext.Type var1) {
      return var1 == DisplayContext.Type.CAPITALIZATION && this.capitalizationSetting != null ? this.capitalizationSetting : DisplayContext.CAPITALIZATION_NONE;
   }

   public Object clone() {
      SimpleDateFormat var1 = (SimpleDateFormat)super.clone();
      var1.formatData = (DateFormatSymbols)this.formatData.clone();
      return var1;
   }

   public int hashCode() {
      return this.pattern.hashCode();
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         SimpleDateFormat var2 = (SimpleDateFormat)var1;
         return this.pattern.equals(var2.pattern) && this.formatData.equals(var2.formatData);
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (this.defaultCenturyStart == null) {
         this.initializeDefaultCenturyStart(this.defaultCenturyBase);
      }

      this.initializeTimeZoneFormat(false);
      var1.defaultWriteObject();
      var1.writeInt(this.capitalizationSetting.value());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = this.serialVersionOnStream > 1 ? var1.readInt() : -1;
      if (this.serialVersionOnStream < 1) {
         this.defaultCenturyBase = System.currentTimeMillis();
      } else {
         this.parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
      }

      this.serialVersionOnStream = 2;
      this.locale = this.getLocale(ULocale.VALID_LOCALE);
      if (this.locale == null) {
         this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
      }

      this.initLocalZeroPaddingNumberFormat();
      this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
      if (var2 >= 0) {
         DisplayContext[] var3 = DisplayContext.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            DisplayContext var6 = var3[var5];
            if (var6.value() == var2) {
               this.capitalizationSetting = var6;
               break;
            }
         }
      }

   }

   public AttributedCharacterIterator formatToCharacterIterator(Object var1) {
      Calendar var2 = this.calendar;
      if (var1 instanceof Calendar) {
         var2 = (Calendar)var1;
      } else if (var1 instanceof Date) {
         this.calendar.setTime((Date)var1);
      } else {
         if (!(var1 instanceof Number)) {
            throw new IllegalArgumentException("Cannot format given Object as a Date");
         }

         this.calendar.setTimeInMillis(((Number)var1).longValue());
      }

      StringBuffer var3 = new StringBuffer();
      FieldPosition var4 = new FieldPosition(0);
      ArrayList var5 = new ArrayList();
      this.format(var2, this.capitalizationSetting, var3, var4, var5);
      AttributedString var6 = new AttributedString(var3.toString());

      for(int var7 = 0; var7 < var5.size(); ++var7) {
         FieldPosition var8 = (FieldPosition)var5.get(var7);
         java.text.Format.Field var9 = var8.getFieldAttribute();
         var6.addAttribute(var9, var9, var8.getBeginIndex(), var8.getEndIndex());
      }

      return var6.getIterator();
   }

   ULocale getLocale() {
      return this.locale;
   }

   boolean isFieldUnitIgnored(int var1) {
      return isFieldUnitIgnored(this.pattern, var1);
   }

   static boolean isFieldUnitIgnored(String var0, int var1) {
      int var2 = CALENDAR_FIELD_TO_LEVEL[var1];
      boolean var5 = false;
      char var6 = 0;
      int var7 = 0;

      int var3;
      for(int var8 = 0; var8 < var0.length(); ++var8) {
         char var4 = var0.charAt(var8);
         if (var4 != var6 && var7 > 0) {
            var3 = PATTERN_CHAR_TO_LEVEL[var6 - 64];
            if (var2 <= var3) {
               return false;
            }

            var7 = 0;
         }

         if (var4 == '\'') {
            if (var8 + 1 < var0.length() && var0.charAt(var8 + 1) == '\'') {
               ++var8;
            } else {
               var5 = !var5;
            }
         } else if (!var5 && (var4 >= 'a' && var4 <= 'z' || var4 >= 'A' && var4 <= 'Z')) {
            var6 = var4;
            ++var7;
         }
      }

      if (var7 > 0) {
         var3 = PATTERN_CHAR_TO_LEVEL[var6 - 64];
         if (var2 <= var3) {
            return false;
         }
      }

      return true;
   }

   /** @deprecated */
   public final StringBuffer intervalFormatByAlgorithm(Calendar var1, Calendar var2, StringBuffer var3, FieldPosition var4) throws IllegalArgumentException {
      if (!var1.isEquivalentTo(var2)) {
         throw new IllegalArgumentException("can not format on two different calendars");
      } else {
         Object[] var5 = this.getPatternItems();
         int var6 = -1;
         int var7 = -1;

         int var8;
         try {
            for(var8 = 0; var8 < var5.length; ++var8) {
               if (var8 != 0) {
                  var6 = var8;
                  break;
               }
            }

            if (var6 == -1) {
               return this.format(var1, var3, var4);
            }

            for(var8 = var5.length - 1; var8 >= var6; --var8) {
               if (var8 != 0) {
                  var7 = var8;
                  break;
               }
            }
         } catch (IllegalArgumentException var14) {
            throw new IllegalArgumentException(var14.toString());
         }

         if (var6 == 0 && var7 == var5.length - 1) {
            this.format(var1, var3, var4);
            var3.append(" – ");
            this.format(var2, var3, var4);
            return var3;
         } else {
            var8 = 1000;

            int var9;
            SimpleDateFormat.PatternItem var10;
            for(var9 = var6; var9 <= var7; ++var9) {
               if (!(var5[var9] instanceof String)) {
                  var10 = (SimpleDateFormat.PatternItem)var5[var9];
                  char var11 = var10.type;
                  int var12 = -1;
                  if ('A' <= var11 && var11 <= 'z') {
                     var12 = PATTERN_CHAR_TO_LEVEL[var11 - 64];
                  }

                  if (var12 == -1) {
                     throw new IllegalArgumentException("Illegal pattern character '" + var11 + "' in \"" + this.pattern + '"');
                  }

                  if (var12 < var8) {
                     var8 = var12;
                  }
               }
            }

            try {
               for(var9 = 0; var9 < var6; ++var9) {
                  if (var8 != 0) {
                     var6 = var9;
                     break;
                  }
               }

               for(var9 = var5.length - 1; var9 > var7; --var9) {
                  if (var8 != 0) {
                     var7 = var9;
                     break;
                  }
               }
            } catch (IllegalArgumentException var13) {
               throw new IllegalArgumentException(var13.toString());
            }

            if (var6 == 0 && var7 == var5.length - 1) {
               this.format(var1, var3, var4);
               var3.append(" – ");
               this.format(var2, var3, var4);
               return var3;
            } else {
               var4.setBeginIndex(0);
               var4.setEndIndex(0);

               for(var9 = 0; var9 <= var7; ++var9) {
                  if (var5[var9] instanceof String) {
                     var3.append((String)var5[var9]);
                  } else {
                     var10 = (SimpleDateFormat.PatternItem)var5[var9];
                     if (this.useFastFormat) {
                        this.subFormat(var3, var10.type, var10.length, var3.length(), var9, this.capitalizationSetting, var4, var1);
                     } else {
                        var3.append(this.subFormat(var10.type, var10.length, var3.length(), var9, this.capitalizationSetting, var4, var1));
                     }
                  }
               }

               var3.append(" – ");

               for(var9 = var6; var9 < var5.length; ++var9) {
                  if (var5[var9] instanceof String) {
                     var3.append((String)var5[var9]);
                  } else {
                     var10 = (SimpleDateFormat.PatternItem)var5[var9];
                     if (this.useFastFormat) {
                        this.subFormat(var3, var10.type, var10.length, var3.length(), var9, this.capitalizationSetting, var4, var2);
                     } else {
                        var3.append(this.subFormat(var10.type, var10.length, var3.length(), var9, this.capitalizationSetting, var4, var2));
                     }
                  }
               }

               return var3;
            }
         }
      }
   }

   /** @deprecated */
   protected NumberFormat getNumberFormat(char var1) {
      Character var2 = var1;
      if (this.overrideMap != null && this.overrideMap.containsKey(var2)) {
         String var3 = ((String)this.overrideMap.get(var2)).toString();
         NumberFormat var4 = (NumberFormat)this.numberFormatters.get(var3);
         return var4;
      } else {
         return this.numberFormat;
      }
   }

   private void initNumberFormatters(ULocale var1) {
      this.numberFormatters = new HashMap();
      this.overrideMap = new HashMap();
      this.processOverrideString(var1, this.override);
   }

   private void processOverrideString(ULocale var1, String var2) {
      if (var2 != null && var2.length() != 0) {
         int var3 = 0;

         int var9;
         for(boolean var7 = true; var7; var3 = var9 + 1) {
            var9 = var2.indexOf(";", var3);
            int var4;
            if (var9 == -1) {
               var7 = false;
               var4 = var2.length();
            } else {
               var4 = var9;
            }

            String var10 = var2.substring(var3, var4);
            int var11 = var10.indexOf("=");
            String var5;
            boolean var8;
            if (var11 == -1) {
               var5 = var10;
               var8 = true;
            } else {
               var5 = var10.substring(var11 + 1);
               Character var6 = var10.charAt(0);
               this.overrideMap.put(var6, var5);
               var8 = false;
            }

            ULocale var12 = new ULocale(var1.getBaseName() + "@numbers=" + var5);
            NumberFormat var13 = NumberFormat.createInstance(var12, 0);
            var13.setGroupingUsed(false);
            if (var8) {
               this.setNumberFormat(var13);
            } else {
               this.useLocalZeroPaddingNumberFormat = false;
            }

            if (!this.numberFormatters.containsKey(var5)) {
               this.numberFormatters.put(var5, var13);
            }
         }

      }
   }

   static boolean access$000(char var0, int var1) {
      return isNumeric(var0, var1);
   }

   static {
      PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE = new DateFormat.Field[]{DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR_WOY, DateFormat.Field.DOW_LOCAL, DateFormat.Field.EXTENDED_YEAR, DateFormat.Field.JULIAN_DAY, DateFormat.Field.MILLISECONDS_IN_DAY, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.MONTH, DateFormat.Field.QUARTER, DateFormat.Field.QUARTER, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE};
      PARSED_PATTERN_CACHE = new SimpleCache();
      DATE_PATTERN_TYPE = (new UnicodeSet("[GyYuUQqMLlwWd]")).freeze();
   }

   private static class PatternItem {
      final char type;
      final int length;
      final boolean isNumeric;

      PatternItem(char var1, int var2) {
         this.type = var1;
         this.length = var2;
         this.isNumeric = SimpleDateFormat.access$000(var1, var2);
      }
   }

   private static enum ContextValue {
      UNKNOWN,
      CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE,
      CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE,
      CAPITALIZATION_FOR_UI_LIST_OR_MENU,
      CAPITALIZATION_FOR_STANDALONE;

      private static final SimpleDateFormat.ContextValue[] $VALUES = new SimpleDateFormat.ContextValue[]{UNKNOWN, CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE, CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE, CAPITALIZATION_FOR_UI_LIST_OR_MENU, CAPITALIZATION_FOR_STANDALONE};
   }
}
