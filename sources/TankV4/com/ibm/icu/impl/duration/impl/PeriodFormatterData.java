package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.duration.TimeUnit;
import java.util.Arrays;

public class PeriodFormatterData {
   final DataRecord dr;
   String localeName;
   public static boolean trace = false;
   private static final int FORM_PLURAL = 0;
   private static final int FORM_SINGULAR = 1;
   private static final int FORM_DUAL = 2;
   private static final int FORM_PAUCAL = 3;
   private static final int FORM_SINGULAR_SPELLED = 4;
   private static final int FORM_SINGULAR_NO_OMIT = 5;
   private static final int FORM_HALF_SPELLED = 6;

   public PeriodFormatterData(String var1, DataRecord var2) {
      this.dr = var2;
      this.localeName = var1;
      if (var1 == null) {
         throw new NullPointerException("localename is null");
      } else if (var2 == null) {
         throw new NullPointerException("data record is null");
      }
   }

   public int pluralization() {
      return this.dr.pl;
   }

   public boolean allowZero() {
      return this.dr.allowZero;
   }

   public boolean weeksAloneOnly() {
      return this.dr.weeksAloneOnly;
   }

   public int useMilliseconds() {
      return this.dr.useMilliseconds;
   }

   public boolean appendPrefix(int var1, int var2, StringBuffer var3) {
      if (this.dr.scopeData != null) {
         int var4 = var1 * 3 + var2;
         DataRecord.ScopeData var5 = this.dr.scopeData[var4];
         if (var5 != null) {
            String var6 = var5.prefix;
            if (var6 != null) {
               var3.append(var6);
               return var5.requiresDigitPrefix;
            }
         }
      }

      return false;
   }

   public void appendSuffix(int var1, int var2, StringBuffer var3) {
      if (this.dr.scopeData != null) {
         int var4 = var1 * 3 + var2;
         DataRecord.ScopeData var5 = this.dr.scopeData[var4];
         if (var5 != null) {
            String var6 = var5.suffix;
            if (var6 != null) {
               if (trace) {
                  System.out.println("appendSuffix '" + var6 + "'");
               }

               var3.append(var6);
            }
         }
      }

   }

   public boolean appendUnit(TimeUnit var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9, StringBuffer var10) {
      int var11 = var1.ordinal();
      boolean var12 = false;
      if (this.dr.requiresSkipMarker != null && this.dr.requiresSkipMarker[var11] && this.dr.skippedUnitMarker != null) {
         if (!var9 && var8) {
            var10.append(this.dr.skippedUnitMarker);
         }

         var12 = true;
      }

      if (var4 != 0) {
         boolean var13 = var4 == 1;
         String[] var14 = var13 ? this.dr.mediumNames : this.dr.shortNames;
         if (var14 == null || var14[var11] == null) {
            var14 = var13 ? this.dr.shortNames : this.dr.mediumNames;
         }

         if (var14 != null && var14[var11] != null) {
            this.appendCount(var1, false, false, var2, var3, var5, var14[var11], var8, var10);
            return false;
         }
      }

      if (var3 == 2 && this.dr.halfSupport != null) {
         switch(this.dr.halfSupport[var11]) {
         case 0:
         default:
            break;
         case 2:
            if (var2 > 1000) {
               break;
            }
         case 1:
            var2 = var2 / 500 * 500;
            var3 = 3;
         }
      }

      String var19 = null;
      int var20 = this.computeForm(var1, var2, var3, var7 && var8);
      if (var20 == 4) {
         if (this.dr.singularNames == null) {
            var20 = 1;
            var19 = this.dr.pluralNames[var11][var20];
         } else {
            var19 = this.dr.singularNames[var11];
         }
      } else if (var20 == 5) {
         var19 = this.dr.pluralNames[var11][1];
      } else if (var20 == 6) {
         var19 = this.dr.halfNames[var11];
      } else {
         try {
            var19 = this.dr.pluralNames[var11][var20];
         } catch (NullPointerException var18) {
            System.out.println("Null Pointer in PeriodFormatterData[" + this.localeName + "].au px: " + var11 + " form: " + var20 + " pn: " + Arrays.toString(this.dr.pluralNames));
            throw var18;
         }
      }

      if (var19 == null) {
         var20 = 0;
         var19 = this.dr.pluralNames[var11][var20];
      }

      boolean var15 = var20 == 4 || var20 == 6 || this.dr.omitSingularCount && var20 == 1 || this.dr.omitDualCount && var20 == 2;
      int var16 = this.appendCount(var1, var15, var6, var2, var3, var5, var19, var8, var10);
      if (var8 && var16 >= 0) {
         String var17 = null;
         if (this.dr.rqdSuffixes != null && var16 < this.dr.rqdSuffixes.length) {
            var17 = this.dr.rqdSuffixes[var16];
         }

         if (var17 == null && this.dr.optSuffixes != null && var16 < this.dr.optSuffixes.length) {
            var17 = this.dr.optSuffixes[var16];
         }

         if (var17 != null) {
            var10.append(var17);
         }
      }

      return var12;
   }

