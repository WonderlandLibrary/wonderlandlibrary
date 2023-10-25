package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class TimeZoneNames implements Serializable {
  private static final long serialVersionUID = -9180227029248969153L;
  
  public enum NameType {
    LONG_GENERIC, LONG_STANDARD, LONG_DAYLIGHT, SHORT_GENERIC, SHORT_STANDARD, SHORT_DAYLIGHT, EXEMPLAR_LOCATION;
  }
  
  private static Cache TZNAMES_CACHE = new Cache();
  
  private static final Factory TZNAMES_FACTORY;
  
  private static final String FACTORY_NAME_PROP = "com.ibm.icu.text.TimeZoneNames.Factory.impl";
  
  private static final String DEFAULT_FACTORY_CLASS = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
  
  static {
    Factory factory = null;
    String classname = ICUConfig.get("com.ibm.icu.text.TimeZoneNames.Factory.impl", "com.ibm.icu.impl.TimeZoneNamesFactoryImpl");
    while (true) {
      try {
        factory = (Factory)Class.forName(classname).newInstance();
        break;
      } catch (ClassNotFoundException cnfe) {
      
      } catch (IllegalAccessException iae) {
      
      } catch (InstantiationException ie) {}
      if (classname.equals("com.ibm.icu.impl.TimeZoneNamesFactoryImpl"))
        break; 
      classname = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
    } 
    if (factory == null)
      factory = new DefaultTimeZoneNames.FactoryImpl(); 
    TZNAMES_FACTORY = factory;
  }
  
  public static TimeZoneNames getInstance(ULocale locale) {
    String key = locale.getBaseName();
    return (TimeZoneNames)TZNAMES_CACHE.getInstance(key, locale);
  }
  
  public final String getDisplayName(String tzID, NameType type, long date) {
    String name = getTimeZoneDisplayName(tzID, type);
    if (name == null) {
      String mzID = getMetaZoneID(tzID, date);
      name = getMetaZoneDisplayName(mzID, type);
    } 
    return name;
  }
  
  public String getExemplarLocationName(String tzID) {
    return TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID);
  }
  
  public Collection<MatchInfo> find(CharSequence text, int start, EnumSet<NameType> types) {
    throw new UnsupportedOperationException("The method is not implemented in TimeZoneNames base class.");
  }
  
  public abstract Set<String> getAvailableMetaZoneIDs();
  
  public abstract Set<String> getAvailableMetaZoneIDs(String paramString);
  
  public abstract String getMetaZoneID(String paramString, long paramLong);
  
  public abstract String getReferenceZoneID(String paramString1, String paramString2);
  
  public abstract String getMetaZoneDisplayName(String paramString, NameType paramNameType);
  
  public abstract String getTimeZoneDisplayName(String paramString, NameType paramNameType);
  
  public static class MatchInfo {
    private TimeZoneNames.NameType _nameType;
    
    private String _tzID;
    
    private String _mzID;
    
    private int _matchLength;
    
    public MatchInfo(TimeZoneNames.NameType nameType, String tzID, String mzID, int matchLength) {
      if (nameType == null)
        throw new IllegalArgumentException("nameType is null"); 
      if (tzID == null && mzID == null)
        throw new IllegalArgumentException("Either tzID or mzID must be available"); 
      if (matchLength <= 0)
        throw new IllegalArgumentException("matchLength must be positive value"); 
      this._nameType = nameType;
      this._tzID = tzID;
      this._mzID = mzID;
      this._matchLength = matchLength;
    }
    
    public String tzID() {
      return this._tzID;
    }
    
    public String mzID() {
      return this._mzID;
    }
    
    public TimeZoneNames.NameType nameType() {
      return this._nameType;
    }
    
    public int matchLength() {
      return this._matchLength;
    }
  }
  
  public static abstract class Factory {
    public abstract TimeZoneNames getTimeZoneNames(ULocale param1ULocale);
  }
  
  private static class Cache extends SoftCache<String, TimeZoneNames, ULocale> {
    private Cache() {}
    
    protected TimeZoneNames createInstance(String key, ULocale data) {
      return TimeZoneNames.TZNAMES_FACTORY.getTimeZoneNames(data);
    }
  }
  
  private static class DefaultTimeZoneNames extends TimeZoneNames {
    private static final long serialVersionUID = -995672072494349071L;
    
    public static final DefaultTimeZoneNames INSTANCE = new DefaultTimeZoneNames();
    
    public Set<String> getAvailableMetaZoneIDs() {
      return Collections.emptySet();
    }
    
    public Set<String> getAvailableMetaZoneIDs(String tzID) {
      return Collections.emptySet();
    }
    
    public String getMetaZoneID(String tzID, long date) {
      return null;
    }
    
    public String getReferenceZoneID(String mzID, String region) {
      return null;
    }
    
    public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type) {
      return null;
    }
    
    public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type) {
      return null;
    }
    
    public Collection<TimeZoneNames.MatchInfo> find(CharSequence text, int start, EnumSet<TimeZoneNames.NameType> nameTypes) {
      return Collections.emptyList();
    }
    
    public static class FactoryImpl extends TimeZoneNames.Factory {
      public TimeZoneNames getTimeZoneNames(ULocale locale) {
        return TimeZoneNames.DefaultTimeZoneNames.INSTANCE;
      }
    }
  }
  
  public static class FactoryImpl extends Factory {
    public TimeZoneNames getTimeZoneNames(ULocale locale) {
      return TimeZoneNames.DefaultTimeZoneNames.INSTANCE;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\TimeZoneNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */