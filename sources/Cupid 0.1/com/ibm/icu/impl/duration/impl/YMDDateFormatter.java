package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.DateFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class YMDDateFormatter implements DateFormatter {
  private String requestedFields;
  
  private String localeName;
  
  private TimeZone timeZone;
  
  private SimpleDateFormat df;
  
  public YMDDateFormatter(String requestedFields) {
    this(requestedFields, Locale.getDefault().toString(), TimeZone.getDefault());
  }
  
  public YMDDateFormatter(String requestedFields, String localeName, TimeZone timeZone) {
    this.requestedFields = requestedFields;
    this.localeName = localeName;
    this.timeZone = timeZone;
    Locale locale = Utils.localeFromString(localeName);
    this.df = new SimpleDateFormat("yyyy/mm/dd", locale);
    this.df.setTimeZone(timeZone);
  }
  
  public String format(long date) {
    return format(new Date(date));
  }
  
  public String format(Date date) {
    return this.df.format(date);
  }
  
  public DateFormatter withLocale(String locName) {
    if (!locName.equals(this.localeName))
      return new YMDDateFormatter(this.requestedFields, locName, this.timeZone); 
    return this;
  }
  
  public DateFormatter withTimeZone(TimeZone tz) {
    if (!tz.equals(this.timeZone))
      return new YMDDateFormatter(this.requestedFields, this.localeName, tz); 
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\YMDDateFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */