package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Map.Entry;

public class DateIntervalInfo implements Cloneable, Freezable, Serializable {
   static final int currentSerialVersion = 1;
   static final String[] CALENDAR_FIELD_TO_PATTERN_LETTER = new String[]{"G", "y", "M", "w", "W", "d", "D", "E", "F", "a", "h", "H", "m"};
   private static final long serialVersionUID = 1L;
   private static final int MINIMUM_SUPPORTED_CALENDAR_FIELD = 12;
   private static String FALLBACK_STRING = "fallback";
   private static String LATEST_FIRST_PREFIX = "latestFirst:";
   private static String EARLIEST_FIRST_PREFIX = "earliestFirst:";
   private static final ICUCache DIICACHE = new SimpleCache();
   private String fFallbackIntervalPattern;
   private boolean fFirstDateInPtnIsLaterDate = false;
   private Map fIntervalPatterns = null;
   private transient boolean frozen = false;
   private transient boolean fIntervalPatternsReadOnly = false;

   /** @deprecated */
   public DateIntervalInfo() {
      this.fIntervalPatterns = new HashMap();
      this.fFallbackIntervalPattern = "{0} – {1}";
   }

   public DateIntervalInfo(ULocale var1) {
      this.initializeData(var1);
   }

   private void initializeData(ULocale var1) {
      String var2 = var1.toString();
      DateIntervalInfo var3 = (DateIntervalInfo)DIICACHE.get(var2);
      if (var3 == null) {
         this.setup(var1);
         this.fIntervalPatternsReadOnly = true;
         DIICACHE.put(var2, ((DateIntervalInfo)this.clone()).freeze());
      } else {
         this.initializeFromReadOnlyPatterns(var3);
      }

   }

   private void initializeFromReadOnlyPatterns(DateIntervalInfo var1) {
      this.fFallbackIntervalPattern = var1.fFallbackIntervalPattern;
      this.fFirstDateInPtnIsLaterDate = var1.fFirstDateInPtnIsLaterDate;
      this.fIntervalPatterns = var1.fIntervalPatterns;
      this.fIntervalPatternsReadOnly = true;
   }

