/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSOneSamplerPeer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import java.util.HashMap;

public class PPSPerspectiveTransformPeer
extends PPSOneSamplerPeer {
    public PPSPerspectiveTransformPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PerspectiveTransform getEffect() {
        return (PerspectiveTransform)super.getEffect();
    }

    private float[][] getITX() {
        PerspectiveTransformState perspectiveTransformState = (PerspectiveTransformState)AccessHelper.getState(this.getEffect());
        return perspectiveTransformState.getITX();
    }

    private float[] getTx0() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f = (float)rectangle.width / (float)rectangle2.width;
        float[] arrf = this.getITX()[0];
        return new float[]{arrf[0] * f, arrf[1] * f, arrf[2] * f};
    }

    private float[] getTx1() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f = (float)rectangle.height / (float)rectangle2.height;
        float[] arrf = this.getITX()[1];
        return new float[]{arrf[0] * f, arrf[1] * f, arrf[2] * f};
    }

    private float[] getTx2() {
        return this.getITX()[2];
    }

    @Override
    public int getTextureCoordinates(int n, float[] arrf, float f, float f2, float f3, float f4, Rectangle rectangle, BaseTransform baseTransform) {
        arrf[0] = rectangle.x;
        arrf[1] = rectangle.y;
        arrf[2] = rectangle.x + rectangle.width;
        arrf[3] = rectangle.y + rectangle.height;
        return 4;
    }

    @Override
    protected boolean isSamplerLinear(int n) {
        switch (n) {
            case 0: {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Shader createShader() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("baseImg", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("tx1", 1);
        hashMap2.put("tx0", 0);
        hashMap2.put("tx2", 2);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        float[] arrf = this.getTx1();
        shader.setConstant("tx1", arrf[0], arrf[1], arrf[2]);
        float[] arrf2 = this.getTx0();
        shader.setConstant("tx0", arrf2[0], arrf2[1], arrf2[2]);
        float[] arrf3 = this.getTx2();
        shader.setConstant("tx2", arrf3[0], arrf3[1], arrf3[2]);
    }
}

