package com.ibm.icu.util;

public class TimeZoneTransition {
  private final TimeZoneRule from;
  
  private final TimeZoneRule to;
  
  private final long time;
  
  public TimeZoneTransition(long time, TimeZoneRule from, TimeZoneRule to) {
    this.time = time;
    this.from = from;
    this.to = to;
  }
  
  public long getTime() {
    return this.time;
  }
  
  public TimeZoneRule getTo() {
    return this.to;
  }
  
  public TimeZoneRule getFrom() {
    return this.from;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("time=" + this.time);
    buf.append(", from={" + this.from + "}");
    buf.append(", to={" + this.to + "}");
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\TimeZoneTransition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */