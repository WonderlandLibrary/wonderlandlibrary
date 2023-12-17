// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

public class SmoothFloat
{
    private float valueLast;
    private float timeFadeUpSec;
    private float timeFadeDownSec;
    private long timeLastMs;
    
    public SmoothFloat(final float valueLast, final float timeFadeSec) {
        this(valueLast, timeFadeSec, timeFadeSec);
    }
    
    public SmoothFloat(final float valueLast, final float timeFadeUpSec, final float timeFadeDownSec) {
        this.valueLast = valueLast;
        this.timeFadeUpSec = timeFadeUpSec;
        this.timeFadeDownSec = timeFadeDownSec;
        this.timeLastMs = System.currentTimeMillis();
    }
    
    public float getValueLast() {
        return this.valueLast;
    }
    
    public float getTimeFadeUpSec() {
        return this.timeFadeUpSec;
    }
    
    public float getTimeFadeDownSec() {
        return this.timeFadeDownSec;
    }
    
    public long getTimeLastMs() {
        return this.timeLastMs;
    }
    
    public float getSmoothValue(final float value, final float timeFadeUpSec, final float timeFadeDownSec) {
        this.timeFadeUpSec = timeFadeUpSec;
        this.timeFadeDownSec = timeFadeDownSec;
        return this.getSmoothValue(value);
    }
    
    public float getSmoothValue(final float value) {
        final long i = System.currentTimeMillis();
        final float f = this.valueLast;
        final long j = this.timeLastMs;
        final float f2 = (i - j) / 1000.0f;
        final float f3 = (value >= f) ? this.timeFadeUpSec : this.timeFadeDownSec;
        final float f4 = getSmoothValue(f, value, f2, f3);
        this.valueLast = f4;
        this.timeLastMs = i;
        return f4;
    }
    
    public static float getSmoothValue(final float valPrev, final float value, final float timeDeltaSec, final float timeFadeSec) {
        if (timeDeltaSec <= 0.0f) {
            return valPrev;
        }
        final float f = value - valPrev;
        float f8;
        if (timeFadeSec > 0.0f && timeDeltaSec < timeFadeSec && Math.abs(f) > 1.0E-6f) {
            final float f2 = timeFadeSec / timeDeltaSec;
            final float f3 = 4.61f;
            final float f4 = 0.13f;
            final float f5 = 10.0f;
            final float f6 = f3 - 1.0f / (f4 + f2 / f5);
            float f7 = timeDeltaSec / timeFadeSec * f6;
            f7 = NumUtils.limit(f7, 0.0f, 1.0f);
            f8 = valPrev + f * f7;
        }
        else {
            f8 = value;
        }
        return f8;
    }
}
