package com.ibm.icu.impl.duration;

import java.util.Locale;
import java.util.TimeZone;

class BasicDurationFormatterFactory implements DurationFormatterFactory {
  private BasicPeriodFormatterService ps;
  
  private PeriodFormatter formatter;
  
  private PeriodBuilder builder;
  
  private DateFormatter fallback;
  
  private long fallbackLimit;
  
  private String localeName;
  
  private TimeZone timeZone;
  
  private BasicDurationFormatter f;
  
  BasicDurationFormatterFactory(BasicPeriodFormatterService ps) {
    this.ps = ps;
    this.localeName = Locale.getDefault().toString();
    this.timeZone = TimeZone.getDefault();
  }
  
  public DurationFormatterFactory setPeriodFormatter(PeriodFormatter formatter) {
    if (formatter != this.formatter) {
      this.formatter = formatter;
      reset();
    } 
    return this;
  }
  
  public DurationFormatterFactory setPeriodBuilder(PeriodBuilder builder) {
    if (builder != this.builder) {
      this.builder = builder;
      reset();
    } 
    return this;
  }
  
  public DurationFormatterFactory setFallback(DateFormatter fallback) {
    boolean doReset = (fallback == null) ? ((this.fallback != null)) : (!fallback.equals(this.fallback));
    if (doReset) {
      this.fallback = fallback;
      reset();
    } 
    return this;
  }
  
  public DurationFormatterFactory setFallbackLimit(long fallbackLimit) {
    if (fallbackLimit < 0L)
      fallbackLimit = 0L; 
    if (fallbackLimit != this.fallbackLimit) {
      this.fallbackLimit = fallbackLimit;
      reset();
    } 
    return this;
  }
  
  public DurationFormatterFactory setLocale(String localeName) {
    if (!localeName.equals(this.localeName)) {
      this.localeName = localeName;
      if (this.builder != null)
        this.builder = this.builder.withLocale(localeName); 
      if (this.formatter != null)
        this.formatter = this.formatter.withLocale(localeName); 
      reset();
    } 
    return this;
  }
  
  public DurationFormatterFactory setTimeZone(TimeZone timeZone) {
    if (!timeZone.equals(this.timeZone)) {
      this.timeZone = timeZone;
      if (this.builder != null)
        this.builder = this.builder.withTimeZone(timeZone); 
      reset();
    } 
    return this;
  }
  
  public DurationFormatter getFormatter() {
    if (this.f == null) {
      if (this.fallback != null)
        this.fallback = this.fallback.withLocale(this.localeName).withTimeZone(this.timeZone); 
      this.formatter = getPeriodFormatter();
      this.builder = getPeriodBuilder();
      this.f = createFormatter();
    } 
    return this.f;
  }
  
  public PeriodFormatter getPeriodFormatter() {
    if (this.formatter == null)
      this.formatter = this.ps.newPeriodFormatterFactory().setLocale(this.localeName).getFormatter(); 
    return this.formatter;
  }
  
  public PeriodBuilder getPeriodBuilder() {
    if (this.builder == null)
      this.builder = this.ps.newPeriodBuilderFactory().setLocale(this.localeName).setTimeZone(this.timeZone).getSingleUnitBuilder(); 
    return this.builder;
  }
  
  public DateFormatter getFallback() {
    return this.fallback;
  }
  
  public long getFallbackLimit() {
    return (this.fallback == null) ? 0L : this.fallbackLimit;
  }
  
  public String getLocaleName() {
    return this.localeName;
  }
  
  public TimeZone getTimeZone() {
    return this.timeZone;
  }
  
  protected BasicDurationFormatter createFormatter() {
    return new BasicDurationFormatter(this.formatter, this.builder, this.fallback, this.fallbackLimit, this.localeName, this.timeZone);
  }
  
  protected void reset() {
    this.f = null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\BasicDurationFormatterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */