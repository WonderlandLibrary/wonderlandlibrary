/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.mapped;

import org.lwjgl.util.mapped.MappedObject;

public class MappedSet4 {
    private final MappedObject a;
    private final MappedObject b;
    private final MappedObject c;
    private final MappedObject d;
    public int view;

    MappedSet4(MappedObject a2, MappedObject b2, MappedObject c2, MappedObject d2) {
        this.a = a2;
        this.b = b2;
        this.c = c2;
        this.d = d2;
    }

    void view(int view) {
        this.a.setViewAddress(this.a.getViewAddress(view));
        this.b.setViewAddress(this.b.getViewAddress(view));
        this.c.setViewAddress(this.c.getViewAddress(view));
        this.d.setViewAddress(this.d.getViewAddress(view));
    }

    public void next() {
        this.a.next();
        this.b.next();
        this.c.next();
        this.d.next();
    }
}

