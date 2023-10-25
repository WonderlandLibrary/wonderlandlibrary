/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point3D
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGLightBase;
import javafx.geometry.Point3D;

public class NGPointLight
extends NGLightBase {
    private static final float DEFAULT_CA = 1.0f;
    private static final float DEFAULT_LA = 0.0f;
    private static final float DEFAULT_QA = 0.0f;
    private static final float DEFAULT_MAX_RANGE = Float.POSITIVE_INFINITY;
    private static final Point3D SIMULATED_DIRECTION = new Point3D(0.0, 0.0, 1.0);
    private static final float SIMULATED_INNER_ANGLE = 0.0f;
    private static final float SIMULATED_OUTER_ANGLE = 180.0f;
    private static final float SIMULATED_FALLOFF = 0.0f;
    private float ca = 1.0f;
    private float la = 0.0f;
    private float qa = 0.0f;
    private float maxRange = Float.POSITIVE_INFINITY;

    public static float getDefaultCa() {
        return 1.0f;
    }

    public static float getDefaultLa() {
        return 0.0f;
    }

    public static float getDefaultQa() {
        return 0.0f;
    }

    public static float getDefaultMaxRange() {
        return Float.POSITIVE_INFINITY;
    }

    public static Point3D getSimulatedDirection() {
        return SIMULATED_DIRECTION;
    }

    public static float getSimulatedInnerAngle() {
        return 0.0f;
    }

    public static float getSimulatedOuterAngle() {
        return 180.0f;
    }

    public static float getSimulatedFalloff() {
        return 0.0f;
    }

    public Point3D getDirection() {
        return SIMULATED_DIRECTION;
    }

    public float getInnerAngle() {
        return 0.0f;
    }

    public float getOuterAngle() {
        return 180.0f;
    }

    public float getFalloff() {
        return 0.0f;
    }

    public float getCa() {
        return this.ca;
    }

    public void setCa(float f) {
        if (this.ca != f) {
            this.ca = f;
            this.visualsChanged();
        }
    }

    public float getLa() {
        return this.la;
    }

    public void setLa(float f) {
        if (this.la != f) {
            this.la = f;
            this.visualsChanged();
        }
    }

    public float getQa() {
        return this.qa;
    }

    public void setQa(float f) {
        if (this.qa != f) {
            this.qa = f;
            this.visualsChanged();
        }
    }

    public float getMaxRange() {
        return this.maxRange;
    }

    public void setMaxRange(float f) {
        float f2 = f = f < 0.0f ? 0.0f : f;
        if (this.maxRange != f) {
            this.maxRange = f;
            this.visualsChanged();
        }
    }
}

