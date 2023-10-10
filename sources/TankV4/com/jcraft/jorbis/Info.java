package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jogg.Packet;

public class Info {
   private static final int OV_EBADPACKET = -136;
   private static final int OV_ENOTAUDIO = -135;
   private static byte[] _vorbis = "vorbis".getBytes();
   private static final int VI_TIMEB = 1;
   private static final int VI_FLOORB = 2;
   private static final int VI_RESB = 3;
   private static final int VI_MAPB = 1;
   private static final int VI_WINDOWB = 1;
   public int version;
   public int channels;
   public int rate;
   int bitrate_upper;
   int bitrate_nominal;
   int bitrate_lower;
   int[] blocksizes = new int[2];
   int modes;
   int maps;
   int times;
   int floors;
   int residues;
   int books;
   int psys;
   InfoMode[] mode_param = null;
   int[] map_type = null;
   Object[] map_param = null;
   int[] time_type = null;
   Object[] time_param = null;
   int[] floor_type = null;
   Object[] floor_param = null;
   int[] residue_type = null;
   Object[] residue_param = null;
   StaticCodeBook[] book_param = null;
   PsyInfo[] psy_param = new PsyInfo[64];
   int envelopesa;
   float preecho_thresh;
   float preecho_clamp;

   public void init() {
      this.rate = 0;
   }

   public void clear() {
      int var1;
      for(var1 = 0; var1 < this.modes; ++var1) {
         this.mode_param[var1] = null;
      }

      this.mode_param = null;

      for(var1 = 0; var1 < this.maps; ++var1) {
         FuncMapping.mapping_P[this.map_type[var1]].free_info(this.map_param[var1]);
      }

      this.map_param = null;

      for(var1 = 0; var1 < this.times; ++var1) {
         FuncTime.time_P[this.time_type[var1]].free_info(this.time_param[var1]);
      }

      this.time_param = null;

      for(var1 = 0; var1 < this.floors; ++var1) {
         FuncFloor.floor_P[this.floor_type[var1]].free_info(this.floor_param[var1]);
      }

      this.floor_param = null;

      for(var1 = 0; var1 < this.residues; ++var1) {
         FuncResidue.residue_P[this.residue_type[var1]].free_info(this.residue_param[var1]);
      }

      this.residue_param = null;

      for(var1 = 0; var1 < this.books; ++var1) {
         if (this.book_param[var1] != null) {
            this.book_param[var1].clear();
            this.book_param[var1] = null;
         }
      }

      this.book_param = null;

      for(var1 = 0; var1 < this.psys; ++var1) {
         this.psy_param[var1].free();
      }

   }

   int unpack_info(Buffer var1) {
      this.version = var1.read(32);
      if (this.version != 0) {
         return -1;
      } else {
         this.channels = var1.read(8);
         this.rate = var1.read(32);
         this.bitrate_upper = var1.read(32);
         this.bitrate_nominal = var1.read(32);
         this.bitrate_lower = var1.read(32);
         this.blocksizes[0] = 1 << var1.read(4);
         this.blocksizes[1] = 1 << var1.read(4);
         if (this.rate >= 1 && this.channels >= 1 && this.blocksizes[0] >= 8 && this.blocksizes[1] >= this.blocksizes[0] && var1.read(1) == 1) {
            return 0;
         } else {
            this.clear();
            return -1;
         }
      }
   }

   int unpack_books(Buffer var1) {
      this.books = var1.read(8) + 1;
      if (this.book_param == null || this.book_param.length != this.books) {
         this.book_param = new StaticCodeBook[this.books];
      }

      int var2;
      for(var2 = 0; var2 < this.books; ++var2) {
         this.book_param[var2] = new StaticCodeBook();
         if (this.book_param[var2].unpack(var1) != 0) {
            this.clear();
            return -1;
         }
      }

      this.times = var1.read(6) + 1;
      if (this.time_type == null || this.time_type.length != this.times) {
         this.time_type = new int[this.times];
      }

      if (this.time_param == null || this.time_param.length != this.times) {
         this.time_param = new Object[this.times];
      }

      for(var2 = 0; var2 < this.times; ++var2) {
         this.time_type[var2] = var1.read(16);
         if (this.time_type[var2] < 0 || this.time_type[var2] >= 1) {
            this.clear();
            return -1;
         }

         this.time_param[var2] = FuncTime.time_P[this.time_type[var2]].unpack(this, var1);
         if (this.time_param[var2] == null) {
            this.clear();
            return -1;
         }
      }

      this.floors = var1.read(6) + 1;
      if (this.floor_type == null || this.floor_type.length != this.floors) {
         this.floor_type = new int[this.floors];
      }

      if (this.floor_param == null || this.floor_param.length != this.floors) {
         this.floor_param = new Object[this.floors];
      }

      for(var2 = 0; var2 < this.floors; ++var2) {
         this.floor_type[var2] = var1.read(16);
         if (this.floor_type[var2] < 0 || this.floor_type[var2] >= 2) {
            this.clear();
            return -1;
         }

         this.floor_param[var2] = FuncFloor.floor_P[this.floor_type[var2]].unpack(this, var1);
         if (this.floor_param[var2] == null) {
            this.clear();
            return -1;
         }
      }

      this.residues = var1.read(6) + 1;
      if (this.residue_type == null || this.residue_type.length != this.residues) {
         this.residue_type = new int[this.residues];
      }

      if (this.residue_param == null || this.residue_param.length != this.residues) {
         this.residue_param = new Object[this.residues];
      }

      for(var2 = 0; var2 < this.residues; ++var2) {
         this.residue_type[var2] = var1.read(16);
         if (this.residue_type[var2] < 0 || this.residue_type[var2] >= 3) {
            this.clear();
            return -1;
         }

         this.residue_param[var2] = FuncResidue.residue_P[this.residue_type[var2]].unpack(this, var1);
         if (this.residue_param[var2] == null) {
            this.clear();
            return -1;
         }
      }

      this.maps = var1.read(6) + 1;
      if (this.map_type == null || this.map_type.length != this.maps) {
         this.map_type = new int[this.maps];
      }

      if (this.map_param == null || this.map_param.length != this.maps) {
         this.map_param = new Object[this.maps];
      }

      for(var2 = 0; var2 < this.maps; ++var2) {
         this.map_type[var2] = var1.read(16);
         if (this.map_type[var2] < 0 || this.map_type[var2] >= 1) {
            this.clear();
            return -1;
         }

         this.map_param[var2] = FuncMapping.mapping_P[this.map_type[var2]].unpack(this, var1);
         if (this.map_param[var2] == null) {
            this.clear();
            return -1;
         }
      }

      this.modes = var1.read(6) + 1;
      if (this.mode_param == null || this.mode_param.length != this.modes) {
         this.mode_param = new InfoMode[this.modes];
      }

      for(var2 = 0; var2 < this.modes; ++var2) {
         this.mode_param[var2] = new InfoMode();
         this.mode_param[var2].blockflag = var1.read(1);
         this.mode_param[var2].windowtype = var1.read(16);
         this.mode_param[var2].transformtype = var1.read(16);
         this.mode_param[var2].mapping = var1.read(8);
         if (this.mode_param[var2].windowtype >= 1 || this.mode_param[var2].transformtype >= 1 || this.mode_param[var2].mapping >= this.maps) {
            this.clear();
            return -1;
         }
      }

      if (var1.read(1) != 1) {
         this.clear();
         return -1;
      } else {
         return 0;
      }
   }

   public int synthesis_headerin(Comment var1, Packet var2) {
      Buffer var3 = new Buffer();
      if (var2 != null) {
         var3.readinit(var2.packet_base, var2.packet, var2.bytes);
         byte[] var4 = new byte[6];
         int var5 = var3.read(8);
         var3.read(var4, 6);
         if (var4[0] != 118 || var4[1] != 111 || var4[2] != 114 || var4[3] != 98 || var4[4] != 105 || var4[5] != 115) {
            return -1;
         }

         switch(var5) {
         case 1:
            if (var2.b_o_s == 0) {
               return -1;
            }

            if (this.rate != 0) {
               return -1;
            }

            return this.unpack_info(var3);
         case 2:
         case 4:
         default:
            break;
         case 3:
            if (this.rate == 0) {
               return -1;
            }

            return var1.unpack(var3);
         case 5:
            if (this.rate != 0 && var1.vendor != null) {
               return this.unpack_books(var3);
            }

            return -1;
         }
      }

      return -1;
   }

   int pack_info(Buffer var1) {
      var1.write(1, 8);
      var1.write(_vorbis);
      var1.write(0, 32);
      var1.write(this.channels, 8);
      var1.write(this.rate, 32);
      var1.write(this.bitrate_upper, 32);
      var1.write(this.bitrate_nominal, 32);
      var1.write(this.bitrate_lower, 32);
      var1.write(Util.ilog2(this.blocksizes[0]), 4);
      var1.write(Util.ilog2(this.blocksizes[1]), 4);
      var1.write(1, 1);
      return 0;
   }

   int pack_books(Buffer var1) {
      var1.write(5, 8);
      var1.write(_vorbis);
      var1.write(this.books - 1, 8);

      int var2;
      for(var2 = 0; var2 < this.books; ++var2) {
         if (this.book_param[var2].pack(var1) != 0) {
            return -1;
         }
      }

      var1.write(this.times - 1, 6);

      for(var2 = 0; var2 < this.times; ++var2) {
         var1.write(this.time_type[var2], 16);
         FuncTime.time_P[this.time_type[var2]].pack(this.time_param[var2], var1);
      }

      var1.write(this.floors - 1, 6);

      for(var2 = 0; var2 < this.floors; ++var2) {
         var1.write(this.floor_type[var2], 16);
         FuncFloor.floor_P[this.floor_type[var2]].pack(this.floor_param[var2], var1);
      }

      var1.write(this.residues - 1, 6);

      for(var2 = 0; var2 < this.residues; ++var2) {
         var1.write(this.residue_type[var2], 16);
         FuncResidue.residue_P[this.residue_type[var2]].pack(this.residue_param[var2], var1);
      }

      var1.write(this.maps - 1, 6);

      for(var2 = 0; var2 < this.maps; ++var2) {
         var1.write(this.map_type[var2], 16);
         FuncMapping.mapping_P[this.map_type[var2]].pack(this, this.map_param[var2], var1);
      }

      var1.write(this.modes - 1, 6);

      for(var2 = 0; var2 < this.modes; ++var2) {
         var1.write(this.mode_param[var2].blockflag, 1);
         var1.write(this.mode_param[var2].windowtype, 16);
         var1.write(this.mode_param[var2].transformtype, 16);
         var1.write(this.mode_param[var2].mapping, 8);
      }

      var1.write(1, 1);
      return 0;
   }

   public int blocksize(Packet var1) {
      Buffer var2 = new Buffer();
      var2.readinit(var1.packet_base, var1.packet, var1.bytes);
      if (var2.read(1) != 0) {
         return -135;
      } else {
         int var4 = 0;

         for(int var5 = this.modes; var5 > 1; var5 >>>= 1) {
            ++var4;
         }

         int var3 = var2.read(var4);
         return var3 == -1 ? -136 : this.blocksizes[this.mode_param[var3].blockflag];
      }
   }

   public String toString() {
      return "version:" + new Integer(this.version) + ", channels:" + new Integer(this.channels) + ", rate:" + new Integer(this.rate) + ", bitrate:" + new Integer(this.bitrate_upper) + "," + new Integer(this.bitrate_nominal) + "," + new Integer(this.bitrate_lower);
   }
}
