// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.cache;

import java.io.Serializable;
import me.gong.mcleaks.util.google.common.util.concurrent.ListenableFutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import me.gong.mcleaks.util.google.common.base.Supplier;
import me.gong.mcleaks.util.google.common.base.Function;
import java.util.Map;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.util.concurrent.Futures;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.util.concurrent.ListenableFuture;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class CacheLoader<K, V>
{
    protected CacheLoader() {
    }
    
    public abstract V load(final K p0) throws Exception;
    
    @GwtIncompatible
    public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(oldValue);
        return Futures.immediateFuture(this.load(key));
    }
    
    public Map<K, V> loadAll(final Iterable<? extends K> keys) throws Exception {
        throw new UnsupportedLoadingOperationException();
    }
    
    public static <K, V> CacheLoader<K, V> from(final Function<K, V> function) {
        return new FunctionToCacheLoader<K, V>(function);
    }
    
    public static <V> CacheLoader<Object, V> from(final Supplier<V> supplier) {
        return (CacheLoader<Object, V>)new SupplierToCacheLoader((Supplier<Object>)supplier);
    }
    
    @GwtIncompatible
    public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> loader, final Executor executor) {
        Preconditions.checkNotNull(loader);
        Preconditions.checkNotNull(executor);
        return new CacheLoader<K, V>() {
            @Override
            public V load(final K key) throws Exception {
                return loader.load(key);
            }
            
            @Override
            public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
                final ListenableFutureTask<V> task = ListenableFutureTask.create((Callable<V>)new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return (V)loader.reload(key, oldValue).get();
                    }
                });
                executor.execute(task);
                return task;
            }
            
            @Override
            public Map<K, V> loadAll(final Iterable<? extends K> keys) throws Exception {
                return loader.loadAll(keys);
            }
        };
    }
    
    private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable
    {
        private final Function<K, V> computingFunction;
        private static final long serialVersionUID = 0L;
        
        public FunctionToCacheLoader(final Function<K, V> computingFunction) {
            this.computingFunction = Preconditions.checkNotNull(computingFunction);
        }
        
        @Override
        public V load(final K key) {
            return this.computingFunction.apply(Preconditions.checkNotNull(key));
        }
    }
    
    private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable
    {
        private final Supplier<V> computingSupplier;
        private static final long serialVersionUID = 0L;
        
        public SupplierToCacheLoader(final Supplier<V> computingSupplier) {
            this.computingSupplier = Preconditions.checkNotNull(computingSupplier);
        }
        
        @Override
        public V load(final Object key) {
            Preconditions.checkNotNull(key);
            return this.computingSupplier.get();
        }
    }
    
    public static final class UnsupportedLoadingOperationException extends UnsupportedOperationException
    {
        UnsupportedLoadingOperationException() {
        }
    }
    
    public static final class InvalidCacheLoadException extends RuntimeException
    {
        public InvalidCacheLoadException(final String message) {
            super(message);
        }
    }
}
