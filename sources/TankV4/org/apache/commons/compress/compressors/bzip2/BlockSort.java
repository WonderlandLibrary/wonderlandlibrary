package org.apache.commons.compress.compressors.bzip2;

import java.util.BitSet;

class BlockSort {
   private static final int QSORT_STACK_SIZE = 1000;
   private static final int FALLBACK_QSORT_STACK_SIZE = 100;
   private static final int STACK_SIZE = 1000;
   private int workDone;
   private int workLimit;
   private boolean firstAttempt;
   private final int[] stack_ll = new int[1000];
   private final int[] stack_hh = new int[1000];
   private final int[] stack_dd = new int[1000];
   private final int[] mainSort_runningOrder = new int[256];
   private final int[] mainSort_copy = new int[256];
   private final boolean[] mainSort_bigDone = new boolean[256];
   private final int[] ftab = new int[65537];
   private final char[] quadrant;
   private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
   private int[] eclass;
   private static final int[] INCS = new int[]{1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484};
   private static final int SMALL_THRESH = 20;
   private static final int DEPTH_THRESH = 10;
   private static final int WORK_FACTOR = 30;
   private static final int SETMASK = 2097152;
   private static final int CLEARMASK = -2097153;

   BlockSort(BZip2CompressorOutputStream.Data var1) {
      this.quadrant = var1.sfmap;
   }

   void blockSort(BZip2CompressorOutputStream.Data var1, int var2) {
      this.workLimit = 30 * var2;
      this.workDone = 0;
      this.firstAttempt = true;
      if (var2 + 1 < 10000) {
         this.fallbackSort(var1, var2);
      } else {
         this.mainSort(var1, var2);
         if (this.firstAttempt && this.workDone > this.workLimit) {
            this.fallbackSort(var1, var2);
         }
      }

      int[] var3 = var1.fmap;
      var1.origPtr = -1;

      for(int var4 = 0; var4 <= var2; ++var4) {
         if (var3[var4] == 0) {
            var1.origPtr = var4;
            break;
         }
      }

   }

   final void fallbackSort(BZip2CompressorOutputStream.Data var1, int var2) {
      var1.block[0] = var1.block[var2 + 1];
      this.fallbackSort(var1.fmap, var1.block, var2 + 1);

      int var3;
      for(var3 = 0; var3 < var2 + 1; ++var3) {
         int var10002 = var1.fmap[var3]--;
      }

      for(var3 = 0; var3 < var2 + 1; ++var3) {
         if (var1.fmap[var3] == -1) {
            var1.fmap[var3] = var2;
            break;
         }
      }

   }

   private void fallbackSimpleSort(int[] var1, int[] var2, int var3, int var4) {
      if (var3 != var4) {
         int var5;
         int var6;
         int var7;
         int var8;
         if (var4 - var3 > 3) {
            for(var6 = var4 - 4; var6 >= var3; --var6) {
               var7 = var1[var6];
               var8 = var2[var7];

               for(var5 = var6 + 4; var5 <= var4 && var8 > var2[var1[var5]]; var5 += 4) {
                  var1[var5 - 4] = var1[var5];
               }

               var1[var5 - 4] = var7;
            }
         }

         for(var6 = var4 - 1; var6 >= var3; --var6) {
            var7 = var1[var6];
            var8 = var2[var7];

            for(var5 = var6 + 1; var5 <= var4 && var8 > var2[var1[var5]]; ++var5) {
               var1[var5 - 1] = var1[var5];
            }

            var1[var5 - 1] = var7;
         }

      }
   }

   private void fswap(int[] var1, int var2, int var3) {
      int var4 = var1[var2];
      var1[var2] = var1[var3];
      var1[var3] = var4;
   }

   private void fvswap(int[] var1, int var2, int var3, int var4) {
      while(var4 > 0) {
         this.fswap(var1, var2, var3);
         ++var2;
         ++var3;
         --var4;
      }

   }

   private int fmin(int var1, int var2) {
      return var1 < var2 ? var1 : var2;
   }

   private void fpush(int var1, int var2, int var3) {
      this.stack_ll[var1] = var2;
      this.stack_hh[var1] = var3;
   }

   private int[] fpop(int var1) {
      return new int[]{this.stack_ll[var1], this.stack_hh[var1]};
   }

