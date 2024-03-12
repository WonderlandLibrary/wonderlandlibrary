// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Iterator;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import java.util.Map;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final ObjectIntIdentityMap<V> underlyingIntegerMap;
    protected final Map<V, K> inverseObjectRegistry;
    
    public RegistryNamespaced() {
        this.underlyingIntegerMap = new ObjectIntIdentityMap<V>();
        this.inverseObjectRegistry = (Map<V, K>)((BiMap)this.registryObjects).inverse();
    }
    
    public void register(final int id, final K key, final V value) {
        this.underlyingIntegerMap.put(value, id);
        this.putObject(key, value);
    }
    
    @Override
    protected Map<K, V> createUnderlyingMap() {
        return (Map<K, V>)HashBiMap.create();
    }
    
    @Override
    public V getObject(final K name) {
        return super.getObject(name);
    }
    
    public K getNameForObject(final V value) {
        return this.inverseObjectRegistry.get(value);
    }
    
    @Override
    public boolean containsKey(final K key) {
        return super.containsKey(key);
    }
    
    public int getIDForObject(final V value) {
        return this.underlyingIntegerMap.get(value);
    }
    
    public V getObjectById(final int id) {
        return this.underlyingIntegerMap.getByValue(id);
    }
    
    @Override
    public Iterator<V> iterator() {
        return this.underlyingIntegerMap.iterator();
    }
}
