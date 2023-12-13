package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.DateInterval;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateIntervalFormat extends UFormat {
   private static final long serialVersionUID = 1L;
   private static ICUCache LOCAL_PATTERN_CACHE = new SimpleCache();
   private DateIntervalInfo fInfo;
   private SimpleDateFormat fDateFormat;
   private Calendar fFromCalendar;
   private Calendar fToCalendar;
   private String fSkeleton = null;
   private boolean isDateIntervalInfoDefault;
   private transient Map fIntervalPatterns = null;

   private DateIntervalFormat() {
   }

   /** @deprecated */
   public DateIntervalFormat(String var1, DateIntervalInfo var2, SimpleDateFormat var3) {
      this.fDateFormat = var3;
      var2.freeze();
      this.fSkeleton = var1;
      this.fInfo = var2;
      this.isDateIntervalInfoDefault = false;
      this.fFromCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
      this.fToCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
      this.initializePattern((ICUCache)null);
   }

   private DateIntervalFormat(String var1, ULocale var2, SimpleDateFormat var3) {
      this.fDateFormat = var3;
      this.fSkeleton = var1;
      this.fInfo = (new DateIntervalInfo(var2)).freeze();
      this.isDateIntervalInfoDefault = true;
      this.fFromCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
      this.fToCalendar = (Calendar)this.fDateFormat.getCalendar().clone();
      this.initializePattern(LOCAL_PATTERN_CACHE);
   }

   public static final DateIntervalFormat getInstance(String var0) {
      return getInstance(var0, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static final DateIntervalFormat getInstance(String var0, Locale var1) {
      return getInstance(var0, ULocale.forLocale(var1));
   }

   public static final DateIntervalFormat getInstance(String var0, ULocale var1) {
      DateTimePatternGenerator var2 = DateTimePatternGenerator.getInstance(var1);
      return new DateIntervalFormat(var0, var1, new SimpleDateFormat(var2.getBestPattern(var0), var1));
   }

   public static final DateIntervalFormat getInstance(String var0, DateIntervalInfo var1) {
      return getInstance(var0, ULocale.getDefault(ULocale.Category.FORMAT), var1);
   }

   public static final DateIntervalFormat getInstance(String var0, Locale var1, DateIntervalInfo var2) {
      return getInstance(var0, ULocale.forLocale(var1), var2);
   }

   public static final DateIntervalFormat getInstance(String var0, ULocale var1, DateIntervalInfo var2) {
      var2 = (DateIntervalInfo)var2.clone();
      DateTimePatternGenerator var3 = DateTimePatternGenerator.getInstance(var1);
      return new DateIntervalFormat(var0, var2, new SimpleDateFormat(var3.getBestPattern(var0), var1));
   }

   public Object clone() {
      DateIntervalFormat var1 = (DateIntervalFormat)super.clone();
      var1.fDateFormat = (SimpleDateFormat)this.fDateFormat.clone();
      var1.fInfo = (DateIntervalInfo)this.fInfo.clone();
      var1.fFromCalendar = (Calendar)this.fFromCalendar.clone();
      var1.fToCalendar = (Calendar)this.fToCalendar.clone();
      return var1;
   }

   public final StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof DateInterval) {
         return this.format((DateInterval)var1, var2, var3);
      } else {
         throw new IllegalArgumentException("Cannot format given Object (" + var1.getClass().getName() + ") as a DateInterval");
      }
   }

   public final StringBuffer format(DateInterval var1, StringBuffer var2, FieldPosition var3) {
      this.fFromCalendar.setTimeInMillis(var1.getFromDate());
      this.fToCalendar.setTimeInMillis(var1.getToDate());
      return this.format(this.fFromCalendar, this.fToCalendar, var2, var3);
   }

   public final StringBuffer format(Calendar var1, Calendar var2, StringBuffer var3, FieldPosition var4) {
      if (!var1.isEquivalentTo(var2)) {
         throw new IllegalArgumentException("can not format on two different calendars");
      } else {
         boolean var5 = true;
         byte var10;
         if (var1.get(0) != var2.get(0)) {
            var10 = 0;
         } else if (var1.get(1) != var2.get(1)) {
            var10 = 1;
         } else if (var1.get(2) != var2.get(2)) {
            var10 = 2;
         } else if (var1.get(5) != var2.get(5)) {
            var10 = 5;
         } else if (var1.get(9) != var2.get(9)) {
            var10 = 9;
         } else if (var1.get(10) != var2.get(10)) {
            var10 = 10;
         } else {
            if (var1.get(12) == var2.get(12)) {
               return this.fDateFormat.format(var1, var3, var4);
            }

            var10 = 12;
         }

         DateIntervalInfo.PatternInfo var6 = (DateIntervalInfo.PatternInfo)this.fIntervalPatterns.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var10]);
         if (var6 == null) {
            return this.fDateFormat.isFieldUnitIgnored(var10) ? this.fDateFormat.format(var1, var3, var4) : this.fallbackFormat(var1, var2, var3, var4);
         } else if (var6.getFirstPart() == null) {
            return this.fallbackFormat(var1, var2, var3, var4, var6.getSecondPart());
         } else {
            Calendar var7;
            Calendar var8;
            if (var6.firstDateInPtnIsLaterDate()) {
               var7 = var2;
               var8 = var1;
            } else {
               var7 = var1;
               var8 = var2;
            }

            String var9 = this.fDateFormat.toPattern();
            this.fDateFormat.applyPattern(var6.getFirstPart());
            this.fDateFormat.format(var7, var3, var4);
            if (var6.getSecondPart() != null) {
               this.fDateFormat.applyPattern(var6.getSecondPart());
               this.fDateFormat.format(var8, var3, var4);
            }

            this.fDateFormat.applyPattern(var9);
            return var3;
         }
      }
   }

   private final StringBuffer fallbackFormat(Calendar var1, Calendar var2, StringBuffer var3, FieldPosition var4) {
      StringBuffer var5 = new StringBuffer(64);
      var5 = this.fDateFormat.format(var1, var5, var4);
      StringBuffer var6 = new StringBuffer(64);
      var6 = this.fDateFormat.format(var2, var6, var4);
      String var7 = this.fInfo.getFallbackIntervalPattern();
      String var8 = MessageFormat.format(var7, var5.toString(), var6.toString());
      var3.append(var8);
      return var3;
   }

   private final StringBuffer fallbackFormat(Calendar var1, Calendar var2, StringBuffer var3, FieldPosition var4, String var5) {
      String var6 = this.fDateFormat.toPattern();
      this.fDateFormat.applyPattern(var5);
      this.fallbackFormat(var1, var2, var3, var4);
      this.fDateFormat.applyPattern(var6);
      return var3;
   }

   /** @deprecated */
   public Object parseObject(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException("parsing is not supported");
   }

   public DateIntervalInfo getDateIntervalInfo() {
      return (DateIntervalInfo)this.fInfo.clone();
   }

   public void setDateIntervalInfo(DateIntervalInfo var1) {
      this.fInfo = (DateIntervalInfo)var1.clone();
      this.isDateIntervalInfoDefault = false;
      this.fInfo.freeze();
      if (this.fDateFormat != null) {
         this.initializePattern((ICUCache)null);
      }

   }

   public DateFormat getDateFormat() {
      return (DateFormat)this.fDateFormat.clone();
   }

   private void initializePattern(ICUCache var1) {
      String var2 = this.fDateFormat.toPattern();
      ULocale var3 = this.fDateFormat.getLocale();
      String var4 = null;
      Map var5 = null;
      if (var1 != null) {
         if (this.fSkeleton != null) {
            var4 = var3.toString() + "+" + var2 + "+" + this.fSkeleton;
         } else {
            var4 = var3.toString() + "+" + var2;
         }

         var5 = (Map)var1.get(var4);
      }

      if (var5 == null) {
         Map var6 = this.initializeIntervalPattern(var2, var3);
         var5 = Collections.unmodifiableMap(var6);
         if (var1 != null) {
            var1.put(var4, var5);
         }
      }

      this.fIntervalPatterns = var5;
   }

   private Map initializeIntervalPattern(String var1, ULocale var2) {
      DateTimePatternGenerator var3 = DateTimePatternGenerator.getInstance(var2);
      if (this.fSkeleton == null) {
         this.fSkeleton = var3.getSkeleton(var1);
      }

      String var4 = this.fSkeleton;
      HashMap var5 = new HashMap();
      StringBuilder var6 = new StringBuilder(var4.length());
      StringBuilder var7 = new StringBuilder(var4.length());
      StringBuilder var8 = new StringBuilder(var4.length());
      StringBuilder var9 = new StringBuilder(var4.length());
      getDateTimeSkeleton(var4, var6, var7, var8, var9);
      String var10 = var6.toString();
      String var11 = var8.toString();
      String var12 = var7.toString();
      String var13 = var9.toString();
      boolean var14 = this.genSeparateDateTimePtn(var12, var13, var5);
      String var15;
      DateIntervalInfo.PatternInfo var16;
      if (!var14) {
         if (var8.length() != 0 && var6.length() == 0) {
            var11 = "yMd" + var11;
            var15 = var3.getBestPattern(var11);
            var16 = new DateIntervalInfo.PatternInfo((String)null, var15, this.fInfo.getDefaultOrder());
            var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], var16);
            var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], var16);
            var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], var16);
         }

         return var5;
      } else {
         if (var8.length() != 0) {
            if (var6.length() == 0) {
               var11 = "yMd" + var11;
               var15 = var3.getBestPattern(var11);
               var16 = new DateIntervalInfo.PatternInfo((String)null, var15, this.fInfo.getDefaultOrder());
               var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5], var16);
               var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2], var16);
               var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1], var16);
            } else {
               if (5 == var10) {
                  var4 = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[5] + var4;
                  this.genFallbackPattern(5, var4, var5, var3);
               }

               if (2 == var10) {
                  var4 = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[2] + var4;
                  this.genFallbackPattern(2, var4, var5, var3);
               }

               if (true == var10) {
                  var4 = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[1] + var4;
                  this.genFallbackPattern(1, var4, var5, var3);
               }

               CalendarData var18 = new CalendarData(var2, (String)null);
               String[] var19 = var18.getDateTimePatterns();
               String var17 = var3.getBestPattern(var10);
               this.concatSingleDate2TimeInterval(var19[8], var17, 9, var5);
               this.concatSingleDate2TimeInterval(var19[8], var17, 10, var5);
               this.concatSingleDate2TimeInterval(var19[8], var17, 12, var5);
            }
         }

         return var5;
      }
   }

   private void genFallbackPattern(int var1, String var2, Map var3, DateTimePatternGenerator var4) {
      String var5 = var4.getBestPattern(var2);
      DateIntervalInfo.PatternInfo var6 = new DateIntervalInfo.PatternInfo((String)null, var5, this.fInfo.getDefaultOrder());
      var3.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var1], var6);
   }

   private static void getDateTimeSkeleton(String var0, StringBuilder var1, StringBuilder var2, StringBuilder var3, StringBuilder var4) {
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      int var12 = 0;
      int var13 = 0;
      int var14 = 0;

      int var5;
      for(var5 = 0; var5 < var0.length(); ++var5) {
         char var15 = var0.charAt(var5);
         switch(var15) {
         case 'A':
         case 'K':
         case 'S':
         case 'V':
         case 'Z':
         case 'j':
         case 'k':
         case 's':
            var3.append(var15);
            var4.append(var15);
         case 'B':
         case 'C':
         case 'I':
         case 'J':
         case 'N':
         case 'O':
         case 'P':
         case 'R':
         case 'T':
         case 'U':
         case 'X':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '_':
         case '`':
         case 'b':
         case 'f':
         case 'i':
         case 'n':
         case 'o':
         case 'p':
         case 'r':
         case 't':
         case 'x':
         default:
            break;
         case 'D':
         case 'F':
         case 'G':
         case 'L':
         case 'Q':
         case 'W':
         case 'Y':
         case 'c':
         case 'e':
         case 'g':
         case 'l':
         case 'q':
         case 'u':
         case 'w':
            var2.append(var15);
            var1.append(var15);
            break;
         case 'E':
            var1.append(var15);
            ++var6;
            break;
         case 'H':
            var3.append(var15);
            ++var11;
            break;
         case 'M':
            var1.append(var15);
            ++var8;
            break;
         case 'a':
            var3.append(var15);
            break;
         case 'd':
            var1.append(var15);
            ++var7;
            break;
         case 'h':
            var3.append(var15);
            ++var10;
            break;
         case 'm':
            var3.append(var15);
            ++var12;
            break;
         case 'v':
            ++var13;
            var3.append(var15);
            break;
         case 'y':
            var1.append(var15);
            ++var9;
            break;
         case 'z':
            ++var14;
            var3.append(var15);
         }
      }

      if (var9 != 0) {
         var2.append('y');
      }

      if (var8 != 0) {
         if (var8 < 3) {
            var2.append('M');
         } else {
            for(var5 = 0; var5 < var8 && var5 < 5; ++var5) {
               var2.append('M');
            }
         }
      }

      if (var6 != 0) {
         if (var6 <= 3) {
            var2.append('E');
         } else {
            for(var5 = 0; var5 < var6 && var5 < 5; ++var5) {
               var2.append('E');
            }
         }
      }

      if (var7 != 0) {
         var2.append('d');
      }

      if (var11 != 0) {
         var4.append('H');
      } else if (var10 != 0) {
         var4.append('h');
      }

      if (var12 != 0) {
         var4.append('m');
      }

      if (var14 != 0) {
         var4.append('z');
      }

      if (var13 != 0) {
         var4.append('v');
      }

   }

   private boolean genSeparateDateTimePtn(String var1, String var2, Map var3) {
      String var4;
      if (var2.length() != 0) {
         var4 = var2;
      } else {
         var4 = var1;
      }

      DateIntervalFormat.BestMatchInfo var5 = this.fInfo.getBestSkeleton(var4);
      String var6 = var5.bestMatchSkeleton;
      int var7 = var5.bestMatchDistanceInfo;
      if (var7 == -1) {
         return false;
      } else {
         if (var2.length() == 0) {
            this.genIntervalPattern(5, var4, var6, var7, var3);
            DateIntervalFormat.SkeletonAndItsBestMatch var8 = this.genIntervalPattern(2, var4, var6, var7, var3);
            if (var8 != null) {
               var6 = var8.skeleton;
               var4 = var8.bestMatchSkeleton;
            }

            this.genIntervalPattern(1, var4, var6, var7, var3);
         } else {
            this.genIntervalPattern(12, var4, var6, var7, var3);
            this.genIntervalPattern(10, var4, var6, var7, var3);
            this.genIntervalPattern(9, var4, var6, var7, var3);
         }

         return true;
      }
   }

   private DateIntervalFormat.SkeletonAndItsBestMatch genIntervalPattern(int var1, String var2, String var3, int var4, Map var5) {
      DateIntervalFormat.SkeletonAndItsBestMatch var6 = null;
      DateIntervalInfo.PatternInfo var7 = this.fInfo.getIntervalPattern(var3, var1);
      String var8;
      if (var7 == null) {
         if (SimpleDateFormat.isFieldUnitIgnored(var3, var1)) {
            DateIntervalInfo.PatternInfo var11 = new DateIntervalInfo.PatternInfo(this.fDateFormat.toPattern(), (String)null, this.fInfo.getDefaultOrder());
            var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var1], var11);
            return null;
         }

         if (var1 == 9) {
            var7 = this.fInfo.getIntervalPattern(var3, 10);
            if (var7 != null) {
               var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var1], var7);
            }

            return null;
         }

         var8 = DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var1];
         var3 = var8 + var3;
         var2 = var8 + var2;
         var7 = this.fInfo.getIntervalPattern(var3, var1);
         if (var7 == null && var4 == 0) {
            DateIntervalFormat.BestMatchInfo var9 = this.fInfo.getBestSkeleton(var2);
            String var10 = var9.bestMatchSkeleton;
            var4 = var9.bestMatchDistanceInfo;
            if (var10.length() != 0 && var4 != -1) {
               var7 = this.fInfo.getIntervalPattern(var10, var1);
               var3 = var10;
            }
         }

         if (var7 != null) {
            var6 = new DateIntervalFormat.SkeletonAndItsBestMatch(var2, var3);
         }
      }

      if (var7 != null) {
         if (var4 != 0) {
            var8 = adjustFieldWidth(var2, var3, var7.getFirstPart(), var4);
            String var12 = adjustFieldWidth(var2, var3, var7.getSecondPart(), var4);
            var7 = new DateIntervalInfo.PatternInfo(var8, var12, var7.firstDateInPtnIsLaterDate());
         }

         var5.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var1], var7);
      }

      return var6;
   }

   private static String adjustFieldWidth(String var0, String var1, String var2, int var3) {
      if (var2 == null) {
         return null;
      } else {
         int[] var4 = new int[58];
         int[] var5 = new int[58];
         DateIntervalInfo.parseSkeleton(var0, var4);
         DateIntervalInfo.parseSkeleton(var1, var5);
         if (var3 == 2) {
            var2 = var2.replace('v', 'z');
         }

         StringBuilder var6 = new StringBuilder(var2);
         boolean var7 = false;
         char var8 = 0;
         int var9 = 0;
         byte var10 = 65;
         int var11 = var6.length();

         int var15;
         for(int var12 = 0; var12 < var11; ++var12) {
            char var13 = var6.charAt(var12);
            if (var13 != var8 && var9 > 0) {
               char var14 = var8;
               if (var8 == 'L') {
                  var14 = 'M';
               }

               var15 = var5[var14 - var10];
               int var16 = var4[var14 - var10];
               if (var15 == var9 && var16 > var15) {
                  var9 = var16 - var15;

                  for(int var17 = 0; var17 < var9; ++var17) {
                     var6.insert(var12, var8);
                  }

                  var12 += var9;
                  var11 += var9;
               }

               var9 = 0;
            }

            if (var13 == '\'') {
               if (var12 + 1 < var6.length() && var6.charAt(var12 + 1) == '\'') {
                  ++var12;
               } else {
                  var7 = !var7;
               }
            } else if (!var7 && (var13 >= 'a' && var13 <= 'z' || var13 >= 'A' && var13 <= 'Z')) {
               var8 = var13;
               ++var9;
            }
         }

         if (var9 > 0) {
            char var19 = var8;
            if (var8 == 'L') {
               var19 = 'M';
            }

            int var18 = var5[var19 - var10];
            int var20 = var4[var19 - var10];
            if (var18 == var9 && var20 > var18) {
               var9 = var20 - var18;

               for(var15 = 0; var15 < var9; ++var15) {
                  var6.append(var8);
               }
            }
         }

         return var6.toString();
      }
   }

   private void concatSingleDate2TimeInterval(String var1, String var2, int var3, Map var4) {
      DateIntervalInfo.PatternInfo var5 = (DateIntervalInfo.PatternInfo)var4.get(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var3]);
      if (var5 != null) {
         String var6 = var5.getFirstPart() + var5.getSecondPart();
         String var7 = MessageFormat.format(var1, var6, var2);
         var5 = DateIntervalInfo.genPatternInfo(var7, var5.firstDateInPtnIsLaterDate());
         var4.put(DateIntervalInfo.CALENDAR_FIELD_TO_PATTERN_LETTER[var3], var5);
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.initializePattern(this.isDateIntervalInfoDefault ? LOCAL_PATTERN_CACHE : null);
   }

   private static final class SkeletonAndItsBestMatch {
      final String skeleton;
      final String bestMatchSkeleton;

      SkeletonAndItsBestMatch(String var1, String var2) {
         this.skeleton = var1;
         this.bestMatchSkeleton = var2;
      }
   }

   static final class BestMatchInfo {
      final String bestMatchSkeleton;
      final int bestMatchDistanceInfo;

      BestMatchInfo(String var1, int var2) {
         this.bestMatchSkeleton = var1;
         this.bestMatchDistanceInfo = var2;
      }
   }
}
