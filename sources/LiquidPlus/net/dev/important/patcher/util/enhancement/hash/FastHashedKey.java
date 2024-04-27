/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.util.enhancement.hash;

public class FastHashedKey {
    public static int mix64(long input) {
        input = (input ^ input >> 30) * -4658895280553007687L;
        input = (input ^ input >> 27) * -7723592293110705685L;
        input ^= input >> 31;
        return Long.hashCode(input);
    }
}

