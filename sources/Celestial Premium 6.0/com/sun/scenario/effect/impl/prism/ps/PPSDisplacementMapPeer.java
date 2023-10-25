/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSTwoSamplerPeer;
import java.util.HashMap;

public class PPSDisplacementMapPeer
extends PPSTwoSamplerPeer {
    public PPSDisplacementMapPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final DisplacementMap getEffect() {
        return (DisplacementMap)super.getEffect();
    }

    private float[] getSampletx() {
        return new float[]{this.getEffect().getOffsetX(), this.getEffect().getOffsetY(), this.getEffect().getScaleX(), this.getEffect().getScaleY()};
    }

    private float[] getImagetx() {
        float f = this.getEffect().getWrap() ? 0.5f : 0.0f;
        return new float[]{f / (float)this.getInputNativeBounds((int)0).width, f / (float)this.getInputNativeBounds((int)0).height, ((float)this.getInputBounds((int)0).width - 2.0f * f) / (float)this.getInputNativeBounds((int)0).width, ((float)this.getInputBounds((int)0).height - 2.0f * f) / (float)this.getInputNativeBounds((int)0).height};
    }

    private float getWrap() {
        return this.getEffect().getWrap() ? 1.0f : 0.0f;
    }

    @Override
    protected Object getSamplerData(int n) {
        return this.getEffect().getMapData();
    }

    @Override
    public int getTextureCoordinates(int n, float[] arrf, float f, float f2, float f3, float f4, Rectangle rectangle, BaseTransform baseTransform) {
        arrf[1] = 0.0f;
        arrf[0] = 0.0f;
        arrf[3] = 1.0f;
        arrf[2] = 1.0f;
        return 4;
    }

    @Override
    protected boolean isSamplerLinear(int n) {
        switch (n) {
            case 0: {
                return true;
            }
            case 1: {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Shader createShader() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("origImg", 0);
        hashMap.put("mapImg", 1);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("imagetx", 1);
        hashMap2.put("wrap", 2);
        hashMap2.put("sampletx", 0);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        float[] arrf = this.getImagetx();
        shader.setConstant("imagetx", arrf[0], arrf[1], arrf[2], arrf[3]);
        shader.setConstant("wrap", this.getWrap());
        float[] arrf2 = this.getSampletx();
        shader.setConstant("sampletx", arrf2[0], arrf2[1], arrf2[2], arrf2[3]);
    }
}

