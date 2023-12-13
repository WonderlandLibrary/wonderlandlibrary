/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

class Util {
    Util() {
    }

    static int ilog(int v2) {
        int ret = 0;
        while (v2 != 0) {
            ++ret;
            v2 >>>= 1;
        }
        return ret;
    }

    static int ilog2(int v2) {
        int ret = 0;
        while (v2 > 1) {
            ++ret;
            v2 >>>= 1;
        }
        return ret;
    }

    static int icount(int v2) {
        int ret = 0;
        while (v2 != 0) {
            ret += v2 & 1;
            v2 >>>= 1;
        }
        return ret;
    }
}

