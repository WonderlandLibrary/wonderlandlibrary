package com.ibm.icu.impl.duration.impl;

import java.util.ArrayList;

public class DataRecord {
   byte pl;
   String[][] pluralNames;
   byte[] genders;
   String[] singularNames;
   String[] halfNames;
   String[] numberNames;
   String[] mediumNames;
   String[] shortNames;
   String[] measures;
   String[] rqdSuffixes;
   String[] optSuffixes;
   String[] halves;
   byte[] halfPlacements;
   byte[] halfSupport;
   String fifteenMinutes;
   String fiveMinutes;
   boolean requiresDigitSeparator;
   String digitPrefix;
   String countSep;
   String shortUnitSep;
   String[] unitSep;
   boolean[] unitSepRequiresDP;
   boolean[] requiresSkipMarker;
   byte numberSystem;
   char zero;
   char decimalSep;
   boolean omitSingularCount;
   boolean omitDualCount;
   byte zeroHandling;
   byte decimalHandling;
   byte fractionHandling;
   String skippedUnitMarker;
   boolean allowZero;
   boolean weeksAloneOnly;
   byte useMilliseconds;
   DataRecord.ScopeData[] scopeData;

   public static DataRecord read(String var0, RecordReader var1) {
      if (!var1.open("DataRecord")) {
         throw new InternalError("did not find DataRecord while reading " + var0);
      } else {
         DataRecord var2 = new DataRecord();
         var2.pl = var1.namedIndex("pl", DataRecord.EPluralization.names);
         var2.pluralNames = var1.stringTable("pluralName");
         var2.genders = var1.namedIndexArray("gender", DataRecord.EGender.names);
         var2.singularNames = var1.stringArray("singularName");
         var2.halfNames = var1.stringArray("halfName");
         var2.numberNames = var1.stringArray("numberName");
         var2.mediumNames = var1.stringArray("mediumName");
         var2.shortNames = var1.stringArray("shortName");
         var2.measures = var1.stringArray("measure");
         var2.rqdSuffixes = var1.stringArray("rqdSuffix");
         var2.optSuffixes = var1.stringArray("optSuffix");
         var2.halves = var1.stringArray("halves");
         var2.halfPlacements = var1.namedIndexArray("halfPlacement", DataRecord.EHalfPlacement.names);
         var2.halfSupport = var1.namedIndexArray("halfSupport", DataRecord.EHalfSupport.names);
         var2.fifteenMinutes = var1.string("fifteenMinutes");
         var2.fiveMinutes = var1.string("fiveMinutes");
         var2.requiresDigitSeparator = var1.bool("requiresDigitSeparator");
         var2.digitPrefix = var1.string("digitPrefix");
         var2.countSep = var1.string("countSep");
         var2.shortUnitSep = var1.string("shortUnitSep");
         var2.unitSep = var1.stringArray("unitSep");
         var2.unitSepRequiresDP = var1.boolArray("unitSepRequiresDP");
         var2.requiresSkipMarker = var1.boolArray("requiresSkipMarker");
         var2.numberSystem = var1.namedIndex("numberSystem", DataRecord.ENumberSystem.names);
         var2.zero = var1.character("zero");
         var2.decimalSep = var1.character("decimalSep");
         var2.omitSingularCount = var1.bool("omitSingularCount");
         var2.omitDualCount = var1.bool("omitDualCount");
         var2.zeroHandling = var1.namedIndex("zeroHandling", DataRecord.EZeroHandling.names);
         var2.decimalHandling = var1.namedIndex("decimalHandling", DataRecord.EDecimalHandling.names);
         var2.fractionHandling = var1.namedIndex("fractionHandling", DataRecord.EFractionHandling.names);
         var2.skippedUnitMarker = var1.string("skippedUnitMarker");
         var2.allowZero = var1.bool("allowZero");
         var2.weeksAloneOnly = var1.bool("weeksAloneOnly");
         var2.useMilliseconds = var1.namedIndex("useMilliseconds", DataRecord.EMilliSupport.names);
         if (var1.open("ScopeDataList")) {
            ArrayList var3 = new ArrayList();

            DataRecord.ScopeData var4;
            while(null != (var4 = DataRecord.ScopeData.read(var1))) {
               var3.add(var4);
            }

            if (var1.close()) {
               var2.scopeData = (DataRecord.ScopeData[])var3.toArray(new DataRecord.ScopeData[var3.size()]);
            }
         }

         if (var1.close()) {
            return var2;
         } else {
            throw new InternalError("null data read while reading " + var0);
         }
      }
   }

