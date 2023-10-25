package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import java.util.TimeZone;

class BasicPeriodBuilderFactory implements PeriodBuilderFactory {
  private PeriodFormatterDataService ds;
  
  private Settings settings;
  
  private static final short allBits = 255;
  
  BasicPeriodBuilderFactory(PeriodFormatterDataService ds) {
    this.ds = ds;
    this.settings = new Settings();
  }
  
  static long approximateDurationOf(TimeUnit unit) {
    return TimeUnit.approxDurations[unit.ordinal];
  }
  
  class Settings {
    boolean inUse;
    
    short uset = 255;
    
    TimeUnit maxUnit = TimeUnit.YEAR;
    
    TimeUnit minUnit = TimeUnit.MILLISECOND;
    
    int maxLimit;
    
    int minLimit;
    
    boolean allowZero = true;
    
    boolean weeksAloneOnly;
    
    boolean allowMillis = true;
    
    Settings setUnits(int uset) {
      if (this.uset == uset)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.uset = (short)uset;
      if ((uset & 0xFF) == 255) {
        result.uset = 255;
        result.maxUnit = TimeUnit.YEAR;
        result.minUnit = TimeUnit.MILLISECOND;
      } else {
        int lastUnit = -1;
        for (int i = 0; i < TimeUnit.units.length; i++) {
          if (0 != (uset & 1 << i)) {
            if (lastUnit == -1)
              result.maxUnit = TimeUnit.units[i]; 
            lastUnit = i;
          } 
        } 
        if (lastUnit == -1) {
          result.minUnit = result.maxUnit = null;
        } else {
          result.minUnit = TimeUnit.units[lastUnit];
        } 
      } 
      return result;
    }
    
    short effectiveSet() {
      if (this.allowMillis)
        return this.uset; 
      return (short)(this.uset & (1 << TimeUnit.MILLISECOND.ordinal ^ 0xFFFFFFFF));
    }
    
    TimeUnit effectiveMinUnit() {
      if (this.allowMillis || this.minUnit != TimeUnit.MILLISECOND)
        return this.minUnit; 
      for (int i = TimeUnit.units.length - 1; --i >= 0;) {
        if (0 != (this.uset & 1 << i))
          return TimeUnit.units[i]; 
      } 
      return TimeUnit.SECOND;
    }
    
    Settings setMaxLimit(float maxLimit) {
      int val = (maxLimit <= 0.0F) ? 0 : (int)(maxLimit * 1000.0F);
      if (maxLimit == val)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.maxLimit = val;
      return result;
    }
    
    Settings setMinLimit(float minLimit) {
      int val = (minLimit <= 0.0F) ? 0 : (int)(minLimit * 1000.0F);
      if (minLimit == val)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.minLimit = val;
      return result;
    }
    
    Settings setAllowZero(boolean allow) {
      if (this.allowZero == allow)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.allowZero = allow;
      return result;
    }
    
    Settings setWeeksAloneOnly(boolean weeksAlone) {
      if (this.weeksAloneOnly == weeksAlone)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.weeksAloneOnly = weeksAlone;
      return result;
    }
    
    Settings setAllowMilliseconds(boolean allowMillis) {
      if (this.allowMillis == allowMillis)
        return this; 
      Settings result = this.inUse ? copy() : this;
      result.allowMillis = allowMillis;
      return result;
    }
    
    Settings setLocale(String localeName) {
      PeriodFormatterData data = BasicPeriodBuilderFactory.this.ds.get(localeName);
      return setAllowZero(data.allowZero()).setWeeksAloneOnly(data.weeksAloneOnly()).setAllowMilliseconds((data.useMilliseconds() != 1));
    }
    
    Settings setInUse() {
      this.inUse = true;
      return this;
    }
    
