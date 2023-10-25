package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class IndianCalendar extends Calendar {
  private static final long serialVersionUID = 3617859668165014834L;
  
  public static final int CHAITRA = 0;
  
  public static final int VAISAKHA = 1;
  
  public static final int JYAISTHA = 2;
  
  public static final int ASADHA = 3;
  
  public static final int SRAVANA = 4;
  
  public static final int BHADRA = 5;
  
  public static final int ASVINA = 6;
  
  public static final int KARTIKA = 7;
  
  public static final int AGRAHAYANA = 8;
  
  public static final int PAUSA = 9;
  
  public static final int MAGHA = 10;
  
  public static final int PHALGUNA = 11;
  
  public static final int IE = 0;
  
  private static final int INDIAN_ERA_START = 78;
  
  private static final int INDIAN_YEAR_START = 80;
  
  public IndianCalendar() {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public IndianCalendar(TimeZone zone) {
    this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public IndianCalendar(Locale aLocale) {
    this(TimeZone.getDefault(), aLocale);
  }
  
  public IndianCalendar(ULocale locale) {
    this(TimeZone.getDefault(), locale);
  }
  
  public IndianCalendar(TimeZone zone, Locale aLocale) {
    super(zone, aLocale);
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public IndianCalendar(TimeZone zone, ULocale locale) {
    super(zone, locale);
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public IndianCalendar(Date date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    setTime(date);
  }
  
  public IndianCalendar(int year, int month, int date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    set(1, year);
    set(2, month);
    set(5, date);
  }
  
  public IndianCalendar(int year, int month, int date, int hour, int minute, int second) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    set(1, year);
    set(2, month);
    set(5, date);
    set(11, hour);
    set(12, minute);
    set(13, second);
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
  
  protected int handleGetYearLength(int extendedYear) {
    return super.handleGetYearLength(extendedYear);
  }
  
  protected int handleGetMonthLength(int extendedYear, int month) {
    if (month < 0 || month > 11) {
      int[] remainder = new int[1];
      extendedYear += floorDivide(month, 12, remainder);
      month = remainder[0];
    } 
    if (isGregorianLeap(extendedYear + 78) && month == 0)
      return 31; 
    if (month >= 1 && month <= 5)
      return 31; 
    return 30;
  }
  
  protected void handleComputeFields(int julianDay) {
    int leapMonth, IndianMonth, IndianDayOfMonth, gregorianDay[] = jdToGregorian(julianDay);
    int IndianYear = gregorianDay[0] - 78;
    double jdAtStartOfGregYear = gregorianToJD(gregorianDay[0], 1, 1);
    int yday = (int)(julianDay - jdAtStartOfGregYear);
    if (yday < 80) {
      IndianYear--;
      leapMonth = isGregorianLeap(gregorianDay[0] - 1) ? 31 : 30;
      yday += leapMonth + 155 + 90 + 10;
    } else {
      leapMonth = isGregorianLeap(gregorianDay[0]) ? 31 : 30;
      yday -= 80;
    } 
    if (yday < leapMonth) {
      IndianMonth = 0;
      IndianDayOfMonth = yday + 1;
    } else {
      int mday = yday - leapMonth;
      if (mday < 155) {
        IndianMonth = mday / 31 + 1;
        IndianDayOfMonth = mday % 31 + 1;
      } else {
        mday -= 155;
        IndianMonth = mday / 30 + 6;
        IndianDayOfMonth = mday % 30 + 1;
      } 
    } 
    internalSet(0, 0);
    internalSet(19, IndianYear);
    internalSet(1, IndianYear);
    internalSet(2, IndianMonth);
    internalSet(5, IndianDayOfMonth);
    internalSet(6, yday + 1);
  }
  
  private static final int[][] LIMITS = new int[][] { 
      { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, {}, { 1, 1, 30, 31 }, { 1, 1, 365, 366 }, {}, { -1, -1, 5, 5 }, {}, 
      {}, {}, {}, {}, {}, {}, {}, { -5000000, -5000000, 5000000, 5000000 }, {}, { -5000000, -5000000, 5000000, 5000000 }, 
      {}, {} };
  
  protected int handleGetLimit(int field, int limitType) {
    return LIMITS[field][limitType];
  }
  
  protected int handleComputeMonthStart(int year, int month, boolean useMonth) {
    int imonth;
    if (month < 0 || month > 11) {
      year += month / 12;
      month %= 12;
    } 
    if (month == 12) {
      imonth = 1;
    } else {
      imonth = month + 1;
    } 
    double jd = IndianToJD(year, imonth, 1);
    return (int)jd;
  }
  
  private static double IndianToJD(int year, int month, int date) {
    int leapMonth;
    double start, jd;
    int gyear = year + 78;
    if (isGregorianLeap(gyear)) {
      leapMonth = 31;
      start = gregorianToJD(gyear, 3, 21);
    } else {
      leapMonth = 30;
      start = gregorianToJD(gyear, 3, 22);
    } 
    if (month == 1) {
      jd = start + (date - 1);
    } else {
      jd = start + leapMonth;
      int m = month - 2;
      m = Math.min(m, 5);
      jd += (m * 31);
      if (month >= 8) {
        m = month - 7;
        jd += (m * 30);
      } 
      jd += (date - 1);
    } 
    return jd;
  }
  
  private static double gregorianToJD(int year, int month, int date) {
    double JULIAN_EPOCH = 1721425.5D;
    int y = year - 1;
    int result = 365 * y + y / 4 - y / 100 + y / 400 + (367 * month - 362) / 12 + ((month <= 2) ? 0 : (isGregorianLeap(year) ? -1 : -2)) + date;
    return (result - 1) + JULIAN_EPOCH;
  }
  
  private static int[] jdToGregorian(double jd) {
    double JULIAN_EPOCH = 1721425.5D;
    double wjd = Math.floor(jd - 0.5D) + 0.5D;
    double depoch = wjd - JULIAN_EPOCH;
    double quadricent = Math.floor(depoch / 146097.0D);
    double dqc = depoch % 146097.0D;
    double cent = Math.floor(dqc / 36524.0D);
    double dcent = dqc % 36524.0D;
    double quad = Math.floor(dcent / 1461.0D);
    double dquad = dcent % 1461.0D;
    double yindex = Math.floor(dquad / 365.0D);
    int year = (int)(quadricent * 400.0D + cent * 100.0D + quad * 4.0D + yindex);
    if (cent != 4.0D && yindex != 4.0D)
      year++; 
    double yearday = wjd - gregorianToJD(year, 1, 1);
    double leapadj = ((wjd < gregorianToJD(year, 3, 1)) ? false : (isGregorianLeap(year) ? true : 2));
    int month = (int)Math.floor(((yearday + leapadj) * 12.0D + 373.0D) / 367.0D);
    int day = (int)(wjd - gregorianToJD(year, month, 1)) + 1;
    int[] julianDate = new int[3];
    julianDate[0] = year;
    julianDate[1] = month;
    julianDate[2] = day;
    return julianDate;
  }
  
  private static boolean isGregorianLeap(int year) {
    return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
  }
  
  public String getType() {
    return "indian";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\IndianCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */