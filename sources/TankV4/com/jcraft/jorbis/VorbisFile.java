package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class VorbisFile {
   static final int CHUNKSIZE = 8500;
   static final int SEEK_SET = 0;
   static final int SEEK_CUR = 1;
   static final int SEEK_END = 2;
   static final int OV_FALSE = -1;
   static final int OV_EOF = -2;
   static final int OV_HOLE = -3;
   static final int OV_EREAD = -128;
   static final int OV_EFAULT = -129;
   static final int OV_EIMPL = -130;
   static final int OV_EINVAL = -131;
   static final int OV_ENOTVORBIS = -132;
   static final int OV_EBADHEADER = -133;
   static final int OV_EVERSION = -134;
   static final int OV_ENOTAUDIO = -135;
   static final int OV_EBADPACKET = -136;
   static final int OV_EBADLINK = -137;
   static final int OV_ENOSEEK = -138;
   InputStream datasource;
   boolean seekable = false;
   long offset;
   long end;
   SyncState oy = new SyncState();
   int links;
   long[] offsets;
   long[] dataoffsets;
   int[] serialnos;
   long[] pcmlengths;
   Info[] vi;
   Comment[] vc;
   long pcm_offset;
   boolean decode_ready = false;
   int current_serialno;
   int current_link;
   float bittrack;
   float samptrack;
   StreamState os = new StreamState();
   DspState vd = new DspState();
   Block vb;

   public VorbisFile(String var1) throws JOrbisException {
      this.vb = new Block(this.vd);
      VorbisFile.SeekableInputStream var2 = null;

      try {
         var2 = new VorbisFile.SeekableInputStream(this, var1);
         int var3 = this.open(var2, (byte[])null, 0);
         if (var3 == -1) {
            throw new JOrbisException("VorbisFile: open return -1");
         }
      } catch (Exception var7) {
         throw new JOrbisException("VorbisFile: " + var7.toString());
      }

      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

   }

   public VorbisFile(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      this.vb = new Block(this.vd);
      int var4 = this.open(var1, var2, var3);
      if (var4 == -1) {
      }

   }

   private int get_data() {
      int var1 = this.oy.buffer(8500);
      byte[] var2 = this.oy.data;
      boolean var3 = false;

      int var6;
      try {
         var6 = this.datasource.read(var2, var1, 8500);
      } catch (Exception var5) {
         return -128;
      }

      this.oy.wrote(var6);
      if (var6 == -1) {
         var6 = 0;
      }

      return var6;
   }

   private void seek_helper(long var1) {
      fseek(this.datasource, var1, 0);
      this.offset = var1;
      this.oy.reset();
   }

   private int get_prev_page(Page var1) throws JOrbisException {
      long var2 = this.offset;
      int var5 = -1;

      label37:
      do {
         int var4;
         while(var5 == -1) {
            var2 -= 8500L;
            if (var2 < 0L) {
               var2 = 0L;
            }

            this.seek_helper(var2);

            while(this.offset < var2 + 8500L) {
               var4 = this.get_next_page(var1, var2 + 8500L - this.offset);
               if (var4 == -128) {
                  return -128;
               }

               if (var4 < 0) {
                  continue label37;
               }

               var5 = var4;
            }
         }

         this.seek_helper((long)var5);
         var4 = this.get_next_page(var1, 8500L);
         if (var4 < 0) {
            return -129;
         }

         return var5;
      } while(var5 != -1);

      throw new JOrbisException();
   }

   int fetch_headers(Info var1, Comment var2, int[] var3, Page var4) {
      Page var5 = new Page();
      Packet var6 = new Packet();
      if (var4 == null) {
         int var7 = this.get_next_page(var5, 8500L);
         if (var7 == -128) {
            return -128;
         }

         if (var7 < 0) {
            return -132;
         }

         var4 = var5;
      }

      if (var3 != null) {
         var3[0] = var4.serialno();
      }

      this.os.init(var4.serialno());
      var1.init();
      var2.init();
      int var8 = 0;

      do {
         if (var8 >= 3) {
            return 0;
         }

         this.os.pagein(var4);

         while(var8 < 3) {
            int var9 = this.os.packetout(var6);
            if (var9 == 0) {
               break;
            }

            if (var9 == -1) {
               var1.clear();
               var2.clear();
               this.os.clear();
               return -1;
            }

            if (var1.synthesis_headerin(var2, var6) != 0) {
               var1.clear();
               var2.clear();
               this.os.clear();
               return -1;
            }

            ++var8;
         }
      } while(var8 >= 3 || var4 >= 1L);

      var1.clear();
      var2.clear();
      this.os.clear();
      return -1;
   }

   void prefetch_all_headers(Info var1, Comment var2, int var3) throws JOrbisException {
      Page var4 = new Page();
      this.vi = new Info[this.links];
      this.vc = new Comment[this.links];
      this.dataoffsets = new long[this.links];
      this.pcmlengths = new long[this.links];
      this.serialnos = new int[this.links];

      label38:
      for(int var6 = 0; var6 < this.links; ++var6) {
         if (var1 != null && var2 != null && var6 == 0) {
            this.vi[var6] = var1;
            this.vc[var6] = var2;
            this.dataoffsets[var6] = (long)var3;
         } else {
            this.seek_helper(this.offsets[var6]);
            this.vi[var6] = new Info();
            this.vc[var6] = new Comment();
            if (this.fetch_headers(this.vi[var6], this.vc[var6], (int[])null, (Page)null) == -1) {
               this.dataoffsets[var6] = -1L;
            } else {
               this.dataoffsets[var6] = this.offset;
               this.os.clear();
            }
         }

         long var7 = this.offsets[var6 + 1];
         this.seek_helper(var7);

         do {
            int var5 = this.get_prev_page(var4);
            if (var5 == -1) {
               this.vi[var6].clear();
               this.vc[var6].clear();
               continue label38;
            }
         } while(var4.granulepos() == -1L);

         this.serialnos[var6] = var4.serialno();
         this.pcmlengths[var6] = var4.granulepos();
      }

   }

   private int make_decode_ready() {
      if (this.decode_ready) {
         System.exit(1);
      }

      this.vd.synthesis_init(this.vi[0]);
      this.vb.init(this.vd);
      this.decode_ready = true;
      return 0;
   }

   int open_seekable() throws JOrbisException {
      Info var1 = new Info();
      Comment var2 = new Comment();
      Page var8 = new Page();
      int[] var9 = new int[1];
      int var6 = this.fetch_headers(var1, var2, var9, (Page)null);
      int var3 = var9[0];
      int var7 = (int)this.offset;
      this.os.clear();
      if (var6 == -1) {
         return -1;
      } else if (var6 < 0) {
         return var6;
      } else {
         this.seekable = true;
         fseek(this.datasource, 0L, 2);
         this.offset = ftell(this.datasource);
         long var4 = this.offset;
         var4 = (long)this.get_prev_page(var8);
         long var10001;
         long var10003;
         if (var8.serialno() != var3) {
            var10001 = 0L;
            long var10002 = 0L;
            var10003 = var4 + 1L;
            if (var3 < false) {
               this.clear();
               return -128;
            }
         } else {
            var10001 = 0L;
            var10003 = var4 + 1L;
            if (var3 < false) {
               this.clear();
               return -128;
            }
         }

         this.prefetch_all_headers(var1, var2, var7);
         return 0;
      }
   }

   int open_nonseekable() {
      this.links = 1;
      this.vi = new Info[this.links];
      this.vi[0] = new Info();
      this.vc = new Comment[this.links];
      this.vc[0] = new Comment();
      int[] var1 = new int[1];
      if (this.fetch_headers(this.vi[0], this.vc[0], var1, (Page)null) == -1) {
         return -1;
      } else {
         this.current_serialno = var1[0];
         this.make_decode_ready();
         return 0;
      }
   }

   void decode_clear() {
      this.os.clear();
      this.vd.clear();
      this.vb.clear();
      this.decode_ready = false;
      this.bittrack = 0.0F;
      this.samptrack = 0.0F;
   }

   int clear() {
      this.vb.clear();
      this.vd.clear();
      this.os.clear();
      if (this.vi != null && this.links != 0) {
         for(int var1 = 0; var1 < this.links; ++var1) {
            this.vi[var1].clear();
            this.vc[var1].clear();
         }

         this.vi = null;
         this.vc = null;
      }

      if (this.dataoffsets != null) {
         this.dataoffsets = null;
      }

      if (this.pcmlengths != null) {
         this.pcmlengths = null;
      }

      if (this.serialnos != null) {
         this.serialnos = null;
      }

      if (this.offsets != null) {
         this.offsets = null;
      }

      this.oy.clear();
      return 0;
   }

   static int fseek(InputStream var0, long var1, int var3) {
      if (var0 instanceof VorbisFile.SeekableInputStream) {
         VorbisFile.SeekableInputStream var4 = (VorbisFile.SeekableInputStream)var0;

         try {
            if (var3 == 0) {
               var4.seek(var1);
            } else if (var3 == 2) {
               var4.seek(var4.getLength() - var1);
            }
         } catch (Exception var6) {
         }

         return 0;
      } else {
         try {
            if (var3 == 0) {
               var0.reset();
            }

            var0.skip(var1);
            return 0;
         } catch (Exception var7) {
            return -1;
         }
      }
   }

   static long ftell(InputStream var0) {
      try {
         if (var0 instanceof VorbisFile.SeekableInputStream) {
            VorbisFile.SeekableInputStream var1 = (VorbisFile.SeekableInputStream)var0;
            return var1.tell();
         }
      } catch (Exception var2) {
      }

      return 0L;
   }

   int open(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      return this.open_callbacks(var1, var2, var3);
   }

   int open_callbacks(InputStream var1, byte[] var2, int var3) throws JOrbisException {
      this.datasource = var1;
      this.oy.init();
      if (var2 != null) {
         int var5 = this.oy.buffer(var3);
         System.arraycopy(var2, 0, this.oy.data, var5, var3);
         this.oy.wrote(var3);
      }

      int var4;
      if (var1 instanceof VorbisFile.SeekableInputStream) {
         var4 = this.open_seekable();
      } else {
         var4 = this.open_nonseekable();
      }

      if (var4 != 0) {
         this.datasource = null;
         this.clear();
      }

      return var4;
   }

   public int streams() {
      return this.links;
   }

   public boolean seekable() {
      return this.seekable;
   }

   public int bitrate(int var1) {
      if (var1 >= this.links) {
         return -1;
      } else if (!this.seekable && var1 != 0) {
         return this.bitrate(0);
      } else if (var1 >= 0) {
         if (this.seekable) {
            return (int)Math.rint((double)((float)((this.offsets[var1 + 1] - this.dataoffsets[var1]) * 8L) / this.time_total(var1)));
         } else if (this.vi[var1].bitrate_nominal > 0) {
            return this.vi[var1].bitrate_nominal;
         } else if (this.vi[var1].bitrate_upper > 0) {
            return this.vi[var1].bitrate_lower > 0 ? (this.vi[var1].bitrate_upper + this.vi[var1].bitrate_lower) / 2 : this.vi[var1].bitrate_upper;
         } else {
            return -1;
         }
      } else {
         long var2 = 0L;

         for(int var4 = 0; var4 < this.links; ++var4) {
            var2 += (this.offsets[var4 + 1] - this.dataoffsets[var4]) * 8L;
         }

         return (int)Math.rint((double)((float)var2 / this.time_total(-1)));
      }
   }

   public int bitrate_instant() {
      int var1 = this.seekable ? this.current_link : 0;
      if (this.samptrack == 0.0F) {
         return -1;
      } else {
         int var2 = (int)((double)(this.bittrack / this.samptrack * (float)this.vi[var1].rate) + 0.5D);
         this.bittrack = 0.0F;
         this.samptrack = 0.0F;
         return var2;
      }
   }

   public int serialnumber(int var1) {
      if (var1 >= this.links) {
         return -1;
      } else if (!this.seekable && var1 >= 0) {
         return this.serialnumber(-1);
      } else {
         return var1 < 0 ? this.current_serialno : this.serialnos[var1];
      }
   }

   public long raw_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return this.offsets[var1 + 1] - this.offsets[var1];
         } else {
            long var2 = 0L;

            for(int var4 = 0; var4 < this.links; ++var4) {
               var2 += this.raw_total(var4);
            }

            return var2;
         }
      } else {
         return -1L;
      }
   }

   public long pcm_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return this.pcmlengths[var1];
         } else {
            long var2 = 0L;

            for(int var4 = 0; var4 < this.links; ++var4) {
               var2 += this.pcm_total(var4);
            }

            return var2;
         }
      } else {
         return -1L;
      }
   }

   public float time_total(int var1) {
      if (this.seekable && var1 < this.links) {
         if (var1 >= 0) {
            return (float)this.pcmlengths[var1] / (float)this.vi[var1].rate;
         } else {
            float var2 = 0.0F;

            for(int var3 = 0; var3 < this.links; ++var3) {
               var2 += this.time_total(var3);
            }

            return var2;
         }
      } else {
         return -1.0F;
      }
   }

   public int pcm_seek(long param1) {
      // $FF: Couldn't be decompiled
   }

   int time_seek(float var1) {
      boolean var2 = true;
      long var3 = this.pcm_total(-1);
      float var5 = this.time_total(-1);
      if (!this.seekable) {
         return -1;
      } else if (!(var1 < 0.0F) && !(var1 > var5)) {
         int var8;
         for(var8 = this.links - 1; var8 >= 0; --var8) {
            var3 -= this.pcmlengths[var8];
            var5 -= this.time_total(var8);
            if (var1 >= var5) {
               break;
            }
         }

         long var6 = (long)((float)var3 + (var1 - var5) * (float)this.vi[var8].rate);
         return this.pcm_seek(var6);
      } else {
         this.pcm_offset = -1L;
         this.decode_clear();
         return -1;
      }
   }

   public long raw_tell() {
      return this.offset;
   }

   public long pcm_tell() {
      return this.pcm_offset;
   }

   public float time_tell() {
      int var1 = -1;
      long var2 = 0L;
      float var4 = 0.0F;
      if (this.seekable) {
         var2 = this.pcm_total(-1);
         var4 = this.time_total(-1);

         for(var1 = this.links - 1; var1 >= 0; --var1) {
            var2 -= this.pcmlengths[var1];
            var4 -= this.time_total(var1);
            if (this.pcm_offset >= var2) {
               break;
            }
         }
      }

      return var4 + (float)(this.pcm_offset - var2) / (float)this.vi[var1].rate;
   }

   public Info getInfo(int var1) {
      if (this.seekable) {
         if (var1 < 0) {
            return this.decode_ready ? this.vi[this.current_link] : null;
         } else {
            return var1 >= this.links ? null : this.vi[var1];
         }
      } else {
         return this.decode_ready ? this.vi[0] : null;
      }
   }

   public Comment getComment(int var1) {
      if (this.seekable) {
         if (var1 < 0) {
            return this.decode_ready ? this.vc[this.current_link] : null;
         } else {
            return var1 >= this.links ? null : this.vc[var1];
         }
      } else {
         return this.decode_ready ? this.vc[0] : null;
      }
   }

   int host_is_big_endian() {
      return 1;
   }

   int read(byte[] var1, int var2, int var3, int var4, int var5, int[] var6) {
      int var7 = this.host_is_big_endian();
      int var8 = 0;

      while(true) {
         if (this.decode_ready) {
            float[][][] var10 = new float[1][][];
            int[] var11 = new int[this.getInfo(-1).channels];
            int var12 = this.vd.synthesis_pcmout(var10, var11);
            float[][] var9 = var10[0];
            if (var12 != 0) {
               int var13 = this.getInfo(-1).channels;
               int var14 = var4 * var13;
               if (var12 > var2 / var14) {
                  var12 = var2 / var14;
               }

               int var15;
               int var16;
               int var17;
               int var18;
               if (var4 == 1) {
                  var16 = var5 != 0 ? 0 : 128;

                  for(var17 = 0; var17 < var12; ++var17) {
                     for(var18 = 0; var18 < var13; ++var18) {
                        var15 = (int)((double)var9[var18][var11[var18] + var17] * 128.0D + 0.5D);
                        if (var15 > 127) {
                           var15 = 127;
                        } else if (var15 < -128) {
                           var15 = -128;
                        }

                        var1[var8++] = (byte)(var15 + var16);
                     }
                  }
               } else {
                  var16 = var5 != 0 ? 0 : '耀';
                  if (var7 == var3) {
                     int var19;
                     int var20;
                     if (var5 != 0) {
                        for(var17 = 0; var17 < var13; ++var17) {
                           var18 = var11[var17];
                           var19 = var17;

                           for(var20 = 0; var20 < var12; ++var20) {
                              var15 = (int)((double)var9[var17][var18 + var20] * 32768.0D + 0.5D);
                              if (var15 > 32767) {
                                 var15 = 32767;
                              } else if (var15 < -32768) {
                                 var15 = -32768;
                              }

                              var1[var19] = (byte)(var15 >>> 8);
                              var1[var19 + 1] = (byte)var15;
                              var19 += var13 * 2;
                           }
                        }
                     } else {
                        for(var17 = 0; var17 < var13; ++var17) {
                           float[] var21 = var9[var17];
                           var19 = var17;

                           for(var20 = 0; var20 < var12; ++var20) {
                              var15 = (int)((double)var21[var20] * 32768.0D + 0.5D);
                              if (var15 > 32767) {
                                 var15 = 32767;
                              } else if (var15 < -32768) {
                                 var15 = -32768;
                              }

                              var1[var19] = (byte)(var15 + var16 >>> 8);
                              var1[var19 + 1] = (byte)(var15 + var16);
                              var19 += var13 * 2;
                           }
                        }
                     }
                  } else if (var3 != 0) {
                     for(var17 = 0; var17 < var12; ++var17) {
                        for(var18 = 0; var18 < var13; ++var18) {
                           var15 = (int)((double)var9[var18][var17] * 32768.0D + 0.5D);
                           if (var15 > 32767) {
                              var15 = 32767;
                           } else if (var15 < -32768) {
                              var15 = -32768;
                           }

                           var15 += var16;
                           var1[var8++] = (byte)(var15 >>> 8);
                           var1[var8++] = (byte)var15;
                        }
                     }
                  } else {
                     for(var17 = 0; var17 < var12; ++var17) {
                        for(var18 = 0; var18 < var13; ++var18) {
                           var15 = (int)((double)var9[var18][var17] * 32768.0D + 0.5D);
                           if (var15 > 32767) {
                              var15 = 32767;
                           } else if (var15 < -32768) {
                              var15 = -32768;
                           }

                           var15 += var16;
                           var1[var8++] = (byte)var15;
                           var1[var8++] = (byte)(var15 >>> 8);
                        }
                     }
                  }
               }

               this.vd.synthesis_read(var12);
               this.pcm_offset += (long)var12;
               if (var6 != null) {
                  var6[0] = this.current_link;
               }

               return var12 * var14;
            }
         }

         switch(this.process_packet(1)) {
         case -1:
            return -1;
         case 0:
            return 0;
         }
      }
   }

   public Info[] getInfo() {
      return this.vi;
   }

   public Comment[] getComment() {
      return this.vc;
   }

   public void close() throws IOException {
      this.datasource.close();
   }

   class SeekableInputStream extends InputStream {
      RandomAccessFile raf;
      final String mode;
      final VorbisFile this$0;

      SeekableInputStream(VorbisFile var1, String var2) throws IOException {
         this.this$0 = var1;
         this.raf = null;
         this.mode = "r";
         this.raf = new RandomAccessFile(var2, "r");
      }

      public int read() throws IOException {
         return this.raf.read();
      }

      public int read(byte[] var1) throws IOException {
         return this.raf.read(var1);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return this.raf.read(var1, var2, var3);
      }

      public long skip(long var1) throws IOException {
         return (long)this.raf.skipBytes((int)var1);
      }

      public long getLength() throws IOException {
         return this.raf.length();
      }

      public long tell() throws IOException {
         return this.raf.getFilePointer();
      }

      public int available() throws IOException {
         return this.raf.length() == this.raf.getFilePointer() ? 0 : 1;
      }

      public void close() throws IOException {
         this.raf.close();
      }

      public synchronized void mark(int var1) {
      }

      public synchronized void reset() throws IOException {
      }

      public boolean markSupported() {
         return false;
      }

      public void seek(long var1) throws IOException {
         this.raf.seek(var1);
      }
   }
}
