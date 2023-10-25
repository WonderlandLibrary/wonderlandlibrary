package com.ibm.icu.util;

import java.util.Date;

public class DangiCalendar extends ChineseCalendar {
  private static final long serialVersionUID = 8156297445349501985L;
  
  private static final int DANGI_EPOCH_YEAR = -2332;
  
  private static final TimeZone KOREA_ZONE;
  
  static {
    InitialTimeZoneRule initialTimeZone = new InitialTimeZoneRule("GMT+8", 28800000, 0);
    long[] millis1897 = { -2302128000000L };
    long[] millis1898 = { -2270592000000L };
    long[] millis1912 = { -1829088000000L };
    TimeZoneRule rule1897 = new TimeArrayTimeZoneRule("Korean 1897", 25200000, 0, millis1897, 1);
    TimeZoneRule rule1898to1911 = new TimeArrayTimeZoneRule("Korean 1898-1911", 28800000, 0, millis1898, 1);
    TimeZoneRule ruleFrom1912 = new TimeArrayTimeZoneRule("Korean 1912-", 32400000, 0, millis1912, 1);
    RuleBasedTimeZone tz = new RuleBasedTimeZone("KOREA_ZONE", initialTimeZone);
    tz.addTransitionRule(rule1897);
    tz.addTransitionRule(rule1898to1911);
    tz.addTransitionRule(ruleFrom1912);
    tz.freeze();
    KOREA_ZONE = tz;
  }
  
  public DangiCalendar() {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public DangiCalendar(Date date) {
    this(TimeZone.getDefault(), ULocale.getDefault(ULocale.Category.FORMAT));
    setTime(date);
  }
  
  public DangiCalendar(TimeZone zone, ULocale locale) {
    super(zone, locale, -2332, KOREA_ZONE);
  }
  
  public String getType() {
    return "dangi";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\DangiCalendar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */