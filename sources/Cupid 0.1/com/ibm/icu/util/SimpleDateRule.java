package com.ibm.icu.util;

import java.util.Date;

public class SimpleDateRule implements DateRule {
  public SimpleDateRule(int month, int dayOfMonth) {
    this.calendar = gCalendar;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.dayOfWeek = 0;
  }
  
  SimpleDateRule(int month, int dayOfMonth, Calendar cal) {
    this.calendar = gCalendar;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.dayOfWeek = 0;
    this.calendar = cal;
  }
  
  public SimpleDateRule(int month, int dayOfMonth, int dayOfWeek, boolean after) {
    this.calendar = gCalendar;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.dayOfWeek = after ? dayOfWeek : -dayOfWeek;
  }
  
  public Date firstAfter(Date start) {
    return doFirstBetween(start, null);
  }
  
  public Date firstBetween(Date start, Date end) {
    return doFirstBetween(start, end);
  }
  
  public boolean isOn(Date date) {
    Calendar c = this.calendar;
    synchronized (c) {
      c.setTime(date);
      int dayOfYear = c.get(6);
      c.setTime(computeInYear(c.get(1), c));
      return (c.get(6) == dayOfYear);
    } 
  }
  
  public boolean isBetween(Date start, Date end) {
    return (firstBetween(start, end) != null);
  }
  
  private Date doFirstBetween(Date start, Date end) {
    Calendar c = this.calendar;
    synchronized (c) {
      c.setTime(start);
      int year = c.get(1);
      int mon = c.get(2);
      if (mon > this.month)
        year++; 
      Date result = computeInYear(year, c);
      if (mon == this.month && result.before(start))
        result = computeInYear(year + 1, c); 
      if (end != null && result.after(end))
        return null; 
      return result;
    } 
  }
  
  private Date computeInYear(int year, Calendar c) {
    synchronized (c) {
      c.clear();
      c.set(0, c.getMaximum(0));
      c.set(1, year);
      c.set(2, this.month);
      c.set(5, this.dayOfMonth);
      if (this.dayOfWeek != 0) {
        c.setTime(c.getTime());
        int weekday = c.get(7);
        int delta = 0;
        if (this.dayOfWeek > 0) {
          delta = (this.dayOfWeek - weekday + 7) % 7;
        } else {
          delta = -((this.dayOfWeek + weekday + 7) % 7);
        } 
        c.add(5, delta);
      } 
      return c.getTime();
    } 
  }
  
  private static GregorianCalendar gCalendar = new GregorianCalendar();
  
  private Calendar calendar;
  
  private int month;
  
  private int dayOfMonth;
  
  private int dayOfWeek;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\SimpleDateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */