/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.util;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;

public final class TempState {
    public BaseBounds bounds = new RectBounds(0.0f, 0.0f, -1.0f, -1.0f);
    public final BaseTransform pickTx = new Affine3D();
    public final Affine3D leafTx = new Affine3D();
    public final Point2D point = new Point2D(0.0f, 0.0f);
    public final Vec3d vec3d = new Vec3d(0.0, 0.0, 0.0);
    public final GeneralTransform3D projViewTx = new GeneralTransform3D();
    public final Affine3D tempTx = new Affine3D();
    private static final ThreadLocal<TempState> tempStateRef = new ThreadLocal<TempState>(){

        @Override
        protected TempState initialValue() {
            return new TempState();
        }
    };

    private TempState() {
    }

    public static TempState getInstance() {
        return tempStateRef.get();
    }
}

