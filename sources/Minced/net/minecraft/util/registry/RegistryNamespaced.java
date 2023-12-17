// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.registry;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import java.util.Map;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.util.IObjectIntIterable;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V>
{
    protected final IntIdentityHashBiMap<V> underlyingIntegerMap;
    protected final Map<V, K> inverseObjectRegistry;
    
    public RegistryNamespaced() {
        this.underlyingIntegerMap = new IntIdentityHashBiMap<V>(256);
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
    
    @Nullable
    @Override
    public V getObject(@Nullable final K name) {
        return super.getObject(name);
    }
    
    @Nullable
    public K getNameForObject(final V value) {
        return this.inverseObjectRegistry.get(value);
    }
    
    @Override
    public boolean containsKey(final K key) {
        return super.containsKey(key);
    }
    
    public int getIDForObject(@Nullable final V value) {
        return this.underlyingIntegerMap.getId(value);
    }
    
    @Nullable
    public V getObjectById(final int id) {
        return this.underlyingIntegerMap.get(id);
    }
    
    @Override
    public Iterator<V> iterator() {
        return this.underlyingIntegerMap.iterator();
    }
}
