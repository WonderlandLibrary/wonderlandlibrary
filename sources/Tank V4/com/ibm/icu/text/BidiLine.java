package com.ibm.icu.text;

import java.util.Arrays;

final class BidiLine {
   static void setTrailingWSStart(Bidi var0) {
      byte[] var1 = var0.dirProps;
      byte[] var2 = var0.levels;
      int var3 = var0.length;
      byte var4 = var0.paraLevel;
      if (Bidi.NoContextRTL(var1[var3 - 1]) == 7) {
         var0.trailingWSStart = var3;
      } else {
         while(var3 > 0 && (Bidi.DirPropFlagNC(var1[var3 - 1]) & Bidi.MASK_WS) != 0) {
            --var3;
         }

         while(var3 > 0 && var2[var3 - 1] == var4) {
            --var3;
         }

         var0.trailingWSStart = var3;
      }
   }

   static Bidi setLine(Bidi var0, int var1, int var2) {
      Bidi var4 = new Bidi();
      int var3 = var4.length = var4.originalLength = var4.resultLength = var2 - var1;
      var4.text = new char[var3];
      System.arraycopy(var0.text, var1, var4.text, 0, var3);
      var4.paraLevel = var0.GetParaLevelAt(var1);
      var4.paraCount = var0.paraCount;
      var4.runs = new BidiRun[0];
      var4.reorderingMode = var0.reorderingMode;
      var4.reorderingOptions = var0.reorderingOptions;
      if (var0.controlCount > 0) {
         for(int var5 = var1; var5 < var2; ++var5) {
            if (Bidi.IsBidiControlChar(var0.text[var5])) {
               ++var4.controlCount;
            }
         }

         var4.resultLength -= var4.controlCount;
      }

      var4.getDirPropsMemory(var3);
      var4.dirProps = var4.dirPropsMemory;
      System.arraycopy(var0.dirProps, var1, var4.dirProps, 0, var3);
      var4.getLevelsMemory(var3);
      var4.levels = var4.levelsMemory;
      System.arraycopy(var0.levels, var1, var4.levels, 0, var3);
      var4.runCount = -1;
      if (var0.direction != 2) {
         var4.direction = var0.direction;
         if (var0.trailingWSStart <= var1) {
            var4.trailingWSStart = 0;
         } else if (var0.trailingWSStart < var2) {
            var4.trailingWSStart = var0.trailingWSStart - var1;
         } else {
            var4.trailingWSStart = var3;
         }
      } else {
         byte[] var9 = var4.levels;
         setTrailingWSStart(var4);
         int var7 = var4.trailingWSStart;
         if (var7 == 0) {
            var4.direction = (byte)(var4.paraLevel & 1);
         } else {
            byte var8 = (byte)(var9[0] & 1);
            if (var7 < var3 && (var4.paraLevel & 1) != var8) {
               var4.direction = 2;
            } else {
               label71: {
                  for(int var6 = 1; var6 != var7; ++var6) {
                     if ((var9[var6] & 1) != var8) {
                        var4.direction = 2;
                        break label71;
                     }
                  }

                  var4.direction = var8;
               }
            }
         }

         switch(var4.direction) {
         case 0:
            var4.paraLevel = (byte)(var4.paraLevel + 1 & -2);
            var4.trailingWSStart = 0;
            break;
         case 1:
            var4.paraLevel = (byte)(var4.paraLevel | 1);
            var4.trailingWSStart = 0;
         }
      }

      var4.paraBidi = var0;
      return var4;
   }

   static byte getLevelAt(Bidi var0, int var1) {
      return var0.direction == 2 && var1 < var0.trailingWSStart ? var0.levels[var1] : var0.GetParaLevelAt(var1);
   }

   static byte[] getLevels(Bidi var0) {
      int var1 = var0.trailingWSStart;
      int var2 = var0.length;
      if (var1 != var2) {
         Arrays.fill(var0.levels, var1, var2, var0.paraLevel);
         var0.trailingWSStart = var2;
      }

      if (var2 < var0.levels.length) {
         byte[] var3 = new byte[var2];
         System.arraycopy(var0.levels, 0, var3, 0, var2);
         return var3;
      } else {
         return var0.levels;
      }
   }

