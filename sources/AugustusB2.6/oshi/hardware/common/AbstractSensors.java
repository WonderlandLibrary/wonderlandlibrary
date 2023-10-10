// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.common;

import java.util.Arrays;
import oshi.util.Memoizer;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.Sensors;

@ThreadSafe
public abstract class AbstractSensors implements Sensors
{
    private final Supplier<Double> cpuTemperature;
    private final Supplier<int[]> fanSpeeds;
    private final Supplier<Double> cpuVoltage;
    
    public AbstractSensors() {
        this.cpuTemperature = Memoizer.memoize(this::queryCpuTemperature, Memoizer.defaultExpiration());
        this.fanSpeeds = Memoizer.memoize(this::queryFanSpeeds, Memoizer.defaultExpiration());
        this.cpuVoltage = Memoizer.memoize(this::queryCpuVoltage, Memoizer.defaultExpiration());
    }
    
    @Override
    public double getCpuTemperature() {
        return this.cpuTemperature.get();
    }
    
    protected abstract double queryCpuTemperature();
    
    @Override
    public int[] getFanSpeeds() {
        return this.fanSpeeds.get();
    }
    
    protected abstract int[] queryFanSpeeds();
    
    @Override
    public double getCpuVoltage() {
        return this.cpuVoltage.get();
    }
    
    protected abstract double queryCpuVoltage();
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CPU Temperature=").append(this.getCpuTemperature()).append("°C, ");
        sb.append("Fan Speeds=").append(Arrays.toString(this.getFanSpeeds())).append(", ");
        sb.append("CPU Voltage=").append(this.getCpuVoltage());
        return sb.toString();
    }
}
