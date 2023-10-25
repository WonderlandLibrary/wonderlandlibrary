package net.minecraft.util;

import net.minecraft.util.RegistrySimple;

public class RegistryDefaulted<K, V>
extends RegistrySimple<K, V> {
    private final V defaultObject;

    public RegistryDefaulted(V defaultObjectIn) {
        this.defaultObject = defaultObjectIn;
    }

    public V getObject(K name) {
        Object v = super.getObject(name);
        return (V)(v == null ? this.defaultObject : v);
    }
}
