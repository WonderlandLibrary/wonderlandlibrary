package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.TimeZoneGenericNames;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream.PutField;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Set;

public class TimeZoneFormat extends UFormat implements Freezable, Serializable {
   private static final long serialVersionUID = 2281246852693575022L;
   private static final int ISO_Z_STYLE_FLAG = 128;
   private static final int ISO_LOCAL_STYLE_FLAG = 256;
   private ULocale _locale;
   private TimeZoneNames _tznames;
   private String _gmtPattern;
   private String[] _gmtOffsetPatterns;
   private String[] _gmtOffsetDigits;
   private String _gmtZeroFormat;
   private boolean _parseAllStyles;
   private transient volatile TimeZoneGenericNames _gnames;
   private transient String _gmtPatternPrefix;
   private transient String _gmtPatternSuffix;
   private transient Object[][] _gmtOffsetPatternItems;
   private transient boolean _abuttingOffsetHoursAndMinutes;
   private transient String _region;
   private transient boolean _frozen;
   private static final String TZID_GMT = "Etc/GMT";
   private static final String[] ALT_GMT_STRINGS = new String[]{"GMT", "UTC", "UT"};
   private static final String DEFAULT_GMT_PATTERN = "GMT{0}";
   private static final String DEFAULT_GMT_ZERO = "GMT";
   private static final String[] DEFAULT_GMT_DIGITS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
   private static final char DEFAULT_GMT_OFFSET_SEP = ':';
   private static final String ASCII_DIGITS = "0123456789";
   private static final String ISO8601_UTC = "Z";
   private static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
   private static final String UNKNOWN_SHORT_ZONE_ID = "unk";
   private static final String UNKNOWN_LOCATION = "Unknown";
   private static final TimeZoneFormat.GMTOffsetPatternType[] PARSE_GMT_OFFSET_TYPES;
   private static final int MILLIS_PER_HOUR = 3600000;
   private static final int MILLIS_PER_MINUTE = 60000;
   private static final int MILLIS_PER_SECOND = 1000;
   private static final int MAX_OFFSET = 86400000;
   private static final int MAX_OFFSET_HOUR = 23;
   private static final int MAX_OFFSET_MINUTE = 59;
   private static final int MAX_OFFSET_SECOND = 59;
   private static final int UNKNOWN_OFFSET = Integer.MAX_VALUE;
   private static TimeZoneFormat.TimeZoneFormatCache _tzfCache;
   private static final EnumSet ALL_SIMPLE_NAME_TYPES;
   private static final EnumSet ALL_GENERIC_NAME_TYPES;
   private static volatile TextTrieMap ZONE_ID_TRIE;
   private static volatile TextTrieMap SHORT_ZONE_ID_TRIE;
   private static final ObjectStreamField[] serialPersistentFields;
   static final boolean $assertionsDisabled = !TimeZoneFormat.class.desiredAssertionStatus();

   protected TimeZoneFormat(ULocale var1) {
      this._locale = var1;
      this._tznames = TimeZoneNames.getInstance(var1);
      String var2 = null;
      String var3 = null;
      this._gmtZeroFormat = "GMT";

      try {
         ICUResourceBundle var4 = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", var1);

         try {
            var2 = var4.getStringWithFallback("zoneStrings/gmtFormat");
         } catch (MissingResourceException var11) {
         }

         try {
            var3 = var4.getStringWithFallback("zoneStrings/hourFormat");
         } catch (MissingResourceException var10) {
         }

         try {
            this._gmtZeroFormat = var4.getStringWithFallback("zoneStrings/gmtZeroFormat");
         } catch (MissingResourceException var9) {
         }
      } catch (MissingResourceException var12) {
      }

      if (var2 == null) {
         var2 = "GMT{0}";
      }

      this.initGMTPattern(var2);
      String[] var13 = new String[TimeZoneFormat.GMTOffsetPatternType.values().length];
      if (var3 != null) {
         String[] var5 = var3.split(";", 2);
         var13[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(var5[0]);
         var13[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HM.ordinal()] = var5[0];
         var13[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HMS.ordinal()] = expandOffsetPattern(var5[0]);
         var13[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(var5[1]);
         var13[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HM.ordinal()] = var5[1];
         var13[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HMS.ordinal()] = expandOffsetPattern(var5[1]);
      } else {
         TimeZoneFormat.GMTOffsetPatternType[] var14 = TimeZoneFormat.GMTOffsetPatternType.values();
         int var6 = var14.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            TimeZoneFormat.GMTOffsetPatternType var8 = var14[var7];
            var13[var8.ordinal()] = TimeZoneFormat.GMTOffsetPatternType.access$100(var8);
         }
      }

      this.initGMTOffsetPatterns(var13);
      this._gmtOffsetDigits = DEFAULT_GMT_DIGITS;
      NumberingSystem var15 = NumberingSystem.getInstance(var1);
      if (!var15.isAlgorithmic()) {
         this._gmtOffsetDigits = toCodePoints(var15.getDescription());
      }

   }

   public static TimeZoneFormat getInstance(ULocale var0) {
      if (var0 == null) {
         throw new NullPointerException("locale is null");
      } else {
         return (TimeZoneFormat)_tzfCache.getInstance(var0, var0);
      }
   }

   public TimeZoneNames getTimeZoneNames() {
      return this._tznames;
   }

   private TimeZoneGenericNames getTimeZoneGenericNames() {
      if (this._gnames == null) {
         synchronized(this){}
         if (this._gnames == null) {
            this._gnames = TimeZoneGenericNames.getInstance(this._locale);
         }
      }

      return this._gnames;
   }

   public TimeZoneFormat setTimeZoneNames(TimeZoneNames var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else {
         this._tznames = var1;
         this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
         return this;
      }
   }

   public String getGMTPattern() {
      return this._gmtPattern;
   }

   public TimeZoneFormat setGMTPattern(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else {
         this.initGMTPattern(var1);
         return this;
      }
   }

   public String getGMTOffsetPattern(TimeZoneFormat.GMTOffsetPatternType var1) {
      return this._gmtOffsetPatterns[var1.ordinal()];
   }

   public TimeZoneFormat setGMTOffsetPattern(TimeZoneFormat.GMTOffsetPatternType var1, String var2) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else if (var2 == null) {
         throw new NullPointerException("Null GMT offset pattern");
      } else {
         Object[] var3 = parseOffsetPattern(var2, TimeZoneFormat.GMTOffsetPatternType.access$200(var1));
         this._gmtOffsetPatterns[var1.ordinal()] = var2;
         this._gmtOffsetPatternItems[var1.ordinal()] = var3;
         this.checkAbuttingHoursAndMinutes();
         return this;
      }
   }

   public String getGMTOffsetDigits() {
      StringBuilder var1 = new StringBuilder(this._gmtOffsetDigits.length);
      String[] var2 = this._gmtOffsetDigits;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.append(var5);
      }

