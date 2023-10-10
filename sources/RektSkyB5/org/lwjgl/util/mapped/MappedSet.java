/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.mapped;

import org.lwjgl.util.mapped.MappedObject;
import org.lwjgl.util.mapped.MappedSet2;
import org.lwjgl.util.mapped.MappedSet3;
import org.lwjgl.util.mapped.MappedSet4;

public class MappedSet {
    public static MappedSet2 create(MappedObject a2, MappedObject b2) {
        return new MappedSet2(a2, b2);
    }

    public static MappedSet3 create(MappedObject a2, MappedObject b2, MappedObject c2) {
        return new MappedSet3(a2, b2, c2);
    }

    public static MappedSet4 create(MappedObject a2, MappedObject b2, MappedObject c2, MappedObject d2) {
        return new MappedSet4(a2, b2, c2, d2);
    }
}

