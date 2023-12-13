package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.Arrays;
import java.util.List;

public class SnappyFramedDecoder extends ByteToMessageDecoder {
   private static final byte[] SNAPPY = new byte[]{115, 78, 97, 80, 112, 89};
   private final Snappy snappy;
   private final boolean validateChecksums;
   private boolean started;
   private boolean corrupted;

   public SnappyFramedDecoder() {
      this(false);
   }

   public SnappyFramedDecoder(boolean var1) {
      this.snappy = new Snappy();
      this.validateChecksums = var1;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (this.corrupted) {
         var2.skipBytes(var2.readableBytes());
      } else {
         try {
            int var4 = var2.readerIndex();
            int var5 = var2.writerIndex() - var4;
            if (var5 >= 4) {
               short var6 = var2.getUnsignedByte(var4);
               SnappyFramedDecoder.ChunkType var7 = mapChunkType((byte)var6);
               int var8 = ByteBufUtil.swapMedium(var2.getUnsignedMedium(var4 + 1));
               int var10;
               switch(var7) {
               case STREAM_IDENTIFIER:
                  if (var8 != SNAPPY.length) {
                     throw new DecompressionException("Unexpected length of stream identifier: " + var8);
                  }

                  if (var5 >= 4 + SNAPPY.length) {
                     byte[] var9 = new byte[var8];
                     var2.skipBytes(4).readBytes(var9);
                     if (!Arrays.equals(var9, SNAPPY)) {
                        throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
                     }

                     this.started = true;
                  }
                  break;
               case RESERVED_SKIPPABLE:
                  if (!this.started) {
                     throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                  }

                  if (var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4 + var8);
                  break;
               case RESERVED_UNSKIPPABLE:
                  throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(var6));
               case UNCOMPRESSED_DATA:
                  if (!this.started) {
                     throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                  }

                  if (var8 > 65540) {
                     throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                  }

                  if (var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4);
                  if (this.validateChecksums) {
                     var10 = ByteBufUtil.swapInt(var2.readInt());
                     Snappy.validateChecksum(var10, var2, var2.readerIndex(), var8 - 4);
                  } else {
                     var2.skipBytes(4);
                  }

                  var3.add(var2.readSlice(var8 - 4).retain());
                  break;
               case COMPRESSED_DATA:
                  if (!this.started) {
                     throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                  }

                  if (var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4);
                  var10 = ByteBufUtil.swapInt(var2.readInt());
                  ByteBuf var11 = var1.alloc().buffer(0);
                  if (this.validateChecksums) {
                     int var12 = var2.writerIndex();
                     var2.writerIndex(var2.readerIndex() + var8 - 4);
                     this.snappy.decode(var2, var11);
                     var2.writerIndex(var12);
                     Snappy.validateChecksum(var10, var11, 0, var11.writerIndex());
                  } else {
                     this.snappy.decode(var2.readSlice(var8 - 4), var11);
                  }

                  var3.add(var11);
                  this.snappy.reset();
               }

            }
         } catch (Exception var14) {
            this.corrupted = true;
            throw var14;
         }
      }
   }

   static SnappyFramedDecoder.ChunkType mapChunkType(byte var0) {
      if (var0 == 0) {
         return SnappyFramedDecoder.ChunkType.COMPRESSED_DATA;
      } else if (var0 == 1) {
         return SnappyFramedDecoder.ChunkType.UNCOMPRESSED_DATA;
      } else if (var0 == -128) {
         return SnappyFramedDecoder.ChunkType.STREAM_IDENTIFIER;
      } else {
         return (var0 & 128) == 128 ? SnappyFramedDecoder.ChunkType.RESERVED_SKIPPABLE : SnappyFramedDecoder.ChunkType.RESERVED_UNSKIPPABLE;
      }
   }

   static enum ChunkType {
      STREAM_IDENTIFIER,
      COMPRESSED_DATA,
      UNCOMPRESSED_DATA,
      RESERVED_UNSKIPPABLE,
      RESERVED_SKIPPABLE;

      private static final SnappyFramedDecoder.ChunkType[] $VALUES = new SnappyFramedDecoder.ChunkType[]{STREAM_IDENTIFIER, COMPRESSED_DATA, UNCOMPRESSED_DATA, RESERVED_UNSKIPPABLE, RESERVED_SKIPPABLE};
   }
}