    Period createLimited(long duration, boolean inPast) {
      if (this.maxLimit > 0) {
        long maxUnitDuration = BasicPeriodBuilderFactory.approximateDurationOf(this.maxUnit);
        if (duration * 1000L > this.maxLimit * maxUnitDuration)
          return Period.moreThan(this.maxLimit / 1000.0F, this.maxUnit).inPast(inPast); 
      } 
      if (this.minLimit > 0) {
        TimeUnit emu = effectiveMinUnit();
        long emud = BasicPeriodBuilderFactory.approximateDurationOf(emu);
        long eml = (emu == this.minUnit) ? this.minLimit : Math.max(1000L, BasicPeriodBuilderFactory.approximateDurationOf(this.minUnit) * this.minLimit / emud);
        if (duration * 1000L < eml * emud)
          return Period.lessThan((float)eml / 1000.0F, emu).inPast(inPast); 
      } 
      return null;
    }
    
    public Settings copy() {
      Settings result = new Settings();
      result.inUse = this.inUse;
      result.uset = this.uset;
      result.maxUnit = this.maxUnit;
      result.minUnit = this.minUnit;
      result.maxLimit = this.maxLimit;
      result.minLimit = this.minLimit;
      result.allowZero = this.allowZero;
      result.weeksAloneOnly = this.weeksAloneOnly;
      result.allowMillis = this.allowMillis;
      return result;
    }
  }
  
  public PeriodBuilderFactory setAvailableUnitRange(TimeUnit minUnit, TimeUnit maxUnit) {
    int uset = 0;
    for (int i = maxUnit.ordinal; i <= minUnit.ordinal; i++)
      uset |= 1 << i; 
    if (uset == 0)
      throw new IllegalArgumentException("range " + minUnit + " to " + maxUnit + " is empty"); 
    this.settings = this.settings.setUnits(uset);
    return this;
  }
  
  public PeriodBuilderFactory setUnitIsAvailable(TimeUnit unit, boolean available) {
    int uset = this.settings.uset;
    if (available) {
      uset |= 1 << unit.ordinal;
    } else {
      uset &= 1 << unit.ordinal ^ 0xFFFFFFFF;
    } 
    this.settings = this.settings.setUnits(uset);
    return this;
  }
  
  public PeriodBuilderFactory setMaxLimit(float maxLimit) {
    this.settings = this.settings.setMaxLimit(maxLimit);
    return this;
  }
  
  public PeriodBuilderFactory setMinLimit(float minLimit) {
    this.settings = this.settings.setMinLimit(minLimit);
    return this;
  }
  
  public PeriodBuilderFactory setAllowZero(boolean allow) {
    this.settings = this.settings.setAllowZero(allow);
    return this;
  }
  
  public PeriodBuilderFactory setWeeksAloneOnly(boolean aloneOnly) {
    this.settings = this.settings.setWeeksAloneOnly(aloneOnly);
    return this;
  }
  
  public PeriodBuilderFactory setAllowMilliseconds(boolean allow) {
    this.settings = this.settings.setAllowMilliseconds(allow);
    return this;
  }
  
  public PeriodBuilderFactory setLocale(String localeName) {
    this.settings = this.settings.setLocale(localeName);
    return this;
  }
  
  public PeriodBuilderFactory setTimeZone(TimeZone timeZone) {
    return this;
  }
  
  private Settings getSettings() {
    if (this.settings.effectiveSet() == 0)
      return null; 
    return this.settings.setInUse();
  }
  
  public PeriodBuilder getFixedUnitBuilder(TimeUnit unit) {
    return FixedUnitBuilder.get(unit, getSettings());
  }
  
  public PeriodBuilder getSingleUnitBuilder() {
    return SingleUnitBuilder.get(getSettings());
  }
  
  public PeriodBuilder getOneOrTwoUnitBuilder() {
    return OneOrTwoUnitBuilder.get(getSettings());
  }
  
  public PeriodBuilder getMultiUnitBuilder(int periodCount) {
    return MultiUnitBuilder.get(periodCount, getSettings());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\BasicPeriodBuilderFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */