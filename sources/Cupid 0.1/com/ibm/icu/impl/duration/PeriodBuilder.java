package com.ibm.icu.impl.duration;

import java.util.TimeZone;

public interface PeriodBuilder {
  Period create(long paramLong);
  
  Period createWithReferenceDate(long paramLong1, long paramLong2);
  
  PeriodBuilder withLocale(String paramString);
  
  PeriodBuilder withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\PeriodBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */