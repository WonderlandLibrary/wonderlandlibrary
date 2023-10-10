package com.jcraft.jorbis;

class Drft {
   int n;
   float[] trigcache;
   int[] splitcache;
   static int[] ntryh = new int[]{4, 2, 3, 5};
   static float tpi = 6.2831855F;
   static float hsqt2 = 0.70710677F;
   static float taui = 0.8660254F;
   static float taur = -0.5F;
   static float sqrt2 = 1.4142135F;

   void backward(float[] var1) {
      if (this.n != 1) {
         drftb1(this.n, var1, this.trigcache, this.trigcache, this.n, this.splitcache);
      }
   }

   void init(int var1) {
      this.n = var1;
      this.trigcache = new float[3 * var1];
      this.splitcache = new int[32];
      fdrffti(var1, this.trigcache, this.splitcache);
   }

   void clear() {
      if (this.trigcache != null) {
         this.trigcache = null;
      }

      if (this.splitcache != null) {
         this.splitcache = null;
      }

   }

   static void drfti1(int var0, float[] var1, int var2, int[] var3) {
      int var8 = 0;
      int var10 = -1;
      int var24 = var0;
      int var25 = 0;
      byte var26 = 101;

      while(true) {
         while(true) {
            int var9;
            switch(var26) {
            case 101:
               ++var10;
               if (var10 < 4) {
                  var8 = ntryh[var10];
               } else {
                  var8 += 2;
               }
            case 104:
               int var19 = var24 / var8;
               int var20 = var24 - var8 * var19;
               if (var20 != 0) {
                  var26 = 101;
                  break;
               } else {
                  ++var25;
                  var3[var25 + 1] = var8;
                  var24 = var19;
                  if (var8 != 2) {
                     var26 = 107;
                     break;
                  } else if (var25 == 1) {
                     var26 = 107;
                     break;
                  } else {
                     for(var9 = 1; var9 < var25; ++var9) {
                        int var14 = var25 - var9 + 1;
                        var3[var14 + 1] = var3[var14];
                     }

                     var3[2] = 2;
                  }
               }
            case 107:
               if (var24 == 1) {
                  var3[0] = var0;
                  var3[1] = var25;
                  float var5 = tpi / (float)var0;
                  int var18 = 0;
                  int var23 = var25 - 1;
                  int var12 = 1;
                  if (var23 == 0) {
                     return;
                  }

                  for(int var11 = 0; var11 < var23; ++var11) {
                     int var17 = var3[var11 + 2];
                     int var15 = 0;
                     int var13 = var12 * var17;
                     int var21 = var0 / var13;
                     int var22 = var17 - 1;

                     for(var10 = 0; var10 < var22; ++var10) {
                        var15 += var12;
                        var9 = var18;
                        float var6 = (float)var15 * var5;
                        float var7 = 0.0F;

                        for(int var16 = 2; var16 < var21; var16 += 2) {
                           ++var7;
                           float var4 = var7 * var6;
                           var1[var2 + var9++] = (float)Math.cos((double)var4);
                           var1[var2 + var9++] = (float)Math.sin((double)var4);
                        }

                        var18 += var21;
                     }

                     var12 = var13;
                  }

                  return;
               }

               var26 = 104;
            }
         }
      }
   }

   static void fdrffti(int var0, float[] var1, int[] var2) {
      if (var0 != 1) {
         drfti1(var0, var1, var0, var2);
      }
   }

   static void dradf2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
      int var11 = 0;
      int var12;
      int var10 = var12 = var1 * var0;
      int var13 = var0 << 1;

      int var7;
      for(var7 = 0; var7 < var1; ++var7) {
         var3[var11 << 1] = var2[var11] + var2[var12];
         var3[(var11 << 1) + var13 - 1] = var2[var11] - var2[var12];
         var11 += var0;
         var12 += var0;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var11 = 0;
            var12 = var10;

            for(var7 = 0; var7 < var1; ++var7) {
               var13 = var12;
               int var14 = (var11 << 1) + (var0 << 1);
               int var15 = var11;
               int var16 = var11 + var11;

               for(int var6 = 2; var6 < var0; var6 += 2) {
                  var13 += 2;
                  var14 -= 2;
                  var15 += 2;
                  var16 += 2;
                  float var9 = var4[var5 + var6 - 2] * var2[var13 - 1] + var4[var5 + var6 - 1] * var2[var13];
                  float var8 = var4[var5 + var6 - 2] * var2[var13] - var4[var5 + var6 - 1] * var2[var13 - 1];
                  var3[var16] = var2[var15] + var8;
                  var3[var14] = var8 - var2[var15];
                  var3[var16 - 1] = var2[var15 - 1] + var9;
                  var3[var14 - 1] = var2[var15 - 1] - var9;
               }

               var11 += var0;
               var12 += var0;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var11 = var0;
         var13 = var12 = var0 - 1;
         var12 += var10;

         for(var7 = 0; var7 < var1; ++var7) {
            var3[var11] = -var2[var12];
            var3[var11 - 1] = var2[var13];
            var11 += var0 << 1;
            var12 += var0;
            var13 += var0;
         }

      }
   }

   static void dradf4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      int var12 = var1 * var0;
      int var13 = var12;
      int var16 = var12 << 1;
      int var14 = var12 + (var12 << 1);
      int var15 = 0;

