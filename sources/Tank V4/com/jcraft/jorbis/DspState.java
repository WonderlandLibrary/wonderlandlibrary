package com.jcraft.jorbis;

public class DspState {
   static final float M_PI = 3.1415927F;
   static final int VI_TRANSFORMB = 1;
   static final int VI_WINDOWB = 1;
   int analysisp;
   Info vi;
   int modebits;
   float[][] pcm;
   int pcm_storage;
   int pcm_current;
   int pcm_returned;
   float[] multipliers;
   int envelope_storage;
   int envelope_current;
   int eofflag;
   int lW;
   int W;
   int nW;
   int centerW;
   long granulepos;
   long sequence;
   long glue_bits;
   long time_bits;
   long floor_bits;
   long res_bits;
   float[][][][][] window;
   Object[][] transform;
   CodeBook[] fullbooks;
   Object[] mode;
   byte[] header;
   byte[] header1;
   byte[] header2;

   public DspState() {
      this.transform = new Object[2][];
      this.window = new float[2][][][][];
      this.window[0] = new float[2][][][];
      this.window[0][0] = new float[2][][];
      this.window[0][1] = new float[2][][];
      this.window[0][0][0] = new float[2][];
      this.window[0][0][1] = new float[2][];
      this.window[0][1][0] = new float[2][];
      this.window[0][1][1] = new float[2][];
      this.window[1] = new float[2][][][];
      this.window[1][0] = new float[2][][];
      this.window[1][1] = new float[2][][];
      this.window[1][0][0] = new float[2][];
      this.window[1][0][1] = new float[2][];
      this.window[1][1][0] = new float[2][];
      this.window[1][1][1] = new float[2][];
   }

   static float[] window(int var0, int var1, int var2, int var3) {
      float[] var4 = new float[var1];
      switch(var0) {
      case 0:
         int var5 = var1 / 4 - var2 / 2;
         int var6 = var1 - var1 / 4 - var3 / 2;

         int var7;
         float var8;
         for(var7 = 0; var7 < var2; ++var7) {
            var8 = (float)(((double)var7 + 0.5D) / (double)var2 * 3.1415927410125732D / 2.0D);
            var8 = (float)Math.sin((double)var8);
            var8 *= var8;
            var8 = (float)((double)var8 * 1.5707963705062866D);
            var8 = (float)Math.sin((double)var8);
            var4[var7 + var5] = var8;
         }

         for(var7 = var5 + var2; var7 < var6; ++var7) {
            var4[var7] = 1.0F;
         }

         for(var7 = 0; var7 < var3; ++var7) {
            var8 = (float)(((double)(var3 - var7) - 0.5D) / (double)var3 * 3.1415927410125732D / 2.0D);
            var8 = (float)Math.sin((double)var8);
            var8 *= var8;
            var8 = (float)((double)var8 * 1.5707963705062866D);
            var8 = (float)Math.sin((double)var8);
            var4[var7 + var6] = var8;
         }

         return var4;
      default:
         return null;
      }
   }

   int init(Info var1, boolean var2) {
      this.vi = var1;
      this.modebits = Util.ilog2(var1.modes);
      this.transform[0] = new Object[1];
      this.transform[1] = new Object[1];
      this.transform[0][0] = new Mdct();
      this.transform[1][0] = new Mdct();
      ((Mdct)this.transform[0][0]).init(var1.blocksizes[0]);
      ((Mdct)this.transform[1][0]).init(var1.blocksizes[1]);
      this.window[0][0][0] = new float[1][];
      this.window[0][0][1] = this.window[0][0][0];
      this.window[0][1][0] = this.window[0][0][0];
      this.window[0][1][1] = this.window[0][0][0];
      this.window[1][0][0] = new float[1][];
      this.window[1][0][1] = new float[1][];
      this.window[1][1][0] = new float[1][];
      this.window[1][1][1] = new float[1][];

      int var3;
      for(var3 = 0; var3 < 1; ++var3) {
         this.window[0][0][0][var3] = window(var3, var1.blocksizes[0], var1.blocksizes[0] / 2, var1.blocksizes[0] / 2);
         this.window[1][0][0][var3] = window(var3, var1.blocksizes[1], var1.blocksizes[0] / 2, var1.blocksizes[0] / 2);
         this.window[1][0][1][var3] = window(var3, var1.blocksizes[1], var1.blocksizes[0] / 2, var1.blocksizes[1] / 2);
         this.window[1][1][0][var3] = window(var3, var1.blocksizes[1], var1.blocksizes[1] / 2, var1.blocksizes[0] / 2);
         this.window[1][1][1][var3] = window(var3, var1.blocksizes[1], var1.blocksizes[1] / 2, var1.blocksizes[1] / 2);
      }

      this.fullbooks = new CodeBook[var1.books];

      for(var3 = 0; var3 < var1.books; ++var3) {
         this.fullbooks[var3] = new CodeBook();
         this.fullbooks[var3].init_decode(var1.book_param[var3]);
      }

      this.pcm_storage = 8192;
      this.pcm = new float[var1.channels][];

      for(var3 = 0; var3 < var1.channels; ++var3) {
         this.pcm[var3] = new float[this.pcm_storage];
      }

      this.lW = 0;
      this.W = 0;
      this.centerW = var1.blocksizes[1] / 2;
      this.pcm_current = this.centerW;
      this.mode = new Object[var1.modes];

      for(var3 = 0; var3 < var1.modes; ++var3) {
         int var4 = var1.mode_param[var3].mapping;
         int var5 = var1.map_type[var4];
         this.mode[var3] = FuncMapping.mapping_P[var5].look(this, var1.mode_param[var3], var1.map_param[var4]);
      }

      return 0;
   }

