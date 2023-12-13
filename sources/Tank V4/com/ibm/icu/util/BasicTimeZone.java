package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.BitSet;
import java.util.Date;
import java.util.LinkedList;

public abstract class BasicTimeZone extends TimeZone {
   private static final long serialVersionUID = -3204278532246180932L;
   private static final long MILLIS_PER_YEAR = 31536000000L;
   /** @deprecated */
   public static final int LOCAL_STD = 1;
   /** @deprecated */
   public static final int LOCAL_DST = 3;
   /** @deprecated */
   public static final int LOCAL_FORMER = 4;
   /** @deprecated */
   public static final int LOCAL_LATTER = 12;
   /** @deprecated */
   protected static final int STD_DST_MASK = 3;
   /** @deprecated */
   protected static final int FORMER_LATTER_MASK = 12;

   public abstract TimeZoneTransition getNextTransition(long var1, boolean var3);

   public abstract TimeZoneTransition getPreviousTransition(long var1, boolean var3);

   public boolean hasEquivalentTransitions(TimeZone var1, long var2, long var4) {
      return this.hasEquivalentTransitions(var1, var2, var4, false);
   }

   public boolean hasEquivalentTransitions(TimeZone var1, long var2, long var4, boolean var6) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof BasicTimeZone)) {
         return false;
      } else {
         int[] var7 = new int[2];
         int[] var8 = new int[2];
         this.getOffset(var2, false, var7);
         var1.getOffset(var2, false, var8);
         if (!var6) {
            if (var7[0] != var8[0] || var7[1] != var8[1]) {
               return false;
            }
         } else if (var7[0] + var7[1] != var8[0] + var8[1] || var7[1] != 0 && var8[1] == 0 || var7[1] == 0 && var8[1] != 0) {
            return false;
         }

         long var9 = var2;

         while(true) {
            TimeZoneTransition var11 = this.getNextTransition(var9, false);
            TimeZoneTransition var12 = ((BasicTimeZone)var1).getNextTransition(var9, false);
            if (var6) {
               while(var11 != null && var11.getTime() <= var4 && var11.getFrom().getRawOffset() + var11.getFrom().getDSTSavings() == var11.getTo().getRawOffset() + var11.getTo().getDSTSavings() && var11.getFrom().getDSTSavings() != 0 && var11.getTo().getDSTSavings() != 0) {
                  var11 = this.getNextTransition(var11.getTime(), false);
               }

               while(var12 != null && var12.getTime() <= var4 && var12.getFrom().getRawOffset() + var12.getFrom().getDSTSavings() == var12.getTo().getRawOffset() + var12.getTo().getDSTSavings() && var12.getFrom().getDSTSavings() != 0 && var12.getTo().getDSTSavings() != 0) {
                  var12 = ((BasicTimeZone)var1).getNextTransition(var12.getTime(), false);
               }
            }

            boolean var13 = false;
            boolean var14 = false;
            if (var11 != null && var11.getTime() <= var4) {
               var13 = true;
            }

            if (var12 != null && var12.getTime() <= var4) {
               var14 = true;
            }

            if (!var13 && !var14) {
               return true;
            }

            if (!var13 || !var14) {
               return false;
            }

            if (var11.getTime() != var12.getTime()) {
               return false;
            }

            if (var6) {
               if (var11.getTo().getRawOffset() + var11.getTo().getDSTSavings() != var12.getTo().getRawOffset() + var12.getTo().getDSTSavings() || var11.getTo().getDSTSavings() != 0 && var12.getTo().getDSTSavings() == 0 || var11.getTo().getDSTSavings() == 0 && var12.getTo().getDSTSavings() != 0) {
                  return false;
               }
            } else if (var11.getTo().getRawOffset() != var12.getTo().getRawOffset() || var11.getTo().getDSTSavings() != var12.getTo().getDSTSavings()) {
               return false;
            }

            var9 = var11.getTime();
         }
      }
   }

   public abstract TimeZoneRule[] getTimeZoneRules();

   public TimeZoneRule[] getTimeZoneRules(long var1) {
      TimeZoneRule[] var3 = this.getTimeZoneRules();
      TimeZoneTransition var4 = this.getPreviousTransition(var1, true);
      if (var4 == null) {
         return var3;
      } else {
         BitSet var5 = new BitSet(var3.length);
         LinkedList var6 = new LinkedList();
         InitialTimeZoneRule var7 = new InitialTimeZoneRule(var4.getTo().getName(), var4.getTo().getRawOffset(), var4.getTo().getDSTSavings());
         var6.add(var7);
         var5.set(0);

         for(int var8 = 1; var8 < var3.length; ++var8) {
            Date var9 = var3[var8].getNextStart(var1, var7.getRawOffset(), var7.getDSTSavings(), false);
            if (var9 == null) {
               var5.set(var8);
            }
         }

         long var24 = var1;
         boolean var10 = false;
         boolean var11 = false;

         while(!var10 || !var11) {
            var4 = this.getNextTransition(var24, false);
            if (var4 == null) {
               break;
            }

            var24 = var4.getTime();
            TimeZoneRule var12 = var4.getTo();

            int var13;
            for(var13 = 1; var13 < var3.length && !var3[var13].equals(var12); ++var13) {
            }

            if (var13 >= var3.length) {
               throw new IllegalStateException("The rule was not found");
            }

            if (!var5.get(var13)) {
               if (var12 instanceof TimeArrayTimeZoneRule) {
                  TimeArrayTimeZoneRule var14 = (TimeArrayTimeZoneRule)var12;
                  long var15 = var1;

                  while(true) {
                     var4 = this.getNextTransition(var15, false);
                     if (var4 == null || var4.getTo().equals(var14)) {
                        if (var4 != null) {
                           Date var17 = var14.getFirstStart(var4.getFrom().getRawOffset(), var4.getFrom().getDSTSavings());
                           if (var17.getTime() > var1) {
                              var6.add(var14);
                           } else {
                              long[] var18 = var14.getStartTimes();
                              int var19 = var14.getTimeType();

                              int var20;
                              for(var20 = 0; var20 < var18.length; ++var20) {
                                 var15 = var18[var20];
                                 if (var19 == 1) {
                                    var15 -= (long)var4.getFrom().getRawOffset();
                                 }

                                 if (var19 == 0) {
                                    var15 -= (long)var4.getFrom().getDSTSavings();
                                 }

                                 if (var15 > var1) {
                                    break;
                                 }
                              }

                              int var21 = var18.length - var20;
                              if (var21 > 0) {
                                 long[] var22 = new long[var21];
                                 System.arraycopy(var18, var20, var22, 0, var21);
                                 TimeArrayTimeZoneRule var23 = new TimeArrayTimeZoneRule(var14.getName(), var14.getRawOffset(), var14.getDSTSavings(), var22, var14.getTimeType());
                                 var6.add(var23);
                              }
                           }
                        }
                        break;
                     }

                     var15 = var4.getTime();
                  }
               } else if (var12 instanceof AnnualTimeZoneRule) {
                  AnnualTimeZoneRule var26 = (AnnualTimeZoneRule)var12;
                  Date var27 = var26.getFirstStart(var4.getFrom().getRawOffset(), var4.getFrom().getDSTSavings());
                  if (var27.getTime() == var4.getTime()) {
                     var6.add(var26);
                  } else {
                     int[] var16 = new int[6];
                     Grego.timeToFields(var4.getTime(), var16);
                     AnnualTimeZoneRule var28 = new AnnualTimeZoneRule(var26.getName(), var26.getRawOffset(), var26.getDSTSavings(), var26.getRule(), var16[0], var26.getEndYear());
                     var6.add(var28);
                  }

                  if (var26.getEndYear() == Integer.MAX_VALUE) {
                     if (var26.getDSTSavings() == 0) {
                        var10 = true;
                     } else {
                        var11 = true;
                     }
                  }
               }

               var5.set(var13);
            }
         }

         TimeZoneRule[] var25 = (TimeZoneRule[])var6.toArray(new TimeZoneRule[var6.size()]);
         return var25;
      }
   }

   public TimeZoneRule[] getSimpleTimeZoneRulesNear(long var1) {
      AnnualTimeZoneRule[] var3 = null;
      InitialTimeZoneRule var4 = null;
      TimeZoneTransition var5 = this.getNextTransition(var1, false);
      String var6;
      if (var5 != null) {
         var6 = var5.getFrom().getName();
         int var7 = var5.getFrom().getRawOffset();
         int var8 = var5.getFrom().getDSTSavings();
         long var9 = var5.getTime();
         if ((var5.getFrom().getDSTSavings() == 0 && var5.getTo().getDSTSavings() != 0 || var5.getFrom().getDSTSavings() != 0 && var5.getTo().getDSTSavings() == 0) && var1 + 31536000000L > var9) {
            var3 = new AnnualTimeZoneRule[2];
            int[] var11 = Grego.timeToFields(var9 + (long)var5.getFrom().getRawOffset() + (long)var5.getFrom().getDSTSavings(), (int[])null);
            int var12 = Grego.getDayOfWeekInMonth(var11[0], var11[1], var11[2]);
            DateTimeRule var13 = new DateTimeRule(var11[1], var12, var11[3], var11[5], 0);
            AnnualTimeZoneRule var14 = null;
            var3[0] = new AnnualTimeZoneRule(var5.getTo().getName(), var7, var5.getTo().getDSTSavings(), var13, var11[0], Integer.MAX_VALUE);
            Date var15;
            if (var5.getTo().getRawOffset() == var7) {
               var5 = this.getNextTransition(var9, false);
               if (var5 != null && (var5.getFrom().getDSTSavings() == 0 && var5.getTo().getDSTSavings() != 0 || var5.getFrom().getDSTSavings() != 0 && var5.getTo().getDSTSavings() == 0) && var9 + 31536000000L > var5.getTime()) {
                  var11 = Grego.timeToFields(var5.getTime() + (long)var5.getFrom().getRawOffset() + (long)var5.getFrom().getDSTSavings(), var11);
                  var12 = Grego.getDayOfWeekInMonth(var11[0], var11[1], var11[2]);
                  var13 = new DateTimeRule(var11[1], var12, var11[3], var11[5], 0);
                  var14 = new AnnualTimeZoneRule(var5.getTo().getName(), var5.getTo().getRawOffset(), var5.getTo().getDSTSavings(), var13, var11[0] - 1, Integer.MAX_VALUE);
                  var15 = var14.getPreviousStart(var1, var5.getFrom().getRawOffset(), var5.getFrom().getDSTSavings(), true);
                  if (var15 != null && var15.getTime() <= var1 && var7 == var5.getTo().getRawOffset() && var8 == var5.getTo().getDSTSavings()) {
                     var3[1] = var14;
                  }
               }
            }

            if (var3[1] == null) {
               var5 = this.getPreviousTransition(var1, true);
               if (var5 != null && (var5.getFrom().getDSTSavings() == 0 && var5.getTo().getDSTSavings() != 0 || var5.getFrom().getDSTSavings() != 0 && var5.getTo().getDSTSavings() == 0)) {
                  var11 = Grego.timeToFields(var5.getTime() + (long)var5.getFrom().getRawOffset() + (long)var5.getFrom().getDSTSavings(), var11);
                  var12 = Grego.getDayOfWeekInMonth(var11[0], var11[1], var11[2]);
                  var13 = new DateTimeRule(var11[1], var12, var11[3], var11[5], 0);
                  var14 = new AnnualTimeZoneRule(var5.getTo().getName(), var7, var8, var13, var3[0].getStartYear() - 1, Integer.MAX_VALUE);
                  var15 = var14.getNextStart(var1, var5.getFrom().getRawOffset(), var5.getFrom().getDSTSavings(), false);
                  if (var15.getTime() > var9) {
                     var3[1] = var14;
                  }
               }
            }

            if (var3[1] == null) {
               var3 = null;
            } else {
               var6 = var3[0].getName();
               var7 = var3[0].getRawOffset();
               var8 = var3[0].getDSTSavings();
            }
         }

         var4 = new InitialTimeZoneRule(var6, var7, var8);
      } else {
         var5 = this.getPreviousTransition(var1, true);
         if (var5 != null) {
            var4 = new InitialTimeZoneRule(var5.getTo().getName(), var5.getTo().getRawOffset(), var5.getTo().getDSTSavings());
         } else {
            int[] var16 = new int[2];
            this.getOffset(var1, false, var16);
            var4 = new InitialTimeZoneRule(this.getID(), var16[0], var16[1]);
         }
      }

      var6 = null;
      TimeZoneRule[] var17;
      if (var3 == null) {
         var17 = new TimeZoneRule[]{var4};
      } else {
         var17 = new TimeZoneRule[]{var4, var3[0], var3[1]};
      }

      return var17;
   }

   /** @deprecated */
   public void getOffsetFromLocal(long var1, int var3, int var4, int[] var5) {
      throw new IllegalStateException("Not implemented");
   }

   protected BasicTimeZone() {
   }

   /** @deprecated */
   protected BasicTimeZone(String var1) {
      super(var1);
   }
}
