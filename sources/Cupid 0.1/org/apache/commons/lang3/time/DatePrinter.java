package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {
  String format(long paramLong);
  
  String format(Date paramDate);
  
  String format(Calendar paramCalendar);
  
  StringBuffer format(long paramLong, StringBuffer paramStringBuffer);
  
  StringBuffer format(Date paramDate, StringBuffer paramStringBuffer);
  
  StringBuffer format(Calendar paramCalendar, StringBuffer paramStringBuffer);
  
  String getPattern();
  
  TimeZone getTimeZone();
  
  Locale getLocale();
  
  StringBuffer format(Object paramObject, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\time\DatePrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */