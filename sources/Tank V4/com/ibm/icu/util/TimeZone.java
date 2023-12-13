package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.JavaTimeZone;
import com.ibm.icu.impl.OlsonTimeZone;
import com.ibm.icu.impl.TimeZoneAdapter;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.text.TimeZoneFormat;
import com.ibm.icu.text.TimeZoneNames;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

public abstract class TimeZone implements Serializable, Cloneable, Freezable {
   private static final Logger LOGGER = Logger.getLogger("com.ibm.icu.util.TimeZone");
   private static final long serialVersionUID = -744942128318337471L;
   public static final int TIMEZONE_ICU = 0;
   public static final int TIMEZONE_JDK = 1;
   public static final int SHORT = 0;
   public static final int LONG = 1;
   public static final int SHORT_GENERIC = 2;
   public static final int LONG_GENERIC = 3;
   public static final int SHORT_GMT = 4;
   public static final int LONG_GMT = 5;
   public static final int SHORT_COMMONLY_USED = 6;
   public static final int GENERIC_LOCATION = 7;
   public static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
   static final String GMT_ZONE_ID = "Etc/GMT";
   public static final TimeZone UNKNOWN_ZONE = (new SimpleTimeZone(0, "Etc/Unknown")).freeze();
   public static final TimeZone GMT_ZONE = (new SimpleTimeZone(0, "Etc/GMT")).freeze();
   private String ID;
   private static TimeZone defaultZone = null;
   private static String TZDATA_VERSION = null;
   private static int TZ_IMPL = 0;
   private static final String TZIMPL_CONFIG_KEY = "com.ibm.icu.util.TimeZone.DefaultTimeZoneType";
   private static final String TZIMPL_CONFIG_ICU = "ICU";
   private static final String TZIMPL_CONFIG_JDK = "JDK";
   static final boolean $assertionsDisabled = !TimeZone.class.desiredAssertionStatus();

   public TimeZone() {
   }

