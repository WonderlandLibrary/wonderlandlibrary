package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Floor0 extends FuncFloor {
   float[] lsp = null;

   void pack(Object var1, Buffer var2) {
      Floor0.InfoFloor0 var3 = (Floor0.InfoFloor0)var1;
      var2.write(var3.order, 8);
      var2.write(var3.rate, 16);
      var2.write(var3.barkmap, 16);
      var2.write(var3.ampbits, 6);
      var2.write(var3.ampdB, 8);
      var2.write(var3.numbooks - 1, 4);

      for(int var4 = 0; var4 < var3.numbooks; ++var4) {
         var2.write(var3.books[var4], 8);
      }

   }

   Object unpack(Info var1, Buffer var2) {
      Floor0.InfoFloor0 var3 = new Floor0.InfoFloor0(this);
      var3.order = var2.read(8);
      var3.rate = var2.read(16);
      var3.barkmap = var2.read(16);
      var3.ampbits = var2.read(6);
      var3.ampdB = var2.read(8);
      var3.numbooks = var2.read(4) + 1;
      if (var3.order >= 1 && var3.rate >= 1 && var3.barkmap >= 1 && var3.numbooks >= 1) {
         for(int var4 = 0; var4 < var3.numbooks; ++var4) {
            var3.books[var4] = var2.read(8);
            if (var3.books[var4] < 0 || var3.books[var4] >= var1.books) {
               return null;
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   Object look(DspState var1, InfoMode var2, Object var3) {
      Info var5 = var1.vi;
      Floor0.InfoFloor0 var6 = (Floor0.InfoFloor0)var3;
      Floor0.LookFloor0 var7 = new Floor0.LookFloor0(this);
      var7.m = var6.order;
      var7.n = var5.blocksizes[var2.blockflag] / 2;
      var7.ln = var6.barkmap;
      var7.vi = var6;
      var7.lpclook.init(var7.ln, var7.m);
      float var4 = (float)var7.ln / toBARK((float)((double)var6.rate / 2.0D));
      var7.linearmap = new int[var7.n];

      for(int var8 = 0; var8 < var7.n; ++var8) {
         int var9 = (int)Math.floor((double)(toBARK((float)((double)var6.rate / 2.0D / (double)var7.n * (double)var8)) * var4));
         if (var9 >= var7.ln) {
            var9 = var7.ln;
         }

         var7.linearmap[var8] = var9;
      }

      return var7;
   }

   static float toBARK(float var0) {
      return (float)(13.1D * Math.atan(7.4E-4D * (double)var0) + 2.24D * Math.atan((double)(var0 * var0) * 1.85E-8D) + 1.0E-4D * (double)var0);
   }

   Object state(Object var1) {
      Floor0.EchstateFloor0 var2 = new Floor0.EchstateFloor0(this);
      Floor0.InfoFloor0 var3 = (Floor0.InfoFloor0)var1;
      var2.codewords = new int[var3.order];
      var2.curve = new float[var3.barkmap];
      var2.frameno = -1L;
      return var2;
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

   int inverse(Block var1, Object var2, float[] var3) {
      Floor0.LookFloor0 var4 = (Floor0.LookFloor0)var2;
      Floor0.InfoFloor0 var5 = var4.vi;
      int var6 = var1.opb.read(var5.ampbits);
      if (var6 > 0) {
         int var7 = (1 << var5.ampbits) - 1;
         float var8 = (float)var6 / (float)var7 * (float)var5.ampdB;
         int var9 = var1.opb.read(Util.ilog(var5.numbooks));
         if (var9 != -1 && var9 < var5.numbooks) {
            synchronized(this){}
            if (this.lsp != null && this.lsp.length >= var4.m) {
               for(int var11 = 0; var11 < var4.m; ++var11) {
                  this.lsp[var11] = 0.0F;
               }
            } else {
               this.lsp = new float[var4.m];
            }

            CodeBook var16 = var1.vd.fullbooks[var5.books[var9]];
            float var12 = 0.0F;

            int var13;
            for(var13 = 0; var13 < var4.m; ++var13) {
               var3[var13] = 0.0F;
            }

            int var14;
            for(var13 = 0; var13 < var4.m; var13 += var16.dim) {
               if (var16.decodevs(this.lsp, var13, var1.opb, 1, -1) == -1) {
                  for(var14 = 0; var14 < var4.n; ++var14) {
                     var3[var14] = 0.0F;
                  }

                  return 0;
               }
            }

            for(var13 = 0; var13 < var4.m; var12 = this.lsp[var13 - 1]) {
               for(var14 = 0; var14 < var16.dim; ++var13) {
                  float[] var10000 = this.lsp;
                  var10000[var13] += var12;
                  ++var14;
               }
            }

            Lsp.lsp_to_curve(var3, var4.linearmap, var4.n, var4.ln, this.lsp, var4.m, var8, (float)var5.ampdB);
            return 1;
         }
      }

      return 0;
   }

   Object inverse1(Block var1, Object var2, Object var3) {
      Floor0.LookFloor0 var4 = (Floor0.LookFloor0)var2;
      Floor0.InfoFloor0 var5 = var4.vi;
      float[] var6 = null;
      if (var3 instanceof float[]) {
         var6 = (float[])((float[])var3);
      }

      int var7 = var1.opb.read(var5.ampbits);
      if (var7 > 0) {
         int var8 = (1 << var5.ampbits) - 1;
         float var9 = (float)var7 / (float)var8 * (float)var5.ampdB;
         int var10 = var1.opb.read(Util.ilog(var5.numbooks));
         if (var10 != -1 && var10 < var5.numbooks) {
            CodeBook var11 = var1.vd.fullbooks[var5.books[var10]];
            float var12 = 0.0F;
            int var13;
            if (var6 != null && var6.length >= var4.m + 1) {
               for(var13 = 0; var13 < var6.length; ++var13) {
                  var6[var13] = 0.0F;
               }
            } else {
               var6 = new float[var4.m + 1];
            }

            for(var13 = 0; var13 < var4.m; var13 += var11.dim) {
               if (var11.decodev_set(var6, var13, var1.opb, var11.dim) == -1) {
                  return null;
               }
            }

            for(var13 = 0; var13 < var4.m; var12 = var6[var13 - 1]) {
               for(int var14 = 0; var14 < var11.dim; ++var13) {
                  var6[var13] += var12;
                  ++var14;
               }
            }

            var6[var4.m] = var9;
            return var6;
         }
      }

      return null;
   }

   int inverse2(Block var1, Object var2, Object var3, float[] var4) {
      Floor0.LookFloor0 var5 = (Floor0.LookFloor0)var2;
      Floor0.InfoFloor0 var6 = var5.vi;
      if (var3 != null) {
         float[] var9 = (float[])((float[])var3);
         float var8 = var9[var5.m];
         Lsp.lsp_to_curve(var4, var5.linearmap, var5.n, var5.ln, var9, var5.m, var8, (float)var6.ampdB);
         return 1;
      } else {
         for(int var7 = 0; var7 < var5.n; ++var7) {
            var4[var7] = 0.0F;
         }

         return 0;
      }
   }

   static float fromdB(float var0) {
      return (float)Math.exp((double)var0 * 0.11512925D);
   }

   static void lsp_to_lpc(float[] var0, float[] var1, int var2) {
      int var5 = var2 / 2;
      float[] var6 = new float[var5];
      float[] var7 = new float[var5];
      float[] var9 = new float[var5 + 1];
      float[] var10 = new float[var5 + 1];
      float[] var12 = new float[var5];
      float[] var13 = new float[var5];

      int var3;
      for(var3 = 0; var3 < var5; ++var3) {
         var6[var3] = (float)(-2.0D * Math.cos((double)var0[var3 * 2]));
         var7[var3] = (float)(-2.0D * Math.cos((double)var0[var3 * 2 + 1]));
      }

      int var4;
      for(var4 = 0; var4 < var5; ++var4) {
         var9[var4] = 0.0F;
         var10[var4] = 1.0F;
         var12[var4] = 0.0F;
         var13[var4] = 1.0F;
      }

      var10[var4] = 1.0F;
      var9[var4] = 1.0F;

      for(var3 = 1; var3 < var2 + 1; ++var3) {
         float var11 = 0.0F;
         float var8 = 0.0F;

         for(var4 = 0; var4 < var5; ++var4) {
            float var14 = var6[var4] * var10[var4] + var9[var4];
            var9[var4] = var10[var4];
            var10[var4] = var8;
            var8 += var14;
            var14 = var7[var4] * var13[var4] + var12[var4];
            var12[var4] = var13[var4];
            var13[var4] = var11;
            var11 += var14;
         }

         var1[var3 - 1] = (var8 + var10[var4] + var11 - var9[var4]) / 2.0F;
         var10[var4] = var8;
         var9[var4] = var11;
      }

   }

   static void lpc_to_curve(float[] var0, float[] var1, float var2, Floor0.LookFloor0 var3, String var4, int var5) {
      float[] var6 = new float[Math.max(var3.ln * 2, var3.m * 2 + 2)];
      int var7;
      if (var2 == 0.0F) {
         for(var7 = 0; var7 < var3.n; ++var7) {
            var0[var7] = 0.0F;
         }

      } else {
         var3.lpclook.lpc_to_curve(var6, var1, var2);

         for(var7 = 0; var7 < var3.n; ++var7) {
            var0[var7] = var6[var3.linearmap[var7]];
         }

      }
   }

   class EchstateFloor0 {
      int[] codewords;
      float[] curve;
      long frameno;
      long codes;
      final Floor0 this$0;

      EchstateFloor0(Floor0 var1) {
         this.this$0 = var1;
      }
   }

   class LookFloor0 {
      int n;
      int ln;
      int m;
      int[] linearmap;
      Floor0.InfoFloor0 vi;
      Lpc lpclook;
      final Floor0 this$0;

      LookFloor0(Floor0 var1) {
         this.this$0 = var1;
         this.lpclook = new Lpc();
      }
   }

   class InfoFloor0 {
      int order;
      int rate;
      int barkmap;
      int ampbits;
      int ampdB;
      int numbooks;
      int[] books;
      final Floor0 this$0;

      InfoFloor0(Floor0 var1) {
         this.this$0 = var1;
         this.books = new int[16];
      }
   }
}
