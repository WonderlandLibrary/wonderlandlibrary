// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

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
        Validate.notNull((Object)this.defaultValueKey);
    }
    
    @Override
    public V getObject(final K name) {
        final V v = super.getObject(name);
        return (v == null) ? this.defaultValue : v;
    }
    
    @Override
    public V getObjectById(final int id) {
        final V v = super.getObjectById(id);
        return (v == null) ? this.defaultValue : v;
    }
}