   private void setup(ULocale var1) {
      byte var2 = 19;
      this.fIntervalPatterns = new HashMap(var2);
      this.fFallbackIntervalPattern = "{0} – {1}";
      HashSet var3 = new HashSet();

      try {
         ULocale var4 = var1;
         String var5 = var1.getKeywordValue("calendar");
         if (var5 == null) {
            String[] var6 = Calendar.getKeywordValuesForLocale("calendar", var1, true);
            var5 = var6[0];
         }

         if (var5 == null) {
            var5 = "gregorian";
         }

         do {
            String var21 = var4.getName();
            if (var21.length() == 0) {
               break;
            }

            ICUResourceBundle var7 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var4);
            ICUResourceBundle var8 = var7.getWithFallback("calendar/" + var5 + "/intervalFormats");
            String var9 = var8.getStringWithFallback(FALLBACK_STRING);
            this.setFallbackIntervalPattern(var9);
            int var10 = var8.getSize();

            for(int var11 = 0; var11 < var10; ++var11) {
               String var12 = var8.get(var11).getKey();
               if (!var3.contains(var12)) {
                  var3.add(var12);
                  if (var12.compareTo(FALLBACK_STRING) != 0) {
                     ICUResourceBundle var13 = (ICUResourceBundle)var8.get(var12);
                     int var14 = var13.getSize();

                     for(int var15 = 0; var15 < var14; ++var15) {
                        String var16 = var13.get(var15).getKey();
                        String var17 = var13.get(var15).getString();
                        byte var18 = -1;
                        if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[1]) == 0) {
                           var18 = 1;
                        } else if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[2]) == 0) {
                           var18 = 2;
                        } else if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[5]) == 0) {
                           var18 = 5;
                        } else if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[9]) == 0) {
                           var18 = 9;
                        } else if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[10]) == 0) {
                           var18 = 10;
                        } else if (var16.compareTo(CALENDAR_FIELD_TO_PATTERN_LETTER[12]) == 0) {
                           var18 = 12;
                        }

                        if (var18 != -1) {
                           this.setIntervalPatternInternally(var12, var16, var17);
                        }
                     }
                  }
               }
            }

            try {
               UResourceBundle var22 = var7.get("%%Parent");
               var4 = new ULocale(var22.getString());
            } catch (MissingResourceException var19) {
               var4 = var4.getFallback();
            }
         } while(var4 != null && !var4.getBaseName().equals("root"));
      } catch (MissingResourceException var20) {
      }

   }

   private static int splitPatternInto2Part(String var0) {
      boolean var1 = false;
      char var2 = 0;
      int var3 = 0;
      int[] var4 = new int[58];
      byte var5 = 65;
      boolean var7 = false;

      int var6;
      for(var6 = 0; var6 < var0.length(); ++var6) {
         char var8 = var0.charAt(var6);
         if (var8 != var2 && var3 > 0) {
            int var9 = var4[var2 - var5];
            if (var9 != 0) {
               var7 = true;
               break;
            }

            var4[var2 - var5] = 1;
            var3 = 0;
         }

         if (var8 == '\'') {
            if (var6 + 1 < var0.length() && var0.charAt(var6 + 1) == '\'') {
               ++var6;
            } else {
               var1 = !var1;
            }
         } else if (!var1 && (var8 >= 'a' && var8 <= 'z' || var8 >= 'A' && var8 <= 'Z')) {
            var2 = var8;
            ++var3;
         }
      }

      if (var3 > 0 && !var7 && var4[var2 - var5] == 0) {
         var3 = 0;
      }

      return var6 - var3;
   }

   public void setIntervalPattern(String var1, int var2, String var3) {
      if (this.frozen) {
         throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
      } else if (var2 > 12) {
         throw new IllegalArgumentException("calendar field is larger than MINIMUM_SUPPORTED_CALENDAR_FIELD");
      } else {
         if (this.fIntervalPatternsReadOnly) {
            this.fIntervalPatterns = cloneIntervalPatterns(this.fIntervalPatterns);
            this.fIntervalPatternsReadOnly = false;
         }

         DateIntervalInfo.PatternInfo var4 = this.setIntervalPatternInternally(var1, CALENDAR_FIELD_TO_PATTERN_LETTER[var2], var3);
         if (var2 == 11) {
            this.setIntervalPattern(var1, CALENDAR_FIELD_TO_PATTERN_LETTER[9], var4);
            this.setIntervalPattern(var1, CALENDAR_FIELD_TO_PATTERN_LETTER[10], var4);
         } else if (var2 == 5 || var2 == 7) {
            this.setIntervalPattern(var1, CALENDAR_FIELD_TO_PATTERN_LETTER[5], var4);
         }

      }
   }

   private DateIntervalInfo.PatternInfo setIntervalPatternInternally(String var1, String var2, String var3) {
      Object var4 = (Map)this.fIntervalPatterns.get(var1);
      boolean var5 = false;
      if (var4 == null) {
         var4 = new HashMap();
         var5 = true;
      }

      boolean var6 = this.fFirstDateInPtnIsLaterDate;
      int var7;
      if (var3.startsWith(LATEST_FIRST_PREFIX)) {
         var6 = true;
         var7 = LATEST_FIRST_PREFIX.length();
         var3 = var3.substring(var7, var3.length());
      } else if (var3.startsWith(EARLIEST_FIRST_PREFIX)) {
         var6 = false;
         var7 = EARLIEST_FIRST_PREFIX.length();
         var3 = var3.substring(var7, var3.length());
      }

      DateIntervalInfo.PatternInfo var8 = genPatternInfo(var3, var6);
      ((Map)var4).put(var2, var8);
      if (var5) {
         this.fIntervalPatterns.put(var1, var4);
      }

      return var8;
   }

   private void setIntervalPattern(String var1, String var2, DateIntervalInfo.PatternInfo var3) {
      Map var4 = (Map)this.fIntervalPatterns.get(var1);
      var4.put(var2, var3);
   }

   static DateIntervalInfo.PatternInfo genPatternInfo(String var0, boolean var1) {
      int var2 = splitPatternInto2Part(var0);
      String var3 = var0.substring(0, var2);
      String var4 = null;
      if (var2 < var0.length()) {
         var4 = var0.substring(var2, var0.length());
      }

      return new DateIntervalInfo.PatternInfo(var3, var4, var1);
   }

   public DateIntervalInfo.PatternInfo getIntervalPattern(String var1, int var2) {
      if (var2 > 12) {
         throw new IllegalArgumentException("no support for field less than MINUTE");
      } else {
         Map var3 = (Map)this.fIntervalPatterns.get(var1);
         if (var3 != null) {
            DateIntervalInfo.PatternInfo var4 = (DateIntervalInfo.PatternInfo)var3.get(CALENDAR_FIELD_TO_PATTERN_LETTER[var2]);
            if (var4 != null) {
               return var4;
            }
         }

         return null;
      }
   }

   public String getFallbackIntervalPattern() {
      return this.fFallbackIntervalPattern;
   }

   public void setFallbackIntervalPattern(String var1) {
      if (this.frozen) {
         throw new UnsupportedOperationException("no modification is allowed after DII is frozen");
      } else {
         int var2 = var1.indexOf("{0}");
         int var3 = var1.indexOf("{1}");
         if (var2 != -1 && var3 != -1) {
            if (var2 > var3) {
               this.fFirstDateInPtnIsLaterDate = true;
            }

            this.fFallbackIntervalPattern = var1;
         } else {
            throw new IllegalArgumentException("no pattern {0} or pattern {1} in fallbackPattern");
         }
      }
   }

   public boolean getDefaultOrder() {
      return this.fFirstDateInPtnIsLaterDate;
   }

   public Object clone() {
      return this.frozen ? this : this.cloneUnfrozenDII();
   }

   private Object cloneUnfrozenDII() {
      try {
         DateIntervalInfo var1 = (DateIntervalInfo)super.clone();
         var1.fFallbackIntervalPattern = this.fFallbackIntervalPattern;
         var1.fFirstDateInPtnIsLaterDate = this.fFirstDateInPtnIsLaterDate;
         if (this.fIntervalPatternsReadOnly) {
            var1.fIntervalPatterns = this.fIntervalPatterns;
            var1.fIntervalPatternsReadOnly = true;
         } else {
            var1.fIntervalPatterns = cloneIntervalPatterns(this.fIntervalPatterns);
            var1.fIntervalPatternsReadOnly = false;
         }

         var1.frozen = false;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException("clone is not supported");
      }
   }

   private static Map cloneIntervalPatterns(Map var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = var0.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = (String)var3.getKey();
         Map var5 = (Map)var3.getValue();
         HashMap var6 = new HashMap();
         Iterator var7 = var5.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            String var9 = (String)var8.getKey();
            DateIntervalInfo.PatternInfo var10 = (DateIntervalInfo.PatternInfo)var8.getValue();
            var6.put(var9, var10);
         }

         var1.put(var4, var6);
      }

      return var1;
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   public DateIntervalInfo freeze() {
      this.frozen = true;
      this.fIntervalPatternsReadOnly = true;
      return this;
   }

   public DateIntervalInfo cloneAsThawed() {
      DateIntervalInfo var1 = (DateIntervalInfo)((DateIntervalInfo)this.cloneUnfrozenDII());
      return var1;
   }

   static void parseSkeleton(String var0, int[] var1) {
      byte var2 = 65;

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         ++var1[var0.charAt(var3) - var2];
      }

   }

   DateIntervalFormat.BestMatchInfo getBestSkeleton(String var1) {
      String var2 = var1;
      int[] var3 = new int[58];
      int[] var4 = new int[58];
      boolean var5 = true;
      boolean var6 = true;
      boolean var7 = true;
      boolean var8 = false;
      if (var1.indexOf(122) != -1) {
         var1 = var1.replace('z', 'v');
         var8 = true;
      }

      parseSkeleton(var1, var3);
      int var9 = Integer.MAX_VALUE;
      byte var10 = 0;
      Iterator var11 = this.fIntervalPatterns.keySet().iterator();

      while(var11.hasNext()) {
         String var12 = (String)var11.next();

         int var13;
         for(var13 = 0; var13 < var4.length; ++var13) {
            var4[var13] = 0;
         }

         parseSkeleton(var12, var4);
         var13 = 0;
         byte var14 = 1;

         for(int var15 = 0; var15 < var3.length; ++var15) {
            int var16 = var3[var15];
            int var17 = var4[var15];
            if (var16 != var17) {
               if (var16 == 0) {
                  var14 = -1;
                  var13 += 4096;
               } else if (var17 == 0) {
                  var14 = -1;
                  var13 += 4096;
               } else if (var17 == (char)(var15 + 65)) {
                  var13 += 256;
               } else {
                  var13 += Math.abs(var16 - var17);
               }
            }
         }

         if (var13 < var9) {
            var2 = var12;
            var9 = var13;
            var10 = var14;
         }

         if (var13 == 0) {
            var10 = 0;
            break;
         }
      }

      if (var8 && var10 != -1) {
         var10 = 2;
      }

      return new DateIntervalFormat.BestMatchInfo(var2, var10);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof DateIntervalInfo) {
         DateIntervalInfo var2 = (DateIntervalInfo)var1;
         return this.fIntervalPatterns.equals(var2.fIntervalPatterns);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.fIntervalPatterns.hashCode();
   }

   /** @deprecated */
   public Map getPatterns() {
      LinkedHashMap var1 = new LinkedHashMap();
      Iterator var2 = this.fIntervalPatterns.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getKey(), new LinkedHashSet(((Map)var3.getValue()).keySet()));
      }

      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   public static final class PatternInfo implements Cloneable, Serializable {
      static final int currentSerialVersion = 1;
      private static final long serialVersionUID = 1L;
      private final String fIntervalPatternFirstPart;
      private final String fIntervalPatternSecondPart;
      private final boolean fFirstDateInPtnIsLaterDate;

      public PatternInfo(String var1, String var2, boolean var3) {
         this.fIntervalPatternFirstPart = var1;
         this.fIntervalPatternSecondPart = var2;
         this.fFirstDateInPtnIsLaterDate = var3;
      }

      public String getFirstPart() {
         return this.fIntervalPatternFirstPart;
      }

      public String getSecondPart() {
         return this.fIntervalPatternSecondPart;
      }

      public boolean firstDateInPtnIsLaterDate() {
         return this.fFirstDateInPtnIsLaterDate;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof DateIntervalInfo.PatternInfo)) {
            return false;
         } else {
            DateIntervalInfo.PatternInfo var2 = (DateIntervalInfo.PatternInfo)var1;
            return Utility.objectEquals(this.fIntervalPatternFirstPart, var2.fIntervalPatternFirstPart) && Utility.objectEquals(this.fIntervalPatternSecondPart, this.fIntervalPatternSecondPart) && this.fFirstDateInPtnIsLaterDate == var2.fFirstDateInPtnIsLaterDate;
         }
      }

      public int hashCode() {
         int var1 = this.fIntervalPatternFirstPart != null ? this.fIntervalPatternFirstPart.hashCode() : 0;
         if (this.fIntervalPatternSecondPart != null) {
            var1 ^= this.fIntervalPatternSecondPart.hashCode();
         }

         if (this.fFirstDateInPtnIsLaterDate) {
            var1 = ~var1;
         }

         return var1;
      }
   }
}
