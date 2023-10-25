/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGDefaultCamera;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;

public abstract class NGCamera
extends NGNode {
    public static final NGCamera INSTANCE = new NGDefaultCamera();
    protected Affine3D worldTransform = new Affine3D();
    protected double viewWidth = 1.0;
    protected double viewHeight = 1.0;
    protected double zNear = 0.1;
    protected double zFar = 100.0;
    private Vec3d worldPosition = new Vec3d();
    protected GeneralTransform3D projViewTx = new GeneralTransform3D();

    @Override
    protected void doRender(Graphics graphics) {
    }

    @Override
    protected void renderContent(Graphics graphics) {
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setNearClip(float f) {
        this.zNear = f;
    }

    public double getNearClip() {
        return this.zNear;
    }

    public void setFarClip(float f) {
        this.zFar = f;
    }

    public double getFarClip() {
        return this.zFar;
    }

    public void setViewWidth(double d) {
        this.viewWidth = d;
    }

    public double getViewWidth() {
        return this.viewWidth;
    }

    public void setViewHeight(double d) {
        this.viewHeight = d;
    }

    public double getViewHeight() {
        return this.viewHeight;
    }

    public void setProjViewTransform(GeneralTransform3D generalTransform3D) {
        this.projViewTx.set(generalTransform3D);
    }

    public void setPosition(Vec3d vec3d) {
        this.worldPosition.set(vec3d);
    }

    public void setWorldTransform(Affine3D affine3D) {
        this.worldTransform.setTransform(affine3D);
    }

    public GeneralTransform3D getProjViewTx(GeneralTransform3D generalTransform3D) {
        if (generalTransform3D == null) {
            generalTransform3D = new GeneralTransform3D();
        }
        return generalTransform3D.set(this.projViewTx);
    }

    public Vec3d getPositionInWorld(Vec3d vec3d) {
        if (vec3d == null) {
            vec3d = new Vec3d();
        }
        vec3d.set(this.worldPosition);
        return vec3d;
    }

    @Override
    public void release() {
    }

    public abstract PickRay computePickRay(float var1, float var2, PickRay var3);
}

