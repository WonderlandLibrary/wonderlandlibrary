// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.math;

import ru.fluger.client.helpers.Helper;

public class GCDCalcHelper implements Helper
{
    public static float getFixedRotation(final float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }
    
    public static float getGCDValue() {
        return (float)(getGCD() * 0.15);
    }
    
    public static float getGCD() {
        final float f1 = (float)(GCDCalcHelper.mc.t.c * 0.6 + 0.2);
        return f1 * f1 * f1 * 8.0f;
    }
    
    public static float getDeltaMouse(final float delta) {
        return (float)Math.round(delta / getGCDValue());
    }
}
