// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Arrays;
import org.apache.logging.log4j.util.BiConsumer;
import java.util.Objects;
import java.util.HashMap;
import org.apache.logging.log4j.util.TriConsumer;
import java.util.Map;
import java.util.Comparator;
import org.apache.logging.log4j.util.StringMap;

public class JdkMapAdapterStringMap implements StringMap
{
    private static final long serialVersionUID = -7348247784983193612L;
    private static final String FROZEN = "Frozen collection cannot be modified";
    private static final Comparator<? super String> NULL_FIRST_COMPARATOR;
    private final Map<String, String> map;
    private boolean immutable;
    private transient String[] sortedKeys;
    private static TriConsumer<String, String, Map<String, String>> PUT_ALL;
    
    public JdkMapAdapterStringMap() {
        this(new HashMap<String, String>());
    }
    
    public JdkMapAdapterStringMap(final Map<String, String> map) {
        this.immutable = false;
        this.map = Objects.requireNonNull(map, "map");
    }
    
    @Override
    public Map<String, String> toMap() {
        return this.map;
    }
    
    private void assertNotFrozen() {
        if (this.immutable) {
            throw new UnsupportedOperationException("Frozen collection cannot be modified");
        }
    }
    
    @Override
    public boolean containsKey(final String key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            action.accept(keys[i], (Object)this.map.get(keys[i]));
        }
    }
    
    @Override
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            action.accept(keys[i], (Object)this.map.get(keys[i]), state);
        }
    }
    
    private String[] getSortedKeys() {
        if (this.sortedKeys == null) {
            Arrays.sort(this.sortedKeys = this.map.keySet().toArray(new String[this.map.size()]), JdkMapAdapterStringMap.NULL_FIRST_COMPARATOR);
        }
        return this.sortedKeys;
    }
    
    @Override
    public <V> V getValue(final String key) {
        return (V)this.map.get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public void clear() {
        if (this.map.isEmpty()) {
            return;
        }
        this.assertNotFrozen();
        this.map.clear();
        this.sortedKeys = null;
    }
    
    @Override
    public void freeze() {
        this.immutable = true;
    }
    
    @Override
    public boolean isFrozen() {
        return this.immutable;
    }
    
    @Override
    public void putAll(final ReadOnlyStringMap source) {
        this.assertNotFrozen();
        source.forEach((TriConsumer<String, ? super Object, Map<String, String>>)JdkMapAdapterStringMap.PUT_ALL, this.map);
        this.sortedKeys = null;
    }
    
    @Override
    public void putValue(final String key, final Object value) {
        this.assertNotFrozen();
        this.map.put(key, (value == null) ? null : String.valueOf(value));
        this.sortedKeys = null;
    }
    
    @Override
    public void remove(final String key) {
        if (!this.map.containsKey(key)) {
            return;
        }
        this.assertNotFrozen();
        this.map.remove(key);
        this.sortedKeys = null;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(this.map.size() * 13);
        result.append('{');
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(keys[i]).append('=').append(this.map.get(keys[i]));
        }
        result.append('}');
        return result.toString();
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof JdkMapAdapterStringMap)) {
            return false;
        }
        final JdkMapAdapterStringMap other = (JdkMapAdapterStringMap)object;
        return this.map.equals(other.map) && this.immutable == other.immutable;
    }
    
    @Override
    public int hashCode() {
        return this.map.hashCode() + (this.immutable ? 31 : 0);
    }
    
    static {
        NULL_FIRST_COMPARATOR = ((left, right) -> {
            if (left == null) {
                return -1;
            }
            else if (right == null) {
                return 1;
            }
            else {
                return left.compareTo(right);
            }
        });
        final String s;
        JdkMapAdapterStringMap.PUT_ALL = ((key, value, stringStringMap) -> s = stringStringMap.put(key, value));
    }
}
