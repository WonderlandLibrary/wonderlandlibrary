package com.ibm.icu.impl;

import com.ibm.icu.util.AnnualTimeZoneRule;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.DateTimeRule;
import com.ibm.icu.util.InitialTimeZoneRule;
import com.ibm.icu.util.SimpleTimeZone;
import com.ibm.icu.util.TimeArrayTimeZoneRule;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneRule;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.MissingResourceException;

public class OlsonTimeZone extends BasicTimeZone {
   static final long serialVersionUID = -6281977362477515376L;
   private int transitionCount;
   private int typeCount;
   private long[] transitionTimes64;
   private int[] typeOffsets;
   private byte[] typeMapData;
   private int finalStartYear = Integer.MAX_VALUE;
   private double finalStartMillis = Double.MAX_VALUE;
   private SimpleTimeZone finalZone = null;
   private volatile String canonicalID = null;
   private static final String ZONEINFORES = "zoneinfo64";
   private static final boolean DEBUG = ICUDebug.enabled("olson");
   private static final int SECONDS_PER_DAY = 86400;
   private transient InitialTimeZoneRule initialRule;
   private transient TimeZoneTransition firstTZTransition;
   private transient int firstTZTransitionIdx;
   private transient TimeZoneTransition firstFinalTZTransition;
   private transient TimeArrayTimeZoneRule[] historicRules;
   private transient SimpleTimeZone finalZoneWithStartYear;
   private transient boolean transitionRulesInitialized;
   private static final int currentSerialVersion = 1;
   private int serialVersionOnStream = 1;
   private transient boolean isFrozen = false;
   static final boolean $assertionsDisabled = !OlsonTimeZone.class.desiredAssertionStatus();

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var3 >= 0 && var3 <= 11) {
         return this.getOffset(var1, var2, var3, var4, var5, var6, Grego.monthLength(var2, var3));
      } else {
         throw new IllegalArgumentException("Month is not in the legal range: " + var3);
      }
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if ((var1 == 1 || var1 == 0) && var3 >= 0 && var3 <= 11 && var4 >= 1 && var4 <= var7 && var5 >= 1 && var5 <= 7 && var6 >= 0 && var6 < 86400000 && var7 >= 28 && var7 <= 31) {
         if (var1 == 0) {
            var2 = -var2;
         }

         if (this.finalZone != null && var2 >= this.finalStartYear) {
            return this.finalZone.getOffset(var1, var2, var3, var4, var5, var6);
         } else {
            long var8 = Grego.fieldsToDay(var2, var3, var4) * 86400000L + (long)var6;
            int[] var10 = new int[2];
            this.getHistoricalOffset(var8, true, 3, 1, var10);
            return var10[0] + var10[1];
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void setRawOffset(int var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
      } else if (this.getRawOffset() != var1) {
         long var2 = System.currentTimeMillis();
         if ((double)var2 < this.finalStartMillis) {
            SimpleTimeZone var4 = new SimpleTimeZone(var1, this.getID());
            boolean var5 = this.useDaylightTime();
            if (var5) {
               TimeZoneRule[] var6 = this.getSimpleTimeZoneRulesNear(var2);
               if (var6.length != 3) {
                  TimeZoneTransition var7 = this.getPreviousTransition(var2, false);
                  if (var7 != null) {
                     var6 = this.getSimpleTimeZoneRulesNear(var7.getTime() - 1L);
                  }
               }

               if (var6.length == 3 && var6[1] instanceof AnnualTimeZoneRule && var6[2] instanceof AnnualTimeZoneRule) {
                  AnnualTimeZoneRule var15 = (AnnualTimeZoneRule)var6[1];
                  AnnualTimeZoneRule var8 = (AnnualTimeZoneRule)var6[2];
                  int var11 = var15.getRawOffset() + var15.getDSTSavings();
                  int var12 = var8.getRawOffset() + var8.getDSTSavings();
                  DateTimeRule var9;
                  DateTimeRule var10;
                  int var13;
                  if (var11 > var12) {
                     var9 = var15.getRule();
                     var10 = var8.getRule();
                     var13 = var11 - var12;
                  } else {
                     var9 = var8.getRule();
                     var10 = var15.getRule();
                     var13 = var12 - var11;
                  }

                  var4.setStartRule(var9.getRuleMonth(), var9.getRuleWeekInMonth(), var9.getRuleDayOfWeek(), var9.getRuleMillisInDay());
                  var4.setEndRule(var10.getRuleMonth(), var10.getRuleWeekInMonth(), var10.getRuleDayOfWeek(), var10.getRuleMillisInDay());
                  var4.setDSTSavings(var13);
               } else {
                  var4.setStartRule(0, 1, 0);
                  var4.setEndRule(11, 31, 86399999);
               }
            }

            int[] var14 = Grego.timeToFields(var2, (int[])null);
            this.finalStartYear = var14[0];
            this.finalStartMillis = (double)Grego.fieldsToDay(var14[0], 0, 1);
            if (var5) {
               var4.setStartYear(this.finalStartYear);
            }

            this.finalZone = var4;
         } else {
            this.finalZone.setRawOffset(var1);
         }

         this.transitionRulesInitialized = false;
      }
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   public void getOffset(long var1, boolean var3, int[] var4) {
      if (this.finalZone != null && (double)var1 >= this.finalStartMillis) {
         this.finalZone.getOffset(var1, var3, var4);
      } else {
         this.getHistoricalOffset(var1, var3, 4, 12, var4);
      }

   }

   /** @deprecated */
   public void getOffsetFromLocal(long var1, int var3, int var4, int[] var5) {
      if (this.finalZone != null && (double)var1 >= this.finalStartMillis) {
         this.finalZone.getOffsetFromLocal(var1, var3, var4, var5);
      } else {
         this.getHistoricalOffset(var1, true, var3, var4, var5);
      }

   }

   public int getRawOffset() {
      int[] var1 = new int[2];
      this.getOffset(System.currentTimeMillis(), false, var1);
      return var1[0];
   }

   public boolean useDaylightTime() {
      long var1 = System.currentTimeMillis();
      if (this.finalZone != null && (double)var1 >= this.finalStartMillis) {
         return this.finalZone != null && this.finalZone.useDaylightTime();
      } else {
         int[] var3 = Grego.timeToFields(var1, (int[])null);
         long var4 = Grego.fieldsToDay(var3[0], 0, 1) * 86400L;
         long var6 = Grego.fieldsToDay(var3[0] + 1, 0, 1) * 86400L;
         int var8 = 0;

         while(true) {
            if (var8 < this.transitionCount && this.transitionTimes64[var8] < var6) {
               if ((this.transitionTimes64[var8] < var4 || this.dstOffsetAt(var8) == 0) && (this.transitionTimes64[var8] <= var4 || var8 <= 0 || this.dstOffsetAt(var8 - 1) == 0)) {
                  ++var8;
                  continue;
               }

               return true;
            }

            return false;
         }
      }
   }

   public boolean observesDaylightTime() {
      long var1 = System.currentTimeMillis();
      if (this.finalZone != null) {
         if (this.finalZone.useDaylightTime()) {
            return true;
         }

         if ((double)var1 >= this.finalStartMillis) {
            return false;
         }
      }

      long var3 = Grego.floorDivide(var1, 1000L);
      int var5 = this.transitionCount - 1;
      if (this.dstOffsetAt(var5) != 0) {
         return true;
      } else {
         while(var5 >= 0 && this.transitionTimes64[var5] > var3) {
            if (this.dstOffsetAt(var5 - 1) != 0) {
               return true;
            }
         }

         return false;
      }
   }

   public int getDSTSavings() {
      return this.finalZone != null ? this.finalZone.getDSTSavings() : super.getDSTSavings();
   }

   public boolean inDaylightTime(Date var1) {
      int[] var2 = new int[2];
      this.getOffset(var1.getTime(), false, var2);
      return var2[1] != 0;
   }

   public boolean hasSameRules(TimeZone var1) {
      if (this == var1) {
         return true;
      } else if (!super.hasSameRules(var1)) {
         return false;
      } else if (!(var1 instanceof OlsonTimeZone)) {
         return false;
      } else {
         OlsonTimeZone var2 = (OlsonTimeZone)var1;
         if (this.finalZone == null) {
            if (var2.finalZone != null) {
               return false;
            }
         } else if (var2.finalZone == null || this.finalStartYear != var2.finalStartYear || !this.finalZone.hasSameRules(var2.finalZone)) {
            return false;
         }

         if (this.transitionCount == var2.transitionCount && Arrays.equals(this.transitionTimes64, var2.transitionTimes64) && this.typeCount == var2.typeCount && Arrays.equals(this.typeMapData, var2.typeMapData) && Arrays.equals(this.typeOffsets, var2.typeOffsets)) {
            return true;
         } else {
            return false;
         }
      }
   }

   public String getCanonicalID() {
      if (this.canonicalID == null) {
         synchronized(this){}
         if (this.canonicalID == null) {
            this.canonicalID = getCanonicalID(this.getID());
            if (!$assertionsDisabled && this.canonicalID == null) {
               throw new AssertionError();
            }

            if (this.canonicalID == null) {
               this.canonicalID = this.getID();
            }
         }
      }

      return this.canonicalID;
   }

   private void constructEmpty() {
      this.transitionCount = 0;
      this.transitionTimes64 = null;
      this.typeMapData = null;
      this.typeCount = 1;
      this.typeOffsets = new int[]{0, 0};
      this.finalZone = null;
      this.finalStartYear = Integer.MAX_VALUE;
      this.finalStartMillis = Double.MAX_VALUE;
      this.transitionRulesInitialized = false;
   }

   public OlsonTimeZone(UResourceBundle var1, UResourceBundle var2, String var3) {
      super(var3);
      this.construct(var1, var2);
   }

   private void construct(UResourceBundle var1, UResourceBundle var2) {
      if (var1 != null && var2 != null) {
         if (DEBUG) {
            System.out.println("OlsonTimeZone(" + var2.getKey() + ")");
         }

         int[] var6 = null;
         int[] var5 = null;
         int[] var4 = null;
         this.transitionCount = 0;

         UResourceBundle var3;
         try {
            var3 = var2.get("transPre32");
            var4 = var3.getIntVector();
            if (var4.length % 2 != 0) {
               throw new IllegalArgumentException("Invalid Format");
            }

            this.transitionCount += var4.length / 2;
         } catch (MissingResourceException var12) {
         }

         try {
            var3 = var2.get("trans");
            var5 = var3.getIntVector();
            this.transitionCount += var5.length;
         } catch (MissingResourceException var11) {
         }

         try {
            var3 = var2.get("transPost32");
            var6 = var3.getIntVector();
            if (var6.length % 2 != 0) {
               throw new IllegalArgumentException("Invalid Format");
            }

            this.transitionCount += var6.length / 2;
         } catch (MissingResourceException var10) {
         }

         int var8;
         if (this.transitionCount > 0) {
            this.transitionTimes64 = new long[this.transitionCount];
            int var7 = 0;
            if (var4 != null) {
               for(var8 = 0; var8 < var4.length / 2; ++var7) {
                  this.transitionTimes64[var7] = ((long)var4[var8 * 2] & 4294967295L) << 32 | (long)var4[var8 * 2 + 1] & 4294967295L;
                  ++var8;
               }
            }

            if (var5 != null) {
               for(var8 = 0; var8 < var5.length; ++var7) {
                  this.transitionTimes64[var7] = (long)var5[var8];
                  ++var8;
               }
            }

            if (var6 != null) {
               for(var8 = 0; var8 < var6.length / 2; ++var7) {
                  this.transitionTimes64[var7] = ((long)var6[var8 * 2] & 4294967295L) << 32 | (long)var6[var8 * 2 + 1] & 4294967295L;
                  ++var8;
               }
            }
         } else {
            this.transitionTimes64 = null;
         }

         var3 = var2.get("typeOffsets");
         this.typeOffsets = var3.getIntVector();
         if (this.typeOffsets.length >= 2 && this.typeOffsets.length <= 32766 && this.typeOffsets.length % 2 == 0) {
            this.typeCount = this.typeOffsets.length / 2;
            if (this.transitionCount > 0) {
               var3 = var2.get("typeMap");
               this.typeMapData = var3.getBinary((byte[])null);
               if (this.typeMapData.length != this.transitionCount) {
                  throw new IllegalArgumentException("Invalid Format");
               }
            } else {
               this.typeMapData = null;
            }

            this.finalZone = null;
            this.finalStartYear = Integer.MAX_VALUE;
            this.finalStartMillis = Double.MAX_VALUE;
            String var14 = null;

            try {
               var14 = var2.getString("finalRule");
               var3 = var2.get("finalRaw");
               var8 = var3.getInt() * 1000;
               var3 = loadRule(var1, var14);
               int[] var9 = var3.getIntVector();
               if (var9 == null || var9.length != 11) {
                  throw new IllegalArgumentException("Invalid Format");
               }

               this.finalZone = new SimpleTimeZone(var8, "", var9[0], var9[1], var9[2], var9[3] * 1000, var9[4], var9[5], var9[6], var9[7], var9[8] * 1000, var9[9], var9[10] * 1000);
               var3 = var2.get("finalYear");
               this.finalStartYear = var3.getInt();
               this.finalStartMillis = (double)(Grego.fieldsToDay(this.finalStartYear, 0, 1) * 86400000L);
            } catch (MissingResourceException var13) {
               if (var14 != null) {
                  throw new IllegalArgumentException("Invalid Format");
               }
            }

         } else {
            throw new IllegalArgumentException("Invalid Format");
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public OlsonTimeZone(String var1) {
      super(var1);
      UResourceBundle var2 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
      UResourceBundle var3 = ZoneMeta.openOlsonResource(var2, var1);
      this.construct(var2, var3);
      if (this.finalZone != null) {
         this.finalZone.setID(var1);
      }

   }

   public void setID(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen OlsonTimeZone instance.");
      } else {
         if (this.canonicalID == null) {
            this.canonicalID = getCanonicalID(this.getID());
            if (!$assertionsDisabled && this.canonicalID == null) {
               throw new AssertionError();
            }

            if (this.canonicalID == null) {
               this.canonicalID = this.getID();
            }
         }

         if (this.finalZone != null) {
            this.finalZone.setID(var1);
         }

         super.setID(var1);
         this.transitionRulesInitialized = false;
      }
   }

   private void getHistoricalOffset(long var1, boolean var3, int var4, int var5, int[] var6) {
      if (this.transitionCount != 0) {
         long var7 = Grego.floorDivide(var1, 1000L);
         if (!var3 && var7 < this.transitionTimes64[0]) {
            var6[0] = this.initialRawOffset() * 1000;
            var6[1] = this.initialDstOffset() * 1000;
         } else {
            int var9;
            for(var9 = this.transitionCount - 1; var9 >= 0; --var9) {
               long var10 = this.transitionTimes64[var9];
               if (var3) {
                  int var12 = this.zoneOffsetAt(var9 - 1);
                  boolean var13 = this.dstOffsetAt(var9 - 1) != 0;
                  int var14 = this.zoneOffsetAt(var9);
                  boolean var15 = this.dstOffsetAt(var9) != 0;
                  boolean var16 = var13 && !var15;
                  boolean var17 = !var13 && var15;
                  if (var14 - var12 >= 0) {
                     if ((var4 & 3) == 1 && var16 || (var4 & 3) == 3 && var17) {
                        var10 += (long)var12;
                     } else if (((var4 & 3) != 1 || !var17) && ((var4 & 3) != 3 || !var16)) {
                        if ((var4 & 12) == 12) {
                           var10 += (long)var12;
                        } else {
                           var10 += (long)var14;
                        }
                     } else {
                        var10 += (long)var14;
                     }
                  } else if ((var5 & 3) == 1 && var16 || (var5 & 3) == 3 && var17) {
                     var10 += (long)var14;
                  } else if ((var5 & 3) == 1 && var17 || (var5 & 3) == 3 && var16) {
                     var10 += (long)var12;
                  } else if ((var5 & 12) == 4) {
                     var10 += (long)var12;
                  } else {
                     var10 += (long)var14;
                  }
               }

               if (var7 >= var10) {
                  break;
               }
            }

            var6[0] = this.rawOffsetAt(var9) * 1000;
            var6[1] = this.dstOffsetAt(var9) * 1000;
         }
      } else {
         var6[0] = this.initialRawOffset() * 1000;
         var6[1] = this.initialDstOffset() * 1000;
      }

   }

   private int getInt(byte var1) {
      return var1 & 255;
   }

   private int zoneOffsetAt(int var1) {
      int var2 = var1 >= 0 ? this.getInt(this.typeMapData[var1]) * 2 : 0;
      return this.typeOffsets[var2] + this.typeOffsets[var2 + 1];
   }

   private int rawOffsetAt(int var1) {
      int var2 = var1 >= 0 ? this.getInt(this.typeMapData[var1]) * 2 : 0;
      return this.typeOffsets[var2];
   }

   private int dstOffsetAt(int var1) {
      int var2 = var1 >= 0 ? this.getInt(this.typeMapData[var1]) * 2 : 0;
      return this.typeOffsets[var2 + 1];
   }

   private int initialRawOffset() {
      return this.typeOffsets[0];
   }

   private int initialDstOffset() {
      return this.typeOffsets[1];
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append('[');
      var1.append("transitionCount=" + this.transitionCount);
      var1.append(",typeCount=" + this.typeCount);
      var1.append(",transitionTimes=");
      int var2;
      if (this.transitionTimes64 != null) {
         var1.append('[');

         for(var2 = 0; var2 < this.transitionTimes64.length; ++var2) {
            if (var2 > 0) {
               var1.append(',');
            }

            var1.append(Long.toString(this.transitionTimes64[var2]));
         }

         var1.append(']');
      } else {
         var1.append("null");
      }

      var1.append(",typeOffsets=");
      if (this.typeOffsets != null) {
         var1.append('[');

         for(var2 = 0; var2 < this.typeOffsets.length; ++var2) {
            if (var2 > 0) {
               var1.append(',');
            }

            var1.append(Integer.toString(this.typeOffsets[var2]));
         }

         var1.append(']');
      } else {
         var1.append("null");
      }

      var1.append(",typeMapData=");
      if (this.typeMapData != null) {
         var1.append('[');

         for(var2 = 0; var2 < this.typeMapData.length; ++var2) {
            if (var2 > 0) {
               var1.append(',');
            }

            var1.append(Byte.toString(this.typeMapData[var2]));
         }
      } else {
         var1.append("null");
      }

      var1.append(",finalStartYear=" + this.finalStartYear);
      var1.append(",finalStartMillis=" + this.finalStartMillis);
      var1.append(",finalZone=" + this.finalZone);
      var1.append(']');
      return var1.toString();
   }

   private static UResourceBundle loadRule(UResourceBundle var0, String var1) {
      UResourceBundle var2 = var0.get("Rules");
      var2 = var2.get(var1);
      return var2;
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         OlsonTimeZone var2 = (OlsonTimeZone)var1;
         return Utility.arrayEquals((byte[])this.typeMapData, var2.typeMapData) || this.finalStartYear == var2.finalStartYear && (this.finalZone == null && var2.finalZone == null || this.finalZone != null && var2.finalZone != null && this.finalZone.equals(var2.finalZone) && this.transitionCount == var2.transitionCount && this.typeCount == var2.typeCount && Utility.arrayEquals((Object)this.transitionTimes64, var2.transitionTimes64) && Utility.arrayEquals((int[])this.typeOffsets, var2.typeOffsets) && Utility.arrayEquals((byte[])this.typeMapData, var2.typeMapData));
      }
   }

   public int hashCode() {
      int var1 = (int)((long)(this.finalStartYear ^ (this.finalStartYear >>> 4) + this.transitionCount ^ (this.transitionCount >>> 6) + this.typeCount) ^ (long)(this.typeCount >>> 8) + Double.doubleToLongBits(this.finalStartMillis) + (long)(this.finalZone == null ? 0 : this.finalZone.hashCode()) + (long)super.hashCode());
      int var2;
      if (this.transitionTimes64 != null) {
         for(var2 = 0; var2 < this.transitionTimes64.length; ++var2) {
            var1 = (int)((long)var1 + (this.transitionTimes64[var2] ^ this.transitionTimes64[var2] >>> 8));
         }
      }

      for(var2 = 0; var2 < this.typeOffsets.length; ++var2) {
         var1 += this.typeOffsets[var2] ^ this.typeOffsets[var2] >>> 8;
      }

      if (this.typeMapData != null) {
         for(var2 = 0; var2 < this.typeMapData.length; ++var2) {
            var1 += this.typeMapData[var2] & 255;
         }
      }

      return var1;
   }

   public TimeZoneTransition getNextTransition(long var1, boolean var3) {
      this.initTransitionRules();
      if (this.finalZone != null) {
         if (var3 && var1 == this.firstFinalTZTransition.getTime()) {
            return this.firstFinalTZTransition;
         }

         if (var1 >= this.firstFinalTZTransition.getTime()) {
            if (this.finalZone.useDaylightTime()) {
               return this.finalZoneWithStartYear.getNextTransition(var1, var3);
            }

            return null;
         }
      }

      if (this.historicRules == null) {
         return null;
      } else {
         int var4;
         for(var4 = this.transitionCount - 1; var4 >= this.firstTZTransitionIdx; --var4) {
            long var5 = this.transitionTimes64[var4] * 1000L;
            if (var1 > var5 || !var3 && var1 == var5) {
               break;
            }
         }

         if (var4 == this.transitionCount - 1) {
            return this.firstFinalTZTransition;
         } else if (var4 < this.firstTZTransitionIdx) {
            return this.firstTZTransition;
         } else {
            TimeArrayTimeZoneRule var9 = this.historicRules[this.getInt(this.typeMapData[var4 + 1])];
            TimeArrayTimeZoneRule var6 = this.historicRules[this.getInt(this.typeMapData[var4])];
            long var7 = this.transitionTimes64[var4 + 1] * 1000L;
            return var6.getName().equals(var9.getName()) && var6.getRawOffset() == var9.getRawOffset() && var6.getDSTSavings() == var9.getDSTSavings() ? this.getNextTransition(var7, false) : new TimeZoneTransition(var7, var6, var9);
         }
      }
   }

   public TimeZoneTransition getPreviousTransition(long var1, boolean var3) {
      this.initTransitionRules();
      if (this.finalZone != null) {
         if (var3 && var1 == this.firstFinalTZTransition.getTime()) {
            return this.firstFinalTZTransition;
         }

         if (var1 > this.firstFinalTZTransition.getTime()) {
            if (this.finalZone.useDaylightTime()) {
               return this.finalZoneWithStartYear.getPreviousTransition(var1, var3);
            }

            return this.firstFinalTZTransition;
         }
      }

      if (this.historicRules == null) {
         return null;
      } else {
         int var4;
         for(var4 = this.transitionCount - 1; var4 >= this.firstTZTransitionIdx; --var4) {
            long var5 = this.transitionTimes64[var4] * 1000L;
            if (var1 > var5 || var3 && var1 == var5) {
               break;
            }
         }

         if (var4 < this.firstTZTransitionIdx) {
            return null;
         } else if (var4 == this.firstTZTransitionIdx) {
            return this.firstTZTransition;
         } else {
            TimeArrayTimeZoneRule var9 = this.historicRules[this.getInt(this.typeMapData[var4])];
            TimeArrayTimeZoneRule var6 = this.historicRules[this.getInt(this.typeMapData[var4 - 1])];
            long var7 = this.transitionTimes64[var4] * 1000L;
            return var6.getName().equals(var9.getName()) && var6.getRawOffset() == var9.getRawOffset() && var6.getDSTSavings() == var9.getDSTSavings() ? this.getPreviousTransition(var7, false) : new TimeZoneTransition(var7, var6, var9);
         }
      }
   }

   public TimeZoneRule[] getTimeZoneRules() {
      this.initTransitionRules();
      int var1 = 1;
      if (this.historicRules != null) {
         for(int var2 = 0; var2 < this.historicRules.length; ++var2) {
            if (this.historicRules[var2] != null) {
               ++var1;
            }
         }
      }

      if (this.finalZone != null) {
         if (this.finalZone.useDaylightTime()) {
            var1 += 2;
         } else {
            ++var1;
         }
      }

      TimeZoneRule[] var5 = new TimeZoneRule[var1];
      byte var3 = 0;
      int var6 = var3 + 1;
      var5[var3] = this.initialRule;
      if (this.historicRules != null) {
         for(int var4 = 0; var4 < this.historicRules.length; ++var4) {
            if (this.historicRules[var4] != null) {
               var5[var6++] = this.historicRules[var4];
            }
         }
      }

      if (this.finalZone != null) {
         if (this.finalZone.useDaylightTime()) {
            TimeZoneRule[] var7 = this.finalZoneWithStartYear.getTimeZoneRules();
            var5[var6++] = var7[1];
            var5[var6++] = var7[2];
         } else {
            var5[var6++] = new TimeArrayTimeZoneRule(this.getID() + "(STD)", this.finalZone.getRawOffset(), 0, new long[]{(long)this.finalStartMillis}, 2);
         }
      }

      return var5;
   }

   private synchronized void initTransitionRules() {
      if (!this.transitionRulesInitialized) {
         this.initialRule = null;
         this.firstTZTransition = null;
         this.firstFinalTZTransition = null;
         this.historicRules = null;
         this.firstTZTransitionIdx = 0;
         this.finalZoneWithStartYear = null;
         String var1 = this.getID() + "(STD)";
         String var2 = this.getID() + "(DST)";
         int var3 = this.initialRawOffset() * 1000;
         int var4 = this.initialDstOffset() * 1000;
         this.initialRule = new InitialTimeZoneRule(var4 == 0 ? var1 : var2, var3, var4);
         if (this.transitionCount > 0) {
            int var5;
            for(var5 = 0; var5 < this.transitionCount && this.getInt(this.typeMapData[var5]) == 0; ++var5) {
               ++this.firstTZTransitionIdx;
            }

            if (var5 != this.transitionCount) {
               long[] var7 = new long[this.transitionCount];

               int var6;
               for(var6 = 0; var6 < this.typeCount; ++var6) {
                  int var8 = 0;

                  for(var5 = this.firstTZTransitionIdx; var5 < this.transitionCount; ++var5) {
                     if (var6 == this.getInt(this.typeMapData[var5])) {
                        long var9 = this.transitionTimes64[var5] * 1000L;
                        if ((double)var9 < this.finalStartMillis) {
                           var7[var8++] = var9;
                        }
                     }
                  }

                  if (var8 > 0) {
                     long[] var15 = new long[var8];
                     System.arraycopy(var7, 0, var15, 0, var8);
                     var3 = this.typeOffsets[var6 * 2] * 1000;
                     var4 = this.typeOffsets[var6 * 2 + 1] * 1000;
                     if (this.historicRules == null) {
                        this.historicRules = new TimeArrayTimeZoneRule[this.typeCount];
                     }

                     this.historicRules[var6] = new TimeArrayTimeZoneRule(var4 == 0 ? var1 : var2, var3, var4, var15, 2);
                  }
               }

               var6 = this.getInt(this.typeMapData[this.firstTZTransitionIdx]);
               this.firstTZTransition = new TimeZoneTransition(this.transitionTimes64[this.firstTZTransitionIdx] * 1000L, this.initialRule, this.historicRules[var6]);
            }
         }

         if (this.finalZone != null) {
            long var11 = (long)this.finalStartMillis;
            Object var12;
            if (this.finalZone.useDaylightTime()) {
               this.finalZoneWithStartYear = (SimpleTimeZone)this.finalZone.clone();
               this.finalZoneWithStartYear.setStartYear(this.finalStartYear);
               TimeZoneTransition var13 = this.finalZoneWithStartYear.getNextTransition(var11, false);
               var12 = var13.getTo();
               var11 = var13.getTime();
            } else {
               this.finalZoneWithStartYear = this.finalZone;
               var12 = new TimeArrayTimeZoneRule(this.finalZone.getID(), this.finalZone.getRawOffset(), 0, new long[]{var11}, 2);
            }

            Object var14 = null;
            if (this.transitionCount > 0) {
               var14 = this.historicRules[this.getInt(this.typeMapData[this.transitionCount - 1])];
            }

            if (var14 == null) {
               var14 = this.initialRule;
            }

            this.firstFinalTZTransition = new TimeZoneTransition(var11, (TimeZoneRule)var14, (TimeZoneRule)var12);
         }

         this.transitionRulesInitialized = true;
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.serialVersionOnStream < 1) {
         boolean var2 = false;
         String var3 = this.getID();
         if (var3 != null) {
            try {
               UResourceBundle var4 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "zoneinfo64", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
               UResourceBundle var5 = ZoneMeta.openOlsonResource(var4, var3);
               this.construct(var4, var5);
               if (this.finalZone != null) {
                  this.finalZone.setID(var3);
               }

               var2 = true;
            } catch (Exception var6) {
            }
         }

         if (!var2) {
            this.constructEmpty();
         }
      }

      this.transitionRulesInitialized = false;
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public TimeZone freeze() {
      this.isFrozen = true;
      return this;
   }

   public TimeZone cloneAsThawed() {
      OlsonTimeZone var1 = (OlsonTimeZone)super.cloneAsThawed();
      if (this.finalZone != null) {
         this.finalZone.setID(this.getID());
         var1.finalZone = (SimpleTimeZone)this.finalZone.clone();
      }

      var1.isFrozen = false;
      return var1;
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }
}
