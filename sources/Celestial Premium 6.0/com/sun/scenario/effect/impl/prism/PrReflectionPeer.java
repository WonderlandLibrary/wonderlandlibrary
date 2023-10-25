/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Reflection;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrReflectionPeer
extends EffectPeer {
    public PrReflectionPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        FilterContext filterContext = this.getFilterContext();
        Reflection reflection = (Reflection)effect;
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        int n = rectangle2.width;
        int n2 = rectangle2.height;
        float f = (float)n2 + reflection.getTopOffset();
        float f2 = reflection.getFraction() * (float)n2;
        int n3 = (int)Math.floor(f);
        int n4 = (int)Math.ceil(f + f2);
        int n5 = n4 - n3;
        int n6 = n4 > n2 ? n4 : n2;
        PrDrawable prDrawable = (PrDrawable)this.getRenderer().getCompatibleImage(n, n6);
        if (!arrimageData[0].validate(filterContext) || prDrawable == null) {
            return new ImageData(filterContext, null, rectangle2);
        }
        PrDrawable prDrawable2 = (PrDrawable)arrimageData[0].getUntransformedImage();
        Object t = prDrawable2.getTextureObject();
        Graphics graphics = prDrawable.createGraphics();
        graphics.transform(arrimageData[0].getTransform());
        float f3 = 0.0f;
        float f4 = n2 - n5;
        float f5 = n;
        float f6 = n2;
        graphics.drawTextureVO((Texture)t, reflection.getBottomOpacity(), reflection.getTopOpacity(), 0.0f, n4, n, n3, f3, f4, f5, f6);
        graphics.drawTexture((Texture)t, 0.0f, 0.0f, n, n2);
        Rectangle rectangle3 = new Rectangle(rectangle2.x, rectangle2.y, n, n6);
        return new ImageData(filterContext, prDrawable, rectangle3);
    }
}

