/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.sg.prism.NGCamera;

public class NGParallelCamera
extends NGCamera {
    @Override
    public PickRay computePickRay(float f, float f2, PickRay pickRay) {
        return PickRay.computeParallelPickRay(f, f2, this.viewHeight, this.worldTransform, this.zNear, this.zFar, pickRay);
    }
}

