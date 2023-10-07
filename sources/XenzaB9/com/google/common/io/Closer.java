// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.common.base.Throwables;
import java.io.IOException;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.Beta;
import java.io.Closeable;

@Beta
public final class Closer implements Closeable
{
    private static final Suppressor SUPPRESSOR;
    @VisibleForTesting
    final Suppressor suppressor;
    private final Deque<Closeable> stack;
    private Throwable thrown;
    
    public static Closer create() {
        return new Closer(Closer.SUPPRESSOR);
    }
    
    @VisibleForTesting
    Closer(final Suppressor suppressor) {
        this.stack = new ArrayDeque<Closeable>(4);
        this.suppressor = Preconditions.checkNotNull(suppressor);
    }
    
    public <C extends Closeable> C register(@Nullable final C closeable) {
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
            catch (final Throwable e) {
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
        SUPPRESSOR = (SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE);
    }
    
    @VisibleForTesting
    static final class LoggingSuppressor implements Suppressor
    {
        static final LoggingSuppressor INSTANCE;
        
        @Override
        public void suppress(final Closeable closeable, final Throwable thrown, final Throwable suppressed) {
            final Logger logger = Closeables.logger;
            final Level warning = Level.WARNING;
            final String value = String.valueOf(String.valueOf(closeable));
            logger.log(warning, new StringBuilder(42 + value.length()).append("Suppressing exception thrown when closing ").append(value).toString(), suppressed);
        }
        
        static {
            INSTANCE = new LoggingSuppressor();
        }
    }
    
    @VisibleForTesting
    static final class SuppressingSuppressor implements Suppressor
    {
        static final SuppressingSuppressor INSTANCE;
        static final Method addSuppressed;
        
        static boolean isAvailable() {
            return SuppressingSuppressor.addSuppressed != null;
        }
        
        private static Method getAddSuppressed() {
            try {
                return Throwable.class.getMethod("addSuppressed", Throwable.class);
            }
            catch (final Throwable e) {
                return null;
            }
        }
        
        @Override
        public void suppress(final Closeable closeable, final Throwable thrown, final Throwable suppressed) {
            if (thrown == suppressed) {
                return;
            }
            try {
                SuppressingSuppressor.addSuppressed.invoke(thrown, suppressed);
            }
            catch (final Throwable e) {
                LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
            }
        }
        
        static {
            INSTANCE = new SuppressingSuppressor();
            addSuppressed = getAddSuppressed();
        }
    }
    
    @VisibleForTesting
    interface Suppressor
    {
        void suppress(final Closeable p0, final Throwable p1, final Throwable p2);
    }
}
