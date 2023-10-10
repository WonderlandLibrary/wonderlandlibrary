package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.ByteOrder;
import java.util.List;

public class LengthFieldBasedFrameDecoder extends ByteToMessageDecoder {
   private final ByteOrder byteOrder;
   private final int maxFrameLength;
   private final int lengthFieldOffset;
   private final int lengthFieldLength;
   private final int lengthFieldEndOffset;
   private final int lengthAdjustment;
   private final int initialBytesToStrip;
   private final boolean failFast;
   private boolean discardingTooLongFrame;
   private long tooLongFrameLength;
   private long bytesToDiscard;

   public LengthFieldBasedFrameDecoder(int var1, int var2, int var3) {
      this(var1, var2, var3, 0, 0);
   }

   public LengthFieldBasedFrameDecoder(int var1, int var2, int var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, true);
   }

   public LengthFieldBasedFrameDecoder(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this(ByteOrder.BIG_ENDIAN, var1, var2, var3, var4, var5, var6);
   }

   public LengthFieldBasedFrameDecoder(ByteOrder var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      if (var1 == null) {
         throw new NullPointerException("byteOrder");
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + var2);
      } else if (var3 < 0) {
         throw new IllegalArgumentException("lengthFieldOffset must be a non-negative integer: " + var3);
      } else if (var6 < 0) {
         throw new IllegalArgumentException("initialBytesToStrip must be a non-negative integer: " + var6);
      } else if (var3 > var2 - var4) {
         throw new IllegalArgumentException("maxFrameLength (" + var2 + ") " + "must be equal to or greater than " + "lengthFieldOffset (" + var3 + ") + " + "lengthFieldLength (" + var4 + ").");
      } else {
         this.byteOrder = var1;
         this.maxFrameLength = var2;
         this.lengthFieldOffset = var3;
         this.lengthFieldLength = var4;
         this.lengthAdjustment = var5;
         this.lengthFieldEndOffset = var3 + var4;
         this.initialBytesToStrip = var6;
         this.failFast = var7;
      }
   }

   protected final void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      Object var4 = this.decode(var1, var2);
      if (var4 != null) {
         var3.add(var4);
      }

   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      if (this.discardingTooLongFrame) {
         long var3 = this.bytesToDiscard;
         int var5 = (int)Math.min(var3, (long)var2.readableBytes());
         var2.skipBytes(var5);
         var3 -= (long)var5;
         this.bytesToDiscard = var3;
         this.failIfNecessary(false);
      }

      if (var2.readableBytes() < this.lengthFieldEndOffset) {
         return null;
      } else {
         int var10 = var2.readerIndex() + this.lengthFieldOffset;
         long var4 = this.getUnadjustedFrameLength(var2, var10, this.lengthFieldLength, this.byteOrder);
         if (var4 < 0L) {
            var2.skipBytes(this.lengthFieldEndOffset);
            throw new CorruptedFrameException("negative pre-adjustment length field: " + var4);
         } else {
            var4 += (long)(this.lengthAdjustment + this.lengthFieldEndOffset);
            if (var4 < (long)this.lengthFieldEndOffset) {
               var2.skipBytes(this.lengthFieldEndOffset);
               throw new CorruptedFrameException("Adjusted frame length (" + var4 + ") is less " + "than lengthFieldEndOffset: " + this.lengthFieldEndOffset);
            } else if (var4 > (long)this.maxFrameLength) {
               long var11 = var4 - (long)var2.readableBytes();
               this.tooLongFrameLength = var4;
               if (var11 < 0L) {
                  var2.skipBytes((int)var4);
               } else {
                  this.discardingTooLongFrame = true;
                  this.bytesToDiscard = var11;
                  var2.skipBytes(var2.readableBytes());
               }

               this.failIfNecessary(true);
               return null;
            } else {
               int var6 = (int)var4;
               if (var2.readableBytes() < var6) {
                  return null;
               } else if (this.initialBytesToStrip > var6) {
                  var2.skipBytes(var6);
                  throw new CorruptedFrameException("Adjusted frame length (" + var4 + ") is less " + "than initialBytesToStrip: " + this.initialBytesToStrip);
               } else {
                  var2.skipBytes(this.initialBytesToStrip);
                  int var7 = var2.readerIndex();
                  int var8 = var6 - this.initialBytesToStrip;
                  ByteBuf var9 = this.extractFrame(var1, var2, var7, var8);
                  var2.readerIndex(var7 + var8);
                  return var9;
               }
            }
         }
      }
   }

   protected long getUnadjustedFrameLength(ByteBuf var1, int var2, int var3, ByteOrder var4) {
      var1 = var1.order(var4);
      long var5;
      switch(var3) {
      case 1:
         var5 = (long)var1.getUnsignedByte(var2);
         break;
      case 2:
         var5 = (long)var1.getUnsignedShort(var2);
         break;
      case 3:
         var5 = (long)var1.getUnsignedMedium(var2);
         break;
      case 4:
         var5 = var1.getUnsignedInt(var2);
         break;
      case 5:
      case 6:
      case 7:
      default:
         throw new DecoderException("unsupported lengthFieldLength: " + this.lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
      case 8:
         var5 = var1.getLong(var2);
      }

      return var5;
   }

   private void failIfNecessary(boolean var1) {
      if (this.bytesToDiscard == 0L) {
         long var2 = this.tooLongFrameLength;
         this.tooLongFrameLength = 0L;
         this.discardingTooLongFrame = false;
         if (!this.failFast || this.failFast && var1) {
            this.fail(var2);
         }
      } else if (this.failFast && var1) {
         this.fail(this.tooLongFrameLength);
      }

   }

   protected ByteBuf extractFrame(ChannelHandlerContext var1, ByteBuf var2, int var3, int var4) {
      ByteBuf var5 = var1.alloc().buffer(var4);
      var5.writeBytes(var2, var3, var4);
      return var5;
   }

   private void fail(long var1) {
      if (var1 > 0L) {
         throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + ": " + var1 + " - discarded");
      } else {
         throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + " - discarding");
      }
   }
}
