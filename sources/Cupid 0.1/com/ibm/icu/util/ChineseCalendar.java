package com.ibm.icu.util;

import com.ibm.icu.impl.CalendarAstronomer;
import com.ibm.icu.impl.CalendarCache;
import com.ibm.icu.text.DateFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Locale;

public class ChineseCalendar extends Calendar {
  private static final long serialVersionUID = 7312110751940929420L;
  
  private int epochYear;
  
  private TimeZone zoneAstro;
  
  private transient CalendarAstronomer astro = new CalendarAstronomer();
  
  private transient CalendarCache winterSolsticeCache = new CalendarCache();
  
  private transient CalendarCache newYearCache = new CalendarCache();
  
  private transient boolean isLeapYear;
  
  public ChineseCalendar() {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
  }
  
  public ChineseCalendar(Date date) {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
    setTime(date);
  }
  
  public ChineseCalendar(int year, int month, int isLeapMonth, int date) {
    this(year, month, isLeapMonth, date, 0, 0, 0);
  }
  
  public ChineseCalendar(int year, int month, int isLeapMonth, int date, int hour, int minute, int second) {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
    set(14, 0);
    set(1, year);
    set(2, month);
    set(22, isLeapMonth);
    set(5, date);
    set(11, hour);
    set(12, minute);
    set(13, second);
  }
  
  public ChineseCalendar(int era, int year, int month, int isLeapMonth, int date) {
    this(era, year, month, isLeapMonth, 0, 0, 0);
  }
  
  public ChineseCalendar(int era, int year, int month, int isLeapMonth, int date, int hour, int minute, int second) {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
    set(14, 0);
    set(0, era);
    set(1, year);
    set(2, month);
    set(22, isLeapMonth);
    set(5, date);
    set(11, hour);
    set(12, minute);
    set(13, second);
  }
  
  public ChineseCalendar(Locale aLocale) {
    this(TimeZone.getDefault(), ULocale.forLocale(aLocale), -2636, CHINA_ZONE);
  }
  
  public ChineseCalendar(TimeZone zone) {
    this(zone, ULocale.getDefault(ULocale.Category.FORMAT), -2636, CHINA_ZONE);
  }
  
  public ChineseCalendar(TimeZone zone, Locale aLocale) {
    this(zone, ULocale.forLocale(aLocale), -2636, CHINA_ZONE);
  }
  
  public ChineseCalendar(ULocale locale) {
    this(TimeZone.getDefault(), locale, -2636, CHINA_ZONE);
  }
  
  public ChineseCalendar(TimeZone zone, ULocale locale) {
    this(zone, locale, -2636, CHINA_ZONE);
  }
  
  protected ChineseCalendar(TimeZone zone, ULocale locale, int epochYear, TimeZone zoneAstroCalc) {
    super(zone, locale);
    this.epochYear = epochYear;
    this.zoneAstro = zoneAstroCalc;
    setTimeInMillis(System.currentTimeMillis());
  }
  
  private static final int[][] LIMITS = new int[][] { 
      { 1, 1, 83333, 83333 }, { 1, 1, 60, 60 }, { 0, 0, 11, 11 }, { 1, 1, 50, 55 }, {}, { 1, 1, 29, 30 }, { 1, 1, 353, 385 }, {}, { -1, -1, 5, 5 }, {}, 
      {}, {}, {}, {}, {}, {}, {}, { -5000000, -5000000, 5000000, 5000000 }, {}, { -5000000, -5000000, 5000000, 5000000 }, 
      {}, {}, { 0, 0, 1, 1 } };
  
  protected int handleGetLimit(int field, int limitType) {
    return LIMITS[field][limitType];
  }
  
  protected int handleGetExtendedYear() {
    int year;
    if (newestStamp(0, 1, 0) <= getStamp(19)) {
      year = internalGet(19, 1);
    } else {
      int cycle = internalGet(0, 1) - 1;
      year = cycle * 60 + internalGet(1, 1) - this.epochYear - -2636;
    } 
    return year;
  }
  
  protected int handleGetMonthLength(int extendedYear, int month) {
    int thisStart = handleComputeMonthStart(extendedYear, month, true) - 2440588 + 1;
    int nextStart = newMoonNear(thisStart + 25, true);
    return nextStart - thisStart;
  }
  
  protected DateFormat handleGetDateFormat(String pattern, String override, ULocale locale) {
    return super.handleGetDateFormat(pattern, override, locale);
  }
  