   private void fallbackQSort3(int[] var1, int[] var2, int var3, int var4) {
      long var12 = 0L;
      byte var14 = 0;
      int var21 = var14 + 1;
      this.fpush(var14, var3, var4);

      while(true) {
         label69:
         while(var21 > 0) {
            --var21;
            int[] var15 = this.fpop(var21);
            int var5 = var15[0];
            int var8 = var15[1];
            if (var8 - var5 < 10) {
               this.fallbackSimpleSort(var1, var2, var5, var8);
            } else {
               var12 = (var12 * 7621L + 1L) % 32768L;
               long var16 = var12 % 3L;
               long var18;
               if (var16 == 0L) {
                  var18 = (long)var2[var1[var5]];
               } else if (var16 == 1L) {
                  var18 = (long)var2[var1[var5 + var8 >>> 1]];
               } else {
                  var18 = (long)var2[var1[var8]];
               }

               int var7 = var5;
               int var6 = var5;
               int var10 = var8;
               int var9 = var8;

               while(true) {
                  while(true) {
                     int var11;
                     if (var6 <= var9) {
                        var11 = var2[var1[var6]] - (int)var18;
                        if (var11 == 0) {
                           this.fswap(var1, var6, var7);
                           ++var7;
                           ++var6;
                           continue;
                        }

                        if (var11 <= 0) {
                           ++var6;
                           continue;
                        }
                     }

                     while(var6 <= var9) {
                        var11 = var2[var1[var9]] - (int)var18;
                        if (var11 == 0) {
                           this.fswap(var1, var9, var10);
                           --var10;
                           --var9;
                        } else {
                           if (var11 < 0) {
                              break;
                           }

                           --var9;
                        }
                     }

                     if (var6 > var9) {
                        if (var10 >= var7) {
                           var11 = this.fmin(var7 - var5, var6 - var7);
                           this.fvswap(var1, var5, var6 - var11, var11);
                           int var20 = this.fmin(var8 - var10, var10 - var9);
                           this.fvswap(var1, var9 + 1, var8 - var20 + 1, var20);
                           var11 = var5 + var6 - var7 - 1;
                           var20 = var8 - (var10 - var9) + 1;
                           if (var11 - var5 > var8 - var20) {
                              this.fpush(var21++, var5, var11);
                              this.fpush(var21++, var20, var8);
                           } else {
                              this.fpush(var21++, var20, var8);
                              this.fpush(var21++, var5, var11);
                           }
                        }
                        continue label69;
                     }

                     this.fswap(var1, var6, var9);
                     ++var6;
                     --var9;
                  }
               }
            }
         }

         return;
      }
   }

   private int[] getEclass() {
      return this.eclass == null ? (this.eclass = new int[this.quadrant.length / 2]) : this.eclass;
   }

   final void fallbackSort(int[] var1, byte[] var2, int var3) {
      int[] var4 = new int[257];
      int[] var15 = this.getEclass();

      int var6;
      for(var6 = 0; var6 < var3; ++var6) {
         var15[var6] = 0;
      }

      for(var6 = 0; var6 < var3; ++var6) {
         ++var4[var2[var6] & 255];
      }

      for(var6 = 1; var6 < 257; ++var6) {
         var4[var6] += var4[var6 - 1];
      }

      int var7;
      int var8;
      for(var6 = 0; var6 < var3; var1[var8] = var6++) {
         var7 = var2[var6] & 255;
         var8 = var4[var7] - 1;
         var4[var7] = var8;
      }

      int var14 = 64 + var3;
      BitSet var16 = new BitSet(var14);

      for(var6 = 0; var6 < 256; ++var6) {
         var16.set(var4[var6]);
      }

      for(var6 = 0; var6 < 32; ++var6) {
         var16.set(var3 + 2 * var6);
         var16.clear(var3 + 2 * var6 + 1);
      }

      int var5 = 1;

      int var13;
      do {
         var7 = 0;

         for(var6 = 0; var6 < var3; ++var6) {
            if (var16.get(var6)) {
               var7 = var6;
            }

            var8 = var1[var6] - var5;
            if (var8 < 0) {
               var8 += var3;
            }

            var15[var8] = var7;
         }

         var13 = 0;
         int var10 = -1;

         label71:
         while(true) {
            int var9;
            do {
               var8 = var10 + 1;
               var8 = var16.nextClearBit(var8);
               var9 = var8 - 1;
               if (var9 >= var3) {
                  break label71;
               }

               var8 = var16.nextSetBit(var8 + 1);
               var10 = var8 - 1;
               if (var10 >= var3) {
                  break label71;
               }
            } while(var10 <= var9);

            var13 += var10 - var9 + 1;
            this.fallbackQSort3(var1, var15, var9, var10);
            int var11 = -1;

            for(var6 = var9; var6 <= var10; ++var6) {
               int var12 = var15[var1[var6]];
               if (var11 != var12) {
                  var16.set(var6);
                  var11 = var12;
               }
            }
         }

         var5 *= 2;
      } while(var5 <= var3 && var13 != 0);

   }

