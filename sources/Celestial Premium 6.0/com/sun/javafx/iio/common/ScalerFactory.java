/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.RoughScaler;
import com.sun.javafx.iio.common.SmoothMinifier;

public class ScalerFactory {
    private ScalerFactory() {
    }

    public static PushbroomScaler createScaler(int n, int n2, int n3, int n4, int n5, boolean bl) {
        boolean bl2;
        if (n <= 0 || n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0) {
            throw new IllegalArgumentException();
        }
        PushbroomScaler pushbroomScaler = null;
        boolean bl3 = bl2 = n4 > n || n5 > n2;
        pushbroomScaler = bl2 ? (bl ? new RoughScaler(n, n2, n3, n4, n5) : new RoughScaler(n, n2, n3, n4, n5)) : (bl ? new SmoothMinifier(n, n2, n3, n4, n5) : new RoughScaler(n, n2, n3, n4, n5));
        return pushbroomScaler;
    }
}

