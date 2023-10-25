package net.minecraft.util;

import net.minecraft.util.LongHashMap;

public class LongHashMap<V> {
    private transient Entry<V>[] hashArray = new Entry[4096];
    private transient int numHashElements;
    private int mask = this.hashArray.length - 1;
    private int capacity = 3072;
    private final float percentUseable = 0.75f;
    private volatile transient int modCount;

    private static int getHashedKey(long originalKey) {
        return (int)(originalKey ^ originalKey >>> 27);
    }

    private static int hash(int integer) {
        integer = integer ^ integer >>> 20 ^ integer >>> 12;
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }

    private static int getHashIndex(int p_76158_0_, int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }

    public int getNumHashElements() {
        return this.numHashElements;
    }

    public V getValueByKey(long p_76164_1_) {
        int i = LongHashMap.getHashedKey(p_76164_1_);
        Entry entry = this.hashArray[LongHashMap.getHashIndex(i, this.mask)];
        while (entry != null) {
            if (entry.key == p_76164_1_) {
                return (V)entry.value;
            }
            entry = entry.nextEntry;
        }
        return null;
    }

    public boolean containsItem(long p_76161_1_) {
        return this.getEntry(p_76161_1_) != null;
    }

    final Entry<V> getEntry(long p_76160_1_) {
        int i = LongHashMap.getHashedKey(p_76160_1_);
        Entry entry = this.hashArray[LongHashMap.getHashIndex(i, this.mask)];
        while (entry != null) {
            if (entry.key == p_76160_1_) {
                return entry;
            }
            entry = entry.nextEntry;
        }
        return null;
    }

    public void add(long p_76163_1_, V p_76163_3_) {
        int i = LongHashMap.getHashedKey(p_76163_1_);
        int j = LongHashMap.getHashIndex(i, this.mask);
        Entry entry = this.hashArray[j];
        while (entry != null) {
            if (entry.key == p_76163_1_) {
                entry.value = p_76163_3_;
                return;
            }
            entry = entry.nextEntry;
        }
        ++this.modCount;
        this.createKey(i, p_76163_1_, p_76163_3_, j);
    }

    private void resizeTable(int p_76153_1_) {
        Entry<V>[] entry = this.hashArray;
        int i = entry.length;
        if (i == 0x40000000) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            Entry[] entry1 = new Entry[p_76153_1_];
            this.copyHashTableTo(entry1);
            this.hashArray = entry1;
            this.mask = this.hashArray.length - 1;
            float f = p_76153_1_;
            this.getClass();
            this.capacity = (int)(f * 0.75f);
        }
    }

    private void copyHashTableTo(Entry<V>[] p_76154_1_) {
        Entry<V>[] entry = this.hashArray;
        int i = p_76154_1_.length;
        for (int j = 0; j < entry.length; ++j) {
            Entry entry2;
            Entry entry1 = entry[j];
            if (entry1 == null) continue;
            entry[j] = null;
            do {
                entry2 = entry1.nextEntry;
                int k = LongHashMap.getHashIndex(entry1.hash, i - 1);
                entry1.nextEntry = p_76154_1_[k];
                p_76154_1_[k] = entry1;
                entry1 = entry2;
            } while (entry2 != null);
        }
    }

    public V remove(long p_76159_1_) {
        Entry<V> entry = this.removeKey(p_76159_1_);
        return (V)(entry == null ? null : entry.value);
    }

    final Entry<V> removeKey(long p_76152_1_) {
        Entry entry;
        int i = LongHashMap.getHashedKey(p_76152_1_);
        int j = LongHashMap.getHashIndex(i, this.mask);
        Entry entry1 = entry = this.hashArray[j];
        while (entry1 != null) {
            Entry entry2 = entry1.nextEntry;
            if (entry1.key == p_76152_1_) {
                ++this.modCount;
                --this.numHashElements;
                if (entry == entry1) {
                    this.hashArray[j] = entry2;
                } else {
                    entry.nextEntry = entry2;
                }
                return entry1;
            }
            entry = entry1;
            entry1 = entry2;
        }
        return entry1;
    }

    private void createKey(int p_76156_1_, long p_76156_2_, V p_76156_4_, int p_76156_5_) {
        Entry<V> entry = this.hashArray[p_76156_5_];
        this.hashArray[p_76156_5_] = new Entry(p_76156_1_, p_76156_2_, p_76156_4_, entry);
        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.hashArray.length);
        }
    }

    public double getKeyDistribution() {
        int i = 0;
        for (int j = 0; j < this.hashArray.length; ++j) {
            if (this.hashArray[j] == null) continue;
            ++i;
        }
        return 1.0 * (double)i / (double)this.numHashElements;
    }

    static /* synthetic */ int access$000(long x0) {
        return LongHashMap.getHashedKey(x0);
    }
}