   private static void vswap(int[] var0, int var1, int var2, int var3) {
      int var4;
      for(var3 += var1; var1 < var3; var0[var2++] = var4) {
         var4 = var0[var1];
         var0[var1++] = var0[var2];
      }

   }

   private static byte med3(byte var0, byte var1, byte var2) {
      return var0 < var1 ? (var1 < var2 ? var1 : (var0 < var2 ? var2 : var0)) : (var1 > var2 ? var1 : (var0 > var2 ? var2 : var0));
   }

   private void mainQSort3(BZip2CompressorOutputStream.Data var1, int var2, int var3, int var4, int var5) {
      int[] var6 = this.stack_ll;
      int[] var7 = this.stack_hh;
      int[] var8 = this.stack_dd;
      int[] var9 = var1.fmap;
      byte[] var10 = var1.block;
      var6[0] = var2;
      var7[0] = var3;
      var8[0] = var4;
      int var11 = 1;

      while(true) {
         label48:
         while(true) {
            --var11;
            if (var11 < 0) {
               return;
            }

            int var12 = var6[var11];
            int var13 = var7[var11];
            int var14 = var8[var11];
            if (var13 - var12 >= 20 && var14 <= 10) {
               int var15 = var14 + 1;
               int var16 = med3(var10[var9[var12] + var15], var10[var9[var13] + var15], var10[var9[var12 + var13 >>> 1] + var15]) & 255;
               int var17 = var12;
               int var18 = var13;
               int var19 = var12;
               int var20 = var13;

               while(true) {
                  while(true) {
                     int var21;
                     int var22;
                     if (var17 <= var18) {
                        var21 = (var10[var9[var17] + var15] & 255) - var16;
                        if (var21 == 0) {
                           var22 = var9[var17];
                           var9[var17++] = var9[var19];
                           var9[var19++] = var22;
                           continue;
                        }

                        if (var21 < 0) {
                           ++var17;
                           continue;
                        }
                     }

                     while(var17 <= var18) {
                        var21 = (var10[var9[var18] + var15] & 255) - var16;
                        if (var21 == 0) {
                           var22 = var9[var18];
                           var9[var18--] = var9[var20];
                           var9[var20--] = var22;
                        } else {
                           if (var21 <= 0) {
                              break;
                           }

                           --var18;
                        }
                     }

                     if (var17 > var18) {
                        if (var20 < var19) {
                           var6[var11] = var12;
                           var7[var11] = var13;
                           var8[var11] = var15;
                           ++var11;
                        } else {
                           var21 = var19 - var12 < var17 - var19 ? var19 - var12 : var17 - var19;
                           vswap(var9, var12, var17 - var21, var21);
                           var22 = var13 - var20 < var20 - var18 ? var13 - var20 : var20 - var18;
                           vswap(var9, var17, var13 - var22 + 1, var22);
                           var21 = var12 + var17 - var19 - 1;
                           var22 = var13 - (var20 - var18) + 1;
                           var6[var11] = var12;
                           var7[var11] = var21;
                           var8[var11] = var14;
                           ++var11;
                           var6[var11] = var21 + 1;
                           var7[var11] = var22 - 1;
                           var8[var11] = var15;
                           ++var11;
                           var6[var11] = var22;
                           var7[var11] = var13;
                           var8[var11] = var14;
                           ++var11;
                        }
                        continue label48;
                     }

                     var21 = var9[var17];
                     var9[var17++] = var9[var18];
                     var9[var18--] = var21;
                  }
               }
            } else if (var14 < var5) {
               return;
            }
         }
      }
   }