   static BidiRun getLogicalRun(Bidi var0, int var1) {
      BidiRun var2 = new BidiRun();
      getRuns(var0);
      int var4 = var0.runCount;
      int var5 = 0;
      int var6 = 0;
      BidiRun var3 = var0.runs[0];

      for(int var7 = 0; var7 < var4; ++var7) {
         var3 = var0.runs[var7];
         var6 = var3.start + var3.limit - var5;
         if (var1 >= var3.start && var1 < var6) {
            break;
         }

         var5 = var3.limit;
      }

      var2.start = var3.start;
      var2.limit = var6;
      var2.level = var3.level;
      return var2;
   }

   static BidiRun getVisualRun(Bidi var0, int var1) {
      int var2 = var0.runs[var1].start;
      byte var4 = var0.runs[var1].level;
      int var3;
      if (var1 > 0) {
         var3 = var2 + var0.runs[var1].limit - var0.runs[var1 - 1].limit;
      } else {
         var3 = var2 + var0.runs[0].limit;
      }

      return new BidiRun(var2, var3, var4);
   }

   static void getSingleRun(Bidi var0, byte var1) {
      var0.runs = var0.simpleRuns;
      var0.runCount = 1;
      var0.runs[0] = new BidiRun(0, var0.length, var1);
   }

   private static void reorderLine(Bidi var0, byte var1, byte var2) {
      if (var2 > (var1 | 1)) {
         ++var1;
         BidiRun[] var3 = var0.runs;
         byte[] var5 = var0.levels;
         int var9 = var0.runCount;
         if (var0.trailingWSStart < var0.length) {
            --var9;
         }

         label70:
         while(true) {
            --var2;
            BidiRun var4;
            int var6;
            if (var2 < var1) {
               if ((var1 & 1) == 0) {
                  var6 = 0;
                  if (var0.trailingWSStart == var0.length) {
                     --var9;
                  }

                  while(var6 < var9) {
                     var4 = var3[var6];
                     var3[var6] = var3[var9];
                     var3[var9] = var4;
                     ++var6;
                     --var9;
                  }
               }

               return;
            }

            var6 = 0;

            while(true) {
               while(var6 >= var9 || var5[var3[var6].start] >= var2) {
                  if (var6 >= var9) {
                     continue label70;
                  }

                  int var8 = var6;

                  do {
                     ++var8;
                  } while(var8 < var9 && var5[var3[var8].start] >= var2);

                  for(int var7 = var8 - 1; var6 < var7; --var7) {
                     var4 = var3[var6];
                     var3[var6] = var3[var7];
                     var3[var7] = var4;
                     ++var6;
                  }

                  if (var8 == var9) {
                     continue label70;
                  }

                  var6 = var8 + 1;
               }

               ++var6;
            }
         }
      }
   }

   static int getRunFromLogicalIndex(Bidi var0, int var1) {
      BidiRun[] var2 = var0.runs;
      int var3 = var0.runCount;
      int var4 = 0;

      for(int var5 = 0; var5 < var3; ++var5) {
         int var6 = var2[var5].limit - var4;
         int var7 = var2[var5].start;
         if (var1 >= var7 && var1 < var7 + var6) {
            return var5;
         }

         var4 += var6;
      }

      throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
   }