   public int synthesis_init(Info var1) {
      this.init(var1, false);
      this.pcm_returned = this.centerW;
      this.centerW -= var1.blocksizes[this.W] / 4 + var1.blocksizes[this.lW] / 4;
      this.granulepos = -1L;
      this.sequence = -1L;
      return 0;
   }

   DspState(Info var1) {
      this();
      this.init(var1, false);
      this.pcm_returned = this.centerW;
      this.centerW -= var1.blocksizes[this.W] / 4 + var1.blocksizes[this.lW] / 4;
      this.granulepos = -1L;
      this.sequence = -1L;
   }

   public int synthesis_blockin(Block var1) {
      int var2;
      int var3;
      if (this.centerW > this.vi.blocksizes[1] / 2 && this.pcm_returned > 8192) {
         var2 = this.centerW - this.vi.blocksizes[1] / 2;
         var2 = this.pcm_returned < var2 ? this.pcm_returned : var2;
         this.pcm_current -= var2;
         this.centerW -= var2;
         this.pcm_returned -= var2;
         if (var2 != 0) {
            for(var3 = 0; var3 < this.vi.channels; ++var3) {
               System.arraycopy(this.pcm[var3], var2, this.pcm[var3], 0, this.pcm_current);
            }
         }
      }

      this.lW = this.W;
      this.W = var1.W;
      this.nW = -1;
      this.glue_bits += (long)var1.glue_bits;
      this.time_bits += (long)var1.time_bits;
      this.floor_bits += (long)var1.floor_bits;
      this.res_bits += (long)var1.res_bits;
      if (this.sequence + 1L != var1.sequence) {
         this.granulepos = -1L;
      }

      this.sequence = var1.sequence;
      var2 = this.vi.blocksizes[this.W];
      var3 = this.centerW + this.vi.blocksizes[this.lW] / 4 + var2 / 4;
      int var4 = var3 - var2 / 2;
      int var5 = var4 + var2;
      int var6 = 0;
      int var7 = 0;
      int var8;
      if (var5 > this.pcm_storage) {
         this.pcm_storage = var5 + this.vi.blocksizes[1];

         for(var8 = 0; var8 < this.vi.channels; ++var8) {
            float[] var9 = new float[this.pcm_storage];
            System.arraycopy(this.pcm[var8], 0, var9, 0, this.pcm[var8].length);
            this.pcm[var8] = var9;
         }
      }

      switch(this.W) {
      case 0:
         var6 = 0;
         var7 = this.vi.blocksizes[0] / 2;
         break;
      case 1:
         var6 = this.vi.blocksizes[1] / 4 - this.vi.blocksizes[this.lW] / 4;
         var7 = var6 + this.vi.blocksizes[this.lW] / 2;
      }

      for(var8 = 0; var8 < this.vi.channels; ++var8) {
         int var11 = var4;
         boolean var10 = false;

         int var12;
         for(var12 = var6; var12 < var7; ++var12) {
            float[] var10000 = this.pcm[var8];
            var10000[var11 + var12] += var1.pcm[var8][var12];
         }

         while(var12 < var2) {
            this.pcm[var8][var11 + var12] = var1.pcm[var8][var12];
            ++var12;
         }
      }

      if (this.granulepos == -1L) {
         this.granulepos = var1.granulepos;
      } else {
         this.granulepos += (long)(var3 - this.centerW);
         if (var1.granulepos != -1L && this.granulepos != var1.granulepos) {
            if (this.granulepos > var1.granulepos && var1.eofflag != 0) {
               var3 = (int)((long)var3 - (this.granulepos - var1.granulepos));
            }

            this.granulepos = var1.granulepos;
         }
      }

      this.centerW = var3;
      this.pcm_current = var5;
      if (var1.eofflag != 0) {
         this.eofflag = 1;
      }

      return 0;
   }

   public int synthesis_pcmout(float[][][] var1, int[] var2) {
      if (this.pcm_returned >= this.centerW) {
         return 0;
      } else {
         if (var1 != null) {
            for(int var3 = 0; var3 < this.vi.channels; ++var3) {
               var2[var3] = this.pcm_returned;
            }

            var1[0] = this.pcm;
         }

         return this.centerW - this.pcm_returned;
      }
   }

   public int synthesis_read(int var1) {
      if (var1 != 0 && this.pcm_returned + var1 > this.centerW) {
         return -1;
      } else {
         this.pcm_returned += var1;
         return 0;
      }
   }

   public void clear() {
   }
}
