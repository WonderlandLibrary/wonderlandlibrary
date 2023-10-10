package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.concurrent.Future;
import java.util.Iterator;
import java.util.Set;

public class SpdyFrameEncoder extends MessageToByteEncoder {
   private final int version;
   private final SpdyHeaderBlockEncoder headerBlockEncoder;

   public SpdyFrameEncoder(SpdyVersion var1) {
      this(var1, 6, 15, 8);
   }

   public SpdyFrameEncoder(SpdyVersion var1, int var2, int var3, int var4) {
      this(var1, SpdyHeaderBlockEncoder.newInstance(var1, var2, var3, var4));
   }

   protected SpdyFrameEncoder(SpdyVersion var1, SpdyHeaderBlockEncoder var2) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.version = var1.getVersion();
         this.headerBlockEncoder = var2;
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      var1.channel().closeFuture().addListener(new ChannelFutureListener(this) {
         final SpdyFrameEncoder this$0;

         {
            this.this$0 = var1;
         }

         public void operationComplete(ChannelFuture var1) throws Exception {
            SpdyFrameEncoder.access$000(this.this$0).end();
         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
   }

   protected void encode(ChannelHandlerContext var1, SpdyFrame var2, ByteBuf var3) throws Exception {
      ByteBuf var5;
      int var6;
      if (var2 instanceof SpdyDataFrame) {
         SpdyDataFrame var4 = (SpdyDataFrame)var2;
         var5 = var4.content();
         var6 = var4.isLast() ? 1 : 0;
         var3.ensureWritable(8 + var5.readableBytes());
         var3.writeInt(var4.getStreamId() & Integer.MAX_VALUE);
         var3.writeByte(var6);
         var3.writeMedium(var5.readableBytes());
         var3.writeBytes(var5, var5.readerIndex(), var5.readableBytes());
      } else {
         int var7;
         int var8;
         if (var2 instanceof SpdySynStreamFrame) {
            SpdySynStreamFrame var13 = (SpdySynStreamFrame)var2;
            var5 = this.headerBlockEncoder.encode(var13);
            var6 = var13.isLast() ? 1 : 0;
            if (var13.isUnidirectional()) {
               var6 = (byte)(var6 | 2);
            }

            var7 = var5.readableBytes();
            var8 = 10 + var7;
            var3.ensureWritable(8 + var8);
            var3.writeShort(this.version | '耀');
            var3.writeShort(1);
            var3.writeByte(var6);
            var3.writeMedium(var8);
            var3.writeInt(var13.getStreamId());
            var3.writeInt(var13.getAssociatedToStreamId());
            var3.writeShort((var13.getPriority() & 255) << 13);
            var3.writeBytes(var5, var5.readerIndex(), var7);
            var5.release();
         } else if (var2 instanceof SpdySynReplyFrame) {
            SpdySynReplyFrame var14 = (SpdySynReplyFrame)var2;
            var5 = this.headerBlockEncoder.encode(var14);
            var6 = var14.isLast() ? 1 : 0;
            var7 = var5.readableBytes();
            var8 = 4 + var7;
            var3.ensureWritable(8 + var8);
            var3.writeShort(this.version | '耀');
            var3.writeShort(2);
            var3.writeByte(var6);
            var3.writeMedium(var8);
            var3.writeInt(var14.getStreamId());
            var3.writeBytes(var5, var5.readerIndex(), var7);
            var5.release();
         } else if (var2 instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame var15 = (SpdyRstStreamFrame)var2;
            var3.ensureWritable(16);
            var3.writeShort(this.version | '耀');
            var3.writeShort(3);
            var3.writeInt(8);
            var3.writeInt(var15.getStreamId());
            var3.writeInt(var15.getStatus().getCode());
         } else if (var2 instanceof SpdySettingsFrame) {
            SpdySettingsFrame var16 = (SpdySettingsFrame)var2;
            int var18 = var16.clearPreviouslyPersistedSettings() ? 1 : 0;
            Set var22 = var16.getIds();
            var7 = var22.size();
            var8 = 4 + var7 * 8;
            var3.ensureWritable(8 + var8);
            var3.writeShort(this.version | '耀');
            var3.writeShort(4);
            var3.writeByte(var18);
            var3.writeMedium(var8);
            var3.writeInt(var7);
            Iterator var9 = var22.iterator();

            while(var9.hasNext()) {
               Integer var10 = (Integer)var9.next();
               byte var11 = 0;
               if (var16.isPersistValue(var10)) {
                  var11 = (byte)(var11 | 1);
               }

               if (var16.isPersisted(var10)) {
                  var11 = (byte)(var11 | 2);
               }

               var3.writeByte(var11);
               var3.writeMedium(var10);
               var3.writeInt(var16.getValue(var10));
            }
         } else if (var2 instanceof SpdyPingFrame) {
            SpdyPingFrame var17 = (SpdyPingFrame)var2;
            var3.ensureWritable(12);
            var3.writeShort(this.version | '耀');
            var3.writeShort(6);
            var3.writeInt(4);
            var3.writeInt(var17.getId());
         } else if (var2 instanceof SpdyGoAwayFrame) {
            SpdyGoAwayFrame var19 = (SpdyGoAwayFrame)var2;
            var3.ensureWritable(16);
            var3.writeShort(this.version | '耀');
            var3.writeShort(7);
            var3.writeInt(8);
            var3.writeInt(var19.getLastGoodStreamId());
            var3.writeInt(var19.getStatus().getCode());
         } else if (var2 instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame var20 = (SpdyHeadersFrame)var2;
            var5 = this.headerBlockEncoder.encode(var20);
            var6 = var20.isLast() ? 1 : 0;
            var7 = var5.readableBytes();
            var8 = 4 + var7;
            var3.ensureWritable(8 + var8);
            var3.writeShort(this.version | '耀');
            var3.writeShort(8);
            var3.writeByte(var6);
            var3.writeMedium(var8);
            var3.writeInt(var20.getStreamId());
            var3.writeBytes(var5, var5.readerIndex(), var7);
            var5.release();
         } else {
            if (!(var2 instanceof SpdyWindowUpdateFrame)) {
               throw new UnsupportedMessageTypeException(var2, new Class[0]);
            }

            SpdyWindowUpdateFrame var21 = (SpdyWindowUpdateFrame)var2;
            var3.ensureWritable(16);
            var3.writeShort(this.version | '耀');
            var3.writeShort(9);
            var3.writeInt(8);
            var3.writeInt(var21.getStreamId());
            var3.writeInt(var21.getDeltaWindowSize());
         }
      }

   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (SpdyFrame)var2, var3);
   }

   static SpdyHeaderBlockEncoder access$000(SpdyFrameEncoder var0) {
      return var0.headerBlockEncoder;
   }
}
