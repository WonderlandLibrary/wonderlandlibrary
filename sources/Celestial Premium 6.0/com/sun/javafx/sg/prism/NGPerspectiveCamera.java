/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.sg.prism.NGCamera;

public class NGPerspectiveCamera
extends NGCamera {
    private final boolean fixedEyeAtCameraZero;
    private double fovrad;
    private boolean verticalFieldOfView;

    public NGPerspectiveCamera(boolean bl) {
        this.fixedEyeAtCameraZero = bl;
    }

    public void setFieldOfView(float f) {
        this.fovrad = Math.toRadians(f);
    }

    public void setVerticalFieldOfView(boolean bl) {
        this.verticalFieldOfView = bl;
    }

    @Override
    public PickRay computePickRay(float f, float f2, PickRay pickRay) {
        return PickRay.computePerspectivePickRay(f, f2, this.fixedEyeAtCameraZero, this.viewWidth, this.viewHeight, this.fovrad, this.verticalFieldOfView, this.worldTransform, this.zNear, this.zFar, pickRay);
    }
}

