// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Collection;
import java.util.Set;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V>
{
    @Nullable
    @CanIgnoreReturnValue
    V put(@Nullable final K p0, @Nullable final V p1);
    
    @Nullable
    @CanIgnoreReturnValue
    V forcePut(@Nullable final K p0, @Nullable final V p1);
    
    void putAll(final Map<? extends K, ? extends V> p0);
    
    Set<V> values();
    
    BiMap<V, K> inverse();
}
