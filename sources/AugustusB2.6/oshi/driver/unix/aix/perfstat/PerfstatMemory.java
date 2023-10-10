// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix.perfstat;

import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfstatMemory
{
    private static final Perfstat PERF;
    
    private PerfstatMemory() {
    }
    
    public static Perfstat.perfstat_memory_total_t queryMemoryTotal() {
        final Perfstat.perfstat_memory_total_t memory = new Perfstat.perfstat_memory_total_t();
        final int ret = PerfstatMemory.PERF.perfstat_memory_total(null, memory, memory.size(), 1);
        if (ret > 0) {
            return memory;
        }
        return new Perfstat.perfstat_memory_total_t();
    }
    
    static {
        PERF = Perfstat.INSTANCE;
    }
}
