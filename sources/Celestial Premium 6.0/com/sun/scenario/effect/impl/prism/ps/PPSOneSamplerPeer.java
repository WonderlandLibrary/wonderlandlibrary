/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class PPSOneSamplerPeer<T extends RenderState>
extends PPSEffectPeer<T> {
    private Shader shader;

    protected PPSOneSamplerPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        Filterable filterable = arrimageData[0].getUntransformedImage();
        PrTexture prTexture = (PrTexture)((Object)filterable);
        Rectangle rectangle = arrimageData[0].getUntransformedBounds();
        Rectangle rectangle2 = this.getDestBounds();
        int n = rectangle2.width;
        int n2 = rectangle2.height;
        PPSRenderer pPSRenderer = this.getRenderer();
        PPSDrawable pPSDrawable = pPSRenderer.getCompatibleImage(n, n2);
        if (pPSDrawable == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        this.setDestNativeBounds(pPSDrawable.getPhysicalWidth(), pPSDrawable.getPhysicalHeight());
        BaseTransform baseTransform = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle);
        this.setInputTransform(0, baseTransform);
        this.setInputNativeBounds(0, prTexture.getNativeBounds());
        float[] arrf = new float[8];
        int n3 = this.getTextureCoordinates(0, arrf, rectangle.x, rectangle.y, filterable.getPhysicalWidth(), filterable.getPhysicalHeight(), rectangle2, baseTransform);
        ShaderGraphics shaderGraphics = pPSDrawable.createGraphics();
        if (shaderGraphics == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        if (this.shader == null) {
            this.shader = this.createShader();
        }
        if (this.shader == null || !this.shader.isValid()) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        shaderGraphics.setExternalShader(this.shader);
        this.updateShader(this.shader);
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = n;
        float f4 = n2;
        Object t = prTexture.getTextureObject();
        if (t == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        float f5 = (float)t.getContentX() / (float)t.getPhysicalWidth();
        float f6 = (float)t.getContentY() / (float)t.getPhysicalHeight();
        float f7 = f5 + arrf[0];
        float f8 = f6 + arrf[1];
        float f9 = f5 + arrf[2];
        float f10 = f6 + arrf[3];
        if (n3 < 8) {
            shaderGraphics.drawTextureRaw((Texture)t, f, f2, f3, f4, f7, f8, f9, f10);
        } else {
            float f11 = f5 + arrf[4];
            float f12 = f6 + arrf[5];
            float f13 = f5 + arrf[6];
            float f14 = f6 + arrf[7];
            shaderGraphics.drawMappedTextureRaw((Texture)t, f, f2, f3, f4, f7, f8, f11, f12, f13, f14, f9, f10);
        }
        shaderGraphics.setExternalShader(null);
        return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
    }
}

