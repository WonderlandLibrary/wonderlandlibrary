/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.Stop;
import java.util.List;

public final class RadialGradient
extends Gradient {
    private final float centerX;
    private final float centerY;
    private final float focusAngle;
    private final float focusDistance;
    private final float radius;

    public RadialGradient(float f, float f2, float f3, float f4, float f5, BaseTransform baseTransform, boolean bl, int n, List<Stop> list) {
        super(Paint.Type.RADIAL_GRADIENT, baseTransform, bl, n, list);
        this.centerX = f;
        this.centerY = f2;
        this.focusAngle = f3;
        this.focusDistance = f4;
        this.radius = f5;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getFocusAngle() {
        return this.focusAngle;
    }

    public float getFocusDistance() {
        return this.focusDistance;
    }

    public float getRadius() {
        return this.radius;
    }

    public String toString() {
        return "RadialGradient: FocusAngle: " + this.focusAngle + " FocusDistance: " + this.focusDistance + " CenterX: " + this.centerX + " CenterY " + this.centerY + " Radius: " + this.radius + "stops:" + this.getStops();
    }
}

