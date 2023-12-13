package com.jcraft.jogg;

public class StreamState {
   byte[] body_data;
   int body_storage;
   int body_fill;
   private int body_returned;
   int[] lacing_vals;
   long[] granule_vals;
   int lacing_storage;
   int lacing_fill;
   int lacing_packet;
   int lacing_returned;
   byte[] header;
   int header_fill;
   public int e_o_s;
   int b_o_s;
   int serialno;
   int pageno;
   long packetno;
   long granulepos;

   public StreamState() {
      this.header = new byte[282];
      this.init();
   }

   StreamState(int var1) {
      this();
      this.init(var1);
   }

   void init() {
      this.body_storage = 16384;
      this.body_data = new byte[this.body_storage];
      this.lacing_storage = 1024;
      this.lacing_vals = new int[this.lacing_storage];
      this.granule_vals = new long[this.lacing_storage];
   }

   public void init(int var1) {
      if (this.body_data == null) {
         this.init();
      } else {
         int var2;
         for(var2 = 0; var2 < this.body_data.length; ++var2) {
            this.body_data[var2] = 0;
         }

         for(var2 = 0; var2 < this.lacing_vals.length; ++var2) {
            this.lacing_vals[var2] = 0;
         }

         for(var2 = 0; var2 < this.granule_vals.length; ++var2) {
            this.granule_vals[var2] = 0L;
         }
      }

      this.serialno = var1;
   }

   public void clear() {
      this.body_data = null;
      this.lacing_vals = null;
      this.granule_vals = null;
   }

   void destroy() {
      this.clear();
   }

   void body_expand(int var1) {
      if (this.body_storage <= this.body_fill + var1) {
         this.body_storage += var1 + 1024;
         byte[] var2 = new byte[this.body_storage];
         System.arraycopy(this.body_data, 0, var2, 0, this.body_data.length);
         this.body_data = var2;
      }

   }

   void lacing_expand(int var1) {
      if (this.lacing_storage <= this.lacing_fill + var1) {
         this.lacing_storage += var1 + 32;
         int[] var2 = new int[this.lacing_storage];
         System.arraycopy(this.lacing_vals, 0, var2, 0, this.lacing_vals.length);
         this.lacing_vals = var2;
         long[] var3 = new long[this.lacing_storage];
         System.arraycopy(this.granule_vals, 0, var3, 0, this.granule_vals.length);
         this.granule_vals = var3;
      }

   }

   public int packetin(Packet var1) {
      int var2 = var1.bytes / 255 + 1;
      if (this.body_returned != 0) {
         this.body_fill -= this.body_returned;
         if (this.body_fill != 0) {
            System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
         }

         this.body_returned = 0;
      }

      this.body_expand(var1.bytes);
      this.lacing_expand(var2);
      System.arraycopy(var1.packet_base, var1.packet, this.body_data, this.body_fill, var1.bytes);
      this.body_fill += var1.bytes;

      int var3;
      for(var3 = 0; var3 < var2 - 1; ++var3) {
         this.lacing_vals[this.lacing_fill + var3] = 255;
         this.granule_vals[this.lacing_fill + var3] = this.granulepos;
      }

      this.lacing_vals[this.lacing_fill + var3] = var1.bytes % 255;
      this.granulepos = this.granule_vals[this.lacing_fill + var3] = var1.granulepos;
      int[] var10000 = this.lacing_vals;
      int var10001 = this.lacing_fill;
      var10000[var10001] |= 256;
      this.lacing_fill += var2;
      ++this.packetno;
      if (var1.e_o_s != 0) {
         this.e_o_s = 1;
      }

      return 0;
   }

   public int packetout(Packet var1) {
      int var2 = this.lacing_returned;
      if (this.lacing_packet <= var2) {
         return 0;
      } else if ((this.lacing_vals[var2] & 1024) != 0) {
         ++this.lacing_returned;
         ++this.packetno;
         return -1;
      } else {
         int var3 = this.lacing_vals[var2] & 255;
         byte var4 = 0;
         var1.packet_base = this.body_data;
         var1.packet = this.body_returned;
         var1.e_o_s = this.lacing_vals[var2] & 512;
         var1.b_o_s = this.lacing_vals[var2] & 256;

         int var6;
         for(var6 = var4 + var3; var3 == 255; var6 += var3) {
            ++var2;
            int var5 = this.lacing_vals[var2];
            var3 = var5 & 255;
            if ((var5 & 512) != 0) {
               var1.e_o_s = 512;
            }
         }

         var1.packetno = this.packetno;
         var1.granulepos = this.granule_vals[var2];
         var1.bytes = var6;
         this.body_returned += var6;
         this.lacing_returned = var2 + 1;
         ++this.packetno;
         return 1;
      }
   }

