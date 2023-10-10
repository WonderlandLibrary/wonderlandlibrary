// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix.perfstat;

import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfstatCpu
{
    private static final Perfstat PERF;
    
    private PerfstatCpu() {
    }
    
    public static Perfstat.perfstat_cpu_total_t queryCpuTotal() {
        final Perfstat.perfstat_cpu_total_t cpu = new Perfstat.perfstat_cpu_total_t();
        final int ret = PerfstatCpu.PERF.perfstat_cpu_total(null, cpu, cpu.size(), 1);
        if (ret > 0) {
            return cpu;
        }
        return new Perfstat.perfstat_cpu_total_t();
    }
    
    public static Perfstat.perfstat_cpu_t[] queryCpu() {
        final Perfstat.perfstat_cpu_t cpu = new Perfstat.perfstat_cpu_t();
        final int cputotal = PerfstatCpu.PERF.perfstat_cpu(null, null, cpu.size(), 0);
        if (cputotal > 0) {
            final Perfstat.perfstat_cpu_t[] statp = (Perfstat.perfstat_cpu_t[])cpu.toArray(cputotal);
            final Perfstat.perfstat_id_t firstcpu = new Perfstat.perfstat_id_t();
            final int ret = PerfstatCpu.PERF.perfstat_cpu(firstcpu, statp, cpu.size(), cputotal);
            if (ret > 0) {
                return statp;
            }
        }
        return new Perfstat.perfstat_cpu_t[0];
    }
    
    public static long queryCpuAffinityMask() {
        final int cpus = queryCpuTotal().ncpus;
        if (cpus < 63) {
            return (1L << cpus) - 1L;
        }
        return (cpus == 63) ? Long.MAX_VALUE : -1L;
    }
    
    static {
        PERF = Perfstat.INSTANCE;
    }
}
