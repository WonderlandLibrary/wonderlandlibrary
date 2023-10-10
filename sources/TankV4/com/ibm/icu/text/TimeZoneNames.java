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
   private static TimeZoneNames.Cache TZNAMES_CACHE = new TimeZoneNames.Cache();
   private static final TimeZoneNames.Factory TZNAMES_FACTORY;
   private static final String FACTORY_NAME_PROP = "com.ibm.icu.text.TimeZoneNames.Factory.impl";
   private static final String DEFAULT_FACTORY_CLASS = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";

   public static TimeZoneNames getInstance(ULocale var0) {
      String var1 = var0.getBaseName();
      return (TimeZoneNames)TZNAMES_CACHE.getInstance(var1, var0);
   }

   public abstract Set getAvailableMetaZoneIDs();

   public abstract Set getAvailableMetaZoneIDs(String var1);

   public abstract String getMetaZoneID(String var1, long var2);

   public abstract String getReferenceZoneID(String var1, String var2);

   public abstract String getMetaZoneDisplayName(String var1, TimeZoneNames.NameType var2);

   public final String getDisplayName(String var1, TimeZoneNames.NameType var2, long var3) {
      String var5 = this.getTimeZoneDisplayName(var1, var2);
      if (var5 == null) {
         String var6 = this.getMetaZoneID(var1, var3);
         var5 = this.getMetaZoneDisplayName(var6, var2);
      }

      return var5;
   }

   public abstract String getTimeZoneDisplayName(String var1, TimeZoneNames.NameType var2);

   public String getExemplarLocationName(String var1) {
      return TimeZoneNamesImpl.getDefaultExemplarLocationName(var1);
   }

   public Collection find(CharSequence var1, int var2, EnumSet var3) {
      throw new UnsupportedOperationException("The method is not implemented in TimeZoneNames base class.");
   }

   protected TimeZoneNames() {
   }

   static TimeZoneNames.Factory access$100() {
      return TZNAMES_FACTORY;
   }

   static {
      Object var0 = null;
      String var1 = ICUConfig.get("com.ibm.icu.text.TimeZoneNames.Factory.impl", "com.ibm.icu.impl.TimeZoneNamesFactoryImpl");

      while(true) {
         try {
            var0 = (TimeZoneNames.Factory)Class.forName(var1).newInstance();
            break;
         } catch (ClassNotFoundException var3) {
         } catch (IllegalAccessException var4) {
         } catch (InstantiationException var5) {
         }

         if (var1.equals("com.ibm.icu.impl.TimeZoneNamesFactoryImpl")) {
            break;
         }

         var1 = "com.ibm.icu.impl.TimeZoneNamesFactoryImpl";
      }

      if (var0 == null) {
         var0 = new TimeZoneNames.DefaultTimeZoneNames.FactoryImpl();
      }

      TZNAMES_FACTORY = (TimeZoneNames.Factory)var0;
   }

   private static class DefaultTimeZoneNames extends TimeZoneNames {
      private static final long serialVersionUID = -995672072494349071L;
      public static final TimeZoneNames.DefaultTimeZoneNames INSTANCE = new TimeZoneNames.DefaultTimeZoneNames();

      public Set getAvailableMetaZoneIDs() {
         return Collections.emptySet();
      }

      public Set getAvailableMetaZoneIDs(String var1) {
         return Collections.emptySet();
      }

      public String getMetaZoneID(String var1, long var2) {
         return null;
      }

      public String getReferenceZoneID(String var1, String var2) {
         return null;
      }

      public String getMetaZoneDisplayName(String var1, TimeZoneNames.NameType var2) {
         return null;
      }

      public String getTimeZoneDisplayName(String var1, TimeZoneNames.NameType var2) {
         return null;
      }

      public Collection find(CharSequence var1, int var2, EnumSet var3) {
         return Collections.emptyList();
      }

      public static class FactoryImpl extends TimeZoneNames.Factory {
         public TimeZoneNames getTimeZoneNames(ULocale var1) {
            return TimeZoneNames.DefaultTimeZoneNames.INSTANCE;
         }
      }
   }

   private static class Cache extends SoftCache {
      private Cache() {
      }

      protected TimeZoneNames createInstance(String var1, ULocale var2) {
         return TimeZoneNames.access$100().getTimeZoneNames(var2);
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((String)var1, (ULocale)var2);
      }

      Cache(Object var1) {
         this();
      }
   }

   public abstract static class Factory {
      public abstract TimeZoneNames getTimeZoneNames(ULocale var1);
   }

   public static class MatchInfo {
      private TimeZoneNames.NameType _nameType;
      private String _tzID;
      private String _mzID;
      private int _matchLength;

      public MatchInfo(TimeZoneNames.NameType var1, String var2, String var3, int var4) {
         if (var1 == null) {
            throw new IllegalArgumentException("nameType is null");
         } else if (var2 == null && var3 == null) {
            throw new IllegalArgumentException("Either tzID or mzID must be available");
         } else if (var4 <= 0) {
            throw new IllegalArgumentException("matchLength must be positive value");
         } else {
            this._nameType = var1;
            this._tzID = var2;
            this._mzID = var3;
            this._matchLength = var4;
         }
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

   public static enum NameType {
      LONG_GENERIC,
      LONG_STANDARD,
      LONG_DAYLIGHT,
      SHORT_GENERIC,
      SHORT_STANDARD,
      SHORT_DAYLIGHT,
      EXEMPLAR_LOCATION;

      private static final TimeZoneNames.NameType[] $VALUES = new TimeZoneNames.NameType[]{LONG_GENERIC, LONG_STANDARD, LONG_DAYLIGHT, SHORT_GENERIC, SHORT_STANDARD, SHORT_DAYLIGHT, EXEMPLAR_LOCATION};
   }
}
