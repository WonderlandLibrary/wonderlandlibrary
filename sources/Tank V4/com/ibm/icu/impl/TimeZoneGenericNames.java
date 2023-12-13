package com.ibm.icu.impl;

import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.text.TimeZoneFormat;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TimeZoneGenericNames implements Serializable, Freezable {
   private static final long serialVersionUID = 2729910342063468417L;
   private ULocale _locale;
   private TimeZoneNames _tznames;
   private transient boolean _frozen;
   private transient String _region;
   private transient WeakReference _localeDisplayNamesRef;
   private transient MessageFormat[] _patternFormatters;
   private transient ConcurrentHashMap _genericLocationNamesMap;
   private transient ConcurrentHashMap _genericPartialLocationNamesMap;
   private transient TextTrieMap _gnamesTrie;
   private transient boolean _gnamesTrieFullyLoaded;
   private static TimeZoneGenericNames.Cache GENERIC_NAMES_CACHE = new TimeZoneGenericNames.Cache();
   private static final long DST_CHECK_RANGE = 15897600000L;
   private static final TimeZoneNames.NameType[] GENERIC_NON_LOCATION_TYPES;
   static final boolean $assertionsDisabled = !TimeZoneGenericNames.class.desiredAssertionStatus();

   public TimeZoneGenericNames(ULocale var1, TimeZoneNames var2) {
      this._locale = var1;
      this._tznames = var2;
      this.init();
   }

   private void init() {
      if (this._tznames == null) {
         this._tznames = TimeZoneNames.getInstance(this._locale);
      }

      this._genericLocationNamesMap = new ConcurrentHashMap();
      this._genericPartialLocationNamesMap = new ConcurrentHashMap();
      this._gnamesTrie = new TextTrieMap(true);
      this._gnamesTrieFullyLoaded = false;
      TimeZone var1 = TimeZone.getDefault();
      String var2 = ZoneMeta.getCanonicalCLDRID(var1);
      if (var2 != null) {
         this.loadStrings(var2);
      }

   }

   private TimeZoneGenericNames(ULocale var1) {
      this(var1, (TimeZoneNames)null);
   }

   public static TimeZoneGenericNames getInstance(ULocale var0) {
      String var1 = var0.getBaseName();
      return (TimeZoneGenericNames)GENERIC_NAMES_CACHE.getInstance(var1, var0);
   }

   public String getDisplayName(TimeZone var1, TimeZoneGenericNames.GenericNameType var2, long var3) {
      String var5 = null;
      String var6 = null;
      switch(var2) {
      case LOCATION:
         var6 = ZoneMeta.getCanonicalCLDRID(var1);
         if (var6 != null) {
            var5 = this.getGenericLocationName(var6);
         }
         break;
      case LONG:
      case SHORT:
         var5 = this.formatGenericNonLocationName(var1, var2, var3);
         if (var5 == null) {
            var6 = ZoneMeta.getCanonicalCLDRID(var1);
            if (var6 != null) {
               var5 = this.getGenericLocationName(var6);
            }
         }
      }

      return var5;
   }

   public String getGenericLocationName(String var1) {
      if (var1 != null && var1.length() != 0) {
         String var2 = (String)this._genericLocationNamesMap.get(var1);
         if (var2 != null) {
            return var2.length() == 0 ? null : var2;
         } else {
            Output var3 = new Output();
            String var4 = ZoneMeta.getCanonicalCountry(var1, var3);
            if (var4 != null) {
               String var5;
               if ((Boolean)var3.value) {
                  var5 = this.getLocaleDisplayNames().regionDisplayName(var4);
                  var2 = this.formatPattern(TimeZoneGenericNames.Pattern.REGION_FORMAT, var5);
               } else {
                  var5 = this._tznames.getExemplarLocationName(var1);
                  var2 = this.formatPattern(TimeZoneGenericNames.Pattern.REGION_FORMAT, var5);
               }
            }

            if (var2 == null) {
               this._genericLocationNamesMap.putIfAbsent(var1.intern(), "");
            } else {
               synchronized(this){}
               var1 = var1.intern();
               String var6 = (String)this._genericLocationNamesMap.putIfAbsent(var1, var2.intern());
               if (var6 == null) {
                  TimeZoneGenericNames.NameInfo var7 = new TimeZoneGenericNames.NameInfo();
                  var7.tzID = var1;
                  var7.type = TimeZoneGenericNames.GenericNameType.LOCATION;
                  this._gnamesTrie.put(var2, var7);
               } else {
                  var2 = var6;
               }
            }

            return var2;
         }
      } else {
         return null;
      }
   }

   public TimeZoneGenericNames setFormatPattern(TimeZoneGenericNames.Pattern var1, String var2) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else {
         if (!this._genericLocationNamesMap.isEmpty()) {
            this._genericLocationNamesMap = new ConcurrentHashMap();
         }

         if (!this._genericPartialLocationNamesMap.isEmpty()) {
            this._genericPartialLocationNamesMap = new ConcurrentHashMap();
         }

         this._gnamesTrie = null;
         this._gnamesTrieFullyLoaded = false;
         if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[TimeZoneGenericNames.Pattern.values().length];
         }

         this._patternFormatters[var1.ordinal()] = new MessageFormat(var2);
         return this;
      }
   }

   private String formatGenericNonLocationName(TimeZone var1, TimeZoneGenericNames.GenericNameType var2, long var3) {
      if (!$assertionsDisabled && var2 != TimeZoneGenericNames.GenericNameType.LONG && var2 != TimeZoneGenericNames.GenericNameType.SHORT) {
         throw new AssertionError();
      } else {
         String var5 = ZoneMeta.getCanonicalCLDRID(var1);
         if (var5 == null) {
            return null;
         } else {
            TimeZoneNames.NameType var6 = var2 == TimeZoneGenericNames.GenericNameType.LONG ? TimeZoneNames.NameType.LONG_GENERIC : TimeZoneNames.NameType.SHORT_GENERIC;
            String var7 = this._tznames.getTimeZoneDisplayName(var5, var6);
            if (var7 != null) {
               return var7;
            } else {
               String var8 = this._tznames.getMetaZoneID(var5, var3);
               if (var8 != null) {
                  boolean var9 = false;
                  int[] var10 = new int[]{0, 0};
                  var1.getOffset(var3, false, var10);
                  if (var10[1] == 0) {
                     var9 = true;
                     if (var1 instanceof BasicTimeZone) {
                        BasicTimeZone var15 = (BasicTimeZone)var1;
                        TimeZoneTransition var12 = var15.getPreviousTransition(var3, true);
                        if (var12 != null && var3 - var12.getTime() < 15897600000L && var12.getFrom().getDSTSavings() != 0) {
                           var9 = false;
                        } else {
                           TimeZoneTransition var13 = var15.getNextTransition(var3, false);
                           if (var13 != null && var13.getTime() - var3 < 15897600000L && var13.getTo().getDSTSavings() != 0) {
                              var9 = false;
                           }
                        }
                     } else {
                        int[] var11 = new int[2];
                        var1.getOffset(var3 - 15897600000L, false, var11);
                        if (var11[1] != 0) {
                           var9 = false;
                        } else {
                           var1.getOffset(var3 + 15897600000L, false, var11);
                           if (var11[1] != 0) {
                              var9 = false;
                           }
                        }
                     }
                  }

                  String var18;
                  if (var9) {
                     TimeZoneNames.NameType var16 = var6 == TimeZoneNames.NameType.LONG_GENERIC ? TimeZoneNames.NameType.LONG_STANDARD : TimeZoneNames.NameType.SHORT_STANDARD;
                     var18 = this._tznames.getDisplayName(var5, var16, var3);
                     if (var18 != null) {
                        var7 = var18;
                        String var19 = this._tznames.getMetaZoneDisplayName(var8, var6);
                        if (var18.equalsIgnoreCase(var19)) {
                           var7 = null;
                        }
                     }
                  }

                  if (var7 == null) {
                     String var17 = this._tznames.getMetaZoneDisplayName(var8, var6);
                     if (var17 != null) {
                        var18 = this._tznames.getReferenceZoneID(var8, this.getTargetRegion());
                        if (var18 != null && !var18.equals(var5)) {
                           TimeZone var20 = TimeZone.getFrozenTimeZone(var18);
                           int[] var14 = new int[]{0, 0};
                           var20.getOffset(var3 + (long)var10[0] + (long)var10[1], true, var14);
                           if (var10[0] == var14[0] && var10[1] == var14[1]) {
                              var7 = var17;
                           } else {
                              var7 = this.getPartialLocationName(var5, var8, var6 == TimeZoneNames.NameType.LONG_GENERIC, var17);
                           }
                        } else {
                           var7 = var17;
                        }
                     }
                  }
               }

               return var7;
            }
         }
      }
   }

   private synchronized String formatPattern(TimeZoneGenericNames.Pattern var1, String... var2) {
      if (this._patternFormatters == null) {
         this._patternFormatters = new MessageFormat[TimeZoneGenericNames.Pattern.values().length];
      }

      int var3 = var1.ordinal();
      if (this._patternFormatters[var3] == null) {
         String var4;
         try {
            ICUResourceBundle var5 = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", this._locale);
            var4 = var5.getStringWithFallback("zoneStrings/" + var1.key());
         } catch (MissingResourceException var6) {
            var4 = var1.defaultValue();
         }

         this._patternFormatters[var3] = new MessageFormat(var4);
      }

      return this._patternFormatters[var3].format(var2);
   }

   private synchronized LocaleDisplayNames getLocaleDisplayNames() {
      LocaleDisplayNames var1 = null;
      if (this._localeDisplayNamesRef != null) {
         var1 = (LocaleDisplayNames)this._localeDisplayNamesRef.get();
      }

      if (var1 == null) {
         var1 = LocaleDisplayNames.getInstance(this._locale);
         this._localeDisplayNamesRef = new WeakReference(var1);
      }

      return var1;
   }

   private synchronized void loadStrings(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.getGenericLocationName(var1);
         Set var2 = this._tznames.getAvailableMetaZoneIDs(var1);
         Iterator var3 = var2.iterator();

         while(true) {
            String var4;
            String var5;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               var4 = (String)var3.next();
               var5 = this._tznames.getReferenceZoneID(var4, this.getTargetRegion());
            } while(var1.equals(var5));

            TimeZoneNames.NameType[] var6 = GENERIC_NON_LOCATION_TYPES;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               TimeZoneNames.NameType var9 = var6[var8];
               String var10 = this._tznames.getMetaZoneDisplayName(var4, var9);
               if (var10 != null) {
                  this.getPartialLocationName(var1, var4, var9 == TimeZoneNames.NameType.LONG_GENERIC, var10);
               }
            }
         }
      }
   }

   private synchronized String getTargetRegion() {
      if (this._region == null) {
         this._region = this._locale.getCountry();
         if (this._region.length() == 0) {
            ULocale var1 = ULocale.addLikelySubtags(this._locale);
            this._region = var1.getCountry();
            if (this._region.length() == 0) {
               this._region = "001";
            }
         }
      }

      return this._region;
   }

   private String getPartialLocationName(String var1, String var2, boolean var3, String var4) {
      String var5 = var3 ? "L" : "S";
      String var6 = var1 + "&" + var2 + "#" + var5;
      String var7 = (String)this._genericPartialLocationNamesMap.get(var6);
      if (var7 != null) {
         return var7;
      } else {
         String var8 = null;
         String var9 = ZoneMeta.getCanonicalCountry(var1);
         if (var9 != null) {
            String var10 = this._tznames.getReferenceZoneID(var2, var9);
            if (var1.equals(var10)) {
               var8 = this.getLocaleDisplayNames().regionDisplayName(var9);
            } else {
               var8 = this._tznames.getExemplarLocationName(var1);
            }
         } else {
            var8 = this._tznames.getExemplarLocationName(var1);
            if (var8 == null) {
               var8 = var1;
            }
         }

         var7 = this.formatPattern(TimeZoneGenericNames.Pattern.FALLBACK_FORMAT, var8, var4);
         synchronized(this){}
         String var11 = (String)this._genericPartialLocationNamesMap.putIfAbsent(var6.intern(), var7.intern());
         if (var11 == null) {
            TimeZoneGenericNames.NameInfo var12 = new TimeZoneGenericNames.NameInfo();
            var12.tzID = var1.intern();
            var12.type = var3 ? TimeZoneGenericNames.GenericNameType.LONG : TimeZoneGenericNames.GenericNameType.SHORT;
            this._gnamesTrie.put(var7, var12);
         } else {
            var7 = var11;
         }

         return var7;
      }
   }

   public TimeZoneGenericNames.GenericMatchInfo findBestMatch(String var1, int var2, EnumSet var3) {
      if (var1 != null && var1.length() != 0 && var2 >= 0 && var2 < var1.length()) {
         TimeZoneGenericNames.GenericMatchInfo var4 = null;
         Collection var5 = this.findTimeZoneNames(var1, var2, var3);
         Iterator var7;
         if (var5 != null) {
            TimeZoneNames.MatchInfo var6 = null;
            var7 = var5.iterator();

            label53:
            while(true) {
               TimeZoneNames.MatchInfo var8;
               do {
                  if (!var7.hasNext()) {
                     if (var6 != null) {
                        var4 = this.createGenericMatchInfo(var6);
                        if (var4.matchLength() == var1.length() - var2 && var4.timeType != TimeZoneFormat.TimeType.STANDARD) {
                           return var4;
                        }
                     }
                     break label53;
                  }

                  var8 = (TimeZoneNames.MatchInfo)var7.next();
               } while(var6 != null && var8.matchLength() <= var6.matchLength());

               var6 = var8;
            }
         }

         Collection var9 = this.findLocal(var1, var2, var3);
         if (var9 != null) {
            var7 = var9.iterator();

            while(true) {
               TimeZoneGenericNames.GenericMatchInfo var10;
               do {
                  if (!var7.hasNext()) {
                     return var4;
                  }

                  var10 = (TimeZoneGenericNames.GenericMatchInfo)var7.next();
               } while(var4 != null && var10.matchLength() < var4.matchLength());

               var4 = var10;
            }
         } else {
            return var4;
         }
      } else {
         throw new IllegalArgumentException("bad input text or range");
      }
   }

   public Collection find(String var1, int var2, EnumSet var3) {
      if (var1 != null && var1.length() != 0 && var2 >= 0 && var2 < var1.length()) {
         Object var4 = this.findLocal(var1, var2, var3);
         Collection var5 = this.findTimeZoneNames(var1, var2, var3);
         TimeZoneNames.MatchInfo var7;
         if (var5 != null) {
            for(Iterator var6 = var5.iterator(); var6.hasNext(); ((Collection)var4).add(this.createGenericMatchInfo(var7))) {
               var7 = (TimeZoneNames.MatchInfo)var6.next();
               if (var4 == null) {
                  var4 = new LinkedList();
               }
            }
         }

         return (Collection)var4;
      } else {
         throw new IllegalArgumentException("bad input text or range");
      }
   }

   private TimeZoneGenericNames.GenericMatchInfo createGenericMatchInfo(TimeZoneNames.MatchInfo var1) {
      TimeZoneGenericNames.GenericNameType var2 = null;
      TimeZoneFormat.TimeType var3 = TimeZoneFormat.TimeType.UNKNOWN;
      switch(var1.nameType()) {
      case LONG_STANDARD:
         var2 = TimeZoneGenericNames.GenericNameType.LONG;
         var3 = TimeZoneFormat.TimeType.STANDARD;
         break;
      case LONG_GENERIC:
         var2 = TimeZoneGenericNames.GenericNameType.LONG;
         break;
      case SHORT_STANDARD:
         var2 = TimeZoneGenericNames.GenericNameType.SHORT;
         var3 = TimeZoneFormat.TimeType.STANDARD;
         break;
      case SHORT_GENERIC:
         var2 = TimeZoneGenericNames.GenericNameType.SHORT;
      }

      if (!$assertionsDisabled && var2 == null) {
         throw new AssertionError();
      } else {
         String var4 = var1.tzID();
         if (var4 == null) {
            String var5 = var1.mzID();
            if (!$assertionsDisabled && var5 == null) {
               throw new AssertionError();
            }

            var4 = this._tznames.getReferenceZoneID(var5, this.getTargetRegion());
         }

         if (!$assertionsDisabled && var4 == null) {
            throw new AssertionError();
         } else {
            TimeZoneGenericNames.GenericMatchInfo var6 = new TimeZoneGenericNames.GenericMatchInfo();
            var6.nameType = var2;
            var6.tzID = var4;
            var6.matchLength = var1.matchLength();
            var6.timeType = var3;
            return var6;
         }
      }
   }

   private Collection findTimeZoneNames(String var1, int var2, EnumSet var3) {
      Collection var4 = null;
      EnumSet var5 = EnumSet.noneOf(TimeZoneNames.NameType.class);
      if (var3.contains(TimeZoneGenericNames.GenericNameType.LONG)) {
         var5.add(TimeZoneNames.NameType.LONG_GENERIC);
         var5.add(TimeZoneNames.NameType.LONG_STANDARD);
      }

      if (var3.contains(TimeZoneGenericNames.GenericNameType.SHORT)) {
         var5.add(TimeZoneNames.NameType.SHORT_GENERIC);
         var5.add(TimeZoneNames.NameType.SHORT_STANDARD);
      }

      if (!var5.isEmpty()) {
         var4 = this._tznames.find(var1, var2, var5);
      }

      return var4;
   }

   private synchronized Collection findLocal(String var1, int var2, EnumSet var3) {
      TimeZoneGenericNames.GenericNameSearchHandler var4 = new TimeZoneGenericNames.GenericNameSearchHandler(var3);
      this._gnamesTrie.find(var1, var2, var4);
      if (var4.getMaxMatchLen() != var1.length() - var2 && !this._gnamesTrieFullyLoaded) {
         Set var5 = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, (String)null, (Integer)null);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            this.loadStrings(var7);
         }

         this._gnamesTrieFullyLoaded = true;
         var4.resetResults();
         this._gnamesTrie.find(var1, var2, var4);
         return var4.getMatches();
      } else {
         return var4.getMatches();
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.init();
   }

   public boolean isFrozen() {
      return this._frozen;
   }

   public TimeZoneGenericNames freeze() {
      this._frozen = true;
      return this;
   }

   public TimeZoneGenericNames cloneAsThawed() {
      TimeZoneGenericNames var1 = null;

      try {
         var1 = (TimeZoneGenericNames)super.clone();
         var1._frozen = false;
      } catch (Throwable var3) {
      }

      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   TimeZoneGenericNames(ULocale var1, Object var2) {
      this(var1);
   }

   static {
      GENERIC_NON_LOCATION_TYPES = new TimeZoneNames.NameType[]{TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC};
   }

   private static class Cache extends SoftCache {
      private Cache() {
      }

      protected TimeZoneGenericNames createInstance(String var1, ULocale var2) {
         return (new TimeZoneGenericNames(var2)).freeze();
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (ULocale)var2);
      }

      Cache(Object var1) {
         this();
      }
   }

   private static class GenericNameSearchHandler implements TextTrieMap.ResultHandler {
      private EnumSet _types;
      private Collection _matches;
      private int _maxMatchLen;

      GenericNameSearchHandler(EnumSet var1) {
         this._types = var1;
      }

      public boolean handlePrefixMatch(int var1, Iterator var2) {
         while(var2.hasNext()) {
            TimeZoneGenericNames.NameInfo var3 = (TimeZoneGenericNames.NameInfo)var2.next();
            if (this._types == null || this._types.contains(var3.type)) {
               TimeZoneGenericNames.GenericMatchInfo var4 = new TimeZoneGenericNames.GenericMatchInfo();
               var4.tzID = var3.tzID;
               var4.nameType = var3.type;
               var4.matchLength = var1;
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

   public static class GenericMatchInfo {
      TimeZoneGenericNames.GenericNameType nameType;
      String tzID;
      int matchLength;
      TimeZoneFormat.TimeType timeType;

      public GenericMatchInfo() {
         this.timeType = TimeZoneFormat.TimeType.UNKNOWN;
      }

      public TimeZoneGenericNames.GenericNameType nameType() {
         return this.nameType;
      }

      public String tzID() {
         return this.tzID;
      }

      public TimeZoneFormat.TimeType timeType() {
         return this.timeType;
      }

      public int matchLength() {
         return this.matchLength;
      }
   }

   private static class NameInfo {
      String tzID;
      TimeZoneGenericNames.GenericNameType type;

      private NameInfo() {
      }

      NameInfo(Object var1) {
         this();
      }
   }

   public static enum Pattern {
      REGION_FORMAT("regionFormat", "({0})"),
      FALLBACK_FORMAT("fallbackFormat", "{1} ({0})");

      String _key;
      String _defaultVal;
      private static final TimeZoneGenericNames.Pattern[] $VALUES = new TimeZoneGenericNames.Pattern[]{REGION_FORMAT, FALLBACK_FORMAT};

      private Pattern(String var3, String var4) {
         this._key = var3;
         this._defaultVal = var4;
      }

      String key() {
         return this._key;
      }

      String defaultValue() {
         return this._defaultVal;
      }
   }

   public static enum GenericNameType {
      LOCATION(new String[]{"LONG", "SHORT"}),
      LONG(new String[0]),
      SHORT(new String[0]);

      String[] _fallbackTypeOf;
      private static final TimeZoneGenericNames.GenericNameType[] $VALUES = new TimeZoneGenericNames.GenericNameType[]{LOCATION, LONG, SHORT};

      private GenericNameType(String... var3) {
         this._fallbackTypeOf = var3;
      }

      public boolean isFallbackTypeOf(TimeZoneGenericNames.GenericNameType var1) {
         String var2 = var1.toString();
         String[] var3 = this._fallbackTypeOf;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.equals(var2)) {
               return true;
            }
         }

         return false;
      }
   }
}
