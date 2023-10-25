/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.AbstractChannel;
import io.netty.channel.AbstractChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;

final class DefaultChannelPipeline
implements ChannelPipeline {
    private static final WeakHashMap<Class<?>, String>[] nameCaches;
    final Map<EventExecutorGroup, EventExecutor> childExecutors;
    final AbstractChannelHandlerContext tail;
    final AbstractChannelHandlerContext head;
    static final InternalLogger logger;
    final AbstractChannel channel;
    private final Map<String, AbstractChannelHandlerContext> name2ctx = new HashMap<String, AbstractChannelHandlerContext>(4);

    public DefaultChannelPipeline(AbstractChannel abstractChannel) {
        this.childExecutors = new IdentityHashMap<EventExecutorGroup, EventExecutor>();
        if (abstractChannel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = abstractChannel;
        this.tail = new TailContext(this);
        this.head = new HeadContext(this);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public ChannelHandler removeLast() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.tail.prev).handler();
    }

    void remove0(AbstractChannelHandlerContext abstractChannelHandlerContext) {
        AbstractChannelHandlerContext abstractChannelHandlerContext2;
        AbstractChannelHandlerContext abstractChannelHandlerContext3 = abstractChannelHandlerContext.prev;
        abstractChannelHandlerContext3.next = abstractChannelHandlerContext2 = abstractChannelHandlerContext.next;
        abstractChannelHandlerContext2.prev = abstractChannelHandlerContext3;
        this.name2ctx.remove(abstractChannelHandlerContext.name());
        this.callHandlerRemoved(abstractChannelHandlerContext);
    }

    private static void checkMultiplicity(ChannelHandlerContext channelHandlerContext) {
        ChannelHandler channelHandler = channelHandlerContext.handler();
        if (channelHandler instanceof ChannelHandlerAdapter) {
            ChannelHandlerAdapter channelHandlerAdapter = (ChannelHandlerAdapter)channelHandler;
            if (!channelHandlerAdapter.isSharable() && channelHandlerAdapter.added) {
                throw new ChannelPipelineException(channelHandlerAdapter.getClass().getName() + " is not a @Sharable handler, so can't be added or removed multiple times.");
            }
            channelHandlerAdapter.added = true;
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return this.tail.connect(socketAddress, channelPromise);
    }

    private void replace0(AbstractChannelHandlerContext abstractChannelHandlerContext, String string, AbstractChannelHandlerContext abstractChannelHandlerContext2) {
        DefaultChannelPipeline.checkMultiplicity(abstractChannelHandlerContext2);
        AbstractChannelHandlerContext abstractChannelHandlerContext3 = abstractChannelHandlerContext.prev;
        AbstractChannelHandlerContext abstractChannelHandlerContext4 = abstractChannelHandlerContext.next;
        abstractChannelHandlerContext2.prev = abstractChannelHandlerContext3;
        abstractChannelHandlerContext2.next = abstractChannelHandlerContext4;
        abstractChannelHandlerContext3.next = abstractChannelHandlerContext2;
        abstractChannelHandlerContext4.prev = abstractChannelHandlerContext2;
        if (!abstractChannelHandlerContext.name().equals(string)) {
            this.name2ctx.remove(abstractChannelHandlerContext.name());
        }
        this.name2ctx.put(string, abstractChannelHandlerContext2);
        abstractChannelHandlerContext.prev = abstractChannelHandlerContext2;
        abstractChannelHandlerContext.next = abstractChannelHandlerContext2;
        this.callHandlerAdded(abstractChannelHandlerContext2);
        this.callHandlerRemoved(abstractChannelHandlerContext);
    }

    @Override
    public ChannelPipeline read() {
        this.tail.read();
        return this;
    }

    @Override
    public ChannelPipeline remove(ChannelHandler channelHandler) {
        this.remove(this.getContextOrDie(channelHandler));
        return this;
    }

    private AbstractChannelHandlerContext getContextOrDie(String string) {
        AbstractChannelHandlerContext abstractChannelHandlerContext = (AbstractChannelHandlerContext)this.context(string);
        if (abstractChannelHandlerContext == null) {
            throw new NoSuchElementException(string);
        }
        return abstractChannelHandlerContext;
    }

    @Override
    public ChannelFuture write(Object object, ChannelPromise channelPromise) {
        return this.tail.write(object, channelPromise);
    }

    @Override
    public ChannelHandler first() {
        ChannelHandlerContext channelHandlerContext = this.firstContext();
        if (channelHandlerContext == null) {
            return null;
        }
        return channelHandlerContext.handler();
    }

    @Override
    public ChannelHandler removeFirst() {
        if (this.head.next == this.tail) {
            throw new NoSuchElementException();
        }
        return this.remove(this.head.next).handler();
    }

    @Override
    public ChannelFuture disconnect() {
        return this.tail.disconnect();
    }

    @Override
    public Iterator<Map.Entry<String, ChannelHandler>> iterator() {
        return this.toMap().entrySet().iterator();
    }

    private ChannelHandler replace(final AbstractChannelHandlerContext abstractChannelHandlerContext, final String string, ChannelHandler channelHandler) {
        Future<?> future;
        assert (abstractChannelHandlerContext != this.head && abstractChannelHandlerContext != this.tail);
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            DefaultChannelHandlerContext defaultChannelHandlerContext;
            boolean bl = abstractChannelHandlerContext.name().equals(string);
            if (!bl) {
                this.checkDuplicateName(string);
            }
            if (!(defaultChannelHandlerContext = new DefaultChannelHandlerContext(this, abstractChannelHandlerContext.executor, string, channelHandler)).channel().isRegistered() || defaultChannelHandlerContext.executor().inEventLoop()) {
                this.replace0(abstractChannelHandlerContext, string, defaultChannelHandlerContext);
                return abstractChannelHandlerContext.handler();
            }
            future = defaultChannelHandlerContext.executor().submit(new Runnable(){

                @Override
                public void run() {
                    DefaultChannelPipeline defaultChannelPipeline = DefaultChannelPipeline.this;
                    synchronized (defaultChannelPipeline) {
                        DefaultChannelPipeline.this.replace0(abstractChannelHandlerContext, string, defaultChannelHandlerContext);
                    }
                }
            });
        }
        DefaultChannelPipeline.waitForFuture(future);
        return abstractChannelHandlerContext.handler();
    }

    @Override
    public <T extends ChannelHandler> T remove(Class<T> clazz) {
        return (T)this.remove(this.getContextOrDie(clazz)).handler();
    }

    private void addFirst0(String string, AbstractChannelHandlerContext abstractChannelHandlerContext) {
        DefaultChannelPipeline.checkMultiplicity(abstractChannelHandlerContext);
        AbstractChannelHandlerContext abstractChannelHandlerContext2 = this.head.next;
        abstractChannelHandlerContext.prev = this.head;
        abstractChannelHandlerContext.next = abstractChannelHandlerContext2;
        this.head.next = abstractChannelHandlerContext;
        abstractChannelHandlerContext2.prev = abstractChannelHandlerContext;
        this.name2ctx.put(string, abstractChannelHandlerContext);
        this.callHandlerAdded(abstractChannelHandlerContext);
    }

    private static void waitForFuture(java.util.concurrent.Future<?> future) {
        try {
            future.get();
        }
        catch (ExecutionException executionException) {
            PlatformDependent.throwException(executionException.getCause());
        }
        catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public ChannelPipeline fireChannelRead(Object object) {
        this.head.fireChannelRead(object);
        return this;
    }

    @Override
    public ChannelFuture close(ChannelPromise channelPromise) {
        return this.tail.close(channelPromise);
    }

    @Override
    public ChannelFuture disconnect(ChannelPromise channelPromise) {
        return this.tail.disconnect(channelPromise);
    }

    static {
        logger = InternalLoggerFactory.getInstance(DefaultChannelPipeline.class);
        nameCaches = new WeakHashMap[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < nameCaches.length; ++i) {
            DefaultChannelPipeline.nameCaches[i] = new WeakHashMap();
        }
    }

    @Override
    public ChannelPipeline fireChannelRegistered() {
        this.head.fireChannelRegistered();
        return this;
    }

    @Override
    public ChannelFuture writeAndFlush(Object object, ChannelPromise channelPromise) {
        return this.tail.writeAndFlush(object, channelPromise);
    }

    private static String generateName0(Class<?> clazz) {
        return StringUtil.simpleClassName(clazz) + "#0";
    }

    private void teardownAll() {
        this.tail.prev.teardown();
    }

    @Override
    public ChannelPipeline fireExceptionCaught(Throwable throwable) {
        this.head.fireExceptionCaught(throwable);
        return this;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
        return this.tail.connect(socketAddress, socketAddress2, channelPromise);
    }

    @Override
    public ChannelFuture writeAndFlush(Object object) {
        return this.tail.writeAndFlush(object);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append('{');
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        while (abstractChannelHandlerContext != this.tail) {
            stringBuilder.append('(');
            stringBuilder.append(abstractChannelHandlerContext.name());
            stringBuilder.append(" = ");
            stringBuilder.append(abstractChannelHandlerContext.handler().getClass().getName());
            stringBuilder.append(')');
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
            if (abstractChannelHandlerContext == this.tail) break;
            stringBuilder.append(", ");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private void checkDuplicateName(String string) {
        if (this.name2ctx.containsKey(string)) {
            throw new IllegalArgumentException("Duplicate handler name: " + string);
        }
    }

    @Override
    public ChannelHandlerContext context(ChannelHandler channelHandler) {
        if (channelHandler == null) {
            throw new NullPointerException("handler");
        }
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        while (abstractChannelHandlerContext != null) {
            if (abstractChannelHandlerContext.handler() == channelHandler) {
                return abstractChannelHandlerContext;
            }
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
        }
        return null;
    }

    @Override
    public <T extends ChannelHandler> T replace(Class<T> clazz, String string, ChannelHandler channelHandler) {
        return (T)this.replace(this.getContextOrDie(clazz), string, channelHandler);
    }

    static /* synthetic */ String access$300(Class clazz) {
        return DefaultChannelPipeline.generateName0(clazz);
    }

    private void callHandlerAdded0(ChannelHandlerContext channelHandlerContext) {
        try {
            channelHandlerContext.handler().handlerAdded(channelHandlerContext);
        }
        catch (Throwable throwable) {
            boolean bl;
            block5: {
                bl = false;
                try {
                    this.remove((AbstractChannelHandlerContext)channelHandlerContext);
                    bl = true;
                }
                catch (Throwable throwable2) {
                    if (!logger.isWarnEnabled()) break block5;
                    logger.warn("Failed to remove a handler: " + channelHandlerContext.name(), throwable2);
                }
            }
            if (bl) {
                this.fireExceptionCaught(new ChannelPipelineException(channelHandlerContext.handler().getClass().getName() + ".handlerAdded() has thrown an exception; removed.", throwable));
            }
            this.fireExceptionCaught(new ChannelPipelineException(channelHandlerContext.handler().getClass().getName() + ".handlerAdded() has thrown an exception; also failed to remove.", throwable));
        }
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
        return this.tail.connect(socketAddress, socketAddress2);
    }

    @Override
    public Map<String, ChannelHandler> toMap() {
        LinkedHashMap<String, ChannelHandler> linkedHashMap = new LinkedHashMap<String, ChannelHandler>();
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        while (abstractChannelHandlerContext != this.tail) {
            linkedHashMap.put(abstractChannelHandlerContext.name(), abstractChannelHandlerContext.handler());
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
        }
        return linkedHashMap;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    private void addAfter0(String string, AbstractChannelHandlerContext abstractChannelHandlerContext, AbstractChannelHandlerContext abstractChannelHandlerContext2) {
        this.checkDuplicateName(string);
        DefaultChannelPipeline.checkMultiplicity(abstractChannelHandlerContext2);
        abstractChannelHandlerContext2.prev = abstractChannelHandlerContext;
        abstractChannelHandlerContext2.next = abstractChannelHandlerContext.next;
        abstractChannelHandlerContext.next.prev = abstractChannelHandlerContext2;
        abstractChannelHandlerContext.next = abstractChannelHandlerContext2;
        this.name2ctx.put(string, abstractChannelHandlerContext2);
        this.callHandlerAdded(abstractChannelHandlerContext2);
    }

    @Override
    public ChannelHandlerContext firstContext() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        if (abstractChannelHandlerContext == this.tail) {
            return null;
        }
        return this.head.next;
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public ChannelHandlerContext context(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        DefaultChannelPipeline defaultChannelPipeline = this;
        // MONITORENTER : defaultChannelPipeline
        // MONITOREXIT : defaultChannelPipeline
        return this.name2ctx.get(string);
    }

    @Override
    public ChannelPipeline fireChannelUnregistered() {
        this.head.fireChannelUnregistered();
        if (!this.channel.isOpen()) {
            this.teardownAll();
        }
        return this;
    }

    @Override
    public ChannelPipeline fireChannelActive() {
        this.head.fireChannelActive();
        if (this.channel.config().isAutoRead()) {
            this.channel.read();
        }
        return this;
    }

    @Override
    public ChannelPipeline addFirst(String string, ChannelHandler channelHandler) {
        return this.addFirst(null, string, channelHandler);
    }

    private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext abstractChannelHandlerContext) {
        AbstractChannelHandlerContext abstractChannelHandlerContext2;
        Future<?> future;
        assert (abstractChannelHandlerContext != this.head && abstractChannelHandlerContext != this.tail);
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            if (!abstractChannelHandlerContext.channel().isRegistered() || abstractChannelHandlerContext.executor().inEventLoop()) {
                this.remove0(abstractChannelHandlerContext);
                return abstractChannelHandlerContext;
            }
            future = abstractChannelHandlerContext.executor().submit(new Runnable(){

                @Override
                public void run() {
                    DefaultChannelPipeline defaultChannelPipeline = DefaultChannelPipeline.this;
                    synchronized (defaultChannelPipeline) {
                        DefaultChannelPipeline.this.remove0(abstractChannelHandlerContext);
                    }
                }
            });
            abstractChannelHandlerContext2 = abstractChannelHandlerContext;
        }
        DefaultChannelPipeline.waitForFuture(future);
        return abstractChannelHandlerContext2;
    }

    @Override
    public ChannelFuture write(Object object) {
        return this.tail.write(object);
    }

    @Override
    public ChannelPipeline addFirst(ChannelHandler ... channelHandlerArray) {
        return this.addFirst((EventExecutorGroup)null, channelHandlerArray);
    }

    @Override
    public ChannelHandler get(String string) {
        ChannelHandlerContext channelHandlerContext = this.context(string);
        if (channelHandlerContext == null) {
            return null;
        }
        return channelHandlerContext.handler();
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
        return this.tail.bind(socketAddress, channelPromise);
    }

    @Override
    public ChannelFuture close() {
        return this.tail.close();
    }

    @Override
    public ChannelPipeline addLast(ChannelHandler ... channelHandlerArray) {
        return this.addLast((EventExecutorGroup)null, channelHandlerArray);
    }

    @Override
    public ChannelFuture bind(SocketAddress socketAddress) {
        return this.tail.bind(socketAddress);
    }

    @Override
    public ChannelPipeline fireChannelWritabilityChanged() {
        this.head.fireChannelWritabilityChanged();
        return this;
    }

    @Override
    public ChannelPipeline addFirst(EventExecutorGroup eventExecutorGroup, String string, ChannelHandler channelHandler) {
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            this.checkDuplicateName(string);
            DefaultChannelHandlerContext defaultChannelHandlerContext = new DefaultChannelHandlerContext(this, eventExecutorGroup, string, channelHandler);
            this.addFirst0(string, defaultChannelHandlerContext);
        }
        return this;
    }

    @Override
    public ChannelPipeline addFirst(EventExecutorGroup eventExecutorGroup, ChannelHandler ... channelHandlerArray) {
        if (channelHandlerArray == null) {
            throw new NullPointerException("handlers");
        }
        if (channelHandlerArray.length == 0 || channelHandlerArray[0] == null) {
            return this;
        }
        for (int i = 1; i < channelHandlerArray.length && channelHandlerArray[i] != null; ++i) {
        }
        for (int i = i - 1; i >= 0; --i) {
            ChannelHandler channelHandler = channelHandlerArray[i];
            this.addFirst(eventExecutorGroup, this.generateName(channelHandler), channelHandler);
        }
        return this;
    }

    private void addBefore0(String string, AbstractChannelHandlerContext abstractChannelHandlerContext, AbstractChannelHandlerContext abstractChannelHandlerContext2) {
        DefaultChannelPipeline.checkMultiplicity(abstractChannelHandlerContext2);
        abstractChannelHandlerContext2.prev = abstractChannelHandlerContext.prev;
        abstractChannelHandlerContext2.next = abstractChannelHandlerContext;
        abstractChannelHandlerContext.prev.next = abstractChannelHandlerContext2;
        abstractChannelHandlerContext.prev = abstractChannelHandlerContext2;
        this.name2ctx.put(string, abstractChannelHandlerContext2);
        this.callHandlerAdded(abstractChannelHandlerContext2);
    }

    private String generateName(ChannelHandler channelHandler) {
        String string;
        WeakHashMap<Class<?>, String> weakHashMap = nameCaches[(int)(Thread.currentThread().getId() % (long)nameCaches.length)];
        Class<?> clazz = channelHandler.getClass();
        Object object = weakHashMap;
        synchronized (object) {
            string = weakHashMap.get(clazz);
            if (string == null) {
                string = DefaultChannelPipeline.generateName0(clazz);
                weakHashMap.put(clazz, string);
            }
        }
        object = this;
        synchronized (object) {
            if (this.name2ctx.containsKey(string)) {
                String string2 = string.substring(0, string.length() - 1);
                int n = 1;
                while (true) {
                    String string3;
                    if (!this.name2ctx.containsKey(string3 = string2 + n)) {
                        string = string3;
                        break;
                    }
                    ++n;
                }
            }
        }
        return string;
    }

    @Override
    public List<String> names() {
        ArrayList<String> arrayList = new ArrayList<String>();
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        while (abstractChannelHandlerContext != null) {
            arrayList.add(abstractChannelHandlerContext.name());
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
        }
        return arrayList;
    }

    private AbstractChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> clazz) {
        AbstractChannelHandlerContext abstractChannelHandlerContext = (AbstractChannelHandlerContext)this.context(clazz);
        if (abstractChannelHandlerContext == null) {
            throw new NoSuchElementException(clazz.getName());
        }
        return abstractChannelHandlerContext;
    }

    private void callHandlerAdded(final ChannelHandlerContext channelHandlerContext) {
        if (channelHandlerContext.channel().isRegistered() && !channelHandlerContext.executor().inEventLoop()) {
            channelHandlerContext.executor().execute(new Runnable(){

                @Override
                public void run() {
                    DefaultChannelPipeline.this.callHandlerAdded0(channelHandlerContext);
                }
            });
            return;
        }
        this.callHandlerAdded0(channelHandlerContext);
    }

    @Override
    public ChannelFuture deregister() {
        return this.tail.deregister();
    }

    @Override
    public ChannelPipeline fireUserEventTriggered(Object object) {
        this.head.fireUserEventTriggered(object);
        return this;
    }

    @Override
    public ChannelPipeline addBefore(EventExecutorGroup eventExecutorGroup, String string, String string2, ChannelHandler channelHandler) {
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            AbstractChannelHandlerContext abstractChannelHandlerContext = this.getContextOrDie(string);
            this.checkDuplicateName(string2);
            DefaultChannelHandlerContext defaultChannelHandlerContext = new DefaultChannelHandlerContext(this, eventExecutorGroup, string2, channelHandler);
            this.addBefore0(string2, abstractChannelHandlerContext, defaultChannelHandlerContext);
        }
        return this;
    }

    @Override
    public ChannelHandler remove(String string) {
        return this.remove(this.getContextOrDie(string)).handler();
    }

    @Override
    public ChannelHandler last() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.tail.prev;
        if (abstractChannelHandlerContext == this.head) {
            return null;
        }
        return abstractChannelHandlerContext.handler();
    }

    private AbstractChannelHandlerContext getContextOrDie(ChannelHandler channelHandler) {
        AbstractChannelHandlerContext abstractChannelHandlerContext = (AbstractChannelHandlerContext)this.context(channelHandler);
        if (abstractChannelHandlerContext == null) {
            throw new NoSuchElementException(channelHandler.getClass().getName());
        }
        return abstractChannelHandlerContext;
    }

    @Override
    public ChannelPipeline addAfter(EventExecutorGroup eventExecutorGroup, String string, String string2, ChannelHandler channelHandler) {
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            AbstractChannelHandlerContext abstractChannelHandlerContext = this.getContextOrDie(string);
            this.checkDuplicateName(string2);
            DefaultChannelHandlerContext defaultChannelHandlerContext = new DefaultChannelHandlerContext(this, eventExecutorGroup, string2, channelHandler);
            this.addAfter0(string2, abstractChannelHandlerContext, defaultChannelHandlerContext);
        }
        return this;
    }

    private void addLast0(String string, AbstractChannelHandlerContext abstractChannelHandlerContext) {
        AbstractChannelHandlerContext abstractChannelHandlerContext2;
        DefaultChannelPipeline.checkMultiplicity(abstractChannelHandlerContext);
        abstractChannelHandlerContext.prev = abstractChannelHandlerContext2 = this.tail.prev;
        abstractChannelHandlerContext.next = this.tail;
        abstractChannelHandlerContext2.next = abstractChannelHandlerContext;
        this.tail.prev = abstractChannelHandlerContext;
        this.name2ctx.put(string, abstractChannelHandlerContext);
        this.callHandlerAdded(abstractChannelHandlerContext);
    }

    @Override
    public ChannelPipeline replace(ChannelHandler channelHandler, String string, ChannelHandler channelHandler2) {
        this.replace(this.getContextOrDie(channelHandler), string, channelHandler2);
        return this;
    }

    @Override
    public <T extends ChannelHandler> T get(Class<T> clazz) {
        ChannelHandlerContext channelHandlerContext = this.context(clazz);
        if (channelHandlerContext == null) {
            return null;
        }
        return (T)channelHandlerContext.handler();
    }

    @Override
    public ChannelPipeline addLast(EventExecutorGroup eventExecutorGroup, String string, ChannelHandler channelHandler) {
        DefaultChannelPipeline defaultChannelPipeline = this;
        synchronized (defaultChannelPipeline) {
            this.checkDuplicateName(string);
            DefaultChannelHandlerContext defaultChannelHandlerContext = new DefaultChannelHandlerContext(this, eventExecutorGroup, string, channelHandler);
            this.addLast0(string, defaultChannelHandlerContext);
        }
        return this;
    }

    @Override
    public ChannelPipeline fireChannelInactive() {
        this.head.fireChannelInactive();
        return this;
    }

    @Override
    public ChannelPipeline addBefore(String string, String string2, ChannelHandler channelHandler) {
        return this.addBefore(null, string, string2, channelHandler);
    }

    @Override
    public ChannelHandler replace(String string, String string2, ChannelHandler channelHandler) {
        return this.replace(this.getContextOrDie(string), string2, channelHandler);
    }

    @Override
    public ChannelFuture deregister(ChannelPromise channelPromise) {
        return this.tail.deregister(channelPromise);
    }

    @Override
    public ChannelPipeline addAfter(String string, String string2, ChannelHandler channelHandler) {
        return this.addAfter(null, string, string2, channelHandler);
    }

    @Override
    public ChannelHandlerContext lastContext() {
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.tail.prev;
        if (abstractChannelHandlerContext == this.head) {
            return null;
        }
        return abstractChannelHandlerContext;
    }

    @Override
    public ChannelPipeline addLast(EventExecutorGroup eventExecutorGroup, ChannelHandler ... channelHandlerArray) {
        if (channelHandlerArray == null) {
            throw new NullPointerException("handlers");
        }
        for (ChannelHandler channelHandler : channelHandlerArray) {
            if (channelHandler == null) break;
            this.addLast(eventExecutorGroup, this.generateName(channelHandler), channelHandler);
        }
        return this;
    }

    private void callHandlerRemoved(final AbstractChannelHandlerContext abstractChannelHandlerContext) {
        if (abstractChannelHandlerContext.channel().isRegistered() && !abstractChannelHandlerContext.executor().inEventLoop()) {
            abstractChannelHandlerContext.executor().execute(new Runnable(){

                @Override
                public void run() {
                    DefaultChannelPipeline.this.callHandlerRemoved0(abstractChannelHandlerContext);
                }
            });
            return;
        }
        this.callHandlerRemoved0(abstractChannelHandlerContext);
    }

    @Override
    public ChannelPipeline addLast(String string, ChannelHandler channelHandler) {
        return this.addLast(null, string, channelHandler);
    }

    @Override
    public ChannelHandlerContext context(Class<? extends ChannelHandler> clazz) {
        if (clazz == null) {
            throw new NullPointerException("handlerType");
        }
        AbstractChannelHandlerContext abstractChannelHandlerContext = this.head.next;
        while (abstractChannelHandlerContext != null) {
            if (clazz.isAssignableFrom(abstractChannelHandlerContext.handler().getClass())) {
                return abstractChannelHandlerContext;
            }
            abstractChannelHandlerContext = abstractChannelHandlerContext.next;
        }
        return null;
    }

    @Override
    public ChannelPipeline flush() {
        this.tail.flush();
        return this;
    }

    private void callHandlerRemoved0(AbstractChannelHandlerContext abstractChannelHandlerContext) {
        try {
            abstractChannelHandlerContext.handler().handlerRemoved(abstractChannelHandlerContext);
            abstractChannelHandlerContext.setRemoved();
        }
        catch (Throwable throwable) {
            this.fireExceptionCaught(new ChannelPipelineException(abstractChannelHandlerContext.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", throwable));
        }
    }

    @Override
    public ChannelPipeline fireChannelReadComplete() {
        this.head.fireChannelReadComplete();
        if (this.channel.config().isAutoRead()) {
            this.read();
        }
        return this;
    }

    @Override
    public ChannelFuture connect(SocketAddress socketAddress) {
        return this.tail.connect(socketAddress);
    }

    static final class TailContext
    extends AbstractChannelHandlerContext
    implements ChannelInboundHandler {
        private static final String TAIL_NAME = DefaultChannelPipeline.access$300(TailContext.class);

        @Override
        public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        }

        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
            logger.debug("Discarded inbound message {} that reached at the tail of the pipeline. Please check your pipeline configuration.", object);
            ReferenceCountUtil.release(object);
        }

        @Override
        public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        TailContext(DefaultChannelPipeline defaultChannelPipeline) {
            super(defaultChannelPipeline, null, TAIL_NAME, true, false);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            logger.warn("An exceptionCaught() event was fired, and it reached at the tail of the pipeline. It usually means the last handler in the pipeline did not handle the exception.", throwable);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        }
    }

    static final class HeadContext
    extends AbstractChannelHandlerContext
    implements ChannelOutboundHandler {
        protected final Channel.Unsafe unsafe;
        private static final String HEAD_NAME = DefaultChannelPipeline.access$300(HeadContext.class);

        HeadContext(DefaultChannelPipeline defaultChannelPipeline) {
            super(defaultChannelPipeline, null, HEAD_NAME, false, true);
            this.unsafe = defaultChannelPipeline.channel().unsafe();
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
            this.unsafe.flush();
        }

        @Override
        public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception {
            this.unsafe.connect(socketAddress, socketAddress2, channelPromise);
        }

        @Override
        public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        }

        @Override
        public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
            this.unsafe.write(object, channelPromise);
        }

        @Override
        public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
            this.unsafe.deregister(channelPromise);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            channelHandlerContext.fireExceptionCaught(throwable);
        }

        @Override
        public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
            this.unsafe.close(channelPromise);
        }

        @Override
        public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
            this.unsafe.disconnect(channelPromise);
        }

        @Override
        public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
            this.unsafe.bind(socketAddress, channelPromise);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }

        @Override
        public void read(ChannelHandlerContext channelHandlerContext) {
            this.unsafe.beginRead();
        }
    }
}

