/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render.animations;

import org.celestial.client.helpers.render.Animation;

public class Expo
extends Animation {
    @Override
    public float easeIn(float t, float b, float c, float d) {
        return t == 0.0f ? b : c * (float)Math.pow(2.0, 10.0f * (t / d - 1.0f)) + b;
    }

    @Override
    public float easeIn(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return t == d ? b + c : c * (-((float)Math.pow(2.0, -10.0f * t / d)) + 1.0f) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        float f;
        if (t == 0.0f) {
            return b;
        }
        if (t == d) {
            return b + c;
        }
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * (float)Math.pow(2.0, 10.0f * (t - 1.0f)) + b;
        }
        return c / 2.0f * (-((float)Math.pow(2.0, -10.0f * (t -= 1.0f))) + 2.0f) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d, float s) {
        return 0.0f;
    }
}

