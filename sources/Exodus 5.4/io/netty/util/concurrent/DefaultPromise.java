/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.Signal;
import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultFutureListeners;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GenericProgressiveFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

public class DefaultPromise<V>
extends AbstractFuture<V>
implements Promise<V> {
    private volatile Object result;
    private static final int MAX_LISTENER_STACK_DEPTH = 8;
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER;
    private static final InternalLogger logger;
    private static final Signal UNCANCELLABLE;
    private static final InternalLogger rejectedExecutionLogger;
    private static final Signal SUCCESS;
    private final EventExecutor executor;
    private LateListeners lateListeners;
    private Object listeners;
    private short waiters;

    private void incWaiters() {
        if (this.waiters == Short.MAX_VALUE) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        this.waiters = (short)(this.waiters + 1);
    }

    @Override
    public boolean tryFailure(Throwable throwable) {
        if (this.setFailure0(throwable)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }

    static {
        logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
        rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
        SUCCESS = Signal.valueOf(DefaultPromise.class.getName() + ".SUCCESS");
        UNCANCELLABLE = Signal.valueOf(DefaultPromise.class.getName() + ".UNCANCELLABLE");
        CANCELLATION_CAUSE_HOLDER = new CauseHolder(new CancellationException());
        DefaultPromise.CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public Promise<V> awaitUninterruptibly() {
        if (this.isDone()) {
            return this;
        }
        boolean bl = false;
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            while (!this.isDone()) {
                this.checkDeadLock();
                this.incWaiters();
                try {
                    this.wait();
                    this.decWaiters();
                }
                catch (InterruptedException interruptedException) {
                    bl = true;
                    this.decWaiters();
                }
            }
        }
        if (!bl) return this;
        Thread.currentThread().interrupt();
        return this;
    }

    @Override
    public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        if (genericFutureListenerArray == null) {
            throw new NullPointerException("listeners");
        }
        for (GenericFutureListener<? extends Future<? super V>> genericFutureListener : genericFutureListenerArray) {
            if (genericFutureListener == null) break;
            this.addListener((GenericFutureListener)genericFutureListener);
        }
        return this;
    }

    private static void execute(EventExecutor eventExecutor, Runnable runnable) {
        try {
            eventExecutor.execute(runnable);
        }
        catch (Throwable throwable) {
            rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", throwable);
        }
    }

    @Override
    public boolean cancel(boolean bl) {
        Object object = this.result;
        if (DefaultPromise.isDone0(object) || object == UNCANCELLABLE) {
            return false;
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            object = this.result;
            if (DefaultPromise.isDone0(object) || object == UNCANCELLABLE) {
                return false;
            }
            this.result = CANCELLATION_CAUSE_HOLDER;
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        this.notifyListeners();
        return true;
    }

    @Override
    public Promise<V> syncUninterruptibly() {
        this.awaitUninterruptibly();
        this.rethrowIfFailed();
        return this;
    }

    protected DefaultPromise() {
        this.executor = null;
    }

    /*
     * WARNING - void declaration
     */
    private synchronized Object progressiveListeners() {
        Object object = this.listeners;
        if (object == null) {
            return null;
        }
        if (object instanceof DefaultFutureListeners) {
            void var7_12;
            DefaultFutureListeners defaultFutureListeners = (DefaultFutureListeners)object;
            int n = defaultFutureListeners.progressiveSize();
            switch (n) {
                case 0: {
                    return null;
                }
                case 1: {
                    for (GenericFutureListener<Future<?>> genericFutureListener : defaultFutureListeners.listeners()) {
                        if (!(genericFutureListener instanceof GenericProgressiveFutureListener)) continue;
                        return genericFutureListener;
                    }
                    return null;
                }
            }
            GenericFutureListener<? extends Future<?>>[] genericFutureListenerArray = defaultFutureListeners.listeners();
            GenericProgressiveFutureListener[] genericProgressiveFutureListenerArray = new GenericProgressiveFutureListener[n];
            int n2 = 0;
            boolean n3 = false;
            while (var7_12 < n) {
                GenericFutureListener<Future<?>> genericFutureListener = genericFutureListenerArray[n2];
                if (genericFutureListener instanceof GenericProgressiveFutureListener) {
                    genericProgressiveFutureListenerArray[++var7_12] = (GenericProgressiveFutureListener)genericFutureListener;
                }
                ++n2;
            }
            return genericProgressiveFutureListenerArray;
        }
        if (object instanceof GenericProgressiveFutureListener) {
            return object;
        }
        return null;
    }

    @Override
    public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        if (genericFutureListener == null) {
            throw new NullPointerException("listener");
        }
        if (this.isDone()) {
            this.notifyLateListener(genericFutureListener);
            return this;
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            if (!this.isDone()) {
                if (this.listeners == null) {
                    this.listeners = genericFutureListener;
                } else if (this.listeners instanceof DefaultFutureListeners) {
                    ((DefaultFutureListeners)this.listeners).add(genericFutureListener);
                } else {
                    GenericFutureListener genericFutureListener2 = (GenericFutureListener)this.listeners;
                    this.listeners = new DefaultFutureListeners(genericFutureListener2, genericFutureListener);
                }
                return this;
            }
        }
        this.notifyLateListener(genericFutureListener);
        return this;
    }

    @Override
    public boolean isCancellable() {
        return this.result == null;
    }

    @Override
    public boolean setUncancellable() {
        Object object = this.result;
        if (DefaultPromise.isDone0(object)) {
            return !DefaultPromise.isCancelled0(object);
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            object = this.result;
            if (DefaultPromise.isDone0(object)) {
                return !DefaultPromise.isCancelled0(object);
            }
            this.result = UNCANCELLABLE;
        }
        return true;
    }

    @Override
    public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
        try {
            return this.await0(timeUnit.toNanos(l), false);
        }
        catch (InterruptedException interruptedException) {
            throw new InternalError();
        }
    }

    private void rethrowIfFailed() {
        Throwable throwable = this.cause();
        if (throwable == null) {
            return;
        }
        PlatformDependent.throwException(throwable);
    }

    @Override
    public Promise<V> setSuccess(V v) {
        if (this.setSuccess0(v)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this);
    }

    private boolean setSuccess0(V v) {
        if (this.isDone()) {
            return false;
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            if (this.isDone()) {
                return false;
            }
            this.result = v == null ? SUCCESS : v;
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        return true;
    }

    @Override
    public boolean isSuccess() {
        Object object = this.result;
        if (object == null || object == UNCANCELLABLE) {
            return false;
        }
        return !(object instanceof CauseHolder);
    }

    @Override
    public boolean isCancelled() {
        return DefaultPromise.isCancelled0(this.result);
    }

    @Override
    public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>> ... genericFutureListenerArray) {
        if (genericFutureListenerArray == null) {
            throw new NullPointerException("listeners");
        }
        for (GenericFutureListener<? extends Future<? super V>> genericFutureListener : genericFutureListenerArray) {
            if (genericFutureListener == null) break;
            this.removeListener((GenericFutureListener)genericFutureListener);
        }
        return this;
    }

    protected static void notifyListener(EventExecutor eventExecutor, final Future<?> future, final GenericFutureListener<?> genericFutureListener) {
        InternalThreadLocalMap internalThreadLocalMap;
        int n;
        if (eventExecutor.inEventLoop() && (n = (internalThreadLocalMap = InternalThreadLocalMap.get()).futureListenerStackDepth()) < 8) {
            internalThreadLocalMap.setFutureListenerStackDepth(n + 1);
            DefaultPromise.notifyListener0(future, genericFutureListener);
            internalThreadLocalMap.setFutureListenerStackDepth(n);
            return;
        }
        DefaultPromise.execute(eventExecutor, new Runnable(){

            @Override
            public void run() {
                DefaultPromise.notifyListener0(future, genericFutureListener);
            }
        });
    }

    public String toString() {
        return this.toStringBuilder().toString();
    }

    private void notifyListeners() {
        Object object;
        int n;
        Object object2 = this.listeners;
        if (object2 == null) {
            return;
        }
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor.inEventLoop() && (n = ((InternalThreadLocalMap)(object = InternalThreadLocalMap.get())).futureListenerStackDepth()) < 8) {
            ((InternalThreadLocalMap)object).setFutureListenerStackDepth(n + 1);
            if (object2 instanceof DefaultFutureListeners) {
                DefaultPromise.notifyListeners0(this, (DefaultFutureListeners)object2);
            } else {
                GenericFutureListener genericFutureListener = (GenericFutureListener)object2;
                DefaultPromise.notifyListener0(this, genericFutureListener);
            }
            this.listeners = null;
            ((InternalThreadLocalMap)object).setFutureListenerStackDepth(n);
            return;
        }
        if (object2 instanceof DefaultFutureListeners) {
            object = (DefaultFutureListeners)object2;
            DefaultPromise.execute(eventExecutor, new Runnable((DefaultFutureListeners)object){
                final /* synthetic */ DefaultFutureListeners val$dfl;

                @Override
                public void run() {
                    DefaultPromise.notifyListeners0(DefaultPromise.this, this.val$dfl);
                    DefaultPromise.this.listeners = null;
                }
                {
                    this.val$dfl = defaultFutureListeners;
                }
            });
        } else {
            object = (GenericFutureListener)object2;
            DefaultPromise.execute(eventExecutor, new Runnable((GenericFutureListener)object){
                final /* synthetic */ GenericFutureListener val$l;

                @Override
                public void run() {
                    DefaultPromise.notifyListener0(DefaultPromise.this, this.val$l);
                    DefaultPromise.this.listeners = null;
                }
                {
                    this.val$l = genericFutureListener;
                }
            });
        }
    }

    private void notifyLateListener(GenericFutureListener<?> genericFutureListener) {
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor.inEventLoop()) {
            if (this.listeners == null && this.lateListeners == null) {
                InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.get();
                int n = internalThreadLocalMap.futureListenerStackDepth();
                if (n < 8) {
                    internalThreadLocalMap.setFutureListenerStackDepth(n + 1);
                    DefaultPromise.notifyListener0(this, genericFutureListener);
                    internalThreadLocalMap.setFutureListenerStackDepth(n);
                    return;
                }
            } else {
                LateListeners lateListeners = this.lateListeners;
                if (lateListeners == null) {
                    this.lateListeners = lateListeners = new LateListeners();
                }
                lateListeners.add(genericFutureListener);
                DefaultPromise.execute(eventExecutor, lateListeners);
                return;
            }
        }
        DefaultPromise.execute(eventExecutor, new LateListenerNotifier(genericFutureListener));
    }

    @Override
    public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
        return this.await0(timeUnit.toNanos(l), true);
    }

    @Override
    public boolean await(long l) throws InterruptedException {
        return this.await0(TimeUnit.MILLISECONDS.toNanos(l), true);
    }

    protected void checkDeadLock() {
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor != null && eventExecutor.inEventLoop()) {
            throw new BlockingOperationException(this.toString());
        }
    }

    protected StringBuilder toStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder(64);
        stringBuilder.append(StringUtil.simpleClassName(this));
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        Object object = this.result;
        if (object == SUCCESS) {
            stringBuilder.append("(success)");
        } else if (object == UNCANCELLABLE) {
            stringBuilder.append("(uncancellable)");
        } else if (object instanceof CauseHolder) {
            stringBuilder.append("(failure(");
            stringBuilder.append(((CauseHolder)object).cause);
            stringBuilder.append(')');
        } else {
            stringBuilder.append("(incomplete)");
        }
        return stringBuilder;
    }

    public DefaultPromise(EventExecutor eventExecutor) {
        if (eventExecutor == null) {
            throw new NullPointerException("executor");
        }
        this.executor = eventExecutor;
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    @Override
    public boolean trySuccess(V v) {
        if (this.setSuccess0(v)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public Throwable cause() {
        Object object = this.result;
        if (object instanceof CauseHolder) {
            return ((CauseHolder)object).cause;
        }
        return null;
    }

    @Override
    public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener) {
        if (genericFutureListener == null) {
            throw new NullPointerException("listener");
        }
        if (this.isDone()) {
            return this;
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            if (!this.isDone()) {
                if (this.listeners instanceof DefaultFutureListeners) {
                    ((DefaultFutureListeners)this.listeners).remove(genericFutureListener);
                } else if (this.listeners == genericFutureListener) {
                    this.listeners = null;
                }
            }
        }
        return this;
    }

    private static void notifyProgressiveListeners0(ProgressiveFuture<?> progressiveFuture, GenericProgressiveFutureListener<?>[] genericProgressiveFutureListenerArray, long l, long l2) {
        for (GenericProgressiveFutureListener<?> genericProgressiveFutureListener : genericProgressiveFutureListenerArray) {
            if (genericProgressiveFutureListener == null) break;
            DefaultPromise.notifyProgressiveListener0(progressiveFuture, genericProgressiveFutureListener, l, l2);
        }
    }

    private static void notifyProgressiveListener0(ProgressiveFuture progressiveFuture, GenericProgressiveFutureListener genericProgressiveFutureListener, long l, long l2) {
        block2: {
            try {
                genericProgressiveFutureListener.operationProgressed(progressiveFuture, l, l2);
            }
            catch (Throwable throwable) {
                if (!logger.isWarnEnabled()) break block2;
                logger.warn("An exception was thrown by " + genericProgressiveFutureListener.getClass().getName() + ".operationProgressed()", throwable);
            }
        }
    }

    private void decWaiters() {
        this.waiters = (short)(this.waiters - 1);
    }

    private boolean setFailure0(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        if (this.isDone()) {
            return false;
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            if (this.isDone()) {
                return false;
            }
            this.result = new CauseHolder(throwable);
            if (this.hasWaiters()) {
                this.notifyAll();
            }
        }
        return true;
    }

    private boolean hasWaiters() {
        return this.waiters > 0;
    }

    @Override
    public Promise<V> sync() throws InterruptedException {
        this.await();
        this.rethrowIfFailed();
        return this;
    }

    @Override
    public Promise<V> await() throws InterruptedException {
        if (this.isDone()) {
            return this;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            while (!this.isDone()) {
                this.checkDeadLock();
                this.incWaiters();
                this.wait();
                this.decWaiters();
            }
        }
        return this;
    }

    @Override
    public boolean isDone() {
        return DefaultPromise.isDone0(this.result);
    }

    void notifyProgressiveListeners(final long l, final long l2) {
        Object object = this.progressiveListeners();
        if (object == null) {
            return;
        }
        final ProgressiveFuture progressiveFuture = (ProgressiveFuture)((Object)this);
        EventExecutor eventExecutor = this.executor();
        if (eventExecutor.inEventLoop()) {
            if (object instanceof GenericProgressiveFutureListener[]) {
                DefaultPromise.notifyProgressiveListeners0(progressiveFuture, (GenericProgressiveFutureListener[])object, l, l2);
            } else {
                DefaultPromise.notifyProgressiveListener0(progressiveFuture, (GenericProgressiveFutureListener)object, l, l2);
            }
        } else if (object instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener[] genericProgressiveFutureListenerArray = (GenericProgressiveFutureListener[])object;
            DefaultPromise.execute(eventExecutor, new Runnable(){

                @Override
                public void run() {
                    DefaultPromise.notifyProgressiveListeners0(progressiveFuture, genericProgressiveFutureListenerArray, l, l2);
                }
            });
        } else {
            final GenericProgressiveFutureListener genericProgressiveFutureListener = (GenericProgressiveFutureListener)object;
            DefaultPromise.execute(eventExecutor, new Runnable(){

                @Override
                public void run() {
                    DefaultPromise.notifyProgressiveListener0(progressiveFuture, genericProgressiveFutureListener, l, l2);
                }
            });
        }
    }

    @Override
    public V getNow() {
        Object object = this.result;
        if (object instanceof CauseHolder || object == SUCCESS) {
            return null;
        }
        return (V)object;
    }

    private static boolean isCancelled0(Object object) {
        return object instanceof CauseHolder && ((CauseHolder)object).cause instanceof CancellationException;
    }

    private static void notifyListeners0(Future<?> future, DefaultFutureListeners defaultFutureListeners) {
        GenericFutureListener<? extends Future<?>>[] genericFutureListenerArray = defaultFutureListeners.listeners();
        int n = defaultFutureListeners.size();
        for (int i = 0; i < n; ++i) {
            DefaultPromise.notifyListener0(future, genericFutureListenerArray[i]);
        }
    }

    static void notifyListener0(Future future, GenericFutureListener genericFutureListener) {
        block2: {
            try {
                genericFutureListener.operationComplete(future);
            }
            catch (Throwable throwable) {
                if (!logger.isWarnEnabled()) break block2;
                logger.warn("An exception was thrown by " + genericFutureListener.getClass().getName() + ".operationComplete()", throwable);
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean await0(long l, boolean bl) throws InterruptedException {
        boolean bl2;
        if (this.isDone()) {
            return true;
        }
        if (l <= 0L) {
            return this.isDone();
        }
        if (bl && Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        }
        long l2 = System.nanoTime();
        long l3 = l;
        boolean bl3 = false;
        DefaultPromise defaultPromise = this;
        synchronized (defaultPromise) {
            if (this.isDone()) {
                boolean bl4 = true;
                // MONITOREXIT @DISABLED, blocks:[1, 6] lbl14 : MonitorExitStatement: MONITOREXIT : var9_6
                if (!bl3) return bl4;
                Thread.currentThread().interrupt();
                return bl4;
            }
            if (l3 <= 0L) {
                boolean bl5 = this.isDone();
                // MONITOREXIT @DISABLED, blocks:[1, 5] lbl20 : MonitorExitStatement: MONITOREXIT : var9_6
                if (!bl3) return bl5;
                Thread.currentThread().interrupt();
                return bl5;
            }
            this.checkDeadLock();
            this.incWaiters();
            do {
                try {
                    this.wait(l3 / 1000000L, (int)(l3 % 1000000L));
                }
                catch (InterruptedException interruptedException) {
                    if (bl) {
                        throw interruptedException;
                    }
                    bl3 = true;
                }
                if (!this.isDone()) continue;
                boolean bl6 = true;
                this.decWaiters();
                // MONITOREXIT @DISABLED, blocks:[1, 3] lbl37 : MonitorExitStatement: MONITOREXIT : var9_6
                if (!bl3) return bl6;
                Thread.currentThread().interrupt();
                return bl6;
            } while ((l3 = l - (System.nanoTime() - l2)) > 0L);
            bl2 = this.isDone();
            this.decWaiters();
        }
        if (!bl3) return bl2;
        Thread.currentThread().interrupt();
        return bl2;
    }

    @Override
    public boolean awaitUninterruptibly(long l) {
        try {
            return this.await0(TimeUnit.MILLISECONDS.toNanos(l), false);
        }
        catch (InterruptedException interruptedException) {
            throw new InternalError();
        }
    }

    @Override
    public Promise<V> setFailure(Throwable throwable) {
        if (this.setFailure0(throwable)) {
            this.notifyListeners();
            return this;
        }
        throw new IllegalStateException("complete already: " + this, throwable);
    }

    private static boolean isDone0(Object object) {
        return object != null && object != UNCANCELLABLE;
    }

    private final class LateListeners
    extends ArrayDeque<GenericFutureListener<?>>
    implements Runnable {
        private static final long serialVersionUID = -687137418080392244L;

        @Override
        public void run() {
            if (DefaultPromise.this.listeners == null) {
                GenericFutureListener genericFutureListener;
                while ((genericFutureListener = (GenericFutureListener)this.poll()) != null) {
                    DefaultPromise.notifyListener0(DefaultPromise.this, genericFutureListener);
                }
            } else {
                DefaultPromise.execute(DefaultPromise.this.executor(), this);
            }
        }

        LateListeners() {
            super(2);
        }
    }

    private static final class CauseHolder {
        final Throwable cause;

        CauseHolder(Throwable throwable) {
            this.cause = throwable;
        }
    }

    private final class LateListenerNotifier
    implements Runnable {
        private GenericFutureListener<?> l;

        LateListenerNotifier(GenericFutureListener<?> genericFutureListener) {
            this.l = genericFutureListener;
        }

        @Override
        public void run() {
            LateListeners lateListeners = DefaultPromise.this.lateListeners;
            if (this.l != null) {
                if (lateListeners == null) {
                    lateListeners = new LateListeners();
                    DefaultPromise.this.lateListeners = lateListeners;
                }
                lateListeners.add(this.l);
                this.l = null;
            }
            lateListeners.run();
        }
    }
}

