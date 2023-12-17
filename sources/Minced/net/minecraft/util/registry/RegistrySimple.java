// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.registry;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collection;
import java.util.Random;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V>
{
    private static final Logger LOGGER;
    protected final Map<K, V> registryObjects;
    private Object[] values;
    
    public RegistrySimple() {
        this.registryObjects = this.createUnderlyingMap();
    }
    
    protected Map<K, V> createUnderlyingMap() {
        return (Map<K, V>)Maps.newHashMap();
    }
    
    @Nullable
    @Override
    public V getObject(@Nullable final K name) {
        return this.registryObjects.get(name);
    }
    
    @Override
    public void putObject(final K key, final V value) {
        Validate.notNull((Object)key);
        Validate.notNull((Object)value);
        this.values = null;
        if (this.registryObjects.containsKey(key)) {
            RegistrySimple.LOGGER.debug("Adding duplicate key '{}' to registry", (Object)key);
        }
        this.registryObjects.put(key, value);
    }
    
    @Override
    public Set<K> getKeys() {
        return Collections.unmodifiableSet((Set<? extends K>)this.registryObjects.keySet());
    }
    
    @Nullable
    public V getRandomObject(final Random random) {
        if (this.values == null) {
            final Collection<?> collection = this.registryObjects.values();
            if (collection.isEmpty()) {
                return null;
            }
            this.values = collection.toArray(new Object[collection.size()]);
        }
        return (V)this.values[random.nextInt(this.values.length)];
    }
    
    public boolean containsKey(final K key) {
        return this.registryObjects.containsKey(key);
    }
    
    @Override
    public Iterator<V> iterator() {
        return this.registryObjects.values().iterator();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
