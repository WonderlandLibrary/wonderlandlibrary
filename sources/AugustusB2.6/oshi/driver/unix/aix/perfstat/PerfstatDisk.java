// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix.perfstat;

import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfstatDisk
{
    private static final Perfstat PERF;
    
    private PerfstatDisk() {
    }
    
    public static Perfstat.perfstat_disk_t[] queryDiskStats() {
        final Perfstat.perfstat_disk_t diskStats = new Perfstat.perfstat_disk_t();
        final int total = PerfstatDisk.PERF.perfstat_disk(null, null, diskStats.size(), 0);
        if (total > 0) {
            final Perfstat.perfstat_disk_t[] statp = (Perfstat.perfstat_disk_t[])diskStats.toArray(total);
            final Perfstat.perfstat_id_t firstdiskStats = new Perfstat.perfstat_id_t();
            final int ret = PerfstatDisk.PERF.perfstat_disk(firstdiskStats, statp, diskStats.size(), total);
            if (ret > 0) {
                return statp;
            }
        }
        return new Perfstat.perfstat_disk_t[0];
    }
    
    static {
        PERF = Perfstat.INSTANCE;
    }
}
