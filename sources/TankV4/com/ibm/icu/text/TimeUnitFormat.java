package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.util.TimeUnit;
import com.ibm.icu.util.TimeUnitAmount;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class TimeUnitFormat extends MeasureFormat {
   public static final int FULL_NAME = 0;
   public static final int ABBREVIATED_NAME = 1;
   private static final int TOTAL_STYLES = 2;
   private static final long serialVersionUID = -3707773153184971529L;
   private static final String DEFAULT_PATTERN_FOR_SECOND = "{0} s";
   private static final String DEFAULT_PATTERN_FOR_MINUTE = "{0} min";
   private static final String DEFAULT_PATTERN_FOR_HOUR = "{0} h";
   private static final String DEFAULT_PATTERN_FOR_DAY = "{0} d";
   private static final String DEFAULT_PATTERN_FOR_WEEK = "{0} w";
   private static final String DEFAULT_PATTERN_FOR_MONTH = "{0} m";
   private static final String DEFAULT_PATTERN_FOR_YEAR = "{0} y";
   private NumberFormat format;
   private ULocale locale;
   private transient Map timeUnitToCountToPatterns;
   private transient PluralRules pluralRules;
   private transient boolean isReady;
   private int style;

   public TimeUnitFormat() {
      this.isReady = false;
      this.style = 0;
   }

   public TimeUnitFormat(ULocale var1) {
      this((ULocale)var1, 0);
   }

   public TimeUnitFormat(Locale var1) {
      this((Locale)var1, 0);
   }

   public TimeUnitFormat(ULocale var1, int var2) {
      if (var2 >= 0 && var2 < 2) {
         this.style = var2;
         this.locale = var1;
         this.isReady = false;
      } else {
         throw new IllegalArgumentException("style should be either FULL_NAME or ABBREVIATED_NAME style");
      }
   }

   public TimeUnitFormat(Locale var1, int var2) {
      this(ULocale.forLocale(var1), var2);
   }

   public TimeUnitFormat setLocale(ULocale var1) {
      if (var1 != this.locale) {
         this.locale = var1;
         this.isReady = false;
      }

      return this;
   }

   public TimeUnitFormat setLocale(Locale var1) {
      return this.setLocale(ULocale.forLocale(var1));
   }

   public TimeUnitFormat setNumberFormat(NumberFormat var1) {
      if (var1 == this.format) {
         return this;
      } else {
         if (var1 == null) {
            if (this.locale == null) {
               this.isReady = false;
               return this;
            }

            this.format = NumberFormat.getNumberInstance(this.locale);
         } else {
            this.format = var1;
         }

         if (!this.isReady) {
            return this;
         } else {
            Iterator var2 = this.timeUnitToCountToPatterns.values().iterator();

            while(var2.hasNext()) {
               Map var3 = (Map)var2.next();
               Iterator var4 = var3.values().iterator();

               while(var4.hasNext()) {
                  Object[] var5 = (Object[])var4.next();
                  MessageFormat var6 = (MessageFormat)var5[0];
                  var6.setFormatByArgumentIndex(0, var1);
                  var6 = (MessageFormat)var5[1];
                  var6.setFormatByArgumentIndex(0, var1);
               }
            }

            return this;
         }
      }
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (!(var1 instanceof TimeUnitAmount)) {
         throw new IllegalArgumentException("can not format non TimeUnitAmount object");
      } else {
         if (!this.isReady) {
            this.setup();
         }

         TimeUnitAmount var4 = (TimeUnitAmount)var1;
         Map var5 = (Map)this.timeUnitToCountToPatterns.get(var4.getTimeUnit());
         double var6 = var4.getNumber().doubleValue();
         String var8 = this.pluralRules.select(var6);
         MessageFormat var9 = (MessageFormat)((Object[])var5.get(var8))[this.style];
         return var9.format(new Object[]{var4.getNumber()}, var2, var3);
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      if (!this.isReady) {
         this.setup();
      }

      Object var3 = null;
      TimeUnit var4 = null;
      int var5 = var2.getIndex();
      int var6 = -1;
      int var7 = 0;
      String var8 = null;
      Iterator var9 = this.timeUnitToCountToPatterns.keySet().iterator();

      while(var9.hasNext()) {
         TimeUnit var10 = (TimeUnit)var9.next();
         Map var11 = (Map)this.timeUnitToCountToPatterns.get(var10);
         Iterator var12 = var11.entrySet().iterator();

         while(var12.hasNext()) {
            Entry var13 = (Entry)var12.next();
            String var14 = (String)var13.getKey();

            for(int var15 = 0; var15 < 2; ++var15) {
               MessageFormat var16 = (MessageFormat)((Object[])var13.getValue())[var15];
               var2.setErrorIndex(-1);
               var2.setIndex(var5);
               Object var17 = var16.parseObject(var1, var2);
               if (var2.getErrorIndex() == -1 && var2.getIndex() != var5) {
                  Number var18 = null;
                  if (((Object[])((Object[])var17)).length != 0) {
                     var18 = (Number)((Object[])((Object[])var17))[0];
                     String var19 = this.pluralRules.select(var18.doubleValue());
                     if (!var14.equals(var19)) {
                        continue;
                     }
                  }

                  int var20 = var2.getIndex() - var5;
                  if (var20 > var7) {
                     var3 = var18;
                     var4 = var10;
                     var6 = var2.getIndex();
                     var7 = var20;
                     var8 = var14;
                  }
               }
            }
         }
      }

      if (var3 == null && var7 != 0) {
         if (var8.equals("zero")) {
            var3 = 0;
         } else if (var8.equals("one")) {
            var3 = 1;
         } else if (var8.equals("two")) {
            var3 = 2;
         } else {
            var3 = 3;
         }
      }

      if (var7 == 0) {
         var2.setIndex(var5);
         var2.setErrorIndex(0);
         return null;
      } else {
         var2.setIndex(var6);
         var2.setErrorIndex(-1);
         return new TimeUnitAmount((Number)var3, var4);
      }
   }

   private void setup() {
      if (this.locale == null) {
         if (this.format != null) {
            this.locale = this.format.getLocale((ULocale.Type)null);
         } else {
            this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
         }
      }

      if (this.format == null) {
         this.format = NumberFormat.getNumberInstance(this.locale);
      }

      this.pluralRules = PluralRules.forLocale(this.locale);
      this.timeUnitToCountToPatterns = new HashMap();
      Set var1 = this.pluralRules.getKeywords();
      this.setup("units", this.timeUnitToCountToPatterns, 0, var1);
      this.setup("unitsShort", this.timeUnitToCountToPatterns, 1, var1);
      this.isReady = true;
   }

   private void setup(String var1, Map var2, int var3, Set var4) {
      int var7;
      try {
         ICUResourceBundle var5 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", this.locale);
         ICUResourceBundle var6 = var5.getWithFallback(var1);
         var7 = var6.getSize();

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6.get(var8).getKey();
            TimeUnit var10 = null;
            if (var9.equals("year")) {
               var10 = TimeUnit.YEAR;
            } else if (var9.equals("month")) {
               var10 = TimeUnit.MONTH;
            } else if (var9.equals("day")) {
               var10 = TimeUnit.DAY;
            } else if (var9.equals("hour")) {
               var10 = TimeUnit.HOUR;
            } else if (var9.equals("minute")) {
               var10 = TimeUnit.MINUTE;
            } else if (var9.equals("second")) {
               var10 = TimeUnit.SECOND;
            } else {
               if (!var9.equals("week")) {
                  continue;
               }

               var10 = TimeUnit.WEEK;
            }

            ICUResourceBundle var11 = var6.getWithFallback(var9);
            int var12 = var11.getSize();
            Object var13 = (Map)var2.get(var10);
            if (var13 == null) {
               var13 = new TreeMap();
               var2.put(var10, var13);
            }

            for(int var14 = 0; var14 < var12; ++var14) {
               String var15 = var11.get(var14).getKey();
               if (var4.contains(var15)) {
                  String var16 = var11.get(var14).getString();
                  MessageFormat var17 = new MessageFormat(var16, this.locale);
                  if (this.format != null) {
                     var17.setFormatByArgumentIndex(0, this.format);
                  }

                  Object[] var18 = (Object[])((Map)var13).get(var15);
                  if (var18 == null) {
                     var18 = new Object[2];
                     ((Map)var13).put(var15, var18);
                  }

                  var18[var3] = var17;
               }
            }
         }
      } catch (MissingResourceException var19) {
      }

      TimeUnit[] var20 = TimeUnit.values();
      Set var21 = this.pluralRules.getKeywords();

      label73:
      for(var7 = 0; var7 < var20.length; ++var7) {
         TimeUnit var22 = var20[var7];
         Object var23 = (Map)var2.get(var22);
         if (var23 == null) {
            var23 = new TreeMap();
            var2.put(var22, var23);
         }

         Iterator var25 = var21.iterator();

         while(true) {
            String var24;
            do {
               if (!var25.hasNext()) {
                  continue label73;
               }

               var24 = (String)var25.next();
            } while(((Map)var23).get(var24) != null && ((Object[])((Map)var23).get(var24))[var3] != null);

            this.searchInTree(var1, var3, var22, var24, var24, (Map)var23);
         }
      }

   }

   private void searchInTree(String var1, int var2, TimeUnit var3, String var4, String var5, Map var6) {
      ULocale var7 = this.locale;
      String var8 = var3.toString();

      while(var7 != null) {
         try {
            ICUResourceBundle var9 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var7);
            var9 = var9.getWithFallback(var1);
            ICUResourceBundle var10 = var9.getWithFallback(var8);
            String var11 = var10.getStringWithFallback(var5);
            MessageFormat var12 = new MessageFormat(var11, this.locale);
            if (this.format != null) {
               var12.setFormatByArgumentIndex(0, this.format);
            }

            Object[] var13 = (Object[])var6.get(var4);
            if (var13 == null) {
               var13 = new Object[2];
               var6.put(var4, var13);
            }

            var13[var2] = var12;
            return;
         } catch (MissingResourceException var14) {
            var7 = var7.getFallback();
         }
      }

      if (var7 == null && var1.equals("unitsShort")) {
         this.searchInTree("units", var2, var3, var4, var5, var6);
         if (var6 != null && var6.get(var4) != null && ((Object[])var6.get(var4))[var2] != null) {
            return;
         }
      }

      if (var5.equals("other")) {
         MessageFormat var16 = null;
         if (var3 == TimeUnit.SECOND) {
            var16 = new MessageFormat("{0} s", this.locale);
         } else if (var3 == TimeUnit.MINUTE) {
            var16 = new MessageFormat("{0} min", this.locale);
         } else if (var3 == TimeUnit.HOUR) {
            var16 = new MessageFormat("{0} h", this.locale);
         } else if (var3 == TimeUnit.WEEK) {
            var16 = new MessageFormat("{0} w", this.locale);
         } else if (var3 == TimeUnit.DAY) {
            var16 = new MessageFormat("{0} d", this.locale);
         } else if (var3 == TimeUnit.MONTH) {
            var16 = new MessageFormat("{0} m", this.locale);
         } else if (var3 == TimeUnit.YEAR) {
            var16 = new MessageFormat("{0} y", this.locale);
         }

         if (this.format != null && var16 != null) {
            var16.setFormatByArgumentIndex(0, this.format);
         }

         Object[] var15 = (Object[])var6.get(var4);
         if (var15 == null) {
            var15 = new Object[2];
            var6.put(var4, var15);
         }

         var15[var2] = var16;
      } else {
         this.searchInTree(var1, var2, var3, var4, "other", var6);
      }

   }
}
