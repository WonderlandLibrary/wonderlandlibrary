/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.group;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ServerChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.CombinedIterator;
import io.netty.channel.group.DefaultChannelGroupFuture;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.ConcurrentSet;
import io.netty.util.internal.StringUtil;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultChannelGroup
extends AbstractSet<Channel>
implements ChannelGroup {
    private final ConcurrentSet<Channel> serverChannels = new ConcurrentSet();
    private final ConcurrentSet<Channel> nonServerChannels = new ConcurrentSet();
    private final ChannelFutureListener remover = new ChannelFutureListener(){

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            DefaultChannelGroup.this.remove(channelFuture.channel());
        }
    };
    private final String name;
    private final EventExecutor executor;
    private static final AtomicInteger nextId = new AtomicInteger();

    @Override
    public boolean add(Channel channel) {
        ConcurrentSet<Channel> concurrentSet = channel instanceof ServerChannel ? this.serverChannels : this.nonServerChannels;
        boolean bl = concurrentSet.add(channel);
        if (bl) {
            channel.closeFuture().addListener(this.remover);
        }
        return bl;
    }

    @Override
    public ChannelGroupFuture write(Object object, ChannelMatcher channelMatcher) {
        if (object == null) {
            throw new NullPointerException("message");
        }
        if (channelMatcher == null) {
            throw new NullPointerException("matcher");
        }
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.write(DefaultChannelGroup.safeDuplicate(object)));
        }
        ReferenceCountUtil.release(object);
        return new DefaultChannelGroupFuture((ChannelGroup)this, linkedHashMap, this.executor);
    }

    @Override
    public Iterator<Channel> iterator() {
        return new CombinedIterator<Channel>(this.serverChannels.iterator(), this.nonServerChannels.iterator());
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public ChannelGroupFuture flushAndWrite(Object object, ChannelMatcher channelMatcher) {
        return this.writeAndFlush(object, channelMatcher);
    }

    @Override
    public ChannelGroupFuture close(ChannelMatcher channelMatcher) {
        if (channelMatcher == null) {
            throw new NullPointerException("matcher");
        }
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (Channel channel : this.serverChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.close());
        }
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.close());
        }
        return new DefaultChannelGroupFuture((ChannelGroup)this, linkedHashMap, this.executor);
    }

    private static Object safeDuplicate(Object object) {
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).duplicate().retain();
        }
        if (object instanceof ByteBufHolder) {
            return ((ByteBufHolder)object).duplicate().retain();
        }
        return ReferenceCountUtil.retain(object);
    }

    @Override
    public boolean contains(Object object) {
        if (object instanceof Channel) {
            Channel channel = (Channel)object;
            if (object instanceof ServerChannel) {
                return this.serverChannels.contains(channel);
            }
            return this.nonServerChannels.contains(channel);
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        ArrayList<Channel> arrayList = new ArrayList<Channel>(this.size());
        arrayList.addAll(this.serverChannels);
        arrayList.addAll(this.nonServerChannels);
        return arrayList.toArray();
    }

    @Override
    public ChannelGroupFuture close() {
        return this.close(ChannelMatchers.all());
    }

    @Override
    public ChannelGroupFuture disconnect() {
        return this.disconnect(ChannelMatchers.all());
    }

    @Override
    public <T> T[] toArray(T[] TArray) {
        ArrayList<Channel> arrayList = new ArrayList<Channel>(this.size());
        arrayList.addAll(this.serverChannels);
        arrayList.addAll(this.nonServerChannels);
        return arrayList.toArray(TArray);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(name: " + this.name() + ", size: " + this.size() + ')';
    }

    @Override
    public int size() {
        return this.nonServerChannels.size() + this.serverChannels.size();
    }

    @Override
    public ChannelGroupFuture write(Object object) {
        return this.write(object, ChannelMatchers.all());
    }

    @Override
    public ChannelGroupFuture flushAndWrite(Object object) {
        return this.writeAndFlush(object);
    }

    @Override
    public ChannelGroup flush() {
        return this.flush(ChannelMatchers.all());
    }

    @Override
    public ChannelGroupFuture writeAndFlush(Object object, ChannelMatcher channelMatcher) {
        if (object == null) {
            throw new NullPointerException("message");
        }
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.writeAndFlush(DefaultChannelGroup.safeDuplicate(object)));
        }
        ReferenceCountUtil.release(object);
        return new DefaultChannelGroupFuture((ChannelGroup)this, linkedHashMap, this.executor);
    }

    @Override
    public ChannelGroupFuture writeAndFlush(Object object) {
        return this.writeAndFlush(object, ChannelMatchers.all());
    }

    @Override
    public ChannelGroupFuture deregister(ChannelMatcher channelMatcher) {
        if (channelMatcher == null) {
            throw new NullPointerException("matcher");
        }
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (Channel channel : this.serverChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.deregister());
        }
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.deregister());
        }
        return new DefaultChannelGroupFuture((ChannelGroup)this, linkedHashMap, this.executor);
    }

    @Override
    public ChannelGroupFuture deregister() {
        return this.deregister(ChannelMatchers.all());
    }

    @Override
    public boolean remove(Object object) {
        if (!(object instanceof Channel)) {
            return false;
        }
        Channel channel = (Channel)object;
        boolean bl = channel instanceof ServerChannel ? this.serverChannels.remove(channel) : this.nonServerChannels.remove(channel);
        if (!bl) {
            return false;
        }
        channel.closeFuture().removeListener(this.remover);
        return true;
    }

    @Override
    public void clear() {
        this.nonServerChannels.clear();
        this.serverChannels.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty();
    }

    public DefaultChannelGroup(EventExecutor eventExecutor) {
        this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), eventExecutor);
    }

    @Override
    public int compareTo(ChannelGroup channelGroup) {
        int n = this.name().compareTo(channelGroup.name());
        if (n != 0) {
            return n;
        }
        return System.identityHashCode(this) - System.identityHashCode(channelGroup);
    }

    public DefaultChannelGroup(String string, EventExecutor eventExecutor) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        this.name = string;
        this.executor = eventExecutor;
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public ChannelGroup flush(ChannelMatcher channelMatcher) {
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            channel.flush();
        }
        return this;
    }

    @Override
    public ChannelGroupFuture disconnect(ChannelMatcher channelMatcher) {
        if (channelMatcher == null) {
            throw new NullPointerException("matcher");
        }
        LinkedHashMap<Channel, ChannelFuture> linkedHashMap = new LinkedHashMap<Channel, ChannelFuture>(this.size());
        for (Channel channel : this.serverChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.disconnect());
        }
        for (Channel channel : this.nonServerChannels) {
            if (!channelMatcher.matches(channel)) continue;
            linkedHashMap.put(channel, channel.disconnect());
        }
        return new DefaultChannelGroupFuture((ChannelGroup)this, linkedHashMap, this.executor);
    }
}

