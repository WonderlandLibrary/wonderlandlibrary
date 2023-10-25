/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.animations;

import org.celestial.client.helpers.render.Animation;

public class Cubic
extends Animation {
    @Override
    public float easeIn(float t, float b, float c, float d) {
        return c * (t /= d) * t * t + b;
    }

    @Override
    public float easeIn(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        t = t / d - 1.0f;
        return c * (t * t * t + 1.0f) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        float f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * t * t * t + b;
        }
        return c / 2.0f * ((t -= 2.0f) * t * t + 2.0f) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }
}