  static final int[][][] CHINESE_DATE_PRECEDENCE = new int[][][] { { { 5 }, { 3, 7 }, { 4, 7 }, { 8, 7 }, { 3, 18 }, { 4, 18 }, { 8, 18 }, { 6 }, { 37, 22 } }, { { 3 }, { 4 }, { 8 }, { 40, 7 }, { 40, 18 } } };
  
  private static final int CHINESE_EPOCH_YEAR = -2636;
  
  protected int[][][] getFieldResolutionTable() {
    return CHINESE_DATE_PRECEDENCE;
  }
  
  private void offsetMonth(int newMoon, int dom, int delta) {
    newMoon += (int)(29.530588853D * (delta - 0.5D));
    newMoon = newMoonNear(newMoon, true);
    int jd = newMoon + 2440588 - 1 + dom;
    if (dom > 29) {
      set(20, jd - 1);
      complete();
      if (getActualMaximum(5) >= dom)
        set(20, jd); 
    } else {
      set(20, jd);
    } 
  }
  
  public void add(int field, int amount) {
    switch (field) {
      case 2:
        if (amount != 0) {
          int dom = get(5);
          int day = get(20) - 2440588;
          int moon = day - dom + 1;
          offsetMonth(moon, dom, amount);
        } 
        return;
    } 
    super.add(field, amount);
  }
  
  public void roll(int field, int amount) {
    switch (field) {
      case 2:
        if (amount != 0) {
          int dom = get(5);
          int day = get(20) - 2440588;
          int moon = day - dom + 1;
          int m = get(2);
          if (this.isLeapYear)
            if (get(22) == 1) {
              m++;
            } else {
              int moon1 = moon - (int)(29.530588853D * (m - 0.5D));
              moon1 = newMoonNear(moon1, true);
              if (isLeapMonthBetween(moon1, moon))
                m++; 
            }  
          int n = this.isLeapYear ? 13 : 12;
          int newM = (m + amount) % n;
          if (newM < 0)
            newM += n; 
          if (newM != m)
            offsetMonth(moon, dom, newM - m); 
        } 
        return;
    } 
    super.roll(field, amount);
  }
  
  private static final TimeZone CHINA_ZONE = (new SimpleTimeZone(28800000, "CHINA_ZONE")).freeze();
  
  private static final int SYNODIC_GAP = 25;
  
  private final long daysToMillis(int days) {
    long millis = days * 86400000L;
    return millis - this.zoneAstro.getOffset(millis);
  }
  
  private final int millisToDays(long millis) {
    return (int)floorDivide(millis + this.zoneAstro.getOffset(millis), 86400000L);
  }
  
  private int winterSolstice(int gyear) {
    long cacheValue = this.winterSolsticeCache.get(gyear);
    if (cacheValue == CalendarCache.EMPTY) {
      long ms = daysToMillis(computeGregorianMonthStart(gyear, 11) + 1 - 2440588);
      this.astro.setTime(ms);
      long solarLong = this.astro.getSunTime(CalendarAstronomer.WINTER_SOLSTICE, true);
      cacheValue = millisToDays(solarLong);
      this.winterSolsticeCache.put(gyear, cacheValue);
    } 
    return (int)cacheValue;
  }
  
  private int newMoonNear(int days, boolean after) {
    this.astro.setTime(daysToMillis(days));
    long newMoon = this.astro.getMoonTime(CalendarAstronomer.NEW_MOON, after);
    return millisToDays(newMoon);
  }
  
  private int synodicMonthsBetween(int day1, int day2) {
    return (int)Math.round((day2 - day1) / 29.530588853D);
  }
  
  private int majorSolarTerm(int days) {
    this.astro.setTime(daysToMillis(days));
    int term = ((int)Math.floor(6.0D * this.astro.getSunLongitude() / Math.PI) + 2) % 12;
    if (term < 1)
      term += 12; 
    return term;
  }
  
  private boolean hasNoMajorSolarTerm(int newMoon) {
    int mst = majorSolarTerm(newMoon);
    int nmn = newMoonNear(newMoon + 25, true);
    int mstt = majorSolarTerm(nmn);
    return (mst == mstt);
  }
  
  private boolean isLeapMonthBetween(int newMoon1, int newMoon2) {
    if (synodicMonthsBetween(newMoon1, newMoon2) >= 50)
      throw new IllegalArgumentException("isLeapMonthBetween(" + newMoon1 + ", " + newMoon2 + "): Invalid parameters"); 
    return (newMoon2 >= newMoon1 && (isLeapMonthBetween(newMoon1, newMoonNear(newMoon2 - 25, false)) || hasNoMajorSolarTerm(newMoon2)));
  }
  
