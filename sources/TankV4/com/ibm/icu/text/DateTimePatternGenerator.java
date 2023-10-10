package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternTokenizer;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DateTimePatternGenerator implements Freezable, Cloneable {
   private static final boolean DEBUG = false;
   public static final int ERA = 0;
   public static final int YEAR = 1;
   public static final int QUARTER = 2;
   public static final int MONTH = 3;
   public static final int WEEK_OF_YEAR = 4;
   public static final int WEEK_OF_MONTH = 5;
   public static final int WEEKDAY = 6;
   public static final int DAY = 7;
   public static final int DAY_OF_YEAR = 8;
   public static final int DAY_OF_WEEK_IN_MONTH = 9;
   public static final int DAYPERIOD = 10;
   public static final int HOUR = 11;
   public static final int MINUTE = 12;
   public static final int SECOND = 13;
   public static final int FRACTIONAL_SECOND = 14;
   public static final int ZONE = 15;
   public static final int TYPE_LIMIT = 16;
   public static final int MATCH_NO_OPTIONS = 0;
   public static final int MATCH_HOUR_FIELD_LENGTH = 2048;
   /** @deprecated */
   public static final int MATCH_MINUTE_FIELD_LENGTH = 4096;
   /** @deprecated */
   public static final int MATCH_SECOND_FIELD_LENGTH = 8192;
   public static final int MATCH_ALL_FIELDS_LENGTH = 65535;
   private TreeMap skeleton2pattern = new TreeMap();
   private TreeMap basePattern_pattern = new TreeMap();
   private String decimal = "?";
   private String dateTimeFormat = "{1} {0}";
   private String[] appendItemFormats = new String[16];
   private String[] appendItemNames = new String[16];
   private char defaultHourFormatChar;
   private boolean frozen;
   private transient DateTimePatternGenerator.DateTimeMatcher current;
   private transient DateTimePatternGenerator.FormatParser fp;
   private transient DateTimePatternGenerator.DistanceInfo _distanceInfo;
   private static final int FRACTIONAL_MASK = 16384;
   private static final int SECOND_AND_FRACTIONAL_MASK = 24576;
   private static ICUCache DTPNG_CACHE = new SimpleCache();
   private static final String[] CLDR_FIELD_APPEND = new String[]{"Era", "Year", "Quarter", "Month", "Week", "*", "Day-Of-Week", "Day", "*", "*", "*", "Hour", "Minute", "Second", "*", "Timezone"};
   private static final String[] CLDR_FIELD_NAME = new String[]{"era", "year", "*", "month", "week", "*", "weekday", "day", "*", "*", "dayperiod", "hour", "minute", "second", "*", "zone"};
   private static final String[] FIELD_NAME = new String[]{"Era", "Year", "Quarter", "Month", "Week_in_Year", "Week_in_Month", "Weekday", "Day", "Day_Of_Year", "Day_of_Week_in_Month", "Dayperiod", "Hour", "Minute", "Second", "Fractional_Second", "Zone"};
   private static final String[] CANONICAL_ITEMS = new String[]{"G", "y", "Q", "M", "w", "W", "E", "d", "D", "F", "H", "m", "s", "S", "v"};
   private static final Set CANONICAL_SET;
   private Set cldrAvailableFormatKeys;
   private static final int DATE_MASK = 1023;
   private static final int TIME_MASK = 64512;
   private static final int DELTA = 16;
   private static final int NUMERIC = 256;
   private static final int NONE = 0;
   private static final int NARROW = -257;
   private static final int SHORT = -258;
   private static final int LONG = -259;
   private static final int EXTRA_FIELD = 65536;
   private static final int MISSING_FIELD = 4096;
   private static final int[][] types;

   public static DateTimePatternGenerator getEmptyInstance() {
      return new DateTimePatternGenerator();
   }

   protected DateTimePatternGenerator() {
      for(int var1 = 0; var1 < 16; ++var1) {
         this.appendItemFormats[var1] = "{0} ├{2}: {1}┤";
         this.appendItemNames[var1] = "F" + var1;
      }

      this.defaultHourFormatChar = 'H';
      this.frozen = false;
      this.current = new DateTimePatternGenerator.DateTimeMatcher();
      this.fp = new DateTimePatternGenerator.FormatParser();
      this._distanceInfo = new DateTimePatternGenerator.DistanceInfo();
      this.complete();
      this.cldrAvailableFormatKeys = new HashSet(20);
   }

   public static DateTimePatternGenerator getInstance() {
      return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static DateTimePatternGenerator getInstance(ULocale var0) {
      return getFrozenInstance(var0).cloneAsThawed();
   }

   /** @deprecated */
   public static DateTimePatternGenerator getFrozenInstance(ULocale var0) {
      String var1 = var0.toString();
      DateTimePatternGenerator var2 = (DateTimePatternGenerator)DTPNG_CACHE.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = new DateTimePatternGenerator();
         DateTimePatternGenerator.PatternInfo var3 = new DateTimePatternGenerator.PatternInfo();
         String var4 = null;

         int var9;
         for(int var5 = 0; var5 <= 3; ++var5) {
            SimpleDateFormat var6 = (SimpleDateFormat)DateFormat.getDateInstance(var5, var0);
            var2.addPattern(var6.toPattern(), false, var3);
            var6 = (SimpleDateFormat)DateFormat.getTimeInstance(var5, var0);
            var2.addPattern(var6.toPattern(), false, var3);
            if (var5 == 3) {
               var4 = var6.toPattern();
               DateTimePatternGenerator.FormatParser var7 = new DateTimePatternGenerator.FormatParser();
               var7.set(var4);
               List var8 = var7.getItems();

               for(var9 = 0; var9 < var8.size(); ++var9) {
                  Object var10 = var8.get(var9);
                  if (var10 instanceof DateTimePatternGenerator.VariableField) {
                     DateTimePatternGenerator.VariableField var11 = (DateTimePatternGenerator.VariableField)var10;
                     if (var11.getType() == 11) {
                        var2.defaultHourFormatChar = var11.toString().charAt(0);
                        break;
                     }
                  }
               }
            }
         }

         ICUResourceBundle var16 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
         String var17 = var0.getKeywordValue("calendar");
         if (var17 == null) {
            String[] var18 = Calendar.getKeywordValuesForLocale("calendar", var0, true);
            var17 = var18[0];
         }

         if (var17 == null) {
            var17 = "gregorian";
         }

         ICUResourceBundle var19;
         ICUResourceBundle var23;
         String var24;
         String var26;
         try {
            var19 = var16.getWithFallback("calendar/" + var17 + "/appendItems");

            for(int var20 = 0; var20 < var19.getSize(); ++var20) {
               var23 = (ICUResourceBundle)var19.get(var20);
               var24 = var19.get(var20).getKey();
               var26 = var23.getString();
               var2.setAppendItemFormat(getAppendFormatNumber(var24), var26);
            }
         } catch (MissingResourceException var15) {
         }

         try {
            var19 = var16.getWithFallback("fields");

            for(int var25 = 0; var25 < 16; ++var25) {
               if (var25 < 0) {
                  ICUResourceBundle var21 = var19.getWithFallback(CLDR_FIELD_NAME[var25]);
                  var23 = var21.getWithFallback("dn");
                  var26 = var23.getString();
                  var2.setAppendItemName(var25, var26);
               }
            }
         } catch (MissingResourceException var14) {
         }

         var19 = null;

         try {
            var19 = var16.getWithFallback("calendar/" + var17 + "/availableFormats");
         } catch (MissingResourceException var13) {
         }

         boolean var22 = true;

         while(var19 != null) {
            for(var9 = 0; var9 < var19.getSize(); ++var9) {
               var24 = var19.get(var9).getKey();
               if (!var2.isAvailableFormatSet(var24)) {
                  var2.setAvailableFormat(var24);
                  var26 = var19.get(var9).getString();
                  var2.addPatternWithSkeleton(var26, var24, var22, var3);
               }
            }

            var23 = (ICUResourceBundle)var19.getParent();
            if (var23 == null) {
               break;
            }

            try {
               var19 = var23.getWithFallback("calendar/" + var17 + "/availableFormats");
            } catch (MissingResourceException var12) {
               var19 = null;
            }

            if (var19 != null && var23.getULocale().getBaseName().equals("root")) {
               var22 = false;
            }
         }

         if (var4 != null) {
            hackTimes(var2, var3, var4);
         }

         var2.setDateTimeFormat(Calendar.getDateTimePattern(Calendar.getInstance(var0), var0, 2));
         DecimalFormatSymbols var27 = new DecimalFormatSymbols(var0);
         var2.setDecimal(String.valueOf(var27.getDecimalSeparator()));
         var2.freeze();
         DTPNG_CACHE.put(var1, var2);
         return var2;
      }
   }

   /** @deprecated */
   public char getDefaultHourFormatChar() {
      return this.defaultHourFormatChar;
   }

   /** @deprecated */
   public void setDefaultHourFormatChar(char var1) {
      this.defaultHourFormatChar = var1;
   }

   private static void hackTimes(DateTimePatternGenerator var0, DateTimePatternGenerator.PatternInfo var1, String var2) {
      var0.fp.set(var2);
      StringBuilder var3 = new StringBuilder();
      boolean var4 = false;

      for(int var5 = 0; var5 < DateTimePatternGenerator.FormatParser.access$000(var0.fp).size(); ++var5) {
         Object var6 = DateTimePatternGenerator.FormatParser.access$000(var0.fp).get(var5);
         if (var6 instanceof String) {
            if (var4) {
               var3.append(var0.fp.quoteLiteral(var6.toString()));
            }
         } else {
            char var7 = var6.toString().charAt(0);
            if (var7 == 'm') {
               var4 = true;
               var3.append(var6);
            } else {
               if (var7 == 's') {
                  if (var4) {
                     var3.append(var6);
                     var0.addPattern(var3.toString(), false, var1);
                  }
                  break;
               }

               if (var4 || var7 == 'z' || var7 == 'Z' || var7 == 'v' || var7 == 'V') {
                  break;
               }
            }
         }
      }

      BitSet var11 = new BitSet();
      BitSet var12 = new BitSet();

      for(int var13 = 0; var13 < DateTimePatternGenerator.FormatParser.access$000(var0.fp).size(); ++var13) {
         Object var8 = DateTimePatternGenerator.FormatParser.access$000(var0.fp).get(var13);
         if (var8 instanceof DateTimePatternGenerator.VariableField) {
            var11.set(var13);
            char var9 = var8.toString().charAt(0);
            if (var9 == 's' || var9 == 'S') {
               var12.set(var13);

               for(int var10 = var13 - 1; var10 >= 0 && !var11.get(var10); ++var10) {
                  var12.set(var13);
               }
            }
         }
      }

      String var14 = getFilteredPattern(var0.fp, var12);
      var0.addPattern(var14, false, var1);
   }

   private static String getFilteredPattern(DateTimePatternGenerator.FormatParser var0, BitSet var1) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < DateTimePatternGenerator.FormatParser.access$000(var0).size(); ++var3) {
         if (!var1.get(var3)) {
            Object var4 = DateTimePatternGenerator.FormatParser.access$000(var0).get(var3);
            if (var4 instanceof String) {
               var2.append(var0.quoteLiteral(var4.toString()));
            } else {
               var2.append(var4.toString());
            }
         }
      }

      return var2.toString();
   }

   /** @deprecated */
   public static int getAppendFormatNumber(String var0) {
      for(int var1 = 0; var1 < CLDR_FIELD_APPEND.length; ++var1) {
         if (CLDR_FIELD_APPEND[var1].equals(var0)) {
            return var1;
         }
      }

      return -1;
   }

   public String getBestPattern(String var1) {
      return this.getBestPattern(var1, (DateTimePatternGenerator.DateTimeMatcher)null, 0);
   }

   public String getBestPattern(String var1, int var2) {
      return this.getBestPattern(var1, (DateTimePatternGenerator.DateTimeMatcher)null, var2);
   }

   private String getBestPattern(String var1, DateTimePatternGenerator.DateTimeMatcher var2, int var3) {
      var1 = var1.replaceAll("j", String.valueOf(this.defaultHourFormatChar));
      synchronized(this){}
      this.current.set(var1, this.fp, false);
      DateTimePatternGenerator.PatternWithMatcher var7 = this.getBestRaw(this.current, -1, this._distanceInfo, var2);
      if (this._distanceInfo.missingFieldMask == 0 && this._distanceInfo.extraFieldMask == 0) {
         return this.adjustFieldTypes(var7, this.current, false, var3);
      } else {
         int var8 = this.current.getFieldMask();
         String var4 = this.getBestAppending(this.current, var8 & 1023, this._distanceInfo, var2, var3);
         String var5 = this.getBestAppending(this.current, var8 & 'ﰀ', this._distanceInfo, var2, var3);
         if (var4 == null) {
            return var5 == null ? "" : var5;
         } else {
            return var5 == null ? var4 : MessageFormat.format(this.getDateTimeFormat(), var5, var4);
         }
      }
   }

   public DateTimePatternGenerator addPattern(String var1, boolean var2, DateTimePatternGenerator.PatternInfo var3) {
      return this.addPatternWithSkeleton(var1, (String)null, var2, var3);
   }

   /** @deprecated */
   public DateTimePatternGenerator addPatternWithSkeleton(String var1, String var2, boolean var3, DateTimePatternGenerator.PatternInfo var4) {
      this.checkFrozen();
      DateTimePatternGenerator.DateTimeMatcher var5;
      if (var2 == null) {
         var5 = (new DateTimePatternGenerator.DateTimeMatcher()).set(var1, this.fp, false);
      } else {
         var5 = (new DateTimePatternGenerator.DateTimeMatcher()).set(var2, this.fp, false);
      }

      String var6 = var5.getBasePattern();
      DateTimePatternGenerator.PatternWithSkeletonFlag var7 = (DateTimePatternGenerator.PatternWithSkeletonFlag)this.basePattern_pattern.get(var6);
      if (var7 != null && (!var7.skeletonWasSpecified || var2 != null && !var3)) {
         var4.status = 1;
         var4.conflictingPattern = var7.pattern;
         if (!var3) {
            return this;
         }
      }

      DateTimePatternGenerator.PatternWithSkeletonFlag var8 = (DateTimePatternGenerator.PatternWithSkeletonFlag)this.skeleton2pattern.get(var5);
      if (var8 != null) {
         var4.status = 2;
         var4.conflictingPattern = var8.pattern;
         if (!var3 || var2 != null && var8.skeletonWasSpecified) {
            return this;
         }
      }

      var4.status = 0;
      var4.conflictingPattern = "";
      DateTimePatternGenerator.PatternWithSkeletonFlag var9 = new DateTimePatternGenerator.PatternWithSkeletonFlag(var1, var2 != null);
      this.skeleton2pattern.put(var5, var9);
      this.basePattern_pattern.put(var6, var9);
      return this;
   }

   public String getSkeleton(String var1) {
      synchronized(this){}
      this.current.set(var1, this.fp, false);
      return this.current.toString();
   }

   /** @deprecated */
   public String getSkeletonAllowingDuplicates(String var1) {
      synchronized(this){}
      this.current.set(var1, this.fp, true);
      return this.current.toString();
   }

   /** @deprecated */
   public String getCanonicalSkeletonAllowingDuplicates(String var1) {
      synchronized(this){}
      this.current.set(var1, this.fp, true);
      return this.current.toCanonicalString();
   }

   public String getBaseSkeleton(String var1) {
      synchronized(this){}
      this.current.set(var1, this.fp, false);
      return this.current.getBasePattern();
   }

   public Map getSkeletons(Map var1) {
      if (var1 == null) {
         var1 = new LinkedHashMap();
      }

      Iterator var2 = this.skeleton2pattern.keySet().iterator();

      while(var2.hasNext()) {
         DateTimePatternGenerator.DateTimeMatcher var3 = (DateTimePatternGenerator.DateTimeMatcher)var2.next();
         DateTimePatternGenerator.PatternWithSkeletonFlag var4 = (DateTimePatternGenerator.PatternWithSkeletonFlag)this.skeleton2pattern.get(var3);
         String var5 = var4.pattern;
         if (!CANONICAL_SET.contains(var5)) {
            ((Map)var1).put(var3.toString(), var5);
         }
      }

      return (Map)var1;
   }

   public Set getBaseSkeletons(Set var1) {
      if (var1 == null) {
         var1 = new HashSet();
      }

      ((Set)var1).addAll(this.basePattern_pattern.keySet());
      return (Set)var1;
   }

   public String replaceFieldTypes(String var1, String var2) {
      return this.replaceFieldTypes(var1, var2, 0);
   }

   public String replaceFieldTypes(String var1, String var2, int var3) {
      synchronized(this){}
      DateTimePatternGenerator.PatternWithMatcher var5 = new DateTimePatternGenerator.PatternWithMatcher(var1, (DateTimePatternGenerator.DateTimeMatcher)null);
      return this.adjustFieldTypes(var5, this.current.set(var2, this.fp, false), false, var3);
   }

   public void setDateTimeFormat(String var1) {
      this.checkFrozen();
      this.dateTimeFormat = var1;
   }

   public String getDateTimeFormat() {
      return this.dateTimeFormat;
   }

   public void setDecimal(String var1) {
      this.checkFrozen();
      this.decimal = var1;
   }

   public String getDecimal() {
      return this.decimal;
   }

   /** @deprecated */
   public Collection getRedundants(Collection var1) {
      synchronized(this){}
      if (var1 == null) {
         var1 = new LinkedHashSet();
      }

      Iterator var3 = this.skeleton2pattern.keySet().iterator();

      while(var3.hasNext()) {
         DateTimePatternGenerator.DateTimeMatcher var4 = (DateTimePatternGenerator.DateTimeMatcher)var3.next();
         DateTimePatternGenerator.PatternWithSkeletonFlag var5 = (DateTimePatternGenerator.PatternWithSkeletonFlag)this.skeleton2pattern.get(var4);
         String var6 = var5.pattern;
         if (!CANONICAL_SET.contains(var6)) {
            String var7 = this.getBestPattern(var4.toString(), var4, 0);
            if (var7.equals(var6)) {
               ((Collection)var1).add(var6);
            }
         }
      }

      return (Collection)var1;
   }

   public void setAppendItemFormat(int var1, String var2) {
      this.checkFrozen();
      this.appendItemFormats[var1] = var2;
   }

   public String getAppendItemFormat(int var1) {
      return this.appendItemFormats[var1];
   }

   public void setAppendItemName(int var1, String var2) {
      this.checkFrozen();
      this.appendItemNames[var1] = var2;
   }

   public String getAppendItemName(int var1) {
      return this.appendItemNames[var1];
   }

   /** @deprecated */
   public static boolean isSingleField(String var0) {
      char var1 = var0.charAt(0);

      for(int var2 = 1; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) != var1) {
            return false;
         }
      }

      return true;
   }

   private void setAvailableFormat(String var1) {
      this.checkFrozen();
      this.cldrAvailableFormatKeys.add(var1);
   }

   private boolean isAvailableFormatSet(String var1) {
      return this.cldrAvailableFormatKeys.contains(var1);
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   public DateTimePatternGenerator freeze() {
      this.frozen = true;
      return this;
   }

   public DateTimePatternGenerator cloneAsThawed() {
      DateTimePatternGenerator var1 = (DateTimePatternGenerator)((DateTimePatternGenerator)this.clone());
      this.frozen = false;
      return var1;
   }

   public Object clone() {
      try {
         DateTimePatternGenerator var1 = (DateTimePatternGenerator)((DateTimePatternGenerator)super.clone());
         var1.skeleton2pattern = (TreeMap)this.skeleton2pattern.clone();
         var1.basePattern_pattern = (TreeMap)this.basePattern_pattern.clone();
         var1.appendItemFormats = (String[])this.appendItemFormats.clone();
         var1.appendItemNames = (String[])this.appendItemNames.clone();
         var1.current = new DateTimePatternGenerator.DateTimeMatcher();
         var1.fp = new DateTimePatternGenerator.FormatParser();
         var1._distanceInfo = new DateTimePatternGenerator.DistanceInfo();
         var1.frozen = false;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalArgumentException("Internal Error");
      }
   }

   /** @deprecated */
   public boolean skeletonsAreSimilar(String var1, String var2) {
      if (var1.equals(var2)) {
         return true;
      } else {
         TreeSet var3 = this.getSet(var1);
         TreeSet var4 = this.getSet(var2);
         if (var3.size() != var4.size()) {
            return false;
         } else {
            Iterator var5 = var4.iterator();
            Iterator var6 = var3.iterator();

            int var8;
            int var10;
            do {
               if (!var6.hasNext()) {
                  return true;
               }

               String var7 = (String)var6.next();
               var8 = getCanonicalIndex(var7, false);
               String var9 = (String)var5.next();
               var10 = getCanonicalIndex(var9, false);
            } while(types[var8][1] == types[var10][1]);

            return false;
         }
      }
   }

   private TreeSet getSet(String var1) {
      List var2 = this.fp.set(var1).getItems();
      TreeSet var3 = new TreeSet();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         String var6 = var5.toString();
         if (!var6.startsWith("G") && !var6.startsWith("a")) {
            var3.add(var6);
         }
      }

      return var3;
   }

   private void checkFrozen() {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      }
   }

   private String getBestAppending(DateTimePatternGenerator.DateTimeMatcher var1, int var2, DateTimePatternGenerator.DistanceInfo var3, DateTimePatternGenerator.DateTimeMatcher var4, int var5) {
      String var6 = null;
      if (var2 != 0) {
         DateTimePatternGenerator.PatternWithMatcher var7 = this.getBestRaw(var1, var2, var3, var4);
         var6 = this.adjustFieldTypes(var7, var1, false, var5);

         while(true) {
            while(var3.missingFieldMask != 0) {
               if ((var3.missingFieldMask & 24576) == 16384 && (var2 & 24576) == 24576) {
                  var7.pattern = var6;
                  var6 = this.adjustFieldTypes(var7, var1, true, var5);
                  var3.missingFieldMask &= -16385;
               } else {
                  int var8 = var3.missingFieldMask;
                  DateTimePatternGenerator.PatternWithMatcher var9 = this.getBestRaw(var1, var3.missingFieldMask, var3, var4);
                  String var10 = this.adjustFieldTypes(var9, var1, false, var5);
                  int var11 = var8 & ~var3.missingFieldMask;
                  int var12 = this.getTopBitNumber(var11);
                  var6 = MessageFormat.format(this.getAppendFormat(var12), var6, var10, this.getAppendName(var12));
               }
            }

            return var6;
         }
      } else {
         return var6;
      }
   }

   private String getAppendName(int var1) {
      return "'" + this.appendItemNames[var1] + "'";
   }

   private String getAppendFormat(int var1) {
      return this.appendItemFormats[var1];
   }

   private int getTopBitNumber(int var1) {
      int var2;
      for(var2 = 0; var1 != 0; ++var2) {
         var1 >>>= 1;
      }

      return var2 - 1;
   }

   private void complete() {
      DateTimePatternGenerator.PatternInfo var1 = new DateTimePatternGenerator.PatternInfo();

      for(int var2 = 0; var2 < CANONICAL_ITEMS.length; ++var2) {
         this.addPattern(String.valueOf(CANONICAL_ITEMS[var2]), false, var1);
      }

   }

   private DateTimePatternGenerator.PatternWithMatcher getBestRaw(DateTimePatternGenerator.DateTimeMatcher var1, int var2, DateTimePatternGenerator.DistanceInfo var3, DateTimePatternGenerator.DateTimeMatcher var4) {
      int var5 = Integer.MAX_VALUE;
      DateTimePatternGenerator.PatternWithMatcher var6 = new DateTimePatternGenerator.PatternWithMatcher("", (DateTimePatternGenerator.DateTimeMatcher)null);
      DateTimePatternGenerator.DistanceInfo var7 = new DateTimePatternGenerator.DistanceInfo();
      Iterator var8 = this.skeleton2pattern.keySet().iterator();

      while(var8.hasNext()) {
         DateTimePatternGenerator.DateTimeMatcher var9 = (DateTimePatternGenerator.DateTimeMatcher)var8.next();
         if (!var9.equals(var4)) {
            int var10 = var1.getDistance(var9, var2, var7);
            if (var10 < var5) {
               var5 = var10;
               DateTimePatternGenerator.PatternWithSkeletonFlag var11 = (DateTimePatternGenerator.PatternWithSkeletonFlag)this.skeleton2pattern.get(var9);
               var6.pattern = var11.pattern;
               if (var11.skeletonWasSpecified) {
                  var6.matcherWithSkeleton = var9;
               } else {
                  var6.matcherWithSkeleton = null;
               }

               var3.setTo(var7);
               if (var10 == 0) {
                  break;
               }
            }
         }
      }

      return var6;
   }

   private String adjustFieldTypes(DateTimePatternGenerator.PatternWithMatcher var1, DateTimePatternGenerator.DateTimeMatcher var2, boolean var3, int var4) {
      this.fp.set(var1.pattern);
      StringBuilder var5 = new StringBuilder();
      Iterator var6 = this.fp.getItems().iterator();

      while(true) {
         while(var6.hasNext()) {
            Object var7 = var6.next();
            if (var7 instanceof String) {
               var5.append(this.fp.quoteLiteral((String)var7));
            } else {
               DateTimePatternGenerator.VariableField var8 = (DateTimePatternGenerator.VariableField)var7;
               StringBuilder var9 = new StringBuilder(var8.toString());
               int var10 = var8.getType();
               String var11;
               if (var3 && var10 == 13) {
                  var11 = DateTimePatternGenerator.DateTimeMatcher.access$600(var2)[14];
                  var9.append(this.decimal);
                  var9.append(var11);
               } else if (DateTimePatternGenerator.DateTimeMatcher.access$700(var2)[var10] != 0) {
                  var11 = DateTimePatternGenerator.DateTimeMatcher.access$600(var2)[var10];
                  int var12 = var11.length();
                  if (var11.charAt(0) == 'E' && var12 < 3) {
                     var12 = 3;
                  }

                  int var13 = var12;
                  DateTimePatternGenerator.DateTimeMatcher var14 = var1.matcherWithSkeleton;
                  int var16;
                  if ((var10 != 11 || (var4 & 2048) != 0) && (var10 != 12 || (var4 & 4096) != 0) && (var10 != 13 || (var4 & 8192) != 0)) {
                     if (var14 != null) {
                        String var15 = var14.origStringForField(var10);
                        var16 = var15.length();
                        boolean var17 = var8.isNumeric();
                        boolean var18 = var14.fieldIsNumeric(var10);
                        if (var16 == var12 || var17 && !var18 || var18 && !var17) {
                           var13 = var9.length();
                        }
                     }
                  } else {
                     var13 = var9.length();
                  }

                  char var19 = var10 != 11 && var10 != 3 && var10 != 6 && var10 != 1 ? var11.charAt(0) : var9.charAt(0);
                  var9 = new StringBuilder();

                  for(var16 = var13; var16 > 0; --var16) {
                     var9.append(var19);
                  }
               }

               var5.append(var9);
            }
         }

         return var5.toString();
      }
   }

   /** @deprecated */
   public String getFields(String var1) {
      this.fp.set(var1);
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = this.fp.getItems().iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         if (var4 instanceof String) {
            var2.append(this.fp.quoteLiteral((String)var4));
         } else {
            var2.append("{" + getName(var4.toString()) + "}");
         }
      }

      return var2.toString();
   }

   private static String showMask(int var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < 16; ++var2) {
         if ((var0 & 1 << var2) != 0) {
            if (var1.length() != 0) {
               var1.append(" | ");
            }

            var1.append(FIELD_NAME[var2]);
            var1.append(" ");
         }
      }

      return var1.toString();
   }

   private static String getName(String var0) {
      int var1 = getCanonicalIndex(var0, true);
      String var2 = FIELD_NAME[types[var1][1]];
      int var3 = types[var1][2];
      boolean var4 = var3 < 0;
      if (var4) {
         var3 = -var3;
      }

      if (var3 < 0) {
         var2 = var2 + ":S";
      } else {
         var2 = var2 + ":N";
      }

      return var2;
   }

   private static int getCanonicalIndex(String var0, boolean var1) {
      int var2 = var0.length();
      if (var2 == 0) {
         return -1;
      } else {
         char var3 = var0.charAt(0);

         int var4;
         for(var4 = 1; var4 < var2; ++var4) {
            if (var0.charAt(var4) != var3) {
               return -1;
            }
         }

         var4 = -1;

         for(int var5 = 0; var5 < types.length; ++var5) {
            int[] var6 = types[var5];
            if (var6[0] == var3) {
               var4 = var5;
               if (var6[3] <= var2 && var6[var6.length - 1] >= var2) {
                  return var5;
               }
            }
         }

         return var1 ? -1 : var4;
      }
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static int access$300(String var0, boolean var1) {
      return getCanonicalIndex(var0, var1);
   }

   static int[][] access$400() {
      return types;
   }

   static String[] access$500() {
      return CANONICAL_ITEMS;
   }

   static String access$900(int var0) {
      return showMask(var0);
   }

   static {
      CANONICAL_SET = new HashSet(Arrays.asList(CANONICAL_ITEMS));
      types = new int[][]{{71, 0, -258, 1, 3}, {71, 0, -259, 4}, {121, 1, 256, 1, 20}, {89, 1, 272, 1, 20}, {117, 1, 288, 1, 20}, {85, 1, -258, 1, 3}, {85, 1, -259, 4}, {85, 1, -257, 5}, {81, 2, 256, 1, 2}, {81, 2, -258, 3}, {81, 2, -259, 4}, {113, 2, 272, 1, 2}, {113, 2, -242, 3}, {113, 2, -243, 4}, {77, 3, 256, 1, 2}, {77, 3, -258, 3}, {77, 3, -259, 4}, {77, 3, -257, 5}, {76, 3, 272, 1, 2}, {76, 3, -274, 3}, {76, 3, -275, 4}, {76, 3, -273, 5}, {108, 3, 272, 1, 1}, {119, 4, 256, 1, 2}, {87, 5, 272, 1}, {69, 6, -258, 1, 3}, {69, 6, -259, 4}, {69, 6, -257, 5}, {99, 6, 288, 1, 2}, {99, 6, -290, 3}, {99, 6, -291, 4}, {99, 6, -289, 5}, {101, 6, 272, 1, 2}, {101, 6, -274, 3}, {101, 6, -275, 4}, {101, 6, -273, 5}, {100, 7, 256, 1, 2}, {68, 8, 272, 1, 3}, {70, 9, 288, 1}, {103, 7, 304, 1, 20}, {97, 10, -258, 1}, {72, 11, 416, 1, 2}, {107, 11, 432, 1, 2}, {104, 11, 256, 1, 2}, {75, 11, 272, 1, 2}, {109, 12, 256, 1, 2}, {115, 13, 256, 1, 2}, {83, 14, 272, 1, 1000}, {65, 13, 288, 1, 1000}, {118, 15, -290, 1}, {118, 15, -291, 4}, {122, 15, -258, 1, 3}, {122, 15, -259, 4}, {90, 15, -274, 1, 3}, {90, 15, -275, 4}, {86, 15, -274, 1, 3}, {86, 15, -275, 4}};
   }

   private static class DistanceInfo {
      int missingFieldMask;
      int extraFieldMask;

      private DistanceInfo() {
      }

      void clear() {
         this.missingFieldMask = this.extraFieldMask = 0;
      }

      void setTo(DateTimePatternGenerator.DistanceInfo var1) {
         this.missingFieldMask = var1.missingFieldMask;
         this.extraFieldMask = var1.extraFieldMask;
      }

      void addMissing(int var1) {
         this.missingFieldMask |= 1 << var1;
      }

      void addExtra(int var1) {
         this.extraFieldMask |= 1 << var1;
      }

      public String toString() {
         return "missingFieldMask: " + DateTimePatternGenerator.access$900(this.missingFieldMask) + ", extraFieldMask: " + DateTimePatternGenerator.access$900(this.extraFieldMask);
      }

      DistanceInfo(Object var1) {
         this();
      }
   }

   private static class DateTimeMatcher implements Comparable {
      private int[] type;
      private String[] original;
      private String[] baseOriginal;

      private DateTimeMatcher() {
         this.type = new int[16];
         this.original = new String[16];
         this.baseOriginal = new String[16];
      }

      public String origStringForField(int var1) {
         return this.original[var1];
      }

      public boolean fieldIsNumeric(int var1) {
         return this.type[var1] > 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < 16; ++var2) {
            if (this.original[var2].length() != 0) {
               var1.append(this.original[var2]);
            }
         }

         return var1.toString();
      }

      public String toCanonicalString() {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < 16; ++var2) {
            if (this.original[var2].length() != 0) {
               for(int var3 = 0; var3 < DateTimePatternGenerator.access$400().length; ++var3) {
                  int[] var4 = DateTimePatternGenerator.access$400()[var3];
                  if (var4[1] == var2) {
                     char var5 = this.original[var2].charAt(0);
                     char var6 = var5 != 'h' && var5 != 'K' ? (char)var4[0] : 104;
                     var1.append(Utility.repeat(String.valueOf(var6), this.original[var2].length()));
                     break;
                  }
               }
            }
         }

         return var1.toString();
      }

      String getBasePattern() {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < 16; ++var2) {
            if (this.baseOriginal[var2].length() != 0) {
               var1.append(this.baseOriginal[var2]);
            }
         }

         return var1.toString();
      }

      DateTimePatternGenerator.DateTimeMatcher set(String var1, DateTimePatternGenerator.FormatParser var2, boolean var3) {
         for(int var4 = 0; var4 < 16; ++var4) {
            this.type[var4] = 0;
            this.original[var4] = "";
            this.baseOriginal[var4] = "";
         }

         var2.set(var1);
         Iterator var14 = var2.getItems().iterator();

         while(var14.hasNext()) {
            Object var5 = var14.next();
            if (var5 instanceof DateTimePatternGenerator.VariableField) {
               DateTimePatternGenerator.VariableField var6 = (DateTimePatternGenerator.VariableField)var5;
               String var7 = var6.toString();
               if (var7.charAt(0) != 'a') {
                  int var8 = DateTimePatternGenerator.VariableField.access$800(var6);
                  int[] var9 = DateTimePatternGenerator.access$400()[var8];
                  int var10 = var9[1];
                  if (this.original[var10].length() != 0) {
                     if (!var3) {
                        throw new IllegalArgumentException("Conflicting fields:\t" + this.original[var10] + ", " + var7 + "\t in " + var1);
                     }
                  } else {
                     this.original[var10] = var7;
                     char var11 = (char)var9[0];
                     int var12 = var9[3];
                     if ("GEzvQ".indexOf(var11) >= 0) {
                        var12 = 1;
                     }

                     this.baseOriginal[var10] = Utility.repeat(String.valueOf(var11), var12);
                     int var13 = var9[2];
                     if (var13 > 0) {
                        var13 += var7.length();
                     }

                     this.type[var10] = var13;
                  }
               }
            }
         }

         return this;
      }

      int getFieldMask() {
         int var1 = 0;

         for(int var2 = 0; var2 < this.type.length; ++var2) {
            if (this.type[var2] != 0) {
               var1 |= 1 << var2;
            }
         }

         return var1;
      }

      void extractFrom(DateTimePatternGenerator.DateTimeMatcher var1, int var2) {
         for(int var3 = 0; var3 < this.type.length; ++var3) {
            if ((var2 & 1 << var3) != 0) {
               this.type[var3] = var1.type[var3];
               this.original[var3] = var1.original[var3];
            } else {
               this.type[var3] = 0;
               this.original[var3] = "";
            }
         }

      }

      int getDistance(DateTimePatternGenerator.DateTimeMatcher var1, int var2, DateTimePatternGenerator.DistanceInfo var3) {
         int var4 = 0;
         var3.clear();

         for(int var5 = 0; var5 < this.type.length; ++var5) {
            int var6 = (var2 & 1 << var5) == 0 ? 0 : this.type[var5];
            int var7 = var1.type[var5];
            if (var6 != var7) {
               if (var6 == 0) {
                  var4 += 65536;
                  var3.addExtra(var5);
               } else if (var7 == 0) {
                  var4 += 4096;
                  var3.addMissing(var5);
               } else {
                  var4 += Math.abs(var6 - var7);
               }
            }
         }

         return var4;
      }

      public int compareTo(DateTimePatternGenerator.DateTimeMatcher var1) {
         for(int var2 = 0; var2 < this.original.length; ++var2) {
            int var3 = this.original[var2].compareTo(var1.original[var2]);
            if (var3 != 0) {
               return -var3;
            }
         }

         return 0;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof DateTimePatternGenerator.DateTimeMatcher)) {
            return false;
         } else {
            DateTimePatternGenerator.DateTimeMatcher var2 = (DateTimePatternGenerator.DateTimeMatcher)var1;

            for(int var3 = 0; var3 < this.original.length; ++var3) {
               if (!this.original[var3].equals(var2.original[var3])) {
                  return false;
               }
            }

            return true;
         }
      }

      public int hashCode() {
         int var1 = 0;

         for(int var2 = 0; var2 < this.original.length; ++var2) {
            var1 ^= this.original[var2].hashCode();
         }

         return var1;
      }

      public int compareTo(Object var1) {
         return this.compareTo((DateTimePatternGenerator.DateTimeMatcher)var1);
      }

      DateTimeMatcher(Object var1) {
         this();
      }

      static String[] access$600(DateTimePatternGenerator.DateTimeMatcher var0) {
         return var0.original;
      }

      static int[] access$700(DateTimePatternGenerator.DateTimeMatcher var0) {
         return var0.type;
      }
   }

   private static class PatternWithSkeletonFlag {
      public String pattern;
      public boolean skeletonWasSpecified;

      public PatternWithSkeletonFlag(String var1, boolean var2) {
         this.pattern = var1;
         this.skeletonWasSpecified = var2;
      }

      public String toString() {
         return this.pattern + "," + this.skeletonWasSpecified;
      }
   }

   private static class PatternWithMatcher {
      public String pattern;
      public DateTimePatternGenerator.DateTimeMatcher matcherWithSkeleton;

      public PatternWithMatcher(String var1, DateTimePatternGenerator.DateTimeMatcher var2) {
         this.pattern = var1;
         this.matcherWithSkeleton = var2;
      }
   }

   /** @deprecated */
   public static class FormatParser {
      private transient PatternTokenizer tokenizer = (new PatternTokenizer()).setSyntaxCharacters(new UnicodeSet("[a-zA-Z]")).setExtraQuotingCharacters(new UnicodeSet("[[[:script=Latn:][:script=Cyrl:]]&[[:L:][:M:]]]")).setUsingQuote(true);
      private List items = new ArrayList();

      /** @deprecated */
      public final DateTimePatternGenerator.FormatParser set(String var1) {
         return this.set(var1, false);
      }

      /** @deprecated */
      public DateTimePatternGenerator.FormatParser set(String var1, boolean var2) {
         this.items.clear();
         if (var1.length() == 0) {
            return this;
         } else {
            this.tokenizer.setPattern(var1);
            StringBuffer var3 = new StringBuffer();
            StringBuffer var4 = new StringBuffer();

            while(true) {
               var3.setLength(0);
               int var5 = this.tokenizer.next(var3);
               if (var5 == 0) {
                  this.addVariable(var4, false);
                  return this;
               }

               if (var5 == 1) {
                  if (var4.length() != 0 && var3.charAt(0) != var4.charAt(0)) {
                     this.addVariable(var4, false);
                  }

                  var4.append(var3);
               } else {
                  this.addVariable(var4, false);
                  this.items.add(var3.toString());
               }
            }
         }
      }

      private void addVariable(StringBuffer var1, boolean var2) {
         if (var1.length() != 0) {
            this.items.add(new DateTimePatternGenerator.VariableField(var1.toString(), var2));
            var1.setLength(0);
         }

      }

      /** @deprecated */
      public List getItems() {
         return this.items;
      }

      /** @deprecated */
      public String toString() {
         return this.toString(0, this.items.size());
      }

      /** @deprecated */
      public String toString(int var1, int var2) {
         StringBuilder var3 = new StringBuilder();

         for(int var4 = var1; var4 < var2; ++var4) {
            Object var5 = this.items.get(var4);
            if (var5 instanceof String) {
               String var6 = (String)var5;
               var3.append(this.tokenizer.quoteLiteral(var6));
            } else {
               var3.append(this.items.get(var4).toString());
            }
         }

         return var3.toString();
      }

      /** @deprecated */
      public boolean hasDateAndTimeFields() {
         int var1 = 0;
         Iterator var2 = this.items.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof DateTimePatternGenerator.VariableField) {
               int var4 = ((DateTimePatternGenerator.VariableField)var3).getType();
               var1 |= 1 << var4;
            }
         }

         boolean var5 = (var1 & 1023) != 0;
         boolean var6 = (var1 & 'ﰀ') != 0;
         return var5 && var6;
      }

      /** @deprecated */
      public Object quoteLiteral(String var1) {
         return this.tokenizer.quoteLiteral(var1);
      }

      static List access$000(DateTimePatternGenerator.FormatParser var0) {
         return var0.items;
      }
   }

   /** @deprecated */
   public static class VariableField {
      private final String string;
      private final int canonicalIndex;

      /** @deprecated */
      public VariableField(String var1) {
         this(var1, false);
      }

      /** @deprecated */
      public VariableField(String var1, boolean var2) {
         this.canonicalIndex = DateTimePatternGenerator.access$300(var1, var2);
         if (this.canonicalIndex < 0) {
            throw new IllegalArgumentException("Illegal datetime field:\t" + var1);
         } else {
            this.string = var1;
         }
      }

      /** @deprecated */
      public int getType() {
         return DateTimePatternGenerator.access$400()[this.canonicalIndex][1];
      }

      /** @deprecated */
      public static String getCanonicalCode(int var0) {
         try {
            return DateTimePatternGenerator.access$500()[var0];
         } catch (Exception var2) {
            return String.valueOf(var0);
         }
      }

      /** @deprecated */
      public boolean isNumeric() {
         return DateTimePatternGenerator.access$400()[this.canonicalIndex][2] > 0;
      }

      private int getCanonicalIndex() {
         return this.canonicalIndex;
      }

      /** @deprecated */
      public String toString() {
         return this.string;
      }

      static int access$800(DateTimePatternGenerator.VariableField var0) {
         return var0.getCanonicalIndex();
      }
   }

   public static final class PatternInfo {
      public static final int OK = 0;
      public static final int BASE_CONFLICT = 1;
      public static final int CONFLICT = 2;
      public int status;
      public String conflictingPattern;
   }
}
