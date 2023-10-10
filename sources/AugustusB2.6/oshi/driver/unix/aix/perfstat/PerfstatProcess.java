// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix.perfstat;

import java.util.Arrays;
import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfstatProcess
{
    private static final Perfstat PERF;
    
    private PerfstatProcess() {
    }
    
    public static Perfstat.perfstat_process_t[] queryProcesses() {
        final Perfstat.perfstat_process_t process = new Perfstat.perfstat_process_t();
        final int procCount = PerfstatProcess.PERF.perfstat_process(null, null, process.size(), 0);
        if (procCount > 0) {
            final Perfstat.perfstat_process_t[] proct = (Perfstat.perfstat_process_t[])process.toArray(procCount);
            final Perfstat.perfstat_id_t firstprocess = new Perfstat.perfstat_id_t();
            final int ret = PerfstatProcess.PERF.perfstat_process(firstprocess, proct, process.size(), procCount);
            if (ret > 0) {
                return Arrays.copyOf(proct, ret);
            }
        }
        return new Perfstat.perfstat_process_t[0];
    }
    
    static {
        PERF = Perfstat.INSTANCE;
    }
}
