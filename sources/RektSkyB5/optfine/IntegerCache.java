/*
 * Decompiled with CFR 0.152.
 */
package optfine;

public class IntegerCache {
    private static final int CACHE_SIZE = 4096;
    private static final Integer[] cache = IntegerCache.makeCache(4096);

    private static Integer[] makeCache(int p_makeCache_0_) {
        Integer[] ainteger = new Integer[p_makeCache_0_];
        for (int i2 = 0; i2 < p_makeCache_0_; ++i2) {
            ainteger[i2] = new Integer(i2);
        }
        return ainteger;
    }

    public static Integer valueOf(int p_valueOf_0_) {
        return p_valueOf_0_ >= 0 && p_valueOf_0_ < 4096 ? cache[p_valueOf_0_] : new Integer(p_valueOf_0_);
    }
}

