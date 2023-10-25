/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GenericProgressiveFutureListener;
import java.util.Arrays;

final class DefaultFutureListeners {
    private int progressiveSize;
    private int size;
    private GenericFutureListener<? extends Future<?>>[] listeners = new GenericFutureListener[2];

    public void remove(GenericFutureListener<? extends Future<?>> genericFutureListener) {
        GenericFutureListener<? extends Future<?>>[] genericFutureListenerArray = this.listeners;
        int n = this.size;
        for (int i = 0; i < n; ++i) {
            if (genericFutureListenerArray[i] != genericFutureListener) continue;
            int n2 = n - i - 1;
            if (n2 > 0) {
                System.arraycopy(genericFutureListenerArray, i + 1, genericFutureListenerArray, i, n2);
            }
            genericFutureListenerArray[--n] = null;
            this.size = n;
            if (genericFutureListener instanceof GenericProgressiveFutureListener) {
                --this.progressiveSize;
            }
            return;
        }
    }

    DefaultFutureListeners(GenericFutureListener<? extends Future<?>> genericFutureListener, GenericFutureListener<? extends Future<?>> genericFutureListener2) {
        this.listeners[0] = genericFutureListener;
        this.listeners[1] = genericFutureListener2;
        this.size = 2;
        if (genericFutureListener instanceof GenericProgressiveFutureListener) {
            ++this.progressiveSize;
        }
        if (genericFutureListener2 instanceof GenericProgressiveFutureListener) {
            ++this.progressiveSize;
        }
    }

    public GenericFutureListener<? extends Future<?>>[] listeners() {
        return this.listeners;
    }

    public int progressiveSize() {
        return this.progressiveSize;
    }

    public void add(GenericFutureListener<? extends Future<?>> genericFutureListener) {
        int n = this.size;
        GenericFutureListener<? extends Future<?>>[] genericFutureListenerArray = this.listeners;
        if (n == genericFutureListenerArray.length) {
            this.listeners = genericFutureListenerArray = Arrays.copyOf(genericFutureListenerArray, n << 1);
        }
        genericFutureListenerArray[n] = genericFutureListener;
        this.size = n + 1;
        if (genericFutureListener instanceof GenericProgressiveFutureListener) {
            ++this.progressiveSize;
        }
    }

    public int size() {
        return this.size;
    }
}

