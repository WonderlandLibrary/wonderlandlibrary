/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class IntHashMap {
    private transient Entry[] slots = new Entry[16];
    private transient int count;
    private int threshold = 12;
    private final float growFactor = 0.75f;
    private static final String __OBFID = "CL_00001490";

    private static int computeHash(int p_76044_0_) {
        p_76044_0_ ^= p_76044_0_ >>> 20 ^ p_76044_0_ >>> 12;
        return p_76044_0_ ^ p_76044_0_ >>> 7 ^ p_76044_0_ >>> 4;
    }

    private static int getSlotIndex(int p_76043_0_, int p_76043_1_) {
        return p_76043_0_ & p_76043_1_ - 1;
    }

    public Object lookup(int p_76041_1_) {
        int var2 = IntHashMap.computeHash(p_76041_1_);
        Entry var3 = this.slots[IntHashMap.getSlotIndex(var2, this.slots.length)];
        while (var3 != null) {
            if (var3.hashEntry == p_76041_1_) {
                return var3.valueEntry;
            }
            var3 = var3.nextEntry;
        }
        return null;
    }

    public boolean containsItem(int p_76037_1_) {
        return this.lookupEntry(p_76037_1_) != null;
    }

    final Entry lookupEntry(int p_76045_1_) {
        int var2 = IntHashMap.computeHash(p_76045_1_);
        Entry var3 = this.slots[IntHashMap.getSlotIndex(var2, this.slots.length)];
        while (var3 != null) {
            if (var3.hashEntry == p_76045_1_) {
                return var3;
            }
            var3 = var3.nextEntry;
        }
        return null;
    }

    public void addKey(int p_76038_1_, Object p_76038_2_) {
        int var3 = IntHashMap.computeHash(p_76038_1_);
        int var4 = IntHashMap.getSlotIndex(var3, this.slots.length);
        Entry var5 = this.slots[var4];
        while (var5 != null) {
            if (var5.hashEntry == p_76038_1_) {
                var5.valueEntry = p_76038_2_;
                return;
            }
            var5 = var5.nextEntry;
        }
        this.insert(var3, p_76038_1_, p_76038_2_, var4);
    }

    private void grow(int p_76047_1_) {
        Entry[] var2 = this.slots;
        int var3 = var2.length;
        if (var3 == 0x40000000) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            Entry[] var4 = new Entry[p_76047_1_];
            this.copyTo(var4);
            this.slots = var4;
            this.threshold = (int)((float)p_76047_1_ * 0.75f);
        }
    }

    private void copyTo(Entry[] p_76048_1_) {
        Entry[] var2 = this.slots;
        int var3 = p_76048_1_.length;
        for (int var4 = 0; var4 < var2.length; ++var4) {
            Entry var6;
            Entry var5 = var2[var4];
            if (var5 == null) continue;
            var2[var4] = null;
            do {
                var6 = var5.nextEntry;
                int var7 = IntHashMap.getSlotIndex(var5.slotHash, var3);
                var5.nextEntry = p_76048_1_[var7];
                p_76048_1_[var7] = var5;
                var5 = var6;
            } while (var6 != null);
        }
    }

    public Object removeObject(int p_76049_1_) {
        Entry var2 = this.removeEntry(p_76049_1_);
        return var2 == null ? null : var2.valueEntry;
    }

    final Entry removeEntry(int p_76036_1_) {
        Entry var4;
        int var2 = IntHashMap.computeHash(p_76036_1_);
        int var3 = IntHashMap.getSlotIndex(var2, this.slots.length);
        Entry var5 = var4 = this.slots[var3];
        while (var5 != null) {
            Entry var6 = var5.nextEntry;
            if (var5.hashEntry == p_76036_1_) {
                --this.count;
                if (var4 == var5) {
                    this.slots[var3] = var6;
                } else {
                    var4.nextEntry = var6;
                }
                return var5;
            }
            var4 = var5;
            var5 = var6;
        }
        return var5;
    }

    public void clearMap() {
        Entry[] var1 = this.slots;
        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = null;
        }
        this.count = 0;
    }

    private void insert(int p_76040_1_, int p_76040_2_, Object p_76040_3_, int p_76040_4_) {
        Entry var5 = this.slots[p_76040_4_];
        this.slots[p_76040_4_] = new Entry(p_76040_1_, p_76040_2_, p_76040_3_, var5);
        if (this.count++ >= this.threshold) {
            this.grow(2 * this.slots.length);
        }
    }

    static class Entry {
        final int hashEntry;
        Object valueEntry;
        Entry nextEntry;
        final int slotHash;
        private static final String __OBFID = "CL_00001491";

        Entry(int p_i1552_1_, int p_i1552_2_, Object p_i1552_3_, Entry p_i1552_4_) {
            this.valueEntry = p_i1552_3_;
            this.nextEntry = p_i1552_4_;
            this.hashEntry = p_i1552_2_;
            this.slotHash = p_i1552_1_;
        }

        public final int getHash() {
            return this.hashEntry;
        }

        public final Object getValue() {
            return this.valueEntry;
        }

        public final boolean equals(Object p_equals_1_) {
            Object var6;
            Object var5;
            Integer var4;
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }
            Entry var2 = (Entry)p_equals_1_;
            Integer var3 = this.getHash();
            return (var3 == (var4 = Integer.valueOf(var2.getHash())) || var3 != null && var3.equals(var4)) && ((var5 = this.getValue()) == (var6 = var2.getValue()) || var5 != null && var5.equals(var6));
        }

        public final int hashCode() {
            return IntHashMap.computeHash(this.hashEntry);
        }

        public final String toString() {
            return String.valueOf(this.getHash()) + "=" + this.getValue();
        }
    }
}

