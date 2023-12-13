package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0 extends FuncResidue {
   private static int[][][] _01inverse_partword = new int[2][][];
   static int[][] _2inverse_partword = (int[][])null;

   void pack(Object var1, Buffer var2) {
      Residue0.InfoResidue0 var3 = (Residue0.InfoResidue0)var1;
      int var4 = 0;
      var2.write(var3.begin, 24);
      var2.write(var3.end, 24);
      var2.write(var3.grouping - 1, 24);
      var2.write(var3.partitions - 1, 6);
      var2.write(var3.groupbook, 8);

      int var5;
      for(var5 = 0; var5 < var3.partitions; ++var5) {
         int var6 = var3.secondstages[var5];
         if (Util.ilog(var6) > 3) {
            var2.write(var6, 3);
            var2.write(1, 1);
            var2.write(var6 >>> 3, 5);
         } else {
            var2.write(var6, 4);
         }

         var4 += Util.icount(var6);
      }

      for(var5 = 0; var5 < var4; ++var5) {
         var2.write(var3.booklist[var5], 8);
      }

   }

   Object unpack(Info var1, Buffer var2) {
      int var3 = 0;
      Residue0.InfoResidue0 var4 = new Residue0.InfoResidue0(this);
      var4.begin = var2.read(24);
      var4.end = var2.read(24);
      var4.grouping = var2.read(24) + 1;
      var4.partitions = var2.read(6) + 1;
      var4.groupbook = var2.read(8);

      int var5;
      for(var5 = 0; var5 < var4.partitions; ++var5) {
         int var6 = var2.read(3);
         if (var2.read(1) != 0) {
            var6 |= var2.read(5) << 3;
         }

         var4.secondstages[var5] = var6;
         var3 += Util.icount(var6);
      }

      for(var5 = 0; var5 < var3; ++var5) {
         var4.booklist[var5] = var2.read(8);
      }

      if (var4.groupbook >= var1.books) {
         this.free_info(var4);
         return null;
      } else {
         for(var5 = 0; var5 < var3; ++var5) {
            if (var4.booklist[var5] >= var1.books) {
               this.free_info(var4);
               return null;
            }
         }

         return var4;
      }
   }

   Object look(DspState var1, InfoMode var2, Object var3) {
      Residue0.InfoResidue0 var4 = (Residue0.InfoResidue0)var3;
      Residue0.LookResidue0 var5 = new Residue0.LookResidue0(this);
      int var6 = 0;
      int var8 = 0;
      var5.info = var4;
      var5.map = var2.mapping;
      var5.parts = var4.partitions;
      var5.fullbooks = var1.fullbooks;
      var5.phrasebook = var1.fullbooks[var4.groupbook];
      int var7 = var5.phrasebook.dim;
      var5.partbooks = new int[var5.parts][];

      int var9;
      int var10;
      int var11;
      int var12;
      for(var9 = 0; var9 < var5.parts; ++var9) {
         var10 = var4.secondstages[var9];
         var11 = Util.ilog(var10);
         if (var11 != 0) {
            if (var11 > var8) {
               var8 = var11;
            }

            var5.partbooks[var9] = new int[var11];

            for(var12 = 0; var12 < var11; ++var12) {
               if ((var10 & 1 << var12) != 0) {
                  var5.partbooks[var9][var12] = var4.booklist[var6++];
               }
            }
         }
      }

      var5.partvals = (int)Math.rint(Math.pow((double)var5.parts, (double)var7));
      var5.stages = var8;
      var5.decodemap = new int[var5.partvals][];

      for(var9 = 0; var9 < var5.partvals; ++var9) {
         var10 = var9;
         var11 = var5.partvals / var5.parts;
         var5.decodemap[var9] = new int[var7];

         for(var12 = 0; var12 < var7; ++var12) {
            int var13 = var10 / var11;
            var10 -= var13 * var11;
            var11 /= var5.parts;
            var5.decodemap[var9][var12] = var13;
         }
      }

      return var5;
   }

   void free_info(Object var1) {
   }

   void free_look(Object var1) {
   }

   static synchronized int _01inverse(Block var0, Object var1, float[][] var2, int var3, int var4) {
      Residue0.LookResidue0 var10 = (Residue0.LookResidue0)var1;
      Residue0.InfoResidue0 var11 = var10.info;
      int var12 = var11.grouping;
      int var13 = var10.phrasebook.dim;
      int var14 = var11.end - var11.begin;
      int var15 = var14 / var12;
      int var16 = (var15 + var13 - 1) / var13;
      if (_01inverse_partword.length < var3) {
         _01inverse_partword = new int[var3][][];
      }

      int var6;
      for(var6 = 0; var6 < var3; ++var6) {
         if (_01inverse_partword[var6] == null || _01inverse_partword[var6].length < var16) {
            _01inverse_partword[var6] = new int[var16][];
         }
      }

      for(int var9 = 0; var9 < var10.stages; ++var9) {
         int var5 = 0;

         for(int var8 = 0; var5 < var15; ++var8) {
            int var17;
            if (var9 == 0) {
               for(var6 = 0; var6 < var3; ++var6) {
                  var17 = var10.phrasebook.decode(var0.opb);
                  if (var17 == -1) {
                     return 0;
                  }

                  _01inverse_partword[var6][var8] = var10.decodemap[var17];
                  if (_01inverse_partword[var6][var8] == null) {
                     return 0;
                  }
               }
            }

            for(int var7 = 0; var7 < var13 && var5 < var15; ++var5) {
               for(var6 = 0; var6 < var3; ++var6) {
                  var17 = var11.begin + var5 * var12;
                  int var18 = _01inverse_partword[var6][var8][var7];
                  if ((var11.secondstages[var18] & 1 << var9) != 0) {
                     CodeBook var19 = var10.fullbooks[var10.partbooks[var18][var9]];
                     if (var19 != null) {
                        if (var4 == 0) {
                           if (var19.decodevs_add(var2[var6], var17, var0.opb, var12) == -1) {
                              return 0;
                           }
                        } else if (var4 == 1 && var19.decodev_add(var2[var6], var17, var0.opb, var12) == -1) {
                           return 0;
                        }
                     }
                  }
               }

               ++var7;
            }
         }
      }

      return 0;
   }

   static synchronized int _2inverse(Block var0, Object var1, float[][] var2, int var3) {
      Residue0.LookResidue0 var8 = (Residue0.LookResidue0)var1;
      Residue0.InfoResidue0 var9 = var8.info;
      int var10 = var9.grouping;
      int var11 = var8.phrasebook.dim;
      int var12 = var9.end - var9.begin;
      int var13 = var12 / var10;
      int var14 = (var13 + var11 - 1) / var11;
      if (_2inverse_partword == null || _2inverse_partword.length < var14) {
         _2inverse_partword = new int[var14][];
      }

      for(int var7 = 0; var7 < var8.stages; ++var7) {
         int var4 = 0;

         for(int var6 = 0; var4 < var13; ++var6) {
            int var15;
            if (var7 == 0) {
               var15 = var8.phrasebook.decode(var0.opb);
               if (var15 == -1) {
                  return 0;
               }

               _2inverse_partword[var6] = var8.decodemap[var15];
               if (_2inverse_partword[var6] == null) {
                  return 0;
               }
            }

            for(int var5 = 0; var5 < var11 && var4 < var13; ++var4) {
               var15 = var9.begin + var4 * var10;
               int var16 = _2inverse_partword[var6][var5];
               if ((var9.secondstages[var16] & 1 << var7) != 0) {
                  CodeBook var17 = var8.fullbooks[var8.partbooks[var16][var7]];
                  if (var17 != null && var17.decodevv_add(var2, var15, var3, var0.opb, var10) == -1) {
                     return 0;
                  }
               }

               ++var5;
            }
         }
      }

      return 0;
   }

   int inverse(Block var1, Object var2, float[][] var3, int[] var4, int var5) {
      int var6 = 0;

      for(int var7 = 0; var7 < var5; ++var7) {
         if (var4[var7] != 0) {
            var3[var6++] = var3[var7];
         }
      }

      if (var6 != 0) {
         return _01inverse(var1, var2, var3, var6, 0);
      } else {
         return 0;
      }
   }

   class InfoResidue0 {
      int begin;
      int end;
      int grouping;
      int partitions;
      int groupbook;
      int[] secondstages;
      int[] booklist;
      float[] entmax;
      float[] ampmax;
      int[] subgrp;
      int[] blimit;
      final Residue0 this$0;

      InfoResidue0(Residue0 var1) {
         this.this$0 = var1;
         this.secondstages = new int[64];
         this.booklist = new int[256];
         this.entmax = new float[64];
         this.ampmax = new float[64];
         this.subgrp = new int[64];
         this.blimit = new int[64];
      }
   }

   class LookResidue0 {
      Residue0.InfoResidue0 info;
      int map;
      int parts;
      int stages;
      CodeBook[] fullbooks;
      CodeBook phrasebook;
      int[][] partbooks;
      int partvals;
      int[][] decodemap;
      int postbits;
      int phrasebits;
      int frames;
      final Residue0 this$0;

      LookResidue0(Residue0 var1) {
         this.this$0 = var1;
      }
   }
}
