/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.registry.RegistrySimple;

public class RegistryDefaulted<K, V>
extends RegistrySimple<K, V> {
    private final V defaultObject;

    public RegistryDefaulted(V defaultObjectIn) {
        this.defaultObject = defaultObjectIn;
    }

    @Override
    @Nonnull
    public V getObject(@Nullable K name) {
        Object v = super.getObject(name);
        return v == null ? this.defaultObject : v;
    }
}