   static void getRuns(Bidi var0) {
      if (var0.runCount < 0) {
         int var1;
         int var2;
         if (var0.direction != 2) {
            getSingleRun(var0, var0.paraLevel);
         } else {
            var1 = var0.length;
            byte[] var3 = var0.levels;
            byte var6 = 126;
            var2 = var0.trailingWSStart;
            int var5 = 0;

            int var4;
            for(var4 = 0; var4 < var2; ++var4) {
               if (var3[var4] != var6) {
                  ++var5;
                  var6 = var3[var4];
               }
            }

            if (var5 == 1 && var2 == var1) {
               getSingleRun(var0, var3[0]);
            } else {
               byte var10 = 62;
               byte var11 = 0;
               if (var2 < var1) {
                  ++var5;
               }

               var0.getRunsMemory(var5);
               BidiRun[] var7 = var0.runsMemory;
               int var8 = 0;
               var4 = 0;

               do {
                  int var9 = var4;
                  var6 = var3[var4];
                  if (var6 < var10) {
                     var10 = var6;
                  }

                  if (var6 > var11) {
                     var11 = var6;
                  }

                  do {
                     ++var4;
                  } while(var4 < var2 && var3[var4] == var6);

                  var7[var8] = new BidiRun(var9, var4 - var9, var6);
                  ++var8;
               } while(var4 < var2);

               if (var2 < var1) {
                  var7[var8] = new BidiRun(var2, var1 - var2, var0.paraLevel);
                  if (var0.paraLevel < var10) {
                     var10 = var0.paraLevel;
                  }
               }

               var0.runs = var7;
               var0.runCount = var5;
               reorderLine(var0, var10, var11);
               var2 = 0;

               for(var4 = 0; var4 < var5; ++var4) {
                  var7[var4].level = var3[var7[var4].start];
                  var2 = var7[var4].limit += var2;
               }

               if (var8 < var5) {
                  int var12 = (var0.paraLevel & 1) != 0 ? 0 : var8;
                  var7[var12].level = var0.paraLevel;
               }
            }
         }

         if (var0.insertPoints.size > 0) {
            for(int var14 = 0; var14 < var0.insertPoints.size; ++var14) {
               Bidi.Point var13 = var0.insertPoints.points[var14];
               var2 = getRunFromLogicalIndex(var0, var13.pos);
               BidiRun var10000 = var0.runs[var2];
               var10000.insertRemove |= var13.flag;
            }
         }

         if (var0.controlCount > 0) {
            for(var2 = 0; var2 < var0.length; ++var2) {
               char var15 = var0.text[var2];
               if (Bidi.IsBidiControlChar(var15)) {
                  var1 = getRunFromLogicalIndex(var0, var2);
                  --var0.runs[var1].insertRemove;
               }
            }
         }

      }
   }

   static int[] prepareReorder(byte[] var0, byte[] var1, byte[] var2) {
      if (var0 != null && var0.length > 0) {
         byte var5 = 62;
         byte var6 = 0;
         int var3 = var0.length;

         while(var3 > 0) {
            --var3;
            byte var4 = var0[var3];
            if (var4 > 62) {
               return null;
            }

            if (var4 < var5) {
               var5 = var4;
            }

            if (var4 > var6) {
               var6 = var4;
            }
         }

         var1[0] = var5;
         var2[0] = var6;
         int[] var7 = new int[var0.length];

         for(var3 = var0.length; var3 > 0; var7[var3] = var3) {
            --var3;
         }

         return var7;
      } else {
         return null;
      }
   }

   static int[] reorderLogical(byte[] var0) {
      byte[] var1 = new byte[1];
      byte[] var2 = new byte[1];
      int[] var8 = prepareReorder(var0, var1, var2);
      if (var8 == null) {
         return null;
      } else {
         byte var6 = var1[0];
         byte var7 = var2[0];
         if (var6 == var7 && (var6 & 1) == 0) {
            return var8;
         } else {
            var6 = (byte)(var6 | 1);

            do {
               int var3 = 0;

               label56:
               while(true) {
                  while(var3 >= var0.length || var0[var3] >= var7) {
                     if (var3 >= var0.length) {
                        break label56;
                     }

                     int var4 = var3;

                     do {
                        ++var4;
                     } while(var4 < var0.length && var0[var4] >= var7);

                     int var5 = var3 + var4 - 1;

                     do {
                        var8[var3] = var5 - var8[var3];
                        ++var3;
                     } while(var3 < var4);

                     if (var4 == var0.length) {
                        break label56;
                     }

                     var3 = var4 + 1;
                  }

                  ++var3;
               }

               --var7;
            } while(var7 >= var6);

            return var8;
         }
      }
   }