   public int appendCount(TimeUnit var1, boolean var2, boolean var3, int var4, int var5, boolean var6, String var7, boolean var8, StringBuffer var9) {
      if (var5 == 2 && this.dr.halves == null) {
         var5 = 0;
      }

      if (!var2 && var3 && this.dr.digitPrefix != null) {
         var9.append(this.dr.digitPrefix);
      }

      int var10;
      var10 = var1.ordinal();
      int var11;
      label183:
      switch(var5) {
      case 0:
         if (!var2) {
            this.appendInteger(var4 / 1000, 1, 10, var9);
         }
         break;
      case 1:
         var11 = var4 / 1000;
         if (var1 == TimeUnit.MINUTE && (this.dr.fiveMinutes != null || this.dr.fifteenMinutes != null) && var11 != 0 && var11 % 5 == 0) {
            if (this.dr.fifteenMinutes != null && (var11 == 15 || var11 == 45)) {
               var11 = var11 == 15 ? 1 : 3;
               if (!var2) {
                  this.appendInteger(var11, 1, 10, var9);
               }

               var7 = this.dr.fifteenMinutes;
               var10 = 8;
               break;
            }

            if (this.dr.fiveMinutes != null) {
               var11 /= 5;
               if (!var2) {
                  this.appendInteger(var11, 1, 10, var9);
               }

               var7 = this.dr.fiveMinutes;
               var10 = 9;
               break;
            }
         }

         if (!var2) {
            this.appendInteger(var11, 1, 10, var9);
         }
         break;
      case 2:
         var11 = var4 / 500;
         if (var11 != 1 && !var2) {
            this.appendCountValue(var4, 1, 0, var9);
         }

         if ((var11 & 1) == 1) {
            if (var11 == 1 && this.dr.halfNames != null && this.dr.halfNames[var10] != null) {
               var9.append(var7);
               return var8 ? var10 : -1;
            }

            int var12 = var11 == 1 ? 0 : 1;
            if (this.dr.genders != null && this.dr.halves.length > 2 && this.dr.genders[var10] == 1) {
               var12 += 2;
            }

            byte var13 = this.dr.halfPlacements == null ? 0 : this.dr.halfPlacements[var12 & 1];
            String var14 = this.dr.halves[var12];
            String var15 = this.dr.measures == null ? null : this.dr.measures[var10];
            switch(var13) {
            case 0:
               var9.append(var14);
               break label183;
            case 1:
               if (var15 != null) {
                  var9.append(var15);
                  var9.append(var14);
                  if (var6 && !var2) {
                     var9.append(this.dr.countSep);
                  }

                  var9.append(var7);
                  return -1;
               }

               var9.append(var7);
               var9.append(var14);
               return var8 ? var10 : -1;
            case 2:
               if (var15 != null) {
                  var9.append(var15);
               }

               if (var6 && !var2) {
                  var9.append(this.dr.countSep);
               }

               var9.append(var7);
               var9.append(var14);
               return var8 ? var10 : -1;
            }
         }
         break;
      default:
         byte var16 = 1;
         switch(var5) {
         case 4:
            var16 = 2;
            break;
         case 5:
            var16 = 3;
         }

         if (!var2) {
            this.appendCountValue(var4, 1, var16, var9);
         }
      }

      if (!var2 && var6) {
         var9.append(this.dr.countSep);
      }

      if (!var2 && this.dr.measures != null && var10 < this.dr.measures.length) {
         String var17 = this.dr.measures[var10];
         if (var17 != null) {
            var9.append(var17);
         }
      }

      var9.append(var7);
      return var8 ? var10 : -1;
   }

   public void appendCountValue(int var1, int var2, int var3, StringBuffer var4) {
      int var5 = var1 / 1000;
      if (var3 == 0) {
         this.appendInteger(var5, var2, 10, var4);
      } else {
         if (this.dr.requiresDigitSeparator && var4.length() > 0) {
            var4.append(' ');
         }

         this.appendDigits((long)var5, var2, 10, var4);
         int var6 = var1 % 1000;
         if (var3 == 1) {
            var6 /= 100;
         } else if (var3 == 2) {
            var6 /= 10;
         }

         var4.append(this.dr.decimalSep);
         this.appendDigits((long)var6, var3, var3, var4);
         if (this.dr.requiresDigitSeparator) {
            var4.append(' ');
         }

      }
   }

