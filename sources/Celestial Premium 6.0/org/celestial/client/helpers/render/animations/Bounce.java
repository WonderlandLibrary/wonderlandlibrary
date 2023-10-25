/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.animations;

import org.celestial.client.helpers.render.Animation;

public class Bounce
extends Animation {
    @Override
    public float easeIn(float t, float b, float c, float d) {
        return c - this.easeOut(d - t, 0.0f, c, d) + b;
    }

    @Override
    public float easeIn(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        float f;
        t /= d;
        if (f < 0.36363637f) {
            return c * (7.5625f * t * t) + b;
        }
        if (t < 0.72727275f) {
            return c * (7.5625f * (t -= 0.54545456f) * t + 0.75f) + b;
        }
        if ((double)t < 0.9090909090909091) {
            return c * (7.5625f * (t -= 0.8181818f) * t + 0.9375f) + b;
        }
        return c * (7.5625f * (t -= 0.95454544f) * t + 0.984375f) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        if (t < d / 2.0f) {
            return this.easeIn(t * 2.0f, 0.0f, c, d) * 0.5f + b;
        }
        return this.easeOut(t * 2.0f - d, 0.0f, c, d) * 0.5f + c * 0.5f + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }
}

