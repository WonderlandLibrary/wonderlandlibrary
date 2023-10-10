// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Throwables;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import com.google.common.base.Strings;
import java.util.concurrent.ScheduledFuture;
import com.google.errorprone.annotations.ForOverride;
import com.google.common.annotations.Beta;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.internal.InternalFutures;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.CancellationException;
import java.util.concurrent.locks.LockSupport;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;
import java.util.logging.Logger;
import com.google.j2objc.annotations.ReflectionSupport;
import com.google.common.annotations.GwtCompatible;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
@ReflectionSupport(ReflectionSupport.Level.FULL)
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V>
{
    private static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Logger log;
    private static final long SPIN_THRESHOLD_NANOS = 1000L;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Object NULL;
    @CheckForNull
    private volatile Object value;
    @CheckForNull
    private volatile Listener listeners;
    @CheckForNull
    private volatile Waiter waiters;
    
    private void removeWaiter(final Waiter node) {
        node.thread = null;
    Label_0005:
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr == Waiter.TOMBSTONE) {
                return;
            }
            while (curr != null) {
                final Waiter succ = curr.next;
                if (curr.thread != null) {
                    pred = curr;
                }
                else if (pred != null) {
                    pred.next = succ;
                    if (pred.thread == null) {
                        continue Label_0005;
                    }
                }
                else if (!AbstractFuture.ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                    continue Label_0005;
                }
                curr = succ;
            }
        }
    }
    
    protected AbstractFuture() {
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long remainingNanos;
        final long timeoutNanos = remainingNanos = unit.toNanos(timeout);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (localValue != null & !(localValue instanceof SetFuture)) {
            return this.getDoneValue(localValue);
        }
        final long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
        Label_0255: {
            if (remainingNanos >= 1000L) {
                Waiter oldHead = this.waiters;
                if (oldHead != Waiter.TOMBSTONE) {
                    final Waiter node = new Waiter();
                    do {
                        node.setNext(oldHead);
                        if (AbstractFuture.ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                            do {
                                OverflowAvoidingLockSupport.parkNanos(this, remainingNanos);
                                if (Thread.interrupted()) {
                                    this.removeWaiter(node);
                                    throw new InterruptedException();
                                }
                                localValue = this.value;
                                if (localValue != null & !(localValue instanceof SetFuture)) {
                                    return this.getDoneValue(localValue);
                                }
                                remainingNanos = endNanos - System.nanoTime();
                            } while (remainingNanos >= 1000L);
                            this.removeWaiter(node);
                            break Label_0255;
                        }
                        oldHead = this.waiters;
                    } while (oldHead != Waiter.TOMBSTONE);
                }
                return this.getDoneValue(Objects.requireNonNull(this.value));
            }
        }
        while (remainingNanos > 0L) {
            localValue = this.value;
            if (localValue != null & !(localValue instanceof SetFuture)) {
                return this.getDoneValue(localValue);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            remainingNanos = endNanos - System.nanoTime();
        }
        final String futureToString = this.toString();
        final String unitString = unit.toString().toLowerCase(Locale.ROOT);
        final String lowerCase = unit.toString().toLowerCase(Locale.ROOT);
        String message = new StringBuilder(28 + String.valueOf(lowerCase).length()).append("Waited ").append(timeout).append(" ").append(lowerCase).toString();
        if (remainingNanos + 1000L < 0L) {
            message = String.valueOf(message).concat(" (plus ");
            final long overWaitNanos = -remainingNanos;
            final long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
            final long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
            final boolean shouldShowExtraNanos = overWaitUnits == 0L || overWaitLeftoverNanos > 1000L;
            if (overWaitUnits > 0L) {
                final String value = String.valueOf(message);
                message = new StringBuilder(21 + String.valueOf(value).length() + String.valueOf(unitString).length()).append(value).append(overWaitUnits).append(" ").append(unitString).toString();
                if (shouldShowExtraNanos) {
                    message = String.valueOf(message).concat(",");
                }
                message = String.valueOf(message).concat(" ");
            }
            if (shouldShowExtraNanos) {
                final String value2 = String.valueOf(message);
                message = new StringBuilder(33 + String.valueOf(value2).length()).append(value2).append(overWaitLeftoverNanos).append(" nanoseconds ").toString();
            }
            message = String.valueOf(message).concat("delay)");
        }
        if (this.isDone()) {
            throw new TimeoutException(String.valueOf(message).concat(" but future completed as timeout expired"));
        }
        final String s = message;
        throw new TimeoutException(new StringBuilder(5 + String.valueOf(s).length() + String.valueOf(futureToString).length()).append(s).append(" for ").append(futureToString).toString());
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (localValue != null & !(localValue instanceof SetFuture)) {
            return this.getDoneValue(localValue);
        }
        Waiter oldHead = this.waiters;
        if (oldHead != Waiter.TOMBSTONE) {
            final Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (AbstractFuture.ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            this.removeWaiter(node);
                            throw new InterruptedException();
                        }
                        localValue = this.value;
                    } while (!(localValue != null & !(localValue instanceof SetFuture)));
                    return this.getDoneValue(localValue);
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
        }
        return this.getDoneValue(Objects.requireNonNull(this.value));
    }
    
    @ParametricNullness
    private V getDoneValue(final Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause);
        }
        if (obj instanceof Failure) {
            throw new ExecutionException(((Failure)obj).exception);
        }
        if (obj == AbstractFuture.NULL) {
            return NullnessCasts.uncheckedNull();
        }
        final V asV = (V)obj;
        return asV;
    }
    
    @Override
    public boolean isDone() {
        final Object localValue = this.value;
        return localValue != null & !(localValue instanceof SetFuture);
    }
    
    @Override
    public boolean isCancelled() {
        final Object localValue = this.value;
        return localValue instanceof Cancellation;
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        Object localValue = this.value;
        boolean rValue = false;
        if (localValue == null | localValue instanceof SetFuture) {
            final Object valueToSet = AbstractFuture.GENERATE_CANCELLATION_CAUSES ? new Cancellation(mayInterruptIfRunning, new CancellationException("Future.cancel() was called.")) : Objects.requireNonNull(mayInterruptIfRunning ? Cancellation.CAUSELESS_INTERRUPTED : Cancellation.CAUSELESS_CANCELLED);
            AbstractFuture<?> abstractFuture = this;
            while (true) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (localValue instanceof SetFuture) {
                        final ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
                        if (futureToPropagateTo instanceof Trusted) {
                            final AbstractFuture<?> trusted = (AbstractFuture<?>)(AbstractFuture)futureToPropagateTo;
                            localValue = trusted.value;
                            if (localValue == null | localValue instanceof SetFuture) {
                                abstractFuture = trusted;
                                continue;
                            }
                        }
                        else {
                            futureToPropagateTo.cancel(mayInterruptIfRunning);
                        }
                        break;
                    }
                    break;
                }
                else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    continue;
                }
            }
        }
        return rValue;
    }
    
    protected void interruptTask() {
    }
    
    protected final boolean wasInterrupted() {
        final Object localValue = this.value;
        return localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted;
    }
    
    @Override
    public void addListener(final Runnable listener, final Executor executor) {
        Preconditions.checkNotNull(listener, (Object)"Runnable was null.");
        Preconditions.checkNotNull(executor, (Object)"Executor was null.");
        if (!this.isDone()) {
            Listener oldHead = this.listeners;
            if (oldHead != Listener.TOMBSTONE) {
                final Listener newNode = new Listener(listener, executor);
                do {
                    newNode.next = oldHead;
                    if (AbstractFuture.ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                        return;
                    }
                    oldHead = this.listeners;
                } while (oldHead != Listener.TOMBSTONE);
            }
        }
        executeListener(listener, executor);
    }
    
    @CanIgnoreReturnValue
    protected boolean set(@ParametricNullness final V value) {
        final Object valueToSet = (value == null) ? AbstractFuture.NULL : value;
        if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }
    
    @CanIgnoreReturnValue
    protected boolean setException(final Throwable throwable) {
        final Object valueToSet = new Failure(Preconditions.checkNotNull(throwable));
        if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }
    
    @CanIgnoreReturnValue
    protected boolean setFuture(final ListenableFuture<? extends V> future) {
        Preconditions.checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                final Object value = getFutureValue(future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, value)) {
                    complete(this);
                    return true;
                }
                return false;
            }
            else {
                final SetFuture<V> valueToSet = new SetFuture<V>(this, future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                    try {
                        future.addListener(valueToSet, DirectExecutor.INSTANCE);
                    }
                    catch (Throwable t) {
                        Failure failure;
                        try {
                            failure = new Failure(t);
                        }
                        catch (Throwable oomMostLikely) {
                            failure = Failure.FALLBACK_INSTANCE;
                        }
                        AbstractFuture.ATOMIC_HELPER.casValue(this, valueToSet, failure);
                    }
                    return true;
                }
                localValue = this.value;
            }
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation)localValue).wasInterrupted);
        }
        return false;
    }
    
    private static Object getFutureValue(final ListenableFuture<?> future) {
        if (future instanceof Trusted) {
            Object v = ((AbstractFuture)future).value;
            if (v instanceof Cancellation) {
                final Cancellation c = (Cancellation)v;
                if (c.wasInterrupted) {
                    v = ((c.cause != null) ? new Cancellation(false, c.cause) : Cancellation.CAUSELESS_CANCELLED);
                }
            }
            return Objects.requireNonNull(v);
        }
        if (future instanceof InternalFutureFailureAccess) {
            final Throwable throwable = InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess)future);
            if (throwable != null) {
                return new Failure(throwable);
            }
        }
        final boolean wasCancelled = future.isCancelled();
        if (!AbstractFuture.GENERATE_CANCELLATION_CAUSES & wasCancelled) {
            return Objects.requireNonNull(Cancellation.CAUSELESS_CANCELLED);
        }
        try {
            final Object v2 = getUninterruptibly(future);
            if (wasCancelled) {
                final boolean wasInterrupted = false;
                final String value = String.valueOf(future);
                return new Cancellation(wasInterrupted, new IllegalArgumentException(new StringBuilder(84 + String.valueOf(value).length()).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(value).toString()));
            }
            return (v2 == null) ? AbstractFuture.NULL : v2;
        }
        catch (ExecutionException exception) {
            if (wasCancelled) {
                final boolean wasInterrupted2 = false;
                final String value2 = String.valueOf(future);
                return new Cancellation(wasInterrupted2, new IllegalArgumentException(new StringBuilder(84 + String.valueOf(value2).length()).append("get() did not throw CancellationException, despite reporting isCancelled() == true: ").append(value2).toString(), exception));
            }
            return new Failure(exception.getCause());
        }
        catch (CancellationException cancellation) {
            if (!wasCancelled) {
                final String value3 = String.valueOf(future);
                return new Failure(new IllegalArgumentException(new StringBuilder(77 + String.valueOf(value3).length()).append("get() threw CancellationException, despite reporting isCancelled() == false: ").append(value3).toString(), cancellation));
            }
            return new Cancellation(false, cancellation);
        }
        catch (Throwable t) {
            return new Failure(t);
        }
    }
    
    @ParametricNullness
    private static <V> V getUninterruptibly(final Future<V> future) throws ExecutionException {
        boolean interrupted = false;
        try {
            return future.get();
        }
        catch (InterruptedException e) {
            interrupted = true;
            return future.get();
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private static void complete(final AbstractFuture<?> param) {
        AbstractFuture<?> future = param;
        Listener next = null;
    Label_0004:
        while (true) {
            future.releaseWaiters();
            future.afterDone();
            next = future.clearListeners(next);
            future = null;
            while (next != null) {
                final Listener curr = next;
                next = next.next;
                final Runnable task = Objects.requireNonNull(curr.task);
                if (task instanceof SetFuture) {
                    final SetFuture<?> setFuture = (SetFuture<?>)task;
                    future = setFuture.owner;
                    if (future.value != setFuture) {
                        continue;
                    }
                    final Object valueToSet = getFutureValue(setFuture.future);
                    if (AbstractFuture.ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                        continue Label_0004;
                    }
                    continue;
                }
                else {
                    executeListener(task, Objects.requireNonNull(curr.executor));
                }
            }
            break;
        }
    }
    
    @Beta
    @ForOverride
    protected void afterDone() {
    }
    
    @CheckForNull
    @Override
    protected final Throwable tryInternalFastPathGetFailure() {
        if (this instanceof Trusted) {
            final Object obj = this.value;
            if (obj instanceof Failure) {
                return ((Failure)obj).exception;
            }
        }
        return null;
    }
    
    final void maybePropagateCancellationTo(@CheckForNull final Future<?> related) {
        if (related != null & this.isCancelled()) {
            related.cancel(this.wasInterrupted());
        }
    }
    
    private void releaseWaiters() {
        Waiter head;
        do {
            head = this.waiters;
        } while (!AbstractFuture.ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
        for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
            currentWaiter.unpark();
        }
    }
    
    @CheckForNull
    private Listener clearListeners(@CheckForNull final Listener onto) {
        Listener head;
        do {
            head = this.listeners;
        } while (!AbstractFuture.ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
        Listener reversedList;
        Listener tmp;
        for (reversedList = onto; head != null; head = head.next, tmp.next = reversedList, reversedList = tmp) {
            tmp = head;
        }
        return reversedList;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (this.getClass().getName().startsWith("com.google.common.util.concurrent.")) {
            builder.append(this.getClass().getSimpleName());
        }
        else {
            builder.append(this.getClass().getName());
        }
        builder.append('@').append(Integer.toHexString(System.identityHashCode(this))).append("[status=");
        if (this.isCancelled()) {
            builder.append("CANCELLED");
        }
        else if (this.isDone()) {
            this.addDoneString(builder);
        }
        else {
            this.addPendingString(builder);
        }
        return builder.append("]").toString();
    }
    
    @CheckForNull
    protected String pendingToString() {
        if (this instanceof ScheduledFuture) {
            return new StringBuilder(41).append("remaining delay=[").append(((ScheduledFuture)this).getDelay(TimeUnit.MILLISECONDS)).append(" ms]").toString();
        }
        return null;
    }
    
    private void addPendingString(final StringBuilder builder) {
        final int truncateLength = builder.length();
        builder.append("PENDING");
        final Object localValue = this.value;
        if (localValue instanceof SetFuture) {
            builder.append(", setFuture=[");
            this.appendUserObject(builder, ((SetFuture)localValue).future);
            builder.append("]");
        }
        else {
            String pendingDescription;
            try {
                pendingDescription = Strings.emptyToNull(this.pendingToString());
            }
            catch (RuntimeException | StackOverflowError ex) {
                final Throwable t;
                final Throwable e = t;
                final String value = String.valueOf(e.getClass());
                pendingDescription = new StringBuilder(38 + String.valueOf(value).length()).append("Exception thrown from implementation: ").append(value).toString();
            }
            if (pendingDescription != null) {
                builder.append(", info=[").append(pendingDescription).append("]");
            }
        }
        if (this.isDone()) {
            builder.delete(truncateLength, builder.length());
            this.addDoneString(builder);
        }
    }
    
    private void addDoneString(final StringBuilder builder) {
        try {
            final V value = getUninterruptibly((Future<V>)this);
            builder.append("SUCCESS, result=[");
            this.appendResultObject(builder, value);
            builder.append("]");
        }
        catch (ExecutionException e) {
            builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
        }
        catch (CancellationException e3) {
            builder.append("CANCELLED");
        }
        catch (RuntimeException e2) {
            builder.append("UNKNOWN, cause=[").append(e2.getClass()).append(" thrown from get()]");
        }
    }
    
    private void appendResultObject(final StringBuilder builder, @CheckForNull final Object o) {
        if (o == null) {
            builder.append("null");
        }
        else if (o == this) {
            builder.append("this future");
        }
        else {
            builder.append(o.getClass().getName()).append("@").append(Integer.toHexString(System.identityHashCode(o)));
        }
    }
    
    private void appendUserObject(final StringBuilder builder, @CheckForNull final Object o) {
        try {
            if (o == this) {
                builder.append("this future");
            }
            else {
                builder.append(o);
            }
        }
        catch (RuntimeException | StackOverflowError ex) {
            final Throwable t;
            final Throwable e = t;
            builder.append("Exception thrown from implementation: ").append(e.getClass());
        }
    }
    
    private static void executeListener(final Runnable runnable, final Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException e) {
            final Logger log = AbstractFuture.log;
            final Level severe = Level.SEVERE;
            final String value = String.valueOf(runnable);
            final String value2 = String.valueOf(executor);
            log.log(severe, new StringBuilder(57 + String.valueOf(value).length() + String.valueOf(value2).length()).append("RuntimeException while executing runnable ").append(value).append(" with executor ").append(value2).toString(), e);
        }
    }
    
    private static CancellationException cancellationExceptionWithCause(final String message, @CheckForNull final Throwable cause) {
        final CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
    
    static {
        boolean generateCancellationCauses;
        try {
            generateCancellationCauses = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        }
        catch (SecurityException e) {
            generateCancellationCauses = false;
        }
        GENERATE_CANCELLATION_CAUSES = generateCancellationCauses;
        log = Logger.getLogger(AbstractFuture.class.getName());
        Throwable thrownUnsafeFailure = null;
        Throwable thrownAtomicReferenceFieldUpdaterFailure = null;
        AtomicHelper helper;
        try {
            helper = new UnsafeAtomicHelper();
        }
        catch (Throwable unsafeFailure) {
            thrownUnsafeFailure = unsafeFailure;
            try {
                helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), (AtomicReferenceFieldUpdater<AbstractFuture, Waiter>)AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), (AtomicReferenceFieldUpdater<AbstractFuture, Listener>)AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), (AtomicReferenceFieldUpdater<AbstractFuture, Object>)AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            }
            catch (Throwable atomicReferenceFieldUpdaterFailure) {
                thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
                helper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = helper;
        final Class<?> ensureLoaded = LockSupport.class;
        if (thrownAtomicReferenceFieldUpdaterFailure != null) {
            AbstractFuture.log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
            AbstractFuture.log.log(Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
        }
        NULL = new Object();
    }
    
    abstract static class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V>
    {
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public final V get() throws InterruptedException, ExecutionException {
            return super.get();
        }
        
        @ParametricNullness
        @CanIgnoreReturnValue
        @Override
        public final V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(timeout, unit);
        }
        
        @Override
        public final boolean isDone() {
            return super.isDone();
        }
        
        @Override
        public final boolean isCancelled() {
            return super.isCancelled();
        }
        
        @Override
        public final void addListener(final Runnable listener, final Executor executor) {
            super.addListener(listener, executor);
        }
        
        @CanIgnoreReturnValue
        @Override
        public final boolean cancel(final boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }
    
    private static final class Waiter
    {
        static final Waiter TOMBSTONE;
        @CheckForNull
        volatile Thread thread;
        @CheckForNull
        volatile Waiter next;
        
        Waiter(final boolean unused) {
        }
        
        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }
        
        void setNext(@CheckForNull final Waiter next) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next);
        }
        
        void unpark() {
            final Thread w = this.thread;
            if (w != null) {
                this.thread = null;
                LockSupport.unpark(w);
            }
        }
        
        static {
            TOMBSTONE = new Waiter(false);
        }
    }
    
    private static final class Listener
    {
        static final Listener TOMBSTONE;
        @CheckForNull
        final Runnable task;
        @CheckForNull
        final Executor executor;
        @CheckForNull
        Listener next;
        
        Listener(final Runnable task, final Executor executor) {
            this.task = task;
            this.executor = executor;
        }
        
        Listener() {
            this.task = null;
            this.executor = null;
        }
        
        static {
            TOMBSTONE = new Listener();
        }
    }
    
    private static final class Failure
    {
        static final Failure FALLBACK_INSTANCE;
        final Throwable exception;
        
        Failure(final Throwable exception) {
            this.exception = Preconditions.checkNotNull(exception);
        }
        
        static {
            FALLBACK_INSTANCE = new Failure(new Throwable() {
                @Override
                public synchronized Throwable fillInStackTrace() {
                    return this;
                }
            });
        }
    }
    
    private static final class Cancellation
    {
        @CheckForNull
        static final Cancellation CAUSELESS_INTERRUPTED;
        @CheckForNull
        static final Cancellation CAUSELESS_CANCELLED;
        final boolean wasInterrupted;
        @CheckForNull
        final Throwable cause;
        
        Cancellation(final boolean wasInterrupted, @CheckForNull final Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
        
        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
            }
            else {
                CAUSELESS_CANCELLED = new Cancellation(false, null);
                CAUSELESS_INTERRUPTED = new Cancellation(true, null);
            }
        }
    }
    
    private static final class SetFuture<V> implements Runnable
    {
        final AbstractFuture<V> owner;
        final ListenableFuture<? extends V> future;
        
        SetFuture(final AbstractFuture<V> owner, final ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }
        
        @Override
        public void run() {
            if (((AbstractFuture<Object>)this.owner).value != this) {
                return;
            }
            final Object valueToSet = getFutureValue(this.future);
            if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
                complete(this.owner);
            }
        }
    }
    
    private abstract static class AtomicHelper
    {
        abstract void putThread(final Waiter p0, final Thread p1);
        
        abstract void putNext(final Waiter p0, @CheckForNull final Waiter p1);
        
        abstract boolean casWaiters(final AbstractFuture<?> p0, @CheckForNull final Waiter p1, @CheckForNull final Waiter p2);
        
        abstract boolean casListeners(final AbstractFuture<?> p0, @CheckForNull final Listener p1, final Listener p2);
        
        abstract boolean casValue(final AbstractFuture<?> p0, @CheckForNull final Object p1, final Object p2);
    }
    
    private static final class UnsafeAtomicHelper extends AtomicHelper
    {
        static final Unsafe UNSAFE;
        static final long LISTENERS_OFFSET;
        static final long WAITERS_OFFSET;
        static final long VALUE_OFFSET;
        static final long WAITER_THREAD_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            UnsafeAtomicHelper.UNSAFE.putObject(waiter, UnsafeAtomicHelper.WAITER_THREAD_OFFSET, newValue);
        }
        
        @Override
        void putNext(final Waiter waiter, @CheckForNull final Waiter newValue) {
            UnsafeAtomicHelper.UNSAFE.putObject(waiter, UnsafeAtomicHelper.WAITER_NEXT_OFFSET, newValue);
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, @CheckForNull final Waiter expect, @CheckForNull final Waiter update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.WAITERS_OFFSET, expect, update);
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, @CheckForNull final Listener expect, final Listener update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.LISTENERS_OFFSET, expect, update);
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, @CheckForNull final Object expect, final Object update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.VALUE_OFFSET, expect, update);
        }
        
        static {
            Unsafe unsafe = null;
            try {
                unsafe = Unsafe.getUnsafe();
            }
            catch (SecurityException tryReflectionInstead) {
                try {
                    unsafe = AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                        @Override
                        public Unsafe run() throws Exception {
                            final Class<Unsafe> k = Unsafe.class;
                            for (final Field f : k.getDeclaredFields()) {
                                f.setAccessible(true);
                                final Object x = f.get(null);
                                if (k.isInstance(x)) {
                                    return k.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                }
                catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            }
            try {
                final Class<?> abstractFuture = AbstractFuture.class;
                WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            }
            catch (Exception e2) {
                Throwables.throwIfUnchecked(e2);
                throw new RuntimeException(e2);
            }
        }
    }
    
    private static final class SafeAtomicHelper extends AtomicHelper
    {
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        
        SafeAtomicHelper(final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }
        
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }
        
        @Override
        void putNext(final Waiter waiter, @CheckForNull final Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, @CheckForNull final Waiter expect, @CheckForNull final Waiter update) {
            return this.waitersUpdater.compareAndSet(future, expect, update);
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, @CheckForNull final Listener expect, final Listener update) {
            return this.listenersUpdater.compareAndSet(future, expect, update);
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, @CheckForNull final Object expect, final Object update) {
            return this.valueUpdater.compareAndSet(future, expect, update);
        }
    }
    
    private static final class SynchronizedHelper extends AtomicHelper
    {
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            waiter.thread = newValue;
        }
        
        @Override
        void putNext(final Waiter waiter, @CheckForNull final Waiter newValue) {
            waiter.next = newValue;
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, @CheckForNull final Waiter expect, @CheckForNull final Waiter update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).waiters == expect) {
                    ((AbstractFuture<Object>)future).waiters = update;
                    return true;
                }
                return false;
            }
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, @CheckForNull final Listener expect, final Listener update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).listeners == expect) {
                    ((AbstractFuture<Object>)future).listeners = update;
                    return true;
                }
                return false;
            }
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, @CheckForNull final Object expect, final Object update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).value == expect) {
                    ((AbstractFuture<Object>)future).value = update;
                    return true;
                }
                return false;
            }
        }
    }
    
    interface Trusted<V> extends ListenableFuture<V>
    {
    }
}
