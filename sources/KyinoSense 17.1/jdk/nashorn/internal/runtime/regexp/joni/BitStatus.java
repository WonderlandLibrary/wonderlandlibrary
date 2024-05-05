/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

final class BitStatus {
    public static final int BIT_STATUS_BITS_NUM = 32;

    BitStatus() {
    }

    public static int bsClear() {
        return 0;
    }

    public static int bsAll() {
        return -1;
    }

    public static boolean bsAt(int stats, int n) {
        return (n < 32 ? stats & 1 << n : stats & 1) != 0;
    }

    public static int bsOnAt(int statsp, int n) {
        int stats = statsp;
        stats = n < 32 ? (stats |= 1 << n) : (stats |= 1);
        return stats;
    }

    public static int bsOnOff(int v, int f, boolean negative) {
        return negative ? v & ~f : v | f;
    }
}

