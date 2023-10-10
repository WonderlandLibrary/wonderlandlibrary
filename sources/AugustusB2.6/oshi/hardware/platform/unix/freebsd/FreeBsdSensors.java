// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix.freebsd;

import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.platform.unix.LibCAPI;
import oshi.jna.platform.unix.FreeBsdLibc;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractSensors;

@ThreadSafe
final class FreeBsdSensors extends AbstractSensors
{
    public double queryCpuTemperature() {
        return queryKldloadCoretemp();
    }
    
    private static double queryKldloadCoretemp() {
        final String name = "dev.cpu.%d.temperature";
        final LibCAPI.size_t.ByReference size = new LibCAPI.size_t.ByReference(new LibCAPI.size_t((long)FreeBsdLibc.INT_SIZE));
        final Pointer p = new Memory(size.longValue());
        int cpu = 0;
        double sumTemp = 0.0;
        while (0 == FreeBsdLibc.INSTANCE.sysctlbyname(String.format(name, cpu), p, size, null, LibCAPI.size_t.ZERO)) {
            sumTemp += p.getInt(0L) / 10.0 - 273.15;
            ++cpu;
        }
        return (cpu > 0) ? (sumTemp / cpu) : Double.NaN;
    }
    
    public int[] queryFanSpeeds() {
        return new int[0];
    }
    
    public double queryCpuVoltage() {
        return 0.0;
    }
}
