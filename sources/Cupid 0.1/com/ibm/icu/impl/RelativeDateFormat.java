package com.ibm.icu.impl;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Comparator;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeSet;

public class RelativeDateFormat extends DateFormat {
  private static final long serialVersionUID = 1131984966440549435L;
  
  private DateFormat fDateFormat;
  
  private DateFormat fTimeFormat;
  
  private MessageFormat fCombinedFormat;
  
  private SimpleDateFormat fDateTimeFormat;
  
  private String fDatePattern;
  
  private String fTimePattern;
  
  int fDateStyle;
  
  int fTimeStyle;
  
  ULocale fLocale;
  
  private transient URelativeString[] fDates;
  
  public static class URelativeString {
    public int offset;
    
    public String string;
    
    URelativeString(int offset, String string) {
      this.offset = offset;
      this.string = string;
    }
    
    URelativeString(String offset, String string) {
      this.offset = Integer.parseInt(offset);
      this.string = string;
    }
  }
  
  public RelativeDateFormat(int timeStyle, int dateStyle, ULocale locale) {
    this.fDateTimeFormat = null;
    this.fDatePattern = null;
    this.fTimePattern = null;
    this.fDates = null;
    this.fLocale = locale;
    this.fTimeStyle = timeStyle;
    this.fDateStyle = dateStyle;
    if (this.fDateStyle != -1) {
      int newStyle = this.fDateStyle & 0xFFFFFF7F;
      DateFormat df = DateFormat.getDateInstance(newStyle, locale);
      if (df instanceof SimpleDateFormat) {
        this.fDateTimeFormat = (SimpleDateFormat)df;
      } else {
        throw new IllegalArgumentException("Can't create SimpleDateFormat for date style");
      } 
      this.fDatePattern = this.fDateTimeFormat.toPattern();
      if (this.fTimeStyle != -1) {
        newStyle = this.fTimeStyle & 0xFFFFFF7F;
        df = DateFormat.getTimeInstance(newStyle, locale);
        if (df instanceof SimpleDateFormat)
          this.fTimePattern = ((SimpleDateFormat)df).toPattern(); 
      } 
    } else {
      int newStyle = this.fTimeStyle & 0xFFFFFF7F;
      DateFormat df = DateFormat.getTimeInstance(newStyle, locale);
      if (df instanceof SimpleDateFormat) {
        this.fDateTimeFormat = (SimpleDateFormat)df;
      } else {
        throw new IllegalArgumentException("Can't create SimpleDateFormat for time style");
      } 
      this.fTimePattern = this.fDateTimeFormat.toPattern();
    } 
    initializeCalendar(null, this.fLocale);
    loadDates();
    initializeCombinedFormat(this.calendar, this.fLocale);
  }
  
  public StringBuffer format(Calendar cal, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    String relativeDayString = null;
    if (this.fDateStyle != -1) {
      int dayDiff = dayDifference(cal);
      relativeDayString = getStringForDay(dayDiff);
    } 
    if (this.fDateTimeFormat != null && (this.fDatePattern != null || this.fTimePattern != null)) {
      if (this.fDatePattern == null) {
        this.fDateTimeFormat.applyPattern(this.fTimePattern);
        this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
      } else if (this.fTimePattern == null) {
        if (relativeDayString != null) {
          toAppendTo.append(relativeDayString);
        } else {
          this.fDateTimeFormat.applyPattern(this.fDatePattern);
          this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
        } 
      } else {
        String datePattern = this.fDatePattern;
        if (relativeDayString != null)
          datePattern = "'" + relativeDayString.replace("'", "''") + "'"; 
        StringBuffer combinedPattern = new StringBuffer("");
        this.fCombinedFormat.format(new Object[] { this.fTimePattern, datePattern }, combinedPattern, new FieldPosition(0));
        this.fDateTimeFormat.applyPattern(combinedPattern.toString());
        this.fDateTimeFormat.format(cal, toAppendTo, fieldPosition);
      } 
    } else if (this.fDateFormat != null) {
      if (relativeDayString != null) {
        toAppendTo.append(relativeDayString);
      } else {
        this.fDateFormat.format(cal, toAppendTo, fieldPosition);
      } 
    } 
    return toAppendTo;
  }
  
  public void parse(String text, Calendar cal, ParsePosition pos) {
    throw new UnsupportedOperationException("Relative Date parse is not implemented yet");
  }
  
  private String getStringForDay(int day) {
    if (this.fDates == null)
      loadDates(); 
    for (int i = 0; i < this.fDates.length; i++) {
      if ((this.fDates[i]).offset == day)
        return (this.fDates[i]).string; 
    } 
    return null;
  }
  
  private synchronized void loadDates() {
    ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", this.fLocale);
    ICUResourceBundle rdb = rb.getWithFallback("fields/day/relative");
    Set<URelativeString> datesSet = new TreeSet<URelativeString>(new Comparator<URelativeString>() {
          public int compare(RelativeDateFormat.URelativeString r1, RelativeDateFormat.URelativeString r2) {
            if (r1.offset == r2.offset)
              return 0; 
            if (r1.offset < r2.offset)
              return -1; 
            return 1;
          }
        });
    for (UResourceBundleIterator i = rdb.getIterator(); i.hasNext(); ) {
      UResourceBundle line = i.next();
      String k = line.getKey();
      String v = line.getString();
      URelativeString rs = new URelativeString(k, v);
      datesSet.add(rs);
    } 
    this.fDates = datesSet.<URelativeString>toArray(new URelativeString[0]);
  }
  
  private static int dayDifference(Calendar until) {
    Calendar nowCal = (Calendar)until.clone();
    Date nowDate = new Date(System.currentTimeMillis());
    nowCal.clear();
    nowCal.setTime(nowDate);
    int dayDiff = until.get(20) - nowCal.get(20);
    return dayDiff;
  }
  
  private Calendar initializeCalendar(TimeZone zone, ULocale locale) {
    if (this.calendar == null)
      if (zone == null) {
        this.calendar = Calendar.getInstance(locale);
      } else {
        this.calendar = Calendar.getInstance(zone, locale);
      }  
    return this.calendar;
  }
  
  private MessageFormat initializeCombinedFormat(Calendar cal, ULocale locale) {
    String pattern = "{1} {0}";
    try {
      CalendarData calData = new CalendarData(locale, cal.getType());
      String[] patterns = calData.getDateTimePatterns();
      if (patterns != null && patterns.length >= 9) {
        int glueIndex = 8;
        if (patterns.length >= 13)
          switch (this.fDateStyle) {
            case 0:
            case 128:
              glueIndex++;
              break;
            case 1:
            case 129:
              glueIndex += 2;
              break;
            case 2:
            case 130:
              glueIndex += 3;
              break;
            case 3:
            case 131:
              glueIndex += 4;
              break;
          }  
        pattern = patterns[glueIndex];
      } 
    } catch (MissingResourceException e) {}
    this.fCombinedFormat = new MessageFormat(pattern, locale);
    return this.fCombinedFormat;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\RelativeDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */