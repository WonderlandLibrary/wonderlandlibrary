/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArraySet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSets;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.util.Set;

public interface ObjectSet<K>
extends ObjectCollection<K>,
Set<K> {
    @Override
    public ObjectIterator<K> iterator();

    @Override
    default public ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 65);
    }

    public static <K> ObjectSet<K> of() {
        return ObjectSets.UNMODIFIABLE_EMPTY_SET;
    }

    public static <K> ObjectSet<K> of(K e2) {
        return ObjectSets.singleton(e2);
    }

    public static <K> ObjectSet<K> of(K e0, K e1) {
        ObjectArraySet<K> innerSet = new ObjectArraySet<K>(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return ObjectSets.unmodifiable(innerSet);
    }

    public static <K> ObjectSet<K> of(K e0, K e1, K e2) {
        ObjectArraySet<K> innerSet = new ObjectArraySet<K>(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return ObjectSets.unmodifiable(innerSet);
    }

    @SafeVarargs
    public static <K> ObjectSet<K> of(K ... a2) {
        switch (a2.length) {
            case 0: {
                return ObjectSet.of();
            }
            case 1: {
                return ObjectSet.of(a2[0]);
            }
            case 2: {
                return ObjectSet.of(a2[0], a2[1]);
            }
            case 3: {
                return ObjectSet.of(a2[0], a2[1], a2[2]);
            }
        }
        AbstractObjectSet innerSet = a2.length <= 4 ? new ObjectArraySet(a2.length) : new ObjectOpenHashSet(a2.length);
        for (K element : a2) {
            if (innerSet.add(element)) continue;
            throw new IllegalArgumentException("Duplicate element: " + element);
        }
        return ObjectSets.unmodifiable(innerSet);
    }
}

