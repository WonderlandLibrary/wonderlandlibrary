package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DateFormatSymbols;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.SimpleDateFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

public abstract class Calendar implements Serializable, Cloneable, Comparable {
   public static final int ERA = 0;
   public static final int YEAR = 1;
   public static final int MONTH = 2;
   public static final int WEEK_OF_YEAR = 3;
   public static final int WEEK_OF_MONTH = 4;
   public static final int DATE = 5;
   public static final int DAY_OF_MONTH = 5;
   public static final int DAY_OF_YEAR = 6;
   public static final int DAY_OF_WEEK = 7;
   public static final int DAY_OF_WEEK_IN_MONTH = 8;
   public static final int AM_PM = 9;
   public static final int HOUR = 10;
   public static final int HOUR_OF_DAY = 11;
   public static final int MINUTE = 12;
   public static final int SECOND = 13;
   public static final int MILLISECOND = 14;
   public static final int ZONE_OFFSET = 15;
   public static final int DST_OFFSET = 16;
   public static final int YEAR_WOY = 17;
   public static final int DOW_LOCAL = 18;
   public static final int EXTENDED_YEAR = 19;
   public static final int JULIAN_DAY = 20;
   public static final int MILLISECONDS_IN_DAY = 21;
   public static final int IS_LEAP_MONTH = 22;
   protected static final int BASE_FIELD_COUNT = 23;
   protected static final int MAX_FIELD_COUNT = 32;
   public static final int SUNDAY = 1;
   public static final int MONDAY = 2;
   public static final int TUESDAY = 3;
   public static final int WEDNESDAY = 4;
   public static final int THURSDAY = 5;
   public static final int FRIDAY = 6;
   public static final int SATURDAY = 7;
   public static final int JANUARY = 0;
   public static final int FEBRUARY = 1;
   public static final int MARCH = 2;
   public static final int APRIL = 3;
   public static final int MAY = 4;
   public static final int JUNE = 5;
   public static final int JULY = 6;
   public static final int AUGUST = 7;
   public static final int SEPTEMBER = 8;
   public static final int OCTOBER = 9;
   public static final int NOVEMBER = 10;
   public static final int DECEMBER = 11;
   public static final int UNDECIMBER = 12;
   public static final int AM = 0;
   public static final int PM = 1;
   public static final int WEEKDAY = 0;
   public static final int WEEKEND = 1;
   public static final int WEEKEND_ONSET = 2;
   public static final int WEEKEND_CEASE = 3;
   public static final int WALLTIME_LAST = 0;
   public static final int WALLTIME_FIRST = 1;
   public static final int WALLTIME_NEXT_VALID = 2;
   protected static final int ONE_SECOND = 1000;
   protected static final int ONE_MINUTE = 60000;
   protected static final int ONE_HOUR = 3600000;
   protected static final long ONE_DAY = 86400000L;
   protected static final long ONE_WEEK = 604800000L;
   protected static final int JAN_1_1_JULIAN_DAY = 1721426;
   protected static final int EPOCH_JULIAN_DAY = 2440588;
   protected static final int MIN_JULIAN = -2130706432;
   protected static final long MIN_MILLIS = -184303902528000000L;
   protected static final Date MIN_DATE = new Date(-184303902528000000L);
   protected static final int MAX_JULIAN = 2130706432;
   protected static final long MAX_MILLIS = 183882168921600000L;
   protected static final Date MAX_DATE = new Date(183882168921600000L);
   private transient int[] fields;
   private transient int[] stamp;
   private long time;
   private transient boolean isTimeSet;
   private transient boolean areFieldsSet;
   private transient boolean areAllFieldsSet;
   private transient boolean areFieldsVirtuallySet;
   private boolean lenient;
   private TimeZone zone;
   private int firstDayOfWeek;
   private int minimalDaysInFirstWeek;
   private int weekendOnset;
   private int weekendOnsetMillis;
   private int weekendCease;
   private int weekendCeaseMillis;
   private int repeatedWallTime;
   private int skippedWallTime;
   private static ICUCache cachedLocaleData = new SimpleCache();
   protected static final int UNSET = 0;
   protected static final int INTERNALLY_SET = 1;
   protected static final int MINIMUM_USER_STAMP = 2;
   private transient int nextStamp;
   private static final long serialVersionUID = 6222646104888790989L;
   private transient int internalSetMask;
   private transient int gregorianYear;
   private transient int gregorianMonth;
   private transient int gregorianDayOfYear;
   private transient int gregorianDayOfMonth;
   private static int STAMP_MAX = 10000;
   private static final String[] calTypes = new String[]{"gregorian", "japanese", "buddhist", "roc", "persian", "islamic-civil", "islamic", "hebrew", "chinese", "indian", "coptic", "ethiopic", "ethiopic-amete-alem", "iso8601", "dangi"};
   private static final int CALTYPE_GREGORIAN = 0;
   private static final int CALTYPE_JAPANESE = 1;
   private static final int CALTYPE_BUDDHIST = 2;
   private static final int CALTYPE_ROC = 3;
   private static final int CALTYPE_PERSIAN = 4;
   private static final int CALTYPE_ISLAMIC_CIVIL = 5;
   private static final int CALTYPE_ISLAMIC = 6;
   private static final int CALTYPE_HEBREW = 7;
   private static final int CALTYPE_CHINESE = 8;
   private static final int CALTYPE_INDIAN = 9;
   private static final int CALTYPE_COPTIC = 10;
   private static final int CALTYPE_ETHIOPIC = 11;
   private static final int CALTYPE_ETHIOPIC_AMETE_ALEM = 12;
   private static final int CALTYPE_ISO8601 = 13;
   private static final int CALTYPE_DANGI = 14;
   private static final int CALTYPE_UNKNOWN = -1;
   private static Calendar.CalendarShim shim;
   private static final ICUCache PATTERN_CACHE = new SimpleCache();
   private static final String[] DEFAULT_PATTERNS = new String[]{"HH:mm:ss z", "HH:mm:ss z", "HH:mm:ss", "HH:mm", "EEEE, yyyy MMMM dd", "yyyy MMMM d", "yyyy MMM d", "yy/MM/dd", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}", "{1} {0}"};
   private static final char QUOTE = '\'';
   private static final int FIELD_DIFF_MAX_INT = Integer.MAX_VALUE;
   private static final int[][] LIMITS = new int[][]{new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], {1, 1, 7, 7}, new int[0], {0, 0, 1, 1}, {0, 0, 11, 11}, {0, 0, 23, 23}, {0, 0, 59, 59}, {0, 0, 59, 59}, {0, 0, 999, 999}, {-43200000, -43200000, 43200000, 43200000}, {0, 0, 3600000, 3600000}, new int[0], {1, 1, 7, 7}, new int[0], {-2130706432, -2130706432, 2130706432, 2130706432}, {0, 0, 86399999, 86399999}, {0, 0, 1, 1}};
   protected static final int MINIMUM = 0;
   protected static final int GREATEST_MINIMUM = 1;
   protected static final int LEAST_MAXIMUM = 2;
   protected static final int MAXIMUM = 3;
   protected static final int RESOLVE_REMAP = 32;
   static final int[][][] DATE_PRECEDENCE = new int[][][]{{{5}, {3, 7}, {4, 7}, {8, 7}, {3, 18}, {4, 18}, {8, 18}, {6}, {37, 1}, {35, 17}}, {{3}, {4}, {8}, {40, 7}, {40, 18}}};
   static final int[][][] DOW_PRECEDENCE = new int[][][]{{{7}, {18}}};
   private static final int[] FIND_ZONE_TRANSITION_TIME_UNITS = new int[]{3600000, 1800000, 60000, 1000};
   private static final int[][] GREGORIAN_MONTH_COUNT = new int[][]{{31, 31, 0, 0}, {28, 29, 31, 31}, {31, 31, 59, 60}, {30, 30, 90, 91}, {31, 31, 120, 121}, {30, 30, 151, 152}, {31, 31, 181, 182}, {31, 31, 212, 213}, {30, 30, 243, 244}, {31, 31, 273, 274}, {30, 30, 304, 305}, {31, 31, 334, 335}};
   private static final String[] FIELD_NAME = new String[]{"ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET", "YEAR_WOY", "DOW_LOCAL", "EXTENDED_YEAR", "JULIAN_DAY", "MILLISECONDS_IN_DAY"};
   private ULocale validLocale;
   private ULocale actualLocale;
   static final boolean $assertionsDisabled = !Calendar.class.desiredAssertionStatus();

   protected Calendar() {
      this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
   }

   protected Calendar(TimeZone var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   protected Calendar(TimeZone var1, ULocale var2) {
      this.lenient = true;
      this.repeatedWallTime = 0;
      this.skippedWallTime = 0;
      this.nextStamp = 2;
      this.zone = var1;
      this.setWeekData(var2);
      this.initInternal();
   }

   private void recalculateStamp() {
      this.nextStamp = 1;

      for(int var3 = 0; var3 < this.stamp.length; ++var3) {
         int var2 = STAMP_MAX;
         int var1 = -1;

         for(int var4 = 0; var4 < this.stamp.length; ++var4) {
            if (this.stamp[var4] > this.nextStamp && this.stamp[var4] < var2) {
               var2 = this.stamp[var4];
               var1 = var4;
            }
         }

         if (var1 < 0) {
            break;
         }

         this.stamp[var1] = ++this.nextStamp;
      }

      ++this.nextStamp;
   }

   private void initInternal() {
      this.fields = this.handleCreateFields();
      if (this.fields != null && this.fields.length >= 23 && this.fields.length <= 32) {
         this.stamp = new int[this.fields.length];
         int var1 = 4718695;

         for(int var2 = 23; var2 < this.fields.length; ++var2) {
            var1 |= 1 << var2;
         }

         this.internalSetMask = var1;
      } else {
         throw new IllegalStateException("Invalid fields[]");
      }
   }

   public static synchronized Calendar getInstance() {
      return getInstanceInternal((TimeZone)null, (ULocale)null);
   }

   public static synchronized Calendar getInstance(TimeZone var0) {
      return getInstanceInternal(var0, (ULocale)null);
   }

   public static synchronized Calendar getInstance(Locale var0) {
      return getInstanceInternal((TimeZone)null, ULocale.forLocale(var0));
   }

   public static synchronized Calendar getInstance(ULocale var0) {
      return getInstanceInternal((TimeZone)null, var0);
   }

   public static synchronized Calendar getInstance(TimeZone var0, Locale var1) {
      return getInstanceInternal(var0, ULocale.forLocale(var1));
   }

   public static synchronized Calendar getInstance(TimeZone var0, ULocale var1) {
      return getInstanceInternal(var0, var1);
   }

   private static Calendar getInstanceInternal(TimeZone var0, ULocale var1) {
      if (var1 == null) {
         var1 = ULocale.getDefault(ULocale.Category.FORMAT);
      }

      if (var0 == null) {
         var0 = TimeZone.getDefault();
      }

      Calendar var2 = getShim().createInstance(var1);
      var2.setTimeZone(var0);
      var2.setTimeInMillis(System.currentTimeMillis());
      return var2;
   }

   private static int getCalendarTypeForLocale(ULocale var0) {
      String var1 = CalendarUtil.getCalendarType(var0);
      if (var1 != null) {
         var1 = var1.toLowerCase(Locale.ENGLISH);

         for(int var2 = 0; var2 < calTypes.length; ++var2) {
            if (var1.equals(calTypes[var2])) {
               return var2;
            }
         }
      }

      return -1;
   }

   public static Locale[] getAvailableLocales() {
      return shim == null ? ICUResourceBundle.getAvailableLocales() : getShim().getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return shim == null ? ICUResourceBundle.getAvailableULocales() : getShim().getAvailableULocales();
   }

   private static Calendar.CalendarShim getShim() {
      if (shim == null) {
         try {
            Class var0 = Class.forName("com.ibm.icu.util.CalendarServiceShim");
            shim = (Calendar.CalendarShim)var0.newInstance();
         } catch (MissingResourceException var1) {
            throw var1;
         } catch (Exception var2) {
            throw new RuntimeException(var2.getMessage());
         }
      }

      return shim;
   }

   static Calendar createInstance(ULocale var0) {
      Object var1 = null;
      TimeZone var2 = TimeZone.getDefault();
      int var3 = getCalendarTypeForLocale(var0);
      if (var3 == -1) {
         var3 = 0;
      }

      switch(var3) {
      case 0:
         var1 = new GregorianCalendar(var2, var0);
         break;
      case 1:
         var1 = new JapaneseCalendar(var2, var0);
         break;
      case 2:
         var1 = new BuddhistCalendar(var2, var0);
         break;
      case 3:
         var1 = new TaiwanCalendar(var2, var0);
         break;
      case 4:
         var1 = new PersianCalendar(var2, var0);
         break;
      case 5:
         var1 = new IslamicCalendar(var2, var0);
         break;
      case 6:
         var1 = new IslamicCalendar(var2, var0);
         ((IslamicCalendar)var1).setCivil(false);
         break;
      case 7:
         var1 = new HebrewCalendar(var2, var0);
         break;
      case 8:
         var1 = new ChineseCalendar(var2, var0);
         break;
      case 9:
         var1 = new IndianCalendar(var2, var0);
         break;
      case 10:
         var1 = new CopticCalendar(var2, var0);
         break;
      case 11:
         var1 = new EthiopicCalendar(var2, var0);
         break;
      case 12:
         var1 = new EthiopicCalendar(var2, var0);
         ((EthiopicCalendar)var1).setAmeteAlemEra(true);
         break;
      case 13:
         var1 = new GregorianCalendar(var2, var0);
         ((Calendar)var1).setFirstDayOfWeek(2);
         ((Calendar)var1).setMinimalDaysInFirstWeek(4);
         break;
      case 14:
         var1 = new DangiCalendar(var2, var0);
         break;
      default:
         throw new IllegalArgumentException("Unknown calendar type");
      }

      return (Calendar)var1;
   }

   static Object registerFactory(Calendar.CalendarFactory var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("factory must not be null");
      } else {
         return getShim().registerFactory(var0);
      }
   }

   static boolean unregister(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("registryKey must not be null");
      } else {
         return shim == null ? false : shim.unregister(var0);
      }
   }

   public static final String[] getKeywordValuesForLocale(String var0, ULocale var1, boolean var2) {
      String var3 = var1.getCountry();
      if (var3.length() == 0) {
         ULocale var4 = ULocale.addLikelySubtags(var1);
         var3 = var4.getCountry();
      }

      ArrayList var11 = new ArrayList();
      UResourceBundle var5 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
      UResourceBundle var6 = var5.get("calendarPreferenceData");
      UResourceBundle var7 = null;

      try {
         var7 = var6.get(var3);
      } catch (MissingResourceException var10) {
         var7 = var6.get("001");
      }

      String[] var8 = var7.getStringArray();
      if (var2) {
         return var8;
      } else {
         int var9;
         for(var9 = 0; var9 < var8.length; ++var9) {
            var11.add(var8[var9]);
         }

         for(var9 = 0; var9 < calTypes.length; ++var9) {
            if (!var11.contains(calTypes[var9])) {
               var11.add(calTypes[var9]);
            }
         }

         return (String[])var11.toArray(new String[var11.size()]);
      }
   }

   public final Date getTime() {
      return new Date(this.getTimeInMillis());
   }

   public final void setTime(Date var1) {
      this.setTimeInMillis(var1.getTime());
   }

   public long getTimeInMillis() {
      if (!this.isTimeSet) {
         this.updateTime();
      }

      return this.time;
   }

   public void setTimeInMillis(long var1) {
      if (var1 > 183882168921600000L) {
         if (!this.isLenient()) {
            throw new IllegalArgumentException("millis value greater than upper bounds for a Calendar : " + var1);
         }

         var1 = 183882168921600000L;
      } else if (var1 < -184303902528000000L) {
         if (!this.isLenient()) {
            throw new IllegalArgumentException("millis value less than lower bounds for a Calendar : " + var1);
         }

         var1 = -184303902528000000L;
      }

      this.time = var1;
      this.areFieldsSet = this.areAllFieldsSet = false;
      this.isTimeSet = this.areFieldsVirtuallySet = true;

      for(int var3 = 0; var3 < this.fields.length; ++var3) {
         this.fields[var3] = this.stamp[var3] = 0;
      }

   }

   public final int get(int var1) {
      this.complete();
      return this.fields[var1];
   }

   protected final int internalGet(int var1) {
      return this.fields[var1];
   }

   protected final int internalGet(int var1, int var2) {
      return this.stamp[var1] > 0 ? this.fields[var1] : var2;
   }

   public final void set(int var1, int var2) {
      if (this.areFieldsVirtuallySet) {
         this.computeFields();
      }

      this.fields[var1] = var2;
      if (this.nextStamp == STAMP_MAX) {
         this.recalculateStamp();
      }

      this.stamp[var1] = this.nextStamp++;
      this.isTimeSet = this.areFieldsSet = this.areFieldsVirtuallySet = false;
   }

   public final void set(int var1, int var2, int var3) {
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
   }

   public final void set(int var1, int var2, int var3, int var4, int var5) {
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
   }

   public final void set(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.set(1, var1);
      this.set(2, var2);
      this.set(5, var3);
      this.set(11, var4);
      this.set(12, var5);
      this.set(13, var6);
   }

   public final void clear() {
      for(int var1 = 0; var1 < this.fields.length; ++var1) {
         this.fields[var1] = this.stamp[var1] = 0;
      }

      this.isTimeSet = this.areFieldsSet = this.areAllFieldsSet = this.areFieldsVirtuallySet = false;
   }

   public final void clear(int var1) {
      if (this.areFieldsVirtuallySet) {
         this.computeFields();
      }

      this.fields[var1] = 0;
      this.stamp[var1] = 0;
      this.isTimeSet = this.areFieldsSet = this.areAllFieldsSet = this.areFieldsVirtuallySet = false;
   }

   protected void complete() {
      if (!this.isTimeSet) {
         this.updateTime();
      }

      if (!this.areFieldsSet) {
         this.computeFields();
         this.areFieldsSet = true;
         this.areAllFieldsSet = true;
      }

   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Calendar var2 = (Calendar)var1;
         return this == var2 && this.getTimeInMillis() == var2.getTime().getTime();
      }
   }

   public int hashCode() {
      return (this.lenient ? 1 : 0) | this.firstDayOfWeek << 1 | this.minimalDaysInFirstWeek << 4 | this.repeatedWallTime << 7 | this.skippedWallTime << 9 | this.zone.hashCode() << 11;
   }

   private long compare(Object var1) {
      long var2;
      if (var1 instanceof Calendar) {
         var2 = ((Calendar)var1).getTimeInMillis();
      } else {
         if (!(var1 instanceof Date)) {
            throw new IllegalArgumentException(var1 + "is not a Calendar or Date");
         }

         var2 = ((Date)var1).getTime();
      }

      return this.getTimeInMillis() - var2;
   }

   public boolean before(Object var1) {
      return this.compare(var1) < 0L;
   }

   public boolean after(Object var1) {
      return this.compare(var1) > 0L;
   }

   public int getActualMaximum(int var1) {
      int var2;
      Calendar var3;
      switch(var1) {
      case 0:
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 18:
      case 20:
      case 21:
         var2 = this.getMaximum(var1);
         break;
      case 1:
      case 2:
      case 3:
      case 4:
      case 8:
      case 17:
      case 19:
      default:
         var2 = this.getActualHelper(var1, this.getLeastMaximum(var1), this.getMaximum(var1));
         break;
      case 5:
         var3 = (Calendar)this.clone();
         var3.setLenient(true);
         var3.prepareGetActual(var1, false);
         var2 = this.handleGetMonthLength(var3.get(19), var3.get(2));
         break;
      case 6:
         var3 = (Calendar)this.clone();
         var3.setLenient(true);
         var3.prepareGetActual(var1, false);
         var2 = this.handleGetYearLength(var3.get(19));
      }

      return var2;
   }

   public int getActualMinimum(int var1) {
      int var2;
      switch(var1) {
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 18:
      case 20:
      case 21:
         var2 = this.getMinimum(var1);
         break;
      case 8:
      case 17:
      case 19:
      default:
         var2 = this.getActualHelper(var1, this.getGreatestMinimum(var1), this.getMinimum(var1));
      }

      return var2;
   }

   protected void prepareGetActual(int var1, boolean var2) {
      this.set(21, 0);
      switch(var1) {
      case 1:
      case 19:
         this.set(6, this.getGreatestMinimum(6));
         break;
      case 2:
         this.set(5, this.getGreatestMinimum(5));
         break;
      case 3:
      case 4:
         int var3 = this.firstDayOfWeek;
         if (var2) {
            var3 = (var3 + 6) % 7;
            if (var3 < 1) {
               var3 += 7;
            }
         }

         this.set(7, var3);
      case 5:
      case 6:
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 18:
      default:
         break;
      case 8:
         this.set(5, 1);
         this.set(7, this.get(7));
         break;
      case 17:
         this.set(3, this.getGreatestMinimum(3));
      }

      this.set(var1, this.getGreatestMinimum(var1));
   }

   private int getActualHelper(int var1, int var2, int var3) {
      if (var2 == var3) {
         return var2;
      } else {
         int var4 = var3 > var2 ? 1 : -1;
         Calendar var5 = (Calendar)this.clone();
         var5.complete();
         var5.setLenient(true);
         var5.prepareGetActual(var1, var4 < 0);
         var5.set(var1, var2);
         if (var5.get(var1) != var2 && var1 != 4 && var4 > 0) {
            return var2;
         } else {
            int var6 = var2;

            do {
               var2 += var4;
               var5.add(var1, var4);
               if (var5.get(var1) != var2) {
                  break;
               }

               var6 = var2;
            } while(var2 != var3);

            return var6;
         }
      }
   }

   public final void roll(int var1, boolean var2) {
      this.roll(var1, var2 ? 1 : -1);
   }

   public void roll(int var1, int var2) {
      if (var2 != 0) {
         this.complete();
         long var3;
         int var4;
         int var5;
         int var6;
         int var7;
         int var8;
         int var10;
         int var11;
         int var17;
         switch(var1) {
         case 0:
         case 5:
         case 9:
         case 12:
         case 13:
         case 14:
         case 21:
            var11 = this.getActualMinimum(var1);
            var4 = this.getActualMaximum(var1);
            var5 = var4 - var11 + 1;
            var6 = this.internalGet(var1) + var2;
            var6 = (var6 - var11) % var5;
            if (var6 < 0) {
               var6 += var5;
            }

            var6 += var11;
            this.set(var1, var6);
            return;
         case 1:
         case 17:
            boolean var12 = false;
            var4 = this.get(0);
            if (var4 == 0) {
               String var16 = this.getType();
               if (var16.equals("gregorian") || var16.equals("roc") || var16.equals("coptic")) {
                  var2 = -var2;
                  var12 = true;
               }
            }

            var5 = this.internalGet(var1) + var2;
            if (var4 <= 0 && var5 < 1) {
               if (var12) {
                  var5 = 1;
               }
            } else {
               var6 = this.getActualMaximum(var1);
               if (var6 < 32768) {
                  if (var5 < 1) {
                     var5 = var6 - -var5 % var6;
                  } else if (var5 > var6) {
                     var5 = (var5 - 1) % var6 + 1;
                  }
               } else if (var5 < 1) {
                  var5 = 1;
               }
            }

            this.set(var1, var5);
            this.pinField(2);
            this.pinField(5);
            return;
         case 2:
            var11 = this.getActualMaximum(2);
            var4 = (this.internalGet(2) + var2) % (var11 + 1);
            if (var4 < 0) {
               var4 += var11 + 1;
            }

            this.set(2, var4);
            this.pinField(5);
            return;
         case 3:
            var11 = this.internalGet(7) - this.getFirstDayOfWeek();
            if (var11 < 0) {
               var11 += 7;
            }

            var4 = (var11 - this.internalGet(6) + 1) % 7;
            if (var4 < 0) {
               var4 += 7;
            }

            if (7 - var4 < this.getMinimalDaysInFirstWeek()) {
               var5 = 8 - var4;
            } else {
               var5 = 1 - var4;
            }

            var6 = this.getActualMaximum(6);
            var7 = (var6 - this.internalGet(6) + var11) % 7;
            var8 = var6 + 7 - var7;
            var17 = var8 - var5;
            var10 = (this.internalGet(6) + var2 * 7 - var5) % var17;
            if (var10 < 0) {
               var10 += var17;
            }

            var10 += var5;
            if (var10 < 1) {
               var10 = 1;
            }

            if (var10 > var6) {
               var10 = var6;
            }

            this.set(6, var10);
            this.clear(2);
            return;
         case 4:
            var11 = this.internalGet(7) - this.getFirstDayOfWeek();
            if (var11 < 0) {
               var11 += 7;
            }

            var4 = (var11 - this.internalGet(5) + 1) % 7;
            if (var4 < 0) {
               var4 += 7;
            }

            if (7 - var4 < this.getMinimalDaysInFirstWeek()) {
               var5 = 8 - var4;
            } else {
               var5 = 1 - var4;
            }

            var6 = this.getActualMaximum(5);
            var7 = (var6 - this.internalGet(5) + var11) % 7;
            var8 = var6 + 7 - var7;
            var17 = var8 - var5;
            var10 = (this.internalGet(5) + var2 * 7 - var5) % var17;
            if (var10 < 0) {
               var10 += var17;
            }

            var10 += var5;
            if (var10 < 1) {
               var10 = 1;
            }

            if (var10 > var6) {
               var10 = var6;
            }

            this.set(5, var10);
            return;
         case 6:
            var3 = (long)var2 * 86400000L;
            long var14 = this.time - (long)(this.internalGet(6) - 1) * 86400000L;
            var7 = this.getActualMaximum(6);
            this.time = (this.time + var3 - var14) % ((long)var7 * 86400000L);
            if (this.time < 0L) {
               this.time += (long)var7 * 86400000L;
            }

            this.setTimeInMillis(this.time + var14);
            return;
         case 7:
         case 18:
            var3 = (long)var2 * 86400000L;
            var5 = this.internalGet(var1);
            var5 -= var1 == 7 ? this.getFirstDayOfWeek() : 1;
            if (var5 < 0) {
               var5 += 7;
            }

            long var13 = this.time - (long)var5 * 86400000L;
            this.time = (this.time + var3 - var13) % 604800000L;
            if (this.time < 0L) {
               this.time += 604800000L;
            }

            this.setTimeInMillis(this.time + var13);
            return;
         case 8:
            var3 = (long)var2 * 604800000L;
            var5 = (this.internalGet(5) - 1) / 7;
            var6 = (this.getActualMaximum(5) - this.internalGet(5)) / 7;
            long var15 = this.time - (long)var5 * 604800000L;
            long var9 = 604800000L * (long)(var5 + var6 + 1);
            this.time = (this.time + var3 - var15) % var9;
            if (this.time < 0L) {
               this.time += var9;
            }

            this.setTimeInMillis(this.time + var15);
            return;
         case 10:
         case 11:
            var3 = this.getTimeInMillis();
            var5 = this.internalGet(var1);
            var6 = this.getMaximum(var1);
            var7 = (var5 + var2) % (var6 + 1);
            if (var7 < 0) {
               var7 += var6 + 1;
            }

            this.setTimeInMillis(var3 + 3600000L * ((long)var7 - (long)var5));
            return;
         case 15:
         case 16:
         default:
            throw new IllegalArgumentException("Calendar.roll(" + this.fieldName(var1) + ") not supported");
         case 19:
            this.set(var1, this.internalGet(var1) + var2);
            this.pinField(2);
            this.pinField(5);
            return;
         case 20:
            this.set(var1, this.internalGet(var1) + var2);
         }
      }
   }

   public void add(int var1, int var2) {
      if (var2 != 0) {
         long var3 = (long)var2;
         boolean var5 = true;
         int var6;
         switch(var1) {
         case 0:
            this.set(var1, this.get(var1) + var2);
            this.pinField(0);
            return;
         case 1:
         case 17:
            var6 = this.get(0);
            if (var6 == 0) {
               String var14 = this.getType();
               if (var14.equals("gregorian") || var14.equals("roc") || var14.equals("coptic")) {
                  var2 = -var2;
               }
            }
         case 2:
         case 19:
            boolean var13 = this.isLenient();
            this.setLenient(true);
            this.set(var1, this.get(var1) + var2);
            this.pinField(5);
            if (!var13) {
               this.complete();
               this.setLenient(var13);
            }

            return;
         case 3:
         case 4:
         case 8:
            var3 *= 604800000L;
            break;
         case 5:
         case 6:
         case 7:
         case 18:
         case 20:
            var3 *= 86400000L;
            break;
         case 9:
            var3 *= 43200000L;
            break;
         case 10:
         case 11:
            var3 *= 3600000L;
            var5 = false;
            break;
         case 12:
            var3 *= 60000L;
            var5 = false;
            break;
         case 13:
            var3 *= 1000L;
            var5 = false;
            break;
         case 14:
         case 21:
            var5 = false;
            break;
         case 15:
         case 16:
         default:
            throw new IllegalArgumentException("Calendar.add(" + this.fieldName(var1) + ") not supported");
         }

         var6 = 0;
         int var7 = 0;
         if (var5) {
            var6 = this.get(16) + this.get(15);
            var7 = this.internalGet(11);
         }

         this.setTimeInMillis(this.getTimeInMillis() + var3);
         if (var5) {
            int var8 = this.get(16) + this.get(15);
            if (var8 != var6) {
               long var9 = (long)(var6 - var8) % 86400000L;
               if (var9 != 0L) {
                  long var11 = this.time;
                  this.setTimeInMillis(this.time + var9);
                  if (this.get(11) != var7) {
                     this.setTimeInMillis(var11);
                  }
               }
            }
         }

      }
   }

   public String getDisplayName(Locale var1) {
      return this.getClass().getName();
   }

   public String getDisplayName(ULocale var1) {
      return this.getClass().getName();
   }

   public int compareTo(Calendar var1) {
      long var2 = this.getTimeInMillis() - var1.getTimeInMillis();
      return var2 < 0L ? -1 : (var2 > 0L ? 1 : 0);
   }

   public DateFormat getDateTimeFormat(int var1, int var2, Locale var3) {
      return formatHelper(this, ULocale.forLocale(var3), var1, var2);
   }

   public DateFormat getDateTimeFormat(int var1, int var2, ULocale var3) {
      return formatHelper(this, var3, var1, var2);
   }

   protected DateFormat handleGetDateFormat(String var1, Locale var2) {
      return this.handleGetDateFormat(var1, (String)null, (ULocale)ULocale.forLocale(var2));
   }

   protected DateFormat handleGetDateFormat(String var1, String var2, Locale var3) {
      return this.handleGetDateFormat(var1, var2, ULocale.forLocale(var3));
   }

   protected DateFormat handleGetDateFormat(String var1, ULocale var2) {
      return this.handleGetDateFormat(var1, (String)null, (ULocale)var2);
   }

   protected DateFormat handleGetDateFormat(String var1, String var2, ULocale var3) {
      Calendar.FormatConfiguration var4 = new Calendar.FormatConfiguration();
      Calendar.FormatConfiguration.access$102(var4, var1);
      Calendar.FormatConfiguration.access$202(var4, var2);
      Calendar.FormatConfiguration.access$302(var4, new DateFormatSymbols(this, var3));
      Calendar.FormatConfiguration.access$402(var4, var3);
      Calendar.FormatConfiguration.access$502(var4, this);
      return SimpleDateFormat.getInstance(var4);
   }

   private static DateFormat formatHelper(Calendar var0, ULocale var1, int var2, int var3) {
      Calendar.PatternData var4 = Calendar.PatternData.access$600(var0, var1);
      String var5 = null;
      String var6 = null;
      if (var3 >= 0 && var2 >= 0) {
         var6 = MessageFormat.format(Calendar.PatternData.access$700(var4, var2), Calendar.PatternData.access$800(var4)[var3], Calendar.PatternData.access$800(var4)[var2 + 4]);
         if (Calendar.PatternData.access$900(var4) != null) {
            String var7 = Calendar.PatternData.access$900(var4)[var2 + 4];
            String var8 = Calendar.PatternData.access$900(var4)[var3];
            var5 = mergeOverrideStrings(Calendar.PatternData.access$800(var4)[var2 + 4], Calendar.PatternData.access$800(var4)[var3], var7, var8);
         }
      } else if (var3 >= 0) {
         var6 = Calendar.PatternData.access$800(var4)[var3];
         if (Calendar.PatternData.access$900(var4) != null) {
            var5 = Calendar.PatternData.access$900(var4)[var3];
         }
      } else {
         if (var2 < 0) {
            throw new IllegalArgumentException("No date or time style specified");
         }

         var6 = Calendar.PatternData.access$800(var4)[var2 + 4];
         if (Calendar.PatternData.access$900(var4) != null) {
            var5 = Calendar.PatternData.access$900(var4)[var2 + 4];
         }
      }

      DateFormat var9 = var0.handleGetDateFormat(var6, var5, var1);
      var9.setCalendar(var0);
      return var9;
   }

   /** @deprecated */
   public static String getDateTimePattern(Calendar var0, ULocale var1, int var2) {
      Calendar.PatternData var3 = Calendar.PatternData.access$600(var0, var1);
      return Calendar.PatternData.access$700(var3, var2);
   }

   private static String mergeOverrideStrings(String var0, String var1, String var2, String var3) {
      if (var2 == null && var3 == null) {
         return null;
      } else if (var2 == null) {
         return expandOverride(var1, var3);
      } else if (var3 == null) {
         return expandOverride(var0, var2);
      } else {
         return var2.equals(var3) ? var2 : expandOverride(var0, var2) + ";" + expandOverride(var1, var3);
      }
   }

   private static String expandOverride(String var0, String var1) {
      if (var1.indexOf(61) >= 0) {
         return var1;
      } else {
         boolean var2 = false;
         char var3 = ' ';
         StringBuilder var4 = new StringBuilder();
         StringCharacterIterator var5 = new StringCharacterIterator(var0);

         for(char var6 = var5.first(); var6 != '\uffff'; var6 = var5.next()) {
            if (var6 == '\'') {
               var2 = !var2;
               var3 = var6;
            } else {
               if (!var2 && var6 != var3) {
                  if (var4.length() > 0) {
                     var4.append(";");
                  }

                  var4.append(var6);
                  var4.append("=");
                  var4.append(var1);
               }

               var3 = var6;
            }
         }

         return var4.toString();
      }
   }

   protected void pinField(int var1) {
      int var2 = this.getActualMaximum(var1);
      int var3 = this.getActualMinimum(var1);
      if (this.fields[var1] > var2) {
         this.set(var1, var2);
      } else if (this.fields[var1] < var3) {
         this.set(var1, var3);
      }

   }

   protected int weekNumber(int var1, int var2, int var3) {
      int var4 = (var3 - this.getFirstDayOfWeek() - var2 + 1) % 7;
      if (var4 < 0) {
         var4 += 7;
      }

      int var5 = (var1 + var4 - 1) / 7;
      if (7 - var4 >= this.getMinimalDaysInFirstWeek()) {
         ++var5;
      }

      return var5;
   }

   protected final int weekNumber(int var1, int var2) {
      return this.weekNumber(var1, var1, var2);
   }

   public int fieldDifference(Date var1, int var2) {
      int var3 = 0;
      long var4 = this.getTimeInMillis();
      long var6 = var1.getTime();
      int var8;
      long var9;
      long var10;
      int var12;
      if (var4 < var6) {
         var8 = 1;

         while(true) {
            this.setTimeInMillis(var4);
            this.add(var2, var8);
            var9 = this.getTimeInMillis();
            if (var9 == var6) {
               return var8;
            }

            if (var9 > var6) {
               while(var8 - var3 > 1) {
                  var12 = var3 + (var8 - var3) / 2;
                  this.setTimeInMillis(var4);
                  this.add(var2, var12);
                  var10 = this.getTimeInMillis();
                  if (var10 == var6) {
                     return var12;
                  }

                  if (var10 > var6) {
                     var8 = var12;
                  } else {
                     var3 = var12;
                  }
               }
               break;
            }

            if (var8 >= Integer.MAX_VALUE) {
               throw new RuntimeException();
            }

            var3 = var8;
            var8 <<= 1;
            if (var8 < 0) {
               var8 = Integer.MAX_VALUE;
            }
         }
      } else if (var4 > var6) {
         var8 = -1;

         while(true) {
            this.setTimeInMillis(var4);
            this.add(var2, var8);
            var9 = this.getTimeInMillis();
            if (var9 == var6) {
               return var8;
            }

            if (var9 < var6) {
               while(var3 - var8 > 1) {
                  var12 = var3 + (var8 - var3) / 2;
                  this.setTimeInMillis(var4);
                  this.add(var2, var12);
                  var10 = this.getTimeInMillis();
                  if (var10 == var6) {
                     return var12;
                  }

                  if (var10 < var6) {
                     var8 = var12;
                  } else {
                     var3 = var12;
                  }
               }
               break;
            }

            var3 = var8;
            var8 <<= 1;
            if (var8 == 0) {
               throw new RuntimeException();
            }
         }
      }

      this.setTimeInMillis(var4);
      this.add(var2, var3);
      return var3;
   }

   public void setTimeZone(TimeZone var1) {
      this.zone = var1;
      this.areFieldsSet = false;
   }

   public TimeZone getTimeZone() {
      return this.zone;
   }

   public void setLenient(boolean var1) {
      this.lenient = var1;
   }

   public boolean isLenient() {
      return this.lenient;
   }

   public void setRepeatedWallTimeOption(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("Illegal repeated wall time option - " + var1);
      } else {
         this.repeatedWallTime = var1;
      }
   }

   public int getRepeatedWallTimeOption() {
      return this.repeatedWallTime;
   }

   public void setSkippedWallTimeOption(int var1) {
      if (var1 != 0 && var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Illegal skipped wall time option - " + var1);
      } else {
         this.skippedWallTime = var1;
      }
   }

   public int getSkippedWallTimeOption() {
      return this.skippedWallTime;
   }

   public void setFirstDayOfWeek(int var1) {
      if (this.firstDayOfWeek != var1) {
         if (var1 < 1 || var1 > 7) {
            throw new IllegalArgumentException("Invalid day of week");
         }

         this.firstDayOfWeek = var1;
         this.areFieldsSet = false;
      }

   }

   public int getFirstDayOfWeek() {
      return this.firstDayOfWeek;
   }

   public void setMinimalDaysInFirstWeek(int var1) {
      if (var1 < 1) {
         var1 = 1;
      } else if (var1 > 7) {
         var1 = 7;
      }

      if (this.minimalDaysInFirstWeek != var1) {
         this.minimalDaysInFirstWeek = var1;
         this.areFieldsSet = false;
      }

   }

   public int getMinimalDaysInFirstWeek() {
      return this.minimalDaysInFirstWeek;
   }

   protected abstract int handleGetLimit(int var1, int var2);

   protected int getLimit(int var1, int var2) {
      switch(var1) {
      case 4:
         int var3;
         if (var2 == 0) {
            var3 = this.getMinimalDaysInFirstWeek() == 1 ? 1 : 0;
         } else if (var2 == 1) {
            var3 = 1;
         } else {
            int var4 = this.getMinimalDaysInFirstWeek();
            int var5 = this.handleGetLimit(5, var2);
            if (var2 == 2) {
               var3 = (var5 + (7 - var4)) / 7;
            } else {
               var3 = (var5 + 6 + (7 - var4)) / 7;
            }
         }

         return var3;
      case 5:
      case 6:
      case 8:
      case 17:
      case 19:
      default:
         return this.handleGetLimit(var1, var2);
      case 7:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 18:
      case 20:
      case 21:
      case 22:
         return LIMITS[var1][var2];
      }
   }

   public final int getMinimum(int var1) {
      return this.getLimit(var1, 0);
   }

   public final int getMaximum(int var1) {
      return this.getLimit(var1, 3);
   }

   public final int getGreatestMinimum(int var1) {
      return this.getLimit(var1, 1);
   }

   public final int getLeastMaximum(int var1) {
      return this.getLimit(var1, 2);
   }

   public int getDayOfWeekType(int var1) {
      if (var1 >= 1 && var1 <= 7) {
         if (this.weekendOnset < this.weekendCease) {
            if (var1 < this.weekendOnset || var1 > this.weekendCease) {
               return 0;
            }
         } else if (var1 > this.weekendCease && var1 < this.weekendOnset) {
            return 0;
         }

         if (var1 == this.weekendOnset) {
            return this.weekendOnsetMillis == 0 ? 1 : 2;
         } else if (var1 == this.weekendCease) {
            return this.weekendCeaseMillis == 0 ? 0 : 3;
         } else {
            return 1;
         }
      } else {
         throw new IllegalArgumentException("Invalid day of week");
      }
   }

   public int getWeekendTransition(int var1) {
      if (var1 == this.weekendOnset) {
         return this.weekendOnsetMillis;
      } else if (var1 == this.weekendCease) {
         return this.weekendCeaseMillis;
      } else {
         throw new IllegalArgumentException("Not weekend transition day");
      }
   }

   public boolean isWeekend(Date var1) {
      this.setTime(var1);
      return this.isWeekend();
   }

   public boolean isWeekend() {
      int var1 = this.get(7);
      int var2 = this.getDayOfWeekType(var1);
      switch(var2) {
      case 0:
         return false;
      case 1:
         return true;
      default:
         int var3 = this.internalGet(14) + 1000 * (this.internalGet(13) + 60 * (this.internalGet(12) + 60 * this.internalGet(11)));
         int var4 = this.getWeekendTransition(var1);
         return var2 == 2 ? var3 >= var4 : var3 < var4;
      }
   }

   public Object clone() {
      try {
         Calendar var1 = (Calendar)super.clone();
         var1.fields = new int[this.fields.length];
         var1.stamp = new int[this.fields.length];
         System.arraycopy(this.fields, 0, var1.fields, 0, this.fields.length);
         System.arraycopy(this.stamp, 0, var1.stamp, 0, this.fields.length);
         var1.zone = (TimeZone)this.zone.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getName());
      var1.append("[time=");
      var1.append(this.isTimeSet ? String.valueOf(this.time) : "?");
      var1.append(",areFieldsSet=");
      var1.append(this.areFieldsSet);
      var1.append(",areAllFieldsSet=");
      var1.append(this.areAllFieldsSet);
      var1.append(",lenient=");
      var1.append(this.lenient);
      var1.append(",zone=");
      var1.append(this.zone);
      var1.append(",firstDayOfWeek=");
      var1.append(this.firstDayOfWeek);
      var1.append(",minimalDaysInFirstWeek=");
      var1.append(this.minimalDaysInFirstWeek);
      var1.append(",repeatedWallTime=");
      var1.append(this.repeatedWallTime);
      var1.append(",skippedWallTime=");
      var1.append(this.skippedWallTime);

      for(int var2 = 0; var2 < this.fields.length; ++var2) {
         var1.append(',').append(this.fieldName(var2)).append('=');
         this.append(var2 == 0 ? String.valueOf(this.fields[var2]) : "?");
      }

      var1.append(']');
      return var1.toString();
   }

   private void setWeekData(ULocale var1) {
      Calendar.WeekData var2 = (Calendar.WeekData)cachedLocaleData.get(var1);
      ULocale var3;
      if (var2 == null) {
         CalendarData var4 = new CalendarData(var1, this.getType());
         ULocale var5 = ULocale.minimizeSubtags(var4.getULocale());
         if (var5.getCountry().length() > 0) {
            var3 = var5;
         } else {
            ULocale var6 = ULocale.addLikelySubtags(var5);
            StringBuilder var7 = new StringBuilder();
            var7.append(var5.getLanguage());
            if (var5.getScript().length() > 0) {
               var7.append("_" + var5.getScript());
            }

            if (var6.getCountry().length() > 0) {
               var7.append("_" + var6.getCountry());
            }

            if (var5.getVariant().length() > 0) {
               var7.append("_" + var5.getVariant());
            }

            var3 = new ULocale(var7.toString());
         }

         UResourceBundle var11 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         UResourceBundle var12 = var11.get("weekData");
         UResourceBundle var8 = null;

         try {
            var8 = var12.get(var3.getCountry());
         } catch (MissingResourceException var10) {
            var8 = var12.get("001");
         }

         int[] var9 = var8.getIntVector();
         var2 = new Calendar.WeekData(var9[0], var9[1], var9[2], var9[3], var9[4], var9[5], var4.getULocale());
         cachedLocaleData.put(var1, var2);
      }

      this.setFirstDayOfWeek(var2.firstDayOfWeek);
      this.setMinimalDaysInFirstWeek(var2.minimalDaysInFirstWeek);
      this.weekendOnset = var2.weekendOnset;
      this.weekendOnsetMillis = var2.weekendOnsetMillis;
      this.weekendCease = var2.weekendCease;
      this.weekendCeaseMillis = var2.weekendCeaseMillis;
      var3 = var2.actualLocale;
      this.setLocale(var3, var3);
   }

   private void updateTime() {
      this.computeTime();
      if (this.isLenient() || !this.areAllFieldsSet) {
         this.areFieldsSet = false;
      }

      this.isTimeSet = true;
      this.areFieldsVirtuallySet = false;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (!this.isTimeSet) {
         try {
            this.updateTime();
         } catch (IllegalArgumentException var3) {
         }
      }

      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.initInternal();
      this.isTimeSet = true;
      this.areFieldsSet = this.areAllFieldsSet = false;
      this.areFieldsVirtuallySet = true;
      this.nextStamp = 2;
   }

   protected void computeFields() {
      int[] var1 = new int[2];
      this.getTimeZone().getOffset(this.time, false, var1);
      long var2 = this.time + (long)var1[0] + (long)var1[1];
      int var4 = this.internalSetMask;

      for(int var5 = 0; var5 < this.fields.length; ++var5) {
         if ((var4 & 1) == 0) {
            this.stamp[var5] = 1;
         } else {
            this.stamp[var5] = 0;
         }

         var4 >>= 1;
      }

      long var8 = floorDivide(var2, 86400000L);
      this.fields[20] = (int)var8 + 2440588;
      this.computeGregorianAndDOWFields(this.fields[20]);
      this.handleComputeFields(this.fields[20]);
      this.computeWeekFields();
      int var7 = (int)(var2 - var8 * 86400000L);
      this.fields[21] = var7;
      this.fields[14] = var7 % 1000;
      var7 /= 1000;
      this.fields[13] = var7 % 60;
      var7 /= 60;
      this.fields[12] = var7 % 60;
      var7 /= 60;
      this.fields[11] = var7;
      this.fields[9] = var7 / 12;
      this.fields[10] = var7 % 12;
      this.fields[15] = var1[0];
      this.fields[16] = var1[1];
   }

   private final void computeGregorianAndDOWFields(int var1) {
      this.computeGregorianFields(var1);
      int var2 = this.fields[7] = julianDayToDayOfWeek(var1);
      int var3 = var2 - this.getFirstDayOfWeek() + 1;
      if (var3 < 1) {
         var3 += 7;
      }

      this.fields[18] = var3;
   }

   protected final void computeGregorianFields(int var1) {
      long var6 = (long)(var1 - 1721426);
      int[] var8 = new int[1];
      int var9 = floorDivide(var6, 146097, var8);
      int var10 = floorDivide(var8[0], 36524, var8);
      int var11 = floorDivide(var8[0], 1461, var8);
      int var12 = floorDivide(var8[0], 365, var8);
      int var2 = 400 * var9 + 100 * var10 + 4 * var11 + var12;
      int var5 = var8[0];
      if (var10 != 4 && var12 != 4) {
         ++var2;
      } else {
         var5 = 365;
      }

      boolean var13 = (var2 & 3) == 0 && (var2 % 100 != 0 || var2 % 400 == 0);
      int var14 = 0;
      int var15 = var13 ? 60 : 59;
      if (var5 >= var15) {
         var14 = var13 ? 1 : 2;
      }

      int var3 = (12 * (var5 + var14) + 6) / 367;
      int var4 = var5 - GREGORIAN_MONTH_COUNT[var3][var13 ? 3 : 2] + 1;
      this.gregorianYear = var2;
      this.gregorianMonth = var3;
      this.gregorianDayOfMonth = var4;
      this.gregorianDayOfYear = var5 + 1;
   }

   private final void computeWeekFields() {
      int var1 = this.fields[19];
      int var2 = this.fields[7];
      int var3 = this.fields[6];
      int var4 = var1;
      int var5 = (var2 + 7 - this.getFirstDayOfWeek()) % 7;
      int var6 = (var2 - var3 + 7001 - this.getFirstDayOfWeek()) % 7;
      int var7 = (var3 - 1 + var6) / 7;
      if (7 - var6 >= this.getMinimalDaysInFirstWeek()) {
         ++var7;
      }

      int var8;
      if (var7 == 0) {
         var8 = var3 + this.handleGetYearLength(var1 - 1);
         var7 = this.weekNumber(var8, var2);
         var4 = var1 - 1;
      } else {
         var8 = this.handleGetYearLength(var1);
         if (var3 >= var8 - 5) {
            int var9 = (var5 + var8 - var3) % 7;
            if (var9 < 0) {
               var9 += 7;
            }

            if (6 - var9 >= this.getMinimalDaysInFirstWeek() && var3 + 7 - var5 > var8) {
               var7 = 1;
               var4 = var1 + 1;
            }
         }
      }

      this.fields[3] = var7;
      this.fields[17] = var4;
      var8 = this.fields[5];
      this.fields[4] = this.weekNumber(var8, var2);
      this.fields[8] = (var8 - 1) / 7 + 1;
   }

   protected int resolveFields(int[][][] var1) {
      int var2 = -1;

      for(int var4 = 0; var4 < var1.length && var2 < 0; ++var4) {
         int[][] var5 = var1[var4];
         int var6 = 0;

         label61:
         for(int var7 = 0; var7 < var5.length; ++var7) {
            int[] var8 = var5[var7];
            int var9 = 0;

            for(int var10 = var8[0] >= 32 ? 1 : 0; var10 < var8.length; ++var10) {
               int var11 = this.stamp[var8[var10]];
               if (var11 == 0) {
                  continue label61;
               }

               var9 = Math.max(var9, var11);
            }

            if (var9 > var6) {
               int var3 = var8[0];
               if (var3 >= 32) {
                  var3 &= 31;
                  if (var3 != 5 || this.stamp[4] < this.stamp[var3]) {
                     var2 = var3;
                  }
               } else {
                  var2 = var3;
               }

               if (var2 == var3) {
                  var6 = var9;
               }
            }
         }
      }

      return var2 >= 32 ? var2 & 31 : var2;
   }

   protected int newestStamp(int var1, int var2, int var3) {
      int var4 = var3;

      for(int var5 = var1; var5 <= var2; ++var5) {
         if (this.stamp[var5] > var4) {
            var4 = this.stamp[var5];
         }
      }

      return var4;
   }

   protected final int getStamp(int var1) {
      return this.stamp[var1];
   }

   protected int newerField(int var1, int var2) {
      return this.stamp[var2] > this.stamp[var1] ? var2 : var1;
   }

   protected void validateFields() {
      for(int var1 = 0; var1 < this.fields.length; ++var1) {
         if (this.stamp[var1] >= 2) {
            this.validateField(var1);
         }
      }

   }

   protected void validateField(int var1) {
      int var2;
      switch(var1) {
      case 5:
         var2 = this.handleGetExtendedYear();
         this.validateField(var1, 1, this.handleGetMonthLength(var2, this.internalGet(2)));
         break;
      case 6:
         var2 = this.handleGetExtendedYear();
         this.validateField(var1, 1, this.handleGetYearLength(var2));
         break;
      case 7:
      default:
         this.validateField(var1, this.getMinimum(var1), this.getMaximum(var1));
         break;
      case 8:
         if (this.internalGet(var1) == 0) {
            throw new IllegalArgumentException("DAY_OF_WEEK_IN_MONTH cannot be zero");
         }

         this.validateField(var1, this.getMinimum(var1), this.getMaximum(var1));
      }

   }

   protected final void validateField(int var1, int var2, int var3) {
      int var4 = this.fields[var1];
      if (var4 < var2 || var4 > var3) {
         throw new IllegalArgumentException(this.fieldName(var1) + '=' + var4 + ", valid range=" + var2 + ".." + var3);
      }
   }

   protected void computeTime() {
      if (!this.isLenient()) {
         this.validateFields();
      }

      int var1 = this.computeJulianDay();
      long var2 = julianDayToMillis(var1);
      int var4;
      if (this.stamp[21] >= 2 && this.newestStamp(9, 14, 0) <= this.stamp[21]) {
         var4 = this.internalGet(21);
      } else {
         var4 = this.computeMillisInDay();
      }

      if (this.stamp[15] < 2 && this.stamp[16] < 2) {
         if (this.lenient && this.skippedWallTime != 2) {
            this.time = var2 + (long)var4 - (long)this.computeZoneOffset(var2, var4);
         } else {
            int var5 = this.computeZoneOffset(var2, var4);
            long var6 = var2 + (long)var4 - (long)var5;
            int var8 = this.zone.getOffset(var6);
            if (var5 != var8) {
               if (!this.lenient) {
                  throw new IllegalArgumentException("The specified wall time does not exist due to time zone offset transition.");
               }

               if (!$assertionsDisabled && this.skippedWallTime != 2) {
                  throw new AssertionError(this.skippedWallTime);
               }

               if (this.zone instanceof BasicTimeZone) {
                  TimeZoneTransition var9 = ((BasicTimeZone)this.zone).getPreviousTransition(var6, true);
                  if (var9 == null) {
                     throw new RuntimeException("Could not locate previous zone transition");
                  }

                  this.time = var9.getTime();
               } else {
                  Long var10 = this.getPreviousZoneTransitionTime(this.zone, var6, 7200000L);
                  if (var10 == null) {
                     var10 = this.getPreviousZoneTransitionTime(this.zone, var6, 108000000L);
                     if (var10 == null) {
                        throw new RuntimeException("Could not locate previous zone transition within 30 hours from " + var6);
                     }
                  }

                  this.time = var10;
               }
            } else {
               this.time = var6;
            }
         }
      } else {
         this.time = var2 + (long)var4 - (long)(this.internalGet(15) + this.internalGet(16));
      }

   }

   private Long getPreviousZoneTransitionTime(TimeZone var1, long var2, long var4) {
      if (!$assertionsDisabled && var4 <= 0L) {
         throw new AssertionError();
      } else {
         long var8 = var2 - var4 - 1L;
         int var10 = var1.getOffset(var2);
         int var11 = var1.getOffset(var8);
         return var10 == var11 ? null : this.findPreviousZoneTransitionTime(var1, var10, var2, var8);
      }
   }

   private Long findPreviousZoneTransitionTime(TimeZone var1, int var2, long var3, long var5) {
      boolean var7 = false;
      long var8 = 0L;
      int[] var10 = FIND_ZONE_TRANSITION_TIME_UNITS;
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         int var13 = var10[var12];
         long var14 = var5 / (long)var13;
         long var16 = var3 / (long)var13;
         if (var16 > var14) {
            var8 = (var14 + var16 + 1L >>> 1) * (long)var13;
            var7 = true;
            break;
         }
      }

      if (!var7) {
         var8 = var3 + var5 >>> 1;
      }

      int var18;
      if (var7) {
         if (var8 != var3) {
            var18 = var1.getOffset(var8);
            if (var18 != var2) {
               return this.findPreviousZoneTransitionTime(var1, var2, var3, var8);
            }

            var3 = var8;
         }

         --var8;
      } else {
         var8 = var3 + var5 >>> 1;
      }

      if (var8 == var5) {
         return var3;
      } else {
         var18 = var1.getOffset(var8);
         if (var18 != var2) {
            return var7 ? var3 : this.findPreviousZoneTransitionTime(var1, var2, var3, var8);
         } else {
            return this.findPreviousZoneTransitionTime(var1, var2, var8, var5);
         }
      }
   }

   protected int computeMillisInDay() {
      int var1 = 0;
      int var2 = this.stamp[11];
      int var3 = Math.max(this.stamp[10], this.stamp[9]);
      int var4 = var3 > var2 ? var3 : var2;
      if (var4 != 0) {
         if (var4 == var2) {
            var1 += this.internalGet(11);
         } else {
            var1 += this.internalGet(10);
            var1 += 12 * this.internalGet(9);
         }
      }

      var1 *= 60;
      var1 += this.internalGet(12);
      var1 *= 60;
      var1 += this.internalGet(13);
      var1 *= 1000;
      var1 += this.internalGet(14);
      return var1;
   }

   protected int computeZoneOffset(long var1, int var3) {
      int[] var4 = new int[2];
      long var5 = var1 + (long)var3;
      if (this.zone instanceof BasicTimeZone) {
         int var7 = this.repeatedWallTime == 1 ? 4 : 12;
         int var8 = this.skippedWallTime == 1 ? 12 : 4;
         ((BasicTimeZone)this.zone).getOffsetFromLocal(var5, var8, var7, var4);
      } else {
         this.zone.getOffset(var5, true, var4);
         boolean var12 = false;
         long var13;
         if (this.repeatedWallTime == 1) {
            var13 = var5 - (long)(var4[0] + var4[1]);
            int var10 = this.zone.getOffset(var13 - 21600000L);
            int var11 = var4[0] + var4[1] - var10;
            if (!$assertionsDisabled && var11 >= -21600000) {
               throw new AssertionError(var11);
            }

            if (var11 < 0) {
               var12 = true;
               this.zone.getOffset(var5 + (long)var11, true, var4);
            }
         }

         if (!var12 && this.skippedWallTime == 1) {
            var13 = var5 - (long)(var4[0] + var4[1]);
            this.zone.getOffset(var13, false, var4);
         }
      }

      return var4[0] + var4[1];
   }

   protected int computeJulianDay() {
      int var1;
      if (this.stamp[20] >= 2) {
         var1 = this.newestStamp(0, 8, 0);
         var1 = this.newestStamp(17, 19, var1);
         if (var1 <= this.stamp[20]) {
            return this.internalGet(20);
         }
      }

      var1 = this.resolveFields(this.getFieldResolutionTable());
      if (var1 < 0) {
         var1 = 5;
      }

      return this.handleComputeJulianDay(var1);
   }

   protected int[][][] getFieldResolutionTable() {
      return DATE_PRECEDENCE;
   }

   protected abstract int handleComputeMonthStart(int var1, int var2, boolean var3);

   protected abstract int handleGetExtendedYear();

   protected int handleGetMonthLength(int var1, int var2) {
      return this.handleComputeMonthStart(var1, var2 + 1, true) - this.handleComputeMonthStart(var1, var2, true);
   }

   protected int handleGetYearLength(int var1) {
      return this.handleComputeMonthStart(var1 + 1, 0, false) - this.handleComputeMonthStart(var1, 0, false);
   }

   protected int[] handleCreateFields() {
      return new int[23];
   }

   protected int getDefaultMonthInYear(int var1) {
      return 0;
   }

   protected int getDefaultDayInMonth(int var1, int var2) {
      return 1;
   }

   protected int handleComputeJulianDay(int var1) {
      boolean var2 = var1 == 5 || var1 == 4 || var1 == 8;
      int var3;
      if (var1 == 3) {
         var3 = this.internalGet(17, this.handleGetExtendedYear());
      } else {
         var3 = this.handleGetExtendedYear();
      }

      this.internalSet(19, var3);
      int var4 = var2 ? this.internalGet(2, this.getDefaultMonthInYear(var3)) : 0;
      int var5 = this.handleComputeMonthStart(var3, var4, var2);
      if (var1 == 5) {
         return 5 == 0 ? var5 + this.internalGet(5, this.getDefaultDayInMonth(var3, var4)) : var5 + this.getDefaultDayInMonth(var3, var4);
      } else if (var1 == 6) {
         return var5 + this.internalGet(6);
      } else {
         int var6 = this.getFirstDayOfWeek();
         int var7 = julianDayToDayOfWeek(var5 + 1) - var6;
         if (var7 < 0) {
            var7 += 7;
         }

         int var8 = 0;
         switch(this.resolveFields(DOW_PRECEDENCE)) {
         case 7:
            var8 = this.internalGet(7) - var6;
            break;
         case 18:
            var8 = this.internalGet(18) - 1;
         }

         var8 %= 7;
         if (var8 < 0) {
            var8 += 7;
         }

         int var9 = 1 - var7 + var8;
         if (var1 == 8) {
            if (var9 < 1) {
               var9 += 7;
            }

            int var10 = this.internalGet(8, 1);
            if (var10 >= 0) {
               var9 += 7 * (var10 - 1);
            } else {
               int var11 = this.internalGet(2, 0);
               int var12 = this.handleGetMonthLength(var3, var11);
               var9 += ((var12 - var9) / 7 + var10 + 1) * 7;
            }
         } else {
            if (7 - var7 < this.getMinimalDaysInFirstWeek()) {
               var9 += 7;
            }

            var9 += 7 * (this.internalGet(var1) - 1);
         }

         return var5 + var9;
      }
   }

   protected int computeGregorianMonthStart(int var1, int var2) {
      if (var2 < 0 || var2 > 11) {
         int[] var3 = new int[1];
         var1 += floorDivide(var2, 12, var3);
         var2 = var3[0];
      }

      boolean var6 = var1 % 4 == 0 && (var1 % 100 != 0 || var1 % 400 == 0);
      int var4 = var1 - 1;
      int var5 = 365 * var4 + floorDivide(var4, 4) - floorDivide(var4, 100) + floorDivide(var4, 400) + 1721426 - 1;
      if (var2 != 0) {
         var5 += GREGORIAN_MONTH_COUNT[var2][var6 ? 3 : 2];
      }

      return var5;
   }

   protected void handleComputeFields(int var1) {
      this.internalSet(2, this.getGregorianMonth());
      this.internalSet(5, this.getGregorianDayOfMonth());
      this.internalSet(6, this.getGregorianDayOfYear());
      int var2 = this.getGregorianYear();
      this.internalSet(19, var2);
      byte var3 = 1;
      if (var2 < 1) {
         var3 = 0;
         var2 = 1 - var2;
      }

      this.internalSet(0, var3);
      this.internalSet(1, var2);
   }

   protected final int getGregorianYear() {
      return this.gregorianYear;
   }

   protected final int getGregorianMonth() {
      return this.gregorianMonth;
   }

   protected final int getGregorianDayOfYear() {
      return this.gregorianDayOfYear;
   }

   protected final int getGregorianDayOfMonth() {
      return this.gregorianDayOfMonth;
   }

   public final int getFieldCount() {
      return this.fields.length;
   }

   protected final void internalSet(int var1, int var2) {
      if ((1 << var1 & this.internalSetMask) == 0) {
         throw new IllegalStateException("Subclass cannot set " + this.fieldName(var1));
      } else {
         this.fields[var1] = var2;
         this.stamp[var1] = 1;
      }
   }

   protected static final int gregorianMonthLength(int var0, int var1) {
      return GREGORIAN_MONTH_COUNT[var1][var0 == 0 ? 1 : 0];
   }

   protected static final int gregorianPreviousMonthLength(int var0, int var1) {
      return var1 > 0 ? gregorianMonthLength(var0, var1 - 1) : 31;
   }

   protected static final long floorDivide(long var0, long var2) {
      return var0 >= 0L ? var0 / var2 : (var0 + 1L) / var2 - 1L;
   }

   protected static final int floorDivide(int var0, int var1) {
      return var0 >= 0 ? var0 / var1 : (var0 + 1) / var1 - 1;
   }

   protected static final int floorDivide(int var0, int var1, int[] var2) {
      if (var0 >= 0) {
         var2[0] = var0 % var1;
         return var0 / var1;
      } else {
         int var3 = (var0 + 1) / var1 - 1;
         var2[0] = var0 - var3 * var1;
         return var3;
      }
   }

   protected static final int floorDivide(long var0, int var2, int[] var3) {
      if (var0 >= 0L) {
         var3[0] = (int)(var0 % (long)var2);
         return (int)(var0 / (long)var2);
      } else {
         int var4 = (int)((var0 + 1L) / (long)var2 - 1L);
         var3[0] = (int)(var0 - (long)var4 * (long)var2);
         return var4;
      }
   }

   protected String fieldName(int var1) {
      try {
         return FIELD_NAME[var1];
      } catch (ArrayIndexOutOfBoundsException var3) {
         return "Field " + var1;
      }
   }

   protected static final int millisToJulianDay(long var0) {
      return (int)(2440588L + floorDivide(var0, 86400000L));
   }

   protected static final long julianDayToMillis(int var0) {
      return (long)(var0 - 2440588) * 86400000L;
   }

   protected static final int julianDayToDayOfWeek(int var0) {
      int var1 = (var0 + 2) % 7;
      if (var1 < 1) {
         var1 += 7;
      }

      return var1;
   }

   protected final long internalGetTimeInMillis() {
      return this.time;
   }

   public String getType() {
      return "unknown";
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

   public int compareTo(Object var1) {
      return this.compareTo((Calendar)var1);
   }

   static ICUCache access$1000() {
      return PATTERN_CACHE;
   }

   static String[] access$1100() {
      return DEFAULT_PATTERNS;
   }

   private static class WeekData {
      public int firstDayOfWeek;
      public int minimalDaysInFirstWeek;
      public int weekendOnset;
      public int weekendOnsetMillis;
      public int weekendCease;
      public int weekendCeaseMillis;
      public ULocale actualLocale;

      public WeekData(int var1, int var2, int var3, int var4, int var5, int var6, ULocale var7) {
         this.firstDayOfWeek = var1;
         this.minimalDaysInFirstWeek = var2;
         this.actualLocale = var7;
         this.weekendOnset = var3;
         this.weekendOnsetMillis = var4;
         this.weekendCease = var5;
         this.weekendCeaseMillis = var6;
      }
   }

   /** @deprecated */
   public static class FormatConfiguration {
      private String pattern;
      private String override;
      private DateFormatSymbols formatData;
      private Calendar cal;
      private ULocale loc;

      private FormatConfiguration() {
      }

      /** @deprecated */
      public String getPatternString() {
         return this.pattern;
      }

      /** @deprecated */
      public String getOverrideString() {
         return this.override;
      }

      /** @deprecated */
      public Calendar getCalendar() {
         return this.cal;
      }

      /** @deprecated */
      public ULocale getLocale() {
         return this.loc;
      }

      /** @deprecated */
      public DateFormatSymbols getDateFormatSymbols() {
         return this.formatData;
      }

      FormatConfiguration(Object var1) {
         this();
      }

      static String access$102(Calendar.FormatConfiguration var0, String var1) {
         return var0.pattern = var1;
      }

      static String access$202(Calendar.FormatConfiguration var0, String var1) {
         return var0.override = var1;
      }

      static DateFormatSymbols access$302(Calendar.FormatConfiguration var0, DateFormatSymbols var1) {
         return var0.formatData = var1;
      }

      static ULocale access$402(Calendar.FormatConfiguration var0, ULocale var1) {
         return var0.loc = var1;
      }

      static Calendar access$502(Calendar.FormatConfiguration var0, Calendar var1) {
         return var0.cal = var1;
      }
   }

   static class PatternData {
      private String[] patterns;
      private String[] overrides;

      public PatternData(String[] var1, String[] var2) {
         this.patterns = var1;
         this.overrides = var2;
      }

      private String getDateTimePattern(int var1) {
         int var2 = 8;
         if (this.patterns.length >= 13) {
            var2 += var1 + 1;
         }

         String var3 = this.patterns[var2];
         return var3;
      }

      private static Calendar.PatternData make(Calendar var0, ULocale var1) {
         String var2 = var0.getType();
         String var3 = var1.getBaseName() + "+" + var2;
         Calendar.PatternData var4 = (Calendar.PatternData)Calendar.access$1000().get(var3);
         if (var4 == null) {
            try {
               CalendarData var5 = new CalendarData(var1, var2);
               var4 = new Calendar.PatternData(var5.getDateTimePatterns(), var5.getOverrides());
            } catch (MissingResourceException var6) {
               var4 = new Calendar.PatternData(Calendar.access$1100(), (String[])null);
            }

            Calendar.access$1000().put(var3, var4);
         }

         return var4;
      }

      static Calendar.PatternData access$600(Calendar var0, ULocale var1) {
         return make(var0, var1);
      }

      static String access$700(Calendar.PatternData var0, int var1) {
         return var0.getDateTimePattern(var1);
      }

      static String[] access$800(Calendar.PatternData var0) {
         return var0.patterns;
      }

      static String[] access$900(Calendar.PatternData var0) {
         return var0.overrides;
      }
   }

   abstract static class CalendarShim {
      abstract Locale[] getAvailableLocales();

      abstract ULocale[] getAvailableULocales();

      abstract Object registerFactory(Calendar.CalendarFactory var1);

      abstract boolean unregister(Object var1);

      abstract Calendar createInstance(ULocale var1);
   }

   abstract static class CalendarFactory {
      public boolean visible() {
         return true;
      }

      public abstract Set getSupportedLocaleNames();

      public Calendar createCalendar(ULocale var1) {
         return null;
      }

      protected CalendarFactory() {
      }
   }
}
