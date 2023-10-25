package com.ibm.icu.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RangeDateRule implements DateRule {
  public void add(DateRule rule) {
    add(new Date(Long.MIN_VALUE), rule);
  }
  
  public void add(Date start, DateRule rule) {
    this.ranges.add(new Range(start, rule));
  }
  
  public Date firstAfter(Date start) {
    int index = startIndex(start);
    if (index == this.ranges.size())
      index = 0; 
    Date result = null;
    Range r = rangeAt(index);
    Range e = rangeAt(index + 1);
    if (r != null && r.rule != null)
      if (e != null) {
        result = r.rule.firstBetween(start, e.start);
      } else {
        result = r.rule.firstAfter(start);
      }  
    return result;
  }
  
  public Date firstBetween(Date start, Date end) {
    if (end == null)
      return firstAfter(start); 
    int index = startIndex(start);
    Date result = null;
    Range next = rangeAt(index);
    while (result == null && next != null && !next.start.after(end)) {
      Range r = next;
      next = rangeAt(index + 1);
      if (r.rule != null) {
        Date e = (next != null && !next.start.after(end)) ? next.start : end;
        result = r.rule.firstBetween(start, e);
      } 
    } 
    return result;
  }
  
  public boolean isOn(Date date) {
    Range r = rangeAt(startIndex(date));
    return (r != null && r.rule != null && r.rule.isOn(date));
  }
  
  public boolean isBetween(Date start, Date end) {
    return (firstBetween(start, end) == null);
  }
  
  private int startIndex(Date start) {
    int lastIndex = this.ranges.size();
    for (int i = 0; i < this.ranges.size(); i++) {
      Range r = this.ranges.get(i);
      if (start.before(r.start))
        break; 
      lastIndex = i;
    } 
    return lastIndex;
  }
  
  private Range rangeAt(int index) {
    return (index < this.ranges.size()) ? this.ranges.get(index) : null;
  }
  
  List<Range> ranges = new ArrayList<Range>(2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\RangeDateRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */