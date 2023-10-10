package com.ibm.icu.impl;

import com.ibm.icu.util.Output;
import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.UResourceBundle;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeSet;

public final class ZoneMeta {
   private static final boolean ASSERT = false;
   private static final String ZONEINFORESNAME = "zoneinfo64";
   private static final String kREGIONS = "Regions";
   private static final String kZONES = "Zones";
   private static final String kNAMES = "Names";
   private static final String kGMT_ID = "GMT";
   private static final String kCUSTOM_TZ_PREFIX = "GMT";
   private static final String kWorld = "001";
   private static SoftReference REF_SYSTEM_ZONES;
   private static SoftReference REF_CANONICAL_SYSTEM_ZONES;
   private static SoftReference REF_CANONICAL_SYSTEM_LOCATION_ZONES;
   private static String[] ZONEIDS = null;
   private static ICUCache CANONICAL_ID_CACHE = new SimpleCache();
   private static ICUCache REGION_CACHE = new SimpleCache();
   private static ICUCache SINGLE_COUNTRY_CACHE = new SimpleCache();
   private static final ZoneMeta.SystemTimeZoneCache SYSTEM_ZONE_CACHE = new ZoneMeta.SystemTimeZoneCache();
   private static final int kMAX_CUSTOM_HOUR = 23;
   private static final int kMAX_CUSTOM_MIN = 59;
   private static final int kMAX_CUSTOM_SEC = 59;
   private static final ZoneMeta.CustomTimeZoneCache CUSTOM_ZONE_CACHE = new ZoneMeta.CustomTimeZoneCache();
   static final boolean $assertionsDisabled = !ZoneMeta.class.desiredAssertionStatus();

   private static synchronized Set getSystemZIDs() {
      Set var0 = null;
      if (REF_SYSTEM_ZONES != null) {
         var0 = (Set)REF_SYSTEM_ZONES.get();
      }

      if (var0 == null) {
         TreeSet var1 = new TreeSet();
         String[] var2 = getZoneIDs();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.equals("Etc/Unknown")) {
               var1.add(var6);
            }
         }

