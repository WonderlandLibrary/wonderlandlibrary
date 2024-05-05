package net.minecraft.util;

import net.minecraft.util.IntHashMap;

/*
 * Exception performing whole class analysis ignored.
 */
static class IntHashMap.Entry<V> {
    final int hashEntry;
    V valueEntry;
    IntHashMap.Entry<V> nextEntry;
    final int slotHash;

    IntHashMap.Entry(int p_i1552_1_, int p_i1552_2_, V p_i1552_3_, IntHashMap.Entry<V> p_i1552_4_) {
        this.valueEntry = p_i1552_3_;
        this.nextEntry = p_i1552_4_;
        this.hashEntry = p_i1552_2_;
        this.slotHash = p_i1552_1_;
    }

    public final int getHash() {
        return this.hashEntry;
    }

    public final V getValue() {
        return this.valueEntry;
    }

    public final boolean equals(Object p_equals_1_) {
        V object3;
        V object2;
        Integer object1;
        if (!(p_equals_1_ instanceof IntHashMap.Entry)) {
            return false;
        }
        IntHashMap.Entry entry = (IntHashMap.Entry)p_equals_1_;
        Integer object = this.getHash();
        return (object == (object1 = Integer.valueOf((int)entry.getHash())) || object != null && object.equals((Object)object1)) && ((object2 = this.getValue()) == (object3 = entry.getValue()) || object2 != null && object2.equals(object3));
    }

    public final int hashCode() {
        return IntHashMap.access$000((int)this.hashEntry);
    }

    public final String toString() {
        return this.getHash() + "=" + this.getValue();
    }
}