      return var1.toString();
   }

   public TimeZoneFormat setGMTOffsetDigits(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else if (var1 == null) {
         throw new NullPointerException("Null GMT offset digits");
      } else {
         String[] var2 = toCodePoints(var1);
         if (var2.length != 10) {
            throw new IllegalArgumentException("Length of digits must be 10");
         } else {
            this._gmtOffsetDigits = var2;
            return this;
         }
      }
   }

   public String getGMTZeroFormat() {
      return this._gmtZeroFormat;
   }

   public TimeZoneFormat setGMTZeroFormat(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      } else if (var1 == null) {
         throw new NullPointerException("Null GMT zero format");
      } else if (var1.length() == 0) {
         throw new IllegalArgumentException("Empty GMT zero format");
      } else {
         this._gmtZeroFormat = var1;
         return this;
      }
   }

   public TimeZoneFormat setDefaultParseOptions(EnumSet var1) {
      this._parseAllStyles = var1.contains(TimeZoneFormat.ParseOption.ALL_STYLES);
      return this;
   }

   public EnumSet getDefaultParseOptions() {
      return this._parseAllStyles ? EnumSet.of(TimeZoneFormat.ParseOption.ALL_STYLES) : EnumSet.noneOf(TimeZoneFormat.ParseOption.class);
   }

   public final String formatOffsetISO8601Basic(int var1, boolean var2, boolean var3, boolean var4) {
      return this.formatOffsetISO8601(var1, true, var2, var3, var4);
   }

   public final String formatOffsetISO8601Extended(int var1, boolean var2, boolean var3, boolean var4) {
      return this.formatOffsetISO8601(var1, false, var2, var3, var4);
   }

   public String formatOffsetLocalizedGMT(int var1) {
      return this.formatOffsetLocalizedGMT(var1, false);
   }

   public String formatOffsetShortLocalizedGMT(int var1) {
      return this.formatOffsetLocalizedGMT(var1, true);
   }

   public final String format(TimeZoneFormat.Style var1, TimeZone var2, long var3) {
      return this.format(var1, var2, var3, (Output)null);
   }

   public String format(TimeZoneFormat.Style var1, TimeZone var2, long var3, Output var5) {
      String var6 = null;
      if (var5 != null) {
         var5.value = TimeZoneFormat.TimeType.UNKNOWN;
      }

      switch(var1) {
      case GENERIC_LOCATION:
         var6 = this.getTimeZoneGenericNames().getGenericLocationName(ZoneMeta.getCanonicalCLDRID(var2));
         break;
      case GENERIC_LONG:
         var6 = this.getTimeZoneGenericNames().getDisplayName(var2, TimeZoneGenericNames.GenericNameType.LONG, var3);
         break;
      case GENERIC_SHORT:
         var6 = this.getTimeZoneGenericNames().getDisplayName(var2, TimeZoneGenericNames.GenericNameType.SHORT, var3);
         break;
      case SPECIFIC_LONG:
         var6 = this.formatSpecific(var2, TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, var3, var5);
         break;
      case SPECIFIC_SHORT:
         var6 = this.formatSpecific(var2, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, var3, var5);
      }

      if (var6 == null) {
         int[] var7 = new int[]{0, 0};
         var2.getOffset(var3, false, var7);
         int var8 = var7[0] + var7[1];
         switch(var1) {
         case GENERIC_LOCATION:
         case GENERIC_LONG:
         case SPECIFIC_LONG:
         case LOCALIZED_GMT:
            var6 = this.formatOffsetLocalizedGMT(var8);
            break;
         case GENERIC_SHORT:
         case SPECIFIC_SHORT:
         case LOCALIZED_GMT_SHORT:
            var6 = this.formatOffsetShortLocalizedGMT(var8);
            break;
         case ISO_BASIC_SHORT:
            var6 = this.formatOffsetISO8601Basic(var8, true, true, true);
            break;
         case ISO_BASIC_LOCAL_SHORT:
            var6 = this.formatOffsetISO8601Basic(var8, false, true, true);
            break;
         case ISO_BASIC_FIXED:
            var6 = this.formatOffsetISO8601Basic(var8, true, false, true);
            break;
         case ISO_BASIC_LOCAL_FIXED:
            var6 = this.formatOffsetISO8601Basic(var8, false, false, true);
            break;
         case ISO_BASIC_FULL:
            var6 = this.formatOffsetISO8601Basic(var8, true, false, false);
            break;
         case ISO_BASIC_LOCAL_FULL:
            var6 = this.formatOffsetISO8601Basic(var8, false, false, false);
            break;
         case ISO_EXTENDED_FIXED:
            var6 = this.formatOffsetISO8601Extended(var8, true, false, true);
            break;
         case ISO_EXTENDED_LOCAL_FIXED:
            var6 = this.formatOffsetISO8601Extended(var8, false, false, true);
            break;
         case ISO_EXTENDED_FULL:
            var6 = this.formatOffsetISO8601Extended(var8, true, false, false);
            break;
         case ISO_EXTENDED_LOCAL_FULL:
            var6 = this.formatOffsetISO8601Extended(var8, false, false, false);
            break;
         case ZONE_ID:
            var6 = var2.getID();
            break;
         case ZONE_ID_SHORT:
            var6 = ZoneMeta.getShortID(var2);
            if (var6 == null) {
               var6 = "unk";
            }
            break;
         case EXEMPLAR_LOCATION:
            var6 = this.formatExemplarLocation(var2);
         }

         if (var5 != null) {
            var5.value = var7[1] != 0 ? TimeZoneFormat.TimeType.DAYLIGHT : TimeZoneFormat.TimeType.STANDARD;
         }
      }

      if (!$assertionsDisabled && var6 == null) {
         throw new AssertionError();
      } else {
         return var6;
      }
   }

   public final int parseOffsetISO8601(String var1, ParsePosition var2) {
      return parseOffsetISO8601(var1, var2, false, (Output)null);
   }

   public int parseOffsetLocalizedGMT(String var1, ParsePosition var2) {
      return this.parseOffsetLocalizedGMT(var1, var2, false, (Output)null);
   }

   public int parseOffsetShortLocalizedGMT(String var1, ParsePosition var2) {
      return this.parseOffsetLocalizedGMT(var1, var2, true, (Output)null);
   }

   public TimeZone parse(TimeZoneFormat.Style var1, String var2, ParsePosition var3, EnumSet var4, Output var5) {
      if (var5 == null) {
         var5 = new Output(TimeZoneFormat.TimeType.UNKNOWN);
      } else {
         var5.value = TimeZoneFormat.TimeType.UNKNOWN;
      }

      int var6 = var3.getIndex();
      int var7 = var2.length();
      boolean var9 = var1 == TimeZoneFormat.Style.SPECIFIC_LONG || var1 == TimeZoneFormat.Style.GENERIC_LONG || var1 == TimeZoneFormat.Style.GENERIC_LOCATION;
      boolean var10 = var1 == TimeZoneFormat.Style.SPECIFIC_SHORT || var1 == TimeZoneFormat.Style.GENERIC_SHORT;
      int var11 = 0;
      ParsePosition var12 = new ParsePosition(var6);
      int var13 = Integer.MAX_VALUE;
      int var14 = -1;
      int var8;
      Output var15;
      if (var9 || var10) {
         var15 = new Output(false);
         var8 = this.parseOffsetLocalizedGMT(var2, var12, var10, var15);
         if (var12.getErrorIndex() == -1) {
            if (var12.getIndex() == var7 || (Boolean)var15.value) {
               var3.setIndex(var12.getIndex());
               return this.getTimeZoneForOffset(var8);
            }

            var13 = var8;
            var14 = var12.getIndex();
         }

         var11 |= TimeZoneFormat.Style.LOCALIZED_GMT.flag | TimeZoneFormat.Style.LOCALIZED_GMT_SHORT.flag;
      }

      Iterator var18;
      TimeZoneNames.MatchInfo var19;
      String var23;
      EnumSet var26;
      switch(var1) {
      case GENERIC_LOCATION:
      case GENERIC_LONG:
      case GENERIC_SHORT:
         var26 = null;
         switch(var1) {
         case GENERIC_LOCATION:
            var26 = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION);
            break;
         case GENERIC_LONG:
            var26 = EnumSet.of(TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.LOCATION);
            break;
         case GENERIC_SHORT:
            var26 = EnumSet.of(TimeZoneGenericNames.GenericNameType.SHORT, TimeZoneGenericNames.GenericNameType.LOCATION);
         }

         TimeZoneGenericNames.GenericMatchInfo var24 = this.getTimeZoneGenericNames().findBestMatch(var2, var6, var26);
         if (var24 != null && var6 + var24.matchLength() > var14) {
            var5.value = var24.timeType();
            var3.setIndex(var6 + var24.matchLength());
            return TimeZone.getTimeZone(var24.tzID());
         }
         break;
      case SPECIFIC_LONG:
      case SPECIFIC_SHORT:
         var15 = null;
         if (var1 == TimeZoneFormat.Style.SPECIFIC_LONG) {
            var26 = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT);
         } else {
            if (!$assertionsDisabled && var1 != TimeZoneFormat.Style.SPECIFIC_SHORT) {
               throw new AssertionError();
            }

            var26 = EnumSet.of(TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT);
         }

         Collection var16 = this._tznames.find(var2, var6, var26);
         if (var16 != null) {
            TimeZoneNames.MatchInfo var17 = null;
            var18 = var16.iterator();

            while(var18.hasNext()) {
               var19 = (TimeZoneNames.MatchInfo)var18.next();
               if (var6 + var19.matchLength() > var14) {
                  var17 = var19;
                  var14 = var6 + var19.matchLength();
               }
            }

            if (var17 != null) {
               var5.value = this.getTimeType(var17.nameType());
               var3.setIndex(var14);
               return TimeZone.getTimeZone(this.getTimeZoneID(var17.tzID(), var17.mzID()));
            }
         }
         break;
      case LOCALIZED_GMT:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var8 = this.parseOffsetLocalizedGMT(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return this.getTimeZoneForOffset(var8);
         }

         var11 |= TimeZoneFormat.Style.LOCALIZED_GMT_SHORT.flag;
         break;
      case LOCALIZED_GMT_SHORT:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var8 = this.parseOffsetShortLocalizedGMT(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return this.getTimeZoneForOffset(var8);
         }

         var11 |= TimeZoneFormat.Style.LOCALIZED_GMT.flag;
         break;
      case ISO_BASIC_SHORT:
      case ISO_BASIC_FIXED:
      case ISO_BASIC_FULL:
      case ISO_EXTENDED_FIXED:
      case ISO_EXTENDED_FULL:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var8 = this.parseOffsetISO8601(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return this.getTimeZoneForOffset(var8);
         }
         break;
      case ISO_BASIC_LOCAL_SHORT:
      case ISO_BASIC_LOCAL_FIXED:
      case ISO_BASIC_LOCAL_FULL:
      case ISO_EXTENDED_LOCAL_FIXED:
      case ISO_EXTENDED_LOCAL_FULL:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var15 = new Output(false);
         var8 = parseOffsetISO8601(var2, var12, false, var15);
         if (var12.getErrorIndex() == -1 && (Boolean)var15.value) {
            var3.setIndex(var12.getIndex());
            return this.getTimeZoneForOffset(var8);
         }
         break;
      case ZONE_ID:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var23 = parseZoneID(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return TimeZone.getTimeZone(var23);
         }
         break;
      case ZONE_ID_SHORT:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var23 = parseShortZoneID(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return TimeZone.getTimeZone(var23);
         }
         break;
      case EXEMPLAR_LOCATION:
         var12.setIndex(var6);
         var12.setErrorIndex(-1);
         var23 = this.parseExemplarLocation(var2, var12);
         if (var12.getErrorIndex() == -1) {
            var3.setIndex(var12.getIndex());
            return TimeZone.getTimeZone(var23);
         }
      }

      var11 |= var1.flag;
      if (var14 > var6) {
         if (!$assertionsDisabled && var13 == Integer.MAX_VALUE) {
            throw new AssertionError();
         } else {
            var3.setIndex(var14);
            return this.getTimeZoneForOffset(var13);
         }
      } else {
         var23 = null;
         TimeZoneFormat.TimeType var25 = TimeZoneFormat.TimeType.UNKNOWN;
         if (!$assertionsDisabled && var14 >= 0) {
            throw new AssertionError();
         } else if (!$assertionsDisabled && var13 != Integer.MAX_VALUE) {
            throw new AssertionError();
         } else {
            Output var27;
            if (var14 < var7 && ((var11 & 128) == 0 || (var11 & 256) == 0)) {
               var12.setIndex(var6);
               var12.setErrorIndex(-1);
               var27 = new Output(false);
               var8 = parseOffsetISO8601(var2, var12, false, var27);
               if (var12.getErrorIndex() == -1) {
                  if (var12.getIndex() == var7 || (Boolean)var27.value) {
                     var3.setIndex(var12.getIndex());
                     return this.getTimeZoneForOffset(var8);
                  }

                  if (var14 < var12.getIndex()) {
                     var13 = var8;
                     var23 = null;
                     var25 = TimeZoneFormat.TimeType.UNKNOWN;
                     var14 = var12.getIndex();
                     if (!$assertionsDisabled && var14 != var6 + 1) {
                        throw new AssertionError();
                     }
                  }
               }
            }

            if (var14 < var7 && (var11 & TimeZoneFormat.Style.LOCALIZED_GMT.flag) == 0) {
               var12.setIndex(var6);
               var12.setErrorIndex(-1);
               var27 = new Output(false);
               var8 = this.parseOffsetLocalizedGMT(var2, var12, false, var27);
               if (var12.getErrorIndex() == -1) {
                  if (var12.getIndex() == var7 || (Boolean)var27.value) {
                     var3.setIndex(var12.getIndex());
                     return this.getTimeZoneForOffset(var8);
                  }

                  if (var14 < var12.getIndex()) {
                     var13 = var8;
                     var23 = null;
                     var25 = TimeZoneFormat.TimeType.UNKNOWN;
                     var14 = var12.getIndex();
                  }
               }
            }

            if (var14 < var7 && (var11 & TimeZoneFormat.Style.LOCALIZED_GMT_SHORT.flag) == 0) {
               var12.setIndex(var6);
               var12.setErrorIndex(-1);
               var27 = new Output(false);
               var8 = this.parseOffsetLocalizedGMT(var2, var12, true, var27);
               if (var12.getErrorIndex() == -1) {
                  if (var12.getIndex() == var7 || (Boolean)var27.value) {
                     var3.setIndex(var12.getIndex());
                     return this.getTimeZoneForOffset(var8);
                  }

                  if (var14 < var12.getIndex()) {
                     var13 = var8;
                     var23 = null;
                     var25 = TimeZoneFormat.TimeType.UNKNOWN;
                     var14 = var12.getIndex();
                  }
               }
            }

            boolean var29 = var4 == null ? this.getDefaultParseOptions().contains(TimeZoneFormat.ParseOption.ALL_STYLES) : var4.contains(TimeZoneFormat.ParseOption.ALL_STYLES);
            if (var29) {
               if (var14 < var7) {
                  Collection var28 = this._tznames.find(var2, var6, ALL_SIMPLE_NAME_TYPES);
                  var19 = null;
                  int var20 = -1;
                  if (var28 != null) {
                     Iterator var21 = var28.iterator();

                     while(var21.hasNext()) {
                        TimeZoneNames.MatchInfo var22 = (TimeZoneNames.MatchInfo)var21.next();
                        if (var6 + var22.matchLength() > var20) {
                           var19 = var22;
                           var20 = var6 + var22.matchLength();
                        }
                     }
                  }

                  if (var14 < var20) {
                     var14 = var20;
                     var23 = this.getTimeZoneID(var19.tzID(), var19.mzID());
                     var25 = this.getTimeType(var19.nameType());
                     var13 = Integer.MAX_VALUE;
                  }
               }

               if (var14 < var7) {
                  TimeZoneGenericNames.GenericMatchInfo var30 = this.getTimeZoneGenericNames().findBestMatch(var2, var6, ALL_GENERIC_NAME_TYPES);
                  if (var30 != null && var14 < var6 + var30.matchLength()) {
                     var14 = var6 + var30.matchLength();
                     var23 = var30.tzID();
                     var25 = var30.timeType();
                     var13 = Integer.MAX_VALUE;
                  }
               }

               String var31;
               if (var14 < var7 && (var11 & TimeZoneFormat.Style.ZONE_ID.flag) == 0) {
                  var12.setIndex(var6);
                  var12.setErrorIndex(-1);
                  var31 = parseZoneID(var2, var12);
                  if (var12.getErrorIndex() == -1 && var14 < var12.getIndex()) {
                     var14 = var12.getIndex();
                     var23 = var31;
                     var25 = TimeZoneFormat.TimeType.UNKNOWN;
                     var13 = Integer.MAX_VALUE;
                  }
               }

               if (var14 < var7 && (var11 & TimeZoneFormat.Style.ZONE_ID_SHORT.flag) == 0) {
                  var12.setIndex(var6);
                  var12.setErrorIndex(-1);
                  var31 = parseShortZoneID(var2, var12);
                  if (var12.getErrorIndex() == -1 && var14 < var12.getIndex()) {
                     var14 = var12.getIndex();
                     var23 = var31;
                     var25 = TimeZoneFormat.TimeType.UNKNOWN;
                     var13 = Integer.MAX_VALUE;
                  }
               }
            }

            if (var14 > var6) {
               var18 = null;
               TimeZone var32;
               if (var23 != null) {
                  var32 = TimeZone.getTimeZone(var23);
               } else {
                  if (!$assertionsDisabled && var13 == Integer.MAX_VALUE) {
                     throw new AssertionError();
                  }

                  var32 = this.getTimeZoneForOffset(var13);
               }

               var5.value = var25;
               var3.setIndex(var14);
               return var32;
            } else {
               var3.setErrorIndex(var6);
               return null;
            }
         }
      }
   }

   public TimeZone parse(TimeZoneFormat.Style var1, String var2, ParsePosition var3, Output var4) {
      return this.parse(var1, var2, var3, (EnumSet)null, var4);
   }

   public final TimeZone parse(String var1, ParsePosition var2) {
      return this.parse(TimeZoneFormat.Style.GENERIC_LOCATION, var1, var2, EnumSet.of(TimeZoneFormat.ParseOption.ALL_STYLES), (Output)null);
   }

   public final TimeZone parse(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      TimeZone var3 = this.parse(var1, var2);
      if (var2.getErrorIndex() >= 0) {
         throw new ParseException("Unparseable time zone: \"" + var1 + "\"", 0);
      } else if (!$assertionsDisabled && var3 == null) {
         throw new AssertionError();
      } else {
         return var3;
      }
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      TimeZone var4 = null;
      long var5 = System.currentTimeMillis();
      if (var1 instanceof TimeZone) {
         var4 = (TimeZone)var1;
      } else {
         if (!(var1 instanceof Calendar)) {
            throw new IllegalArgumentException("Cannot format given Object (" + var1.getClass().getName() + ") as a time zone");
         }

         var4 = ((Calendar)var1).getTimeZone();
         var5 = ((Calendar)var1).getTimeInMillis();
      }

      if (!$assertionsDisabled && var4 == null) {
         throw new AssertionError();
      } else {
         String var7 = this.formatOffsetLocalizedGMT(var4.getOffset(var5));
         var2.append(var7);
         if (var3.getFieldAttribute() == DateFormat.Field.TIME_ZONE || var3.getField() == 17) {
            var3.setBeginIndex(0);
            var3.setEndIndex(var7.length());
         }

         return var2;
      }
   }

   public AttributedCharacterIterator formatToCharacterIterator(Object var1) {
      StringBuffer var2 = new StringBuffer();
      FieldPosition var3 = new FieldPosition(0);
      var2 = this.format(var1, var2, var3);
      AttributedString var4 = new AttributedString(var2.toString());
      var4.addAttribute(DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE);
      return var4.getIterator();
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parse(var1, var2);
   }

   private String formatOffsetLocalizedGMT(int var1, boolean var2) {
      if (var1 == 0) {
         return this._gmtZeroFormat;
      } else {
         StringBuilder var3 = new StringBuilder();
         boolean var4 = true;
         if (var1 < 0) {
            var1 = -var1;
            var4 = false;
         }

         int var5 = var1 / 3600000;
         var1 %= 3600000;
         int var6 = var1 / '\uea60';
         var1 %= 60000;
         int var7 = var1 / 1000;
         if (var5 <= 23 && var6 <= 59 && var7 <= 59) {
            Object[] var8;
            if (var4) {
               if (var7 != 0) {
                  var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HMS.ordinal()];
               } else if (var6 == 0 && var2) {
                  var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_H.ordinal()];
               } else {
                  var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HM.ordinal()];
               }
            } else if (var7 != 0) {
               var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HMS.ordinal()];
            } else if (var6 == 0 && var2) {
               var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_H.ordinal()];
            } else {
               var8 = this._gmtOffsetPatternItems[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HM.ordinal()];
            }

            var3.append(this._gmtPatternPrefix);
            Object[] var9 = var8;
            int var10 = var8.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Object var12 = var9[var11];
               if (var12 instanceof String) {
                  var3.append((String)var12);
               } else if (var12 instanceof TimeZoneFormat.GMTOffsetField) {
                  TimeZoneFormat.GMTOffsetField var13 = (TimeZoneFormat.GMTOffsetField)var12;
                  switch(var13.getType()) {
                  case 'H':
                     this.appendOffsetDigits(var3, var5, var2 ? 1 : 2);
                     break;
                  case 'm':
                     this.appendOffsetDigits(var3, var6, 2);
                     break;
                  case 's':
                     this.appendOffsetDigits(var3, var7, 2);
                  }
               }
            }

            var3.append(this._gmtPatternSuffix);
            return var3.toString();
         } else {
            throw new IllegalArgumentException("Offset out of range :" + var1);
         }
      }
   }

   private String formatOffsetISO8601(int var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      int var6 = var1 < 0 ? -var1 : var1;
      if (var3 && (var6 < 1000 || var5 && var6 < 60000)) {
         return "Z";
      } else {
         TimeZoneFormat.OffsetFields var7 = var4 ? TimeZoneFormat.OffsetFields.H : TimeZoneFormat.OffsetFields.HM;
         TimeZoneFormat.OffsetFields var8 = var5 ? TimeZoneFormat.OffsetFields.HM : TimeZoneFormat.OffsetFields.HMS;
         Character var9 = var2 ? null : ':';
         if (var6 >= 86400000) {
            throw new IllegalArgumentException("Offset out of range :" + var1);
         } else {
            int[] var10 = new int[]{var6 / 3600000, 0, 0};
            var6 %= 3600000;
            var10[1] = var6 / '\uea60';
            var6 %= 60000;
            var10[2] = var6 / 1000;
            if ($assertionsDisabled || var10[0] >= 0 && var10[0] <= 23) {
               if (!$assertionsDisabled && (var10[1] < 0 || var10[1] > 59)) {
                  throw new AssertionError();
               } else if (!$assertionsDisabled && (var10[2] < 0 || var10[2] > 59)) {
                  throw new AssertionError();
               } else {
                  int var11;
                  for(var11 = var8.ordinal(); var11 > var7.ordinal() && var10[var11] == 0; --var11) {
                  }

                  StringBuilder var12 = new StringBuilder();
                  char var13 = '+';
                  int var14;
                  if (var1 < 0) {
                     for(var14 = 0; var14 <= var11; ++var14) {
                        if (var10[var14] != 0) {
                           var13 = '-';
                           break;
                        }
                     }
                  }

                  var12.append(var13);

                  for(var14 = 0; var14 <= var11; ++var14) {
                     if (var9 != null && var14 != 0) {
                        var12.append(var9);
                     }

                     if (var10[var14] < 10) {
                        var12.append('0');
                     }

                     var12.append(var10[var14]);
                  }

                  return var12.toString();
               }
            } else {
               throw new AssertionError();
            }
         }
      }
   }

   private String formatSpecific(TimeZone var1, TimeZoneNames.NameType var2, TimeZoneNames.NameType var3, long var4, Output var6) {
      if (!$assertionsDisabled && var2 != TimeZoneNames.NameType.LONG_STANDARD && var2 != TimeZoneNames.NameType.SHORT_STANDARD) {
         throw new AssertionError();
      } else if (!$assertionsDisabled && var3 != TimeZoneNames.NameType.LONG_DAYLIGHT && var3 != TimeZoneNames.NameType.SHORT_DAYLIGHT) {
         throw new AssertionError();
      } else {
         boolean var7 = var1.inDaylightTime(new Date(var4));
         String var8 = var7 ? this.getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(var1), var3, var4) : this.getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(var1), var2, var4);
         if (var8 != null && var6 != null) {
            var6.value = var7 ? TimeZoneFormat.TimeType.DAYLIGHT : TimeZoneFormat.TimeType.STANDARD;
         }

         return var8;
      }
   }

   private String formatExemplarLocation(TimeZone var1) {
      String var2 = this.getTimeZoneNames().getExemplarLocationName(ZoneMeta.getCanonicalCLDRID(var1));
      if (var2 == null) {
         var2 = this.getTimeZoneNames().getExemplarLocationName("Etc/Unknown");
         if (var2 == null) {
            var2 = "Unknown";
         }
      }

      return var2;
   }

   private String getTimeZoneID(String var1, String var2) {
      String var3 = var1;
      if (var1 == null) {
         if (!$assertionsDisabled && var2 == null) {
            throw new AssertionError();
         }

         var3 = this._tznames.getReferenceZoneID(var2, this.getTargetRegion());
         if (var3 == null) {
            throw new IllegalArgumentException("Invalid mzID: " + var2);
         }
      }

      return var3;
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

   private TimeZoneFormat.TimeType getTimeType(TimeZoneNames.NameType var1) {
      switch(var1) {
      case LONG_STANDARD:
      case SHORT_STANDARD:
         return TimeZoneFormat.TimeType.STANDARD;
      case LONG_DAYLIGHT:
      case SHORT_DAYLIGHT:
         return TimeZoneFormat.TimeType.DAYLIGHT;
      default:
         return TimeZoneFormat.TimeType.UNKNOWN;
      }
   }

   private void initGMTPattern(String var1) {
      int var2 = var1.indexOf("{0}");
      if (var2 < 0) {
         throw new IllegalArgumentException("Bad localized GMT pattern: " + var1);
      } else {
         this._gmtPattern = var1;
         this._gmtPatternPrefix = unquote(var1.substring(0, var2));
         this._gmtPatternSuffix = unquote(var1.substring(var2 + 3));
      }
   }

   private static String unquote(String var0) {
      if (var0.indexOf(39) < 0) {
         return var0;
      } else {
         boolean var1 = false;
         boolean var2 = false;
         StringBuilder var3 = new StringBuilder();

         for(int var4 = 0; var4 < var0.length(); ++var4) {
            char var5 = var0.charAt(var4);
            if (var5 == '\'') {
               if (var1) {
                  var3.append(var5);
                  var1 = false;
               } else {
                  var1 = true;
               }

               var2 = !var2;
            } else {
               var1 = false;
               var3.append(var5);
            }
         }

         return var3.toString();
      }
   }

   private void initGMTOffsetPatterns(String[] var1) {
      int var2 = TimeZoneFormat.GMTOffsetPatternType.values().length;
      if (var1.length < var2) {
         throw new IllegalArgumentException("Insufficient number of elements in gmtOffsetPatterns");
      } else {
         Object[][] var3 = new Object[var2][];
         TimeZoneFormat.GMTOffsetPatternType[] var4 = TimeZoneFormat.GMTOffsetPatternType.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            TimeZoneFormat.GMTOffsetPatternType var7 = var4[var6];
            int var8 = var7.ordinal();
            Object[] var9 = parseOffsetPattern(var1[var8], TimeZoneFormat.GMTOffsetPatternType.access$200(var7));
            var3[var8] = var9;
         }

         this._gmtOffsetPatterns = new String[var2];
         System.arraycopy(var1, 0, this._gmtOffsetPatterns, 0, var2);
         this._gmtOffsetPatternItems = var3;
         this.checkAbuttingHoursAndMinutes();
      }
   }

   private void checkAbuttingHoursAndMinutes() {
      this._abuttingOffsetHoursAndMinutes = false;
      Object[][] var1 = this._gmtOffsetPatternItems;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object[] var4 = var1[var3];
         boolean var5 = false;
         Object[] var6 = var4;
         int var7 = var4.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Object var9 = var6[var8];
            if (var9 instanceof TimeZoneFormat.GMTOffsetField) {
               TimeZoneFormat.GMTOffsetField var10 = (TimeZoneFormat.GMTOffsetField)var9;
               if (var5) {
                  this._abuttingOffsetHoursAndMinutes = true;
               } else if (var10.getType() == 'H') {
                  var5 = true;
               }
            } else if (var5) {
               break;
            }
         }
      }

   }

   private static Object[] parseOffsetPattern(String var0, String var1) {
      boolean var2 = false;
      boolean var3 = false;
      StringBuilder var4 = new StringBuilder();
      char var5 = 0;
      int var6 = 1;
      boolean var7 = false;
      ArrayList var8 = new ArrayList();
      BitSet var9 = new BitSet(var1.length());

      for(int var10 = 0; var10 < var0.length(); ++var10) {
         char var11 = var0.charAt(var10);
         if (var11 == '\'') {
            if (var2) {
               var4.append('\'');
               var2 = false;
            } else {
               var2 = true;
               if (var5 != 0) {
                  if (!TimeZoneFormat.GMTOffsetField.isValid(var5, var6)) {
                     var7 = true;
                     break;
                  }

                  var8.add(new TimeZoneFormat.GMTOffsetField(var5, var6));
                  var5 = 0;
               }
            }

            var3 = !var3;
         } else {
            var2 = false;
            if (var3) {
               var4.append(var11);
            } else {
               int var12 = var1.indexOf(var11);
               if (var12 >= 0) {
                  if (var11 == var5) {
                     ++var6;
                  } else {
                     if (var5 == 0) {
                        if (var4.length() > 0) {
                           var8.add(var4.toString());
                           var4.setLength(0);
                        }
                     } else {
                        if (!TimeZoneFormat.GMTOffsetField.isValid(var5, var6)) {
                           var7 = true;
                           break;
                        }

                        var8.add(new TimeZoneFormat.GMTOffsetField(var5, var6));
                     }

                     var5 = var11;
                     var6 = 1;
                     var9.set(var12);
                  }
               } else {
                  if (var5 != 0) {
                     if (!TimeZoneFormat.GMTOffsetField.isValid(var5, var6)) {
                        var7 = true;
                        break;
                     }

                     var8.add(new TimeZoneFormat.GMTOffsetField(var5, var6));
                     var5 = 0;
                  }

                  var4.append(var11);
               }
            }
         }
      }

      if (!var7) {
         if (var5 == 0) {
            if (var4.length() > 0) {
               var8.add(var4.toString());
               var4.setLength(0);
            }
         } else if (TimeZoneFormat.GMTOffsetField.isValid(var5, var6)) {
            var8.add(new TimeZoneFormat.GMTOffsetField(var5, var6));
         } else {
            var7 = true;
         }
      }

      if (!var7 && var9.cardinality() == var1.length()) {
         return var8.toArray(new Object[var8.size()]);
      } else {
         throw new IllegalStateException("Bad localized GMT offset pattern: " + var0);
      }
   }

   private static String expandOffsetPattern(String var0) {
      int var1 = var0.indexOf("mm");
      if (var1 < 0) {
         throw new RuntimeException("Bad time zone hour pattern data");
      } else {
         String var2 = ":";
         int var3 = var0.substring(0, var1).lastIndexOf("H");
         if (var3 >= 0) {
            var2 = var0.substring(var3 + 1, var1);
         }

         return var0.substring(0, var1 + 2) + var2 + "ss" + var0.substring(var1 + 2);
      }
   }

   private static String truncateOffsetPattern(String var0) {
      int var1 = var0.indexOf("mm");
      if (var1 < 0) {
         throw new RuntimeException("Bad time zone hour pattern data");
      } else {
         int var2 = var0.substring(0, var1).lastIndexOf("HH");
         if (var2 >= 0) {
            return var0.substring(0, var2 + 2);
         } else {
            int var3 = var0.substring(0, var1).lastIndexOf("H");
            if (var3 >= 0) {
               return var0.substring(0, var3 + 1);
            } else {
               throw new RuntimeException("Bad time zone hour pattern data");
            }
         }
      }
   }

   private void appendOffsetDigits(StringBuilder var1, int var2, int var3) {
      if ($assertionsDisabled || var2 >= 0 && var2 < 60) {
         int var4 = var2 >= 10 ? 2 : 1;

         for(int var5 = 0; var5 < var3 - var4; ++var5) {
            var1.append(this._gmtOffsetDigits[0]);
         }

         if (var4 == 2) {
            var1.append(this._gmtOffsetDigits[var2 / 10]);
         }

         var1.append(this._gmtOffsetDigits[var2 % 10]);
      } else {
         throw new AssertionError();
      }
   }

   private TimeZone getTimeZoneForOffset(int var1) {
      return var1 == 0 ? TimeZone.getTimeZone("Etc/GMT") : ZoneMeta.getCustomTimeZone(var1);
   }

   private int parseOffsetLocalizedGMT(String var1, ParsePosition var2, boolean var3, Output var4) {
      int var5 = var2.getIndex();
      boolean var6 = false;
      int[] var7 = new int[]{0};
      if (var4 != null) {
         var4.value = false;
      }

      int var12 = this.parseOffsetLocalizedGMTPattern(var1, var5, var3, var7);
      if (var7[0] > 0) {
         if (var4 != null) {
            var4.value = true;
         }

         var2.setIndex(var5 + var7[0]);
         return var12;
      } else {
         var12 = this.parseOffsetDefaultLocalizedGMT(var1, var5, var7);
         if (var7[0] > 0) {
            if (var4 != null) {
               var4.value = true;
            }

            var2.setIndex(var5 + var7[0]);
            return var12;
         } else if (var1.regionMatches(true, var5, this._gmtZeroFormat, 0, this._gmtZeroFormat.length())) {
            var2.setIndex(var5 + this._gmtZeroFormat.length());
            return 0;
         } else {
            String[] var8 = ALT_GMT_STRINGS;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               if (var1.regionMatches(true, var5, var11, 0, var11.length())) {
                  var2.setIndex(var5 + var11.length());
                  return 0;
               }
            }

            var2.setErrorIndex(var5);
            return 0;
         }
      }
   }

   private int parseOffsetLocalizedGMTPattern(String var1, int var2, boolean var3, int[] var4) {
      int var5 = var2;
      int var6 = 0;
      boolean var7 = false;
      int var8 = this._gmtPatternPrefix.length();
      if (var8 <= 0 || var1.regionMatches(true, var2, this._gmtPatternPrefix, 0, var8)) {
         var5 = var2 + var8;
         int[] var9 = new int[1];
         var6 = this.parseOffsetFields(var1, var5, false, var9);
         if (var9[0] != 0) {
            var5 += var9[0];
            var8 = this._gmtPatternSuffix.length();
            if (var8 <= 0 || var1.regionMatches(true, var5, this._gmtPatternSuffix, 0, var8)) {
               var5 += var8;
               var7 = true;
            }
         }
      }

      var4[0] = var7 ? var5 - var2 : 0;
      return var6;
   }

   private int parseOffsetFields(String var1, int var2, boolean var3, int[] var4) {
      int var5 = 0;
      int var6 = 0;
      int var7 = 1;
      if (var4 != null && var4.length >= 1) {
         var4[0] = 0;
      }

      int var10 = 0;
      int var9 = 0;
      int var8 = 0;
      int[] var11 = new int[]{0, 0, 0};
      TimeZoneFormat.GMTOffsetPatternType[] var12 = PARSE_GMT_OFFSET_TYPES;
      int var13 = var12.length;

      for(int var14 = 0; var14 < var13; ++var14) {
         TimeZoneFormat.GMTOffsetPatternType var15 = var12[var14];
         Object[] var16 = this._gmtOffsetPatternItems[var15.ordinal()];
         if (!$assertionsDisabled && var16 == null) {
            throw new AssertionError();
         }

         var5 = this.parseOffsetFieldsWithPattern(var1, var2, var16, false, var11);
         if (var5 > 0) {
            var7 = TimeZoneFormat.GMTOffsetPatternType.access$300(var15) ? 1 : -1;
            var8 = var11[0];
            var9 = var11[1];
            var10 = var11[2];
            break;
         }
      }

      if (var5 > 0 && this._abuttingOffsetHoursAndMinutes) {
         int var19 = 0;
         var13 = 1;
         TimeZoneFormat.GMTOffsetPatternType[] var20 = PARSE_GMT_OFFSET_TYPES;
         int var21 = var20.length;

         for(int var22 = 0; var22 < var21; ++var22) {
            TimeZoneFormat.GMTOffsetPatternType var17 = var20[var22];
            Object[] var18 = this._gmtOffsetPatternItems[var17.ordinal()];
            if (!$assertionsDisabled && var18 == null) {
               throw new AssertionError();
            }

            var19 = this.parseOffsetFieldsWithPattern(var1, var2, var18, true, var11);
            if (var19 > 0) {
               var13 = TimeZoneFormat.GMTOffsetPatternType.access$300(var17) ? 1 : -1;
               break;
            }
         }

         if (var19 > var5) {
            var5 = var19;
            var7 = var13;
            var8 = var11[0];
            var9 = var11[1];
            var10 = var11[2];
         }
      }

      if (var4 != null && var4.length >= 1) {
         var4[0] = var5;
      }

      if (var5 > 0) {
         var6 = ((var8 * 60 + var9) * 60 + var10) * 1000 * var7;
      }

      return var6;
   }

   private int parseOffsetFieldsWithPattern(String var1, int var2, Object[] var3, boolean var4, int[] var5) {
      if ($assertionsDisabled || var5 != null && var5.length >= 3) {
         var5[0] = var5[1] = var5[2] = 0;
         boolean var6 = false;
         int var9 = 0;
         int var8 = 0;
         int var7 = 0;
         int var10 = var2;
         int[] var11 = new int[]{0};

         for(int var12 = 0; var12 < var3.length; ++var12) {
            if (var3[var12] instanceof String) {
               String var13 = (String)var3[var12];
               int var14 = var13.length();
               if (!var1.regionMatches(true, var10, var13, 0, var14)) {
                  var6 = true;
                  break;
               }

               var10 += var14;
            } else {
               if (!$assertionsDisabled && !(var3[var12] instanceof TimeZoneFormat.GMTOffsetField)) {
                  throw new AssertionError();
               }

               TimeZoneFormat.GMTOffsetField var16 = (TimeZoneFormat.GMTOffsetField)var3[var12];
               char var17 = var16.getType();
               if (var17 == 'H') {
                  int var15 = var4 ? 1 : 2;
                  var7 = this.parseOffsetFieldWithLocalizedDigits(var1, var10, 1, var15, 0, 23, var11);
               } else if (var17 == 'm') {
                  var8 = this.parseOffsetFieldWithLocalizedDigits(var1, var10, 2, 2, 0, 59, var11);
               } else if (var17 == 's') {
                  var9 = this.parseOffsetFieldWithLocalizedDigits(var1, var10, 2, 2, 0, 59, var11);
               }

               if (var11[0] == 0) {
                  var6 = true;
                  break;
               }

               var10 += var11[0];
            }
         }

         if (var6) {
            return 0;
         } else {
            var5[0] = var7;
            var5[1] = var8;
            var5[2] = var9;
            return var10 - var2;
         }
      } else {
         throw new AssertionError();
      }
   }

   private int parseOffsetDefaultLocalizedGMT(String var1, int var2, int[] var3) {
      int var4 = var2;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      String[] var8 = ALT_GMT_STRINGS;
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String var11 = var8[var10];
         int var12 = var11.length();
         if (var1.regionMatches(true, var4, var11, 0, var12)) {
            var7 = var12;
            break;
         }
      }

      if (var7 != 0) {
         var4 += var7;
         if (var4 + 1 < var1.length()) {
            label45: {
               boolean var14 = true;
               char var16 = var1.charAt(var4);
               byte var15;
               if (var16 == '+') {
                  var15 = 1;
               } else {
                  if (var16 != '-') {
                     break label45;
                  }

                  var15 = -1;
               }

               ++var4;
               int[] var17 = new int[]{0};
               int var18 = this.parseDefaultOffsetFields(var1, var4, ':', var17);
               if (var17[0] == var1.length() - var4) {
                  var5 = var18 * var15;
                  var4 += var17[0];
               } else {
                  int[] var19 = new int[]{0};
                  int var13 = this.parseAbuttingOffsetFields(var1, var4, var19);
                  if (var17[0] > var19[0]) {
                     var5 = var18 * var15;
                     var4 += var17[0];
                  } else {
                     var5 = var13 * var15;
                     var4 += var19[0];
                  }
               }

               var6 = var4 - var2;
            }
         }
      }

      var3[0] = var6;
      return var5;
   }

   private int parseDefaultOffsetFields(String var1, int var2, char var3, int[] var4) {
      int var5 = var1.length();
      int var6 = var2;
      int[] var7 = new int[]{0};
      boolean var8 = false;
      int var9 = 0;
      int var10 = 0;
      int var11 = this.parseOffsetFieldWithLocalizedDigits(var1, var2, 1, 2, 0, 23, var7);
      if (var7[0] != 0) {
         var6 = var2 + var7[0];
         if (var6 + 1 < var5 && var1.charAt(var6) == var3) {
            var9 = this.parseOffsetFieldWithLocalizedDigits(var1, var6 + 1, 2, 2, 0, 59, var7);
            if (var7[0] != 0) {
               var6 += 1 + var7[0];
               if (var6 + 1 < var5 && var1.charAt(var6) == var3) {
                  var10 = this.parseOffsetFieldWithLocalizedDigits(var1, var6 + 1, 2, 2, 0, 59, var7);
                  if (var7[0] != 0) {
                     var6 += 1 + var7[0];
                  }
               }
            }
         }
      }

      if (var6 == var2) {
         var4[0] = 0;
         return 0;
      } else {
         var4[0] = var6 - var2;
         return var11 * 3600000 + var9 * '\uea60' + var10 * 1000;
      }
   }

   private int parseAbuttingOffsetFields(String var1, int var2, int[] var3) {
      boolean var4 = true;
      int[] var5 = new int[6];
      int[] var6 = new int[6];
      int var7 = var2;
      int[] var8 = new int[]{0};
      int var9 = 0;

      int var10;
      for(var10 = 0; var10 < 6; ++var10) {
         var5[var10] = this.parseSingleLocalizedDigit(var1, var7, var8);
         if (var5[var10] < 0) {
            break;
         }

         var7 += var8[0];
         var6[var10] = var7 - var2;
         ++var9;
      }

      if (var9 == 0) {
         var3[0] = 0;
         return 0;
      } else {
         var10 = 0;

         while(true) {
            if (var9 > 0) {
               int var11 = 0;
               int var12 = 0;
               int var13 = 0;
               if (!$assertionsDisabled && (var9 <= 0 || var9 > 6)) {
                  throw new AssertionError();
               }

               switch(var9) {
               case 1:
                  var11 = var5[0];
                  break;
               case 2:
                  var11 = var5[0] * 10 + var5[1];
                  break;
               case 3:
                  var11 = var5[0];
                  var12 = var5[1] * 10 + var5[2];
                  break;
               case 4:
                  var11 = var5[0] * 10 + var5[1];
                  var12 = var5[2] * 10 + var5[3];
                  break;
               case 5:
                  var11 = var5[0];
                  var12 = var5[1] * 10 + var5[2];
                  var13 = var5[3] * 10 + var5[4];
                  break;
               case 6:
                  var11 = var5[0] * 10 + var5[1];
                  var12 = var5[2] * 10 + var5[3];
                  var13 = var5[4] * 10 + var5[5];
               }

               if (var11 > 23 || var12 > 59 || var13 > 59) {
                  --var9;
                  continue;
               }

               var10 = var11 * 3600000 + var12 * '\uea60' + var13 * 1000;
               var3[0] = var6[var9 - 1];
            }

            return var10;
         }
      }
   }

   private int parseOffsetFieldWithLocalizedDigits(String var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      var7[0] = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = var2;

      for(int[] var11 = new int[]{0}; var10 < var1.length() && var9 < var4; var10 += var11[0]) {
         int var12 = this.parseSingleLocalizedDigit(var1, var10, var11);
         if (var12 < 0) {
            break;
         }

         int var13 = var8 * 10 + var12;
         if (var13 > var6) {
            break;
         }

         var8 = var13;
         ++var9;
      }

      if (var9 >= var3 && var8 >= var5) {
         var7[0] = var10 - var2;
      } else {
         var8 = -1;
         boolean var14 = false;
      }

      return var8;
   }

   private int parseSingleLocalizedDigit(String var1, int var2, int[] var3) {
      int var4 = -1;
      var3[0] = 0;
      if (var2 < var1.length()) {
         int var5 = Character.codePointAt(var1, var2);

         for(int var6 = 0; var6 < this._gmtOffsetDigits.length; ++var6) {
            if (var5 == this._gmtOffsetDigits[var6].codePointAt(0)) {
               var4 = var6;
               break;
            }
         }

         if (var4 < 0) {
            var4 = UCharacter.digit(var5);
         }

         if (var4 >= 0) {
            var3[0] = Character.charCount(var5);
         }
      }

      return var4;
   }

   private static String[] toCodePoints(String var0) {
      int var1 = var0.codePointCount(0, var0.length());
      String[] var2 = new String[var1];
      int var3 = 0;

      for(int var4 = 0; var3 < var1; ++var3) {
         int var5 = var0.codePointAt(var4);
         int var6 = Character.charCount(var5);
         var2[var3] = var0.substring(var4, var4 + var6);
         var4 += var6;
      }

      return var2;
   }

   private static int parseOffsetISO8601(String var0, ParsePosition var1, boolean var2, Output var3) {
      if (var3 != null) {
         var3.value = false;
      }

      int var4 = var1.getIndex();
      if (var4 >= var0.length()) {
         var1.setErrorIndex(var4);
         return 0;
      } else {
         char var5 = var0.charAt(var4);
         if (Character.toUpperCase(var5) == "Z".charAt(0)) {
            var1.setIndex(var4 + 1);
            return 0;
         } else {
            byte var6;
            if (var5 == '+') {
               var6 = 1;
            } else {
               if (var5 != '-') {
                  var1.setErrorIndex(var4);
                  return 0;
               }

               var6 = -1;
            }

            ParsePosition var7 = new ParsePosition(var4 + 1);
            int var8 = parseAsciiOffsetFields(var0, var7, ':', TimeZoneFormat.OffsetFields.H, TimeZoneFormat.OffsetFields.HMS);
            if (var7.getErrorIndex() == -1 && !var2 && var7.getIndex() - var4 <= 3) {
               ParsePosition var9 = new ParsePosition(var4 + 1);
               int var10 = parseAbuttingAsciiOffsetFields(var0, var9, TimeZoneFormat.OffsetFields.H, TimeZoneFormat.OffsetFields.HMS, false);
               if (var9.getErrorIndex() == -1 && var9.getIndex() > var7.getIndex()) {
                  var8 = var10;
                  var7.setIndex(var9.getIndex());
               }
            }

            if (var7.getErrorIndex() != -1) {
               var1.setErrorIndex(var4);
               return 0;
            } else {
               var1.setIndex(var7.getIndex());
               if (var3 != null) {
                  var3.value = true;
               }

               return var6 * var8;
            }
         }
      }
   }

   private static int parseAbuttingAsciiOffsetFields(String var0, ParsePosition var1, TimeZoneFormat.OffsetFields var2, TimeZoneFormat.OffsetFields var3, boolean var4) {
      int var5 = var1.getIndex();
      int var6 = 2 * (var2.ordinal() + 1) - (var4 ? 0 : 1);
      int var7 = 2 * (var3.ordinal() + 1);
      int[] var8 = new int[var7];
      int var9 = 0;

      int var11;
      for(int var10 = var5; var9 < var8.length && var10 < var0.length(); ++var10) {
         var11 = "0123456789".indexOf(var0.charAt(var10));
         if (var11 < 0) {
            break;
         }

         var8[var9] = var11;
         ++var9;
      }

      if (var4 && (var9 & 1) != 0) {
         --var9;
      }

      if (var9 < var6) {
         var1.setErrorIndex(var5);
         return 0;
      } else {
         var11 = 0;
         int var12 = 0;
         int var13 = 0;

         boolean var14;
         for(var14 = false; var9 >= var6; var11 = 0) {
            switch(var9) {
            case 1:
               var11 = var8[0];
               break;
            case 2:
               var11 = var8[0] * 10 + var8[1];
               break;
            case 3:
               var11 = var8[0];
               var12 = var8[1] * 10 + var8[2];
               break;
            case 4:
               var11 = var8[0] * 10 + var8[1];
               var12 = var8[2] * 10 + var8[3];
               break;
            case 5:
               var11 = var8[0];
               var12 = var8[1] * 10 + var8[2];
               var13 = var8[3] * 10 + var8[4];
               break;
            case 6:
               var11 = var8[0] * 10 + var8[1];
               var12 = var8[2] * 10 + var8[3];
               var13 = var8[4] * 10 + var8[5];
            }

            if (var11 <= 23 && var12 <= 59 && var13 <= 59) {
               var14 = true;
               break;
            }

            var9 -= var4 ? 2 : 1;
            var13 = 0;
            var12 = 0;
         }

         if (!var14) {
            var1.setErrorIndex(var5);
            return 0;
         } else {
            var1.setIndex(var5 + var9);
            return ((var11 * 60 + var12) * 60 + var13) * 1000;
         }
      }
   }

   private static int parseAsciiOffsetFields(String var0, ParsePosition var1, char var2, TimeZoneFormat.OffsetFields var3, TimeZoneFormat.OffsetFields var4) {
      int var5 = var1.getIndex();
      int[] var6 = new int[]{0, 0, 0};
      int[] var7 = new int[]{0, -1, -1};
      int var8 = var5;

      int var9;
      for(var9 = 0; var8 < var0.length() && var9 <= var4.ordinal(); ++var8) {
         char var10 = var0.charAt(var8);
         if (var10 == var2) {
            if (var9 == 0) {
               if (var7[0] == 0) {
                  break;
               }

               ++var9;
            } else {
               if (var7[var9] != -1) {
                  break;
               }

               var7[var9] = 0;
            }
         } else {
            if (var7[var9] == -1) {
               break;
            }

            int var11 = "0123456789".indexOf(var10);
            if (var11 < 0) {
               break;
            }

            var6[var9] = var6[var9] * 10 + var11;
            int var10002 = var7[var9]++;
            if (var7[var9] >= 2) {
               ++var9;
            }
         }
      }

      var8 = 0;
      var9 = 0;
      TimeZoneFormat.OffsetFields var12 = null;
      if (var7[0] != 0) {
         if (var6[0] > 23) {
            var8 = var6[0] / 10 * 3600000;
            var12 = TimeZoneFormat.OffsetFields.H;
            var9 = 1;
         } else {
            var8 = var6[0] * 3600000;
            var9 = var7[0];
            var12 = TimeZoneFormat.OffsetFields.H;
            if (var7[1] == 2 && var6[1] <= 59) {
               var8 += var6[1] * '\uea60';
               var9 += 1 + var7[1];
               var12 = TimeZoneFormat.OffsetFields.HM;
               if (var7[2] == 2 && var6[2] <= 59) {
                  var8 += var6[2] * 1000;
                  var9 += 1 + var7[2];
                  var12 = TimeZoneFormat.OffsetFields.HMS;
               }
            }
         }
      }

      if (var12 != null && var12.ordinal() >= var3.ordinal()) {
         var1.setIndex(var5 + var9);
         return var8;
      } else {
         var1.setErrorIndex(var5);
         return 0;
      }
   }

   private static String parseZoneID(String var0, ParsePosition var1) {
      String var2 = null;
      if (ZONE_ID_TRIE == null) {
         Class var3 = TimeZoneFormat.class;
         synchronized(TimeZoneFormat.class){}
         if (ZONE_ID_TRIE == null) {
            TextTrieMap var4 = new TextTrieMap(true);
            String[] var5 = TimeZone.getAvailableIDs();
            String[] var6 = var5;
            int var7 = var5.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               var4.put(var9, var9);
            }

            ZONE_ID_TRIE = var4;
         }
      }

      int[] var11 = new int[]{0};
      Iterator var12 = ZONE_ID_TRIE.get(var0, var1.getIndex(), var11);
      if (var12 != null) {
         var2 = (String)var12.next();
         var1.setIndex(var1.getIndex() + var11[0]);
      } else {
         var1.setErrorIndex(var1.getIndex());
      }

      return var2;
   }

   private static String parseShortZoneID(String var0, ParsePosition var1) {
      String var2 = null;
      if (SHORT_ZONE_ID_TRIE == null) {
         Class var3 = TimeZoneFormat.class;
         synchronized(TimeZoneFormat.class){}
         if (SHORT_ZONE_ID_TRIE == null) {
            TextTrieMap var4 = new TextTrieMap(true);
            Set var5 = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, (String)null, (Integer)null);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               String var8 = ZoneMeta.getShortID(var7);
               if (var8 != null) {
                  var4.put(var8, var7);
               }
            }

            var4.put("unk", "Etc/Unknown");
            SHORT_ZONE_ID_TRIE = var4;
         }
      }

      int[] var10 = new int[]{0};
      Iterator var11 = SHORT_ZONE_ID_TRIE.get(var0, var1.getIndex(), var10);
      if (var11 != null) {
         var2 = (String)var11.next();
         var1.setIndex(var1.getIndex() + var10[0]);
      } else {
         var1.setErrorIndex(var1.getIndex());
      }

      return var2;
   }

   private String parseExemplarLocation(String var1, ParsePosition var2) {
      int var3 = var2.getIndex();
      int var4 = -1;
      String var5 = null;
      EnumSet var6 = EnumSet.of(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
      Collection var7 = this._tznames.find(var1, var3, var6);
      if (var7 != null) {
         TimeZoneNames.MatchInfo var8 = null;
         Iterator var9 = var7.iterator();

         while(var9.hasNext()) {
            TimeZoneNames.MatchInfo var10 = (TimeZoneNames.MatchInfo)var9.next();
            if (var3 + var10.matchLength() > var4) {
               var8 = var10;
               var4 = var3 + var10.matchLength();
            }
         }

         if (var8 != null) {
            var5 = this.getTimeZoneID(var8.tzID(), var8.mzID());
            var2.setIndex(var4);
         }
      }

      if (var5 == null) {
         var2.setErrorIndex(var3);
      }

      return var5;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      PutField var2 = var1.putFields();
      var2.put("_locale", this._locale);
      var2.put("_tznames", this._tznames);
      var2.put("_gmtPattern", this._gmtPattern);
      var2.put("_gmtOffsetPatterns", this._gmtOffsetPatterns);
      var2.put("_gmtOffsetDigits", this._gmtOffsetDigits);
      var2.put("_gmtZeroFormat", this._gmtZeroFormat);
      var2.put("_parseAllStyles", this._parseAllStyles);
      var1.writeFields();
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      GetField var2 = var1.readFields();
      this._locale = (ULocale)var2.get("_locale", (Object)null);
      if (this._locale == null) {
         throw new InvalidObjectException("Missing field: locale");
      } else {
         this._tznames = (TimeZoneNames)var2.get("_tznames", (Object)null);
         if (this._tznames == null) {
            throw new InvalidObjectException("Missing field: tznames");
         } else {
            this._gmtPattern = (String)var2.get("_gmtPattern", (Object)null);
            if (this._gmtPattern == null) {
               throw new InvalidObjectException("Missing field: gmtPattern");
            } else {
               String[] var3 = (String[])((String[])var2.get("_gmtOffsetPatterns", (Object)null));
               if (var3 == null) {
                  throw new InvalidObjectException("Missing field: gmtOffsetPatterns");
               } else if (var3.length < 4) {
                  throw new InvalidObjectException("Incompatible field: gmtOffsetPatterns");
               } else {
                  this._gmtOffsetPatterns = new String[6];
                  if (var3.length == 4) {
                     for(int var4 = 0; var4 < 4; ++var4) {
                        this._gmtOffsetPatterns[var4] = var3[var4];
                     }

                     this._gmtOffsetPatterns[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HM.ordinal()]);
                     this._gmtOffsetPatterns[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HM.ordinal()]);
                  } else {
                     this._gmtOffsetPatterns = var3;
                  }

                  this._gmtOffsetDigits = (String[])((String[])var2.get("_gmtOffsetDigits", (Object)null));
                  if (this._gmtOffsetDigits == null) {
                     throw new InvalidObjectException("Missing field: gmtOffsetDigits");
                  } else if (this._gmtOffsetDigits.length != 10) {
                     throw new InvalidObjectException("Incompatible field: gmtOffsetDigits");
                  } else {
                     this._gmtZeroFormat = (String)var2.get("_gmtZeroFormat", (Object)null);
                     if (this._gmtZeroFormat == null) {
                        throw new InvalidObjectException("Missing field: gmtZeroFormat");
                     } else {
                        this._parseAllStyles = var2.get("_parseAllStyles", false);
                        if (var2.defaulted("_parseAllStyles")) {
                           throw new InvalidObjectException("Missing field: parseAllStyles");
                        } else {
                           if (this._tznames instanceof TimeZoneNamesImpl) {
                              this._tznames = TimeZoneNames.getInstance(this._locale);
                              this._gnames = null;
                           } else {
                              this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
                           }

                           this.initGMTPattern(this._gmtPattern);
                           this.initGMTOffsetPatterns(this._gmtOffsetPatterns);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean isFrozen() {
      return this._frozen;
   }

   public TimeZoneFormat freeze() {
      this._frozen = true;
      return this;
   }

   public TimeZoneFormat cloneAsThawed() {
      TimeZoneFormat var1 = (TimeZoneFormat)super.clone();
      var1._frozen = false;
      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static {
      PARSE_GMT_OFFSET_TYPES = new TimeZoneFormat.GMTOffsetPatternType[]{TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HMS, TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HMS, TimeZoneFormat.GMTOffsetPatternType.POSITIVE_HM, TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_HM, TimeZoneFormat.GMTOffsetPatternType.POSITIVE_H, TimeZoneFormat.GMTOffsetPatternType.NEGATIVE_H};
      _tzfCache = new TimeZoneFormat.TimeZoneFormatCache();
      ALL_SIMPLE_NAME_TYPES = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.EXEMPLAR_LOCATION);
      ALL_GENERIC_NAME_TYPES = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION, TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.SHORT);
      serialPersistentFields = new ObjectStreamField[]{new ObjectStreamField("_locale", ULocale.class), new ObjectStreamField("_tznames", TimeZoneNames.class), new ObjectStreamField("_gmtPattern", String.class), new ObjectStreamField("_gmtOffsetPatterns", String[].class), new ObjectStreamField("_gmtOffsetDigits", String[].class), new ObjectStreamField("_gmtZeroFormat", String.class), new ObjectStreamField("_parseAllStyles", Boolean.TYPE)};
   }

   private static class TimeZoneFormatCache extends SoftCache {
      private TimeZoneFormatCache() {
      }

      protected TimeZoneFormat createInstance(ULocale var1, ULocale var2) {
         TimeZoneFormat var3 = new TimeZoneFormat(var2);
         var3.freeze();
         return var3;
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((ULocale)var1, (ULocale)var2);
      }

      TimeZoneFormatCache(Object var1) {
         this();
      }
   }

   private static class GMTOffsetField {
      final char _type;
      final int _width;

      GMTOffsetField(char var1, int var2) {
         this._type = var1;
         this._width = var2;
      }

      char getType() {
         return this._type;
      }

      int getWidth() {
         return this._width;
      }

      static boolean isValid(char var0, int var1) {
         return var1 == 1 || var1 == 2;
      }
   }

   private static enum OffsetFields {
      H,
      HM,
      HMS;

      private static final TimeZoneFormat.OffsetFields[] $VALUES = new TimeZoneFormat.OffsetFields[]{H, HM, HMS};
   }

   public static enum ParseOption {
      ALL_STYLES;

      private static final TimeZoneFormat.ParseOption[] $VALUES = new TimeZoneFormat.ParseOption[]{ALL_STYLES};
   }

   public static enum TimeType {
      UNKNOWN,
      STANDARD,
      DAYLIGHT;

      private static final TimeZoneFormat.TimeType[] $VALUES = new TimeZoneFormat.TimeType[]{UNKNOWN, STANDARD, DAYLIGHT};
   }

   public static enum GMTOffsetPatternType {
      POSITIVE_HM("+H:mm", "Hm", true),
      POSITIVE_HMS("+H:mm:ss", "Hms", true),
      NEGATIVE_HM("-H:mm", "Hm", false),
      NEGATIVE_HMS("-H:mm:ss", "Hms", false),
      POSITIVE_H("+H", "H", true),
      NEGATIVE_H("-H", "H", false);

      private String _defaultPattern;
      private String _required;
      private boolean _isPositive;
      private static final TimeZoneFormat.GMTOffsetPatternType[] $VALUES = new TimeZoneFormat.GMTOffsetPatternType[]{POSITIVE_HM, POSITIVE_HMS, NEGATIVE_HM, NEGATIVE_HMS, POSITIVE_H, NEGATIVE_H};

      private GMTOffsetPatternType(String var3, String var4, boolean var5) {
         this._defaultPattern = var3;
         this._required = var4;
         this._isPositive = var5;
      }

      private String defaultPattern() {
         return this._defaultPattern;
      }

      private String required() {
         return this._required;
      }

      private boolean isPositive() {
         return this._isPositive;
      }

      static String access$100(TimeZoneFormat.GMTOffsetPatternType var0) {
         return var0.defaultPattern();
      }

      static String access$200(TimeZoneFormat.GMTOffsetPatternType var0) {
         return var0.required();
      }

      static boolean access$300(TimeZoneFormat.GMTOffsetPatternType var0) {
         return var0.isPositive();
      }
   }

   public static enum Style {
      GENERIC_LOCATION(1),
      GENERIC_LONG(2),
      GENERIC_SHORT(4),
      SPECIFIC_LONG(8),
      SPECIFIC_SHORT(16),
      LOCALIZED_GMT(32),
      LOCALIZED_GMT_SHORT(64),
      ISO_BASIC_SHORT(128),
      ISO_BASIC_LOCAL_SHORT(256),
      ISO_BASIC_FIXED(128),
      ISO_BASIC_LOCAL_FIXED(256),
      ISO_BASIC_FULL(128),
      ISO_BASIC_LOCAL_FULL(256),
      ISO_EXTENDED_FIXED(128),
      ISO_EXTENDED_LOCAL_FIXED(256),
      ISO_EXTENDED_FULL(128),
      ISO_EXTENDED_LOCAL_FULL(256),
      ZONE_ID(512),
      ZONE_ID_SHORT(1024),
      EXEMPLAR_LOCATION(2048);

      final int flag;
      private static final TimeZoneFormat.Style[] $VALUES = new TimeZoneFormat.Style[]{GENERIC_LOCATION, GENERIC_LONG, GENERIC_SHORT, SPECIFIC_LONG, SPECIFIC_SHORT, LOCALIZED_GMT, LOCALIZED_GMT_SHORT, ISO_BASIC_SHORT, ISO_BASIC_LOCAL_SHORT, ISO_BASIC_FIXED, ISO_BASIC_LOCAL_FIXED, ISO_BASIC_FULL, ISO_BASIC_LOCAL_FULL, ISO_EXTENDED_FIXED, ISO_EXTENDED_LOCAL_FIXED, ISO_EXTENDED_FULL, ISO_EXTENDED_LOCAL_FULL, ZONE_ID, ZONE_ID_SHORT, EXEMPLAR_LOCATION};

      private Style(int var3) {
         this.flag = var3;
      }
   }
}
