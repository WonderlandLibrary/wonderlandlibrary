/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.RegistryNamespaced;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V>
extends RegistryNamespaced<K, V> {
    private final K defaultValueKey;
    private V defaultValue;

    public RegistryNamespacedDefaultedByKey(K p_i46017_1_) {
        this.defaultValueKey = p_i46017_1_;
    }

    @Override
    public void register(int id, K p_177775_2_, V p_177775_3_) {
        if (this.defaultValueKey.equals(p_177775_2_)) {
            this.defaultValue = p_177775_3_;
        }
        super.register(id, p_177775_2_, p_177775_3_);
    }

    public void validateKey() {
        Validate.notNull(this.defaultValueKey);
    }

    @Override
    public V getObject(K name) {
        Object v;
        Object v2 = super.getObject(name);
        if (v2 == null) {
            v = this.defaultValue;
            return v;
        }
        v = v2;
        return v;
    }

    @Override
    public V getObjectById(int id) {
        Object v;
        Object v2 = super.getObjectById(id);
        if (v2 == null) {
            v = this.defaultValue;
            return v;
        }
        v = v2;
        return v;
    }
}