      int var11;
      int var17;
      float var29;
      float var30;
      for(var11 = 0; var11 < var1; ++var11) {
         var29 = var2[var13] + var2[var14];
         var30 = var2[var15] + var2[var16];
         var3[var17 = var15 << 2] = var29 + var30;
         var3[(var0 << 2) + var17 - 1] = var30 - var29;
         var3[(var17 += var0 << 1) - 1] = var2[var15] - var2[var16];
         var3[var17] = var2[var14] - var2[var13];
         var13 += var0;
         var14 += var0;
         var15 += var0;
         var16 += var0;
      }

      if (var0 >= 2) {
         int var18;
         float var25;
         if (var0 != 2) {
            var13 = 0;

            for(var11 = 0; var11 < var1; ++var11) {
               var14 = var13;
               var16 = var13 << 2;
               var17 = (var18 = var0 << 1) + var16;

               for(int var10 = 2; var10 < var0; var10 += 2) {
                  var14 += 2;
                  var16 += 2;
                  var17 -= 2;
                  var15 = var14 + var12;
                  float var22 = var4[var5 + var10 - 2] * var2[var15 - 1] + var4[var5 + var10 - 1] * var2[var15];
                  float var19 = var4[var5 + var10 - 2] * var2[var15] - var4[var5 + var10 - 1] * var2[var15 - 1];
                  var15 += var12;
                  float var23 = var6[var7 + var10 - 2] * var2[var15 - 1] + var6[var7 + var10 - 1] * var2[var15];
                  float var20 = var6[var7 + var10 - 2] * var2[var15] - var6[var7 + var10 - 1] * var2[var15 - 1];
                  var15 += var12;
                  float var24 = var8[var9 + var10 - 2] * var2[var15 - 1] + var8[var9 + var10 - 1] * var2[var15];
                  float var21 = var8[var9 + var10 - 2] * var2[var15] - var8[var9 + var10 - 1] * var2[var15 - 1];
                  var29 = var22 + var24;
                  float var32 = var24 - var22;
                  var25 = var19 + var21;
                  float var28 = var19 - var21;
                  float var26 = var2[var14] + var20;
                  float var27 = var2[var14] - var20;
                  var30 = var2[var14 - 1] + var23;
                  float var31 = var2[var14 - 1] - var23;
                  var3[var16 - 1] = var29 + var30;
                  var3[var16] = var25 + var26;
                  var3[var17 - 1] = var31 - var28;
                  var3[var17] = var32 - var27;
                  var3[var16 + var18 - 1] = var28 + var31;
                  var3[var16 + var18] = var32 + var27;
                  var3[var17 + var18 - 1] = var30 - var29;
                  var3[var17 + var18] = var25 - var26;
               }

               var13 += var0;
            }

            if ((var0 & 1) != 0) {
               return;
            }
         }

         var14 = (var13 = var12 + var0 - 1) + (var12 << 1);
         var15 = var0 << 2;
         var16 = var0;
         var17 = var0 << 1;
         var18 = var0;

         for(var11 = 0; var11 < var1; ++var11) {
            var25 = -hsqt2 * (var2[var13] + var2[var14]);
            var29 = hsqt2 * (var2[var13] - var2[var14]);
            var3[var16 - 1] = var29 + var2[var18 - 1];
            var3[var16 + var17 - 1] = var2[var18 - 1] - var29;
            var3[var16] = var25 - var2[var13 + var12];
            var3[var16 + var17] = var25 + var2[var13 + var12];
            var13 += var0;
            var14 += var0;
            var16 += var15;
            var18 += var0;
         }

      }
   }

   static void dradfg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
      int var22 = 0;
      float var38 = 0.0F;
      float var40 = 0.0F;
      float var39 = tpi / (float)var1;
      var38 = (float)Math.cos((double)var39);
      var40 = (float)Math.sin((double)var39);
      int var12 = var1 + 1 >> 1;
      int var44 = var1;
      int var43 = var0;
      int var37 = var0 - 1 >> 1;
      int var20 = var2 * var0;
      int var30 = var1 * var0;
      short var45 = 100;

      while(true) {
         int var13;
         int var14;
         int var15;
         int var18;
         int var21;
         int var23;
         int var24;
         int var25;
         int var26;
         int var27;
         int var28;
         int var29;
         switch(var45) {
         case 101:
            if (var0 == 1) {
               var45 = 119;
               break;
            } else {
               for(var18 = 0; var18 < var3; ++var18) {
                  var8[var18] = var6[var18];
               }

               var21 = 0;

               for(var14 = 1; var14 < var1; ++var14) {
                  var21 += var20;
                  var22 = var21;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var7[var22] = var5[var22];
                     var22 += var0;
                  }
               }

               int var19 = -var0;
               var21 = 0;
               int var11;
               if (var37 > var2) {
                  for(var14 = 1; var14 < var1; ++var14) {
                     var21 += var20;
                     var19 += var0;
                     var22 = -var0 + var21;

                     for(var15 = 0; var15 < var2; ++var15) {
                        var11 = var19 - 1;
                        var22 += var0;
                        var23 = var22;

                        for(var13 = 2; var13 < var0; var13 += 2) {
                           var11 += 2;
                           var23 += 2;
                           var7[var23 - 1] = var9[var10 + var11 - 1] * var5[var23 - 1] + var9[var10 + var11] * var5[var23];
                           var7[var23] = var9[var10 + var11 - 1] * var5[var23] - var9[var10 + var11] * var5[var23 - 1];
                        }
                     }
                  }
               } else {
                  for(var14 = 1; var14 < var1; ++var14) {
                     var19 += var0;
                     var11 = var19 - 1;
                     var21 += var20;
                     var22 = var21;

                     for(var13 = 2; var13 < var0; var13 += 2) {
                        var11 += 2;
                        var22 += 2;
                        var23 = var22;

                        for(var15 = 0; var15 < var2; ++var15) {
                           var7[var23 - 1] = var9[var10 + var11 - 1] * var5[var23 - 1] + var9[var10 + var11] * var5[var23];
                           var7[var23] = var9[var10 + var11 - 1] * var5[var23] - var9[var10 + var11] * var5[var23 - 1];
                           var23 += var0;
                        }
                     }
                  }
               }

               var21 = 0;
               var22 = var44 * var20;
               if (var37 < var2) {
                  for(var14 = 1; var14 < var12; ++var14) {
                     var21 += var20;
                     var22 -= var20;
                     var23 = var21;
                     var24 = var22;

                     for(var13 = 2; var13 < var0; var13 += 2) {
                        var23 += 2;
                        var24 += 2;
                        var25 = var23 - var0;
                        var26 = var24 - var0;

                        for(var15 = 0; var15 < var2; ++var15) {
                           var25 += var0;
                           var26 += var0;
                           var5[var25 - 1] = var7[var25 - 1] + var7[var26 - 1];
                           var5[var26 - 1] = var7[var25] - var7[var26];
                           var5[var25] = var7[var25] + var7[var26];
                           var5[var26] = var7[var26 - 1] - var7[var25 - 1];
                        }
                     }
                  }
               } else {
                  for(var14 = 1; var14 < var12; ++var14) {
                     var21 += var20;
                     var22 -= var20;
                     var23 = var21;
                     var24 = var22;

                     for(var15 = 0; var15 < var2; ++var15) {
                        var25 = var23;
                        var26 = var24;

                        for(var13 = 2; var13 < var0; var13 += 2) {
                           var25 += 2;
                           var26 += 2;
                           var5[var25 - 1] = var7[var25 - 1] + var7[var26 - 1];
                           var5[var26 - 1] = var7[var25] - var7[var26];
                           var5[var25] = var7[var25] + var7[var26];
                           var5[var26] = var7[var26 - 1] - var7[var25 - 1];
                        }

                        var23 += var0;
                        var24 += var0;
                     }
                  }
               }
            }
         case 119:
            for(var18 = 0; var18 < var3; ++var18) {
               var6[var18] = var8[var18];
            }

            var21 = 0;
            var22 = var44 * var3;

            for(var14 = 1; var14 < var12; ++var14) {
               var21 += var20;
               var22 -= var20;
               var23 = var21 - var0;
               var24 = var22 - var0;

               for(var15 = 0; var15 < var2; ++var15) {
                  var23 += var0;
                  var24 += var0;
                  var5[var23] = var7[var23] + var7[var24];
                  var5[var24] = var7[var24] - var7[var23];
               }
            }

            float var34 = 1.0F;
            float var32 = 0.0F;
            var21 = 0;
            var22 = var44 * var3;
            var23 = (var1 - 1) * var3;

            for(int var16 = 1; var16 < var12; ++var16) {
               var21 += var3;
               var22 -= var3;
               float var41 = var38 * var34 - var40 * var32;
               var32 = var38 * var32 + var40 * var34;
               var34 = var41;
               var24 = var21;
               var25 = var22;
               var26 = var23;
               var27 = var3;

               for(var18 = 0; var18 < var3; ++var18) {
                  var8[var24++] = var6[var18] + var34 * var6[var27++];
                  var8[var25++] = var32 * var6[var26++];
               }

               float var31 = var34;
               float var36 = var32;
               float var35 = var34;
               float var33 = var32;
               var24 = var3;
               var25 = (var44 - 1) * var3;

               for(var14 = 2; var14 < var12; ++var14) {
                  var24 += var3;
                  var25 -= var3;
                  float var42 = var31 * var35 - var36 * var33;
                  var33 = var31 * var33 + var36 * var35;
                  var35 = var42;
                  var26 = var21;
                  var27 = var22;
                  var28 = var24;
                  var29 = var25;

                  for(var18 = 0; var18 < var3; ++var18) {
                     int var10001 = var26++;
                     var8[var10001] += var35 * var6[var28++];
                     var10001 = var27++;
                     var8[var10001] += var33 * var6[var29++];
                  }
               }
            }

            var21 = 0;

            for(var14 = 1; var14 < var12; ++var14) {
               var21 += var3;
               var22 = var21;

               for(var18 = 0; var18 < var3; ++var18) {
                  var8[var18] += var6[var22++];
               }
            }

            if (var0 < var2) {
               var45 = 132;
               break;
            }

            var21 = 0;
            var22 = 0;

            for(var15 = 0; var15 < var2; ++var15) {
               var23 = var21;
               var24 = var22;

               for(var13 = 0; var13 < var0; ++var13) {
                  var4[var24++] = var7[var23++];
               }

               var21 += var0;
               var22 += var30;
            }

            var45 = 135;
            break;
         case 132:
            for(var13 = 0; var13 < var0; ++var13) {
               var21 = var13;
               var22 = var13;

               for(var15 = 0; var15 < var2; ++var15) {
                  var4[var22] = var7[var21];
                  var21 += var0;
                  var22 += var30;
               }
            }
         case 135:
            var21 = 0;
            var22 = var0 << 1;
            var23 = 0;
            var24 = var44 * var20;

            for(var14 = 1; var14 < var12; ++var14) {
               var21 += var22;
               var23 += var20;
               var24 -= var20;
               var25 = var21;
               var26 = var23;
               var27 = var24;

               for(var15 = 0; var15 < var2; ++var15) {
                  var4[var25 - 1] = var7[var26];
                  var4[var25] = var7[var27];
                  var25 += var30;
                  var26 += var0;
                  var27 += var0;
               }
            }

            if (var0 == 1) {
               return;
            }

            if (var37 >= var2) {
               var21 = -var0;
               var23 = 0;
               var24 = 0;
               var25 = var44 * var20;

               for(var14 = 1; var14 < var12; ++var14) {
                  var21 += var22;
                  var23 += var22;
                  var24 += var20;
                  var25 -= var20;
                  var26 = var21;
                  var27 = var23;
                  var28 = var24;
                  var29 = var25;

                  for(var15 = 0; var15 < var2; ++var15) {
                     for(var13 = 2; var13 < var0; var13 += 2) {
                        int var17 = var43 - var13;
                        var4[var13 + var27 - 1] = var7[var13 + var28 - 1] + var7[var13 + var29 - 1];
                        var4[var17 + var26 - 1] = var7[var13 + var28 - 1] - var7[var13 + var29 - 1];
                        var4[var13 + var27] = var7[var13 + var28] + var7[var13 + var29];
                        var4[var17 + var26] = var7[var13 + var29] - var7[var13 + var28];
                     }

                     var26 += var30;
                     var27 += var30;
                     var28 += var0;
                     var29 += var0;
                  }
               }

               return;
            }

            var45 = 141;
            break;
         case 141:
            var21 = -var0;
            var23 = 0;
            var24 = 0;
            var25 = var44 * var20;

            for(var14 = 1; var14 < var12; ++var14) {
               var21 += var22;
               var23 += var22;
               var24 += var20;
               var25 -= var20;

               for(var13 = 2; var13 < var0; var13 += 2) {
                  var26 = var43 + var21 - var13;
                  var27 = var13 + var23;
                  var28 = var13 + var24;
                  var29 = var13 + var25;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var4[var27 - 1] = var7[var28 - 1] + var7[var29 - 1];
                     var4[var26 - 1] = var7[var28 - 1] - var7[var29 - 1];
                     var4[var27] = var7[var28] + var7[var29];
                     var4[var26] = var7[var29] - var7[var28];
                     var26 += var30;
                     var27 += var30;
                     var28 += var0;
                     var29 += var0;
                  }
               }
            }

            return;
         }
      }
   }

   static void drftf1(int var0, float[] var1, float[] var2, float[] var3, int[] var4) {
      int var11 = var4[1];
      int var9 = 1;
      int var8 = var0;
      int var13 = var0;

      for(int var6 = 0; var6 < var11; ++var6) {
         int var10 = var11 - var6;
         int var12 = var4[var10 + 1];
         int var7 = var8 / var12;
         int var14 = var0 / var8;
         int var15 = var14 * var7;
         var13 -= (var12 - 1) * var14;
         var9 = 1 - var9;
         byte var18 = 100;

         label62:
         while(true) {
            switch(var18) {
            case 100:
               if (var12 != 4) {
                  var18 = 102;
               } else {
                  int var16 = var13 + var14;
                  int var17 = var16 + var14;
                  if (var9 != 0) {
                     dradf4(var14, var7, var2, var1, var3, var13 - 1, var3, var16 - 1, var3, var17 - 1);
                  } else {
                     dradf4(var14, var7, var1, var2, var3, var13 - 1, var3, var16 - 1, var3, var17 - 1);
                  }

                  var18 = 110;
               }
            case 101:
            case 105:
            case 106:
            case 107:
            case 108:
            default:
               break;
            case 102:
               if (var12 != 2) {
                  var18 = 104;
               } else if (var9 != 0) {
                  var18 = 103;
               } else {
                  dradf2(var14, var7, var1, var2, var3, var13 - 1);
                  var18 = 110;
               }
               break;
            case 103:
               dradf2(var14, var7, var2, var1, var3, var13 - 1);
            case 104:
               if (var14 == 1) {
                  var9 = 1 - var9;
               }

               if (var9 != 0) {
                  var18 = 109;
               } else {
                  dradfg(var14, var12, var7, var15, var1, var1, var1, var2, var2, var3, var13 - 1);
                  var9 = 1;
                  var18 = 110;
               }
               break;
            case 109:
               dradfg(var14, var12, var7, var15, var2, var2, var2, var1, var1, var3, var13 - 1);
               var9 = 0;
            case 110:
               break label62;
            }
         }

         var8 = var7;
      }

      if (var9 != 1) {
         for(int var5 = 0; var5 < var0; ++var5) {
            var1[var5] = var2[var5];
         }

      }
   }

   static void dradb2(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5) {
      int var8 = var1 * var0;
      int var9 = 0;
      int var10 = 0;
      int var11 = (var0 << 1) - 1;

      int var7;
      for(var7 = 0; var7 < var1; ++var7) {
         var3[var9] = var2[var10] + var2[var11 + var10];
         var3[var9 + var8] = var2[var10] - var2[var11 + var10];
         var10 = (var9 += var0) << 1;
      }

      if (var0 >= 2) {
         if (var0 != 2) {
            var9 = 0;
            var10 = 0;

            for(var7 = 0; var7 < var1; ++var7) {
               var11 = var9;
               int var12 = var10;
               int var13 = var10 + (var0 << 1);
               int var14 = var8 + var9;

               for(int var6 = 2; var6 < var0; var6 += 2) {
                  var11 += 2;
                  var12 += 2;
                  var13 -= 2;
                  var14 += 2;
                  var3[var11 - 1] = var2[var12 - 1] + var2[var13 - 1];
                  float var16 = var2[var12 - 1] - var2[var13 - 1];
                  var3[var11] = var2[var12] - var2[var13];
                  float var15 = var2[var12] + var2[var13];
                  var3[var14 - 1] = var4[var5 + var6 - 2] * var16 - var4[var5 + var6 - 1] * var15;
                  var3[var14] = var4[var5 + var6 - 2] * var15 + var4[var5 + var6 - 1] * var16;
               }

               var10 = (var9 += var0) << 1;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var9 = var0 - 1;
         var10 = var0 - 1;

         for(var7 = 0; var7 < var1; ++var7) {
            var3[var9] = var2[var10] + var2[var10];
            var3[var9 + var8] = -(var2[var10 + 1] + var2[var10 + 1]);
            var9 += var0;
            var10 += var0 << 1;
         }

      }
   }

   static void dradb3(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7) {
      int var10 = var1 * var0;
      int var11 = 0;
      int var12 = var10 << 1;
      int var13 = var0 << 1;
      int var14 = var0 + (var0 << 1);
      int var15 = 0;

      int var9;
      float var22;
      float var25;
      float var30;
      for(var9 = 0; var9 < var1; ++var9) {
         var30 = var2[var13 - 1] + var2[var13 - 1];
         var25 = var2[var15] + taur * var30;
         var3[var11] = var2[var15] + var30;
         var22 = taui * (var2[var13] + var2[var13]);
         var3[var11 + var10] = var25 - var22;
         var3[var11 + var12] = var25 + var22;
         var11 += var0;
         var13 += var14;
         var15 += var14;
      }

      if (var0 != 1) {
         var11 = 0;
         var13 = var0 << 1;

         for(var9 = 0; var9 < var1; ++var9) {
            int var17 = var11 + (var11 << 1);
            int var16 = var15 = var17 + var13;
            int var18 = var11;
            int var19;
            int var20 = (var19 = var11 + var10) + var10;

            for(int var8 = 2; var8 < var0; var8 += 2) {
               var15 += 2;
               var16 -= 2;
               var17 += 2;
               var18 += 2;
               var19 += 2;
               var20 += 2;
               var30 = var2[var15 - 1] + var2[var16 - 1];
               var25 = var2[var17 - 1] + taur * var30;
               var3[var18 - 1] = var2[var17 - 1] + var30;
               float var29 = var2[var15] - var2[var16];
               float var21 = var2[var17] + taur * var29;
               var3[var18] = var2[var17] + var29;
               float var26 = taui * (var2[var15 - 1] - var2[var16 - 1]);
               var22 = taui * (var2[var15] + var2[var16]);
               float var27 = var25 - var22;
               float var28 = var25 + var22;
               float var23 = var21 + var26;
               float var24 = var21 - var26;
               var3[var19 - 1] = var4[var5 + var8 - 2] * var27 - var4[var5 + var8 - 1] * var23;
               var3[var19] = var4[var5 + var8 - 2] * var23 + var4[var5 + var8 - 1] * var27;
               var3[var20 - 1] = var6[var7 + var8 - 2] * var28 - var6[var7 + var8 - 1] * var24;
               var3[var20] = var6[var7 + var8 - 2] * var24 + var6[var7 + var8 - 1] * var28;
            }

            var11 += var0;
         }

      }
   }

   static void dradb4(int var0, int var1, float[] var2, float[] var3, float[] var4, int var5, float[] var6, int var7, float[] var8, int var9) {
      int var12 = var1 * var0;
      int var13 = 0;
      int var14 = var0 << 2;
      int var15 = 0;
      int var18 = var0 << 1;

      int var11;
      int var16;
      int var17;
      float var31;
      float var32;
      float var33;
      float var34;
      for(var11 = 0; var11 < var1; ++var11) {
         var16 = var15 + var18;
         var33 = var2[var16 - 1] + var2[var16 - 1];
         var34 = var2[var16] + var2[var16];
         var31 = var2[var15] - var2[(var16 += var18) - 1];
         var32 = var2[var15] + var2[var16 - 1];
         var3[var13] = var32 + var33;
         var3[var17 = var13 + var12] = var31 - var34;
         var3[var17 += var12] = var32 - var33;
         var3[var17 + var12] = var31 + var34;
         var13 += var0;
         var15 += var14;
      }

      if (var0 >= 2) {
         float var27;
         float var28;
         if (var0 != 2) {
            var13 = 0;

            for(var11 = 0; var11 < var1; ++var11) {
               var17 = (var16 = var15 = (var14 = var13 << 2) + var18) + var18;
               int var19 = var13;

               for(int var10 = 2; var10 < var0; var10 += 2) {
                  var14 += 2;
                  var15 += 2;
                  var16 -= 2;
                  var17 -= 2;
                  var19 += 2;
                  var27 = var2[var14] + var2[var17];
                  var28 = var2[var14] - var2[var17];
                  float var29 = var2[var15] - var2[var16];
                  var34 = var2[var15] + var2[var16];
                  var31 = var2[var14 - 1] - var2[var17 - 1];
                  var32 = var2[var14 - 1] + var2[var17 - 1];
                  float var30 = var2[var15 - 1] - var2[var16 - 1];
                  var33 = var2[var15 - 1] + var2[var16 - 1];
                  var3[var19 - 1] = var32 + var33;
                  float var25 = var32 - var33;
                  var3[var19] = var28 + var29;
                  float var22 = var28 - var29;
                  float var24 = var31 - var34;
                  float var26 = var31 + var34;
                  float var21 = var27 + var30;
                  float var23 = var27 - var30;
                  int var20;
                  var3[(var20 = var19 + var12) - 1] = var4[var5 + var10 - 2] * var24 - var4[var5 + var10 - 1] * var21;
                  var3[var20] = var4[var5 + var10 - 2] * var21 + var4[var5 + var10 - 1] * var24;
                  var3[(var20 += var12) - 1] = var6[var7 + var10 - 2] * var25 - var6[var7 + var10 - 1] * var22;
                  var3[var20] = var6[var7 + var10 - 2] * var22 + var6[var7 + var10 - 1] * var25;
                  var3[(var20 += var12) - 1] = var8[var9 + var10 - 2] * var26 - var8[var9 + var10 - 1] * var23;
                  var3[var20] = var8[var9 + var10 - 2] * var23 + var8[var9 + var10 - 1] * var26;
               }

               var13 += var0;
            }

            if (var0 % 2 == 1) {
               return;
            }
         }

         var13 = var0;
         var14 = var0 << 2;
         var15 = var0 - 1;
         var16 = var0 + (var0 << 1);

         for(var11 = 0; var11 < var1; ++var11) {
            var27 = var2[var13] + var2[var16];
            var28 = var2[var16] - var2[var13];
            var31 = var2[var13 - 1] - var2[var16 - 1];
            var32 = var2[var13 - 1] + var2[var16 - 1];
            var3[var15] = var32 + var32;
            var3[var17 = var15 + var12] = sqrt2 * (var31 - var27);
            var3[var17 += var12] = var28 + var28;
            var3[var17 + var12] = -sqrt2 * (var31 + var27);
            var15 += var0;
            var13 += var14;
            var16 += var14;
         }

      }
   }

   static void dradbg(int var0, int var1, int var2, int var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float[] var9, int var10) {
      int var12 = 0;
      int var19 = 0;
      int var29 = 0;
      int var38 = 0;
      float var39 = 0.0F;
      float var41 = 0.0F;
      int var44 = 0;
      short var45 = 100;

      while(true) {
         int var11;
         int var13;
         int var14;
         int var15;
         int var17;
         int var18;
         int var20;
         int var21;
         int var22;
         int var23;
         int var24;
         int var25;
         int var26;
         int var27;
         int var28;
         int var30;
         int var31;
         switch(var45) {
         case 100:
            var29 = var1 * var0;
            var19 = var2 * var0;
            float var40 = tpi / (float)var1;
            var39 = (float)Math.cos((double)var40);
            var41 = (float)Math.sin((double)var40);
            var38 = var0 - 1 >>> 1;
            var44 = var1;
            var12 = var1 + 1 >>> 1;
            if (var0 < var2) {
               var45 = 103;
               break;
            }

            var20 = 0;
            var21 = 0;

            for(var15 = 0; var15 < var2; ++var15) {
               var22 = var20;
               var23 = var21;

               for(var13 = 0; var13 < var0; ++var13) {
                  var7[var22] = var4[var23];
                  ++var22;
                  ++var23;
               }

               var20 += var0;
               var21 += var29;
            }

            var45 = 106;
            break;
         case 103:
            var20 = 0;

            for(var13 = 0; var13 < var0; ++var13) {
               var21 = var20;
               var22 = var20;

               for(var15 = 0; var15 < var2; ++var15) {
                  var7[var21] = var4[var22];
                  var21 += var0;
                  var22 += var29;
               }

               ++var20;
            }
         case 106:
            var20 = 0;
            var21 = var44 * var19;
            var26 = var24 = var0 << 1;

            for(var14 = 1; var14 < var12; ++var14) {
               var20 += var19;
               var21 -= var19;
               var22 = var20;
               var23 = var21;
               var25 = var24;

               for(var15 = 0; var15 < var2; ++var15) {
                  var7[var22] = var4[var25 - 1] + var4[var25 - 1];
                  var7[var23] = var4[var25] + var4[var25];
                  var22 += var0;
                  var23 += var0;
                  var25 += var29;
               }

               var24 += var26;
            }

            if (var0 == 1) {
               var45 = 116;
            } else {
               if (var38 < var2) {
                  var45 = 112;
                  break;
               }

               var20 = 0;
               var21 = var44 * var19;
               var26 = 0;

               for(var14 = 1; var14 < var12; ++var14) {
                  var20 += var19;
                  var21 -= var19;
                  var22 = var20;
                  var23 = var21;
                  var26 += var0 << 1;
                  var27 = var26;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var24 = var22;
                     var25 = var23;
                     var28 = var27;
                     var30 = var27;

                     for(var13 = 2; var13 < var0; var13 += 2) {
                        var24 += 2;
                        var25 += 2;
                        var28 += 2;
                        var30 -= 2;
                        var7[var24 - 1] = var4[var28 - 1] + var4[var30 - 1];
                        var7[var25 - 1] = var4[var28 - 1] - var4[var30 - 1];
                        var7[var24] = var4[var28] - var4[var30];
                        var7[var25] = var4[var28] + var4[var30];
                     }

                     var22 += var0;
                     var23 += var0;
                     var27 += var29;
                  }
               }

               var45 = 116;
            }
            break;
         case 112:
            var20 = 0;
            var21 = var44 * var19;
            var26 = 0;

            for(var14 = 1; var14 < var12; ++var14) {
               var20 += var19;
               var21 -= var19;
               var22 = var20;
               var23 = var21;
               var26 += var0 << 1;
               var27 = var26;
               var28 = var26;

               for(var13 = 2; var13 < var0; var13 += 2) {
                  var22 += 2;
                  var23 += 2;
                  var27 += 2;
                  var28 -= 2;
                  var24 = var22;
                  var25 = var23;
                  var30 = var27;
                  var31 = var28;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var7[var24 - 1] = var4[var30 - 1] + var4[var31 - 1];
                     var7[var25 - 1] = var4[var30 - 1] - var4[var31 - 1];
                     var7[var24] = var4[var30] - var4[var31];
                     var7[var25] = var4[var30] + var4[var31];
                     var24 += var0;
                     var25 += var0;
                     var30 += var29;
                     var31 += var29;
                  }
               }
            }
         case 116:
            float var35 = 1.0F;
            float var33 = 0.0F;
            var20 = 0;
            var28 = var21 = var44 * var3;
            var22 = (var1 - 1) * var3;

            for(int var16 = 1; var16 < var12; ++var16) {
               var20 += var3;
               var21 -= var3;
               float var42 = var39 * var35 - var41 * var33;
               var33 = var39 * var33 + var41 * var35;
               var35 = var42;
               var23 = var20;
               var24 = var21;
               var25 = 0;
               var26 = var3;
               var27 = var22;

               for(var17 = 0; var17 < var3; ++var17) {
                  var6[var23++] = var8[var25++] + var35 * var8[var26++];
                  var6[var24++] = var33 * var8[var27++];
               }

               float var32 = var35;
               float var37 = var33;
               float var36 = var35;
               float var34 = var33;
               var25 = var3;
               var26 = var28 - var3;

               for(var14 = 2; var14 < var12; ++var14) {
                  var25 += var3;
                  var26 -= var3;
                  float var43 = var32 * var36 - var37 * var34;
                  var34 = var32 * var34 + var37 * var36;
                  var36 = var43;
                  var23 = var20;
                  var24 = var21;
                  var30 = var25;
                  var31 = var26;

                  for(var17 = 0; var17 < var3; ++var17) {
                     int var10001 = var23++;
                     var6[var10001] += var36 * var8[var30++];
                     var10001 = var24++;
                     var6[var10001] += var34 * var8[var31++];
                  }
               }
            }

            var20 = 0;

            for(var14 = 1; var14 < var12; ++var14) {
               var20 += var3;
               var21 = var20;

               for(var17 = 0; var17 < var3; ++var17) {
                  var8[var17] += var8[var21++];
               }
            }

            var20 = 0;
            var21 = var44 * var19;

            for(var14 = 1; var14 < var12; ++var14) {
               var20 += var19;
               var21 -= var19;
               var22 = var20;
               var23 = var21;

               for(var15 = 0; var15 < var2; ++var15) {
                  var7[var22] = var5[var22] - var5[var23];
                  var7[var23] = var5[var22] + var5[var23];
                  var22 += var0;
                  var23 += var0;
               }
            }

            if (var0 == 1) {
               var45 = 132;
            } else {
               if (var38 < var2) {
                  var45 = 128;
                  break;
               }

               var20 = 0;
               var21 = var44 * var19;

               for(var14 = 1; var14 < var12; ++var14) {
                  var20 += var19;
                  var21 -= var19;
                  var22 = var20;
                  var23 = var21;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var24 = var22;
                     var25 = var23;

                     for(var13 = 2; var13 < var0; var13 += 2) {
                        var24 += 2;
                        var25 += 2;
                        var7[var24 - 1] = var5[var24 - 1] - var5[var25];
                        var7[var25 - 1] = var5[var24 - 1] + var5[var25];
                        var7[var24] = var5[var24] + var5[var25 - 1];
                        var7[var25] = var5[var24] - var5[var25 - 1];
                     }

                     var22 += var0;
                     var23 += var0;
                  }
               }

               var45 = 132;
            }
            break;
         case 128:
            var20 = 0;
            var21 = var44 * var19;

            for(var14 = 1; var14 < var12; ++var14) {
               var20 += var19;
               var21 -= var19;
               var22 = var20;
               var23 = var21;

               for(var13 = 2; var13 < var0; var13 += 2) {
                  var22 += 2;
                  var23 += 2;
                  var24 = var22;
                  var25 = var23;

                  for(var15 = 0; var15 < var2; ++var15) {
                     var7[var24 - 1] = var5[var24 - 1] - var5[var25];
                     var7[var25 - 1] = var5[var24 - 1] + var5[var25];
                     var7[var24] = var5[var24] + var5[var25 - 1];
                     var7[var25] = var5[var24] - var5[var25 - 1];
                     var24 += var0;
                     var25 += var0;
                  }
               }
            }
         case 132:
            if (var0 == 1) {
               return;
            }

            for(var17 = 0; var17 < var3; ++var17) {
               var6[var17] = var8[var17];
            }

            var20 = 0;

            for(var14 = 1; var14 < var1; ++var14) {
               var21 = var20 += var19;

               for(var15 = 0; var15 < var2; ++var15) {
                  var5[var21] = var7[var21];
                  var21 += var0;
               }
            }

            if (var38 <= var2) {
               var18 = -var0 - 1;
               var20 = 0;

               for(var14 = 1; var14 < var1; ++var14) {
                  var18 += var0;
                  var20 += var19;
                  var11 = var18;
                  var21 = var20;

                  for(var13 = 2; var13 < var0; var13 += 2) {
                     var21 += 2;
                     var11 += 2;
                     var22 = var21;

                     for(var15 = 0; var15 < var2; ++var15) {
                        var5[var22 - 1] = var9[var10 + var11 - 1] * var7[var22 - 1] - var9[var10 + var11] * var7[var22];
                        var5[var22] = var9[var10 + var11 - 1] * var7[var22] + var9[var10 + var11] * var7[var22 - 1];
                        var22 += var0;
                     }
                  }
               }

               return;
            }

            var45 = 139;
            break;
         case 139:
            var18 = -var0 - 1;
            var20 = 0;

            for(var14 = 1; var14 < var1; ++var14) {
               var18 += var0;
               var20 += var19;
               var21 = var20;

               for(var15 = 0; var15 < var2; ++var15) {
                  var11 = var18;
                  var22 = var21;

                  for(var13 = 2; var13 < var0; var13 += 2) {
                     var11 += 2;
                     var22 += 2;
                     var5[var22 - 1] = var9[var10 + var11 - 1] * var7[var22 - 1] - var9[var10 + var11] * var7[var22];
                     var5[var22] = var9[var10 + var11 - 1] * var7[var22] + var9[var10 + var11] * var7[var22 - 1];
                  }

                  var21 += var0;
               }
            }

            return;
         }
      }
   }

   static void drftb1(int var0, float[] var1, float[] var2, float[] var3, int var4, int[] var5) {
      int var9 = 0;
      int var12 = 0;
      int var16 = 0;
      int var17 = 0;
      int var11 = var5[1];
      int var10 = 0;
      int var8 = 1;
      int var13 = 1;

      for(int var7 = 0; var7 < var11; ++var7) {
         byte var18 = 100;

         label71:
         while(true) {
            int var14;
            switch(var18) {
            case 100:
               var12 = var5[var7 + 2];
               var9 = var12 * var8;
               var16 = var0 / var9;
               var17 = var16 * var8;
               if (var12 != 4) {
                  var18 = 103;
               } else {
                  var14 = var13 + var16;
                  int var15 = var14 + var16;
                  if (var10 != 0) {
                     dradb4(var16, var8, var2, var1, var3, var4 + var13 - 1, var3, var4 + var14 - 1, var3, var4 + var15 - 1);
                  } else {
                     dradb4(var16, var8, var1, var2, var3, var4 + var13 - 1, var3, var4 + var14 - 1, var3, var4 + var15 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 103:
               if (var12 != 2) {
                  var18 = 106;
               } else {
                  if (var10 != 0) {
                     dradb2(var16, var8, var2, var1, var3, var4 + var13 - 1);
                  } else {
                     dradb2(var16, var8, var1, var2, var3, var4 + var13 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 106:
               if (var12 != 3) {
                  var18 = 109;
               } else {
                  var14 = var13 + var16;
                  if (var10 != 0) {
                     dradb3(var16, var8, var2, var1, var3, var4 + var13 - 1, var3, var4 + var14 - 1);
                  } else {
                     dradb3(var16, var8, var1, var2, var3, var4 + var13 - 1, var3, var4 + var14 - 1);
                  }

                  var10 = 1 - var10;
                  var18 = 115;
               }
               break;
            case 109:
               if (var10 != 0) {
                  dradbg(var16, var12, var8, var17, var2, var2, var2, var1, var1, var3, var4 + var13 - 1);
               } else {
                  dradbg(var16, var12, var8, var17, var1, var1, var1, var2, var2, var3, var4 + var13 - 1);
               }

               if (var16 == 1) {
                  var10 = 1 - var10;
               }
            case 115:
               break label71;
            }
         }

         var8 = var9;
         var13 += (var12 - 1) * var16;
      }

      if (var10 != 0) {
         for(int var6 = 0; var6 < var0; ++var6) {
            var1[var6] = var2[var6];
         }

      }
   }
}
