/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.RegistrySimple;

public class RegistryDefaulted<K, V>
extends RegistrySimple<K, V> {
    private final V defaultObject;

    public RegistryDefaulted(V v) {
        this.defaultObject = v;
    }

    @Override
    public V getObject(K k) {
        Object v = super.getObject(k);
        return v == null ? this.defaultObject : v;
    }
}

