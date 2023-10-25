package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache<F extends Format> {
  static final int NONE = -1;
  
  private final ConcurrentMap<MultipartKey, F> cInstanceCache = new ConcurrentHashMap<MultipartKey, F>(7);
  
  private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap<MultipartKey, String>(7);
  
  public F getInstance() {
    return getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
  }
  
  public F getInstance(String pattern, TimeZone timeZone, Locale locale) {
    if (pattern == null)
      throw new NullPointerException("pattern must not be null"); 
    if (timeZone == null)
      timeZone = TimeZone.getDefault(); 
    if (locale == null)
      locale = Locale.getDefault(); 
    MultipartKey key = new MultipartKey(new Object[] { pattern, timeZone, locale });
    Format format = (Format)this.cInstanceCache.get(key);
    if (format == null) {
      format = (Format)createInstance(pattern, timeZone, locale);
      Format format1 = (Format)this.cInstanceCache.putIfAbsent(key, (F)format);
      if (format1 != null)
        format = format1; 
    } 
    return (F)format;
  }
  
  protected abstract F createInstance(String paramString, TimeZone paramTimeZone, Locale paramLocale);
  
  private F getDateTimeInstance(Integer dateStyle, Integer timeStyle, TimeZone timeZone, Locale locale) {
    if (locale == null)
      locale = Locale.getDefault(); 
    String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
    return getInstance(pattern, timeZone, locale);
  }
  
  F getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
  }
  
  F getDateInstance(int dateStyle, TimeZone timeZone, Locale locale) {
    return getDateTimeInstance(Integer.valueOf(dateStyle), (Integer)null, timeZone, locale);
  }
  
  F getTimeInstance(int timeStyle, TimeZone timeZone, Locale locale) {
    return getDateTimeInstance((Integer)null, Integer.valueOf(timeStyle), timeZone, locale);
  }
  
  static String getPatternForStyle(Integer dateStyle, Integer timeStyle, Locale locale) {
    MultipartKey key = new MultipartKey(new Object[] { dateStyle, timeStyle, locale });
    String pattern = cDateTimeInstanceCache.get(key);
    if (pattern == null)
      try {
        DateFormat formatter;
        if (dateStyle == null) {
          formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
        } else if (timeStyle == null) {
          formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
        } else {
          formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
        } 
        pattern = ((SimpleDateFormat)formatter).toPattern();
        String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
        if (previous != null)
          pattern = previous; 
      } catch (ClassCastException ex) {
        throw new IllegalArgumentException("No date time pattern for locale: " + locale);
      }  
    return pattern;
  }
  
  private static class MultipartKey {
    private final Object[] keys;
    
    private int hashCode;
    
    public MultipartKey(Object... keys) {
      this.keys = keys;
    }
    
    public boolean equals(Object obj) {
      return Arrays.equals(this.keys, ((MultipartKey)obj).keys);
    }
    
    public int hashCode() {
      if (this.hashCode == 0) {
        int rc = 0;
        for (Object key : this.keys) {
          if (key != null)
            rc = rc * 7 + key.hashCode(); 
        } 
        this.hashCode = rc;
      } 
      return this.hashCode;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\time\FormatCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */