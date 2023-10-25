/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;

public class PerspectiveTransform
extends CoreEffect<RenderState> {
    private float[][] tx = new float[3][3];
    private float ulx;
    private float uly;
    private float urx;
    private float ury;
    private float lrx;
    private float lry;
    private float llx;
    private float lly;
    private float[] devcoords = new float[8];
    private final PerspectiveTransformState state = new PerspectiveTransformState();

    public PerspectiveTransform() {
        this(DefaultInput);
    }

    public PerspectiveTransform(Effect effect) {
        super(effect);
        this.setQuadMapping(0.0f, 0.0f, 100.0f, 0.0f, 100.0f, 100.0f, 0.0f, 100.0f);
        this.updatePeerKey("PerspectiveTransform");
    }

    @Override
    Object getState() {
        return this.state;
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    private void setUnitQuadMapping(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f - f3 + f5 - f7;
        float f10 = f2 - f4 + f6 - f8;
        this.tx[2][2] = 1.0f;
        if (f9 == 0.0f && f10 == 0.0f) {
            this.tx[0][0] = f3 - f;
            this.tx[0][1] = f5 - f3;
            this.tx[0][2] = f;
            this.tx[1][0] = f4 - f2;
            this.tx[1][1] = f6 - f4;
            this.tx[1][2] = f2;
            this.tx[2][0] = 0.0f;
            this.tx[2][1] = 0.0f;
        } else {
            float f11 = f3 - f5;
            float f12 = f4 - f6;
            float f13 = f7 - f5;
            float f14 = f8 - f6;
            float f15 = 1.0f / (f11 * f14 - f13 * f12);
            this.tx[2][0] = (f9 * f14 - f13 * f10) * f15;
            this.tx[2][1] = (f11 * f10 - f9 * f12) * f15;
            this.tx[0][0] = f3 - f + this.tx[2][0] * f3;
            this.tx[0][1] = f7 - f + this.tx[2][1] * f7;
            this.tx[0][2] = f;
            this.tx[1][0] = f4 - f2 + this.tx[2][0] * f4;
            this.tx[1][1] = f8 - f2 + this.tx[2][1] * f8;
            this.tx[1][2] = f2;
        }
        this.state.updateTx(this.tx);
    }

    public final void setQuadMapping(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.ulx = f;
        this.uly = f2;
        this.urx = f3;
        this.ury = f4;
        this.lrx = f5;
        this.lry = f6;
        this.llx = f7;
        this.lly = f8;
    }

    @Override
    public RectBounds getBounds(BaseTransform baseTransform, Effect effect) {
        float f;
        float f2;
        this.setupDevCoords(baseTransform);
        float f3 = f2 = this.devcoords[0];
        float f4 = f = this.devcoords[1];
        for (int i = 2; i < this.devcoords.length; i += 2) {
            if (f3 > this.devcoords[i]) {
                f3 = this.devcoords[i];
            } else if (f2 < this.devcoords[i]) {
                f2 = this.devcoords[i];
            }
            if (f4 > this.devcoords[i + 1]) {
                f4 = this.devcoords[i + 1];
                continue;
            }
            if (!(f < this.devcoords[i + 1])) continue;
            f = this.devcoords[i + 1];
        }
        return new RectBounds(f3, f4, f2, f);
    }

    private void setupDevCoords(BaseTransform baseTransform) {
        this.devcoords[0] = this.ulx;
        this.devcoords[1] = this.uly;
        this.devcoords[2] = this.urx;
        this.devcoords[3] = this.ury;
        this.devcoords[4] = this.lrx;
        this.devcoords[5] = this.lry;
        this.devcoords[6] = this.llx;
        this.devcoords[7] = this.lly;
        baseTransform.transform(this.devcoords, 0, this.devcoords, 0, 4);
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        this.setupTransforms(baseTransform);
        RenderState renderState = this.getRenderState(filterContext, baseTransform, rectangle, object, effect);
        Effect effect2 = this.getDefaultedInput(0, effect);
        Rectangle rectangle2 = renderState.getInputClip(0, rectangle);
        ImageData imageData = effect2.filter(filterContext, BaseTransform.IDENTITY_TRANSFORM, rectangle2, null, effect);
        if (!imageData.validate(filterContext)) {
            imageData.unref();
            return new ImageData(filterContext, null, imageData.getUntransformedBounds());
        }
        ImageData imageData2 = this.filterImageDatas(filterContext, baseTransform, rectangle, renderState, new ImageData[]{imageData});
        imageData.unref();
        return imageData2;
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = new Rectangle(this.getBounds(baseTransform, null));
        rectangle2.intersectWith(rectangle);
        return rectangle2;
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        this.setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
        Effect effect2 = this.getDefaultedInput(0, effect);
        point2D = effect2.transform(point2D, effect);
        BaseBounds baseBounds = effect2.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        float f = (point2D.x - baseBounds.getMinX()) / baseBounds.getWidth();
        float f2 = (point2D.y - baseBounds.getMinY()) / baseBounds.getHeight();
        float f3 = this.tx[0][0] * f + this.tx[0][1] * f2 + this.tx[0][2];
        float f4 = this.tx[1][0] * f + this.tx[1][1] * f2 + this.tx[1][2];
        float f5 = this.tx[2][0] * f + this.tx[2][1] * f2 + this.tx[2][2];
        point2D = new Point2D(f3 / f5, f4 / f5);
        return point2D;
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        this.setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
        Effect effect2 = this.getDefaultedInput(0, effect);
        float f = point2D.x;
        float f2 = point2D.y;
        float[][] arrf = this.state.getITX();
        float f3 = arrf[0][0] * f + arrf[0][1] * f2 + arrf[0][2];
        float f4 = arrf[1][0] * f + arrf[1][1] * f2 + arrf[1][2];
        float f5 = arrf[2][0] * f + arrf[2][1] * f2 + arrf[2][2];
        BaseBounds baseBounds = effect2.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        point2D = new Point2D(baseBounds.getMinX() + f3 / f5 * baseBounds.getWidth(), baseBounds.getMinY() + f4 / f5 * baseBounds.getHeight());
        point2D = this.getDefaultedInput(0, effect).untransform(point2D, effect);
        return point2D;
    }

    private void setupTransforms(BaseTransform baseTransform) {
        this.setupDevCoords(baseTransform);
        this.setUnitQuadMapping(this.devcoords[0], this.devcoords[1], this.devcoords[2], this.devcoords[3], this.devcoords[4], this.devcoords[5], this.devcoords[6], this.devcoords[7]);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
        dirtyRegionContainer.deriveWithNewRegion(this.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect));
        return dirtyRegionContainer;
    }
}

