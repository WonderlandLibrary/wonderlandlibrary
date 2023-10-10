// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.Set;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntMap;

public class Int2IntBiHashMap implements Int2IntBiMap
{
    private final Int2IntMap map;
    private final Int2IntBiHashMap inverse;
    
    public Int2IntBiHashMap() {
        this.map = new Int2IntOpenHashMap();
        this.inverse = new Int2IntBiHashMap(this);
    }
    
    private Int2IntBiHashMap(final Int2IntBiHashMap inverse) {
        this.map = new Int2IntOpenHashMap();
        this.inverse = inverse;
    }
    
    @Override
    public Int2IntBiMap inverse() {
        return this.inverse;
    }
    
    @Override
    public int put(final int key, final int value) {
        if (this.containsKey(key) && value == this.get(key)) {
            return value;
        }
        Preconditions.checkArgument(!this.containsValue(value), "value already present: %s", new Object[] { value });
        this.map.put(key, value);
        this.inverse.map.put(value, key);
        return this.defaultReturnValue();
    }
    
    @Override
    public boolean remove(final int key, final int value) {
        this.map.remove(key, value);
        return this.inverse.map.remove(key, value);
    }
    
    @Override
    public int get(final int key) {
        return this.map.get(key);
    }
    
    @Override
    public void clear() {
        this.map.clear();
        this.inverse.map.clear();
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Override
    public void defaultReturnValue(final int rv) {
        this.map.defaultReturnValue(rv);
        this.inverse.map.defaultReturnValue(rv);
    }
    
    @Override
    public int defaultReturnValue() {
        return this.map.defaultReturnValue();
    }
    
    @Override
    public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
        return this.map.int2IntEntrySet();
    }
    
    @Override
    public IntSet keySet() {
        return this.map.keySet();
    }
    
    @Override
    public IntSet values() {
        return this.inverse.map.keySet();
    }
    
    @Override
    public boolean containsKey(final int key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(final int value) {
        return this.inverse.map.containsKey(value);
    }
}
