// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import java.util.concurrent.ConcurrentMap;
import com.google.errorprone.annotations.CheckReturnValue;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CompatibleWith;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use CacheBuilder.newBuilder().build()")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Cache<K, V>
{
    @CheckForNull
    V getIfPresent(@CompatibleWith("K") final Object p0);
    
    V get(final K p0, final Callable<? extends V> p1) throws ExecutionException;
    
    ImmutableMap<K, V> getAllPresent(final Iterable<?> p0);
    
    void put(final K p0, final V p1);
    
    void putAll(final Map<? extends K, ? extends V> p0);
    
    void invalidate(@CompatibleWith("K") final Object p0);
    
    void invalidateAll(final Iterable<?> p0);
    
    void invalidateAll();
    
    @CheckReturnValue
    long size();
    
    @CheckReturnValue
    CacheStats stats();
    
    @CheckReturnValue
    ConcurrentMap<K, V> asMap();
    
    void cleanUp();
}
