/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterator<K>
extends Iterator<K> {
    default public int skip(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
        }
        int i2 = n2;
        while (i2-- != 0 && this.hasNext()) {
            this.next();
        }
        return n2 - i2 - 1;
    }
}

