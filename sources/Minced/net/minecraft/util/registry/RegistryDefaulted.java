// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegistryDefaulted<K, V> extends RegistrySimple<K, V>
{
    private final V defaultObject;
    
    public RegistryDefaulted(final V defaultObjectIn) {
        this.defaultObject = defaultObjectIn;
    }
    
    @Nonnull
    @Override
    public V getObject(@Nullable final K name) {
        final V v = super.getObject(name);
        return (v == null) ? this.defaultObject : v;
    }
}
