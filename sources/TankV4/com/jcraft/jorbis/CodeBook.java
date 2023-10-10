package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
   int dim;
   int entries;
   StaticCodeBook c = new StaticCodeBook();
   float[] valuelist;
   int[] codelist;
   CodeBook.DecodeAux decode_tree;
   private int[] t = new int[15];

   int encode(int var1, Buffer var2) {
      var2.write(this.codelist[var1], this.c.lengthlist[var1]);
      return this.c.lengthlist[var1];
   }

   int errorv(float[] var1) {
      int var2 = this.best(var1, 1);

      for(int var3 = 0; var3 < this.dim; ++var3) {
         var1[var3] = this.valuelist[var2 * this.dim + var3];
      }

      return var2;
   }

   int encodev(int var1, float[] var2, Buffer var3) {
      for(int var4 = 0; var4 < this.dim; ++var4) {
         var2[var4] = this.valuelist[var1 * this.dim + var4];
      }

      return this.encode(var1, var3);
   }

   int encodevs(float[] var1, Buffer var2, int var3, int var4) {
      int var5 = this.besterror(var1, var3, var4);
      return this.encode(var5, var2);
   }

   synchronized int decodevs_add(float[] var1, int var2, Buffer var3, int var4) {
      int var5 = var4 / this.dim;
      if (this.t.length < var5) {
         this.t = new int[var5];
      }

      int var7;
      for(var7 = 0; var7 < var5; ++var7) {
         int var6 = this.decode(var3);
         if (var6 == -1) {
            return -1;
         }

         this.t[var7] = var6 * this.dim;
      }

      var7 = 0;

      for(int var9 = 0; var7 < this.dim; var9 += var5) {
         for(int var8 = 0; var8 < var5; ++var8) {
            var1[var2 + var9 + var8] += this.valuelist[this.t[var8] + var7];
         }

         ++var7;
      }

      return 0;
   }

   int decodev_add(float[] var1, int var2, Buffer var3, int var4) {
      int var10002;
      int var5;
      int var6;
      int var7;
      int var8;
      if (this.dim > 8) {
         var5 = 0;

         while(var5 < var4) {
            var7 = this.decode(var3);
            if (var7 == -1) {
               return -1;
            }

            var8 = var7 * this.dim;

            for(var6 = 0; var6 < this.dim; var1[var2 + var10002] += this.valuelist[var8 + var6++]) {
               var10002 = var5++;
            }
         }
      } else {
         var5 = 0;

         while(var5 < var4) {
            var7 = this.decode(var3);
            if (var7 == -1) {
               return -1;
            }

            var8 = var7 * this.dim;
            var6 = 0;
            switch(this.dim) {
            case 0:
            default:
               break;
            case 8:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 7:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 6:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 5:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 4:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 3:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 2:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            case 1:
               var10002 = var5++;
               var1[var2 + var10002] += this.valuelist[var8 + var6++];
            }
         }
      }

      return 0;
   }

   int decodev_set(float[] var1, int var2, Buffer var3, int var4) {
      int var5 = 0;

      while(var5 < var4) {
         int var7 = this.decode(var3);
         if (var7 == -1) {
            return -1;
         }

         int var8 = var7 * this.dim;

         for(int var6 = 0; var6 < this.dim; var1[var2 + var5++] = this.valuelist[var8 + var6++]) {
         }
      }

      return 0;
   }

   int decodevv_add(float[][] var1, int var2, int var3, Buffer var4, int var5) {
      int var9 = 0;
      int var6 = var2 / var3;

      while(var6 < (var2 + var5) / var3) {
         int var8 = this.decode(var4);
         if (var8 == -1) {
            return -1;
         }

         int var10 = var8 * this.dim;

         for(int var7 = 0; var7 < this.dim; ++var7) {
            int var10001 = var9++;
            var1[var10001][var6] += this.valuelist[var10 + var7];
            if (var9 == var3) {
               var9 = 0;
               ++var6;
            }
         }
      }

      return 0;
   }

   int decode(Buffer var1) {
      int var2 = 0;
      CodeBook.DecodeAux var3 = this.decode_tree;
      int var4 = var1.look(var3.tabn);
      if (var4 >= 0) {
         var2 = var3.tab[var4];
         var1.adv(var3.tabl[var4]);
         if (var2 <= 0) {
            return -var2;
         }
      }

      do {
         switch(var1.read1()) {
         case -1:
         default:
            return -1;
         case 0:
            var2 = var3.ptr0[var2];
            break;
         case 1:
            var2 = var3.ptr1[var2];
         }
      } while(var2 > 0);

      return -var2;
   }

   int decodevs(float[] var1, int var2, Buffer var3, int var4, int var5) {
      int var6 = this.decode(var3);
      if (var6 == -1) {
         return -1;
      } else {
         int var7;
         int var8;
         switch(var5) {
         case -1:
            var7 = 0;

            for(var8 = 0; var7 < this.dim; var8 += var4) {
               var1[var2 + var8] = this.valuelist[var6 * this.dim + var7];
               ++var7;
            }

            return var6;
         case 0:
            var7 = 0;

            for(var8 = 0; var7 < this.dim; var8 += var4) {
               var1[var2 + var8] += this.valuelist[var6 * this.dim + var7];
               ++var7;
            }

            return var6;
         case 1:
            var7 = 0;

            for(var8 = 0; var7 < this.dim; var8 += var4) {
               var1[var2 + var8] *= this.valuelist[var6 * this.dim + var7];
               ++var7;
            }
         }

         return var6;
      }
   }

   int best(float[] var1, int var2) {
      int var3 = -1;
      float var4 = 0.0F;
      int var5 = 0;

      for(int var6 = 0; var6 < this.entries; ++var6) {
         if (this.c.lengthlist[var6] > 0) {
            float var7 = dist(this.dim, this.valuelist, var5, var1, var2);
            if (var3 == -1 || var7 < var4) {
               var4 = var7;
               var3 = var6;
            }
         }

         var5 += this.dim;
      }

      return var3;
   }

   int besterror(float[] var1, int var2, int var3) {
      int var4 = this.best(var1, var2);
      int var5;
      int var6;
      switch(var3) {
      case 0:
         var5 = 0;

         for(var6 = 0; var5 < this.dim; var6 += var2) {
            var1[var6] -= this.valuelist[var4 * this.dim + var5];
            ++var5;
         }

         return var4;
      case 1:
         var5 = 0;

         for(var6 = 0; var5 < this.dim; var6 += var2) {
            float var7 = this.valuelist[var4 * this.dim + var5];
            if (var7 == 0.0F) {
               var1[var6] = 0.0F;
            } else {
               var1[var6] /= var7;
            }

            ++var5;
         }
      }

      return var4;
   }

   void clear() {
   }

   private static float dist(int var0, float[] var1, int var2, float[] var3, int var4) {
      float var5 = 0.0F;

      for(int var6 = 0; var6 < var0; ++var6) {
         float var7 = var1[var2 + var6] - var3[var6 * var4];
         var5 += var7 * var7;
      }

      return var5;
   }

   int init_decode(StaticCodeBook var1) {
      this.c = var1;
      this.entries = var1.entries;
      this.dim = var1.dim;
      this.valuelist = var1.unquantize();
      this.decode_tree = this.make_decode_tree();
      if (this.decode_tree == null) {
         this.clear();
         return -1;
      } else {
         return 0;
      }
   }

   static int[] make_words(int[] var0, int var1) {
      int[] var2 = new int[33];
      int[] var3 = new int[var1];

      int var4;
      int var5;
      int var6;
      for(var4 = 0; var4 < var1; ++var4) {
         var5 = var0[var4];
         if (var5 > 0) {
            var6 = var2[var5];
            if (var5 < 32 && var6 >>> var5 != 0) {
               return null;
            }

            var3[var4] = var6;

            int var7;
            for(var7 = var5; var7 > 0; --var7) {
               int var10002;
               if ((var2[var7] & 1) != 0) {
                  if (var7 == 1) {
                     var10002 = var2[1]++;
                  } else {
                     var2[var7] = var2[var7 - 1] << 1;
                  }
                  break;
               }

               var10002 = var2[var7]++;
            }

            for(var7 = var5 + 1; var7 < 33 && var2[var7] >>> 1 == var6; ++var7) {
               var6 = var2[var7];
               var2[var7] = var2[var7 - 1] << 1;
            }
         }
      }

      for(var4 = 0; var4 < var1; ++var4) {
         var5 = 0;

         for(var6 = 0; var6 < var0[var4]; ++var6) {
            var5 <<= 1;
            var5 |= var3[var4] >>> var6 & 1;
         }

         var3[var4] = var5;
      }

      return var3;
   }

   CodeBook.DecodeAux make_decode_tree() {
      int var1 = 0;
      CodeBook.DecodeAux var2 = new CodeBook.DecodeAux(this);
      int[] var3 = var2.ptr0 = new int[this.entries * 2];
      int[] var4 = var2.ptr1 = new int[this.entries * 2];
      int[] var5 = make_words(this.c.lengthlist, this.c.entries);
      if (var5 == null) {
         return null;
      } else {
         var2.aux = this.entries * 2;

         int var6;
         int var7;
         int var8;
         int var9;
         for(var6 = 0; var6 < this.entries; ++var6) {
            if (this.c.lengthlist[var6] > 0) {
               var7 = 0;

               for(var8 = 0; var8 < this.c.lengthlist[var6] - 1; ++var8) {
                  var9 = var5[var6] >>> var8 & 1;
                  if (var9 == 0) {
                     if (var3[var7] == 0) {
                        ++var1;
                        var3[var7] = var1;
                     }

                     var7 = var3[var7];
                  } else {
                     if (var4[var7] == 0) {
                        ++var1;
                        var4[var7] = var1;
                     }

                     var7 = var4[var7];
                  }
               }

               if ((var5[var6] >>> var8 & 1) == 0) {
                  var3[var7] = -var6;
               } else {
                  var4[var7] = -var6;
               }
            }
         }

         var2.tabn = Util.ilog(this.entries) - 4;
         if (var2.tabn < 5) {
            var2.tabn = 5;
         }

         var6 = 1 << var2.tabn;
         var2.tab = new int[var6];
         var2.tabl = new int[var6];

         for(var7 = 0; var7 < var6; ++var7) {
            var8 = 0;
            boolean var10 = false;

            for(var9 = 0; var9 < var2.tabn && (var8 > 0 || var9 == 0); ++var9) {
               if ((var7 & 1 << var9) != 0) {
                  var8 = var4[var8];
               } else {
                  var8 = var3[var8];
               }
            }

            var2.tab[var7] = var8;
            var2.tabl[var7] = var9;
         }

         return var2;
      }
   }

   class DecodeAux {
      int[] tab;
      int[] tabl;
      int tabn;
      int[] ptr0;
      int[] ptr1;
      int aux;
      final CodeBook this$0;

      DecodeAux(CodeBook var1) {
         this.this$0 = var1;
      }
   }
}
