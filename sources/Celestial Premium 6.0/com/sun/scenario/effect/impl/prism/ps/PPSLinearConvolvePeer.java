/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSOneSamplerPeer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class PPSLinearConvolvePeer
extends PPSOneSamplerPeer<LinearConvolveRenderState> {
    public PPSLinearConvolvePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final Effect getEffect() {
        return super.getEffect();
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getTransformedBounds(null);
        rectangle2 = ((LinearConvolveRenderState)this.getRenderState()).getPassResultBounds(rectangle2, rectangle);
        return rectangle2;
    }

    private int getCount() {
        return (((LinearConvolveRenderState)this.getRenderState()).getPassKernelSize() + 3) / 4;
    }

    private float[] getOffset() {
        return ((LinearConvolveRenderState)this.getRenderState()).getPassVector();
    }

    private FloatBuffer getWeights() {
        return ((LinearConvolveRenderState)this.getRenderState()).getPassWeights();
    }

    private int getWeightsArrayLength() {
        return ((LinearConvolveRenderState)this.getRenderState()).getPassWeightsArrayLength();
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
        hashMap.put("img", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("offset", 1);
        hashMap2.put("count", 0);
        hashMap2.put("weights", 2);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        float[] arrf = this.getOffset();
        shader.setConstant("offset", arrf[0], arrf[1], arrf[2], arrf[3]);
        shader.setConstant("count", this.getCount());
        shader.setConstants("weights", this.getWeights(), 0, this.getWeightsArrayLength());
    }
}

