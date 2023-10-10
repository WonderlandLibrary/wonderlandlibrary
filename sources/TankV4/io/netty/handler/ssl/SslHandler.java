package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PendingWrite;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
   private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
   private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
   private static final SSLException SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
   private static final SSLException HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
   private static final ClosedChannelException CHANNEL_CLOSED = new ClosedChannelException();
   private volatile ChannelHandlerContext ctx;
   private final SSLEngine engine;
   private final int maxPacketBufferSize;
   private final Executor delegatedTaskExecutor;
   private final boolean startTls;
   private boolean sentFirstMessage;
   private final SslHandler.LazyChannelPromise handshakePromise;
   private final SslHandler.LazyChannelPromise sslCloseFuture;
   private final Deque pendingUnencryptedWrites;
   private boolean needsFlush;
   private int packetLength;
   private ByteBuf decodeOut;
   private volatile long handshakeTimeoutMillis;
   private volatile long closeNotifyTimeoutMillis;
   static final boolean $assertionsDisabled = !SslHandler.class.desiredAssertionStatus();

   public SslHandler(SSLEngine var1) {
      this(var1, false);
   }

   public SslHandler(SSLEngine var1, boolean var2) {
      this(var1, var2, ImmediateExecutor.INSTANCE);
   }

   /** @deprecated */
   @Deprecated
   public SslHandler(SSLEngine var1, Executor var2) {
      this(var1, false, var2);
   }

   /** @deprecated */
   @Deprecated
   public SslHandler(SSLEngine var1, boolean var2, Executor var3) {
      this.handshakePromise = new SslHandler.LazyChannelPromise(this);
      this.sslCloseFuture = new SslHandler.LazyChannelPromise(this);
      this.pendingUnencryptedWrites = new ArrayDeque();
      this.handshakeTimeoutMillis = 10000L;
      this.closeNotifyTimeoutMillis = 3000L;
      if (var1 == null) {
         throw new NullPointerException("engine");
      } else if (var3 == null) {
         throw new NullPointerException("delegatedTaskExecutor");
      } else {
         this.engine = var1;
         this.delegatedTaskExecutor = var3;
         this.startTls = var2;
         this.maxPacketBufferSize = var1.getSession().getPacketBufferSize();
      }
   }

   public long getHandshakeTimeoutMillis() {
      return this.handshakeTimeoutMillis;
   }

   public void setHandshakeTimeout(long var1, TimeUnit var3) {
      if (var3 == null) {
         throw new NullPointerException("unit");
      } else {
         this.setHandshakeTimeoutMillis(var3.toMillis(var1));
      }
   }

   public void setHandshakeTimeoutMillis(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("handshakeTimeoutMillis: " + var1 + " (expected: >= 0)");
      } else {
         this.handshakeTimeoutMillis = var1;
      }
   }

   public long getCloseNotifyTimeoutMillis() {
      return this.closeNotifyTimeoutMillis;
   }

   public void setCloseNotifyTimeout(long var1, TimeUnit var3) {
      if (var3 == null) {
         throw new NullPointerException("unit");
      } else {
         this.setCloseNotifyTimeoutMillis(var3.toMillis(var1));
      }
   }

   public void setCloseNotifyTimeoutMillis(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + var1 + " (expected: >= 0)");
      } else {
         this.closeNotifyTimeoutMillis = var1;
      }
   }

   public SSLEngine engine() {
      return this.engine;
   }

   public Future handshakeFuture() {
      return this.handshakePromise;
   }

   public ChannelFuture close() {
      return this.close(this.ctx.newPromise());
   }

   public ChannelFuture close(ChannelPromise var1) {
      ChannelHandlerContext var2 = this.ctx;
      var2.executor().execute(new Runnable(this, var2, var1) {
         final ChannelHandlerContext val$ctx;
         final ChannelPromise val$future;
         final SslHandler this$0;

         {
            this.this$0 = var1;
            this.val$ctx = var2;
            this.val$future = var3;
         }

         public void run() {
            SslHandler.access$100(this.this$0).closeOutbound();

            try {
               this.this$0.write(this.val$ctx, Unpooled.EMPTY_BUFFER, this.val$future);
               this.this$0.flush(this.val$ctx);
            } catch (Exception var2) {
               if (!this.val$future.tryFailure(var2)) {
                  SslHandler.access$200().warn("flush() raised a masked exception.", (Throwable)var2);
               }
            }

         }
      });
      return var1;
   }

   public Future sslCloseFuture() {
      return this.sslCloseFuture;
   }

   public void handlerRemoved0(ChannelHandlerContext var1) throws Exception {
      if (this.decodeOut != null) {
         this.decodeOut.release();
         this.decodeOut = null;
      }

      while(true) {
         PendingWrite var2 = (PendingWrite)this.pendingUnencryptedWrites.poll();
         if (var2 == null) {
            return;
         }

         var2.failAndRecycle(new ChannelException("Pending write on removal of SslHandler"));
      }
   }

   public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
      var1.bind(var2, var3);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
      var1.connect(var2, var3, var4);
   }

   /** @deprecated */
   @Deprecated
   public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      var1.deregister(var2);
   }

   public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.closeOutboundAndChannel(var1, var2, true);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.closeOutboundAndChannel(var1, var2, false);
   }

   public void read(ChannelHandlerContext var1) {
      var1.read();
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.pendingUnencryptedWrites.add(PendingWrite.newInstance(var2, var3));
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      if (this.startTls && !this.sentFirstMessage) {
         this.sentFirstMessage = true;

         while(true) {
            PendingWrite var2 = (PendingWrite)this.pendingUnencryptedWrites.poll();
            if (var2 == null) {
               var1.flush();
               return;
            }

            var1.write(var2.msg(), (ChannelPromise)var2.recycleAndGet());
         }
      } else {
         if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(PendingWrite.newInstance(Unpooled.EMPTY_BUFFER, (Promise)null));
         }

         this.wrap(var1, false);
         var1.flush();
      }
   }

   private void wrap(ChannelHandlerContext var1, boolean var2) throws SSLException {
      ByteBuf var3 = null;
      ChannelPromise var4 = null;

      label58: {
         label57: {
            try {
               while(true) {
                  PendingWrite var5 = (PendingWrite)this.pendingUnencryptedWrites.peek();
                  if (var5 == null) {
                     break;
                  }

                  if (var3 == null) {
                     var3 = var1.alloc().buffer(this.maxPacketBufferSize);
                  }

                  if (!(var5.msg() instanceof ByteBuf)) {
                     var1.write(var5.msg(), (ChannelPromise)var5.recycleAndGet());
                     this.pendingUnencryptedWrites.remove();
                  } else {
                     ByteBuf var6 = (ByteBuf)var5.msg();
                     SSLEngineResult var7 = this.wrap(this.engine, var6, var3);
                     if (!var6.isReadable()) {
                        var6.release();
                        var4 = (ChannelPromise)var5.recycleAndGet();
                        this.pendingUnencryptedWrites.remove();
                     } else {
                        var4 = null;
                     }

                     if (var7.getStatus() == Status.CLOSED) {
                        while(true) {
                           PendingWrite var8 = (PendingWrite)this.pendingUnencryptedWrites.poll();
                           if (var8 == null) {
                              break label58;
                           }

                           var8.failAndRecycle(SSLENGINE_CLOSED);
                        }
                     }

                     switch(var7.getHandshakeStatus()) {
                     case NEED_TASK:
                        this.runDelegatedTasks();
                        break;
                     case FINISHED:
                        this.setHandshakeSuccess();
                     case NOT_HANDSHAKING:
                     case NEED_WRAP:
                        this.finishWrap(var1, var3, var4, var2);
                        var4 = null;
                        var3 = null;
                        break;
                     case NEED_UNWRAP:
                        break label57;
                     default:
                        throw new IllegalStateException("Unknown handshake status: " + var7.getHandshakeStatus());
                     }
                  }
               }
            } catch (SSLException var10) {
               this.setHandshakeFailure(var10);
               throw var10;
            }

            this.finishWrap(var1, var3, var4, var2);
            return;
         }

         this.finishWrap(var1, var3, var4, var2);
         return;
      }

      this.finishWrap(var1, var3, var4, var2);
   }

   private void finishWrap(ChannelHandlerContext var1, ByteBuf var2, ChannelPromise var3, boolean var4) {
      if (var2 == null) {
         var2 = Unpooled.EMPTY_BUFFER;
      } else if (!var2.isReadable()) {
         var2.release();
         var2 = Unpooled.EMPTY_BUFFER;
      }

      if (var3 != null) {
         var1.write(var2, var3);
      } else {
         var1.write(var2);
      }

      if (var4) {
         this.needsFlush = true;
      }

   }

   private void wrapNonAppData(ChannelHandlerContext var1, boolean var2) throws SSLException {
      ByteBuf var3 = null;

      SSLEngineResult var4;
      try {
         do {
            if (var3 == null) {
               var3 = var1.alloc().buffer(this.maxPacketBufferSize);
            }

            var4 = this.wrap(this.engine, Unpooled.EMPTY_BUFFER, var3);
            if (var4.bytesProduced() > 0) {
               var1.write(var3);
               if (var2) {
                  this.needsFlush = true;
               }

               var3 = null;
            }

            switch(var4.getHandshakeStatus()) {
            case NEED_TASK:
               this.runDelegatedTasks();
               break;
            case FINISHED:
               this.setHandshakeSuccess();
               break;
            case NOT_HANDSHAKING:
               if (!var2) {
                  this.unwrap(var1);
               }
            case NEED_WRAP:
               break;
            case NEED_UNWRAP:
               if (!var2) {
                  this.unwrap(var1);
               }
               break;
            default:
               throw new IllegalStateException("Unknown handshake status: " + var4.getHandshakeStatus());
            }
         } while(var4.bytesProduced() != 0);
      } catch (SSLException var6) {
         this.setHandshakeFailure(var6);
         throw var6;
      }

      if (var3 != null) {
         var3.release();
      }

   }

   private SSLEngineResult wrap(SSLEngine var1, ByteBuf var2, ByteBuf var3) throws SSLException {
      ByteBuffer var4 = var2.nioBuffer();

      while(true) {
         ByteBuffer var5 = var3.nioBuffer(var3.writerIndex(), var3.writableBytes());
         SSLEngineResult var6 = var1.wrap(var4, var5);
         var2.skipBytes(var6.bytesConsumed());
         var3.writerIndex(var3.writerIndex() + var6.bytesProduced());
         switch(var6.getStatus()) {
         case BUFFER_OVERFLOW:
            var3.ensureWritable(this.maxPacketBufferSize);
            break;
         default:
            return var6;
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.setHandshakeFailure(CHANNEL_CLOSED);
      super.channelInactive(var1);
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if (var2 == false) {
         if (logger.isDebugEnabled()) {
            logger.debug("Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", var2);
         }

         if (var1.channel().isActive()) {
            var1.close();
         }
      } else {
         var1.fireExceptionCaught(var2);
      }

   }

   public static boolean isEncrypted(ByteBuf var0) {
      if (var0.readableBytes() < 5) {
         throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
      } else {
         return getEncryptedPacketLength(var0, var0.readerIndex()) != -1;
      }
   }

   private static int getEncryptedPacketLength(ByteBuf var0, int var1) {
      int var2 = 0;
      boolean var3;
      switch(var0.getUnsignedByte(var1)) {
      case 20:
      case 21:
      case 22:
      case 23:
         var3 = true;
         break;
      default:
         var3 = false;
      }

      if (var3) {
         short var4 = var0.getUnsignedByte(var1 + 1);
         if (var4 == 3) {
            var2 = var0.getUnsignedShort(var1 + 3) + 5;
            if (var2 <= 5) {
               var3 = false;
            }
         } else {
            var3 = false;
         }
      }

      if (!var3) {
         boolean var7 = true;
         int var5 = (var0.getUnsignedByte(var1) & 128) != 0 ? 2 : 3;
         short var6 = var0.getUnsignedByte(var1 + var5 + 1);
         if (var6 != 2 && var6 != 3) {
            var7 = false;
         } else {
            if (var5 == 2) {
               var2 = (var0.getShort(var1) & 32767) + 2;
            } else {
               var2 = (var0.getShort(var1) & 16383) + 3;
            }

            if (var2 <= var5) {
               var7 = false;
            }
         }

         if (!var7) {
            return -1;
         }
      }

      return var2;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws SSLException {
      int var4 = var2.readerIndex();
      int var5 = var2.writerIndex();
      int var6 = var4;
      if (this.packetLength > 0) {
         if (var5 - var4 < this.packetLength) {
            return;
         }

         var6 = var4 + this.packetLength;
         this.packetLength = 0;
      }

      boolean var7 = false;

      int var8;
      while(true) {
         var8 = var5 - var6;
         if (var8 < 5) {
            break;
         }

         int var9 = getEncryptedPacketLength(var2, var6);
         if (var9 == -1) {
            var7 = true;
            break;
         }

         if (!$assertionsDisabled && var9 <= 0) {
            throw new AssertionError();
         }

         if (var9 > var8) {
            this.packetLength = var9;
            break;
         }

         var6 += var9;
      }

      var8 = var6 - var4;
      if (var8 > 0) {
         var2.skipBytes(var8);
         ByteBuffer var10 = var2.nioBuffer(var4, var8);
         this.unwrap(var1, var10, var3);
      }

      if (var7) {
         NotSslRecordException var11 = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(var2));
         var2.skipBytes(var2.readableBytes());
         var1.fireExceptionCaught(var11);
         this.setHandshakeFailure(var11);
      }

   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      if (this.needsFlush) {
         this.needsFlush = false;
         var1.flush();
      }

      super.channelReadComplete(var1);
   }

   private void unwrap(ChannelHandlerContext var1) throws SSLException {
      RecyclableArrayList var2 = RecyclableArrayList.newInstance();
      this.unwrap((ChannelHandlerContext)var1, Unpooled.EMPTY_BUFFER.nioBuffer(), (List)var2);
      int var3 = var2.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         var1.fireChannelRead(var2.get(var4));
      }

      var2.recycle();
   }

   private void unwrap(ChannelHandlerContext var1, ByteBuffer var2, List var3) throws SSLException {
      boolean var4 = false;
      int var5 = 0;

      try {
         while(true) {
            if (this.decodeOut == null) {
               this.decodeOut = var1.alloc().buffer(var2.remaining());
            }

            SSLEngineResult var6 = unwrap(this.engine, var2, this.decodeOut);
            Status var7 = var6.getStatus();
            HandshakeStatus var8 = var6.getHandshakeStatus();
            int var9 = var6.bytesProduced();
            int var10 = var6.bytesConsumed();
            var5 += var9;
            if (var7 == Status.CLOSED) {
               this.sslCloseFuture.trySuccess(var1.channel());
            } else {
               switch(var8) {
               case NEED_TASK:
                  this.runDelegatedTasks();
                  break;
               case FINISHED:
                  this.setHandshakeSuccess();
                  var4 = true;
                  continue;
               case NOT_HANDSHAKING:
               case NEED_UNWRAP:
                  break;
               case NEED_WRAP:
                  this.wrapNonAppData(var1, true);
                  break;
               default:
                  throw new IllegalStateException("Unknown handshake status: " + var8);
               }

               if (var7 != Status.BUFFER_UNDERFLOW && (var10 != 0 || var9 != 0)) {
                  continue;
               }
            }

            if (var4) {
               this.wrap(var1, true);
            }
            break;
         }
      } catch (SSLException var13) {
         this.setHandshakeFailure(var13);
         throw var13;
      }

      if (var5 > 0) {
         ByteBuf var14 = this.decodeOut;
         this.decodeOut = null;
         var3.add(var14);
      }

   }

   private static SSLEngineResult unwrap(SSLEngine var0, ByteBuffer var1, ByteBuf var2) throws SSLException {
      int var3 = 0;

      while(true) {
         ByteBuffer var4 = var2.nioBuffer(var2.writerIndex(), var2.writableBytes());
         SSLEngineResult var5 = var0.unwrap(var1, var4);
         var2.writerIndex(var2.writerIndex() + var5.bytesProduced());
         switch(var5.getStatus()) {
         case BUFFER_OVERFLOW:
            int var6 = var0.getSession().getApplicationBufferSize();
            switch(var3++) {
            case 0:
               var2.ensureWritable(Math.min(var6, var1.remaining()));
               continue;
            default:
               var2.ensureWritable(var6);
               continue;
            }
         default:
            return var5;
         }
      }
   }

   private void runDelegatedTasks() {
      if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
         while(true) {
            Runnable var6 = this.engine.getDelegatedTask();
            if (var6 == null) {
               break;
            }

            var6.run();
         }
      } else {
         ArrayList var1 = new ArrayList(2);

         while(true) {
            Runnable var2 = this.engine.getDelegatedTask();
            if (var2 == null) {
               if (var1.isEmpty()) {
                  return;
               }

               CountDownLatch var7 = new CountDownLatch(1);
               this.delegatedTaskExecutor.execute(new Runnable(this, var1, var7) {
                  final List val$tasks;
                  final CountDownLatch val$latch;
                  final SslHandler this$0;

                  {
                     this.this$0 = var1;
                     this.val$tasks = var2;
                     this.val$latch = var3;
                  }

                  public void run() {
                     try {
                        Iterator var1 = this.val$tasks.iterator();

                        while(var1.hasNext()) {
                           Runnable var2 = (Runnable)var1.next();
                           var2.run();
                        }
                     } catch (Exception var4) {
                        SslHandler.access$300(this.this$0).fireExceptionCaught(var4);
                        this.val$latch.countDown();
                        return;
                     }

                     this.val$latch.countDown();
                  }
               });
               boolean var3 = false;

               while(var7.getCount() != 0L) {
                  try {
                     var7.await();
                  } catch (InterruptedException var5) {
                     var3 = true;
                  }
               }

               if (var3) {
                  Thread.currentThread().interrupt();
               }
               break;
            }

            var1.add(var2);
         }
      }

   }

   private void setHandshakeSuccess() {
      if (this.handshakePromise.trySuccess(this.ctx.channel())) {
         this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
      }

   }

   private void setHandshakeFailure(Throwable var1) {
      this.engine.closeOutbound();

      try {
         this.engine.closeInbound();
      } catch (SSLException var4) {
         String var3 = var4.getMessage();
         if (var3 == null || !var3.contains("possible truncation attack")) {
            logger.debug("SSLEngine.closeInbound() raised an exception.", (Throwable)var4);
         }
      }

      this.notifyHandshakeFailure(var1);

      while(true) {
         PendingWrite var2 = (PendingWrite)this.pendingUnencryptedWrites.poll();
         if (var2 == null) {
            return;
         }

         var2.failAndRecycle(var1);
      }
   }

   private void notifyHandshakeFailure(Throwable var1) {
      if (this.handshakePromise.tryFailure(var1)) {
         this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(var1));
         this.ctx.close();
      }

   }

   private void closeOutboundAndChannel(ChannelHandlerContext var1, ChannelPromise var2, boolean var3) throws Exception {
      if (!var1.channel().isActive()) {
         if (var3) {
            var1.disconnect(var2);
         } else {
            var1.close(var2);
         }

      } else {
         this.engine.closeOutbound();
         ChannelPromise var4 = var1.newPromise();
         this.write(var1, Unpooled.EMPTY_BUFFER, var4);
         this.flush(var1);
         this.safeClose(var1, var4, var2);
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
      if (var1.channel().isActive()) {
         this.handshake();
      }

   }

   private Future handshake() {
      ScheduledFuture var1;
      if (this.handshakeTimeoutMillis > 0L) {
         var1 = this.ctx.executor().schedule(new Runnable(this) {
            final SslHandler this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               if (!SslHandler.access$400(this.this$0).isDone()) {
                  SslHandler.access$600(this.this$0, SslHandler.access$500());
               }
            }
         }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
      } else {
         var1 = null;
      }

      this.handshakePromise.addListener(new GenericFutureListener(this, var1) {
         final java.util.concurrent.ScheduledFuture val$timeoutFuture;
         final SslHandler this$0;

         {
            this.this$0 = var1;
            this.val$timeoutFuture = var2;
         }

         public void operationComplete(Future var1) throws Exception {
            if (this.val$timeoutFuture != null) {
               this.val$timeoutFuture.cancel(false);
            }

         }
      });

      try {
         this.engine.beginHandshake();
         this.wrapNonAppData(this.ctx, false);
         this.ctx.flush();
      } catch (Exception var3) {
         this.notifyHandshakeFailure(var3);
      }

      return this.handshakePromise;
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      if (!this.startTls && this.engine.getUseClientMode()) {
         this.handshake().addListener(new GenericFutureListener(this, var1) {
            final ChannelHandlerContext val$ctx;
            final SslHandler this$0;

            {
               this.this$0 = var1;
               this.val$ctx = var2;
            }

            public void operationComplete(Future var1) throws Exception {
               if (!var1.isSuccess()) {
                  SslHandler.access$200().debug("Failed to complete handshake", var1.cause());
                  this.val$ctx.close();
               }

            }
         });
      }

      var1.fireChannelActive();
   }

   private void safeClose(ChannelHandlerContext var1, ChannelFuture var2, ChannelPromise var3) {
      if (!var1.channel().isActive()) {
         var1.close(var3);
      } else {
         ScheduledFuture var4;
         if (this.closeNotifyTimeoutMillis > 0L) {
            var4 = var1.executor().schedule(new Runnable(this, var1, var3) {
               final ChannelHandlerContext val$ctx;
               final ChannelPromise val$promise;
               final SslHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
                  this.val$promise = var3;
               }

               public void run() {
                  SslHandler.access$200().warn(this.val$ctx.channel() + " last write attempt timed out." + " Force-closing the connection.");
                  this.val$ctx.close(this.val$promise);
               }
            }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS);
         } else {
            var4 = null;
         }

         var2.addListener(new ChannelFutureListener(this, var4, var1, var3) {
            final java.util.concurrent.ScheduledFuture val$timeoutFuture;
            final ChannelHandlerContext val$ctx;
            final ChannelPromise val$promise;
            final SslHandler this$0;

            {
               this.this$0 = var1;
               this.val$timeoutFuture = var2;
               this.val$ctx = var3;
               this.val$promise = var4;
            }

            public void operationComplete(ChannelFuture var1) throws Exception {
               if (this.val$timeoutFuture != null) {
                  this.val$timeoutFuture.cancel(false);
               }

               if (this.val$ctx.channel().isActive()) {
                  this.val$ctx.close(this.val$promise);
               }

            }

            public void operationComplete(Future var1) throws Exception {
               this.operationComplete((ChannelFuture)var1);
            }
         });
      }
   }

   static SSLEngine access$100(SslHandler var0) {
      return var0.engine;
   }

   static InternalLogger access$200() {
      return logger;
   }

   static ChannelHandlerContext access$300(SslHandler var0) {
      return var0.ctx;
   }

   static SslHandler.LazyChannelPromise access$400(SslHandler var0) {
      return var0.handshakePromise;
   }

   static SSLException access$500() {
      return HANDSHAKE_TIMED_OUT;
   }

   static void access$600(SslHandler var0, Throwable var1) {
      var0.notifyHandshakeFailure(var1);
   }

   static {
      SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   private final class LazyChannelPromise extends DefaultPromise {
      final SslHandler this$0;

      private LazyChannelPromise(SslHandler var1) {
         this.this$0 = var1;
      }

      protected EventExecutor executor() {
         if (SslHandler.access$300(this.this$0) == null) {
            throw new IllegalStateException();
         } else {
            return SslHandler.access$300(this.this$0).executor();
         }
      }

      LazyChannelPromise(SslHandler var1, Object var2) {
         this(var1);
      }
   }
}
