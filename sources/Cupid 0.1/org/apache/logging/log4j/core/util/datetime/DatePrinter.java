package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DatePrinter {
  String format(long paramLong);
  
  String format(Date paramDate);
  
  String format(Calendar paramCalendar);
  
  <B extends Appendable> B format(long paramLong, B paramB);
  
  <B extends Appendable> B format(Date paramDate, B paramB);
  
  <B extends Appendable> B format(Calendar paramCalendar, B paramB);
  
  String getPattern();
  
  TimeZone getTimeZone();
  
  Locale getLocale();
  
  StringBuilder format(Object paramObject, StringBuilder paramStringBuilder, FieldPosition paramFieldPosition);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\datetime\DatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */