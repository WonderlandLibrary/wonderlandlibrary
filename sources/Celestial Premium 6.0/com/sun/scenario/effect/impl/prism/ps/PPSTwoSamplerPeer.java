/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Texture;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.prism.ps.PPSDrawable;
import com.sun.scenario.effect.impl.prism.ps.PPSEffectPeer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;

public abstract class PPSTwoSamplerPeer
extends PPSEffectPeer {
    private Shader shader;

    protected PPSTwoSamplerPeer(FilterContext filterContext, Renderer renderer, String string) {
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
        int n;
        Object object;
        Rectangle rectangle;
        PrTexture prTexture;
        Object object2;
        Rectangle rectangle2 = this.getDestBounds();
        int n2 = rectangle2.width;
        int n3 = rectangle2.height;
        PPSRenderer pPSRenderer = this.getRenderer();
        PPSDrawable pPSDrawable = pPSRenderer.getCompatibleImage(n2, n3);
        if (pPSDrawable == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        this.setDestNativeBounds(pPSDrawable.getPhysicalWidth(), pPSDrawable.getPhysicalHeight());
        Filterable filterable = arrimageData[0].getUntransformedImage();
        PrTexture prTexture2 = (PrTexture)((Object)filterable);
        Rectangle rectangle3 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle3);
        this.setInputTransform(0, baseTransform);
        this.setInputNativeBounds(0, prTexture2.getNativeBounds());
        float[] arrf = new float[8];
        if (arrimageData.length > 1) {
            object2 = arrimageData[1].getUntransformedImage();
            prTexture = (PrTexture)object2;
            if (prTexture == null) {
                pPSRenderer.markLost();
                return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
            }
            rectangle = arrimageData[1].getUntransformedBounds();
            object = arrimageData[1].getTransform();
            this.setInputBounds(1, rectangle);
            this.setInputTransform(1, (BaseTransform)object);
            this.setInputNativeBounds(1, prTexture.getNativeBounds());
            n = this.getTextureCoordinates(1, arrf, rectangle.x, rectangle.y, object2.getPhysicalWidth(), object2.getPhysicalHeight(), rectangle2, (BaseTransform)object);
        } else {
            object2 = (FloatMap)this.getSamplerData(1);
            prTexture = (PrTexture)((FloatMap)object2).getAccelData(this.getFilterContext());
            if (prTexture == null) {
                pPSRenderer.markLost();
                return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
            }
            rectangle = new Rectangle(((FloatMap)object2).getWidth(), ((FloatMap)object2).getHeight());
            object = prTexture.getNativeBounds();
            this.setInputBounds(1, rectangle);
            this.setInputNativeBounds(1, (Rectangle)object);
            arrf[1] = 0.0f;
            arrf[0] = 0.0f;
            arrf[2] = (float)rectangle.width / (float)((Rectangle)object).width;
            arrf[3] = (float)rectangle.height / (float)((Rectangle)object).height;
            n = 4;
        }
        object2 = new float[8];
        int n4 = this.getTextureCoordinates(0, (float[])object2, rectangle3.x, rectangle3.y, filterable.getPhysicalWidth(), filterable.getPhysicalHeight(), rectangle2, baseTransform);
        object = pPSDrawable.createGraphics();
        if (object == null) {
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
        object.setExternalShader(this.shader);
        this.updateShader(this.shader);
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = n2;
        float f4 = n3;
        Object t = prTexture2.getTextureObject();
        if (t == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        Object t2 = prTexture.getTextureObject();
        if (t2 == null) {
            pPSRenderer.markLost();
            return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
        }
        float f5 = (float)t.getContentX() / (float)t.getPhysicalWidth();
        float f6 = (float)t.getContentY() / (float)t.getPhysicalHeight();
        float f7 = f5 + object2[0];
        float f8 = f6 + object2[1];
        float f9 = f5 + object2[2];
        float f10 = f6 + object2[3];
        float f11 = (float)t2.getContentX() / (float)t2.getPhysicalWidth();
        float f12 = (float)t2.getContentY() / (float)t2.getPhysicalHeight();
        float f13 = f11 + arrf[0];
        float f14 = f12 + arrf[1];
        float f15 = f11 + arrf[2];
        float f16 = f12 + arrf[3];
        if (n4 < 8 && n < 8) {
            object.drawTextureRaw2((Texture)t, (Texture)t2, f, f2, f3, f4, f7, f8, f9, f10, f13, f14, f15, f16);
        } else {
            float f17;
            float f18;
            float f19;
            float f20;
            float f21;
            float f22;
            float f23;
            float f24;
            if (n4 < 8) {
                f24 = f9;
                f23 = f8;
                f22 = f7;
                f21 = f10;
            } else {
                f24 = f5 + object2[4];
                f23 = f6 + object2[5];
                f22 = f5 + object2[6];
                f21 = f6 + object2[7];
            }
            if (n < 8) {
                f20 = f15;
                f19 = f14;
                f18 = f13;
                f17 = f16;
            } else {
                f20 = f11 + arrf[4];
                f19 = f12 + arrf[5];
                f18 = f11 + arrf[6];
                f17 = f12 + arrf[7];
            }
            object.drawMappedTextureRaw2((Texture)t, (Texture)t2, f, f2, f3, f4, f7, f8, f24, f23, f22, f21, f9, f10, f13, f14, f20, f19, f18, f17, f15, f16);
        }
        object.setExternalShader(null);
        if (arrimageData.length <= 1) {
            prTexture.unlock();
        }
        return new ImageData(this.getFilterContext(), pPSDrawable, rectangle2);
    }
}

