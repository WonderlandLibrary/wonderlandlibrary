// 
// Decompiled by Procyon v0.6.0
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
    
    static {
        Smoother.mapSmoothValues = new HashMap<Integer, SmoothFloat>();
        Smoother.counterIds = new CounterInt(1);
    }
    
    public static float getSmoothValue(final int id, final float value, final float timeFadeUpSec, final float timeFadeDownSec) {
        synchronized (Smoother.mapSmoothValues) {
            final Integer integer = id;
            SmoothFloat smoothfloat = Smoother.mapSmoothValues.get(integer);
            if (smoothfloat == null) {
                smoothfloat = new SmoothFloat(value, timeFadeUpSec, timeFadeDownSec);
                Smoother.mapSmoothValues.put(integer, smoothfloat);
            }
            final float smoothValue;
            final float f = smoothValue = smoothfloat.getSmoothValue(value, timeFadeUpSec, timeFadeDownSec);
            monitorexit(Smoother.mapSmoothValues);
            return smoothValue;
        }
    }
    
    public static int getNextId() {
        synchronized (Smoother.counterIds) {
            final int nextValue = Smoother.counterIds.nextValue();
            monitorexit(Smoother.counterIds);
            return nextValue;
        }
    }
    
    public static void resetValues() {
        synchronized (Smoother.mapSmoothValues) {
            Smoother.mapSmoothValues.clear();
            monitorexit(Smoother.mapSmoothValues);
        }
    }
}
