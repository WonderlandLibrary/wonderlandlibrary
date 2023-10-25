/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.LightFilter;
import com.jhlabs.image.TransferFilter;
import java.awt.image.BufferedImage;

public class ChromeFilter
extends LightFilter {
    private float amount = 0.5f;
    private float exposure = 1.0f;

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }

    public float getExposure() {
        return this.exposure;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.setColorSource(1);
        dst = super.filter(src, dst);
        TransferFilter tf = new TransferFilter(){

            @Override
            protected float transferFunction(float v) {
                v += ChromeFilter.this.amount * (float)Math.sin((double)(v * 2.0f) * Math.PI);
                return 1.0f - (float)Math.exp(-v * ChromeFilter.this.exposure);
            }
        };
        return tf.filter(dst, dst);
    }

    @Override
    public String toString() {
        return "Effects/Chrome...";
    }
}

