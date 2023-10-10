package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Floor1 extends FuncFloor {
   static final int floor1_rangedb = 140;
   static final int VIF_POSIT = 63;
   private static float[] FLOOR_fromdB_LOOKUP = new float[]{1.0649863E-7F, 1.1341951E-7F, 1.2079015E-7F, 1.2863978E-7F, 1.369995E-7F, 1.459025E-7F, 1.5538409E-7F, 1.6548181E-7F, 1.7623574E-7F, 1.8768856E-7F, 1.998856E-7F, 2.128753E-7F, 2.2670913E-7F, 2.4144197E-7F, 2.5713223E-7F, 2.7384212E-7F, 2.9163792E-7F, 3.1059022E-7F, 3.307741E-7F, 3.5226967E-7F, 3.7516213E-7F, 3.995423E-7F, 4.255068E-7F, 4.5315863E-7F, 4.8260745E-7F, 5.1397E-7F, 5.4737063E-7F, 5.829419E-7F, 6.208247E-7F, 6.611694E-7F, 7.041359E-7F, 7.4989464E-7F, 7.98627E-7F, 8.505263E-7F, 9.057983E-7F, 9.646621E-7F, 1.0273513E-6F, 1.0941144E-6F, 1.1652161E-6F, 1.2409384E-6F, 1.3215816E-6F, 1.4074654E-6F, 1.4989305E-6F, 1.5963394E-6F, 1.7000785E-6F, 1.8105592E-6F, 1.9282195E-6F, 2.053526E-6F, 2.1869757E-6F, 2.3290977E-6F, 2.4804558E-6F, 2.6416496E-6F, 2.813319E-6F, 2.9961443E-6F, 3.1908505E-6F, 3.39821E-6F, 3.619045E-6F, 3.8542307E-6F, 4.1047006E-6F, 4.371447E-6F, 4.6555283E-6F, 4.958071E-6F, 5.280274E-6F, 5.623416E-6F, 5.988857E-6F, 6.3780467E-6F, 6.7925284E-6F, 7.2339453E-6F, 7.704048E-6F, 8.2047E-6F, 8.737888E-6F, 9.305725E-6F, 9.910464E-6F, 1.0554501E-5F, 1.1240392E-5F, 1.1970856E-5F, 1.2748789E-5F, 1.3577278E-5F, 1.4459606E-5F, 1.5399271E-5F, 1.6400005E-5F, 1.7465769E-5F, 1.8600793E-5F, 1.9809577E-5F, 2.1096914E-5F, 2.2467912E-5F, 2.3928002E-5F, 2.5482977E-5F, 2.7139005E-5F, 2.890265E-5F, 3.078091E-5F, 3.2781227E-5F, 3.4911533E-5F, 3.718028E-5F, 3.9596467E-5F, 4.2169668E-5F, 4.491009E-5F, 4.7828602E-5F, 5.0936775E-5F, 5.424693E-5F, 5.7772202E-5F, 6.152657E-5F, 6.552491E-5F, 6.9783084E-5F, 7.4317984E-5F, 7.914758E-5F, 8.429104E-5F, 8.976875E-5F, 9.560242E-5F, 1.0181521E-4F, 1.0843174E-4F, 1.1547824E-4F, 1.2298267E-4F, 1.3097477E-4F, 1.3948625E-4F, 1.4855085E-4F, 1.5820454E-4F, 1.6848555E-4F, 1.7943469E-4F, 1.9109536E-4F, 2.0351382E-4F, 2.167393E-4F, 2.3082423E-4F, 2.4582449E-4F, 2.6179955E-4F, 2.7881275E-4F, 2.9693157E-4F, 3.1622787E-4F, 3.3677815E-4F, 3.5866388E-4F, 3.8197188E-4F, 4.0679457E-4F, 4.3323037E-4F, 4.613841E-4F, 4.913675E-4F, 5.2329927E-4F, 5.573062E-4F, 5.935231E-4F, 6.320936E-4F, 6.731706E-4F, 7.16917E-4F, 7.635063E-4F, 8.1312325E-4F, 8.6596457E-4F, 9.2223985E-4F, 9.821722E-4F, 0.0010459992F, 0.0011139743F, 0.0011863665F, 0.0012634633F, 0.0013455702F, 0.0014330129F, 0.0015261382F, 0.0016253153F, 0.0017309374F, 0.0018434235F, 0.0019632196F, 0.0020908006F, 0.0022266726F, 0.0023713743F, 0.0025254795F, 0.0026895993F, 0.0028643848F, 0.0030505287F, 0.003248769F, 0.0034598925F, 0.0036847359F, 0.0039241905F, 0.0041792067F, 0.004450795F, 0.004740033F, 0.005048067F, 0.0053761187F, 0.005725489F, 0.0060975635F, 0.0064938175F, 0.0069158226F, 0.0073652514F, 0.007843887F, 0.008353627F, 0.008896492F, 0.009474637F, 0.010090352F, 0.01074608F, 0.011444421F, 0.012188144F, 0.012980198F, 0.013823725F, 0.014722068F, 0.015678791F, 0.016697686F, 0.017782796F, 0.018938422F, 0.020169148F, 0.021479854F, 0.022875736F, 0.02436233F, 0.025945531F, 0.027631618F, 0.029427277F, 0.031339627F, 0.03337625F, 0.035545226F, 0.037855156F, 0.0403152F, 0.042935107F, 0.045725275F, 0.048696756F, 0.05186135F, 0.05523159F, 0.05882085F, 0.062643364F, 0.06671428F, 0.07104975F, 0.075666964F, 0.08058423F, 0.08582105F, 0.09139818F, 0.097337745F, 0.1036633F, 0.11039993F, 0.11757434F, 0.12521498F, 0.13335215F, 0.14201812F, 0.15124726F, 0.16107617F, 0.1715438F, 0.18269168F, 0.19456401F, 0.20720787F, 0.22067343F, 0.23501402F, 0.25028655F, 0.26655158F, 0.28387362F, 0.3023213F, 0.32196787F, 0.34289113F, 0.36517414F, 0.3889052F, 0.41417846F, 0.44109413F, 0.4697589F, 0.50028646F, 0.53279793F, 0.5674221F, 0.6042964F, 0.64356697F, 0.6853896F, 0.72993004F, 0.777365F, 0.8278826F, 0.88168305F, 0.9389798F, 1.0F};

   void pack(Object var1, Buffer var2) {
      Floor1.InfoFloor1 var3 = (Floor1.InfoFloor1)var1;
      int var4 = 0;
      int var6 = var3.postlist[1];
      int var7 = -1;
      var2.write(var3.partitions, 5);

      int var8;
      for(var8 = 0; var8 < var3.partitions; ++var8) {
         var2.write(var3.partitionclass[var8], 4);
         if (var7 < var3.partitionclass[var8]) {
            var7 = var3.partitionclass[var8];
         }
      }

      int var9;
      for(var8 = 0; var8 < var7 + 1; ++var8) {
         var2.write(var3.class_dim[var8] - 1, 3);
         var2.write(var3.class_subs[var8], 2);
         if (var3.class_subs[var8] != 0) {
            var2.write(var3.class_book[var8], 8);
         }

         for(var9 = 0; var9 < 1 << var3.class_subs[var8]; ++var9) {
            var2.write(var3.class_subbook[var8][var9] + 1, 8);
         }
      }

      var2.write(var3.mult - 1, 2);
      var2.write(Util.ilog2(var6), 4);
      int var5 = Util.ilog2(var6);
      var8 = 0;

      for(var9 = 0; var8 < var3.partitions; ++var8) {
         for(var4 += var3.class_dim[var3.partitionclass[var8]]; var9 < var4; ++var9) {
            var2.write(var3.postlist[var9 + 2], var5);
         }
      }

   }

   Object unpack(Info var1, Buffer var2) {
      int var3 = 0;
      int var4 = -1;
      Floor1.InfoFloor1 var6 = new Floor1.InfoFloor1(this);
      var6.partitions = var2.read(5);

      int var7;
      for(var7 = 0; var7 < var6.partitions; ++var7) {
         var6.partitionclass[var7] = var2.read(4);
         if (var4 < var6.partitionclass[var7]) {
            var4 = var6.partitionclass[var7];
         }
      }

      int var8;
      for(var7 = 0; var7 < var4 + 1; ++var7) {
         var6.class_dim[var7] = var2.read(3) + 1;
         var6.class_subs[var7] = var2.read(2);
         if (var6.class_subs[var7] < 0) {
            var6.free();
            return null;
         }

         if (var6.class_subs[var7] != 0) {
            var6.class_book[var7] = var2.read(8);
         }

         if (var6.class_book[var7] < 0 || var6.class_book[var7] >= var1.books) {
            var6.free();
            return null;
         }

         for(var8 = 0; var8 < 1 << var6.class_subs[var7]; ++var8) {
            var6.class_subbook[var7][var8] = var2.read(8) - 1;
            if (var6.class_subbook[var7][var8] < -1 || var6.class_subbook[var7][var8] >= var1.books) {
               var6.free();
               return null;
            }
         }
      }

      var6.mult = var2.read(2) + 1;
      int var5 = var2.read(4);
      var7 = 0;

      for(var8 = 0; var7 < var6.partitions; ++var7) {
         for(var3 += var6.class_dim[var6.partitionclass[var7]]; var8 < var3; ++var8) {
            int var9 = var6.postlist[var8 + 2] = var2.read(var5);
            if (var9 < 0 || var9 >= 1 << var5) {
               var6.free();
               return null;
            }
         }
      }

      var6.postlist[0] = 0;
      var6.postlist[1] = 1 << var5;
      return var6;
   }

   Object look(DspState var1, InfoMode var2, Object var3) {
      int var4 = 0;
      int[] var5 = new int[65];
      Floor1.InfoFloor1 var6 = (Floor1.InfoFloor1)var3;
      Floor1.LookFloor1 var7 = new Floor1.LookFloor1(this);
      var7.vi = var6;
      var7.n = var6.postlist[1];

      int var8;
      for(var8 = 0; var8 < var6.partitions; ++var8) {
         var4 += var6.class_dim[var6.partitionclass[var8]];
      }

      var4 += 2;
      var7.posts = var4;

      for(var8 = 0; var8 < var4; var5[var8] = var8++) {
      }

      int var9;
      int var10;
      for(var9 = 0; var9 < var4 - 1; ++var9) {
         for(var10 = var9; var10 < var4; ++var10) {
            if (var6.postlist[var5[var9]] > var6.postlist[var5[var10]]) {
               var8 = var5[var10];
               var5[var10] = var5[var9];
               var5[var9] = var8;
            }
         }
      }

      for(var9 = 0; var9 < var4; ++var9) {
         var7.forward_index[var9] = var5[var9];
      }

      for(var9 = 0; var9 < var4; var7.reverse_index[var7.forward_index[var9]] = var9++) {
      }

      for(var9 = 0; var9 < var4; ++var9) {
         var7.sorted_index[var9] = var6.postlist[var7.forward_index[var9]];
      }

      switch(var6.mult) {
      case 1:
         var7.quant_q = 256;
         break;
      case 2:
         var7.quant_q = 128;
         break;
      case 3:
         var7.quant_q = 86;
         break;
      case 4:
         var7.quant_q = 64;
         break;
      default:
         var7.quant_q = -1;
      }

      for(var9 = 0; var9 < var4 - 2; ++var9) {
         var10 = 0;
         int var11 = 1;
         int var12 = 0;
         int var13 = var7.n;
         int var14 = var6.postlist[var9 + 2];

         for(int var15 = 0; var15 < var9 + 2; ++var15) {
            int var16 = var6.postlist[var15];
            if (var16 > var12 && var16 < var14) {
               var10 = var15;
               var12 = var16;
            }

            if (var16 < var13 && var16 > var14) {
               var11 = var15;
               var13 = var16;
            }
         }

         var7.loneighbor[var9] = var10;
         var7.hineighbor[var9] = var11;
      }

      return var7;
   }

   void free_info(Object var1) {
   }

   void free_look(Object var1) {
   }

   void free_state(Object var1) {
   }

   int forward(Block var1, Object var2, float[] var3, float[] var4, Object var5) {
      return 0;
   }

   Object inverse1(Block var1, Object var2, Object var3) {
      Floor1.LookFloor1 var4 = (Floor1.LookFloor1)var2;
      Floor1.InfoFloor1 var5 = var4.vi;
      CodeBook[] var6 = var1.vd.fullbooks;
      if (var1.opb.read(1) == 1) {
         int[] var7 = null;
         if (var3 instanceof int[]) {
            var7 = (int[])((int[])var3);
         }

         int var8;
         if (var7 != null && var7.length >= var4.posts) {
            for(var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = 0;
            }
         } else {
            var7 = new int[var4.posts];
         }

         var7[0] = var1.opb.read(Util.ilog(var4.quant_q - 1));
         var7[1] = var1.opb.read(Util.ilog(var4.quant_q - 1));
         var8 = 0;

         int var9;
         int var10;
         int var12;
         int var13;
         for(var9 = 2; var8 < var5.partitions; ++var8) {
            var10 = var5.partitionclass[var8];
            int var11 = var5.class_dim[var10];
            var12 = var5.class_subs[var10];
            var13 = 1 << var12;
            int var14 = 0;
            if (var12 != 0) {
               var14 = var6[var5.class_book[var10]].decode(var1.opb);
               if (var14 == -1) {
                  return null;
               }
            }

            for(int var15 = 0; var15 < var11; ++var15) {
               int var16 = var5.class_subbook[var10][var14 & var13 - 1];
               var14 >>>= var12;
               if (var16 >= 0) {
                  if ((var7[var9 + var15] = var6[var16].decode(var1.opb)) == -1) {
                     return null;
                  }
               } else {
                  var7[var9 + var15] = 0;
               }
            }

            var9 += var11;
         }

         for(var8 = 2; var8 < var4.posts; ++var8) {
            var9 = render_point(var5.postlist[var4.loneighbor[var8 - 2]], var5.postlist[var4.hineighbor[var8 - 2]], var7[var4.loneighbor[var8 - 2]], var7[var4.hineighbor[var8 - 2]], var5.postlist[var8]);
            var10 = var4.quant_q - var9;
            var12 = (var10 < var9 ? var10 : var9) << 1;
            var13 = var7[var8];
            if (var13 != 0) {
               if (var13 >= var12) {
                  if (var10 > var9) {
                     var13 -= var9;
                  } else {
                     var13 = -1 - (var13 - var10);
                  }
               } else if ((var13 & 1) != 0) {
                  var13 = -(var13 + 1 >>> 1);
               } else {
                  var13 >>= 1;
               }

               var7[var8] = var13 + var9;
               int var10001 = var4.loneighbor[var8 - 2];
               var7[var10001] &= 32767;
               var10001 = var4.hineighbor[var8 - 2];
               var7[var10001] &= 32767;
            } else {
               var7[var8] = var9 | '耀';
            }
         }

         return var7;
      } else {
         return null;
      }
   }

   private static int render_point(int var0, int var1, int var2, int var3, int var4) {
      var2 &= 32767;
      var3 &= 32767;
      int var5 = var3 - var2;
      int var6 = var1 - var0;
      int var7 = Math.abs(var5);
      int var8 = var7 * (var4 - var0);
      int var9 = var8 / var6;
      return var5 < 0 ? var2 - var9 : var2 + var9;
   }

   int inverse2(Block var1, Object var2, Object var3, float[] var4) {
      Floor1.LookFloor1 var5 = (Floor1.LookFloor1)var2;
      Floor1.InfoFloor1 var6 = var5.vi;
      int var7 = var1.vd.vi.blocksizes[var1.mode] / 2;
      if (var3 != null) {
         int[] var15 = (int[])((int[])var3);
         int var9 = 0;
         int var10 = 0;
         int var11 = var15[0] * var6.mult;

         int var12;
         for(var12 = 1; var12 < var5.posts; ++var12) {
            int var13 = var5.forward_index[var12];
            int var14 = var15[var13] & 32767;
            if (var14 == var15[var13]) {
               var14 *= var6.mult;
               var9 = var6.postlist[var13];
               render_line(var10, var9, var11, var14, var4);
               var10 = var9;
               var11 = var14;
            }
         }

         for(var12 = var9; var12 < var7; ++var12) {
            var4[var12] *= var4[var12 - 1];
         }

         return 1;
      } else {
         for(int var8 = 0; var8 < var7; ++var8) {
            var4[var8] = 0.0F;
         }

         return 0;
      }
   }

   private static void render_line(int var0, int var1, int var2, int var3, float[] var4) {
      int var5 = var3 - var2;
      int var6 = var1 - var0;
      int var7 = Math.abs(var5);
      int var8 = var5 / var6;
      int var9 = var5 < 0 ? var8 - 1 : var8 + 1;
      int var10 = var0;
      int var11 = var2;
      int var12 = 0;
      var7 -= Math.abs(var8 * var6);
      var4[var0] *= FLOOR_fromdB_LOOKUP[var2];

      while(true) {
         ++var10;
         if (var10 >= var1) {
            return;
         }

         var12 += var7;
         if (var12 >= var6) {
            var12 -= var6;
            var11 += var9;
         } else {
            var11 += var8;
         }

         var4[var10] *= FLOOR_fromdB_LOOKUP[var11];
      }
   }

   class EchstateFloor1 {
      int[] codewords;
      float[] curve;
      long frameno;
      long codes;
      final Floor1 this$0;

      EchstateFloor1(Floor1 var1) {
         this.this$0 = var1;
      }
   }

   class Lsfit_acc {
      long x0;
      long x1;
      long xa;
      long ya;
      long x2a;
      long y2a;
      long xya;
      long n;
      long an;
      long un;
      long edgey0;
      long edgey1;
      final Floor1 this$0;

      Lsfit_acc(Floor1 var1) {
         this.this$0 = var1;
      }
   }

   class LookFloor1 {
      static final int VIF_POSIT = 63;
      int[] sorted_index;
      int[] forward_index;
      int[] reverse_index;
      int[] hineighbor;
      int[] loneighbor;
      int posts;
      int n;
      int quant_q;
      Floor1.InfoFloor1 vi;
      int phrasebits;
      int postbits;
      int frames;
      final Floor1 this$0;

      LookFloor1(Floor1 var1) {
         this.this$0 = var1;
         this.sorted_index = new int[65];
         this.forward_index = new int[65];
         this.reverse_index = new int[65];
         this.hineighbor = new int[63];
         this.loneighbor = new int[63];
      }

      void free() {
         this.sorted_index = null;
         this.forward_index = null;
         this.reverse_index = null;
         this.hineighbor = null;
         this.loneighbor = null;
      }
   }

   class InfoFloor1 {
      static final int VIF_POSIT = 63;
      static final int VIF_CLASS = 16;
      static final int VIF_PARTS = 31;
      int partitions;
      int[] partitionclass;
      int[] class_dim;
      int[] class_subs;
      int[] class_book;
      int[][] class_subbook;
      int mult;
      int[] postlist;
      float maxover;
      float maxunder;
      float maxerr;
      int twofitminsize;
      int twofitminused;
      int twofitweight;
      float twofitatten;
      int unusedminsize;
      int unusedmin_n;
      int n;
      final Floor1 this$0;

      InfoFloor1(Floor1 var1) {
         this.this$0 = var1;
         this.partitionclass = new int[31];
         this.class_dim = new int[16];
         this.class_subs = new int[16];
         this.class_book = new int[16];
         this.class_subbook = new int[16][];
         this.postlist = new int[65];

         for(int var2 = 0; var2 < this.class_subbook.length; ++var2) {
            this.class_subbook[var2] = new int[8];
         }

      }

      void free() {
         this.partitionclass = null;
         this.class_dim = null;
         this.class_subs = null;
         this.class_book = null;
         this.class_subbook = (int[][])null;
         this.postlist = null;
      }

      Object copy_info() {
         Floor1.InfoFloor1 var1 = this;
         Floor1.InfoFloor1 var2 = this.this$0.new InfoFloor1(this.this$0);
         var2.partitions = this.partitions;
         System.arraycopy(this.partitionclass, 0, var2.partitionclass, 0, 31);
         System.arraycopy(this.class_dim, 0, var2.class_dim, 0, 16);
         System.arraycopy(this.class_subs, 0, var2.class_subs, 0, 16);
         System.arraycopy(this.class_book, 0, var2.class_book, 0, 16);

         for(int var3 = 0; var3 < 16; ++var3) {
            System.arraycopy(var1.class_subbook[var3], 0, var2.class_subbook[var3], 0, 8);
         }

         var2.mult = var1.mult;
         System.arraycopy(var1.postlist, 0, var2.postlist, 0, 65);
         var2.maxover = var1.maxover;
         var2.maxunder = var1.maxunder;
         var2.maxerr = var1.maxerr;
         var2.twofitminsize = var1.twofitminsize;
         var2.twofitminused = var1.twofitminused;
         var2.twofitweight = var1.twofitweight;
         var2.twofitatten = var1.twofitatten;
         var2.unusedminsize = var1.unusedminsize;
         var2.unusedmin_n = var1.unusedmin_n;
         var2.n = var1.n;
         return var2;
      }
   }
}
