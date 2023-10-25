package com.ibm.icu.impl.duration;

import java.util.TimeZone;

public interface DurationFormatterFactory {
  DurationFormatterFactory setPeriodFormatter(PeriodFormatter paramPeriodFormatter);
  
  DurationFormatterFactory setPeriodBuilder(PeriodBuilder paramPeriodBuilder);
  
  DurationFormatterFactory setFallback(DateFormatter paramDateFormatter);
  
  DurationFormatterFactory setFallbackLimit(long paramLong);
  
  DurationFormatterFactory setLocale(String paramString);
  
  DurationFormatterFactory setTimeZone(TimeZone paramTimeZone);
  
  DurationFormatter getFormatter();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\DurationFormatterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */