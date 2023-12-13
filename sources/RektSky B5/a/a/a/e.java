/*
 * Decompiled with CFR 0.152.
 */
package a.a.a;

import java.util.Random;

public final class e {
    static {
        new Random();
    }

    public static int a(int n2) {
        if (n2 < 0) {
            return 0;
        }
        if (n2 > 255) {
            return 255;
        }
        return n2;
    }
}

