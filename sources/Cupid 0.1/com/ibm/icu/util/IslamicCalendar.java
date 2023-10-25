package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarAstronomer;
import com.ibm.icu.impl.CalendarCache;
import java.util.Date;
import java.util.Locale;

public class IslamicCalendar extends Calendar {
  private static final long serialVersionUID = -6253365474073869325L;
  
  public static final int MUHARRAM = 0;
  
  public static final int SAFAR = 1;
  
  public static final int RABI_1 = 2;
  
  public static final int RABI_2 = 3;
  
  public static final int JUMADA_1 = 4;
  
  public static final int JUMADA_2 = 5;
  
  public static final int RAJAB = 6;
  
  public static final int SHABAN = 7;
  
  public static final int RAMADAN = 8;
  
  public static final int SHAWWAL = 9;
  
  public static final int DHU_AL_QIDAH = 10;
  
  public static final int DHU_AL_HIJJAH = 11;
  
  private static final long HIJRA_MILLIS = -42521587200000L;
  
  public IslamicCalendar() {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public IslamicCalendar(TimeZone zone) {
    this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public IslamicCalendar(Locale aLocale) {
    this(TimeZone.getDefault(), aLocale);
  }
  
  public IslamicCalendar(ULocale locale) {
    this(TimeZone.getDefault(), locale);
  }
  
  public IslamicCalendar(TimeZone zone, Locale aLocale) {
    super(zone, aLocale);
    this.civil = true;
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public IslamicCalendar(TimeZone zone, ULocale locale) {
    super(zone, locale);
    this.civil = true;
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public IslamicCalendar(Date date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    this.civil = true;
    setTime(date);
  }
  
  public IslamicCalendar(int year, int month, int date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    this.civil = true;
    set(1, year);
    set(2, month);
    set(5, date);
  }
  
  public IslamicCalendar(int year, int month, int date, int hour, int minute, int second) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    this.civil = true;
    set(1, year);
    set(2, month);
    set(5, date);
    set(11, hour);
    set(12, minute);
    set(13, second);
  }
  
  public void setCivil(boolean beCivil) {
    if (this.civil != beCivil) {
      long m = getTimeInMillis();
      this.civil = beCivil;
      clear();
      setTimeInMillis(m);
    } 
  }
  
  public boolean isCivil() {
    return this.civil;
  }
  
  private static final int[][] LIMITS = new int[][] { 
      { 0, 0, 0, 0 }, { 1, 1, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 50, 51 }, {}, { 1, 1, 29, 30 }, { 1, 1, 354, 355 }, {}, { -1, -1, 5, 5 }, {}, 
      {}, {}, {}, {}, {}, {}, {}, { 1, 1, 5000000, 5000000 }, {}, { 1, 1, 5000000, 5000000 }, 
      {}, {} };
  
  protected int handleGetLimit(int field, int limitType) {
    return LIMITS[field][limitType];
  }
  
  private static final boolean civilLeapYear(int year) {
    return ((14 + 11 * year) % 30 < 11);
  }
  
  private long yearStart(int year) {
    if (this.civil)
      return ((year - 1) * 354) + (long)Math.floor((3 + 11 * year) / 30.0D); 
    return trueMonthStart((12 * (year - 1)));
  }
  
  private long monthStart(int year, int month) {
    int realYear = year + month / 12;
    int realMonth = month % 12;
    if (this.civil)
      return (long)Math.ceil(29.5D * realMonth) + ((realYear - 1) * 354) + (long)Math.floor((3 + 11 * realYear) / 30.0D); 
    return trueMonthStart((12 * (realYear - 1) + realMonth));
  }
  
  private static final long trueMonthStart(long month) {
    long start = cache.get(month);
    if (start == CalendarCache.EMPTY) {
      long origin = -42521587200000L + (long)Math.floor(month * 29.530588853D) * 86400000L;
      double age = moonAge(origin);
      if (moonAge(origin) >= 0.0D) {
        do {
          origin -= 86400000L;
          age = moonAge(origin);
        } while (age >= 0.0D);
      } else {
        do {
          origin += 86400000L;
          age = moonAge(origin);
        } while (age < 0.0D);
      } 
      start = (origin - -42521587200000L) / 86400000L + 1L;
      cache.put(month, start);
    } 
    return start;
  }
  
  static final double moonAge(long time) {
    double age = 0.0D;
    synchronized (astro) {
      astro.setTime(time);
      age = astro.getMoonAge();
    } 
    age = age * 180.0D / Math.PI;
    if (age > 180.0D)
      age -= 360.0D; 
    return age;
  }
  
  private static CalendarAstronomer astro = new CalendarAstronomer();
  
  private static CalendarCache cache = new CalendarCache();
  
  private boolean civil;
  
  protected int handleGetMonthLength(int extendedYear, int month) {
    int length = 0;
    if (this.civil) {
      length = 29 + (month + 1) % 2;
      if (month == 11 && civilLeapYear(extendedYear))
        length++; 
    } else {
      month = 12 * (extendedYear - 1) + month;
      length = (int)(trueMonthStart((month + 1)) - trueMonthStart(month));
    } 
    return length;
  }
  
  protected int handleGetYearLength(int extendedYear) {
    if (this.civil)
      return 354 + (civilLeapYear(extendedYear) ? 1 : 0); 
    int month = 12 * (extendedYear - 1);
    return (int)(trueMonthStart((month + 12)) - trueMonthStart(month));
  }
  
  protected int handleComputeMonthStart(int eyear, int month, boolean useMonth) {
    return (int)monthStart(eyear, month) + 1948439;
  }
  
  protected int handleGetExtendedYear() {
    int year;
    if (newerField(19, 1) == 19) {
      year = internalGet(19, 1);
    } else {
      year = internalGet(1, 1);
    } 
    return year;
  }
  
  protected void handleComputeFields(int julianDay) {
    int year, month;
    long days = (julianDay - 1948440);
    if (this.civil) {
      year = (int)Math.floor((30L * days + 10646L) / 10631.0D);
      month = (int)Math.ceil((days - 29L - yearStart(year)) / 29.5D);
      month = Math.min(month, 11);
    } else {
      int months = (int)Math.floor(days / 29.530588853D);
      long monthStart = (long)Math.floor(months * 29.530588853D - 1.0D);
      if (days - monthStart >= 25L && moonAge(internalGetTimeInMillis()) > 0.0D)
        months++; 
      while ((monthStart = trueMonthStart(months)) > days)
        months--; 
      year = months / 12 + 1;
      month = months % 12;
    } 
    int dayOfMonth = (int)(days - monthStart(year, month)) + 1;
    int dayOfYear = (int)(days - monthStart(year, 0) + 1L);
    internalSet(0, 0);
    internalSet(1, year);
    internalSet(19, year);
    internalSet(2, month);
    internalSet(5, dayOfMonth);
    internalSet(6, dayOfYear);
  }
  
  public String getType() {
    if (this.civil)
      return "islamic-civil"; 
    return "islamic";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\IslamicCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */