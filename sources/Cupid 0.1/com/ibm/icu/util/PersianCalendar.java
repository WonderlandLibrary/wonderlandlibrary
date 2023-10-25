package com.ibm.icu.util;

import java.util.Date;
import java.util.Locale;

public class PersianCalendar extends Calendar {
  private static final long serialVersionUID = -6727306982975111643L;
  
  private static final int[][] MONTH_COUNT = new int[][] { 
      { 31, 31, 0 }, { 31, 31, 31 }, { 31, 31, 62 }, { 31, 31, 93 }, { 31, 31, 124 }, { 31, 31, 155 }, { 30, 30, 186 }, { 30, 30, 216 }, { 30, 30, 246 }, { 30, 30, 276 }, 
      { 30, 30, 306 }, { 29, 30, 336 } };
  
  private static final int PERSIAN_EPOCH = 1948320;
  
  public PersianCalendar() {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public PersianCalendar(TimeZone zone) {
    this(zone, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public PersianCalendar(Locale aLocale) {
    this(TimeZone.getDefault(), aLocale);
  }
  
  public PersianCalendar(ULocale locale) {
    this(TimeZone.getDefault(), locale);
  }
  
  public PersianCalendar(TimeZone zone, Locale aLocale) {
    super(zone, aLocale);
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public PersianCalendar(TimeZone zone, ULocale locale) {
    super(zone, locale);
    setTimeInMillis(System.currentTimeMillis());
  }
  
  public PersianCalendar(Date date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    setTime(date);
  }
  
  public PersianCalendar(int year, int month, int date) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    set(1, year);
    set(2, month);
    set(5, date);
  }
  
  public PersianCalendar(int year, int month, int date, int hour, int minute, int second) {
    super(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    set(1, year);
    set(2, month);
    set(5, date);
    set(11, hour);
    set(12, minute);
    set(13, second);
  }
  
  private static final int[][] LIMITS = new int[][] { 
      { 0, 0, 0, 0 }, { -5000000, -5000000, 5000000, 5000000 }, { 0, 0, 11, 11 }, { 1, 1, 52, 53 }, {}, { 1, 1, 29, 31 }, { 1, 1, 365, 366 }, {}, { -1, -1, 5, 5 }, {}, 
      {}, {}, {}, {}, {}, {}, {}, { -5000000, -5000000, 5000000, 5000000 }, {}, { -5000000, -5000000, 5000000, 5000000 }, 
      {}, {} };
  
  protected int handleGetLimit(int field, int limitType) {
    return LIMITS[field][limitType];
  }
  
  private static final boolean isLeapYear(int year) {
    int[] remainder = new int[1];
    floorDivide(25 * year + 11, 33, remainder);
    return (remainder[0] < 8);
  }
  
  protected int handleGetMonthLength(int extendedYear, int month) {
    if (month < 0 || month > 11) {
      int[] rem = new int[1];
      extendedYear += floorDivide(month, 12, rem);
      month = rem[0];
    } 
    return MONTH_COUNT[month][isLeapYear(extendedYear) ? 1 : 0];
  }
  
  protected int handleGetYearLength(int extendedYear) {
    return isLeapYear(extendedYear) ? 366 : 365;
  }
  
  protected int handleComputeMonthStart(int eyear, int month, boolean useMonth) {
    if (month < 0 || month > 11) {
      int[] rem = new int[1];
      eyear += floorDivide(month, 12, rem);
      month = rem[0];
    } 
    int julianDay = 1948319 + 365 * (eyear - 1) + floorDivide(8 * eyear + 21, 33);
    if (month != 0)
      julianDay += MONTH_COUNT[month][2]; 
    return julianDay;
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
    int month;
    long daysSinceEpoch = (julianDay - 1948320);
    int year = 1 + (int)floorDivide(33L * daysSinceEpoch + 3L, 12053L);
    long farvardin1 = (365 * (year - 1) + floorDivide(8 * year + 21, 33));
    int dayOfYear = (int)(daysSinceEpoch - farvardin1);
    if (dayOfYear < 216) {
      month = dayOfYear / 31;
    } else {
      month = (dayOfYear - 6) / 30;
    } 
    int dayOfMonth = dayOfYear - MONTH_COUNT[month][2] + 1;
    dayOfYear++;
    internalSet(0, 0);
    internalSet(1, year);
    internalSet(19, year);
    internalSet(2, month);
    internalSet(5, dayOfMonth);
    internalSet(6, dayOfYear);
  }
  
  public String getType() {
    return "persian";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\PersianCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */