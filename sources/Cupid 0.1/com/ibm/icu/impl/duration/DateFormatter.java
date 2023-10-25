package com.ibm.icu.impl.duration;

import java.util.Date;
import java.util.TimeZone;

public interface DateFormatter {
  String format(Date paramDate);
  
  String format(long paramLong);
  
  DateFormatter withLocale(String paramString);
  
  DateFormatter withTimeZone(TimeZone paramTimeZone);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\DateFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */