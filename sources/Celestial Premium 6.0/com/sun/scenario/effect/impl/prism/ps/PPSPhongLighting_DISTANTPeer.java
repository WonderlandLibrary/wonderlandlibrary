/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSTwoSamplerPeer;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;
import java.util.HashMap;

public class PPSPhongLighting_DISTANTPeer
extends PPSTwoSamplerPeer {
    private FloatBuffer kvals;

    public PPSPhongLighting_DISTANTPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PhongLighting getEffect() {
        return (PhongLighting)super.getEffect();
    }

    private float getSurfaceScale() {
        return this.getEffect().getSurfaceScale();
    }

    private float getDiffuseConstant() {
        return this.getEffect().getDiffuseConstant();
    }

    private float getSpecularConstant() {
        return this.getEffect().getSpecularConstant();
    }

    private float getSpecularExponent() {
        return this.getEffect().getSpecularExponent();
    }

    private float[] getNormalizedLightPosition() {
        return this.getEffect().getLight().getNormalizedLightPosition();
    }

    private float[] getLightPosition() {
        PointLight pointLight = (PointLight)this.getEffect().getLight();
        return new float[]{pointLight.getX(), pointLight.getY(), pointLight.getZ()};
    }

    private float[] getLightColor() {
        return this.getEffect().getLight().getColor().getPremultipliedRGBComponents();
    }

    private float getLightSpecularExponent() {
        return ((SpotLight)this.getEffect().getLight()).getSpecularExponent();
    }

    private float[] getNormalizedLightDirection() {
        return ((SpotLight)this.getEffect().getLight()).getNormalizedLightDirection();
    }

    private FloatBuffer getKvals() {
        Rectangle rectangle = this.getInputNativeBounds(0);
        float f = 1.0f / (float)rectangle.width;
        float f2 = 1.0f / (float)rectangle.height;
        float[] arrf = new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        float[] arrf2 = new float[]{-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        if (this.kvals == null) {
            this.kvals = BufferUtil.newFloatBuffer(32);
        }
        this.kvals.clear();
        int n = 0;
        float f3 = -this.getSurfaceScale() * 0.25f;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i != 0 || j != 0) {
                    this.kvals.put((float)j * f);
                    this.kvals.put((float)i * f2);
                    this.kvals.put(arrf[n] * f3);
                    this.kvals.put(arrf2[n] * f3);
                }
                ++n;
            }
        }
        this.kvals.rewind();
        return this.kvals;
    }

    private int getKvalsArrayLength() {
        return 8;
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
        hashMap.put("bumpImg", 0);
        hashMap.put("origImg", 1);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("normalizedLightPosition", 12);
        hashMap2.put("specularExponent", 2);
        hashMap2.put("kvals", 4);
        hashMap2.put("diffuseConstant", 0);
        hashMap2.put("lightColor", 3);
        hashMap2.put("specularConstant", 1);
        return this.getRenderer().createShader(this.getShaderName(), hashMap, hashMap2, false);
    }

    @Override
    protected void updateShader(Shader shader) {
        float[] arrf = this.getNormalizedLightPosition();
        shader.setConstant("normalizedLightPosition", arrf[0], arrf[1], arrf[2]);
        shader.setConstant("specularExponent", this.getSpecularExponent());
        shader.setConstants("kvals", this.getKvals(), 0, this.getKvalsArrayLength());
        shader.setConstant("diffuseConstant", this.getDiffuseConstant());
        float[] arrf2 = this.getLightColor();
        shader.setConstant("lightColor", arrf2[0], arrf2[1], arrf2[2]);
        shader.setConstant("specularConstant", this.getSpecularConstant());
    }
}

