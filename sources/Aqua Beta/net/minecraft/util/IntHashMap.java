package net.minecraft.util;

import net.minecraft.util.IntHashMap;

public class IntHashMap<V> {
    private transient Entry<V>[] slots = new Entry[16];
    private transient int count;
    private int threshold = 12;
    private final float growFactor = 0.75f;

    private static int computeHash(int integer) {
        integer = integer ^ integer >>> 20 ^ integer >>> 12;
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }

    private static int getSlotIndex(int hash, int slotCount) {
        return hash & slotCount - 1;
    }

    public V lookup(int p_76041_1_) {
        int i = IntHashMap.computeHash(p_76041_1_);
        Entry entry = this.slots[IntHashMap.getSlotIndex(i, this.slots.length)];
        while (entry != null) {
            if (entry.hashEntry == p_76041_1_) {
                return (V)entry.valueEntry;
            }
            entry = entry.nextEntry;
        }
        return null;
    }

    public boolean containsItem(int p_76037_1_) {
        return this.lookupEntry(p_76037_1_) != null;
    }

    final Entry<V> lookupEntry(int p_76045_1_) {
        int i = IntHashMap.computeHash(p_76045_1_);
        Entry entry = this.slots[IntHashMap.getSlotIndex(i, this.slots.length)];
        while (entry != null) {
            if (entry.hashEntry == p_76045_1_) {
                return entry;
            }
            entry = entry.nextEntry;
        }
        return null;
    }

    public void addKey(int p_76038_1_, V p_76038_2_) {
        int i = IntHashMap.computeHash(p_76038_1_);
        int j = IntHashMap.getSlotIndex(i, this.slots.length);
        Entry entry = this.slots[j];
        while (entry != null) {
            if (entry.hashEntry == p_76038_1_) {
                entry.valueEntry = p_76038_2_;
                return;
            }
            entry = entry.nextEntry;
        }
        this.insert(i, p_76038_1_, p_76038_2_, j);
    }

    private void grow(int p_76047_1_) {
        Entry<V>[] entry = this.slots;
        int i = entry.length;
        if (i == 0x40000000) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            Entry[] entry1 = new Entry[p_76047_1_];
            this.copyTo(entry1);
            this.slots = entry1;
            this.threshold = (int)((float)p_76047_1_ * this.growFactor);
        }
    }

    private void copyTo(Entry<V>[] p_76048_1_) {
        Entry<V>[] entry = this.slots;
        int i = p_76048_1_.length;
        for (int j = 0; j < entry.length; ++j) {
            Entry entry2;
            Entry entry1 = entry[j];
            if (entry1 == null) continue;
            entry[j] = null;
            do {
                entry2 = entry1.nextEntry;
                int k = IntHashMap.getSlotIndex(entry1.slotHash, i);
                entry1.nextEntry = p_76048_1_[k];
                p_76048_1_[k] = entry1;
                entry1 = entry2;
            } while (entry2 != null);
        }
    }

    public V removeObject(int p_76049_1_) {
        Entry<V> entry = this.removeEntry(p_76049_1_);
        return (V)(entry == null ? null : entry.valueEntry);
    }

    final Entry<V> removeEntry(int p_76036_1_) {
        Entry entry;
        int i = IntHashMap.computeHash(p_76036_1_);
        int j = IntHashMap.getSlotIndex(i, this.slots.length);
        Entry entry1 = entry = this.slots[j];
        while (entry1 != null) {
            Entry entry2 = entry1.nextEntry;
            if (entry1.hashEntry == p_76036_1_) {
                --this.count;
                if (entry == entry1) {
                    this.slots[j] = entry2;
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

    public void clearMap() {
        Entry<V>[] entry = this.slots;
        for (int i = 0; i < entry.length; ++i) {
            entry[i] = null;
        }
        this.count = 0;
    }

    private void insert(int p_76040_1_, int p_76040_2_, V p_76040_3_, int p_76040_4_) {
        Entry<V> entry = this.slots[p_76040_4_];
        this.slots[p_76040_4_] = new Entry(p_76040_1_, p_76040_2_, p_76040_3_, entry);
        if (this.count++ >= this.threshold) {
            this.grow(2 * this.slots.length);
        }
    }

    static /* synthetic */ int access$000(int x0) {
        return IntHashMap.computeHash(x0);
    }
}
