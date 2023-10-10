// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface BiMap<K, V> extends Map<K, V>
{
    @CheckForNull
    @CanIgnoreReturnValue
    V put(@ParametricNullness final K p0, @ParametricNullness final V p1);
    
    @CheckForNull
    @CanIgnoreReturnValue
    V forcePut(@ParametricNullness final K p0, @ParametricNullness final V p1);
    
    void putAll(final Map<? extends K, ? extends V> p0);
    
    Set<V> values();
    
    BiMap<V, K> inverse();
}
