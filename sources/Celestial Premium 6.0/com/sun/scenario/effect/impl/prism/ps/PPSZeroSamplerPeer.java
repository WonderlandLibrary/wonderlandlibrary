/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.paint.Color;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;

public abstract class PPSZeroSamplerPeer
extends PPSEffectPeer {
    private Shader shader;

    protected PPSZeroSamplerPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public void dispose() {
        if (this.shader != null) {
            this.shader.dispose();
        }
    }

    @Override
    ImageData filterImpl(ImageData ... arrimageData) {
        Rectangle rectangle = this.getDestBounds();
        int n = rectangle.width;
        int n2 = rectangle.height;
        PPSRenderer pPSRenderer = this.getRenderer();
        PPSDrawable pPSDrawable = pPSRenderer.getCompatibleImage(n, n2);
        if (pPSDrawable == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle);
        }
        this.setDestNativeBounds(pPSDrawable.getPhysicalWidth(), pPSDrawable.getPhysicalHeight());
        ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
        if (shaderGraphics == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle);
        }
        if (this.shader == null) {
            this.shader = this.createShader();
        }
        if (this.shader == null || !this.shader.isValid()) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle);
        }
        shaderGraphics.setExternalShader(this.shader);
        this.updateShader(this.shader);
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = n;
        float f4 = n2;
        shaderGraphics.setPaint(Color.WHITE);
        shaderGraphics.fillQuad(f, f2, f3, f4);
        shaderGraphics.setExternalShader(null);
        return new ImageData(this.getFilterContext(), pPSDrawable, rectangle);
    }
}

