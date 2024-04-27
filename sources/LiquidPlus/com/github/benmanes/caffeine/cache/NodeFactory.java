/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Node;
import com.github.benmanes.caffeine.cache.References;
import com.github.benmanes.caffeine.cache.Weigher;
import java.lang.ref.ReferenceQueue;

interface NodeFactory<K, V> {
    public static final Object RETIRED_STRONG_KEY = new Object();
    public static final Object DEAD_STRONG_KEY = new Object();
    public static final References.WeakKeyReference<Object> RETIRED_WEAK_KEY = new References.WeakKeyReference<Object>(null, null);
    public static final References.WeakKeyReference<Object> DEAD_WEAK_KEY = new References.WeakKeyReference<Object>(null, null);

    public Node<K, V> newNode(K var1, ReferenceQueue<K> var2, V var3, ReferenceQueue<V> var4, int var5, long var6);

    public Node<K, V> newNode(Object var1, V var2, ReferenceQueue<V> var3, int var4, long var5);

    default public Object newReferenceKey(K key, ReferenceQueue<K> referenceQueue) {
        return key;
    }

    default public Object newLookupKey(Object key) {
        return key;
    }

    public static <K, V> NodeFactory<K, V> newFactory(Caffeine<K, V> builder, boolean isAsync) {
        StringBuilder sb = new StringBuilder("com.github.benmanes.caffeine.cache.");
        if (builder.isStrongKeys()) {
            sb.append('P');
        } else {
            sb.append('F');
        }
        if (builder.isStrongValues()) {
            sb.append('S');
        } else if (builder.isWeakValues()) {
            sb.append('W');
        } else {
            sb.append('D');
        }
        if (builder.expiresVariable()) {
            if (builder.refreshAfterWrite()) {
                sb.append('A');
                if (builder.evicts()) {
                    sb.append('W');
                }
            } else {
                sb.append('W');
            }
        } else {
            if (builder.expiresAfterAccess()) {
                sb.append('A');
            }
            if (builder.expiresAfterWrite()) {
                sb.append('W');
            }
        }
        if (builder.refreshAfterWrite()) {
            sb.append('R');
        }
        if (builder.evicts()) {
            sb.append('M');
            if (isAsync || builder.isWeighted() && builder.weigher != Weigher.singletonWeigher()) {
                sb.append('W');
            } else {
                sb.append('S');
            }
        }
        try {
            Class<?> clazz = Class.forName(sb.toString());
            NodeFactory factory = (NodeFactory)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return factory;
        }
        catch (ReflectiveOperationException e) {
            throw new IllegalStateException(sb.toString(), e);
        }
    }

    default public boolean weakValues() {
        return false;
    }

    default public boolean softValues() {
        return false;
    }
}