   public void appendInteger(int var1, int var2, int var3, StringBuffer var4) {
      if (this.dr.numberNames != null && var1 < this.dr.numberNames.length) {
         String var5 = this.dr.numberNames[var1];
         if (var5 != null) {
            var4.append(var5);
            return;
         }
      }

      if (this.dr.requiresDigitSeparator && var4.length() > 0) {
         var4.append(' ');
      }

      switch(this.dr.numberSystem) {
      case 0:
         this.appendDigits((long)var1, var2, var3, var4);
         break;
      case 1:
         var4.append(Utils.chineseNumber((long)var1, Utils.ChineseDigits.TRADITIONAL));
         break;
      case 2:
         var4.append(Utils.chineseNumber((long)var1, Utils.ChineseDigits.SIMPLIFIED));
         break;
      case 3:
         var4.append(Utils.chineseNumber((long)var1, Utils.ChineseDigits.KOREAN));
      }

      if (this.dr.requiresDigitSeparator) {
         var4.append(' ');
      }

   }

   public void appendDigits(long var1, int var3, int var4, StringBuffer var5) {
      char[] var6 = new char[var4];

      int var7;
      for(var7 = var4; var7 > 0 && var1 > 0L; var1 /= 10L) {
         --var7;
         var6[var7] = (char)((int)((long)this.dr.zero + var1 % 10L));
      }

      for(int var8 = var4 - var3; var7 > var8; var6[var7] = this.dr.zero) {
         --var7;
      }

      var5.append(var6, var7, var4 - var7);
   }

   public void appendSkippedUnit(StringBuffer var1) {
      if (this.dr.skippedUnitMarker != null) {
         var1.append(this.dr.skippedUnitMarker);
      }

   }

   public boolean appendUnitSeparator(TimeUnit var1, boolean var2, boolean var3, boolean var4, StringBuffer var5) {
      if (var2 && this.dr.unitSep != null || this.dr.shortUnitSep != null) {
         if (var2 && this.dr.unitSep != null) {
            int var6 = (var3 ? 2 : 0) + (var4 ? 1 : 0);
            var5.append(this.dr.unitSep[var6]);
            return this.dr.unitSepRequiresDP != null && this.dr.unitSepRequiresDP[var6];
         }

         var5.append(this.dr.shortUnitSep);
      }

      return false;
   }

   private int computeForm(TimeUnit var1, int var2, int var3, boolean var4) {
      if (trace) {
         System.err.println("pfd.cf unit: " + var1 + " count: " + var2 + " cv: " + var3 + " dr.pl: " + this.dr.pl);
         Thread.dumpStack();
      }

      if (this.dr.pl == 0) {
         return 0;
      } else {
         int var5 = var2 / 1000;
         switch(var3) {
         case 2:
            int var6;
            switch(this.dr.fractionHandling) {
            case 0:
               return 0;
            case 1:
            case 2:
               var6 = var2 / 500;
               if (var6 == 1) {
                  if (this.dr.halfNames != null && this.dr.halfNames[var1.ordinal()] != null) {
                     return 6;
                  }

                  return 5;
               } else {
                  if ((var6 & 1) == 1) {
                     if (this.dr.pl == 5 && var6 > 21) {
                        return 5;
                     } else if (var6 == 3 && this.dr.pl == 1 && this.dr.fractionHandling != 2) {
                        return 0;
                     }
                  }
                  break;
               }
            case 3:
               var6 = var2 / 500;
               if (var6 == 1 || var6 == 3) {
                  return 3;
               }
               break;
            default:
               throw new IllegalStateException();
            }
         case 0:
         case 1:
            if (trace && var2 == 0) {
               System.err.println("EZeroHandling = " + this.dr.zeroHandling);
            }

            if (var2 == 0 && this.dr.zeroHandling == 1) {
               return 4;
            } else {
               byte var8 = 0;
               switch(this.dr.pl) {
               case 0:
                  break;
               case 1:
                  if (var5 == 1) {
                     var8 = 4;
                  }
                  break;
               case 2:
                  if (var5 == 2) {
                     var8 = 2;
                  } else if (var5 == 1) {
                     var8 = 1;
                  }
                  break;
               case 3:
                  int var7 = var5 % 100;
                  if (var7 > 20) {
                     var7 %= 10;
                  }

                  if (var7 == 1) {
                     var8 = 1;
                  } else if (var7 > 1 && var7 < 5) {
                     var8 = 3;
                  }
                  break;
               case 4:
                  if (var5 == 2) {
                     var8 = 2;
                  } else if (var5 == 1) {
                     if (var4) {
                        var8 = 4;
                     } else {
                        var8 = 1;
                     }
                  } else if (var1 == TimeUnit.YEAR && var5 > 11) {
                     var8 = 5;
                  }
                  break;
               case 5:
                  if (var5 == 2) {
                     var8 = 2;
                  } else if (var5 == 1) {
                     var8 = 1;
                  } else if (var5 > 10) {
                     var8 = 5;
                  }
                  break;
               default:
                  System.err.println("dr.pl is " + this.dr.pl);
                  throw new IllegalStateException();
               }

               return var8;
            }
         default:
            switch(this.dr.decimalHandling) {
            case 0:
            default:
               break;
            case 1:
               return 5;
            case 2:
               if (var2 < 1000) {
                  return 5;
               }
               break;
            case 3:
               if (this.dr.pl == 3) {
                  return 3;
               }
            }

            return 0;
         }
      }
   }
}
