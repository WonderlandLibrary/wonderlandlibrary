/*
 * Decompiled with CFR 0_122.
 */
package me.tojatta.api.utilities.vector.impl;

import me.tojatta.api.utilities.vector.Vector;
import me.tojatta.api.utilities.vector.impl.Vector2;

public class Vector3<T extends Number>
extends Vector<Number> {
    public Vector3(T x, T y, T z) {
        super(x, y, z);
    }

    public Vector2<T> toVector2() {
        return new Vector2(this.getX(), this.getY());
    }
}

