/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

public class LongHashMap {
    private transient Entry[] hashArray = new Entry[4096];
    private transient int numHashElements;
    private int field_180201_c = this.hashArray.length - 1;
    private int capacity = 3072;
    private final float percentUseable = 0.75f;
    private volatile transient int modCount;
    private static final String __OBFID = "CL_00001492";

    private static int getHashedKey(long originalKey) {
        return (int)(originalKey ^ originalKey >>> 27);
    }

    private static int hash(int integer) {
        integer ^= integer >>> 20 ^ integer >>> 12;
        return integer ^ integer >>> 7 ^ integer >>> 4;
    }

    private static int getHashIndex(int p_76158_0_, int p_76158_1_) {
        return p_76158_0_ & p_76158_1_;
    }

    public int getNumHashElements() {
        return this.numHashElements;
    }

    public Object getValueByKey(long p_76164_1_) {
        int var3 = LongHashMap.getHashedKey(p_76164_1_);
        Entry var4 = this.hashArray[LongHashMap.getHashIndex(var3, this.field_180201_c)];
        while (var4 != null) {
            if (var4.key == p_76164_1_) {
                return var4.value;
            }
            var4 = var4.nextEntry;
        }
        return null;
    }

    public boolean containsItem(long p_76161_1_) {
        return this.getEntry(p_76161_1_) != null;
    }

    final Entry getEntry(long p_76160_1_) {
        int var3 = LongHashMap.getHashedKey(p_76160_1_);
        Entry var4 = this.hashArray[LongHashMap.getHashIndex(var3, this.field_180201_c)];
        while (var4 != null) {
            if (var4.key == p_76160_1_) {
                return var4;
            }
            var4 = var4.nextEntry;
        }
        return null;
    }

    public void add(long p_76163_1_, Object p_76163_3_) {
        int var4 = LongHashMap.getHashedKey(p_76163_1_);
        int var5 = LongHashMap.getHashIndex(var4, this.field_180201_c);
        Entry var6 = this.hashArray[var5];
        while (var6 != null) {
            if (var6.key == p_76163_1_) {
                var6.value = p_76163_3_;
                return;
            }
            var6 = var6.nextEntry;
        }
        ++this.modCount;
        this.createKey(var4, p_76163_1_, p_76163_3_, var5);
    }

    private void resizeTable(int p_76153_1_) {
        Entry[] var2 = this.hashArray;
        int var3 = var2.length;
        if (var3 == 0x40000000) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            Entry[] var4 = new Entry[p_76153_1_];
            this.copyHashTableTo(var4);
            this.hashArray = var4;
            this.field_180201_c = this.hashArray.length - 1;
            float var10001 = p_76153_1_;
            this.getClass();
            this.capacity = (int)(var10001 * 0.75f);
        }
    }

    private void copyHashTableTo(Entry[] p_76154_1_) {
        Entry[] var2 = this.hashArray;
        int var3 = p_76154_1_.length;
        for (int var4 = 0; var4 < var2.length; ++var4) {
            Entry var6;
            Entry var5 = var2[var4];
            if (var5 == null) continue;
            var2[var4] = null;
            do {
                var6 = var5.nextEntry;
                int var7 = LongHashMap.getHashIndex(var5.hash, var3 - 1);
                var5.nextEntry = p_76154_1_[var7];
                p_76154_1_[var7] = var5;
                var5 = var6;
            } while (var6 != null);
        }
    }

    public Object remove(long p_76159_1_) {
        Entry var3 = this.removeKey(p_76159_1_);
        return var3 == null ? null : var3.value;
    }

    final Entry removeKey(long p_76152_1_) {
        Entry var5;
        int var3 = LongHashMap.getHashedKey(p_76152_1_);
        int var4 = LongHashMap.getHashIndex(var3, this.field_180201_c);
        Entry var6 = var5 = this.hashArray[var4];
        while (var6 != null) {
            Entry var7 = var6.nextEntry;
            if (var6.key == p_76152_1_) {
                ++this.modCount;
                --this.numHashElements;
                if (var5 == var6) {
                    this.hashArray[var4] = var7;
                } else {
                    var5.nextEntry = var7;
                }
                return var6;
            }
            var5 = var6;
            var6 = var7;
        }
        return var6;
    }

    private void createKey(int p_76156_1_, long p_76156_2_, Object p_76156_4_, int p_76156_5_) {
        Entry var6 = this.hashArray[p_76156_5_];
        this.hashArray[p_76156_5_] = new Entry(p_76156_1_, p_76156_2_, p_76156_4_, var6);
        if (this.numHashElements++ >= this.capacity) {
            this.resizeTable(2 * this.hashArray.length);
        }
    }

    public double getKeyDistribution() {
        int countValid = 0;
        for (int i = 0; i < this.hashArray.length; ++i) {
            if (this.hashArray[i] == null) continue;
            ++countValid;
        }
        return 1.0 * (double)countValid / (double)this.numHashElements;
    }

    static class Entry {
        final long key;
        Object value;
        Entry nextEntry;
        final int hash;
        private static final String __OBFID = "CL_00001493";

        Entry(int p_i1553_1_, long p_i1553_2_, Object p_i1553_4_, Entry p_i1553_5_) {
            this.value = p_i1553_4_;
            this.nextEntry = p_i1553_5_;
            this.key = p_i1553_2_;
            this.hash = p_i1553_1_;
        }

        public final long getKey() {
            return this.key;
        }

        public final Object getValue() {
            return this.value;
        }

        public final boolean equals(Object p_equals_1_) {
            Object var6;
            Object var5;
            Long var4;
            if (!(p_equals_1_ instanceof Entry)) {
                return false;
            }
            Entry var2 = (Entry)p_equals_1_;
            Long var3 = this.getKey();
            return (var3 == (var4 = Long.valueOf(var2.getKey())) || var3 != null && var3.equals(var4)) && ((var5 = this.getValue()) == (var6 = var2.getValue()) || var5 != null && var5.equals(var6));
        }

        public final int hashCode() {
            return LongHashMap.getHashedKey(this.key);
        }

        public final String toString() {
            return String.valueOf(this.getKey()) + "=" + this.getValue();
        }
    }
}