   public void write(RecordWriter var1) {
      var1.open("DataRecord");
      var1.namedIndex("pl", DataRecord.EPluralization.names, this.pl);
      var1.stringTable("pluralName", this.pluralNames);
      var1.namedIndexArray("gender", DataRecord.EGender.names, this.genders);
      var1.stringArray("singularName", this.singularNames);
      var1.stringArray("halfName", this.halfNames);
      var1.stringArray("numberName", this.numberNames);
      var1.stringArray("mediumName", this.mediumNames);
      var1.stringArray("shortName", this.shortNames);
      var1.stringArray("measure", this.measures);
      var1.stringArray("rqdSuffix", this.rqdSuffixes);
      var1.stringArray("optSuffix", this.optSuffixes);
      var1.stringArray("halves", this.halves);
      var1.namedIndexArray("halfPlacement", DataRecord.EHalfPlacement.names, this.halfPlacements);
      var1.namedIndexArray("halfSupport", DataRecord.EHalfSupport.names, this.halfSupport);
      var1.string("fifteenMinutes", this.fifteenMinutes);
      var1.string("fiveMinutes", this.fiveMinutes);
      var1.bool("requiresDigitSeparator", this.requiresDigitSeparator);
      var1.string("digitPrefix", this.digitPrefix);
      var1.string("countSep", this.countSep);
      var1.string("shortUnitSep", this.shortUnitSep);
      var1.stringArray("unitSep", this.unitSep);
      var1.boolArray("unitSepRequiresDP", this.unitSepRequiresDP);
      var1.boolArray("requiresSkipMarker", this.requiresSkipMarker);
      var1.namedIndex("numberSystem", DataRecord.ENumberSystem.names, this.numberSystem);
      var1.character("zero", this.zero);
      var1.character("decimalSep", this.decimalSep);
      var1.bool("omitSingularCount", this.omitSingularCount);
      var1.bool("omitDualCount", this.omitDualCount);
      var1.namedIndex("zeroHandling", DataRecord.EZeroHandling.names, this.zeroHandling);
      var1.namedIndex("decimalHandling", DataRecord.EDecimalHandling.names, this.decimalHandling);
      var1.namedIndex("fractionHandling", DataRecord.EFractionHandling.names, this.fractionHandling);
      var1.string("skippedUnitMarker", this.skippedUnitMarker);
      var1.bool("allowZero", this.allowZero);
      var1.bool("weeksAloneOnly", this.weeksAloneOnly);
      var1.namedIndex("useMilliseconds", DataRecord.EMilliSupport.names, this.useMilliseconds);
      if (this.scopeData != null) {
         var1.open("ScopeDataList");

         for(int var2 = 0; var2 < this.scopeData.length; ++var2) {
            this.scopeData[var2].write(var1);
         }

         var1.close();
      }

      var1.close();
   }

   public interface EGender {
      byte M = 0;
      byte F = 1;
      byte N = 2;
      String[] names = new String[]{"M", "F", "N"};
   }

   public interface ESeparatorVariant {
      byte NONE = 0;
      byte SHORT = 1;
      byte FULL = 2;
      String[] names = new String[]{"NONE", "SHORT", "FULL"};
   }

   public interface EMilliSupport {
      byte YES = 0;
      byte NO = 1;
      byte WITH_SECONDS = 2;
      String[] names = new String[]{"YES", "NO", "WITH_SECONDS"};
   }

