// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.registry;

import java.util.Set;
import javax.annotation.Nullable;

public interface IRegistry<K, V> extends Iterable<V>
{
    @Nullable
    V getObject(final K p0);
    
    void putObject(final K p0, final V p1);
    
    Set<K> getKeys();
}
