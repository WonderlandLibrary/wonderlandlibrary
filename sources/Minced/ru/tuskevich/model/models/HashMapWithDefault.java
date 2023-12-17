// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.model.models;

import java.util.HashMap;

public class HashMapWithDefault<K, V> extends HashMap<K, V>
{
    private static final long serialVersionUID = 5995791692010816132L;
    private V defaultValue;
    
    public void setDefault(final V value) {
        this.defaultValue = value;
    }
    
    public V getDefault() {
        return this.defaultValue;
    }
    
    @Override
    public V get(final Object key) {
        return this.containsKey(key) ? super.get(key) : this.defaultValue;
    }
}
