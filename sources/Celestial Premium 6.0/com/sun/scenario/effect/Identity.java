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
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Offset;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;
import java.util.Map;

public class Identity
extends Effect {
    private Filterable src;
    private Point2D loc = new Point2D();
    private final Map<FilterContext, ImageData> datacache = new HashMap<FilterContext, ImageData>();

    public Identity(Filterable filterable) {
        this.src = filterable;
    }

    public final Filterable getSource() {
        return this.src;
    }

    public void setSource(Filterable filterable) {
        Filterable filterable2 = this.src;
        this.src = filterable;
        this.clearCache();
    }

    public final Point2D getLocation() {
        return this.loc;
    }

    public void setLocation(Point2D point2D) {
        if (point2D == null) {
            throw new IllegalArgumentException("Location must be non-null");
        }
        Point2D point2D2 = this.loc;
        this.loc.setLocation(point2D);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        if (this.src == null) {
            return new RectBounds();
        }
        float f = (float)this.src.getPhysicalWidth() / this.src.getPixelScale();
        float f2 = (float)this.src.getPhysicalHeight() / this.src.getPixelScale();
        BaseBounds baseBounds = new RectBounds(this.loc.x, this.loc.y, this.loc.x + f, this.loc.y + f2);
        if (baseTransform != null && !baseTransform.isIdentity()) {
            baseBounds = Identity.transformBounds(baseTransform, baseBounds);
        }
        return baseBounds;
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        ImageData imageData = this.datacache.get(filterContext);
        if (imageData != null && !imageData.addref()) {
            imageData.setReusable(false);
            this.datacache.remove(filterContext);
            imageData.unref();
            imageData = null;
        }
        if (imageData == null) {
            Renderer renderer = Renderer.getRenderer(filterContext);
            Filterable filterable = this.src;
            if (filterable == null) {
                filterable = Identity.getCompatibleImage(filterContext, 1, 1);
                imageData = new ImageData(filterContext, filterable, new Rectangle(1, 1));
            } else {
                imageData = renderer.createImageData(filterContext, filterable);
            }
            if (imageData == null) {
                return new ImageData(filterContext, null, null);
            }
            imageData.setReusable(true);
            this.datacache.put(filterContext, imageData);
        }
        baseTransform = Offset.getOffsetTransform(baseTransform, this.loc.x, this.loc.y);
        imageData = imageData.transform(baseTransform);
        return imageData;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return Effect.AccelType.INTRINSIC;
    }

    private void clearCache() {
        this.datacache.clear();
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
        dirtyRegionContainer.reset();
        return dirtyRegionContainer;
    }
}

