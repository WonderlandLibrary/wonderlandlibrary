/*
 * Decompiled with CFR 0.150.
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
import io.netty.channel.FileRegion;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractChannel
extends DefaultAttributeMap
implements Channel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
    static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
    static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
    private MessageSizeEstimator.Handle estimatorHandle;
    private final Channel parent;
    private final long hashCode = ThreadLocalRandom.current().nextLong();
    private final Channel.Unsafe unsafe;
    private final DefaultChannelPipeline pipeline;
    private final ChannelFuture succeededFuture = new SucceededChannelFuture(this, null);
    private final VoidChannelPromise voidPromise = new VoidChannelPromise(this, true);
    private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
    private final CloseFuture closeFuture = new CloseFuture(this);
    private volatile SocketAddress localAddress;
    private volatile SocketAddress remoteAddress;
    private volatile EventLoop eventLoop;
    private volatile boolean registered;
    private boolean strValActive;
    private String strVal;

    protected AbstractChannel(Channel parent) {
        this.parent = parent;
        this.unsafe = this.newUnsafe();
        this.pipeline = new DefaultChannelPipeline(this);
    }

    @Override
    public boolean isWritable() {
        ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return buf != null && buf.getWritable();
    }

    @Override
    public Channel parent() {
        return this.parent;
    }

    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public ByteBufAllocator alloc() {
        return this.config().getAllocator();
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
    public SocketAddress localAddress() {
        SocketAddress localAddress = this.localAddress;
        if (localAddress == null) {
            try {
                this.localAddress = localAddress = this.unsafe().localAddress();
            }
            catch (Throwable t) {
                return null;
            }
        }
        return localAddress;
    }

    protected void invalidateLocalAddress() {
        this.localAddress = null;
    }

    @Override
    public SocketAddress remoteAddress() {
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            try {
                this.remoteAddress = remoteAddress = this.unsafe().remoteAddress();
            }
            catch (Throwable t) {
                return null;
            }
        }
        return remoteAddress;
    }

    protected void invalidateRemoteAddress() {
        this.remoteAddress = null;
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
        return this.pipeline.bind(localAddress);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress) {
        return this.pipeline.connect(remoteAddress);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return this.pipeline.connect(remoteAddress, localAddress);
    }

    @Override
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
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
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.bind(localAddress, promise);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, promise);
    }

    @Override
    public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public ChannelFuture disconnect(ChannelPromise promise) {
        return this.pipeline.disconnect(promise);
    }

    @Override
    public ChannelFuture close(ChannelPromise promise) {
        return this.pipeline.close(promise);
    }

    @Override
    public ChannelFuture deregister(ChannelPromise promise) {
        return this.pipeline.deregister(promise);
    }

    @Override
    public Channel read() {
        this.pipeline.read();
        return this;
    }

    @Override
    public ChannelFuture write(Object msg) {
        return this.pipeline.write(msg);
    }

    @Override
    public ChannelFuture write(Object msg, ChannelPromise promise) {
        return this.pipeline.write(msg, promise);
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg) {
        return this.pipeline.writeAndFlush(msg);
    }

    @Override
    public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
        return this.pipeline.writeAndFlush(msg, promise);
    }

    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this);
    }

    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this);
    }

    @Override
    public ChannelFuture newSucceededFuture() {
        return this.succeededFuture;
    }

    @Override
    public ChannelFuture newFailedFuture(Throwable cause) {
        return new FailedChannelFuture(this, null, cause);
    }

    @Override
    public ChannelFuture closeFuture() {
        return this.closeFuture;
    }

    @Override
    public Channel.Unsafe unsafe() {
        return this.unsafe;
    }

    protected abstract AbstractUnsafe newUnsafe();

    public final int hashCode() {
        return (int)this.hashCode;
    }

    public final boolean equals(Object o) {
        return this == o;
    }

    @Override
    public final int compareTo(Channel o) {
        if (this == o) {
            return 0;
        }
        long ret = this.hashCode - (long)o.hashCode();
        if (ret > 0L) {
            return 1;
        }
        if (ret < 0L) {
            return -1;
        }
        ret = System.identityHashCode(this) - System.identityHashCode(o);
        if (ret != 0L) {
            return (int)ret;
        }
        throw new Error();
    }

    public String toString() {
        boolean active = this.isActive();
        if (this.strValActive == active && this.strVal != null) {
            return this.strVal;
        }
        SocketAddress remoteAddr = this.remoteAddress();
        SocketAddress localAddr = this.localAddress();
        if (remoteAddr != null) {
            SocketAddress dstAddr;
            SocketAddress srcAddr;
            if (this.parent == null) {
                srcAddr = localAddr;
                dstAddr = remoteAddr;
            } else {
                srcAddr = remoteAddr;
                dstAddr = localAddr;
            }
            this.strVal = String.format("[id: 0x%08x, %s %s %s]", (int)this.hashCode, srcAddr, active ? "=>" : ":>", dstAddr);
        } else {
            this.strVal = localAddr != null ? String.format("[id: 0x%08x, %s]", (int)this.hashCode, localAddr) : String.format("[id: 0x%08x]", (int)this.hashCode);
        }
        this.strValActive = active;
        return this.strVal;
    }

    @Override
    public final ChannelPromise voidPromise() {
        return this.voidPromise;
    }

    final MessageSizeEstimator.Handle estimatorHandle() {
        if (this.estimatorHandle == null) {
            this.estimatorHandle = this.config().getMessageSizeEstimator().newHandle();
        }
        return this.estimatorHandle;
    }

    protected abstract boolean isCompatible(EventLoop var1);

    protected abstract SocketAddress localAddress0();

    protected abstract SocketAddress remoteAddress0();

    protected void doRegister() throws Exception {
    }

    protected abstract void doBind(SocketAddress var1) throws Exception;

    protected abstract void doDisconnect() throws Exception;

    protected abstract void doClose() throws Exception;

    protected void doDeregister() throws Exception {
    }

    protected abstract void doBeginRead() throws Exception;

    protected abstract void doWrite(ChannelOutboundBuffer var1) throws Exception;

    protected static void checkEOF(FileRegion region) throws IOException {
        if (region.transfered() < region.count()) {
            throw new EOFException("Expected to be able to write " + region.count() + " bytes, but only wrote " + region.transfered());
        }
    }

    static {
        CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    static final class CloseFuture
    extends DefaultChannelPromise {
        CloseFuture(AbstractChannel ch) {
            super(ch);
        }

        @Override
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }

        @Override
        public ChannelPromise setFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        @Override
        public boolean trySuccess() {
            throw new IllegalStateException();
        }

        @Override
        public boolean tryFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        boolean setClosed() {
            return super.trySuccess();
        }
    }

    protected abstract class AbstractUnsafe
    implements Channel.Unsafe {
        private ChannelOutboundBuffer outboundBuffer;
        private boolean inFlush0;

        protected AbstractUnsafe() {
            this.outboundBuffer = ChannelOutboundBuffer.newInstance(AbstractChannel.this);
        }

        @Override
        public final ChannelOutboundBuffer outboundBuffer() {
            return this.outboundBuffer;
        }

        @Override
        public final SocketAddress localAddress() {
            return AbstractChannel.this.localAddress0();
        }

        @Override
        public final SocketAddress remoteAddress() {
            return AbstractChannel.this.remoteAddress0();
        }

        @Override
        public final void register(EventLoop eventLoop, final ChannelPromise promise) {
            if (eventLoop == null) {
                throw new NullPointerException("eventLoop");
            }
            if (AbstractChannel.this.isRegistered()) {
                promise.setFailure(new IllegalStateException("registered to an event loop already"));
                return;
            }
            if (!AbstractChannel.this.isCompatible(eventLoop)) {
                promise.setFailure(new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
                return;
            }
            AbstractChannel.this.eventLoop = eventLoop;
            if (eventLoop.inEventLoop()) {
                this.register0(promise);
            } else {
                try {
                    eventLoop.execute(new Runnable(){

                        @Override
                        public void run() {
                            AbstractUnsafe.this.register0(promise);
                        }
                    });
                }
                catch (Throwable t) {
                    logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", (Object)AbstractChannel.this, (Object)t);
                    this.closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    promise.setFailure(t);
                }
            }
        }

        private void register0(ChannelPromise promise) {
            block4: {
                try {
                    if (!this.ensureOpen(promise)) {
                        return;
                    }
                    AbstractChannel.this.doRegister();
                    AbstractChannel.this.registered = true;
                    promise.setSuccess();
                    AbstractChannel.this.pipeline.fireChannelRegistered();
                    if (AbstractChannel.this.isActive()) {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                }
                catch (Throwable t) {
                    this.closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    if (promise.tryFailure(t)) break block4;
                    logger.warn("Tried to fail the registration promise, but it is complete already. Swallowing the cause of the registration failure:", t);
                }
            }
        }

        @Override
        public final void bind(SocketAddress localAddress, ChannelPromise promise) {
            if (!this.ensureOpen(promise)) {
                return;
            }
            if (!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && localAddress instanceof InetSocketAddress && !((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress()) {
                logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
            }
            boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doBind(localAddress);
            }
            catch (Throwable t) {
                promise.setFailure(t);
                this.closeIfClosed();
                return;
            }
            promise.setSuccess();
            if (!wasActive && AbstractChannel.this.isActive()) {
                this.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                });
            }
        }

        @Override
        public final void disconnect(ChannelPromise promise) {
            boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doDisconnect();
            }
            catch (Throwable t) {
                promise.setFailure(t);
                this.closeIfClosed();
                return;
            }
            promise.setSuccess();
            if (wasActive && !AbstractChannel.this.isActive()) {
                this.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelInactive();
                    }
                });
            }
            this.closeIfClosed();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public final void close(final ChannelPromise promise) {
            if (this.inFlush0) {
                this.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        AbstractUnsafe.this.close(promise);
                    }
                });
                return;
            }
            if (AbstractChannel.this.closeFuture.isDone()) {
                promise.setSuccess();
                return;
            }
            boolean wasActive = AbstractChannel.this.isActive();
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            this.outboundBuffer = null;
            try {
                AbstractChannel.this.doClose();
                AbstractChannel.this.closeFuture.setClosed();
                promise.setSuccess();
            }
            catch (Throwable t) {
                AbstractChannel.this.closeFuture.setClosed();
                promise.setFailure(t);
            }
            try {
                outboundBuffer.failFlushed(CLOSED_CHANNEL_EXCEPTION);
                outboundBuffer.close(CLOSED_CHANNEL_EXCEPTION);
            }
            catch (Throwable throwable) {
                if (wasActive && !AbstractChannel.this.isActive()) {
                    this.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            AbstractChannel.this.pipeline.fireChannelInactive();
                        }
                    });
                }
                this.deregister(this.voidPromise());
                throw throwable;
            }
            if (wasActive && !AbstractChannel.this.isActive()) {
                this.invokeLater(new /* invalid duplicate definition of identical inner class */);
            }
            this.deregister(this.voidPromise());
        }

        @Override
        public final void closeForcibly() {
            try {
                AbstractChannel.this.doClose();
            }
            catch (Exception e) {
                logger.warn("Failed to close a channel.", e);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public final void deregister(ChannelPromise promise) {
            if (!AbstractChannel.this.registered) {
                promise.setSuccess();
                return;
            }
            try {
                AbstractChannel.this.doDeregister();
            }
            catch (Throwable t) {
                try {
                    logger.warn("Unexpected exception occurred while deregistering a channel.", t);
                }
                catch (Throwable throwable) {
                    if (AbstractChannel.this.registered) {
                        AbstractChannel.this.registered = false;
                        promise.setSuccess();
                        this.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                AbstractChannel.this.pipeline.fireChannelUnregistered();
                            }
                        });
                    } else {
                        promise.setSuccess();
                    }
                    throw throwable;
                }
                if (AbstractChannel.this.registered) {
                    AbstractChannel.this.registered = false;
                    promise.setSuccess();
                    this.invokeLater(new /* invalid duplicate definition of identical inner class */);
                } else {
                    promise.setSuccess();
                }
            }
            if (AbstractChannel.this.registered) {
                AbstractChannel.this.registered = false;
                promise.setSuccess();
                this.invokeLater(new /* invalid duplicate definition of identical inner class */);
            } else {
                promise.setSuccess();
            }
        }

        @Override
        public void beginRead() {
            if (!AbstractChannel.this.isActive()) {
                return;
            }
            try {
                AbstractChannel.this.doBeginRead();
            }
            catch (Exception e) {
                this.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireExceptionCaught(e);
                    }
                });
                this.close(this.voidPromise());
            }
        }

        @Override
        public void write(Object msg, ChannelPromise promise) {
            if (!AbstractChannel.this.isActive()) {
                if (AbstractChannel.this.isOpen()) {
                    promise.tryFailure(NOT_YET_CONNECTED_EXCEPTION);
                } else {
                    promise.tryFailure(CLOSED_CHANNEL_EXCEPTION);
                }
                ReferenceCountUtil.release(msg);
            } else {
                this.outboundBuffer.addMessage(msg, promise);
            }
        }

        @Override
        public void flush() {
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                return;
            }
            outboundBuffer.addFlush();
            this.flush0();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        protected void flush0() {
            if (this.inFlush0) {
                return;
            }
            ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null || outboundBuffer.isEmpty()) {
                return;
            }
            this.inFlush0 = true;
            if (!AbstractChannel.this.isActive()) {
                try {
                    if (AbstractChannel.this.isOpen()) {
                        outboundBuffer.failFlushed(NOT_YET_CONNECTED_EXCEPTION);
                    } else {
                        outboundBuffer.failFlushed(CLOSED_CHANNEL_EXCEPTION);
                    }
                }
                finally {
                    this.inFlush0 = false;
                }
                return;
            }
            try {
                AbstractChannel.this.doWrite(outboundBuffer);
            }
            catch (Throwable t) {
                outboundBuffer.failFlushed(t);
                if (t instanceof IOException && AbstractChannel.this.config().isAutoClose()) {
                    this.close(this.voidPromise());
                }
            }
            finally {
                this.inFlush0 = false;
            }
        }

        @Override
        public ChannelPromise voidPromise() {
            return AbstractChannel.this.unsafeVoidPromise;
        }

        protected final boolean ensureOpen(ChannelPromise promise) {
            if (AbstractChannel.this.isOpen()) {
                return true;
            }
            promise.setFailure(CLOSED_CHANNEL_EXCEPTION);
            return false;
        }

        protected final void closeIfClosed() {
            if (AbstractChannel.this.isOpen()) {
                return;
            }
            this.close(this.voidPromise());
        }

        private void invokeLater(Runnable task) {
            try {
                AbstractChannel.this.eventLoop().execute(task);
            }
            catch (RejectedExecutionException e) {
                logger.warn("Can't invoke task later as EventLoop rejected it", e);
            }
        }
    }
}

