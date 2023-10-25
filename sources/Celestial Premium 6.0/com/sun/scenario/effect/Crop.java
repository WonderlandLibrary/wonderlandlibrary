/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

public class Crop
extends CoreEffect<RenderState> {
    public Crop(Effect effect) {
        this(effect, DefaultInput);
    }

    public Crop(Effect effect, Effect effect2) {
        super(effect, effect2);
        this.updatePeerKey("Crop");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public final Effect getBoundsInput() {
        return this.getInputs().get(1);
    }

    public void setBoundsInput(Effect effect) {
        this.setInput(1, effect);
    }

    private Effect getBoundsInput(Effect effect) {
        return this.getDefaultedInput(1, effect);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        return this.getBoundsInput(effect).getBounds(baseTransform, effect);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        Effect effect2 = this.getDefaultedInput(1, effect);
        BaseBounds baseBounds = effect2.getBounds(baseTransform, effect);
        Rectangle rectangle2 = new Rectangle(baseBounds);
        rectangle2.intersectWith(rectangle);
        Effect effect3 = this.getDefaultedInput(0, effect);
        ImageData imageData = effect3.filter(filterContext, baseTransform, rectangle2, null, effect);
        if (!imageData.validate(filterContext)) {
            return new ImageData(filterContext, null, null);
        }
        RenderState renderState = this.getRenderState(filterContext, baseTransform, rectangle2, object, effect);
        ImageData imageData2 = this.filterImageDatas(filterContext, baseTransform, rectangle2, renderState, new ImageData[]{imageData});
        imageData.unref();
        return imageData2;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        Effect effect3 = this.getDefaultedInput(1, effect);
        BaseBounds baseBounds = effect3.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        for (int i = 0; i < dirtyRegionContainer.size(); ++i) {
            dirtyRegionContainer.getDirtyRegion(i).intersectWith(baseBounds);
            if (!dirtyRegionContainer.checkAndClearRegion(i)) continue;
            --i;
        }
        return dirtyRegionContainer;
    }
}