   static int[] reorderVisual(byte[] var0) {
      byte[] var1 = new byte[1];
      byte[] var2 = new byte[1];
      int[] var9 = prepareReorder(var0, var1, var2);
      if (var9 == null) {
         return null;
      } else {
         byte var7 = var1[0];
         byte var8 = var2[0];
         if (var7 == var8 && (var7 & 1) == 0) {
            return var9;
         } else {
            var7 = (byte)(var7 | 1);

            do {
               int var3 = 0;

               label58:
               while(true) {
                  while(var3 >= var0.length || var0[var3] >= var8) {
                     if (var3 >= var0.length) {
                        break label58;
                     }

                     int var5 = var3;

                     do {
                        ++var5;
                     } while(var5 < var0.length && var0[var5] >= var8);

                     for(int var4 = var5 - 1; var3 < var4; --var4) {
                        int var6 = var9[var3];
                        var9[var3] = var9[var4];
                        var9[var4] = var6;
                        ++var3;
                     }

                     if (var5 == var0.length) {
                        break label58;
                     }

                     var3 = var5 + 1;
                  }

                  ++var3;
               }

               --var8;
            } while(var8 >= var7);

            return var9;
         }
      }
   }

   static int getVisualIndex(Bidi var0, int var1) {
      int var2 = -1;
      BidiRun[] var3;
      int var4;
      int var5;
      int var6;
      int var7;
      switch(var0.direction) {
      case 0:
         var2 = var1;
         break;
      case 1:
         var2 = var0.length - var1 - 1;
         break;
      default:
         getRuns(var0);
         var3 = var0.runs;
         var5 = 0;

         for(var4 = 0; var4 < var0.runCount; ++var4) {
            var7 = var3[var4].limit - var5;
            var6 = var1 - var3[var4].start;
            if (var6 >= 0 && var6 < var7) {
               if (var3[var4].isEvenRun()) {
                  var2 = var5 + var6;
               } else {
                  var2 = var5 + var7 - var6 - 1;
               }
               break;
            }

            var5 += var7;
         }

         if (var4 >= var0.runCount) {
            return -1;
         }
      }

      int var8;
      if (var0.insertPoints.size > 0) {
         var3 = var0.runs;
         var7 = 0;
         var8 = 0;
         var4 = 0;

         while(true) {
            var5 = var3[var4].limit - var7;
            var6 = var3[var4].insertRemove;
            if ((var6 & 5) > 0) {
               ++var8;
            }

            if (var2 < var3[var4].limit) {
               return var2 + var8;
            }

            if ((var6 & 10) > 0) {
               ++var8;
            }

            ++var4;
            var7 += var5;
         }
      } else if (var0.controlCount <= 0) {
         return var2;
      } else {
         var3 = var0.runs;
         int var10 = 0;
         int var11 = 0;
         char var12 = var0.text[var1];
         if (Bidi.IsBidiControlChar(var12)) {
            return -1;
         } else {
            var4 = 0;

            while(true) {
               var8 = var3[var4].limit - var10;
               int var9 = var3[var4].insertRemove;
               if (var2 < var3[var4].limit) {
                  if (var9 == 0) {
                     return var2 - var11;
                  } else {
                     if (var3[var4].isEvenRun()) {
                        var6 = var3[var4].start;
                        var7 = var1;
                     } else {
                        var6 = var1 + 1;
                        var7 = var3[var4].start + var8;
                     }

                     for(var5 = var6; var5 < var7; ++var5) {
                        var12 = var0.text[var5];
                        if (Bidi.IsBidiControlChar(var12)) {
                           ++var11;
                        }
                     }

                     return var2 - var11;
                  }
               }

               var11 -= var9;
               ++var4;
               var10 += var8;
            }
         }
      }
   }

