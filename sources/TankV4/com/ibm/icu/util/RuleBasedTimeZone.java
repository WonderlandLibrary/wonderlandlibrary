package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RuleBasedTimeZone extends BasicTimeZone {
   private static final long serialVersionUID = 7580833058949327935L;
   private final InitialTimeZoneRule initialRule;
   private List historicRules;
   private AnnualTimeZoneRule[] finalRules;
   private transient List historicTransitions;
   private transient boolean upToDate;
   private transient boolean isFrozen = false;

   public RuleBasedTimeZone(String var1, InitialTimeZoneRule var2) {
      super(var1);
      this.initialRule = var2;
   }

   public void addTransitionRule(TimeZoneRule var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to modify a frozen RuleBasedTimeZone instance.");
      } else if (!var1.isTransitionRule()) {
         throw new IllegalArgumentException("Rule must be a transition rule");
      } else {
         if (var1 instanceof AnnualTimeZoneRule && ((AnnualTimeZoneRule)var1).getEndYear() == Integer.MAX_VALUE) {
            if (this.finalRules == null) {
               this.finalRules = new AnnualTimeZoneRule[2];
               this.finalRules[0] = (AnnualTimeZoneRule)var1;
            } else {
               if (this.finalRules[1] != null) {
                  throw new IllegalStateException("Too many final rules");
               }

               this.finalRules[1] = (AnnualTimeZoneRule)var1;
            }
         } else {
            if (this.historicRules == null) {
               this.historicRules = new ArrayList();
            }

            this.historicRules.add(var1);
         }

         this.upToDate = false;
      }
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var1 == 0) {
         var2 = 1 - var2;
      }

      long var7 = Grego.fieldsToDay(var2, var3, var4) * 86400000L + (long)var6;
      int[] var9 = new int[2];
      this.getOffset(var7, true, 3, 1, var9);
      return var9[0] + var9[1];
   }

   public void getOffset(long var1, boolean var3, int[] var4) {
      this.getOffset(var1, var3, 4, 12, var4);
   }

   /** @deprecated */
   public void getOffsetFromLocal(long var1, int var3, int var4, int[] var5) {
      this.getOffset(var1, true, var3, var4, var5);
   }

   public int getRawOffset() {
      long var1 = System.currentTimeMillis();
      int[] var3 = new int[2];
      this.getOffset(var1, false, var3);
      return var3[0];
   }

   public boolean inDaylightTime(Date var1) {
      int[] var2 = new int[2];
      this.getOffset(var1.getTime(), false, var2);
      return var2[1] != 0;
   }

   public void setRawOffset(int var1) {
      throw new UnsupportedOperationException("setRawOffset in RuleBasedTimeZone is not supported.");
   }

   public boolean useDaylightTime() {
      long var1 = System.currentTimeMillis();
      int[] var3 = new int[2];
      this.getOffset(var1, false, var3);
      if (var3[1] != 0) {
         return true;
      } else {
         TimeZoneTransition var4 = this.getNextTransition(var1, false);
         return var4 != null && var4.getTo().getDSTSavings() != 0;
      }
   }

   public boolean observesDaylightTime() {
      long var1 = System.currentTimeMillis();
      int[] var3 = new int[2];
      this.getOffset(var1, false, var3);
      if (var3[1] != 0) {
         return true;
      } else {
         BitSet var4 = this.finalRules == null ? null : new BitSet(this.finalRules.length);

         while(true) {
            TimeZoneTransition var5 = this.getNextTransition(var1, false);
            if (var5 == null) {
               break;
            }

            TimeZoneRule var6 = var5.getTo();
            if (var6.getDSTSavings() != 0) {
               return true;
            }

            if (var4 != null) {
               for(int var7 = 0; var7 < this.finalRules.length; ++var7) {
                  if (this.finalRules[var7].equals(var6)) {
                     var4.set(var7);
                  }
               }

               if (var4.cardinality() == this.finalRules.length) {
                  break;
               }
            }

            var1 = var5.getTime();
         }

         return false;
      }
   }

   public boolean hasSameRules(TimeZone var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof RuleBasedTimeZone)) {
         return false;
      } else {
         RuleBasedTimeZone var2 = (RuleBasedTimeZone)var1;
         if (!this.initialRule.isEquivalentTo(var2.initialRule)) {
            return false;
         } else {
            if (this.finalRules != null && var2.finalRules != null) {
               for(int var3 = 0; var3 < this.finalRules.length; ++var3) {
                  if ((this.finalRules[var3] != null || var2.finalRules[var3] != null) && (this.finalRules[var3] == null || var2.finalRules[var3] == null || !this.finalRules[var3].isEquivalentTo(var2.finalRules[var3]))) {
                     return false;
                  }
               }
            } else if (this.finalRules != null || var2.finalRules != null) {
               return false;
            }

            if (this.historicRules != null && var2.historicRules != null) {
               if (this.historicRules.size() != var2.historicRules.size()) {
                  return false;
               }

               Iterator var8 = this.historicRules.iterator();

               while(var8.hasNext()) {
                  TimeZoneRule var4 = (TimeZoneRule)var8.next();
                  boolean var5 = false;
                  Iterator var6 = var2.historicRules.iterator();

                  while(var6.hasNext()) {
                     TimeZoneRule var7 = (TimeZoneRule)var6.next();
                     if (var4.isEquivalentTo(var7)) {
                        var5 = true;
                        break;
                     }
                  }

                  if (!var5) {
                     return false;
                  }
               }
            } else if (this.historicRules != null || var2.historicRules != null) {
               return false;
            }

            return true;
         }
      }
   }

   public TimeZoneRule[] getTimeZoneRules() {
      int var1 = 1;
      if (this.historicRules != null) {
         var1 += this.historicRules.size();
      }

      if (this.finalRules != null) {
         if (this.finalRules[1] != null) {
            var1 += 2;
         } else {
            ++var1;
         }
      }

      TimeZoneRule[] var2 = new TimeZoneRule[var1];
      var2[0] = this.initialRule;
      int var3 = 1;
      if (this.historicRules != null) {
         while(var3 < this.historicRules.size() + 1) {
            var2[var3] = (TimeZoneRule)this.historicRules.get(var3 - 1);
            ++var3;
         }
      }

      if (this.finalRules != null) {
         var2[var3++] = this.finalRules[0];
         if (this.finalRules[1] != null) {
            var2[var3] = this.finalRules[1];
         }
      }

      return var2;
   }

   public TimeZoneTransition getNextTransition(long var1, boolean var3) {
      this.complete();
      if (this.historicTransitions == null) {
         return null;
      } else {
         boolean var4 = false;
         TimeZoneTransition var5 = null;
         TimeZoneTransition var6 = (TimeZoneTransition)this.historicTransitions.get(0);
         long var7 = var6.getTime();
         if (var7 <= var1 && (!var3 || var7 != var1)) {
            int var9 = this.historicTransitions.size() - 1;
            var6 = (TimeZoneTransition)this.historicTransitions.get(var9);
            var7 = var6.getTime();
            if (var3 && var7 == var1) {
               var5 = var6;
            } else if (var7 <= var1) {
               if (this.finalRules == null) {
                  return null;
               }

               Date var10 = this.finalRules[0].getNextStart(var1, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), var3);
               Date var11 = this.finalRules[1].getNextStart(var1, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), var3);
               if (var11.after(var10)) {
                  var6 = new TimeZoneTransition(var10.getTime(), this.finalRules[1], this.finalRules[0]);
               } else {
                  var6 = new TimeZoneTransition(var11.getTime(), this.finalRules[0], this.finalRules[1]);
               }

               var5 = var6;
               var4 = true;
            } else {
               --var9;

               TimeZoneTransition var13;
               for(var13 = var6; var9 > 0; var13 = var6) {
                  var6 = (TimeZoneTransition)this.historicTransitions.get(var9);
                  var7 = var6.getTime();
                  if (var7 < var1 || !var3 && var7 == var1) {
                     break;
                  }

                  --var9;
               }

               var5 = var13;
            }
         } else {
            var5 = var6;
         }

         if (var5 != null) {
            TimeZoneRule var12 = var5.getFrom();
            TimeZoneRule var14 = var5.getTo();
            if (var12.getRawOffset() == var14.getRawOffset() && var12.getDSTSavings() == var14.getDSTSavings()) {
               if (var4) {
                  return null;
               }

               var5 = this.getNextTransition(var5.getTime(), false);
            }
         }

         return var5;
      }
   }

   public TimeZoneTransition getPreviousTransition(long var1, boolean var3) {
      this.complete();
      if (this.historicTransitions == null) {
         return null;
      } else {
         TimeZoneTransition var4 = null;
         TimeZoneTransition var5 = (TimeZoneTransition)this.historicTransitions.get(0);
         long var6 = var5.getTime();
         if (var3 && var6 == var1) {
            var4 = var5;
         } else {
            if (var6 >= var1) {
               return null;
            }

            int var8 = this.historicTransitions.size() - 1;
            var5 = (TimeZoneTransition)this.historicTransitions.get(var8);
            var6 = var5.getTime();
            if (var3 && var6 == var1) {
               var4 = var5;
            } else if (var6 < var1) {
               if (this.finalRules != null) {
                  Date var9 = this.finalRules[0].getPreviousStart(var1, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), var3);
                  Date var10 = this.finalRules[1].getPreviousStart(var1, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), var3);
                  if (var10.before(var9)) {
                     var5 = new TimeZoneTransition(var9.getTime(), this.finalRules[1], this.finalRules[0]);
                  } else {
                     var5 = new TimeZoneTransition(var10.getTime(), this.finalRules[0], this.finalRules[1]);
                  }
               }

               var4 = var5;
            } else {
               --var8;

               while(var8 >= 0) {
                  var5 = (TimeZoneTransition)this.historicTransitions.get(var8);
                  var6 = var5.getTime();
                  if (var6 < var1 || var3 && var6 == var1) {
                     break;
                  }

                  --var8;
               }

               var4 = var5;
            }
         }

         if (var4 != null) {
            TimeZoneRule var11 = var4.getFrom();
            TimeZoneRule var12 = var4.getTo();
            if (var11.getRawOffset() == var12.getRawOffset() && var11.getDSTSavings() == var12.getDSTSavings()) {
               var4 = this.getPreviousTransition(var4.getTime(), false);
            }
         }

         return var4;
      }
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   private void complete() {
      if (!this.upToDate) {
         if (this.finalRules != null && this.finalRules[1] == null) {
            throw new IllegalStateException("Incomplete final rules");
         } else {
            if (this.historicRules != null || this.finalRules != null) {
               Object var1 = this.initialRule;
               long var2 = -184303902528000000L;
               if (this.historicRules != null) {
                  BitSet var4 = new BitSet(this.historicRules.size());

                  while(true) {
                     int var5 = ((TimeZoneRule)var1).getRawOffset();
                     int var6 = ((TimeZoneRule)var1).getDSTSavings();
                     long var7 = 183882168921600000L;
                     Object var9 = null;

                     Date var10;
                     long var11;
                     int var13;
                     for(var13 = 0; var13 < this.historicRules.size(); ++var13) {
                        if (!var4.get(var13)) {
                           TimeZoneRule var14 = (TimeZoneRule)this.historicRules.get(var13);
                           var10 = var14.getNextStart(var2, var5, var6, false);
                           if (var10 == null) {
                              var4.set(var13);
                           } else if (var14 != var1 && (!var14.getName().equals(((TimeZoneRule)var1).getName()) || var14.getRawOffset() != ((TimeZoneRule)var1).getRawOffset() || var14.getDSTSavings() != ((TimeZoneRule)var1).getDSTSavings())) {
                              var11 = var10.getTime();
                              if (var11 < var7) {
                                 var7 = var11;
                                 var9 = var14;
                              }
                           }
                        }
                     }

                     if (var9 == null) {
                        boolean var17 = true;

                        for(int var18 = 0; var18 < this.historicRules.size(); ++var18) {
                           if (!var4.get(var18)) {
                              var17 = false;
                              break;
                           }
                        }

                        if (var17) {
                           break;
                        }
                     }

                     if (this.finalRules != null) {
                        for(var13 = 0; var13 < 2; ++var13) {
                           if (this.finalRules[var13] != var1) {
                              var10 = this.finalRules[var13].getNextStart(var2, var5, var6, false);
                              if (var10 != null) {
                                 var11 = var10.getTime();
                                 if (var11 < var7) {
                                    var7 = var11;
                                    var9 = this.finalRules[var13];
                                 }
                              }
                           }
                        }
                     }

                     if (var9 == null) {
                        break;
                     }

                     if (this.historicTransitions == null) {
                        this.historicTransitions = new ArrayList();
                     }

                     this.historicTransitions.add(new TimeZoneTransition(var7, (TimeZoneRule)var1, (TimeZoneRule)var9));
                     var2 = var7;
                     var1 = var9;
                  }
               }

               if (this.finalRules != null) {
                  if (this.historicTransitions == null) {
                     this.historicTransitions = new ArrayList();
                  }

                  Date var15 = this.finalRules[0].getNextStart(var2, ((TimeZoneRule)var1).getRawOffset(), ((TimeZoneRule)var1).getDSTSavings(), false);
                  Date var16 = this.finalRules[1].getNextStart(var2, ((TimeZoneRule)var1).getRawOffset(), ((TimeZoneRule)var1).getDSTSavings(), false);
                  if (var16.after(var15)) {
                     this.historicTransitions.add(new TimeZoneTransition(var15.getTime(), (TimeZoneRule)var1, this.finalRules[0]));
                     var16 = this.finalRules[1].getNextStart(var15.getTime(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), false);
                     this.historicTransitions.add(new TimeZoneTransition(var16.getTime(), this.finalRules[0], this.finalRules[1]));
                  } else {
                     this.historicTransitions.add(new TimeZoneTransition(var16.getTime(), (TimeZoneRule)var1, this.finalRules[1]));
                     var15 = this.finalRules[0].getNextStart(var16.getTime(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), false);
                     this.historicTransitions.add(new TimeZoneTransition(var15.getTime(), this.finalRules[1], this.finalRules[0]));
                  }
               }
            }

            this.upToDate = true;
         }
      }
   }

   private void getOffset(long var1, boolean var3, int var4, int var5, int[] var6) {
      this.complete();
      Object var7 = null;
      if (this.historicTransitions == null) {
         var7 = this.initialRule;
      } else {
         long var8 = getTransitionTime((TimeZoneTransition)this.historicTransitions.get(0), var3, var4, var5);
         if (var1 < var8) {
            var7 = this.initialRule;
         } else {
            int var10 = this.historicTransitions.size() - 1;
            long var11 = getTransitionTime((TimeZoneTransition)this.historicTransitions.get(var10), var3, var4, var5);
            if (var1 > var11) {
               if (this.finalRules != null) {
                  var7 = this.findRuleInFinal(var1, var3, var4, var5);
               }

               if (var7 == null) {
                  var7 = ((TimeZoneTransition)this.historicTransitions.get(var10)).getTo();
               }
            } else {
               while(var10 >= 0 && var1 < getTransitionTime((TimeZoneTransition)this.historicTransitions.get(var10), var3, var4, var5)) {
                  --var10;
               }

               var7 = ((TimeZoneTransition)this.historicTransitions.get(var10)).getTo();
            }
         }
      }

      var6[0] = ((TimeZoneRule)var7).getRawOffset();
      var6[1] = ((TimeZoneRule)var7).getDSTSavings();
   }

   private TimeZoneRule findRuleInFinal(long var1, boolean var3, int var4, int var5) {
      if (this.finalRules != null && this.finalRules.length == 2 && this.finalRules[0] != null && this.finalRules[1] != null) {
         long var8 = var1;
         int var10;
         if (var3) {
            var10 = getLocalDelta(this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), var4, var5);
            var8 = var1 - (long)var10;
         }

         Date var6 = this.finalRules[0].getPreviousStart(var8, this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), true);
         var8 = var1;
         if (var3) {
            var10 = getLocalDelta(this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), this.finalRules[1].getRawOffset(), this.finalRules[1].getDSTSavings(), var4, var5);
            var8 = var1 - (long)var10;
         }

         Date var7 = this.finalRules[1].getPreviousStart(var8, this.finalRules[0].getRawOffset(), this.finalRules[0].getDSTSavings(), true);
         if (var6 != null && var7 != null) {
            return var6.after(var7) ? this.finalRules[0] : this.finalRules[1];
         } else if (var6 != null) {
            return this.finalRules[0];
         } else {
            return var7 != null ? this.finalRules[1] : null;
         }
      } else {
         return null;
      }
   }

   private static long getTransitionTime(TimeZoneTransition var0, boolean var1, int var2, int var3) {
      long var4 = var0.getTime();
      if (var1) {
         var4 += (long)getLocalDelta(var0.getFrom().getRawOffset(), var0.getFrom().getDSTSavings(), var0.getTo().getRawOffset(), var0.getTo().getDSTSavings(), var2, var3);
      }

      return var4;
   }

   private static int getLocalDelta(int var0, int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = false;
      int var7 = var0 + var1;
      int var8 = var2 + var3;
      boolean var9 = var1 != 0 && var3 == 0;
      boolean var10 = var1 == 0 && var3 != 0;
      int var11;
      if (var8 - var7 >= 0) {
         if ((var4 & 3) == 1 && var9 || (var4 & 3) == 3 && var10) {
            var11 = var7;
         } else if (((var4 & 3) != 1 || !var10) && ((var4 & 3) != 3 || !var9)) {
            if ((var4 & 12) == 12) {
               var11 = var7;
            } else {
               var11 = var8;
            }
         } else {
            var11 = var8;
         }
      } else if ((var5 & 3) == 1 && var9 || (var5 & 3) == 3 && var10) {
         var11 = var8;
      } else if (((var5 & 3) != 1 || !var10) && ((var5 & 3) != 3 || !var9)) {
         if ((var5 & 12) == 4) {
            var11 = var7;
         } else {
            var11 = var8;
         }
      } else {
         var11 = var7;
      }

      return var11;
   }

   public boolean isFrozen() {
      return this.isFrozen;
   }

   public TimeZone freeze() {
      this.complete();
      this.isFrozen = true;
      return this;
   }

   public TimeZone cloneAsThawed() {
      RuleBasedTimeZone var1 = (RuleBasedTimeZone)super.cloneAsThawed();
      if (this.historicRules != null) {
         var1.historicRules = new ArrayList(this.historicRules);
      }

      if (this.finalRules != null) {
         var1.finalRules = (AnnualTimeZoneRule[])this.finalRules.clone();
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