         var0 = Collections.unmodifiableSet(var1);
         REF_SYSTEM_ZONES = new SoftReference(var0);
      }

      return var0;
   }

   private static synchronized Set getCanonicalSystemZIDs() {
      Set var0 = null;
      if (REF_CANONICAL_SYSTEM_ZONES != null) {
         var0 = (Set)REF_CANONICAL_SYSTEM_ZONES.get();
      }

      if (var0 == null) {
         TreeSet var1 = new TreeSet();
         String[] var2 = getZoneIDs();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.equals("Etc/Unknown")) {
               String var7 = getCanonicalCLDRID(var6);
               if (var6.equals(var7)) {
                  var1.add(var6);
               }
            }
         }

         var0 = Collections.unmodifiableSet(var1);
         REF_CANONICAL_SYSTEM_ZONES = new SoftReference(var0);
      }

      return var0;
   }

   private static synchronized Set getCanonicalSystemLocationZIDs() {
      Set var0 = null;
      if (REF_CANONICAL_SYSTEM_LOCATION_ZONES != null) {
         var0 = (Set)REF_CANONICAL_SYSTEM_LOCATION_ZONES.get();
      }

      if (var0 == null) {
         TreeSet var1 = new TreeSet();
         String[] var2 = getZoneIDs();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (!var6.equals("Etc/Unknown")) {
               String var7 = getCanonicalCLDRID(var6);
               if (var6.equals(var7)) {
                  String var8 = getRegion(var6);
                  if (var8 != null && !var8.equals("001")) {
                     var1.add(var6);
                  }
               }
            }
         }

         var0 = Collections.unmodifiableSet(var1);
         REF_CANONICAL_SYSTEM_LOCATION_ZONES = new SoftReference(var0);
      }

      return var0;
   }

   public static Set getAvailableIDs(TimeZone.SystemTimeZoneType var0, String var1, Integer var2) {
      Set var3 = null;
      switch(var0) {
      case ANY:
         var3 = getSystemZIDs();
         break;
      case CANONICAL:
         var3 = getCanonicalSystemZIDs();
         break;
      case CANONICAL_LOCATION:
         var3 = getCanonicalSystemLocationZIDs();
         break;
      default:
         throw new IllegalArgumentException("Unknown SystemTimeZoneType");
      }

      if (var1 == null && var2 == null) {
         return var3;
      } else {
         if (var1 != null) {
            var1 = var1.toUpperCase(Locale.ENGLISH);
         }

         TreeSet var4 = new TreeSet();
         Iterator var5 = var3.iterator();

         while(true) {
            String var6;
            TimeZone var8;
            do {
               String var7;
               do {
                  if (!var5.hasNext()) {
                     if (var4.isEmpty()) {
                        return Collections.emptySet();
                     }

                     return Collections.unmodifiableSet(var4);
                  }

                  var6 = (String)var5.next();
                  if (var1 == null) {
                     break;
                  }

                  var7 = getRegion(var6);
               } while(!var1.equals(var7));

               if (var2 == null) {
                  break;
               }

               var8 = getSystemTimeZone(var6);
            } while(var8 == null || !var2.equals(var8.getRawOffset()));

            var4.add(var6);
         }
      }
   }

   public static synchronized int countEquivalentIDs(String var0) {
      int var1 = 0;
      UResourceBundle var2 = openOlsonResource((UResourceBundle)null, var0);
      if (var2 != null) {
         try {
            UResourceBundle var3 = var2.get("links");
            int[] var4 = var3.getIntVector();
            var1 = var4.length;
         } catch (MissingResourceException var5) {
         }
      }

      return var1;
   }

   public static synchronized String getEquivalentID(String var0, int var1) {
      String var2 = "";
      if (var1 >= 0) {
         UResourceBundle var3 = openOlsonResource((UResourceBundle)null, var0);
         if (var3 != null) {
            int var4 = -1;

            try {
               UResourceBundle var5 = var3.get("links");
               int[] var6 = var5.getIntVector();
               if (var1 < var6.length) {
                  var4 = var6[var1];
               }
            } catch (MissingResourceException var7) {
            }

            if (var4 >= 0) {
               String var8 = getZoneID(var4);
               if (var8 != null) {
                  var2 = var8;
               }
            }
         }
      }

      return var2;
   }

   private static synchronized String[] getZoneIDs() {
      if (ZONEIDS == null) {
         try {
            UResourceBundle var0 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle var1 = var0.get("Names");
            ZONEIDS = var1.getStringArray();
         } catch (MissingResourceException var2) {
         }
      }

      if (ZONEIDS == null) {
         ZONEIDS = new String[0];
      }

      return ZONEIDS;
   }

   private static String getZoneID(int var0) {
      if (var0 >= 0) {
         String[] var1 = getZoneIDs();
         if (var0 < var1.length) {
            return var1[var0];
         }
      }

      return null;
   }

   private static int getZoneIndex(String var0) {
      int var1 = -1;
      String[] var2 = getZoneIDs();
      if (var2.length > 0) {
         int var3 = 0;
         int var4 = var2.length;
         int var5 = Integer.MAX_VALUE;

         while(true) {
            int var6 = (var3 + var4) / 2;
            if (var5 == var6) {
               break;
            }

            var5 = var6;
            int var7 = var0.compareTo(var2[var6]);
            if (var7 == 0) {
               var1 = var6;
               break;
            }

            if (var7 < 0) {
               var4 = var6;
            } else {
               var3 = var6;
            }
         }
      }

      return var1;
   }

   public static String getCanonicalCLDRID(TimeZone var0) {
      return var0 instanceof OlsonTimeZone ? ((OlsonTimeZone)var0).getCanonicalID() : getCanonicalCLDRID(var0.getID());
   }

   public static String getCanonicalCLDRID(String var0) {
      String var1 = (String)CANONICAL_ID_CACHE.get(var0);
      if (var1 == null) {
         var1 = findCLDRCanonicalID(var0);
         if (var1 == null) {
            try {
               int var2 = getZoneIndex(var0);
               if (var2 >= 0) {
                  UResourceBundle var3 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                  UResourceBundle var4 = var3.get("Zones");
                  UResourceBundle var5 = var4.get(var2);
                  if (var5.getType() == 7) {
                     var0 = getZoneID(var5.getInt());
                     var1 = findCLDRCanonicalID(var0);
                  }

                  if (var1 == null) {
                     var1 = var0;
                  }
               }
            } catch (MissingResourceException var6) {
            }
         }

         if (var1 != null) {
            CANONICAL_ID_CACHE.put(var0, var1);
         }
      }

      return var1;
   }

   private static String findCLDRCanonicalID(String var0) {
      String var1 = null;
      String var2 = var0.replace('/', ':');

      try {
         UResourceBundle var3 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         UResourceBundle var4 = var3.get("typeMap");
         UResourceBundle var5 = var4.get("timezone");

         try {
            var5.get(var2);
            var1 = var0;
         } catch (MissingResourceException var8) {
         }

         if (var1 == null) {
            UResourceBundle var6 = var3.get("typeAlias");
            UResourceBundle var7 = var6.get("timezone");
            var1 = var7.getString(var2);
         }
      } catch (MissingResourceException var9) {
      }

      return var1;
   }

   public static String getRegion(String var0) {
      String var1 = (String)REGION_CACHE.get(var0);
      if (var1 == null) {
         int var2 = getZoneIndex(var0);
         if (var2 >= 0) {
            try {
               UResourceBundle var3 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
               UResourceBundle var4 = var3.get("Regions");
               if (var2 < var4.getSize()) {
                  var1 = var4.getString(var2);
               }
            } catch (MissingResourceException var5) {
            }

            if (var1 != null) {
               REGION_CACHE.put(var0, var1);
            }
         }
      }

      return var1;
   }

   public static String getCanonicalCountry(String var0) {
      String var1 = getRegion(var0);
      if (var1 != null && var1.equals("001")) {
         var1 = null;
      }

      return var1;
   }

   public static String getCanonicalCountry(String var0, Output var1) {
      var1.value = Boolean.FALSE;
      String var2 = getRegion(var0);
      if (var2 != null && var2.equals("001")) {
         return null;
      } else {
         Boolean var3 = (Boolean)SINGLE_COUNTRY_CACHE.get(var0);
         if (var3 == null) {
            Set var4 = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL_LOCATION, var2, (Integer)null);
            if (!$assertionsDisabled && var4.size() < 1) {
               throw new AssertionError();
            }

            var3 = var4.size() <= 1;
            SINGLE_COUNTRY_CACHE.put(var0, var3);
         }

         if (var3) {
            var1.value = Boolean.TRUE;
         } else {
            try {
               UResourceBundle var9 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "metaZones");
               UResourceBundle var5 = var9.get("primaryZones");
               String var6 = var5.getString(var2);
               if (var0.equals(var6)) {
                  var1.value = Boolean.TRUE;
               } else {
                  String var7 = getCanonicalCLDRID(var0);
                  if (var7 != null && var7.equals(var6)) {
                     var1.value = Boolean.TRUE;
                  }
               }
            } catch (MissingResourceException var8) {
            }
         }

         return var2;
      }
   }

   public static UResourceBundle openOlsonResource(UResourceBundle var0, String var1) {
      UResourceBundle var2 = null;
      int var3 = getZoneIndex(var1);
      if (var3 >= 0) {
         try {
            if (var0 == null) {
               var0 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            }

            UResourceBundle var4 = var0.get("Zones");
            UResourceBundle var5 = var4.get(var3);
            if (var5.getType() == 7) {
               var5 = var4.get(var5.getInt());
            }

            var2 = var5;
         } catch (MissingResourceException var6) {
            var2 = null;
         }
      }

      return var2;
   }

   public static TimeZone getSystemTimeZone(String var0) {
      return (TimeZone)SYSTEM_ZONE_CACHE.getInstance(var0, var0);
   }

   public static TimeZone getCustomTimeZone(String var0) {
      int[] var1 = new int[4];
      if (var1 != null) {
         Integer var2 = var1[0] * (var1[1] | var1[2] << 5 | var1[3] << 11);
         return (TimeZone)CUSTOM_ZONE_CACHE.getInstance(var2, var1);
      } else {
         return null;
      }
   }

   public static String getCustomID(String var0) {
      int[] var1 = new int[4];
      return var1 != null ? formatCustomID(var1[1], var1[2], var1[3], var1[0] < 0) : null;
   }

   public static TimeZone getCustomTimeZone(int var0) {
      boolean var1 = false;
      int var2 = var0;
      if (var0 < 0) {
         var1 = true;
         var2 = -var0;
      }

      var2 /= 1000;
      int var5 = var2 % 60;
      var2 /= 60;
      int var4 = var2 % 60;
      int var3 = var2 / 60;
      String var6 = formatCustomID(var3, var4, var5, var1);
      return new SimpleTimeZone(var0, var6);
   }

   static String formatCustomID(int var0, int var1, int var2, boolean var3) {
      StringBuilder var4 = new StringBuilder("GMT");
      if (var0 != 0 || var1 != 0) {
         if (var3) {
            var4.append('-');
         } else {
            var4.append('+');
         }

         if (var0 < 10) {
            var4.append('0');
         }

         var4.append(var0);
         var4.append(':');
         if (var1 < 10) {
            var4.append('0');
         }

         var4.append(var1);
         if (var2 != 0) {
            var4.append(':');
            if (var2 < 10) {
               var4.append('0');
            }

            var4.append(var2);
         }
      }

      return var4.toString();
   }

   public static String getShortID(TimeZone var0) {
      String var1 = null;
      if (var0 instanceof OlsonTimeZone) {
         var1 = ((OlsonTimeZone)var0).getCanonicalID();
      }

      var1 = getCanonicalCLDRID(var0.getID());
      return var1 == null ? null : getShortIDFromCanonical(var1);
   }

   public static String getShortID(String var0) {
      String var1 = getCanonicalCLDRID(var0);
      return var1 == null ? null : getShortIDFromCanonical(var1);
   }

   private static String getShortIDFromCanonical(String var0) {
      String var1 = null;
      String var2 = var0.replace('/', ':');

      try {
         UResourceBundle var3 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         UResourceBundle var4 = var3.get("typeMap");
         UResourceBundle var5 = var4.get("timezone");
         var1 = var5.getString(var2);
      } catch (MissingResourceException var6) {
      }

      return var1;
   }

   private static class CustomTimeZoneCache extends SoftCache {
      static final boolean $assertionsDisabled = !ZoneMeta.class.desiredAssertionStatus();

      private CustomTimeZoneCache() {
      }

      protected SimpleTimeZone createInstance(Integer var1, int[] var2) {
         if (!$assertionsDisabled && var2.length != 4) {
            throw new AssertionError();
         } else if (!$assertionsDisabled && var2[0] != 1 && var2[0] != -1) {
            throw new AssertionError();
         } else if (!$assertionsDisabled && (var2[1] < 0 || var2[1] > 23)) {
            throw new AssertionError();
         } else if (!$assertionsDisabled && (var2[2] < 0 || var2[2] > 59)) {
            throw new AssertionError();
         } else if ($assertionsDisabled || var2[3] >= 0 && var2[3] <= 59) {
            String var3 = ZoneMeta.formatCustomID(var2[1], var2[2], var2[3], var2[0] < 0);
            int var4 = var2[0] * ((var2[1] * 60 + var2[2]) * 60 + var2[3]) * 1000;
            SimpleTimeZone var5 = new SimpleTimeZone(var4, var3);
            var5.freeze();
            return var5;
         } else {
            throw new AssertionError();
         }
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((Integer)var1, (int[])var2);
      }

      CustomTimeZoneCache(Object var1) {
         this();
      }
   }

   private static class SystemTimeZoneCache extends SoftCache {
      private SystemTimeZoneCache() {
      }

      protected OlsonTimeZone createInstance(String var1, String var2) {
         OlsonTimeZone var3 = null;

         try {
            UResourceBundle var4 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle var5 = ZoneMeta.openOlsonResource(var4, var2);
            if (var5 != null) {
               var3 = new OlsonTimeZone(var4, var5, var2);
               var3.freeze();
            }
         } catch (MissingResourceException var6) {
         }

         return var3;
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (String)var2);
      }

      SystemTimeZoneCache(Object var1) {
         this();
      }
   }
}
