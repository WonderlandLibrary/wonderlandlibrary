/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Flood;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrFloodPeer
extends EffectPeer {
    public PrFloodPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        FilterContext filterContext = this.getFilterContext();
        Flood flood = (Flood)effect;
        RectBounds rectBounds = flood.getFloodBounds();
        int n = (int)((BaseBounds)rectBounds).getMinX();
        int n2 = (int)((BaseBounds)rectBounds).getMinY();
        int n3 = (int)((BaseBounds)rectBounds).getWidth();
        int n4 = (int)((BaseBounds)rectBounds).getHeight();
        BaseBounds baseBounds = Effect.transformBounds(baseTransform, rectBounds);
        Rectangle rectangle2 = new Rectangle(baseBounds);
        rectangle2.intersectWith(rectangle);
        int n5 = rectangle2.width;
        int n6 = rectangle2.height;
        PrDrawable prDrawable = (PrDrawable)this.getRenderer().getCompatibleImage(n5, n6);
        if (prDrawable != null) {
            Graphics graphics = prDrawable.createGraphics();
            graphics.translate(-rectangle2.x, -rectangle2.y);
            if (baseTransform != null && !baseTransform.isIdentity()) {
                graphics.transform(baseTransform);
            }
            graphics.setPaint((Paint)flood.getPaint());
            graphics.fillQuad(n, n2, n + n3, n2 + n4);
        }
        return new ImageData(filterContext, prDrawable, rectangle2);
    }
}

