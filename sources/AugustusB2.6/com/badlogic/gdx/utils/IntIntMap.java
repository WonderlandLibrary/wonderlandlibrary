// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

import java.util.NoSuchElementException;
import java.util.Iterator;
import com.badlogic.gdx.math.MathUtils;

public final class IntIntMap implements Iterable<Entry>
{
    public int size;
    int[] keyTable;
    int[] valueTable;
    int capacity;
    int stashSize;
    int zeroValue;
    boolean hasZeroValue;
    private float loadFactor;
    private int hashShift;
    private int mask;
    private int threshold;
    private int stashCapacity;
    private int pushIterations;
    private Entries entries1;
    private Entries entries2;
    
    public IntIntMap() {
        this(51, 0.8f);
    }
    
    private IntIntMap(int initialCapacity, final float loadFactor) {
        if ((initialCapacity = MathUtils.nextPowerOfTwo((int)Math.ceil(63.75))) > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
        }
        this.capacity = initialCapacity;
        this.loadFactor = 0.8f;
        this.threshold = (int)(this.capacity * 0.8f);
        this.mask = this.capacity - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
        this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(this.capacity)) << 1);
        this.pushIterations = Math.max(Math.min(this.capacity, 8), (int)Math.sqrt(this.capacity) / 8);
        this.keyTable = new int[this.capacity + this.stashCapacity];
        this.valueTable = new int[this.keyTable.length];
    }
    
    public final void put(final int key, final int value) {
        if (key == 0) {
            this.zeroValue = value;
            if (!this.hasZeroValue) {
                this.hasZeroValue = true;
                ++this.size;
            }
            return;
        }
        final int[] keyTable = this.keyTable;
        final int index1 = key & this.mask;
        final int key2 = keyTable[index1];
        if (key == key2) {
            this.valueTable[index1] = value;
            return;
        }
        final int index2 = this.hash2(key);
        final int key3 = keyTable[index2];
        if (key == key3) {
            this.valueTable[index2] = value;
            return;
        }
        final int index3 = this.hash3(key);
        final int key4 = keyTable[index3];
        if (key == key4) {
            this.valueTable[index3] = value;
            return;
        }
        int i;
        for (int n = (i = this.capacity) + this.stashSize; i < n; ++i) {
            if (key == keyTable[i]) {
                this.valueTable[i] = value;
                return;
            }
        }
        if (key2 == 0) {
            keyTable[index1] = key;
            this.valueTable[index1] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        if (key3 == 0) {
            keyTable[index2] = key;
            this.valueTable[index2] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        if (key4 == 0) {
            keyTable[index3] = key;
            this.valueTable[index3] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        this.push(key, value, index1, key2, index2, key3, index3, key4);
    }
    
    private void putResize(final int key, final int value) {
        if (key == 0) {
            this.zeroValue = value;
            this.hasZeroValue = true;
            return;
        }
        final int index1 = key & this.mask;
        final int key2;
        if ((key2 = this.keyTable[index1]) == 0) {
            this.keyTable[index1] = key;
            this.valueTable[index1] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        final int index2 = this.hash2(key);
        final int key3;
        if ((key3 = this.keyTable[index2]) == 0) {
            this.keyTable[index2] = key;
            this.valueTable[index2] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        final int index3 = this.hash3(key);
        final int key4;
        if ((key4 = this.keyTable[index3]) == 0) {
            this.keyTable[index3] = key;
            this.valueTable[index3] = value;
            if (this.size++ >= this.threshold) {
                this.resize(this.capacity << 1);
            }
            return;
        }
        this.push(key, value, index1, key2, index2, key3, index3, key4);
    }
    
    private void push(int insertKey, int insertValue, int index1, int key1, int index2, int key2, int index3, int key3) {
        final int[] keyTable = this.keyTable;
        final int[] valueTable = this.valueTable;
        final int mask = this.mask;
        int i = 0;
        final int pushIterations = this.pushIterations;
        while (true) {
            int evictedKey = 0;
            int evictedValue = 0;
            switch (MathUtils.random(2)) {
                case 0: {
                    evictedKey = key1;
                    evictedValue = valueTable[index1];
                    keyTable[index1] = insertKey;
                    valueTable[index1] = insertValue;
                    break;
                }
                case 1: {
                    evictedKey = key2;
                    evictedValue = valueTable[index2];
                    keyTable[index2] = insertKey;
                    valueTable[index2] = insertValue;
                    break;
                }
                default: {
                    evictedKey = key3;
                    evictedValue = valueTable[index3];
                    keyTable[index3] = insertKey;
                    valueTable[index3] = insertValue;
                    break;
                }
            }
            index1 = (evictedKey & mask);
            if ((key1 = keyTable[index1]) == 0) {
                keyTable[index1] = evictedKey;
                valueTable[index1] = evictedValue;
                if (this.size++ >= this.threshold) {
                    this.resize(this.capacity << 1);
                }
                return;
            }
            index2 = this.hash2(evictedKey);
            if ((key2 = keyTable[index2]) == 0) {
                keyTable[index2] = evictedKey;
                valueTable[index2] = evictedValue;
                if (this.size++ >= this.threshold) {
                    this.resize(this.capacity << 1);
                }
                return;
            }
            index3 = this.hash3(evictedKey);
            if ((key3 = keyTable[index3]) == 0) {
                keyTable[index3] = evictedKey;
                valueTable[index3] = evictedValue;
                if (this.size++ >= this.threshold) {
                    this.resize(this.capacity << 1);
                }
                return;
            }
            if (++i == pushIterations) {
                this.putStash(evictedKey, evictedValue);
                return;
            }
            insertKey = evictedKey;
            insertValue = evictedValue;
        }
    }
    
    private void putStash(final int key, final int value) {
        if (this.stashSize == this.stashCapacity) {
            this.resize(this.capacity << 1);
            this.putResize(key, value);
            return;
        }
        final int index = this.capacity + this.stashSize;
        this.keyTable[index] = key;
        this.valueTable[index] = value;
        ++this.stashSize;
        ++this.size;
    }
    
    public final int get(final int key, int defaultValue) {
        if (key != 0) {
            defaultValue = (key & this.mask);
            if (this.keyTable[defaultValue] != key) {
                defaultValue = this.hash2(key);
                if (this.keyTable[defaultValue] != key) {
                    defaultValue = this.hash3(key);
                    if (this.keyTable[defaultValue] != key) {
                        final int n = 0;
                        defaultValue = key;
                        final int[] keyTable = this.keyTable;
                        int i = 0;
                        while (i < (i = this.capacity) + this.stashSize) {
                            if (defaultValue == keyTable[i]) {
                                return this.valueTable[i];
                            }
                            ++i;
                        }
                        return n;
                    }
                }
            }
            return this.valueTable[defaultValue];
        }
        if (!this.hasZeroValue) {
            return 0;
        }
        return this.zeroValue;
    }
    
    private void resize(int newSize) {
        final int oldEndIndex = this.capacity + this.stashSize;
        this.capacity = newSize;
        this.threshold = (int)(newSize * this.loadFactor);
        this.mask = newSize - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
        this.stashCapacity = Math.max(3, (int)Math.ceil(Math.log(newSize)) << 1);
        this.pushIterations = Math.max(Math.min(newSize, 8), (int)Math.sqrt(newSize) / 8);
        final int[] oldKeyTable = this.keyTable;
        final int[] oldValueTable = this.valueTable;
        this.keyTable = new int[newSize + this.stashCapacity];
        this.valueTable = new int[newSize + this.stashCapacity];
        newSize = this.size;
        this.size = (this.hasZeroValue ? 1 : 0);
        this.stashSize = 0;
        if (newSize > 0) {
            int key;
            for (newSize = 0; newSize < oldEndIndex; ++newSize) {
                if ((key = oldKeyTable[newSize]) != 0) {
                    this.putResize(key, oldValueTable[newSize]);
                }
            }
        }
    }
    
    private int hash2(int h) {
        return ((h *= -1262997959) ^ h >>> this.hashShift) & this.mask;
    }
    
    private int hash3(int h) {
        return ((h *= -825114047) ^ h >>> this.hashShift) & this.mask;
    }
    
    @Override
    public final int hashCode() {
        int h = 0;
        if (this.hasZeroValue) {
            h = 0 + Float.floatToIntBits((float)this.zeroValue);
        }
        final int[] keyTable = this.keyTable;
        final int[] valueTable = this.valueTable;
        for (int i = 0, n = this.capacity + this.stashSize; i < n; ++i) {
            final int key;
            if ((key = keyTable[i]) != 0) {
                h += key * 31;
                final int value = valueTable[i];
                h += value;
            }
        }
        return h;
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IntIntMap)) {
            return false;
        }
        final IntIntMap other;
        if ((other = (IntIntMap)obj).size != this.size) {
            return false;
        }
        if (other.hasZeroValue != this.hasZeroValue) {
            return false;
        }
        if (this.hasZeroValue && other.zeroValue != this.zeroValue) {
            return false;
        }
        final int[] keyTable = this.keyTable;
        final int[] valueTable = this.valueTable;
        for (int i = 0, n = this.capacity + this.stashSize; i < n; ++i) {
            final int key;
            if ((key = keyTable[i]) != 0) {
                final int otherValue;
                if ((otherValue = other.get(key, 0)) == 0) {
                    final IntIntMap intIntMap = other;
                    final int n2 = key;
                    final IntIntMap intIntMap2 = intIntMap;
                    boolean hasZeroValue = false;
                    Label_0270: {
                        if (n2 == 0) {
                            hasZeroValue = intIntMap2.hasZeroValue;
                        }
                        else if (intIntMap2.keyTable[n2 & intIntMap2.mask] != n2 && intIntMap2.keyTable[intIntMap2.hash2(n2)] != n2 && intIntMap2.keyTable[intIntMap2.hash3(n2)] != n2) {
                            final IntIntMap intIntMap3 = intIntMap2;
                            final int n3 = n2;
                            final IntIntMap intIntMap4 = intIntMap3;
                            final int[] keyTable2 = intIntMap3.keyTable;
                            int j = 0;
                            while (j < (j = intIntMap4.capacity) + intIntMap4.stashSize) {
                                if (n3 == keyTable2[j]) {
                                    hasZeroValue = true;
                                    break Label_0270;
                                }
                                ++j;
                            }
                            hasZeroValue = false;
                        }
                        else {
                            hasZeroValue = true;
                        }
                    }
                    if (!hasZeroValue) {
                        return false;
                    }
                }
                final int value = valueTable[i];
                if (otherValue != value) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public final String toString() {
        if (this.size == 0) {
            return "{}";
        }
        final com.badlogic.gdx.utils.StringBuilder buffer;
        (buffer = new com.badlogic.gdx.utils.StringBuilder(32)).append('{');
        final int[] keyTable = this.keyTable;
        final int[] valueTable = this.valueTable;
        int i = keyTable.length;
        if (this.hasZeroValue) {
            buffer.append("0=");
            buffer.append(this.zeroValue, 0);
        }
        else {
            while (i-- > 0) {
                final int key;
                if ((key = keyTable[i]) != 0) {
                    buffer.append(key, 0);
                    buffer.append('=');
                    buffer.append(valueTable[i], 0);
                    break;
                }
            }
        }
        while (i-- > 0) {
            final int key;
            if ((key = keyTable[i]) != 0) {
                buffer.append(", ");
                buffer.append(key, 0);
                buffer.append('=');
                buffer.append(valueTable[i], 0);
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
    
    @Override
    public final Iterator<Entry> iterator() {
        if (this.entries1 == null) {
            this.entries1 = new Entries(this);
            this.entries2 = new Entries(this);
        }
        if (!this.entries1.valid) {
            this.entries1.reset();
            this.entries1.valid = true;
            this.entries2.valid = false;
            return this.entries1;
        }
        this.entries2.reset();
        this.entries2.valid = true;
        this.entries1.valid = false;
        return this.entries2;
    }
    
    public static final class Entry
    {
        public int key;
        public int value;
        
        @Override
        public final String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    static class MapIterator
    {
        public boolean hasNext;
        final IntIntMap map;
        int nextIndex;
        int currentIndex;
        boolean valid;
        
        public MapIterator(final IntIntMap map) {
            this.valid = true;
            this.map = map;
            this.reset();
        }
        
        public void reset() {
            this.currentIndex = -2;
            this.nextIndex = -1;
            if (this.map.hasZeroValue) {
                this.hasNext = true;
                return;
            }
            this.findNextIndex();
        }
        
        final void findNextIndex() {
            this.hasNext = false;
            final int[] keyTable = this.map.keyTable;
            final int n = this.map.capacity + this.map.stashSize;
            while (++this.nextIndex < n) {
                if (keyTable[this.nextIndex] != 0) {
                    this.hasNext = true;
                    break;
                }
            }
        }
        
        public void remove() {
            if (this.currentIndex == -1 && this.map.hasZeroValue) {
                this.map.hasZeroValue = false;
            }
            else {
                if (this.currentIndex < 0) {
                    throw new IllegalStateException("next must be called before remove.");
                }
                if (this.currentIndex >= this.map.capacity) {
                    final IntIntMap map = this.map;
                    final int currentIndex = this.currentIndex;
                    final IntIntMap intIntMap = map;
                    --map.stashSize;
                    final int n = intIntMap.capacity + intIntMap.stashSize;
                    if (currentIndex < n) {
                        final int n2 = currentIndex;
                        final int[] keyTable = intIntMap.keyTable;
                        keyTable[n2] = keyTable[n];
                        final int n3 = currentIndex;
                        final int[] valueTable = intIntMap.valueTable;
                        valueTable[n3] = valueTable[n];
                    }
                    this.nextIndex = this.currentIndex - 1;
                    this.findNextIndex();
                }
                else {
                    this.map.keyTable[this.currentIndex] = 0;
                }
            }
            this.currentIndex = -2;
            final IntIntMap map2 = this.map;
            --map2.size;
        }
    }
    
    public static final class Entries extends MapIterator implements Iterable<Entry>, Iterator<Entry>
    {
        private Entry entry;
        
        public Entries(final IntIntMap map) {
            super(map);
            this.entry = new Entry();
        }
        
        @Override
        public final boolean hasNext() {
            if (!this.valid) {
                throw new GdxRuntimeException("#iterator() cannot be used nested.");
            }
            return this.hasNext;
        }
        
        @Override
        public final Iterator<Entry> iterator() {
            return this;
        }
        
        @Override
        public final void remove() {
            super.remove();
        }
    }
}
