/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.util.enhancement.hash.impl;

import java.util.Objects;

public abstract class AbstractHash {
    private final int hash;
    private final Object[] objects;

    public AbstractHash(Object ... items) {
        this.hash = Objects.hash(items);
        this.objects = items;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractHash)) {
            return false;
        }
        Object[] a = this.objects;
        Object[] a2 = ((AbstractHash)obj).objects;
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            if (a[i] == a2[i] || a[i].equals(a2[i])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.hash;
    }
}

