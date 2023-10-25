package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public interface DurationFormatter {
  String formatDurationFromNowTo(Date paramDate);
  
  String formatDurationFromNow(long paramLong);
  
  String formatDurationFrom(long paramLong1, long paramLong2);
  
  DurationFormatter withLocale(String paramString);
  
  DurationFormatter withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\DurationFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */