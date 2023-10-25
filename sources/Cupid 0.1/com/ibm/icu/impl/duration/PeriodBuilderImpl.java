package com.ibm.icu.impl.duration;

import java.util.TimeZone;

abstract class PeriodBuilderImpl implements PeriodBuilder {
  protected BasicPeriodBuilderFactory.Settings settings;
  
  public Period create(long duration) {
    return createWithReferenceDate(duration, System.currentTimeMillis());
  }
  
  public long approximateDurationOf(TimeUnit unit) {
    return BasicPeriodBuilderFactory.approximateDurationOf(unit);
  }
  
  public Period createWithReferenceDate(long duration, long referenceDate) {
    boolean inPast = (duration < 0L);
    if (inPast)
      duration = -duration; 
    Period ts = this.settings.createLimited(duration, inPast);
    if (ts == null) {
      ts = handleCreate(duration, referenceDate, inPast);
      if (ts == null)
        ts = Period.lessThan(1.0F, this.settings.effectiveMinUnit()).inPast(inPast); 
    } 
    return ts;
  }
  
  public PeriodBuilder withTimeZone(TimeZone timeZone) {
    return this;
  }
  
  public PeriodBuilder withLocale(String localeName) {
    BasicPeriodBuilderFactory.Settings newSettings = this.settings.setLocale(localeName);
    if (newSettings != this.settings)
      return withSettings(newSettings); 
    return this;
  }
  
  protected abstract PeriodBuilder withSettings(BasicPeriodBuilderFactory.Settings paramSettings);
  
  protected abstract Period handleCreate(long paramLong1, long paramLong2, boolean paramBoolean);
  
  protected PeriodBuilderImpl(BasicPeriodBuilderFactory.Settings settings) {
    this.settings = settings;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\PeriodBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */