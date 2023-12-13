package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class BZip2CompressorInputStream extends CompressorInputStream implements BZip2Constants {
   private int last;
   private int origPtr;
   private int blockSize100k;
   private boolean blockRandomised;
   private int bsBuff;
   private int bsLive;
   private final CRC crc;
   private int nInUse;
   private InputStream in;
   private final boolean decompressConcatenated;
   private static final int EOF = 0;
   private static final int START_BLOCK_STATE = 1;
   private static final int RAND_PART_A_STATE = 2;
   private static final int RAND_PART_B_STATE = 3;
   private static final int RAND_PART_C_STATE = 4;
   private static final int NO_RAND_PART_A_STATE = 5;
   private static final int NO_RAND_PART_B_STATE = 6;
   private static final int NO_RAND_PART_C_STATE = 7;
   private int currentState;
   private int storedBlockCRC;
   private int storedCombinedCRC;
   private int computedBlockCRC;
   private int computedCombinedCRC;
   private int su_count;
   private int su_ch2;
   private int su_chPrev;
   private int su_i2;
   private int su_j2;
   private int su_rNToGo;
   private int su_rTPos;
   private int su_tPos;
   private char su_z;
   private BZip2CompressorInputStream.Data data;

   public BZip2CompressorInputStream(InputStream var1) throws IOException {
      this(var1, false);
   }

   public BZip2CompressorInputStream(InputStream var1, boolean var2) throws IOException {
      this.crc = new CRC();
      this.currentState = 1;
      this.in = var1;
      this.decompressConcatenated = var2;
      this.init(true);
      this.initBlock();
   }

   public int read() throws IOException {
      if (this.in != null) {
         int var1 = this.read0();
         this.count(var1 < 0 ? -1 : 1);
         return var1;
      } else {
         throw new IOException("stream closed");
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException("offs(" + var2 + ") < 0.");
      } else if (var3 < 0) {
         throw new IndexOutOfBoundsException("len(" + var3 + ") < 0.");
      } else if (var2 + var3 > var1.length) {
         throw new IndexOutOfBoundsException("offs(" + var2 + ") + len(" + var3 + ") > dest.length(" + var1.length + ").");
      } else if (this.in == null) {
         throw new IOException("stream closed");
      } else {
         int var4 = var2 + var3;
         int var5 = var2;

         int var6;
         while(var5 < var4 && (var6 = this.read0()) >= 0) {
            var1[var5++] = (byte)var6;
            this.count(1);
         }

         int var7 = var5 == var2 ? -1 : var5 - var2;
         return var7;
      }
   }

   private void makeMaps() {
      boolean[] var1 = this.data.inUse;
      byte[] var2 = this.data.seqToUnseq;
      int var3 = 0;

      for(int var4 = 0; var4 < 256; ++var4) {
         if (var1[var4]) {
            var2[var3++] = (byte)var4;
         }
      }

      this.nInUse = var3;
   }

   private int read0() throws IOException {
      switch(this.currentState) {
      case 0:
         return -1;
      case 1:
         return this.setupBlock();
      case 2:
         throw new IllegalStateException();
      case 3:
         return this.setupRandPartB();
      case 4:
         return this.setupRandPartC();
      case 5:
         throw new IllegalStateException();
      case 6:
         return this.setupNoRandPartB();
      case 7:
         return this.setupNoRandPartC();
      default:
         throw new IllegalStateException();
      }
   }

   private void initBlock() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void endBlock() throws IOException {
      this.computedBlockCRC = this.crc.getFinalCRC();
      if (this.storedBlockCRC != this.computedBlockCRC) {
         this.computedCombinedCRC = this.storedCombinedCRC << 1 | this.storedCombinedCRC >>> 31;
         this.computedCombinedCRC ^= this.storedBlockCRC;
         throw new IOException("BZip2 CRC error");
      } else {
         this.computedCombinedCRC = this.computedCombinedCRC << 1 | this.computedCombinedCRC >>> 31;
         this.computedCombinedCRC ^= this.computedBlockCRC;
      }
   }

   public void close() throws IOException {
      InputStream var1 = this.in;
      if (var1 != null) {
         if (var1 != System.in) {
            var1.close();
         }

         this.data = null;
         this.in = null;
      }

   }

   private int bsR(int var1) throws IOException {
      int var2 = this.bsLive;
      int var3 = this.bsBuff;
      if (var2 < var1) {
         InputStream var4 = this.in;

         do {
            int var5 = var4.read();
            if (var5 < 0) {
               throw new IOException("unexpected end of stream");
            }

            var3 = var3 << 8 | var5;
            var2 += 8;
         } while(var2 < var1);

         this.bsBuff = var3;
      }

      this.bsLive = var2 - var1;
      return var3 >> var2 - var1 & (1 << var1) - 1;
   }

   private char bsGetUByte() throws IOException {
      return (char)this.bsR(8);
   }

   private int bsGetInt() throws IOException {
      return ((this.bsR(8) << 8 | this.bsR(8)) << 8 | this.bsR(8)) << 8 | this.bsR(8);
   }

   private static void hbCreateDecodeTables(int[] var0, int[] var1, int[] var2, char[] var3, int var4, int var5, int var6) {
      int var7 = var4;

      int var8;
      int var9;
      for(var8 = 0; var7 <= var5; ++var7) {
         for(var9 = 0; var9 < var6; ++var9) {
            if (var3[var9] == var7) {
               var2[var8++] = var9;
            }
         }
      }

      var7 = 23;

      while(true) {
         --var7;
         if (var7 <= 0) {
            for(var7 = 0; var7 < var6; ++var7) {
               ++var1[var3[var7] + 1];
            }

            var7 = 1;

            for(var8 = var1[0]; var7 < 23; ++var7) {
               var8 += var1[var7];
               var1[var7] = var8;
            }

            var7 = var4;
            var8 = 0;

            for(var9 = var1[var4]; var7 <= var5; ++var7) {
               int var10 = var1[var7 + 1];
               var8 += var10 - var9;
               var9 = var10;
               var0[var7] = var8 - 1;
               var8 <<= 1;
            }

            for(var7 = var4 + 1; var7 <= var5; ++var7) {
               var1[var7] = (var0[var7 - 1] + 1 << 1) - var1[var7];
            }

            return;
         }

         var1[var7] = 0;
         var0[var7] = 0;
      }
   }

   private void recvDecodingTables() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void createHuffmanDecodingTables(int var1, int var2) {
      BZip2CompressorInputStream.Data var3 = this.data;
      char[][] var4 = var3.temp_charArray2d;
      int[] var5 = var3.minLens;
      int[][] var6 = var3.limit;
      int[][] var7 = var3.base;
      int[][] var8 = var3.perm;

      for(int var9 = 0; var9 < var2; ++var9) {
         char var10 = ' ';
         char var11 = 0;
         char[] var12 = var4[var9];
         int var13 = var1;

         while(true) {
            --var13;
            if (var13 < 0) {
               hbCreateDecodeTables(var6[var9], var7[var9], var8[var9], var4[var9], var10, var11, var1);
               var5[var9] = var10;
               break;
            }

            char var14 = var12[var13];
            if (var14 > var11) {
               var11 = var14;
            }

            if (var14 < var10) {
               var10 = var14;
            }
         }
      }

   }

   private void getAndMoveToFrontDecode() throws IOException {
      this.origPtr = this.bsR(24);
      this.recvDecodingTables();
      InputStream var1 = this.in;
      BZip2CompressorInputStream.Data var2 = this.data;
      byte[] var3 = var2.ll8;
      int[] var4 = var2.unzftab;
      byte[] var5 = var2.selector;
      byte[] var6 = var2.seqToUnseq;
      char[] var7 = var2.getAndMoveToFrontDecode_yy;
      int[] var8 = var2.minLens;
      int[][] var9 = var2.limit;
      int[][] var10 = var2.base;
      int[][] var11 = var2.perm;
      int var12 = this.blockSize100k * 100000;
      int var13 = 256;

      while(true) {
         --var13;
         if (var13 < 0) {
            var13 = 0;
            int var14 = 49;
            int var15 = this.nInUse + 1;
            int var16 = this.getAndMoveToFrontDecode0(0);
            int var17 = this.bsBuff;
            int var18 = this.bsLive;
            int var19 = -1;
            int var20 = var5[var13] & 255;
            int[] var21 = var10[var20];
            int[] var22 = var9[var20];
            int[] var23 = var11[var20];
            int var24 = var8[var20];

            while(true) {
               while(var16 != var15) {
                  int var26;
                  int var27;
                  int var28;
                  if (var16 != 0 && var16 != 1) {
                     ++var19;
                     if (var19 >= var12) {
                        throw new IOException("block overrun");
                     }

                     char var30 = var7[var16 - 1];
                     ++var4[var6[var30] & 255];
                     var3[var19] = var6[var30];
                     if (var16 <= 16) {
                        for(var26 = var16 - 1; var26 > 0; var7[var26--] = var7[var26]) {
                        }
                     } else {
                        System.arraycopy(var7, 0, var7, 1, var16 - 1);
                     }

                     var7[0] = var30;
                     if (var14 == 0) {
                        var14 = 49;
                        ++var13;
                        var20 = var5[var13] & 255;
                        var21 = var10[var20];
                        var22 = var9[var20];
                        var23 = var11[var20];
                        var24 = var8[var20];
                     } else {
                        --var14;
                     }

                     for(var26 = var24; var18 < var26; var18 += 8) {
                        var27 = var1.read();
                        if (var27 < 0) {
                           throw new IOException("unexpected end of stream");
                        }

                        var17 = var17 << 8 | var27;
                     }

                     var27 = var17 >> var18 - var26 & (1 << var26) - 1;

                     for(var18 -= var26; var27 > var22[var26]; var27 = var27 << 1 | var17 >> var18 & 1) {
                        ++var26;

                        while(var18 < 1) {
                           var28 = var1.read();
                           if (var28 < 0) {
                              throw new IOException("unexpected end of stream");
                           }

                           var17 = var17 << 8 | var28;
                           var18 += 8;
                        }

                        --var18;
                     }

                     var16 = var23[var27 - var21[var26]];
                  } else {
                     int var25 = -1;
                     var26 = 1;

                     while(true) {
                        if (var16 == 0) {
                           var25 += var26;
                        } else {
                           if (var16 != 1) {
                              byte var31 = var6[var7[0]];

                              for(var4[var31 & 255] += var25 + 1; var25-- >= 0; var3[var19] = var31) {
                                 ++var19;
                              }

                              if (var19 >= var12) {
                                 throw new IOException("block overrun");
                              }
                              break;
                           }

                           var25 += var26 << 1;
                        }

                        if (var14 == 0) {
                           var14 = 49;
                           ++var13;
                           var20 = var5[var13] & 255;
                           var21 = var10[var20];
                           var22 = var9[var20];
                           var23 = var11[var20];
                           var24 = var8[var20];
                        } else {
                           --var14;
                        }

                        for(var27 = var24; var18 < var27; var18 += 8) {
                           var28 = var1.read();
                           if (var28 < 0) {
                              throw new IOException("unexpected end of stream");
                           }

                           var17 = var17 << 8 | var28;
                        }

                        var28 = var17 >> var18 - var27 & (1 << var27) - 1;

                        for(var18 -= var27; var28 > var22[var27]; var28 = var28 << 1 | var17 >> var18 & 1) {
                           ++var27;

                           while(var18 < 1) {
                              int var29 = var1.read();
                              if (var29 < 0) {
                                 throw new IOException("unexpected end of stream");
                              }

                              var17 = var17 << 8 | var29;
                              var18 += 8;
                           }

                           --var18;
                        }

                        var16 = var23[var28 - var21[var27]];
                        var26 <<= 1;
                     }
                  }
               }

               this.last = var19;
               this.bsLive = var18;
               this.bsBuff = var17;
               return;
            }
         }

         var7[var13] = (char)var13;
         var4[var13] = 0;
      }
   }

   private int getAndMoveToFrontDecode0(int var1) throws IOException {
      InputStream var2 = this.in;
      BZip2CompressorInputStream.Data var3 = this.data;
      int var4 = var3.selector[var1] & 255;
      int[] var5 = var3.limit[var4];
      int var6 = var3.minLens[var4];
      int var7 = this.bsR(var6);
      int var8 = this.bsLive;

      int var9;
      for(var9 = this.bsBuff; var7 > var5[var6]; var7 = var7 << 1 | var9 >> var8 & 1) {
         ++var6;

         while(var8 < 1) {
            int var10 = var2.read();
            if (var10 < 0) {
               throw new IOException("unexpected end of stream");
            }

            var9 = var9 << 8 | var10;
            var8 += 8;
         }

         --var8;
      }

      this.bsLive = var8;
      this.bsBuff = var9;
      return var3.perm[var4][var7 - var3.base[var4][var6]];
   }

   private int setupBlock() throws IOException {
      if (this.currentState != 0 && this.data != null) {
         int[] var1 = this.data.cftab;
         int[] var2 = this.data.initTT(this.last + 1);
         byte[] var3 = this.data.ll8;
         var1[0] = 0;
         System.arraycopy(this.data.unzftab, 0, var1, 1, 256);
         int var4 = 1;

         int var5;
         for(var5 = var1[0]; var4 <= 256; ++var4) {
            var5 += var1[var4];
            var1[var4] = var5;
         }

         var4 = 0;

         int var10004;
         for(var5 = this.last; var4 <= var5; var2[var10004] = var4++) {
            int var10002 = var3[var4] & 255;
            var10004 = var1[var3[var4] & 255];
            var1[var10002] = var1[var3[var4] & 255] + 1;
         }

         if (this.origPtr >= 0 && this.origPtr < var2.length) {
            this.su_tPos = var2[this.origPtr];
            this.su_count = 0;
            this.su_i2 = 0;
            this.su_ch2 = 256;
            if (this.blockRandomised) {
               this.su_rNToGo = 0;
               this.su_rTPos = 0;
               return this.setupRandPartA();
            } else {
               return this.setupNoRandPartA();
            }
         } else {
            throw new IOException("stream corrupted");
         }
      } else {
         return -1;
      }
   }

   private int setupRandPartA() throws IOException {
      if (this.su_i2 <= this.last) {
         this.su_chPrev = this.su_ch2;
         int var1 = this.data.ll8[this.su_tPos] & 255;
         this.su_tPos = this.data.tt[this.su_tPos];
         if (this.su_rNToGo == 0) {
            this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
            if (++this.su_rTPos == 512) {
               this.su_rTPos = 0;
            }
         } else {
            --this.su_rNToGo;
         }

         this.su_ch2 = var1 ^= this.su_rNToGo == 1 ? 1 : 0;
         ++this.su_i2;
         this.currentState = 3;
         this.crc.updateCRC(var1);
         return var1;
      } else {
         this.endBlock();
         this.initBlock();
         return this.setupBlock();
      }
   }

   private int setupNoRandPartA() throws IOException {
      if (this.su_i2 <= this.last) {
         this.su_chPrev = this.su_ch2;
         int var1 = this.data.ll8[this.su_tPos] & 255;
         this.su_ch2 = var1;
         this.su_tPos = this.data.tt[this.su_tPos];
         ++this.su_i2;
         this.currentState = 6;
         this.crc.updateCRC(var1);
         return var1;
      } else {
         this.currentState = 5;
         this.endBlock();
         this.initBlock();
         return this.setupBlock();
      }
   }

   private int setupRandPartB() throws IOException {
      if (this.su_ch2 != this.su_chPrev) {
         this.currentState = 2;
         this.su_count = 1;
         return this.setupRandPartA();
      } else if (++this.su_count >= 4) {
         this.su_z = (char)(this.data.ll8[this.su_tPos] & 255);
         this.su_tPos = this.data.tt[this.su_tPos];
         if (this.su_rNToGo == 0) {
            this.su_rNToGo = Rand.rNums(this.su_rTPos) - 1;
            if (++this.su_rTPos == 512) {
               this.su_rTPos = 0;
            }
         } else {
            --this.su_rNToGo;
         }

         this.su_j2 = 0;
         this.currentState = 4;
         if (this.su_rNToGo == 1) {
            this.su_z = (char)(this.su_z ^ 1);
         }

         return this.setupRandPartC();
      } else {
         this.currentState = 2;
         return this.setupRandPartA();
      }
   }

   private int setupRandPartC() throws IOException {
      if (this.su_j2 < this.su_z) {
         this.crc.updateCRC(this.su_ch2);
         ++this.su_j2;
         return this.su_ch2;
      } else {
         this.currentState = 2;
         ++this.su_i2;
         this.su_count = 0;
         return this.setupRandPartA();
      }
   }

   private int setupNoRandPartB() throws IOException {
      if (this.su_ch2 != this.su_chPrev) {
         this.su_count = 1;
         return this.setupNoRandPartA();
      } else if (++this.su_count >= 4) {
         this.su_z = (char)(this.data.ll8[this.su_tPos] & 255);
         this.su_tPos = this.data.tt[this.su_tPos];
         this.su_j2 = 0;
         return this.setupNoRandPartC();
      } else {
         return this.setupNoRandPartA();
      }
   }

   private int setupNoRandPartC() throws IOException {
      if (this.su_j2 < this.su_z) {
         int var1 = this.su_ch2;
         this.crc.updateCRC(var1);
         ++this.su_j2;
         this.currentState = 7;
         return var1;
      } else {
         ++this.su_i2;
         this.su_count = 0;
         return this.setupNoRandPartA();
      }
   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < 3) {
         return false;
      } else if (var0[0] != 66) {
         return false;
      } else if (var0[1] != 90) {
         return false;
      } else {
         return var0[2] == 104;
      }
   }

   private static final class Data {
      final boolean[] inUse = new boolean[256];
      final byte[] seqToUnseq = new byte[256];
      final byte[] selector = new byte[18002];
      final byte[] selectorMtf = new byte[18002];
      final int[] unzftab = new int[256];
      final int[][] limit = new int[6][258];
      final int[][] base = new int[6][258];
      final int[][] perm = new int[6][258];
      final int[] minLens = new int[6];
      final int[] cftab = new int[257];
      final char[] getAndMoveToFrontDecode_yy = new char[256];
      final char[][] temp_charArray2d = new char[6][258];
      final byte[] recvDecodingTables_pos = new byte[6];
      int[] tt;
      byte[] ll8;

      Data(int var1) {
         this.ll8 = new byte[var1 * 100000];
      }

      int[] initTT(int var1) {
         int[] var2 = this.tt;
         if (var2 == null || var2.length < var1) {
            this.tt = var2 = new int[var1];
         }

         return var2;
      }
   }
}
