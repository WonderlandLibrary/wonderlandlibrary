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
   private static Set METAZONE_IDS;
   private static final TimeZoneNamesImpl.TZ2MZsCache TZ_TO_MZS_CACHE = new TimeZoneNamesImpl.TZ2MZsCache();
   private static final TimeZoneNamesImpl.MZ2TZsCache MZ_TO_TZS_CACHE = new TimeZoneNamesImpl.MZ2TZsCache();
   private transient ICUResourceBundle _zoneStrings;
   private transient ConcurrentHashMap _mzNamesMap;
   private transient ConcurrentHashMap _tzNamesMap;
   private transient TextTrieMap _namesTrie;
   private transient boolean _namesTrieFullyLoaded;
   private static final Pattern LOC_EXCLUSION_PATTERN = Pattern.compile("Etc/.*|SystemV/.*|.*/Riyadh8[7-9]");

   public TimeZoneNamesImpl(ULocale var1) {
      this.initialize(var1);
   }

   public synchronized Set getAvailableMetaZoneIDs() {
      if (METAZONE_IDS == null) {
         UResourceBundle var1 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
         UResourceBundle var2 = var1.get("mapTimezones");
         Set var3 = var2.keySet();
         METAZONE_IDS = Collections.unmodifiableSet(var3);
      }

      return METAZONE_IDS;
   }

   public Set getAvailableMetaZoneIDs(String var1) {
      if (var1 != null && var1.length() != 0) {
         List var2 = (List)TZ_TO_MZS_CACHE.getInstance(var1, var1);
         if (var2.isEmpty()) {
            return Collections.emptySet();
         } else {
            HashSet var3 = new HashSet(var2.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               TimeZoneNamesImpl.MZMapEntry var5 = (TimeZoneNamesImpl.MZMapEntry)var4.next();
               var3.add(var5.mzID());
            }

            return Collections.unmodifiableSet(var3);
         }
      } else {
         return Collections.emptySet();
      }
   }

   public String getMetaZoneID(String var1, long var2) {
      if (var1 != null && var1.length() != 0) {
         String var4 = null;
         List var5 = (List)TZ_TO_MZS_CACHE.getInstance(var1, var1);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            TimeZoneNamesImpl.MZMapEntry var7 = (TimeZoneNamesImpl.MZMapEntry)var6.next();
            if (var2 >= var7.from() && var2 < var7.to()) {
               var4 = var7.mzID();
               break;
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   public String getReferenceZoneID(String var1, String var2) {
      if (var1 != null && var1.length() != 0) {
         String var3 = null;
         Map var4 = (Map)MZ_TO_TZS_CACHE.getInstance(var1, var1);
         if (!var4.isEmpty()) {
            var3 = (String)var4.get(var2);
            if (var3 == null) {
               var3 = (String)var4.get("001");
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public String getMetaZoneDisplayName(String var1, TimeZoneNames.NameType var2) {
      return var1 != null && var1.length() != 0 ? this.loadMetaZoneNames(var1).getName(var2) : null;
   }

   public String getTimeZoneDisplayName(String var1, TimeZoneNames.NameType var2) {
      return var1 != null && var1.length() != 0 ? this.loadTimeZoneNames(var1).getName(var2) : null;
   }

   public String getExemplarLocationName(String var1) {
      if (var1 != null && var1.length() != 0) {
         String var2 = this.loadTimeZoneNames(var1).getName(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
         return var2;
      } else {
         return null;
      }
   }

   public synchronized Collection find(CharSequence var1, int var2, EnumSet var3) {
      if (var1 != null && var1.length() != 0 && var2 >= 0 && var2 < var1.length()) {
         TimeZoneNamesImpl.NameSearchHandler var4 = new TimeZoneNamesImpl.NameSearchHandler(var3);
         this._namesTrie.find(var1, var2, var4);
         if (var4.getMaxMatchLen() != var1.length() - var2 && !this._namesTrieFullyLoaded) {
            Set var5 = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, (String)null, (Integer)null);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               this.loadTimeZoneNames(var7);
            }

            Set var9 = this.getAvailableMetaZoneIDs();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               String var8 = (String)var10.next();
               this.loadMetaZoneNames(var8);
            }

            this._namesTrieFullyLoaded = true;
            var4.resetResults();
            this._namesTrie.find(var1, var2, var4);
            return var4.getMatches();
         } else {
            return var4.getMatches();
         }
      } else {
         throw new IllegalArgumentException("bad input text or range");
      }
   }

   private void initialize(ULocale var1) {
      ICUResourceBundle var2 = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", var1);
      this._zoneStrings = (ICUResourceBundle)var2.get("zoneStrings");
      this._tzNamesMap = new ConcurrentHashMap();
      this._mzNamesMap = new ConcurrentHashMap();
      this._namesTrie = new TextTrieMap(true);
      this._namesTrieFullyLoaded = false;
      TimeZone var3 = TimeZone.getDefault();
      String var4 = ZoneMeta.getCanonicalCLDRID(var3);
      if (var4 != null) {
         this.loadStrings(var4);
      }

   }

   private synchronized void loadStrings(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.loadTimeZoneNames(var1);
         Set var2 = this.getAvailableMetaZoneIDs(var1);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            this.loadMetaZoneNames(var4);
         }

      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      ULocale var2 = this._zoneStrings.getULocale();
      var1.writeObject(var2);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      ULocale var2 = (ULocale)var1.readObject();
      this.initialize(var2);
   }

   private synchronized TimeZoneNamesImpl.ZNames loadMetaZoneNames(String var1) {
      TimeZoneNamesImpl.ZNames var2 = (TimeZoneNamesImpl.ZNames)this._mzNamesMap.get(var1);
      if (var2 == null) {
         var2 = TimeZoneNamesImpl.ZNames.getInstance(this._zoneStrings, "meta:" + var1);
         var1 = var1.intern();
         TimeZoneNames.NameType[] var3 = TimeZoneNames.NameType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TimeZoneNames.NameType var6 = var3[var5];
            String var7 = var2.getName(var6);
            if (var7 != null) {
               TimeZoneNamesImpl.NameInfo var8 = new TimeZoneNamesImpl.NameInfo();
               var8.mzID = var1;
               var8.type = var6;
               this._namesTrie.put(var7, var8);
            }
         }

         TimeZoneNamesImpl.ZNames var9 = (TimeZoneNamesImpl.ZNames)this._mzNamesMap.putIfAbsent(var1, var2);
         var2 = var9 == null ? var2 : var9;
      }

      return var2;
   }

   private synchronized TimeZoneNamesImpl.TZNames loadTimeZoneNames(String var1) {
      TimeZoneNamesImpl.TZNames var2 = (TimeZoneNamesImpl.TZNames)this._tzNamesMap.get(var1);
      if (var2 == null) {
         var2 = TimeZoneNamesImpl.TZNames.getInstance(this._zoneStrings, var1.replace('/', ':'), var1);
         var1 = var1.intern();
         TimeZoneNames.NameType[] var3 = TimeZoneNames.NameType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TimeZoneNames.NameType var6 = var3[var5];
            String var7 = var2.getName(var6);
            if (var7 != null) {
               TimeZoneNamesImpl.NameInfo var8 = new TimeZoneNamesImpl.NameInfo();
               var8.tzID = var1;
               var8.type = var6;
               this._namesTrie.put(var7, var8);
            }
         }

         TimeZoneNamesImpl.TZNames var9 = (TimeZoneNamesImpl.TZNames)this._tzNamesMap.putIfAbsent(var1, var2);
         var2 = var9 == null ? var2 : var9;
      }

      return var2;
   }

   public static String getDefaultExemplarLocationName(String var0) {
      if (var0 != null && var0.length() != 0 && !LOC_EXCLUSION_PATTERN.matcher(var0).matches()) {
         String var1 = null;
         int var2 = var0.lastIndexOf(47);
         if (var2 > 0 && var2 + 1 < var0.length()) {
            var1 = var0.substring(var2 + 1).replace('_', ' ');
         }

         return var1;
      } else {
         return null;
      }
   }

   private static class MZ2TZsCache extends SoftCache {
      private MZ2TZsCache() {
      }

      protected Map createInstance(String var1, String var2) {
         Object var3 = null;
         UResourceBundle var4 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
         UResourceBundle var5 = var4.get("mapTimezones");

         try {
            UResourceBundle var6 = var5.get(var1);
            Set var7 = var6.keySet();
            var3 = new HashMap(var7.size());
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               String var10 = var6.getString(var9).intern();
               ((Map)var3).put(var9.intern(), var10);
            }
         } catch (MissingResourceException var11) {
            var3 = Collections.emptyMap();
         }

         return (Map)var3;
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (String)var2);
      }

      MZ2TZsCache(Object var1) {
         this();
      }
   }

   private static class TZ2MZsCache extends SoftCache {
      private TZ2MZsCache() {
      }

      protected List createInstance(String var1, String var2) {
         Object var3 = null;
         UResourceBundle var4 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
         UResourceBundle var5 = var4.get("metazoneInfo");
         String var6 = var2.replace('/', ':');

         try {
            UResourceBundle var7 = var5.get(var6);
            var3 = new ArrayList(var7.getSize());

            for(int var8 = 0; var8 < var7.getSize(); ++var8) {
               UResourceBundle var9 = var7.get(var8);
               String var10 = var9.getString(0);
               String var11 = "1970-01-01 00:00";
               String var12 = "9999-12-31 23:59";
               if (var9.getSize() == 3) {
                  var11 = var9.getString(1);
                  var12 = var9.getString(2);
               }

               long var13 = parseDate(var11);
               long var15 = parseDate(var12);
               ((List)var3).add(new TimeZoneNamesImpl.MZMapEntry(var10, var13, var15));
            }
         } catch (MissingResourceException var17) {
            var3 = Collections.emptyList();
         }

         return (List)var3;
      }

      private static long parseDate(String var0) {
         int var1 = 0;
         int var2 = 0;
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;

         int var6;
         int var7;
         for(var6 = 0; var6 <= 3; ++var6) {
            var7 = var0.charAt(var6) - 48;
            if (var7 < 0 || var7 >= 10) {
               throw new IllegalArgumentException("Bad year");
            }

            var1 = 10 * var1 + var7;
         }

         for(var6 = 5; var6 <= 6; ++var6) {
            var7 = var0.charAt(var6) - 48;
            if (var7 < 0 || var7 >= 10) {
               throw new IllegalArgumentException("Bad month");
            }

            var2 = 10 * var2 + var7;
         }

         for(var6 = 8; var6 <= 9; ++var6) {
            var7 = var0.charAt(var6) - 48;
            if (var7 < 0 || var7 >= 10) {
               throw new IllegalArgumentException("Bad day");
            }

            var3 = 10 * var3 + var7;
         }

         for(var6 = 11; var6 <= 12; ++var6) {
            var7 = var0.charAt(var6) - 48;
            if (var7 < 0 || var7 >= 10) {
               throw new IllegalArgumentException("Bad hour");
            }

            var4 = 10 * var4 + var7;
         }

         for(var6 = 14; var6 <= 15; ++var6) {
            var7 = var0.charAt(var6) - 48;
            if (var7 < 0 || var7 >= 10) {
               throw new IllegalArgumentException("Bad minute");
            }

            var5 = 10 * var5 + var7;
         }

         long var8 = Grego.fieldsToDay(var1, var2 - 1, var3) * 86400000L + (long)var4 * 3600000L + (long)var5 * 60000L;
         return var8;
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (String)var2);
      }

      TZ2MZsCache(Object var1) {
         this();
      }
   }

   private static class MZMapEntry {
      private String _mzID;
      private long _from;
      private long _to;

      MZMapEntry(String var1, long var2, long var4) {
         this._mzID = var1;
         this._from = var2;
         this._to = var4;
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

   private static class TZNames extends TimeZoneNamesImpl.ZNames {
      private String _locationName;
      private static final TimeZoneNamesImpl.TZNames EMPTY_TZNAMES = new TimeZoneNamesImpl.TZNames((String[])null, (String)null);

      public static TimeZoneNamesImpl.TZNames getInstance(ICUResourceBundle var0, String var1, String var2) {
         if (var0 != null && var1 != null && var1.length() != 0) {
            String[] var3 = loadData(var0, var1);
            String var4 = null;
            ICUResourceBundle var5 = null;

            try {
               var5 = var0.getWithFallback(var1);
               var4 = var5.getStringWithFallback("ec");
            } catch (MissingResourceException var7) {
            }

            if (var4 == null) {
               var4 = TimeZoneNamesImpl.getDefaultExemplarLocationName(var2);
            }

            return var4 == null && var3 == null ? EMPTY_TZNAMES : new TimeZoneNamesImpl.TZNames(var3, var4);
         } else {
            return EMPTY_TZNAMES;
         }
      }

      public String getName(TimeZoneNames.NameType var1) {
         return var1 == TimeZoneNames.NameType.EXEMPLAR_LOCATION ? this._locationName : super.getName(var1);
      }

      private TZNames(String[] var1, String var2) {
         super(var1);
         this._locationName = var2;
      }
   }

   private static class ZNames {
      private static final TimeZoneNamesImpl.ZNames EMPTY_ZNAMES = new TimeZoneNamesImpl.ZNames((String[])null);
      private String[] _names;
      private static final String[] KEYS = new String[]{"lg", "ls", "ld", "sg", "ss", "sd"};

      protected ZNames(String[] var1) {
         this._names = var1;
      }

      public static TimeZoneNamesImpl.ZNames getInstance(ICUResourceBundle var0, String var1) {
         String[] var2 = loadData(var0, var1);
         return var2 == null ? EMPTY_ZNAMES : new TimeZoneNamesImpl.ZNames(var2);
      }

      public String getName(TimeZoneNames.NameType var1) {
         if (this._names == null) {
            return null;
         } else {
            String var2 = null;
            switch(var1) {
            case LONG_GENERIC:
               var2 = this._names[0];
               break;
            case LONG_STANDARD:
               var2 = this._names[1];
               break;
            case LONG_DAYLIGHT:
               var2 = this._names[2];
               break;
            case SHORT_GENERIC:
               var2 = this._names[3];
               break;
            case SHORT_STANDARD:
               var2 = this._names[4];
               break;
            case SHORT_DAYLIGHT:
               var2 = this._names[5];
               break;
            case EXEMPLAR_LOCATION:
               var2 = null;
            }

            return var2;
         }
      }

      protected static String[] loadData(ICUResourceBundle var0, String var1) {
         if (var0 != null && var1 != null && var1.length() != 0) {
            ICUResourceBundle var2 = null;

            try {
               var2 = var0.getWithFallback(var1);
            } catch (MissingResourceException var8) {
               return null;
            }

            boolean var3 = true;
            String[] var4 = new String[KEYS.length];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               try {
                  var4[var5] = var2.getStringWithFallback(KEYS[var5]);
                  var3 = false;
               } catch (MissingResourceException var7) {
                  var4[var5] = null;
               }
            }

            return var3 ? null : var4;
         } else {
            return null;
         }
      }
   }

   private static class NameSearchHandler implements TextTrieMap.ResultHandler {
      private EnumSet _nameTypes;
      private Collection _matches;
      private int _maxMatchLen;
      static final boolean $assertionsDisabled = !TimeZoneNamesImpl.class.desiredAssertionStatus();

      NameSearchHandler(EnumSet var1) {
         this._nameTypes = var1;
      }

      public boolean handlePrefixMatch(int var1, Iterator var2) {
         while(var2.hasNext()) {
            TimeZoneNamesImpl.NameInfo var3 = (TimeZoneNamesImpl.NameInfo)var2.next();
            if (this._nameTypes == null || this._nameTypes.contains(var3.type)) {
               TimeZoneNames.MatchInfo var4;
               if (var3.tzID != null) {
                  var4 = new TimeZoneNames.MatchInfo(var3.type, var3.tzID, (String)null, var1);
               } else {
                  if (!$assertionsDisabled && var3.mzID == null) {
                     throw new AssertionError();
                  }

                  var4 = new TimeZoneNames.MatchInfo(var3.type, (String)null, var3.mzID, var1);
               }

               if (this._matches == null) {
                  this._matches = new LinkedList();
               }

               this._matches.add(var4);
               if (var1 > this._maxMatchLen) {
                  this._maxMatchLen = var1;
               }
            }
         }

         return true;
      }

      public Collection getMatches() {
         return (Collection)(this._matches == null ? Collections.emptyList() : this._matches);
      }

      public int getMaxMatchLen() {
         return this._maxMatchLen;
      }

      public void resetResults() {
         this._matches = null;
         this._maxMatchLen = 0;
      }
   }

   private static class NameInfo {
      String tzID;
      String mzID;
      TimeZoneNames.NameType type;

      private NameInfo() {
      }

      NameInfo(Object var1) {
         this();
      }
   }
}
