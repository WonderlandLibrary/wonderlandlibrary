/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.Pair;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectObjectImmutableSortedPair;
import java.util.Objects;

public interface SortedPair<K extends Comparable<K>>
extends Pair<K, K> {
    public static <K extends Comparable<K>> SortedPair<K> of(K l2, K r2) {
        return ObjectObjectImmutableSortedPair.of(l2, r2);
    }

    default public boolean contains(Object o2) {
        return Objects.equals(o2, this.left()) || Objects.equals(o2, this.right());
    }
}

