package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class BZip2CompressorOutputStream extends CompressorOutputStream implements BZip2Constants {
   public static final int MIN_BLOCKSIZE = 1;
   public static final int MAX_BLOCKSIZE = 9;
   private static final int GREATER_ICOST = 15;
   private static final int LESSER_ICOST = 0;
   private int last;
   private final int blockSize100k;
   private int bsBuff;
   private int bsLive;
   private final CRC crc;
   private int nInUse;
   private int nMTF;
   private int currentChar;
   private int runLength;
   private int blockCRC;
   private int combinedCRC;
   private final int allowableBlockSize;
   private BZip2CompressorOutputStream.Data data;
   private BlockSort blockSorter;
   private OutputStream out;

   private static void hbMakeCodeLengths(byte[] var0, int[] var1, BZip2CompressorOutputStream.Data var2, int var3, int var4) {
      int[] var5 = var2.heap;
      int[] var6 = var2.weight;
      int[] var7 = var2.parent;
      int var8 = var3;

      while(true) {
         --var8;
         if (var8 < 0) {
            boolean var19 = true;

            while(true) {
               int var11;
               int var12;
               do {
                  if (!var19) {
                     return;
                  }

                  var19 = false;
                  int var9 = var3;
                  int var10 = 0;
                  var5[0] = 0;
                  var6[0] = 0;
                  var7[0] = -2;

                  int var13;
                  for(var11 = 1; var11 <= var3; ++var11) {
                     var7[var11] = -1;
                     ++var10;
                     var5[var10] = var11;
                     var12 = var10;

                     for(var13 = var5[var10]; var6[var13] < var6[var5[var12 >> 1]]; var12 >>= 1) {
                        var5[var12] = var5[var12 >> 1];
                     }

                     var5[var12] = var13;
                  }

                  int var14;
                  while(var10 > 1) {
                     var11 = var5[1];
                     var5[1] = var5[var10];
                     --var10;
                     boolean var20 = false;
                     var13 = 1;
                     var14 = var5[1];

                     while(true) {
                        var12 = var13 << 1;
                        if (var12 > var10) {
                           break;
                        }

                        if (var12 < var10 && var6[var5[var12 + 1]] < var6[var5[var12]]) {
                           ++var12;
                        }

                        if (var6[var14] < var6[var5[var12]]) {
                           break;
                        }

                        var5[var13] = var5[var12];
                        var13 = var12;
                     }

                     var5[var13] = var14;
                     int var15 = var5[1];
                     var5[1] = var5[var10];
                     --var10;
                     var20 = false;
                     var13 = 1;
                     var14 = var5[1];

                     while(true) {
                        var12 = var13 << 1;
                        if (var12 > var10) {
                           break;
                        }

                        if (var12 < var10 && var6[var5[var12 + 1]] < var6[var5[var12]]) {
                           ++var12;
                        }

                        if (var6[var14] < var6[var5[var12]]) {
                           break;
                        }

                        var5[var13] = var5[var12];
                        var13 = var12;
                     }

                     var5[var13] = var14;
                     ++var9;
                     var7[var11] = var7[var15] = var9;
                     int var16 = var6[var11];
                     int var17 = var6[var15];
                     var6[var9] = (var16 & -256) + (var17 & -256) | 1 + ((var16 & 255) > (var17 & 255) ? var16 & 255 : var17 & 255);
                     var7[var9] = -1;
                     ++var10;
                     var5[var10] = var9;
                     boolean var21 = false;
                     var13 = var10;
                     var14 = var5[var10];

                     for(int var18 = var6[var14]; var18 < var6[var5[var13 >> 1]]; var13 >>= 1) {
                        var5[var13] = var5[var13 >> 1];
                     }

                     var5[var13] = var14;
                  }

                  for(var11 = 1; var11 <= var3; ++var11) {
                     var12 = 0;

                     for(var13 = var11; (var14 = var7[var13]) >= 0; ++var12) {
                        var13 = var14;
                     }

                     var0[var11 - 1] = (byte)var12;
                     if (var12 > var4) {
                        var19 = true;
                     }
                  }
               } while(!var19);

               for(var11 = 1; var11 < var3; ++var11) {
                  var12 = var6[var11] >> 8;
                  var12 = 1 + (var12 >> 1);
                  var6[var11] = var12 << 8;
               }
            }
         }

         var6[var8 + 1] = (var1[var8] == 0 ? 1 : var1[var8]) << 8;
      }
   }

   public static int chooseBlockSize(long var0) {
      return var0 > 0L ? (int)Math.min(var0 / 132000L + 1L, 9L) : 9;
   }

   public BZip2CompressorOutputStream(OutputStream var1) throws IOException {
      this(var1, 9);
   }

   public BZip2CompressorOutputStream(OutputStream var1, int var2) throws IOException {
      this.crc = new CRC();
      this.currentChar = -1;
      this.runLength = 0;
      if (var2 < 1) {
         throw new IllegalArgumentException("blockSize(" + var2 + ") < 1");
      } else if (var2 > 9) {
         throw new IllegalArgumentException("blockSize(" + var2 + ") > 9");
      } else {
         this.blockSize100k = var2;
         this.out = var1;
         this.allowableBlockSize = this.blockSize100k * 100000 - 20;
         this.init();
      }
   }

   public void write(int var1) throws IOException {
      if (this.out != null) {
         this.write0(var1);
      } else {
         throw new IOException("closed");
      }
   }

   private void writeRun() throws IOException {
      int var1 = this.last;
      if (var1 < this.allowableBlockSize) {
         int var2 = this.currentChar;
         BZip2CompressorOutputStream.Data var3 = this.data;
         var3.inUse[var2] = true;
         byte var4 = (byte)var2;
         int var5 = this.runLength;
         this.crc.updateCRC(var2, var5);
         byte[] var6;
         switch(var5) {
         case 1:
            var3.block[var1 + 2] = var4;
            this.last = var1 + 1;
            break;
         case 2:
            var3.block[var1 + 2] = var4;
            var3.block[var1 + 3] = var4;
            this.last = var1 + 2;
            break;
         case 3:
            var6 = var3.block;
            var6[var1 + 2] = var4;
            var6[var1 + 3] = var4;
            var6[var1 + 4] = var4;
            this.last = var1 + 3;
            break;
         default:
            var5 -= 4;
            var3.inUse[var5] = true;
            var6 = var3.block;
            var6[var1 + 2] = var4;
            var6[var1 + 3] = var4;
            var6[var1 + 4] = var4;
            var6[var1 + 5] = var4;
            var6[var1 + 6] = (byte)var5;
            this.last = var1 + 5;
         }
      } else {
         this.endBlock();
         this.initBlock();
         this.writeRun();
      }

   }

   protected void finalize() throws Throwable {
      this.finish();
      super.finalize();
   }

   public void finish() throws IOException {
      if (this.out != null) {
         if (this.runLength > 0) {
            this.writeRun();
         }

         this.currentChar = -1;
         this.endBlock();
         this.endCompression();
         this.out = null;
         this.data = null;
         this.blockSorter = null;
      }

   }

   public void close() throws IOException {
      if (this.out != null) {
         OutputStream var1 = this.out;
         this.finish();
         var1.close();
      }

   }

   public void flush() throws IOException {
      OutputStream var1 = this.out;
      if (var1 != null) {
         var1.flush();
      }

   }

   private void init() throws IOException {
      this.bsPutUByte(66);
      this.bsPutUByte(90);
      this.data = new BZip2CompressorOutputStream.Data(this.blockSize100k);
      this.blockSorter = new BlockSort(this.data);
      this.bsPutUByte(104);
      this.bsPutUByte(48 + this.blockSize100k);
      this.combinedCRC = 0;
      this.initBlock();
   }

   private void initBlock() {
      this.crc.initialiseCRC();
      this.last = -1;
      boolean[] var1 = this.data.inUse;
      int var2 = 256;

      while(true) {
         --var2;
         if (var2 < 0) {
            return;
         }

         var1[var2] = false;
      }
   }

   private void endBlock() throws IOException {
      this.blockCRC = this.crc.getFinalCRC();
      this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
      this.combinedCRC ^= this.blockCRC;
      if (this.last != -1) {
         this.blockSort();
         this.bsPutUByte(49);
         this.bsPutUByte(65);
         this.bsPutUByte(89);
         this.bsPutUByte(38);
         this.bsPutUByte(83);
         this.bsPutUByte(89);
         this.bsPutInt(this.blockCRC);
         this.bsW(1, 0);
         this.moveToFrontCodeAndSend();
      }
   }

   private void endCompression() throws IOException {
      this.bsPutUByte(23);
      this.bsPutUByte(114);
      this.bsPutUByte(69);
      this.bsPutUByte(56);
      this.bsPutUByte(80);
      this.bsPutUByte(144);
      this.bsPutInt(this.combinedCRC);
      this.bsFinishedWithStream();
   }

   public final int getBlockSize() {
      return this.blockSize100k;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException("offs(" + var2 + ") < 0.");
      } else if (var3 < 0) {
         throw new IndexOutOfBoundsException("len(" + var3 + ") < 0.");
      } else if (var2 + var3 > var1.length) {
         throw new IndexOutOfBoundsException("offs(" + var2 + ") + len(" + var3 + ") > buf.length(" + var1.length + ").");
      } else if (this.out == null) {
         throw new IOException("stream closed");
      } else {
         int var4 = var2 + var3;

         while(var2 < var4) {
            this.write0(var1[var2++]);
         }

      }
   }

   private void write0(int var1) throws IOException {
      if (this.currentChar != -1) {
         var1 &= 255;
         if (this.currentChar == var1) {
            if (++this.runLength > 254) {
               this.writeRun();
               this.currentChar = -1;
               this.runLength = 0;
            }
         } else {
            this.writeRun();
            this.runLength = 1;
            this.currentChar = var1;
         }
      } else {
         this.currentChar = var1 & 255;
         ++this.runLength;
      }

   }

   private static void hbAssignCodes(int[] var0, byte[] var1, int var2, int var3, int var4) {
      int var5 = 0;

      for(int var6 = var2; var6 <= var3; ++var6) {
         for(int var7 = 0; var7 < var4; ++var7) {
            if ((var1[var7] & 255) == var6) {
               var0[var7] = var5++;
            }
         }

         var5 <<= 1;
      }

   }

   private void bsFinishedWithStream() throws IOException {
      while(this.bsLive > 0) {
         int var1 = this.bsBuff >> 24;
         this.out.write(var1);
         this.bsBuff <<= 8;
         this.bsLive -= 8;
      }

   }

   private void bsW(int var1, int var2) throws IOException {
      OutputStream var3 = this.out;
      int var4 = this.bsLive;

      int var5;
      for(var5 = this.bsBuff; var4 >= 8; var4 -= 8) {
         var3.write(var5 >> 24);
         var5 <<= 8;
      }

      this.bsBuff = var5 | var2 << 32 - var4 - var1;
      this.bsLive = var4 + var1;
   }

   private void bsPutUByte(int var1) throws IOException {
      this.bsW(8, var1);
   }

   private void bsPutInt(int var1) throws IOException {
      this.bsW(8, var1 >> 24 & 255);
      this.bsW(8, var1 >> 16 & 255);
      this.bsW(8, var1 >> 8 & 255);
      this.bsW(8, var1 & 255);
   }

   private void sendMTFValues() throws IOException {
      byte[][] var1 = this.data.sendMTFValues_len;
      int var2 = this.nInUse + 2;
      int var3 = 6;

      while(true) {
         --var3;
         if (var3 < 0) {
            var3 = this.nMTF < 200 ? 2 : (this.nMTF < 600 ? 3 : (this.nMTF < 1200 ? 4 : (this.nMTF < 2400 ? 5 : 6)));
            this.sendMTFValues0(var3, var2);
            int var6 = this.sendMTFValues1(var3, var2);
            this.sendMTFValues2(var3, var6);
            this.sendMTFValues3(var3, var2);
            this.sendMTFValues4();
            this.sendMTFValues5(var3, var6);
            this.sendMTFValues6(var3, var2);
            this.sendMTFValues7();
            return;
         }

         byte[] var4 = var1[var3];
         int var5 = var2;

         while(true) {
            --var5;
            if (var5 < 0) {
               break;
            }

            var4[var5] = 15;
         }
      }
   }

   private void sendMTFValues0(int var1, int var2) {
      byte[][] var3 = this.data.sendMTFValues_len;
      int[] var4 = this.data.mtfFreq;
      int var5 = this.nMTF;
      int var6 = 0;

      label50:
      for(int var7 = var1; var7 > 0; --var7) {
         int var8 = var5 / var7;
         int var9 = var6 - 1;
         int var10 = 0;

         for(int var11 = var2 - 1; var10 < var8 && var9 < var11; var10 += var4[var9]) {
            ++var9;
         }

         if (var9 > var6 && var7 != var1 && var7 != 1 && (var1 - var7 & 1) != 0) {
            var10 -= var4[var9--];
         }

         byte[] var13 = var3[var7 - 1];
         int var12 = var2;

         while(true) {
            while(true) {
               --var12;
               if (var12 < 0) {
                  var6 = var9 + 1;
                  var5 -= var10;
                  continue label50;
               }

               if (var12 >= var6 && var12 <= var9) {
                  var13[var12] = 0;
               } else {
                  var13[var12] = 15;
               }
            }
         }
      }

   }

   private int sendMTFValues1(int var1, int var2) {
      BZip2CompressorOutputStream.Data var3 = this.data;
      int[][] var4 = var3.sendMTFValues_rfreq;
      int[] var5 = var3.sendMTFValues_fave;
      short[] var6 = var3.sendMTFValues_cost;
      char[] var7 = var3.sfmap;
      byte[] var8 = var3.selector;
      byte[][] var9 = var3.sendMTFValues_len;
      byte[] var10 = var9[0];
      byte[] var11 = var9[1];
      byte[] var12 = var9[2];
      byte[] var13 = var9[3];
      byte[] var14 = var9[4];
      byte[] var15 = var9[5];
      int var16 = this.nMTF;
      int var17 = 0;

      for(int var18 = 0; var18 < 4; ++var18) {
         int var19 = var1;

         while(true) {
            --var19;
            int var21;
            if (var19 < 0) {
               var17 = 0;

               int var29;
               for(var19 = 0; var19 < this.nMTF; var19 = var29 + 1) {
                  var29 = Math.min(var19 + 50 - 1, var16 - 1);
                  int var23;
                  short var24;
                  if (var1 == 6) {
                     short var31 = 0;
                     short var30 = 0;
                     short var33 = 0;
                     var24 = 0;
                     short var25 = 0;
                     short var26 = 0;

                     for(int var27 = var19; var27 <= var29; ++var27) {
                        char var28 = var7[var27];
                        var31 = (short)(var31 + (var10[var28] & 255));
                        var30 = (short)(var30 + (var11[var28] & 255));
                        var33 = (short)(var33 + (var12[var28] & 255));
                        var24 = (short)(var24 + (var13[var28] & 255));
                        var25 = (short)(var25 + (var14[var28] & 255));
                        var26 = (short)(var26 + (var15[var28] & 255));
                     }

                     var6[0] = var31;
                     var6[1] = var30;
                     var6[2] = var33;
                     var6[3] = var24;
                     var6[4] = var25;
                     var6[5] = var26;
                  } else {
                     var21 = var1;

                     while(true) {
                        --var21;
                        if (var21 < 0) {
                           for(var21 = var19; var21 <= var29; ++var21) {
                              char var22 = var7[var21];
                              var23 = var1;

                              while(true) {
                                 --var23;
                                 if (var23 < 0) {
                                    break;
                                 }

                                 var6[var23] = (short)(var6[var23] + (var9[var23][var22] & 255));
                              }
                           }
                           break;
                        }

                        var6[var21] = 0;
                     }
                  }

                  var21 = -1;
                  int var32 = var1;
                  var23 = 999999999;

                  while(true) {
                     --var32;
                     if (var32 < 0) {
                        int var10002 = var5[var21]++;
                        var8[var17] = (byte)var21;
                        ++var17;
                        int[] var34 = var4[var21];

                        for(var23 = var19; var23 <= var29; ++var23) {
                           ++var34[var7[var23]];
                        }
                        break;
                     }

                     var24 = var6[var32];
                     if (var24 < var23) {
                        var23 = var24;
                        var21 = var32;
                     }
                  }
               }

               for(var19 = 0; var19 < var1; ++var19) {
                  hbMakeCodeLengths(var9[var19], var4[var19], this.data, var2, 20);
               }
               break;
            }

            var5[var19] = 0;
            int[] var20 = var4[var19];
            var21 = var2;

            while(true) {
               --var21;
               if (var21 < 0) {
                  break;
               }

               var20[var21] = 0;
            }
         }
      }

      return var17;
   }

   private void sendMTFValues2(int var1, int var2) {
      BZip2CompressorOutputStream.Data var3 = this.data;
      byte[] var4 = var3.sendMTFValues2_pos;
      int var5 = var1;

      while(true) {
         --var5;
         if (var5 < 0) {
            for(var5 = 0; var5 < var2; ++var5) {
               byte var6 = var3.selector[var5];
               byte var7 = var4[0];

               int var8;
               byte var9;
               for(var8 = 0; var6 != var7; var4[var8] = var9) {
                  ++var8;
                  var9 = var7;
                  var7 = var4[var8];
               }

               var4[0] = var7;
               var3.selectorMtf[var5] = (byte)var8;
            }

            return;
         }

         var4[var5] = (byte)var5;
      }
   }

   private void sendMTFValues3(int var1, int var2) {
      int[][] var3 = this.data.sendMTFValues_code;
      byte[][] var4 = this.data.sendMTFValues_len;

      for(int var5 = 0; var5 < var1; ++var5) {
         int var6 = 32;
         int var7 = 0;
         byte[] var8 = var4[var5];
         int var9 = var2;

         while(true) {
            --var9;
            if (var9 < 0) {
               hbAssignCodes(var3[var5], var4[var5], var6, var7, var2);
               break;
            }

            int var10 = var8[var9] & 255;
            if (var10 > var7) {
               var7 = var10;
            }

            if (var10 < var6) {
               var6 = var10;
            }
         }
      }

   }

   private void sendMTFValues4() throws IOException {
      boolean[] var1 = this.data.inUse;
      boolean[] var2 = this.data.sentMTFValues4_inUse16;
      int var3 = 16;

      while(true) {
         --var3;
         int var4;
         int var5;
         if (var3 < 0) {
            for(var3 = 0; var3 < 16; ++var3) {
               this.bsW(1, var2[var3] ? 1 : 0);
            }

            OutputStream var9 = this.out;
            var4 = this.bsLive;
            var5 = this.bsBuff;

            for(int var6 = 0; var6 < 16; ++var6) {
               if (var2[var6]) {
                  int var7 = var6 * 16;

                  for(int var8 = 0; var8 < 16; ++var8) {
                     while(var4 >= 8) {
                        var9.write(var5 >> 24);
                        var5 <<= 8;
                        var4 -= 8;
                     }

                     if (var1[var7 + var8]) {
                        var5 |= 1 << 32 - var4 - 1;
                     }

                     ++var4;
                  }
               }
            }

            this.bsBuff = var5;
            this.bsLive = var4;
            return;
         }

         var2[var3] = false;
         var4 = var3 * 16;
         var5 = 16;

         while(true) {
            --var5;
            if (var5 < 0) {
               break;
            }

            if (var1[var4 + var5]) {
               var2[var3] = true;
            }
         }
      }
   }

   private void sendMTFValues5(int var1, int var2) throws IOException {
      this.bsW(3, var1);
      this.bsW(15, var2);
      OutputStream var3 = this.out;
      byte[] var4 = this.data.selectorMtf;
      int var5 = this.bsLive;
      int var6 = this.bsBuff;

      for(int var7 = 0; var7 < var2; ++var7) {
         int var8 = 0;

         for(int var9 = var4[var7] & 255; var8 < var9; ++var8) {
            while(var5 >= 8) {
               var3.write(var6 >> 24);
               var6 <<= 8;
               var5 -= 8;
            }

            var6 |= 1 << 32 - var5 - 1;
            ++var5;
         }

         while(var5 >= 8) {
            var3.write(var6 >> 24);
            var6 <<= 8;
            var5 -= 8;
         }

         ++var5;
      }

      this.bsBuff = var6;
      this.bsLive = var5;
   }

   private void sendMTFValues6(int var1, int var2) throws IOException {
      byte[][] var3 = this.data.sendMTFValues_len;
      OutputStream var4 = this.out;
      int var5 = this.bsLive;
      int var6 = this.bsBuff;

      for(int var7 = 0; var7 < var1; ++var7) {
         byte[] var8 = var3[var7];

         int var9;
         for(var9 = var8[0] & 255; var5 >= 8; var5 -= 8) {
            var4.write(var6 >> 24);
            var6 <<= 8;
         }

         var6 |= var9 << 32 - var5 - 5;
         var5 += 5;

         for(int var10 = 0; var10 < var2; ++var10) {
            int var11;
            for(var11 = var8[var10] & 255; var9 < var11; ++var9) {
               while(var5 >= 8) {
                  var4.write(var6 >> 24);
                  var6 <<= 8;
                  var5 -= 8;
               }

               var6 |= 2 << 32 - var5 - 2;
               var5 += 2;
            }

            while(var9 > var11) {
               while(var5 >= 8) {
                  var4.write(var6 >> 24);
                  var6 <<= 8;
                  var5 -= 8;
               }

               var6 |= 3 << 32 - var5 - 2;
               var5 += 2;
               --var9;
            }

            while(var5 >= 8) {
               var4.write(var6 >> 24);
               var6 <<= 8;
               var5 -= 8;
            }

            ++var5;
         }
      }

      this.bsBuff = var6;
      this.bsLive = var5;
   }

   private void sendMTFValues7() throws IOException {
      BZip2CompressorOutputStream.Data var1 = this.data;
      byte[][] var2 = var1.sendMTFValues_len;
      int[][] var3 = var1.sendMTFValues_code;
      OutputStream var4 = this.out;
      byte[] var5 = var1.selector;
      char[] var6 = var1.sfmap;
      int var7 = this.nMTF;
      int var8 = 0;
      int var9 = this.bsLive;
      int var10 = this.bsBuff;

      for(int var11 = 0; var11 < var7; ++var8) {
         int var12 = Math.min(var11 + 50 - 1, var7 - 1);
         int var13 = var5[var8] & 255;
         int[] var14 = var3[var13];

         for(byte[] var15 = var2[var13]; var11 <= var12; ++var11) {
            char var16;
            for(var16 = var6[var11]; var9 >= 8; var9 -= 8) {
               var4.write(var10 >> 24);
               var10 <<= 8;
            }

            int var17 = var15[var16] & 255;
            var10 |= var14[var16] << 32 - var9 - var17;
            var9 += var17;
         }

         var11 = var12 + 1;
      }

      this.bsBuff = var10;
      this.bsLive = var9;
   }

   private void moveToFrontCodeAndSend() throws IOException {
      this.bsW(24, this.data.origPtr);
      this.generateMTFValues();
      this.sendMTFValues();
   }

   private void blockSort() {
      this.blockSorter.blockSort(this.data, this.last);
   }

   private void generateMTFValues() {
      int var1 = this.last;
      BZip2CompressorOutputStream.Data var2 = this.data;
      boolean[] var3 = var2.inUse;
      byte[] var4 = var2.block;
      int[] var5 = var2.fmap;
      char[] var6 = var2.sfmap;
      int[] var7 = var2.mtfFreq;
      byte[] var8 = var2.unseqToSeq;
      byte[] var9 = var2.generateMTFValues_yy;
      int var10 = 0;

      int var11;
      for(var11 = 0; var11 < 256; ++var11) {
         if (var3[var11]) {
            var8[var11] = (byte)var10;
            ++var10;
         }
      }

      this.nInUse = var10;
      var11 = var10 + 1;

      int var12;
      for(var12 = var11; var12 >= 0; --var12) {
         var7[var12] = 0;
      }

      var12 = var10;

      while(true) {
         --var12;
         if (var12 < 0) {
            var12 = 0;
            int var13 = 0;

            int var10002;
            for(int var14 = 0; var14 <= var1; ++var14) {
               byte var15 = var8[var4[var5[var14]] & 255];
               byte var16 = var9[0];

               int var17;
               byte var18;
               for(var17 = 0; var15 != var16; var9[var17] = var18) {
                  ++var17;
                  var18 = var16;
                  var16 = var9[var17];
               }

               var9[0] = var16;
               if (var17 == 0) {
                  ++var13;
               } else {
                  if (var13 > 0) {
                     --var13;

                     while(true) {
                        if ((var13 & 1) == 0) {
                           var6[var12] = 0;
                           ++var12;
                           var10002 = var7[0]++;
                        } else {
                           var6[var12] = 1;
                           ++var12;
                           var10002 = var7[1]++;
                        }

                        if (var13 < 2) {
                           var13 = 0;
                           break;
                        }

                        var13 = var13 - 2 >> 1;
                     }
                  }

                  var6[var12] = (char)(var17 + 1);
                  ++var12;
                  ++var7[var17 + 1];
               }
            }

            if (var13 > 0) {
               --var13;

               while(true) {
                  if ((var13 & 1) == 0) {
                     var6[var12] = 0;
                     ++var12;
                     var10002 = var7[0]++;
                  } else {
                     var6[var12] = 1;
                     ++var12;
                     var10002 = var7[1]++;
                  }

                  if (var13 < 2) {
                     break;
                  }

                  var13 = var13 - 2 >> 1;
               }
            }

            var6[var12] = (char)var11;
            var10002 = var7[var11]++;
            this.nMTF = var12 + 1;
            return;
         }

         var9[var12] = (byte)var12;
      }
   }

   static final class Data {
      final boolean[] inUse = new boolean[256];
      final byte[] unseqToSeq = new byte[256];
      final int[] mtfFreq = new int[258];
      final byte[] selector = new byte[18002];
      final byte[] selectorMtf = new byte[18002];
      final byte[] generateMTFValues_yy = new byte[256];
      final byte[][] sendMTFValues_len = new byte[6][258];
      final int[][] sendMTFValues_rfreq = new int[6][258];
      final int[] sendMTFValues_fave = new int[6];
      final short[] sendMTFValues_cost = new short[6];
      final int[][] sendMTFValues_code = new int[6][258];
      final byte[] sendMTFValues2_pos = new byte[6];
      final boolean[] sentMTFValues4_inUse16 = new boolean[16];
      final int[] heap = new int[260];
      final int[] weight = new int[516];
      final int[] parent = new int[516];
      final byte[] block;
      final int[] fmap;
      final char[] sfmap;
      int origPtr;

      Data(int var1) {
         int var2 = var1 * 100000;
         this.block = new byte[var2 + 1 + 20];
         this.fmap = new int[var2];
         this.sfmap = new char[2 * var2];
      }
   }
}
