/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupException;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

final class DefaultChannelGroupFuture
extends DefaultPromise<Void>
implements ChannelGroupFuture {
    private int successCount;
    private final Map<Channel, ChannelFuture> futures;
    private final ChannelFutureListener childListener = new ChannelFutureListener(){

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            boolean bl;
            boolean bl2 = channelFuture.isSuccess();
            Iterable<ChannelFuture> iterable = DefaultChannelGroupFuture.this;
            synchronized (iterable) {
                if (bl2) {
                    DefaultChannelGroupFuture.this.successCount++;
                } else {
                    DefaultChannelGroupFuture.this.failureCount++;
                }
                boolean bl3 = bl = DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size();
                assert (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount <= DefaultChannelGroupFuture.this.futures.size());
            }
            if (!bl) return;
            if (DefaultChannelGroupFuture.this.failureCount > 0) {
                iterable = new ArrayList(DefaultChannelGroupFuture.this.failureCount);
                for (ChannelFuture channelFuture2 : DefaultChannelGroupFuture.this.futures.values()) {
                    if (channelFuture2.isSuccess()) continue;
                    iterable.add(new DefaultEntry<Channel, Throwable>(channelFuture2.channel(), channelFuture2.cause()));
                }
                DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException((Collection<Map.Entry<Channel, Throwable>>)iterable));
                return;
            } else {
                DefaultChannelGroupFuture.this.setSuccess0();
            }
        }
    };
    private final ChannelGroup group;
    private int failureCount;

    @Override
    public DefaultChannelGroupFuture awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override
    public Iterator<ChannelFuture> iterator() {
        return this.futures.values().iterator();
    }

    @Override
    public DefaultChannelGroupFuture syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    DefaultChannelGroupFuture(ChannelGroup channelGroup, Collection<ChannelFuture> collection, EventExecutor eventExecutor) {
        super(eventExecutor);
        if (channelGroup == null) {
            throw new NullPointerException("group");
        }
        if (collection == null) {
            throw new NullPointerException("futures");
        }
        this.group = channelGroup;
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>();
        for (ChannelFuture channelFuture : collection) {
            linkedHashMap.put(channelFuture.channel(), channelFuture);
        }
        this.futures = Collections.unmodifiableMap(linkedHashMap);
        for (ChannelFuture channelFuture : this.futures.values()) {
            channelFuture.addListener(this.childListener);
        }
        if (this.futures.isEmpty()) {
            this.setSuccess0();
        }
    }

    @Override
    public synchronized boolean isPartialSuccess() {
        return this.successCount != 0 && this.successCount != this.futures.size();
    }

    @Override
    public boolean tryFailure(Throwable throwable) {
        throw new IllegalStateException();
    }

    DefaultChannelGroupFuture(ChannelGroup channelGroup, Map<Channel, ChannelFuture> map, EventExecutor eventExecutor) {
        super(eventExecutor);
        this.group = channelGroup;
        this.futures = Collections.unmodifiableMap(map);
        for (ChannelFuture channelFuture : this.futures.values()) {
            channelFuture.addListener(this.childListener);
        }
        if (this.futures.isEmpty()) {
            this.setSuccess0();
        }
    }

    @Override
    public boolean trySuccess(Void void_) {
        throw new IllegalStateException();
    }

    @Override
    public DefaultChannelGroupFuture removeListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        super.removeListener(genericFutureListener);
        return this;
    }

    @Override
    public DefaultChannelGroupFuture addListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        super.addListeners(genericFutureListenerArray);
        return this;
    }

    @Override
    protected void checkDeadLock() {
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor != null && eventExecutor != ImmediateEventExecutor.INSTANCE && eventExecutor.inEventLoop()) {
            throw new BlockingOperationException();
        }
    }

    @Override
    public ChannelGroup group() {
        return this.group;
    }

    private void setSuccess0() {
        super.setSuccess(null);
    }

    public DefaultChannelGroupFuture setSuccess(Void void_) {
        throw new IllegalStateException();
    }

    public DefaultChannelGroupFuture setFailure(Throwable throwable) {
        throw new IllegalStateException();
    }

    private void setFailure0(ChannelGroupException channelGroupException) {
        super.setFailure(channelGroupException);
    }

    @Override
    public ChannelFuture find(Channel channel) {
        return this.futures.get(channel);
    }

    @Override
    public DefaultChannelGroupFuture removeListeners(GenericFutureListener<? extends Future<? super Void>> ... genericFutureListenerArray) {
        super.removeListeners(genericFutureListenerArray);
        return this;
    }

    @Override
    public synchronized boolean isPartialFailure() {
        return this.failureCount != 0 && this.failureCount != this.futures.size();
    }

    @Override
    public DefaultChannelGroupFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public DefaultChannelGroupFuture await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public DefaultChannelGroupFuture addListener(GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        super.addListener(genericFutureListener);
        return this;
    }

    @Override
    public ChannelGroupException cause() {
        return (ChannelGroupException)super.cause();
    }

    private static final class DefaultEntry<K, V>
    implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        DefaultEntry(K k, V v) {
            this.key = k;
            this.value = v;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException("read-only");
        }
    }
}

