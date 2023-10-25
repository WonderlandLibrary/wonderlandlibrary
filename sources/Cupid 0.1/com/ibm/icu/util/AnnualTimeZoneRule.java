package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.Date;

public class AnnualTimeZoneRule extends TimeZoneRule {
  private static final long serialVersionUID = -8870666707791230688L;
  
  public static final int MAX_YEAR = 2147483647;
  
  private final DateTimeRule dateTimeRule;
  
  private final int startYear;
  
  private final int endYear;
  
  public AnnualTimeZoneRule(String name, int rawOffset, int dstSavings, DateTimeRule dateTimeRule, int startYear, int endYear) {
    super(name, rawOffset, dstSavings);
    this.dateTimeRule = dateTimeRule;
    this.startYear = startYear;
    this.endYear = (endYear > Integer.MAX_VALUE) ? Integer.MAX_VALUE : endYear;
  }
  
  public DateTimeRule getRule() {
    return this.dateTimeRule;
  }
  
  public int getStartYear() {
    return this.startYear;
  }
  
  public int getEndYear() {
    return this.endYear;
  }
  
  public Date getStartInYear(int year, int prevRawOffset, int prevDSTSavings) {
    long ruleDay;
    if (year < this.startYear || year > this.endYear)
      return null; 
    int type = this.dateTimeRule.getDateRuleType();
    if (type == 0) {
      ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), this.dateTimeRule.getRuleDayOfMonth());
    } else {
      boolean after = true;
      if (type == 1) {
        int weeks = this.dateTimeRule.getRuleWeekInMonth();
        if (weeks > 0) {
          ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), 1);
          ruleDay += (7 * (weeks - 1));
        } else {
          after = false;
          ruleDay = Grego.fieldsToDay(year, this.dateTimeRule.getRuleMonth(), Grego.monthLength(year, this.dateTimeRule.getRuleMonth()));
          ruleDay += (7 * (weeks + 1));
        } 
      } else {
        int month = this.dateTimeRule.getRuleMonth();
        int dom = this.dateTimeRule.getRuleDayOfMonth();
        if (type == 3) {
          after = false;
          if (month == 1 && dom == 29 && !Grego.isLeapYear(year))
            dom--; 
        } 
        ruleDay = Grego.fieldsToDay(year, month, dom);
      } 
      int dow = Grego.dayOfWeek(ruleDay);
      int delta = this.dateTimeRule.getRuleDayOfWeek() - dow;
      if (after) {
        delta = (delta < 0) ? (delta + 7) : delta;
      } else {
        delta = (delta > 0) ? (delta - 7) : delta;
      } 
      ruleDay += delta;
    } 
    long ruleTime = ruleDay * 86400000L + this.dateTimeRule.getRuleMillisInDay();
    if (this.dateTimeRule.getTimeRuleType() != 2)
      ruleTime -= prevRawOffset; 
    if (this.dateTimeRule.getTimeRuleType() == 0)
      ruleTime -= prevDSTSavings; 
    return new Date(ruleTime);
  }
  
  public Date getFirstStart(int prevRawOffset, int prevDSTSavings) {
    return getStartInYear(this.startYear, prevRawOffset, prevDSTSavings);
  }
  
  public Date getFinalStart(int prevRawOffset, int prevDSTSavings) {
    if (this.endYear == Integer.MAX_VALUE)
      return null; 
    return getStartInYear(this.endYear, prevRawOffset, prevDSTSavings);
  }
  
  public Date getNextStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive) {
    int[] fields = Grego.timeToFields(base, null);
    int year = fields[0];
    if (year < this.startYear)
      return getFirstStart(prevRawOffset, prevDSTSavings); 
    Date d = getStartInYear(year, prevRawOffset, prevDSTSavings);
    if (d != null && (d.getTime() < base || (!inclusive && d.getTime() == base)))
      d = getStartInYear(year + 1, prevRawOffset, prevDSTSavings); 
    return d;
  }
  
  public Date getPreviousStart(long base, int prevRawOffset, int prevDSTSavings, boolean inclusive) {
    int[] fields = Grego.timeToFields(base, null);
    int year = fields[0];
    if (year > this.endYear)
      return getFinalStart(prevRawOffset, prevDSTSavings); 
    Date d = getStartInYear(year, prevRawOffset, prevDSTSavings);
    if (d != null && (d.getTime() > base || (!inclusive && d.getTime() == base)))
      d = getStartInYear(year - 1, prevRawOffset, prevDSTSavings); 
    return d;
  }
  
  public boolean isEquivalentTo(TimeZoneRule other) {
    if (!(other instanceof AnnualTimeZoneRule))
      return false; 
    AnnualTimeZoneRule otherRule = (AnnualTimeZoneRule)other;
    if (this.startYear == otherRule.startYear && this.endYear == otherRule.endYear && this.dateTimeRule.equals(otherRule.dateTimeRule))
      return super.isEquivalentTo(other); 
    return false;
  }
  
  public boolean isTransitionRule() {
    return true;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(super.toString());
    buf.append(", rule={" + this.dateTimeRule + "}");
    buf.append(", startYear=" + this.startYear);
    buf.append(", endYear=");
    if (this.endYear == Integer.MAX_VALUE) {
      buf.append("max");
    } else {
      buf.append(this.endYear);
    } 
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\AnnualTimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */