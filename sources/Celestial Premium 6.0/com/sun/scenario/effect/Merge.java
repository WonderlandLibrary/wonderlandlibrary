/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.ImageDataRenderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class Merge
extends CoreEffect<RenderState> {
    public Merge(Effect effect, Effect effect2) {
        super(effect, effect2);
        this.updatePeerKey("Merge");
    }

    public final Effect getBottomInput() {
        return this.getInputs().get(0);
    }

    public void setBottomInput(Effect effect) {
        this.setInput(0, effect);
    }

    public final Effect getTopInput() {
        return this.getInputs().get(1);
    }

    public void setTopInput(Effect effect) {
        this.setInput(1, effect);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).untransform(point2D, effect);
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        Object object2;
        Effect effect2 = this.getDefaultedInput(0, effect);
        Effect effect3 = this.getDefaultedInput(1, effect);
        ImageData imageData = effect2.filter(filterContext, baseTransform, rectangle, object, effect);
        if (imageData != null) {
            if (!imageData.validate(filterContext)) {
                return new ImageData(filterContext, null, null);
            }
            if (object instanceof ImageDataRenderer) {
                object2 = (ImageDataRenderer)object;
                object2.renderImage(imageData, BaseTransform.IDENTITY_TRANSFORM, filterContext);
                imageData.unref();
                imageData = null;
            }
        }
        if (imageData == null) {
            return effect3.filter(filterContext, baseTransform, rectangle, object, effect);
        }
        object2 = effect3.filter(filterContext, baseTransform, rectangle, null, effect);
        if (!((ImageData)object2).validate(filterContext)) {
            return new ImageData(filterContext, null, null);
        }
        RenderState renderState = this.getRenderState(filterContext, baseTransform, rectangle, object, effect);
        ImageData imageData2 = this.filterImageDatas(filterContext, baseTransform, rectangle, renderState, new ImageData[]{imageData, object2});
        imageData.unref();
        ((ImageData)object2).unref();
        return imageData2;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getTopInput();
        Effect effect2 = this.getBottomInput();
        return effect != null && effect.reducesOpaquePixels() && effect2 != null && effect2.reducesOpaquePixels();
    }
}

