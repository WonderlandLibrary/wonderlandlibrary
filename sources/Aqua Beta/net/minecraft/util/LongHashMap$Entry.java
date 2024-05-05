package net.minecraft.util;

import net.minecraft.util.LongHashMap;

/*
 * Exception performing whole class analysis ignored.
 */
static class LongHashMap.Entry<V> {
    final long key;
    V value;
    LongHashMap.Entry<V> nextEntry;
    final int hash;

    LongHashMap.Entry(int p_i1553_1_, long p_i1553_2_, V p_i1553_4_, LongHashMap.Entry<V> p_i1553_5_) {
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

    public final boolean equals(Object p_equals_1_) {
        V object3;
        V object2;
        Long object1;
        if (!(p_equals_1_ instanceof LongHashMap.Entry)) {
            return false;
        }
        LongHashMap.Entry entry = (LongHashMap.Entry)p_equals_1_;
        Long object = this.getKey();
        return (object == (object1 = Long.valueOf((long)entry.getKey())) || object != null && object.equals((Object)object1)) && ((object2 = this.getValue()) == (object3 = entry.getValue()) || object2 != null && object2.equals(object3));
    }

    public final int hashCode() {
        return LongHashMap.access$000((long)this.key);
    }

    public final String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}
