/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrCropPeer
extends EffectPeer {
    public PrCropPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        FilterContext filterContext = this.getFilterContext();
        ImageData imageData = arrimageData[0];
        Rectangle rectangle2 = imageData.getTransformedBounds(null);
        if (rectangle.contains(rectangle2)) {
            imageData.addref();
            return imageData;
        }
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.intersectWith(rectangle);
        int n = rectangle3.width;
        int n2 = rectangle3.height;
        PrDrawable prDrawable = (PrDrawable)this.getRenderer().getCompatibleImage(n, n2);
        if (!imageData.validate(filterContext) || prDrawable == null) {
            prDrawable = null;
        } else {
            Graphics graphics = prDrawable.createGraphics();
            PrEffectHelper.renderImageData(graphics, imageData, rectangle3);
        }
        return new ImageData(filterContext, prDrawable, rectangle3);
    }
}

