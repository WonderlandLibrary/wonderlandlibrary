/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.animations;

import org.celestial.client.helpers.render.Animation;

public class Sine
extends Animation {
    @Override
    public float easeIn(float t, float b, float c, float d) {
        return -c * (float)Math.cos((double)(t / d) * 1.5707963267948966) + c + b;
    }

    @Override
    public float easeIn(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return c * (float)Math.sin((double)(t / d) * 1.5707963267948966) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        return -c / 2.0f * ((float)Math.cos(Math.PI * (double)t / (double)d) - 1.0f) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }
}