  protected void handleComputeFields(int julianDay) {
    computeChineseFields(julianDay - 2440588, getGregorianYear(), getGregorianMonth(), true);
  }
  
  private void computeChineseFields(int days, int gyear, int gmonth, boolean setAllFields) {
    int solsticeBefore, solsticeAfter = winterSolstice(gyear);
    if (days < solsticeAfter) {
      solsticeBefore = winterSolstice(gyear - 1);
    } else {
      solsticeBefore = solsticeAfter;
      solsticeAfter = winterSolstice(gyear + 1);
    } 
    int firstMoon = newMoonNear(solsticeBefore + 1, true);
    int lastMoon = newMoonNear(solsticeAfter + 1, false);
    int thisMoon = newMoonNear(days + 1, false);
    this.isLeapYear = (synodicMonthsBetween(firstMoon, lastMoon) == 12);
    int month = synodicMonthsBetween(firstMoon, thisMoon);
    if (this.isLeapYear && isLeapMonthBetween(firstMoon, thisMoon))
      month--; 
    if (month < 1)
      month += 12; 
    boolean isLeapMonth = (this.isLeapYear && hasNoMajorSolarTerm(thisMoon) && !isLeapMonthBetween(firstMoon, newMoonNear(thisMoon - 25, false)));
    internalSet(2, month - 1);
    internalSet(22, isLeapMonth ? 1 : 0);
    if (setAllFields) {
      int extended_year = gyear - this.epochYear;
      int cycle_year = gyear - -2636;
      if (month < 11 || gmonth >= 6) {
        extended_year++;
        cycle_year++;
      } 
      int dayOfMonth = days - thisMoon + 1;
      internalSet(19, extended_year);
      int[] yearOfCycle = new int[1];
      int cycle = floorDivide(cycle_year - 1, 60, yearOfCycle);
      internalSet(0, cycle + 1);
      internalSet(1, yearOfCycle[0] + 1);
      internalSet(5, dayOfMonth);
      int newYear = newYear(gyear);
      if (days < newYear)
        newYear = newYear(gyear - 1); 
      internalSet(6, days - newYear + 1);
    } 
  }
  
  private int newYear(int gyear) {
    long cacheValue = this.newYearCache.get(gyear);
    if (cacheValue == CalendarCache.EMPTY) {
      int solsticeBefore = winterSolstice(gyear - 1);
      int solsticeAfter = winterSolstice(gyear);
      int newMoon1 = newMoonNear(solsticeBefore + 1, true);
      int newMoon2 = newMoonNear(newMoon1 + 25, true);
      int newMoon11 = newMoonNear(solsticeAfter + 1, false);
      if (synodicMonthsBetween(newMoon1, newMoon11) == 12 && (hasNoMajorSolarTerm(newMoon1) || hasNoMajorSolarTerm(newMoon2))) {
        cacheValue = newMoonNear(newMoon2 + 25, true);
      } else {
        cacheValue = newMoon2;
      } 
      this.newYearCache.put(gyear, cacheValue);
    } 
    return (int)cacheValue;
  }
  
  protected int handleComputeMonthStart(int eyear, int month, boolean useMonth) {
    if (month < 0 || month > 11) {
      int[] rem = new int[1];
      eyear += floorDivide(month, 12, rem);
      month = rem[0];
    } 
    int gyear = eyear + this.epochYear - 1;
    int newYear = newYear(gyear);
    int newMoon = newMoonNear(newYear + month * 29, true);
    int julianDay = newMoon + 2440588;
    int saveMonth = internalGet(2);
    int saveIsLeapMonth = internalGet(22);
    int isLeapMonth = useMonth ? saveIsLeapMonth : 0;
    computeGregorianFields(julianDay);
    computeChineseFields(newMoon, getGregorianYear(), getGregorianMonth(), false);
    if (month != internalGet(2) || isLeapMonth != internalGet(22)) {
      newMoon = newMoonNear(newMoon + 25, true);
      julianDay = newMoon + 2440588;
    } 
    internalSet(2, saveMonth);
    internalSet(22, saveIsLeapMonth);
    return julianDay - 1;
  }
  
  public String getType() {
    return "chinese";
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    this.epochYear = -2636;
    this.zoneAstro = CHINA_ZONE;
    stream.defaultReadObject();
    this.astro = new CalendarAstronomer();
    this.winterSolsticeCache = new CalendarCache();
    this.newYearCache = new CalendarCache();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\ChineseCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */