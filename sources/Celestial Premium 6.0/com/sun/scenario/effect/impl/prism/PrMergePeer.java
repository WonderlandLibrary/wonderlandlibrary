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
import com.sun.scenario.effect.Merge;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrEffectHelper;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrMergePeer
extends EffectPeer {
    public PrMergePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        FilterContext filterContext = this.getFilterContext();
        Merge merge = (Merge)effect;
        Rectangle rectangle2 = merge.getResultBounds(baseTransform, rectangle, arrimageData);
        PrDrawable prDrawable = (PrDrawable)this.getRenderer().getCompatibleImage(rectangle2.width, rectangle2.height);
        if (prDrawable == null) {
            return new ImageData(filterContext, null, rectangle2);
        }
        Graphics graphics = prDrawable.createGraphics();
        for (ImageData imageData : arrimageData) {
            PrEffectHelper.renderImageData(graphics, imageData, rectangle2);
        }
        return new ImageData(filterContext, prDrawable, rectangle2);
    }
}

