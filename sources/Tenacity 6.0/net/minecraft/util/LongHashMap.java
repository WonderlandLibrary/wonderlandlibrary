// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class LongHashMap<V>
{
    private transient Entry<V>[] hashArray;
    private transient int numHashElements;
    private int mask;
    private int capacity;
    private final float percentUseable = 0.75f;
    private transient volatile int modCount;
    
    public LongHashMap() {
        this.hashArray = (Entry<V>[])new Entry[4096];
        this.capacity = 3072;
        this.mask = this.hashArray.length - 1;
    }
    
    private static int getHashedKey(final long originalKey) {
        return (int)(originalKey ^ originalKey >>> 27);
    }
    
    private static int hash(int integer) {
        integer = (integer ^ integer >>> 20 ^ integer >>> 12);
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }
    
    private static int getHashIndex(final int p_76158_0_, final int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }
    
    public int getNumHashElements() {
        return this.numHashElements;
    }
    
    public V getValueByKey(final long p_76164_1_) {
        final int i = getHashedKey(p_76164_1_);
        for (Entry<V> entry = this.hashArray[getHashIndex(i, this.mask)]; entry != null; entry = entry.nextEntry) {
            if (entry.key == p_76164_1_) {
                return entry.value;
            }
        }
        return null;
    }
    
    public boolean containsItem(final long p_76161_1_) {
        return this.getEntry(p_76161_1_) != null;
    }
    
    final Entry<V> getEntry(final long p_76160_1_) {
        final int i = getHashedKey(p_76160_1_);
        for (Entry<V> entry = this.hashArray[getHashIndex(i, this.mask)]; entry != null; entry = entry.nextEntry) {
            if (entry.key == p_76160_1_) {
                return entry;
            }
        }
        return null;
    }
    
    public void add(final long p_76163_1_, final V p_76163_3_) {
        final int i = getHashedKey(p_76163_1_);
        final int j = getHashIndex(i, this.mask);
        for (Entry<V> entry = this.hashArray[j]; entry != null; entry = entry.nextEntry) {
            if (entry.key == p_76163_1_) {
                entry.value = p_76163_3_;
                return;
            }
        }
        ++this.modCount;
        this.createKey(i, p_76163_1_, p_76163_3_, j);
    }
    
    private void resizeTable(final int p_76153_1_) {
        final Entry<V>[] entry = this.hashArray;
        final int i = entry.length;
        if (i == 1073741824) {
            this.capacity = Integer.MAX_VALUE;
        }
        else {
            final Entry<V>[] entry2 = (Entry<V>[])new Entry[p_76153_1_];
            this.copyHashTableTo(entry2);
            this.hashArray = entry2;
            this.mask = this.hashArray.length - 1;
            final float f = (float)p_76153_1_;
            this.getClass();
            this.capacity = (int)(f * 0.75f);
        }
    }
    
    private void copyHashTableTo(final Entry<V>[] p_76154_1_) {
        final Entry<V>[] entry = this.hashArray;
        final int i = p_76154_1_.length;
        for (int j = 0; j < entry.length; ++j) {
            Entry<V> entry2 = entry[j];
            if (entry2 != null) {
                entry[j] = null;
                Entry<V> entry3;
                do {
                    entry3 = entry2.nextEntry;
                    final int k = getHashIndex(entry2.hash, i - 1);
                    entry2.nextEntry = p_76154_1_[k];
                    p_76154_1_[k] = entry2;
                    entry2 = entry3;
                } while (entry3 != null);
            }
        }
    }
    
    public V remove(final long p_76159_1_) {
        final Entry<V> entry = this.removeKey(p_76159_1_);
        return (entry == null) ? null : entry.value;
    }
    
    final Entry<V> removeKey(final long p_76152_1_) {
        final int i = getHashedKey(p_76152_1_);
        final int j = getHashIndex(i, this.mask);
        Entry<V> entry2;
        Entry<V> entry3;
        for (Entry<V> entry = entry2 = this.hashArray[j]; entry2 != null; entry2 = entry3) {
            entry3 = entry2.nextEntry;
            if (entry2.key == p_76152_1_) {
                ++this.modCount;
                --this.numHashElements;
                if (entry == entry2) {
                    this.hashArray[j] = entry3;
                }
                else {
                    entry.nextEntry = entry3;
                }
                return entry2;
            }
            entry = entry2;
        }
        return entry2;
    }
    
    private void createKey(final int p_76156_1_, final long p_76156_2_, final V p_76156_4_, final int p_76156_5_) {
        final Entry<V> entry = this.hashArray[p_76156_5_];
        this.hashArray[p_76156_5_] = new Entry<V>(p_76156_1_, p_76156_2_, p_76156_4_, entry);
        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.hashArray.length);
        }
    }
    
    public double getKeyDistribution() {
        int i = 0;
        for (int j = 0; j < this.hashArray.length; ++j) {
            if (this.hashArray[j] != null) {
                ++i;
            }
        }
        return 1.0 * i / this.numHashElements;
    }
    
    static class Entry<V>
    {
        final long key;
        V value;
        Entry<V> nextEntry;
        final int hash;
        
        Entry(final int p_i1553_1_, final long p_i1553_2_, final V p_i1553_4_, final Entry<V> p_i1553_5_) {
            this.value = p_i1553_4_;
            this.nextEntry = p_i1553_5_;
            this.key = p_i1553_2_;
            this.hash = p_i1553_1_;
        }
        
        public final long getKey() {
            return this.key;
        }
        
        public final V getValue() {
            return this.value;
        }
        
        @Override
        public final boolean equals(final Object p_equals_1_) {
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }
            final Entry<V> entry = (Entry<V>)p_equals_1_;
            final Object object = this.getKey();
            final Object object2 = entry.getKey();
            if (object == object2 || (object != null && object.equals(object2))) {
                final Object object3 = this.getValue();
                final Object object4 = entry.getValue();
                if (object3 == object4 || (object3 != null && object3.equals(object4))) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public final int hashCode() {
            return getHashedKey(this.key);
        }
        
        @Override
        public final String toString() {
            return this.getKey() + "=" + this.getValue();
        }
    }
}
