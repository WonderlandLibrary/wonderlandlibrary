// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.registry;

import java.util.Random;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

public class RegistryNamespacedDefaultedByKey<K, V> extends RegistryNamespaced<K, V>
{
    private final K defaultValueKey;
    private V defaultValue;
    
    public RegistryNamespacedDefaultedByKey(final K defaultValueKeyIn) {
        this.defaultValueKey = defaultValueKeyIn;
    }
    
    @Override
    public void register(final int id, final K key, final V value) {
        if (this.defaultValueKey.equals(key)) {
            this.defaultValue = value;
        }
        super.register(id, key, value);
    }
    
    public void validateKey() {
        Validate.notNull((Object)this.defaultValue, "Missing default of DefaultedMappedRegistry: " + this.defaultValueKey, new Object[0]);
    }
    
    @Override
    public int getIDForObject(final V value) {
        final int i = super.getIDForObject(value);
        return (i == -1) ? super.getIDForObject(this.defaultValue) : i;
    }
    
    @Nonnull
    @Override
    public K getNameForObject(final V value) {
        final K k = super.getNameForObject(value);
        return (k == null) ? this.defaultValueKey : k;
    }
    
    @Nonnull
    @Override
    public V getObject(@Nullable final K name) {
        final V v = super.getObject(name);
        return (v == null) ? this.defaultValue : v;
    }
    
    @Nonnull
    @Override
    public V getObjectById(final int id) {
        final V v = super.getObjectById(id);
        return (v == null) ? this.defaultValue : v;
    }
    
    @Nonnull
    @Override
    public V getRandomObject(final Random random) {
        final V v = super.getRandomObject(random);
        return (v == null) ? this.defaultValue : v;
    }
}
