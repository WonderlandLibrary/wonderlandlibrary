package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;

public class Block {
   float[][] pcm = new float[0][];
   Buffer opb = new Buffer();
   int lW;
   int W;
   int nW;
   int pcmend;
   int mode;
   int eofflag;
   long granulepos;
   long sequence;
   DspState vd;
   int glue_bits;
   int time_bits;
   int floor_bits;
   int res_bits;

   public Block(DspState var1) {
      this.vd = var1;
      if (var1.analysisp != 0) {
         this.opb.writeinit();
      }

   }

   public void init(DspState var1) {
      this.vd = var1;
   }

   public int clear() {
      if (this.vd != null && this.vd.analysisp != 0) {
         this.opb.writeclear();
      }

      return 0;
   }

   public int synthesis(Packet var1) {
      Info var2 = this.vd.vi;
      this.opb.readinit(var1.packet_base, var1.packet, var1.bytes);
      if (this.opb.read(1) != 0) {
         return -1;
      } else {
         int var3 = this.opb.read(this.vd.modebits);
         if (var3 == -1) {
            return -1;
         } else {
            this.mode = var3;
            this.W = var2.mode_param[this.mode].blockflag;
            if (this.W != 0) {
               this.lW = this.opb.read(1);
               this.nW = this.opb.read(1);
               if (this.nW == -1) {
                  return -1;
               }
            } else {
               this.lW = 0;
               this.nW = 0;
            }

            this.granulepos = var1.granulepos;
            this.sequence = var1.packetno - 3L;
            this.eofflag = var1.e_o_s;
            this.pcmend = var2.blocksizes[this.W];
            if (this.pcm.length < var2.channels) {
               this.pcm = new float[var2.channels][];
            }

            int var4;
            for(var4 = 0; var4 < var2.channels; ++var4) {
               if (this.pcm[var4] != null && this.pcm[var4].length >= this.pcmend) {
                  for(int var5 = 0; var5 < this.pcmend; ++var5) {
                     this.pcm[var4][var5] = 0.0F;
                  }
               } else {
                  this.pcm[var4] = new float[this.pcmend];
               }
            }

            var4 = var2.map_type[var2.mode_param[this.mode].mapping];
            return FuncMapping.mapping_P[var4].inverse(this, this.vd.mode[this.mode]);
         }
      }
   }
}
