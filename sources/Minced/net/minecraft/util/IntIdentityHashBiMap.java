// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Arrays;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nullable;

public class IntIdentityHashBiMap<K> implements IObjectIntIterable<K>
{
    private static final Object EMPTY;
    private K[] values;
    private int[] intKeys;
    private K[] byId;
    private int nextFreeIndex;
    private int mapSize;
    
    public IntIdentityHashBiMap(int initialCapacity) {
        initialCapacity /= (int)0.8f;
        this.values = (K[])new Object[initialCapacity];
        this.intKeys = new int[initialCapacity];
        this.byId = (K[])new Object[initialCapacity];
    }
    
    public int getId(@Nullable final K p_186815_1_) {
        return this.getValue(this.getIndex(p_186815_1_, this.hashObject(p_186815_1_)));
    }
    
    @Nullable
    public K get(final int idIn) {
        return (idIn >= 0 && idIn < this.byId.length) ? this.byId[idIn] : null;
    }
    
    private int getValue(final int p_186805_1_) {
        return (p_186805_1_ == -1) ? -1 : this.intKeys[p_186805_1_];
    }
    
    public int add(final K objectIn) {
        final int i = this.nextId();
        this.put(objectIn, i);
        return i;
    }
    
    private int nextId() {
        while (this.nextFreeIndex < this.byId.length && this.byId[this.nextFreeIndex] != null) {
            ++this.nextFreeIndex;
        }
        return this.nextFreeIndex;
    }
    
    private void grow(final int capacity) {
        final K[] ak = this.values;
        final int[] aint = this.intKeys;
        this.values = (K[])new Object[capacity];
        this.intKeys = new int[capacity];
        this.byId = (K[])new Object[capacity];
        this.nextFreeIndex = 0;
        this.mapSize = 0;
        for (int i = 0; i < ak.length; ++i) {
            if (ak[i] != null) {
                this.put(ak[i], aint[i]);
            }
        }
    }
    
    public void put(final K objectIn, final int intKey) {
        final int i = Math.max(intKey, this.mapSize + 1);
        if (i >= this.values.length * 0.8f) {
            int j;
            for (j = this.values.length << 1; j < intKey; j <<= 1) {}
            this.grow(j);
        }
        final int k = this.findEmpty(this.hashObject(objectIn));
        this.values[k] = objectIn;
        this.intKeys[k] = intKey;
        this.byId[intKey] = objectIn;
        ++this.mapSize;
        if (intKey == this.nextFreeIndex) {
            ++this.nextFreeIndex;
        }
    }
    
    private int hashObject(@Nullable final K obectIn) {
        return (MathHelper.hash(System.identityHashCode(obectIn)) & Integer.MAX_VALUE) % this.values.length;
    }
    
    private int getIndex(@Nullable final K objectIn, final int p_186816_2_) {
        for (int i = p_186816_2_; i < this.values.length; ++i) {
            if (this.values[i] == objectIn) {
                return i;
            }
            if (this.values[i] == IntIdentityHashBiMap.EMPTY) {
                return -1;
            }
        }
        for (int j = 0; j < p_186816_2_; ++j) {
            if (this.values[j] == objectIn) {
                return j;
            }
            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return -1;
            }
        }
        return -1;
    }
    
    private int findEmpty(final int p_186806_1_) {
        for (int i = p_186806_1_; i < this.values.length; ++i) {
            if (this.values[i] == IntIdentityHashBiMap.EMPTY) {
                return i;
            }
        }
        for (int j = 0; j < p_186806_1_; ++j) {
            if (this.values[j] == IntIdentityHashBiMap.EMPTY) {
                return j;
            }
        }
        throw new RuntimeException("Overflowed :(");
    }
    
    @Override
    public Iterator<K> iterator() {
        return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
    }
    
    public void clear() {
        Arrays.fill(this.values, null);
        Arrays.fill(this.byId, null);
        this.nextFreeIndex = 0;
        this.mapSize = 0;
    }
    
    public int size() {
        return this.mapSize;
    }
    
    static {
        EMPTY = null;
    }
}
