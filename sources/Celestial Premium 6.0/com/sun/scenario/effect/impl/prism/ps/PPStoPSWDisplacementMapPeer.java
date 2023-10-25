/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.state.RenderState;

public class PPStoPSWDisplacementMapPeer
extends EffectPeer {
    PrRenderer softwareRenderer;
    EffectPeer softwarePeer;

    public PPStoPSWDisplacementMapPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
        this.softwareRenderer = (PrRenderer)Renderer.getRenderer(filterContext);
        this.softwarePeer = this.softwareRenderer.getPeerInstance(filterContext, "DisplacementMap", 0);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        ImageData imageData = arrimageData[0];
        PrTexture prTexture = (PrTexture)((Object)imageData.getUntransformedImage());
        RTTexture rTTexture = (RTTexture)prTexture.getTextureObject();
        PrDrawable prDrawable = this.softwareRenderer.createDrawable(rTTexture);
        ImageData imageData2 = new ImageData(this.getFilterContext(), prDrawable, imageData.getUntransformedBounds());
        imageData2 = imageData2.transform(imageData.getTransform());
        ImageData imageData3 = this.softwarePeer.filter(effect, renderState, baseTransform, rectangle, imageData2);
        return imageData3;
    }
}

