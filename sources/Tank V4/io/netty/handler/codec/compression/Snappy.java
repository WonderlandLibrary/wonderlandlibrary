package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class Snappy {
   private static final int MAX_HT_SIZE = 16384;
   private static final int MIN_COMPRESSIBLE_BYTES = 15;
   private static final int PREAMBLE_NOT_FULL = -1;
   private static final int NOT_ENOUGH_INPUT = -1;
   private static final int LITERAL = 0;
   private static final int COPY_1_BYTE_OFFSET = 1;
   private static final int COPY_2_BYTE_OFFSET = 2;
   private static final int COPY_4_BYTE_OFFSET = 3;
   private Snappy.State state;
   private byte tag;
   private int written;

   public Snappy() {
      this.state = Snappy.State.READY;
   }

   public void reset() {
      this.state = Snappy.State.READY;
      this.tag = 0;
      this.written = 0;
   }

   public void encode(ByteBuf var1, ByteBuf var2, int var3) {
      int var4 = 0;

      while(true) {
         int var5 = var3 >>> var4 * 7;
         if ((var5 & -128) == 0) {
            var2.writeByte(var5);
            var4 = var1.readerIndex();
            var5 = var1.readerIndex();
            int var6 = var3;
            short[] var7 = getHashTable(var3);
            int var8 = 32 - (int)Math.floor(Math.log((double)var7.length) / Math.log(2.0D));
            int var9 = var4;
            if (var3 - var4 >= 15) {
               ++var4;
               int var10 = hash(var1, var4, var8);

               label38:
               while(true) {
                  int var11 = 32;
                  int var13 = var4;

                  int var12;
                  int var14;
                  int var15;
                  do {
                     var4 = var13;
                     var14 = var10;
                     var15 = var11++ >> 5;
                     var13 += var15;
                     if (var13 > var6 - 4) {
                        break label38;
                     }

                     var10 = hash(var1, var13, var8);
                     var12 = var5 + var7[var14];
                     var7[var14] = (short)(var4 - var5);
                  } while(var1.getInt(var4) != var1.getInt(var12));

                  encodeLiteral(var1, var2, var4 - var9);

                  do {
                     var15 = var4;
                     int var16 = 4 + findMatchingLength(var1, var12 + 4, var4 + 4, var6);
                     var4 += var16;
                     int var17 = var15 - var12;
                     encodeCopy(var2, var17, var16);
                     var1.readerIndex(var1.readerIndex() + var16);
                     var14 = var4 - 1;
                     var9 = var4;
                     if (var4 >= var6 - 4) {
                        break label38;
                     }

                     int var18 = hash(var1, var14, var8);
                     var7[var18] = (short)(var4 - var5 - 1);
                     int var19 = hash(var1, var14 + 1, var8);
                     var12 = var5 + var7[var19];
                     var7[var19] = (short)(var4 - var5);
                  } while(var1.getInt(var14 + 1) == var1.getInt(var12));

                  var10 = hash(var1, var14 + 2, var8);
                  ++var4;
               }
            }

            if (var9 < var6) {
               encodeLiteral(var1, var2, var6 - var9);
            }

            return;
         }

         var2.writeByte(var5 & 127 | 128);
         ++var4;
      }
   }

   private static int hash(ByteBuf var0, int var1, int var2) {
      return var0.getInt(var1) + 506832829 >>> var2;
   }

   private static short[] getHashTable(int var0) {
      int var1;
      for(var1 = 256; var1 < 16384 && var1 < var0; var1 <<= 1) {
      }

      short[] var2;
      if (var1 <= 256) {
         var2 = new short[256];
      } else {
         var2 = new short[16384];
      }

      return var2;
   }

   private static int findMatchingLength(ByteBuf var0, int var1, int var2, int var3) {
      int var4;
      for(var4 = 0; var2 <= var3 - 4 && var0.getInt(var2) == var0.getInt(var1 + var4); var4 += 4) {
         var2 += 4;
      }

      while(var2 < var3 && var0.getByte(var1 + var4) == var0.getByte(var2)) {
         ++var2;
         ++var4;
      }

      return var4;
   }

   private static int bitsToEncode(int var0) {
      int var1 = Integer.highestOneBit(var0);

      int var2;
      for(var2 = 0; (var1 >>= 1) != 0; ++var2) {
      }

      return var2;
   }

   private static void encodeLiteral(ByteBuf var0, ByteBuf var1, int var2) {
      if (var2 < 61) {
         var1.writeByte(var2 - 1 << 2);
      } else {
         int var3 = bitsToEncode(var2 - 1);
         int var4 = 1 + var3 / 8;
         var1.writeByte(59 + var4 << 2);

         for(int var5 = 0; var5 < var4; ++var5) {
            var1.writeByte(var2 - 1 >> var5 * 8 & 255);
         }
      }

      var1.writeBytes(var0, var2);
   }

   private static void encodeCopyWithOffset(ByteBuf var0, int var1, int var2) {
      if (var2 < 12 && var1 < 2048) {
         var0.writeByte(1 | var2 - 4 << 2 | var1 >> 8 << 5);
         var0.writeByte(var1 & 255);
      } else {
         var0.writeByte(2 | var2 - 1 << 2);
         var0.writeByte(var1 & 255);
         var0.writeByte(var1 >> 8 & 255);
      }

   }

   private static void encodeCopy(ByteBuf var0, int var1, int var2) {
      while(var2 >= 68) {
         encodeCopyWithOffset(var0, var1, 64);
         var2 -= 64;
      }

      if (var2 > 64) {
         encodeCopyWithOffset(var0, var1, 60);
         var2 -= 60;
      }

      encodeCopyWithOffset(var0, var1, var2);
   }

   public void decode(ByteBuf var1, ByteBuf var2) {
      while(var1.isReadable()) {
         switch(this.state) {
         case READY:
            this.state = Snappy.State.READING_PREAMBLE;
         case READING_PREAMBLE:
            int var3 = readPreamble(var1);
            if (var3 == -1) {
               return;
            }

            if (var3 == 0) {
               this.state = Snappy.State.READY;
               return;
            }

            var2.ensureWritable(var3);
            this.state = Snappy.State.READING_TAG;
         case READING_TAG:
            if (!var1.isReadable()) {
               return;
            }

            this.tag = var1.readByte();
            switch(this.tag & 3) {
            case 0:
               this.state = Snappy.State.READING_LITERAL;
               continue;
            case 1:
            case 2:
            case 3:
               this.state = Snappy.State.READING_COPY;
            default:
               continue;
            }
         case READING_LITERAL:
            int var4 = decodeLiteral(this.tag, var1, var2);
            if (var4 != -1) {
               this.state = Snappy.State.READING_TAG;
               this.written += var4;
               break;
            }

            return;
         case READING_COPY:
            int var5;
            switch(this.tag & 3) {
            case 1:
               var5 = decodeCopyWith1ByteOffset(this.tag, var1, var2, this.written);
               if (var5 != -1) {
                  this.state = Snappy.State.READING_TAG;
                  this.written += var5;
                  break;
               }

               return;
            case 2:
               var5 = decodeCopyWith2ByteOffset(this.tag, var1, var2, this.written);
               if (var5 != -1) {
                  this.state = Snappy.State.READING_TAG;
                  this.written += var5;
                  break;
               }

               return;
            case 3:
               var5 = decodeCopyWith4ByteOffset(this.tag, var1, var2, this.written);
               if (var5 == -1) {
                  return;
               }

               this.state = Snappy.State.READING_TAG;
               this.written += var5;
            }
         }
      }

   }

   private static int readPreamble(ByteBuf var0) {
      int var1 = 0;
      int var2 = 0;

      do {
         if (!var0.isReadable()) {
            return 0;
         }

         short var3 = var0.readUnsignedByte();
         var1 |= (var3 & 127) << var2++ * 7;
         if ((var3 & 128) == 0) {
            return var1;
         }
      } while(var2 < 4);

      throw new DecompressionException("Preamble is greater than 4 bytes");
   }

   private static int decodeLiteral(byte var0, ByteBuf var1, ByteBuf var2) {
      var1.markReaderIndex();
      int var3;
      switch(var0 >> 2 & 63) {
      case 60:
         if (!var1.isReadable()) {
            return -1;
         }

         var3 = var1.readUnsignedByte();
         break;
      case 61:
         if (var1.readableBytes() < 2) {
            return -1;
         }

         var3 = ByteBufUtil.swapShort(var1.readShort());
         break;
      case 62:
         if (var1.readableBytes() < 3) {
            return -1;
         }

         var3 = ByteBufUtil.swapMedium(var1.readUnsignedMedium());
         break;
      case 63:
      default:
         var3 = var0 >> 2 & 63;
         break;
      case 64:
         if (var1.readableBytes() < 4) {
            return -1;
         }

         var3 = ByteBufUtil.swapInt(var1.readInt());
      }

      ++var3;
      if (var1.readableBytes() < var3) {
         var1.resetReaderIndex();
         return -1;
      } else {
         var2.writeBytes(var1, var3);
         return var3;
      }
   }

   private static int decodeCopyWith1ByteOffset(byte var0, ByteBuf var1, ByteBuf var2, int var3) {
      if (!var1.isReadable()) {
         return -1;
      } else {
         int var4 = var2.writerIndex();
         int var5 = 4 + ((var0 & 28) >> 2);
         int var6 = (var0 & 224) << 8 >> 5 | var1.readUnsignedByte();
         validateOffset(var6, var3);
         var2.markReaderIndex();
         if (var6 < var5) {
            for(int var7 = var5 / var6; var7 > 0; --var7) {
               var2.readerIndex(var4 - var6);
               var2.readBytes(var2, var6);
            }

            if (var5 % var6 != 0) {
               var2.readerIndex(var4 - var6);
               var2.readBytes(var2, var5 % var6);
            }
         } else {
            var2.readerIndex(var4 - var6);
            var2.readBytes(var2, var5);
         }

         var2.resetReaderIndex();
         return var5;
      }
   }

   private static int decodeCopyWith2ByteOffset(byte var0, ByteBuf var1, ByteBuf var2, int var3) {
      if (var1.readableBytes() < 2) {
         return -1;
      } else {
         int var4 = var2.writerIndex();
         int var5 = 1 + (var0 >> 2 & 63);
         short var6 = ByteBufUtil.swapShort(var1.readShort());
         validateOffset(var6, var3);
         var2.markReaderIndex();
         if (var6 < var5) {
            for(int var7 = var5 / var6; var7 > 0; --var7) {
               var2.readerIndex(var4 - var6);
               var2.readBytes((ByteBuf)var2, var6);
            }

            if (var5 % var6 != 0) {
               var2.readerIndex(var4 - var6);
               var2.readBytes(var2, var5 % var6);
            }
         } else {
            var2.readerIndex(var4 - var6);
            var2.readBytes(var2, var5);
         }

         var2.resetReaderIndex();
         return var5;
      }
   }

   private static int decodeCopyWith4ByteOffset(byte var0, ByteBuf var1, ByteBuf var2, int var3) {
      if (var1.readableBytes() < 4) {
         return -1;
      } else {
         int var4 = var2.writerIndex();
         int var5 = 1 + (var0 >> 2 & 63);
         int var6 = ByteBufUtil.swapInt(var1.readInt());
         validateOffset(var6, var3);
         var2.markReaderIndex();
         if (var6 < var5) {
            for(int var7 = var5 / var6; var7 > 0; --var7) {
               var2.readerIndex(var4 - var6);
               var2.readBytes(var2, var6);
            }

            if (var5 % var6 != 0) {
               var2.readerIndex(var4 - var6);
               var2.readBytes(var2, var5 % var6);
            }
         } else {
            var2.readerIndex(var4 - var6);
            var2.readBytes(var2, var5);
         }

         var2.resetReaderIndex();
         return var5;
      }
   }

   private static void validateOffset(int var0, int var1) {
      if (var0 > 32767) {
         throw new DecompressionException("Offset exceeds maximum permissible value");
      } else if (var0 <= 0) {
         throw new DecompressionException("Offset is less than minimum permissible value");
      } else if (var0 > var1) {
         throw new DecompressionException("Offset exceeds size of chunk");
      }
   }

   public static int calculateChecksum(ByteBuf var0) {
      return calculateChecksum(var0, var0.readerIndex(), var0.readableBytes());
   }

   public static int calculateChecksum(ByteBuf var0, int var1, int var2) {
      Crc32c var3 = new Crc32c();
      if (var0.hasArray()) {
         var3.update(var0.array(), var0.arrayOffset() + var1, var2);
      } else {
         byte[] var4 = new byte[var2];
         var0.getBytes(var1, var4);
         var3.update(var4, 0, var2);
      }

      int var6 = maskChecksum((int)var3.getValue());
      var3.reset();
      return var6;
   }

   static void validateChecksum(int var0, ByteBuf var1) {
      validateChecksum(var0, var1, var1.readerIndex(), var1.readableBytes());
   }

   static void validateChecksum(int var0, ByteBuf var1, int var2, int var3) {
      int var4 = calculateChecksum(var1, var2, var3);
      if (var4 != var0) {
         throw new DecompressionException("mismatching checksum: " + Integer.toHexString(var4) + " (expected: " + Integer.toHexString(var0) + ')');
      }
   }

   static int maskChecksum(int var0) {
      return (var0 >> 15 | var0 << 17) + -1568478504;
   }

   private static enum State {
      READY,
      READING_PREAMBLE,
      READING_TAG,
      READING_LITERAL,
      READING_COPY;

      private static final Snappy.State[] $VALUES = new Snappy.State[]{READY, READING_PREAMBLE, READING_TAG, READING_LITERAL, READING_COPY};
   }
}
