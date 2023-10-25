package com.ibm.icu.impl.duration;

public final class Period {
  final byte timeLimit;
  
  final boolean inFuture;
  
  final int[] counts;
  
  public static Period at(float count, TimeUnit unit) {
    checkCount(count);
    return new Period(0, false, count, unit);
  }
  
  public static Period moreThan(float count, TimeUnit unit) {
    checkCount(count);
    return new Period(2, false, count, unit);
  }
  
  public static Period lessThan(float count, TimeUnit unit) {
    checkCount(count);
    return new Period(1, false, count, unit);
  }
  
  public Period and(float count, TimeUnit unit) {
    checkCount(count);
    return setTimeUnitValue(unit, count);
  }
  
  public Period omit(TimeUnit unit) {
    return setTimeUnitInternalValue(unit, 0);
  }
  
  public Period at() {
    return setTimeLimit((byte)0);
  }
  
  public Period moreThan() {
    return setTimeLimit((byte)2);
  }
  
  public Period lessThan() {
    return setTimeLimit((byte)1);
  }
  
  public Period inFuture() {
    return setFuture(true);
  }
  
  public Period inPast() {
    return setFuture(false);
  }
  
  public Period inFuture(boolean future) {
    return setFuture(future);
  }
  
  public Period inPast(boolean past) {
    return setFuture(!past);
  }
  
  public boolean isSet() {
    for (int i = 0; i < this.counts.length; i++) {
      if (this.counts[i] != 0)
        return true; 
    } 
    return false;
  }
  
  public boolean isSet(TimeUnit unit) {
    return (this.counts[unit.ordinal] > 0);
  }
  
  public float getCount(TimeUnit unit) {
    int ord = unit.ordinal;
    if (this.counts[ord] == 0)
      return 0.0F; 
    return (this.counts[ord] - 1) / 1000.0F;
  }
  
  public boolean isInFuture() {
    return this.inFuture;
  }
  
  public boolean isInPast() {
    return !this.inFuture;
  }
  
  public boolean isMoreThan() {
    return (this.timeLimit == 2);
  }
  
  public boolean isLessThan() {
    return (this.timeLimit == 1);
  }
  
  public boolean equals(Object rhs) {
    try {
      return equals((Period)rhs);
    } catch (ClassCastException e) {
      return false;
    } 
  }
  
  public boolean equals(Period rhs) {
    if (rhs != null && this.timeLimit == rhs.timeLimit && this.inFuture == rhs.inFuture) {
      for (int i = 0; i < this.counts.length; i++) {
        if (this.counts[i] != rhs.counts[i])
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  public int hashCode() {
    int hc = this.timeLimit << 1 | (this.inFuture ? 1 : 0);
    for (int i = 0; i < this.counts.length; i++)
      hc = hc << 2 ^ this.counts[i]; 
    return hc;
  }
  
  private Period(int limit, boolean future, float count, TimeUnit unit) {
    this.timeLimit = (byte)limit;
    this.inFuture = future;
    this.counts = new int[TimeUnit.units.length];
    this.counts[unit.ordinal] = (int)(count * 1000.0F) + 1;
  }
  
  Period(int timeLimit, boolean inFuture, int[] counts) {
    this.timeLimit = (byte)timeLimit;
    this.inFuture = inFuture;
    this.counts = counts;
  }
  
  private Period setTimeUnitValue(TimeUnit unit, float value) {
    if (value < 0.0F)
      throw new IllegalArgumentException("value: " + value); 
    return setTimeUnitInternalValue(unit, (int)(value * 1000.0F) + 1);
  }
  
  private Period setTimeUnitInternalValue(TimeUnit unit, int value) {
    int ord = unit.ordinal;
    if (this.counts[ord] != value) {
      int[] newCounts = new int[this.counts.length];
      for (int i = 0; i < this.counts.length; i++)
        newCounts[i] = this.counts[i]; 
      newCounts[ord] = value;
      return new Period(this.timeLimit, this.inFuture, newCounts);
    } 
    return this;
  }
  
  private Period setFuture(boolean future) {
    if (this.inFuture != future)
      return new Period(this.timeLimit, future, this.counts); 
    return this;
  }
  
  private Period setTimeLimit(byte limit) {
    if (this.timeLimit != limit)
      return new Period(limit, this.inFuture, this.counts); 
    return this;
  }
  
  private static void checkCount(float count) {
    if (count < 0.0F)
      throw new IllegalArgumentException("count (" + count + ") cannot be negative"); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\Period.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */