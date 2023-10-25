/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.impl.prism.PrDrawable;

public class PPSDrawable
extends PrDrawable {
    private RTTexture rtt;

    private PPSDrawable(RTTexture rTTexture) {
        super(rTTexture);
        this.rtt = rTTexture;
    }

    static PPSDrawable create(RTTexture rTTexture) {
        return new PPSDrawable(rTTexture);
    }

    static int getCompatibleWidth(ResourceFactory resourceFactory, int n) {
        return resourceFactory.getRTTWidth(n, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static int getCompatibleHeight(ResourceFactory resourceFactory, int n) {
        return resourceFactory.getRTTHeight(n, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static PPSDrawable create(ResourceFactory resourceFactory, int n, int n2) {
        RTTexture rTTexture = resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO);
        return new PPSDrawable(rTTexture);
    }

    @Override
    public boolean isLost() {
        return this.rtt == null || this.rtt.isSurfaceLost();
    }

    @Override
    public void flush() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
        }
    }

    @Override
    public Object getData() {
        return this;
    }

    @Override
    public int getContentWidth() {
        return this.rtt.getContentWidth();
    }

    @Override
    public int getContentHeight() {
        return this.rtt.getContentHeight();
    }

    @Override
    public int getMaxContentWidth() {
        return this.rtt.getMaxContentWidth();
    }

    @Override
    public int getMaxContentHeight() {
        return this.rtt.getMaxContentHeight();
    }

    @Override
    public void setContentWidth(int n) {
        this.rtt.setContentWidth(n);
    }

    @Override
    public void setContentHeight(int n) {
        this.rtt.setContentHeight(n);
    }

    @Override
    public int getPhysicalWidth() {
        return this.rtt.getPhysicalWidth();
    }

    @Override
    public int getPhysicalHeight() {
        return this.rtt.getPhysicalHeight();
    }

    @Override
    public ShaderGraphics createGraphics() {
        return (ShaderGraphics)this.rtt.createGraphics();
    }
}

