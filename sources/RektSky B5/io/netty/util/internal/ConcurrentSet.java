/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

public final class ConcurrentSet<E>
extends AbstractSet<E>
implements Serializable {
    private static final long serialVersionUID = -6761513279741915432L;
    private final ConcurrentMap<E, Boolean> map = PlatformDependent.newConcurrentHashMap();

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean contains(Object o2) {
        return this.map.containsKey(o2);
    }

    @Override
    public boolean add(E o2) {
        return this.map.putIfAbsent(o2, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o2) {
        return this.map.remove(o2) != null;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }
}

