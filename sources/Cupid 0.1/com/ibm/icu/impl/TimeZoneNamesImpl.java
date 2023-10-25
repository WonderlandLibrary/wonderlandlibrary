package com.ibm.icu.impl;

import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class TimeZoneNamesImpl extends TimeZoneNames {
  private static final long serialVersionUID = -2179814848495897472L;
  
  private static final String ZONE_STRINGS_BUNDLE = "zoneStrings";
  
  private static final String MZ_PREFIX = "meta:";
  
  private static Set<String> METAZONE_IDS;
  
  private static final TZ2MZsCache TZ_TO_MZS_CACHE = new TZ2MZsCache();
  
  private static final MZ2TZsCache MZ_TO_TZS_CACHE = new MZ2TZsCache();
  
  private transient ICUResourceBundle _zoneStrings;
  
  private transient ConcurrentHashMap<String, ZNames> _mzNamesMap;
  
  private transient ConcurrentHashMap<String, TZNames> _tzNamesMap;
  
  private transient TextTrieMap<NameInfo> _namesTrie;
  
  private transient boolean _namesTrieFullyLoaded;
  
  public TimeZoneNamesImpl(ULocale locale) {
    initialize(locale);
  }
  
  public synchronized Set<String> getAvailableMetaZoneIDs() {
    if (METAZONE_IDS == null) {
      UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
      UResourceBundle mapTimezones = bundle.get("mapTimezones");
      Set<String> keys = mapTimezones.keySet();
      METAZONE_IDS = Collections.unmodifiableSet(keys);
    } 
    return METAZONE_IDS;
  }
  
  public Set<String> getAvailableMetaZoneIDs(String tzID) {
    if (tzID == null || tzID.length() == 0)
      return Collections.emptySet(); 
    List<MZMapEntry> maps = TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
    if (maps.isEmpty())
      return Collections.emptySet(); 
    Set<String> mzIDs = new HashSet<String>(maps.size());
    for (MZMapEntry map : maps)
      mzIDs.add(map.mzID()); 
    return Collections.unmodifiableSet(mzIDs);
  }
  
  public String getMetaZoneID(String tzID, long date) {
    if (tzID == null || tzID.length() == 0)
      return null; 
    String mzID = null;
    List<MZMapEntry> maps = TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
    for (MZMapEntry map : maps) {
      if (date >= map.from() && date < map.to()) {
        mzID = map.mzID();
        break;
      } 
    } 
    return mzID;
  }
  
  public String getReferenceZoneID(String mzID, String region) {
    if (mzID == null || mzID.length() == 0)
      return null; 
    String refID = null;
    Map<String, String> regionTzMap = MZ_TO_TZS_CACHE.getInstance(mzID, mzID);
    if (!regionTzMap.isEmpty()) {
      refID = regionTzMap.get(region);
      if (refID == null)
        refID = regionTzMap.get("001"); 
    } 
    return refID;
  }
  
  public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type) {
    if (mzID == null || mzID.length() == 0)
      return null; 
    return loadMetaZoneNames(mzID).getName(type);
  }
  
  public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type) {
    if (tzID == null || tzID.length() == 0)
      return null; 
    return loadTimeZoneNames(tzID).getName(type);
  }
  
  public String getExemplarLocationName(String tzID) {
    if (tzID == null || tzID.length() == 0)
      return null; 
    String locName = loadTimeZoneNames(tzID).getName(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
    return locName;
  }
  
  public synchronized Collection<TimeZoneNames.MatchInfo> find(CharSequence text, int start, EnumSet<TimeZoneNames.NameType> nameTypes) {
    if (text == null || text.length() == 0 || start < 0 || start >= text.length())
      throw new IllegalArgumentException("bad input text or range"); 
    NameSearchHandler handler = new NameSearchHandler(nameTypes);
    this._namesTrie.find(text, start, handler);
    if (handler.getMaxMatchLen() == text.length() - start || this._namesTrieFullyLoaded)
      return handler.getMatches(); 
    Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
    for (String tzID : tzIDs)
      loadTimeZoneNames(tzID); 
    Set<String> mzIDs = getAvailableMetaZoneIDs();
    for (String mzID : mzIDs)
      loadMetaZoneNames(mzID); 
    this._namesTrieFullyLoaded = true;
    handler.resetResults();
    this._namesTrie.find(text, start, handler);
    return handler.getMatches();
  }
  
  private void initialize(ULocale locale) {
    ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", locale);
    this._zoneStrings = (ICUResourceBundle)bundle.get("zoneStrings");
    this._tzNamesMap = new ConcurrentHashMap<String, TZNames>();
    this._mzNamesMap = new ConcurrentHashMap<String, ZNames>();
    this._namesTrie = new TextTrieMap<NameInfo>(true);
    this._namesTrieFullyLoaded = false;
    TimeZone tz = TimeZone.getDefault();
    String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
    if (tzCanonicalID != null)
      loadStrings(tzCanonicalID); 
  }
  
  private synchronized void loadStrings(String tzCanonicalID) {
    if (tzCanonicalID == null || tzCanonicalID.length() == 0)
      return; 
    loadTimeZoneNames(tzCanonicalID);
    Set<String> mzIDs = getAvailableMetaZoneIDs(tzCanonicalID);
    for (String mzID : mzIDs)
      loadMetaZoneNames(mzID); 
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    ULocale locale = this._zoneStrings.getULocale();
    out.writeObject(locale);
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    ULocale locale = (ULocale)in.readObject();
    initialize(locale);
  }
  
  private synchronized ZNames loadMetaZoneNames(String mzID) {
    ZNames znames = this._mzNamesMap.get(mzID);
    if (znames == null) {
      znames = ZNames.getInstance(this._zoneStrings, "meta:" + mzID);
      mzID = mzID.intern();
      for (TimeZoneNames.NameType t : TimeZoneNames.NameType.values()) {
        String name = znames.getName(t);
        if (name != null) {
          NameInfo info = new NameInfo();
          info.mzID = mzID;
          info.type = t;
          this._namesTrie.put(name, info);
        } 
      } 
      ZNames tmpZnames = this._mzNamesMap.putIfAbsent(mzID, znames);
      znames = (tmpZnames == null) ? znames : tmpZnames;
    } 
    return znames;
  }
  
  private synchronized TZNames loadTimeZoneNames(String tzID) {
    TZNames tznames = this._tzNamesMap.get(tzID);
    if (tznames == null) {
      tznames = TZNames.getInstance(this._zoneStrings, tzID.replace('/', ':'), tzID);
      tzID = tzID.intern();
      for (TimeZoneNames.NameType t : TimeZoneNames.NameType.values()) {
        String name = tznames.getName(t);
        if (name != null) {
          NameInfo info = new NameInfo();
          info.tzID = tzID;
          info.type = t;
          this._namesTrie.put(name, info);
        } 
      } 
      TZNames tmpTznames = this._tzNamesMap.putIfAbsent(tzID, tznames);
      tznames = (tmpTznames == null) ? tznames : tmpTznames;
    } 
    return tznames;
  }
  
  private static class NameInfo {
    String tzID;
    
    String mzID;
    
    TimeZoneNames.NameType type;
    
    private NameInfo() {}
  }
  
  private static class NameSearchHandler implements TextTrieMap.ResultHandler<NameInfo> {
    private EnumSet<TimeZoneNames.NameType> _nameTypes;
    
    private Collection<TimeZoneNames.MatchInfo> _matches;
    
    private int _maxMatchLen;
    
    NameSearchHandler(EnumSet<TimeZoneNames.NameType> nameTypes) {
      this._nameTypes = nameTypes;
    }
    
    public boolean handlePrefixMatch(int matchLength, Iterator<TimeZoneNamesImpl.NameInfo> values) {
      while (values.hasNext()) {
        TimeZoneNames.MatchInfo minfo;
        TimeZoneNamesImpl.NameInfo ninfo = values.next();
        if (this._nameTypes != null && !this._nameTypes.contains(ninfo.type))
          continue; 
        if (ninfo.tzID != null) {
          minfo = new TimeZoneNames.MatchInfo(ninfo.type, ninfo.tzID, null, matchLength);
        } else {
          assert ninfo.mzID != null;
          minfo = new TimeZoneNames.MatchInfo(ninfo.type, null, ninfo.mzID, matchLength);
        } 
        if (this._matches == null)
          this._matches = new LinkedList<TimeZoneNames.MatchInfo>(); 
        this._matches.add(minfo);
        if (matchLength > this._maxMatchLen)
          this._maxMatchLen = matchLength; 
      } 
      return true;
    }
    
    public Collection<TimeZoneNames.MatchInfo> getMatches() {
      if (this._matches == null)
        return Collections.emptyList(); 
      return this._matches;
    }
    
    public int getMaxMatchLen() {
      return this._maxMatchLen;
    }
    
    public void resetResults() {
      this._matches = null;
      this._maxMatchLen = 0;
    }
  }
  
  private static class ZNames {
    private static final ZNames EMPTY_ZNAMES = new ZNames(null);
    
    private String[] _names;
    
    private static final String[] KEYS = new String[] { "lg", "ls", "ld", "sg", "ss", "sd" };
    
    protected ZNames(String[] names) {
      this._names = names;
    }
    
    public static ZNames getInstance(ICUResourceBundle zoneStrings, String key) {
      String[] names = loadData(zoneStrings, key);
      if (names == null)
        return EMPTY_ZNAMES; 
      return new ZNames(names);
    }
    
    public String getName(TimeZoneNames.NameType type) {
      if (this._names == null)
        return null; 
      String name = null;
      switch (type) {
        case LONG_GENERIC:
          name = this._names[0];
          break;
        case LONG_STANDARD:
          name = this._names[1];
          break;
        case LONG_DAYLIGHT:
          name = this._names[2];
          break;
        case SHORT_GENERIC:
          name = this._names[3];
          break;
        case SHORT_STANDARD:
          name = this._names[4];
          break;
        case SHORT_DAYLIGHT:
          name = this._names[5];
          break;
        case EXEMPLAR_LOCATION:
          name = null;
          break;
      } 
      return name;
    }
    
    protected static String[] loadData(ICUResourceBundle zoneStrings, String key) {
      if (zoneStrings == null || key == null || key.length() == 0)
        return null; 
      ICUResourceBundle table = null;
      try {
        table = zoneStrings.getWithFallback(key);
      } catch (MissingResourceException e) {
        return null;
      } 
      boolean isEmpty = true;
      String[] names = new String[KEYS.length];
      for (int i = 0; i < names.length; i++) {
        try {
          names[i] = table.getStringWithFallback(KEYS[i]);
          isEmpty = false;
        } catch (MissingResourceException e) {
          names[i] = null;
        } 
      } 
      if (isEmpty)
        return null; 
      return names;
    }
  }
  
  private static class TZNames extends ZNames {
    private String _locationName;
    
    private static final TZNames EMPTY_TZNAMES = new TZNames(null, null);
    
    public static TZNames getInstance(ICUResourceBundle zoneStrings, String key, String tzID) {
      if (zoneStrings == null || key == null || key.length() == 0)
        return EMPTY_TZNAMES; 
      String[] names = loadData(zoneStrings, key);
      String locationName = null;
      ICUResourceBundle table = null;
      try {
        table = zoneStrings.getWithFallback(key);
        locationName = table.getStringWithFallback("ec");
      } catch (MissingResourceException e) {}
      if (locationName == null)
        locationName = TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID); 
      if (locationName == null && names == null)
        return EMPTY_TZNAMES; 
      return new TZNames(names, locationName);
    }
    
    public String getName(TimeZoneNames.NameType type) {
      if (type == TimeZoneNames.NameType.EXEMPLAR_LOCATION)
        return this._locationName; 
      return super.getName(type);
    }
    
    private TZNames(String[] names, String locationName) {
      super(names);
      this._locationName = locationName;
    }
  }
  
  private static class MZMapEntry {
    private String _mzID;
    
    private long _from;
    
    private long _to;
    
    MZMapEntry(String mzID, long from, long to) {
      this._mzID = mzID;
      this._from = from;
      this._to = to;
    }
    
    String mzID() {
      return this._mzID;
    }
    
    long from() {
      return this._from;
    }
    
    long to() {
      return this._to;
    }
  }
  
  private static class TZ2MZsCache extends SoftCache<String, List<MZMapEntry>, String> {
    private TZ2MZsCache() {}
    
    protected List<TimeZoneNamesImpl.MZMapEntry> createInstance(String key, String data) {
      List<TimeZoneNamesImpl.MZMapEntry> mzMaps = null;
      UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
      UResourceBundle metazoneInfoBundle = bundle.get("metazoneInfo");
      String tzkey = data.replace('/', ':');
      try {
        UResourceBundle zoneBundle = metazoneInfoBundle.get(tzkey);
        mzMaps = new ArrayList<TimeZoneNamesImpl.MZMapEntry>(zoneBundle.getSize());
        for (int idx = 0; idx < zoneBundle.getSize(); idx++) {
          UResourceBundle mz = zoneBundle.get(idx);
          String mzid = mz.getString(0);
          String fromStr = "1970-01-01 00:00";
          String toStr = "9999-12-31 23:59";
          if (mz.getSize() == 3) {
            fromStr = mz.getString(1);
            toStr = mz.getString(2);
          } 
          long from = parseDate(fromStr);
          long to = parseDate(toStr);
          mzMaps.add(new TimeZoneNamesImpl.MZMapEntry(mzid, from, to));
        } 
      } catch (MissingResourceException mre) {
        mzMaps = Collections.emptyList();
      } 
      return mzMaps;
    }
    
    private static long parseDate(String text) {
      int year = 0, month = 0, day = 0, hour = 0, min = 0;
      int idx;
      for (idx = 0; idx <= 3; idx++) {
        int n = text.charAt(idx) - 48;
        if (n >= 0 && n < 10) {
          year = 10 * year + n;
        } else {
          throw new IllegalArgumentException("Bad year");
        } 
      } 
      for (idx = 5; idx <= 6; idx++) {
        int n = text.charAt(idx) - 48;
        if (n >= 0 && n < 10) {
          month = 10 * month + n;
        } else {
          throw new IllegalArgumentException("Bad month");
        } 
      } 
      for (idx = 8; idx <= 9; idx++) {
        int n = text.charAt(idx) - 48;
        if (n >= 0 && n < 10) {
          day = 10 * day + n;
        } else {
          throw new IllegalArgumentException("Bad day");
        } 
      } 
      for (idx = 11; idx <= 12; idx++) {
        int n = text.charAt(idx) - 48;
        if (n >= 0 && n < 10) {
          hour = 10 * hour + n;
        } else {
          throw new IllegalArgumentException("Bad hour");
        } 
      } 
      for (idx = 14; idx <= 15; idx++) {
        int n = text.charAt(idx) - 48;
        if (n >= 0 && n < 10) {
          min = 10 * min + n;
        } else {
          throw new IllegalArgumentException("Bad minute");
        } 
      } 
      long date = Grego.fieldsToDay(year, month - 1, day) * 86400000L + hour * 3600000L + min * 60000L;
      return date;
    }
  }
  
  private static class MZ2TZsCache extends SoftCache<String, Map<String, String>, String> {
    private MZ2TZsCache() {}
    
    protected Map<String, String> createInstance(String key, String data) {
      Map<String, String> map = null;
      UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
      UResourceBundle mapTimezones = bundle.get("mapTimezones");
      try {
        UResourceBundle regionMap = mapTimezones.get(key);
        Set<String> regions = regionMap.keySet();
        map = new HashMap<String, String>(regions.size());
        for (String region : regions) {
          String tzID = regionMap.getString(region).intern();
          map.put(region.intern(), tzID);
        } 
      } catch (MissingResourceException e) {
        map = Collections.emptyMap();
      } 
      return map;
    }
  }
  
  private static final Pattern LOC_EXCLUSION_PATTERN = Pattern.compile("Etc/.*|SystemV/.*|.*/Riyadh8[7-9]");
  
  public static String getDefaultExemplarLocationName(String tzID) {
    if (tzID == null || tzID.length() == 0 || LOC_EXCLUSION_PATTERN.matcher(tzID).matches())
      return null; 
    String location = null;
    int sep = tzID.lastIndexOf('/');
    if (sep > 0 && sep + 1 < tzID.length())
      location = tzID.substring(sep + 1).replace('_', ' '); 
    return location;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\TimeZoneNamesImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */