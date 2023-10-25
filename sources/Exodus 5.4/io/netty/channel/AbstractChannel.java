/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.DefaultChannelProgressivePromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractChannel
extends DefaultAttributeMap
implements Channel {
    private final DefaultChannelPipeline pipeline;
    private final CloseFuture closeFuture;
    private final VoidChannelPromise unsafeVoidPromise;
    private final ChannelFuture succeededFuture;
    private boolean strValActive;
    private MessageSizeEstimator.Handle estimatorHandle;
    private final Channel parent;
    static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION;
    private String strVal;
    private volatile boolean registered;
    private final long hashCode = ThreadLocalRandom.current().nextLong();
    private volatile EventLoop eventLoop;
    static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION;
    private volatile SocketAddress localAddress;
    private final Channel.Unsafe unsafe;
    private static final InternalLogger logger;
    private volatile SocketAddress remoteAddress;
    private final VoidChannelPromise voidPromise;

    protected void doRegister() throws Exception {
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
        return this.pipeline.connect(socketAddress, socketAddress2);
    }

    @Override
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }

    @Override
    public EventLoop eventLoop() {
        EventLoop eventLoop = this.eventLoop;
        if (eventLoop == null) {
            throw new IllegalStateException("channel not registered to an event loop");
        }
        return eventLoop;
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }

    protected void invalidateLocalAddress() {
        this.localAddress = null;
    }

    @Override
    public SocketAddress remoteAddress() {
        SocketAddress socketAddress = this.remoteAddress;
        if (socketAddress == null) {
            try {
                this.remoteAddress = socketAddress = this.unsafe().remoteAddress();
            }
            catch (Throwable throwable) {
                return null;
            }
        }
        return socketAddress;
    }

    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this);
    }

    protected abstract boolean isCompatible(EventLoop var1);

    @Override
    public ChannelFuture writeAndFlush(Object object) {
        return this.pipeline.writeAndFlush(object);
    }

    @Override
    public ChannelFuture writeAndFlush(Object object, ChannelPromise channelPromise) {
        return this.pipeline.writeAndFlush(object, channelPromise);
    }

    protected void invalidateRemoteAddress() {
        this.remoteAddress = null;
    }

    protected void doDeregister() throws Exception {
    }

    public final boolean equals(Object object) {
        return this == object;
    }

    public String toString() {
        boolean bl = this.isActive();
        if (this.strValActive == bl && this.strVal != null) {
            return this.strVal;
        }
        SocketAddress socketAddress = this.remoteAddress();
        SocketAddress socketAddress2 = this.localAddress();
        if (socketAddress != null) {
            SocketAddress socketAddress3;
            SocketAddress socketAddress4;
            if (this.parent == null) {
                socketAddress4 = socketAddress2;
                socketAddress3 = socketAddress;
            } else {
                socketAddress4 = socketAddress;
                socketAddress3 = socketAddress2;
            }
            this.strVal = String.format("[id: 0x%08x, %s %s %s]", (int)this.hashCode, socketAddress4, bl ? "=>" : ":>", socketAddress3);
        } else {
            this.strVal = socketAddress2 != null ? String.format("[id: 0x%08x, %s]", (int)this.hashCode, socketAddress2) : String.format("[id: 0x%08x]", (int)this.hashCode);
        }
        this.strValActive = bl;
        return this.strVal;
    }

    protected abstract SocketAddress remoteAddress0();

    @Override
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
    }

    @Override
    public boolean isWritable() {
        ChannelOutboundBuffer channelOutboundBuffer = this.unsafe.outboundBuffer();
        return channelOutboundBuffer != null && channelOutboundBuffer.isWritable();
    }

    @Override
    public Channel read() {
        this.pipeline.read();
        return this;
    }

    protected abstract void doDisconnect() throws Exception;

    @Override
    public Channel parent() {
        return this.parent;
    }

    @Override
    public SocketAddress localAddress() {
        SocketAddress socketAddress = this.localAddress;
        if (socketAddress == null) {
            try {
                this.localAddress = socketAddress = this.unsafe().localAddress();
            }
            catch (Throwable throwable) {
                return null;
            }
        }
        return socketAddress;
    }

    @Override
    public ChannelFuture disconnect(ChannelPromise channelPromise) {
        return this.pipeline.disconnect(channelPromise);
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return this.pipeline.bind(socketAddress, channelPromise);
    }

    @Override
    public ChannelFuture write(Object object) {
        return this.pipeline.write(object);
    }

    @Override
    public Channel.Unsafe unsafe() {
        return this.unsafe;
    }

    protected abstract void doBind(SocketAddress var1) throws Exception;

    @Override
    public ChannelFuture newSucceededFuture() {
        return this.succeededFuture;
    }

    protected abstract SocketAddress localAddress0();

    @Override
    public ChannelFuture closeFuture() {
        return this.closeFuture;
    }

    protected abstract AbstractUnsafe newUnsafe();

    @Override
    public final ChannelPromise voidPromise() {
        return this.voidPromise;
    }

    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    protected abstract void doWrite(ChannelOutboundBuffer var1) throws Exception;

    protected AbstractChannel(Channel channel) {
        this.succeededFuture = new SucceededChannelFuture(this, null);
        this.voidPromise = new VoidChannelPromise(this, true);
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = channel;
        this.unsafe = this.newUnsafe();
        this.pipeline = new DefaultChannelPipeline(this);
    }

    @Override
    public ChannelFuture close() {
        return this.pipeline.close();
    }

    @Override
    public ChannelFuture deregister() {
        return this.pipeline.deregister();
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.config().getAllocator();
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress) {
        return this.pipeline.bind(socketAddress);
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return this.pipeline.connect(socketAddress, channelPromise);
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this);
    }

    public final int hashCode() {
        return (int)this.hashCode;
    }

    final MessageSizeEstimator.Handle estimatorHandle() {
        if (this.estimatorHandle == null) {
            this.estimatorHandle = this.config().getMessageSizeEstimator().newHandle();
        }
        return this.estimatorHandle;
    }

    @Override
    public ChannelFuture close(ChannelPromise channelPromise) {
        return this.pipeline.close(channelPromise);
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable throwable) {
        return new FailedChannelFuture(this, null, throwable);
    }

    protected abstract void doClose() throws Exception;

    protected abstract void doBeginRead() throws Exception;

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
        return this.pipeline.connect(socketAddress, socketAddress2, channelPromise);
    }

    @Override
    public ChannelFuture deregister(ChannelPromise channelPromise) {
        return this.pipeline.deregister(channelPromise);
    }

    protected Object filterOutboundMessage(Object object) throws Exception {
        return object;
    }

    @Override
    public ChannelFuture write(Object object, ChannelPromise channelPromise) {
        return this.pipeline.write(object, channelPromise);
    }

    static {
        logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
        CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
        NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
        CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    @Override
    public final int compareTo(Channel channel) {
        if (this == channel) {
            return 0;
        }
        long l = this.hashCode - (long)channel.hashCode();
        if (l > 0L) {
            return 1;
        }
        if (l < 0L) {
            return -1;
        }
        l = System.identityHashCode(this) - System.identityHashCode(channel);
        if (l != 0L) {
            return (int)l;
        }
        throw new Error();
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress) {
        return this.pipeline.connect(socketAddress);
    }

    static final class CloseFuture
    extends DefaultChannelPromise {
        @Override
        public ChannelPromise setFailure(Throwable throwable) {
            throw new IllegalStateException();
        }

        @Override
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }

        @Override
        public boolean tryFailure(Throwable throwable) {
            throw new IllegalStateException();
        }

        CloseFuture(AbstractChannel abstractChannel) {
            super(abstractChannel);
        }

        @Override
        public boolean trySuccess() {
            throw new IllegalStateException();
        }

        boolean setClosed() {
            return super.trySuccess();
        }
    }

    protected abstract class AbstractUnsafe
    implements Channel.Unsafe {
        private boolean inFlush0;
        private ChannelOutboundBuffer outboundBuffer;

        @Override
        public final ChannelOutboundBuffer outboundBuffer() {
            return this.outboundBuffer;
        }

        @Override
        public final void closeForcibly() {
            try {
                AbstractChannel.this.doClose();
            }
            catch (Exception exception) {
                logger.warn("Failed to close a channel.", exception);
            }
        }

        @Override
        public final void close(final ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable()) {
                return;
            }
            if (this.inFlush0) {
                this.invokeLater(new OneTimeTask(){

                    @Override
                    public void run() {
                        AbstractUnsafe.this.close(channelPromise);
                    }
                });
                return;
            }
            if (AbstractChannel.this.closeFuture.isDone()) {
                this.safeSetSuccess(channelPromise);
                return;
            }
            boolean bl = AbstractChannel.this.isActive();
            ChannelOutboundBuffer channelOutboundBuffer = this.outboundBuffer;
            this.outboundBuffer = null;
            try {
                AbstractChannel.this.doClose();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetSuccess(channelPromise);
            }
            catch (Throwable throwable) {
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(channelPromise, throwable);
            }
            channelOutboundBuffer.failFlushed(CLOSED_CHANNEL_EXCEPTION);
            channelOutboundBuffer.close(CLOSED_CHANNEL_EXCEPTION);
            if (bl && !AbstractChannel.this.isActive()) {
                this.invokeLater(new OneTimeTask(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelInactive();
                    }
                });
            }
            this.deregister(this.voidPromise());
        }

        @Override
        public final void deregister(ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable()) {
                return;
            }
            if (!AbstractChannel.this.registered) {
                this.safeSetSuccess(channelPromise);
                return;
            }
            try {
                AbstractChannel.this.doDeregister();
            }
            catch (Throwable throwable) {
                logger.warn("Unexpected exception occurred while deregistering a channel.", throwable);
                if (AbstractChannel.this.registered) {
                    AbstractChannel.this.registered = false;
                    this.invokeLater(new OneTimeTask(){

                        @Override
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelUnregistered();
                        }
                    });
                    this.safeSetSuccess(channelPromise);
                } else {
                    this.safeSetSuccess(channelPromise);
                }
            }
            if (AbstractChannel.this.registered) {
                AbstractChannel.this.registered = false;
                this.invokeLater(new /* invalid duplicate definition of identical inner class */);
                this.safeSetSuccess(channelPromise);
            } else {
                this.safeSetSuccess(channelPromise);
            }
        }

        protected final void safeSetSuccess(ChannelPromise channelPromise) {
            if (!(channelPromise instanceof VoidChannelPromise) && !channelPromise.trySuccess()) {
                logger.warn("Failed to mark a promise as success because it is done already: {}", (Object)channelPromise);
            }
        }

        protected void flush0() {
            if (this.inFlush0) {
                return;
            }
            ChannelOutboundBuffer channelOutboundBuffer = this.outboundBuffer;
            if (channelOutboundBuffer == null || channelOutboundBuffer.isEmpty()) {
                return;
            }
            this.inFlush0 = true;
            if (!AbstractChannel.this.isActive()) {
                if (AbstractChannel.this.isOpen()) {
                    channelOutboundBuffer.failFlushed(NOT_YET_CONNECTED_EXCEPTION);
                } else {
                    channelOutboundBuffer.failFlushed(CLOSED_CHANNEL_EXCEPTION);
                }
                this.inFlush0 = false;
                return;
            }
            try {
                AbstractChannel.this.doWrite(channelOutboundBuffer);
                this.inFlush0 = false;
            }
            catch (Throwable throwable) {
                channelOutboundBuffer.failFlushed(throwable);
                if (throwable instanceof IOException && AbstractChannel.this.config().isAutoClose()) {
                    this.close(this.voidPromise());
                }
                this.inFlush0 = false;
            }
        }

        @Override
        public final SocketAddress localAddress() {
            return AbstractChannel.this.localAddress0();
        }

        protected final void safeSetFailure(ChannelPromise channelPromise, Throwable throwable) {
            if (!(channelPromise instanceof VoidChannelPromise) && !channelPromise.tryFailure(throwable)) {
                logger.warn("Failed to mark a promise as failure because it's done already: {}", (Object)channelPromise, (Object)throwable);
            }
        }

        protected final void closeIfClosed() {
            if (AbstractChannel.this.isOpen()) {
                return;
            }
            this.close(this.voidPromise());
        }

        private void register0(ChannelPromise channelPromise) {
            try {
                if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                    return;
                }
                AbstractChannel.this.doRegister();
                AbstractChannel.this.registered = true;
                this.safeSetSuccess(channelPromise);
                AbstractChannel.this.pipeline.fireChannelRegistered();
                if (AbstractChannel.this.isActive()) {
                    AbstractChannel.this.pipeline.fireChannelActive();
                }
            }
            catch (Throwable throwable) {
                this.closeForcibly();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(channelPromise, throwable);
            }
        }

        @Override
        public final void register(EventLoop eventLoop, final ChannelPromise channelPromise) {
            if (eventLoop == null) {
                throw new NullPointerException("eventLoop");
            }
            if (AbstractChannel.this.isRegistered()) {
                channelPromise.setFailure(new IllegalStateException("registered to an event loop already"));
                return;
            }
            if (!AbstractChannel.this.isCompatible(eventLoop)) {
                channelPromise.setFailure(new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
                return;
            }
            AbstractChannel.this.eventLoop = eventLoop;
            if (eventLoop.inEventLoop()) {
                this.register0(channelPromise);
            } else {
                try {
                    eventLoop.execute(new OneTimeTask(){

                        @Override
                        public void run() {
                            AbstractUnsafe.this.register0(channelPromise);
                        }
                    });
                }
                catch (Throwable throwable) {
                    logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", (Object)AbstractChannel.this, (Object)throwable);
                    this.closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    this.safeSetFailure(channelPromise, throwable);
                }
            }
        }

        @Override
        public final void flush() {
            ChannelOutboundBuffer channelOutboundBuffer = this.outboundBuffer;
            if (channelOutboundBuffer == null) {
                return;
            }
            channelOutboundBuffer.addFlush();
            this.flush0();
        }

        private void invokeLater(Runnable runnable) {
            try {
                AbstractChannel.this.eventLoop().execute(runnable);
            }
            catch (RejectedExecutionException rejectedExecutionException) {
                logger.warn("Can't invoke task later as EventLoop rejected it", rejectedExecutionException);
            }
        }

        @Override
        public final SocketAddress remoteAddress() {
            return AbstractChannel.this.remoteAddress0();
        }

        @Override
        public final ChannelPromise voidPromise() {
            return AbstractChannel.this.unsafeVoidPromise;
        }

        @Override
        public final void bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                return;
            }
            if (!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && socketAddress instanceof InetSocketAddress && !((InetSocketAddress)socketAddress).getAddress().isAnyLocalAddress()) {
                logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + socketAddress + ") anyway as requested.");
            }
            boolean bl = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doBind(socketAddress);
            }
            catch (Throwable throwable) {
                this.safeSetFailure(channelPromise, throwable);
                this.closeIfClosed();
                return;
            }
            if (!bl && AbstractChannel.this.isActive()) {
                this.invokeLater(new OneTimeTask(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                });
            }
            this.safeSetSuccess(channelPromise);
        }

        @Override
        public final void disconnect(ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable()) {
                return;
            }
            boolean bl = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doDisconnect();
            }
            catch (Throwable throwable) {
                this.safeSetFailure(channelPromise, throwable);
                this.closeIfClosed();
                return;
            }
            if (bl && !AbstractChannel.this.isActive()) {
                this.invokeLater(new OneTimeTask(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelInactive();
                    }
                });
            }
            this.safeSetSuccess(channelPromise);
            this.closeIfClosed();
        }

        protected final boolean ensureOpen(ChannelPromise channelPromise) {
            if (AbstractChannel.this.isOpen()) {
                return true;
            }
            this.safeSetFailure(channelPromise, CLOSED_CHANNEL_EXCEPTION);
            return false;
        }

        @Override
        public final void beginRead() {
            if (!AbstractChannel.this.isActive()) {
                return;
            }
            try {
                AbstractChannel.this.doBeginRead();
            }
            catch (Exception exception) {
                this.invokeLater(new OneTimeTask(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireExceptionCaught(exception);
                    }
                });
                this.close(this.voidPromise());
            }
        }

        @Override
        public final void write(Object object, ChannelPromise channelPromise) {
            int n;
            ChannelOutboundBuffer channelOutboundBuffer = this.outboundBuffer;
            if (channelOutboundBuffer == null) {
                this.safeSetFailure(channelPromise, CLOSED_CHANNEL_EXCEPTION);
                ReferenceCountUtil.release(object);
                return;
            }
            try {
                object = AbstractChannel.this.filterOutboundMessage(object);
                n = AbstractChannel.this.estimatorHandle().size(object);
                if (n < 0) {
                    n = 0;
                }
            }
            catch (Throwable throwable) {
                this.safeSetFailure(channelPromise, throwable);
                ReferenceCountUtil.release(object);
                return;
            }
            channelOutboundBuffer.addMessage(object, n, channelPromise);
        }

        protected AbstractUnsafe() {
            this.outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
        }
    }
}

