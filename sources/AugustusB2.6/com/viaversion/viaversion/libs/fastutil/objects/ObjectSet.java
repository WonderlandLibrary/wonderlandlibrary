// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.Set;

public interface ObjectSet<K> extends ObjectCollection<K>, Set<K>
{
    ObjectIterator<K> iterator();
    
    default ObjectSpliterator<K> spliterator() {
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 65);
    }
    
    default <K> ObjectSet<K> of() {
        return (ObjectSet<K>)ObjectSets.UNMODIFIABLE_EMPTY_SET;
    }
    
    default <K> ObjectSet<K> of(final K e) {
        return ObjectSets.singleton(e);
    }
    
    default <K> ObjectSet<K> of(final K e0, final K e1) {
        final ObjectArraySet<K> innerSet = new ObjectArraySet<K>(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return ObjectSets.unmodifiable((ObjectSet<? extends K>)innerSet);
    }
    
    default <K> ObjectSet<K> of(final K e0, final K e1, final K e2) {
        final ObjectArraySet<K> innerSet = new ObjectArraySet<K>(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return ObjectSets.unmodifiable((ObjectSet<? extends K>)innerSet);
    }
    
    @SafeVarargs
    default <K> ObjectSet<K> of(final K... a) {
        switch (a.length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(a[0]);
            }
            case 2: {
                return of(a[0], a[1]);
            }
            case 3: {
                return of(a[0], a[1], a[2]);
            }
            default: {
                final ObjectSet<K> innerSet = (ObjectSet<K>)((a.length <= 4) ? new ObjectArraySet<Object>(a.length) : new ObjectOpenHashSet<Object>(a.length));
                for (final K element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }
                return ObjectSets.unmodifiable((ObjectSet<? extends K>)innerSet);
            }
        }
    }
}