   /** @deprecated */
   protected TimeZone(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.ID = var1;
      }
   }

   public abstract int getOffset(int var1, int var2, int var3, int var4, int var5, int var6);

   public int getOffset(long var1) {
      int[] var3 = new int[2];
      this.getOffset(var1, false, var3);
      return var3[0] + var3[1];
   }

   public void getOffset(long var1, boolean var3, int[] var4) {
      var4[0] = this.getRawOffset();
      if (!var3) {
         var1 += (long)var4[0];
      }

      int[] var5 = new int[6];
      int var6 = 0;

      while(true) {
         Grego.timeToFields(var1, var5);
         var4[1] = this.getOffset(1, var5[0], var5[1], var5[2], var5[3], var5[5]) - var4[0];
         if (var6 != 0 || !var3 || var4[1] == 0) {
            return;
         }

         var1 -= (long)var4[1];
         ++var6;
      }
   }

   public abstract void setRawOffset(int var1);

   public abstract int getRawOffset();

   public String getID() {
      return this.ID;
   }

   public void setID(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen TimeZone instance.");
      } else {
         this.ID = var1;
      }
   }

   public final String getDisplayName() {
      return this._getDisplayName(3, false, ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public final String getDisplayName(Locale var1) {
      return this._getDisplayName(3, false, ULocale.forLocale(var1));
   }

   public final String getDisplayName(ULocale var1) {
      return this._getDisplayName(3, false, var1);
   }

   public final String getDisplayName(boolean var1, int var2) {
      return this.getDisplayName(var1, var2, ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String getDisplayName(boolean var1, int var2, Locale var3) {
      return this.getDisplayName(var1, var2, ULocale.forLocale(var3));
   }

   public String getDisplayName(boolean var1, int var2, ULocale var3) {
      if (var2 >= 0 && var2 <= 7) {
         return this._getDisplayName(var2, var1, var3);
      } else {
         throw new IllegalArgumentException("Illegal style: " + var2);
      }
   }

   private String _getDisplayName(int var1, boolean var2, ULocale var3) {
      if (var3 == null) {
         throw new NullPointerException("locale is null");
      } else {
         String var4 = null;
         TimeZoneFormat var5;
         if (var1 != 7 && var1 != 3 && var1 != 2) {
            if (var1 != 5 && var1 != 4) {
               if (!$assertionsDisabled && var1 != 1 && var1 != 0 && var1 != 6) {
                  throw new AssertionError();
               }

               long var11 = System.currentTimeMillis();
               TimeZoneNames var7 = TimeZoneNames.getInstance(var3);
               TimeZoneNames.NameType var13 = null;
               switch(var1) {
               case 0:
               case 6:
                  var13 = var2 ? TimeZoneNames.NameType.SHORT_DAYLIGHT : TimeZoneNames.NameType.SHORT_STANDARD;
                  break;
               case 1:
                  var13 = var2 ? TimeZoneNames.NameType.LONG_DAYLIGHT : TimeZoneNames.NameType.LONG_STANDARD;
               }

               var4 = var7.getDisplayName(ZoneMeta.getCanonicalCLDRID(this), var13, var11);
               if (var4 == null) {
                  TimeZoneFormat var14 = TimeZoneFormat.getInstance(var3);
                  int var10 = var2 && this.useDaylightTime() ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
                  var4 = var1 == 1 ? var14.formatOffsetLocalizedGMT(var10) : var14.formatOffsetShortLocalizedGMT(var10);
               }
            } else {
               var5 = TimeZoneFormat.getInstance(var3);
               int var12 = var2 && this.useDaylightTime() ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
               switch(var1) {
               case 4:
                  var4 = var5.formatOffsetISO8601Basic(var12, false, false, false);
                  break;
               case 5:
                  var4 = var5.formatOffsetLocalizedGMT(var12);
               }
            }
         } else {
            var5 = TimeZoneFormat.getInstance(var3);
            long var6 = System.currentTimeMillis();
            Output var8 = new Output(TimeZoneFormat.TimeType.UNKNOWN);
            switch(var1) {
            case 2:
               var4 = var5.format(TimeZoneFormat.Style.GENERIC_SHORT, this, var6, var8);
               break;
            case 3:
               var4 = var5.format(TimeZoneFormat.Style.GENERIC_LONG, this, var6, var8);
               break;
            case 7:
               var4 = var5.format(TimeZoneFormat.Style.GENERIC_LOCATION, this, var6, var8);
            }

            if (var2 && var8.value == TimeZoneFormat.TimeType.STANDARD || !var2 && var8.value == TimeZoneFormat.TimeType.DAYLIGHT) {
               int var9 = var2 ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
               var4 = var1 == 2 ? var5.formatOffsetShortLocalizedGMT(var9) : var5.formatOffsetLocalizedGMT(var9);
            }
         }

         if (!$assertionsDisabled && var4 == null) {
            throw new AssertionError();
         } else {
            return var4;
         }
      }
   }

   public int getDSTSavings() {
      return this.useDaylightTime() ? 3600000 : 0;
   }

   public abstract boolean useDaylightTime();

   public boolean observesDaylightTime() {
      return this.useDaylightTime() || this.inDaylightTime(new Date());
   }

   public abstract boolean inDaylightTime(Date var1);

   public static TimeZone getTimeZone(String var0) {
      return getTimeZone(var0, TZ_IMPL, false);
   }

   public static TimeZone getFrozenTimeZone(String var0) {
      return getTimeZone(var0, TZ_IMPL, true);
   }

   public static TimeZone getTimeZone(String var0, int var1) {
      return getTimeZone(var0, var1, false);
   }

   private static synchronized TimeZone getTimeZone(String var0, int var1, boolean var2) {
      Object var3;
      if (var1 == 1) {
         var3 = JavaTimeZone.createTimeZone(var0);
         if (var3 != null) {
            return (TimeZone)(var2 ? ((TimeZone)var3).freeze() : var3);
         }
      } else {
         if (var0 == null) {
            throw new NullPointerException();
         }

         var3 = ZoneMeta.getSystemTimeZone(var0);
      }

      if (var3 == null) {
         var3 = ZoneMeta.getCustomTimeZone(var0);
      }

      if (var3 == null) {
         LOGGER.fine("\"" + var0 + "\" is a bogus id so timezone is falling back to Etc/Unknown(GMT).");
         var3 = UNKNOWN_ZONE;
      }

      return (TimeZone)(var2 ? var3 : ((TimeZone)var3).cloneAsThawed());
   }

   public static synchronized void setDefaultTimeZoneType(int var0) {
      if (var0 != 0 && var0 != 1) {
         throw new IllegalArgumentException("Invalid timezone type");
      } else {
         TZ_IMPL = var0;
      }
   }

   public static int getDefaultTimeZoneType() {
      return TZ_IMPL;
   }

   public static Set getAvailableIDs(TimeZone.SystemTimeZoneType var0, String var1, Integer var2) {
      return ZoneMeta.getAvailableIDs(var0, var1, var2);
   }

   public static String[] getAvailableIDs(int var0) {
      Set var1 = getAvailableIDs(TimeZone.SystemTimeZoneType.ANY, (String)null, var0);
      return (String[])var1.toArray(new String[0]);
   }

   public static String[] getAvailableIDs(String var0) {
      Set var1 = getAvailableIDs(TimeZone.SystemTimeZoneType.ANY, var0, (Integer)null);
      return (String[])var1.toArray(new String[0]);
   }

   public static String[] getAvailableIDs() {
      Set var0 = getAvailableIDs(TimeZone.SystemTimeZoneType.ANY, (String)null, (Integer)null);
      return (String[])var0.toArray(new String[0]);
   }

   public static int countEquivalentIDs(String var0) {
      return ZoneMeta.countEquivalentIDs(var0);
   }

   public static String getEquivalentID(String var0, int var1) {
      return ZoneMeta.getEquivalentID(var0, var1);
   }

   public static synchronized TimeZone getDefault() {
      if (defaultZone == null) {
         if (TZ_IMPL == 1) {
            defaultZone = new JavaTimeZone();
         } else {
            java.util.TimeZone var0 = java.util.TimeZone.getDefault();
            defaultZone = getFrozenTimeZone(var0.getID());
         }
      }

      return defaultZone.cloneAsThawed();
   }

   public static synchronized void setDefault(TimeZone var0) {
      defaultZone = var0;
      java.util.TimeZone var1 = null;
      if (defaultZone instanceof JavaTimeZone) {
         var1 = ((JavaTimeZone)defaultZone).unwrap();
      } else if (var0 != null) {
         if (var0 instanceof OlsonTimeZone) {
            String var2 = var0.getID();
            var1 = java.util.TimeZone.getTimeZone(var2);
            if (!var2.equals(var1.getID())) {
               var1 = null;
            }
         }

         if (var1 == null) {
            var1 = TimeZoneAdapter.wrap(var0);
         }
      }

      java.util.TimeZone.setDefault(var1);
   }

   public boolean hasSameRules(TimeZone var1) {
      return var1 != null && this.getRawOffset() == var1.getRawOffset() && this.useDaylightTime() == var1.useDaylightTime();
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 != null && this.getClass() == var1.getClass() ? this.ID.equals(((TimeZone)var1).ID) : false;
      }
   }

   public int hashCode() {
      return this.ID.hashCode();
   }

   public static synchronized String getTZDataVersion() {
      if (TZDATA_VERSION == null) {
         UResourceBundle var0 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64");
         TZDATA_VERSION = var0.getString("TZVersion");
      }

      return TZDATA_VERSION;
   }

   public static String getCanonicalID(String var0) {
      return getCanonicalID(var0, (boolean[])null);
   }

   public static String getCanonicalID(String var0, boolean[] var1) {
      String var2 = null;
      boolean var3 = false;
      if (var0 != null && var0.length() != 0) {
         if (var0.equals("Etc/Unknown")) {
            var2 = "Etc/Unknown";
            var3 = false;
         } else {
            var2 = ZoneMeta.getCanonicalCLDRID(var0);
            if (var2 != null) {
               var3 = true;
            } else {
               var2 = ZoneMeta.getCustomID(var0);
            }
         }
      }

      if (var1 != null) {
         var1[0] = var3;
      }

      return var2;
   }

   public static String getRegion(String var0) {
      String var1 = null;
      if (!var0.equals("Etc/Unknown")) {
         var1 = ZoneMeta.getRegion(var0);
      }

      if (var1 == null) {
         throw new IllegalArgumentException("Unknown system zone id: " + var0);
      } else {
         return var1;
      }
   }

   public boolean isFrozen() {
      return false;
   }

   public TimeZone freeze() {
      throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
   }

   public TimeZone cloneAsThawed() {
      try {
         TimeZone var1 = (TimeZone)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static {
      String var0 = ICUConfig.get("com.ibm.icu.util.TimeZone.DefaultTimeZoneType", "ICU");
      if (var0.equalsIgnoreCase("JDK")) {
         TZ_IMPL = 1;
      }

   }

   public static enum SystemTimeZoneType {
      ANY,
      CANONICAL,
      CANONICAL_LOCATION;

      private static final TimeZone.SystemTimeZoneType[] $VALUES = new TimeZone.SystemTimeZoneType[]{ANY, CANONICAL, CANONICAL_LOCATION};
   }
}
