/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick;

import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.geom.Shape;
import me.kiras.aimwhere.libraries.slick.geom.Vector2f;

public interface ShapeFill {
    public Color colorAt(Shape var1, float var2, float var3);

    public Vector2f getOffsetAt(Shape var1, float var2, float var3);
}

