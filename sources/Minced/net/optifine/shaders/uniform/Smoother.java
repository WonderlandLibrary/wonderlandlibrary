// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.uniform;

import java.util.HashMap;
import net.optifine.util.CounterInt;
import net.optifine.util.SmoothFloat;
import java.util.Map;

public class Smoother
{
    private static Map<Integer, SmoothFloat> mapSmoothValues;
    private static CounterInt counterIds;
    
    public static float getSmoothValue(final int id, final float value, final float timeFadeUpSec, final float timeFadeDownSec) {
        synchronized (Smoother.mapSmoothValues) {
            final Integer integer = id;
            SmoothFloat smoothfloat = Smoother.mapSmoothValues.get(integer);
            if (smoothfloat == null) {
                smoothfloat = new SmoothFloat(value, timeFadeUpSec, timeFadeDownSec);
                Smoother.mapSmoothValues.put(integer, smoothfloat);
            }
            final float f = smoothfloat.getSmoothValue(value, timeFadeUpSec, timeFadeDownSec);
            return f;
        }
    }
    
    public static int getNextId() {
        synchronized (Smoother.counterIds) {
            return Smoother.counterIds.nextValue();
        }
    }
    
    public static void resetValues() {
        synchronized (Smoother.mapSmoothValues) {
            Smoother.mapSmoothValues.clear();
        }
    }
    
    static {
        Smoother.mapSmoothValues = new HashMap<Integer, SmoothFloat>();
        Smoother.counterIds = new CounterInt(1);
    }
}
