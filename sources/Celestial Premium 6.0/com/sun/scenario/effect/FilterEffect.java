/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.ImageDataRenderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class FilterEffect<T extends RenderState>
extends Effect {
    protected FilterEffect() {
    }

    protected FilterEffect(Effect effect) {
        super(effect);
    }

    protected FilterEffect(Effect effect, Effect effect2) {
        super(effect, effect2);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseBounds baseBounds;
        int n = this.getNumInputs();
        T t = this.getRenderState(null, baseTransform, null, null, effect);
        BaseTransform baseTransform2 = t.getInputTransform(baseTransform);
        if (n == 1) {
            Effect effect2 = this.getDefaultedInput(0, effect);
            baseBounds = effect2.getBounds(baseTransform2, effect);
        } else {
            BaseBounds[] arrbaseBounds = new BaseBounds[n];
            for (int i = 0; i < n; ++i) {
                Effect effect3 = this.getDefaultedInput(i, effect);
                arrbaseBounds[i] = effect3.getBounds(baseTransform2, effect);
            }
            baseBounds = FilterEffect.combineBounds(arrbaseBounds);
        }
        return FilterEffect.transformBounds(t.getResultTransform(baseTransform), baseBounds);
    }

    protected static Rectangle untransformClip(BaseTransform baseTransform, Rectangle rectangle) {
        if (baseTransform.isIdentity() || rectangle == null || rectangle.isEmpty()) {
            return rectangle;
        }
        Rectangle rectangle2 = new Rectangle();
        if (baseTransform.isTranslateOrIdentity()) {
            rectangle2.setBounds(rectangle);
            double d = -baseTransform.getMxt();
            double d2 = -baseTransform.getMyt();
            int n = (int)Math.floor(d);
            int n2 = (int)Math.floor(d2);
            rectangle2.translate(n, n2);
            if ((double)n != d) {
                ++rectangle2.width;
            }
            if ((double)n2 != d2) {
                ++rectangle2.height;
            }
            return rectangle2;
        }
        RectBounds rectBounds = new RectBounds(rectangle);
        try {
            rectBounds.grow(-0.5f, -0.5f);
            rectBounds = (RectBounds)baseTransform.inverseTransform(rectBounds, rectBounds);
            rectBounds.grow(0.5f, 0.5f);
            rectangle2.setBounds(rectBounds);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        return rectangle2;
    }

    public abstract T getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5);

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        T t = this.getRenderState(filterContext, baseTransform, rectangle, object, effect);
        int n = this.getNumInputs();
        ImageData[] arrimageData = new ImageData[n];
        BaseTransform baseTransform2 = t.getInputTransform(baseTransform);
        BaseTransform baseTransform3 = t.getResultTransform(baseTransform);
        Rectangle rectangle2 = baseTransform3.isIdentity() ? rectangle : FilterEffect.untransformClip(baseTransform3, rectangle);
        for (int i = 0; i < n; ++i) {
            Effect effect2 = this.getDefaultedInput(i, effect);
            arrimageData[i] = effect2.filter(filterContext, baseTransform2, t.getInputClip(i, rectangle2), null, effect);
            if (arrimageData[i].validate(filterContext)) continue;
            for (int j = 0; j <= i; ++j) {
                arrimageData[j].unref();
            }
            return new ImageData(filterContext, null, null);
        }
        ImageData imageData = this.filterImageDatas(filterContext, baseTransform2, rectangle2, t, arrimageData);
        for (int i = 0; i < n; ++i) {
            arrimageData[i].unref();
        }
        if (!baseTransform3.isIdentity()) {
            if (object instanceof ImageDataRenderer) {
                ImageDataRenderer imageDataRenderer = (ImageDataRenderer)object;
                imageDataRenderer.renderImage(imageData, baseTransform3, filterContext);
                imageData.unref();
                imageData = null;
            } else {
                imageData = imageData.transform(baseTransform3);
            }
        }
        return imageData;
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }

    protected abstract ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, T var4, ImageData ... var5);
}

