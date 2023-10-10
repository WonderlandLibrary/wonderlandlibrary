// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.aix;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class AixSensors extends AbstractSensors
{
    private final Supplier<List<String>> lscfg;
    
    AixSensors(final Supplier<List<String>> lscfg) {
        this.lscfg = lscfg;
    }
    
    public double queryCpuTemperature() {
        return 0.0;
    }
    
    public int[] queryFanSpeeds() {
        int fans = 0;
        for (final String s : this.lscfg.get()) {
            if (s.contains("Air Mover")) {
                ++fans;
            }
        }
        return new int[fans];
    }
    
    public double queryCpuVoltage() {
        return 0.0;
    }
}
