package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class StaticCodeBook {
   int dim;
   int entries;
   int[] lengthlist;
   int maptype;
   int q_min;
   int q_delta;
   int q_quant;
   int q_sequencep;
   int[] quantlist;
   static final int VQ_FEXP = 10;
   static final int VQ_FMAN = 21;
   static final int VQ_FEXP_BIAS = 768;

   int pack(Buffer var1) {
      boolean var3 = false;
      var1.write(5653314, 24);
      var1.write(this.dim, 16);
      var1.write(this.entries, 24);

      int var2;
      for(var2 = 1; var2 < this.entries && this.lengthlist[var2] >= this.lengthlist[var2 - 1]; ++var2) {
      }

      if (var2 == this.entries) {
         var3 = true;
      }

      int var4;
      if (var3) {
         var4 = 0;
         var1.write(1, 1);
         var1.write(this.lengthlist[0] - 1, 5);

         for(var2 = 1; var2 < this.entries; ++var2) {
            int var5 = this.lengthlist[var2];
            int var6 = this.lengthlist[var2 - 1];
            if (var5 > var6) {
               for(int var7 = var6; var7 < var5; ++var7) {
                  var1.write(var2 - var4, Util.ilog(this.entries - var4));
                  var4 = var2;
               }
            }
         }

         var1.write(var2 - var4, Util.ilog(this.entries - var4));
      } else {
         var1.write(0, 1);

         for(var2 = 0; var2 < this.entries && this.lengthlist[var2] != 0; ++var2) {
         }

         if (var2 == this.entries) {
            var1.write(0, 1);

            for(var2 = 0; var2 < this.entries; ++var2) {
               var1.write(this.lengthlist[var2] - 1, 5);
            }
         } else {
            var1.write(1, 1);

            for(var2 = 0; var2 < this.entries; ++var2) {
               if (this.lengthlist[var2] == 0) {
                  var1.write(0, 1);
               } else {
                  var1.write(1, 1);
                  var1.write(this.lengthlist[var2] - 1, 5);
               }
            }
         }
      }

      var1.write(this.maptype, 4);
      switch(this.maptype) {
      case 1:
      case 2:
         if (this.quantlist == null) {
            return -1;
         } else {
            var1.write(this.q_min, 32);
            var1.write(this.q_delta, 32);
            var1.write(this.q_quant - 1, 4);
            var1.write(this.q_sequencep, 1);
            var4 = 0;
            switch(this.maptype) {
            case 1:
               var4 = this.maptype1_quantvals();
               break;
            case 2:
               var4 = this.entries * this.dim;
            }

            for(var2 = 0; var2 < var4; ++var2) {
               var1.write(Math.abs(this.quantlist[var2]), this.q_quant);
            }
         }
      case 0:
         return 0;
      default:
         return -1;
      }
   }

   int unpack(Buffer var1) {
      if (var1.read(24) != 5653314) {
         this.clear();
         return -1;
      } else {
         this.dim = var1.read(16);
         this.entries = var1.read(24);
         if (this.entries == -1) {
            this.clear();
            return -1;
         } else {
            int var2;
            int var3;
            label89:
            switch(var1.read(1)) {
            case 0:
               this.lengthlist = new int[this.entries];
               if (var1.read(1) != 0) {
                  var2 = 0;

                  while(true) {
                     if (var2 >= this.entries) {
                        break label89;
                     }

                     if (var1.read(1) != 0) {
                        var3 = var1.read(5);
                        if (var3 == -1) {
                           this.clear();
                           return -1;
                        }

                        this.lengthlist[var2] = var3 + 1;
                     } else {
                        this.lengthlist[var2] = 0;
                     }

                     ++var2;
                  }
               } else {
                  var2 = 0;

                  while(true) {
                     if (var2 >= this.entries) {
                        break label89;
                     }

                     var3 = var1.read(5);
                     if (var3 == -1) {
                        this.clear();
                        return -1;
                     }

                     this.lengthlist[var2] = var3 + 1;
                     ++var2;
                  }
               }
            case 1:
               var3 = var1.read(5) + 1;
               this.lengthlist = new int[this.entries];
               var2 = 0;

               while(true) {
                  if (var2 >= this.entries) {
                     break label89;
                  }

                  int var4 = var1.read(Util.ilog(this.entries - var2));
                  if (var4 == -1) {
                     this.clear();
                     return -1;
                  }

                  for(int var5 = 0; var5 < var4; ++var2) {
                     this.lengthlist[var2] = var3;
                     ++var5;
                  }

                  ++var3;
               }
            default:
               return -1;
            }

            switch(this.maptype = var1.read(4)) {
            case 1:
            case 2:
               this.q_min = var1.read(32);
               this.q_delta = var1.read(32);
               this.q_quant = var1.read(4) + 1;
               this.q_sequencep = var1.read(1);
               var3 = 0;
               switch(this.maptype) {
               case 1:
                  var3 = this.maptype1_quantvals();
                  break;
               case 2:
                  var3 = this.entries * this.dim;
               }

               this.quantlist = new int[var3];

               for(var2 = 0; var2 < var3; ++var2) {
                  this.quantlist[var2] = var1.read(this.q_quant);
               }

               if (this.quantlist[var3 - 1] == -1) {
                  this.clear();
                  return -1;
               }
            case 0:
               return 0;
            default:
               this.clear();
               return -1;
            }
         }
      }
   }

   private int maptype1_quantvals() {
      int var1 = (int)Math.floor(Math.pow((double)this.entries, 1.0D / (double)this.dim));

      while(true) {
         int var2 = 1;
         int var3 = 1;

         for(int var4 = 0; var4 < this.dim; ++var4) {
            var2 *= var1;
            var3 *= var1 + 1;
         }

         if (var2 <= this.entries && var3 > this.entries) {
            return var1;
         }

         if (var2 > this.entries) {
            --var1;
         } else {
            ++var1;
         }
      }
   }

   void clear() {
   }

   float[] unquantize() {
      if (this.maptype != 1 && this.maptype != 2) {
         return null;
      } else {
         float var2 = float32_unpack(this.q_min);
         float var3 = float32_unpack(this.q_delta);
         float[] var4 = new float[this.entries * this.dim];
         int var5;
         float var6;
         int var7;
         switch(this.maptype) {
         case 1:
            int var1 = this.maptype1_quantvals();

            for(var5 = 0; var5 < this.entries; ++var5) {
               var6 = 0.0F;
               var7 = 1;

               for(int var11 = 0; var11 < this.dim; ++var11) {
                  int var9 = var5 / var7 % var1;
                  float var10 = (float)this.quantlist[var9];
                  var10 = Math.abs(var10) * var3 + var2 + var6;
                  if (this.q_sequencep != 0) {
                     var6 = var10;
                  }

                  var4[var5 * this.dim + var11] = var10;
                  var7 *= var1;
               }
            }

            return var4;
         case 2:
            for(var5 = 0; var5 < this.entries; ++var5) {
               var6 = 0.0F;

               for(var7 = 0; var7 < this.dim; ++var7) {
                  float var8 = (float)this.quantlist[var5 * this.dim + var7];
                  var8 = Math.abs(var8) * var3 + var2 + var6;
                  if (this.q_sequencep != 0) {
                     var6 = var8;
                  }

                  var4[var5 * this.dim + var7] = var8;
               }
            }
         }

         return var4;
      }
   }

   static long float32_pack(float var0) {
      int var1 = 0;
      if (var0 < 0.0F) {
         var1 = Integer.MIN_VALUE;
         var0 = -var0;
      }

      int var2 = (int)Math.floor(Math.log((double)var0) / Math.log(2.0D));
      int var3 = (int)Math.rint(Math.pow((double)var0, (double)(20 - var2)));
      var2 = var2 + 768 << 21;
      return (long)(var1 | var2 | var3);
   }

   static float float32_unpack(int var0) {
      float var1 = (float)(var0 & 2097151);
      float var2 = (float)((var0 & 2145386496) >>> 21);
      if ((var0 & Integer.MIN_VALUE) != 0) {
         var1 = -var1;
      }

      return ldexp(var1, (int)var2 - 20 - 768);
   }

   static float ldexp(float var0, int var1) {
      return (float)((double)var0 * Math.pow(2.0D, (double)var1));
   }
}
