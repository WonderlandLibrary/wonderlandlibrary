/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSOneSamplerPeer;
import java.util.HashMap;

public class PPSColorAdjustPeer
extends PPSOneSamplerPeer {
    public PPSColorAdjustPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final ColorAdjust getEffect() {
        return (ColorAdjust)super.getEffect();
    }

    private float getHue() {
        return this.getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return this.getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return this.getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float f = this.getEffect().getContrast();
        if (f > 0.0f) {
            f *= 3.0f;
        }
        return f + 1.0f;
    }

    @Override
    protected boolean isSamplerLinear(int n) {
        switch (n) {
            default: 
        }
        return false;
    }

    @Override
    protected Shader createShader() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("baseImg", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("saturation", 1);
        hashMap2.put("brightness", 2);
        hashMap2.put("contrast", 3);
        hashMap2.put("hue", 0);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        shader.setConstant("saturation", this.getSaturation());
        shader.setConstant("brightness", this.getBrightness());
        shader.setConstant("contrast", this.getContrast());
        shader.setConstant("hue", this.getHue());
    }
}

