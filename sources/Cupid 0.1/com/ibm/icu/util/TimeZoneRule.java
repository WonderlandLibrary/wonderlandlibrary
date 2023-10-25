package com.ibm.icu.util;

import java.io.Serializable;
import java.util.Date;

public abstract class TimeZoneRule implements Serializable {
  private static final long serialVersionUID = 6374143828553768100L;
  
  private final String name;
  
  private final int rawOffset;
  
  private final int dstSavings;
  
  public TimeZoneRule(String name, int rawOffset, int dstSavings) {
    this.name = name;
    this.rawOffset = rawOffset;
    this.dstSavings = dstSavings;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getRawOffset() {
    return this.rawOffset;
  }
  
  public int getDSTSavings() {
    return this.dstSavings;
  }
  
  public boolean isEquivalentTo(TimeZoneRule other) {
    if (this.rawOffset == other.rawOffset && this.dstSavings == other.dstSavings)
      return true; 
    return false;
  }
  
  public abstract Date getFirstStart(int paramInt1, int paramInt2);
  
  public abstract Date getFinalStart(int paramInt1, int paramInt2);
  
  public abstract Date getNextStart(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public abstract Date getPreviousStart(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public abstract boolean isTransitionRule();
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("name=" + this.name);
    buf.append(", stdOffset=" + this.rawOffset);
    buf.append(", dstSaving=" + this.dstSavings);
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\TimeZoneRule.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */