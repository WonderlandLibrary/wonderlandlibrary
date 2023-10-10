// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Suppliers
{
    private Suppliers() {
    }
    
    public static <F, T> Supplier<T> compose(final Function<? super F, T> function, final Supplier<F> supplier) {
        return new SupplierComposition<Object, T>(function, supplier);
    }
    
    public static <T> Supplier<T> memoize(final Supplier<T> delegate) {
        if (delegate instanceof NonSerializableMemoizingSupplier || delegate instanceof MemoizingSupplier) {
            return delegate;
        }
        return (delegate instanceof Serializable) ? new MemoizingSupplier<T>((Supplier<Object>)delegate) : new NonSerializableMemoizingSupplier<T>((Supplier<Object>)delegate);
    }
    
    public static <T> Supplier<T> memoizeWithExpiration(final Supplier<T> delegate, final long duration, final TimeUnit unit) {
        return new ExpiringMemoizingSupplier<T>(delegate, duration, unit);
    }
    
    public static <T> Supplier<T> ofInstance(@ParametricNullness final T instance) {
        return new SupplierOfInstance<T>(instance);
    }
    
    public static <T> Supplier<T> synchronizedSupplier(final Supplier<T> delegate) {
        return new ThreadSafeSupplier<T>(delegate);
    }
    
    public static <T> Function<Supplier<T>, T> supplierFunction() {
        final SupplierFunction<T> sf = (SupplierFunction<T>)SupplierFunctionImpl.INSTANCE;
        return sf;
    }
    
    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable
    {
        final Function<? super F, T> function;
        final Supplier<F> supplier;
        private static final long serialVersionUID = 0L;
        
        SupplierComposition(final Function<? super F, T> function, final Supplier<F> supplier) {
            this.function = Preconditions.checkNotNull(function);
            this.supplier = Preconditions.checkNotNull(supplier);
        }
        
        @ParametricNullness
        @Override
        public T get() {
            return this.function.apply((Object)this.supplier.get());
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof SupplierComposition) {
                final SupplierComposition<?, ?> that = (SupplierComposition<?, ?>)obj;
                return this.function.equals(that.function) && this.supplier.equals(that.supplier);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.function, this.supplier);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.function);
            final String value2 = String.valueOf(this.supplier);
            return new StringBuilder(21 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Suppliers.compose(").append(value).append(", ").append(value2).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class MemoizingSupplier<T> implements Supplier<T>, Serializable
    {
        final Supplier<T> delegate;
        transient volatile boolean initialized;
        @CheckForNull
        transient T value;
        private static final long serialVersionUID = 0L;
        
        MemoizingSupplier(final Supplier<T> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @ParametricNullness
        @Override
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        final T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }
            return NullnessCasts.uncheckedCastNullableTToT(this.value);
        }
        
        @Override
        public String toString() {
            Object obj;
            if (this.initialized) {
                final String value = String.valueOf(this.value);
                obj = new StringBuilder(25 + String.valueOf(value).length()).append("<supplier that returned ").append(value).append(">").toString();
            }
            else {
                obj = this.delegate;
            }
            final String value2 = String.valueOf(obj);
            return new StringBuilder(19 + String.valueOf(value2).length()).append("Suppliers.memoize(").append(value2).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class NonSerializableMemoizingSupplier<T> implements Supplier<T>
    {
        @CheckForNull
        volatile Supplier<T> delegate;
        volatile boolean initialized;
        @CheckForNull
        T value;
        
        NonSerializableMemoizingSupplier(final Supplier<T> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @ParametricNullness
        @Override
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        final T t = java.util.Objects.requireNonNull(this.delegate).get();
                        this.value = t;
                        this.initialized = true;
                        this.delegate = null;
                        return t;
                    }
                }
            }
            return NullnessCasts.uncheckedCastNullableTToT(this.value);
        }
        
        @Override
        public String toString() {
            final Supplier<T> delegate = this.delegate;
            Object string;
            if (delegate == null) {
                final String value = String.valueOf(this.value);
                string = new StringBuilder(25 + String.valueOf(value).length()).append("<supplier that returned ").append(value).append(">").toString();
            }
            else {
                string = delegate;
            }
            final String value2 = String.valueOf(string);
            return new StringBuilder(19 + String.valueOf(value2).length()).append("Suppliers.memoize(").append(value2).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable
    {
        final Supplier<T> delegate;
        final long durationNanos;
        @CheckForNull
        transient volatile T value;
        transient volatile long expirationNanos;
        private static final long serialVersionUID = 0L;
        
        ExpiringMemoizingSupplier(final Supplier<T> delegate, final long duration, final TimeUnit unit) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument(duration > 0L, "duration (%s %s) must be > 0", duration, unit);
        }
        
        @ParametricNullness
        @Override
        public T get() {
            long nanos = this.expirationNanos;
            final long now = Platform.systemNanoTime();
            if (nanos == 0L || now - nanos >= 0L) {
                synchronized (this) {
                    if (nanos == this.expirationNanos) {
                        final T t = this.delegate.get();
                        this.value = t;
                        nanos = now + this.durationNanos;
                        this.expirationNanos = ((nanos == 0L) ? 1L : nanos);
                        return t;
                    }
                }
            }
            return NullnessCasts.uncheckedCastNullableTToT(this.value);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.delegate);
            return new StringBuilder(62 + String.valueOf(value).length()).append("Suppliers.memoizeWithExpiration(").append(value).append(", ").append(this.durationNanos).append(", NANOS)").toString();
        }
    }
    
    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable
    {
        @ParametricNullness
        final T instance;
        private static final long serialVersionUID = 0L;
        
        SupplierOfInstance(@ParametricNullness final T instance) {
            this.instance = instance;
        }
        
        @ParametricNullness
        @Override
        public T get() {
            return this.instance;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            if (obj instanceof SupplierOfInstance) {
                final SupplierOfInstance<?> that = (SupplierOfInstance<?>)obj;
                return Objects.equal(this.instance, that.instance);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.instance);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.instance);
            return new StringBuilder(22 + String.valueOf(value).length()).append("Suppliers.ofInstance(").append(value).append(")").toString();
        }
    }
    
    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable
    {
        final Supplier<T> delegate;
        private static final long serialVersionUID = 0L;
        
        ThreadSafeSupplier(final Supplier<T> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @ParametricNullness
        @Override
        public T get() {
            synchronized (this.delegate) {
                return this.delegate.get();
            }
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.delegate);
            return new StringBuilder(32 + String.valueOf(value).length()).append("Suppliers.synchronizedSupplier(").append(value).append(")").toString();
        }
    }
    
    private enum SupplierFunctionImpl implements SupplierFunction<Object>
    {
        INSTANCE;
        
        @CheckForNull
        @Override
        public Object apply(final Supplier<Object> input) {
            return input.get();
        }
        
        @Override
        public String toString() {
            return "Suppliers.supplierFunction()";
        }
        
        private static /* synthetic */ SupplierFunctionImpl[] $values() {
            return new SupplierFunctionImpl[] { SupplierFunctionImpl.INSTANCE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    private interface SupplierFunction<T> extends Function<Supplier<T>, T>
    {
    }
}
