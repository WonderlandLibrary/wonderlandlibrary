/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.util.Objects;

class JavaSuperAdapter {
    private final Object adapter;

    JavaSuperAdapter(Object adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }

    public Object getAdapter() {
        return this.adapter;
    }
}

