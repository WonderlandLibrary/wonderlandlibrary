// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix.perfstat;

import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PerfstatProtocol
{
    private static final Perfstat PERF;
    
    private PerfstatProtocol() {
    }
    
    public static Perfstat.perfstat_protocol_t[] queryProtocols() {
        final Perfstat.perfstat_protocol_t protocol = new Perfstat.perfstat_protocol_t();
        final int total = PerfstatProtocol.PERF.perfstat_protocol(null, null, protocol.size(), 0);
        if (total > 0) {
            final Perfstat.perfstat_protocol_t[] statp = (Perfstat.perfstat_protocol_t[])protocol.toArray(total);
            final Perfstat.perfstat_id_t firstprotocol = new Perfstat.perfstat_id_t();
            final int ret = PerfstatProtocol.PERF.perfstat_protocol(firstprotocol, statp, protocol.size(), total);
            if (ret > 0) {
                return statp;
            }
        }
        return new Perfstat.perfstat_protocol_t[0];
    }
    
    static {
        PERF = Perfstat.INSTANCE;
    }
}
