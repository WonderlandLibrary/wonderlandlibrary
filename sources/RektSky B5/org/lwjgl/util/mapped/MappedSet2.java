/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.mapped;

import org.lwjgl.util.mapped.MappedObject;

public class MappedSet2 {
    private final MappedObject a;
    private final MappedObject b;
    public int view;

    MappedSet2(MappedObject a2, MappedObject b2) {
        this.a = a2;
        this.b = b2;
    }

    void view(int view) {
        this.a.setViewAddress(this.a.getViewAddress(view));
        this.b.setViewAddress(this.b.getViewAddress(view));
    }

    public void next() {
        this.a.next();
        this.b.next();
    }
}

