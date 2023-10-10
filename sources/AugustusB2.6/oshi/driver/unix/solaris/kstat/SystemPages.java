// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.solaris.kstat;

import com.sun.jna.platform.unix.solaris.LibKstat;
import oshi.util.platform.unix.solaris.KstatUtil;
import oshi.software.os.unix.solaris.SolarisOperatingSystem;
import oshi.util.tuples.Pair;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class SystemPages
{
    private SystemPages() {
    }
    
    public static Pair<Long, Long> queryAvailableTotal() {
        if (SolarisOperatingSystem.IS_11_4_OR_HIGHER) {
            return queryAvailableTotal2();
        }
        long memAvailable = 0L;
        long memTotal = 0L;
        final KstatUtil.KstatChain kc = KstatUtil.openChain();
        try {
            final LibKstat.Kstat ksp = KstatUtil.KstatChain.lookup(null, -1, "system_pages");
            if (ksp != null && KstatUtil.KstatChain.read(ksp)) {
                memAvailable = KstatUtil.dataLookupLong(ksp, "availrmem");
                memTotal = KstatUtil.dataLookupLong(ksp, "physmem");
            }
            if (kc != null) {
                kc.close();
            }
        }
        catch (Throwable t) {
            if (kc != null) {
                try {
                    kc.close();
                }
                catch (Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
            throw t;
        }
        return new Pair<Long, Long>(memAvailable, memTotal);
    }
    
    private static Pair<Long, Long> queryAvailableTotal2() {
        final Object[] results = KstatUtil.queryKstat2("kstat:/pages/unix/system_pages", "availrmem", "physmem");
        final long avail = (long)((results[0] == null) ? 0L : results[0]);
        final long total = (long)((results[1] == null) ? 0L : results[1]);
        return new Pair<Long, Long>(avail, total);
    }
}