   public interface EHalfSupport {
      byte YES = 0;
      byte NO = 1;
      byte ONE_PLUS = 2;
      String[] names = new String[]{"YES", "NO", "ONE_PLUS"};
   }

   public interface EFractionHandling {
      byte FPLURAL = 0;
      byte FSINGULAR_PLURAL = 1;
      byte FSINGULAR_PLURAL_ANDAHALF = 2;
      byte FPAUCAL = 3;
      String[] names = new String[]{"FPLURAL", "FSINGULAR_PLURAL", "FSINGULAR_PLURAL_ANDAHALF", "FPAUCAL"};
   }

   public interface EDecimalHandling {
      byte DPLURAL = 0;
      byte DSINGULAR = 1;
      byte DSINGULAR_SUBONE = 2;
      byte DPAUCAL = 3;
      String[] names = new String[]{"DPLURAL", "DSINGULAR", "DSINGULAR_SUBONE", "DPAUCAL"};
   }

   public interface EZeroHandling {
      byte ZPLURAL = 0;
      byte ZSINGULAR = 1;
      String[] names = new String[]{"ZPLURAL", "ZSINGULAR"};
   }

   public interface ENumberSystem {
      byte DEFAULT = 0;
      byte CHINESE_TRADITIONAL = 1;
      byte CHINESE_SIMPLIFIED = 2;
      byte KOREAN = 3;
      String[] names = new String[]{"DEFAULT", "CHINESE_TRADITIONAL", "CHINESE_SIMPLIFIED", "KOREAN"};
   }

   public interface EHalfPlacement {
      byte PREFIX = 0;
      byte AFTER_FIRST = 1;
      byte LAST = 2;
      String[] names = new String[]{"PREFIX", "AFTER_FIRST", "LAST"};
   }

   public interface EPluralization {
      byte NONE = 0;
      byte PLURAL = 1;
      byte DUAL = 2;
      byte PAUCAL = 3;
      byte HEBREW = 4;
      byte ARABIC = 5;
      String[] names = new String[]{"NONE", "PLURAL", "DUAL", "PAUCAL", "HEBREW", "ARABIC"};
   }

   public interface ECountVariant {
      byte INTEGER = 0;
      byte INTEGER_CUSTOM = 1;
      byte HALF_FRACTION = 2;
      byte DECIMAL1 = 3;
      byte DECIMAL2 = 4;
      byte DECIMAL3 = 5;
      String[] names = new String[]{"INTEGER", "INTEGER_CUSTOM", "HALF_FRACTION", "DECIMAL1", "DECIMAL2", "DECIMAL3"};
   }

   public interface EUnitVariant {
      byte PLURALIZED = 0;
      byte MEDIUM = 1;
      byte SHORT = 2;
      String[] names = new String[]{"PLURALIZED", "MEDIUM", "SHORT"};
   }

   public interface ETimeDirection {
      byte NODIRECTION = 0;
      byte PAST = 1;
      byte FUTURE = 2;
      String[] names = new String[]{"NODIRECTION", "PAST", "FUTURE"};
   }

   public interface ETimeLimit {
      byte NOLIMIT = 0;
      byte LT = 1;
      byte MT = 2;
      String[] names = new String[]{"NOLIMIT", "LT", "MT"};
   }

   public static class ScopeData {
      String prefix;
      boolean requiresDigitPrefix;
      String suffix;

      public void write(RecordWriter var1) {
         var1.open("ScopeData");
         var1.string("prefix", this.prefix);
         var1.bool("requiresDigitPrefix", this.requiresDigitPrefix);
         var1.string("suffix", this.suffix);
         var1.close();
      }

      public static DataRecord.ScopeData read(RecordReader var0) {
         if (var0.open("ScopeData")) {
            DataRecord.ScopeData var1 = new DataRecord.ScopeData();
            var1.prefix = var0.string("prefix");
            var1.requiresDigitPrefix = var0.bool("requiresDigitPrefix");
            var1.suffix = var0.string("suffix");
            if (var0.close()) {
               return var1;
            }
         }

         return null;
      }
   }
}
