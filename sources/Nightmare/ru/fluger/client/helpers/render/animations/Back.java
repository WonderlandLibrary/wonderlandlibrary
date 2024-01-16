// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.render.animations;

import ru.fluger.client.helpers.render.Animation;

public class Back extends Animation
{
    @Override
    public float easeIn(float t, final float b, final float c, final float d) {
        final float s = 1.70158f;
        return c * (t /= d) * t * ((s + 1.0f) * t - s) + b;
    }
    
    @Override
    public float easeIn(float t, final float b, final float c, final float d, final float s) {
        return c * (t /= d) * t * ((s + 1.0f) * t - s) + b;
    }
    
    @Override
    public float easeOut(float t, final float b, final float c, final float d) {
        final float s = 1.70158f;
        t = t / d - 1.0f;
        return c * (t * t * ((s + 1.0f) * t + s) + 1.0f) + b;
    }
    
    @Override
    public float easeOut(float t, final float b, final float c, final float d, final float s) {
        t = t / d - 1.0f;
        return c * (t * t * ((s + 1.0f) * t + s) + 1.0f) + b;
    }
    
    @Override
    public float easeInOut(float t, final float b, final float c, final float d) {
        final float f = 0.0f;
        float s = 1.70158f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * (t * t * (((s *= 1.525f) + 1.0f) * t - s)) + b;
        }
        return c / 2.0f * ((t -= 2.0f) * t * (((s *= 1.525f) + 1.0f) * t + s) + 2.0f) + b;
    }
    
    @Override
    public float easeInOut(float t, final float b, final float c, final float d, float s) {
        final float f = 0.0f;
        t /= d / 2.0f;
        if (f < 1.0f) {
            return c / 2.0f * (t * t * (((s *= 1.525f) + 1.0f) * t - s)) + b;
        }
        return c / 2.0f * ((t -= 2.0f) * t * (((s *= 1.525f) + 1.0f) * t + s) + 2.0f) + b;
    }
}
