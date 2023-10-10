package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class SpdyFrameDecoder extends ByteToMessageDecoder {
   private static final SpdyProtocolException INVALID_FRAME = new SpdyProtocolException("Received invalid frame");
   private final int spdyVersion;
   private final int maxChunkSize;
   private final SpdyHeaderBlockDecoder headerBlockDecoder;
   private SpdyFrameDecoder.State state;
   private SpdySettingsFrame spdySettingsFrame;
   private SpdyHeadersFrame spdyHeadersFrame;
   private byte flags;
   private int length;
   private int version;
   private int type;
   private int streamId;

   public SpdyFrameDecoder(SpdyVersion var1) {
      this(var1, 8192, 16384);
   }

   public SpdyFrameDecoder(SpdyVersion var1, int var2, int var3) {
      this(var1, var2, SpdyHeaderBlockDecoder.newInstance(var1, var3));
   }

   protected SpdyFrameDecoder(SpdyVersion var1, int var2, SpdyHeaderBlockDecoder var3) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + var2);
      } else {
         this.spdyVersion = var1.getVersion();
         this.maxChunkSize = var2;
         this.headerBlockDecoder = var3;
         this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
      }
   }

   public void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      this.decode(var1, var2, var3);
      this.headerBlockDecoder.end();
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      int var5;
      int var7;
      int var8;
      switch(this.state) {
      case READ_COMMON_HEADER:
         this.state = this.readCommonHeader(var2);
         if (this.state == SpdyFrameDecoder.State.FRAME_ERROR) {
            if (this.version != this.spdyVersion) {
               fireProtocolException(var1, "Unsupported version: " + this.version);
            } else {
               fireInvalidFrameException(var1);
            }
         }

         if (this.length == 0) {
            if (this.state == SpdyFrameDecoder.State.READ_DATA_FRAME) {
               DefaultSpdyDataFrame var16 = new DefaultSpdyDataFrame(this.streamId);
               var16.setLast((this.flags & 1) != 0);
               this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
               var3.add(var16);
               return;
            }

            this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
         }

         return;
      case READ_CONTROL_FRAME:
         try {
            Object var15 = this.readControlFrame(var2);
            if (var15 != null) {
               this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
               var3.add(var15);
            }

            return;
         } catch (IllegalArgumentException var14) {
            this.state = SpdyFrameDecoder.State.FRAME_ERROR;
            fireInvalidFrameException(var1);
            return;
         }
      case READ_SETTINGS_FRAME:
         int var4;
         if (this.spdySettingsFrame == null) {
            if (var2.readableBytes() < 4) {
               return;
            }

            var4 = SpdyCodecUtil.getUnsignedInt(var2, var2.readerIndex());
            var2.skipBytes(4);
            this.length -= 4;
            if ((this.length & 7) != 0 || this.length >> 3 != var4) {
               this.state = SpdyFrameDecoder.State.FRAME_ERROR;
               fireInvalidFrameException(var1);
               return;
            }

            this.spdySettingsFrame = new DefaultSpdySettingsFrame();
            boolean var18 = (this.flags & 1) != 0;
            this.spdySettingsFrame.setClearPreviouslyPersistedSettings(var18);
         }

         var4 = Math.min(var2.readableBytes() >> 3, this.length >> 3);

         for(var5 = 0; var5 < var4; ++var5) {
            byte var19 = var2.getByte(var2.readerIndex());
            var7 = SpdyCodecUtil.getUnsignedMedium(var2, var2.readerIndex() + 1);
            var8 = SpdyCodecUtil.getSignedInt(var2, var2.readerIndex() + 4);
            var2.skipBytes(8);
            if (!this.spdySettingsFrame.isSet(var7)) {
               boolean var22 = (var19 & 1) != 0;
               boolean var23 = (var19 & 2) != 0;
               this.spdySettingsFrame.setValue(var7, var8, var22, var23);
            }
         }

         this.length -= 8 * var4;
         if (this.length == 0) {
            this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
            SpdySettingsFrame var20 = this.spdySettingsFrame;
            this.spdySettingsFrame = null;
            var3.add(var20);
            return;
         }

         return;
      case READ_HEADER_BLOCK_FRAME:
         try {
            this.spdyHeadersFrame = this.readHeaderBlockFrame(var2);
            if (this.spdyHeadersFrame != null) {
               if (this.length == 0) {
                  this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
                  SpdyHeadersFrame var17 = this.spdyHeadersFrame;
                  this.spdyHeadersFrame = null;
                  var3.add(var17);
                  return;
               }

               this.state = SpdyFrameDecoder.State.READ_HEADER_BLOCK;
            }

            return;
         } catch (IllegalArgumentException var13) {
            this.state = SpdyFrameDecoder.State.FRAME_ERROR;
            fireInvalidFrameException(var1);
            return;
         }
      case READ_HEADER_BLOCK:
         var5 = Math.min(var2.readableBytes(), this.length);
         ByteBuf var6 = var2.slice(var2.readerIndex(), var5);

         try {
            this.headerBlockDecoder.decode(var6, this.spdyHeadersFrame);
         } catch (Exception var12) {
            this.state = SpdyFrameDecoder.State.FRAME_ERROR;
            this.spdyHeadersFrame = null;
            var1.fireExceptionCaught(var12);
            return;
         }

         var7 = var5 - var6.readableBytes();
         var2.skipBytes(var7);
         this.length -= var7;
         SpdyHeadersFrame var21;
         if (this.spdyHeadersFrame == null || !this.spdyHeadersFrame.isInvalid() && !this.spdyHeadersFrame.isTruncated()) {
            if (this.length == 0) {
               var21 = this.spdyHeadersFrame;
               this.spdyHeadersFrame = null;
               this.headerBlockDecoder.reset();
               this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
               if (var21 != null) {
                  var3.add(var21);
               }
            }

            return;
         }

         var21 = this.spdyHeadersFrame;
         this.spdyHeadersFrame = null;
         if (this.length == 0) {
            this.headerBlockDecoder.reset();
            this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
         }

         var3.add(var21);
         return;
      case READ_DATA_FRAME:
         if (this.streamId == 0) {
            this.state = SpdyFrameDecoder.State.FRAME_ERROR;
            fireProtocolException(var1, "Received invalid data frame");
            return;
         } else {
            var8 = Math.min(this.maxChunkSize, this.length);
            if (var2.readableBytes() < var8) {
               return;
            }

            ByteBuf var9 = var1.alloc().buffer(var8);
            var9.writeBytes(var2, var8);
            DefaultSpdyDataFrame var10 = new DefaultSpdyDataFrame(this.streamId, var9);
            this.length -= var8;
            if (this.length == 0) {
               var10.setLast((this.flags & 1) != 0);
               this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
            }

            var3.add(var10);
            return;
         }
      case DISCARD_FRAME:
         int var11 = Math.min(var2.readableBytes(), this.length);
         var2.skipBytes(var11);
         this.length -= var11;
         if (this.length == 0) {
            this.state = SpdyFrameDecoder.State.READ_COMMON_HEADER;
         }

         return;
      case FRAME_ERROR:
         var2.skipBytes(var2.readableBytes());
         return;
      default:
         throw new Error("Shouldn't reach here.");
      }
   }

   private SpdyFrameDecoder.State readCommonHeader(ByteBuf var1) {
      if (var1.readableBytes() < 8) {
         return SpdyFrameDecoder.State.READ_COMMON_HEADER;
      } else {
         int var2 = var1.readerIndex();
         int var3 = var2 + 4;
         int var4 = var2 + 5;
         var1.skipBytes(8);
         boolean var5 = (var1.getByte(var2) & 128) != 0;
         this.flags = var1.getByte(var3);
         this.length = SpdyCodecUtil.getUnsignedMedium(var1, var4);
         if (var5) {
            this.version = SpdyCodecUtil.getUnsignedShort(var1, var2) & 32767;
            int var6 = var2 + 2;
            this.type = SpdyCodecUtil.getUnsignedShort(var1, var6);
            this.streamId = 0;
         } else {
            this.version = this.spdyVersion;
            this.type = 0;
            this.streamId = SpdyCodecUtil.getUnsignedInt(var1, var2);
         }

         if (this.version == this.spdyVersion && this == false) {
            SpdyFrameDecoder.State var7;
            if (this.willGenerateFrame()) {
               switch(this.type) {
               case 0:
                  var7 = SpdyFrameDecoder.State.READ_DATA_FRAME;
                  break;
               case 1:
               case 2:
               case 8:
                  var7 = SpdyFrameDecoder.State.READ_HEADER_BLOCK_FRAME;
                  break;
               case 3:
               case 5:
               case 6:
               case 7:
               default:
                  var7 = SpdyFrameDecoder.State.READ_CONTROL_FRAME;
                  break;
               case 4:
                  var7 = SpdyFrameDecoder.State.READ_SETTINGS_FRAME;
               }
            } else if (this.length != 0) {
               var7 = SpdyFrameDecoder.State.DISCARD_FRAME;
            } else {
               var7 = SpdyFrameDecoder.State.READ_COMMON_HEADER;
            }

            return var7;
         } else {
            return SpdyFrameDecoder.State.FRAME_ERROR;
         }
      }
   }

   private Object readControlFrame(ByteBuf var1) {
      int var2;
      int var3;
      switch(this.type) {
      case 3:
         if (var1.readableBytes() < 8) {
            return null;
         }

         var2 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex());
         var3 = SpdyCodecUtil.getSignedInt(var1, var1.readerIndex() + 4);
         var1.skipBytes(8);
         return new DefaultSpdyRstStreamFrame(var2, var3);
      case 4:
      case 5:
      case 8:
      default:
         throw new Error("Shouldn't reach here.");
      case 6:
         if (var1.readableBytes() < 4) {
            return null;
         }

         int var4 = SpdyCodecUtil.getSignedInt(var1, var1.readerIndex());
         var1.skipBytes(4);
         return new DefaultSpdyPingFrame(var4);
      case 7:
         if (var1.readableBytes() < 8) {
            return null;
         }

         int var5 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex());
         var3 = SpdyCodecUtil.getSignedInt(var1, var1.readerIndex() + 4);
         var1.skipBytes(8);
         return new DefaultSpdyGoAwayFrame(var5, var3);
      case 9:
         if (var1.readableBytes() < 8) {
            return null;
         } else {
            var2 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex());
            int var6 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex() + 4);
            var1.skipBytes(8);
            return new DefaultSpdyWindowUpdateFrame(var2, var6);
         }
      }
   }

   private SpdyHeadersFrame readHeaderBlockFrame(ByteBuf var1) {
      int var2;
      switch(this.type) {
      case 1:
         if (var1.readableBytes() < 10) {
            return null;
         }

         int var3 = var1.readerIndex();
         var2 = SpdyCodecUtil.getUnsignedInt(var1, var3);
         int var4 = SpdyCodecUtil.getUnsignedInt(var1, var3 + 4);
         byte var5 = (byte)(var1.getByte(var3 + 8) >> 5 & 7);
         var1.skipBytes(10);
         this.length -= 10;
         DefaultSpdySynStreamFrame var6 = new DefaultSpdySynStreamFrame(var2, var4, var5);
         var6.setLast((this.flags & 1) != 0);
         var6.setUnidirectional((this.flags & 2) != 0);
         return var6;
      case 2:
         if (var1.readableBytes() < 4) {
            return null;
         }

         var2 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex());
         var1.skipBytes(4);
         this.length -= 4;
         DefaultSpdySynReplyFrame var7 = new DefaultSpdySynReplyFrame(var2);
         var7.setLast((this.flags & 1) != 0);
         return var7;
      case 8:
         if (var1.readableBytes() < 4) {
            return null;
         }

         var2 = SpdyCodecUtil.getUnsignedInt(var1, var1.readerIndex());
         var1.skipBytes(4);
         this.length -= 4;
         DefaultSpdyHeadersFrame var8 = new DefaultSpdyHeadersFrame(var2);
         var8.setLast((this.flags & 1) != 0);
         return var8;
      default:
         throw new Error("Shouldn't reach here.");
      }
   }

   private boolean willGenerateFrame() {
      switch(this.type) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 6:
      case 7:
      case 8:
      case 9:
         return true;
      case 5:
      default:
         return false;
      }
   }

   private static void fireInvalidFrameException(ChannelHandlerContext var0) {
      var0.fireExceptionCaught(INVALID_FRAME);
   }

   private static void fireProtocolException(ChannelHandlerContext var0, String var1) {
      var0.fireExceptionCaught(new SpdyProtocolException(var1));
   }

   private static enum State {
      READ_COMMON_HEADER,
      READ_CONTROL_FRAME,
      READ_SETTINGS_FRAME,
      READ_HEADER_BLOCK_FRAME,
      READ_HEADER_BLOCK,
      READ_DATA_FRAME,
      DISCARD_FRAME,
      FRAME_ERROR;

      private static final SpdyFrameDecoder.State[] $VALUES = new SpdyFrameDecoder.State[]{READ_COMMON_HEADER, READ_CONTROL_FRAME, READ_SETTINGS_FRAME, READ_HEADER_BLOCK_FRAME, READ_HEADER_BLOCK, READ_DATA_FRAME, DISCARD_FRAME, FRAME_ERROR};
   }
}
