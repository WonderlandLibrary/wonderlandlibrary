/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSOneSamplerPeer;
import java.util.HashMap;

public class PPSSepiaTonePeer
extends PPSOneSamplerPeer {
    public PPSSepiaTonePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final SepiaTone getEffect() {
        return (SepiaTone)super.getEffect();
    }

    private float getLevel() {
        return this.getEffect().getLevel();
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
        hashMap2.put("level", 0);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        shader.setConstant("level", this.getLevel());
    }
}