   final void mainSort(BZip2CompressorOutputStream.Data var1, int var2) {
      int[] var3 = this.mainSort_runningOrder;
      int[] var4 = this.mainSort_copy;
      boolean[] var5 = this.mainSort_bigDone;
      int[] var6 = this.ftab;
      byte[] var7 = var1.block;
      int[] var8 = var1.fmap;
      char[] var9 = this.quadrant;
      int var10 = this.workLimit;
      boolean var11 = this.firstAttempt;
      int var12 = 65537;

      while(true) {
         --var12;
         if (var12 < 0) {
            for(var12 = 0; var12 < 20; ++var12) {
               var7[var2 + var12 + 2] = var7[var12 % (var2 + 1) + 1];
            }

            var12 = var2 + 20 + 1;

            while(true) {
               --var12;
               if (var12 < 0) {
                  var7[0] = var7[var2 + 1];
                  var12 = var7[0] & 255;

                  int var13;
                  int var14;
                  for(var13 = 0; var13 <= var2; ++var13) {
                     var14 = var7[var13 + 1] & 255;
                     ++var6[(var12 << 8) + var14];
                     var12 = var14;
                  }

                  for(var13 = 1; var13 <= 65536; ++var13) {
                     var6[var13] += var6[var13 - 1];
                  }

                  var12 = var7[1] & 255;

                  for(var13 = 0; var13 < var2; ++var13) {
                     var14 = var7[var13 + 2] & 255;
                     var8[--var6[(var12 << 8) + var14]] = var13;
                     var12 = var14;
                  }

                  var8[--var6[((var7[var2 + 1] & 255) << 8) + (var7[1] & 255)]] = var2;
                  var13 = 256;

                  while(true) {
                     --var13;
                     if (var13 < 0) {
                        var13 = 364;

                        int var15;
                        int var16;
                        int var17;
                        int var18;
                        int var19;
                        while(var13 != 1) {
                           var13 /= 3;

                           for(var14 = var13; var14 <= 255; ++var14) {
                              var15 = var3[var14];
                              var16 = var6[var15 + 1 << 8] - var6[var15 << 8];
                              var17 = var13 - 1;
                              var18 = var14;

                              for(var19 = var3[var14 - var13]; var6[var19 + 1 << 8] - var6[var19 << 8] > var16; var19 = var3[var18 - var13]) {
                                 var3[var18] = var19;
                                 var18 -= var13;
                                 if (var18 <= var17) {
                                    break;
                                 }
                              }

                              var3[var18] = var15;
                           }
                        }

                        for(var13 = 0; var13 <= 255; ++var13) {
                           var14 = var3[var13];

                           for(var15 = 0; var15 <= 255; ++var15) {
                              var16 = (var14 << 8) + var15;
                              var17 = var6[var16];
                              if ((var17 & 2097152) != 2097152) {
                                 var18 = var17 & -2097153;
                                 var19 = (var6[var16 + 1] & -2097153) - 1;
                                 if (var19 > var18) {
                                    this.mainQSort3(var1, var18, var19, 2, var2);
                                    if (var11 && this.workDone > var10) {
                                       return;
                                    }
                                 }

                                 var6[var16] = var17 | 2097152;
                              }
                           }

                           for(var15 = 0; var15 <= 255; ++var15) {
                              var4[var15] = var6[(var15 << 8) + var14] & -2097153;
                           }

                           var15 = var6[var14 << 8] & -2097153;

                           for(var16 = var6[var14 + 1 << 8] & -2097153; var15 < var16; ++var15) {
                              var17 = var8[var15];
                              var12 = var7[var17] & 255;
                              if (!var5[var12]) {
                                 var8[var4[var12]] = var17 == 0 ? var2 : var17 - 1;
                                 int var10002 = var4[var12]++;
                              }
                           }

                           var15 = 256;

                           while(true) {
                              --var15;
                              if (var15 < 0) {
                                 var5[var14] = true;
                                 if (var13 < 255) {
                                    var15 = var6[var14 << 8] & -2097153;
                                    var16 = (var6[var14 + 1 << 8] & -2097153) - var15;

                                    for(var17 = 0; var16 >> var17 > 65534; ++var17) {
                                    }

                                    for(var18 = 0; var18 < var16; ++var18) {
                                       var19 = var8[var15 + var18];
                                       char var20 = (char)(var18 >> var17);
                                       var9[var19] = var20;
                                       if (var19 < 20) {
                                          var9[var19 + var2 + 1] = var20;
                                       }
                                    }
                                 }
                                 break;
                              }

                              var6[(var15 << 8) + var14] |= 2097152;
                           }
                        }

                        return;
                     }

                     var5[var13] = false;
                     var3[var13] = var13;
                  }
               }

               var9[var12] = 0;
            }
         }

         var6[var12] = 0;
      }
   }
}
