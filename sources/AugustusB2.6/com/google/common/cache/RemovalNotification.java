// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.AbstractMap;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class RemovalNotification<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
{
    private final RemovalCause cause;
    private static final long serialVersionUID = 0L;
    
    public static <K, V> RemovalNotification<K, V> create(@CheckForNull final K key, @CheckForNull final V value, final RemovalCause cause) {
        return new RemovalNotification<K, V>(key, value, cause);
    }
    
    private RemovalNotification(@CheckForNull final K key, @CheckForNull final V value, final RemovalCause cause) {
        super(key, value);
        this.cause = Preconditions.checkNotNull(cause);
    }
    
    public RemovalCause getCause() {
        return this.cause;
    }
    
    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }
}
