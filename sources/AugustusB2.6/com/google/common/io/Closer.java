// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.common.base.Throwables;
import java.io.IOException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import javax.annotation.CheckForNull;
import java.util.Deque;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.Closeable;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class Closer implements Closeable
{
    private static final Suppressor SUPPRESSOR;
    @VisibleForTesting
    final Suppressor suppressor;
    private final Deque<Closeable> stack;
    @CheckForNull
    private Throwable thrown;
    
    public static Closer create() {
        return new Closer(Closer.SUPPRESSOR);
    }
    
    @VisibleForTesting
    Closer(final Suppressor suppressor) {
        this.stack = new ArrayDeque<Closeable>(4);
        this.suppressor = Preconditions.checkNotNull(suppressor);
    }
    
    @ParametricNullness
    @CanIgnoreReturnValue
    public <C extends Closeable> C register(@ParametricNullness final C closeable) {
        if (closeable != null) {
            this.stack.addFirst(closeable);
        }
        return closeable;
    }
    
    public RuntimeException rethrow(final Throwable e) throws IOException {
        Preconditions.checkNotNull(e);
        Throwables.propagateIfPossible(this.thrown = e, IOException.class);
        throw new RuntimeException(e);
    }
    
    public <X extends Exception> RuntimeException rethrow(final Throwable e, final Class<X> declaredType) throws IOException, X, Exception {
        Preconditions.checkNotNull(e);
        Throwables.propagateIfPossible(this.thrown = e, IOException.class);
        Throwables.propagateIfPossible(e, declaredType);
        throw new RuntimeException(e);
    }
    
    public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(final Throwable e, final Class<X1> declaredType1, final Class<X2> declaredType2) throws IOException, X1, X2, Exception {
        Preconditions.checkNotNull(e);
        Throwables.propagateIfPossible(this.thrown = e, IOException.class);
        Throwables.propagateIfPossible(e, declaredType1, declaredType2);
        throw new RuntimeException(e);
    }
    
    @Override
    public void close() throws IOException {
        Throwable throwable = this.thrown;
        while (!this.stack.isEmpty()) {
            final Closeable closeable = this.stack.removeFirst();
            try {
                closeable.close();
            }
            catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                }
                else {
                    this.suppressor.suppress(closeable, throwable, e);
                }
            }
        }
        if (this.thrown == null && throwable != null) {
            Throwables.propagateIfPossible(throwable, IOException.class);
            throw new AssertionError((Object)throwable);
        }
    }
    
    static {
        final SuppressingSuppressor suppressingSuppressor = SuppressingSuppressor.tryCreate();
        SUPPRESSOR = ((suppressingSuppressor == null) ? LoggingSuppressor.INSTANCE : suppressingSuppressor);
    }
    
    @VisibleForTesting
    static final class LoggingSuppressor implements Suppressor
    {
        static final LoggingSuppressor INSTANCE;
        
        @Override
        public void suppress(final Closeable closeable, final Throwable thrown, final Throwable suppressed) {
            final Logger logger = Closeables.logger;
            final Level warning = Level.WARNING;
            final String value = String.valueOf(closeable);
            logger.log(warning, new StringBuilder(42 + String.valueOf(value).length()).append("Suppressing exception thrown when closing ").append(value).toString(), suppressed);
        }
        
        static {
            INSTANCE = new LoggingSuppressor();
        }
    }
    
    @VisibleForTesting
    static final class SuppressingSuppressor implements Suppressor
    {
        private final Method addSuppressed;
        
        @CheckForNull
        static SuppressingSuppressor tryCreate() {
            Method addSuppressed;
            try {
                addSuppressed = Throwable.class.getMethod("addSuppressed", Throwable.class);
            }
            catch (Throwable e) {
                return null;
            }
            return new SuppressingSuppressor(addSuppressed);
        }
        
        private SuppressingSuppressor(final Method addSuppressed) {
            this.addSuppressed = addSuppressed;
        }
        
        @Override
        public void suppress(final Closeable closeable, final Throwable thrown, final Throwable suppressed) {
            if (thrown == suppressed) {
                return;
            }
            try {
                this.addSuppressed.invoke(thrown, suppressed);
            }
            catch (Throwable e) {
                LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
            }
        }
    }
    
    @VisibleForTesting
    interface Suppressor
    {
        void suppress(final Closeable p0, final Throwable p1, final Throwable p2);
    }
}
