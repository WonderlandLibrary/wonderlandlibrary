package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.EmptyArrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class SpdySessionHandler extends ChannelDuplexHandler {
   private static final SpdyProtocolException PROTOCOL_EXCEPTION = new SpdyProtocolException();
   private static final SpdyProtocolException STREAM_CLOSED = new SpdyProtocolException("Stream closed");
   private static final int DEFAULT_WINDOW_SIZE = 65536;
   private int initialSendWindowSize = 65536;
   private int initialReceiveWindowSize = 65536;
   private final SpdySession spdySession;
   private int lastGoodStreamId;
   private static final int DEFAULT_MAX_CONCURRENT_STREAMS = Integer.MAX_VALUE;
   private int remoteConcurrentStreams;
   private int localConcurrentStreams;
   private final Object flowControlLock;
   private final AtomicInteger pings;
   private boolean sentGoAwayFrame;
   private boolean receivedGoAwayFrame;
   private ChannelFutureListener closeSessionFutureListener;
   private final boolean server;
   private final int minorVersion;
   private final boolean sessionFlowControl;

   public SpdySessionHandler(SpdyVersion var1, boolean var2) {
      this.spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
      this.remoteConcurrentStreams = Integer.MAX_VALUE;
      this.localConcurrentStreams = Integer.MAX_VALUE;
      this.flowControlLock = new Object();
      this.pings = new AtomicInteger();
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.server = var2;
         this.minorVersion = var1.getMinorVersion();
         this.sessionFlowControl = var1.useSessionFlowControl();
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      int var4;
      int var5;
      int var6;
      if (var2 instanceof SpdyDataFrame) {
         SpdyDataFrame var14 = (SpdyDataFrame)var2;
         var4 = var14.getStreamId();
         DefaultSpdyWindowUpdateFrame var17;
         if (this.sessionFlowControl) {
            var5 = -1 * var14.content().readableBytes();
            var6 = this.spdySession.updateReceiveWindowSize(0, var5);
            if (var6 < 0) {
               this.issueSessionError(var1, SpdySessionStatus.PROTOCOL_ERROR);
               return;
            }

            if (var6 <= this.initialReceiveWindowSize / 2) {
               var5 = this.initialReceiveWindowSize - var6;
               this.spdySession.updateReceiveWindowSize(0, var5);
               var17 = new DefaultSpdyWindowUpdateFrame(0, var5);
               var1.writeAndFlush(var17);
            }
         }

         if (!this.spdySession.isActiveStream(var4)) {
            var14.release();
            if (var4 <= this.lastGoodStreamId) {
               this.issueStreamError(var1, var4, SpdyStreamStatus.PROTOCOL_ERROR);
            } else if (!this.sentGoAwayFrame) {
               this.issueStreamError(var1, var4, SpdyStreamStatus.INVALID_STREAM);
            }

            return;
         }

         if (this.spdySession.isRemoteSideClosed(var4)) {
            var14.release();
            this.issueStreamError(var1, var4, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
            return;
         }

         if (var4 != 0 && !this.spdySession.hasReceivedReply(var4)) {
            var14.release();
            this.issueStreamError(var1, var4, SpdyStreamStatus.PROTOCOL_ERROR);
            return;
         }

         var5 = -1 * var14.content().readableBytes();
         var6 = this.spdySession.updateReceiveWindowSize(var4, var5);
         if (var6 < this.spdySession.getReceiveWindowSizeLowerBound(var4)) {
            var14.release();
            this.issueStreamError(var1, var4, SpdyStreamStatus.FLOW_CONTROL_ERROR);
            return;
         }

         if (var6 < 0) {
            while(var14.content().readableBytes() > this.initialReceiveWindowSize) {
               DefaultSpdyDataFrame var18 = new DefaultSpdyDataFrame(var4, var14.content().readSlice(this.initialReceiveWindowSize).retain());
               var1.writeAndFlush(var18);
            }
         }

         if (var6 <= this.initialReceiveWindowSize / 2 && !var14.isLast()) {
            var5 = this.initialReceiveWindowSize - var6;
            this.spdySession.updateReceiveWindowSize(var4, var5);
            var17 = new DefaultSpdyWindowUpdateFrame(var4, var5);
            var1.writeAndFlush(var17);
         }

         if (var14.isLast()) {
            this.halfCloseStream(var4, true, var1.newSucceededFuture());
         }
      } else if (var2 instanceof SpdySynStreamFrame) {
         SpdySynStreamFrame var13 = (SpdySynStreamFrame)var2;
         var4 = var13.getStreamId();
         if (var13.isInvalid() || var4 == 0 || this.spdySession.isActiveStream(var4)) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.PROTOCOL_ERROR);
            return;
         }

         if (var4 <= this.lastGoodStreamId) {
            this.issueSessionError(var1, SpdySessionStatus.PROTOCOL_ERROR);
            return;
         }

         byte var15 = var13.getPriority();
         boolean var16 = var13.isLast();
         boolean var7 = var13.isUnidirectional();
         if (!var7) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.REFUSED_STREAM);
            return;
         }
      } else if (var2 instanceof SpdySynReplyFrame) {
         SpdySynReplyFrame var12 = (SpdySynReplyFrame)var2;
         var4 = var12.getStreamId();
         if (var12.isInvalid() || var4 == 0 || this.spdySession.isRemoteSideClosed(var4)) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.INVALID_STREAM);
            return;
         }

         if (this.spdySession.hasReceivedReply(var4)) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.STREAM_IN_USE);
            return;
         }

         this.spdySession.receivedReply(var4);
         if (var12.isLast()) {
            this.halfCloseStream(var4, true, var1.newSucceededFuture());
         }
      } else if (var2 instanceof SpdyRstStreamFrame) {
         SpdyRstStreamFrame var3 = (SpdyRstStreamFrame)var2;
         this.removeStream(var3.getStreamId(), var1.newSucceededFuture());
      } else if (var2 instanceof SpdySettingsFrame) {
         SpdySettingsFrame var8 = (SpdySettingsFrame)var2;
         var4 = var8.getValue(0);
         if (var4 >= 0 && var4 != this.minorVersion) {
            this.issueSessionError(var1, SpdySessionStatus.PROTOCOL_ERROR);
            return;
         }

         var5 = var8.getValue(4);
         if (var5 >= 0) {
            this.remoteConcurrentStreams = var5;
         }

         if (var8.isPersisted(7)) {
            var8.removeValue(7);
         }

         var8.setPersistValue(7, false);
         var6 = var8.getValue(7);
         if (var6 >= 0) {
            this.updateInitialSendWindowSize(var6);
         }
      } else if (var2 instanceof SpdyPingFrame) {
         SpdyPingFrame var9 = (SpdyPingFrame)var2;
         if (var9.getId() != 0) {
            var1.writeAndFlush(var9);
            return;
         }

         if (this.pings.get() == 0) {
            return;
         }

         this.pings.getAndDecrement();
      } else if (var2 instanceof SpdyGoAwayFrame) {
         this.receivedGoAwayFrame = true;
      } else if (var2 instanceof SpdyHeadersFrame) {
         SpdyHeadersFrame var10 = (SpdyHeadersFrame)var2;
         var4 = var10.getStreamId();
         if (var10.isInvalid()) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.PROTOCOL_ERROR);
            return;
         }

         if (this.spdySession.isRemoteSideClosed(var4)) {
            this.issueStreamError(var1, var4, SpdyStreamStatus.INVALID_STREAM);
            return;
         }

         if (var10.isLast()) {
            this.halfCloseStream(var4, true, var1.newSucceededFuture());
         }
      } else if (var2 instanceof SpdyWindowUpdateFrame) {
         SpdyWindowUpdateFrame var11 = (SpdyWindowUpdateFrame)var2;
         var4 = var11.getStreamId();
         var5 = var11.getDeltaWindowSize();
         if (var4 != 0 && this.spdySession.isLocalSideClosed(var4)) {
            return;
         }

         if (this.spdySession.getSendWindowSize(var4) > Integer.MAX_VALUE - var5) {
            if (var4 == 0) {
               this.issueSessionError(var1, SpdySessionStatus.PROTOCOL_ERROR);
            } else {
               this.issueStreamError(var1, var4, SpdyStreamStatus.FLOW_CONTROL_ERROR);
            }

            return;
         }

         this.updateSendWindowSize(var1, var4, var5);
      }

      var1.fireChannelRead(var2);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      Iterator var2 = this.spdySession.getActiveStreams().iterator();

      while(var2.hasNext()) {
         Integer var3 = (Integer)var2.next();
         this.removeStream(var3, var1.newSucceededFuture());
      }

      var1.fireChannelInactive();
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if (var2 instanceof SpdyProtocolException) {
         this.issueSessionError(var1, SpdySessionStatus.PROTOCOL_ERROR);
      }

      var1.fireExceptionCaught(var2);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.sendGoAwayFrame(var1, var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      if (!(var2 instanceof SpdyDataFrame) && !(var2 instanceof SpdySynStreamFrame) && !(var2 instanceof SpdySynReplyFrame) && !(var2 instanceof SpdyRstStreamFrame) && !(var2 instanceof SpdySettingsFrame) && !(var2 instanceof SpdyPingFrame) && !(var2 instanceof SpdyGoAwayFrame) && !(var2 instanceof SpdyHeadersFrame) && !(var2 instanceof SpdyWindowUpdateFrame)) {
         var1.write(var2, var3);
      } else {
         this.handleOutboundMessage(var1, var2, var3);
      }

   }

   private void handleOutboundMessage(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      int var5;
      int var7;
      if (var2 instanceof SpdyDataFrame) {
         SpdyDataFrame var17 = (SpdyDataFrame)var2;
         var5 = var17.getStreamId();
         if (this.spdySession.isLocalSideClosed(var5)) {
            var17.release();
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }

         Object var19;
         synchronized(var19 = this.flowControlLock){}
         var7 = var17.content().readableBytes();
         int var21 = this.spdySession.getSendWindowSize(var5);
         if (this.sessionFlowControl) {
            int var9 = this.spdySession.getSendWindowSize(0);
            var21 = Math.min(var21, var9);
         }

         if (var21 <= 0) {
            this.spdySession.putPendingWrite(var5, new SpdySession.PendingWrite(var17, var3));
            return;
         }

         if (var21 < var7) {
            this.spdySession.updateSendWindowSize(var5, -1 * var21);
            if (this.sessionFlowControl) {
               this.spdySession.updateSendWindowSize(0, -1 * var21);
            }

            DefaultSpdyDataFrame var22 = new DefaultSpdyDataFrame(var5, var17.content().readSlice(var21).retain());
            this.spdySession.putPendingWrite(var5, new SpdySession.PendingWrite(var17, var3));
            var1.write(var22).addListener(new ChannelFutureListener(this, var1) {
               final ChannelHandlerContext val$context;
               final SpdySessionHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$context = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     SpdySessionHandler.access$000(this.this$0, this.val$context, SpdySessionStatus.INTERNAL_ERROR);
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
            return;
         }

         this.spdySession.updateSendWindowSize(var5, -1 * var7);
         if (this.sessionFlowControl) {
            this.spdySession.updateSendWindowSize(0, -1 * var7);
         }

         var3.addListener(new ChannelFutureListener(this, var1) {
            final ChannelHandlerContext val$context;
            final SpdySessionHandler this$0;

            {
               this.this$0 = var1;
               this.val$context = var2;
            }

            public void operationComplete(ChannelFuture var1) throws Exception {
               if (!var1.isSuccess()) {
                  SpdySessionHandler.access$000(this.this$0, this.val$context, SpdySessionStatus.INTERNAL_ERROR);
               }

            }

            public void operationComplete(Future var1) throws Exception {
               this.operationComplete((ChannelFuture)var1);
            }
         });
         if (var17.isLast()) {
            this.halfCloseStream(var5, false, var3);
         }
      } else if (var2 instanceof SpdySynStreamFrame) {
         SpdySynStreamFrame var16 = (SpdySynStreamFrame)var2;
         var5 = var16.getStreamId();
         if (var5 != 0) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }

         byte var18 = var16.getPriority();
         boolean var20 = var16.isUnidirectional();
         boolean var8 = var16.isLast();
         if (!var8) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }
      } else if (var2 instanceof SpdySynReplyFrame) {
         SpdySynReplyFrame var15 = (SpdySynReplyFrame)var2;
         var5 = var15.getStreamId();
         if (var5 == 0 || this.spdySession.isLocalSideClosed(var5)) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }

         if (var15.isLast()) {
            this.halfCloseStream(var5, false, var3);
         }
      } else if (var2 instanceof SpdyRstStreamFrame) {
         SpdyRstStreamFrame var4 = (SpdyRstStreamFrame)var2;
         this.removeStream(var4.getStreamId(), var3);
      } else if (var2 instanceof SpdySettingsFrame) {
         SpdySettingsFrame var12 = (SpdySettingsFrame)var2;
         var5 = var12.getValue(0);
         if (var5 >= 0 && var5 != this.minorVersion) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }

         int var6 = var12.getValue(4);
         if (var6 >= 0) {
            this.localConcurrentStreams = var6;
         }

         if (var12.isPersisted(7)) {
            var12.removeValue(7);
         }

         var12.setPersistValue(7, false);
         var7 = var12.getValue(7);
         if (var7 >= 0) {
            this.updateInitialReceiveWindowSize(var7);
         }
      } else if (var2 instanceof SpdyPingFrame) {
         SpdyPingFrame var13 = (SpdyPingFrame)var2;
         if (var13.getId() != 0) {
            var1.fireExceptionCaught(new IllegalArgumentException("invalid PING ID: " + var13.getId()));
            return;
         }

         this.pings.getAndIncrement();
      } else {
         if (var2 instanceof SpdyGoAwayFrame) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }

         if (var2 instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame var14 = (SpdyHeadersFrame)var2;
            var5 = var14.getStreamId();
            if (this.spdySession.isLocalSideClosed(var5)) {
               var3.setFailure(PROTOCOL_EXCEPTION);
               return;
            }

            if (var14.isLast()) {
               this.halfCloseStream(var5, false, var3);
            }
         } else if (var2 instanceof SpdyWindowUpdateFrame) {
            var3.setFailure(PROTOCOL_EXCEPTION);
            return;
         }
      }

      var1.write(var2, var3);
   }

   private void issueSessionError(ChannelHandlerContext var1, SpdySessionStatus var2) {
      this.sendGoAwayFrame(var1, var2).addListener(new SpdySessionHandler.ClosingChannelFutureListener(var1, var1.newPromise()));
   }

   private void issueStreamError(ChannelHandlerContext var1, int var2, SpdyStreamStatus var3) {
      boolean var4 = !this.spdySession.isRemoteSideClosed(var2);
      ChannelPromise var5 = var1.newPromise();
      this.removeStream(var2, var5);
      DefaultSpdyRstStreamFrame var6 = new DefaultSpdyRstStreamFrame(var2, var3);
      var1.writeAndFlush(var6, var5);
      if (var4) {
         var1.fireChannelRead(var6);
      }

   }

   private synchronized void updateInitialSendWindowSize(int var1) {
      int var2 = var1 - this.initialSendWindowSize;
      this.initialSendWindowSize = var1;
      this.spdySession.updateAllSendWindowSizes(var2);
   }

   private synchronized void updateInitialReceiveWindowSize(int var1) {
      int var2 = var1 - this.initialReceiveWindowSize;
      this.initialReceiveWindowSize = var1;
      this.spdySession.updateAllReceiveWindowSizes(var2);
   }

   private void halfCloseStream(int var1, boolean var2, ChannelFuture var3) {
      if (var2) {
         this.spdySession.closeRemoteSide(var1, this.isRemoteInitiatedId(var1));
      } else {
         this.spdySession.closeLocalSide(var1, this.isRemoteInitiatedId(var1));
      }

      if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
         var3.addListener(this.closeSessionFutureListener);
      }

   }

   private void removeStream(int var1, ChannelFuture var2) {
      this.spdySession.removeStream(var1, STREAM_CLOSED, this.isRemoteInitiatedId(var1));
      if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
         var2.addListener(this.closeSessionFutureListener);
      }

   }

   private void updateSendWindowSize(ChannelHandlerContext var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.flowControlLock){}
      int var5 = this.spdySession.updateSendWindowSize(var2, var3);
      if (this.sessionFlowControl && var2 != 0) {
         int var6 = this.spdySession.getSendWindowSize(0);
         var5 = Math.min(var5, var6);
      }

      while(var5 > 0) {
         SpdySession.PendingWrite var12 = this.spdySession.getPendingWrite(var2);
         if (var12 == null) {
            break;
         }

         SpdyDataFrame var7 = var12.spdyDataFrame;
         int var8 = var7.content().readableBytes();
         int var9 = var7.getStreamId();
         if (this.sessionFlowControl && var2 == 0) {
            var5 = Math.min(var5, this.spdySession.getSendWindowSize(var9));
         }

         if (var5 >= var8) {
            this.spdySession.removePendingWrite(var9);
            var5 = this.spdySession.updateSendWindowSize(var9, -1 * var8);
            if (this.sessionFlowControl) {
               int var10 = this.spdySession.updateSendWindowSize(0, -1 * var8);
               var5 = Math.min(var5, var10);
            }

            if (var7.isLast()) {
               this.halfCloseStream(var9, false, var12.promise);
            }

            var1.writeAndFlush(var7, var12.promise).addListener(new ChannelFutureListener(this, var1) {
               final ChannelHandlerContext val$ctx;
               final SpdySessionHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     SpdySessionHandler.access$000(this.this$0, this.val$ctx, SpdySessionStatus.INTERNAL_ERROR);
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         } else {
            this.spdySession.updateSendWindowSize(var9, -1 * var5);
            if (this.sessionFlowControl) {
               this.spdySession.updateSendWindowSize(0, -1 * var5);
            }

            DefaultSpdyDataFrame var13 = new DefaultSpdyDataFrame(var9, var7.content().readSlice(var5).retain());
            var1.writeAndFlush(var13).addListener(new ChannelFutureListener(this, var1) {
               final ChannelHandlerContext val$ctx;
               final SpdySessionHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     SpdySessionHandler.access$000(this.this$0, this.val$ctx, SpdySessionStatus.INTERNAL_ERROR);
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
            var5 = 0;
         }
      }

   }

   private void sendGoAwayFrame(ChannelHandlerContext var1, ChannelPromise var2) {
      if (!var1.channel().isActive()) {
         var1.close(var2);
      } else {
         ChannelFuture var3 = this.sendGoAwayFrame(var1, SpdySessionStatus.OK);
         if (this.spdySession.noActiveStreams()) {
            var3.addListener(new SpdySessionHandler.ClosingChannelFutureListener(var1, var2));
         } else {
            this.closeSessionFutureListener = new SpdySessionHandler.ClosingChannelFutureListener(var1, var2);
         }

      }
   }

   private synchronized ChannelFuture sendGoAwayFrame(ChannelHandlerContext var1, SpdySessionStatus var2) {
      if (!this.sentGoAwayFrame) {
         this.sentGoAwayFrame = true;
         DefaultSpdyGoAwayFrame var3 = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, var2);
         return var1.writeAndFlush(var3);
      } else {
         return var1.newSucceededFuture();
      }
   }

   static void access$000(SpdySessionHandler var0, ChannelHandlerContext var1, SpdySessionStatus var2) {
      var0.issueSessionError(var1, var2);
   }

   static {
      PROTOCOL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      STREAM_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   private static final class ClosingChannelFutureListener implements ChannelFutureListener {
      private final ChannelHandlerContext ctx;
      private final ChannelPromise promise;

      ClosingChannelFutureListener(ChannelHandlerContext var1, ChannelPromise var2) {
         this.ctx = var1;
         this.promise = var2;
      }

      public void operationComplete(ChannelFuture var1) throws Exception {
         this.ctx.close(this.promise);
      }

      public void operationComplete(Future var1) throws Exception {
         this.operationComplete((ChannelFuture)var1);
      }
   }
}