   static int getLogicalIndex(Bidi var0, int var1) {
      BidiRun[] var2 = var0.runs;
      int var4 = var0.runCount;
      int var3;
      int var6;
      int var7;
      int var8;
      int var9;
      if (var0.insertPoints.size > 0) {
         var6 = 0;
         var8 = 0;
         var3 = 0;

         while(true) {
            var9 = var2[var3].limit - var8;
            var7 = var2[var3].insertRemove;
            if ((var7 & 5) > 0) {
               if (var1 <= var8 + var6) {
                  return -1;
               }

               ++var6;
            }

            if (var1 < var2[var3].limit + var6) {
               var1 -= var6;
               break;
            }

            if ((var7 & 10) > 0) {
               if (var1 == var8 + var9 + var6) {
                  return -1;
               }

               ++var6;
            }

            ++var3;
            var8 += var9;
         }
      } else if (var0.controlCount > 0) {
         var6 = 0;
         int var11 = 0;
         var3 = 0;

         while(true) {
            var8 = var2[var3].limit - var11;
            var7 = var2[var3].insertRemove;
            if (var1 < var2[var3].limit - var6 + var7) {
               if (var7 == 0) {
                  var1 += var6;
               } else {
                  var9 = var2[var3].start;
                  boolean var15 = var2[var3].isEvenRun();
                  int var10 = var9 + var8 - 1;

                  for(int var12 = 0; var12 < var8; ++var12) {
                     int var13 = var15 ? var9 + var12 : var10 - var12;
                     char var14 = var0.text[var13];
                     if (Bidi.IsBidiControlChar(var14)) {
                        ++var6;
                     }

                     if (var1 + var6 == var11 + var12) {
                        break;
                     }
                  }

                  var1 += var6;
               }
               break;
            }

            var6 -= var7;
            ++var3;
            var11 += var8;
         }
      }

      if (var4 <= 10) {
         for(var3 = 0; var1 >= var2[var3].limit; ++var3) {
         }
      } else {
         var6 = 0;
         var7 = var4;

         label76:
         while(true) {
            while(true) {
               var3 = var6 + var7 >>> 1;
               if (var1 >= var2[var3].limit) {
                  var6 = var3 + 1;
               } else {
                  if (var3 == 0 || var1 >= var2[var3 - 1].limit) {
                     break label76;
                  }

                  var7 = var3;
               }
            }
         }
      }

      int var5 = var2[var3].start;
      if (var2[var3].isEvenRun()) {
         if (var3 > 0) {
            var1 -= var2[var3 - 1].limit;
         }

         return var5 + var1;
      } else {
         return var5 + var2[var3].limit - var1 - 1;
      }
   }

   static int[] getLogicalMap(Bidi var0) {
      BidiRun[] var1 = var0.runs;
      int[] var6 = new int[var0.length];
      if (var0.length > var0.resultLength) {
         Arrays.fill(var6, -1);
      }

      int var3 = 0;

      int var2;
      int var7;
      for(var7 = 0; var7 < var0.runCount; ++var7) {
         var2 = var1[var7].start;
         int var5 = var1[var7].limit;
         if (var1[var7].isEvenRun()) {
            do {
               var6[var2++] = var3++;
            } while(var3 < var5);
         } else {
            var2 += var5 - var3;

            do {
               --var2;
               var6[var2] = var3++;
            } while(var3 < var5);
         }
      }

      int var4;
      int var8;
      int var9;
      int var10;
      int var11;
      int var12;
      if (var0.insertPoints.size > 0) {
         var7 = 0;
         var8 = var0.runCount;
         var1 = var0.runs;
         var3 = 0;

         for(var11 = 0; var11 < var8; var3 += var9) {
            var9 = var1[var11].limit - var3;
            var10 = var1[var11].insertRemove;
            if ((var10 & 5) > 0) {
               ++var7;
            }

            if (var7 > 0) {
               var2 = var1[var11].start;
               var4 = var2 + var9;

               for(var12 = var2; var12 < var4; ++var12) {
                  var6[var12] += var7;
               }
            }

            if ((var10 & 10) > 0) {
               ++var7;
            }

            ++var11;
         }
      } else if (var0.controlCount > 0) {
         var7 = 0;
         var8 = var0.runCount;
         var1 = var0.runs;
         var3 = 0;

         for(var11 = 0; var11 < var8; var3 += var9) {
            var9 = var1[var11].limit - var3;
            var10 = var1[var11].insertRemove;
            if (var7 - var10 != 0) {
               var2 = var1[var11].start;
               boolean var14 = var1[var11].isEvenRun();
               var4 = var2 + var9;
               if (var10 == 0) {
                  for(var12 = var2; var12 < var4; ++var12) {
                     var6[var12] -= var7;
                  }
               } else {
                  for(var12 = 0; var12 < var9; ++var12) {
                     int var13 = var14 ? var2 + var12 : var4 - var12 - 1;
                     char var15 = var0.text[var13];
                     if (Bidi.IsBidiControlChar(var15)) {
                        ++var7;
                        var6[var13] = -1;
                     } else {
                        var6[var13] -= var7;
                     }
                  }
               }
            }

            ++var11;
         }
      }

      return var6;
   }