   public int pagein(Page var1) {
      byte[] var2 = var1.header_base;
      int var3 = var1.header;
      byte[] var4 = var1.body_base;
      int var5 = var1.body;
      int var6 = var1.body_len;
      int var7 = 0;
      int var8 = var1.version();
      int var9 = var1.continued();
      int var10 = var1.bos();
      int var11 = var1.eos();
      long var12 = var1.granulepos();
      int var14 = var1.serialno();
      int var15 = var1.pageno();
      int var16 = var2[var3 + 26] & 255;
      int var17 = this.lacing_returned;
      int var18 = this.body_returned;
      if (var18 != 0) {
         this.body_fill -= var18;
         if (this.body_fill != 0) {
            System.arraycopy(this.body_data, var18, this.body_data, 0, this.body_fill);
         }

         this.body_returned = 0;
      }

      if (var17 != 0) {
         if (this.lacing_fill - var17 != 0) {
            System.arraycopy(this.lacing_vals, var17, this.lacing_vals, 0, this.lacing_fill - var17);
            System.arraycopy(this.granule_vals, var17, this.granule_vals, 0, this.lacing_fill - var17);
         }

         this.lacing_fill -= var17;
         this.lacing_packet -= var17;
         this.lacing_returned = 0;
      }

      if (var14 != this.serialno) {
         return -1;
      } else if (var8 > 0) {
         return -1;
      } else {
         this.lacing_expand(var16 + 1);
         if (var15 != this.pageno) {
            for(var17 = this.lacing_packet; var17 < this.lacing_fill; ++var17) {
               this.body_fill -= this.lacing_vals[var17] & 255;
            }

            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
               this.lacing_vals[this.lacing_fill++] = 1024;
               ++this.lacing_packet;
            }

            if (var9 != 0) {
               for(var10 = 0; var7 < var16; ++var7) {
                  var18 = var2[var3 + 27 + var7] & 255;
                  var5 += var18;
                  var6 -= var18;
                  if (var18 < 255) {
                     ++var7;
                     break;
                  }
               }
            }
         }

         if (var6 != 0) {
            this.body_expand(var6);
            System.arraycopy(var4, var5, this.body_data, this.body_fill, var6);
            this.body_fill += var6;
         }

         var17 = -1;

         int[] var10000;
         int var10001;
         while(var7 < var16) {
            var18 = var2[var3 + 27 + var7] & 255;
            this.lacing_vals[this.lacing_fill] = var18;
            this.granule_vals[this.lacing_fill] = -1L;
            if (var10 != 0) {
               var10000 = this.lacing_vals;
               var10001 = this.lacing_fill;
               var10000[var10001] |= 256;
               var10 = 0;
            }

            if (var18 < 255) {
               var17 = this.lacing_fill;
            }

            ++this.lacing_fill;
            ++var7;
            if (var18 < 255) {
               this.lacing_packet = this.lacing_fill;
            }
         }

         if (var17 != -1) {
            this.granule_vals[var17] = var12;
         }

         if (var11 != 0) {
            this.e_o_s = 1;
            if (this.lacing_fill > 0) {
               var10000 = this.lacing_vals;
               var10001 = this.lacing_fill - 1;
               var10000[var10001] |= 512;
            }
         }

         this.pageno = var15 + 1;
         return 0;
      }
   }

   public int flush(Page var1) {
      boolean var3 = false;
      int var4 = this.lacing_fill > 255 ? 255 : this.lacing_fill;
      int var5 = 0;
      int var6 = 0;
      long var7 = this.granule_vals[0];
      if (var4 == 0) {
         return 0;
      } else {
         int var10;
         if (this.b_o_s == 0) {
            var7 = 0L;

            for(var10 = 0; var10 < var4; ++var10) {
               if ((this.lacing_vals[var10] & 255) < 255) {
                  ++var10;
                  break;
               }
            }
         } else {
            for(var10 = 0; var10 < var4 && var6 <= 4096; ++var10) {
               var6 += this.lacing_vals[var10] & 255;
               var7 = this.granule_vals[var10];
            }
         }

         System.arraycopy("OggS".getBytes(), 0, this.header, 0, 4);
         this.header[4] = 0;
         this.header[5] = 0;
         byte[] var10000;
         if ((this.lacing_vals[0] & 256) == 0) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 1);
         }

         if (this.b_o_s == 0) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 2);
         }

         if (this.e_o_s != 0 && this.lacing_fill == var10) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 4);
         }

         this.b_o_s = 1;

         int var2;
         for(var2 = 6; var2 < 14; ++var2) {
            this.header[var2] = (byte)((int)var7);
            var7 >>>= 8;
         }

         int var9 = this.serialno;

         for(var2 = 14; var2 < 18; ++var2) {
            this.header[var2] = (byte)var9;
            var9 >>>= 8;
         }

         if (this.pageno == -1) {
            this.pageno = 0;
         }

         var9 = this.pageno++;

         for(var2 = 18; var2 < 22; ++var2) {
            this.header[var2] = (byte)var9;
            var9 >>>= 8;
         }

         this.header[22] = 0;
         this.header[23] = 0;
         this.header[24] = 0;
         this.header[25] = 0;
         this.header[26] = (byte)var10;

         for(var2 = 0; var2 < var10; ++var2) {
            this.header[var2 + 27] = (byte)this.lacing_vals[var2];
            var5 += this.header[var2 + 27] & 255;
         }

         var1.header_base = this.header;
         var1.header = 0;
         var1.header_len = this.header_fill = var10 + 27;
         var1.body_base = this.body_data;
         var1.body = this.body_returned;
         var1.body_len = var5;
         this.lacing_fill -= var10;
         System.arraycopy(this.lacing_vals, var10, this.lacing_vals, 0, this.lacing_fill * 4);
         System.arraycopy(this.granule_vals, var10, this.granule_vals, 0, this.lacing_fill * 8);
         this.body_returned += var5;
         var1.checksum();
         return 1;
      }
   }

   public int pageout(Page var1) {
      return (this.e_o_s == 0 || this.lacing_fill == 0) && this.body_fill - this.body_returned <= 4096 && this.lacing_fill < 255 && (this.lacing_fill == 0 || this.b_o_s != 0) ? 0 : this.flush(var1);
   }

   public int eof() {
      return this.e_o_s;
   }

   public int reset() {
      this.body_fill = 0;
      this.body_returned = 0;
      this.lacing_fill = 0;
      this.lacing_packet = 0;
      this.lacing_returned = 0;
      this.header_fill = 0;
      this.e_o_s = 0;
      this.b_o_s = 0;
      this.pageno = -1;
      this.packetno = 0L;
      this.granulepos = 0L;
      return 0;
   }
}
