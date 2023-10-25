/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class UniqueName
implements Comparable<UniqueName> {
    private final String name;
    private final int id;
    private static final AtomicInteger nextId = new AtomicInteger();

    public UniqueName(ConcurrentMap<String, Boolean> concurrentMap, String string, Object ... objectArray) {
        if (concurrentMap == null) {
            throw new NullPointerException("map");
        }
        if (string == null) {
            throw new NullPointerException("name");
        }
        if (objectArray != null && objectArray.length > 0) {
            this.validateArgs(objectArray);
        }
        if (concurrentMap.putIfAbsent(string, Boolean.TRUE) != null) {
            throw new IllegalArgumentException(String.format("'%s' is already in use", string));
        }
        this.id = nextId.incrementAndGet();
        this.name = string;
    }

    protected void validateArgs(Object ... objectArray) {
    }

    public final String name() {
        return this.name;
    }

    public final boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public int compareTo(UniqueName uniqueName) {
        if (this == uniqueName) {
            return 0;
        }
        int n = this.name.compareTo(uniqueName.name);
        if (n != 0) {
            return n;
        }
        return Integer.valueOf(this.id).compareTo(uniqueName.id);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final int id() {
        return this.id;
    }

    public String toString() {
        return this.name();
    }
}