   static int[] getVisualMap(Bidi var0) {
      BidiRun[] var1 = var0.runs;
      int var5 = var0.length > var0.resultLength ? var0.length : var0.resultLength;
      int[] var6 = new int[var5];
      int var3 = 0;
      int var7 = 0;

      int var2;
      int var4;
      int var8;
      for(var8 = 0; var8 < var0.runCount; ++var8) {
         var2 = var1[var8].start;
         var4 = var1[var8].limit;
         if (var1[var8].isEvenRun()) {
            do {
               var6[var7++] = var2++;
               ++var3;
            } while(var3 < var4);
         } else {
            var2 += var4 - var3;

            do {
               int var10001 = var7++;
               --var2;
               var6[var10001] = var2;
               ++var3;
            } while(var3 < var4);
         }
      }

      int var9;
      int var10;
      int var11;
      int var12;
      int var13;
      if (var0.insertPoints.size > 0) {
         var8 = 0;
         var9 = var0.runCount;
         var1 = var0.runs;

         for(var11 = 0; var11 < var9; ++var11) {
            var10 = var1[var11].insertRemove;
            if ((var10 & 5) > 0) {
               ++var8;
            }

            if ((var10 & 10) > 0) {
               ++var8;
            }
         }

         var13 = var0.resultLength;

         for(var11 = var9 - 1; var11 >= 0 && var8 > 0; --var11) {
            var10 = var1[var11].insertRemove;
            if ((var10 & 10) > 0) {
               --var13;
               var6[var13] = -1;
               --var8;
            }

            var3 = var11 > 0 ? var1[var11 - 1].limit : 0;

            for(var12 = var1[var11].limit - 1; var12 >= var3 && var8 > 0; --var12) {
               --var13;
               var6[var13] = var6[var12];
            }

            if ((var10 & 5) > 0) {
               --var13;
               var6[var13] = -1;
               --var8;
            }
         }
      } else if (var0.controlCount > 0) {
         var8 = var0.runCount;
         var1 = var0.runs;
         var3 = 0;
         int var14 = 0;

         for(var12 = 0; var12 < var8; var3 += var11) {
            var11 = var1[var12].limit - var3;
            var10 = var1[var12].insertRemove;
            if (var10 == 0 && var14 == var3) {
               var14 += var11;
            } else if (var10 == 0) {
               var4 = var1[var12].limit;

               for(var13 = var3; var13 < var4; ++var13) {
                  var6[var14++] = var6[var13];
               }
            } else {
               var2 = var1[var12].start;
               boolean var17 = var1[var12].isEvenRun();
               var9 = var2 + var11 - 1;

               for(var13 = 0; var13 < var11; ++var13) {
                  int var15 = var17 ? var2 + var13 : var9 - var13;
                  char var16 = var0.text[var15];
                  if (!Bidi.IsBidiControlChar(var16)) {
                     var6[var14++] = var15;
                  }
               }
            }

            ++var12;
         }
      }

      if (var5 == var0.resultLength) {
         return var6;
      } else {
         int[] var18 = new int[var0.resultLength];
         System.arraycopy(var6, 0, var18, 0, var0.resultLength);
         return var18;
      }
   }

   static int[] invertMap(int[] var0) {
      int var1 = var0.length;
      int var2 = -1;
      int var3 = 0;

      int var4;
      int var5;
      for(var4 = 0; var4 < var1; ++var4) {
         var5 = var0[var4];
         if (var5 > var2) {
            var2 = var5;
         }

         if (var5 >= 0) {
            ++var3;
         }
      }

      ++var2;
      int[] var6 = new int[var2];
      if (var3 < var2) {
         Arrays.fill(var6, -1);
      }

      for(var4 = 0; var4 < var1; ++var4) {
         var5 = var0[var4];
         if (var5 >= 0) {
            var6[var5] = var4;
         }
      }

      return var6;
   }
}
