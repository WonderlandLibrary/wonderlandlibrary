/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V>
extends RegistryNamespaced<K, V> {
    private final K defaultValueKey;
    private V defaultValue;

    public RegistryNamespacedDefaultedByKey(K defaultValueKeyIn) {
        this.defaultValueKey = defaultValueKeyIn;
    }

    @Override
    public void register(int id, K key, V value) {
        if (this.defaultValueKey.equals(key)) {
            this.defaultValue = value;
        }
        super.register(id, key, value);
    }

    public void validateKey() {
        Validate.notNull(this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey, new Object[0]);
    }

    @Override
    public int getIDForObject(V value) {
        int i = super.getIDForObject(value);
        return i == -1 ? super.getIDForObject(this.defaultValue) : i;
    }

    @Override
    @Nonnull
    public K getNameForObject(V value) {
        Object k = super.getNameForObject(value);
        return k == null ? this.defaultValueKey : k;
    }

    @Override
    @Nonnull
    public V getObject(@Nullable K name) {
        Object v = super.getObject(name);
        return v == null ? this.defaultValue : v;
    }

    @Override
    @Nonnull
    public V getObjectById(int id) {
        Object v = super.getObjectById(id);
        return v == null ? this.defaultValue : v;
    }

    @Override
    @Nonnull
    public V getRandomObject(Random random) {
        Object v = super.getRandomObject(random);
        return v == null ? this.defaultValue : v;
    }
}

