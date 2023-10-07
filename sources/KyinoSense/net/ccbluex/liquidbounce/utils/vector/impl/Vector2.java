/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.vector.impl;

import net.ccbluex.liquidbounce.utils.vector.Vector;
import net.ccbluex.liquidbounce.utils.vector.impl.Vector3;

public class Vector2<T extends Number>
extends Vector<Number> {
    public Vector2(T x, T y) {
        super(x, y, 0);
    }

    public Vector3<T> toVector3() {
        return new Vector3(this.getX(), this.getY(), this.getZ());
    }
}

